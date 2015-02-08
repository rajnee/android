package com.rneel.apps.mytwitter.models;

/**
 * Created by rneel on 2/6/15.
 */
// models/Tweet.java

import android.util.Log;

import com.activeandroid.annotation.Table;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.ArrayList;

@Table(name = "Tweets")
public class Tweet extends Model {
    // Define database columns and associated fields
    @Column(name = "userId")
    String userId;
    @Column(name = "userHandle")
    String userHandle;
    @Column(name = "timestamp", index = true)
    String timestamp;
    @Column(name = "body")
    String body;

    @Column(name="tweetId", index = true)
    String tweetId;


    public String getTweetId() {
        return tweetId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        super();

        Log.d("Tweet", "JSON object" + object.toString() );
        try {
            JSONObject userObj = object.getJSONObject("user");
            this.tweetId = object.getString("id_str");
            this.userId = userObj.getString("id");
            this.userHandle = userObj.getString("name");
            this.timestamp = object.getString("created_at");
            this.body = object.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }
}
