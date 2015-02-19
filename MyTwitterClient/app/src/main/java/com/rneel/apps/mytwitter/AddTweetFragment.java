package com.rneel.apps.mytwitter;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTweetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTweetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTweetFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etTweet;
    private TextView tvCurrentUser;
    private ImageView ivCurrentUserProfileImage;
    
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTweetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTweetFragment newInstance() {
        AddTweetFragment fragment = new AddTweetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AddTweetFragment() {
        // Required empty public constructor
    }

    private void showCurrentUser()
    {
        RestClient client = RestApplication.getRestClient();
        client.getProfileForCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String user = response.getString("screen_name");
                    tvCurrentUser.setText("@" + user);
                    String url = response.getString("profile_image_url");
                    Picasso.with(getActivity()).load(url).into(ivCurrentUserProfileImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_tweet, container, false);
        Button btnCancel = (Button)v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTweetFragment.this.dismiss();
            }
        });
        
        Button btnTweet = (Button)v.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddTweetFragment.this.onTweetClick();
                    
                }
            } 
        );
        
        etTweet = (EditText)v.findViewById(R.id.etTweet);
        tvCurrentUser = (TextView)v.findViewById(R.id.tvUser);
        ivCurrentUserProfileImage = (ImageView)v.findViewById(R.id.ivCurrentUserProfile);
        showCurrentUser();
        return v;

    }

    public void onTweetClick()
    {
//        Toast.makeText(this, "Tweet has been clicked", Toast.LENGTH_SHORT).show();
        RestClient client = RestApplication.getRestClient();
        client.postTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getActivity(), "Tweet successfully saved", Toast.LENGTH_SHORT).show();
                AddTweetFragment.this.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "Error saving tweet", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onCancelClick(View view)
    {
        dismiss();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
