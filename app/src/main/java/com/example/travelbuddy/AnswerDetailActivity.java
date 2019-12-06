package com.example.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelbuddy.Objects.Answer;
import com.example.travelbuddy.Objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class AnswerDetailActivity extends AppCompatActivity {

    private Answer answer;
    private User currUser;
    private FirebaseFirestore dbInstance;


    private ImageView backBtn;
    private ImageView answererImg;
    private TextView answererName;
    private ImageButton upvoteBtn;
    private ImageButton downvoteBtn;
    private TextView voteCount;
    private TextView answerBody;
    private TextView timePosted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_detail);

        Intent intent = getIntent();
        answer = (Answer) intent.getSerializableExtra("answer");
        currUser = ((TravelBuddyApplication)getApplication()).getCurUser();
        dbInstance = FirebaseFirestore.getInstance();

        backBtn = findViewById(R.id.detailBackButton);
        answererImg = findViewById(R.id.answererImgView);
        answererName = findViewById(R.id.answererNameTextView);
        upvoteBtn = findViewById(R.id.aDetailUpBtn);
        downvoteBtn = findViewById(R.id.aDetailDownBtn);
        voteCount = findViewById(R.id.aDetailVoteTextView);
        answerBody = findViewById(R.id.answerDetailTextView);
        timePosted = findViewById(R.id.aDetailTimePostedTextView);

        dbInstance.collection("answers").document(answer.getAnswerId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("DEBUG", "Can't get answer");
                }

                answer = documentSnapshot.toObject(Answer.class);
                loadAnswer();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        upvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbInstance.collection("answers")
                        .document(answer.getAnswerId())
                        .update("vote", FieldValue.increment(1));

            }
        });

        downvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbInstance.collection("answers")
                        .document(answer.getAnswerId())
                        .update("vote", FieldValue.increment(-1));

            }
        });

        loadAnswer();

    }

    private void loadAnswer() {
        answerBody.setText(answer.getAnswerBody());
        timePosted.setText(answer.getDateTime().toString().substring(0,16));
        voteCount.setText(answer.getVote() + "");
        answererName.setText(answer.getUserName());

        if (!answer.getUserPhotoUrl().isEmpty()) {
            Picasso.get().load(answer.getUserPhotoUrl()).into(answererImg);
        }
    }


}
