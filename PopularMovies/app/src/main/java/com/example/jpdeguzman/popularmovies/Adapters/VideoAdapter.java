package com.example.jpdeguzman.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Models.VideoModel;
import com.example.jpdeguzman.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by jpdeguzman on 12/7/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private ArrayList<VideoModel> mVideos;

    public VideoAdapter(ArrayList<VideoModel> videos) {
        mVideos = videos;
    }

    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_details_videos, parent, false);
        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder holder, int position) {
        holder.videoNameTextView.setText(mVideos.get(position).getVideoName());
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView videoNameTextView;

        public VideoAdapterViewHolder(View itemView) {
            super(itemView);
            videoNameTextView = itemView.findViewById(R.id.tv_video_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
