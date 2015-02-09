package com.rneel.apps.mytwitter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class AddTweetActivity extends ActionBarActivity {

    private EditText etTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tweet);
        etTweet = (EditText)findViewById(R.id.etTweet);
        etTweet.setBackground(getResources().getDrawable(android.R.drawable.editbox_background_normal));
    }

    private void tweet() {
        // SomeActivity.java
        RestClient client = RestApplication.getRestClient();
        client.getHomeTimeLineAfter(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // Response is automatically parsed into a JSONArray
                // json.getJSONObject(0).getLong("id");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_tweet, menu);
        return true;
    }

    public void onCancelClick(View view)
    {
        finish();        
    }
    
    public void onTweetClick(View view)
    {
//        Toast.makeText(this, "Tweet has been clicked", Toast.LENGTH_SHORT).show();
        RestClient client = RestApplication.getRestClient();
        client.postTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(AddTweetActivity.this,"Tweet successfully saved",Toast.LENGTH_SHORT).show();
                AddTweetActivity.this.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(AddTweetActivity.this,"Error saving tweet",Toast.LENGTH_SHORT).show();
            }
        });
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
