package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.travelbuddy.Objects.User;

import java.util.ArrayList;
import java.util.Arrays;

public class CountriesActivity extends AppCompatActivity {

    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        Intent intent = getIntent();
        curUser = (User) intent.getSerializableExtra("User");

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        DatabaseHandler dbHandler = new DatabaseHandler();
    }

}
