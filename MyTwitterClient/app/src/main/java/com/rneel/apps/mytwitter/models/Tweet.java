package com.rneel.apps.mytwitter.models;

/**
 * Created by rneel on 2/6/15.
 */
// models/Tweet.java

import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.annotation.Table;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.text.ParseException;


public abstract class Tweet extends Model {
    // Define database columns and associated fields
    public abstract long getTweetId();

    public abstract String getProfileImage();
    public abstract String getUserId();

    public abstract void setUserId(String userId);
    

    public abstract String getUserHandle();
    

    public abstract void setTweetId(long tweetId);
    public abstract void setUserHandle(String userHandle);
    public abstract String getTimestamp();

    public abstract void setTimestamp(String timestamp);
    

    public abstract void setProfileImageUrl(String profileImageUrl);
    
    public abstract String getBody();
    

    public abstract void setBody(String body);
    

    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

    public String getRelativeTime()
    {
        return getRelativeTimeAgo(getTimestamp());
    }
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }    
    
    
    // Add a constructor that creates an object from the JSON response
    public void ingestJson(JSONObject object){

//        Log.d("Tweet", "JSON object" + object.toString() );
        try {
            JSONObject userObj = object.getJSONObject("user");
            String tid = object.getString("id_str");
            this.setTweetId(Long.parseLong(tid));
            this.setUserId(userObj.getString("screen_name"));
            this.setUserHandle(userObj.getString("name"));
            this.setTimestamp(object.getString("created_at"));
            this.setBody(object.getString("text"));
            this.setProfileImageUrl(userObj.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray, Class<? extends Tweet> tweetClass) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = null;
            try {
                tweet = tweetClass.newInstance();
                //TODO: pass the json to the created tweet
                tweet.ingestJson(tweetJson);
               
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            //TODO: Kludge to avoiding saving in db,
            if (tweetClass != UserTweet.class) {
                tweet.save();
            }
            tweets.add(tweet);
        }

        return tweets;
    }
}
