package com.em_projects.shift_timer.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.em_projects.shift_timer.BuildConfig;
import com.em_projects.shift_timer.model.DataWrapper;
import com.em_projects.shift_timer.model.remote.connector.users.LoginRequest;
import com.em_projects.shift_timer.model.remote.connector.users.LoginResponse;
import com.em_projects.shift_timer.model.remote.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    public UserViewModel(Application application) {
        super(application);

        repository = new UserRepository();
    }

    public LiveData<DataWrapper<LoginResponse>> login(String empCod, String empTag, String empPwd) {
        LoginRequest request = new LoginRequest(empCod, empTag, empPwd);
        return repository.login(BuildConfig.APP_BEARER, request);
    }

}
