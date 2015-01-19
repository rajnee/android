package com.example.rneel.simpletodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rneel on 1/18/15.
 */
public class Item {
    private String description;
    private Date date;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/y");

    public Item(String s) {
        String[] i = s.split("#SEP#");
        String desc = i[0];
        String dt = null;
        if (i.length == 2) {
            dt = i[1];
        }
        init(desc, dt);
    }

    public Item(String description, String dt) {
        init(description, dt);
    }

    public void init(String description, String dt) {
        this.description = description;
        try {
            date = simpleDateFormat.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        return description + "#SEP#" + date == null?"":simpleDateFormat.format(date);
    }
}
