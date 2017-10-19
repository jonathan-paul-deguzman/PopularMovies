package com.example.jpdeguzman.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

/**
 * POJO
 */
public class MovieModel {

    @SerializedName("overview")
    private String movieOverview;

    @SerializedName("poster_path")
    private String moviePosterPath;

    @SerializedName("title")
    private String movieTitle;

    @SerializedName("vote_average")
    private String movieUserRating;

    @SerializedName("release_date")
    private String movieReleaseDate;

    /**
     *
     * @return movieOverview
     */
    public String getMovieOverview() {
        return movieOverview;
    }

    /**
     *
     * @param movieOverview movie plot synopsis
     */
    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    /**
     *
     * @return moviePosterPath
     */
    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    /**
     *
     * @param moviePosterPath movie image thumbnail
     */
    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }

    /**
     *
     * @return movieTitle
     */
    public String getMovieTitle() {
        return movieTitle;
    }

    /**
     *
     * @param movieTitle movie title
     */
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    /**
     *
     * @return movieUserRating
     */
    public String getMovieUserRating() {
        return movieUserRating;
    }

    /**
     *
     * @param movieUserRating movie rating determined by users' average votes
     */
    public void setMovieUserRating(String movieUserRating) {
        this.movieUserRating = movieUserRating;
    }

    /**
     *
     * @return movieReleaseDate
     */
    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    /**
     *
     * @param movieReleaseDate movie release date
     */
    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }
}
