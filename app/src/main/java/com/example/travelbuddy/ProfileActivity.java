package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Objects.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    public String userId;
    public DocumentReference userRef;
    public User user;

    private FirebaseFirestore dbInstance;
    private DatabaseHandler dbHandler;

    private TextView nameTextView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHandler = new DatabaseHandler();
        dbInstance = dbHandler.getDbInstance();

        nameTextView = findViewById(R.id.nameTextView);
        profileImageView = findViewById(R.id.profileImageView);

        userId = "dMm1rpBKuvYeZ4bBqy2j";

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
                    user = snapshot.toObject(User.class);

                    loadUserUI();
                } else {
                    System.out.println("UserRef get data failed");
                }
            }
        });

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
    }

    public void loadUserUI(){

        nameTextView.setText(user.getName());
        if(user.getProfilePhotoUrl() != null && user.getProfilePhotoUrl() != ""){
            Picasso.get().load(user.getProfilePhotoUrl()).into(profileImageView);
        }

    }
}
