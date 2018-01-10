package com.example.jpdeguzman.popularmovies.moviesearch;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jpdeguzman.popularmovies.Adapters.FavoriteMovieAdapter;
import com.example.jpdeguzman.popularmovies.Adapters.MovieAdapter;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.data.database.FavoriteMovieContract;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays a grid view of movie posters received from TMDb. User can choose to sort movies by
 * popular, top rated, or favorite. Clicking on a poster will launch the MovieDetailsActivity.
 */
public class MovieSearchFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler,
        FavoriteMovieAdapter.FavoriteMovieAdapterOnClickHandler, MovieSearchContract.View {

    private static final int NUMBER_OF_GRID_COLUMNS = 2;

    private MovieSearchContract.Presenter mPresenter;

    private MovieSearchType mCurrentMovieSearchType;

    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingProgressBar;

    @BindView(R.id.rv_movie_posters) RecyclerView mMoviePostersRecyclerView;

    public MovieSearchFragment() {}

    public static MovieSearchFragment newInstance() {
        return new MovieSearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new MovieSearchPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentMovieSearchType == null) {
            mCurrentMovieSearchType = MovieSearchType.MOVIE_TYPE_POPULAR;
        }
        mPresenter.loadMovies(mCurrentMovieSearchType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_popular:
                mCurrentMovieSearchType = MovieSearchType.MOVIE_TYPE_POPULAR;
                break;
            case R.id.menu_sort_by_top_rated:
                mCurrentMovieSearchType = MovieSearchType.MOVIE_TYPE_TOP_RATED;
                break;
            case R.id.menu_sort_by_favorites:
                mCurrentMovieSearchType = MovieSearchType.MOVIE_TYPE_FAVORITE;
                break;
        }
        mPresenter.loadMovies(mCurrentMovieSearchType);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_movies, menu);
    }

    public void showMovies(ArrayList<MovieModel> movieList) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_GRID_COLUMNS);
        mMoviePostersRecyclerView.setLayoutManager(layoutManager);
        MovieAdapter adapter = new MovieAdapter(movieList, this);
        mMoviePostersRecyclerView.setAdapter(adapter);
    }

    public void showFavoriteMovies(ArrayList<MovieModel> favoriteMovieList) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_GRID_COLUMNS);
        mMoviePostersRecyclerView.setLayoutManager(layoutManager);
//        FavoriteMovieAdapter adapter = new FavoriteMovieAdapter(mContext, favoriteMovieList, this);
//        mMoviePostersRecyclerView.setAdapter(adapter);
    }

    public void showLoadingProgressBar() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoadingProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showNoNetworkConnection() {
        View view = getActivity().findViewById(R.id.movie_search_activity_layout);
        Snackbar.make(view, getResources().getString(R.string.error_no_network_connection),
                Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.loadMovies(mCurrentMovieSearchType);
                    }
                }).show();
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnFavoriteItemClick(int position) {

    }
}
