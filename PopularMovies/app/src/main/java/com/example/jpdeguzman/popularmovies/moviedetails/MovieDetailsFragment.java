package com.example.jpdeguzman.popularmovies.moviedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpdeguzman.popularmovies.data.adapters.MovieDetailsAdapter;
import com.example.jpdeguzman.popularmovies.data.constants.MovieConstants;
import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.ReviewModel;
import com.example.jpdeguzman.popularmovies.data.models.VideoModel;
import com.example.jpdeguzman.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends Fragment implements MovieDetailsContract.View {

    private MovieDetailsContract.Presenter mDetailsPresenter;

    @BindView(R.id.rv_movie_details) RecyclerView mMovieDetailsRecyclerView;

    public MovieDetailsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDetailsPresenter = new MovieDetailsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        mMovieDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle data = getActivity().getIntent().getExtras();
        MovieModel movieDetails = data.getParcelable(MovieConstants.MOVIE_EXTRA);
        mDetailsPresenter.getDetailsResults(movieDetails);

        return view;
    }

    @Override
    public void bindDataToAdapter(ArrayList<MovieModel> detailsList, ArrayList<VideoModel>
            videoList, ArrayList<ReviewModel> reviewList) {
        mMovieDetailsRecyclerView.setAdapter(new MovieDetailsAdapter(detailsList, videoList, reviewList));
    }
}
