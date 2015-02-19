package com.rneel.apps.mytwitter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rneel.apps.mytwitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rneel on 2/6/15.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {
    public TweetListAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (position < getCount()) {
            final Tweet tweet = getItem(position);
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tweet, parent, false);
                vh.tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
                vh.tvUser = (TextView) convertView.findViewById(R.id.tvUser);
                vh.tvRelativeDate = (TextView) convertView.findViewById(R.id.tvRelativeTime);
                vh.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);                
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tvUser.setText(tweet.getUserHandle());
            vh.tvTweet.setText(tweet.getBody());
            vh.tvRelativeDate.setText(tweet.getRelativeTime());
            Picasso.with(getContext()).load(tweet.getProfileImage()).into(vh.ivProfile);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(),ProfileActivity.class);
                    i.putExtra("screen_name",tweet.getUserId());
                    getContext().startActivity(i);

                }
            });
        }
        return convertView;
    }
    
    private static class ViewHolder {
        TextView tvTweet;
        TextView tvUser;
        TextView tvRelativeDate;
        ImageView ivProfile;
    }
}
