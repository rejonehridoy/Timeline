package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AdminDeleteUser extends AppCompatActivity {
    private DeleteReportInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    private RecyclerView info_recylerview;
    private ArrayList<DeleteReportInfo> user_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user);

        // this is for toolbar
        toolbar = findViewById(R.id.ADU_toolbar);
        toolbar.setTitle("Delete Users");
        setSupportActionBar(toolbar);
        info_recylerview = findViewById(R.id.ADU_recylerView);

        get_all_user_info_from_firebase();
    }
    @Override
    protected void onStart() {
        super.onStart();
        get_all_user_info_from_firebase();
    }

    private void get_all_user_info_from_firebase(){

        FirebaseFirestore.getInstance().collection("UserDeleteReport").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()){
                    user_list.clear();
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        user_list.add(snapshot.toObject(DeleteReportInfo.class));
                    }
                    info_recylerview.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(AdminDeleteUser.this);
                    mAdapter = new DeleteReportInfoAdapter(user_list);
                    info_recylerview.setLayoutManager(mLayoutManager);
                    info_recylerview.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new DeleteReportInfoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(AdminDeleteUser.this,AdminDeleteUserDetails.class);
                            intent.putExtra("userName",user_list.get(position).getUserName());
                            intent.putExtra("uid",user_list.get(position).getUid());
                            intent.putExtra("fullName",user_list.get(position).getFullName());
                            intent.putExtra("email",user_list.get(position).getEmail());
                            intent.putExtra("date",user_list.get(position).getDate());
                            intent.putExtra("reason",user_list.get(position).getReason());
                            intent.putExtra("phone",user_list.get(position).getPhone());
                            startActivity(intent);

                        }
                    });
                }
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
}
