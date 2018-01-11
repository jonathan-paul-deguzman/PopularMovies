package com.example.jpdeguzman.popularmovies.moviesearch;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.database.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import java.util.ArrayList;

/**
 * Loads the data from the Content Provider for display in MovieSearchFragment
 */
public class FavoriteMovieLoader extends AsyncTaskLoader<ArrayList<MovieModel>> {

    public FavoriteMovieLoader(Context context) {
        super(context);
    }

    /**
     * Queries for favorite movies in the background thread
     *
     * @return an ArrayList of favorite movies
     */
    @Override
    public ArrayList<MovieModel> loadInBackground() {
        try {
            Cursor cursor = ApplicationContext.getContext().getContentResolver().query(
                    FavoriteMovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
            return getMoviesFromCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Uses a cursor to convert database column values into a MovieModel object
     *
     * @param cursor a row representing a favorite movie in the favorites database
     * @return a MovieModel object consisting of values from the cursor
     */
    private MovieModel populateMovieUsingCursor(Cursor cursor) {
        int movieIdIndex =
                cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_ID));
        String overviewIndex =
                cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW));
        String posterPathIndex =
                cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH));
        String titleIndex =
                cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_TITLE));
        String userRatingIndex =
                cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING));
        String releaseDateIndex =
                cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE));
        String backdropPathIndex =
                cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH));
        return new MovieModel(movieIdIndex, overviewIndex, posterPathIndex, titleIndex,
                userRatingIndex, releaseDateIndex, backdropPathIndex, true);
    }

    private ArrayList<MovieModel> getMoviesFromCursor(Cursor cursor) {
        ArrayList<MovieModel> favoriteMovieList = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            MovieModel favoriteMovie = populateMovieUsingCursor(cursor);
            favoriteMovieList.add(favoriteMovie);
        }
        return favoriteMovieList;
    }
}
