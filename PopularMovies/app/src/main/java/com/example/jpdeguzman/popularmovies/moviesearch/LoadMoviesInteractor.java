package com.example.jpdeguzman.popularmovies.moviesearch;

import com.example.jpdeguzman.popularmovies.data.models.MovieResultsModel;

import retrofit2.Call;

/**
 * Interface defined for LoadMoviesInteractorImpl
 */
public interface LoadMoviesInteractor {
    Call<MovieResultsModel> loadMoviesByType(String movieType);
}
