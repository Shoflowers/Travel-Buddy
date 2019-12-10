package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.travelbuddy.Objects.Forum;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {

    private RecyclerView countriesView;
    private CountryAdapter countryAdapter;

    private FirebaseFirestore dbInstance;
    private DatabaseHandler dbHandler;

    private List<Forum> countriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        dbHandler = new DatabaseHandler();
        dbInstance = dbHandler.getDbInstance();

        countriesView = findViewById(R.id.favCountriesView);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent a = new Intent(ExploreActivity.this, HomeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(a);
                        break;
                    case R.id.action_explore:
                        break;
                    case R.id.action_profile:
                        Intent c = new Intent(ExploreActivity.this, ProfileActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(c);

                        break;
                }
                return false;
            }
        });
    }

    public void loadFavoriteCountries(){
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

                    }
                });
    }
}
