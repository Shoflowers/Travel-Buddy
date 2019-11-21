package com.example.travelbuddy;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHandler {

    private DatabaseReference mDatabase;

    public DatabaseHandler() {

        mDatabase = FirebaseDatabase.getInstance().getReference("forums");

        mDatabase.child("users").child("mexico").child("username").setValue("ITWORKED");


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                System.out.println("OnDataChange worked");
                System.out.println(dataSnapshot.getValue());
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.out.println("Database read failed");
            }

        };
        mDatabase.addValueEventListener(postListener);

        System.out.println("GOT DATABASE");
        System.out.println(mDatabase);

    }



    // Add functions for getting data and setting data from Firebase


}
