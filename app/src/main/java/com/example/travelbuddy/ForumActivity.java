package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.ForumQuestion;
import com.example.travelbuddy.Objects.User;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    Forum forum;
    private RecyclerView qListView;
    private TextView countryNameTextView;
    private ImageView countryImgView;
    private List<ForumQuestion> qList;
    private QuestionRecyclerAdapter questionRecyclerAdapter;
    private DatabaseHandler dbHandler;


    //default data for testing
    private String defaultCountryName = "Japan";
    private List<ForumQuestion> defaultQList;

    private void getDefaultData() {
        ForumQuestion q1 = new ForumQuestion("q1",
                "Would it be correct to say, \"Aus diesem Grund möchte ich sie bitten, meinen Vertrag schnell wie möglich zu kündigen\"?",
                "empty",
                "user1",
                Arrays.asList("food", "language"),
                Arrays.asList("ans11", "ans12"),
                100, new Timestamp(System.currentTimeMillis()));

        ForumQuestion q2 = new ForumQuestion("q2",
                "What my experience was like living in Hamburg for 8 months.",
                "empty",
                "user2",
                Arrays.asList("food", "language"),
                Arrays.asList("ans21", "ans22"),
                100, new Timestamp(System.currentTimeMillis()));


        ForumQuestion q3 = new ForumQuestion("q3",
                "What is the difference between Northern people and Southern people of Germany?",
                "empty",
                "user3",
                Arrays.asList("food", "language"),
                Arrays.asList("ans31", "ans32"),
                100, new Timestamp(System.currentTimeMillis()));

        defaultQList = new ArrayList<>(3);
        defaultQList.add(q1);
        defaultQList.add(q2);
        defaultQList.add(q3);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("Forum");


        //xiaoya's code
        qListView = findViewById(R.id.question_list_view);
        countryNameTextView = findViewById(R.id.countryNameTextView);
        countryImgView = findViewById(R.id.countryImageView);


        //TODO: create function in DatabaseHandler to get the question_list
        //qList = dbHandler.getQestionList(String QuestionID);

        loadData(intent);

    }

    private void loadData(Intent intent) {
        countryNameTextView.setText(defaultCountryName);
        //countryImgView.setImageDrawable();
        Log.d("Load Question List", "Loading" );

        getDefaultData();
        questionRecyclerAdapter = new QuestionRecyclerAdapter(defaultQList);

        qListView.setLayoutManager(new LinearLayoutManager(this));
        qListView.setAdapter(questionRecyclerAdapter);


    }
}
