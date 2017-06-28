package com.a2017002.optimustechproject.optimus_tech_project_2017002.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 28/6/17.
 */

public class ProfileUpdatePOJO {

    @SerializedName("error")
    @Expose
    private Boolean errror;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private LoginDataumPOJO data;

    public Boolean getErrror() {
        return errror;
    }

    public void setErrror(Boolean errror) {
        this.errror = errror;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginDataumPOJO getData() {
        return data;
    }

    public void setData(LoginDataumPOJO data) {
        this.data = data;
    }

}
