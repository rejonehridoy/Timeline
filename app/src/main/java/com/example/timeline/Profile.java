package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Profile extends AppCompatActivity {

    private TextView fullName,Username;
    private ImageView avater;
    private Button EditProfile;
    private TextView userType;
    private TextInputLayout email,phone,gender,dateOfBirth,createdDate,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName= findViewById(R.id.P_FullName);
        EditProfile = findViewById(R.id.P_submit);
        userType = findViewById(R.id.P_userType);
        Username = findViewById(R.id.P_userName);
        email = findViewById(R.id.P_Email);
        phone = findViewById(R.id.P_phone);
        gender = findViewById(R.id.P_gender);
        dateOfBirth = findViewById(R.id.P_dateOfBirth);
        createdDate = findViewById(R.id.P_AccountCreated);
        status = findViewById(R.id.P_Status);
        avater = findViewById(R.id.P_avater);

        get_data_from_sharedpreference();



        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the setting is set for verify password for password manager protection
                // for this first have to check sharedpreference variable
                Intent verifyPasswordIntent = new Intent(Profile.this,PasswordVerification.class);
                verifyPasswordIntent.putExtra("Aid",2);
                startActivity(verifyPasswordIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_data_from_sharedpreference();
    }

    private void get_data_from_sharedpreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            String uid = sharedPreferences.getString("uid","");
            String userName = sharedPreferences.getString("userName","");
            String Type = sharedPreferences.getString("userType","");
            String userstatus = sharedPreferences.getString("status","");
            //userPassword = sharedPreferences.getString("password","");
            String userEmail = sharedPreferences.getString("email","");
            String userFullName = sharedPreferences.getString("fullName","");
            String userPhone = sharedPreferences.getString("phone","");
            String userGender = sharedPreferences.getString("gender","");
            String userCreated = sharedPreferences.getString("createdDate","");
            String userdateofBirth = sharedPreferences.getString("dateOfBirth","");
            String userAvater = sharedPreferences.getString("avaterCode","");

            //set data into textbox
            if (!userstatus.isEmpty() && userstatus.toLowerCase().equals("verified")){
                fullName.setText(userFullName+" ");
                // for verified user
                fullName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified_user_black_24dp, 0);
            }else{
                fullName.setText(userFullName);
            }
            Username.setText(userName);
            userType.setText(Type);
            email.getEditText().setText(userEmail);
            if (!userPhone.isEmpty()){
                phone.getEditText().setText(userPhone);
            }else{
                phone.getEditText().setText("N/A");
            }

            if (!userstatus.isEmpty()){
                status.getEditText().setText(userstatus);
            }else{
                status.getEditText().setText("N/A");
            }
            if (!userdateofBirth.isEmpty()){
                dateOfBirth.getEditText().setText(userdateofBirth);
            }else{
                dateOfBirth.getEditText().setText("N/A");
            }

            if (!userAvater.isEmpty()){
                avater.setImageResource(convert_avaterName_to_int(userAvater));
            }
            createdDate.getEditText().setText(userCreated);

            gender.getEditText().setText(userGender);

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
    private int convert_avaterName_to_int(String name) {
        if (name.equals("Boy 1")) {
            return R.drawable.profile_avater_boy;
        } else if (name.equals("Boy 2")) {
            return R.drawable.profile_avater_boy2;
        } else if (name.equals("Girl 1")) {
            return R.drawable.profile_avater_girl;
        } else if (name.equals("Girl 2")) {
            return R.drawable.profile_avater_girl2;
        } else if (name.equals("Hacker")) {
            return R.drawable.profile_avater_hacker;
        } else if (name.equals("Lifeguard")) {
            return R.drawable.profile_avater_lifeguard;
        } else if (name.equals("Male")) {
            return R.drawable.profile_avater_male;
        } else if (name.equals("Men")) {
            return R.drawable.profile_avater_men;
        } else if (name.equals("Women")) {
            return R.drawable.profile_avater_women;
        } else {
            return R.drawable.profile_avater_boy;
        }
    }

    public void onNameClick(View v){
        if (userType != null && !userType.getText().toString().isEmpty()){
            if (!userType.getText().toString().toLowerCase().equals("user")){
                Intent adminLogin = new Intent(Profile.this,AdminLogin.class);
                startActivity(adminLogin);
            }
        }
    }
}
