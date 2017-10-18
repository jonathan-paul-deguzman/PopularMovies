package com.example.jpdeguzman.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Helpers.NetworkHelper;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mDisplayMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayMovies = (TextView) findViewById(R.id.tv_display_movies);

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMovieDatabaseTask().execute("popular");
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
