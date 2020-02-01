package com.em_projects.shift_timer.model.remote.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.em_projects.shift_timer.model.DataWrapper;
import com.em_projects.shift_timer.model.remote.connector.ApiController;
import com.em_projects.shift_timer.model.remote.connector.RemoteUserApi;
import com.em_projects.shift_timer.model.remote.connector.users.LoginRequest;
import com.em_projects.shift_timer.model.remote.connector.users.LoginResponse;
import com.em_projects.shift_timer.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";

    public LiveData<DataWrapper<LoginResponse>> login(String bearer, final LoginRequest request) {
        final MutableLiveData<DataWrapper<LoginResponse>> liveData = new MutableLiveData<>();
        final DataWrapper<LoginResponse> dataWrapper = new DataWrapper<>();

        RemoteUserApi userApi = ApiController.createService(RemoteUserApi.class);
        userApi.login(bearer, request).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public Call<UpdateUserVersionResponse> updateUserVersion(String bearer, UpdateUserVersionRequest request) {
        RemoteUserApi userApi = ApiController.createService(RemoteUserApi.class);
        return userApi.updateUserAppVersionInServer(bearer, request);
    }

    public LiveData<DataWrapper<BaseResponse>> deleteUser(String bearer) {
        final MutableLiveData<DataWrapper<BaseResponse>> liveData = new MutableLiveData<>();
        final DataWrapper<BaseResponse> dataWrapper = new DataWrapper<>();

        RemoteUserApi userApi = ApiController.createService(RemoteUserApi.class);
        userApi.deleteUser(bearer).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }
}
