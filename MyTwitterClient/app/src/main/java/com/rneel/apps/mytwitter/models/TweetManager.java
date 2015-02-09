package com.rneel.apps.mytwitter.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rneel.apps.mytwitter.RestApplication;
import com.rneel.apps.mytwitter.RestClient;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rneel on 2/8/15.
 */
public class TweetManager {
    
    private static  TweetManager tweetManager;
    private Context context;
    private MyTwitterDB myTwitterDB;
    private SQLiteDatabase db;
    
    public static void init(Context context)
    {
        if (tweetManager == null)
        {
            tweetManager = new TweetManager(context);            
        }
    }
    
    private TweetManager(Context context)
    {
        this.context = context;
        myTwitterDB = new MyTwitterDB();
        db = myTwitterDB.getReadableDatabase();
    }
    
    public static TweetManager getInstance()
    {
        return tweetManager;
    }
    
    private  class MyTwitterDB extends SQLiteOpenHelper
    {
        public MyTwitterDB()
        {
            super(TweetManager.this.context, "MyTwitter.db", null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) { }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }
    
    public long selectMaxId()
    {
        long maxId = -1;
        Cursor cursor = db.rawQuery("select max(tweetId) from Tweets", new String[0]);
        if (cursor.moveToFirst())
        {
            maxId = cursor.getLong(0);
            if (maxId == 0)
            {
                maxId = -1;
            }
        }
        return maxId;
    }
    
    public void loadMore(int howMany, long beforeTweetId, final TweetsReceiver tweetsReceiver)
    {
        //Get from database first
        final List<Tweet> dbTweets = new Select().from(Tweet.class).where("tweetId < " + beforeTweetId).limit(howMany).execute();
        if (dbTweets.size() == howMany)
        {
            tweetsReceiver.tweetsReceived(dbTweets);
            return;    
        }

        long lastTweetInDB = dbTweets.get(dbTweets.size()-1).getTweetId();
        //We do not have enough tweets in the db, let us load more
        RestClient client = RestApplication.getRestClient();
        client.getHomeTimelineBefore(lastTweetInDB, new TweetResponseHandler() {
            @Override
            protected void tweetsReceived(List<Tweet> t1) {
                dbTweets.addAll(t1);
                tweetsReceiver.tweetsReceived(dbTweets);
            }

            @Override
            protected void tweetsError(String message) {
                tweetsReceiver.tweetError(message);
            }
        });
        
    }
    public void refresh(final TweetsReceiver tweetsReceiver)
    {
        RestClient client = RestApplication.getRestClient();
        client.getHomeTimeLineAfter(selectMaxId(), new TweetResponseHandler() {
            @Override
            protected void tweetsReceived(List<Tweet> t1) {
                tweetsReceiver.tweetsReceived(t1);
            }

            @Override
            protected void tweetsError(String message) {
                tweetsReceiver.tweetError(message);
            }
        });
    }
    
    public static abstract class TweetResponseHandler extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
            // Response is automatically parsed into a JSONArray
            // json.getJSONObject(0).getLong("id");
            ArrayList<Tweet> t = Tweet.fromJson(json);
            List <Tweet> t1 = new Select().from(Tweet.class).orderBy("tweetId").limit(20).execute();
            tweetsReceived(t1);
        }

        protected abstract void tweetsReceived(List<Tweet> t1);
        protected abstract void tweetsError(String message);
            
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("DisplayTweetActivity", "Error:" + responseString, throwable);
            tweetsError(throwable.getMessage());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            HttpResponseException exception = (HttpResponseException)throwable;
//                Log.d("DisplayTweetActivity", exception.getMessage());
//                for (Header h:headers)
//                {
//                    Log.d("DisplayTweetActivity",h.getName() + ":" + h.getValue());
//                }
//                Log.d("DisplayTweetActivity","Error:" + errorResponse, throwable);
            tweetsError(throwable.getMessage());
        }        
        
    }
}
