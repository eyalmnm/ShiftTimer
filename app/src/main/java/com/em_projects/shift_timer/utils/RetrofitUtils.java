package com.em_projects.shift_timer.utils;


import retrofit2.Response;


public class RetrofitUtils {

    public static String handleErrorResponse(Response response) {
        return response.raw().message();
    }

}
