package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import com.example.timeline.TimelineDB.PasswordManager.*;
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
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PasswordManager extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private RecyclerView info_recylerView;
    private AccountInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton btnAdd;
    private ArrayList<AccountInfo> acc_list = new ArrayList<>();
    private Toolbar toolbar;
    private String userName,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager);

        // this is for toolbar
        toolbar = findViewById(R.id.home_toolbar);
        toolbar.setTitle("Password Manager");
        setSupportActionBar(toolbar);
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();

        info_recylerView = findViewById(R.id.PM_recylerView);
        btnAdd = findViewById(R.id.PM_btn_add);


        get_user_info();
        get_all_info();



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PasswordManager.this,AddNewPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_info();
    }

    private void get_all_info(){

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM "+ TimelineDB.PasswordManager.TABLE_NAME +" WHERE "+ TimelineDB.PasswordManager.COLUMN_USERNAME + " LIKE '%"+
                userName+"%' ORDER BY "+ TimelineDB.PasswordManager.COLUMN_ACCOUNT_PRIORITY +" DESC;",null);

        acc_list.clear();
        if (cursor.moveToFirst()){
            do {
                int str_id = cursor.getInt(cursor.getColumnIndex(TimelineDB.PasswordManager._ID));
                String str_userName = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_USERNAME));
                String str_acc_name = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_NAME));
                String str_acc_Logo = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_LOGO));
                String str_acc_userName = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_USERNAME));
                String str_acc_email = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_EMAIL));
                String str_acc_phone = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PHONE));
                String str_acc_password = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PASSWORD));
                String str_acc_priority = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PRIORITY));
                String str_acc_notes = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_NOTES));
                String str_acc_createdDate = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_CREATED_DATE));
                String str_acc_modifiedDate = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_MODIFIED_DATE));

                acc_list.add(new AccountInfo(str_id,str_userName,str_acc_name,str_acc_Logo,str_acc_userName,str_acc_email,str_acc_phone,str_acc_priority,str_acc_password,
                        str_acc_notes,str_acc_createdDate,str_acc_modifiedDate));

            }while (cursor.moveToNext());

        }

        info_recylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AccountInfoAdapter(acc_list);
        info_recylerView.setLayoutManager(mLayoutManager);
        info_recylerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AccountInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent passwordDetailsIntent = new Intent(PasswordManager.this,PasswordManagerDetails.class);
                passwordDetailsIntent.putExtra("ID",acc_list.get(position).getId());
                startActivity(passwordDetailsIntent);

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
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
