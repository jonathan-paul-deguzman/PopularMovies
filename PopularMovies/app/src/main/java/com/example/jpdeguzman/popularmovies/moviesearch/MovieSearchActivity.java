package com.example.jpdeguzman.popularmovies.moviesearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.jpdeguzman.popularmovies.R;

/**
 * Responsible for creating the fragment and managing the activity lifecycle of the view
 */
public class MovieSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);
    }
}
