package com.em_projects.shift_timer.model.remote;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("result_code")
    int resultCode;

    @SerializedName("error_message")
    String message;

    public BaseResponse(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}
