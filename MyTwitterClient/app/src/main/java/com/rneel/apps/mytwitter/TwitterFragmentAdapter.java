package com.rneel.apps.mytwitter;

/**
 * Created by rneel on 2/17/15.
 */

import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class TwitterFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "Mentions" };

    private HomeLineFragment homeLineFragment;
    private MentionsFragment mentionsFragment;
    public TwitterFragmentAdapter(FragmentManager fm) {
        super(fm);
        homeLineFragment = new HomeLineFragment();
        mentionsFragment = new MentionsFragment();
        
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            return homeLineFragment;
        }
        else
        {
            return mentionsFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
