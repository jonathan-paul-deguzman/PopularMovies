package com.example.jpdeguzman.popularmovies.moviedetails;

import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.VideosAndReviewsModel;

import io.reactivex.Observable;

/**
 * Interface defined for LoadDetailsInteractorImpl
 */
public interface LoadDetailsInteractor {
    Observable<VideosAndReviewsModel> setupMovieDetailsPage(MovieModel selectedMovie);
}
