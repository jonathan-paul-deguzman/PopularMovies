package com.example.jpdeguzman.popularmovies.data.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.data.models.ReviewModel;
import com.example.jpdeguzman.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View Holder for movie reviews
 */

public class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_review_author) TextView reviewAuthorTextView;

    @BindView(R.id.tv_review_content) TextView reviewContentTextView;

    public MovieReviewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
