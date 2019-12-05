package com.example.travelbuddy;

import com.example.travelbuddy.Objects.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> {

    public List<ForumQuestion> qList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public QuestionRecyclerAdapter(List<ForumQuestion> qList) {
        this.qList = qList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        ForumQuestion curr = qList.get(position);
        final String currQuestionId = curr.getQuestionId();

        Log.d("qListItem", "In binding view");
        holder.setQuestionText(curr.getQuestionTitle());
        holder.setCommentCount(curr.getAnswerIds().size());
        holder.setVote(curr.getVotes());
        holder.setViewCount(curr.getViewCount());
        holder.setTime(curr.getDateTime());
        holder.setButtons();

        //Todo: set onclick for buttons and text (Right now only works for one click)

        holder.downvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEBUG", "click downvote");

                firebaseFirestore.collection("questions")
                        .document(currQuestionId)
                        .update("votes", FieldValue.increment(-1));
            }
        });


        holder.upvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("questions")
                        .document(currQuestionId)
                        .update("votes", FieldValue.increment(1));
            }
        });


        holder.qView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("questions")
                        .document(currQuestionId)
                        .update("viewCount", FieldValue.increment(1));

                Intent newIntent = new Intent(context, AnswerActivity.class);
                newIntent.putExtra("question", qList.get(holder.getAdapterPosition()));
                //todo: add user to intent
                //newIntent.putExtra("user", firebaseAuth.getCurrentUser());
                context.startActivity(newIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return qList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private View qView;
        private TextView question;
        private ImageButton upvoteBtn;
        private ImageButton downvoteBtn;
        private TextView timePosted;
        private TextView viewCount;
        private TextView answerCount;
        private TextView vote;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qView = itemView;
        }

        public void setButtons() {
            upvoteBtn = qView.findViewById(R.id.upvoteBtn);
            downvoteBtn = qView.findViewById(R.id.downvoteBtn);
        }

        public void setQuestionText(String questionText) {
            question = qView.findViewById(R.id.questionTitle);
            question.setText(questionText);
            Log.d("qListItem", "In set Question text");
        }

        public void setCommentCount(int count) {
            answerCount = qView.findViewById(R.id.answerCount);
            answerCount.setText("" + count);
        }

        public void setViewCount(int count) {
            viewCount = qView.findViewById(R.id.viewCount);
            viewCount.setText("" + count);
        }

        public void setVote(int count) {
            vote = qView.findViewById(R.id.voteCount);
            vote.setText("" + count);
        }

        public void setTime(Date dateTime) {
            timePosted = qView.findViewById(R.id.timePosted);
            timePosted.setText(dateTime.toString().substring(0,16));
        }

    }
}
