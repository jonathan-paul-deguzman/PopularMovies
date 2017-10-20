package com.example.jpdeguzman.popularmovies.Clients;

import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MovieClient creates our {@link MovieClient} object using {@link MovieDetailsService}.
 */
public class MovieClient {

    private static final String NETWORK_BASE_URL = "http://api.themoviedb.org/3/";

    private static Retrofit getMovieClientInstance() {
        return new Retrofit.Builder()
                .baseUrl(NETWORK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MovieDetailsService getMovieDetailsService() {
        return getMovieClientInstance().create(MovieDetailsService.class);
    }
}