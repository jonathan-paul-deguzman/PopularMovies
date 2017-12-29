package com.example.jpdeguzman.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jpdeguzman.popularmovies.Data.FavoriteMoviesContract;
import com.example.jpdeguzman.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 *  Adapter for creating the favorite movies layout
 */
public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder> {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private Context mContext;

    private Cursor mCursor;

    private FavoriteMovieAdapterOnClickHandler mClickHandler;

    public interface FavoriteMovieAdapterOnClickHandler {
        void OnFavoriteItemClick(int position);
    }

    public FavoriteMovieAdapter(Context context, FavoriteMovieAdapterOnClickHandler clickHandler, Cursor cursor) {
        mClickHandler = clickHandler;
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_poster_item, parent, false);
        return new FavoriteMovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String moviePosterPath = mCursor.getString(mCursor.getColumnIndex(
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH));
        Picasso.with(mContext)
                .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                .into(holder.moviePosterImageView);

        holder.moviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.OnFavoriteItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    class FavoriteMovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePosterImageView;

        public FavoriteMovieAdapterViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
