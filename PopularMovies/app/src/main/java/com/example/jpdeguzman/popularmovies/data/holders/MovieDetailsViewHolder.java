package com.example.jpdeguzman.popularmovies.data.holders;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Constants.Images;
import com.example.jpdeguzman.popularmovies.data.database.FavoriteMovieContract;
import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View Holder for movie details
 */
public class MovieDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_movie_backdrop) ImageView movieBackdropImageView;

    @BindView(R.id.iv_movie_poster) ImageView moviePosterImageView;

    @BindView(R.id.tv_movie_title) TextView movieTitleTextView;

    @BindView(R.id.tv_movie_release_date) TextView movieReleaseDateTextView;

    @BindView(R.id.tv_movie_user_rating) TextView movieUserRatingTextView;

    @BindView(R.id.tv_movie_overview) TextView movieOverviewTextView;

    @BindView(R.id.button_mark_as_favorite) Button markMovieAsFavoriteButton;

    private final Context context;

    private MovieModel currentMovieDetails;

    public MovieDetailsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
        markMovieAsFavoriteButton.setOnClickListener(this);
    }

    public void configureDetailsViewHolder(
            MovieDetailsViewHolder detailsHolder, ArrayList<MovieModel> detailItemList, int position) {
        currentMovieDetails = detailItemList.get(position);
        if (currentMovieDetails != null) {
            String movieBackdropPath = currentMovieDetails.getMovieBackdropPath();
            Picasso.with(context)
                    .load(Images.IMAGE_BASE_URL + Images.IMAGE_RECOMMENDED_SIZE + movieBackdropPath)
                    .into(movieBackdropImageView);
            String moviePosterPath = currentMovieDetails.getMoviePosterPath();
            Picasso.with(context)
                    .load(Images.IMAGE_BASE_URL + Images.IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                    .into(moviePosterImageView);
            detailsHolder.movieTitleTextView.setText(currentMovieDetails.getMovieTitle());
            detailsHolder.movieReleaseDateTextView.setText(
                    context.getString(R.string.details_release_date) + " " + currentMovieDetails.getMovieReleaseDate());
            detailsHolder.movieUserRatingTextView.setText(
                    context.getString(R.string.details_user_rating) + " " + currentMovieDetails.getMovieUserRating());
            detailsHolder.movieOverviewTextView.setText(currentMovieDetails.getMovieOverview());

            if (currentMovieDetails.getFavorite()) {
                markMovieAsFavoriteButton.setText(context.getString(R.string.details_remove_favorite));
            } else {
                markMovieAsFavoriteButton.setText(context.getString(R.string.details_make_favorite));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (markMovieAsFavoriteButton.getText().equals(context.getString(R.string.details_make_favorite))) {
            insertFavoriteMovie(currentMovieDetails);
            markMovieAsFavoriteButton.setText(context.getString(R.string.details_remove_favorite));
        } else {
            removeFavoriteMovie(currentMovieDetails);
            markMovieAsFavoriteButton.setText(context.getString(R.string.details_make_favorite));
        }
    }

    private void insertFavoriteMovie(MovieModel favoriteMovie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                favoriteMovie.getMovieId());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH,
                favoriteMovie.getMoviePosterPath());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                favoriteMovie.getMovieBackdropPath());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE,
                favoriteMovie.getMovieTitle());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING,
                favoriteMovie.getMovieUserRating());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                favoriteMovie.getMovieReleaseDate());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW,
                favoriteMovie.getMovieOverview());
        context.getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                contentValues);
    }

    private void removeFavoriteMovie(MovieModel favoriteMovie) {
        Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(favoriteMovie.getMovieId())).build();
        context.getContentResolver().delete(uri, null, null);
    }
}
