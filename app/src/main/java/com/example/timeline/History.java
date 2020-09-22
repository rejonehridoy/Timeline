package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class History extends AppCompatActivity {
    private Toolbar all_toolbar,analysis_toolbar;
    private SQLiteDatabase mDatabase;
    private LinearLayout all_layout,analysis_layout;
    private RecyclerView info_recylerView;
    private ActivityInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ActivityInfo> infos = new ArrayList<ActivityInfo>();
    private String uid,userName;
    //analysis section
    private Spinner analysis_spinner;
    private TextView watching_movies,watching_tvSeries,watching_others,watching_duration,watching_total_cost,watching_SeriesDrama_duration,watching_tutorialLecture_duration;
    private TextView eating_food_items,eating_places,eating_most_eaten,eating_duration,eating_total_cost;
    private TextView playing_distinct_game,playing_most_played,most_played_game_title,playing_matches,playing_win_lose,playing_win_ratio,playing_duration;
    private TextView working_works,working_duration,working_complete;
    private TextView reading_books,reading_category_mostRead,reading_total_cost,reading_duration;
    private TextView solving_problems,solving_languages,solving_accepted,solving_ratio,solving_duration;
    private TextView writing_item,writing_completed,writing_duration,writing_language;
    private TextView buying_items,buying_total_costs;
    private TextView attending_function,attending_duration;
    private TextView wastingTime_ratio,wastingTime_duration;
    private String extra_added_query="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        all_layout = findViewById(R.id.history_layout_all);
        analysis_layout = findViewById(R.id.history_layout_analysis);
        // this is for analysis toolbar
        analysis_toolbar = findViewById(R.id.History_Analysis_toolbar);
        analysis_toolbar.setTitle("Analysis");
        setSupportActionBar(analysis_toolbar);
        // this is for all toolbar
        all_toolbar = findViewById(R.id.History_All_toolbar);
        all_toolbar.setTitle("History");
        setSupportActionBar(all_toolbar);


        find_view_by_analysis_id();
        get_user_info();

        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();

        get_analysis_info();

        info_recylerView = findViewById(R.id.history_recylerView);
        get_all_info();

        BottomNavigationView bottomNav = findViewById(R.id.history_bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        analysis_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    //all time analysis

                    extra_added_query = "";
                    get_analysis_info();
                }else if (i==1){
                    //last 7 days
                    String extra_day = "7";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();

                }else if (i==2){
                    //last 14 days
                    String extra_day = "14";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();

                }else if (i==3){
                    //last 21 days
                    String extra_day = "21";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();

                }else if (i==4){
                    //last 1 month
                    String extra_day = "30";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();

                }else if (i==5){
                    //last 2 months
                    String extra_day = "60";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();
                }else if (i==6){
                    //last 3 months
                    String extra_day = "90";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();
                }else if (i==7){
                    //last 6 months
                    String extra_day = "180";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();
                }else if (i==8){
                    //last 1 year
                    String extra_day = "365";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();
                }else if (i==9){
                    //last 2 years
                    String extra_day = "730";
                    extra_added_query = " and (select substr(date, 7, 4)||'-'||substr(date, 4,2)||'-'||substr(date, 1,2)) > date('now','-"+extra_day+" day')";
                    get_analysis_info();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_info();
        get_analysis_info();
    }

    private void find_view_by_analysis_id(){
        analysis_spinner = findViewById(R.id.History_analysis_spinner);
        watching_movies = findViewById(R.id.HW_movies);
        watching_tvSeries = findViewById(R.id.HW_TVseries);
        watching_others = findViewById(R.id.HW_others);
        watching_duration = findViewById(R.id.HW_duration);
        watching_total_cost = findViewById(R.id.HW_totalCost);
        watching_SeriesDrama_duration = findViewById(R.id.HW_seriesDrama_duration);
        watching_tutorialLecture_duration = findViewById(R.id.HW_LectureTutorial_duration);

        eating_food_items = findViewById(R.id.HE_food_items);
        eating_places = findViewById(R.id.HE_places);
        eating_most_eaten = findViewById(R.id.HE_most_eaten_item);
        eating_duration = findViewById(R.id.HE_duration);
        eating_total_cost = findViewById(R.id.HE_food_cost);

        playing_distinct_game = findViewById(R.id.HP_distinct_game);
        playing_most_played = findViewById(R.id.HP_most_played_game);
        playing_matches = findViewById(R.id.HP_matches_most_played_game);
        most_played_game_title = findViewById(R.id.HP_matches_most_played_game_title);
        playing_win_lose = findViewById(R.id.HP_win_lose);
        playing_win_ratio = findViewById(R.id.HP_win_ratio);
        playing_duration = findViewById(R.id.HP_duration);

        working_works = findViewById(R.id.HWK_works);
        working_duration = findViewById(R.id.HWK_duration);
        working_complete = findViewById(R.id.HWK_completed);

        reading_books = findViewById(R.id.HR_books);
        reading_category_mostRead = findViewById(R.id.HR_category_most_read);
        reading_duration = findViewById(R.id.HR_duration);
        reading_total_cost = findViewById(R.id.HR_total_cost);

        solving_problems = findViewById(R.id.HS_problems);
        solving_languages = findViewById(R.id.HS_language);
        solving_accepted = findViewById(R.id.HS_accepted);
        solving_duration = findViewById(R.id.HS_duration);
        solving_ratio = findViewById(R.id.HS_solving_ratio);

        writing_item = findViewById(R.id.HWR_items);
        writing_completed = findViewById(R.id.HWR_completed);
        writing_duration = findViewById(R.id.HWR_duration);
        writing_language = findViewById(R.id.HWR_language);

        buying_items = findViewById(R.id.HB_items);
        buying_total_costs = findViewById(R.id.HB_total_cost);

        attending_function = findViewById(R.id.HA_function);
        attending_duration = findViewById(R.id.HA_duration);

        wastingTime_ratio  = findViewById(R.id.HWT_positive_ratio);
        wastingTime_duration  = findViewById(R.id.HWT_duration);

        //setting up result spinner
        String[] analysis = getResources().getStringArray(R.array.history_analysis_string);
        ArrayAdapter<String> analysis_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,analysis);
        analysis_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        analysis_spinner.setAdapter(analysis_adapter);

    }
    private void get_analysis_info(){
        //watching section
        Cursor cursor1 = mDatabase.rawQuery("select count(_id) from Activity where category like 'movie' and activityType like 'watching'"+extra_added_query,null);
        if (cursor1.moveToFirst()){
            watching_movies.setText(cursor1.getString(0));
        }
        Cursor cursor2 = mDatabase.rawQuery("select count(_id) from Activity where category like 'tv series' and activityType like 'watching'"+extra_added_query,null);
        if (cursor2.moveToFirst()){
            watching_tvSeries.setText(cursor2.getString(0));
        }
        Cursor cursor3 = mDatabase.rawQuery("select count(_id) from Activity where category not in ('Movie','TV Series') and activityType like 'watching'"+extra_added_query,null);
        if (cursor3.moveToFirst()){
            watching_others.setText(cursor3.getString(0));
        }
        Cursor cursor4 = mDatabase.rawQuery("select sum(duration) as TotalDuration,sum(price) as TotalCost from Activity where activityType like 'watching' and category like 'movie' "+extra_added_query,null);
        if (cursor4.moveToFirst()){
            watching_duration.setText(convert_duration_into_hourMinutes(cursor4.getString(0)));
            watching_total_cost.setText(cursor4.getString(1));
        }

        // tv series and drama duration
        Cursor cursor27 = mDatabase.rawQuery("select sum(duration) as TotalDuration from Activity where activityType like 'watching' and (category like 'TV Series' or category like 'Drama' ) "+extra_added_query,null);
        if (cursor27.moveToFirst()){
            watching_SeriesDrama_duration.setText(convert_duration_into_hourMinutes(cursor27.getString(0)));
        }
        //tutorial and video lecture duration
        Cursor cursor28 = mDatabase.rawQuery("select sum(duration) as TotalDuration,sum(price) as TotalCost from Activity where activityType like 'watching' and (category like 'Tutorial' or category like 'Video Lecture') "+extra_added_query,null);
        if (cursor28.moveToFirst()){
            watching_tutorialLecture_duration.setText(convert_duration_into_hourMinutes(cursor28.getString(0)));
        }

        //eating section
        Cursor cursor5 = mDatabase.rawQuery("select count(distinct lower(name)) as FoodItem,count(distinct lower( place)) as Place, sum(price) as TotalPricePrice,sum(duration) as TotalTime from Activity where activityType like 'eating'"+extra_added_query,null);
        if (cursor5.moveToFirst()){
            eating_food_items.setText(cursor5.getString(0));
            eating_places.setText(cursor5.getString(1));
            eating_total_cost.setText(cursor5.getString(2));
            eating_duration.setText(convert_duration_into_hourMinutes(cursor5.getString(3)));
        }
        Cursor cursor6 = mDatabase.rawQuery("select name,count(name) as TotalNumber from Activity where activityType like 'eating'"+ extra_added_query+ " group by lower(name) order by TotalNumber desc limit 1",null);
        if (cursor6.moveToFirst()){
            eating_most_eaten.setText(cursor6.getString(0));
        }


        //playing section
        Cursor cursor7 = mDatabase.rawQuery("select count(distinct lower(name)) as DistinctGame from Activity where activityType like 'playing'"+extra_added_query,null);
        if (cursor7.moveToFirst()){
            playing_distinct_game.setText(cursor7.getString(0));
        }
        Cursor cursor8 = mDatabase.rawQuery("select name, count(name) as PlayedTimes from Activity where activityType like 'playing' "+extra_added_query+" group by lower(name) order by PlayedTimes desc limit 1",null);
        String most_played_game="";
        if (cursor8.moveToFirst()){
            most_played_game = cursor8.getString(0);
            playing_most_played.setText(most_played_game);
        }
        double matches=0.0;
        Cursor cursor9 = mDatabase.rawQuery("select count(_id) as Matches from Activity where activityType like 'playing' and name like '"+most_played_game+"'"+extra_added_query,null);
        if (cursor9.moveToFirst()){
            matches = Double.parseDouble(cursor9.getString(0));
            playing_matches.setText(cursor9.getString(0));
            most_played_game_title.setText(most_played_game+ " Matches");
        }
        String win="",lose="";
        Cursor cursor10 = mDatabase.rawQuery("select count(_id) as NoOFWin from Activity where activityType like 'playing' and name like 'fifa' and status like 'win'"+extra_added_query,null);
        if (cursor10.moveToFirst()){
            win = cursor10.getString(0);
        }
        Cursor cursor11 = mDatabase.rawQuery("select count(_id) as NoOfLose from Activity where activityType like 'playing' and name like 'fifa' and status like 'lose'"+extra_added_query,null);
        if (cursor11.moveToFirst()){
            lose = cursor11.getString(0);
            playing_win_lose.setText(win+"/"+lose);
            double match_win=Double.parseDouble(win),match_lose=Double.parseDouble(lose);
            if (matches!=0){
                double ratio = (match_win/matches)*100;
                NumberFormat formatter = new DecimalFormat("#0.0");
                playing_win_ratio.setText(formatter.format(ratio)+"%");
            }else{
                playing_win_ratio.setText("N/A");
            }

        }
        Cursor cursor12 = mDatabase.rawQuery("select sum(duration) as TotalDuration from Activity where activityType like 'playing'"+extra_added_query,null);
        if (cursor12.moveToFirst()){
            playing_duration.setText(convert_duration_into_hourMinutes(cursor12.getString(0)));
        }



        //working section
        Cursor cursor14 = mDatabase.rawQuery("select count(_id) as TotalWorks,sum(duration) as TotalDuration from Activity where activityType like 'working'"+extra_added_query,null);
        if (cursor14.moveToFirst()){
            working_works.setText(cursor14.getString(0));
            working_duration.setText(cursor14.getString(1)+" min");
        }
        Cursor cursor15 = mDatabase.rawQuery("select count(_id) as completeWorks from Activity where activityType like 'working' and status like 'completed'"+extra_added_query,null);
        if (cursor15.moveToFirst()){
            working_complete.setText(cursor15.getString(0));
        }


        //reading section
        Cursor cursor13 = mDatabase.rawQuery("select count(distinct lower(name)) as TotalBooks,sum(price) as TotalPrice,sum(duration) as TotalDuration from Activity where activityType like 'reading'"+extra_added_query,null);
        if (cursor13.moveToFirst()){
            reading_books.setText(cursor13.getString(0));
            reading_total_cost.setText(cursor13.getString(1));
            reading_duration.setText(convert_duration_into_hourMinutes(cursor13.getString(2)));
        }
        Cursor cursor16 = mDatabase.rawQuery("select category,count(category) as CategoryNo from Activity where activityType like 'reading'"+extra_added_query+" group by category order by categoryNo desc limit 1",null);
        if (cursor16.moveToFirst()){
            reading_category_mostRead.setText(cursor16.getString(0));
        }

        //solving section
        String problems="",accepted="";
        Cursor cursor17 = mDatabase.rawQuery("select count(_id) as Problems,sum(duration) as TotalDuration from Activity where activityType like 'solving'"+extra_added_query,null);
        if (cursor17.moveToFirst()){
            problems = cursor17.getString(0);
            solving_problems.setText(cursor17.getString(0));
            solving_duration.setText(convert_duration_into_hourMinutes(cursor17.getString(1)));
        }
        Cursor cursor18 = mDatabase.rawQuery("select language,count(language) as TotalNumber from Activity where activityType like 'solving'"+extra_added_query+" group by language order by TotalNumber desc limit 1",null);
        if (cursor18.moveToFirst()){
            solving_languages.setText(cursor18.getString(0));
        }
        Cursor cursor19 = mDatabase.rawQuery("select count(_id) as Accepted from Activity where activityType like 'solving' and result like '%accepted%'"+extra_added_query,null);
        if (cursor19.moveToFirst()){
            accepted = cursor19.getString(0);
            solving_accepted.setText(accepted);
            if (Double.parseDouble(problems)!=0){
                double ratio = (Double.parseDouble(accepted)/Double.parseDouble(problems))*100;
                NumberFormat formatter = new DecimalFormat("#0.0");
                solving_ratio.setText(formatter.format(ratio)+"%");
            }else{
                solving_ratio.setText("N/A");
            }
        }

        //Writing section
        Cursor cursor20 = mDatabase.rawQuery("select count(_id) as TotalItems,sum(duration) as TotalDuration from Activity where activityType like 'writing'"+extra_added_query,null);
        if (cursor20.moveToFirst()){
            writing_item.setText(cursor20.getString(0));
            writing_duration.setText(convert_duration_into_hourMinutes(cursor20.getString(1)));
        }
        Cursor cursor21 = mDatabase.rawQuery("select count(_id) as TotalItems from Activity where activityType like 'writing' and status like 'finished'"+extra_added_query,null);
        if (cursor21.moveToFirst()){
            writing_completed.setText(cursor21.getString(0));
        }Cursor cursor22 = mDatabase.rawQuery("select language,count(_id) as TotalNo from Activity where activityType like 'writing'"+extra_added_query+" group by language order by TotalNo desc",null);
        if (cursor22.moveToFirst()){
            writing_language.setText(cursor22.getString(0));
        }

        //buying section
        Cursor cursor23 = mDatabase.rawQuery("select sum(price) as TotalCost,count(_id) as TotalItems from Activity where activityType like 'buying'"+extra_added_query,null);
        if (cursor23.moveToFirst()){
            buying_total_costs.setText(cursor23.getString(0));
            buying_items.setText(cursor23.getString(1));
        }

        //attending section
        Cursor cursor24 = mDatabase.rawQuery("select sum(duration) as TotalDuration,count(_id) as TotalFunctions from Activity where activityType like 'attending'"+extra_added_query,null);
        if (cursor24.moveToFirst()){
            attending_function.setText(cursor24.getString(1));
            attending_duration.setText(convert_duration_into_hourMinutes(cursor24.getString(0)));
        }

        //wasting time
        String positive="";
        Cursor cursor25 = mDatabase.rawQuery("select count(_id) as PositiveItems from Activity where activityType like 'wasting Time' and result like 'positive'"+extra_added_query,null);
        if (cursor25.moveToFirst()){
            positive = cursor25.getString(0);
        }
        String total_item="";
        Cursor cursor26 = mDatabase.rawQuery("select sum(duration) as TotalDuration,count(_id) as TotalItems from Activity where activityType like 'wasting Time'"+extra_added_query,null);
        if (cursor26.moveToFirst()){
            wastingTime_duration.setText(convert_duration_into_hourMinutes(cursor26.getString(0)));
            total_item = cursor26.getString(1);
            if (Double.parseDouble(total_item)!=0){
                double ratio = (Double.parseDouble(positive)/Double.parseDouble(total_item))*100;
                NumberFormat formatter = new DecimalFormat("#0.0");
                wastingTime_ratio.setText(formatter.format(ratio)+"%");
            }
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.history_nav_all:
                            all_layout.setVisibility(View.VISIBLE);
                            analysis_layout.setVisibility(View.GONE);
                            break;
                        case R.id.history_nav_analysis:
                            all_layout.setVisibility(View.GONE);
                            analysis_layout.setVisibility(View.VISIBLE);
                            break;

                    }

                    return true;
                }
            };

    private void get_all_info(){

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM "+ TimelineDB.Activity.TABLE_NAME +" WHERE "+ TimelineDB.Activity.COLUMN_USERNAME + " LIKE '%"+
                userName+"%' ORDER BY "+ TimelineDB.Activity._ID +" DESC;",null);
        infos.clear();
        if (cursor.moveToFirst()){
            do {
                int str_id = cursor.getInt(cursor.getColumnIndex(TimelineDB.Activity._ID));
                String  str_userName = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_USERNAME));
                String str_actvity_Type = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_ACTIVITY_TYPE));
                String str_name = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_NAME));
                String str_category = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_CATEGORY));
                String str_place = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_PLACE));
                String str_price = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_PRICE));
                String str_duration = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_DURATION));
                String str_year = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_YEAR));
                String str_yourTeam = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_YOUR_TEAM));
                String str_opponent = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_OPPONENT_TEAM));
                String str_language = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_LANGUAGE));
                String str_review = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_REVIEW));
                String str_notes = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_NOTES));
                String str_status = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_STATUS));
                String str_date = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_DATE));
                String str_time = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_TIME));
                String str_result = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_RESULT));
                String str_modifiedDate = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_MODIFIED_DATE));
                String str_backup = cursor.getString(cursor.getColumnIndex(TimelineDB.Activity.COLUMN_BACKUP));

                infos.add(new ActivityInfo(str_id,str_userName,str_actvity_Type,str_name,str_category,str_place,
                        str_price,str_duration,str_year,str_yourTeam,str_opponent,str_language,str_review,str_notes,str_status,
                        str_date,str_time,str_result,str_modifiedDate,str_backup));

            }while (cursor.moveToNext());
        }

        info_recylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ActivityInfoAdapter(infos);
        info_recylerView.setLayoutManager(mLayoutManager);
        info_recylerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ActivityInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent ActivityDetailsIntent = new Intent(History.this,ActivityView.class);
                ActivityDetailsIntent.putExtra("ID",infos.get(position).getId());
                startActivity(ActivityDetailsIntent);

                //mAdapter.notifyItemChanged(position);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.password_manager_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.PM_search_menu);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
    private String convert_duration_into_hourMinutes(String minutes) {
        if (minutes !=null && !minutes.isEmpty() && !minutes.equals("0")) {
            int input_min = Integer.parseInt(minutes);
            int hour = input_min / 60;
            int minute = input_min - (hour * 60);
            if (hour == 0) {
                return minute + " min";
            } else if (minute ==0){
                return hour + " hours";
            }else{
                return hour + " hours " + minute + " min";
            }
        } else {
            return "N/A";
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
}
