package com.rneel.apps.mytwitter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.rneel.apps.mytwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DisplayTweetsActivity extends ActionBarActivity {

    private ArrayList<Tweet> tweets;
    private TweetListAdapter tweetListAdapter;
    private ListView lvTweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tweets);
        tweets = new ArrayList<>();
        tweetListAdapter = new TweetListAdapter(this,tweets);
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetListAdapter);
        getTweetHomeLine();
    }

    private void getTweetHomeLine() {
        // SomeActivity.java
        RestClient client = RestApplication.getRestClient();
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // Response is automatically parsed into a JSONArray
                // json.getJSONObject(0).getLong("id");
                ArrayList<Tweet> t = Tweet.fromJson(json);
                tweets.addAll(t);
                tweetListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(DisplayTweetsActivity.this, "Error occurred getting tweets", Toast.LENGTH_SHORT).show();
                Log.d("DisplayTweetActivity","Error:" + responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(DisplayTweetsActivity.this, "Error occurred getting tweets", Toast.LENGTH_SHORT).show();
                HttpResponseException exception = (HttpResponseException)throwable;
                Log.d("DisplayTweetActivity", exception.getMessage());
                for (Header h:headers)
                {
                    Log.d("DisplayTweetActivity",h.getName() + ":" + h.getValue());
                }
                Log.d("DisplayTweetActivity","Error:" + errorResponse, throwable);
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
