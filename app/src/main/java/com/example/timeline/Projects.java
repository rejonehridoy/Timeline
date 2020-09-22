package com.example.timeline;

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
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nullable;
class ProjectComparator implements Comparator<ProjectInfo>{
    @Override
    public int compare(ProjectInfo t1, ProjectInfo t2) {
        if (t2.getModifiedDate().equals(t1.getModifiedDate())){
            return t2.getCreatedDate().compareTo(t1.getCreatedDate());
        }else{
            return t2.getModifiedDate().compareTo(t1.getModifiedDate());
        }

    }
}
public class Projects extends AppCompatActivity {
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView info_recylerView;

    FloatingActionButton btnAdd;
    private ProjectInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String uid,userName,userType,userstatus,userEmail,userFullName;
    private static final String USER_TABLE = "UserDetails";
    private static final String PROJECT_TABLE = "Projects";
    private static final String MEMBERS_PROJECT_TABLE = "MembersProject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        // this is for toolbar
        toolbar = findViewById(R.id.pro_toolbar);
        toolbar.setTitle("Projects");
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.pro_progressbar);
        info_recylerView = findViewById(R.id.pro_recylerView);
        btnAdd = findViewById(R.id.pro_btnAdd);

        get_user_info();
        get_all_info();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create project activity
                startActivity(new Intent(getApplicationContext(),ProjectNew.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_all_info();
    }

    private void get_all_info() {

        final ArrayList<ProjectInfo> infos = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection(MEMBERS_PROJECT_TABLE).whereEqualTo("memberUserName",userName)
                //.orderBy("projectName", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e !=null){
                            return;
                        }
                        infos.clear();
                        for (final QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                            String pid = snapshot.getString("pid");
                            FirebaseFirestore.getInstance().collection(PROJECT_TABLE).whereEqualTo("id",pid)
                                    .addSnapshotListener(Projects.this, new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            if (e !=null){
                                                return;
                                            }
                                            for (QueryDocumentSnapshot snapshot1: queryDocumentSnapshots){
                                                ProjectInfo project = snapshot1.toObject(ProjectInfo.class);
                                                infos.add(project);
                                            }
                                            // additional work of adapter and recylerview
                                            progressBar.setVisibility(View.GONE);
                                            info_recylerView.setHasFixedSize(true);
                                            mLayoutManager = new LinearLayoutManager(Projects.this);
                                            Collections.sort(infos,new ProjectComparator());
                                            mAdapter = new ProjectInfoAdapter(infos);
                                            info_recylerView.setLayoutManager(mLayoutManager);
                                            info_recylerView.setAdapter(mAdapter);

                                            mAdapter.setOnItemClickListener(new ProjectInfoAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                    Intent intent = new Intent(Projects.this,ProjectDetails.class);
                                                    intent.putExtra("Pid",infos.get(position).getId());
                                                    intent.putExtra("name",infos.get(position).getName());
                                                    intent.putExtra("category",infos.get(position).getCategory());
                                                    intent.putExtra("descripton",infos.get(position).getDescription());
                                                    intent.putExtra("framework",infos.get(position).getFramework());
                                                    intent.putExtra("platform",infos.get(position).getPlatform());
                                                    intent.putExtra("requirement",infos.get(position).getRequirement());
                                                    intent.putExtra("prerequisite",infos.get(position).getPrerequisite());
                                                    intent.putExtra("status",infos.get(position).getStatus());
                                                    intent.putExtra("startingDate",infos.get(position).getStartingDate());
                                                    intent.putExtra("endDate",infos.get(position).getEndDate());
                                                    intent.putExtra("modifiedUser",infos.get(position).getModifiedUser());
                                                    intent.putExtra("modifiedDate",infos.get(position).getModifiedDate());
                                                    intent.putExtra("createdDate",infos.get(position).getCreatedDate());
                                                    intent.putExtra("createdUser",infos.get(position).getCreatedUser());
                                                    intent.putExtra("totalTasks",infos.get(position).getTotalTasks());
                                                    intent.putExtra("totalTaskPoints",infos.get(position).getTotalTaskPoints());
                                                    intent.putExtra("completedTasks",infos.get(position).getCompletedTask());
                                                    intent.putExtra("completedTaskPoints",infos.get(position).getCompletedTaskPoints());
                                                    intent.putExtra("modifiedVersion",infos.get(position).getModifiedVersion());
                                                    startActivity(intent);
                                                    //mAdapter.notifyItemChanged(position);
                                                }
                                            });
                                        }
                                    });
                        }
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

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userType = sharedPreferences.getString("userType","");
            userstatus = sharedPreferences.getString("status","");
            //userPassword = sharedPreferences.getString("password","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
}
