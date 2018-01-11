package com.example.jpdeguzman.popularmovies.moviesearch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jpdeguzman.popularmovies.data.adapters.FavoriteMovieAdapter;
import com.example.jpdeguzman.popularmovies.data.adapters.MovieAdapter;
import com.example.jpdeguzman.popularmovies.data.constants.MovieConstants;
import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays a grid view of movie posters received from TMDb. User can choose to sort movies by
 * popular, top rated, or favorite. Clicking on a poster will launch the DetailsActivity.
 */
public class MovieSearchFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler,
        FavoriteMovieAdapter.FavoriteMovieAdapterOnClickHandler, MovieSearchContract.View,
        LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    private MovieSearchContract.Presenter mPresenter;

    private String mCurrentSearchType;

    private int mCurrentScrollPosition;

    private int mCurrentScrollOffset;

    private ArrayList<MovieModel> mFavoriteMoviesList = new ArrayList<>();

    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingProgressBar;

    @BindView(R.id.rv_movie_posters) RecyclerView mMoviePostersRecyclerView;

    public MovieSearchFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new MovieSearchPresenter(this);
        getLoaderManager().initLoader(MovieConstants.FAVORITE_MOVIE_LOADER_ID, null, this).forceLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager =
                new GridLayoutManager(getContext(), MovieConstants.MOVIE_POSTERS_DEFAULT_COLUMNS);
        mMoviePostersRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mCurrentScrollPosition = preferences.getInt(MovieConstants.MOVIE_POSTERS_SCROLL_POSITION_KEY, 0);
        mCurrentScrollOffset = preferences.getInt(MovieConstants.MOVIE_POSTERS_SCROLL_OFFSET_KEY, 0);
        String savedMovieType = preferences.getString(
                MovieConstants.MOVIE_TYPE_SAVED_KEY, MovieConstants.MOVIE_TYPE_POPULAR_KEY);
        mPresenter.loadMovies(savedMovieType);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View firstChildInView = mMoviePostersRecyclerView.getChildAt(0);
        int currentScrollPosition = mMoviePostersRecyclerView.getChildAdapterPosition(firstChildInView);
        int currentScrollOffset = firstChildInView.getTop();
        preferences.edit()
                .putInt(MovieConstants.MOVIE_POSTERS_SCROLL_POSITION_KEY, currentScrollPosition)
                .putInt(MovieConstants.MOVIE_POSTERS_SCROLL_OFFSET_KEY, currentScrollOffset)
                .putString(MovieConstants.MOVIE_TYPE_SAVED_KEY, mCurrentSearchType)
                .apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_popular:
                mCurrentSearchType = MovieConstants.MOVIE_TYPE_POPULAR_KEY;
                mPresenter.loadMovies(mCurrentSearchType);
                break;
            case R.id.menu_sort_by_top_rated:
                mCurrentSearchType = MovieConstants.MOVIE_TYPE_TOP_RATED_KEY;
                mPresenter.loadMovies(mCurrentSearchType);
                break;
            case R.id.menu_sort_by_favorites:
                mCurrentSearchType = MovieConstants.MOVIE_TYPE_FAVORITE_KEY;
                mPresenter.loadFavoriteMovies(mFavoriteMoviesList);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_movies, menu);
    }

    @Override
    public void showMovies(ArrayList<MovieModel> movieList) {
        MovieAdapter adapter = new MovieAdapter(movieList, this);
        mMoviePostersRecyclerView.setAdapter(adapter);
        mMoviePostersRecyclerView.scrollToPosition(mCurrentScrollPosition);
        mMoviePostersRecyclerView.scrollBy(0, - mCurrentScrollOffset);
    }

    @Override
    public void showFavoriteMovies(ArrayList<MovieModel> favoriteMovieList) {
        FavoriteMovieAdapter adapter = new FavoriteMovieAdapter(favoriteMovieList, this);
        mMoviePostersRecyclerView.setAdapter(adapter);
        mMoviePostersRecyclerView.scrollToPosition(mCurrentScrollPosition);
        mMoviePostersRecyclerView.scrollBy(0, - mCurrentScrollOffset);
    }

    @Override
    public void showLoadingProgressBar() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoNetworkConnection() {
        View view = getActivity().findViewById(R.id.movie_search_activity_layout);
        String snackBarActionMessage = getResources().getString(R.string.error_retry);
        String snackBarErrorMessage = getResources().getString(R.string.error_no_network_connection);
        Snackbar.make(view, snackBarErrorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(snackBarActionMessage, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.loadMovies(MovieConstants.MOVIE_TYPE_POPULAR_KEY);
                    }
                }).show();
    }

    @Override
    public void OnItemClick(MovieModel movie) {
        if (mPresenter.isFavoriteMovie(movie, mFavoriteMoviesList)) movie.setFavorite(true);
        mPresenter.launchMovieDetailsIntent(movie);
    }

    @Override
    public void OnFavoriteItemClick(MovieModel favoriteMovie) {
        favoriteMovie.setFavorite(true);
        mPresenter.launchMovieDetailsIntent(favoriteMovie);
    }

    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, Bundle args) {
        return new FavoriteMovieLoader(ApplicationContext.getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {
        mFavoriteMoviesList = data;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {}
}
