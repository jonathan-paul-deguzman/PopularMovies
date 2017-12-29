package com.example.jpdeguzman.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *  Adapter for creating the popular and top-rated movies layout
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private final Context mContext;

    private ArrayList<MovieModel> mMovieList;

    private MovieAdapterOnClickHandler mClickListener;

    public interface MovieAdapterOnClickHandler {
        void OnItemClick(int position);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler listener, ArrayList<MovieModel> movieList) {
        mContext = context;
        mClickListener = listener;
        mMovieList = movieList;
    }

    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_poster_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, final int position) {
        final MovieModel currentMovie = mMovieList.get(position);
        if (currentMovie != null) {
            String moviePosterPath = currentMovie.getMoviePosterPath();
            Picasso.with(mContext)
                    .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                    .into(holder.moviePosterImageView);
        }

        holder.moviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
