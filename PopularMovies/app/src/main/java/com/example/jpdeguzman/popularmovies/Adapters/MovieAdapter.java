package com.example.jpdeguzman.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private Context mContext;
    private ArrayList<MovieModel> mMovieList;

    public MovieAdapter(Context context, ArrayList<MovieModel> movieList) {
        super(context, 0, movieList);
        mContext = context;
        mMovieList = movieList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MovieModel currentMovie = (MovieModel) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.movie_poster_item, parent, false);
        }

        String moviePosterPath = currentMovie.getMoviePosterPath();
        ImageView imageView = convertView.findViewById(R.id.iv_movie_poster);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext)
                .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                .into(imageView);

        return convertView;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
