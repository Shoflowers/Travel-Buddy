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
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    String userId;
    DocumentReference userRef;
    User curUser;

    private FirebaseFirestore dbInstance;
    private DatabaseHandler dbHandler;

    private List<Forum> favoritesList;

    private RecyclerView favoritesListView;
    private ForumRecyclerAdapter favoritesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        userId = (String) intent.getSerializableExtra("UserId");

        ImageView addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCountryButtonPressed();
            }
        });

        favoritesListView = findViewById(R.id.favoritesListView);
        favoritesListView.setHasFixedSize(true);

        dbHandler = new DatabaseHandler();
        dbInstance = dbHandler.getDbInstance();

        System.out.println(userId);

        userRef = dbInstance.collection("users").document(userId);
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

                    favoritesList = new ArrayList<>();
                    getForums();

                } else {
                    System.out.println("UserRef get data failed");
                }
            }
        });


    }

    public void getForums(){
        final Context curContext = this;
        dbInstance.collection("forums").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Forum curForum = document.toObject(Forum.class);
                                if(curUser.getForumIds().contains(curForum.getForumId())) {
                                    favoritesList.add(document.toObject(Forum.class));
                                }
                            }

                            ClickListener mylistener = new ClickListener() {
                                @Override public void onPositionClicked(int position) {
                                    forumButtonPressed(favoritesList.get(position));
                                }
                                @Override public void onButtonClicked(int position) {
                                    // MOVE TO NEXT SCREEN
                                }
                            };
                            favoritesRecyclerAdapter = new ForumRecyclerAdapter(favoritesList, mylistener);

                            favoritesListView.setLayoutManager(new LinearLayoutManager(curContext));
                            favoritesListView.setAdapter(favoritesRecyclerAdapter);
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    }
                });
    }

    // Opens Forum Activity when user presses Germany Forum and sends Forum data
    public void forumButtonPressed(Forum forum){

        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra("Forum", forum);
        startActivity(intent);
    }

    public void addCountryButtonPressed(){

        Intent intent = new Intent(this, CountriesActivity.class);
        intent.putExtra("UserId", curUser.getUserId());
        startActivity(intent);

    }
}
