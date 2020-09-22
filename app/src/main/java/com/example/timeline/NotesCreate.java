package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesCreate extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private TextInputLayout subject,description;
    private Button btnSave;
    private CheckBox addFavorite_checkbox;
    private String uid,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_create);

        subject = findViewById(R.id.NC_subject);
        description = findViewById(R.id.NC_description);
        addFavorite_checkbox = findViewById(R.id.NC_addTofav_checkbox);
        btnSave = findViewById(R.id.NC_btnSave);
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        get_user_info();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save note in sqlite
                save_note();
            }
        });
    }

    private void save_note(){
        if (!check_validation(subject) | !check_validation(description)){
            return;
        }
        String str_subject,str_description,str_addtoFav;
        str_subject = subject.getEditText().getText().toString();
        str_description = description.getEditText().getText().toString();
        if (addFavorite_checkbox.isChecked()){
            str_addtoFav = "yes";
        }else{
            str_addtoFav = "no";
        }
        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Notes.COLUMN_USERNAME,userName);
        cv.put(TimelineDB.Notes.COLUMN_SUBJECT,str_subject);
        cv.put(TimelineDB.Notes.COLUMN_DESCRIPTION,str_description);
        cv.put(TimelineDB.Notes.COLUMN_DATE,get_current_Date());
        cv.put(TimelineDB.Notes.COLUMN_TIME,get_current_Time());
        cv.put(TimelineDB.Notes.COLUMN_DAYOFWEEK,get_current_day());
        cv.put(TimelineDB.Notes.COLUMN_MODIFIED_DATE,"");
        cv.put(TimelineDB.Notes.COLUMN_FAVORITE,str_addtoFav);
        cv.put(TimelineDB.Notes.COLUMN_BACKUP,"0");
        mDatabase.insert(TimelineDB.Notes.TABLE_NAME,null,cv);
        Toasty.success(getApplicationContext(),"Saved",Toasty.LENGTH_SHORT).show();
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
    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }

    public String get_current_DateTimeDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    public String get_current_Time(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    public String get_current_day(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
