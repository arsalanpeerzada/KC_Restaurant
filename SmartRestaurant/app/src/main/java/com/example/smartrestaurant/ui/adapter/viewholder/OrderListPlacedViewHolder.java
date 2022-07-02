package com.example.smartrestaurant.ui.adapter.viewholder;

import android.view.View;

import com.example.smartrestaurant.databinding.LiItemOrderListBinding;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderSingleItem;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;

/**
 * Created by Dell 5521 on 9/5/2017.
 */

public class OrderListPlacedViewHolder extends GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> {

    LiItemOrderListBinding binding;
    RecyclerViewBaseAdapter.EventCallback<Tap,PlacedOrderModel> eventCallback;


    public OrderListPlacedViewHolder(LiItemOrderListBinding binding, RecyclerViewBaseAdapter.EventCallback<Tap,PlacedOrderModel> callback) {
        super(binding.getRoot());
        this.binding = binding;
        this.eventCallback = callback;
    }

    @Override
    public void setItem(int position, RecyclerViewBaseAdapter.AdapterDataType data) {
        final PlacedOrderModel itemListModel = (PlacedOrderModel) data;
        binding.tableNoTV.setText("Table: "+itemListModel.getTableNo());
        binding.orderNoTV.setText("Order: "+itemListModel.getOmMainId());
        binding.ListStatusBtn.setVisibility(View.GONE);
        binding.ListDeleteBtn.setVisibility(View.GONE);
        binding.statusLL.setVisibility(View.VISIBLE);

        //1 Placed
        //2 Process
        //3 Ready
        //4 Dispatch
        //5 hold
        //6 close
        //7 served

        int inProcess = 0, ready = 0, served = 0, newItem = 0;
        if(itemListModel.getOrderDetail() != null){
            for (int i = 0; i < itemListModel.getOrderDetail().size(); i++) {
                PlacedOrderSingleItem singleItem = itemListModel.getOrderDetail().get(i);
                switch (singleItem.getItemCookingStatus()){
                    case "1":
                        newItem++;
                        break;
                    case "2":
                        inProcess++;
                        break;
                    case "3":
                        ready++;
                        break;
                    case "4":
                        break;
                    case "5":
                        break;
                    case "6":
                        break;
                    case "7":
                        served++;
                        break;
                }
            }
        }

        binding.txtItemnew.setText(String.valueOf(newItem));
        binding.txtIteminprocess.setText(String.valueOf(inProcess));
        binding.txtItemready.setText(String.valueOf(ready));
        binding.txtItemserved.setText(String.valueOf(served));

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventCallback.onEvent(Tap.ROOT,itemListModel);
            }
        });
    }

    public enum Tap{ ROOT }
}
