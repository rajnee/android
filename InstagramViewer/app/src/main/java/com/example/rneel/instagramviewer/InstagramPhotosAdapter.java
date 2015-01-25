package com.example.rneel.instagramviewer;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rneel on 1/24/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{

    private static class ViewHolder {
        ImageView imageView;
        TextView caption;
        TextView likedByCount;
        TextView user;
    }

    public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
        super(context, 0, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        ViewHolder vh;
        if (convertView == null)
        {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.instagram_photo_layout,parent, false);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.test_photo_layout1,parent, false);
            vh = new ViewHolder();
            vh.imageView = (ImageView)convertView.findViewById(R.id.ivPhoto);
            vh.caption = (TextView)convertView.findViewById(R.id.tvTitle);
            vh.likedByCount = (TextView)convertView.findViewById(R.id.tvLikedByCount);
            vh.user = (TextView)convertView.findViewById(R.id.tvUser);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder)convertView.getTag();
        }

        if (photo.imageHeight >= 0) {
            vh.imageView.getLayoutParams().height = photo.imageHeight;
        }
        else
        {
            vh.imageView.getLayoutParams().height = 0;
        }
        Picasso.with(getContext()).load(photo.imageUrl).into(vh.imageView);
        vh.caption.setText(photo.title);
        vh.user.setText(photo.userName);
        vh.likedByCount.setText(photo.likesCount);
        return convertView;
    }
}
