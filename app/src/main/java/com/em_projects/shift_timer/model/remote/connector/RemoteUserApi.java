package com.em_projects.shift_timer.model.remote.connector;


import com.em_projects.shift_timer.model.remote.connector.users.LoginRequest;
import com.em_projects.shift_timer.model.remote.connector.users.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RemoteUserApi {

    @POST("user/login")
    Call<LoginResponse> login(@Header("AuthenticationToken") String bearer, @Body LoginRequest request);
}
