package com.example.jpdeguzman.popularmovies.data.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jpdeguzman.popularmovies.data.constants.ImageConstants;
import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.utils.ApplicationContext;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *  Adapter for creating the favorite movies layout
 */
public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder> {

    private Context mContext;

    private ArrayList<MovieModel> mFavoriteMovieList;

    private FavoriteMovieAdapterOnClickHandler mClickHandler;

    public interface FavoriteMovieAdapterOnClickHandler {
        void OnFavoriteItemClick(MovieModel favoriteMovie);
    }

    public FavoriteMovieAdapter(ArrayList<MovieModel> favoriteMovieList,
                                FavoriteMovieAdapterOnClickHandler clickHandler) {
        mContext = ApplicationContext.getContext();
        mFavoriteMovieList = favoriteMovieList;
        mClickHandler = clickHandler;
    }

    @Override
    public FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_poster_item, parent, false);
        return new FavoriteMovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieAdapterViewHolder holder, final int position) {
        final MovieModel currentMovie = mFavoriteMovieList.get(position);
        if (currentMovie != null) {
            String moviePosterPath = currentMovie.getMoviePosterPath();
            Picasso.with(mContext)
                    .load(ImageConstants.IMAGE_BASE_URL + ImageConstants.IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                    .into(holder.moviePosterImageView);
        }

        holder.moviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.OnFavoriteItemClick(currentMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mFavoriteMovieList != null) ? mFavoriteMovieList.size() : 0;
    }

    class FavoriteMovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePosterImageView;

        public FavoriteMovieAdapterViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
