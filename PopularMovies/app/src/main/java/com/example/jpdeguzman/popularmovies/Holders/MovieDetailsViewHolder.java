package com.example.jpdeguzman.popularmovies.Holders;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Data.FavoriteMovieDbHelper;
import com.example.jpdeguzman.popularmovies.Data.FavoriteMoviesContract;
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

    private SQLiteDatabase mDb;

    private MovieModel mCurrentMovieDetails;

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

        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(context);
        mDb = dbHelper.getWritableDatabase();
    }

    public void configureDetailsViewHolder(
            MovieDetailsViewHolder detailsHolder, ArrayList<MovieModel> detailItemList, int position) {

        mCurrentMovieDetails = detailItemList.get(position);
        if (mCurrentMovieDetails != null) {
            String moviePosterPath = mCurrentMovieDetails.getMoviePosterPath();
            Picasso.with(context)
                    .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                    .into(moviePosterImageView);
            detailsHolder.movieTitleTextView.setText(mCurrentMovieDetails.getMovieTitle());
            detailsHolder.movieReleaseDateTextView.setText(
                    context.getString(R.string.details_release_date) + " " + mCurrentMovieDetails.getMovieReleaseDate());
            detailsHolder.movieUserRatingTextView.setText(
                    context.getString(R.string.details_user_rating) + " " + mCurrentMovieDetails.getMovieUserRating());
            detailsHolder.movieOverviewTextView.setText(mCurrentMovieDetails.getMovieOverview());
        }
    }

    @Override
    public void onClick(View v) {
        if (markMovieAsFavoriteButton.getText().equals(context.getString(R.string.details_make_favorite))) {
            addNewFavoriteMovie(mCurrentMovieDetails);
            markMovieAsFavoriteButton.setText(context.getString(R.string.details_remove_favorite));
        } else {
            markMovieAsFavoriteButton.setText(context.getString(R.string.details_make_favorite));
        }
    }

    private long addNewFavoriteMovie(MovieModel favoriteMovie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                favoriteMovie.getMovieId());
        contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH,
                favoriteMovie.getMoviePosterPath());
        contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE,
                favoriteMovie.getMovieTitle());
        contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING,
                favoriteMovie.getMovieUserRating());
        contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                favoriteMovie.getMovieReleaseDate());
        contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW,
                favoriteMovie.getMovieOverview());
        return mDb.insert(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME, null, contentValues);
    }
}
