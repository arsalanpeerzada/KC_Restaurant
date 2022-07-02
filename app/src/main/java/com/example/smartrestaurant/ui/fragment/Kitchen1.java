package com.example.smartrestaurant.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.FrKitchenOneBinding;
import com.example.smartrestaurant.databinding.FrNfcBinding;
import com.example.smartrestaurant.databinding.LiItemKitchenItemsBinding;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewGeneralAdapter;
import com.example.smartrestaurant.ui.adapter.base.ViewHolderFactory;
import com.example.smartrestaurant.ui.adapter.viewholder.KitchenItemsViewHolder;
import com.example.smartrestaurant.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class Kitchen1 extends BaseFragment {

    private FrKitchenOneBinding binding;
    private OnKitchen1Listener mListener;
    RecyclerViewGeneralAdapter<RecyclerViewBaseAdapter.AdapterDataType> adapter;
    List<RecyclerViewBaseAdapter.AdapterDataType> list = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnKitchen1Listener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_kitchen_one,container,false);
        setupComponents();
        return binding.getRoot();
    }

    public void updateListItems(){
        if(adapter != null){
            list.clear();
//            list.addAll(TransactionKitchenItemsModel.getInstance().getListItems());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initializeComponents() {
        setAdapter();
    }

    private void setAdapter(){
        adapter = new RecyclerViewGeneralAdapter<>(list, new ViewHolderFactory<RecyclerViewBaseAdapter.AdapterDataType>() {
            @Override
            public int getItemViewType(int position, List<RecyclerViewBaseAdapter.AdapterDataType> list) {
                return list.get(position).getItemViewType();
            }

            @Override
            public GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> createViewHolder(ViewGroup parent, int viewType) {
                switch (viewType){
                    case Constant.LIST_TYPE_KITCHENS_ITEMS:
                        LiItemKitchenItemsBinding b2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                R.layout.li_item_kitchen_items,
                                parent,
                                false
                        );

                        return new KitchenItemsViewHolder(context,true,b2, new RecyclerViewBaseAdapter.EventCallback<KitchenItemsViewHolder.Tap, KitchenItemListModel>() {
                            @Override
                            public void onEvent(KitchenItemsViewHolder.Tap event, KitchenItemListModel data) {
                                mListener.onKitchen1Listen(data);
                            }
                        });
                }
                return null;
            }
        });
        binding.kitchenOneItemsRV.setAdapter(adapter);
        binding.kitchenOneItemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.kitchenOneItemsRV.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void setupListeners() {

    }

    public interface OnKitchen1Listener{
        void onKitchen1Listen(KitchenItemListModel model);
    }
}
