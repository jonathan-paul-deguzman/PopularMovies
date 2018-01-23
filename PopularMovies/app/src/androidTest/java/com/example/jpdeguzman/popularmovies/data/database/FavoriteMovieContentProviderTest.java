package com.example.jpdeguzman.popularmovies.data.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.jpdeguzman.popularmovies.data.database.FavoriteMovieContentProvider;
import com.example.jpdeguzman.popularmovies.data.database.FavoriteMovieContract;
import com.example.jpdeguzman.popularmovies.data.database.FavoriteMovieDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Instrumented test for {@link FavoriteMovieContentProvider}
 */

public class FavoriteMovieContentProviderTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /**
     *  Delete all entries in the Favorites database so that each test will start clean
     */
    @Before
    public void setUp() {
        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL" + ");";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @After
    public void tearDown() {
        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL" + ");";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    // Content Uri for favorites directory
    private static final Uri TEST_FAVORITES = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
    // Content Uri for a single favorite item with id = 1
    private static final Uri TEST_FAVORITES_WITH_ID = TEST_FAVORITES.buildUpon().appendPath("1").build();

    /**
     *  This function tests that the UriMatcher returns the correct integer value for
     *  each of the Uri types that the ContentProvider can handle.
     */
    @Test
    public void testUriMatcher() {
        UriMatcher testMatcher = FavoriteMovieContentProvider.buildUriMatcher();

        String favoritesUriDoesNotMatch = "Error: FAVORITES Uri was matched incorrectly";
        int actualFavoritesMatchCode = testMatcher.match(TEST_FAVORITES);
        int expectedFavoritesMatchCode = FavoriteMovieContentProvider.FAVORITES;
        assertEquals(
                favoritesUriDoesNotMatch,
                actualFavoritesMatchCode,
                expectedFavoritesMatchCode
        );

        String favoritesWithIdDoesNotMatch = "Error: FAVORITES_WITH_ID Uri was matched incorrectly";
        int actualFavoritesWithIdMatchCode = testMatcher.match(TEST_FAVORITES_WITH_ID);
        int expectedFavoritesWithIdMatchCode = FavoriteMovieContentProvider.FAVORITES_WITH_ID;
        assertEquals(
                favoritesWithIdDoesNotMatch,
                actualFavoritesWithIdMatchCode,
                expectedFavoritesWithIdMatchCode
        );
    }

    @Test
    public void testInsert() {
        ContentValues testFavoriteValues = new ContentValues();
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, "testId");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, "testTitle");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH, "testPosterPath");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH, "testBackdropPath");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING, "testUserRating");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE, "testReleaseDate");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW, "testOverview");

        ContentResolver testContentResolver = mContext.getContentResolver();

        Uri actualUri = testContentResolver.insert(
                FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, testFavoriteValues);
        Uri expectedUri = ContentUris.withAppendedId(
                FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, 1);

        String insertProviderFailed = "Unable to insert item through Provider";
        assertEquals(insertProviderFailed, actualUri, expectedUri);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testQuery() {
        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        ContentValues testFavoriteValues = new ContentValues();
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, "testId");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, "testTitle");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH, "testPosterPath");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH, "testBackdropPath");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING, "testUserRating");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE, "testReleaseDate");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW, "testOverview");

        long favoritesRowId = db.insert(
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                null,
                testFavoriteValues
        );

        String insertFailed = "Unable to insert directly into database";
        assertTrue(insertFailed, favoritesRowId != -1);

        db.close();

        Cursor favoritesCursor = mContext.getContentResolver().query(
                FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null
        );

        String queryFailed = "Query failed to return a valid cursor";
        assertTrue(queryFailed, favoritesCursor != null);

        favoritesCursor.close();
    }

    //@Test
    public void testDelete() {
        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        ContentValues testFavoriteValues = new ContentValues();
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, "testId");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, "testTitle");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH, "testPosterPath");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH, "testBackdropPath");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING, "testUserRating");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE, "testReleaseDate");
        testFavoriteValues.put(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW, "testOverview");

        long favoritesRowId = db.insert(
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                null,
                testFavoriteValues
        );

        db.close();

        String insertFailed = "Unable to insert directly into database";
        assertTrue(insertFailed, favoritesRowId != -1);

        ContentResolver contentResolver = mContext.getContentResolver();

        Uri uriToDelete = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI
                .buildUpon().appendPath("testId").build();
        Log.d("flasher", uriToDelete.toString());
        int favoritesDeleted = contentResolver.delete(uriToDelete, null, null);

        String deleteFailed = "Unable to delete item in database";
        assertTrue(deleteFailed, favoritesDeleted != 0);
    }
}
