package com.example.jpdeguzman.popularmovies;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jpdeguzman.popularmovies.Adapters.MovieDetailsAdapter;
import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Data.FavoriteMovieDbHelper;
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
        Bundle data = getIntent().getExtras();
        mMovieDetails = data.getParcelable(".MovieModel");
        Log.d("test", "is favorite (activity)?" + mMovieDetails.getFavorite());
        mRecyclerViewMovieDetails = findViewById(R.id.rv_movie_details);
        mRecyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupMovieDetails();
        setupMovieVideos();
        setupMovieReviews();
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
                        if (response.body() != null) {
                            Log.i(TAG, "setupMovieTrailers:onResponse:isSuccessful");
                            mVideoResultsList = response.body().getVideos();
                            isFinishedRequestingVideos = true;
                            if (isFinishedRequestingVideos && isFinishedRequestingReviews) {
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
                        if (response.body() != null) {
                            Log.i(TAG, "setupMovieReviews:onResponse:isSuccessful");
                            mReviewResultsList = response.body().getReviews();
                            isFinishedRequestingReviews = true;
                            if (isFinishedRequestingVideos && isFinishedRequestingReviews) {
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