package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

public class AdminProjectDetails extends AppCompatActivity {
    private Toolbar info_toolbar,setting_toolbar,tasks_toolbar;
    private TextInputEditText etProjectName, etCategory, etStatus, etDescription, etFramework,etPlatform,etRequirement,etPrerequisite,
            etModifiedDate, etModifiedUser, etCreatedDate, etCreatedUser, etStartDate,
            etEndDate, etTotalTasks, etTotalTaskPoints, etCompletedTasks, etCompletedTaskPoints,etCompleteTaskRatio;
    private TextInputLayout status_layout,project_name,category,description;
    private LinearLayout layout_tasks, layout_info, layout_settings;
    private CoordinatorLayout layout_cordinator;
    private RecyclerView task_recylerView,member_recylerView;
    private MemberInfoAdapter member_adapter;
    private static final String MEMBERS_PROJECT_TABLE = "MembersProject";
    private static final String PROJECT_TABLE = "Projects";
    private static final String TASK_TABLE = "Tasks";
    ArrayList<MemberInfo> members = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private TaskInfoAdapter mAdapter;
    private String str_projectName,str_Pid,str_category,str_description,str_framework,str_platform,str_requirement,str_prerequisite,str_status,str_startDate,str_endDate,
            str_modifiedUser,str_modifiedDate,str_createdDate,str_createdUser,str_totalTasks,str_totalTaskPoints,str_completedTasks,str_completedTaskPoints,str_modifiedVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_project_details);

        toolbar_settings();
        find_view_by_id();
        get_data_from_intent();
        get_member_list_and_show();
        get_all_task_lists();

        BottomNavigationView bottomNav = findViewById(R.id.history_bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_task_lists();
        get_member_list_and_show();
    }

    private void toolbar_settings(){
        // this is for info toolbar
        info_toolbar = findViewById(R.id.APD_info_toolbar);
        info_toolbar.setTitle("Details");
        setSupportActionBar(info_toolbar);
        // this is for settings toolbar
        setting_toolbar = findViewById(R.id.APD_setting_toolbar);
        setting_toolbar.setTitle("Settings");
        setSupportActionBar(setting_toolbar);
        // this is for tasks toolbar
        tasks_toolbar = findViewById(R.id.APD_tasks_toolbar);
        tasks_toolbar.setTitle("Tasks");
        setSupportActionBar(tasks_toolbar);
    }
    private void find_view_by_id() {
        // all tasks components
        layout_tasks = findViewById(R.id.APD_layout_tasks);
        layout_info = findViewById(R.id.APD_layout_Info);
        layout_settings = findViewById(R.id.APD_layout_Setting);
        layout_cordinator = findViewById(R.id.APD_cordinator_layout);
        task_recylerView = findViewById(R.id.APD_tasks_recylerView);

        // all info components
        etProjectName = findViewById(R.id.APD_info_name_input);
        etCategory = findViewById(R.id.APD_info_category_input);
        etDescription = findViewById(R.id.APD_info_description_input);
        etStatus = findViewById(R.id.APD_info_status_input);
        etFramework = findViewById(R.id.APD_info_framework_input);
        etPlatform = findViewById(R.id.APD_info_platform_input);
        etRequirement = findViewById(R.id.APD_info_requirement_input);
        etPrerequisite = findViewById(R.id.APD_info_prerequisite_input);
        etModifiedDate = findViewById(R.id.APD_info_modifiedDate_input);
        etModifiedUser = findViewById(R.id.APD_info_modifiedUser_input);
        etCreatedDate = findViewById(R.id.APD_info_createdDate_input);
        etCreatedUser = findViewById(R.id.APD_info_createdUser_input);
        etStartDate = findViewById(R.id.APD_info_startingDate_input);
        etEndDate = findViewById(R.id.APD_info_endDate_input);
        etTotalTasks = findViewById(R.id.APD_info_totalTasks_input);
        etTotalTaskPoints = findViewById(R.id.APD_info_totalTaskPoints_input);
        etCompletedTasks = findViewById(R.id.APD_info_completedTasks_input);
        etCompletedTaskPoints = findViewById(R.id.APD_info_completedTaskPoints_input);
        etCompleteTaskRatio = findViewById(R.id.APD_info_completedTaskRatio_input);
        status_layout = findViewById(R.id.APD_info_status);
        project_name = findViewById(R.id.APD_info_name);
        category = findViewById(R.id.APD_info_category);
        description = findViewById(R.id.APD_info_description);

        //seting section
        member_recylerView = findViewById(R.id.APD_settings_members_recylerView);
    }
    private void get_data_from_intent(){
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
        set_all_info_data_into_editBoxes();
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

    }
    private void get_member_list_and_show(){
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).whereEqualTo("pid",str_Pid)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        };
                        members.clear();
                        for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                            String fullname = snapshot.getString("memberFullName");
                            String mid = snapshot.getString("mid");
                            String role = snapshot.getString("role");
                            String name = snapshot.getString("memberUserName");
                            members.add(new MemberInfo(mid,fullname,name,role));
                        }
                        mLayoutManager = new LinearLayoutManager(AdminProjectDetails.this);
                        member_adapter = new MemberInfoAdapter(members);
                        member_recylerView.setLayoutManager(mLayoutManager);
                        member_recylerView.setAdapter(member_adapter);
                    }
                });
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
                        task_recylerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(AdminProjectDetails.this);
                        mAdapter = new TaskInfoAdapter(task_infos);
                        task_recylerView.setLayoutManager(mLayoutManager);
                        task_recylerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new TaskInfoAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(AdminProjectDetails.this, AdminTaskDetails.class);
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
                                startActivity(intent);
                                //mAdapter.notifyItemChanged(position);
                            }
                        });

                    }
                });
    }
    private String convert_Date_format(String DateTime){
        //format july 22,2020, wed at 05:29 PM
        // 22-08-2020 Wed 05:34 PM
        if (!DateTime.isEmpty()){
            String localDate = DateTime.substring(0,10);
            String localTime = DateTime.substring(14,23);
            String day = DateTime.substring(10,14);
            if (localDate.equals(get_current_Date())){
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
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
