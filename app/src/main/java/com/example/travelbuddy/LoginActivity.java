package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);
        if (user != null) {
            // User is signed in
            User curUser = ((TravelBuddyApplication) LoginActivity.this.getApplication()).getCurUser();
            if (curUser != null) {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
            } else {
                loginSuccess();
            }
        }

        final Activity curActivity = this;
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginSuccess();

                String email = emailId.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                if (email.isEmpty()) {
                    emailId.setError("Please enter email. ");
                    emailId.requestFocus();

                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else {

                    mAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(curActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        loginSuccess();

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
                Intent intSignUp = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intSignUp);
            }
        });
    }

    public void loginSuccess(){
        System.out.println( "signInWithEmail:success");
        Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();

        FirebaseUser fbUser = mAuth.getCurrentUser();
        fbUser.getUid();

        DatabaseHandler dbHandler = new DatabaseHandler();
        FirebaseFirestore dbInstance = dbHandler.getDbInstance();

        dbInstance.collection("users").document(fbUser.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.out.println("UserRef get data failed");
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            User curUser = snapshot.toObject(User.class);

                            if(((TravelBuddyApplication) LoginActivity.this.getApplication()).getCurUser() == null) {
                                ((TravelBuddyApplication) LoginActivity.this.getApplication()).setCurUser(curUser);
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                            }
                            ((TravelBuddyApplication) LoginActivity.this.getApplication()).setCurUser(curUser);

                        } else {
                            System.out.println("UserRef get data failed");
                        }
                    }
                });
    }
}