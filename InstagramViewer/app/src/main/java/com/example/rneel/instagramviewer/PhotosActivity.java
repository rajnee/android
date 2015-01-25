package com.example.rneel.instagramviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends Activity {

    private ArrayList<InstagramPhoto> photos = new ArrayList<InstagramPhoto>();
    private InstagramPhotosAdapter adapter;
    private static final String LOGTAG = "PhotoActivitiy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        adapter = new InstagramPhotosAdapter(this,photos);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        getPopularPhotos();

    }

    public void onImageClick(View view) {
        
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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


    private JSONObject getJSONObjectAndSwallowException(JSONObject jsonObject, String key) {
        JSONObject ret = null;
        if (jsonObject != null) {
            try {
                ret = jsonObject.getJSONObject(key);
            } catch (JSONException e) {
                //no json object was found, so got this exception
            }
        }
        return ret;
    }
    public void getPopularPhotos() {

        String url = "https://api.instagram.com/v1/media/popular?client_id=0791c0f9218c4c86a0a094ec08d93bd4";
        AsyncHttpClient client = new AsyncHttpClient();
        photos.clear();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray dataArray = null;
                try {
                    dataArray = response.getJSONArray("data");
                } catch (JSONException e) {
                }
                for (int i = 0; dataArray != null && i < dataArray.length(); i++)
                    {
                        try {
                            InstagramPhoto instagramPhoto = new InstagramPhoto();
                            JSONObject photo = dataArray.getJSONObject(i);
                            if (photo != null) {
                                JSONObject caption = getJSONObjectAndSwallowException (photo, "caption");
                                if (caption != null) {
                                    String title = caption.getString("text");
                                    instagramPhoto.title = title;
                                }
                                JSONObject likes = getJSONObjectAndSwallowException (photo, "likes");
                                if (likes != null) {
                                    String count = likes.getString("count");
                                    instagramPhoto.likesCount = count;
                                }
                                JSONObject user = getJSONObjectAndSwallowException (photo, "user");
                                if (user != null) {
                                    String userName = user.getString("username");
                                    instagramPhoto.userName = userName;
                                    String profilePicture = user.getString("profile_picture");
                                    instagramPhoto.profilePicture = profilePicture;
                                }

                                JSONObject images = getJSONObjectAndSwallowException(photo,"images");
                                if (images != null) {
                                    JSONObject standardRes = getJSONObjectAndSwallowException(images, "standard_resolution");
                                    String url = standardRes.getString("url");
                                    Log.d(LOGTAG, "image url:" + instagramPhoto.profilePicture);
                                    int height = standardRes.getInt("height");
                                    instagramPhoto.imageUrl = url;
                                    instagramPhoto.imageHeight = height;

                                }

                                photos.add(instagramPhoto);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                adapter.notifyDataSetChanged();
            }
        });
    }

}
