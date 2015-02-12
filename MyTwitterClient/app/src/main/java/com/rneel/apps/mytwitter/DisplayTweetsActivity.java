package com.rneel.apps.mytwitter;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.rneel.apps.mytwitter.models.Tweet;
import com.rneel.apps.mytwitter.models.TweetManager;
import com.rneel.apps.mytwitter.models.TweetsReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DisplayTweetsActivity extends ActionBarActivity {

    private ArrayList<Tweet> tweets;
    private TweetListAdapter tweetListAdapter;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tweets);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.ic_twitter);
//        getSupportActionBar().setIcon(R.drawable.ic_twitter);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        tweets = new ArrayList<>();
        tweetListAdapter = new TweetListAdapter(this,tweets);
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetListAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        TweetManager.init(this);
        refresh();

        lvTweets.setOnScrollListener(new TweetViewOnScrollListener() {
            @Override
            public void onLoadMore(int firstVisibleItem, int page, int totalItemsCount, int visibleItemCount) {
                Log.d("DisplayTweetsActivity", "OnScroll: first=" + firstVisibleItem + ",page=" + page + ",total=" + totalItemsCount);
                loadMore(firstVisibleItem, page, totalItemsCount, visibleItemCount);
            }
        });

    }
    

    private TweetsReceiver getTweetsReceiver()
    {
        return new TweetsReceiver() {
            @Override
            public void tweetsReceived(List<Tweet> tweets) {
                DisplayTweetsActivity.this.tweets.addAll(tweets);
                DisplayTweetsActivity.this.tweetListAdapter.notifyDataSetChanged();
                DisplayTweetsActivity.this.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void tweetError(String message) {
                Toast.makeText(DisplayTweetsActivity.this,message,Toast.LENGTH_SHORT);
                DisplayTweetsActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        };
        
    }

    private void loadMore(int firstVisibleItem, int page, int totalItemsCount, int visibleItemCount)
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
            tweetManager.loadMore(neededMore, beforeTweetId, getTweetsReceiver());
        }
    }
    
    private void refresh() {
        // SomeActivity.java
        TweetManager tweetManager = TweetManager.getInstance();
        tweetManager.refresh(getTweetsReceiver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_tweets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
//            Toast.makeText(this,"clicked compose", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,AddTweetActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
