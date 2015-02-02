package com.example.rneel.gridimagesearch;

import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import com.example.rneel.gridimagesearch.model.ImageResult;
import com.example.rneel.gridimagesearch.model.ImageResultsReceiver;
import com.example.rneel.gridimagesearch.model.Searcher;

import java.util.List;

/**
 * Created by rneel on 1/31/15.
 */
public class SearcherTest {

    @MediumTest
    public void testSearch()
    {
        Searcher searcher = new Searcher();
        searcher.setQuery("android");
        searcher.getAndFillResults(new ImageResultsReceiver() {
            @Override
            public void setResult(List<ImageResult> imageResultList) {
                Log.i("TEST","Received results:" + imageResultList.size());
            }

            @Override
            public void setError() {
                Log.i("TEST", "Recieved error");
            }
        });
        
    }
}
