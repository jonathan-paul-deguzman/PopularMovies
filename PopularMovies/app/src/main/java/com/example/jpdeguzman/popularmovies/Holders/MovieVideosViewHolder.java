package com.example.jpdeguzman.popularmovies.Holders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.VideoModel;
import com.example.jpdeguzman.popularmovies.R;

import java.util.ArrayList;

/**
 * View holder for movie videos
 */

public class MovieVideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    private TextView videoNameTextView;

    private String videoKey;

    public MovieVideosViewHolder(View itemView) {
        super(itemView);
        videoNameTextView = itemView.findViewById(R.id.tv_video_name);
        itemView.setOnClickListener(this);
    }

    public void configureVideosViewHolder(
            MovieVideosViewHolder videosHolder, ArrayList<VideoModel> videoItemList, int position) {
        VideoModel video = videoItemList.get(position);
        if (video != null) {
            videosHolder.videoNameTextView.setText(video.getVideoName());
            videoKey = video.getVideoKey();
        }
    }

    /**
     * Sends an implicit intent to watch the youtube video of the selected video key.
     *
     * @param v view from within the holder that was clicked
     */
    @Override
    public void onClick(View v) {
        Intent launchViewingOptionsIntent = new Intent(Intent.ACTION_VIEW);
        launchViewingOptionsIntent.setData(Uri.parse(YOUTUBE_BASE_URL + videoKey));
        if (launchViewingOptionsIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            v.getContext().startActivity(launchViewingOptionsIntent);
        }
    }
}

