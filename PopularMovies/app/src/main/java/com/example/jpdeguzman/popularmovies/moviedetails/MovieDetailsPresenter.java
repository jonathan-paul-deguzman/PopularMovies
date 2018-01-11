package com.example.jpdeguzman.popularmovies.moviedetails;

import android.util.Log;

import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.ReviewModel;
import com.example.jpdeguzman.popularmovies.data.models.VideoModel;
import com.example.jpdeguzman.popularmovies.data.models.VideosAndReviewsModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {

    private static final String TAG = MovieDetailsPresenter.class.getSimpleName();

    private MovieDetailsContract.View mDetailsView;

    public MovieDetailsPresenter(MovieDetailsContract.View detailsView) {
        mDetailsView = detailsView;
    }

    @Override
    public void getDetailsResults(final MovieModel movieSelected) {
        Observable<VideosAndReviewsModel> combinedResults =
                new LoadDetailsInteractorImpl().setupMovieDetailsPage(movieSelected);
        combinedResults.subscribe(new Observer<VideosAndReviewsModel>() {
            @Override
            public void onNext(VideosAndReviewsModel value) {
                ArrayList<VideoModel> videoList = value.getVideoResults().getVideos();
                ArrayList<ReviewModel> reviewList = value.getReviewResults().getReviews();
                ArrayList<MovieModel> detailList = new ArrayList<>();
                detailList.add(movieSelected);
                mDetailsView.bindDataToAdapter(detailList, videoList, reviewList);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "getDetailsResults:onError:" + e.getMessage());
            }

            @Override
            public void onComplete() { }

            @Override
            public void onSubscribe(Disposable d) { }
        });
    }
}
