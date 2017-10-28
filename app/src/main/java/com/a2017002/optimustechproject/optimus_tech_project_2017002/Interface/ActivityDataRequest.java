package com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.ActivityDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 4/7/17.
 */

public interface ActivityDataRequest {
    @GET("Optimus_cont/activity_detail")
    Call<ActivityDataPOJO> requestResponse();
}
