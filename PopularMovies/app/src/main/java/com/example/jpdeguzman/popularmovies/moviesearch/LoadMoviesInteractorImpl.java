package com.example.jpdeguzman.popularmovies.moviesearch;

import android.text.TextUtils;

import com.example.jpdeguzman.popularmovies.data.constants.MovieConstants;
import com.example.jpdeguzman.popularmovies.data.services.MovieClient;
import com.example.jpdeguzman.popularmovies.data.models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.data.services.MovieDetailsService;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import retrofit2.Call;

/**
 * Responsible for interacting with {@link MovieDetailsService} to retrieve movie data based on
 * the movie type.
 */
public class LoadMoviesInteractorImpl implements LoadMoviesInteractor {

    @Override
    public Call<MovieResultsModel> loadMoviesByType(String movieType) {
        String userApiKey = ApplicationContext.getContext().getResources().getString(R.string.api_key);
        if (!TextUtils.isEmpty(movieType)) {
            if (movieType.equals(MovieConstants.MOVIE_TYPE_POPULAR_KEY)) {
                return MovieClient.getMovieDetailsService().getPopularMovies(userApiKey);
            } else if (movieType.equals(MovieConstants.MOVIE_TYPE_TOP_RATED_KEY)) {
                return MovieClient.getMovieDetailsService().getTopRatedMovies(userApiKey);
            }
        }
        return null;
    }
}
