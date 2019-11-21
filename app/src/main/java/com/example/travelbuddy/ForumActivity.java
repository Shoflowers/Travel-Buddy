package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.User;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    Forum forum;
    private RecyclerView qListView;
    private List<Question> qList;
    private QuestionRecyclerAdapter questionRecyclerAdapter;
    private DatabaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("Forum");


        //xiaoya's code
        qListView = findViewById(R.id.question_list_view);


        //TODO: create function in DatabaseHandler to get the question_list
        //qList = dbHandler.getQestionList(String QuestionID);

        questionRecyclerAdapter = new QuestionRecyclerAdapter(qList);

        //qListView.setLayoutManager(new LinearLayoutManager(this));
        qListView.setAdapter(questionRecyclerAdapter);


    }
}
