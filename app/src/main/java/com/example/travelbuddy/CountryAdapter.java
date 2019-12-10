package com.example.travelbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Objects.Forum;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CountryAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Forum> forumList;

    public CountryAdapter(Context context, List<Forum> forumList) {
        this.mContext = context;
        this.forumList = forumList;
    }

    @Override
    public int getCount() {
        return forumList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Forum forum = forumList.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.country_item, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.countryImageView);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.nameTextView);

        if(forum.getPhotoUrl() != null && forum.getPhotoUrl() != "") {
            Picasso.get().load(forum.getPhotoUrl()).into(imageView);
        }
        nameTextView.setText(forum.getCountryName().toUpperCase());

        return convertView;
    }
}
