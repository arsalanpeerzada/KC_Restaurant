package com.example.smartrestaurant.model.kitchen;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell 5521 on 1/13/2018.
 */

public class KitchenDataModel {

    @SerializedName("data")
    private List<KitchenNameModel> data = null;

    public List<KitchenNameModel> getData() {
        return data;
    }

    public void setData(List<KitchenNameModel> data) {
        this.data = data;
    }
}
