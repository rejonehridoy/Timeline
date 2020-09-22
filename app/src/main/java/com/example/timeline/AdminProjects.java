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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nullable;

public class AdminProjects extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView info_recylerView;
    private ProgressBar progressBar;
    private ProjectInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String PROJECT_TABLE = "Projects";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_projects);

        // this is for toolbar
        toolbar = findViewById(R.id.AP_toolbar);
        toolbar.setTitle("Projects");
        setSupportActionBar(toolbar);

        info_recylerView = findViewById(R.id.AP_recylerView);
        progressBar = findViewById(R.id.AP_progressbar);

        get_project_info();
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_project_info();
    }

    private void get_project_info(){
        final ArrayList<ProjectInfo> infos = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection(PROJECT_TABLE).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    return;
                }
                infos.clear();
                for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                    infos.add(snapshot.toObject(ProjectInfo.class));
                }
                // additional work of adapter and recylerview
                progressBar.setVisibility(View.GONE);
                info_recylerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(AdminProjects.this);
                Collections.sort(infos,new ProjectComparator());
                mAdapter = new ProjectInfoAdapter(infos);
                info_recylerView.setLayoutManager(mLayoutManager);
                info_recylerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new ProjectInfoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(AdminProjects.this,AdminProjectDetails.class);
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
