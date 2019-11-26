package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.ForumQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private RequestQueue requestQueue;


    //default data for testing
    private String defaultCountryName = "Japan";
    private List<ForumQuestion> defaultQList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("Forum");

        qListView = findViewById(R.id.question_list_view);
        countryNameTextView = findViewById(R.id.countryNameTextView);
        countryImgView = findViewById(R.id.countryImageView);

        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        dbInstance = FirebaseFirestore.getInstance();

        countryNameTextView.setText(forum.getCountryName());

        loadImage(forum.getPhotoUrl());

        loadData(forum);

    }

    private void loadData(Forum forum) {
        //countryNameTextView.setText(defaultCountryName);
        //countryImgView.setImageDrawable();
        Log.d("Load Question List", "Loading" );


        final Context currContext = this;
        qList = new LinkedList<>();
        Log.d("DEBUG", "qIDs: " + forum.getQuestionIds().size());

        dbInstance.collection("questions")
                .whereEqualTo("forumId", forum.getForumId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DEBUG", "getting " +document.getData().toString());
                                qList.add(document.toObject(ForumQuestion.class));
                            }


                            Log.d("DEBUG", "successfully load " + qList.size());

                            questionRecyclerAdapter = new QuestionRecyclerAdapter(qList);
                            qListView.setLayoutManager(new LinearLayoutManager(currContext));
                            qListView.setAdapter(questionRecyclerAdapter);

                        } else {
                            Log.d("DEBUG", "Cannot get questions list.");
                        }
                    }
                });

    }

    private void loadImage(String url) {
        ImageRequest request = new ImageRequest(url,
            new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    countryImgView.setImageBitmap(response);
                }
        }, 1440, 720, null,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("DEBUG", "fail to load country image");
                }
            });

        requestQueue.add(request);
    }

    private void getDefaultData() {
        ForumQuestion q1 = new ForumQuestion("q1",
                "Would it be correct to say, \"Aus diesem Grund möchte ich sie bitten, meinen Vertrag schnell wie möglich zu kündigen\"?",
                "empty",
                "user1",
                Arrays.asList("food", "language"),
                Arrays.asList("ans11", "ans12"),
                100, new Date(System.currentTimeMillis()), "Germany", 100);

        ForumQuestion q2 = new ForumQuestion("q2",
                "What my experience was like living in Hamburg for 8 months.",
                "empty",
                "user2",
                Arrays.asList("food", "language"),
                Arrays.asList("ans21", "ans22"),
                100, new Date(System.currentTimeMillis()),"Germany", 100);


        ForumQuestion q3 = new ForumQuestion("q3",
                "What is the difference between Northern people and Southern people of Germany?",
                "empty",
                "user3",
                Arrays.asList("food", "language"),
                Arrays.asList("ans31", "ans32"),
                100, new Date(System.currentTimeMillis()),"Germany", 100);

        defaultQList = new ArrayList<>(3);
        defaultQList.add(q1);
        defaultQList.add(q2);
        defaultQList.add(q3);
    }
}
