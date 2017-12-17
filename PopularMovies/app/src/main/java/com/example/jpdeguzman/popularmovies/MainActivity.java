package com.example.jpdeguzman.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import com.example.jpdeguzman.popularmovies.Data.FavoriteMoviesContract;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MainActivity is responsible for creating the main screen that the user will see when the app
 * first boots up. This screen comprises of a GridView of movie posters and selecting a poster
 * will launch {@link MovieDetailsService} to display the corresponding movie details for the
 * selected movie.
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DEFAULT_MOVIE_TYPE = "popular";

    private static final int DEFAULT_NUMBER_OF_COLUMNS = 2;

    private static final int FAVORITE_LOADER_ID = 0;

    private ProgressBar mLoadingProgressBar;

    private MovieModel mMovieResultSelected;

    private RecyclerView mMoviePosterRecyclerView;

    private Cursor mCursor;

    private ArrayList<MovieModel> mMovieResultsList = new ArrayList<>();

    private ArrayList<String> mFavoriteMoviesIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingProgressBar = findViewById(R.id.pb_loading_indicator);
        mMoviePosterRecyclerView = findViewById(R.id.rv_movie_posters);
        mMoviePosterRecyclerView.setHasFixedSize(true);
        getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMoviesByType(DEFAULT_MOVIE_TYPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMoviesByType(DEFAULT_MOVIE_TYPE);
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
            case R.id.menu_sort_by_favorites:
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

        Call<MovieResultsModel> movieResults = null;
        if (movieType.equals("popular")) {
            movieResults = movieDetails.getPopularMovies(getResources().getString(R.string.api_key));
        } else if (movieType.equals("top_rated")) {
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
                        if (response.body() != null) {
                            mMovieResultsList = response.body().getMovies();
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
        mMovieResultSelected = mMovieResultsList.get(position);

        /*
         *  Check the favorites database to see if the selected movie is a favorite movie
         */
        String movieId = Integer.toString(mMovieResultSelected.getMovieId());
        if (mFavoriteMoviesIdList.contains(movieId)) {
            mMovieResultSelected.setFavorite(true);
        }

        Intent launchMovieDetailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        launchMovieDetailsIntent.putExtra(".MovieModel", mMovieResultSelected);
        startActivity(launchMovieDetailsIntent);
    }

    /**
     * Responsible for using our custom movie adapter to store each MovieModel object's poster
     * image in each index of the grid view
     */
    private void loadMoviePostersIntoRecyclerView() {
        MovieAdapter movieAdapter = new MovieAdapter(this, this, mMovieResultsList);
        mMoviePosterRecyclerView.setAdapter(movieAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, DEFAULT_NUMBER_OF_COLUMNS);
        mMoviePosterRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void loadFavoriteMoviesIntoRecyclerView() {
        FavoriteMovieAdapter favoriteMovieAdapter = new FavoriteMovieAdapter(this, mCursor);
        mMoviePosterRecyclerView.setAdapter(favoriteMovieAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, DEFAULT_NUMBER_OF_COLUMNS);
        mMoviePosterRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFavoritesData = null;

            @Override
            protected void onStartLoading() {
                if (mFavoritesData != null) {
                    deliverResult(mFavoritesData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return  getContentResolver().query(
                            FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavoriteMoviesContract.FavoriteMovieEntry._ID
                    );
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data");
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener
             *
             * @param data A cursor
             */
            public void deliverResult(Cursor data) {
                mFavoritesData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;

        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            mFavoriteMoviesIdList.add(data.getString(
                    data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
                        loadMoviesByType(DEFAULT_MOVIE_TYPE);
                    }
                })
                .show();
    }
}
