package com.example.jpdeguzman.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Helpers.NetworkHelper;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mDisplayMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplayMovies = (TextView) findViewById(R.id.tv_display_movies);
        loadPopularMovies();
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
                loadPopularMovies();
                return true;
            case R.id.menu_sort_by_top_rated:
                //loadMovieDataByType("top_rated");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadPopularMovies() {
        String apiKeyFromResFolder = getResources().getString(R.string.api_key);
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();
        Call<MovieResultsModel> movieResults = movieDetails.getPopularMovies(apiKeyFromResFolder);
        movieResults.enqueue(new Callback<MovieResultsModel>() {
            @Override
            public void onResponse(Call<MovieResultsModel> call, Response<MovieResultsModel> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "loadPopularMovies:onResponse:isSuccessful");
                    ArrayList<MovieModel> movieResultsList = response.body().getMovies();
                    mDisplayMovies.setText(movieResultsList.toString());
                } else {
                    Log.i(TAG, "loadPopularMovies:onResponse:isNotSuccessful:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResultsModel> call, Throwable t) {
                Log.i(TAG, "loadPopularMovies:onFailure");
            }
        });
    }

    public class FetchMovieDatabaseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String typeOfMovie = params[0];
            URL movieRequestUrl = NetworkHelper.buildUrl(typeOfMovie);

            try {
                String jsonMovieResponse = NetworkHelper.getResponseFromHttpUrl(movieRequestUrl);
                return jsonMovieResponse;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String movieResponse) {
            mDisplayMovies.setText(movieResponse);
        }
    }
}
