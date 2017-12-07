package com.example.jpdeguzman.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

/**
 * POJO class to represent a video object.
 */

public class VideoModel {

    @SerializedName("key")
    private String videoKey;

    @SerializedName("name")
    private String videoName;

    @SerializedName("type")
    private String videoType;

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
