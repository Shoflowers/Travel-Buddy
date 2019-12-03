package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.travelbuddy.Objects.Answer;
import com.example.travelbuddy.Objects.ForumQuestion;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AnswerActivity extends AppCompatActivity {

    private ForumQuestion question;
    private ImageView questionerImg;
    private TextView questionerUserName;
    private TextView qTitle;
    private TextView qBody;
    private TextView qTimePosted;
    private TextView qViewCount;
    private TextView qAnswerCount;
    private List<Answer> answerList;
    private AnswerRecyclerAdapter answerRecyclerAdapter;

    private FirebaseFirestore dbInstance;
    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        Intent intent = getIntent();
        question = (ForumQuestion) intent.getSerializableExtra("question");

        questionerImg = findViewById(R.id.questionerImgView);
        questionerUserName = findViewById(R.id.userNameTextView);
        qTitle = findViewById(R.id.qTitleTextView);
        qBody = findViewById(R.id.qBodyTextView);
        qTimePosted = findViewById(R.id.questionTimePosted);
        qViewCount = findViewById(R.id.qViewCount);
        qAnswerCount = findViewById(R.id.qAnswerCount);


    }
}
