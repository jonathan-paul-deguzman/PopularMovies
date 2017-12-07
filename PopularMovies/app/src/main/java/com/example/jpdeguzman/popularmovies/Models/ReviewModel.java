package com.example.jpdeguzman.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

/**
 * POJO class to represent a Review object.
 */

public class ReviewModel {

    @SerializedName("author")
    String reviewAuthor;

    @SerializedName("content")
    String reviewContent;

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
