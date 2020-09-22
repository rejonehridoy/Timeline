package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class ProjectNew extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout name,category,description,framework,platform,requirement,prerequisite,member_layout;
    private AutoCompleteTextView member_name;
    private SearchableSpinner status_spinner,role_spinner;
    private Button btnAddMember,btnCreateProject;
    private RecyclerView info_recylerView;
    private ArrayList<MemberInfo> infos = new ArrayList<>();
    private ArrayList<MemberInfo> member_infos = new ArrayList<>();         //a arraylist of all users and found from firebase
    private MemberInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String userName,userFullName,userType,uid,userEmail,userstatus;
    private ProgressBar progressBar;
    private static final String USER_TABLE = "UserDetails";
    private static final String PROJECT_TABLE = "Projects";
    private static final String MEMBERS_PROJECT_TABLE = "MembersProject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_new);

        name = findViewById(R.id.PN_name);
        category = findViewById(R.id.PN_category);
        description = findViewById(R.id.PN_description);
        framework = findViewById(R.id.PN_framework);
        platform = findViewById(R.id.PN_platform);
        requirement = findViewById(R.id.PN_requirement);
        prerequisite = findViewById(R.id.PN_prerequisite);
        status_spinner = findViewById(R.id.PN_status_spinner);
        role_spinner = findViewById(R.id.PN_role_spinner);
        btnAddMember = findViewById(R.id.PN_btnAddMember);
        btnCreateProject = findViewById(R.id.PN_btnCreate);
        member_name = findViewById(R.id.PN_member_name);
        member_layout = findViewById(R.id.PN_member);
        info_recylerView = findViewById(R.id.PN_recylerView);
        progressBar = findViewById(R.id.PN_progressBar);


        get_user_info();            //this will fetch current logged in user info from sharedpreference
        setUp_spinner();
        get_all_user_info();
        setUp_autoComplete_textView();
        initialize_member_info();           //will setup recylerview
        implement_swap_remove();



        btnAddMember.setOnClickListener(this);
        btnCreateProject.setOnClickListener(this);

    }

    private void implement_swap_remove(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((MemberInfo) viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(info_recylerView);
    }

    private void removeItem(MemberInfo ob) {
        infos.remove(ob);
        mAdapter.notifyDataSetChanged();
        Toasty.success(getApplicationContext(),"Member removed",Toasty.LENGTH_SHORT).show();
    }

    private void setUp_spinner(){
        // status spinner
        String[] status = getResources().getStringArray(R.array.project_status_stirng);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(status_adapter);

        //role spinner
        String[] role = getResources().getStringArray(R.array.project_member_role_string);

        ArrayAdapter<String> role_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,role);
        role_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spinner.setAdapter(role_adapter);
    }
    private void setUp_autoComplete_textView(){
        ArrayAdapter<MemberInfo> adapter = new ArrayAdapter<MemberInfo>(this,android.R.layout.simple_list_item_1,member_infos);
        member_name.setAdapter(adapter);
    }

    private void get_all_user_info(){
        // in this method all user list will be found from firebase,for testing purpose we are making a duplicate
        //get All user data from firestore

        FirebaseFirestore.getInstance().collection(USER_TABLE).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    return;
                }
                member_infos.clear();
                for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                    String id = snapshot.getString("uid");
                    String fullName = snapshot.getString("fullName");
                    String userName = snapshot.getString("userName");
                    member_infos.add(new MemberInfo(id,fullName,userName,""));
                }
            }
        });
    }

    private void initialize_member_info(){
        //info_recylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MemberInfoAdapter(infos);
        info_recylerView.setLayoutManager(mLayoutManager);
        info_recylerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.PN_btnAddMember:

                if (role_spinner != null && role_spinner.getSelectedItem() !=null && member_name != null){
                    String name = member_name.getText().toString();
                    check_userName(name);
                }else{
                    Toasty.error(getApplicationContext(),"Role can not be empty",Toasty.LENGTH_SHORT).show();
                }

                break;

            case R.id.PN_btnCreate:
                //check validaiton first
                create_project_in_firebase();

                break;
        }
    }

    private void create_project_in_firebase(){
        if (!check_validation_Name() | !check_validation_category() | !check_validation_description()){
            return;
        }
        if (status_spinner == null || status_spinner.getSelectedItem() == null || status_spinner.getSelectedItem().toString().isEmpty()){
            Toasty.error(getApplicationContext(),"Status canot be empty",Toasty.LENGTH_SHORT).show();
            return;
        }

        String project_name = name.getEditText().getText().toString();
        String project_category = category.getEditText().getText().toString();
        String project_description = description.getEditText().getText().toString();
        String project_platform,project_framework,project_requirement,project_prerequisite,project_startDate="N/A",project_endDate="N/A";
        if (framework.getEditText().getText().toString().isEmpty()){
            project_framework = "N/A";
        }else{
            project_framework = framework.getEditText().getText().toString();
        }

        if (platform.getEditText().getText().toString().isEmpty()){
            project_platform = "N/A";
        }else{
            project_platform = platform.getEditText().getText().toString();
        }
        if (requirement.getEditText().getText().toString().isEmpty()){
            project_requirement = "N/A";
        }else{
            project_requirement = requirement.getEditText().getText().toString();
        }
        if (prerequisite.getEditText().getText().toString().isEmpty()){
            project_prerequisite = "N/A";
        }else{
            project_prerequisite = prerequisite.getEditText().getText().toString();
        }
        if (status_spinner.getSelectedItem().toString().toLowerCase().equals("in progress")){
            project_startDate = get_current_Date();
        }else if (status_spinner.getSelectedItem().toString().toLowerCase().equals("completed")){
            project_startDate = get_current_Date();
            project_endDate = get_current_Date();
        }
        //create value in the firebase Projects table

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(PROJECT_TABLE);
        String key = db.push().getKey();

        ProjectInfo project = new ProjectInfo(key,project_name,project_category,project_description,project_framework,project_platform,project_requirement,project_prerequisite,
                status_spinner.getSelectedItem().toString(),project_startDate,project_endDate,"","",get_current_Date(),userName,"0",
                "0","0","0","1");

        //Realtime database
        db.child(key).setValue(project);
        //Firestore
        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(key).set(project);

        DatabaseReference members = FirebaseDatabase.getInstance().getReference(MEMBERS_PROJECT_TABLE);
        //create user in the project Member
        //first one for project creator
        String mid = members.push().getKey();
        MembersProject mp = new MembersProject(key,mid,project_name,userFullName,userName,uid,"Manager");
        members.child(mid).setValue(mp);
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).document(mid).set(mp);
        // for other members
        for (MemberInfo member: infos){
            String member_id = members.push().getKey();
            MembersProject membersProject = new MembersProject(key,member_id,project_name,member.getFullName(),member.getUserName(),member.getId(),member.getRole());
            members.child(member_id).setValue(membersProject);
            FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).document(member_id).set(membersProject);
        }

        progressBar.setVisibility(View.GONE);
        clear_textFields();
        Toasty.success(getApplicationContext(),"Project Created",Toasty.LENGTH_SHORT).show();
        this.finish();
    }

    private void clear_textFields(){
        name.getEditText().setText("");
        category.getEditText().setText("");
        description.getEditText().setText("");
        framework.getEditText().setText("");
        platform.getEditText().setText("");
        requirement.getEditText().setText("");
        prerequisite.getEditText().setText("");
        infos.clear();
        mAdapter.notifyDataSetChanged();
    }
    private void check_userName(String name){
        if (name == null || name.isEmpty()) {
            member_layout.setError("Field cannot be empty");
        }
        else {
            Boolean isGot = false,isDuplicate = false;
            for (MemberInfo member:member_infos){
                if (name.equals(member.getUserName())){
                    if (name.equals(userName)){
                        Toasty.info(getApplicationContext(),"You will be automatically added after creation of the project",Toasty.LENGTH_LONG).show();
                        isDuplicate = true;
                    }else{
                        for (MemberInfo m:infos){
                            if (m.getUserName().equals(name)){
                                Toasty.error(getApplicationContext(),"Already added in the list",Toasty.LENGTH_LONG).show();
                                isDuplicate = true;
                                break;
                            }
                        }
                    }

                    if (!isDuplicate){
                        infos.add(new MemberInfo(member.getId(),member.getFullName(),member.getUserName(),role_spinner.getSelectedItem().toString()));
                        mAdapter.notifyDataSetChanged();
                        info_recylerView.setVisibility(View.VISIBLE);
                        Toasty.success(getApplicationContext(),"Member added",Toasty.LENGTH_SHORT).show();
                    }
                    member_name.setText("");
                    isGot =  true;

                }
            }
            if (isGot){
                member_layout.setError(null);
                member_layout.setErrorEnabled(false);
            }else{
                member_layout.setError("Username is not found");
            }
        }
    }

    private Boolean check_validation_Name() {

        if (name == null || name.getEditText() == null || name.getEditText().getText().toString().isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
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

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userType = sharedPreferences.getString("userType","");
            userstatus = sharedPreferences.getString("status","");
            //userPassword = sharedPreferences.getString("password","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
