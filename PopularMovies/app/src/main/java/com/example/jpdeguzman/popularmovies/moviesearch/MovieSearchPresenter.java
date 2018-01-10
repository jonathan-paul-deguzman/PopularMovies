package com.example.jpdeguzman.popularmovies.moviesearch;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jpdeguzman on 1/5/18.
 */

public class MovieSearchPresenter implements MovieSearchContract.Presenter {

    private static final String TAG = MovieSearchPresenter.class.getSimpleName();

    private MovieSearchContract.View mMoviesView;

    public MovieSearchPresenter( MovieSearchContract.View moviesView) {
        mMoviesView = moviesView;
    }

    @Override
    public void loadMovies(MovieSearchType movieType) {
        mMoviesView.showLoadingProgressBar();
        Call<MovieResultsModel> movieResults = new LoadMoviesInteractorImpl().loadMoviesByType(movieType);
        if (movieResults != null) {
            movieResults.enqueue(new Callback<MovieResultsModel>() {
                @Override
                public void onResponse(Call<MovieResultsModel> call, Response<MovieResultsModel> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "loadMovies:onResponse:isSuccessful");
                        mMoviesView.hideLoadingProgressBar();
                        ArrayList<MovieModel> movieResultsList = response.body().getMovies();
                        mMoviesView.showMovies(movieResultsList);
                    } else {
                        Log.e(TAG, "loadMovies:notSuccessful:" + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MovieResultsModel> call, Throwable t) {
                    if (!isNetworkAvailable()) {
                        Log.e(TAG, "loadMovies:onFailure:noNetworkConnection");
                        mMoviesView.showNoNetworkConnection();
                    }
                }
            });
        }
    }

    @Override
    public void loadFavoriteMovies(ArrayList<MovieModel> favoriteMovies) {
        mMoviesView.showFavoriteMovies(favoriteMovies);
    }

    public boolean isFavoriteMovie(MovieModel movieSelected, ArrayList<MovieModel> favoriteMovies) {
        int movieId = movieSelected.getMovieId();
        for (int i = 0; i < favoriteMovies.size(); i++) {
            if (movieId == favoriteMovies.get(i).getMovieId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNetworkAvailable() {
        Context context =  ApplicationContext.getContext();
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void moviePosterItemClicked(int position) {

    }
}
