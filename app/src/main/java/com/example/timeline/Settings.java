package com.example.timeline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btnDelete,btnCheckUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnCheckUpdate = findViewById(R.id.setting_btnCheckUpdate);
        btnDelete = findViewById(R.id.setting_btnDelete);

        // this is for toolbar
        toolbar = findViewById(R.id.setting_toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);

        btnDelete.setOnClickListener(this);
        btnCheckUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_btnCheckUpdate:
                startActivity(new Intent(getApplicationContext(),CheckUpdate.class));
                finish();

                break;
            case R.id.setting_btnDelete:
                open_delete_dialog();
                break;
        }
    }

    private void delete_account(){
        Intent intent = new Intent(Settings.this,PasswordVerification.class);
        intent.putExtra("Aid",3);
        startActivity(intent);
    }
    private void open_delete_dialog() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure to delete your account? If deleted, all your personal data will be deleted forever and it can not be undone")
                .setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked
                        delete_account();
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
}
