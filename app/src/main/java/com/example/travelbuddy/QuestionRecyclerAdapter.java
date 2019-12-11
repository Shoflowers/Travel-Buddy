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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> implements Filterable {

    public List<ForumQuestion> qList;
    public List<ForumQuestion> filteredQList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public QuestionRecyclerAdapter(List<ForumQuestion> qList) {
        this.qList = qList;
        this.filteredQList = qList;
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
        ForumQuestion curr = filteredQList.get(position);
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
                newIntent.putExtra("question", filteredQList.get(holder.getAdapterPosition()));
                //todo: add user to intent
                //newIntent.putExtra("user", firebaseAuth.getCurrentUser());
                context.startActivity(newIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredQList.size();
    }

    @Override
    public Filter getFilter() {

        final Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                filteredQList = (LinkedList<ForumQuestion>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                LinkedList<ForumQuestion> FilteredArrList = new LinkedList<>();

                if (qList == null) {
                    qList = new LinkedList<>(filteredQList); // saves the original data in mOriginalValues
                }
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = qList.size();
                    results.values = qList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < qList.size(); i++) {
                        String data = qList.get(i).getQuestionTitle();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(qList.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
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
