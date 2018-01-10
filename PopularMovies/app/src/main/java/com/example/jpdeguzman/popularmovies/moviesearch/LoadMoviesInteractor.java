package com.example.jpdeguzman.popularmovies.moviesearch;

import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;

import retrofit2.Call;

/**
 * Interface defined for LoadMoviesInteractorImpl
 */
public interface LoadMoviesInteractor {
    Call<MovieResultsModel> loadMoviesByType(MovieSearchType movieType);
}
