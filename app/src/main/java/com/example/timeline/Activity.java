package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Activity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase mDatabase;
    private Spinner activity_spinner, watching_category, watching_language, watching_releaseYear, eating_category,
            playing_name, playing_status, working_status, reading_category, reading_status, writing_status,
            solving_language, solving_result, solving_status, wastingTime_result;
    private LinearLayout layout_watching, layout_eating, layout_playing, layout_working, layout_reading, layout_writing,
            layout_solving, layout_attending, layout_buying, layout_wasting_time, layout_Watching_SeasonEpisode;
    private TextInputLayout watching_price, playing_other_name, playing_result;
    private Button watching_btnSubmit, eating_btnSubmit, playing_btnSubmit, working_btnSubmit, reading_btnSubmit, writing_btnSubmit, solving_btnSubmit, attending_btnSubmit,
            buying_btnSubmit, wastingTime_btnSubmit;
    private String uid, userName;
    private TextInputLayout watching_name, watching_season, watching_episode, watching_place, watching_Hours, watching_minutes, watching_review, watching_notes;
    private TextInputLayout eating_name, eating_place, eating_price, eating_hours, eating_minutes, eating_review, eating_notes;
    private TextInputLayout playing_category, playing_yourTeam, playing_opponent, playing_place, playing_hours, playing_minutes, playing_year, playing_price,
            playing_review, playing_notes;
    private TextInputLayout working_name, working_category, working_place, working_hours, working_minutes, working_result, working_review, working_notes;
    private TextInputLayout reading_place, reading_name, reading_language, reading_year, reading_author, reading_translator, reading_price, reading_hours, reading_minutes,
            reading_review, reading_notes;
    private TextInputLayout writing_name, writing_category, writing_language, writing_hours, writing_minutes, writing_place, writing_review, writing_notes;
    private TextInputLayout solving_name, solving_organizer, solving_difficulty, solving_algorithm, solving_hours, solving_minutes, solving_place, solving_review, solving_notes;
    private TextInputLayout attending_name, attending_category, attending_place, attending_hours, attending_minutes, attending_status, attending_review, attending_notes;
    private TextInputLayout buying_name, buying_category, buying_year, buying_price, buying_place, buying_review, buying_notes;
    private TextInputLayout wastingTime_name, wastingTime_place, wastingTime_hours, wastingTime_minutes, wastingTime_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new ActivityFragmentNew()).commit();
//        }
        //Find layout id
        layout_eating = findViewById(R.id.layout_eating);
        layout_playing = findViewById(R.id.layout_playing);
        layout_watching = findViewById(R.id.layout_watching);
        layout_working = findViewById(R.id.layout_working);
        layout_reading = findViewById(R.id.layout_studying);
        layout_writing = findViewById(R.id.layout_writing);
        layout_solving = findViewById(R.id.layout_solving);
        layout_attending = findViewById(R.id.layout_attending);
        layout_buying = findViewById(R.id.layout_buying);
        layout_wasting_time = findViewById(R.id.layout_wasting_time);
        layout_Watching_SeasonEpisode = findViewById(R.id.layout_watching_SeasonEpisode);

        activity_spinner = findViewById(R.id.activity_spinner);

        find_view_by_id();
        get_user_info();
        set_activity_spinner_item();
        set_layout();       // this will set suitable layout according to activity choose
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        watching_btnSubmit.setOnClickListener(this);
        eating_btnSubmit.setOnClickListener(this);
        working_btnSubmit.setOnClickListener(this);
        reading_btnSubmit.setOnClickListener(this);
        writing_btnSubmit.setOnClickListener(this);
        playing_btnSubmit.setOnClickListener(this);
        attending_btnSubmit.setOnClickListener(this);
        buying_btnSubmit.setOnClickListener(this);
        solving_btnSubmit.setOnClickListener(this);
        wastingTime_btnSubmit.setOnClickListener(this);

    }

    private void find_view_by_id() {
        //watching section
        watching_category = findViewById(R.id.Watching_category_spinner);
        watching_name = findViewById(R.id.watching_name);
        watching_season = findViewById(R.id.watching_Season);
        watching_episode = findViewById(R.id.watching_Episode);
        watching_language = findViewById(R.id.Watching_language_spinner);
        watching_releaseYear = findViewById(R.id.Watching_releaseYear_spinner);
        watching_place = findViewById(R.id.watching_place);
        watching_Hours = findViewById(R.id.watching_durationHours);
        watching_minutes = findViewById(R.id.watching_durationMinutes);
        watching_price = findViewById(R.id.watching_price);
        watching_review = findViewById(R.id.watching_review);
        watching_notes = findViewById(R.id.watching_notes);
        watching_btnSubmit = findViewById(R.id.watching_submit);

        //eating section
        eating_name = findViewById(R.id.eating_title);
        eating_category = findViewById(R.id.eating_category_spinner);
        eating_place = findViewById(R.id.eating_place);
        eating_price = findViewById(R.id.eating_price);
        eating_hours = findViewById(R.id.eating_durationHours);
        eating_minutes = findViewById(R.id.eating_durationMinutes);
        eating_review = findViewById(R.id.eating_review);
        eating_notes = findViewById(R.id.eating_notes);
        eating_btnSubmit = findViewById(R.id.eating_submit);

        //playing section
        playing_name = findViewById(R.id.playing_name_spinner);
        playing_other_name = findViewById(R.id.playing_otherName);
        playing_category = findViewById(R.id.playing_category);     //game mode
        playing_yourTeam = findViewById(R.id.playing_YourTeamName);
        playing_opponent = findViewById(R.id.playing_opponentTeam);
        playing_place = findViewById(R.id.playing_place);
        playing_status = findViewById(R.id.playing_Status_spinner);
        playing_result = findViewById(R.id.playing_Result);
        playing_hours = findViewById(R.id.playing_durationHours);
        playing_minutes = findViewById(R.id.playing_durationMinutes);
        playing_year = findViewById(R.id.playing_year);
        playing_price = findViewById(R.id.playing_price);
        playing_review = findViewById(R.id.playing_review);
        playing_notes = findViewById(R.id.playing_notes);
        playing_btnSubmit = findViewById(R.id.playing_Submit);

        //working section
        working_name = findViewById(R.id.working_name);
        working_category = findViewById(R.id.working_category);
        working_place = findViewById(R.id.working_place);
        working_hours = findViewById(R.id.working_durationHours);
        working_minutes = findViewById(R.id.working_durationMinutes);
        working_status = findViewById(R.id.working_status_spinner);
        working_result = findViewById(R.id.working_result);
        working_review = findViewById(R.id.working_review);
        working_notes = findViewById(R.id.working_notes);
        working_btnSubmit = findViewById(R.id.working_Submit);

        //reading section
        reading_name = findViewById(R.id.reading_name);
        reading_category = findViewById(R.id.reading_catergory_spinner);
        reading_language = findViewById(R.id.reading_language);
        reading_year = findViewById(R.id.reading_year);
        reading_author = findViewById(R.id.reading_author);
        reading_translator = findViewById(R.id.reading_translator);
        reading_price = findViewById(R.id.reading_price);
        reading_place = findViewById(R.id.reading_place);
        reading_hours = findViewById(R.id.reading_durationHours);
        reading_minutes = findViewById(R.id.reading_durationMinutes);
        reading_status = findViewById(R.id.reading_status_spinner);
        reading_review = findViewById(R.id.reading_review);
        reading_notes = findViewById(R.id.reading_notes);
        reading_btnSubmit = findViewById(R.id.reading_Submit);

        //writing section
        writing_name = findViewById(R.id.writing_name);
        writing_category = findViewById(R.id.writing_category);
        writing_language = findViewById(R.id.writing_language);
        writing_hours = findViewById(R.id.writing_durationHours);
        writing_minutes = findViewById(R.id.writing_durationMinutes);
        writing_place = findViewById(R.id.writing_place);
        writing_status = findViewById(R.id.writing_status_spinner);
        writing_review = findViewById(R.id.writing_review);
        writing_notes = findViewById(R.id.writing_notes);
        writing_btnSubmit = findViewById(R.id.writing_Submit);

        //solving section
        solving_name = findViewById(R.id.solving_name);
        solving_organizer = findViewById(R.id.solving_organiser);
        solving_difficulty = findViewById(R.id.solving_category);
        solving_algorithm = findViewById(R.id.solving_algorithm);
        solving_language = findViewById(R.id.solving_language_spinner);
        solving_hours = findViewById(R.id.solving_durationHours);
        solving_minutes = findViewById(R.id.solving_durationMinutes);
        solving_place = findViewById(R.id.solving_place);
        solving_result = findViewById(R.id.solving_result_spinner);
        solving_status = findViewById(R.id.solving_status_spinner);
        solving_review = findViewById(R.id.solving_review);
        solving_notes = findViewById(R.id.solving_notes);
        solving_btnSubmit = findViewById(R.id.solving_Submit);

        //attending section
        attending_name = findViewById(R.id.attending_name);
        attending_category = findViewById(R.id.attending_category);
        attending_place = findViewById(R.id.attending_place);
        attending_hours = findViewById(R.id.attending_durationHours);
        attending_minutes = findViewById(R.id.attending_durationMinutes);
        attending_status = findViewById(R.id.attending_status);
        attending_review = findViewById(R.id.attending_review);
        attending_notes = findViewById(R.id.attending_notes);
        attending_btnSubmit = findViewById(R.id.attending_Submit);

        //buying section
        buying_name = findViewById(R.id.buying_name);
        buying_category = findViewById(R.id.buying_category);
        buying_year = findViewById(R.id.buying_year);
        buying_price = findViewById(R.id.buying_price);
        buying_place = findViewById(R.id.buying_place);
        buying_review = findViewById(R.id.buying_review);
        buying_notes = findViewById(R.id.buying_notes);
        buying_btnSubmit = findViewById(R.id.buying_Submit);

        //wastingTime section
        wastingTime_name = findViewById(R.id.wastingTime_name);
        wastingTime_place = findViewById(R.id.wastingTime_place);
        wastingTime_hours = findViewById(R.id.wastingTime_durationHours);
        wastingTime_minutes = findViewById(R.id.wastingTime_durationMinutes);
        wastingTime_result = findViewById(R.id.wastingTime_result_spinner);
        wastingTime_notes = findViewById(R.id.wastingTime_notes);
        wastingTime_btnSubmit = findViewById(R.id.wastingTime_Submit);
    }


    private void set_layout() {
        activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        set_visible_layout(0);
                        break;
                    case 1:
                        set_visible_layout(1);
                        set_all_watching_spinner();
                        break;
                    case 2:
                        set_visible_layout(2);
                        set_all_eating_spinner();
                        break;
                    case 3:
                        set_visible_layout(3);
                        set_all_playing_spinner();
                        break;
                    case 4:
                        set_visible_layout(4);
                        set_all_working_spinner();
                        break;
                    case 5:
                        set_visible_layout(5);
                        set_all_reading_spinner();
                        break;
                    case 6:
                        set_visible_layout(6);
                        set_all_writing_spinner();
                        break;
                    case 7:
                        set_visible_layout(7);
                        set_all_solving_spinner();
                        break;
                    case 8:
                        set_visible_layout(8);
                        break;
                    case 9:
                        set_visible_layout(9);
                        break;
                    case 10:
                        set_visible_layout(10);
                        set_all_wastingTime_spinner();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void set_visible_layout(int no) {
        if (no == 1) {
            layout_watching.setVisibility(View.VISIBLE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);
        } else if (no == 2) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.VISIBLE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);
        } else if (no == 3) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.VISIBLE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);
        } else if (no == 4) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.VISIBLE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);
        } else if (no == 5) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.VISIBLE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);
        } else if (no == 6) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.VISIBLE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);

        } else if (no == 7) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.VISIBLE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);

        } else if (no == 8) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.VISIBLE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);

        } else if (no == 9) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.VISIBLE);
            layout_wasting_time.setVisibility(View.GONE);
        } else if (no == 10) {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.VISIBLE);
        } else {
            layout_watching.setVisibility(View.GONE);
            layout_working.setVisibility(View.GONE);
            layout_playing.setVisibility(View.GONE);
            layout_eating.setVisibility(View.GONE);
            layout_reading.setVisibility(View.GONE);
            layout_writing.setVisibility(View.GONE);
            layout_solving.setVisibility(View.GONE);
            layout_attending.setVisibility(View.GONE);
            layout_buying.setVisibility(View.GONE);
            layout_wasting_time.setVisibility(View.GONE);
        }
    }

    private void set_activity_spinner_item() {

        String[] activity = getResources().getStringArray(R.array.activity_string);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activity);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_spinner.setAdapter(adapter);
    }

    private void set_all_watching_spinner() {
        //Category Spinner
        String[] category = getResources().getStringArray(R.array.watching_category_string);

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        watching_category.setAdapter(category_adapter);

        //Language spinner
        String[] language = getResources().getStringArray(R.array.watching_language_string);

        ArrayAdapter<String> language_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, language);
        language_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        watching_language.setAdapter(language_adapter);

        //Release year spinner
        String[] release_year = new String[30];
        for (int i = 0, year = 2020; i < 30; i++, year--) {
            release_year[i] = String.valueOf(year);
        }
        ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, release_year);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        watching_releaseYear.setAdapter(year_adapter);


        //implement item select click listner
        //category item select listner
        watching_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        layout_Watching_SeasonEpisode.setVisibility(View.GONE);
                        watching_price.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        layout_Watching_SeasonEpisode.setVisibility(View.VISIBLE);
                        watching_price.setVisibility(View.GONE);
                        break;
                    case 2:
                        layout_Watching_SeasonEpisode.setVisibility(View.VISIBLE);
                        watching_price.setVisibility(View.GONE);
                        break;
                    case 3:
                        layout_Watching_SeasonEpisode.setVisibility(View.GONE);
                        watching_price.setVisibility(View.GONE);
                        break;
                    case 4:
                        layout_Watching_SeasonEpisode.setVisibility(View.GONE);
                        watching_price.setVisibility(View.GONE);
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //String text = mySpinner.getSelectedItem().toString();

    }

    private void set_all_eating_spinner() {
        //category spinner
        String[] category = getResources().getStringArray(R.array.eating_category_string);

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eating_category.setAdapter(category_adapter);
    }

    private void set_all_playing_spinner() {
        //name spinner
        String[] name = getResources().getStringArray(R.array.playing_name_string);

        ArrayAdapter<String> name_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, name);
        name_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playing_name.setAdapter(name_adapter);

        //set selected item listner
        playing_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 14) {
                    //other option is selected
                    playing_other_name.setVisibility(View.VISIBLE);
                } else {
                    playing_other_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Status spinner
        String[] status = getResources().getStringArray(R.array.playing_status_string);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playing_status.setAdapter(status_adapter);

        // set status item listner
        playing_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 3) {
                    playing_result.setVisibility(View.GONE);
                } else {
                    playing_result.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void set_all_working_spinner() {
        //status spinner
        String[] status = getResources().getStringArray(R.array.working_status_string);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        working_status.setAdapter(status_adapter);

    }

    private void set_all_reading_spinner() {
        //reading category spinner
        String[] category = getResources().getStringArray(R.array.reading_catergory_string);

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reading_category.setAdapter(category_adapter);

        //reading status spinner
        String[] status = getResources().getStringArray(R.array.reading_status_string);

        ArrayAdapter<String> stauts_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        stauts_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reading_status.setAdapter(stauts_adapter);
    }

    private void set_all_writing_spinner() {
        //writing status spinner
        String[] status = getResources().getStringArray(R.array.writing_status_string);

        ArrayAdapter<String> stauts_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        stauts_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        writing_status.setAdapter(stauts_adapter);
    }

    private void set_all_solving_spinner() {
        //solving language spinner
        String[] language = getResources().getStringArray(R.array.solving_language_string);

        ArrayAdapter<String> language_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, language);
        language_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        solving_language.setAdapter(language_adapter);

        //solving result spinner
        String[] result = getResources().getStringArray(R.array.solving_result_string);

        ArrayAdapter<String> result_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, result);
        result_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        solving_result.setAdapter(result_adapter);

        //solving result spinner
        String[] status = getResources().getStringArray(R.array.solving_status_string);

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        solving_status.setAdapter(status_adapter);
    }

    private void set_all_wastingTime_spinner() {
        //wastingTime result spinner
        String[] result = getResources().getStringArray(R.array.wastingTime_result);

        ArrayAdapter<String> result_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, result);
        result_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wastingTime_result.setAdapter(result_adapter);
    }

    private void get_user_info() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin", false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid", "");
            userName = sharedPreferences.getString("userName", "");


        } else {
            this.finish();
            Toasty.error(getApplicationContext(), "User didn't log in", Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.watching_submit:
                //watching
                create_watching_info();
                break;
            case R.id.eating_submit:
                create_eating_info();
                break;
            case R.id.playing_Submit:
                create_playing_info();
                break;
            case R.id.working_Submit:
                create_working_info();
                break;
            case R.id.reading_Submit:
                create_reading_info();
                break;
            case R.id.writing_Submit:
                create_writing_info();
                break;
            case R.id.solving_Submit:
                create_solving_info();
                break;
            case R.id.attending_Submit:
                create_attending_info();
                break;
            case R.id.buying_Submit:
                create_buying_info();
                break;
            case R.id.wastingTime_Submit:
                create_wastingTime_info();
                break;
        }
    }

    private void create_watching_info() {
        if (watching_category == null || watching_category.getSelectedItem() == null || watching_category.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Category", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (!check_validation(watching_name) | !check_validation_review(watching_review)) {
            return;
        }
        String name, category, language = "", year = "", place, price = "0", season, episode, review, duration, notes;
        name = watching_name.getEditText().getText().toString().trim();
        category = watching_category.getSelectedItem().toString();
        if (watching_language != null && watching_language.getSelectedItem() != null && !watching_language.getSelectedItem().toString().isEmpty()) {
            language = watching_language.getSelectedItem().toString();
        }
        if (watching_releaseYear != null && watching_releaseYear.getSelectedItem() != null && !watching_releaseYear.getSelectedItem().toString().isEmpty()) {
            year = watching_releaseYear.getSelectedItem().toString();
        }
        place = watching_place.getEditText().getText().toString().trim();
        if (watching_price.getEditText() != null || !watching_price.getEditText().getText().toString().isEmpty()) {
            price = watching_price.getEditText().getText().toString();
        }
        season = watching_season.getEditText().getText().toString().trim();
        episode = watching_episode.getEditText().getText().toString().trim();
        duration = convert_into_minutes(watching_Hours.getEditText().getText().toString(), watching_minutes.getEditText().getText().toString());
        review = watching_review.getEditText().getText().toString();
        notes = watching_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, language);
        cv.put(TimelineDB.Activity.COLUMN_YEAR, year);
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, price);
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, season);
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, episode);
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, "");
        cv.put(TimelineDB.Activity.COLUMN_RESULT, "");
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_eating_info() {
        if (!check_validation(eating_name) | !check_validation_review(eating_review)) {
            return;
        }
        if (eating_category == null || eating_category.getSelectedItem() == null || eating_category.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Category", Toasty.LENGTH_SHORT).show();
            return;
        }
        String name, category, place, price = "0", review, duration, notes;
        name = eating_name.getEditText().getText().toString().trim();
        category = eating_category.getSelectedItem().toString();
        place = eating_place.getEditText().getText().toString().trim();
        if (eating_price.getEditText() != null || !eating_price.getEditText().getText().toString().isEmpty()) {
            price = eating_price.getEditText().getText().toString();
        }
        duration = convert_into_minutes(eating_hours.getEditText().getText().toString(), eating_minutes.getEditText().getText().toString());
        review = eating_review.getEditText().getText().toString();
        notes = eating_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, "");
        cv.put(TimelineDB.Activity.COLUMN_YEAR, "");
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, price);
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, "");
        cv.put(TimelineDB.Activity.COLUMN_RESULT, "");
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_playing_info() {
        if (playing_name.getSelectedItem() == null || playing_name.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Name", Toasty.LENGTH_SHORT).show();
            return;
        }
        String str_name = playing_name.getSelectedItem().toString();
        if (playing_name.getSelectedItem().toString().equals("Other")) {
            if (!check_validation(playing_other_name)) {
                return;
            }
            str_name = playing_other_name.getEditText().getText().toString().trim();
        }
        if (!check_validation(playing_category) | !check_validation_review(playing_review)) {
            return;
        }
        if (playing_status.getSelectedItem() == null || playing_status.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select status", Toasty.LENGTH_SHORT).show();
            return;
        }
        String str_category, str_yourTeam, str_opponentTeam, str_place, str_status, str_result, str_duration, str_year, str_price = "0", str_review, str_notes;
        str_category = playing_category.getEditText().getText().toString().trim();
        str_yourTeam = playing_yourTeam.getEditText().getText().toString().trim();
        str_opponentTeam = playing_opponent.getEditText().getText().toString().trim();
        str_place = playing_place.getEditText().getText().toString().trim();
        str_status = playing_status.getSelectedItem().toString();
        str_result = playing_result.getEditText().getText().toString().trim();
        str_duration = convert_into_minutes(playing_hours.getEditText().getText().toString(), playing_minutes.getEditText().getText().toString());
        str_year = playing_year.getEditText().getText().toString().trim();
        if (!playing_price.getEditText().getText().toString().isEmpty()) {
            str_price = playing_price.getEditText().getText().toString();
        }
        str_review = playing_review.getEditText().getText().toString();
        str_notes = playing_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, str_name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, str_category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, "");
        cv.put(TimelineDB.Activity.COLUMN_YEAR, str_year);
        cv.put(TimelineDB.Activity.COLUMN_PLACE, str_place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, str_price);
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, str_yourTeam);
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, str_opponentTeam);
        cv.put(TimelineDB.Activity.COLUMN_DURATION, str_duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, str_review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, str_notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, str_status);
        cv.put(TimelineDB.Activity.COLUMN_RESULT, str_result);
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_working_info() {
        if (!check_validation(working_name) | !check_validation_review(working_review)) {
            return;
        }
        if (working_status.getSelectedItem() == null || working_status.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Status", Toasty.LENGTH_SHORT).show();
            return;
        }
        String name = working_name.getEditText().getText().toString().trim();
        String category = working_category.getEditText().getText().toString().trim();
        String place = working_place.getEditText().getText().toString().trim();
        String duration = convert_into_minutes(working_hours.getEditText().getText().toString(), working_minutes.getEditText().getText().toString());
        String status = working_status.getSelectedItem().toString();
        String result = working_result.getEditText().getText().toString().trim();
        String review = working_review.getEditText().getText().toString();
        String notes = working_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, "");
        cv.put(TimelineDB.Activity.COLUMN_YEAR, "");
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, "0");
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, status);
        cv.put(TimelineDB.Activity.COLUMN_RESULT, result);
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_reading_info() {
        if (!check_validation(reading_name) | !check_validation_review(reading_review)) {
            return;
        }
        if (reading_category.getSelectedItem() == null || reading_category.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select category", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (reading_status.getSelectedItem() == null || reading_status.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Status", Toasty.LENGTH_SHORT).show();
            return;
        }
        String name = reading_name.getEditText().getText().toString().trim();
        String category = reading_category.getSelectedItem().toString();
        String language = reading_language.getEditText().getText().toString();
        String year = reading_year.getEditText().getText().toString().trim();
        String author = reading_author.getEditText().getText().toString().trim();
        String translator = reading_translator.getEditText().getText().toString().trim();
        String price = "0";
        if (!reading_price.getEditText().getText().toString().isEmpty()) {
            price = reading_price.getEditText().getText().toString();
        }
        String place = reading_place.getEditText().getText().toString().trim();
        String duration = convert_into_minutes(reading_hours.getEditText().getText().toString(), reading_minutes.getEditText().getText().toString());
        String status = reading_status.getSelectedItem().toString();
        String review = reading_review.getEditText().getText().toString();
        String notes = reading_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, language);
        cv.put(TimelineDB.Activity.COLUMN_YEAR, year);
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, price);
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, author);
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, translator);
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, status);
        cv.put(TimelineDB.Activity.COLUMN_RESULT, "");
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_writing_info() {
        if (!check_validation(writing_name) | !check_validation(writing_category) | !check_validation_review(writing_review)) {
            return;
        }
        if (writing_status.getSelectedItem() == null || writing_status.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Status", Toasty.LENGTH_SHORT).show();
            return;
        }
        String name = writing_name.getEditText().getText().toString().trim();
        String category = writing_category.getEditText().getText().toString().trim();
        String language = writing_language.getEditText().getText().toString().trim();
        String duration = convert_into_minutes(writing_hours.getEditText().getText().toString(), writing_minutes.getEditText().getText().toString());
        String place = writing_place.getEditText().getText().toString().trim();
        String status = writing_status.getSelectedItem().toString();
        String review = writing_review.getEditText().getText().toString();
        String notes = writing_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, language);
        cv.put(TimelineDB.Activity.COLUMN_YEAR, "");
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, "0");
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, status);
        cv.put(TimelineDB.Activity.COLUMN_RESULT, "");
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_solving_info() {
        if (!check_validation(solving_name) | !check_validation_review(solving_review)) {
            return;
        }
        if (solving_language.getSelectedItem() == null || solving_language.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Language", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (solving_status.getSelectedItem() == null || solving_status.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Status", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (solving_result.getSelectedItem() == null || solving_result.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Language", Toasty.LENGTH_SHORT).show();
            return;
        }
        String name = solving_name.getEditText().getText().toString().trim();
        String category = solving_difficulty.getEditText().getText().toString().trim();
        String organizer = solving_organizer.getEditText().getText().toString().trim();
        String algorithm = solving_algorithm.getEditText().getText().toString().trim();
        String language = solving_language.getSelectedItem().toString();
        String status = solving_status.getSelectedItem().toString();
        String result = solving_result.getSelectedItem().toString();
        String duration = convert_into_minutes(solving_hours.getEditText().getText().toString(), solving_minutes.getEditText().getText().toString());
        String place = solving_place.getEditText().getText().toString().trim();
        String review = solving_review.getEditText().getText().toString();
        String notes = solving_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, language);
        cv.put(TimelineDB.Activity.COLUMN_YEAR, "");
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, "0");
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, organizer);
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, algorithm);
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, status);
        cv.put(TimelineDB.Activity.COLUMN_RESULT, result);
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();
    }

    private void create_attending_info() {
        if (!check_validation(attending_name) | !check_validation(attending_category) | !check_validation_review(attending_review)) {
            return;
        }
        String name = attending_name.getEditText().getText().toString().trim();
        String category = attending_category.getEditText().getText().toString().trim();
        String place = attending_place.getEditText().getText().toString().trim();
        String status = attending_status.getEditText().getText().toString().trim();
        String review = attending_review.getEditText().getText().toString();
        String notes = attending_notes.getEditText().getText().toString();
        String duration = convert_into_minutes(attending_hours.getEditText().getText().toString(), attending_minutes.getEditText().getText().toString());

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, "");
        cv.put(TimelineDB.Activity.COLUMN_YEAR, "");
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, "0");
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, status);
        cv.put(TimelineDB.Activity.COLUMN_RESULT, "");
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_buying_info() {
        if (!check_validation(buying_name) | !check_validation(buying_category) | !check_validation_review(buying_review)) {
            return;
        }
        String name = buying_name.getEditText().getText().toString().trim();
        String category = buying_category.getEditText().getText().toString().trim();
        String place = buying_place.getEditText().getText().toString().trim();
        String year = buying_year.getEditText().getText().toString().trim();
        String price = "0";
        if (!buying_price.getEditText().getText().toString().isEmpty()) {
            price = buying_price.getEditText().getText().toString();
        }
        String review = buying_review.getEditText().getText().toString();
        String notes = buying_notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, category);
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, "");
        cv.put(TimelineDB.Activity.COLUMN_YEAR, year);
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, price);
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_DURATION, "");
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, review);
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, "");
        cv.put(TimelineDB.Activity.COLUMN_RESULT, "");
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
        this.finish();

    }

    private void create_wastingTime_info() {
        if (!check_validation(wastingTime_name)) {
            return;
        }
        if (wastingTime_result.getSelectedItem() == null || wastingTime_result.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select result", Toasty.LENGTH_SHORT).show();
            return;
        }
        String name = wastingTime_name.getEditText().getText().toString().trim();
        String place = wastingTime_place.getEditText().getText().toString().trim();
        String duration = convert_into_minutes(wastingTime_hours.getEditText().getText().toString(), wastingTime_minutes.getEditText().getText().toString());
        String notes = wastingTime_notes.getEditText().getText().toString();
        String result = wastingTime_result.getSelectedItem().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.Activity.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE, activity_spinner.getSelectedItem().toString());
        cv.put(TimelineDB.Activity.COLUMN_NAME, name);
        cv.put(TimelineDB.Activity.COLUMN_CATEGORY, "");
        cv.put(TimelineDB.Activity.COLUMN_LANGUAGE, "");
        cv.put(TimelineDB.Activity.COLUMN_YEAR, "");
        cv.put(TimelineDB.Activity.COLUMN_PLACE, place);
        cv.put(TimelineDB.Activity.COLUMN_PRICE, "0");
        cv.put(TimelineDB.Activity.COLUMN_YOUR_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_OPPONENT_TEAM, "");
        cv.put(TimelineDB.Activity.COLUMN_DURATION, duration);
        cv.put(TimelineDB.Activity.COLUMN_REVIEW, "");
        cv.put(TimelineDB.Activity.COLUMN_NOTES, notes);
        cv.put(TimelineDB.Activity.COLUMN_DATE, get_current_Date());
        cv.put(TimelineDB.Activity.COLUMN_TIME, get_current_Time());
        cv.put(TimelineDB.Activity.COLUMN_STATUS, "");
        cv.put(TimelineDB.Activity.COLUMN_RESULT, result);
        cv.put(TimelineDB.Activity.COLUMN_DAYOFWEEK, get_current_DayOfWeek());
        cv.put(TimelineDB.Activity.COLUMN_MODIFIED_DATE, "");
        cv.put(TimelineDB.Activity.COLUMN_BACKUP, "0");
        long id = mDatabase.insert(TimelineDB.Activity.TABLE_NAME, null, cv);
        Toasty.success(getApplicationContext(), "Saved", Toasty.LENGTH_SHORT).show();
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

    private String convert_into_minutes(String Hours, String Minutes) {
        if (Hours != null & !Hours.isEmpty() && Minutes != null && !Minutes.isEmpty()) {
            return String.valueOf(Integer.parseInt(Hours) * 60 + Integer.parseInt(Minutes));
        } else if (Hours == null || Hours.isEmpty()) {
            return Minutes;
        } else {
            return String.valueOf(Integer.parseInt(Hours) * 60);
        }

    }

    public String get_current_Date() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

    public String get_current_Time() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

    private String get_current_DayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = "";
        switch (day) {
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;
        }
        return dayOfWeek;
    }


//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment selectedFragment = null;
//                    switch (item.getItemId()) {
//                        case R.id.Anav_add:
//                            selectedFragment = new ActivityFragmentNew();
//                            break;
//                        case R.id.Anav_analysis:
//                            selectedFragment = new ActivityFragmentAnalysis();
//                            break;
//                        case R.id.Anav_details:
//                            selectedFragment = new ActivityFragmentDetails();
//                            break;
//                    }
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
//                    return true;
//                }
//            };
}
