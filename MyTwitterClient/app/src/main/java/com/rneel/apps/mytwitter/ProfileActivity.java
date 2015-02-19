package com.rneel.apps.mytwitter;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rneel.apps.mytwitter.models.User;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class ProfileActivity extends ActionBarActivity {

    private User user;
    private TextView tvUser;
    private TextView tvTag;
    private ImageView ivUser;
    private TextView tvFollower;
    private TextView tvFollowing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Get screen name from activity that launched this
        String screenName = getIntent().getStringExtra("screen_name");
        UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screenName);
        //Display the user fragment inside this activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flUserTimeLine, userTimeLineFragment);
        ft.commit();
        
        tvUser = (TextView)findViewById(R.id.tvUser);
        tvTag = (TextView)findViewById(R.id.tvTag);
        tvFollowing = (TextView)findViewById(R.id.tvFollowing);
        tvFollower = (TextView)findViewById(R.id.tvFollower);
        ivUser = (ImageView)findViewById(R.id.ivCurrentUserProfile);
        
        RestClient client = RestApplication.getRestClient();
        client.getProfileForUser(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJson(response);
                if (user != null) {
                    getSupportActionBar().setTitle("@" + user.screenName);
                    populateUser(user);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ProfileActivity.this, "Error getting user data", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void populateUser(User user)
    {
        tvUser.setText(user.screenName);
        tvTag.setText(user.tagLine);
        Picasso.with(this).load(user.url).into(ivUser);
        tvFollowing.setText("" + user.followingCount);
        tvFollower.setText("" + user.followerCount);
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
