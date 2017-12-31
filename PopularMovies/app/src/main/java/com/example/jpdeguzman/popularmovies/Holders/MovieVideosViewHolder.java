package com.example.jpdeguzman.popularmovies.Holders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.VideoModel;
import com.example.jpdeguzman.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * View holder for movie videos
 */

public class MovieVideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    private static final String YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";

    private static final String YOUTUBE_THUMBNAIL_END_URL = "/0.jpg";

    @BindView(R.id.iv_video_thumbnail) ImageView videoThumbnailImageView;

    @BindView(R.id.tv_video_name) TextView videoNameTextView;

    private String videoKey;

    private final Context context;

    public MovieVideosViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void configureVideosViewHolder(
            MovieVideosViewHolder videosHolder, ArrayList<VideoModel> videoItemList, int position) {
        VideoModel video = videoItemList.get(position);
        if (video != null) {
            Picasso.with(context)
                    .load(YOUTUBE_THUMBNAIL_BASE_URL + video.getVideoKey() + YOUTUBE_THUMBNAIL_END_URL)
                    .into(videoThumbnailImageView);
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

