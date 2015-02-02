package com.example.rneel.gridimagesearch.model;

/**
 * Created by rneel on 1/31/15.
 */
public class ImageResult {
    private String url;
    private String tburl;
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTburl() {
        return tburl;
    }

    public void setTburl(String tburl) {
        this.tburl = tburl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String toString() {
        return "title:[" + title + "], url = [" + url + "]";
        
    }
}
