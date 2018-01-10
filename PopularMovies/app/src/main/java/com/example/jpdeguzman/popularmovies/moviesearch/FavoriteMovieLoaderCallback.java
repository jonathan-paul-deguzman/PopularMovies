package com.example.jpdeguzman.popularmovies.moviesearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;

import java.util.ArrayList;

/**
 * Created by jpdeguzman on 1/6/18.
 */
public class FavoriteMovieLoaderCallback implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    private Context mContext;

    private MovieSearchContract.View mView;

    public FavoriteMovieLoaderCallback(Context context, MovieSearchContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, Bundle args) {
        return new FavoriteMovieLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {
        mView.showFavoriteMovies(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {

    }
}
