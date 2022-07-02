package com.example.smartrestaurant.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.FrKitchenOneBinding;
import com.example.smartrestaurant.databinding.FrOrderListBinding;
import com.example.smartrestaurant.databinding.LiItemOrderListBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.OrderListModel;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenDataModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderGetResponse;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderSingleItem;
import com.example.smartrestaurant.model.nfc.NFCDataModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewGeneralAdapter;
import com.example.smartrestaurant.ui.adapter.base.ViewHolderFactory;
import com.example.smartrestaurant.ui.adapter.viewholder.OrderListPlacedViewHolder;
import com.example.smartrestaurant.ui.adapter.viewholder.OrderListViewHolder;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class OrderList extends BaseFragment {

    private FrOrderListBinding binding;
    private OnOrderListListener mListener;
    private String tableNumber;
    private NFCDataModel nfcDataModel;
    private OrderOnHoldModel orderOnHoldModel;


    public static OrderList newInstance(NFCDataModel nfcDataModel) {
        OrderList list = new OrderList();
        list.nfcDataModel = nfcDataModel;
        return list;
    }

    public static OrderList newInstance(OrderOnHoldModel orderOnHoldModel) {
        OrderList list = new OrderList();
        list.orderOnHoldModel = orderOnHoldModel;
        return list;
    }/*
    public static OrderList newInstance(String tableNumber, NFCDataModel nfcDataModel) {
        OrderList list = new OrderList();
        list.tableNumber = tableNumber;
        list.nfcDataModel = nfcDataModel;
        return list;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnOrderListListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_order_list, container, false);
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        // Configure the refreshing colors
        binding.orderListSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorGreen,
                R.color.colorOrange,
                R.color.Red);
        binding.orderListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh(true);
            }
        });
        dismissDialog();
        if(InternetConnection.isOnline()) getKitchens(false);
        else showConnectionErrorMsg();
    }

    public void doRefresh(boolean isPullToRefresh){
        binding.orderListSwipeRefreshLayout.setRefreshing(true);
        if(InternetConnection.isOnline()) getKitchens(isPullToRefresh);
        else {
            showConnectionErrorMsg();
            binding.orderListSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setAdapter(List<RecyclerViewBaseAdapter.AdapterDataType> list,boolean isPullRefresh) {
        Util.ProgressDialog.dismiss();
        binding.orderListSwipeRefreshLayout.setRefreshing(false);
        RecyclerViewGeneralAdapter<RecyclerViewBaseAdapter.AdapterDataType> adapter;

        adapter = new RecyclerViewGeneralAdapter<>(list, new ViewHolderFactory<RecyclerViewBaseAdapter.AdapterDataType>() {
            @Override
            public int getItemViewType(int position, List<RecyclerViewBaseAdapter.AdapterDataType> list) {
                return list.get(position).getItemViewType();
            }

            @Override
            public GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> createViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case Constant.LIST_TYPE_ORDER_PLACED:
                        LiItemOrderListBinding b = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                R.layout.li_item_order_list,
                                parent,
                                false
                        );

                        return new OrderListPlacedViewHolder(b, new RecyclerViewBaseAdapter.EventCallback<OrderListPlacedViewHolder.Tap, PlacedOrderModel>() {
                            @Override
                            public void onEvent(OrderListPlacedViewHolder.Tap event, PlacedOrderModel data) {
                                mListener.onOrderListListen(null, data);
                            }
                        });

                    case Constant.LIST_TYPE_ORDER_ON_HOLD:
                        LiItemOrderListBinding b2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                R.layout.li_item_order_list,
                                parent,
                                false
                        );

                        return new OrderListViewHolder(context,b2, new RecyclerViewBaseAdapter.EventCallback<OrderListViewHolder.Tap, OrderOnHoldModel>() {
                            @Override
                            public void onEvent(OrderListViewHolder.Tap event, OrderOnHoldModel data) {
                                mListener.onOrderListListen(data, null);
                            }
                        });
                }
                return null;
            }
        });
        binding.orderListRV.setAdapter(adapter);
        if(!isPullRefresh){
            binding.orderListRV.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.orderListRV.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        }
    }


    private void setupAdapter(PlacedOrderGetResponse placedOrderGetResponse,boolean isPullRefresh) {
        if (TransactionKitchenItemsModel.getInstance().getKitchenDataModelForPlaceOrder() != null){
            TransactionKitchenItemsModel.getInstance().getKitchenDataModelForPlaceOrder().getData().clear();
            TransactionKitchenItemsModel.getInstance().setKitchenDataModelForPlaceOrder(
                    TransactionKitchenItemsModel.getInstance().getKitchenDataModel());
        }

        List<RecyclerViewBaseAdapter.AdapterDataType> list = new ArrayList<>();
        List<OrderOnHoldModel> selectedList = DbScript.getInstance().getAllOrders();
        list.addAll(selectedList);

        for (int i = 0; i < placedOrderGetResponse.getData().size(); i++) {
            if(!placedOrderGetResponse.getData().get(i).getOmOrderStatus().equalsIgnoreCase("6")){
                list.add(placedOrderGetResponse.getData().get(i));
            }
        }

        Collections.reverse(list);

        if (list.size() > 0)
            setAdapter(list,isPullRefresh);
        else
            binding.noOrdersTakenTV.setVisibility(View.VISIBLE);
    }

    private void getPlacedOrderFromServer(final boolean isPullRefresh){
        RequestHandler.getPlacedOrder(new Callback<PlacedOrderGetResponse>() {
            @Override
            public void invoke(PlacedOrderGetResponse obj) {
                binding.orderListSwipeRefreshLayout.setRefreshing(false);
                Util.ProgressDialog.dismiss();
                binding.progressBar.setVisibility(View.GONE);
                if(obj != null){
                    setupAdapter(obj,isPullRefresh);
                } else {
                    AppLog.toast("Request Failed.");
                }
            }

            @Override
            public void alreadyLogin() {
                Util.showAlreadyLoginDialog(context);
            }

            @Override
            public void _422(String msg) {
                AppLog.toast(msg);
            }
        });
    }

    @Override
    public void setupListeners() {
        binding.fabNeworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAllStack();
            }
        });
    }

    public interface OnOrderListListener {
        void onOrderListListen(OrderOnHoldModel model, PlacedOrderModel placedOrderModel);
    }

    private void getKitchens(final boolean isPullRefresh){
        if(!isPullRefresh) {
            showDialog();
        }
        String rest_id = AppClass.getUserRestId();
        Map<String, String> map = BodyParams.setRestautant(rest_id);
        RequestHandler.getKitchenItemsForPlacedOrder(map,new Callback<KitchenDataModel>() {
            @Override
            public void invoke(KitchenDataModel obj) {
                if (obj != null) {
                    dismissDialog();
                    TransactionKitchenItemsModel.getInstance().setKitchenDataModel(obj);
                    getPlacedOrderFromServer(isPullRefresh);
                    if(!isPullRefresh)
                        binding.progressBar.setVisibility(View.VISIBLE);

                } else {
                    binding.orderListSwipeRefreshLayout.setRefreshing(false);
                    showConnectionErrorMsg();
                }
            }

            @Override
            public void alreadyLogin() {
                Util.showAlreadyLoginDialog(context);
            }

            @Override
            public void _422(String msg) {
                AppLog.toast(msg);
            }
        });
    }


}
