package com.example.travelbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.Objects.ClickListener;
import com.example.travelbuddy.Objects.Forum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ForumRecyclerAdapter extends RecyclerView.Adapter<ForumRecyclerAdapter.ViewHolder> implements Filterable {

    private List<Forum> forumList;
    private List<Forum> filterestedForumList;
    private ClickListener listener;
    private boolean showAddbutton;

    public ForumRecyclerAdapter(List<Forum> forumList, ClickListener listener, boolean showAddbutton) {
        this.forumList = forumList;
        this.filterestedForumList = forumList;
        this.listener = listener;
        this.showAddbutton = showAddbutton;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_item, parent, false);
        return new ViewHolder(view, showAddbutton);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Forum curr = filterestedForumList.get(position);

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
                listener.onButtonClicked(filterestedForumList.get(holder.getAdapterPosition()));
            }
        });
    }

    public void deleteItem(int position){
        listener.onDeleteItem(filterestedForumList.get(position));
    }


@Override
    public int getItemCount() {
        return filterestedForumList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                filterestedForumList = (ArrayList<Forum>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Forum> FilteredArrList = new ArrayList<Forum>();

                if (forumList == null) {
                    forumList = new ArrayList<Forum>(filterestedForumList); // saves the original data in mOriginalValues
                }
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = forumList.size();
                    results.values = forumList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < forumList.size(); i++) {
                        String data = forumList.get(i).getCountryName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(forumList.get(i));
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

        private View forumView;
        private TextView countryName;
        private ImageView addForum;
        private ImageView countryImageView;


        public ViewHolder(@NonNull View itemView, boolean showAddButton) {
            super(itemView);
            forumView = itemView;

            addForum = forumView.findViewById(R.id.addForumButton);
            if(!showAddButton) addForum.setVisibility(View.INVISIBLE);
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
