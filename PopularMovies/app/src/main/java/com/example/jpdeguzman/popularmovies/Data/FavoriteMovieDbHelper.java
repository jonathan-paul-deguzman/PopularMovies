package com.example.jpdeguzman.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jpdeguzman on 12/12/17.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates and executes a String query called SQL_CREATE_FAVORITES_TABLE
     *
     * @param db the SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMoviesContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL" + ");";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }


    /**
     * Executes a drop table query, and then calls onCreate to re-create the database
     *
     * @param db            the SQLiteDatabase
     * @param oldVersion    the old database version
     * @param newVersion    the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
