package com.example.rneel.myrottentomatoes;

import java.util.ArrayList;

/**
 * Created by rneel on 1/20/15.
 */
public class Movie {
    private String title;
    private String posterUrl;
    private int score;

    public Movie(String title, String posterUrl, int score) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.score = score;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public static ArrayList<Movie> getFakeMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("MI 1","", 90));
        movies.add(new Movie("MI 2","", 90));
        movies.add(new Movie("MI 3","", 70));
        movies.add(new Movie("To kill a mocking bird","", 80));

        return movies;
    }

    public String toString() {
        return title + "-" + score;
    }

}
