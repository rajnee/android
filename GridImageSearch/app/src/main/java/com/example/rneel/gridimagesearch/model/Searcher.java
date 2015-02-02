package com.example.rneel.gridimagesearch.model;

import android.text.Html;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rneel on 1/31/15.
 */
public class Searcher {
    
    String baseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    
    private String query;
    private SettingsModel model;

    public void setQuery(String query)
    {
        this.query = query;                
    }
    
    private String getFullQuery()
    {
        if (query == null)
        {
            return  null;
        }
        
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append("&").append("q=").append(Html.escapeHtml(query));
        
        if (model != null) {
            if (model.colorFilter != null && !model.colorFilter.equals("any")) {
                sb.append("&").append("imgcolor=").append(model.colorFilter);
            }

            if (model.imageSize != null  && !model.imageSize.equals("any")) {
                sb.append("&").append("imgsz=").append(model.imageSize);
            }

            if (model.imageType != null  && !model.imageType.equals("any")) {
                String t = model.imageType.replace(" ","");
                t = t.toLowerCase();
                sb.append("&").append("imgtype=").append(t);
            }

            if (model.siteFilter != null  && !model.siteFilter.trim().equals("")) {
                sb.append("&").append("as_sitesearch=").append(model.siteFilter);
            }
        }
        
        return sb.toString();        
    }

    public void getAndFillResults(final ImageResultsReceiver receiver) {
        AsyncHttpClient client = new AsyncHttpClient();
        String fullQuery = getFullQuery();
        Log.d("Searcher", "Executing:" + fullQuery);
        client.get(fullQuery, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("responseData");
                    JSONArray array = obj.getJSONArray("results");
                    List<ImageResult> imageResultList = new ArrayList<ImageResult>();
                    for (int i = 0; i < array.length(); i++) {

                        try {
                            JSONObject imageObj = array.getJSONObject(i);
                            String title = imageObj.getString("title");
                            String url = imageObj.getString("url");
                            String tbUrl = imageObj.getString("tbUrl");
                            ImageResult imageResult = new ImageResult();
                            imageResult.setTburl(tbUrl);
                            imageResult.setTitle(title);
                            imageResult.setUrl(url);
                            imageResultList.add(imageResult);
                            Log.d("Searcher", "Obtained image:" + imageResult);
                        } catch (JSONException e) {
                        }
                    }
                    if (imageResultList.size() == 0) {
                        receiver.setError();
                    } else {
                        receiver.setResult(imageResultList);
                    }

                } catch (JSONException e) {
                    receiver.setError();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        
    }

    public void setModel(SettingsModel model) {
        this.model = model;
    }
}
