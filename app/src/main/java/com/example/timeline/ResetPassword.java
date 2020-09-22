package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResetPassword extends AppCompatActivity {
    private TextView message;
    private TextInputLayout newPassword,retypePassword;
    private Button btnSubmit;
    private String str_userName,str_password,str_email,str_fullName,str_newPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        message = findViewById(R.id.RP_message);
        newPassword = findViewById(R.id.RP_password);
        retypePassword = findViewById(R.id.RP_RetypePasswprd);
        btnSubmit = findViewById(R.id.RP_btnSubmit);
        progressBar = findViewById(R.id.RP_progessbar);

        get_data_from_intent();
        message.setText("Hey "+str_userName+ ", enter your new password");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation_password()){
                    store_new_password_in_firebase();
                }
            }
        });
    }
    private void get_data_from_intent(){
        Intent intent = getIntent();
        str_email = intent.getStringExtra("email");
        str_fullName = intent.getStringExtra("fullName");
        str_password = intent.getStringExtra("password");
        str_userName = intent.getStringExtra("userName");
    }
    private void store_new_password_in_firebase(){
        //first login in Firebaseauth ,otherwise you can not update your password
        progressBar.setVisibility(View.VISIBLE);
        str_newPassword = newPassword.getEditText().getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(str_email,encrypt_password(str_password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //successfully logged in
                    //update data in Firebase authentication
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(encrypt_password(str_newPassword)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //now update password in the firebase
                                FirebaseFirestore.getInstance().collection("UserDetails").document(str_userName).update("password",encrypt_password(str_newPassword));
                                FirebaseFirestore.getInstance().collection("UserDetails").document(str_userName).update("recoveryDate",get_current_Date());
                                //now update in the Realtime database
                                FirebaseDatabase.getInstance().getReference().child("UserDetails").child(str_userName).child("password").setValue(encrypt_password(str_newPassword));
                                FirebaseDatabase.getInstance().getReference().child("UserDetails").child(str_userName).child("recoveryDate").setValue(get_current_Date());
                                Toasty.success(getApplicationContext(),"Password updated, Login with new password",Toasty.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                check_sharedpreference(str_newPassword);
                                ResetPassword.this.finish();
                            }else{
                                Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void check_sharedpreference(String newPassword){
        //this method will check the sharedpreference,if this user is already in sharedpreferance storage then password must be updated
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("email") && sharedPreferences.contains("userName")) {

            String userName = sharedPreferences.getString("userName","");
            String userEmail = sharedPreferences.getString("email","");

            if (userName.equals(str_userName) && userEmail.equals(str_email)){
                //update the password
                SharedPreferences.Editor editor;
                SharedPreferences sharedPreferences2 = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("password",newPassword);
                editor.apply();
            }

        }
    }
    private String encrypt_password(String password){
        try {
            return EncryptionDecryption.SecurityEncryption.encrypt(password, EncryptionDecryption.SecurityEncryption.KEY_PASSWORD);
        }catch (Exception e){
            return password;
        }
    }
    private Boolean validation_password(){
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";
        String val = newPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            newPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            newPassword.setError("Password is too weak");
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            String retypePass = retypePassword.getEditText().getText().toString();
            if (retypePass.isEmpty() || !retypePass.equals(val)){
                retypePassword.setError("Password mismatched");
                return false;
            }else{
                retypePassword.setError(null);
                retypePassword.setErrorEnabled(false);
                return true;
            }
        }
    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
