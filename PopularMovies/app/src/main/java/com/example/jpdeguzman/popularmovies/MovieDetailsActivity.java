package com.example.jpdeguzman.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;

public class MovieDetailsActivity extends AppCompatActivity {

    TextView mMovieDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle data = getIntent().getExtras();
        MovieModel movie = data.getParcelable(".MovieModel");
        mMovieDetailsTextView = findViewById(R.id.tv_movie_details);
        if (movie != null) {
            mMovieDetailsTextView.setText(movie.toString());
        } else {
            Log.d("MovieDetailsActivity", "NOT WORKING ASKJDKLASDJKLA");
        }
    }
}
