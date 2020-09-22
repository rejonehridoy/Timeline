package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class TaskCreate extends AppCompatActivity {
    private TextInputLayout name, serial, description, solution, deadline, assigning, points, issues, link;
    private SearchableSpinner category_spinner, status_spinner, priority_spinner;
    private Button btnCreate;
    private MultiAutoCompleteTextView TVassign;
    private String pid;
    private int totalTasks, totalTaskPoints, completedTask, completedTaskPoints, modifiedVersion;
    private static final String MEMBERS_PROJECT_TABLE = "MembersProject";
    private static final String PROJECT_TABLE = "Projects";
    private static final String TASK_TABLE = "Tasks";
    private ArrayList<MembersProject> membersProjects = new ArrayList<>();
    private String uid, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        name = findViewById(R.id.TC_name);
        serial = findViewById(R.id.TC_serial);
        description = findViewById(R.id.TC_description);
        solution = findViewById(R.id.TC_solution);
        deadline = findViewById(R.id.TC_deadline);
        assigning = findViewById(R.id.TC_assigningTo);
        points = findViewById(R.id.TC_points);
        issues = findViewById(R.id.TC_issues);
        link = findViewById(R.id.TC_link);
        category_spinner = findViewById(R.id.TC_category_spinner);
        status_spinner = findViewById(R.id.TC_status_spinner);
        priority_spinner = findViewById(R.id.TC_priority_spinner);
        btnCreate = findViewById(R.id.TC_btnCreate);
        TVassign = findViewById(R.id.TC_assigningTo_MACTV);

        get_user_info();
        get_pid_from_intent();
        get_project_members_from_firebase();
        setUp_spinner();


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create task btn
                create_task_in_firebase();
            }
        });
    }

    private void create_task_in_firebase() {
        if (!check_validation_Name() | !check_validation_description()) {
            return;
        }
        if (category_spinner == null || category_spinner.getSelectedItem() == null || category_spinner.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select category", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (priority_spinner == null || priority_spinner.getSelectedItem() == null || priority_spinner.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select priority", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (status_spinner == null || status_spinner.getSelectedItem() == null || status_spinner.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select satus", Toasty.LENGTH_SHORT).show();
            return;
        }
        String str_name, str_description, str_category, str_priority, str_status, str_serial = "", str_solution = "", str_deadline, str_assigningTo, str_points = "0", str_issues, str_links;
        str_name = name.getEditText().getText().toString();
        str_serial = serial.getEditText().getText().toString();
        str_category = category_spinner.getSelectedItem().toString();
        str_description = description.getEditText().getText().toString();
        str_priority = priority_spinner.getSelectedItem().toString();
        str_solution = solution.getEditText().getText().toString();
        str_deadline = deadline.getEditText().getText().toString();
        str_status = status_spinner.getSelectedItem().toString();
        str_assigningTo = assigning.getEditText().getText().toString();
        if (!points.getEditText().getText().toString().isEmpty()) {
            str_points = points.getEditText().getText().toString();
        }
        str_issues = issues.getEditText().getText().toString();
        str_links = link.getEditText().getText().toString();

        //create data in the firebase
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(TASK_TABLE);
        String key = db.push().getKey();

        TaskInfo taskInfo = new TaskInfo(pid, key, str_name, str_serial, str_category, str_description, str_solution, str_deadline, str_assigningTo, str_points, str_issues, str_links,
                str_status, str_priority, "", "", get_current_Date(), userName, "");

        db.child(key).setValue(taskInfo);
        FirebaseFirestore.getInstance().collection(TASK_TABLE).document(key).set(taskInfo);



        //update information in the project table

        DocumentReference doc = FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(pid);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(pid);

        //update modified Date and modified User
        doc.update("modifiedDate",get_current_Date());
        dref.child("modifiedDate").setValue(get_current_Date());
        doc.update("modifiedUser",userName);
        dref.child("modifiedUser").setValue(userName);

        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(pid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    int version = Integer.parseInt(documentSnapshot.getString("modifiedVersion"));
                    //update modified Version
                    FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(pid).update("modifiedVersion",String.valueOf(version+1));
                    FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(pid).child("modifiedVersion").setValue(String.valueOf(version+1));
                    Toasty.success(getApplicationContext(), "Task Created", Toasty.LENGTH_SHORT).show();
                    TaskCreate.this.finish();
                }else{
                    Toasty.error(getApplicationContext(), "Error Occurred", Toasty.LENGTH_SHORT).show();
                    TaskCreate.this.finish();
                }
            }
        });

    }

    private void get_pid_from_intent() {
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
    }

    private void get_project_members_from_firebase() {
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).whereEqualTo("pid", pid)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        membersProjects.clear();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            membersProjects.add(snapshot.toObject(MembersProject.class));
                        }
                        setTp_MultiAutoCompleteTV_assignTo();
                    }
                });
    }

    private void setUp_spinner() {
        // status spinner
        String[] status = getResources().getStringArray(R.array.task_status_string);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(status_adapter);

        // category spinner
        String[] category = getResources().getStringArray(R.array.task_category_string);

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_adapter);

        // priority spinner
        String[] priority = getResources().getStringArray(R.array.task_priority_string);

        ArrayAdapter<String> priority_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priority);
        priority_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority_spinner.setAdapter(priority_adapter);

    }

    private void setTp_MultiAutoCompleteTV_assignTo() {
        ArrayAdapter<MembersProject> adapter = new ArrayAdapter<MembersProject>(this, android.R.layout.simple_list_item_1, membersProjects);
        TVassign.setAdapter(adapter);
        TVassign.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // get single item from TVassign
        /*String input = TVassign.getText().toString().trim();
        String[] singleInput = input.split("\\s*,\\s*");*/
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

    public String get_current_Date() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

    private void get_user_info() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin", false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid", "");
            userName = sharedPreferences.getString("userName", "");

        } else {
            this.finish();
            Toasty.error(getApplicationContext(), "User didn't log in", Toasty.LENGTH_LONG).show();
        }
    }


}
