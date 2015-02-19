package com.rneel.apps.mytwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.rneel.apps.mytwitter.models.Tweet;
import com.rneel.apps.mytwitter.models.TweetManager;
import com.rneel.apps.mytwitter.models.TweetsReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rneel on 2/17/15.
 */
public abstract class BaseFragment extends Fragment {

    protected ArrayList<Tweet> tweets;
    protected TweetListAdapter tweetListAdapter;
    protected ListView lvTweets;
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.fragment_home_line,container,false);
        tweets = new ArrayList<>();
        tweetListAdapter = new TweetListAdapter(getActivity(),tweets);
        lvTweets = (ListView)v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetListAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        TweetManager.init(getActivity());
        refresh();

        lvTweets.setOnScrollListener(new TweetViewOnScrollListener() {
            @Override
            public void onLoadMore(int firstVisibleItem, int page, int totalItemsCount, int visibleItemCount) {
                Log.d("DisplayTweetsActivity", "OnScroll: first=" + firstVisibleItem + ",page=" + page + ",total=" + totalItemsCount);
                loadMore(firstVisibleItem, page, totalItemsCount, visibleItemCount);
            }
        });

        Log.d("BaseFragment", "OnCreateView for:" + this.getClass().getName());
        return v;
    }

    protected TweetsReceiver getTweetsReceiver()
    {
        return new TweetsReceiver() {
            @Override
            public void tweetsReceived(List<Tweet> tweets) {
                BaseFragment.this.tweets.addAll(tweets);
                BaseFragment.this.tweetListAdapter.notifyDataSetChanged();
                BaseFragment.this.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void tweetError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                BaseFragment.this.swipeRefreshLayout.setRefreshing(false);
            }
        };

    }

    protected abstract int getTimeLine();

    protected void loadMore(int firstVisibleItem, int page, int totalItemsCount, int visibleItemCount)
    {
        int count = tweetListAdapter.getCount();
        Tweet t2 = tweetListAdapter.getItem(count - 1);
        //We want to have a page more than mentioned page
        //page n has items, needed till : firstVisibleItem + (page+1)*visibleItemCount
        int requiredTill = firstVisibleItem + (page+1)*visibleItemCount;
        int neededMore = requiredTill - totalItemsCount;
        long beforeTweetId = t2.getTweetId();

        if (requiredTill > 0)
        {
            TweetManager tweetManager = TweetManager.getInstance();
            tweetManager.loadMore(neededMore, beforeTweetId, getTweetsReceiver(),getTimeLine());
        }
    }

    protected void refresh() {
        // SomeActivity.java
        TweetManager tweetManager = TweetManager.getInstance();
        tweetManager.refresh(getTweetsReceiver(),getTimeLine());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
