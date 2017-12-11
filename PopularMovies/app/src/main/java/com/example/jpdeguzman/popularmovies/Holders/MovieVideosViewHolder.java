package com.example.jpdeguzman.popularmovies.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.R;

/**
 * View holder for movie videos
 */

public class MovieVideosViewHolder extends RecyclerView.ViewHolder {

    TextView videoNameTextView;

    public MovieVideosViewHolder(View itemView) {
        super(itemView);
        videoNameTextView = itemView.findViewById(R.id.tv_video_name);
    }

    public TextView getVideoNameTextView() {
        return videoNameTextView;
    }

    public void setVideoNameTextView(TextView mVideoNameTextView) {
        this.videoNameTextView = mVideoNameTextView;
    }
}

