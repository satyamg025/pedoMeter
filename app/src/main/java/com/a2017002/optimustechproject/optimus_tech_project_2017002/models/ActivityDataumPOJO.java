package com.a2017002.optimustechproject.optimus_tech_project_2017002.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 4/7/17.
 */

public class ActivityDataumPOJO {
    @SerializedName("distance")
    @Expose
    private List<List<String>> distance = null;
    @SerializedName("speed")
    @Expose
    private List<List<String>> speed = null;
    @SerializedName("time")
    @Expose
    private List<List<String>> time = null;
    @SerializedName("date")
    @Expose
    private List<String> date = null;

    public List<List<String>> getDistance() {
        return distance;
    }

    public void setDistance(List<List<String>> distance) {
        this.distance = distance;
    }

    public List<List<String>> getSpeed() {
        return speed;
    }

    public void setSpeed(List<List<String>> speed) {
        this.speed = speed;
    }

    public List<List<String>> getTime() {
        return time;
    }

    public void setTime(List<List<String>> time) {
        this.time = time;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }


}
