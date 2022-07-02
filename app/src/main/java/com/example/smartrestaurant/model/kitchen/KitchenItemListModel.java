package com.example.smartrestaurant.model.kitchen;

import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 1/13/2018.
 */

public class KitchenItemListModel implements RecyclerViewBaseAdapter.AdapterDataType  {
    @SerializedName("item_trans_id")
    private String itemTransId;
    
    @SerializedName("item_main_id")
    private String itemMainId;
    
    @SerializedName("cat_trans_id")
    private String catTransId;
    
    @SerializedName("cat_main_id")
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

    private Integer is_selected = 0;
    private Integer selected_count = 1;
    private String table_number = "-1";
    private String on_hold = "yes";
    private String order_number = "O-";

    public String getItemTransId() {
        return itemTransId;
    }

    public void setItemTransId(String itemTransId) {
        this.itemTransId = itemTransId;
    }

    public String getItemMainId() {
        return itemMainId;
    }

    public void setItemMainId(String itemMainId) {
        this.itemMainId = itemMainId;
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

    public Integer getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(Integer is_selected) {
        this.is_selected = is_selected;
    }

    public Integer getSelected_count() {
        return selected_count;
    }

    public void setSelected_count(Integer selected_count) {
        this.selected_count = selected_count;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public String getOn_hold() {
        return on_hold;
    }

    public void setOn_hold(String on_hold) {
        this.on_hold = on_hold;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public int getItemViewType() {
        return Constant.LIST_TYPE_KITCHENS_ITEMS;
    }
}
