package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdminDeleteUserDetails extends AppCompatActivity {
    private TextInputEditText uid,userName,fullName,email,date,phone,reason;
    private String str_uid,str_userName,str_fullName,str_email,str_date,str_phone,str_reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user_details);


        uid = findViewById(R.id.ADUD_uid);
        userName = findViewById(R.id.ADUD_userName);
        fullName = findViewById(R.id.ADUD_fullName);
        email = findViewById(R.id.ADUD_email);
        date = findViewById(R.id.ADUD_date);
        phone = findViewById(R.id.ADUD_phone);
        reason = findViewById(R.id.ADUD_reason);
        get_data_from_intent();
    }

    private void get_data_from_intent(){
        Intent intent = getIntent();
        str_uid = intent.getStringExtra("uid");
        str_userName = intent.getStringExtra("userName");
        str_fullName = intent.getStringExtra("fullName");
        str_email = intent.getStringExtra("email");
        str_date = intent.getStringExtra("date");
        str_phone = intent.getStringExtra("phone");
        str_reason = intent.getStringExtra("reason");

        uid.setText(str_uid);
        userName.setText(str_userName);
        fullName.setText(str_fullName);
        email.setText(str_email);
        date.setText(str_date);
        phone.setText(str_phone);
        reason.setText(str_reason);
    }
}
