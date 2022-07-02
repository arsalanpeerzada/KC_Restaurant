package com.example.smartrestaurant.model.kitchen;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 1/18/2018.
 */

public class PlacedOrderSingleItem {
    @SerializedName("od_main_id")
    
    private String odMainId;
    @SerializedName("master_id")
    
    private String masterId;
    @SerializedName("item_trans_id")
    
    private String itemTransId;
    @SerializedName("item_qty")
    
    private String itemQty;
    @SerializedName("item_cooking_status")
    
    private String itemCookingStatus;
    @SerializedName("item_taking_time")
    
    private String itemTakingTime;
    @SerializedName("item_dispatch_time")
    
    private String itemDispatchTime;
    @SerializedName("item_serve_time")
    
    private String itemServeTime;
    @SerializedName("item_main_id")
    
    private String itemMainId;
    @SerializedName("cat_trans_id")
    
    private String catTransId;
    @SerializedName("ktc_trans_id")
    
    private String ktcTransId;
    @SerializedName("item_name")
    
    private String itemName;
    @SerializedName("item_price")
    
    private String itemPrice;
    @SerializedName("item_sale_price")
    
    private String itemSalePrice;
    @SerializedName("item_image")
    
    private String itemImage;
    @SerializedName("item_unit")
    
    private String itemUnit;
    @SerializedName("item_sync_date")
    
    private String itemSyncDate;
    @SerializedName("item_avail")
    
    private String itemAvail;

    @SerializedName("remarks")
    private String remarks;

    private int isSelected;
    private String isNewAdded = "0";

    public String getIsNewAdded() {
        return isNewAdded;
    }

    public void setIsNewAdded(String isNewAdded) {
        this.isNewAdded = isNewAdded;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getOdMainId() {
        return odMainId;
    }

    public void setOdMainId(String odMainId) {
        this.odMainId = odMainId;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getItemTransId() {
        return itemTransId;
    }

    public void setItemTransId(String itemTransId) {
        this.itemTransId = itemTransId;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemCookingStatus() {
        return itemCookingStatus;
    }

    public void setItemCookingStatus(String itemCookingStatus) {
        this.itemCookingStatus = itemCookingStatus;
    }

    public String getItemTakingTime() {
        return itemTakingTime;
    }

    public void setItemTakingTime(String itemTakingTime) {
        this.itemTakingTime = itemTakingTime;
    }

    public String getItemDispatchTime() {
        return itemDispatchTime;
    }

    public void setItemDispatchTime(String itemDispatchTime) {
        this.itemDispatchTime = itemDispatchTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getItemServeTime() {
        return itemServeTime;
    }

    public void setItemServeTime(String itemServeTime) {
        this.itemServeTime = itemServeTime;
    }

    public String getItemMainId() {
        return itemMainId;
    }

    public void setItemMainId(String itemTransId) {
        this.itemMainId = itemTransId;
    }

    public String getCatTransId() {
        return catTransId;
    }

    public void setCatTransId(String catTransId) {
        this.catTransId = catTransId;
    }

    public String getKtcTransId() {
        return ktcTransId;
    }

    public void setKtcTransId(String ktcTransId) {
        this.ktcTransId = ktcTransId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemSalePrice() {
        return itemSalePrice;
    }

    public void setItemSalePrice(String itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemSyncDate() {
        return itemSyncDate;
    }

    public void setItemSyncDate(String itemSyncDate) {
        this.itemSyncDate = itemSyncDate;
    }

    public String getItemAvail() {
        return itemAvail;
    }

    public void setItemAvail(String itemAvail) {
        this.itemAvail = itemAvail;
    }
}
