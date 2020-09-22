package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {
    private Button btn_userDetails,btn_create_notice,btn_user_home,btn_project_details,btn_app_releases,btn_user_feedback,btn_account_delete_report;
    private String uid,userName,userType,userstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btn_create_notice = findViewById(R.id.AD_notice_create);
        btn_project_details = findViewById(R.id.AD_project_details);
        btn_user_home = findViewById(R.id.AD_user_home);
        btn_userDetails = findViewById(R.id.AD_user_details);
        btn_app_releases = findViewById(R.id.AD_app_releases);
        btn_user_feedback = findViewById(R.id.AD_user_feedback);
        btn_account_delete_report = findViewById(R.id.AD_user_delete_report);

        get_user_info();
        set_privacy();

        btn_userDetails.setOnClickListener(this);
        btn_app_releases.setOnClickListener(this);
        btn_user_feedback.setOnClickListener(this);
        btn_account_delete_report.setOnClickListener(this);
        btn_create_notice.setOnClickListener(this);
        btn_user_home.setOnClickListener(this);
        btn_project_details.setOnClickListener(this);
    }
    private void set_privacy(){
        if (userType != null && !userType.isEmpty() && !userType.toLowerCase().equals("admin")){
            btn_project_details.setVisibility(View.GONE);
            btn_userDetails.setVisibility(View.GONE);
            btn_app_releases.setVisibility(View.GONE);
            btn_account_delete_report.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AD_notice_create:
                Intent adminNotice = new Intent(AdminDashboard.this,AdminNotice.class);
                startActivity(adminNotice);

                break;
            case R.id.AD_project_details:
                startActivity(new Intent(AdminDashboard.this,AdminProjects.class));
                break;
            case R.id.AD_user_details:
                startActivity(new Intent(AdminDashboard.this,AdminUserDetails.class));
                break;
            case R.id.AD_user_home:
                Intent home = new Intent(AdminDashboard.this,Home.class);
                startActivity(home);
                break;

            case R.id.AD_app_releases:
                startActivity(new Intent(AdminDashboard.this,AdminAppReleases.class));
                break;
            case R.id.AD_user_feedback:
                startActivity(new Intent(AdminDashboard.this,AdminFeedback.class));
                break;
            case R.id.AD_user_delete_report:
                startActivity(new Intent(AdminDashboard.this,AdminDeleteUser.class));
                break;
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

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in", Toasty.LENGTH_LONG).show();
        }
    }
}
