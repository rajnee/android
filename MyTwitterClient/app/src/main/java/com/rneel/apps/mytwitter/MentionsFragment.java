package com.rneel.apps.mytwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rneel.apps.mytwitter.models.TweetManager;

public class MentionsFragment extends BaseFragment
{

    @Override
    protected int getTimeLine() {
        return TweetManager.MENTIONS_TIMELINE;
    }

}
