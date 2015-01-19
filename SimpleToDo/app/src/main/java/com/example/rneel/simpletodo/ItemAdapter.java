package com.example.rneel.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rneel on 1/18/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        TextView dateView;
        TextView descriptionView;
    }

    public ItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ItemAdapter(Context context, int resource,  List<Item> objects) {
        super(context, resource,  objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.dateView = (TextView)parent.findViewById(R.id.textDate);
            viewHolder.descriptionView = (TextView)parent.findViewById(R.id.textDesc);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_item,parent,false);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.dateView.setText(item.getDate().toString());
        viewHolder.descriptionView.setText(item.getDescription());
        return convertView;
    }
}
