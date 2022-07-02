package com.example.smartrestaurant.model;

import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 1/21/2018.
 */

public class NotificationItemModel implements RecyclerViewBaseAdapter.AdapterDataType {


    @SerializedName("od_main_id")

    private String odmainID;


    @SerializedName("item_name")

    private String itemName;
    @SerializedName("item_id")

    private String itemId;
    @SerializedName("item_qty")

    private String itemQty;
    @SerializedName("ktc_name")

    private String ktcName;
    @SerializedName("ktc_id")

    private String ktcId;
    @SerializedName("table_no")

    private String tableNo;
    @SerializedName("order_no")

    private String orderNo;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getKtcName() {
        return ktcName;
    }

    public void setKtcName(String ktcName) {
        this.ktcName = ktcName;
    }

    public String getKtcId() {
        return ktcId;
    }

    public void setKtcId(String ktcId) {
        this.ktcId = ktcId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOdmainID() {
        return odmainID;
    }

    public void setOdmainID(String odmainID) {
        this.odmainID = odmainID;
    }

    @Override
    public int getItemViewType() {
        return Constant.LIST_TYPE_NOTIFICATION_ITEM;
    }
}
