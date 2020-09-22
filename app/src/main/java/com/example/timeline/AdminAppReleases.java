package com.example.timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminAppReleases extends AppCompatActivity {
    private TextInputLayout id,bug,description,downloadLink,releaseDate,version,versionType;
    private ReleaseInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView info_recylerView;
    private Toolbar all_toolbar,latest_toolbar;
    private LinearLayout layout_all_releases,layout_latest;
    private FloatingActionButton btnAdd;
    private Button btnBrowse;
    private ArrayList<ReleaseInfo> infos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_app_releases);
        layout_all_releases = findViewById(R.id.AAR_layout_all);
        layout_latest = findViewById(R.id.AAR_layout_latest);
        btnAdd = findViewById(R.id.AAR_btnAdd);
        info_recylerView = findViewById(R.id.AAR_all_recylerView);
        //latest section
        id = findViewById(R.id.AAR_id);
        description = findViewById(R.id.AAR_description);
        bug = findViewById(R.id.AAR_bug);
        downloadLink = findViewById(R.id.AAR_downloadLink);
        releaseDate = findViewById(R.id.AAR_releaseDate);
        version = findViewById(R.id.AAR_appVersion);
        versionType = findViewById(R.id.AAR_versionType);
        btnBrowse = findViewById(R.id.AAR_btnBrowse);

        toolbar_setting();
        get_data_from_firebase();
        get_latest_release_data();

        BottomNavigationView bottomNav = findViewById(R.id.AAR_bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminAppReleases.this,AdminAppReleasesNew.class));
            }
        });
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downloadLink !=null && downloadLink.getEditText() !=null && !downloadLink.getEditText().toString().isEmpty()){
                    Uri webpage = Uri.parse(downloadLink.getEditText().getText().toString());
                    Intent webintent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(webintent);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_data_from_firebase();
        get_latest_release_data();
    }

    private void get_data_from_firebase(){
        FirebaseFirestore.getInstance().collection("UpdateRelease").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    return;
                }
                if (!queryDocumentSnapshots.isEmpty()){
                    infos.clear();
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        infos.add(snapshot.toObject(ReleaseInfo.class));
                    }
                    info_recylerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(AdminAppReleases.this);
                    mAdapter = new ReleaseInfoAdapter(infos);
                    info_recylerView.setLayoutManager(mLayoutManager);
                    info_recylerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new ReleaseInfoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(AdminAppReleases.this,AdminAppReleaseDetails.class);
                            intent.putExtra("id",infos.get(position).getId());
                            intent.putExtra("bug",infos.get(position).getBug());
                            intent.putExtra("downloadLink",infos.get(position).getDownloadLink());
                            intent.putExtra("description",infos.get(position).getDescription());
                            intent.putExtra("releaseDate",infos.get(position).getReleaseDate());
                            intent.putExtra("version",infos.get(position).getVersion());
                            intent.putExtra("versionType",infos.get(position).getVersionType());
                            startActivity(intent);

                        }
                    });
                }
            }
        });
    }

    private void toolbar_setting(){
        // this is for latest toolbar
        latest_toolbar = findViewById(R.id.AAR_latest_toolbar);
        latest_toolbar.setTitle("Latest Version");
        setSupportActionBar(latest_toolbar);
        // this is for all toolbar
        all_toolbar = findViewById(R.id.AAR_all_toolbar);
        all_toolbar.setTitle("Releases");
        setSupportActionBar(all_toolbar);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.releases_nav_all:
                            layout_all_releases.setVisibility(View.VISIBLE);
                            layout_latest.setVisibility(View.GONE);

                            break;
                        case R.id.releases_nav_latest:
                            layout_all_releases.setVisibility(View.GONE);
                            layout_latest.setVisibility(View.VISIBLE);
                            break;

                    }

                    return true;
                }
            };

    private void get_latest_release_data(){
        FirebaseFirestore.getInstance().collection("UpdateRelease").document("latestRelease")
                .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()){
                            id.getEditText().setText(documentSnapshot.getString("id"));
                            bug.getEditText().setText(documentSnapshot.getString("bug"));
                            description.getEditText().setText(documentSnapshot.getString("description"));
                            version.getEditText().setText(documentSnapshot.getString("version"));
                            versionType.getEditText().setText(documentSnapshot.getString("versionType"));
                            releaseDate.getEditText().setText(documentSnapshot.getString("releaseDate"));
                            downloadLink.getEditText().setText(documentSnapshot.getString("downloadLink"));
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
