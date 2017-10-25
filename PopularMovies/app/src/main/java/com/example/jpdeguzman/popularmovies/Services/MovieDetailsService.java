package com.example.jpdeguzman.popularmovies.Services;

import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDetailsService {
    @GET("movie/popular")
    Call<MovieResultsModel> getPopularMovies(@Query("api_key") String userApiKey);

    @GET("movie/top_rated")
    Call<MovieResultsModel> getTopRatedMovies(@Query("api_key") String userApiKey);

    @GET("movie/{movie_id}/title")
    String getMovieTitle();

    @GET("movie/{movie_id}/poster_path")
    String getMoviePosterPath();

    @GET("movie/{movie_id}/overview")
    String getMovieOverview();

    @GET("movie/{movie_id}/vote_average")
    Double getMovieUserRating();

    @GET("movie/{movie_id}/release_date")
    String getMovieReleaseDate();
}
