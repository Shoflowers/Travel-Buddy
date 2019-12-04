package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.travelbuddy.Objects.Answer;
import com.example.travelbuddy.Objects.ForumQuestion;
import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
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

        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        dbInstance = FirebaseFirestore.getInstance();

        loadQuestionData();
        loadUserData(question.getUserId());



    }

    private void loadQuestionData() {
        qTitle.setText(question.getQuestionTitle());
        qBody.setText(question.getQuestionBody());
        qTimePosted.setText(question.getDateTime().toString().substring(0,16));
        qViewCount.setText(question.getViewCount() + "");
        qAnswerCount.setText(question.getAnswerIds().size() + "");
    }

    private void loadUserData(String uid) {

       dbInstance.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    User questioner = task.getResult().toObject(User.class);
                    questionerUserName.setText(questioner.getName());

                    if (!questioner.getProfilePhotoUrl().isEmpty()) {
                        ImageRequest request = new ImageRequest(questioner.getProfilePhotoUrl(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        questionerImg.setImageBitmap(response);
                                    }
                                }, 40, 40, null,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("DEBUG", "fail to load user image");
                                    }
                                });

                        requestQueue.add(request);
                    }
                }
            }
        });
    }

    private void loadAnswersData() {
        final Context currContext = this;
        answerList = new LinkedList<>();

        //todo
    }
}
