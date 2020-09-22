package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout etFullname, etUsername, etEmail,etPassword, etPhone,etRecoveryAns;
    private TextInputEditText iFullname, iUsername, iEmail,iPassword, iPhone,iRecoveryAns;
    private Button btnSubmit,btnLogin;
    private Spinner question_spinner,gender_spinner;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFullname = findViewById(R.id.SU_Fullname);
        etUsername = findViewById(R.id.SU_userName);
        etEmail = findViewById(R.id.SU_Email);
        etPassword = findViewById(R.id.SU_Password);
        etPhone = findViewById(R.id.SU_phone);
        etRecoveryAns = findViewById(R.id.SU_recoveryAns);
        iFullname = findViewById(R.id.SU_Fullname_input);
        iUsername = findViewById(R.id.SU_userName_input);
        iEmail = findViewById(R.id.SU_Email_input);
        iPassword = findViewById(R.id.SU_Password_input);
        iPhone = findViewById(R.id.SU_phone_input);
        iRecoveryAns = findViewById(R.id.SU_ans_input);
        btnSubmit =findViewById(R.id.SU_btnsubmit);
        btnLogin = findViewById(R.id.SU_btnLogin);
        question_spinner = findViewById(R.id.SU_question);
        gender_spinner = findViewById(R.id.SU_gender);
        progressBar = findViewById(R.id.SU_progressBar);

        set_recovery_question();
        set_gender();

        create_instruction_alertDialog();

        //setup firestore offline data mode
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        btnLogin.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void set_recovery_question(){
        List<String> question = new ArrayList<String>();
        question.add("Select Password Recovery Question");
        question.add("What Is your favorite book?");
        question.add("What is the name of the road you grew up on?");
        question.add("What is your mother's maiden name?");
        question.add("What is your favorite food?");
        question.add("What was the name of your first pet?");
        question.add("What city were you born in?");
        question.add("Where did you go to high school/college?");
        question.add("Where did you meet your spouse?");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,question);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        question_spinner.setAdapter(adapter);
    }

    private void set_gender(){
        String[] gender = getResources().getStringArray(R.array.SU_gender_string);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SU_btnLogin:
                Intent login = new Intent(Signup.this,Login.class);
                startActivity(login);
                finish();
                break;
            case R.id.SU_btnsubmit:
                //Create_Firebase_Operation();
                Signup_user();

                break;
        }
    }

    private void Signup_user(){

        if (!validation_Email() | !validation_FullName() | !validation_userName() | !validation_password() |
        !validation_phone() | !validation_RecoveryAns()){
            return;
        }
        if (gender_spinner.getSelectedItem() == null || gender_spinner.getSelectedItem().toString().equals("Gender")){
            Toasty.error(getApplicationContext(),"Select a Gender",Toasty.LENGTH_SHORT).show();
            return;
        }
        if (question_spinner.getSelectedItem() == null || question_spinner.getSelectedItem().toString().equals("Select Password Recovery Question")){
            Toasty.error(this, "Select a recovery question", Toasty.LENGTH_SHORT).show();
            return;
        }


        FirebaseFirestore.getInstance().collection("UserDetails").document(etUsername.getEditText().getText().toString().trim()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    etUsername.setError("Username is already taken by other");
                }else{
                    etUsername.setError(null);
                    etUsername.setErrorEnabled(false);
                    //Store data in SQLite and cloud Firestore

                    //Create user in Firebase authentication and Firestore and Firebase Database
                    create_user_Firebase();

                }
            }
        });


    }

    private void create_user_Firebase(){
        final String username = etUsername.getEditText().getText().toString().trim();
        final String email = etEmail.getEditText().getText().toString().trim();
        final String fullName = etFullname.getEditText().getText().toString().trim();
        final String phone = etPhone.getEditText().getText().toString().trim();
        final String gender = gender_spinner.getSelectedItem().toString();
        final String password = encrypt_password(etPassword.getEditText().getText().toString());
        final String recoveryQuestion = encrypt_recovery_info(question_spinner.getSelectedItem().toString());
        final String ans = encrypt_recovery_info(etRecoveryAns.getEditText().getText().toString());


        // Creating instance of firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("UserDetails");
        databaseReference.keepSynced(true);

        // firebase auth
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        //initialize progress bar
        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //authentication successful

                    //creating object of UserDetails class
                    final UserDetails user = new UserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(),username,fullName,email,password,phone,gender,"",
                            convert_int_to_avaterName(R.drawable.profile_avater_boy),"Active","User",get_current_Date(),recoveryQuestion,ans,"",get_current_Date());

                    //update profile
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                // send Email Verification
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            //store data in the database
                                            //Real time Database
                                            databaseReference.child(username).setValue(user);

                                            // firestore
                                            FirebaseFirestore.getInstance().collection("UserDetails").document(username).set(user);

                                            Toasty.success(getApplicationContext(),"Account Created. Verify your email",Toasty.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            clear_all_text_input();
                                            Signup.this.finish();

                                        }else{
                                            Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });



                            }else{
                                Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


                }else{
                    Toasty.error(getApplicationContext(),task.getException().getMessage(),Toasty.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        btnSubmit.setEnabled(true);


    }
    private String encrypt_password(String password){
        try {
            return EncryptionDecryption.SecurityEncryption.encrypt(password, EncryptionDecryption.SecurityEncryption.KEY_PASSWORD);
        }catch (Exception e){
            return password;
        }
    }
    private String encrypt_recovery_info(String info){
        try {
            return EncryptionDecryption.SecurityEncryption.encrypt(info, EncryptionDecryption.SecurityEncryption.KEY_RECOVERY_INFO);
        }catch (Exception e){
            return info;
        }
    }

    private Boolean validation_FullName(){
        String name = etFullname.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            etFullname.setError("Field cannot be empty");
            return false;
        }
        else {
            etFullname.setError(null);
            etFullname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validation_userName(){
        String val = etUsername.getEditText().getText().toString().trim();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            etUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            etUsername.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            etUsername.setError("White Spaces are not allowed");
            return false;
        }
        else {
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

    private Boolean validation_phone(){
        String val = etPhone.getEditText().getText().toString();

        if (val.isEmpty()) {
            etPhone.setError("Field cannot be empty");
            return false;
        } else {
            etPhone.setError(null);
            etPhone.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validation_RecoveryAns(){
        String val = etRecoveryAns.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            etRecoveryAns.setError("Field cannot be empty");
            return false;
        }else if (val.length() >= 10) {
            etUsername.setError("Ans too long");
            return false;
        }
        else{
            etRecoveryAns.setError(null);
            etRecoveryAns.setErrorEnabled(false);
            return true;
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
        String val = etPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            etPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            etPassword.setError("Password is too weak");
            return false;
        } else {
            etPassword.setError(null);
            etPassword.setErrorEnabled(false);
            return true;
        }
    }

    private String convert_int_to_avaterName(int num) {
        if (num == R.drawable.profile_avater_boy) {
            return "Boy 1";
        } else if (num == R.drawable.profile_avater_boy2) {
            return "Boy 2";
        } else if (num == R.drawable.profile_avater_girl) {
            return "Girl 1";
        } else if (num == R.drawable.profile_avater_girl2) {
            return "Girl 2";
        } else if (num == R.drawable.profile_avater_hacker) {
            return "Hacker";
        } else if (num == R.drawable.profile_avater_lifeguard) {
            return "Lifeguard";
        } else if (num == R.drawable.profile_avater_male) {
            return "Male";
        } else if (num == R.drawable.profile_avater_men) {
            return "Men";
        } else if (num == R.drawable.profile_avater_women) {
            return "Women";
        } else {
            return "Boy 1";
        }
    }

    private void clear_all_text_input(){
        iUsername.setText("");
        iFullname.setText("");
        iRecoveryAns.setText("");
        iEmail.setText("");
        iPhone.setText("");
        iPassword.setText("");
    }

    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    private void create_instruction_alertDialog(){

            AlertDialog.Builder alertDialogBuilder;
            alertDialogBuilder = new AlertDialog.Builder(this)
                    .setTitle("Instruction")
                    .setMessage("Provide your real information so that you can verify yourself when you want to recover your account.\n\nYour email should be verified.In order " +
                            "to verify your email,you should click a link that will send automatically after creating of your account\n\nYour recovery question and ans should be " +
                            "private\n\nYour information will be end to end encypted before storing in the server")
                    //.setIcon(R.drawable.ic_delete_forever_red)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked
                            //update work completion info in request table
                           dialog.dismiss();


                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


    }
}
