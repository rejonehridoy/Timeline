package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AdminNotice extends AppCompatActivity {
    private RecyclerView info_recylerView;
    private Toolbar toolbar;
    FloatingActionButton btnAdd;
    private NoticeInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private ArrayList<NoticeInfo> infos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);

        // this is for toolbar
        toolbar = findViewById(R.id.AN_toolbar);
        toolbar.setTitle("All Notices");
        setSupportActionBar(toolbar);

        info_recylerView = findViewById(R.id.AN_recylerView);
        progressBar = findViewById(R.id.AN_progressBar);
        btnAdd = findViewById(R.id.AN_btnAdd);

        get_all_info();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminNoticeCreate = new Intent(AdminNotice.this,AdminNoticeCreate.class);
                startActivity(adminNoticeCreate);
                AdminNotice.this.finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_info();
    }

    private void get_all_info(){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("Notices").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return;
                }
                infos.clear();
                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    NoticeInfo notice = documentSnapshot.toObject(NoticeInfo.class);
                    infos.add(notice);
                }
                info_recylerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(AdminNotice.this);
                mAdapter = new NoticeInfoAdapter(infos);
                info_recylerView.setLayoutManager(mLayoutManager);
                info_recylerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new NoticeInfoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(AdminNotice.this,AdminNoticeDetails.class);
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

        progressBar.setVisibility(View.GONE);
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
