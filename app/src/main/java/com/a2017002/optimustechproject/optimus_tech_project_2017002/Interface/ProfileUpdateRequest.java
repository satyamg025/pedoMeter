package com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.ProfileUpdatePOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.RegDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by satyam on 28/6/17.
 */

public interface ProfileUpdateRequest {
    @GET("Optimus_cont/profile_update")
    Call<ProfileUpdatePOJO> requestResponse(@Query("first_name") String first_name, @Query("last_name") String last_name, @Query("dob") String dob, @Query("gender") String gender, @Query("mobile") String mobile, @Query("height") String height, @Query("weight") String weight);

}
