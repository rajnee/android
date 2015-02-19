package com.rneel.apps.mytwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "yqvhGvkOAS4Gt4CGFs2qfGQpO";
    public static final String REST_CONSUMER_SECRET = "Uu2B85urUkeQYJHMJQF2rv2DzVzcZYx8if4LY6vmCcGpvYv2M6";
	public static final String REST_CALLBACK_URL = "oauth://rajtweets";

    private static long lastTimeOfRequest;
	public RestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getProfileForCurrentUser(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        Log.d("RestClient", "Url:" + apiUrl);
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, params, handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
    // RestClient.java
    public void getHomeTimeLineAfter(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        getTimeLineAfter(tweetId,handler,apiUrl);
    }

    public void getMentionsTimeLineAfter(long tweetId, AsyncHttpResponseHandler handler) {
        if (System.currentTimeMillis() - lastTimeOfRequest < 5000)
        {
            return;
        }
        lastTimeOfRequest = System.currentTimeMillis();
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
//        getTimeLineAfter(tweetId,handler,apiUrl);
    }

    private void getTimeLineAfter(long tweetId, AsyncHttpResponseHandler handler, String apiUrl)
    {
        Log.d("RestClient", "Url:" + apiUrl);
        RequestParams params = new RequestParams();
        if (tweetId != -1) {
            params.put("since_id", tweetId);
        }
        params.put("count", 200);
        getClient().get(apiUrl, params, handler);
    }

    public void getHomeTimelineBefore(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        getTimelineBefore(tweetId, handler, apiUrl);
    }

    public void getMentionsTimelineBefore(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        getTimelineBefore(tweetId, handler, apiUrl);
    }

    private void getTimelineBefore(long tweetId, AsyncHttpResponseHandler handler, String apiUrl) {
        Log.d("RestClient", "Url:" + apiUrl);
        RequestParams params = new RequestParams();
        if (tweetId != -1) {
            params.put("max_id", tweetId);
        }
        params.put("count", 200);
        getClient().get(apiUrl, params, handler);
    }

    // RestClient.java
    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        getClient().post(apiUrl, params, handler);
    }
}
