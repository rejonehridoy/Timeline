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
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class NotesComparator implements Comparator<NoteInfo>{

    @Override
    public int compare(NoteInfo t1, NoteInfo t2) {
        String Datetime1="",Datetime2="";
        if (!t1.getModifiedDate().isEmpty()){
            Datetime1 = t1.getModifiedDate().substring(6,10)+t1.getModifiedDate().substring(3,5)+t1.getModifiedDate().substring(0,2)+t1.getModifiedDate().substring(14,23);
        }else{
            Datetime1 = t1.getDate().substring(6,10)+t1.getDate().substring(3,5)+t1.getDate().substring(0,2)+t1.getTime();
        }
        if (!t2.getModifiedDate().isEmpty()){
            Datetime2 = t2.getModifiedDate().substring(6,10)+t2.getModifiedDate().substring(3,5)+t2.getModifiedDate().substring(0,2)+t2.getModifiedDate().substring(14,23);
        }else{
            Datetime2 = t2.getDate().substring(6,10)+t2.getDate().substring(3,5)+t2.getDate().substring(0,2)+t2.getTime();
        }
        return Datetime2.compareTo(Datetime1);

    }
}

public class Notes extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private NoteInfoAdapter cAdapter,fAdapter;
    private RecyclerView.LayoutManager cLayoutManager,fLayoutManager;
    private Toolbar collection_toolbar,favourites_toolbar;
    private LinearLayout layout_collection,layout_favorite;
    private FloatingActionButton btnAdd;
    private ArrayList<NoteInfo> collection_info = new ArrayList<>();
    private ArrayList<NoteInfo> favorite_info = new ArrayList<>();
    private RecyclerView collection_reclyerView,favorite_recylerView;
    private String userName,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        toolbar_setting();

        layout_collection = findViewById(R.id.notes_layout_collections);
        layout_favorite = findViewById(R.id.notes_layout_favorite);
        btnAdd = findViewById(R.id.notes_btnAdd);
        collection_reclyerView = findViewById(R.id.notes_collection_recylerView);
        favorite_recylerView = findViewById(R.id.notes_favourites_recylerView);

        get_user_info();
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();

        get_data_from_sqlite();



        BottomNavigationView bottomNav = findViewById(R.id.notes_bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new item
                startActivity(new Intent(getApplicationContext(),NotesCreate.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_data_from_sqlite();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.notes_nav_collections:
                            layout_collection.setVisibility(View.VISIBLE);
                            layout_favorite.setVisibility(View.GONE);

                            break;
                        case R.id.notes_nav_favorite:
                            layout_collection.setVisibility(View.GONE);
                            layout_favorite.setVisibility(View.VISIBLE);
                            break;

                    }

                    return true;
                }
            };

    private void get_data_from_sqlite(){
        //collection item
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM "+ TimelineDB.Notes.TABLE_NAME+ " WHERE "+ TimelineDB.Notes.COLUMN_USERNAME + " LIKE '"+ userName+"' ORDER BY "+ TimelineDB.Notes._ID+ " DESC;",null);
        collection_info.clear();
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(TimelineDB.Notes._ID));
                String user = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_USERNAME));
                String subject = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_SUBJECT));
                String description = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_TIME));
                String dayOfWeek = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_DAYOFWEEK));
                String modifiedDate = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_MODIFIED_DATE));
                String favortie = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_FAVORITE));
                String backup = cursor.getString(cursor.getColumnIndex(TimelineDB.Notes.COLUMN_BACKUP));
                collection_info.add(new NoteInfo(id,user,subject,description,date,time,dayOfWeek,modifiedDate,favortie,backup));
            }while (cursor.moveToNext());
        }

        Collections.sort(collection_info,new NotesComparator());
        collection_reclyerView.setHasFixedSize(true);
        cLayoutManager = new LinearLayoutManager(this);
        cAdapter = new NoteInfoAdapter(collection_info);
        collection_reclyerView.setLayoutManager(cLayoutManager);
        collection_reclyerView.setAdapter(cAdapter);

        cAdapter.setOnItemClickListener(new NoteInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent noteDetailsIntent = new Intent(Notes.this,NotesDetails.class);
                noteDetailsIntent.putExtra("ID",collection_info.get(position).getId());
                startActivity(noteDetailsIntent);

            }
        });

        // favorite items
        Cursor cursor1 = mDatabase.rawQuery("SELECT * FROM "+ TimelineDB.Notes.TABLE_NAME+ " WHERE "+ TimelineDB.Notes.COLUMN_USERNAME + " LIKE '"+ userName+
                "' AND "+ TimelineDB.Notes.COLUMN_FAVORITE+" LIKE "+"'yes'" +" ORDER BY "+ TimelineDB.Notes._ID+ " DESC;",null);
        favorite_info.clear();
        if (cursor1.moveToFirst()){
            do {
                int id = cursor1.getInt(cursor1.getColumnIndex(TimelineDB.Notes._ID));
                String user = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_USERNAME));
                String subject = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_SUBJECT));
                String description = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_DESCRIPTION));
                String date = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_DATE));
                String time = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_TIME));
                String dayOfWeek = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_DAYOFWEEK));
                String modifiedDate = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_MODIFIED_DATE));
                String favortie = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_FAVORITE));
                String backup = cursor1.getString(cursor1.getColumnIndex(TimelineDB.Notes.COLUMN_BACKUP));
                favorite_info.add(new NoteInfo(id,user,subject,description,date,time,dayOfWeek,modifiedDate,favortie,backup));
            }while (cursor1.moveToNext());
        }


        Collections.sort(favorite_info,new NotesComparator());
        favorite_recylerView.setHasFixedSize(true);
        fLayoutManager = new LinearLayoutManager(this);
        fAdapter = new NoteInfoAdapter(favorite_info);
        favorite_recylerView.setLayoutManager(fLayoutManager);
        favorite_recylerView.setAdapter(fAdapter);

        fAdapter.setOnItemClickListener(new NoteInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent noteDetailsIntent = new Intent(Notes.this,NotesDetails.class);
                noteDetailsIntent.putExtra("ID",favorite_info.get(position).getId());
                startActivity(noteDetailsIntent);
            }
        });

    }
    private void toolbar_setting(){
        // this is for info toolbar
        favourites_toolbar = findViewById(R.id.notes_favorite_toolbar);
        favourites_toolbar.setTitle("Favorites");
        setSupportActionBar(favourites_toolbar);
        // this is for settings toolbar
        collection_toolbar = findViewById(R.id.notes_collections_toolbar);
        collection_toolbar.setTitle("Notes");
        setSupportActionBar(collection_toolbar);
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
                cAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}

