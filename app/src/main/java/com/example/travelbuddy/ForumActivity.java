package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.User;

public class ForumActivity extends AppCompatActivity {

    Forum forum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("Forum");
    }
}
