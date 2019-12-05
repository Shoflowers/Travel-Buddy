package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AnswerActivity extends AppCompatActivity {

    private ForumQuestion question;
    private User currUser;

    private ImageView questionerImg;
    private TextView questionerUserName;
    private TextView qTitle;
    private TextView qBody;
    private TextView qTimePosted;
    private TextView qViewCount;
    private TextView qAnswerCount;
    private List<Answer> answerList;
    private AnswerRecyclerAdapter answerRecyclerAdapter;
    private RecyclerView answerListView;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private Button addBtn;
    private Button cancelBtn;
    private Button sendBtn;
    private ImageView backBtn;

    private FirebaseFirestore dbInstance;
    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        Intent intent = getIntent();
        question = (ForumQuestion) intent.getSerializableExtra("question");


        backBtn = findViewById(R.id.answerBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        answerListView = findViewById(R.id.answerListView);
        questionerImg = findViewById(R.id.questionerImgView);
        questionerUserName = findViewById(R.id.userNameTextView);
        qTitle = findViewById(R.id.qTitleTextView);
        qBody = findViewById(R.id.qBodyTextView);
        qTimePosted = findViewById(R.id.questionTimePosted);
        qViewCount = findViewById(R.id.qViewCount);
        qAnswerCount = findViewById(R.id.qAnswerCount);
        addBtn = findViewById(R.id.newAnswerBtn);
        cancelBtn = findViewById(R.id.aCancelButton);
        sendBtn = findViewById(R.id.aSendButton);
        bottomSheet = findViewById(R.id.new_answer_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        dbInstance = FirebaseFirestore.getInstance();

        currUser = ((TravelBuddyApplication)getApplication()).getCurUser();

        loadQuestionData();
        loadUserData(question.getUserId());
        loadAnswersData();
        loadBottomSheet();



    }

    private void loadBottomSheet() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    EditText aBody = findViewById(R.id.answerEditText);

                    //add new question to questions collection
                    DocumentReference addedDocRef = dbInstance.collection("answers").document();
                    Answer newAnswer = new Answer(aBody.getText().toString(), addedDocRef.getId(),
                            question.getQuestionId(), currUser.getUserId(), 0, 0, new Date(System.currentTimeMillis()),
                            new LinkedList<String>(), currUser.getName(), currUser.getProfilePhotoUrl() );
                    addedDocRef.set(newAnswer);

                    //update forum and users
                    dbInstance.collection("questions")
                            .document(question.getQuestionId())
                            .update("answerIds", FieldValue.arrayUnion(addedDocRef.getId()));

                    dbInstance.collection("users")
                            .document(currUser.getUserId())
                            .update("answerIds", FieldValue.arrayUnion(addedDocRef.getId()));

                    //clear text
                    aBody.setHint("Write your answer ...");
                }
            }
        });
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

        dbInstance.collection("answers")
                .whereEqualTo("questionId", question.getQuestionId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("DEBUG","Load answer list fail.");
                            return;
                        }

                        answerList = new LinkedList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            answerList.add(doc.toObject(Answer.class));
                        }

                        answerRecyclerAdapter = new AnswerRecyclerAdapter(answerList);
                        answerListView.setLayoutManager(new LinearLayoutManager(currContext));
                        answerListView.setAdapter(answerRecyclerAdapter);
                    }
                });
    }
}
