package com.rneel.apps.mytwitter;

import com.rneel.apps.mytwitter.models.TweetManager;

public class HomeLineFragment extends BaseFragment
{

    @Override
    protected int getTimeLine() {
        return TweetManager.HOME_TIMELINE;
    }
}
