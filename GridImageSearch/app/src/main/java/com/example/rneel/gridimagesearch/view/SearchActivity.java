package com.example.rneel.gridimagesearch.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.rneel.gridimagesearch.PictureDetailActivity;
import com.example.rneel.gridimagesearch.R;
import com.example.rneel.gridimagesearch.SettingsActivity;
import com.example.rneel.gridimagesearch.model.ImageResult;
import com.example.rneel.gridimagesearch.model.ImageResultsReceiver;
import com.example.rneel.gridimagesearch.model.Searcher;
import com.example.rneel.gridimagesearch.model.SettingsModel;
import com.example.rneel.gridimagesearch.model.SettingsUtil;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends ActionBarActivity {

    private EditText etSearch;
    private GridView gvSearchImages;
    private ImageResultsAdapter imageResultsAdapter;
    private List<ImageResult> imageResultList;
    private SettingsModel settingsModel = new SettingsModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setElements();
        SettingsUtil.registerResources(getResources());
        imageResultList = new ArrayList<ImageResult>();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResultList);
        gvSearchImages.setAdapter(imageResultsAdapter);
        gvSearchImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, PictureDetailActivity.class);
                intent.putExtra("url", imageResultsAdapter.getItem(i).getUrl());
                startActivity(intent);

            }
        });
    }

    public void onSearchForImage(View view){
        refreshSearch();
        
    }

    private void refreshSearch() {
        Searcher searcher = new Searcher();
        String query = etSearch.getText().toString();
        searcher.setQuery(query);
        searcher.setModel(settingsModel);
        searcher.getAndFillResults(new ImageResultsReceiver() {
            @Override
            public void setResult(List<ImageResult> imageResultList) {
                imageResultsAdapter.clear();
                imageResultsAdapter.addAll(imageResultList);
            }

            @Override
            public void setError() {
                Toast.makeText(SearchActivity.this, "Error retrieving images for search", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setElements()
    {
        etSearch = (EditText)findViewById(R.id.etSearch);
        gvSearchImages = (GridView)findViewById(R.id.gvSearchImages);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miSettings) {

            Intent intent = new Intent(this,SettingsActivity.class);
            intent.putExtra("settings",settingsModel);
            startActivityForResult(intent, REQUEST_SEARCH_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static final int REQUEST_SEARCH_SETTINGS = 199;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Handle form data
        if (requestCode == REQUEST_SEARCH_SETTINGS) {
            if (resultCode == RESULT_OK) {
                SettingsModel settings = (SettingsModel)data.getSerializableExtra("settings");
                //Toast YES ot NO based on age
//                String toastMessage = "NO";
//                if (settings.age >= 21) {
//                    toastMessage = "YES";
//                }
//                Toast.makeText(this,toastMessage,Toast.LENGTH_SHORT).show();
                settingsModel = settings;                
            }
        }

    }

}
