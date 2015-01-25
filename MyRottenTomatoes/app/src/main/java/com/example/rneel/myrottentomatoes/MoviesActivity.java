package com.example.rneel.myrottentomatoes;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MoviesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        // 2. Get the list of movies
        ArrayList<Movie> movies = Movie.getFakeMovies();
        // 1. create an array adapter
//        ArrayAdapter<Movie> movieArrayAdapter =
//                new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1,movies);
        MovieAdapter movieArrayAdapter = new MovieAdapter(this, 0, movies);
        // 3. Get the list view
        ListView lvMovies = (ListView)findViewById(R.id.lvMovies);
        // 4. Bind the list view to the adapter
        lvMovies.setAdapter(movieArrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
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
}
