package com.example.jpdeguzman.popularmovies.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;

import java.util.ArrayList;

/**
 * View Holder for movie details
 */

public class MovieDetailsViewHolder extends RecyclerView.ViewHolder {

    private ImageView moviePosterImageView;

    private TextView movieTitleTextView;

    private TextView movieReleaseDateTextView;

    private TextView movieUserRatingTextView;

    private TextView movieOverviewTextView;

    public MovieDetailsViewHolder(View itemView) {
        super(itemView);
        moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        movieTitleTextView = itemView.findViewById(R.id.tv_movie_title);
        movieReleaseDateTextView = itemView.findViewById(R.id.tv_movie_release_date);
        movieUserRatingTextView = itemView.findViewById(R.id.tv_movie_user_rating);
        movieOverviewTextView = itemView.findViewById(R.id.tv_movie_overview);
    }

    public ImageView getMoviePosterImageView() {
        return moviePosterImageView;
    }

    public void setMoviePosterImageView(ImageView mMoviePosterImageView) {
        this.moviePosterImageView = mMoviePosterImageView;
    }

    public TextView getMovieTitleTextView() {
        return movieTitleTextView;
    }

    public void setMovieTitleTextView(TextView mMovieTitleTextView) {
        this.movieTitleTextView = mMovieTitleTextView;
    }

    public TextView getMovieReleaseDateTextView() {
        return movieReleaseDateTextView;
    }

    public void setMovieReleaseDateTextView(TextView mMovieReleaseDateTextView) {
        this.movieReleaseDateTextView = mMovieReleaseDateTextView;
    }

    public TextView getMovieUserRatingTextView() {
        return movieUserRatingTextView;
    }

    public void setmovieUserRatingTextView(TextView mMovieUserRatingTextView) {
        this.movieUserRatingTextView = mMovieUserRatingTextView;
    }

    public TextView getMovieOverviewTextView() {
        return movieOverviewTextView;
    }

    public void setMovieOverviewTextView(TextView mMovieOverviewTextView) {
        this.movieOverviewTextView = mMovieOverviewTextView;
    }

    public static void configureMovieInformationViewHolder(
            MovieDetailsViewHolder holder, ArrayList<MovieModel> movieModelArrayList) {
    }
}
