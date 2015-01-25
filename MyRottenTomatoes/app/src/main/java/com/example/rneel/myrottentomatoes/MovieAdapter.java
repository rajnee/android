package com.example.rneel.myrottentomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rneel on 1/21/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }
        TextView tvTitle  = (TextView)convertView.findViewById(R.id.tvTitle);
        TextView tvScore  = (TextView)convertView.findViewById(R.id.tvScore);
        tvTitle.setText(movie.getTitle());
        tvScore.setText("" + movie.getScore());
        return convertView;
    }
}
