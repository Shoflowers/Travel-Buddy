package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        curUser = ((TravelBuddyApplication) HomeActivity.this.getApplication()).getCurUser();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        break;
                    case R.id.action_explore:
                        Intent b = new Intent(HomeActivity.this, ExploreActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(b);
                        break;
                    case R.id.action_profile:
                        Intent c = new Intent(HomeActivity.this, ProfileActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(c);

                        break;
                }
                return false;
            }
        });


    }

    public void getForums(){
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
                            setUpRecylcerView();
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    }
                });
    }

    private void setUpRecylcerView(){

        ClickListener mylistener = new ClickListener() {
            @Override public void onPositionClicked(int position) {
                forumButtonPressed(favoritesList.get(position));
            }
            @Override public void onButtonClicked(int position) { }
            @Override public void onDeleteItem(int position) {
                favoritesList.remove(position);
                List<String> forumIds = curUser.getForumIds();
                forumIds.remove(position);
                curUser.setForumIds(forumIds);
                dbHandler.updateUser(curUser);
            }
        };
        favoritesRecyclerAdapter = new ForumRecyclerAdapter(favoritesList, mylistener, false);

        favoritesListView.setLayoutManager(new LinearLayoutManager(this));
        favoritesListView.setAdapter(favoritesRecyclerAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                favoritesRecyclerAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(favoritesListView);
    }

    // Opens Forum Activity when user presses Germany Forum and sends Forum data
    public void forumButtonPressed(Forum forum){
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra("Forum", forum);
        startActivity(intent);
    }

    public void addCountryButtonPressed(){
        Intent intent = new Intent(this, CountriesActivity.class);
        startActivity(intent);
    }
}
