package com.example.jpdeguzman.popularmovies.data.services;

import com.example.jpdeguzman.popularmovies.data.models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.data.models.ReviewResultsModel;
import com.example.jpdeguzman.popularmovies.data.models.VideoResultsModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDetailsService {
    @GET("movie/popular")
    Call<MovieResultsModel> getPopularMovies(@Query("api_key") String userApiKey);

    @GET("movie/top_rated")
    Call<MovieResultsModel> getTopRatedMovies(@Query("api_key") String userApiKey);

    @GET("movie/{movie_id}/videos")
    Observable<VideoResultsModel> getMovieVideos(@Path("movie_id") int movieId, @Query("api_key") String userApiKey);

    @GET("movie/{movie_id}/reviews")
    Observable<ReviewResultsModel> getMovieReviews(@Path("movie_id") int movieId, @Query("api_key") String userApiKey);


}
