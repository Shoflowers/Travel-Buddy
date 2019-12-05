package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountriesActivity extends AppCompatActivity {

    String userId;
    DocumentReference userRef;
    User curUser;

    private FirebaseFirestore dbInstance;
    private DatabaseHandler dbHandler;

    private List<Forum> forumList;

    private RecyclerView forumListView;
    private ForumRecyclerAdapter forumRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        curUser = ((TravelBuddyApplication) this.getApplication()).getCurUser();

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        forumListView = findViewById(R.id.countryListView);
        forumListView.setHasFixedSize(true);

        dbHandler = new DatabaseHandler();
        dbInstance = dbHandler.getDbInstance();

        userRef = dbInstance.collection("users").document(curUser.getUserId());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.out.println("UserRef get data failed");
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    curUser = snapshot.toObject(User.class);
                } else {
                    System.out.println("UserRef get data failed");
                }
            }
        });

        forumList = new ArrayList<>();
        this.getForums();

    }

    public void getForums(){
        final Context curContext = this;
        dbInstance.collection("forums").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                forumList.add(document.toObject(Forum.class));
                            }

                            ClickListener mylistener = new ClickListener() {
                                @Override public void onPositionClicked(int position) { }
                                @Override public void onButtonClicked(int position) {
                                    addForum(forumList.get(position));
                                }
                                @Override public void onDeleteItem(int position) { }
                            };
                            forumRecyclerAdapter = new ForumRecyclerAdapter(forumList, mylistener, true);

                            forumListView.setLayoutManager(new LinearLayoutManager(curContext));
                            forumListView.setAdapter(forumRecyclerAdapter);
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    }
                });
    }

    // Add newForum to users Forums
    public void addForum(Forum newForum){
        List<String> curForums = curUser.getForumIds();
        if(!curForums.contains(newForum.getForumId())) {

            curForums.add(newForum.getForumId());
            curUser.setForumIds(curForums);

            dbHandler.updateUser(curUser);

        }
    }

}
