package com.example.smartrestaurant.ui.adapter.viewholder;

import com.example.smartrestaurant.databinding.LiNotificationLebelBinding;
import com.example.smartrestaurant.databinding.LiNotificationsListItemBinding;
import com.example.smartrestaurant.model.NotificationLabelModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;

/**
 * Created by Dell 5521 on 9/5/2017.
 */

public class NotificationListLabelViewHolder extends GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> {

    LiNotificationLebelBinding binding;
//    RecyclerViewBaseAdapter.EventCallback<Tap,PlacedOrderModel> eventCallback;

    public NotificationListLabelViewHolder(LiNotificationLebelBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
//        this.eventCallback = callback;
    }

    @Override
    public void setItem(int position, RecyclerViewBaseAdapter.AdapterDataType data) {
        final NotificationLabelModel itemListModel = (NotificationLabelModel) data;

        binding.labelTextTV.setText(itemListModel.getTitleText());
        //1 Placed
        //2 Process
        //3 Ready
        //4 Dispatch
        //5 hold
        //6 close
        //7 served

    }

    public enum Tap {ROOT}
}
