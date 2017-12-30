package com.example.jpdeguzman.popularmovies.Services;

/**
 * Created by jpdeguzman on 12/30/17.
 */

public interface OnEventListener<Cursor> {
    public void onSuccess(Cursor cursor);
    public void onFailure(Exception e);
}
