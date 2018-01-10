package com.example.jpdeguzman.popularmovies.moviesearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jpdeguzman.popularmovies.R;

/**
 * Created by jpdeguzman on 1/5/18.
 */
public class MovieSearchActivity extends AppCompatActivity {

    private static final String TAG = MovieSearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);
    }
}
