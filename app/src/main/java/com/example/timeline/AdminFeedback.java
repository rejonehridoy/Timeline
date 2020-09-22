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

public class AdminFeedback extends AppCompatActivity {
    private FeedbackInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    private RecyclerView info_recylerview;
    private ArrayList<FeedbackInfo> feedback_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);

        // this is for toolbar
        toolbar = findViewById(R.id.AF_toolbar);
        toolbar.setTitle("Feedback");
        setSupportActionBar(toolbar);
        info_recylerview = findViewById(R.id.AF_recylerView);

        get_all_user_info_from_firebase();
    }
    @Override
    protected void onStart() {
        super.onStart();
        get_all_user_info_from_firebase();
    }
    private void get_all_user_info_from_firebase(){
        FirebaseFirestore.getInstance().collection("Feedback").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()){
                    feedback_list.clear();
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        feedback_list.add(snapshot.toObject(FeedbackInfo.class));
                    }
                    info_recylerview.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(AdminFeedback.this);
                    mAdapter = new FeedbackInfoAdapter(feedback_list);
                    info_recylerview.setLayoutManager(mLayoutManager);
                    info_recylerview.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new FeedbackInfoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(AdminFeedback.this,AdminFeedbackDetails.class);
                            intent.putExtra("id",feedback_list.get(position).getId());
                            intent.putExtra("appVerion",feedback_list.get(position).getAppVersion());
                            intent.putExtra("subject",feedback_list.get(position).getSubject());
                            intent.putExtra("message",feedback_list.get(position).getMessage());
                            intent.putExtra("userName",feedback_list.get(position).getUserName());
                            intent.putExtra("email",feedback_list.get(position).getEmail());
                            intent.putExtra("review",feedback_list.get(position).getReview());
                            intent.putExtra("fullName",feedback_list.get(position).getFullName());
                            intent.putExtra("date",feedback_list.get(position).getDate());
                            intent.putExtra("reply",feedback_list.get(position).getReply());
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
