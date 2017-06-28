package com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.RegDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by satyam on 23/6/17.
 */

public interface RegistrationRequest {
    @GET("Optimus_cont/registration")
    Call<RegDataPOJO> requestResponse(@Query("first_name") String first_name, @Query("last_name") String last_name, @Query("dob") String dob, @Query("gender") String gender, @Query("mobile") String mobile, @Query("username") String username, @Query("password") String password, @Query("fcm") String fcm, @Query("height") String height, @Query("weight") String weight);
}
