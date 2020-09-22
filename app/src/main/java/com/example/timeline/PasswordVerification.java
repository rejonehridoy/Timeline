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

public class PasswordVerification extends AppCompatActivity {
    private TextInputLayout layout_password;
    private Button btn_submit;
    private int false_entry_count = 0;
    private int destination_activity_number;
    private String uid,userName,userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_verification);

        layout_password = findViewById(R.id.PV_password);
        btn_submit = findViewById(R.id.PV_btSubmit);

        Intent intent = getIntent();
        destination_activity_number = intent.getIntExtra("Aid",0);

        get_user_info();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check_empty_password_field()){
                    return;
                }else{
                    //match password with sharedprefernce password
                    if (verify_password()){
                        //verification successfull,now go to next activity
                        transfer_activity();
                    }else{
                        false_entry_count++;
                        if (false_entry_count >2){
                            Toasty.error(PasswordVerification.this,"Too many wrong attemps",Toasty.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            }
        });
    }

    private Boolean verify_password(){
        //this method will verify password ,if matched then true will return otherwise false
        if (layout_password.getEditText().getText().toString().equals(userPassword)){
            return true;
        }else {
            Toasty.error(getApplicationContext(),"Wrong Password",Toasty.LENGTH_SHORT).show();
            return false;
        }

    }


    private Boolean check_empty_password_field() {

        String val = layout_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            layout_password.setError("Field cannot be empty");
            return false;
        } else {
            layout_password.setError(null);
            layout_password.setErrorEnabled(false);
            return true;
        }

    }

    private void transfer_activity(){
        if (destination_activity_number == 1){          // 1 for Password Manager
            Intent PasswordManagerIntent = new Intent(PasswordVerification.this,PasswordManager.class);
            this.finish();
            startActivity(PasswordManagerIntent);
        }else if (destination_activity_number == 2){
            Intent ProfileEditIntent = new Intent(PasswordVerification.this,ProfileEdit.class);
            this.finish();
            startActivity(ProfileEditIntent);
        }else if (destination_activity_number ==3){
            Intent deleteAccountIntent = new Intent(PasswordVerification.this,DeleteAccountConfirmation.class);
            this.finish();
            startActivity(deleteAccountIntent);
        }
    }
    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userPassword = sharedPreferences.getString("password","");


        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
}
