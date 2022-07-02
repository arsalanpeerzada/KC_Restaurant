package com.example.smartrestaurant.model.kitchen;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell 5521 on 1/13/2018.
 */

public class KitchenNameModel {

    @SerializedName("ktc_trans_id")
    private String ktcTransId;
    
    @SerializedName("cat_main_id")
    private String ktcMainId;
    
    @SerializedName("rest_id")
    private String restTransId;
    
    @SerializedName("cat_name")
    private String ktcName;
    
    @SerializedName("cat_desciption")
    private String ktcDesc;
    
    @SerializedName("ktc_status")
    private String ktcStatus;
    
    @SerializedName("cat_system_date")
    private String ktcSystemDate;
    
    @SerializedName("cat_sync_date")
    private String ktcSyncDate;

    private int isSelected = 0;

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    @SerializedName("item_list")
    private List<KitchenItemListModel> itemList = null;

    public String getKtcTransId() {
        return ktcTransId;
    }

    public void setKtcTransId(String ktcTransId) {
        this.ktcTransId = ktcTransId;
    }

    public String getKtcMainId() {
        return ktcMainId;
    }

    public void setKtcMainId(String ktcMainId) {
        this.ktcMainId = ktcMainId;
    }

    public String getRestTransId() {
        return restTransId;
    }

    public void setRestTransId(String restTransId) {
        this.restTransId = restTransId;
    }

    public String getKtcName() {
        return ktcName;
    }

    public void setKtcName(String ktcName) {
        this.ktcName = ktcName;
    }

    public String getKtcDesc() {
        return ktcDesc;
    }

    public void setKtcDesc(String ktcDesc) {
        this.ktcDesc = ktcDesc;
    }

    public String getKtcStatus() {
        return ktcStatus;
    }

    public void setKtcStatus(String ktcStatus) {
        this.ktcStatus = ktcStatus;
    }

    public String getKtcSystemDate() {
        return ktcSystemDate;
    }

    public void setKtcSystemDate(String ktcSystemDate) {
        this.ktcSystemDate = ktcSystemDate;
    }

    public String getKtcSyncDate() {
        return ktcSyncDate;
    }

    public void setKtcSyncDate(String ktcSyncDate) {
        this.ktcSyncDate = ktcSyncDate;
    }

    public List<KitchenItemListModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<KitchenItemListModel> itemList) {
        this.itemList = itemList;
    }
}
