package com.example.timeline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminAppReleaseDetails extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout id,bug,description,downloadLink,releaseDate,version,versionType;
    private String str_id,str_bug,str_description,str_downloadLink,str_releaseDate,str_version,str_versionType;
    private Button btnUpdate,btnEdit,btnDelete,btnMakeLatest,btnBrowse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_app_release_details);

        id = findViewById(R.id.AARD_id);
        description = findViewById(R.id.AARD_description);
        bug = findViewById(R.id.AARD_bug);
        downloadLink = findViewById(R.id.AARD_downloadLink);
        releaseDate = findViewById(R.id.AARD_releaseDate);
        version = findViewById(R.id.AARD_appVersion);
        versionType = findViewById(R.id.AARD_versionType);
        btnDelete = findViewById(R.id.AARD_btnDelete);
        btnEdit = findViewById(R.id.AARD_btnEdit);
        btnMakeLatest = findViewById(R.id.AARD_btnMakeLatest);
        btnUpdate = findViewById(R.id.AARD_btnUpdate);
        btnBrowse = findViewById(R.id.AARD_btnBrowse);


        get_data_from_intent();
        btnBrowse.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnMakeLatest.setOnClickListener(this);
    }

    private void get_data_from_intent(){
        Intent intent = getIntent();
        str_id = intent.getStringExtra("id");
        str_bug = intent.getStringExtra("bug");
        str_downloadLink = intent.getStringExtra("downloadLink");
        str_description = intent.getStringExtra("description");
        str_releaseDate = intent.getStringExtra("releaseDate");
        str_version = intent.getStringExtra("version");
        str_versionType = intent.getStringExtra("versionType");

        id.getEditText().setText(str_id);
        bug.getEditText().setText(str_bug);
        downloadLink.getEditText().setText(str_downloadLink);
        description.getEditText().setText(str_description);
        releaseDate.getEditText().setText(str_releaseDate);
        version.getEditText().setText(str_version);
        versionType.getEditText().setText(str_versionType);

    }

    private void set_enable_editboxes(){
        bug.getEditText().setEnabled(true);
        downloadLink.getEditText().setEnabled(true);
        description.getEditText().setEnabled(true);
        releaseDate.getEditText().setEnabled(true);
        version.getEditText().setEnabled(true);
        versionType.getEditText().setEnabled(true);
        btnEdit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
    }
    private void set_disable_editboxes(){
        bug.getEditText().setEnabled(false);
        downloadLink.getEditText().setEnabled(false);
        description.getEditText().setEnabled(false);
        releaseDate.getEditText().setEnabled(false);
        version.getEditText().setEnabled(false);
        versionType.getEditText().setEnabled(false);
        btnEdit.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
    }
    private void update_info_firebase(){
        if (!check_validation(version) | !check_validation(description) | !check_validation(downloadLink)){
            return;
        }
        DocumentReference doc = FirebaseFirestore.getInstance().collection("UpdateRelease").document(str_id);
        doc.update("bug",bug.getEditText().getText().toString());
        doc.update("description",description.getEditText().getText().toString());
        doc.update("releaseDate",releaseDate.getEditText().getText().toString());
        doc.update("version",version.getEditText().getText().toString());
        doc.update("versionType",versionType.getEditText().getText().toString());
        doc.update("downloadLink",downloadLink.getEditText().getText().toString());

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("UpdateRelease").child(str_id);
        dref.child("bug").setValue(bug.getEditText().getText().toString());
        dref.child("description").setValue(description.getEditText().getText().toString());
        dref.child("releaseDate").setValue(releaseDate.getEditText().getText().toString());
        dref.child("version").setValue(version.getEditText().getText().toString());
        dref.child("versionType").setValue(versionType.getEditText().getText().toString());
        dref.child("downloadLink").setValue(downloadLink.getEditText().getText().toString());

        Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
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

    private void make_latest_version(){
        DocumentReference doc = FirebaseFirestore.getInstance().collection("UpdateRelease").document("latestRelease");
        doc.update("bug",bug.getEditText().getText().toString());
        doc.update("description",description.getEditText().getText().toString());
        doc.update("releaseDate",releaseDate.getEditText().getText().toString());
        doc.update("version",version.getEditText().getText().toString());
        doc.update("versionType",versionType.getEditText().getText().toString());
        doc.update("downloadLink",downloadLink.getEditText().getText().toString());

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("UpdateRelease").child("latestRelease");
        dref.child("bug").setValue(bug.getEditText().getText().toString());
        dref.child("description").setValue(description.getEditText().getText().toString());
        dref.child("releaseDate").setValue(releaseDate.getEditText().getText().toString());
        dref.child("version").setValue(version.getEditText().getText().toString());
        dref.child("versionType").setValue(versionType.getEditText().getText().toString());
        dref.child("downloadLink").setValue(downloadLink.getEditText().getText().toString());

        Toasty.success(getApplicationContext(),"Made Latest Version",Toasty.LENGTH_SHORT).show();
    }

    private void delete_release_info(){
        FirebaseFirestore.getInstance().collection("UpdateRelease").document(str_id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(getApplicationContext(),"Deleted",Toasty.LENGTH_SHORT).show();
                    }
                });
        FirebaseDatabase.getInstance().getReference().child("UpdateRelease").child(str_id).removeValue();
        this.finish();
    }
    private void MakeLatest_Dialog() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Latest Version")
                .setMessage("Are you sure to make this version as latest version?")
                //.setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked
                        make_latest_version();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No button clicked
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    private void delete_Dialog() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete Version")
                .setMessage("Are you sure to delete this version? if deleted it can not be undone")
                .setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked
                        delete_release_info();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No button clicked
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AARD_btnBrowse:
                if (str_downloadLink != null && !str_downloadLink.isEmpty()){
                    Uri webpage = Uri.parse(str_downloadLink);
                    Intent webintent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(webintent);
                }
                break;
            case R.id.AARD_btnEdit:
                set_enable_editboxes();
                break;
            case R.id.AARD_btnDelete:
                delete_Dialog();
                break;
            case R.id.AARD_btnUpdate:
                update_info_firebase();
                set_disable_editboxes();
                break;
            case R.id.AARD_btnMakeLatest:
                MakeLatest_Dialog();
                break;
        }
    }
}
