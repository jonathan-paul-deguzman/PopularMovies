package com.example.jpdeguzman.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.jpdeguzman.popularmovies.Adapters.MovieDetailsAdapter;
import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Constants.MovieDetails;
import com.example.jpdeguzman.popularmovies.Constants.Movies;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.ReviewModel;
import com.example.jpdeguzman.popularmovies.Models.ReviewResultsModel;
import com.example.jpdeguzman.popularmovies.Models.VideoModel;
import com.example.jpdeguzman.popularmovies.Models.VideoResultsModel;
import com.example.jpdeguzman.popularmovies.Models.VideosAndReviewsModel;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @BindView(R.id.rv_movie_details) RecyclerView mRecyclerViewMovieDetails;

    @BindString(R.string.api_key) String mUserApiKey;

    private MovieModel mMovieDetails;

    private ArrayList<MovieModel> mMovieDetailsList = new ArrayList<>();

    private ArrayList<VideoModel> mVideoResultsList = new ArrayList<>();

    private ArrayList<ReviewModel> mReviewResultsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        mRecyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState != null) {
            mMovieDetails = savedInstanceState.getParcelable(MovieDetails.DETAILS_CURRENT_MOVIE_SAVED);
            mVideoResultsList = savedInstanceState.getParcelableArrayList(MovieDetails.DETAILS_VIDEO_LIST_SAVED);
            mReviewResultsList = savedInstanceState.getParcelableArrayList(MovieDetails.DETAILS_REVIEW_LIST_SAVED);
            mMovieDetailsList.add(mMovieDetails);
            bindDataToAdapter(mMovieDetailsList, mVideoResultsList, mReviewResultsList);
        } else {
            Bundle data = getIntent().getExtras();
            mMovieDetails = data.getParcelable(Movies.MOVIE_EXTRA);
            mMovieDetailsList.add(mMovieDetails);
            setupMovieDetailsPageWithObservables();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MovieDetails.DETAILS_CURRENT_MOVIE_SAVED, mMovieDetails);
        outState.putParcelableArrayList(MovieDetails.DETAILS_VIDEO_LIST_SAVED, mVideoResultsList);
        outState.putParcelableArrayList(MovieDetails.DETAILS_REVIEW_LIST_SAVED, mReviewResultsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupMovieDetailsPageWithObservables() {
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();

        Observable<VideoResultsModel> videoObservable = movieDetails
                .getMovieVideos(mMovieDetails.getMovieId(), mUserApiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<ReviewResultsModel> reviewObservable = movieDetails
                .getMovieReviews(mMovieDetails.getMovieId(), mUserApiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<VideosAndReviewsModel> combinedResults = Observable.zip(videoObservable, reviewObservable,
                new BiFunction<VideoResultsModel, ReviewResultsModel, VideosAndReviewsModel>() {
                    @Override
                    public VideosAndReviewsModel apply(VideoResultsModel videoResultsModel, ReviewResultsModel
                            reviewResultsModel) throws Exception {
                        return new VideosAndReviewsModel(videoResultsModel, reviewResultsModel);
                    }
                });

        combinedResults.subscribe(new Observer<VideosAndReviewsModel>() {
            @Override
            public void onNext(VideosAndReviewsModel videosAndReviewsModel) {
                mVideoResultsList = videosAndReviewsModel.getVideoResults().getVideos();
                mReviewResultsList = videosAndReviewsModel.getReviewResults().getReviews();
                bindDataToAdapter(mMovieDetailsList, mVideoResultsList, mReviewResultsList);
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "setupMovieDetailsPageWithObservables:onError:" + t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "setupMovieDetailsPageWithObservables:onComplete");
            }

            @Override
            public void onSubscribe(Disposable d) {}
        });
    }

    private void bindDataToAdapter(ArrayList<MovieModel> detailsList,
                                   ArrayList<VideoModel> videoList, ArrayList<ReviewModel> reviewList) {
        mRecyclerViewMovieDetails.setAdapter(
                new MovieDetailsAdapter(this, detailsList, videoList, reviewList));
    }
}