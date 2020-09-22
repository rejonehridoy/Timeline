package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AdminUserDetails extends AppCompatActivity {
    private UserInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    private RecyclerView info_recylerview;
    private ArrayList<UserDetails> user_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details);

        // this is for toolbar
        toolbar = findViewById(R.id.AUD_toolbar);
        toolbar.setTitle("Users");
        setSupportActionBar(toolbar);
        info_recylerview = findViewById(R.id.AUD_recylerView);

        get_all_user_info_from_firebase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_user_info_from_firebase();
    }

    private void get_all_user_info_from_firebase(){

        FirebaseFirestore.getInstance().collection("UserDetails").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()){
                    user_list.clear();
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        user_list.add(snapshot.toObject(UserDetails.class));
                    }
                    info_recylerview.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(AdminUserDetails.this);
                    mAdapter = new UserInfoAdapter(user_list);
                    info_recylerview.setLayoutManager(mLayoutManager);
                    info_recylerview.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new UserInfoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(AdminUserDetails.this,AdminUserDetailsEdit.class);
                            intent.putExtra("userName",user_list.get(position).getUserName());
                            intent.putExtra("uid",user_list.get(position).getUid());
                            intent.putExtra("fullName",user_list.get(position).getFullName());
                            intent.putExtra("email",user_list.get(position).getEmail());
                            intent.putExtra("password",user_list.get(position).getPassword());
                            intent.putExtra("phone",user_list.get(position).getPhone());
                            intent.putExtra("gender",user_list.get(position).getGender());
                            intent.putExtra("dateOfBirth",user_list.get(position).getDateOfBirth());
                            intent.putExtra("avaterCode",user_list.get(position).getAvaterCode());
                            intent.putExtra("status",user_list.get(position).getStatus());
                            intent.putExtra("userType",user_list.get(position).getUserType());
                            intent.putExtra("createdDate",user_list.get(position).getCreatedDate());
                            intent.putExtra("recoveryQuestion",user_list.get(position).getRecoveryQuestion());
                            intent.putExtra("recoveryAns",user_list.get(position).getRecoveryAns());
                            intent.putExtra("recoveryDate",user_list.get(position).getRecoveryDate());
                            intent.putExtra("lastVisit",user_list.get(position).getLastVisit());
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
