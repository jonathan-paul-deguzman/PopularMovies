package com.example.jpdeguzman.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private ImageView mMoviePosterImageView;

    private TextView mMovieTitleTextView;

    private TextView mMovieReleaseDateTextView;

    private TextView mMovieUserRatingTextView;

    private TextView mMovieOverviewTextView;

    private MovieModel mMovieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle data = getIntent().getExtras();
        mMovieDetails = data.getParcelable(".MovieModel");

        mMoviePosterImageView = findViewById(R.id.iv_movie_poster);
        mMovieTitleTextView = findViewById(R.id.tv_movie_title);
        mMovieReleaseDateTextView = findViewById(R.id.tv_movie_release_date);
        mMovieUserRatingTextView = findViewById(R.id.tv_movie_user_rating);
        mMovieOverviewTextView = findViewById(R.id.tv_movie_overview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpMoviePage();
    }

    private void setUpMoviePage() {
        String moviePosterPath = mMovieDetails.getMoviePosterPath();
        Picasso.with(this)
                .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                .into(mMoviePosterImageView);
        mMovieTitleTextView.setText(mMovieDetails.getMovieTitle());
        mMovieReleaseDateTextView.setText(mMovieDetails.getMovieReleaseDate());
        mMovieUserRatingTextView.setText(mMovieDetails.getMovieUserRating());
        mMovieOverviewTextView.setText(mMovieDetails.getMovieOverview());
    }
}
