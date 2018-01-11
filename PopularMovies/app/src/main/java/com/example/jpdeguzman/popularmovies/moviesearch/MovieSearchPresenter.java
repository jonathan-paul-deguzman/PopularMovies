package com.example.jpdeguzman.popularmovies.moviesearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.jpdeguzman.popularmovies.data.constants.MovieConstants;
import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.moviedetails.MovieDetailsActivity;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Hosts the business logic associated with the movie search feature, and the corresponding commands
 * for the view's UI elements.
 */
public class MovieSearchPresenter implements MovieSearchContract.Presenter {

    private static final String TAG = MovieSearchPresenter.class.getSimpleName();

    private MovieSearchContract.View mMoviesView;

    public MovieSearchPresenter( MovieSearchContract.View moviesView) {
        mMoviesView = moviesView;
    }

    public void launchMovieDetailsIntent(MovieModel selectedMovie) {
        Context context = ApplicationContext.getContext();
        Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(MovieConstants.MOVIE_EXTRA, selectedMovie);
        context.startActivity(movieDetailsIntent);
    }

    @Override
    public void loadMovies(String movieType) {
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
                    Log.e(TAG, "loadMovies:onFailure:" + t.getMessage());
                    if (!isNetworkAvailable()) {
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
        if (favoriteMovies == null) return false;
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
}
