package com.example.jpdeguzman.popularmovies.moviesearch;

import com.example.jpdeguzman.popularmovies.BasePresenter;
import com.example.jpdeguzman.popularmovies.BaseView;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;

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

        void loadMovies(MovieSearchType movieType);

        void loadFavoriteMovies(ArrayList<MovieModel> favoriteMovies);

        boolean isNetworkAvailable();

        void moviePosterItemClicked(int position);
    }
}
