package com.example.jpdeguzman.popularmovies.moviesearch;

import com.example.jpdeguzman.popularmovies.data.models.MovieModel;

import java.util.ArrayList;

/**
 * Specifies the contract between the view {@link MovieSearchFragment} and the presenter
 * {@link MovieSearchPresenter}
 */
public interface MovieSearchContract {

    interface View {

        void showMovies(ArrayList<MovieModel> movieList);

        void showFavoriteMovies(ArrayList<MovieModel> favoriteMovieList);

        void showLoadingProgressBar();

        void hideLoadingProgressBar();

        void showNoNetworkConnection();
    }

    interface Presenter {

        void launchMovieDetailsIntent(MovieModel selectedMovie);

        void loadMovies(String movieType);

        void loadFavoriteMovies(ArrayList<MovieModel> favoriteMovies);

        boolean isFavoriteMovie(MovieModel movieSelected, ArrayList<MovieModel> favoriteMovies);

        boolean isNetworkAvailable();
    }
}
