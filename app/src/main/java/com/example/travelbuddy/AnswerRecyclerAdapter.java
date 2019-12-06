package com.example.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import com.example.travelbuddy.Objects.Answer;

import java.util.List;


public class AnswerRecyclerAdapter extends RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder> {

    List<Answer> answerList;
    private FirebaseFirestore firebaseFirestore;
    public Context context;

    public AnswerRecyclerAdapter(List<Answer> aList) {this.answerList = aList;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Answer curr = answerList.get(position);
        final String currAnswerId = curr.getAnswerId();

        holder.setAnswerCommentCount(curr.getCommentList().size());
        holder.setAnswerText(curr);
        holder.setAnswerViewCount(curr.getView());
        holder.setVoteCount(curr.getVote());
        holder.setAnswererImg(curr.getUserPhotoUrl());
        holder.setAnswererName(curr.getUserName());
        holder.setButtons();

        holder.upvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("answers")
                        .document(currAnswerId)
                        .update("vote", FieldValue.increment(1));
            }
        });

        holder.downvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("answers")
                        .document(currAnswerId)
                        .update("vote", FieldValue.increment(-1));
            }
        });

        holder.answerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("answers")
                        .document(currAnswerId)
                        .update("view", FieldValue.increment(1));

                Intent newIntent = new Intent(context, AnswerDetailActivity.class);
                newIntent.putExtra("answer", answerList.get(holder.getAdapterPosition()));

                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View answerView;
        ImageButton upvoteBtn;
        ImageButton downvoteBtn;
        TextView voteCount;
        TextView answerPreview;
        TextView answerTimePosted;
        TextView answerViewCount;
        TextView answerCommentCount;
        ImageView answererImg;
        TextView answererName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerView = itemView;
        }

        public void setAnswerText(Answer answer) {
            answerPreview = answerView.findViewById(R.id.answerPreviewTextView);
            answerTimePosted = answerView.findViewById(R.id.answerTimePosted);

            String previewStr = answer.getAnswerBody().length() > 220 ?
                    answer.getAnswerBody().substring(0, 216) + " ..." :
                    answer.getAnswerBody();
            answerPreview.setText(previewStr);

            answerTimePosted.setText(answer.getDateTime().toString().substring(0,16));
        }

        public void setVoteCount(int count) {
            voteCount = answerView.findViewById(R.id.answerVoteCount);
            voteCount.setText(count + "");
        }

        public void setAnswerViewCount(int count) {
            answerViewCount = answerView.findViewById(R.id.answerViewCount);
            answerViewCount.setText(count + "");
        }

        public void setAnswerCommentCount(int count) {
            answerCommentCount = answerView.findViewById(R.id.commentCount);
            answerCommentCount.setText(count + "");
        }

        public void setButtons() {
            upvoteBtn = answerView.findViewById(R.id.answerUpvoteBtn);
            downvoteBtn = answerView.findViewById(R.id.answerDownvoteBtn);


        }

        public void setAnswererName(String userName ) {
            answererName = answerView.findViewById(R.id.userNameTextView);
            answererName.setText(userName);
        }

        public void setAnswererImg(String url) {
            answererImg = answerView.findViewById(R.id.profileImgView);
            if (!url.isEmpty()) {
                Picasso.get().load(url).into(answererImg);
            }
        }
    }
}
