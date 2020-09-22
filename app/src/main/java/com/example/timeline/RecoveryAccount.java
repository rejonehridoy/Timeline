package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecoveryAccount extends AppCompatActivity implements View.OnClickListener {

    private Button btnSearchAccount,btnSubmit;
    private TextInputLayout etUsername,etEmail,etPhone,etRecoveryQuestion,etAnswer;
    private TextInputEditText etViewQuestion;
    private ProgressBar progressBar;
    private String str_email,str_userName,str_recoveryQuestion,str_recoveryAns,str_password,str_fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_account);

        btnSearchAccount = findViewById(R.id.RA_btnFindAccount);
        btnSubmit = findViewById(R.id.RA_btnSubmit);
        etUsername = findViewById(R.id.RA_Username);
        etEmail = findViewById(R.id.RA_Email);
        etRecoveryQuestion = findViewById(R.id.RA_Question);
        etAnswer = findViewById(R.id.RA_Answer);
        etViewQuestion = findViewById(R.id.RA_ETquestion);
        progressBar = findViewById(R.id.RA_progressBar);



        check_internet_connection();
        btnSubmit.setOnClickListener(this);
        btnSearchAccount.setOnClickListener(this);
    }

    private void fetch_data_from_firebase(){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("UserDetails")
                .whereEqualTo("userName",etUsername.getEditText().getText().toString().trim())
                .whereEqualTo("email",etEmail.getEditText().getText().toString())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){

                        str_email = snapshot.getString("email");
                        str_password = decrypt_password(snapshot.getString("password"));
                        str_recoveryAns = decrypt_recovery_info(snapshot.getString("recoveryAns"));
                        str_recoveryQuestion = decrypt_recovery_info(snapshot.getString("recoveryQuestion"));
                        str_userName = snapshot.getString("userName");
                        str_fullName = snapshot.getString("fullName");
                    }


                    //setVisibility on
                    etRecoveryQuestion.setVisibility(View.VISIBLE);
                    etAnswer.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSearchAccount.setVisibility(View.GONE);

                    etRecoveryQuestion.getEditText().setText(str_recoveryQuestion);
                    progressBar.setVisibility(View.GONE);


                }else{
                    Toasty.error(getApplicationContext(),"Wrong username or email",Toasty.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RA_btnFindAccount:
                // do work here
                if (!check_validation_username()  | !validation_Email()){
                    return;
                }
                if (check_internet_connection()){
                    //after successfully done visible Recovery question and ans Editbox and submit button
                    //fetch the data according to username,phone,email
                    fetch_data_from_firebase();

                }


                break;
            case R.id.RA_btnSubmit:
                // Do work here
                if (check_validation_recoveryAns()){
                    Intent resetPasswordIntent = new Intent(RecoveryAccount.this, ResetPassword.class);
                    resetPasswordIntent.putExtra("email",str_email);
                    resetPasswordIntent.putExtra("userName",str_userName);
                    resetPasswordIntent.putExtra("fullName",str_fullName);
                    resetPasswordIntent.putExtra("password",str_password);
                    startActivity(resetPasswordIntent);
                    RecoveryAccount.this.finish();
                }


                break;
        }
    }
    private String decrypt_recovery_info(String info){
        try {
            return EncryptionDecryption.SecurityEncryption.decrypt(info, EncryptionDecryption.SecurityEncryption.KEY_RECOVERY_INFO);
        }catch (Exception e){
            return info;
        }

    }
    private String decrypt_password(String pass){
        try {
            return EncryptionDecryption.SecurityEncryption.decrypt(pass, EncryptionDecryption.SecurityEncryption.KEY_PASSWORD);
        }catch (Exception e){
            return pass;
        }

    }

    private Boolean check_validation_username() {

        if (etUsername == null || etUsername.getEditText() == null || etUsername.getEditText().getText().toString().isEmpty()) {
            etUsername.setError("Field cannot be empty");
            return false;
        } else {
            etUsername.setError(null);
            etUsername.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_validation_recoveryAns() {

        if (etAnswer == null || etAnswer.getEditText() == null || etAnswer.getEditText().getText().toString().isEmpty()) {
            etAnswer.setError("Field cannot be empty");
            return false;
        } else if (!etAnswer.getEditText().getText().toString().equals(str_recoveryAns)){
            etAnswer.setError("Wrong Ans");
            return false;
        }else{
            etUsername.setError(null);
            etUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validation_Email(){
        String val = etEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            etEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            etEmail.setError("Invalid email address");
            return false;
        } else {
            etEmail.setError(null);
            etEmail.setErrorEnabled(false);
            return true;
        }
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
            Toasty.error(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
