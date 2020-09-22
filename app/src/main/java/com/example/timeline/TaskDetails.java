package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDetails extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layout_link;
    private TextInputLayout name,serial,category,status,priority,description,solution,deadline,assigning,points
            ,issues,link,comments,modifiedUser,modifiedDate,createdDate,createdUser,MATV_layout;
    private MultiAutoCompleteTextView assign_MATV;
    private TextInputEditText etName,etSerial,etCategory,etStatus,etPriority,etDescription,etSolution,etDeadline,etAssigning
            ,etPoints,etIssues,etLink,etComments,etModifiedDate,etModifiedUser,etCreatedDate,etCreatedUser;
    private SearchableSpinner category_spinner,status_spinner,priority_spinner;
    private Button btnEdit,btnUpdate,btnDelete,btnCopy,btnCancel;
    private static final String TASK_TABLE = "Tasks";
    private static final String PROJECT_TABLE = "Projects";
    private String str_tid,str_pid,str_name,str_serial,str_catrgory,str_status,str_priority,str_description,str_soluton,str_deadline,str_assigning,str_points,
            str_issues,str_link,str_createdDate,str_createdUser,str_modifiedDate,str_modifiedUser,str_comments,str_modifiedVersion;
    private String[] member_list;
    private String uid,userName,userFullName,userType,userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        find_viewBy_id();
        get_user_info();
        get_info_from_intent();
        setUp_all_info_into_textBoxes();
        setUp_spinner();

        setUp_delete_button_constrains();

        btnEdit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    private void find_viewBy_id(){
        name = findViewById(R.id.TD_name);
        etName = findViewById(R.id.TD_name_input);
        serial = findViewById(R.id.TD_serial);
        etSerial = findViewById(R.id.TD_serial_input);
        category = findViewById(R.id.TD_category);
        etCategory = findViewById(R.id.TD_category_input);
        status = findViewById(R.id.TD_status);
        etStatus = findViewById(R.id.TD_status_input);
        priority = findViewById(R.id.TD_priority);
        etPriority = findViewById(R.id.TD_priority_input);
        description = findViewById(R.id.TD_description);
        etDescription = findViewById(R.id.TD_description_input);
        solution = findViewById(R.id.TD_solution);
        etSolution = findViewById(R.id.TD_solution_input);
        deadline = findViewById(R.id.TD_deadline);
        etDeadline = findViewById(R.id.TD_deadline_input);
        assigning = findViewById(R.id.TD_assigningTo);
        etAssigning = findViewById(R.id.TD_assigningTo_input);
        points = findViewById(R.id.TD_points);
        etPoints = findViewById(R.id.TD_points_input);
        issues = findViewById(R.id.TD_issues);
        etIssues = findViewById(R.id.TD_issues_input);
        link = findViewById(R.id.TD_link);
        etLink = findViewById(R.id.TD_link_input);
        category = findViewById(R.id.TD_category);
        etCategory = findViewById(R.id.TD_category_input);
        comments = findViewById(R.id.TD_comments);
        etComments = findViewById(R.id.TD_comments_input);
        modifiedDate = findViewById(R.id.TD_modifiedDate);
        etModifiedDate = findViewById(R.id.TD_modifiedDate_input);
        modifiedUser = findViewById(R.id.TD_modifiedUser);
        etModifiedUser = findViewById(R.id.TD_modifiedUser_input);
        createdDate = findViewById(R.id.TD_createdDate);
        etCreatedDate = findViewById(R.id.TD_createdDate_input);
        createdUser = findViewById(R.id.TD_createdUser);
        etCreatedUser = findViewById(R.id.TD_createdUser_input);
        category_spinner = findViewById(R.id.TD_category_spinner);
        status_spinner = findViewById(R.id.TD_status_spinner);
        priority_spinner = findViewById(R.id.TD_priority_spinner);
        MATV_layout = findViewById(R.id.TD_assigningTo_TV);
        assign_MATV = findViewById(R.id.TD_assigningTo_MACTV);
        layout_link = findViewById(R.id.TD_link_layout);
        btnEdit = findViewById(R.id.TD_btnEdit);
        btnUpdate = findViewById(R.id.TD_btnUpdate);
        btnDelete = findViewById(R.id.TD_btnDelete);
        btnCopy = findViewById(R.id.TD_btnCopy);
        btnCancel = findViewById(R.id.TD_btnCancel);
    }

    private void get_info_from_intent(){
        Intent intent = getIntent();
        str_pid = intent.getStringExtra("Pid");
        str_tid = intent.getStringExtra("Tid");
        str_name = intent.getStringExtra("name");
        str_serial = intent.getStringExtra("serial");
        str_catrgory = intent.getStringExtra("category");
        str_description = intent.getStringExtra("description");
        str_soluton = intent.getStringExtra("solution");
        str_deadline = intent.getStringExtra("deadline");
        str_assigning = intent.getStringExtra("assigner");
        str_points = intent.getStringExtra("points");
        str_issues = intent.getStringExtra("issues");
        str_link = intent.getStringExtra("link");
        str_priority = intent.getStringExtra("priority");
        str_status = intent.getStringExtra("status");
        str_modifiedDate = intent.getStringExtra("modifiedDate");
        str_modifiedUser = intent.getStringExtra("modifiedUser");
        str_createdDate = intent.getStringExtra("createdDate");
        str_createdUser = intent.getStringExtra("createdUser");
        str_comments = intent.getStringExtra("comments");
        member_list = intent.getStringArrayExtra("member_list");
        userRole = intent.getStringExtra("userRole");
        str_modifiedVersion = intent.getStringExtra("modifiedVersion");
        setTp_MultiAutoCompleteTV_assignTo();

    }

    private void setUp_all_info_into_textBoxes(){
        if (str_name != null && !str_name.isEmpty()){
            etName.setText(str_name);
        }
        if (str_serial != null && !str_serial.isEmpty()){
            etSerial.setText(str_serial);
        }
        if (str_catrgory != null && !str_catrgory.isEmpty()){
            etCategory.setText(str_catrgory);
        }
        if (str_description != null && !str_description.isEmpty()){
            etDescription.setText(str_description);
        }
        if (str_soluton != null && !str_soluton.isEmpty()){
            etSolution.setText(str_soluton);
        }
        if (str_deadline != null && !str_deadline.isEmpty()){
            etDeadline.setText(str_deadline);
        }
        if (str_assigning != null && !str_assigning.isEmpty()){
            etAssigning.setText(str_assigning);
            assign_MATV.setText(str_assigning);
        }
        if (str_points != null && !str_points.isEmpty()){
            etPoints.setText(str_points);
        }
        if (str_issues != null && !str_issues.isEmpty()){
            etIssues.setText(str_issues);
        }
        if (str_link != null && !str_link.isEmpty()){
            etLink.setText(str_link);
        }
        if (str_priority != null && !str_priority.isEmpty()){
            etPriority.setText(str_priority);
        }
        if (str_status != null && !str_status.isEmpty()){
            etStatus.setText(str_status);
        }
        if (str_modifiedDate != null && !str_modifiedDate.isEmpty()){
            etModifiedDate.setText(convert_Date_format(str_modifiedDate));
        }
        if (str_modifiedUser != null && !str_modifiedUser.isEmpty()){
            etModifiedUser.setText(str_modifiedUser);
        }
        if (str_createdDate != null && !str_createdDate.isEmpty()){
            etCreatedDate.setText(convert_Date_format(str_createdDate));
        }
        if (str_createdUser != null && !str_createdUser.isEmpty()){
            etCreatedUser.setText(str_createdUser);
        }
        if (str_comments != null && !str_comments.isEmpty()){
            etComments.setText(str_comments);
        }
    }

    private void setUp_spinner(){
        // status spinner
        String[] status = getResources().getStringArray(R.array.task_status_string);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(status_adapter);
        //set selected item of status spinner
        if (str_status != null && !str_status.isEmpty()){
            int position = status_adapter.getPosition(str_status);
            status_spinner.setSelection(position);
        }

        // category spinner
        String[] category = getResources().getStringArray(R.array.task_category_string);

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,category);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_adapter);
        //set selected item of category spinner
        if (str_catrgory != null && !str_catrgory.isEmpty()){
            int position = category_adapter.getPosition(str_catrgory);
            category_spinner.setSelection(position);
        }

        // priority spinner
        String[] priority = getResources().getStringArray(R.array.task_priority_string);

        ArrayAdapter<String> priority_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,priority);
        priority_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority_spinner.setAdapter(priority_adapter);
        //set selected item of priority spinner
        if (str_priority != null && !str_priority.isEmpty()){
            int position = priority_adapter.getPosition(str_priority);
            priority_spinner.setSelection(position);
        }

    }

    private void set_enable_editBoxes_spinner(){
        if (str_createdUser != null && !str_createdUser.isEmpty() &&
                (str_createdUser.equals(userName) || userRole.equals("Leader") || userRole.equals("Co-Leader") || userRole.equals("Manager"))){
            name.setEnabled(true);
            category.setVisibility(View.GONE);
            category_spinner.setVisibility(View.VISIBLE);
            assigning.setVisibility(View.GONE);
            MATV_layout.setVisibility(View.VISIBLE);
            priority.setVisibility(View.GONE);
            priority_spinner.setVisibility(View.VISIBLE);
        }

        serial.setEnabled(true);
        description.setEnabled(true);
        solution.setEnabled(true);
        deadline.setEnabled(true);
        points.setEnabled(true);
        issues.setEnabled(true);
        link.setEnabled(true);
        status.setVisibility(View.GONE);
        status_spinner.setVisibility(View.VISIBLE);
        comments.setEnabled(true);
        btnEdit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }
    private void set_disable_editBoxes(){
        etName.setEnabled(false);
        category.setVisibility(View.VISIBLE);
        category.getEditText().setText(category_spinner.getSelectedItem().toString());
        category_spinner.setVisibility(View.GONE);
        assigning.setVisibility(View.VISIBLE);
        assigning.getEditText().setText(assign_MATV.getText().toString());
        MATV_layout.setVisibility(View.GONE);
        priority.setVisibility(View.VISIBLE);
        priority.getEditText().setText(priority_spinner.getSelectedItem().toString());
        priority_spinner.setVisibility(View.GONE);
        etSerial.setEnabled(false);
        etDescription.setEnabled(false);
        etSolution.setEnabled(false);
        etDeadline.setEnabled(false);
        etPoints.setEnabled(false);
        etIssues.setEnabled(false);
        etLink.setEnabled(false);
        etComments.setEnabled(false);
        status.setVisibility(View.VISIBLE);
        status.getEditText().setText(status_spinner.getSelectedItem().toString());
        status_spinner.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);


    }

    private void setUp_delete_button_constrains(){
        if (str_createdUser != null && !str_createdUser.isEmpty() &&
                (str_createdUser.equals(userName) || userRole.equals("Leader") || userRole.equals("Co-Leader") || userRole.equals("Manager"))){
            btnDelete.setVisibility(View.VISIBLE);
        }
    }
    private void setTp_MultiAutoCompleteTV_assignTo(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,member_list);
        assign_MATV.setAdapter(adapter);
        assign_MATV.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // get single item from TVassign
        /*String input = TVassign.getText().toString().trim();
        String[] singleInput = input.split("\\s*,\\s*");*/
    }

    private void update_data_info_firebase(){
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
        String str_name, str_description, str_category, str_priority, str_status, str_serial = "", str_solution = "", str_deadline, str_assigningTo,
                str_points = "0", str_issues, str_links,str_comments="",str_modifiedDate,str_modifiedUser;
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
        str_comments = comments.getEditText().getText().toString();
        str_modifiedDate = get_current_Date();
        str_modifiedUser = userName;

        //update data into the database
        DocumentReference doc = FirebaseFirestore.getInstance().collection(TASK_TABLE).document(str_tid);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child(TASK_TABLE).child(str_tid);

        //Task table update
        //Firestore
        doc.update("name",str_name);
        doc.update("serial",str_serial);
        doc.update("category",str_category);
        doc.update("description",str_description);
        doc.update("priority",str_priority);
        doc.update("solution",str_solution);
        doc.update("deadline",str_deadline);
        doc.update("status",str_status);
        doc.update("assigner",str_assigningTo);
        doc.update("points",str_points);
        doc.update("issues",str_issues);
        doc.update("link",str_links);
        doc.update("comments",str_comments);
        doc.update("modifiedDate",str_modifiedDate);
        doc.update("modifiedUser",str_modifiedUser);

        //Project table update
        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_pid).update("modifiedDate",get_current_Date());
        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_pid).update("modifiedUser",userName);
        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).document(str_pid).update("modifiedVersion",String.valueOf(Integer.parseInt(str_modifiedVersion)+1));

        //Realtime Database
        dref.child("name").setValue(str_name);
        dref.child("serial").setValue(str_serial);
        dref.child("category").setValue(str_category);
        dref.child("description").setValue(str_description);
        dref.child("priority").setValue(str_priority);
        dref.child("solution").setValue(str_solution);
        dref.child("deadline").setValue(str_deadline);
        dref.child("status").setValue(str_status);
        dref.child("assigner").setValue(str_assigningTo);
        dref.child("points").setValue(str_points);
        dref.child("issues").setValue(str_issues);
        dref.child("link").setValue(str_links);
        dref.child("comments").setValue(str_comments);
        dref.child("modifiedDate").setValue(get_current_Date());
        dref.child("modifiedUser").setValue(userName);

        //Project Table update
        FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_pid).child("modifiedDate").setValue(get_current_Date());
        FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_pid).child("modifiedUser").setValue(userName);
        FirebaseDatabase.getInstance().getReference().child(PROJECT_TABLE).child(str_pid).child("modifiedVersion").setValue(String.valueOf(Integer.parseInt(str_modifiedVersion)+1));


        Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
        // show modifiedDate and modifiedUser
        etModifiedDate.setText(get_current_Date());
        etModifiedUser.setText(userName);
        set_disable_editBoxes();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.TD_btnEdit:
                set_enable_editBoxes_spinner();
                Toasty.info(getApplicationContext(),"You are now in edit mode",Toasty.LENGTH_LONG).show();

                break;
            case R.id.TD_btnUpdate:
                update_data_info_firebase();
                break;

            case R.id.TD_btnCancel:
                set_disable_editBoxes();
                break;

            case R.id.TD_btnDelete:
                delete_task_dialogue();
                break;

            case R.id.TD_btnCopy:
                if (str_link != null && !str_link.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Link", link.getEditText().getText().toString().trim());
                    clipboard.setPrimaryClip(clip);

                    Uri webpage = Uri.parse(str_link);
                    Intent webintent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(webintent);
                }else{
                    Toasty.error(getApplicationContext(),"Link empty",Toasty.LENGTH_SHORT).show();
                }

                break;
        }
    }
    private void delete_task_dialogue(){
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure to delete this task? If deleted it can not be undone")
                .setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked
                        delete_task();
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

    private void delete_task(){
        //delete task from firebase
        FirebaseFirestore.getInstance().collection(TASK_TABLE).document(str_tid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference().child(TASK_TABLE).child(str_tid).removeValue();
                        Toasty.success(getApplicationContext(),"Task deleted",Toasty.LENGTH_SHORT);
                        TaskDetails.this.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG);
            }
        });
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
    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userType = sharedPreferences.getString("userType","");
            //userPassword = sharedPreferences.getString("password","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
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
}
