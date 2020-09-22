package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminUserDetailsEdit extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout lastVisit,userName,email,uid,phone,gender,dateOfBirth,password,accountCreated,recoveryQuestion,recoveryAns,recoveryDate;
    private SearchableSpinner userType_spinner,status_spinner;
    private TextView fullName;
    private ImageView avater_image;
    private Button btnPassword,btnQuestion,btnAns,btnUpdate;
    private String str_uid,str_userName,str_fullName,str_email,str_password,str_phone,str_gender,str_dateOfBirth,str_avaterCode,str_status,str_userType,str_createdDate,
            str_recoverQuestion,str_recoveryAns,str_recoveryDate,str_lastVisit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details_edit);

        lastVisit = findViewById(R.id.AUDE_lastVisit);
        userName = findViewById(R.id.AUDE_userName);
        email = findViewById(R.id.AUDE_email);
        uid = findViewById(R.id.AUDE_uid);
        phone = findViewById(R.id.AUDE_phone);
        gender = findViewById(R.id.AUDE_gender);
        dateOfBirth = findViewById(R.id.AUDE_dateOfBirth);
        password = findViewById(R.id.AUDE_password);
        userType_spinner = findViewById(R.id.AUDE_userType_spinner);
        status_spinner = findViewById(R.id.AUDE_status_spinner);
        accountCreated = findViewById(R.id.AUDE_createdDate);
        recoveryQuestion = findViewById(R.id.AUDE_recoveryQestion);
        recoveryAns = findViewById(R.id.AUDE_recoveryAns);
        recoveryDate = findViewById(R.id.AUDE_recoveryDate);
        fullName = findViewById(R.id.AUDE_fullName);
        avater_image = findViewById(R.id.AUDE_avater);
        btnPassword = findViewById(R.id.AUDE_btnPasswordDecrpt);
        btnQuestion = findViewById(R.id.AUDE_btnRecoveryQuestion);
        btnAns = findViewById(R.id.AUDE_btnRecoveryAns);
        btnUpdate = findViewById(R.id.AUDE_btnUpdate);


        get_data_from_intent();


        btnUpdate.setOnClickListener(this);
        btnPassword.setOnClickListener(this);
        btnQuestion.setOnClickListener(this);
        btnAns.setOnClickListener(this);
    }
    private void get_data_from_intent(){
        Intent intent = getIntent();
        str_uid = intent.getStringExtra("uid");
        str_userName = intent.getStringExtra("userName");
        str_fullName = intent.getStringExtra("fullName");
        str_email = intent.getStringExtra("email");
        str_password = intent.getStringExtra("password");
        str_phone = intent.getStringExtra("phone");
        str_gender = intent.getStringExtra("gender");
        str_dateOfBirth = intent.getStringExtra("dateOfBirth");
        str_avaterCode = intent.getStringExtra("avaterCode");
        str_status = intent.getStringExtra("status");
        str_userType = intent.getStringExtra("userType");
        str_createdDate = intent.getStringExtra("createdDate");
        str_recoverQuestion = intent.getStringExtra("recoveryQuestion");
        str_recoveryAns = intent.getStringExtra("recoveryAns");
        str_recoveryDate = intent.getStringExtra("recoveryDate");
        str_lastVisit = intent.getStringExtra("lastVisit");

        fullName.setText(str_fullName);
        uid.getEditText().setText(str_uid);
        userName.getEditText().setText(str_userName);
        email.getEditText().setText(str_email);
        password.getEditText().setText(str_password);
        phone.getEditText().setText(str_phone);
        gender.getEditText().setText(str_gender);
        dateOfBirth.getEditText().setText(str_dateOfBirth);
        avater_image.setImageResource(convert_avaterName_to_int(str_avaterCode));
        accountCreated.getEditText().setText(str_createdDate);
        recoveryQuestion.getEditText().setText(str_recoverQuestion);
        recoveryAns.getEditText().setText(str_recoveryAns);
        recoveryDate.getEditText().setText(str_recoveryDate);
        lastVisit.getEditText().setText(convert_Date_format(str_lastVisit));

        // status spinner
        String[] status = getResources().getStringArray(R.array.user_status_string);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(status_adapter);
        //set selected item of status spinner
        if (str_status != null && !str_status.isEmpty()){
            int position = status_adapter.getPosition(str_status);
            status_spinner.setSelection(position);
        }

        // type spinner
        String[] type = getResources().getStringArray(R.array.user_type_string);

        ArrayAdapter<String> type_adater = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,type);
        type_adater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType_spinner.setAdapter(type_adater);
        //set selected item of status spinner
        if (str_userType != null && !str_userType.isEmpty()){
            int position = type_adater.getPosition(str_userType);
            userType_spinner.setSelection(position);
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

    private String convert_Date_format(String DateTime){
        // 22-08-2020 Wed 05:34 PM
        if (!DateTime.isEmpty()){
            String localDate = DateTime.substring(0,10);
            String localTime = DateTime.substring(14,23);
            String day = DateTime.substring(10,14);
            if (localDate.equals(get_current_Date())){
                return "Today at "+localTime;
            }
            try {
                DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                DateFormat targetFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat targetFormat2 = new SimpleDateFormat("MMMM dd");
                DateFormat targetFormat3 = new SimpleDateFormat("MMMM dd, yyyy");
                Date date = originalFormat.parse(localDate);
                if (localDate.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                    return targetFormat2.format(date)+","+day+" at "+localTime;
                }else{
                    return targetFormat3.format(date)+","+day+" at "+localTime;
                }


            }catch (Exception e){
                Log.d("ERROR",e.getMessage());
            }
        }


        return DateTime;
    }

    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AUDE_btnPasswordDecrpt:
                password.getEditText().setText(decrypt_password(str_password));
                break;
            case R.id.AUDE_btnRecoveryQuestion:
                recoveryQuestion.getEditText().setText(decrypt_recovery_info(str_recoverQuestion));
                break;
            case R.id.AUDE_btnRecoveryAns:
                recoveryAns.getEditText().setText(decrypt_recovery_info(str_recoveryAns));
                break;
            case R.id.AUDE_btnUpdate:
                FirebaseFirestore.getInstance().collection("UserDetails").document(str_userName).update("status",status_spinner.getSelectedItem().toString());
                FirebaseFirestore.getInstance().collection("UserDetails").document(str_userName).update("userType",userType_spinner.getSelectedItem().toString());
                FirebaseDatabase.getInstance().getReference().child("UserDetails").child(str_userName).child("status").setValue(status_spinner.getSelectedItem().toString());
                FirebaseDatabase.getInstance().getReference().child("UserDetails").child(str_userName).child("userType").setValue(userType_spinner.getSelectedItem().toString());
                Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
                break;
        }
    }
}
