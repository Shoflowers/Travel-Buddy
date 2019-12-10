package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.SQLOutput;

public class SettingsActivity extends AppCompatActivity {

    public User curUser;
    private DatabaseHandler dbHandler;

    private ImageView cancelButton;
    private ImageView confirmButton;
    private ImageView profileImageView;
    private CardView changePicButton;

    private Uri filePath;

    private EditText nameText;
    private EditText userNameText;
    private EditText bioText;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHandler = new DatabaseHandler();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        curUser = ((TravelBuddyApplication) this.getApplication()).getCurUser();

        nameText = findViewById(R.id.nameEditText);
        userNameText = findViewById(R.id.userNameEditText);
        bioText = findViewById(R.id.bioEditText);

        nameText.setText(curUser.getName());
        userNameText.setText(curUser.getUsername());
        bioText.setText(curUser.getProfileBio());

        profileImageView = findViewById(R.id.profileImageView);

        changePicButton = findViewById(R.id.changePictureButton);
        changePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSettings();
            }
        });

        if(curUser.getProfilePhotoUrl() != null && curUser.getProfilePhotoUrl() != ""){
            Picasso.get().load(curUser.getProfilePhotoUrl()).into(profileImageView);
        }
    }

    public void deleteAccount(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);
        System.out.println("Button clicked");
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("User account deleted.");
                            ((TravelBuddyApplication) SettingsActivity.this.getApplication()).setCurUser(null);
                            Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    }
                });
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        ((TravelBuddyApplication) SettingsActivity.this.getApplication()).setCurUser(null);
        //((TravelBuddyApplication) LoginActivity.class.getClasses().setCurUser(null);
        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void confirmSettings(){
        String newName = nameText.getText().toString();
        String newUsername = userNameText.getText().toString();
        String newBio = bioText.getText().toString();

        if(newName.length() > 20){
            Toast.makeText(SettingsActivity.this, "Name too long", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newUsername.length() > 20){
            Toast.makeText(SettingsActivity.this, "Username too long", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newUsername.equals("") || newUsername.contains(" ")){
            Toast.makeText(SettingsActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newBio.length() > 80){
            Toast.makeText(SettingsActivity.this, "Bio Too Long", Toast.LENGTH_SHORT).show();
            return;
        }

        curUser.setName(newName);
        curUser.setUsername(newUsername);
        curUser.setProfileBio(newBio);
        dbHandler.updateUser(curUser);
        finish();

    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage();
            Picasso.get().load(filePath).into(profileImageView);
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final StorageReference ref = storageReference.child("images/"+ curUser.getUserId());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SettingsActivity.this, "Uploaded Image", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    System.out.println("get Image Url succeeded");
                                    curUser.setProfilePhotoUrl(uri.toString());
                                    dbHandler.updateUser(curUser);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    System.out.println("get Image Url failed");
                                }
                            });
                        }
                    });
        }
    }


}
