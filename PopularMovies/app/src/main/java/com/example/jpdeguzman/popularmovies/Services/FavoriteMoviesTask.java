package com.example.jpdeguzman.popularmovies.Services;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.jpdeguzman.popularmovies.Data.FavoriteMoviesContract;

/**
 * Asynchronously queries the favorites database for the user's favorite movies
 */

public class FavoriteMoviesTask extends AsyncTask<Void, Void, Cursor> {

    private OnEventListener<Cursor> mCallback;

    private Context mContext;

    private Exception mException;

    public FavoriteMoviesTask(Context context, OnEventListener callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected Cursor doInBackground(Void... params) {
        try {
            return mContext.getContentResolver().query(
                    FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavoriteMoviesContract.FavoriteMovieEntry._ID
            );
        } catch (Exception e) {
            mException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        if (mCallback != null) {
            if (mException == null) {
                mCallback.onSuccess(cursor);
            } else {
                mCallback.onFailure(mException);
            }
        }
    }
}
