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

import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;

public class SignUp extends AppCompatActivity {

    public EditText emailId,password,name,username;
    Button btnSignUp;
    TextView tvSignIn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dbInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailText);
        password = findViewById(R.id.pwdText);
        btnSignUp = findViewById(R.id.logInButton);
        tvSignIn = findViewById(R.id.textView2);
        name = findViewById(R.id.nameText);
        username = findViewById(R.id.usernameText);
        dbInstance = FirebaseFirestore.getInstance();

        }

    public void SignUpButton(View view){
        final Activity curActivity = this;

        final String email = emailId.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        final String officialName = name.getText().toString().trim();
        final String officialUserName = username.getText().toString().trim();

        if (email.isEmpty()){

            System.out.println("Please Enter email.");
            emailId.setError("Please enter email. ");
            emailId.requestFocus();

        }
        else if (pwd.isEmpty()){
            password.setError("Please enter your password");
            password.requestFocus();
        } else {
            System.out.println(email + " " + pwd);
            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(curActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                System.out.println("Success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                System.out.println("USERID: " + mAuth.getCurrentUser().getUid());
                                DocumentReference Ref = dbInstance.collection("users").document(mAuth.getCurrentUser().getUid());
                                User newUser = new User(officialName,officialUserName,email,"",
                                        mAuth.getCurrentUser().getUid(),new LinkedList<String>(),new LinkedList<String>(), new LinkedList<String>(), "");
                                Ref.set(newUser);


                                Intent i = new Intent(SignUp.this,LoginActivity.class);
                                startActivity(i);



                            } else {
                                // If sign in fails, display a message to the user.
                                System.out.println("Fail");
                                Toast.makeText(SignUp.this, "Please Sign Up.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
            tvSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SignUp.this,LoginActivity.class);
                    startActivity(i);

                }
            });
        }
    }





    }


//    public EditText emailId,password;
//    Button btnSignUp;
//    TextView tvSignIn;
//    FirebaseAuth mFirebaseAuth;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.signup);
//
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        emailId = findViewById(R.id.editText);
//        password = findViewById(R.id.editText2);
//        btnSignUp = findViewById(R.id.logInButton);
//        tvSignIn = findViewById(R.id.textView2);
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = emailId.getText().toString();
//                String pwd = password.getText().toString();
//                if (email.isEmpty()){
//                    emailId.setError("Please enter email. ");
//                    emailId.requestFocus();
//
//                }
//                else if (pwd.isEmpty()){
//                    password.setError("Please enter your password");
//                    password.requestFocus();
//                }
//
//                else if (email.isEmpty() && pwd.isEmpty()){
//                    Toast.makeText(SignUp.this,"Fields are empty!",Toast.LENGTH_SHORT);
//
//                }
//                else if (!(email.isEmpty() && pwd.isEmpty())){
//                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (!task.isSuccessful()){
//                                Toast.makeText(SignUp.this,"Signup Unsuccessful! Please Try Again.",Toast.LENGTH_SHORT);
//
//
//                            }
//                            else{
//                                startActivity(new Intent(SignUp.this,HomeActivity.class));
//                            }
//                        }
//                    });
//                }
//
//                else{
//                    Toast.makeText(SignUp.this,"Error Occurred!",Toast.LENGTH_SHORT);
//                }
//
//            }
//        });
//
//        tvSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(SignUp.this,LoginActivity.class);
//                startActivity(i);
//
//            }
//        });
//    }








//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = emailId.getText().toString().trim();
//                String pwd = password.getText().toString().trim();
//                if (email.isEmpty()){
//
//                    System.out.println("Please Enter email.");
//                    emailId.setError("Please enter email. ");
//                    emailId.requestFocus();
//
//                }
//                else if (pwd.isEmpty()){
//                    password.setError("Please enter your password");
//                    password.requestFocus();
//                }
//
//
//                else{
//                    System.out.println("EMAIL AND PWD NOT EMPTY");
//                    System.out.println(email);
//                    System.out.println(pwd);
//                    mAuth.createUserWithEmailAndPassword(email, pwd)
//                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        System.out.println( "signupWithEmail:success");                                        FirebaseUser user = mAuth.getCurrentUser();
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        System.out.println("signUpWithEmail: Failed" + task.getException());
//                                    }
//
//                                    // ...
//                                }
//
//                            }
//
//            };
//        });