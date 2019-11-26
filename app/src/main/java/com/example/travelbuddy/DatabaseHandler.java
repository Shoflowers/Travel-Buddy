package com.example.travelbuddy;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.User;
import com.example.travelbuddy.Objects.ForumQuestion;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;


import java.util.ArrayList;
import java.util.Locale;

import java.util.LinkedList;
import java.util.List;

import java.util.concurrent.Future;

import javax.security.auth.callback.Callback;

public class DatabaseHandler {

    private FirebaseFirestore db;

    public DatabaseHandler() {

        db = FirebaseFirestore.getInstance();

        String countryName = "germany";

        /**

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
         **/



    }

    public FirebaseFirestore getDbInstance(){

        return db;

    }

    public void updateUser(User user){

        db.collection("users").document(user.getUserId())
                .set(user);

    }



    // Add functions for getting data and setting data from Firebase

    public List<ForumQuestion> getQestionList(List<String> qIds) {

        List<ForumQuestion> qList = new LinkedList<>();
        db.collection("questions")
                .whereArrayContains("questionId", qIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //todo: construct qList
//                                ForumQuestion q = new ForumQuestion(
//                                        document.get("questionId"),
//                                        document.get("questionTitle"),
//                                        document.get("questionBody"),
//                                        document.get("userId"),
//                                        document.get("tags"),
//                                        document.get("answerIds"),
//                                        document.get("votes"),
//                                        document.get("dateTime")
//                                )
                            }
                        } else {
                            Log.d("DEBUG", "Cannot get questions list.");
                        }
                    }
                });

        return qList;
    }


}
