package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.ForumQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    Forum forum;
    private RecyclerView qListView;
    private TextView countryNameTextView;
    private ImageView countryImgView;
    private List<ForumQuestion> qList;
    private QuestionRecyclerAdapter questionRecyclerAdapter;
    private DatabaseHandler dbHandler;
    private FirebaseFirestore dbInstance;


    //default data for testing
    private String defaultCountryName = "Japan";
    private List<ForumQuestion> defaultQList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

//        Intent intent = getIntent();
//        forum = (Forum) intent.getSerializableExtra("Forum");

        // test
        // dbHandler = new DatabaseHandler();
        dbInstance = FirebaseFirestore.getInstance();
        dbInstance.collection("forums")
                .document("germany")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d("DEBUG", "get forum from db");
                        forum = task.getResult().toObject(Forum.class);
                    }
                });


        qListView = findViewById(R.id.question_list_view);
        countryNameTextView = findViewById(R.id.countryNameTextView);
        countryImgView = findViewById(R.id.countryImageView);

        countryNameTextView.setText(forum.getCountryName());
        Drawable imgDrawable = LoadImageFromWebOperations(forum.getPhotoUrl());
        if (imgDrawable != null) {
            countryImgView.setImageDrawable(imgDrawable);
        }


        loadData(forum);

    }

    private void loadData(Forum forum) {
        countryNameTextView.setText(defaultCountryName);
        //countryImgView.setImageDrawable();
        Log.d("Load Question List", "Loading" );

//        getDefaultData();
//        questionRecyclerAdapter = new QuestionRecyclerAdapter(defaultQList);
//
//        qListView.setLayoutManager(new LinearLayoutManager(this));
//        qListView.setAdapter(questionRecyclerAdapter);

        final Context currContext = this;
        qList = new LinkedList<>();

        dbInstance.collection("questions")
                .whereArrayContains("questionId", forum.getQuestionIds())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                qList.add(document.toObject(ForumQuestion.class));
                            }

                            questionRecyclerAdapter = new QuestionRecyclerAdapter(qList);
                            qListView.setLayoutManager(new LinearLayoutManager(currContext));
                            qListView.setAdapter(questionRecyclerAdapter);

                        } else {
                            Log.d("DEBUG", "Cannot get questions list.");
                        }
                    }
                });

    }

    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "country image");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private void getDefaultData() {
        ForumQuestion q1 = new ForumQuestion("q1",
                "Would it be correct to say, \"Aus diesem Grund möchte ich sie bitten, meinen Vertrag schnell wie möglich zu kündigen\"?",
                "empty",
                "user1",
                Arrays.asList("food", "language"),
                Arrays.asList("ans11", "ans12"),
                100, new Timestamp(System.currentTimeMillis()), "Germany", 100);

        ForumQuestion q2 = new ForumQuestion("q2",
                "What my experience was like living in Hamburg for 8 months.",
                "empty",
                "user2",
                Arrays.asList("food", "language"),
                Arrays.asList("ans21", "ans22"),
                100, new Timestamp(System.currentTimeMillis()),"Germany", 100);


        ForumQuestion q3 = new ForumQuestion("q3",
                "What is the difference between Northern people and Southern people of Germany?",
                "empty",
                "user3",
                Arrays.asList("food", "language"),
                Arrays.asList("ans31", "ans32"),
                100, new Timestamp(System.currentTimeMillis()),"Germany", 100);

        defaultQList = new ArrayList<>(3);
        defaultQList.add(q1);
        defaultQList.add(q2);
        defaultQList.add(q3);
    }
}
