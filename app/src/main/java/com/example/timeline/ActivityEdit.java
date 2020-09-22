package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityEdit extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private TextView activtiy;
    private int ID;
    private LinearLayout layout_duration;
    private SearchableSpinner category_spinner,year_spinner,result_spinner,status_spinner,language_spinner;
    private TextInputLayout name,category,language,place,yourTeamName,opponentName,durationHours,durationMins,price,status,date,time,modifedDate,
            notes,review,result;
    private TextInputEditText etName,etCategory,etLanguage,etPlace,etYourTeamName,etOpponetTeam,etPrice,etStatus,etDate,etTime,etModifiedDate,
            etDurationHours,etDurationMins,etNotes,etReview,etResult;
    private String str_activity,str_name,str_category,str_language,str_year,str_place,str_yourTeamName,str_opponentName,str_duration,
            str_price,str_status,str_date,str_modifiedDate,str_notes,str_review,str_result,str_time,str_dayOfWeek;
    private Button btnUpdate,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //find view by id for all components
        all_find_view_by_id();
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        if (ID < 1) {
            Toasty.error(getApplicationContext(), "Invalid Operation", Toasty.LENGTH_SHORT).show();
            this.finish();
        }
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        get_all_value_from_sqliteDatabase();
        setup_info_according_to_activity_type();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this buttton will update
                update_info();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityEdit.this.finish();
            }
        });

    }
    private void update_info(){
        String str_name,str_category,str_language,str_year="",str_place,str_price="0",str_result="",str_status="",str_duration,str_yourTeam="",str_opponentTeam="",str_review,str_notes;
        String modifiedDateTime = get_current_DateTime_DayOfWeek(),str_time="",str_date="";
        //for watching section
        if (str_activity.toLowerCase().equals("watching")){
            if (!check_validation(name) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_language = language_spinner.getSelectedItem().toString();
            str_year = year_spinner.getSelectedItem().toString();
            str_place = place.getEditText().getText().toString();
            if (!price.getEditText().getText().toString().isEmpty()){
                str_price = price.getEditText().getText().toString();
            }
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());

            if (category_spinner.getSelectedItem().toString().toLowerCase().equals("tv series") || category_spinner.getSelectedItem().toString().toLowerCase().equals("drama")){
                if (etYourTeamName.getText() != null && !etYourTeamName.getText().toString().isEmpty()){
                    str_yourTeam = etYourTeamName.getText().toString();
                }
                if (etOpponetTeam.getText() != null && !etOpponetTeam.getText().toString().isEmpty()){
                    str_opponentTeam = etOpponetTeam.getText().toString();
                }
            }
            //update data into SQLite
            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_LANGUAGE,str_language);
            cv.put(TimelineDB.Activity.COLUMN_YEAR,str_year);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_PRICE,str_price);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM,str_yourTeam);
            cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM,str_opponentTeam);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }

            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();
        }else if (str_activity.toLowerCase().equals("eating")){
            if (!check_validation(name) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_category = category_spinner.getSelectedItem().toString();
            str_place = place.getEditText().getText().toString();
            if (!price.getEditText().getText().toString().isEmpty()){
                str_price = price.getEditText().getText().toString();
            }
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
            }

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_PRICE,str_price);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();

        }else if (str_activity.toLowerCase().equals("playing")){
            //playing section
            if (!check_validation(name) | !check_validation(category) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_category = category.getEditText().getText().toString();
            str_place = place.getEditText().getText().toString();
            if (!price.getEditText().getText().toString().isEmpty()){
                str_price = price.getEditText().getText().toString();
            }
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());
            str_status = status_spinner.getSelectedItem().toString();
            str_result = result.getEditText().getText().toString();
            if (year_spinner.getSelectedItem() != null && !year_spinner.getSelectedItem().toString().isEmpty()){
                str_year = year_spinner.getSelectedItem().toString();
            }

            if (etYourTeamName.getText() != null && !etYourTeamName.getText().toString().isEmpty()){
                str_yourTeam = etYourTeamName.getText().toString();
            }
            if (etOpponetTeam.getText() != null && !etOpponetTeam.getText().toString().isEmpty()){
                str_opponentTeam = etOpponetTeam.getText().toString();
            }
            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_PRICE,str_price);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_RESULT,str_result);
            cv.put(TimelineDB.Activity.COLUMN_STATUS,str_status);
            cv.put(TimelineDB.Activity.COLUMN_YEAR,str_year);
            cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM,str_yourTeam);
            cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM,str_opponentTeam);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();
        }else if (str_activity.toLowerCase().equals("working")){
            if (!check_validation(name) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_category = category.getEditText().getText().toString();
            str_place = place.getEditText().getText().toString();
            str_status = status_spinner.getSelectedItem().toString();
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_result = result.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_RESULT,str_result);
            cv.put(TimelineDB.Activity.COLUMN_STATUS,str_status);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();

        }else if (str_activity.toLowerCase().equals("reading")){
            if (!check_validation(name) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_language = language.getEditText().getText().toString();
            str_category = category_spinner.getSelectedItem().toString();
            str_status = status_spinner.getSelectedItem().toString();
            if (year_spinner.getSelectedItem() != null && !year_spinner.getSelectedItem().toString().isEmpty()){
                str_year = year_spinner.getSelectedItem().toString();
            }

            if (etYourTeamName.getText() != null && !etYourTeamName.getText().toString().isEmpty()){
                str_yourTeam = etYourTeamName.getText().toString();
            }
            if (etOpponetTeam.getText() != null && !etOpponetTeam.getText().toString().isEmpty()){
                str_opponentTeam = etOpponetTeam.getText().toString();
            }
            str_place = place.getEditText().getText().toString();
            if (!price.getEditText().getText().toString().isEmpty()){
                str_price = price.getEditText().getText().toString();
            }
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_LANGUAGE,str_language);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_PRICE,str_price);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_STATUS,str_status);
            cv.put(TimelineDB.Activity.COLUMN_YEAR,str_year);
            cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM,str_yourTeam);
            cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM,str_opponentTeam);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();

        }else if (str_activity.toLowerCase().equals("writing")){
            //writing section
            if (!check_validation(name) | !check_validation(category) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_category = category.getEditText().getText().toString();
            str_language = language.getEditText().getText().toString();
            str_place = place.getEditText().getText().toString();
            str_status = status_spinner.getSelectedItem().toString();
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_STATUS,str_status);
            cv.put(TimelineDB.Activity.COLUMN_LANGUAGE,str_language);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();

        }else if (str_activity.toLowerCase().equals("solving")){
            //solving
            if (!check_validation(name) | !check_validation_review(review)) {
                return;
            }
            str_language = language_spinner.getSelectedItem().toString();
            str_result = result_spinner.getSelectedItem().toString();
            str_status = status_spinner.getSelectedItem().toString();
            str_name = name.getEditText().getText().toString();
            str_category = category.getEditText().getText().toString();
            if (etYourTeamName.getText() != null && !etYourTeamName.getText().toString().isEmpty()){
                str_yourTeam = etYourTeamName.getText().toString();
            }
            if (etOpponetTeam.getText() != null && !etOpponetTeam.getText().toString().isEmpty()){
                str_opponentTeam = etOpponetTeam.getText().toString();
            }
            str_place = place.getEditText().getText().toString();
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_LANGUAGE,str_language);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_RESULT,str_result);
            cv.put(TimelineDB.Activity.COLUMN_STATUS,str_status);
            cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM,str_yourTeam);
            cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM,str_opponentTeam);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();

        }else if (str_activity.toLowerCase().equals("buying")){
            //buying section
            if (!check_validation(name) | !check_validation(category) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_category = category.getEditText().getText().toString();
            str_place = place.getEditText().getText().toString();
            if (!price.getEditText().getText().toString().isEmpty()){
                str_price = price.getEditText().getText().toString();
            }
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            if (year_spinner.getSelectedItem() != null && !year_spinner.getSelectedItem().toString().isEmpty()){
                str_year = year_spinner.getSelectedItem().toString();
            }

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_PRICE,str_price);
            cv.put(TimelineDB.Activity.COLUMN_YEAR,str_year);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();

        }else if (str_activity.toLowerCase().equals("attending")){
            //attending section
            if (!check_validation(name) | !check_validation(category) | !check_validation_review(review)) {
                return;
            }
            str_name = name.getEditText().getText().toString();
            str_category = category.getEditText().getText().toString();
            str_place = place.getEditText().getText().toString();
            str_status = status.getEditText().getText().toString();
            str_review = review.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_CATEGORY,str_category);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_REVIEW,str_review);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_STATUS,str_status);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();
        }else if (str_activity.toLowerCase().equals("wasting time")){
            //wastingTime section
            if (!check_validation(name)) {
                return;
            }
            str_result = result_spinner.getSelectedItem().toString();
            str_name = name.getEditText().getText().toString();
            str_duration = convert_into_minutes(etDurationHours.getText().toString(),etDurationMins.getText().toString());
            str_place = place.getEditText().getText().toString();
            str_notes = notes.getEditText().getText().toString();

            ContentValues cv = new ContentValues();
            cv.put(TimelineDB.Activity.COLUMN_NAME,str_name);
            cv.put(TimelineDB.Activity.COLUMN_PLACE,str_place);
            cv.put(TimelineDB.Activity.COLUMN_RESULT,str_result);
            cv.put(TimelineDB.Activity.COLUMN_NOTES,str_notes);
            cv.put(TimelineDB.Activity.COLUMN_DURATION,str_duration);
            cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE,modifiedDateTime);
            if(!date.getEditText().getText().toString().isEmpty() && !time.getEditText().getText().toString().isEmpty()){
                str_date = etDate.getText().toString();
                str_time = etTime.getText().toString();
                cv.put(TimelineDB.Activity.COLUMN_DATE,str_date);
                cv.put(TimelineDB.Activity.COLUMN_TIME,str_time);
            }
            mDatabase.update(TimelineDB.Activity.TABLE_NAME, cv, TimelineDB.Activity._ID + "=" + ID, null);
            Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_SHORT).show();
            this.finish();
        }
    }

    private void all_find_view_by_id(){
        activtiy = findViewById(R.id.AE_activity);
        name = findViewById(R.id.AE_name);
        etName = findViewById(R.id.AE_name_input);
        category = findViewById(R.id.AE_category);
        etCategory = findViewById(R.id.AE_category_input);
        category_spinner = findViewById(R.id.AE_category_spinner);
        language = findViewById(R.id.AE_language);
        etLanguage = findViewById(R.id.AE_language_input);
        language_spinner = findViewById(R.id.AE_language_spinner);
        year_spinner = findViewById(R.id.AE_year_spinner);
        result_spinner = findViewById(R.id.AE_result_spinner);
        result = findViewById(R.id.AE_result);
        etResult = findViewById(R.id.AE_result_input);
        place = findViewById(R.id.AE_place);
        etPlace = findViewById(R.id.AE_place_input);
        yourTeamName = findViewById(R.id.AE_yourTeam);
        etYourTeamName = findViewById(R.id.AE_yourTeam_input);
        opponentName = findViewById(R.id.AE_opponetTeam);
        etOpponetTeam = findViewById(R.id.AE_opponentTeam_input);
        status = findViewById(R.id.AE_status);
        etStatus = findViewById(R.id.AE_status_input);
        status_spinner = findViewById(R.id.AE_status_spinner);
        durationHours = findViewById(R.id.AE_durationHours);
        etDurationHours = findViewById(R.id.AE_durationHours_input);
        durationMins = findViewById(R.id.AE_durationMinutes);
        etDurationMins = findViewById(R.id.AE_durationMinutes_input);
        price = findViewById(R.id.AE_price);
        etPrice = findViewById(R.id.AE_price_input);
        date = findViewById(R.id.AE_date);
        etDate = findViewById(R.id.AE_date_input);
        time = findViewById(R.id.AE_time);
        etTime = findViewById(R.id.AE_time_input);
        etPrice = findViewById(R.id.AE_price_input);
        modifedDate = findViewById(R.id.AE_modifiedDate);
        etModifiedDate = findViewById(R.id.AE_modifiedDate_input);
        notes = findViewById(R.id.AE_notes);
        etNotes = findViewById(R.id.AE_notes_input);
        review = findViewById(R.id.AE_review);
        etReview = findViewById(R.id.AE_review_input);
        btnUpdate = findViewById(R.id.AE_btnUpdate);
        btnCancel = findViewById(R.id.AE_btnCancel);
        layout_duration = findViewById(R.id.AE_duration);
    }
    private void get_all_value_from_sqliteDatabase(){

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TimelineDB.Activity.TABLE_NAME + " WHERE " + TimelineDB.Activity._ID + " = " + ID, null);
        if (cursor.moveToFirst()) {
            do {
                int str_id = cursor.getInt(cursor.getColumnIndex(TimelineDB.Activity._ID));
                str_activity = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE));
                str_name = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_NAME));
                str_category = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_CATEGORY));
                str_place = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_PLACE));
                str_price = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_PRICE));
                str_duration = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_DURATION));
                str_year = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_YEAR));
                str_yourTeamName = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_YOUR_TEAM));
                str_opponentName = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_OPPONENT_TEAM));
                str_language = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_LANGUAGE));
                str_review = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_REVIEW));
                str_notes = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_NOTES));
                str_status = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_STATUS));
                str_date = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_DATE));
                str_time = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_TIME));
                str_dayOfWeek = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_DAYOFWEEK));
                str_result = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_RESULT));
                str_modifiedDate = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_MODIFIED_DATE));
            } while (cursor.moveToNext());
        }

    }
    private void setup_info_according_to_activity_type(){
        activtiy.setText(str_activity);
        etDate.setText(str_date);
        etTime.setText(str_time);
        etName.setText(str_name);
        if (str_notes != null && !str_notes.equals("")){
            etNotes.setText(str_notes);
        }
        if (str_modifiedDate != null && !str_modifiedDate.equals("")){
            modifedDate.setVisibility(View.VISIBLE);
            etModifiedDate.setText(str_modifiedDate);
            etModifiedDate.setEnabled(false);
        }else{
            modifedDate.setVisibility(View.VISIBLE);
            etModifiedDate.setText("N/A");
            etModifiedDate.setEnabled(false);
        }
        if (str_activity.toLowerCase().equals("watching")){
            // set visible components
            language_spinner.setVisibility(View.VISIBLE);
            year_spinner.setVisibility(View.VISIBLE);
            category_spinner.setVisibility(View.VISIBLE);
            category_spinner.setEnabled(false);
            place.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);

            // setting up category spinner
            String[] category = getResources().getStringArray(R.array.watching_category_string);

            ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,category);
            category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_spinner.setAdapter(category_adapter);
            // set selected item
            int spinnerPosition = category_adapter.getPosition(str_category);
            category_spinner.setSelection(spinnerPosition);

            //setting up language spinner
            String[] language = getResources().getStringArray(R.array.watching_language_string);

            ArrayAdapter<String> language_adater = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,language);
            language_adater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            language_spinner.setAdapter(language_adater);
            // set selected item
            if (str_language != null && !str_language.equals("")){
                int languageAdaterPositionPosition = language_adater.getPosition(str_language);
                language_spinner.setSelection(languageAdaterPositionPosition);
            }

            //year spinner
            String[] year_string = new String[30];
            for (int i=0,j=2020; i<30; i++,j--){
                year_string[i] = String.valueOf(j);
            }
            ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,year_string);
            year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year_spinner.setAdapter(year_adapter);
            if (str_year != null && !str_year.equals("")){
                int pos = year_adapter.getPosition(str_year);
                year_spinner.setSelection(pos);
            }

            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }

            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }

            if (str_category.toLowerCase().equals("movie")){
                price.setVisibility(View.VISIBLE);
                if (str_price != null && !str_price.equals("")){
                    etPrice.setText(str_price);
                }

            }else if (str_category.toLowerCase().equals("tv series") || str_category.toLowerCase().equals("drama")){
                yourTeamName.setVisibility(View.VISIBLE);
                opponentName.setVisibility(View.VISIBLE);
                yourTeamName.setHint("Season");
                opponentName.setHint("Episode");
                if (str_yourTeamName != null && !str_yourTeamName.equals("")){
                    etYourTeamName.setText(str_yourTeamName);
                }
                if (str_opponentName != null && !str_opponentName.equals("")){
                    etOpponetTeam.setText(str_opponentName);
                }
            }

        }else if (str_activity.toLowerCase().equals("eating")){
            // set visible components
            category_spinner.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);


            // setting up category spinner
            String[] category = getResources().getStringArray(R.array.eating_category_string);

            ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,category);
            category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_spinner.setAdapter(category_adapter);
            // set selected item
            int spinnerPosition = category_adapter.getPosition(str_category);
            category_spinner.setSelection(spinnerPosition);

            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }

            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            if (str_price != null && !str_price.equals("")){
                etPrice.setText(str_price);
            }


        }else if (str_activity.toLowerCase().equals("playing")){
            // set visible components
            category.setVisibility(View.VISIBLE);
            yourTeamName.setVisibility(View.VISIBLE);
            opponentName.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            status_spinner.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            year_spinner.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);

            if (str_category != null && str_category.equals("")){
                etCategory.setText(str_category);
            }
            if (str_yourTeamName != null && !str_yourTeamName.equals("")){
                etYourTeamName.setText(str_yourTeamName);
            }
            if (str_opponentName != null && !str_opponentName.equals("")){
                etOpponetTeam.setText(str_opponentName);
            }
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }

            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            if (str_price != null && !str_price.equals("")){
                etPrice.setText(str_price);
            }

            if (str_result != null && !str_result.equals("")){
                etResult.setText(str_result);
            }

            //setting up status spinner
            String[] status = getResources().getStringArray(R.array.playing_status_string);
            ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
            status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status_spinner.setAdapter(status_adapter);
            // set selected item
            if (str_status != null && !str_status.equals("")){
                int status_adapterPosition = status_adapter.getPosition(str_status);
                status_spinner.setSelection(status_adapterPosition);
            }

            //year spinner
            String[] year_string = new String[30];
            for (int i=0,j=2020; i<30; i++,j--){
                year_string[i] = String.valueOf(j);
            }
            ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,year_string);
            year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year_spinner.setAdapter(year_adapter);
            if (str_year != null && !str_year.equals("")){
                int pos = year_adapter.getPosition(str_year);
                year_spinner.setSelection(pos);
            }

        }else if (str_activity.toLowerCase().equals("working")){
            //set visible for suitable components
            category.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            status_spinner.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);

            if (str_category != null && !str_category.equals("")){
                etCategory.setText(str_category);
            }
            if (str_result != null && !str_result.equals("")){
                etResult.setText(str_result);
            }
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }
            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            //setting up status spinner
            String[] status = getResources().getStringArray(R.array.working_status_string);
            ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
            status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status_spinner.setAdapter(status_adapter);
            // set selected item
            if (str_status != null && !str_status.equals("")){
                int status_adapterPosition = status_adapter.getPosition(str_status);
                status_spinner.setSelection(status_adapterPosition);
            }

        }else if (str_activity.toLowerCase().equals("reading")){
            //set visible for suitable components
            category_spinner.setVisibility(View.VISIBLE);
            language.setVisibility(View.VISIBLE);
            year_spinner.setVisibility(View.VISIBLE);
            yourTeamName.setVisibility(View.VISIBLE);
            opponentName.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            status_spinner.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);

            if (str_language != null && !str_language.equals("")){
                etLanguage.setText(str_language);
            }
            yourTeamName.setHint("Author");
            opponentName.setHint("Translator");
            if (str_yourTeamName != null && !str_yourTeamName.equals("")){
                etYourTeamName.setText(str_yourTeamName);
            }
            if (str_opponentName != null && !str_opponentName.equals("")){
                etOpponetTeam.setText(str_opponentName);
            }
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }
            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            if (str_price != null && !str_price.equals("")){
                etPrice.setText(str_price);
            }
            //setting up status spinner
            String[] status = getResources().getStringArray(R.array.reading_status_string);
            ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
            status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status_spinner.setAdapter(status_adapter);
            // set selected item
            if (str_status != null && !str_status.equals("")){
                int status_adapterPosition = status_adapter.getPosition(str_status);
                status_spinner.setSelection(status_adapterPosition);
            }
            //year spinner
            String[] year_string = new String[50];
            for (int i=0,j=2020; i<50; i++,j--){
                year_string[i] = String.valueOf(j);
            }
            ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,year_string);
            year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year_spinner.setAdapter(year_adapter);
            if (str_year != null && !str_year.equals("")){
                int pos = year_adapter.getPosition(str_year);
                year_spinner.setSelection(pos);
            }
            // setting up category spinner
            String[] category = getResources().getStringArray(R.array.reading_catergory_string);

            ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,category);
            category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_spinner.setAdapter(category_adapter);
            if (str_category != null && !str_category.equals("")){
                int pos = year_adapter.getPosition(str_category);
                category_spinner.setSelection(pos);
            }

        }else if (str_activity.toLowerCase().equals("writing")){
            //set visible for suitable components
            category.setVisibility(View.VISIBLE);
            language.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            status_spinner.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);

            //setting up status spinner
            String[] status = getResources().getStringArray(R.array.writing_status_string);
            ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
            status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status_spinner.setAdapter(status_adapter);
            // set selected item
            if (str_status != null && !str_status.equals("")){
                int status_adapterPosition = status_adapter.getPosition(str_status);
                status_spinner.setSelection(status_adapterPosition);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }
            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_category != null && !str_category.equals("")){
                etCategory.setText(str_category);
            }
            if (str_language != null && !str_language.equals("")){
                etLanguage.setText(str_language);
            }
        }else if (str_activity.toLowerCase().equals("solving")){
            //set visible for suitable components
            category.setVisibility(View.VISIBLE);
            yourTeamName.setVisibility(View.VISIBLE);
            opponentName.setVisibility(View.VISIBLE);
            language_spinner.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            result_spinner.setVisibility(View.VISIBLE);
            status_spinner.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);
            yourTeamName.setHint("Organizer");
            opponentName.setHint("Algorithm");
            category.setHint("Difficulty");

            if (str_category != null && !str_category.equals("")){
                etCategory.setText(str_category);
            }
            if (str_yourTeamName != null && !str_yourTeamName.equals("")){
                etYourTeamName.setText(str_yourTeamName);
            }
            if (str_opponentName != null && !str_opponentName.equals("")){
                etOpponetTeam.setText(str_opponentName);
            }
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }
            //setting up language spinner
            String[] language = getResources().getStringArray(R.array.solving_language_string);

            ArrayAdapter<String> language_adater = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,language);
            language_adater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            language_spinner.setAdapter(language_adater);
            // set selected item
            if (str_language != null && !str_language.equals("")){
                int languageAdaterPositionPosition = language_adater.getPosition(str_language);
                language_spinner.setSelection(languageAdaterPositionPosition);
            }
            //setting up status spinner
            String[] status = getResources().getStringArray(R.array.solving_status_string);
            ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
            status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status_spinner.setAdapter(status_adapter);
            // set selected item
            if (str_status != null && !str_status.equals("")){
                int status_adapterPosition = status_adapter.getPosition(str_status);
                status_spinner.setSelection(status_adapterPosition);
            }

            //setting up result spinner
            String[] result = getResources().getStringArray(R.array.solving_status_string);
            ArrayAdapter<String> result_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,result);
            result_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            result_spinner.setAdapter(result_adapter);
            // set selected item
            if (str_result != null && !str_result.equals("")){
                int status_adapterPosition = result_adapter.getPosition(str_result);
                result_spinner.setSelection(status_adapterPosition);
            }

        }else if (str_activity.toLowerCase().equals("buying")){
            //set visible for suitable components
            category.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            year_spinner.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);

            if (str_category != null && !str_category.equals("")){
                etCategory.setText(str_category);
            }
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }
            if (str_price != null && !str_price.equals("")){
                etPrice.setText(str_price);
            }
            //year spinner
            String[] year_string = new String[50];
            for (int i=0,j=2020; i<50; i++,j--){
                year_string[i] = String.valueOf(j);
            }
            ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,year_string);
            year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year_spinner.setAdapter(year_adapter);
            if (str_year != null && !str_year.equals("")){
                int pos = year_adapter.getPosition(str_year);
                year_spinner.setSelection(pos);
            }

        }else if (str_activity.toLowerCase().equals("attending")){
            // set visible for suitable components
            category.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            status.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);

            if (str_category != null && !str_category.equals("")){
                etCategory.setText(str_category);
            }
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_review != null && !str_review.equals("")){
                etReview.setText(str_review);
            }
            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            if (str_status != null && !str_status.equals("")){
                etStatus.setText(str_status);
            }



        }else if (str_activity.toLowerCase().equals("wasting time")){
            // set visible suitable components
            place.setVisibility(View.VISIBLE);
            layout_duration.setVisibility(View.VISIBLE);
            result_spinner.setVisibility(View.VISIBLE);
            if (str_place != null && !str_place.equals("")){
                etPlace.setText(str_place);
            }
            if (str_duration != null && !str_duration.equals("")){
                convert_duration_into_hourMinutes(str_duration);
            }
            //setting up result spinner
            String[] result = getResources().getStringArray(R.array.wastingTime_result);
            ArrayAdapter<String> result_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,result);
            result_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            result_spinner.setAdapter(result_adapter);
            // set selected item
            if (str_result != null && !str_result.equals("")){
                int status_adapterPosition = result_adapter.getPosition(str_result);
                result_spinner.setSelection(status_adapterPosition);
            }

        }
    }
    public String get_current_DateTime_DayOfWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy, EEEE, hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }


    private void convert_duration_into_hourMinutes(String minutes) {
        if (!minutes.isEmpty() && !minutes.equals("0")) {
            int input_min = Integer.parseInt(minutes);
            int hour = input_min / 60;
            int minute = input_min - (hour * 60);
            etDurationMins.setText(String.valueOf(minute));
            etDurationHours.setText(String.valueOf(hour));
        }
    }
    private String convert_into_minutes(String Hours, String Minutes) {
        if (Hours != null & !Hours.isEmpty() && Minutes != null && !Minutes.isEmpty()) {
            return String.valueOf(Integer.parseInt(Hours) * 60 + Integer.parseInt(Minutes));
        } else if (Hours == null || Hours.isEmpty()) {
            return Minutes;
        } else {
            return String.valueOf(Integer.parseInt(Hours) * 60);
        }

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
    private String convert_date_format(String localDate,String localTime,String localDay){

        if (localDate.equals(get_current_Date())){
            return "Today at "+localTime;
        }
        try {
            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat targetFormat1 = new SimpleDateFormat("MMMM dd, yyyy");
            DateFormat targetFormat2 = new SimpleDateFormat("MMMM dd");
            Date date = originalFormat.parse(localDate);
            if (localDate.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                return targetFormat2.format(date)+", "+localDay+" at "+localTime;
            }else{
                return targetFormat1.format(date)+", "+localDay+ " at "+localTime;
            }


        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }

        return localDate+", "+localDay+ " at "+localTime;
    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

    private Boolean check_validation_review(TextInputLayout field) {
        if (field.getEditText().getText().toString().length() > 3) {
            field.setError("Length overflow");
            return false;
        } else {
            field.setError(null);
            field.setErrorEnabled(false);
            return true;
        }
    }

}
