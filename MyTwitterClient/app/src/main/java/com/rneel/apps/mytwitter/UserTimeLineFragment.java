package com.rneel.apps.mytwitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rneel.apps.mytwitter.models.Tweet;
import com.rneel.apps.mytwitter.models.UserTweet;

import org.apache.http.Header;
import org.json.JSONArray;

/**
 * Created by rneel on 2/18/15.
 */
public class UserTimeLineFragment extends BaseFragment{
    private RestClient client;
    @Override
    protected int getTimeLine() {
        return 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadMore(int firstVisibleItem, int page, int totalItemsCount, int visibleItemCount) {
        long maxTweetId = -1;
        if (tweets.size() > 0) {
            Tweet t = tweets.get(tweets.size()-1);
            maxTweetId = t.getTweetId();
        }
        client.getUserTimeLineBefore( getArguments().getString("screen_name"), maxTweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweets.addAll(Tweet.fromJson(response, UserTweet.class));
                tweetListAdapter.notifyDataSetChanged();
            }
        });
    }

    public static UserTimeLineFragment newInstance(String screenName) {
        UserTimeLineFragment fragment = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void refresh() {
        
    }

    private  void populateTimeLine()
    {
        client.getUserTimeLine( getArguments().getString("screen_name"), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweets.addAll(Tweet.fromJson(response, UserTweet.class));
                tweetListAdapter.notifyDataSetChanged();
            }
        });
        
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RestApplication.getRestClient();
        populateTimeLine();
    }
}
