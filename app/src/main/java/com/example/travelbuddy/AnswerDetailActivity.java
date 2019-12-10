package com.example.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.Objects.Answer;
import com.example.travelbuddy.Objects.Comment;
import com.example.travelbuddy.Objects.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

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
    private TextView viewAllComment;

    private CommentRecyclerAdapter commentRecyclerAdapter;
    private RecyclerView commentListView;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private EditText commentText;
    private Button sendCommentBtn;
    private List<Comment> commentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_detail);

        Intent intent = getIntent();
        answer = (Answer) intent.getSerializableExtra("answer");
        currUser = ((TravelBuddyApplication)getApplication()).getCurUser();


        backBtn = findViewById(R.id.detailBackButton);
        answererImg = findViewById(R.id.answererImgView);
        answererName = findViewById(R.id.answererNameTextView);
        upvoteBtn = findViewById(R.id.aDetailUpBtn);
        downvoteBtn = findViewById(R.id.aDetailDownBtn);
        voteCount = findViewById(R.id.aDetailVoteTextView);
        answerBody = findViewById(R.id.answerDetailTextView);
        timePosted = findViewById(R.id.aDetailTimePostedTextView);
        viewAllComment = findViewById(R.id.viewCommentTextView);

        commentListView = findViewById(R.id.commentRecyclerView);
        sendCommentBtn = findViewById(R.id.commentBtn);
        commentText = findViewById(R.id.commentEditText);
        bottomSheet = findViewById(R.id.comment_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

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
        loadBottomSheet();
        loadComments();

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

    private void loadBottomSheet() {
        viewAllComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });


        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    DocumentReference addedDocRef = dbInstance.collection("comment").document();
                    Comment newComment = new Comment(answer.getAnswerId(), currUser.getUserId(),
                            currUser.getName(), commentText.getText().toString());
                    addedDocRef.set(newComment);

                    dbInstance.collection("answers")
                            .document(answer.getAnswerId())
                            .update("commentList", FieldValue.arrayUnion(addedDocRef.getId()));

                    commentText.setText("");
                    commentText.setHint("Leave your comment ...");
                }
            }
        });
    }

    private void loadComments() {
        final Context currContext = this;
        commentList = new LinkedList<>();

        dbInstance.collection("comment")
                .whereEqualTo("answerId", answer.getAnswerId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("DEBUG","Load comment list fail.");
                            return;
                        }

                        commentList = new LinkedList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            commentList.add(doc.toObject(Comment.class));
                        }

                        commentRecyclerAdapter = new CommentRecyclerAdapter(commentList);
                        commentListView.setLayoutManager(new LinearLayoutManager(currContext));
                        commentListView.setAdapter(commentRecyclerAdapter);
                    }
                });

    }

}
