package com.example.jpdeguzman.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.jpdeguzman.popularmovies.Adapters.FavoriteMovieAdapter;
import com.example.jpdeguzman.popularmovies.Adapters.MovieAdapter;
import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Constants.Movies;
import com.example.jpdeguzman.popularmovies.Data.FavoriteMoviesContract;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.Services.FavoriteMoviesTask;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;
import com.example.jpdeguzman.popularmovies.Services.OnEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MainActivity is responsible for creating the main screen that the user will see when the app
 * first boots up. This screen comprises of a GridView of movie posters and selecting a poster
 * will launch {@link MovieDetailsService} to display the corresponding movie details for the
 * selected movie.
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        FavoriteMovieAdapter.FavoriteMovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingProgressBar;

    @BindView(R.id.rv_movie_posters) RecyclerView mMoviePosterRecyclerView;

    private String mCurrentMovieType;

    private ArrayList<MovieModel> mMovieResultsList = new ArrayList<>();

    private ArrayList<MovieModel> mFavoriteMoviesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getFavoriteMovies();

        if (savedInstanceState != null) {
            String savedMovieType = savedInstanceState.getString(Movies.MOVIE_TYPE_SAVED);
            loadSavedMovieTypeIntoRecyclerView(savedMovieType);
        } else {
            loadMoviesByType(Movies.MOVIE_TYPE_DEFAULT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavoriteMovies();
        if (getIntent().getExtras() != null) {
            String savedMovieType = getIntent().getExtras().getString(Movies.MOVIE_TYPE_SAVED);
            loadSavedMovieTypeIntoRecyclerView(savedMovieType);
        } else {
            loadMoviesByType(Movies.MOVIE_TYPE_DEFAULT);
        }
    }

    @Override
    protected void onStop() {
        getIntent().putExtra(Movies.MOVIE_TYPE_SAVED, mCurrentMovieType);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Movies.MOVIE_TYPE_SAVED, mCurrentMovieType);
        super.onSaveInstanceState(outState);
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
                mCurrentMovieType = Movies.MOVIE_TYPE_POPULAR;
                loadMoviesByType(mCurrentMovieType);
                return true;
            case R.id.menu_sort_by_top_rated:
                mCurrentMovieType = Movies.MOVIE_TYPE_TOP_RATED;
                loadMoviesByType(mCurrentMovieType);
                return true;
            case R.id.menu_sort_by_favorites:
                mCurrentMovieType = Movies.MOVIE_TYPE_FAVORITE;
                loadFavoriteMoviesIntoRecyclerView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Responsible for interacting with {@link MovieDetailsService} to retrieve movie data based on
     * the movie type
     *
     * @param movieType either popular or top_rated as of right now
     */
    private void loadMoviesByType(String movieType) {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();

        Log.i(TAG, "loadMoviesByType:Type:" + movieType);
        Call<MovieResultsModel> movieResults = null;
        if (Movies.MOVIE_TYPE_POPULAR.equals(movieType)) {
            movieResults = movieDetails.getPopularMovies(getResources().getString(R.string.api_key));
        } else if (Movies.MOVIE_TYPE_TOP_RATED.equals(movieType)) {
            movieResults = movieDetails.getTopRatedMovies(getResources().getString(R.string.api_key));
        }

        /*
         *  Asynchronously send network request for movie data and notify Callback of its response
         */
        if (movieResults != null) {
            movieResults.enqueue(new Callback<MovieResultsModel>() {
                @Override
                public void onResponse(Call<MovieResultsModel> call, Response<MovieResultsModel> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "loadMoviesByType:onResponse:isSuccessful");
                        mLoadingProgressBar.setVisibility(View.INVISIBLE);
                        if (!mMovieResultsList.isEmpty()) {
                            mMovieResultsList.clear();
                        }
                        mMovieResultsList = response.body().getMovies();
                        if (mMovieResultsList != null) {
                            loadMoviePostersIntoRecyclerView();
                        }
                    } else {
                        Log.i(TAG, "loadMoviesByType:onResponse:isNotSuccessful:" + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MovieResultsModel> call, Throwable t) {
                    if (!isNetworkAvailable()) {
                        Log.i(TAG, "loadMoviesByType:onFailure:noNetworkConnection");
                        showErrorMessageNoNetworkConnection();
                    }
                }
            });
        }
    }

    /**
     * Override OnItemClick from MovieAdapterOnClickHandler defined in MovieAdapter
     *
     * @param position the item that was selected from the recycler view
     */
    @Override
    public void OnItemClick(int position) {
        MovieModel movieSelected = mMovieResultsList.get(position);
        String movieId = Integer.toString(movieSelected.getMovieId());
        for (int i = 0; i < mFavoriteMoviesList.size(); i++) {
            if (movieId.equals(mFavoriteMoviesList.get(i).getMovieId())) {
                movieSelected.setFavorite(true);
            }
        }
        launchMovieDetailsIntent(movieSelected);
    }

    /**
     * Override OnFavoriteItemClick from FavoriteMovieAdapterOnClickHandler defined in
     * FavoriteMovieAdapter
     *
     * @param position the item that was selected from the recycler view
     */
    @Override
    public void OnFavoriteItemClick(int position) {
        MovieModel favoriteMovieSelected = mFavoriteMoviesList.get(position);
        favoriteMovieSelected.setFavorite(true);
        launchMovieDetailsIntent(favoriteMovieSelected);
    }

    private void launchMovieDetailsIntent(MovieModel movieSelected) {
        Intent movieDetailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(Movies.MOVIE_EXTRA, movieSelected);
        startActivity(movieDetailsIntent);
    }

    private void loadMoviePostersIntoRecyclerView() {
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, Movies.MOVIE_POSTERS_DEFAULT_NUMBER_OF_COLUMNS);
        mMoviePosterRecyclerView.setLayoutManager(gridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(this, this, mMovieResultsList);
        mMoviePosterRecyclerView.setAdapter(movieAdapter);
    }

    private void loadFavoriteMoviesIntoRecyclerView() {
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, Movies.MOVIE_POSTERS_DEFAULT_NUMBER_OF_COLUMNS);
        mMoviePosterRecyclerView.setLayoutManager(gridLayoutManager);
        FavoriteMovieAdapter favoriteMovieAdapter =
                new FavoriteMovieAdapter(this, this, mFavoriteMoviesList);
        mMoviePosterRecyclerView.setAdapter(favoriteMovieAdapter);
    }

    private void loadSavedMovieTypeIntoRecyclerView(String savedMovieType) {
        if (savedMovieType == Movies.MOVIE_TYPE_FAVORITE) {
            loadFavoriteMoviesIntoRecyclerView();
        } else if (savedMovieType == Movies.MOVIE_TYPE_POPULAR
                || savedMovieType == Movies.MOVIE_TYPE_TOP_RATED) {
            loadMoviesByType(savedMovieType);
        } else {
            loadMoviesByType(Movies.MOVIE_TYPE_DEFAULT);
        }
    }

    private MovieModel populateMovieWithCursorData(Cursor cursor) {
        int movieIdIndex = cursor.getInt(cursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID));
        String overviewIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW));
        String posterPathIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH));
        String titleIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE));
        String userRatingIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING));
        String releaseDateIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE));
        String backdropPathIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH));

        return new MovieModel(movieIdIndex, overviewIndex, posterPathIndex, titleIndex,
                userRatingIndex, releaseDateIndex, backdropPathIndex, true);
    }


    private void getFavoriteMovies() {
        FavoriteMoviesTask favoriteMoviesTask =
                new FavoriteMoviesTask(this, new OnEventListener<Cursor>() {
                    @Override
                    public void onSuccess(Cursor data) {
                        Log.i(TAG, "getFavoriteMovies:onSuccess");
                        if (mFavoriteMoviesList != null) {
                            mFavoriteMoviesList.clear();
                        }

                        Cursor cursor = null;
                        try {
                            cursor = data;
                            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                                MovieModel favoriteMovie = populateMovieWithCursorData(cursor);
                                mFavoriteMoviesList.add(favoriteMovie);
                            }
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "getFavoriteMovies:onFailure:" + e.getMessage());
                    }
                });
        favoriteMoviesTask.execute();
    }

    /**
     * Responsible for determining if the device is connected to a network
     *
     * @return boolean value true if connected, false otherwise
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Responsible for prompting the user to connect to a network using a snackbar message
     */
    private void showErrorMessageNoNetworkConnection() {
        View view = findViewById(R.id.main_activity_layout);
        Snackbar.make(view, getResources().getString(R.string.error_no_network_connection),
                Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadMoviesByType(Movies.MOVIE_TYPE_POPULAR);
                    }
                })
                .show();
    }
}
