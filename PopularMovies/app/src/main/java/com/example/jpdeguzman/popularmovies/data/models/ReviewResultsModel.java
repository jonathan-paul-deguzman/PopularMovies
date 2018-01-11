package com.example.jpdeguzman.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Responsible for representing an ArrayList of Review objects.
 */

public class ReviewResultsModel {
    @SerializedName("results")
    private ArrayList<ReviewModel> reviews = new ArrayList<>();

    public ArrayList<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewModel> reviews) {
        this.reviews = reviews;
    }
}
