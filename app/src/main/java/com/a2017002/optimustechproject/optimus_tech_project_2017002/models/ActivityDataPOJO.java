package com.a2017002.optimustechproject.optimus_tech_project_2017002.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 4/7/17.
 */

public class ActivityDataPOJO {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ActivityDataumPOJO data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ActivityDataumPOJO getData() {
        return data;
    }

    public void setData(ActivityDataumPOJO data) {
        this.data = data;
    }

}
