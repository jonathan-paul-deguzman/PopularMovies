package com.example.jpdeguzman.popularmovies.Services;

import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDetailsService {
    @GET("movie/popular")
    public Call<MovieResultsModel> getPopularMovies(@Query("api_key") String userApiKey);

    @GET("movie/top_rated")
    public Call<MovieResultsModel> getTopRatedMovies(@Query("api_key") String userApiKey);

    @GET("movie/{movie_id}/title")
    public String getMovieTitle();

    @GET("movie/{movie_id}/poster_path")
    public String getMoviePosterPath();

    @GET("movie/{movie_id}/overview")
    public String getMovieOverview();

    @GET("movie/{movie_id}/vote_average")
    public Double getMovieUserRating();

    @GET("movie/{movie_id}/release_date")
    public String getMovieReleaseDate();
}
