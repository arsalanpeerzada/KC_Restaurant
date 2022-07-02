package com.example.smartrestaurant.model.network_error_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 11/6/2017.
 */

public class _401ErrorModel {

    @SerializedName("error")
    private String error;

    @SerializedName("error_message")
    private String message;

    @SerializedName("error_no")
    private Integer error_no;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getError_no() {
        return error_no;
    }

    public void setError_no(Integer error_no) {
        this.error_no = error_no;
    }
}
