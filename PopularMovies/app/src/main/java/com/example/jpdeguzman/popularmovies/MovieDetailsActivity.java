package com.example.jpdeguzman.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jpdeguzman.popularmovies.Adapters.MovieDetailsAdapter;
import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.ReviewModel;
import com.example.jpdeguzman.popularmovies.Models.ReviewResultsModel;
import com.example.jpdeguzman.popularmovies.Models.VideoModel;
import com.example.jpdeguzman.popularmovies.Models.VideoResultsModel;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private RecyclerView mRecyclerViewMovieDetails;

    private MovieModel mMovieDetails;

    private ArrayList<MovieModel> mMovieDetailsList = new ArrayList<>();

    private ArrayList<VideoModel> mVideoResultsList = new ArrayList<>();

    private ArrayList<ReviewModel> mReviewResultsList = new ArrayList<>();

    private boolean isFinishedRequestingVideos = false;

    private boolean isFinishedRequestingReviews = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mRecyclerViewMovieDetails = findViewById(R.id.rv_movie_details);
        mRecyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState != null) {
            mMovieDetails = savedInstanceState.getParcelable("currentMovie");
            mVideoResultsList = savedInstanceState.getParcelableArrayList("videoList");
            mReviewResultsList = savedInstanceState.getParcelableArrayList("reviewList");
            setupMovieDetails();
            bindDataToAdapter(mMovieDetailsList, mVideoResultsList, mReviewResultsList);
        } else {
            Bundle data = getIntent().getExtras();
            mMovieDetails = data.getParcelable(".MovieModel");
            setupMovieDetails();
            setupMovieVideos();
            setupMovieReviews();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("currentMovie", mMovieDetails);
        outState.putParcelableArrayList("videoList", mVideoResultsList);
        outState.putParcelableArrayList("reviewList", mReviewResultsList);
        super.onSaveInstanceState(outState);
    }

    private void setupMovieVideos() {
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();
        Call<VideoResultsModel> videoResults = movieDetails.getMovieVideos(mMovieDetails.getMovieId(),
                getResources().getString(R.string.api_key));
        if (videoResults != null) {
            videoResults.enqueue(new Callback<VideoResultsModel>() {
                @Override
                public void onResponse(Call<VideoResultsModel> call, Response<VideoResultsModel> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "setupMovieTrailers:onResponse:isSuccessful");
                        mVideoResultsList = response.body().getVideos();
                        if (mVideoResultsList != null) {
                            isFinishedRequestingVideos = true;
                            if (isFinishedRequestingReviews) {
                                bindDataToAdapter(mMovieDetailsList, mVideoResultsList, mReviewResultsList);
                            }
                        }
                    } else {
                        Log.i(TAG, "setupMovieTrailers:onResponse:notSuccessful:" + response.message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResultsModel> call, Throwable t) {
                    Log.i(TAG, "setupMovieTrailers:onFailure");
                }
            });
        }
    }

    private void setupMovieReviews() {
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();
        Call<ReviewResultsModel> reviewResults = movieDetails.getMovieReviews(mMovieDetails.getMovieId(),
                getResources().getString(R.string.api_key));
        if (reviewResults != null) {
            reviewResults.enqueue(new Callback<ReviewResultsModel>() {
                @Override
                public void onResponse(Call<ReviewResultsModel> call, Response<ReviewResultsModel> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "setupMovieReviews:onResponse:isSuccessful");
                        mReviewResultsList = response.body().getReviews();
                        if (mReviewResultsList != null) {
                            isFinishedRequestingReviews = true;
                            if (isFinishedRequestingVideos) {
                                bindDataToAdapter(mMovieDetailsList, mVideoResultsList, mReviewResultsList);
                            }
                        }
                    } else {
                        Log.i(TAG, "setupMovieReviews:onResponse:notSuccessful: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ReviewResultsModel> call, Throwable t) {
                    Log.i(TAG, "setupMovieReviews:onFailure");
                }
            });
        }
    }

    private void setupMovieDetails() {
        mMovieDetailsList.add(mMovieDetails);
    }

    private void bindDataToAdapter(ArrayList<MovieModel> detailsList,
                                   ArrayList<VideoModel> videoList, ArrayList<ReviewModel> reviewList) {
        mRecyclerViewMovieDetails.setAdapter(
                new MovieDetailsAdapter(this, detailsList, videoList, reviewList));
    }
}