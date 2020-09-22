package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminFeedbackDetails extends AppCompatActivity {
    private TextInputLayout id,subject,message,userName,fullName,email,appVersion,review,reply,date;
    private Button btnUpdate;
    private String str_id,str_subject,str_message,str_userName,str_fullName,str_email,str_appVersion,str_review,str_reply,str_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback_details);

        id = findViewById(R.id.AFD_id);
        subject = findViewById(R.id.AFD_subject);
        message = findViewById(R.id.AFD_message);
        userName = findViewById(R.id.AFD_userName);
        fullName = findViewById(R.id.AFD_fullname);
        email = findViewById(R.id.AFD_email);
        appVersion = findViewById(R.id.AFD_appVersion);
        review = findViewById(R.id.AFD_review);
        reply = findViewById(R.id.AFD_reply);
        date = findViewById(R.id.AFD_date);
        btnUpdate = findViewById(R.id.AFD_btnUpdate);

        get_info_from_intent();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update
                FirebaseFirestore.getInstance().collection("Feedback").document(str_id).update("reply",reply.getEditText().getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Feedback").child(str_id).child("reply").setValue(reply.getEditText().getText().toString());
                Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            }
        });
    }
    private void get_info_from_intent(){
        Intent intent = getIntent();
        str_id = intent.getStringExtra("id");
        str_appVersion = intent.getStringExtra("appVerion");
        str_subject = intent.getStringExtra("subject");
        str_userName = intent.getStringExtra("userName");
        str_message = intent.getStringExtra("message");
        str_email = intent.getStringExtra("email");
        str_review = intent.getStringExtra("review");
        str_fullName = intent.getStringExtra("fullName");
        str_date = intent.getStringExtra("date");
        str_reply = intent.getStringExtra("reply");

        id.getEditText().setText(str_id);
        appVersion.getEditText().setText(str_appVersion);
        subject.getEditText().setText(str_subject);
        userName.getEditText().setText(str_userName);
        message.getEditText().setText(str_message);
        email.getEditText().setText(str_email);
        review.getEditText().setText(str_review);
        fullName.getEditText().setText(str_fullName);
        date.getEditText().setText(convert_Date_format(str_date));
        reply.getEditText().setText(str_reply);
        reply.getEditText().setEnabled(true);
    }


    private String convert_Date_format(String DateTime){
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
