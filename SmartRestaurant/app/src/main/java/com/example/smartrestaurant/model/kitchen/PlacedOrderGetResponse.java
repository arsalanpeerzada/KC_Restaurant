package com.example.smartrestaurant.model.kitchen;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell 5521 on 1/20/2018.
 */

public class PlacedOrderGetResponse {
    @SerializedName("data")
    private List<PlacedOrderModel> data = null;

    public List<PlacedOrderModel> getData() {
        return data;
    }

    public void setData(List<PlacedOrderModel> data) {
        this.data = data;
    }
}
