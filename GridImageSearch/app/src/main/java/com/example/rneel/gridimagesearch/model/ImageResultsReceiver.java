package com.example.rneel.gridimagesearch.model;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by rneel on 1/31/15.
 */
public interface ImageResultsReceiver {
    void setResult(List<ImageResult> imageResultList);
    void setError();
}
