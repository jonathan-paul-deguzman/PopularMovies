package com.example.jpdeguzman.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.jpdeguzman.popularmovies.Adapters.MovieAdapter;
import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mMoviePostersGridView;

    private MovieModel mMovieResultSelected;

    private ArrayList<MovieModel> mMovieResultsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviePostersGridView = findViewById(R.id.gv_movie_posters);
        mMoviePostersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMovieResultSelected = (MovieModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra(".MovieModel", mMovieResultSelected);
                startActivity(intent);
            }
        });
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
                    if (!mMovieResultsList.isEmpty()) {
                        mMovieResultsList.clear();
                    }
                    mMovieResultsList = response.body().getMovies();
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
        MovieAdapter movieAdapter = new MovieAdapter(this, mMovieResultsList);
        mMoviePostersGridView.setAdapter(movieAdapter);
    }
}
