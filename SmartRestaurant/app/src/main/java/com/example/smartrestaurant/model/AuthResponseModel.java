package com.example.smartrestaurant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 1/6/2018.
 */

public class AuthResponseModel {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("username")
    private String username;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private Integer status;

    @SerializedName("session_id")
    private String sessionId;

    @SerializedName("location")
    private String location;

    @SerializedName("rest_id")
    private String rest_id;


    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
