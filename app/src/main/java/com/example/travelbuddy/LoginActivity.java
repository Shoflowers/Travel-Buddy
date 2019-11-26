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
        String dummyUserId = "dMm1rpBKuvYeZ4bBqy2j";

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("UserId", dummyUserId);
        startActivity(intent);
    }
}
