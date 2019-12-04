package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public EditText emailId,password;
    Button btnSignUp;
    TextView tvSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        tvSignUp = findViewById(R.id.textView2);

        Button logInButton = findViewById(R.id.logInButton);

        final Activity curActivity = this;
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginSuccess();
                System.out.println("LOG IN BUTTON CLICKED");

                String email = emailId.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                if (email.isEmpty()) {
                    emailId.setError("Please enter email. ");
                    emailId.requestFocus();

                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else {
                    System.out.println("EMAIL AND PWD NOT EMPTY");
                    System.out.println(email);
                    System.out.println(pwd);
                    mAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(curActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    System.out.println("INSIDE ONCOMPLETE");
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        System.out.println( "signInWithEmail:success");
                                        Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        System.out.println(  "signInWithEmail:failure" + task.getException());
                                        Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intSignUp);
            }
        });
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