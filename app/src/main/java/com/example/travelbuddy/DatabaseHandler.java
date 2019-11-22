package com.example.travelbuddy;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelbuddy.Objects.Forum;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.AsyncCallable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.concurrent.Future;

import javax.security.auth.callback.Callback;

public class DatabaseHandler {

    private FirebaseFirestore db;

    public DatabaseHandler() {

        db = FirebaseFirestore.getInstance();

        String countryName = "germany";

        // Get Forum for Germany:
        db.collection("forums").document(countryName).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Forum form = document.toObject(Forum.class);
                            System.out.println(form);
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    }
                });

        // Get all forums:
        db.collection("forums").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                            }
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    }
                });

    }

    // Add functions for getting data and setting data from Firebase


}
