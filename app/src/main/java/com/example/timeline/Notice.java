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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Notice extends AppCompatActivity {
    private LinearLayout all_layout, setting_layout;
    private Toolbar all_toolbar, setting_toolbar;
    private Button btnSubmit;
    private TextInputLayout tag;
    private TextInputEditText etTag;
    private RecyclerView info_recylerView;
    private NoticeInfoAdapter mAdapter;
    ArrayList<NoticeInfo> infos = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String str_tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        all_layout = findViewById(R.id.notice_layout_all);
        setting_layout = findViewById(R.id.notice_layout_setting);
        // this is for analysis toolbar
        setting_toolbar = findViewById(R.id.notice_setting_toolbar);
        setting_toolbar.setTitle("Settings");
        setSupportActionBar(setting_toolbar);
        // this is for all toolbar
        all_toolbar = findViewById(R.id.noitce_all_toolbar);
        all_toolbar.setTitle("Notices");
        setSupportActionBar(all_toolbar);
        btnSubmit = findViewById(R.id.notice_btnSubmit);
        tag = findViewById(R.id.notice_tag);
        etTag = findViewById(R.id.notice_tag_input);
        BottomNavigationView bottomNav = findViewById(R.id.notice_bottomNavigation);

        get_tag_from_sharedpreference();

        info_recylerView = findViewById(R.id.notice_recylerview);
        get_all_info();


        bottomNav.setOnNavigationItemSelectedListener(navListener);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag != null && tag.getEditText() != null && !tag.getEditText().getText().toString().isEmpty()){
                    tag.setError(null);
                    tag.setErrorEnabled(false);
                    str_tag = tag.getEditText().getText().toString().trim();
                    SharedPreferences.Editor editor;
                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("tag",str_tag);
                    editor.apply();
                    get_all_info();
                    Toasty.success(getApplicationContext(),"Tag applied",Toasty.LENGTH_SHORT).show();
                }else{
                    tag.setError("Empty Text Field");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_info();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.notice_nav_all:
                            all_layout.setVisibility(View.VISIBLE);
                            setting_layout.setVisibility(View.GONE);
                            break;
                        case R.id.notice_nav_setting:
                            all_layout.setVisibility(View.GONE);
                            setting_layout.setVisibility(View.VISIBLE);
                            break;

                    }

                    return true;
                }
            };

    private void get_all_info() {

        FirebaseFirestore.getInstance().collection("Notices")
                .whereEqualTo("tag",str_tag)
                .whereEqualTo("visible","Visible")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return;
                }

                infos.clear();
                for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                    infos.add(snapshot.toObject(NoticeInfo.class));
                }
                info_recylerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(Notice.this);
                mAdapter = new NoticeInfoAdapter(infos);
                info_recylerView.setLayoutManager(mLayoutManager);
                info_recylerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new NoticeInfoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(Notice.this,NoticeDetails.class);
                        intent.putExtra("ID",infos.get(position).getId());
                        intent.putExtra("visible",infos.get(position).getVisible());
                        intent.putExtra("subject",infos.get(position).getSubject());
                        intent.putExtra("category",infos.get(position).getCategory());
                        intent.putExtra("description",infos.get(position).getDescription());
                        intent.putExtra("eventDate",infos.get(position).getEventDate());
                        intent.putExtra("eventTime",infos.get(position).getEventTime());
                        intent.putExtra("createdDate",infos.get(position).getCreatedDate());
                        intent.putExtra("creator",infos.get(position).getCreator());
                        intent.putExtra("tag",infos.get(position).getTag());
                        intent.putExtra("repeation",infos.get(position).getRepetition());
                        intent.putExtra("dayofweek",infos.get(position).getDayOfWeek());
                        intent.putExtra("link",infos.get(position).getLink());
                        intent.putExtra("modifiedDate",infos.get(position).getModifiedDate());
                        intent.putExtra("modifier",infos.get(position).getModifer());
                        startActivity(intent);
                        //mAdapter.notifyItemChanged(position);
                    }
                });

            }
        });

    }

    private void get_tag_from_sharedpreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("tag") && sharedPreferences.contains("userName")) {
            str_tag = sharedPreferences.getString("tag","");
            etTag.setText(str_tag);
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
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}
