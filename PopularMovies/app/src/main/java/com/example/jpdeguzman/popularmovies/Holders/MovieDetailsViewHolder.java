package com.example.jpdeguzman.popularmovies.Holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * View Holder for movie details
 */

public class MovieDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private ImageView moviePosterImageView;

    private TextView movieTitleTextView;

    private TextView movieReleaseDateTextView;

    private TextView movieUserRatingTextView;

    private TextView movieOverviewTextView;

    private Button markMovieAsFavoriteButton;

    private final Context context;

    public MovieDetailsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        movieTitleTextView = itemView.findViewById(R.id.tv_movie_title);
        movieReleaseDateTextView = itemView.findViewById(R.id.tv_movie_release_date);
        movieUserRatingTextView = itemView.findViewById(R.id.tv_movie_user_rating);
        movieOverviewTextView = itemView.findViewById(R.id.tv_movie_overview);
        markMovieAsFavoriteButton = itemView.findViewById(R.id.button_mark_as_favorite);
        markMovieAsFavoriteButton.setOnClickListener(this);
    }

    public void configureDetailsViewHolder(
            MovieDetailsViewHolder detailsHolder, ArrayList<MovieModel> detailItemList, int position) {
        MovieModel movieDetails = detailItemList.get(position);
        if (movieDetails != null) {
            String moviePosterPath = movieDetails.getMoviePosterPath();
            Picasso.with(context)
                    .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                    .into(moviePosterImageView);
            detailsHolder.movieTitleTextView.setText(movieDetails.getMovieTitle());
            detailsHolder.movieReleaseDateTextView.setText(
                    context.getString(R.string.details_release_date) + " " + movieDetails.getMovieReleaseDate());
            detailsHolder.movieUserRatingTextView.setText(
                    context.getString(R.string.details_user_rating) + " " + movieDetails.getMovieUserRating());
            detailsHolder.movieOverviewTextView.setText(movieDetails.getMovieOverview());
        }
    }

    @Override
    public void onClick(View v) {
        if (markMovieAsFavoriteButton.getText().equals(context.getString(R.string.details_is_not_favorite))) {
            markMovieAsFavoriteButton.setText(context.getString(R.string.details_is_favorite));
        } else {
            markMovieAsFavoriteButton.setText(context.getString(R.string.details_is_not_favorite));
        }
    }
}
