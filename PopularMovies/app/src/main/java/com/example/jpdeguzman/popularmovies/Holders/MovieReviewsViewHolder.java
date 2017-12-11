package com.example.jpdeguzman.popularmovies.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.R;

/**
 * View Holder for movie reviews
 */

public class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

    TextView reviewAuthorTextView;

    TextView reviewContentTextView;

    public MovieReviewsViewHolder(View itemView) {
        super(itemView);
        reviewAuthorTextView = itemView.findViewById(R.id.tv_review_author);
        reviewContentTextView = itemView.findViewById(R.id.tv_review_content);
    }

    public TextView getReviewAuthorTextView() {
        return reviewAuthorTextView;
    }

    public void setReviewAuthorTextView(TextView mReviewAuthorTextView) {
        this.reviewAuthorTextView = mReviewAuthorTextView;
    }

    public TextView getReviewContentTextView() {
        return reviewContentTextView;
    }

    public void setReviewContentTextView(TextView mReviewContentTextView) {
        this.reviewContentTextView = mReviewContentTextView;
    }
}
