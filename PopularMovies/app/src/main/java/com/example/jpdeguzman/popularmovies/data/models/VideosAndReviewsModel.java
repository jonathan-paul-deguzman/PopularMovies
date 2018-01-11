package com.example.jpdeguzman.popularmovies.data.models;

/**
 * Created by jpdeguzman on 1/1/18.
 */

public class VideosAndReviewsModel {

    private VideoResultsModel videoResults;

    private ReviewResultsModel reviewResults;

    public VideosAndReviewsModel(VideoResultsModel videoResults, ReviewResultsModel reviewResults) {
        this.videoResults = videoResults;
        this.reviewResults = reviewResults;
    }

    public VideoResultsModel getVideoResults() {
        return videoResults;
    }

    public void setVideoResults(VideoResultsModel videoResults) {
        this.videoResults = videoResults;
    }

    public ReviewResultsModel getReviewResults() {
        return reviewResults;
    }

    public void setReviewResults(ReviewResultsModel reviewResults) {
        this.reviewResults = reviewResults;
    }
}
