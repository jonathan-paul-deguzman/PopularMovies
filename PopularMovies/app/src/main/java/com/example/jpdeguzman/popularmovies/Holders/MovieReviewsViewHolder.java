package com.example.jpdeguzman.popularmovies.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.ReviewModel;
import com.example.jpdeguzman.popularmovies.R;

import java.util.ArrayList;

/**
 * View Holder for movie reviews
 */

public class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

    private TextView reviewAuthorTextView;

    private TextView reviewContentTextView;

    public MovieReviewsViewHolder(View itemView) {
        super(itemView);
        reviewAuthorTextView = itemView.findViewById(R.id.tv_review_author);
        reviewContentTextView = itemView.findViewById(R.id.tv_review_content);
    }

    public void configureReviewsViewHolder(
            MovieReviewsViewHolder reviewsHolder, ArrayList<ReviewModel> reviewItemList, int position) {
        ReviewModel review = reviewItemList.get(position);
        if (review != null) {
            reviewsHolder.reviewAuthorTextView.setText(review.getReviewAuthor());
            reviewsHolder.reviewContentTextView.setText(review.getReviewContent());
        }
    }
}
