package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Objects.ClickListener;
import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CountriesActivity extends AppCompatActivity {

    User curUser;

    private FirebaseFirestore dbInstance;
    private DatabaseHandler dbHandler;

    private List<Forum> forumList;

    private RecyclerView forumListView;
    private ForumRecyclerAdapter forumRecyclerAdapter;
    private EditText searchBar;
    private TextView titleText;
    private ImageView searchButton;
    private ImageView searchIcon;
    private ImageView cancelSearchButton;

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

        searchBar = findViewById(R.id.searchEditText);
        searchBar.setVisibility(View.INVISIBLE);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                forumRecyclerAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setVisibility(View.INVISIBLE);

        cancelSearchButton = findViewById(R.id.cancelIcon);
        cancelSearchButton.setVisibility(View.INVISIBLE);
        cancelSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
                titleText.setVisibility(View.VISIBLE);
                searchBar.setVisibility(View.INVISIBLE);
                searchIcon.setVisibility(View.INVISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                cancelSearchButton.setVisibility(View.INVISIBLE);
            }
        });

        titleText = findViewById(R.id.titleText);
        titleText.setVisibility(View.VISIBLE);

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setVisibility(View.INVISIBLE);
                searchBar.setVisibility(View.VISIBLE);
                searchIcon.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.INVISIBLE);
                cancelSearchButton.setVisibility(View.VISIBLE);
            }
        });

        dbHandler = new DatabaseHandler();
        dbInstance = dbHandler.getDbInstance();

        curUser = ((TravelBuddyApplication) this.getApplication()).getCurUser();

        forumList = new ArrayList<>();
        this.getForums();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent a = new Intent(CountriesActivity.this, HomeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(a);
                        break;
                    case R.id.action_explore:
                        Intent b = new Intent(CountriesActivity.this, ExploreActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(b);
                        break;
                    case R.id.action_profile:
                        Intent c = new Intent(CountriesActivity.this, ProfileActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(c);

                        break;
                }
                return false;
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
                                forumList.add(document.toObject(Forum.class));
                            }

                            ClickListener mylistener = new ClickListener() {
                                @Override public void onPositionClicked(int position) { }
                                @Override public void onButtonClicked(Forum forum) {
                                    addForum(forum);
                                }
                                @Override public void onDeleteItem(Forum forum) { }
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
