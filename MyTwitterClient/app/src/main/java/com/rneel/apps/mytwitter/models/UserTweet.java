package com.rneel.apps.mytwitter.models;

/**
 * Created by rneel on 2/6/15.
 */
// models/Tweet.java

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "UserTweets")
public  class UserTweet extends Tweet {
    // Define database columns and associated fields
    @Column(name = "userId")
    public String userId;
    @Column(name = "userHandle")
    public String userHandle;
    @Column(name = "timestamp", index = true)
    public String timestamp;
    @Column(name = "body")
    public String body;

    @Column(name="profileImage")
    public String profileImage;

    @Column(name="tweetId", index = true)
    public long tweetId;


    public long getTweetId() {
        return tweetId;
    }

    public String getProfileImage() { return profileImage; }
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
    public UserTweet() {
        super();
    }

    @Override
    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    @Override
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImage = profileImageUrl;
    }
}
