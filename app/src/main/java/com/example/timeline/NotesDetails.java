package com.example.timeline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotesDetails extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase mDatabase;
    private TextInputLayout subject,description,createdDate,modifiedDate;
    private Button btnUpdate,btnDelete,btnCancel,btnEdit;
    private CheckBox fav_checkBox;
    private String uid,userName;
    private int ID;
    private String str_subject,str_description,str_fav,str_createdDate,str_modifiedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        subject = findViewById(R.id.noteDetails_subject);
        description = findViewById(R.id.noteDetails_description);
        createdDate = findViewById(R.id.noteDetails_createdDate);
        modifiedDate = findViewById(R.id.noteDetails_modifiedDate);
        btnCancel = findViewById(R.id.noteDetails_btnCancel);
        btnUpdate = findViewById(R.id.noteDetails_btnUpdate);
        btnDelete = findViewById(R.id.noteDetails_btnDelete);
        btnEdit = findViewById(R.id.noteDetails_btnEdit);
        fav_checkBox = findViewById(R.id.noteDetails_addTofav_checkbox);

        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();


        get_user_info();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID",0);
        if (ID < 1){
            Toasty.error(getApplicationContext(),"Error Occured", Toasty.LENGTH_SHORT).show();
            finish();
        }
        get_data_from_sqlite();

        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }

    private void get_data_from_sqlite(){
        Cursor cursor1 = mDatabase.rawQuery("SELECT * FROM "+ TimelineDB.Notes.TABLE_NAME+ " WHERE "+ TimelineDB.Notes._ID+"="+ID,null);

        if (cursor1.moveToFirst()){
            do {
                str_subject = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_SUBJECT));
                str_description  = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_DESCRIPTION));
                String date = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_DATE));
                String time = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_TIME));
                String dayOfWeek = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_DAYOFWEEK));
                str_modifiedDate = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_MODIFIED_DATE));
                str_fav = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_FAVORITE));
                str_createdDate = convert_createdDate_format(date,time,dayOfWeek);
            }while (cursor1.moveToNext());

            subject.getEditText().setText(str_subject);
            description.getEditText().setText(str_description);
            createdDate.getEditText().setText(str_createdDate);
            modifiedDate.getEditText().setText(convert_ModifiedDate_format(str_modifiedDate));
            if (str_fav.toLowerCase().equals("yes")){
                fav_checkBox.setChecked(true);
            }else{
                fav_checkBox.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.noteDetails_btnCancel:
                set_disable_editBoxes();
                break;
            case R.id.noteDetails_btnDelete:
                delete_button_Dialog();
                break;
            case R.id.noteDetails_btnUpdate:
                update_note();

                break;
            case R.id.noteDetails_btnEdit:
                set_enable_editBoxes();
                break;
        }
    }
    private void update_note(){
        if (!check_validation(subject) | !check_validation(description)){
            return;
        }
        str_subject = subject.getEditText().getText().toString();
        str_description = description.getEditText().getText().toString();
        if (fav_checkBox.isChecked()){
            str_fav = "yes";
        }else{
            str_fav = "no";
        }
        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Notes.COLUMN_SUBJECT,str_subject);
        cv.put(TimelineDB.Notes.COLUMN_DESCRIPTION,str_description);
        cv.put(TimelineDB.Notes.COLUMN_FAVORITE,str_fav);
        cv.put(TimelineDB.Notes.COLUMN_MODIFIED_DATE,get_current_DateTimeDay());
        mDatabase.update(TimelineDB.Notes.TABLE_NAME, cv, TimelineDB.Notes._ID + "=" + ID, null);
        Toasty.success(NotesDetails.this, "updated", Toasty.LENGTH_SHORT).show();
        get_data_from_sqlite();
        set_disable_editBoxes();
    }
    private void set_enable_editBoxes(){
        subject.getEditText().setEnabled(true);
        description.getEditText().setEnabled(true);
        fav_checkBox.setEnabled(true);
        btnEdit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
    }
    private void set_disable_editBoxes(){
        subject.getEditText().setEnabled(false);
        description.getEditText().setEnabled(false);
        fav_checkBox.setEnabled(false);
        btnEdit.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnDelete.setVisibility(View.VISIBLE);
    }
    private void delete_note(){
        mDatabase.delete(TimelineDB.Notes.TABLE_NAME, TimelineDB.Notes._ID + "=" + ID, null);
        Toasty.success(getApplicationContext(), "Deleted", Toasty.LENGTH_SHORT).show();
        this.finish();
    }
    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in", Toasty.LENGTH_LONG).show();
        }
    }
    private void delete_button_Dialog() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure to delete this note? If deleted it can not be undone")
                .setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked
                        //update work completion info in request table
                        delete_note();


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
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    public String get_current_DateTimeDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

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
    private String convert_ModifiedDate_format(String DateTime){
        //format july 22,2020, wed at 05:29 PM
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
    private String convert_createdDate_format(String localDate,String localTime,String localDay){
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
                return targetFormat2.format(date)+", "+localDay+" at "+localTime;
            }else{
                return targetFormat3.format(date)+", "+localDay+ " at "+localTime;
            }


        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }

        return localDate+ " "+ localDay+" "+localTime;

    }


}
