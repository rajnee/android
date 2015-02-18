package com.example.rneel.fragmentswithviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OneFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View v  = inflater.inflate(R.layout.fragment_one,container,false);
        TextView t = (TextView)v.findViewById(R.id.textView);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    public void onButton(View view)
    {
        assert getActivity() != null;
        Intent i = new Intent(getActivity(),FragmentActivity.class);
        startActivity(i);
        
    }
}
