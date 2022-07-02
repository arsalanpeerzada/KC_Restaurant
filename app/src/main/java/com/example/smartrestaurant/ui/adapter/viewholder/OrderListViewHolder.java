package com.example.smartrestaurant.ui.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.databinding.LiItemOrderListBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.OrderListModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.example.smartrestaurant.ui.fragment.OrderList;

/**
 * Created by Dell 5521 on 9/5/2017.
 */

public class OrderListViewHolder extends GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> {

    LiItemOrderListBinding binding;
    RecyclerViewBaseAdapter.EventCallback<Tap,OrderOnHoldModel> eventCallback;
Context context;

    public OrderListViewHolder(Context context, LiItemOrderListBinding binding,
                               RecyclerViewBaseAdapter.EventCallback<Tap,OrderOnHoldModel> callback) {
        super(binding.getRoot());
        this.binding = binding;
        this.eventCallback = callback;
        this.context = context;
    }

    @Override
    public void setItem(int position, RecyclerViewBaseAdapter.AdapterDataType data) {
        final OrderOnHoldModel itemListModel = (OrderOnHoldModel) data;
        binding.tableNoTV.setText("Table: "+itemListModel.getTable_number());
        binding.orderNoTV.setText("Order: "+itemListModel.getOrder_number());
        binding.statusLL.setVisibility(itemListModel.getOn_hold().equalsIgnoreCase("yes") ?
                View.GONE : View.VISIBLE);
        binding.ListStatusBtn.setVisibility(itemListModel.getOn_hold().equalsIgnoreCase("yes") ?
                View.VISIBLE : View.GONE);
        binding.ListDeleteBtn.setVisibility(itemListModel.getOn_hold().equalsIgnoreCase("yes") ?
                View.VISIBLE : View.GONE);
        binding.ListStatusBtn.setText(itemListModel.getOn_hold().equalsIgnoreCase("yes") ?
            "Hold" : "Placed");
        binding.ListDeleteBtn.setText(itemListModel.getOn_hold().equalsIgnoreCase("yes") ?
                "Delete" : "Delete");

        binding.liItemOrderListRootRL.setBackgroundColor(ContextCompat.getColor(context,
                itemListModel.getOn_hold().equalsIgnoreCase("yes") ?
                R.color.colorGrayLight1 : R.color.colorWhite));

        binding.ListStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventCallback.onEvent(Tap.ROOT,itemListModel);
            }
        });

        binding.ListDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbScript.getInstance().clearHoldOrders(itemListModel.getTable_number(),itemListModel.getOrder_number());

//                binding.tableNoTV.setVisibility(View.GONE);
//                binding.orderNoTV.setVisibility(View.GONE);
//                binding.statusLL.setVisibility(View.GONE);
//                binding.ListStatusBtn.setVisibility(View.GONE);
//                binding.ListDeleteBtn.setVisibility(View.GONE);
                binding.liItemOrderListRootRL.setVisibility(View.GONE);


            }
        });
    }

    public enum Tap{ ROOT }
}
