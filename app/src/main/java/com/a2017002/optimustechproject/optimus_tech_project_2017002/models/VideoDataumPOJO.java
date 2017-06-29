package com.a2017002.optimustechproject.optimus_tech_project_2017002.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 29/6/17.
 */

public class VideoDataumPOJO {
    @SerializedName("name")
    @Expose
    private List<String> name = null;
    @SerializedName("video_by")
    @Expose
    private List<String> videoBy = null;
    @SerializedName("fetch_url")
    @Expose
    private List<String> fetchUrl = null;
    @SerializedName("background")
    @Expose
    private List<String> background = null;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getVideoBy() {
        return videoBy;
    }

    public void setVideoBy(List<String> videoBy) {
        this.videoBy = videoBy;
    }

    public List<String> getFetchUrl() {
        return fetchUrl;
    }

    public void setFetchUrl(List<String> fetchUrl) {
        this.fetchUrl = fetchUrl;
    }

    public List<String> getBackground() {
        return background;
    }

    public void setBackground(List<String> background) {
        this.background = background;
    }
}
