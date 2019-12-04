package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    public EditText emailId,password;
    Button btnSignUp;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        tvSignUp = findViewById(R.id.textView2);

        Button logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //loginSuccess();

               String email = emailId.getText().toString();
               String pwd = password.getText().toString();
               if (email.isEmpty()){
                   emailId.setError("Please enter email. ");
                   emailId.requestFocus();

               }
               else if (pwd.isEmpty()){
                   password.setError("Please enter your password");
                   password.requestFocus();
               }

               else {
                   mAuthStateListener = new FirebaseAuth.AuthStateListener() {


                       @Override
                       public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                           FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                           if (mFirebaseUser != null) {
                               Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                               Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                               startActivity(i);
                           } else {
                               Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();

                           }
                       }
                   };
               }

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });
            }
            });

    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    // Opens Home Activity after successful login and passes user object
//    public void loginSuccess(){
//        // Initalize Dummy User for now
//        User dummyUser = new User(
//                "DummyUser",
//                "Dummy",
//                "dummyuser@gmail.com",
//                null,
//                "dummyuserid123",
//                new ArrayList<>(Arrays.asList("germanyForum123")),
//                null,
//                null);
//
//
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("User", dummyUser);
//        startActivity(intent);
//    }
    public void loginSuccess(){
        // Initalize Dummy User for now
        String dummyUserId = "dMm1rpBKuvYeZ4bBqy2j";

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("UserId", dummyUserId);
        startActivity(intent);
    }
}
