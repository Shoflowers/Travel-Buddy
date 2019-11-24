package com.example.travelbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.Objects.Forum;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

public class ForumRecyclerAdapter extends RecyclerView.Adapter<ForumRecyclerAdapter.ViewHolder>{

    private List<Forum> forumList;
    private ClickListener listener;

    public ForumRecyclerAdapter(List<Forum> forumList, ClickListener listener) {
        this.forumList = forumList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Forum curr = forumList.get(position);

        holder.setCountryName(curr.getCountryName());

        //Todo: set onclick for buttons and text

        if(curr.getPhotoUrl() != null && !curr.getPhotoUrl().equals("")) {
            holder.setImageView(curr.getPhotoUrl());
        }else{
            holder.setDefaultImageView();
        }

        holder.forumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPositionClicked(holder.getAdapterPosition());
            }
        });

        holder.addForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return forumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View forumView;
        private TextView countryName;
        private ImageView addForum;
        private ImageView countryImageView;
        private WeakReference<ClickListener> listenerRef;


        public ViewHolder(@NonNull View itemView, final ClickListener listener) {
            super(itemView);
            forumView = itemView;
            listenerRef = new WeakReference<>(listener);

            addForum = forumView.findViewById(R.id.addForumButton);
        }

        public void setCountryName(String name) {
            countryName = forumView.findViewById(R.id.nameTextView);
            countryName.setText(name.toUpperCase());
        }

        public void setImageView(String url){
            countryImageView = forumView.findViewById(R.id.countryImageView);
            Picasso.get().load(url).into(countryImageView);
        }

        public void setDefaultImageView(){
            countryImageView = forumView.findViewById(R.id.countryImageView);
            countryImageView.setBackgroundResource(R.drawable.countryimg);
        }

    }
}
