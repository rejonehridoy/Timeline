package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Feedback extends AppCompatActivity {
    private Button btnSubmit;
    private TextInputLayout subject,message;
    private RatingBar ratingBar;
    private String uid,userName,userEmail,userFullName,str_current_version,str_current_version_type;
    private static final String FEEDBACK_TABLE = "Feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnSubmit = findViewById(R.id.feedback_btnSubmit);
        subject = findViewById(R.id.feedback_subject);
        message = findViewById(R.id.feeback_message);
        ratingBar = findViewById(R.id.feedback_rating);
        get_user_info();
        get_current_app_info();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit feedback
                store_feedback();
            }
        });
    }

    private void store_feedback(){
        if (!check_validation(subject) | !check_validation(message)){
            return;
        }
        if (!check_internet_connection()){
            Toasty.error(getApplicationContext(),"Check your internet connection and try again",Toasty.LENGTH_SHORT).show();
            return;
        }
        if (ratingBar.getRating()==0.0){
            Toasty.info(getApplicationContext(),"Give us rating",Toasty.LENGTH_SHORT).show();
            return;
        }
        //store data into firebase

        String key = FirebaseFirestore.getInstance().collection(FEEDBACK_TABLE).document().getId();

        FeedbackInfo fb = new FeedbackInfo(key,str_current_version,subject.getEditText().getText().toString(),message.getEditText().getText().toString(),userName,
                userFullName,userEmail,String.valueOf(ratingBar.getRating()),get_current_Date(),"");

        FirebaseDatabase.getInstance().getReference(FEEDBACK_TABLE).child(key).setValue(fb);
        FirebaseFirestore.getInstance().collection(FEEDBACK_TABLE).document(key).set(fb);

        Toasty.success(getApplicationContext(),"Feedback Sent",Toasty.LENGTH_SHORT).show();
        this.finish();


    }

    private Boolean check_validation(TextInputLayout field) {

        if (field == null || field.getEditText() == null || field.getEditText().getText().toString().isEmpty()) {
            field.setError("Field cannot be empty");
            return false;
        } else {
            field.setError(null);
            field.setErrorEnabled(false);
            return true;
        }
    }
    private void get_current_app_info() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        str_current_version = sharedPreferences.getString("appVersion", "");
        str_current_version_type = sharedPreferences.getString("appVersionType", "");

    }

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
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
