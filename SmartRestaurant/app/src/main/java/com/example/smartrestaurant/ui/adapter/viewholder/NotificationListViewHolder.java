package com.example.smartrestaurant.ui.adapter.viewholder;

import android.view.View;

import com.example.smartrestaurant.databinding.LiItemOrderListBinding;
import com.example.smartrestaurant.databinding.LiNotificationsListItemBinding;
import com.example.smartrestaurant.model.NotificationItemModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderSingleItem;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.example.smartrestaurant.ui.fragment.Notification;

/**
 * Created by Dell 5521 on 9/5/2017.
 */

public class NotificationListViewHolder extends GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> {

    LiNotificationsListItemBinding binding;
    RecyclerViewBaseAdapter.EventCallback<Tap,NotificationItemModel> eventCallback;
    int displayType;

    public NotificationListViewHolder(int displayType, LiNotificationsListItemBinding binding,RecyclerViewBaseAdapter.EventCallback<Tap,NotificationItemModel> eventCallback) {
        super(binding.getRoot());
        this.binding = binding;
        this.eventCallback = eventCallback;
        this.displayType = displayType;
    }

    @Override
    public void setItem(int position, RecyclerViewBaseAdapter.AdapterDataType data) {
        final NotificationItemModel itemListModel = (NotificationItemModel) data;

        //1 Placed
        //2 Process
        //3 Ready
        //4 Dispatch
        //5 hold
        //6 close
        //7 served

        switch (displayType){
            case Notification.SORT_BY_TABLE:
                binding.liNotiTitle.setText(itemListModel.getItemName());
                binding.liNotiQty.setText("Qty. "+itemListModel.getItemQty()+
                "     Kitchen: "+itemListModel.getKtcName());
                break;
            case Notification.SORT_BY_ITEM:
                binding.liNotiTitle.setText("Table # "+itemListModel.getTableNo());
                binding.liNotiQty.setText("Qty. "+itemListModel.getItemQty()+
                        "     Kitchen: "+itemListModel.getKtcName());
                break;
            case Notification.SORT_BY_KITCHEN:
                binding.liNotiTitle.setText(itemListModel.getItemName());
                binding.liNotiQty.setText("Qty. "+itemListModel.getItemQty()+
                        "     Table # "+itemListModel.getTableNo());
                break;

        }
        binding.itemStatusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventCallback.onEvent(Tap.ROOT,itemListModel);
            }
        });
    }

    public enum Tap{ ROOT }
}
