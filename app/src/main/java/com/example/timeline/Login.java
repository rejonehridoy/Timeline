package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import javax.annotation.Nullable;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin,btnForegetPassword,btnSignup;
    private TextInputLayout ETuserName,ETpassword;
    private CheckBox rememberMe;
    private long backPressedTime;
    private ProgressBar progressBar;
    private static final String LOGIN_TABLE_NAME = "UserDetails";
    private static final String APP_VERSION = "1.7.25";
    private static final String APP_VERSION_TYPE = "beta";          //beta/stable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        btnForegetPassword = findViewById(R.id.btn_forget_password);
        btnSignup = findViewById(R.id.btn_signup);
        ETuserName = findViewById(R.id.L_username);
        ETpassword = findViewById(R.id.L_password);
        rememberMe = findViewById(R.id.rememberme_checkbox);
        progressBar = findViewById(R.id.login_Progressbar);

        check_previous_login();

        //setup firestore offline data mode
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        //rememberMe.isChecked();

        app_update_version_info();

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForegetPassword.setOnClickListener(this);
    }

    private void app_update_version_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String version = sharedPreferences.getString("appVersion","");
        if (version.isEmpty()) {
            //write data into sharedprefrence
            SharedPreferences.Editor editor;
            SharedPreferences sharedPreferences1 = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            editor = sharedPreferences1.edit();
            editor.putString("appVersion",APP_VERSION);
            editor.putString("appVersionType",APP_VERSION_TYPE);
            editor.apply();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                login_user();
                break;
            case R.id.btn_signup:
                Intent signupIntent = new Intent(Login.this,Signup.class);
                startActivity(signupIntent);

                break;
            case R.id.btn_forget_password:
                Intent forgetPasswordIntent = new Intent(Login.this,RecoveryAccount.class);
                startActivity(forgetPasswordIntent);

                break;
        }
    }

    private void login_user(){
        if (!check_empty_userName() | !check_empty_password()){
            return;
        }
        // login credentials
        //1. Check sharedpreference data...if there is a enough data to login then login
        //2. check SQLite date if sharedpreference data is not found
        //3. Check Firebase authetication data if upper two option is false
        final String userName = ETuserName.getEditText().getText().toString().trim();
        final String password = ETpassword.getEditText().getText().toString();
        final Boolean remember = rememberMe.isChecked();

        progressBar.setVisibility(View.VISIBLE);
        // get data from sharepreference
        // first step from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("userName") && sharedPreferences.contains("password")) {

            String user = sharedPreferences.getString("userName", "");
            String pass = sharedPreferences.getString("password", "");
            String userStatus = sharedPreferences.getString("status", "");
            if (userName.equals(user) && pass.equals(password)){
                // successfully logged in from sharedpreference
                //Save data in the sharedpreference
                if (userStatus.toLowerCase().equals("disable")){
                    Toasty.error(getApplicationContext(),"Your account is disabled. please contact with admin",Toasty.LENGTH_LONG).show();

                    check_disable_account();
                    return;
                }
                SharedPreferences.Editor editor;
                SharedPreferences sharedPreferences2 = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                editor = sharedPreferences2.edit();
                editor.putBoolean("isLoggedin",true);
                editor.putBoolean("rememberMe",remember);
                editor.apply();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),Home.class));
                this.finish();

                return;
            }

        }

        //third step from firestore
        FirebaseFirestore.getInstance().collection(LOGIN_TABLE_NAME).document(userName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name = documentSnapshot.getString("userName");
                    String uid = documentSnapshot.getString("uid");
                    String fullName = documentSnapshot.getString("fullName");
                    String email = documentSnapshot.getString("email");
                    String pass = documentSnapshot.getString("password");
                    String phone = documentSnapshot.getString("phone");
                    String gender = documentSnapshot.getString("gender");
                    String dateOfBirth = documentSnapshot.getString("dateOfBirth");
                    String avaterCode = documentSnapshot.getString("avaterCode");
                    String status = documentSnapshot.getString("status");
                    String userType = documentSnapshot.getString("userType");
                    String createdDate = documentSnapshot.getString("createdDate");
                    String recoveryQuestion = decrypt_recovery_info(documentSnapshot.getString("recoveryQuestion"));
                    String recoveryAns = decrypt_recovery_info(documentSnapshot.getString("recoveryAns"));

                    if (userName.equals(name) && password.equals(decrypt_password(pass))){
                        progressBar.setVisibility(View.GONE);

                        if (status.toLowerCase().equals("disable")){
                            Toasty.error(getApplicationContext(),"Your account is disabled, please contact with admin",Toasty.LENGTH_LONG).show();
                        }else{
                            //Save data in the sharedpreference
                            String password = decrypt_password(pass);
                            SharedPreferences.Editor editor;
                            SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedin",true);
                            editor.putBoolean("rememberMe",remember);
                            editor.putString("uid",uid);
                            editor.putString("userName",name);
                            editor.putString("fullName",fullName);
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.putString("phone",phone);
                            editor.putString("gender",gender);
                            editor.putString("dateOfBirth",dateOfBirth);
                            editor.putString("avaterCode",avaterCode);
                            editor.putString("status",status);
                            editor.putString("userType",userType);
                            editor.putString("createdDate",createdDate);
                            editor.putString("recoveryQuestion",recoveryQuestion);
                            editor.putString("recoveryAns",recoveryAns);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            Login.this.finish();
                        }



                    }else{
                        Toasty.error(getApplicationContext(),"Wrong Username or Password",Toasty.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    Toasty.error(getApplicationContext(),"Wrong Username or Password",Toasty.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });



    }
    private String decrypt_password(String pass){
        try {
            return EncryptionDecryption.SecurityEncryption.decrypt(pass, EncryptionDecryption.SecurityEncryption.KEY_PASSWORD);
        }catch (Exception e){
            return pass;
        }

    }
    private String decrypt_recovery_info(String info){
        try {
            return EncryptionDecryption.SecurityEncryption.decrypt(info, EncryptionDecryption.SecurityEncryption.KEY_RECOVERY_INFO);
        }catch (Exception e){
            return info;
        }

    }

    private Boolean check_empty_userName(){
        String name = ETuserName.getEditText().getText().toString();
        if (name.isEmpty()) {
            ETuserName.setError("Field cannot be empty");
            return false;
        }
        else {
            ETuserName.setError(null);
            ETuserName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_empty_password(){
        String password = ETpassword.getEditText().getText().toString();
        if (password.isEmpty()) {
            ETpassword.setError("Field cannot be empty");
            return false;
        }
        else {
            ETpassword.setError(null);
            ETpassword.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }


    private void check_previous_login(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("rememberMe")) {
            Boolean user_login = sharedPreferences.getBoolean("rememberMe", false);
            if (user_login){
                startActivity(new Intent(getApplicationContext(),Home.class));
                finish();
            }
        }
    }

    private void check_disable_account(){
        FirebaseFirestore.getInstance().collection("UserDetails").document(ETuserName.getEditText().getText().toString()).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return;
                }
                String status = documentSnapshot.getString("status");
                if (!status.toLowerCase().equals("disable")){
                    SharedPreferences.Editor editor;
                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("status",status);
                    editor.apply();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //This is a method of Authentication where email and password will required
        /*
        String email = ETuserName.getEditText().getText().toString().trim();
        String password = ETpassword.getEditText().getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toasty.success(getApplicationContext(),"Successful Login with User Email: "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+ " Username : "+
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),Toasty.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_LONG).show();

                }
            }
        });*/

}
