package com.example.smartrestaurant.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell 5521 on 4/11/2018.
 */

public class LocationModel {
    @SerializedName("data")
    private List<LocationData> data = null;

    public List<LocationData> getData() {
        return data;
    }

    public void setData(List<LocationData> data) {
        this.data = data;
    }

    public static class LocationData{
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("location_area")
        private String locationArea;

        @SerializedName("address")
        private String address;

        @SerializedName("branch")
        private String branch;

        @SerializedName("rest_status")
        private String restStatus;

        @SerializedName("rest_desc")
        private String restDesc;


        @SerializedName("res_main_id")
        private String res_main_id;

        public String getRes_main_id() {
            return res_main_id;
        }

        public void setRes_main_id(String res_main_id) {
            this.res_main_id = res_main_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocationArea() {
            return locationArea;
        }

        public void setLocationArea(String locationArea) {
            this.locationArea = locationArea;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getRestStatus() {
            return restStatus;
        }

        public void setRestStatus(String restStatus) {
            this.restStatus = restStatus;
        }

        public String getRestDesc() {
            return restDesc;
        }

        public void setRestDesc(String restDesc) {
            this.restDesc = restDesc;
        }

    }
}
