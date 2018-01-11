package com.example.jpdeguzman.popularmovies.moviedetails;

import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.VideosAndReviewsModel;

import io.reactivex.Observable;

/**
 * Created by jpdeguzman on 1/9/18.
 */

public interface LoadDetailsInteractor {
    Observable<VideosAndReviewsModel> setupMovieDetailsPage(MovieModel selectedMovie);
}
