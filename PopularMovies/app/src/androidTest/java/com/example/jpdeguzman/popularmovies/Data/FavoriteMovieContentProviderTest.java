package com.example.jpdeguzman.popularmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

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
        Log.d("flash", "deleting");
        int rowsDeleted = db.delete(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME, "1", null);
        Log.d("flash", "rows deleted: " + rowsDeleted);
    }

    // Content Uri for favorites directory
    private static final Uri TEST_FAVORITES = FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI;
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
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, "testId");
        testFavoriteValues.put(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, "testTitle");
        testFavoriteValues.put(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH, "testPosterPath");
        testFavoriteValues.put(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING, "testUserRating");
        testFavoriteValues.put(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE, "testReleaseDate");
        testFavoriteValues.put(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW, "testOverview");

        ContentResolver testContentResolver = mContext.getContentResolver();

        Uri uri = testContentResolver.insert(
                FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI, testFavoriteValues);
        Uri expectedUri = ContentUris.withAppendedId(
                FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI, 1);
        Log.d("flash", "uri: " + uri);
        Log.d("flash", "expected uri: " + expectedUri);

        String insertProviderFailed = "Unable to insert item through Provider";
        assertEquals(insertProviderFailed, uri, expectedUri);
    }

//    @Test
//    public void testQuery() {
//        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        assertTrue(db.isOpen());
//    }
//
//    @Test
//    public void testDelete() {
//        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        assertTrue(db.isOpen());
//    }
}
