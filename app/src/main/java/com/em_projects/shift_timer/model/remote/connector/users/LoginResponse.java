package com.em_projects.shift_timer.model.remote.connector.users;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("result_code")
    int resultCode;

    @SerializedName("jwt_token")
    String jwt;

    @SerializedName("error_message")
    String errorMessage;

    @SerializedName("data_exists")
    boolean dataExist;

    public LoginResponse(int resultCode, String jwt, String errorMessage, boolean dataExist) {
        this.resultCode = resultCode;
        this.jwt = jwt;
        this.errorMessage = errorMessage;
        this.dataExist = dataExist;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getJwt() {
        return jwt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isDataExist() {
        return dataExist;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "resultCode=" + resultCode +
                ", jwt='" + jwt + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", dataExist=" + dataExist +
                '}';
    }
}
