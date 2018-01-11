package com.example.jpdeguzman.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Responsible for representing an ArrayList of Video objects.
 */
public class VideoResultsModel {

    @SerializedName("results")
    private ArrayList<VideoModel> videos = new ArrayList<>();

    public ArrayList<VideoModel> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<VideoModel> videos) {
        this.videos = videos;
    }
}
