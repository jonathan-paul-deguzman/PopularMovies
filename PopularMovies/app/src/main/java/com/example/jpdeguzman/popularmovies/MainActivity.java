package com.example.jpdeguzman.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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

import com.example.jpdeguzman.popularmovies.data.adapters.FavoriteMovieAdapter;
import com.example.jpdeguzman.popularmovies.data.adapters.MovieAdapter;
import com.example.jpdeguzman.popularmovies.data.services.MovieClient;
import com.example.jpdeguzman.popularmovies.Constants.Movies;
import com.example.jpdeguzman.popularmovies.data.database.FavoriteMovieContract;
import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.MovieResultsModel;
import com.example.jpdeguzman.popularmovies.data.services.MovieDetailsService;

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
        FavoriteMovieAdapter.FavoriteMovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String CURRENT_SCROLL_POSITION = "currentScrollPosition";

    private static final String CURRENT_SCROLL_OFFSET = "currentScrollOffset";

    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingProgressBar;

    @BindView(R.id.rv_movie_posters) RecyclerView mMoviePosterRecyclerView;

    private FavoriteMovieAdapter mFavoriteMovieAdapter;

    private GridLayoutManager mGridLayoutManager;

    private int mCurrentScrollPosition;

    private int mCurrentScrollOffset;

    private String mCurrentMovieType = Movies.MOVIE_TYPE_DEFAULT;

    private ArrayList<MovieModel> mMovieResultsList = new ArrayList<>();

    private ArrayList<MovieModel> mFavoriteMoviesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCurrentScrollPosition = preferences.getInt(CURRENT_SCROLL_POSITION, 0);
        mCurrentScrollOffset = preferences.getInt(CURRENT_SCROLL_OFFSET, 0);
        String savedMovieType = preferences.getString(Movies.MOVIE_TYPE_SAVED, Movies.MOVIE_TYPE_POPULAR);
        loadSavedMovieTypeIntoRecyclerView(savedMovieType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        View firstChildInView = mMoviePosterRecyclerView.getChildAt(0);
        int currentScrollPosition = mMoviePosterRecyclerView.getChildAdapterPosition(firstChildInView);
        int currentScrollOffset = firstChildInView.getTop();
        preferences.edit()
                .putInt(CURRENT_SCROLL_POSITION, currentScrollPosition)
                .putInt(CURRENT_SCROLL_OFFSET, currentScrollOffset)
                .putString(Movies.MOVIE_TYPE_SAVED, mCurrentMovieType)
                .apply();
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

//    /**
//     * Override OnItemClick from MovieAdapterOnClickHandler defined in MovieAdapter
//     *
//     * @param position the item that was selected from the recycler view
//     */
//    @Override
//    public void OnItemClick(int position) {
//        MovieModel movieSelected = mMovieResultsList.get(position);
//        int movieId = movieSelected.getMovieId();
//        for (int i = 0; i < mFavoriteMoviesList.size(); i++) {
//            if (movieId == mFavoriteMoviesList.get(i).getMovieId()) {
//                movieSelected.setFavorite(true);
//            }
//        }
//        launchMovieDetailsIntent(movieSelected);
//    }

    @Override
    public void OnItemClick(MovieModel movieSelected) {
        //MovieModel movieSelected = mMovieResultsList.get(position);
        int movieId = movieSelected.getMovieId();
        for (int i = 0; i < mFavoriteMoviesList.size(); i++) {
            if (movieId == mFavoriteMoviesList.get(i).getMovieId()) {
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
        Intent movieDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        movieDetailsIntent.putExtra(Movies.MOVIE_EXTRA, movieSelected);
        startActivity(movieDetailsIntent);
    }

    private void loadMoviePostersIntoRecyclerView() {
        mGridLayoutManager = new GridLayoutManager(this, Movies.MOVIE_POSTERS_DEFAULT_NUMBER_OF_COLUMNS);
        mMoviePosterRecyclerView.setLayoutManager(mGridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(mMovieResultsList, this);
        mMoviePosterRecyclerView.setAdapter(movieAdapter);
        mMoviePosterRecyclerView.scrollToPosition(mCurrentScrollPosition);
        mMoviePosterRecyclerView.scrollBy(0, - mCurrentScrollOffset);
    }

    private void loadFavoriteMoviesIntoRecyclerView() {
        mGridLayoutManager = new GridLayoutManager(this, Movies.MOVIE_POSTERS_DEFAULT_NUMBER_OF_COLUMNS);
        mMoviePosterRecyclerView.setLayoutManager(mGridLayoutManager);
        mFavoriteMovieAdapter = new FavoriteMovieAdapter(mFavoriteMoviesList, this);
        mMoviePosterRecyclerView.setAdapter(mFavoriteMovieAdapter);
        mMoviePosterRecyclerView.scrollToPosition(mCurrentScrollPosition);
        mMoviePosterRecyclerView.scrollBy(0, - mCurrentScrollOffset);
    }

    private void loadSavedMovieTypeIntoRecyclerView(String savedMovieType) {
        if (savedMovieType.equals(Movies.MOVIE_TYPE_FAVORITE)) {
            loadFavoriteMoviesIntoRecyclerView();
        } else if (savedMovieType.equals(Movies.MOVIE_TYPE_POPULAR)
                || savedMovieType.equals(Movies.MOVIE_TYPE_TOP_RATED)) {
            loadMoviesByType(savedMovieType);
        } else {
            loadMoviesByType(Movies.MOVIE_TYPE_DEFAULT);
        }
    }

    private MovieModel populateMovieWithCursorData(Cursor cursor) {
        int movieIdIndex = cursor.getInt(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID));
        String overviewIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW));
        String posterPathIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH));
        String titleIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE));
        String userRatingIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_USER_RATING));
        String releaseDateIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE));
        String backdropPathIndex = cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_BACKDROP_PATH));
        return new MovieModel(movieIdIndex, overviewIndex, posterPathIndex, titleIndex,
                userRatingIndex, releaseDateIndex, backdropPathIndex, true);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        try {
            return new CursorLoader(
                    this,
                    FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            Log.e(TAG, "onCreateLoader:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mFavoriteMoviesList != null) {
            mFavoriteMoviesList.clear();
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            MovieModel favoriteMovie = populateMovieWithCursorData(cursor);
            mFavoriteMoviesList.add(favoriteMovie);
            mFavoriteMovieAdapter = new FavoriteMovieAdapter(mFavoriteMoviesList, this);
            mMoviePosterRecyclerView.setAdapter(mFavoriteMovieAdapter);
        }

        if (mFavoriteMovieAdapter != null) {
            mFavoriteMovieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
