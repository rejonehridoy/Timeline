package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminAppReleasesNew extends AppCompatActivity {
    private TextInputLayout bug,description,downloadLink,releaseDate,version,versionType;
    private Button btnCreate,btnBrowse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_app_releases_new);

        description = findViewById(R.id.AARN_description);
        bug = findViewById(R.id.AARN_bug);
        downloadLink = findViewById(R.id.AARN_downloadLink);
        releaseDate = findViewById(R.id.AARN_releaseDate);
        version = findViewById(R.id.AARN_appVersion);
        versionType = findViewById(R.id.AARN_versionType);
        btnCreate = findViewById(R.id.AARN_btnCreate);
        btnBrowse = findViewById(R.id.AARN_btnBrowse);


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create release info
                create_release_info();
            }
        });
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downloadLink != null && downloadLink.getEditText() != null && !downloadLink.getEditText().getText().toString().isEmpty()){
                    Uri webpage = Uri.parse(downloadLink.getEditText().getText().toString());
                    Intent webintent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(webintent);
                }else{
                    Toasty.error(getApplicationContext(),"Empty link",Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void create_release_info(){

        if (!check_validation(version) && !check_validation(description)){
            return;
        }
        String key = FirebaseFirestore.getInstance().collection("UpdateRelease").document().getId();

        ReleaseInfo info = new ReleaseInfo(key,bug.getEditText().getText().toString(),description.getEditText().getText().toString(),downloadLink.getEditText().getText().toString(),
                releaseDate.getEditText().getText().toString(),version.getEditText().getText().toString(),versionType.getEditText().getText().toString());
        FirebaseFirestore.getInstance().collection("UpdateRelease").document(key).set(info);
        FirebaseDatabase.getInstance().getReference("UpdateRelease").child(key).setValue(info);

        Toasty.success(getApplicationContext(),"Created info",Toasty.LENGTH_SHORT).show();
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

}
