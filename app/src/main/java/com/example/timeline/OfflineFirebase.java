package com.example.timeline;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class OfflineFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // for firebase database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // fore cloud firestore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
    }
}
