package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileEdit extends AppCompatActivity {

    private Spinner gender_spinner, recoveryQuestion, avater_spinner;
    private TextInputLayout fullName, phone, dateOfBirth, recoveryAns, newPassword, retypePassword;
    private ImageView avater_iamge;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private String uid, userName, userEmail, userType, userstatus, userFullName, userPassword, userPhone, userGender, userDateOfBirth, userAvaterCode, userQuestion, userAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        fullName = findViewById(R.id.EP_fullname);
        phone = findViewById(R.id.EP_phone);
        dateOfBirth = findViewById(R.id.EP_dateOfBirth);
        recoveryAns = findViewById(R.id.EP_answer);
        newPassword = findViewById(R.id.EP_new_password);
        retypePassword = findViewById(R.id.EP_retype_password);
        btnSubmit = findViewById(R.id.EP_btnSubmit);
        progressBar = findViewById(R.id.EP_progessbar);

        gender_spinner = findViewById(R.id.EP_gender_spinner);
        recoveryQuestion = findViewById(R.id.EP_recoveryQuestion_spinner);
        avater_spinner = findViewById(R.id.EP_avater_spinner);
        avater_iamge = findViewById(R.id.EP_image_avater);

        get_user_data();
        set_spinner_value();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update profile
                update_profile();
            }
        });
    }

    private void update_profile() {
        if (!check_validation_fullName()) {
            return;
        }
        if (avater_spinner == null || avater_spinner.getSelectedItem() == null || avater_spinner.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Avater", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (recoveryQuestion == null || recoveryQuestion.getSelectedItem() == null || recoveryQuestion.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Recovery Question", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (!check_validation_recoveryAns()) {
            return;
        }
        if (!retypePassword.getEditText().getText().toString().isEmpty() && newPassword.getEditText().getText().toString().isEmpty()) {
            retypePassword.setError("Wrong Input,New Password Box is empty");
            return;
        }
        if (newPassword.getEditText() != null && !newPassword.getEditText().getText().toString().isEmpty()) {
            if (!validation_Newpassword()) {
                return;
            }
            if (!retypePassword.getEditText().getText().toString().equals(newPassword.getEditText().getText().toString())) {
                retypePassword.setError("Password didn't match");
                return;
            } else {
                retypePassword.setError(null);
                retypePassword.setErrorEnabled(false);
                // now authentication process should be on

                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, encrypt_password(userPassword)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(encrypt_password(newPassword.getEditText().getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //save in the database
                                        update_data_in_firebase(true);
                                        update_data_in_sharedpreference(true);
                                        Toasty.success(getApplicationContext(), "Updated", Toasty.LENGTH_SHORT).show();
                                        ProfileEdit.this.finish();
                                    } else {
                                        Toasty.error(getApplicationContext(), task.getException().getMessage(), Toasty.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toasty.error(getApplicationContext(), task.getException().getMessage(), Toasty.LENGTH_SHORT).show();
                        }
                    }
                });


                progressBar.setVisibility(View.GONE);
                return;
            }
        }
        // no need to update password,only update info

        update_data_in_firebase(false);
        update_data_in_sharedpreference(false);

        Toasty.success(getApplicationContext(), "Updated", Toasty.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        ProfileEdit.this.finish();


    }

    private void update_data_in_firebase(Boolean isPassword) {       //isPassword will ensure that whether pass will be update or not
        //Firestore
        DocumentReference document = FirebaseFirestore.getInstance().collection("UserDetails").document(userName);
        document.update("fullName", fullName.getEditText().getText().toString().trim());
        document.update("avaterCode", avater_spinner.getSelectedItem().toString());
        document.update("recoveryQuestion", encrypt_recovery_info(recoveryQuestion.getSelectedItem().toString()));
        document.update("recoveryAns", encrypt_recovery_info(recoveryAns.getEditText().getText().toString()));
        document.update("phone", phone.getEditText().getText().toString());
        document.update("dateOfBirth", dateOfBirth.getEditText().getText().toString());
        document.update("gender", gender_spinner.getSelectedItem().toString());

        //Realtime database
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("UserDetails");
        db.child(userName).child("fullName").setValue(fullName.getEditText().getText().toString());
        db.child(userName).child("avaterCode").setValue(avater_spinner.getSelectedItem().toString());
        db.child(userName).child("recoveryQuestion").setValue(encrypt_recovery_info(recoveryQuestion.getSelectedItem().toString()));
        db.child(userName).child("recoveryAns").setValue(encrypt_recovery_info(recoveryAns.getEditText().getText().toString()));
        db.child(userName).child("phone").setValue(phone.getEditText().getText().toString());
        db.child(userName).child("dateOfBirth").setValue(dateOfBirth.getEditText().getText().toString());
        db.child(userName).child("gender").setValue(gender_spinner.getSelectedItem().toString());
        if (isPassword) {
            document.update("password", encrypt_password(newPassword.getEditText().getText().toString()));
            db.child(userName).child("password").setValue(encrypt_password(newPassword.getEditText().getText().toString()));
        }
    }

    private void update_data_in_sharedpreference(Boolean isPassword) {
        SharedPreferences.Editor editor;
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString("fullName", fullName.getEditText().getText().toString());
        editor.putString("phone", phone.getEditText().getText().toString());
        editor.putString("gender", gender_spinner.getSelectedItem().toString());
        editor.putString("dateOfBirth", dateOfBirth.getEditText().getText().toString());
        editor.putString("avaterCode", avater_spinner.getSelectedItem().toString());
        editor.putString("recoveryQuestion", recoveryQuestion.getSelectedItem().toString());
        editor.putString("recoveryAns", recoveryAns.getEditText().getText().toString());
        if (isPassword) {
            editor.putString("password", newPassword.getEditText().getText().toString());
        }
        editor.apply();
    }

    private String encrypt_recovery_info(String info){
        try {
            return EncryptionDecryption.SecurityEncryption.encrypt(info, EncryptionDecryption.SecurityEncryption.KEY_RECOVERY_INFO);
        }catch (Exception e){
            return info;
        }
    }

    private String encrypt_password(String password){
        try {
            return EncryptionDecryption.SecurityEncryption.encrypt(password, EncryptionDecryption.SecurityEncryption.KEY_PASSWORD);
        }catch (Exception e){
            return password;
        }
    }

    private void get_user_data() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin", false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid", "");
            userName = sharedPreferences.getString("userName", "");
            userType = sharedPreferences.getString("userType", "");
            userstatus = sharedPreferences.getString("status", "");
            userPassword = sharedPreferences.getString("password", "");
            userEmail = sharedPreferences.getString("email", "");
            userFullName = sharedPreferences.getString("fullName", "");
            userPhone = sharedPreferences.getString("phone", "");
            userGender = sharedPreferences.getString("gender", "");
            userDateOfBirth = sharedPreferences.getString("dateOfBirth", "");
            userAvaterCode = sharedPreferences.getString("avaterCode", "");
            userQuestion = sharedPreferences.getString("recoveryQuestion", "");
            userAns = sharedPreferences.getString("recoveryAns", "");

            // Setup value in textboxes and spinner
            fullName.getEditText().setText(userFullName);
            phone.getEditText().setText(userPhone);
            dateOfBirth.getEditText().setText(userDateOfBirth);
            recoveryAns.getEditText().setText(userAns);
        } else {
            this.finish();
            Toasty.error(getApplicationContext(), "User didn't log in", Toasty.LENGTH_LONG).show();
        }

    }

    private void set_spinner_value() {
        //set avater spinner
        final String[] avater = getResources().getStringArray(R.array.EP_avater_string);

        final ArrayAdapter<String> avater_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, avater);
        avater_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        avater_spinner.setAdapter(avater_adapter);
        // set selected item
        // set the avater image into image view
        if (userAvaterCode != null && !userAvaterCode.isEmpty()) {
            int avaterspinnerPosition = avater_adapter.getPosition(userAvaterCode);       //original value will be here
            avater_spinner.setSelection(avaterspinnerPosition);
            avater_iamge.setImageResource(convert_avaterName_to_int(userAvaterCode));
        } else {
            int avaterspinnerPosition = avater_adapter.getPosition(convert_int_to_avaterName(R.drawable.profile_avater_boy));       //original value will be here
            avater_spinner.setSelection(avaterspinnerPosition);
            avater_iamge.setImageResource(R.drawable.profile_avater_boy);       //default image if not found

        }


        // set status item listner
        avater_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_avater = avater_spinner.getSelectedItem().toString();
                avater_iamge.setImageResource(convert_avaterName_to_int(selected_avater));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //set gender spinner
        String[] gender = getResources().getStringArray(R.array.EP_gender_string);

        ArrayAdapter<String> gender_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_adapter);
        // set selected item
        int spinnerPosition = gender_adapter.getPosition(userGender);       //original value will be here
        gender_spinner.setSelection(spinnerPosition);

        // set Recovery question spinner
        String[] questions = getResources().getStringArray(R.array.EP_recoveryQuestion_spinner);

        ArrayAdapter<String> question_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questions);
        question_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recoveryQuestion.setAdapter(question_adapter);
        // set selected item
        int spinnerPosition2 = question_adapter.getPosition(userQuestion);   //original value will be here
        recoveryQuestion.setSelection(spinnerPosition2);
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

    private Boolean check_validation_recoveryAns() {

        if (recoveryAns == null || recoveryAns.getEditText() == null || recoveryAns.getEditText().getText().toString().isEmpty()) {
            recoveryAns.setError("Field cannot be empty");
            return false;
        } else {
            recoveryAns.setError(null);
            recoveryAns.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_fullName() {

        if (fullName == null || fullName.getEditText() == null || fullName.getEditText().getText().toString().isEmpty()) {
            fullName.setError("Field cannot be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validation_Newpassword() {
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
            return true;
        }
    }

}
