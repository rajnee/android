package com.example.rneel.gridimagesearch.view;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rneel.gridimagesearch.R;
import com.example.rneel.gridimagesearch.model.ImageResult;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by rneel on 1/31/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> imageResultList) {
        super(context, android.R.layout.simple_list_item_1, imageResultList);
    }

    private static class ViewHolder {
        ImageView ivPicture;
        TextView tvTitle;
    } 
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_layout, parent,false);
            vh = new ViewHolder();
            vh.ivPicture = (ImageView)convertView.findViewById(R.id.ivPicture);
            vh.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            convertView.setTag(vh);
        }
        else 
        {
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tvTitle.setText(Html.fromHtml(getItem(position).getTitle()));
        Picasso.with(getContext()).load(getItem(position).getTburl()).into(vh.ivPicture);
        return convertView;
    }
}
