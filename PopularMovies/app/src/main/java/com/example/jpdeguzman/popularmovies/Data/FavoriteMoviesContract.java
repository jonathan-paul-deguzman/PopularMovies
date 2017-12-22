package com.example.jpdeguzman.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for creating the favorites table and columns
 */
public class FavoriteMoviesContract {

    public static final String SCHEME = "content://";

    public static final String AUTHORITY = "com.example.jpdeguzman.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    /**
     * Represents the database table with its respective columns
     */
    public static final class FavoriteMovieEntry implements BaseColumns {
        /*
         *  CONTENT_URI will represent <scheme> :// <authority> / <path>
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        /*
         *  Favorites table and column names
         */
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_POSTER_PATH = "posterPath";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_USER_RATING = "userRating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
    }
}
