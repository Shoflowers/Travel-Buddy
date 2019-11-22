package com.example.travelbuddy;

import com.example.travelbuddy.Objects.*;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> {

    List<ForumQuestion> qList;

    public QuestionRecyclerAdapter(List<ForumQuestion> qList) {
        this.qList = qList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForumQuestion curr = qList.get(position);

        Log.d("qListItem", "In binding view");
        holder.setQuestionText(curr.getQuestionTitle());
        holder.setCommentCount(curr.getAnswerIds().size());
        holder.setVote(curr.getVotes());
        holder.setViewCount(curr.getViewCount());
        holder.setTime(curr.getDateTime().toString());

        //Todo: set onclick for buttons and text

//        holder.downvoteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        holder.upvoteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        holder.question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return qList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View qView;
        private TextView question;
        private Button upvoteBtn;
        private Button downvoteBtn;
        private TextView timePosted;
        private TextView viewCount;
        private TextView answerCount;
        private TextView vote;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qView = itemView;
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

        public void setTime(String time) {
            timePosted = qView.findViewById(R.id.timePosted);
            timePosted.setText(time);
        }

    }
}
