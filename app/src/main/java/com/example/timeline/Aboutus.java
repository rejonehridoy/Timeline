package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Aboutus extends AppCompatActivity {
    private String str_current_version,str_current_version_type;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        version = findViewById(R.id.about_version);
        get_current_app_info();
    }
    private void get_current_app_info() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        str_current_version = sharedPreferences.getString("appVersion", "");
        str_current_version_type = sharedPreferences.getString("appVersionType", "");

        if (str_current_version_type.toLowerCase().equals("beta")){
            version.setText("Version "+str_current_version+" ("+str_current_version_type+")");
        }else{
            version.setText("Version "+str_current_version);
        }

    }
}
