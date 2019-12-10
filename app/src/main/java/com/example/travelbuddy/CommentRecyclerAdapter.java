package com.example.travelbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.Objects.Comment;


import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private List<Comment> commentList;

    public CommentRecyclerAdapter(List<Comment> lst) {
        this.commentList = lst;
    }

    public CommentRecyclerAdapter(){}

    @NonNull
    @Override
    public CommentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.ViewHolder holder, int position) {

        Comment curr = commentList.get(position);
        holder.setCommenterName(curr.getCommenterName());
        holder.setCommentBody(curr.getCommentBody());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View commentCardView;
        private TextView commenterName;
        private TextView commentBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentCardView = itemView;
        }

        public void setCommenterName(String name) {
            commenterName = commentCardView.findViewById(R.id.commenterNameTextView);
            commenterName.setText(name);
        }

        public void setCommentBody(String comment) {
            commentBody= commentCardView.findViewById(R.id.commentTextView);
            commentBody.setText(comment);
        }
    }

}
