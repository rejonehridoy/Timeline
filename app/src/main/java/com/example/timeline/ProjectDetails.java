package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observer;

import javax.annotation.Nullable;

public class ProjectDetails extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText etProjectName, etCategory, etStatus, etDescription, etFramework,etPlatform,etRequirement,etPrerequisite,
             etModifiedDate, etModifiedUser, etCreatedDate, etCreatedUser, etStartDate,
            etEndDate, etTotalTasks, etTotalTaskPoints, etCompletedTasks, etCompletedTaskPoints,etCompleteTaskRatio;
    private TextInputLayout status_layout,project_name,category,description;
    private LinearLayout layout_tasks, layout_info, layout_settings,layout_settings_memberFunctionality;
    private Toolbar tasks_toolbar, info_toolbar, setting_toolbar;
    private AutoCompleteTextView ACTV_member;
    private CoordinatorLayout layout_cordinator;
    private FloatingActionButton btnAdd;
    private RecyclerView task_recylerView,member_recylerView;
    private TaskInfoAdapter mAdapter;
    private ArrayList<MemberInfo> members_list = new ArrayList<>();
    private ArrayList<MemberInfo> members = new ArrayList<>();
    private MemberInfoAdapter member_adapter;
    private static final String MEMBERS_PROJECT_TABLE = "MembersProject";
    private static final String PROJECT_TABLE = "Projects";
    private static final String TASK_TABLE = "Tasks";
    ArrayList<MemberInfo> all_member_list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private Button info_btnEdit,info_btnUpdate,setting_btnAddNew,setting_btnUpdateMember,setting_btnAddMember,setting_btnDeleteMember,setting_btnDeleteProject,setting_btnCancel;
    private String str_Pid,str_projectName,str_category,str_description,str_status,str_framework,str_platform,str_requirement,str_prerequisite,str_modifiedDate,str_modifiedUser,str_createdDate,
        str_createdUser,str_startDate,str_endDate,str_totalTasks,str_totalTaskPoints,str_completedTasks,str_completedTaskPoints,str_modifiedVersion;
    private SearchableSpinner status_spinner,memberRole_spinner;
    private String userName,userFullName,userType,userRole,uid,userstatus;
    private int totalTasks=0,totalTaskpoints=0,completedTasks=0,completedTaskPoints=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);


        find_view_by_id();
        get_user_info();
        toolbar_settings();
        get_all_info_data_from_intent();        //info section

        //settings section
        get_memberList_and_show();

        // task section
        get_all_task_lists();

        // info section
        set_all_info_data_into_editBoxes();
        //set_edit_privacy();         // this will create privacy who can edit the info




        btnAdd.setOnClickListener(this);
        info_btnEdit.setOnClickListener(this);
        info_btnUpdate.setOnClickListener(this);
        setting_btnUpdateMember.setOnClickListener(this);
        setting_btnAddMember.setOnClickListener(this);
        setting_btnAddNew.setOnClickListener(this);
        setting_btnCancel.setOnClickListener(this);
        setting_btnDeleteMember.setOnClickListener(this);
        setting_btnDeleteProject.setOnClickListener(this);
        BottomNavigationView bottomNav = findViewById(R.id.history_bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_task_lists();
        get_project_info_from_firebase();       // it will refresh the project data,and get updated data
    }

    private void toolbar_settings(){
        // this is for info toolbar
        info_toolbar = findViewById(R.id.PD_info_toolbar);
        info_toolbar.setTitle("Details");
        setSupportActionBar(info_toolbar);
        // this is for settings toolbar
        setting_toolbar = findViewById(R.id.PD_setting_toolbar);
        setting_toolbar.setTitle("Settings");
        setSupportActionBar(setting_toolbar);
        // this is for tasks toolbar
        tasks_toolbar = findViewById(R.id.PD_tasks_toolbar);
        tasks_toolbar.setTitle("Tasks");
        setSupportActionBar(tasks_toolbar);
    }

    private void find_view_by_id() {
        // all tasks components
        layout_tasks = findViewById(R.id.PD_layout_tasks);
        layout_info = findViewById(R.id.PD_layout_Info);
        layout_settings = findViewById(R.id.PD_layout_Setting);
        layout_cordinator = findViewById(R.id.PD_cordinator_layout);
        task_recylerView = findViewById(R.id.PD_tasks_recylerView);
        btnAdd = findViewById(R.id.PD_btnAdd);

        // all info components
        etProjectName = findViewById(R.id.PD_info_name_input);
        etCategory = findViewById(R.id.PD_info_category_input);
        etDescription = findViewById(R.id.PD_info_description_input);
        etStatus = findViewById(R.id.PD_info_status_input);
        etFramework = findViewById(R.id.PD_info_framework_input);
        etPlatform = findViewById(R.id.PD_info_platform_input);
        etRequirement = findViewById(R.id.PD_info_requirement_input);
        etPrerequisite = findViewById(R.id.PD_info_prerequisite_input);
        etModifiedDate = findViewById(R.id.PD_info_modifiedDate_input);
        etModifiedUser = findViewById(R.id.PD_info_modifiedUser_input);
        etCreatedDate = findViewById(R.id.PD_info_createdDate_input);
        etCreatedUser = findViewById(R.id.PD_info_createdUser_input);
        etStartDate = findViewById(R.id.PD_info_startingDate_input);
        etEndDate = findViewById(R.id.PD_info_endDate_input);
        etTotalTasks = findViewById(R.id.PD_info_totalTasks_input);
        etTotalTaskPoints = findViewById(R.id.PD_info_totalTaskPoints_input);
        etCompletedTasks = findViewById(R.id.PD_info_completedTasks_input);
        etCompletedTaskPoints = findViewById(R.id.PD_info_completedTaskPoints_input);
        etCompleteTaskRatio = findViewById(R.id.PD_info_completedTaskRatio_input);
        status_layout = findViewById(R.id.PD_info_status);
        info_btnEdit = findViewById(R.id.PD_info_btnEdit);
        info_btnUpdate = findViewById(R.id.PD_info_btnUpdate);
        status_spinner = findViewById(R.id.PD_info_status_spinner);
        project_name = findViewById(R.id.PD_info_name);
        category = findViewById(R.id.PD_info_category);
        description = findViewById(R.id.PD_info_description);

        //seting section
        member_recylerView = findViewById(R.id.PD_settings_members_recylerView);
        setting_btnAddNew = findViewById(R.id.PD_settings_btnAddNew);
        setting_btnAddMember = findViewById(R.id.PD_setting_btnAddMember);
        setting_btnUpdateMember = findViewById(R.id.PD_setting_btnUpdateMember);
        layout_settings_memberFunctionality = findViewById(R.id.PD_setting_AddUpdatemember_layout);
        ACTV_member = findViewById(R.id.PD_settings_memberName_ACTV);
        memberRole_spinner = findViewById(R.id.PD_setting_role_spinner);
        setting_btnDeleteMember = findViewById(R.id.PD_setting_btnDeleteMember);
        setting_btnDeleteProject = findViewById(R.id.PD_setting_btnDeleteProject);
        setting_btnCancel = findViewById(R.id.PD_setting_btnCancel);


    }

    private void get_all_task_lists() {
        final ArrayList<TaskInfo> task_infos = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(TASK_TABLE).whereEqualTo("pid",str_Pid)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        task_infos.clear();
                        for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                            task_infos.add(snapshot.toObject(TaskInfo.class));
                        }

                        //count all details
                        totalTaskpoints=0;completedTaskPoints=0;completedTasks=0;
                        for (TaskInfo task:task_infos){
                            totalTaskpoints+=Integer.parseInt(task.getPoints());
                            if (task.getStatus().toLowerCase().equals("done")){
                                completedTasks++;
                                completedTaskPoints+=Integer.parseInt(task.getPoints());
                            }
                        }
                        totalTasks = task_infos.size();
                        analyse_and_check_update_project_info();    //this method will mainly count totaltasks,totalpoints,completedTaskpoints and check with the previous value
                        //Toasty.info(getApplicationContext(),"Total Tasks : "+totalTasks + " Total Task points: "+totalTaskpoints+ "\nCompleted task : "+completedTasks+" Completed points: "+completedTaskPoints,Toasty.LENGTH_LONG).show();
                        task_recylerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(ProjectDetails.this);
                        mAdapter = new TaskInfoAdapter(task_infos);
                        task_recylerView.setLayoutManager(mLayoutManager);
                        task_recylerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new TaskInfoAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(ProjectDetails.this, TaskDetails.class);
                                intent.putExtra("Tid", task_infos.get(position).getTid());
                                intent.putExtra("Pid", task_infos.get(position).getPid());
                                intent.putExtra("name", task_infos.get(position).getName());
                                intent.putExtra("serial", task_infos.get(position).getSerial());
                                intent.putExtra("category", task_infos.get(position).getCategory());
                                intent.putExtra("description", task_infos.get(position).getDescription());
                                intent.putExtra("solution", task_infos.get(position).getSolution());
                                intent.putExtra("deadline", task_infos.get(position).getDeadline());
                                intent.putExtra("assigner", task_infos.get(position).getAssigner());
                                intent.putExtra("points", task_infos.get(position).getPoints());
                                intent.putExtra("issues", task_infos.get(position).getIssues());
                                intent.putExtra("link", task_infos.get(position).getLink());
                                intent.putExtra("priority", task_infos.get(position).getPriority());
                                intent.putExtra("status", task_infos.get(position).getStatus());
                                intent.putExtra("modifiedDate", task_infos.get(position).getModifiedDate());
                                intent.putExtra("modifiedUser", task_infos.get(position).getModifiedUser());
                                intent.putExtra("createdDate", task_infos.get(position).getCreatedDate());
                                intent.putExtra("createdUser", task_infos.get(position).getCreatedUser());
                                intent.putExtra("comments", task_infos.get(position).getComments());
                                intent.putExtra("userRole",userRole);
                                intent.putExtra("modifiedVersion",str_modifiedVersion);
                                String [] member_list = new String[members.size()];
                                int pos=0;
                                for (MemberInfo member:members){
                                    member_list[pos++] = member.getUserName();
                                }
                                intent.putExtra("member_list", member_list);

                                startActivity(intent);
                                //mAdapter.notifyItemChanged(position);
                            }
                        });

                    }
                });
    }

    private void get_project_info_from_firebase(){
        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid)
                .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        if (documentSnapshot.exists()){
                            str_projectName = documentSnapshot.getString("name");
                            str_Pid = documentSnapshot.getString("id");
                            str_category = documentSnapshot.getString("category");
                            str_description = documentSnapshot.getString("description");
                            str_framework = documentSnapshot.getString("framework");
                            str_platform = documentSnapshot.getString("platform");
                            str_requirement = documentSnapshot.getString("requirement");
                            str_prerequisite = documentSnapshot.getString("prerequisite");
                            str_status = documentSnapshot.getString("status");
                            str_startDate = documentSnapshot.getString("startingDate");
                            str_endDate = documentSnapshot.getString("endDate");
                            str_modifiedUser = documentSnapshot.getString("modifiedUser");
                            str_modifiedDate = documentSnapshot.getString("modifiedDate");
                            str_createdDate = documentSnapshot.getString("createdDate");
                            str_createdUser = documentSnapshot.getString("createdUser");
                            str_totalTasks = documentSnapshot.getString("totalTasks");
                            str_totalTaskPoints = documentSnapshot.getString("totalTaskPoints");
                            str_completedTasks = documentSnapshot.getString("completedTask");
                            str_completedTaskPoints = documentSnapshot.getString("completedTaskPoints");
                            str_modifiedVersion = documentSnapshot.getString("modifiedVersion");
                            set_all_info_data_into_editBoxes();
                        }
                    }
                });
    }
    private void analyse_and_check_update_project_info(){
        if (!str_completedTasks.equals(String.valueOf(completedTasks))){
            FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid).update("completedTask",String.valueOf(completedTasks));
            FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid).child("completedTask").setValue(String.valueOf(completedTasks));
        }
        if (!str_totalTasks.equals(String.valueOf(totalTasks))){
            FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid).update("totalTasks",String.valueOf(totalTasks));
            FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid).child("totalTasks").setValue(String.valueOf(totalTasks));
        }
        if (!str_completedTaskPoints.equals(String.valueOf(completedTaskPoints))){
            FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid).update("completedTaskPoints",String.valueOf(completedTaskPoints));
            FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid).child("completedTaskPoints").setValue(String.valueOf(completedTaskPoints));
        }
        if (!str_totalTaskPoints.equals(String.valueOf(totalTaskpoints))){
            FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid).update("totalTaskPoints",String.valueOf(totalTaskpoints));
            FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid).child("totalTaskPoints").setValue(String.valueOf(totalTaskpoints));
        }

    }


    private void get_all_info_data_from_intent(){
        Intent intent = getIntent();
        str_projectName = intent.getStringExtra("name");
        str_Pid = intent.getStringExtra("Pid");
        str_category = intent.getStringExtra("category");
        str_description = intent.getStringExtra("descripton");
        str_framework = intent.getStringExtra("framework");
        str_platform = intent.getStringExtra("platform");
        str_requirement = intent.getStringExtra("requirement");
        str_prerequisite = intent.getStringExtra("prerequisite");
        str_status = intent.getStringExtra("status");
        str_startDate = intent.getStringExtra("startingDate");
        str_endDate = intent.getStringExtra("endDate");
        str_modifiedUser = intent.getStringExtra("modifiedUser");
        str_modifiedDate = intent.getStringExtra("modifiedDate");
        str_createdDate = intent.getStringExtra("createdDate");
        str_createdUser = intent.getStringExtra("createdUser");
        str_totalTasks = intent.getStringExtra("totalTasks");
        str_totalTaskPoints = intent.getStringExtra("totalTaskPoints");
        str_completedTasks = intent.getStringExtra("completedTasks");
        str_completedTaskPoints = intent.getStringExtra("completedTaskPoints");
        str_modifiedVersion = intent.getStringExtra("modifiedVersion");
    }

    private void set_all_info_data_into_editBoxes(){
        if (str_projectName != null && !str_projectName.isEmpty()){
            etProjectName.setText(str_projectName);
        }
        if (str_category != null && !str_category.isEmpty()){
            etCategory.setText(str_category);
        }
        if (str_description != null && !str_description.isEmpty()){
            etDescription.setText(str_description);
        }
        if (str_framework != null && !str_framework.isEmpty()){
            etFramework.setText(str_framework);
        }
        if (str_platform != null && !str_platform.isEmpty()){
            etPlatform.setText(str_platform);
        }
        if (str_requirement != null && !str_requirement.isEmpty()){
            etRequirement.setText(str_requirement);
        }
        if (str_prerequisite != null && !str_prerequisite.isEmpty()){
            etPrerequisite.setText(str_prerequisite);
        }
        if (str_startDate != null && !str_startDate.isEmpty()){
            if (str_startDate.equals("N/A")){
                etStartDate.setText(str_startDate);
            }else{
                etStartDate.setText(convert_Date_format(str_startDate));
            }
        }else{
            etStartDate.setText("N/A");
        }
        if (str_status != null && !str_status.isEmpty()){
            etStatus.setText(str_status);
        }
        if (str_endDate != null && !str_endDate.isEmpty()){
            if (str_endDate.equals("N/A")){
                etEndDate.setText(str_endDate);
            }else{
                etEndDate.setText(convert_Date_format(str_endDate));
            }
        }else{
            etEndDate.setText("N/A");
        }
        if (str_modifiedUser != null && !str_modifiedUser.isEmpty()){
            etModifiedUser.setText(str_modifiedUser);
        }else{
            etModifiedUser.setText("N/A");
        }

        if (str_modifiedDate != null && !str_modifiedDate.isEmpty()){
            if (str_modifiedDate.equals("N/A")){
                etModifiedDate.setText(str_modifiedDate);
            }else{
                etModifiedDate.setText(convert_Date_format(str_modifiedDate));
            }
        }else{
            etModifiedDate.setText("N/A");
        }

        if (str_createdDate != null && !str_createdDate.isEmpty()){
            if (str_createdDate.equals("N/A")){
                etCreatedDate.setText(str_createdDate);
            }else{
                etCreatedDate.setText(convert_Date_format(str_createdDate));
            }
        }
        if (str_createdUser != null && !str_createdUser.isEmpty()){
            etCreatedUser.setText(str_createdUser);
        }
        if (str_totalTasks != null && !str_totalTasks.isEmpty()){
            etTotalTasks.setText(str_totalTasks);
        }
        if (str_totalTaskPoints != null && !str_totalTaskPoints.isEmpty()){
            etTotalTaskPoints.setText(str_totalTaskPoints);
        }
        if (str_completedTasks != null && !str_completedTasks.isEmpty()){
            etCompletedTasks.setText(str_completedTasks);
        }
        if (str_completedTaskPoints != null && !str_completedTaskPoints.isEmpty()){
            etCompletedTaskPoints.setText(str_completedTaskPoints);
        }
        if (str_totalTaskPoints != null && !str_totalTaskPoints.isEmpty() && str_completedTasks != null && !str_completedTasks.isEmpty()){
            try {
                double totalPoints = Double.parseDouble(str_totalTaskPoints);
                double completedPoints = Double.parseDouble(str_completedTaskPoints);
                if (totalPoints > 0){
                    double ratio = (completedPoints/totalPoints)*100;
                    NumberFormat formatter = new DecimalFormat("#0.0");
                    etCompleteTaskRatio.setText(String.valueOf(formatter.format(ratio)+"%"));
                }else{
                    etCompleteTaskRatio.setText("N/A");
                }
            }catch (Exception e){
                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        }

        // why its here???
        // role spinner
        String[] role_array = getResources().getStringArray(R.array.project_member_role_string);

        ArrayAdapter<String> role_adapter = new ArrayAdapter<>(ProjectDetails.this,android.R.layout.simple_spinner_item,role_array);
        role_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberRole_spinner.setAdapter(role_adapter);
        memberRole_spinner.setVisibility(View.VISIBLE);
        memberRole_spinner.setEnabled(true);

    }

    private void set_edit_privacy(){
        if (str_createdUser != null && !str_createdUser.isEmpty() &&
                (str_createdUser.equals(userName) || userRole.equals("Leader") || userRole.equals("Co-Leader") || userRole.equals("Manager"))) {

            info_btnEdit.setVisibility(View.VISIBLE);
            setting_btnAddNew.setVisibility(View.VISIBLE);
            setting_btnDeleteProject.setVisibility(View.VISIBLE);
        }else{
            info_btnEdit.setVisibility(View.GONE);
            setting_btnAddNew.setVisibility(View.GONE);
            setting_btnDeleteProject.setVisibility(View.GONE);
        }

    }

    private void get_memberList_and_show(){

        //here data(members of this project) will be fetch from firestore and store into the arraylist
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).whereEqualTo("pid",str_Pid)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e!= null){
                            return;
                        }
                        members.clear();
                        for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                            String fullname = snapshot.getString("memberFullName");
                            String mid = snapshot.getString("mid");
                            String role = snapshot.getString("role");
                            String name = snapshot.getString("memberUserName");
                            members.add(new MemberInfo(mid,fullname,name,role));
                            if (name.equals(userName)){
                                userRole = role;
                            }
                        }
                        set_edit_privacy();     // this will create privacy who can edit the info
//                        members_list.clear();
//                        members_list = members;
                        //info_recylerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(ProjectDetails.this);
                        member_adapter = new MemberInfoAdapter(members);
                        member_recylerView.setLayoutManager(mLayoutManager);
                        member_recylerView.setAdapter(member_adapter);

                        //listener of the member recyler view
                        member_adapter.setOnItemClickListener(new MemberInfoAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                // if the user is the creator of this project or leader or co-leader or manager,then only he/she can edit/update
                                if (str_createdUser != null && !str_createdUser.isEmpty() &&
                                        (str_createdUser.equals(userName) || userRole.equals("Leader") || userRole.equals("Co-Leader") || userRole.equals("Manager"))) {
                                    String name = members.get(position).getUserName();
                                    String role = members.get(position).getRole();
                                    //set visible autocomplete textView
                                    ACTV_member.setText(name);

                                    //set visible layout and components
                                    layout_settings_memberFunctionality.setVisibility(View.VISIBLE);
                                    ACTV_member.setVisibility(View.VISIBLE);
                                    ACTV_member.setEnabled(false);
                                    setting_btnUpdateMember.setVisibility(View.VISIBLE);
                                    memberRole_spinner.setVisibility(View.VISIBLE);
                                    setting_btnAddMember.setVisibility(View.GONE);
                                    setting_btnCancel.setVisibility(View.VISIBLE);
                                    setting_btnDeleteMember.setVisibility(View.VISIBLE);

                                    // role spinner
                                    String[] role_array = getResources().getStringArray(R.array.project_member_role_string);

                                    ArrayAdapter<String> role_adapter = new ArrayAdapter<>(ProjectDetails.this,android.R.layout.simple_spinner_item,role_array);
                                    role_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    memberRole_spinner.setAdapter(role_adapter);
                                    //set selected item of status spinner

                                    int pos = role_adapter.getPosition(role);
                                    memberRole_spinner.setSelection(pos);

                                }
                                //mAdapter.notifyItemChanged(position);
                            }
                        });
                    }
                });



        // get all users from firestore and setup in the auto complete textview
        // suppose in the string array the all has stored

        FirebaseFirestore.getInstance().collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    all_member_list.clear();
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        String id = snapshot.getString("uid");
                        String fullname = snapshot.getString("fullName");
                        String username = snapshot.getString("userName");
                        all_member_list.add(new MemberInfo(id,fullname,username,""));
                    }
                }
            }
        });
        //setup auto complete textView suggestion
        ArrayAdapter<MemberInfo> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,all_member_list);
        ACTV_member.setAdapter(adapter);


    }

    private void set_enable_editBoxes(){

        etProjectName.setEnabled(true);
        etCategory.setEnabled(true);
        etDescription.setEnabled(true);
        etFramework.setEnabled(true);
        etPlatform.setEnabled(true);
        etRequirement.setEnabled(true);
        etPrerequisite.setEnabled(true);
        status_layout.setVisibility(View.GONE);
        status_spinner.setVisibility(View.VISIBLE);
        if (etFramework.getText() !=null && etFramework.getText().toString().equals("N/A")){
            etFramework.setText("");
        }
        if (etPlatform.getText() !=null && etPlatform.getText().toString().equals("N/A")){
            etPlatform.setText("");
        }
        if (etRequirement.getText() !=null && etRequirement.getText().toString().equals("N/A")){
            etRequirement.setText("");
        }
        if (etPrerequisite.getText() !=null && etPrerequisite.getText().toString().equals("N/A")){
            etPrerequisite.setText("");
        }

        // status spinner
        String[] status = getResources().getStringArray(R.array.project_status_stirng);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(status_adapter);
        //set selected item of status spinner
        if (str_status != null && !str_status.isEmpty()){
            int position = status_adapter.getPosition(str_status);
            status_spinner.setSelection(position);
        }
    }
    private void disable_editBoxes(){
        etProjectName.setEnabled(false);
        etCategory.setEnabled(false);
        etDescription.setEnabled(false);
        etFramework.setEnabled(false);
        etPlatform.setEnabled(false);
        etRequirement.setEnabled(false);
        etPrerequisite.setEnabled(false);
        status_layout.setVisibility(View.VISIBLE);
        status_spinner.setVisibility(View.GONE);
        get_project_info_from_firebase();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.PD_nav_info:
                            layout_tasks.setVisibility(View.GONE);
                            layout_info.setVisibility(View.VISIBLE);
                            layout_settings.setVisibility(View.GONE);
                            layout_cordinator.setVisibility(View.GONE);
                            break;
                        case R.id.PD_nav_settings:
                            layout_tasks.setVisibility(View.GONE);
                            layout_info.setVisibility(View.GONE);
                            layout_settings.setVisibility(View.VISIBLE);
                            layout_cordinator.setVisibility(View.GONE);
                            break;

                        case R.id.PD_nav_tasks:
                            layout_tasks.setVisibility(View.VISIBLE);
                            layout_info.setVisibility(View.GONE);
                            layout_settings.setVisibility(View.GONE);
                            layout_cordinator.setVisibility(View.VISIBLE);

                            break;

                    }

                    return true;
                }
            };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PD_btnAdd:
                Intent taskCreate = new Intent(getApplicationContext(),TaskCreate.class);
                taskCreate.putExtra("pid",str_Pid);
                startActivity(taskCreate);

                break;
            case R.id.PD_info_btnEdit:
                // in info section , edit button
                set_enable_editBoxes();
                info_btnUpdate.setVisibility(View.VISIBLE);
                info_btnEdit.setVisibility(View.GONE);
                Toasty.info(getApplicationContext(),"You are in editable mode",Toasty.LENGTH_SHORT).show();
                break;
            case R.id.PD_info_btnUpdate:


                update_project_info();
                disable_editBoxes();
                info_btnUpdate.setVisibility(View.GONE);
                info_btnEdit.setVisibility(View.VISIBLE);
                Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
                break;

            case R.id.PD_setting_btnAddMember:
                //add member button
                if (ACTV_member != null && ACTV_member.getText() != null && !ACTV_member.getText().toString().isEmpty() && memberRole_spinner !=null && memberRole_spinner.getSelectedItem() != null){
                    String name = ACTV_member.getText().toString();
                    String role = memberRole_spinner.getSelectedItem().toString();
                    for (MemberInfo m: members){
                        if (name.equals(m.getUserName())){
                            Toasty.error(getApplicationContext(),"Already added in the list",Toasty.LENGTH_SHORT).show();
                            return;

                        }
                    }
                    for (MemberInfo m:all_member_list){
                        if (name.equals(m.getUserName())){
                            // ready to create new member
                            String key = add_new_member_into_project(m.getId(),m.getFullName(),m.getUserName(),role);
                            members.add(new MemberInfo(key,m.getFullName(),m.getUserName(),role));
                            //member_adapter.notifyDataSetChanged();        do not entry this
                            Toasty.success(getApplicationContext(),"Added",Toasty.LENGTH_SHORT).show();
                            ACTV_member.setText("");
                            layout_settings_memberFunctionality.setVisibility(View.GONE);
                            return;
                        }
                    }
                    Toasty.error(getApplicationContext(),"username is not valid",Toasty.LENGTH_SHORT).show();

                }else{
                    Toasty.error(getApplicationContext(),"check input constrains",Toasty.LENGTH_SHORT).show();
                }



                break;
            case R.id.PD_setting_btnUpdateMember:
                // update member button
                String name = ACTV_member.getText().toString();
                String role = memberRole_spinner.getSelectedItem().toString();
                //now update in the firebase and member_list arraylist also
                for (MemberInfo m:members){
                    if (name.equals(m.getUserName()) && !name.equals(userName)){
                        m.setRole(role);
                        //member_adapter.notifyDataSetChanged();
                        //update role in the firebase
                        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).document(m.getId()).update("role",role);
                        FirebaseDatabase.getInstance().getReference().child(MEMBERS_PROJECT_TABLE).child(m.getId()).child("role").setValue(role);
                        get_memberList_and_show();
                        ACTV_member.setText("");
                        layout_settings_memberFunctionality.setVisibility(View.GONE);
                        Toasty.success(getApplicationContext(),"updated",Toasty.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toasty.error(getApplicationContext(),"Invalid Operation",Toasty.LENGTH_SHORT).show();
                ACTV_member.setText("");
                layout_settings_memberFunctionality.setVisibility(View.GONE);

                break;
            case R.id.PD_settings_btnAddNew:
                //upper corner add button

                //set Visible of required components
                layout_settings_memberFunctionality.setVisibility(View.VISIBLE);
                ACTV_member.setEnabled(true);
                ACTV_member.setVisibility(View.VISIBLE);
                memberRole_spinner.setVisibility(View.VISIBLE);
                setting_btnAddMember.setVisibility(View.VISIBLE);
                setting_btnUpdateMember.setVisibility(View.GONE);

                break;

            case R.id.PD_setting_btnDeleteMember:
                // this will delete the current member from the database and arraylist also
                if (ACTV_member != null && ACTV_member.getText() != null && !ACTV_member.getText().toString().isEmpty()){
                    String member_name = ACTV_member.getText().toString();
                    for (MemberInfo m: members){
                        if (member_name.equals(m.getUserName()) && !member_name.equals(userName)){
                            members.remove(m);
                            //member_adapter.notifyDataSetChanged();
                            delete_member_from_project(m.getId());
                            ACTV_member.setText("");
                            layout_settings_memberFunctionality.setVisibility(View.GONE);
                            return;
                        }
                    }
                    Toasty.error(getApplicationContext(),"Invaild operation",Toasty.LENGTH_SHORT).show();
                    ACTV_member.setText("");
                    layout_settings_memberFunctionality.setVisibility(View.GONE);
                }
                break;
            case R.id.PD_setting_btnCancel:
                layout_settings_memberFunctionality.setVisibility(View.GONE);
                break;
            case R.id.PD_setting_btnDeleteProject:
                // this is delete the whole project
                // confirm dialogue to confirm the request
                delete_project_button_Dialog();
                break;
        }

    }
    private String add_new_member_into_project(String id,String fullname,String username,String role){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(MEMBERS_PROJECT_TABLE);
        String key = db.push().getKey();

        MembersProject member = new MembersProject(str_Pid,key,str_projectName,fullname,username,id,role);
        db.child(key).setValue(member);
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).document(key).set(member);


        return key;

    }
    private void delete_member_from_project(final String id){
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).document(id)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child(MEMBERS_PROJECT_TABLE).child(id).removeValue();
                Toasty.success(getApplicationContext(),"Member deleted",Toasty.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG).show();
            }
        });
    }
    private void update_project_info(){
        if (!check_validation_Name() | !check_validation_category() | !check_validation_description()){
            return;
        }
        if (status_spinner == null || status_spinner.getSelectedItem()==null || status_spinner.getSelectedItem().toString().isEmpty()){
            Toasty.error(getApplicationContext(),"Status can not be empty",Toasty.LENGTH_SHORT).show();
            return;
        }
        String str_framework="",str_platform="",str_requirement="",str_prerequisite="",str_status,str_startDate,str_endDate,str_modifiedDate,str_modifiedUser;
        String str_name = project_name.getEditText().getText().toString();
        String str_category = category.getEditText().getText().toString();
        String str_description = description.getEditText().getText().toString();
        if (etFramework.getText() !=null && !etFramework.getText().toString().isEmpty())    str_framework=etFramework.getText().toString();
        if (etPlatform.getText() !=null && !etPlatform.getText().toString().isEmpty())    str_platform=etPlatform.getText().toString();
        if (etRequirement.getText() !=null && !etRequirement.getText().toString().isEmpty())    str_requirement=etRequirement.getText().toString();
        if (etPrerequisite.getText() !=null && !etPrerequisite.getText().toString().isEmpty())    str_prerequisite=etPrerequisite.getText().toString();
        str_status = status_spinner.getSelectedItem().toString();

        if (str_status.toLowerCase().equals("in progress")){
            if (etStartDate.getText() ==null || etStartDate.getText().toString().isEmpty() || etStartDate.getText().toString().equals("N/A")){
                str_startDate = get_current_Date();
            }else{
                str_startDate = etStartDate.getText().toString();
            }
            FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid).update("startingDate",str_startDate);
            FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid).child("startingDate").setValue(str_startDate);
        }else if (str_status.toLowerCase().equals("completed")){
            if (etEndDate.getText() == null || etEndDate.getText().toString().isEmpty() || etEndDate.getText().toString().equals("N/A")){
                str_endDate = get_current_Date();
            }else{
                str_endDate = etEndDate.getText().toString();
            }
            FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid).update("endDate",str_endDate);
            FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid).child("endDate").setValue(str_endDate);
        }
        // update data into firebase
        DocumentReference doc = FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid);

        doc.update("name",str_name);
        dref.child("name").setValue(str_name);
        doc.update("category",str_category);
        dref.child("category").setValue(str_category);
        doc.update("description",str_description);
        dref.child("description").setValue(str_description);
        doc.update("framework",str_framework);
        dref.child("framework").setValue(str_framework);
        doc.update("platform",str_platform);
        dref.child("platform").setValue(str_platform);
        doc.update("requirement",str_requirement);
        dref.child("requirement").setValue(str_requirement);
        doc.update("prerequisite",str_prerequisite);
        dref.child("prerequisite").setValue(str_prerequisite);
        doc.update("status",str_status);
        dref.child("status").setValue(str_status);
        doc.update("modifiedDate",get_current_Date());
        dref.child("modifiedDate").setValue(get_current_Date());
        doc.update("modifiedUser",userName);
        dref.child("modifiedUser").setValue(userName);
        doc.update("modifiedVersion",String.valueOf(Integer.parseInt(str_modifiedVersion)+1));
        dref.child("modifiedVersion").setValue(String.valueOf(Integer.parseInt(str_modifiedVersion)+1));


    }
    private Boolean check_validation_Name() {

        if (project_name == null || project_name.getEditText() == null || project_name.getEditText().getText().toString().isEmpty()) {
            project_name.setError("Field cannot be empty");
            return false;
        } else {
            project_name.setError(null);
            project_name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_validation_category() {

        if (category == null || category.getEditText() == null || category.getEditText().getText().toString().isEmpty()) {
            category.setError("Field cannot be empty");
            return false;
        } else {
            category.setError(null);
            category.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_validation_description() {

        if (description == null || description.getEditText() == null || description.getEditText().getText().toString().isEmpty()) {
            description.setError("Field cannot be empty");
            return false;
        } else {
            description.setError(null);
            description.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.password_manager_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.PM_search_menu);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    public String get_current_DateOnly(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    private String convert_Date_format(String DateTime){
        //format july 22,2020, wed at 05:29 PM
        // 22-08-2020 Wed 05:34 PM
        if (!DateTime.isEmpty()){
            String localDate = DateTime.substring(0,10);
            String localTime = DateTime.substring(14,23);
            String day = DateTime.substring(10,14);
            if (localDate.equals(get_current_DateOnly())){
                return "Today at "+localTime;
            }
            try {
                DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                DateFormat targetFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat targetFormat2 = new SimpleDateFormat("MMMM dd");
                DateFormat targetFormat3 = new SimpleDateFormat("MMMM dd, yyyy");
                Date date = originalFormat.parse(localDate);
                if (localDate.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                    return targetFormat2.format(date)+","+day+" at "+localTime;
                }else{
                    return targetFormat3.format(date)+","+day+" at "+localTime;
                }


            }catch (Exception e){
                Log.d("ERROR",e.getMessage());
            }
        }


        return DateTime;
    }

    private void delete_project_button_Dialog() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Are you sure to delete this project? If deleted it can not be undone")
                .setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked

                        delete_project();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No button clicked
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void delete_project(){
        //1. delete all tasks
        //2. delete all project members
        //3. delete the project

        //1
        FirebaseFirestore.getInstance().collection(TASK_TABLE).whereEqualTo("pid",str_Pid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                        final String tid = snapshot.getString("tid");
                        FirebaseFirestore.getInstance().collection(TASK_TABLE).document(tid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference().child(TASK_TABLE).child(tid).removeValue();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG).show();
            }
        });
        //2
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).whereEqualTo("pid",str_Pid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        final String mid = snapshot.getString("mid");
                        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).document(mid).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference().child(MEMBERS_PROJECT_TABLE).child(mid).removeValue();
                                    }
                                });
                    }
                }
            }
        });
        //3
        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_Pid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_Pid).removeValue();
                        Toasty.success(getApplicationContext(),"Project Deleted",Toasty.LENGTH_SHORT).show();
                        ProjectDetails.this.finish();

                    }
                });

    }

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userType = sharedPreferences.getString("userType","");
            userstatus = sharedPreferences.getString("status","");
            //userPassword = sharedPreferences.getString("password","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }

}
