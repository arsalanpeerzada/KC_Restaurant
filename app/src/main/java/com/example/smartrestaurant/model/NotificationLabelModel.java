package com.example.smartrestaurant.model;

import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;

/**
 * Created by Dell 5521 on 1/21/2018.
 */

public class NotificationLabelModel implements RecyclerViewBaseAdapter.AdapterDataType{
    String titleText;

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    @Override
    public int getItemViewType() {
        return Constant.LIST_TYPE_NOTIFICATION_LABEL;
    }
}
