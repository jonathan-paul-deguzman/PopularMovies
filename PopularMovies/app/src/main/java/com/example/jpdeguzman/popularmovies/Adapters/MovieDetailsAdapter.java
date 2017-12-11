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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jpdeguzman on 12/10/17.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private ArrayList<?> movieItem;

    private final Context context;

    private static final int MOVIE_DETAILS_ID = 0;

    private static final int MOVIE_VIDEOS_ID = 1;

    private static final int MOVIE_REVIEWS_ID = 2;

    public MovieDetailsAdapter(Context context, ArrayList<?> movieItem) {
        this.context = context;
        this.movieItem = movieItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MOVIE_DETAILS_ID:
                View detailsView = inflater.inflate(R.layout.movie_details_information, parent, false);
                viewHolder = new MovieDetailsViewHolder(detailsView);
                break;
            case MOVIE_VIDEOS_ID:
                View videosView = inflater.inflate(R.layout.movie_details_videos, parent, false);
                viewHolder = new MovieVideosViewHolder(videosView);
                break;
            case MOVIE_REVIEWS_ID:
                View reviewsView = inflater.inflate(R.layout.movie_details_reviews, parent, false);
                viewHolder = new MovieReviewsViewHolder(reviewsView);
                break;
            default:
                View newView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new MovieDetailsViewHolder(newView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MOVIE_DETAILS_ID:
                MovieDetailsViewHolder movieDetailsViewHolder = (MovieDetailsViewHolder) holder;
                configureDetailsViewHolder(movieDetailsViewHolder, position);
                break;
            case MOVIE_VIDEOS_ID:
                MovieVideosViewHolder movieVideosViewHolder = (MovieVideosViewHolder) holder;
                configureVideosViewHolder(movieVideosViewHolder, position);
                break;
            case MOVIE_REVIEWS_ID:
                MovieReviewsViewHolder movieReviewsViewHolder = (MovieReviewsViewHolder) holder;
                configureReviewsViewHolder(movieReviewsViewHolder, position);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (movieItem.get(position) instanceof MovieModel) {
            return MOVIE_DETAILS_ID;
        } else if (movieItem.get(position) instanceof VideoModel) {
            return MOVIE_VIDEOS_ID;
        } else if (movieItem.get(position) instanceof ReviewModel) {
            return MOVIE_REVIEWS_ID;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return this.movieItem.size();
    }

    private void configureDetailsViewHolder(MovieDetailsViewHolder detailsHolder, int position) {
        MovieModel movieDetails = (MovieModel) movieItem.get(position);
        if (movieDetails != null) {
            String moviePosterPath = movieDetails.getMoviePosterPath();
            Picasso.with(context)
                    .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                    .into(detailsHolder.getMoviePosterImageView());
            detailsHolder.getMovieTitleTextView().setText(movieDetails.getMovieTitle());
            detailsHolder.getMovieReleaseDateTextView().setText(movieDetails.getMovieReleaseDate());
            detailsHolder.getMovieUserRatingTextView().setText(movieDetails.getMovieUserRating());
            detailsHolder.getMovieOverviewTextView().setText(movieDetails.getMovieOverview());
        }
    }

    private void configureVideosViewHolder(MovieVideosViewHolder videosHolder, int position) {
        VideoModel video = (VideoModel) movieItem.get(position);
        if (video != null) {
            videosHolder.getVideoNameTextView().setText(video.getVideoName());
        }
    }

    private void configureReviewsViewHolder(MovieReviewsViewHolder reviewsHolder, int position) {
        ReviewModel review = (ReviewModel) movieItem.get(position);
        if (review != null) {
            reviewsHolder.getReviewAuthorTextView().setText(review.getReviewAuthor());
            reviewsHolder.getReviewContentTextView().setText(review.getReviewContent());
        }
    }
}
