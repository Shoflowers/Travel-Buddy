package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.ForumQuestion;
import com.example.travelbuddy.Objects.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.opencensus.stats.Aggregation;

public class ProfileActivity extends AppCompatActivity {

    public User curUser;

    private FirebaseFirestore dbInstance;
    private DatabaseHandler dbHandler;

    private TextView nameTextView;
    private ImageView profileImageView;
    private TextView bioTextView;
    private TextView questionsTextView;
    private TextView answersTextView;
    private TextView likesTextView;

    private ImageView settingsButton;
    private RecyclerView favCountriesView;
    private CountryAdapter countryAdapter;


    private List<Forum> countriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHandler = new DatabaseHandler();
        dbInstance = dbHandler.getDbInstance();

        favCountriesView = findViewById(R.id.favCountriesView);

        nameTextView = findViewById(R.id.nameTextView);
        profileImageView = findViewById(R.id.profileImageView);
        bioTextView = findViewById(R.id.profileTextView);
        questionsTextView = findViewById(R.id.numQuestions);
        answersTextView = findViewById(R.id.numAnswers);
        likesTextView = findViewById(R.id.numLikes);
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(ProfileActivity.this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(a);
            }
        });

        curUser = ((TravelBuddyApplication) this.getApplication()).getCurUser();

        loadUserUI();

        loadFavoriteCountries();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent a = new Intent(ProfileActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(a);
                        break;
                    case R.id.action_explore:
                        Intent b = new Intent(ProfileActivity.this, ExploreActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(b);
                        break;
                    case R.id.action_profile:
                        break;
                }
                return false;
            }
        });

        dbInstance.collection("users").document(curUser.getUserId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.out.println("UserRef get data failed");
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            curUser = snapshot.toObject(User.class);
                            loadUserUI();
                            loadFavoriteCountries();
                        } else {
                            System.out.println("UserRef get data failed");
                        }
                    }
                });
    }

    public void loadUserUI(){
        nameTextView.setText(curUser.getName());
        questionsTextView.setText(Integer.toString(curUser.getQuestionIds().size()));
        answersTextView.setText(Integer.toString(curUser.getAnswerIds().size()));

        if(curUser.getProfileBio() != null ){
            bioTextView.setText(curUser.getProfileBio());
        }
        if(curUser.getProfilePhotoUrl() != null && curUser.getProfilePhotoUrl() != ""){
            Picasso.get().load(curUser.getProfilePhotoUrl()).into(profileImageView);
        }

        if(!curUser.getQuestionIds().isEmpty()) {
            dbInstance.collection("questions")
                    .whereIn("questionId", curUser.getQuestionIds())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.d("DEBUG", "Listen failed");
                                likesTextView.setText(0);
                                return;
                            }

                            int votes = 0;
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                ForumQuestion question = doc.toObject(ForumQuestion.class);
                                votes += question.getVotes();
                            }
                            likesTextView.setText(Integer.toString(votes));
                        }
                    });
        }else{
            int votes = 0;
            likesTextView.setText(Integer.toString(votes));
        };

    }

    public void loadFavoriteCountries(){
        if(!curUser.getForumIds().isEmpty()) {
            dbInstance.collection("forums")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.d("DEBUG", "Listen failed");
                                return;
                            }

                            countriesList = new LinkedList<>();
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Forum forum = doc.toObject(Forum.class);
                                if(curUser.getForumIds().contains(forum.getForumId())){
                                    countriesList.add(forum);
                                }
                            }

                            favCountriesView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 2));
                            countryAdapter = new CountryAdapter(ProfileActivity.this, countriesList);
                            favCountriesView.setAdapter(countryAdapter);

                            /**
                            GridView gridView = findViewById(R.id.countryGridList);
                            CountryAdapter booksAdapter = new CountryAdapter(ProfileActivity.this, countriesList);
                            gridView.setAdapter(booksAdapter);
                             */
                        }
                    });
        }
    }

}
