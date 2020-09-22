package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminNoticeCreate extends AppCompatActivity {
    private SearchableSpinner repetation_spinner, dayofweek_spinner;
    private TextInputLayout subject, category, description, eventDate, eventTime, tag, link;
    private Button btnCreate;
    private static final String NOTICE_TABLE_NAME = "Notices";
    private String str_subject="",str_category="",str_description="",str_eventDate="",str_eventTime="",str_tag="",str_link="",str_repetation="",str_dayOfWeek="";
    private String uid,userName, userType, userEmail, userFullName,userstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice_create);

        get_user_info();

        subject = findViewById(R.id.ANC_subject);
        category = findViewById(R.id.ANC_category);
        description = findViewById(R.id.ANC_description);
        eventDate = findViewById(R.id.ANC_eventDate);
        eventTime = findViewById(R.id.ANC_eventTime);
        tag = findViewById(R.id.ANC_tag);
        link = findViewById(R.id.ANC_link);
        repetation_spinner = findViewById(R.id.ANC_repetation_spinner);
        dayofweek_spinner = findViewById(R.id.ANC_dayofweek_spinner);
        btnCreate = findViewById(R.id.ANC_btnsubmit);
        setup_spinner();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_notice();

            }
        });

    }

    private void setup_spinner() {


        //repetation spinner
        String[] repetation = getResources().getStringArray(R.array.ANC_repetation_string);

        ArrayAdapter<String> repetation_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repetation);
        repetation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repetation_spinner.setAdapter(repetation_adapter);

        // day of week spinner
        String[] dayofweek = getResources().getStringArray(R.array.ANC_dayOfWeek_string);

        ArrayAdapter<String> dayofweek_adater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dayofweek);
        dayofweek_adater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayofweek_spinner.setAdapter(dayofweek_adater);


        //set selected item listner
        repetation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    //Once is selected
                    dayofweek_spinner.setVisibility(View.GONE);

                } else {
                    //weekly is selected
                    dayofweek_spinner.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void create_notice() {
        if (!check_validation_subject() | !check_validation_description() | !check_validation_category() | !check_validation_tag()) {
            return;
        }
        if (repetation_spinner == null || repetation_spinner.getSelectedItem() == null || repetation_spinner.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Repetation", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (repetation_spinner.getSelectedItem().toString().toLowerCase().equals("weekly")) {
            if (dayofweek_spinner == null || dayofweek_spinner.getSelectedItem() == null || dayofweek_spinner.getSelectedItem().toString().isEmpty()) {
                Toasty.error(getApplicationContext(), "Select Day of Week", Toasty.LENGTH_SHORT).show();
                return;
            }else{
                str_dayOfWeek = dayofweek_spinner.getSelectedItem().toString();
            }
        }
        str_subject = subject.getEditText().getText().toString().trim();
        str_category = category.getEditText().getText().toString();
        str_description = description.getEditText().getText().toString();
        str_tag = tag.getEditText().getText().toString();
        str_repetation = repetation_spinner.getSelectedItem().toString();

        if (eventDate !=null && eventDate.getEditText() !=null && !eventDate.getEditText().getText().toString().isEmpty()){
            str_eventDate = eventDate.getEditText().getText().toString();
        }
        if (eventTime !=null && eventTime.getEditText() !=null && !eventTime.getEditText().getText().toString().isEmpty()){
            str_eventTime = eventTime.getEditText().getText().toString();
        }
        if (link !=null && link.getEditText() !=null && !link.getEditText().getText().toString().isEmpty()){
            str_link = link.getEditText().getText().toString();
        }

        //store in the database
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(NOTICE_TABLE_NAME);
        String key = db.push().getKey();

        NoticeInfo notice = new NoticeInfo(key,"Visible",str_subject,str_category,str_description,str_eventDate,str_eventTime,
                str_tag,userName,get_current_Date(),str_repetation,"","",str_dayOfWeek,str_link);

        db.child(key).setValue(notice);
        FirebaseFirestore.getInstance().collection(NOTICE_TABLE_NAME).document(key).set(notice);
        Toasty.success(getApplicationContext(),"Notice Created",Toasty.LENGTH_LONG).show();
        // clear all text fields
        clear_all_fields();
    }

    private Boolean check_validation_subject() {

        if (subject == null || subject.getEditText() == null || subject.getEditText().getText().toString().isEmpty()) {
            subject.setError("Field cannot be empty");
            return false;
        } else {
            subject.setError(null);
            subject.setErrorEnabled(false);
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

    private Boolean check_validation_tag() {

        if (tag == null || tag.getEditText() == null || tag.getEditText().getText().toString().isEmpty()) {
            tag.setError("Field cannot be empty");
            return false;
        } else {
            tag.setError(null);
            tag.setErrorEnabled(false);
            return true;
        }
    }
    private void clear_all_fields(){
        subject.getEditText().getText().clear();
        category.getEditText().getText().clear();
        description.getEditText().getText().clear();
        eventDate.getEditText().getText().clear();
        eventTime.getEditText().getText().clear();
        tag.getEditText().getText().clear();
        link.getEditText().getText().clear();
    }
    private String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
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
            userstatus = sharedPreferences.getString("status","");
            //userPassword = sharedPreferences.getString("password","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
}
