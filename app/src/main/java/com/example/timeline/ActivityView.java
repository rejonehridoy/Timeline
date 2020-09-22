package com.example.timeline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityView extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase mDatabase;
    private int ID;
    private TextView activity;
    private TextInputLayout name, category, language, year, place, yourTeamName, opponentName, duration, price, status, date, modifedDate,
            notes, review, result;
    private TextInputEditText etName, etCategory, etLanguage, etYear, etPlace, etYourTeamName, etOpponetTeam, etPrice, etStatus, etDate, etModifiedDate,
            etDuration, etNotes, etReview, etResult;
    private String str_activity, str_name, str_category, str_language, str_year, str_place, str_yourTeamName, str_opponentName, str_duration,
            str_price, str_status, str_date, str_modifiedDate, str_notes, str_review, str_result, str_time, str_dayOfWeek;

    private Button btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // find view by id everything
        activity = findViewById(R.id.AV_activityName);
        name = findViewById(R.id.AV_name);
        category = findViewById(R.id.AV_category);
        language = findViewById(R.id.AV_language);
        year = findViewById(R.id.AV_RelaseYear);
        place = findViewById(R.id.AV_place);
        yourTeamName = findViewById(R.id.AV_yourTeam);
        opponentName = findViewById(R.id.AV_opponetTeam);
        duration = findViewById(R.id.AV_duration);
        price = findViewById(R.id.AV_price);
        status = findViewById(R.id.AV_status);
        date = findViewById(R.id.AV_date);
        modifedDate = findViewById(R.id.AV_modifiedDate);
        notes = findViewById(R.id.AV_notes);
        review = findViewById(R.id.AV_review);
        result = findViewById(R.id.AV_result);

        etName = findViewById(R.id.AV_name_input);
        etCategory = findViewById(R.id.AV_category_input);
        etLanguage = findViewById(R.id.AV_language_input);
        etYear = findViewById(R.id.AV_RelaseYear_input);
        etPlace = findViewById(R.id.AV_place_input);
        etYourTeamName = findViewById(R.id.AV_yourTeam_input);
        etOpponetTeam = findViewById(R.id.AV_opponentTeam_input);
        etDuration = findViewById(R.id.AV_duration_input);
        etPrice = findViewById(R.id.AV_price_input);
        etStatus = findViewById(R.id.AV_status_input);
        etDate = findViewById(R.id.AV_date_input);
        etModifiedDate = findViewById(R.id.AV_modifiedDate_input);
        etNotes = findViewById(R.id.AV_notes_input);
        etReview = findViewById(R.id.AV_review_input);
        etResult = findViewById(R.id.AV_result_input);

        btnEdit = findViewById(R.id.AV_btn_edit);
        btnDelete = findViewById(R.id.AV_btn_delete);


        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        if (ID < 1) {
            Toasty.error(getApplicationContext(), "Invalid Operation", Toasty.LENGTH_SHORT).show();
            this.finish();
        }
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();


        // fetch every data from database corresponding to id
        get_info_from_database();

        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_info_from_database();
    }

    private void get_info_from_database() {
        //get data from database and store data into str varibles

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


        //set the info into textboxes
        activity.setText(str_activity);
        etName.setText(str_name);
        etCategory.setText(str_category);
        etDate.setText(convert_date_format(str_date,str_time,str_dayOfWeek));
        if (str_language != null && !str_language.equals("")) {
            language.setVisibility(View.VISIBLE);
            etLanguage.setText(str_language);
        }
        if (str_year != null && !str_year.equals("")) {
            year.setVisibility(View.VISIBLE);
            etYear.setText(str_year);
        }
        if (str_price != null && !str_price.equals("")) {
            price.setVisibility(View.VISIBLE);
            etPrice.setText(str_price);
        }
        if (str_activity.toLowerCase().equals("watching") && str_category.toLowerCase().equals("tv series")) {
            if (str_yourTeamName != null && !str_yourTeamName.equals("")) {
                yourTeamName.setVisibility(View.VISIBLE);
                yourTeamName.setHint("Season");
                etYourTeamName.setText(str_yourTeamName);
            }
            if (str_opponentName != null && !str_opponentName.equals("")) {
                opponentName.setVisibility(View.VISIBLE);
                opponentName.setHint("Episode");
                etOpponetTeam.setText(str_opponentName);
            }
        } else if (str_activity.toLowerCase().equals("playing")) {
            if (str_yourTeamName != null && !str_yourTeamName.equals("")) {
                yourTeamName.setVisibility(View.VISIBLE);
                yourTeamName.setHint("Your Team");
                etYourTeamName.setText(str_yourTeamName);
            }
            if (str_opponentName != null && !str_opponentName.equals("")) {
                opponentName.setVisibility(View.VISIBLE);
                opponentName.setHint("Opponent Team");
                etOpponetTeam.setText(str_opponentName);
            }
        } else if (str_activity.toLowerCase().equals("reading")) {
            if (str_yourTeamName != null && !str_yourTeamName.equals("")) {
                yourTeamName.setVisibility(View.VISIBLE);
                yourTeamName.setHint("Author");
                etYourTeamName.setText(str_yourTeamName);
            }
            if (str_opponentName != null && !str_opponentName.equals("")) {
                opponentName.setVisibility(View.VISIBLE);
                opponentName.setHint("Translator");
                etOpponetTeam.setText(str_opponentName);
            }
        } else if (str_activity.toLowerCase().equals("solving")) {
            if (str_yourTeamName != null && !str_yourTeamName.equals("")) {
                yourTeamName.setVisibility(View.VISIBLE);
                yourTeamName.setHint("Organizer");
                etYourTeamName.setText(str_yourTeamName);
            }
            if (str_opponentName != null && !str_opponentName.equals("")) {
                opponentName.setVisibility(View.VISIBLE);
                opponentName.setHint("Algoirthm");
                etOpponetTeam.setText(str_opponentName);
            }
        }


        if (str_result != null && !str_result.equals("")) {
            result.setVisibility(View.VISIBLE);
            etResult.setText(str_result);
        }

        if (str_duration != null && !str_duration.equals("")) {
            duration.setVisibility(View.VISIBLE);
            etDuration.setText(convert_duration_into_hourMinutes(str_duration));
        }

        if (str_place != null && !str_place.equals("")) {
            place.setVisibility(View.VISIBLE);
            etPlace.setText(str_place);
        }

        if (str_status != null && !str_status.equals("")) {
            status.setVisibility(View.VISIBLE);
            etStatus.setText(str_status);
        }

        if (str_review != null && !str_review.equals("")) {
            review.setVisibility(View.VISIBLE);
            etReview.setText(str_review);
        }

        if (str_modifiedDate != null && !str_modifiedDate.equals("")) {
            modifedDate.setVisibility(View.VISIBLE);
            etModifiedDate.setText(str_modifiedDate);
        }

        if (str_notes != null && !str_notes.equals("")) {
            notes.setVisibility(View.VISIBLE);
            etNotes.setText(str_notes);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AV_btn_edit:
                // edit button
                Intent in = new Intent(ActivityView.this, com.example.timeline.ActivityEdit.class);
                in.putExtra("ID", ID);
                startActivity(in);

                break;
            case R.id.AV_btn_delete:
                // alert dialogue will show for confiramtion
                open_delete_dialogue();

                break;

        }
    }
    private void open_delete_dialogue(){
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete Activity")
                .setMessage("Are you sure to delete this activity? If deleted it can not be undone")
                .setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked
                        delete_activity();
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
    private void delete_activity(){
        mDatabase.delete(TimelineDB.Activity.TABLE_NAME, TimelineDB.Activity._ID + "=" + ID, null);
        Toasty.success(getApplicationContext(), "Deleted", Toasty.LENGTH_SHORT).show();
        this.finish();
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
    private String convert_duration_into_hourMinutes(String minutes) {
        if (!minutes.isEmpty() && !minutes.equals("0")) {
            int input_min = Integer.parseInt(minutes);
            int hour = input_min / 60;
            int minute = input_min - (hour * 60);
            if (hour == 0) {
                return  minute + " minutes";
            } else if (minute ==0){
                return hour + " hours";
            }else{
                return hour + " hours " + minute + " minutes";
            }
        } else {
            return "Duration : N/A";
        }

    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
