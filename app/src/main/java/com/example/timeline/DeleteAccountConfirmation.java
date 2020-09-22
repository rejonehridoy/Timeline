package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeleteAccountConfirmation extends AppCompatActivity implements View.OnClickListener {
    private Button btnDelete,btnCancel;
    private TextInputLayout reason;
    private ProgressBar progressBar;
    private SQLiteDatabase mDatabase;
    private String uid,userName,userPassword,userEmail,userFullName,userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_confirmation);

        btnDelete = findViewById(R.id.DAC_btnDelete);
        btnCancel = findViewById(R.id.DAC_btnCancel);
        reason = findViewById(R.id.DAC_reason);
        progressBar = findViewById(R.id.DAC_progressBar);
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        get_user_info();

        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DAC_btnCancel:
                this.finish();
                break;
            case R.id.DAC_btnDelete:
                delete_account();
                break;
        }
    }
    private void delete_account(){
        if (!check_validation_reason()){
            return;
        }
        if (!check_internet_connection()){
            Toasty.error(getApplicationContext(),"Check your internet connection",Toasty.LENGTH_SHORT).show();
            return;
        }

        //first sign in up in the firebase authentication user
        //1. delete user from firebase authentication
        //2. delete user from firebase "UserDetails" table
        //3. store user report
        //4. delete activities of user from firebase such as MembersProject
        //5. delete activities from Activity
        //6. delete passwormanager
        //7. delete notes
        //8. delete values from sharedpreference

        //sign in from firebase authetication
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,encrypt_password(userPassword)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //1
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //deleted from authentication
                                //2
                                FirebaseFirestore.getInstance().collection("UserDetails").document(userName).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseDatabase.getInstance().getReference().child("UserDetails").child(userName).removeValue();
                                                //3
                                                UserDeleteReport report = new UserDeleteReport(uid,userName,get_current_Date(),userEmail,userFullName,userPhone,reason.getEditText().getText().toString());
                                                FirebaseFirestore.getInstance().collection("UserDeleteReport").document(userName).set(report);
                                                FirebaseDatabase.getInstance().getReference("UserDeleteReport").child(userName).setValue(report);

                                                //4
                                                FirebaseFirestore.getInstance().collection("MembersProject").whereEqualTo("memberUserName",userName).get()
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                                                                    String mid = snapshot.getString("mid");
                                                                    FirebaseFirestore.getInstance().collection("MembersProject").document(mid).delete()
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                }
                                                                            });
                                                                    FirebaseDatabase.getInstance().getReference().child("MembersProject").child(mid).removeValue();
                                                                }
                                                            }
                                                        });

                                                //5
                                                mDatabase.delete(TimelineDB.PasswordManager.TABLE_NAME, TimelineDB.PasswordManager.COLUMN_USERNAME + " LIKE '" + userName+"';", null);
                                                mDatabase.delete(TimelineDB.Activity.TABLE_NAME, TimelineDB.Activity.COLUMN_USERNAME + " LIKE '" + userName+"';", null);
                                                mDatabase.delete(TimelineDB.Notes.TABLE_NAME, TimelineDB.Notes.COLUMN_USERNAME + " LIKE '" + userName+"';", null);
                                                //mDatabase.delete(TimelineDB.UserDetails.TABLE_NAME, TimelineDB.UserDetails.COLUMN_USERNAME + " LIKE '" + userName+"';", null);

                                                //6

                                                SharedPreferences.Editor editor;
                                                SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                                                editor = sharedPreferences.edit();
                                                editor.putBoolean("isLoggedin",false);
                                                editor.putBoolean("rememberMe",false);
                                                editor.putString("userName","");
                                                editor.putString("uid","");
                                                editor.putString("fullName","");
                                                editor.putString("email","");
                                                editor.putString("password","");
                                                editor.putString("phone","");
                                                editor.putString("gender","");
                                                editor.putString("dateOfBirth","");
                                                editor.putString("avaterCode","");
                                                editor.putString("status","");
                                                editor.putString("userType","");
                                                editor.putString("createdDate","");
                                                editor.putString("recoveryQuestion","");
                                                editor.putString("recoveryAns","");
                                                editor.putString("tag","");
                                                editor.apply();

                                                Toasty.success(getApplicationContext(),"Account Deleted",Toasty.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	//clear all background activity
                                                //DeleteAccountConfirmation.this.finish();
                                                DeleteAccountConfirmation.this.finishAffinity();
                                                startActivity(intent);
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        });
                            }else{
                                Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
    private String encrypt_password(String password){
        try {
            return EncryptionDecryption.SecurityEncryption.encrypt(password, EncryptionDecryption.SecurityEncryption.KEY_PASSWORD);
        }catch (Exception e){
            return password;
        }
    }

    private Boolean check_validation_reason() {

        if (reason == null || reason.getEditText() == null || reason.getEditText().getText().toString().isEmpty()) {
            reason.setError("Field cannot be empty");
            return false;
        } else {
            reason.setError(null);
            reason.setErrorEnabled(false);
            return true;
        }
    }

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userPassword = sharedPreferences.getString("password","");
            userPhone = sharedPreferences.getString("phone","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in", Toasty.LENGTH_LONG).show();
        }
    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

    public boolean check_internet_connection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                //Toast.makeText(this,"Wifi Connected",Toast.LENGTH_LONG).show();

            }else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                //Toast.makeText(this,"Mobile Network Connected",Toast.LENGTH_LONG).show();
            }
            return true;
        }else{
            //Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
