package com.em_projects.shift_timer.model.remote.connector.users;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("code")
    String employee_code;

    @SerializedName("password")
    String password;

    @SerializedName("tag")
    String employee_Tag;

    public LoginRequest(String empCod, String empTag, String empPwd) {
        employee_code = empCod;
        employee_Tag = empTag;
        password = empPwd;
    }

    public String getEmployee_code() {
        return employee_code;
    }

    public String getPassword() {
        return password;
    }

    public String getEmployee_Tag() {
        return employee_Tag;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "employee_code='" + employee_code + '\'' +
                ", password='" + password + '\'' +
                ", employee_Tag='" + employee_Tag + '\'' +
                '}';
    }
}
