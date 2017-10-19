package com.example.jpdeguzman.popularmovies.Clients;

import android.net.Uri;
import android.util.Log;

import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MovieClient creates our {@link MovieClient} object using {@link MovieDetailsService}.
 */
public class MovieClient {

    private static final String TAG = MovieClient.class.getSimpleName();
    private static final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String TMDB_POSTER_SIZE = "w185/";

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
