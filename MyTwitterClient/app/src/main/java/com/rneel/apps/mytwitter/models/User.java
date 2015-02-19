package com.rneel.apps.mytwitter.models;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rneel on 2/18/15.
 */
public class User {
    public String screenName;
    public String url;
    public String name;
    public int followingCount;
    public int followerCount;
    public String tagLine;
    
    public static User fromJson(JSONObject response)
    {
        try {
            String screenName = response.getString("screen_name");
            String url = response.getString("profile_image_url");
            User user = new User();
            user.screenName = screenName;
            user.url = url;
            user.name = response.getString("name");
            user.followerCount = response.getInt("followers_count");
            user.followingCount = response.getInt("friends_count");
            user.tagLine = response.getString("description");
            
            return user;
            
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
