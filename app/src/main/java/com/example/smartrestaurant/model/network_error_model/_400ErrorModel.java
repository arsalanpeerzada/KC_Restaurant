package com.example.smartrestaurant.model.network_error_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 11/6/2017.
 */

public class _400ErrorModel {

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("hint")
    private String hint;

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

    public String getHint() {
        return hint;
    }

    public void setHint(String message) {
        this.hint = message;
    }

}
