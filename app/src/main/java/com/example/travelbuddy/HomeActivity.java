package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.User;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        curUser = (User) intent.getSerializableExtra("User");

        Button forumButton = findViewById(R.id.forumButton);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumButtonPressed();
            }
        });

        ImageView addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCountryButtonPressed();
            }
        });

        DatabaseHandler dbHandler = new DatabaseHandler();
    }

    // // Opens Forum Activity when user presses Germany Forum and sends Forum data
    public void forumButtonPressed(){

        // Initialize Dummy Forum for now
        Forum dummyForum = new Forum("Germany",
                "dummyForumUrl",
                "forumId123",
                new ArrayList<String>());

        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra("Forum", dummyForum);
        startActivity(intent);
    }

    public void addCountryButtonPressed(){

        Intent intent = new Intent(this, CountriesActivity.class);
        intent.putExtra("User", curUser);
        startActivity(intent);

    }
}
