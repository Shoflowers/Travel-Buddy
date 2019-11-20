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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSuccess();
            }
        });
    }



    // Opens Home Activity after successful login and passes user object
    public void loginSuccess(){
        // Initalize Dummy User for now
        User dummyUser = new User(
                "DummyUser",
                "Dummy",
                "dummyuser@gmail.com",
                null,
                "dummyuserid123",
                new ArrayList<>(Arrays.asList("germanyForum123")),
                null,
                null);


        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("User", dummyUser);
        startActivity(intent);
    }
}
