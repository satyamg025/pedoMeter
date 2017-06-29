package com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.VideoDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by satyam on 29/6/17.
 */

public interface VideoRequest {
    @GET("Optimus_cont/video_details")
    Call<VideoDataPOJO> requestResponse();
}
