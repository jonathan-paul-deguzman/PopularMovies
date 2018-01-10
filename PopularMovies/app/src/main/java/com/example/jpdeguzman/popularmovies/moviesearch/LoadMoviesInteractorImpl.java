package com.example.jpdeguzman.popularmovies.moviesearch;

import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import retrofit2.Call;

/**
 * Implementation for the LoadMoviesInteractor interface
 */
public class LoadMoviesInteractorImpl implements LoadMoviesInteractor {

    @Override
    public Call<MovieResultsModel> loadMoviesByType(MovieSearchType movieType) {
        String userApiKey = ApplicationContext.getContext().getResources().getString(R.string.api_key);
        if (movieType != null) {
            if (movieType.equals(MovieSearchType.MOVIE_TYPE_POPULAR)) {
                return MovieClient.getMovieDetailsService().getPopularMovies(userApiKey);
            } else if (movieType.equals(MovieSearchType.MOVIE_TYPE_TOP_RATED)) {
                return MovieClient.getMovieDetailsService().getTopRatedMovies(userApiKey);
            }
        }
        return null;
    }
}
