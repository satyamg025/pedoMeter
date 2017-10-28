package com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.ActivityDataPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.StoreActivityPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 4/7/17.
 */

public interface StoreActivityRequest {
    @GET("Optimus_cont/store_activity")
    Call<StoreActivityPOJO> requestResponse(@Query("distance") String distance, @Query("time") String time, @Query("speed") String speed, @Query("date") String date);

}
