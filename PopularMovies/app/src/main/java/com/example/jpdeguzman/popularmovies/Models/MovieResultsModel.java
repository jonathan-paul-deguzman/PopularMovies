package com.example.jpdeguzman.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieResultsModel {
    @SerializedName("results")
    private ArrayList<MovieModel> movies = new ArrayList<>();

    /**
     *
     * @return movies
     */
    public ArrayList<MovieModel> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies list of {@link MovieModel} objects
     */
    public void setMovies(ArrayList<MovieModel> movies) {
        this.movies = movies;
    }
}
