package com.rneel.apps.mytwitter;

import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;


public class MainActivity extends ActionBarActivity implements AddTweetFragment.OnFragmentInteractionListener {

    private PagerSlidingTabStrip tabs;
    private AddTweetFragment addTweetFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TwitterFragmentAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.action_compose) {
//            Toast.makeText(this,"clicked compose", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(this,AddTweetActivity.class);
//            startActivity(i);
//            addTweetFragment = AddTweetFragment.newInstance("x","y");
//            addTweetFragment.show(getSupportFragmentManager(), "test");
            showDialog();
            return true;
        }


        if (id == R.id.action_profile)
        {
//            Toast.makeText(this,"Profile clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,ProfileActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showDialog() {
//        mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);

        // Create and show the dialog.
        addTweetFragment = AddTweetFragment.newInstance();
        addTweetFragment.show(ft, "dialog");
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onCancelClick(View view)
    {
        addTweetFragment.dismiss();
    }

}
