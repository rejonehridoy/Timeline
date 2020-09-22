package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class AdminLogin extends AppCompatActivity {
    private Button btnLogin;
    private TextInputLayout username,password;
    private String uid,userName,userType,userstatus,userPassword,userEmail,userFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        username = findViewById(R.id.AL_username);
        password = findViewById(R.id.AL_password);
        btnLogin = findViewById(R.id.AL_btnLogin);

        get_user_info();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_login();

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
            userPassword = sharedPreferences.getString("password","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }

    private void check_login(){
        if (!check_validation(username) | !check_validation(password)){
            return;
        }
        if (!username.getEditText().getText().toString().equals(userName) || !password.getEditText().getText().toString().equals(userPassword) ||
                userType.toLowerCase().equals("user")){
            Toasty.error(getApplicationContext(),"Wrong Access",Toasty.LENGTH_SHORT).show();
            return;
        }
        Toasty.success(getApplicationContext(),"Access Successful",Toasty.LENGTH_SHORT).show();
        Intent dashboardIntent = new Intent(AdminLogin.this,AdminDashboard.class);
        AdminLogin.this.finish();
        startActivity(dashboardIntent);
    }
    private Boolean check_validation(TextInputLayout field) {

        if (field == null || field.getEditText() == null || field.getEditText().getText().toString().isEmpty()) {
            field.setError("Field cannot be empty");
            return false;
        } else {
            field.setError(null);
            field.setErrorEnabled(false);
            return true;
        }
    }
}
