package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class CheckUpdate extends AppCompatActivity {
    private Button btnDownload,btnGoBack;
    private LinearLayout update_layout;
    private TextView version, description, currentVersion;
    private String str_downloadLink, str_version = "", str_date, str_description, str_version_type = "", str_current_version, str_current_version_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_update);

        update_layout = findViewById(R.id.CU_layout_update);
        btnDownload = findViewById(R.id.CU_downloadLink);
        btnGoBack = findViewById(R.id.CU_btnGoBack);
        currentVersion = findViewById(R.id.CU_currentVersion);
        version = findViewById(R.id.CU_version);
        description = findViewById(R.id.CU_description);

        get_current_app_info();
        get_checkUpdate_info();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_downloadLink !=null && !str_downloadLink.isEmpty()){
                    Uri webpage = Uri.parse(str_downloadLink);
                    Intent webintent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(webintent);
//                    String msg = "rejone.hridoy@gmail.com";
//                    Intent send = new Intent(Intent.ACTION_SEND);
//                    send.putExtra(Intent.EXTRA_EMAIL,msg);
//                    send.setType("text/plain");
//
//                    Intent chooser = Intent.createChooser(send,"Share with friends");
//                    if (send.resolveActivity(getPackageManager()) !=null){
//                        startActivity(chooser);
//                    }

                }
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckUpdate.this.finish();
            }
        });
    }

    private void get_current_app_info() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        str_current_version = sharedPreferences.getString("appVersion", "");
        str_current_version_type = sharedPreferences.getString("appVersionType", "");

    }

    private void get_checkUpdate_info() {
        FirebaseFirestore.getInstance().collection("UpdateRelease").document("latestRelease")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()) {
                            str_description = documentSnapshot.getString("description");
                            str_version = documentSnapshot.getString("version");
                            str_version_type = documentSnapshot.getString("versionType");
                            str_date = documentSnapshot.getString("releaseDate");
                            str_downloadLink = documentSnapshot.getString("downloadLink");

                            int check = str_version.compareTo(str_current_version);
                            if (check>0){
                                btnGoBack.setVisibility(View.GONE);
                                update_layout.setVisibility(View.VISIBLE);
                                currentVersion.setVisibility(View.GONE);
                                version.setText("There is found a new version "+str_version);
                                description.setText(str_description+"\n\nReleased Date: "+str_date);

                            }

                        }
                    }
                });
    }
}
