package com.example.jpdeguzman.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpdeguzman.popularmovies.Holders.MovieDetailsViewHolder;
import com.example.jpdeguzman.popularmovies.Holders.MovieReviewsViewHolder;
import com.example.jpdeguzman.popularmovies.Holders.MovieVideosViewHolder;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.ReviewModel;
import com.example.jpdeguzman.popularmovies.Models.VideoModel;
import com.example.jpdeguzman.popularmovies.R;

import java.util.ArrayList;

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MOVIE_DETAILS_ID = 0;

    private static final int MOVIE_VIDEOS_ID = 1;

    private static final int MOVIE_REVIEWS_ID = 2;

    private ArrayList<MovieModel> detailItemList;

    private ArrayList<VideoModel> videoItemList;

    private ArrayList<ReviewModel> reviewItemList;

    private final Context context;

    public MovieDetailsAdapter(Context context, ArrayList<MovieModel> detailItemList,
                               ArrayList<VideoModel> videoItemList, ArrayList<ReviewModel> reviewItemList) {
        this.context = context;
        this.detailItemList = detailItemList;
        this.videoItemList = videoItemList;
        this.reviewItemList = reviewItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MOVIE_DETAILS_ID:
                View detailsView = inflater.inflate(R.layout.movie_details, parent, false);
                viewHolder = new MovieDetailsViewHolder(context, detailsView);
                break;
            case MOVIE_VIDEOS_ID:
                View videosView = inflater.inflate(R.layout.movie_details_videos, parent, false);
                viewHolder = new MovieVideosViewHolder(context, videosView);
                break;
            case MOVIE_REVIEWS_ID:
                View reviewsView = inflater.inflate(R.layout.movie_details_reviews, parent, false);
                viewHolder = new MovieReviewsViewHolder(reviewsView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MOVIE_DETAILS_ID:
                MovieDetailsViewHolder movieDetailsViewHolder = (MovieDetailsViewHolder) holder;
                movieDetailsViewHolder.configureDetailsViewHolder(movieDetailsViewHolder,
                        detailItemList, position);
                break;
            case MOVIE_VIDEOS_ID:
                MovieVideosViewHolder movieVideosViewHolder = (MovieVideosViewHolder) holder;
                movieVideosViewHolder.configureVideosViewHolder(movieVideosViewHolder,
                        videoItemList, position - detailItemList.size());
                break;
            case MOVIE_REVIEWS_ID:
                MovieReviewsViewHolder movieReviewsViewHolder = (MovieReviewsViewHolder) holder;
                movieReviewsViewHolder.configureReviewsViewHolder(movieReviewsViewHolder,
                        reviewItemList, position - detailItemList.size() - videoItemList.size());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MOVIE_DETAILS_ID;
        } else if (position <= videoItemList.size()) {
            return MOVIE_VIDEOS_ID;
        } else {
            return MOVIE_REVIEWS_ID;
        }
    }

    @Override
    public int getItemCount() {
        return detailItemList.size() + videoItemList.size() + reviewItemList.size();
    }
}
