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
 *  Adapter for creating the favorite movies layout
 */
public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder> {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private Context mContext;

    private ArrayList<MovieModel> mFavoriteMovieList;

    private FavoriteMovieAdapterOnClickHandler mClickHandler;

    public interface FavoriteMovieAdapterOnClickHandler {
        void OnFavoriteItemClick(int position);
    }

    public FavoriteMovieAdapter(Context context, FavoriteMovieAdapterOnClickHandler clickHandler,
                                ArrayList<MovieModel> favoriteMovieList) {
        mClickHandler = clickHandler;
        mContext = context;
        mFavoriteMovieList = favoriteMovieList;
    }

    @Override
    public FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_poster_item, parent, false);
        return new FavoriteMovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder holder, final int position) {
        final MovieModel currentMovie = mFavoriteMovieList.get(position);
        if (currentMovie != null) {
            String moviePosterPath = currentMovie.getMoviePosterPath();
            Picasso.with(mContext)
                    .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                    .into(holder.moviePosterImageView);
        }

        holder.moviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.OnFavoriteItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavoriteMovieList.size();
    }

    class FavoriteMovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePosterImageView;

        public FavoriteMovieAdapterViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
