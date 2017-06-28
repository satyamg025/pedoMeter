package com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by satyam on 23/6/17.
 */

public interface LoginRequest {
    @GET("Optimus_cont/login")
    Call<LoginDataPOJO> requestResponse(@Query("fcm") String fcm);
}
