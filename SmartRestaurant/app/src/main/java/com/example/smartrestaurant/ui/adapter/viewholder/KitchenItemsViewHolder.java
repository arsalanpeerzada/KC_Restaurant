package com.example.smartrestaurant.ui.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.API;
import com.example.smartrestaurant.databinding.LiItemKitchenItemsBinding;
import com.example.smartrestaurant.databinding.LiItemOrderListBinding;
import com.example.smartrestaurant.model.OrderListModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Dell 5521 on 9/5/2017.
 */

public class KitchenItemsViewHolder extends GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> {

    LiItemKitchenItemsBinding binding;
    RecyclerViewBaseAdapter.EventCallback<Tap, KitchenItemListModel> eventCallback;
    boolean isFromPlacedOrderDetail;
    Context context;

    public KitchenItemsViewHolder(Context context,boolean isFromPlacedOrderDetail, LiItemKitchenItemsBinding binding, RecyclerViewBaseAdapter.EventCallback<Tap, KitchenItemListModel> callback) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.isFromPlacedOrderDetail = isFromPlacedOrderDetail;
        this.eventCallback = callback;
    }

    @Override
    public void setItem(int position, RecyclerViewBaseAdapter.AdapterDataType data) {
        final KitchenItemListModel model = (KitchenItemListModel) data;
        Picasso.with(context).load(API.CONCAT_ITEM_IMAGE+model.getItemTransId()+".jpg")
                .resize(250,250).centerCrop()
                .placeholder(R.mipmap.icon_item_placeholder).into(binding.menuItemImageIV);
        binding.menuItemNameIV.setText(model.getItemName());
        binding.menuItemPriceIV.setText("Rs. "+model.getItemPrice());
        binding.sameItemCountTV.setText("" + model.getSelected_count());
        binding.menuItemCheckImageView.setVisibility(model.getIs_selected() == 1 ? View.VISIBLE: View.GONE);

        binding.addOneMoreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int addOne = model.getSelected_count();
                addOne++;
                model.setSelected_count(addOne);
                binding.sameItemCountTV.setText("" + model.getSelected_count());
                if (isFromPlacedOrderDetail)
                    eventCallback.onEvent(Tap.UPDATE_PLACED, model);
                else
                    eventCallback.onEvent(Tap.UPDATE, model);
            }
        });
        binding.removeOneMoreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int removeOne = model.getSelected_count();
                if (removeOne > 1) {
                    removeOne--;
                    model.setSelected_count(removeOne);
                    binding.sameItemCountTV.setText("" + model.getSelected_count());
                    if (isFromPlacedOrderDetail)
                        eventCallback.onEvent(Tap.UPDATE_PLACED, model);
                    else
                        eventCallback.onEvent(Tap.UPDATE, model);
                }
            }
        });
        binding.addOneMoreIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int addOne = model.getSelected_count();
                addOne = addOne + 10;
                model.setSelected_count(addOne);
                binding.sameItemCountTV.setText("" + model.getSelected_count());
                if (isFromPlacedOrderDetail)
                    eventCallback.onEvent(Tap.UPDATE_PLACED, model);
                else
                    eventCallback.onEvent(Tap.UPDATE, model);
                return true;
            }
        });
        binding.removeOneMoreIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int removeOne = model.getSelected_count();
                if (removeOne > 11) {
                    removeOne = removeOne - 10;
                    model.setSelected_count(removeOne);
                    binding.sameItemCountTV.setText("" + model.getSelected_count());
                    if (isFromPlacedOrderDetail)
                        eventCallback.onEvent(Tap.UPDATE_PLACED, model);
                    else
                        eventCallback.onEvent(Tap.UPDATE, model);
                }

                return true;
            }
        });


        /*binding.menuItemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setIs_selected(model.getIs_selected() == 0 ? 1 : 0);
                binding.menuItemCheckBox.setChecked(model.getIs_selected() == 1);
                if (isFromPlacedOrderDetail)
                    eventCallback.onEvent(Tap.UPDATE_PLACED, model);
                else
                    eventCallback.onEvent(Tap.UPDATE, model);
            }
        });*/
        binding.menuItemImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setIs_selected(model.getIs_selected() == 0 ? 1 : 0);
                binding.menuItemCheckImageView.setVisibility(model.getIs_selected() == 1 ? View.VISIBLE : View.GONE);
                if (isFromPlacedOrderDetail)
                    eventCallback.onEvent(Tap.UPDATE_PLACED, model);
                else
                    eventCallback.onEvent(Tap.UPDATE, model);
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFromPlacedOrderDetail)
                    eventCallback.onEvent(Tap.UPDATE_PLACED, model);
                else
                    eventCallback.onEvent(Tap.UPDATE, model);
            }
        });
    }

    public enum Tap {ROOT, UPDATE, UPDATE_PLACED}
}
