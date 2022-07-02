package com.example.smartrestaurant.model;

import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;

/**
 * Created by Dell 5521 on 9/5/2017.
 */

public class OrderSummaryItemsModel implements RecyclerViewBaseAdapter.AdapterDataType {
    int id;

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    String statusText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getItemViewType() {
        return Constant.LIST_TYPE_ORDER_SUMMARY_ITEMS;
    }
}
