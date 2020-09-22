package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;

public class Home extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private long backPressedTime;
    private CardView CVactivity,CVpasswordManager,CVprofile,CVhistory,CVnotice,CVnotes,CVeventTime,CVsettings;
    private String uid,userName,userType,userstatus,userFullName,userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // this is for toolbar
        toolbar = findViewById(R.id.home_toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //this is for custom toast
        Toasty.Config.getInstance()
                .allowQueue(false) // optional (prevents several Toastys from queuing)
                .apply(); // required
        get_user_info();
        update_last_Visit();
        check_disable_account();

        CVactivity = findViewById(R.id.home_activity_cardView);
        CVpasswordManager = findViewById(R.id.home_password_manager_cardView);
        CVprofile = findViewById(R.id.home_profile_cardView);
        CVhistory = findViewById(R.id.home_history_cardView);
        CVnotice = findViewById(R.id.home_notice_cardView);
        CVnotes = findViewById(R.id.home_notes_cardView);
        CVeventTime = findViewById(R.id.home_event_timer_cardView);
        CVsettings = findViewById(R.id.home_settings_cardView);

        CVactivity.setOnClickListener(this);
        CVpasswordManager.setOnClickListener(this);
        CVprofile.setOnClickListener(this);
        CVhistory.setOnClickListener(this);
        CVnotice.setOnClickListener(this);
        CVnotes.setOnClickListener(this);
        CVeventTime.setOnClickListener(this);
        CVsettings.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_user_info();
    }

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userType = sharedPreferences.getString("userType","");
            userstatus = sharedPreferences.getString("status","");
            //userPassword = sharedPreferences.getString("password","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
    private void update_last_Visit(){
        if (check_internet_connection()){
            FirebaseFirestore.getInstance().collection("UserDetails").document(userName).update("lastVisit",get_current_Date());
            FirebaseDatabase.getInstance().getReference().child("UserDetails").child(userName).child("lastVisit").setValue(get_current_Date());
        }
    }
    private void check_disable_account(){
        if (check_internet_connection()){
            FirebaseFirestore.getInstance().collection("UserDetails").document(userName).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e!=null){
                        return;
                    }
                    String status = documentSnapshot.getString("status");
                    if (status != null && !status.isEmpty() && status.toLowerCase().equals("disable")){
                        SharedPreferences.Editor editor;
                        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("status",status);
                        editor.putBoolean("isLoggedin",false);
                        editor.putBoolean("rememberMe",false);
                        editor.apply();
                        Toasty.error(getApplicationContext(),"Your account is disabled. please contact with admin",Toasty.LENGTH_LONG).show();
                        startActivity(new Intent(Home.this,Login.class));
                        Home.this.finish();

                    }else if (status.toLowerCase().equals("active")){
                        if (!userstatus.toLowerCase().equals("active")){
                            SharedPreferences.Editor editor;
                            SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("status",status);
                            editor.apply();
                        }

                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_menu_About:
                startActivity(new Intent(Home.this,Aboutus.class));
                break;
            case R.id.home_menu_feedback:
                startActivity(new Intent(Home.this,Feedback.class));
                break;
            case R.id.home_menu_Logout:
                Toasty.info(this, "logged out successfully", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor;
                SharedPreferences sharedPreferences2 = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                editor = sharedPreferences2.edit();
                editor.putBoolean("isLoggedin",false);
                editor.putBoolean("rememberMe",false);
                editor.apply();
                startActivity(new Intent(getApplicationContext(),Login.class));
                this.finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_activity_cardView:
                Intent activityIntent = new Intent(Home.this,Activity.class);
                startActivity(activityIntent);
                break;
            case R.id.home_password_manager_cardView:
                // if the setting is set for verify password for password manager protection
                // for this first have to check sharedpreference variable
                Intent verifyPasswordIntent = new Intent(Home.this,PasswordVerification.class);
                verifyPasswordIntent.putExtra("Aid",1);
                startActivity(verifyPasswordIntent);

//                Intent addNewPasswordIntent = new Intent(Home.this,PasswordManager.class);
//                startActivity(addNewPasswordIntent);
                break;
            case R.id.home_profile_cardView:
                Intent profileIntent = new Intent(Home.this,Profile.class);
                startActivity(profileIntent);
                break;
            case R.id.home_history_cardView:
                Intent HistoryIntent = new Intent(Home.this,History.class);
                startActivity(HistoryIntent);

                break;
            case R.id.home_notice_cardView:
                Intent noticeIntent = new Intent(Home.this,Notice.class);
                startActivity(noticeIntent);
                break;
            case R.id.home_notes_cardView:
                startActivity(new Intent(Home.this,Notes.class));
                break;
            case R.id.home_event_timer_cardView:
                Intent projectsintent = new Intent(Home.this,Projects.class);
                startActivity(projectsintent);

                break;
            case R.id.home_settings_cardView:
                Intent intent = new Intent(this,Settings.class);
                startActivity(intent);
                break;
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

    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
