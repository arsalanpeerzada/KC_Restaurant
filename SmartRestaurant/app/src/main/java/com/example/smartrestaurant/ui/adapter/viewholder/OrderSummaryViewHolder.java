package com.example.smartrestaurant.ui.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.databinding.LiItemOrderListBinding;
import com.example.smartrestaurant.databinding.LiItemOrderSummaryBinding;
import com.example.smartrestaurant.model.OrderListModel;
import com.example.smartrestaurant.model.OrderSummaryItemsModel;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;

/**
 * Created by Dell 5521 on 9/5/2017.
 */

public class OrderSummaryViewHolder extends GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> {

    LiItemOrderSummaryBinding binding;


    public OrderSummaryViewHolder(LiItemOrderSummaryBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void setItem(int position, RecyclerViewBaseAdapter.AdapterDataType data) {
        final OrderSummaryItemsModel orderListModel = (OrderSummaryItemsModel) data;
        orderListModel.setStatusText(position%2 == 0 ? "Pending" : "Ready");
        binding.ListItemStatusTV.setTextColor(
                orderListModel.getStatusText().equals("Pending") ?
                        ContextCompat.getColor(AppClass.getCtx(), R.color.colorOrange) :
                        ContextCompat.getColor(AppClass.getCtx(), R.color.colorGreen));
        binding.ListItemStatusTV.setText(orderListModel.getStatusText());
    }

}
