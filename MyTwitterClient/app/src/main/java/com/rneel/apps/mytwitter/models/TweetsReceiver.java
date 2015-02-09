package com.rneel.apps.mytwitter.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rneel on 2/8/15.
 */
public interface TweetsReceiver {
    
    public void tweetsReceived(List<Tweet> tweets);
    public void tweetError(String message);
}
