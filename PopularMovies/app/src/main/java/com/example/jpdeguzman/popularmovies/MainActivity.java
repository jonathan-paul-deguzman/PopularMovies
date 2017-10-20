package com.example.jpdeguzman.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.jpdeguzman.popularmovies.Adapters.ImageAdapter;
import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mMoviePostersGridView;

    private Map<Integer, MovieModel> mMovieHashMap = new HashMap<>();
    private ArrayList<String> mMoviePosterPathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviePostersGridView = (GridView) findViewById(R.id.gv_movie_posters);
        loadMoviesByType("popular");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_popular:
                loadMoviesByType("popular");
                return true;
            case R.id.menu_sort_by_top_rated:
                loadMoviesByType("top_rated");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMoviesByType(String movieType) {
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();

        Call<MovieResultsModel> movieResults = null;
        if (movieType.equals("popular")) {
            movieResults = movieDetails.getPopularMovies(getResources().getString(R.string.api_key));
        } else if (movieType.equals("top_rated")) {
            movieResults = movieDetails.getTopRatedMovies(getResources().getString(R.string.api_key));
        }

        movieResults.enqueue(new Callback<MovieResultsModel>() {
            @Override
            public void onResponse(Call<MovieResultsModel> call, Response<MovieResultsModel> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "loadMoviesByType:onResponse:isSuccessful");
                    mMoviePosterPathList.clear();
                    ArrayList<MovieModel> movieResultsList = response.body().getMovies();
                    for (MovieModel movie : movieResultsList) {
                        mMovieHashMap.put(movie.getMovieId(), movie);
                        mMoviePosterPathList.add(movie.getMoviePosterPath());
                    }
                    loadImagesIntoGridView();
                } else {
                    Log.i(TAG, "loadMoviesByType:onResponse:isNotSuccessful:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResultsModel> call, Throwable t) {
                Log.i(TAG, "loadMoviesByType:onFailure");
            }
        });
    }

    private void loadImagesIntoGridView() {
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        ImageAdapter imageAdapter = new ImageAdapter(this, mMoviePosterPathList);
        mMoviePostersGridView.setColumnWidth(displayWidth / 2);
        mMoviePostersGridView.setAdapter(imageAdapter);
    }
}
