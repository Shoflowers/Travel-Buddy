package com.example.travelbuddy;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import com.example.travelbuddy.Objects.Answer;

import java.util.List;


public class AnswerRecyclerAdapter extends RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder> {

    List<Answer> answerList;
    private FirebaseFirestore firebaseFirestore;

    public AnswerRecyclerAdapter(List<Answer> aList) {this.answerList = aList;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Answer curr = answerList.get(position);
        final String currAnswerId = curr.getAnswerId();

        holder.setAnswerCommentCount(curr.getCommentList().size());
        holder.setAnswerText(curr);
        holder.setAnswerViewCount(curr.getView());
        holder.setButtons();
        holder.setVoteCount(curr.getVote());
        holder.setAnswererImg(curr.getUserPhotoUrl());
        holder.setAnswererName(curr.getUserName());
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

            answerTimePosted.setText(answer.getDate().toString().substring(0,16));
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
            Picasso.get().load(url).into(answererImg);
        }
    }
}
