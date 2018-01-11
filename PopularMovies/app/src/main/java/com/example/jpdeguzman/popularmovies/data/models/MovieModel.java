package com.example.jpdeguzman.popularmovies.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * MovieModel is a POJO class to represent a movie object. MovieModel is also responsible for using
 * a Parcleable to get the movie object cross-activity.
 */
public class MovieModel implements Parcelable {

    @SerializedName("id")
    private int movieId;

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

    @SerializedName("backdrop_path")
    private String movieBackdropPath;

    private boolean isFavorite = false;

    public MovieModel(int movieId, String movieOverview, String moviePosterPath, String movieTitle,
                      String movieUserRating, String movieReleaseDate, String movieBackdropPath,
                      boolean isFavorite) {
        this.movieId = movieId;
        this.movieOverview = movieOverview;
        this.moviePosterPath = moviePosterPath;
        this.movieTitle = movieTitle;
        this.movieUserRating = movieUserRating;
        this.movieReleaseDate = movieReleaseDate;
        this.movieBackdropPath = movieBackdropPath;
        this.isFavorite = isFavorite;
    }

    /**
     * Constructor for re-constructing objects from a parcel
     *
     * @param in the parcel from which to read an object
     */
    public MovieModel(Parcel in) {
        movieId = in.readInt();
        movieOverview = in.readString();
        moviePosterPath = in.readString();
        movieTitle = in.readString();
        movieUserRating = in.readString();
        movieReleaseDate = in.readString();
        movieBackdropPath = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MovieModel> CREATOR
            = new Parcelable.Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(movieOverview);
        dest.writeString(moviePosterPath);
        dest.writeString(movieTitle);
        dest.writeString(movieUserRating);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieBackdropPath);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    /**
     *
     * @return movieId
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     *
     * @param movieId movie ID
     */
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

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

    public String getMovieBackdropPath() {
        return movieBackdropPath;
    }

    public void setMovieBackdropPath(String movieBackdropPath) {
        this.movieBackdropPath = movieBackdropPath;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
