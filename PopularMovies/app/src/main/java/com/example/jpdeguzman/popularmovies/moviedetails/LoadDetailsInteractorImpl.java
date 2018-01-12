package com.example.jpdeguzman.popularmovies.moviedetails;

import com.example.jpdeguzman.popularmovies.data.services.MovieClient;
import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.ReviewResultsModel;
import com.example.jpdeguzman.popularmovies.data.models.VideoResultsModel;
import com.example.jpdeguzman.popularmovies.data.models.VideosAndReviewsModel;
import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.data.services.MovieDetailsService;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Responsible for interacting with {@link MovieDetailsService} to retrieve movie details data such
 * as trailers or reviews.
 */
public class LoadDetailsInteractorImpl implements LoadDetailsInteractor {

    @Override
    public Observable<VideosAndReviewsModel> setupMovieDetailsPage(MovieModel selectedMovie) {
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();
        String userApiKey = ApplicationContext.getContext().getResources().getString(R.string.api_key);

        Observable<VideoResultsModel> videoObservable = movieDetails
                .getMovieVideos(selectedMovie.getMovieId(), userApiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<ReviewResultsModel> reviewObservable = movieDetails
                .getMovieReviews(selectedMovie.getMovieId(), userApiKey)
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

        return combinedResults;
    }
}
