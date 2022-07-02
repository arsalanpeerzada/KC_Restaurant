package com.example.smartrestaurant.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell 5521 on 1/22/2018.
 */

public class NotificationResponseModel {

    @SerializedName("data")
    private List<NotificationItemModel> data = null;

    public List<NotificationItemModel> getData() {
        return data;
    }

    public void setData(List<NotificationItemModel> data) {
        this.data = data;
    }
}
