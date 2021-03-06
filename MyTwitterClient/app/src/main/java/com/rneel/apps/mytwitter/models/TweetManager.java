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
    
    public static final int HOME_TIMELINE = 1;
    public static final int MENTIONS_TIMELINE = 2;

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
    
    private static class SelectMaxIdTask extends AsyncTask<Void, Void, Long>
    {
        
        private int timeline;
        private SQLiteDatabase db;
        private Runnable task;
        private long maxTweetId;
        public SelectMaxIdTask(int timeline, SQLiteDatabase db, Runnable task)
        {
            this.timeline = timeline;
            this.db = db;
            this.task = task;
        }

        public long getMaxTweetId()
        {
            return maxTweetId;
            
        }
        
        public long selectMaxId()
        {
            long maxId = -1;
            Cursor cursor;
            if (timeline == HOME_TIMELINE) {
                cursor = db.rawQuery("select max(tweetId) from Tweets", new String[0]);
            }
            else
            {
                cursor = db.rawQuery("select max(tweetId) from MentionsTweets", new String[0]);
            }
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

        @Override
        protected Long doInBackground(Void... params) {
            maxTweetId = selectMaxId();
            return maxTweetId;
        }

        @Override
        protected void onPostExecute(Long maxTweetId) {
            task.run();
        }
    }
    
    private class LoadTweetTask extends AsyncTask<Void, Void, List<Tweet>>
    {
        private final TweetsReceiver tweetsReceiver;
        private final long beforeTweetId;
        private final int howMany;
        private final int timeline;
        private boolean loadIfNotEnough;
        public LoadTweetTask(int howMany, long beforeTweetId, TweetsReceiver tweetsReceiver, int timeline, boolean loadIfNotEnough)
        {
            this.tweetsReceiver = tweetsReceiver;
            this.beforeTweetId = beforeTweetId;
            this.howMany = howMany;
            this.timeline = timeline;
            this.loadIfNotEnough = loadIfNotEnough;
        }
        
        public void setLoadIfNotEnough(boolean loadIfNotEnough)
        {
            this.loadIfNotEnough = loadIfNotEnough;
            
        }
        @Override
        protected List<Tweet> doInBackground(Void... params) {
            final List<Tweet> dbTweets;
            if (timeline == HOME_TIMELINE) {
                dbTweets = new Select().from(HomeTweet.class).where("tweetId < " + beforeTweetId).limit(howMany).execute();
            }
            else
            {
                dbTweets = new Select().from(MentionsTweet.class).where("tweetId < " + beforeTweetId).limit(howMany).execute();
            }
            return dbTweets;
        }

        @Override
        protected void onPostExecute(final List<Tweet> dbTweets) {
            if (!loadIfNotEnough || dbTweets.size() == howMany)
            {
                tweetsReceiver.tweetsReceived(dbTweets);
                return;
            }
            else 
            {

                //we have to attempt to get from Twitter by calling API
                long lastTweetInDB = Long.MAX_VALUE;
                if (dbTweets.size() > 0) {
                 lastTweetInDB = dbTweets.get(dbTweets.size() - 1).getTweetId();
                }
                //We do not have enough tweets in the db, let us load more
                RestClient client = RestApplication.getRestClient();
                TweetResponseHandler responseHandler = new TweetResponseHandler() {
                    @Override
                    protected void tweetsReceived(List<Tweet> t1) {
                        dbTweets.addAll(t1);
                        tweetsReceiver.tweetsReceived(dbTweets);
                    }

                    @Override
                    protected void tweetsError(String message) {
                        tweetsReceiver.tweetError(message);
                    }

                    @Override
                    protected int getTimeLine() {
                        return timeline;
                    }
                };
                if (timeline == HOME_TIMELINE) {
                    client.getHomeTimelineBefore(lastTweetInDB, responseHandler);
                }
                else {
                    client.getMentionsTimelineBefore(lastTweetInDB, responseHandler);
                }

            }
        }
        
    }


    public void loadMore(int howMany, long beforeTweetId, final TweetsReceiver tweetsReceiver, int timeline) 
    {
        new LoadTweetTask(howMany,beforeTweetId,tweetsReceiver, timeline, true).execute();
    }
    
    public void refresh(final TweetsReceiver tweetsReceiver, final int timeline)
    {
        final RestClient client = RestApplication.getRestClient();
        final TweetResponseHandler responseHandler = new TweetResponseHandler() {
            @Override
            protected void tweetsReceived(List<Tweet> t1) {
                //Tweets are loaded from twitter after a http call
                //tweets would have been saved to db, get from db and show
//                loadMore(15, Long.MAX_VALUE,tweetsReceiver,timeline);
//                tweetsReceiver.tweetsReceived(t1);
                new LoadTweetTask(15,Long.MAX_VALUE,tweetsReceiver, timeline, false).execute();
            }

            @Override
            protected void tweetsError(String message) {
                tweetsReceiver.tweetError(message);
            }

            @Override
            protected int getTimeLine() {
                return timeline;
            }
        };
        final SelectMaxIdTask task = new SelectMaxIdTask(timeline,db, null);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (timeline == HOME_TIMELINE) {
                    client.getHomeTimeLineAfter(task.getMaxTweetId(), responseHandler);
                }
                else {
                    client.getMentionsTimeLineAfter(task.getMaxTweetId(), responseHandler);
                }
            }
        };
        task.task = r;
        task.execute();
    }
    
    public static abstract class TweetResponseHandler extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
            // Response is automatically parsed into a JSONArray
            // json.getJSONObject(0).getLong("id");
            
            ArrayList<Tweet> t = null;
            List <Tweet> t1 = null;
            if (getTimeLine() == HOME_TIMELINE)
            {
                t = HomeTweet.fromJson(json, HomeTweet.class);
                t1 = new Select().from(HomeTweet.class).orderBy("tweetId").limit(20).execute();

            } 
            else
            {
                t = MentionsTweet.fromJson(json, MentionsTweet.class);
                t1 = new Select().from(MentionsTweet.class).orderBy("tweetId").limit(20).execute();
            }
            tweetsReceived(t1);
        }

        protected abstract void tweetsReceived(List<Tweet> t1);
        protected abstract void tweetsError(String message);
        protected abstract int getTimeLine();
            
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.e("DisplayTweetActivity", "Error:" + responseString, throwable);
            tweetsError(throwable.getMessage());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            tweetsError(throwable.getMessage());
        }
        
        private void logException(Header[] headers, JSONObject errorResponse, Throwable throwable)
        {
            HttpResponseException exception = (HttpResponseException)throwable;
            Log.e("DisplayTweetActivity", exception.getMessage());
            for (Header h:headers)
            {
                Log.e("DisplayTweetActivity",h.getName() + ":" + h.getValue());
            }
            Log.e("DisplayTweetActivity","Error:" + errorResponse, throwable);
        }
        
    }
}
