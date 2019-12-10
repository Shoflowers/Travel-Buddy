package com.example.travelbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.Objects.ClickListener;
import com.example.travelbuddy.Objects.Forum;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private final List<Forum> forumList;
    private ClickListener listener;

    public CountryAdapter(List<Forum> forumList, ClickListener listener) {
        this.listener = listener;
        this.forumList = forumList;
    }

    @NonNull
    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CountryAdapter.ViewHolder holder, int position) {
        Forum curForum = forumList.get(position);

        holder.setCountryName(curForum.getCountryName());

        if(curForum.getPhotoUrl()!=null && curForum.getPhotoUrl()!=""){
            holder.setImageView(curForum.getPhotoUrl());
        }

        holder.forumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPositionClicked(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return forumList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View forumView;
        private TextView countryName;
        private ImageView countryImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            forumView = itemView;
        }

        public void setCountryName(String name) {
            countryName = forumView.findViewById(R.id.nameTextView);
            countryName.setText(name.toUpperCase());
        }

        public void setImageView(String url){
            countryImageView = forumView.findViewById(R.id.countryImageView);
            Picasso.get().load(url).into(countryImageView);
        }

    }
}
