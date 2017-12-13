package com.example.jpdeguzman.popularmovies.Data;

import android.provider.BaseColumns;

/**
 * Created by jpdeguzman on 12/12/17.
 */

public class FavoriteMoviesContract {
    /**
     * Represents the database table with its respective columns
     */
    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_POSTER_PATH = "posterPath";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_USER_RATING = "userRating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
    }
}
