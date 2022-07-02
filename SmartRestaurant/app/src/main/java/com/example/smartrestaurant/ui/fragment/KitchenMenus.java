package com.example.smartrestaurant.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.FrKitchenMenusBinding;
import com.example.smartrestaurant.databinding.FrKitchensBinding;
import com.example.smartrestaurant.databinding.LiItemKitchenItemsBinding;
import com.example.smartrestaurant.databinding.LiItemKitchenMenuBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.handler.FragmentHandler;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenDataModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.nfc.NFCDataModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewGeneralAdapter;
import com.example.smartrestaurant.ui.adapter.base.ViewHolderFactory;
import com.example.smartrestaurant.ui.adapter.viewholder.KitchenItemsViewHolder;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class KitchenMenus extends BaseFragment {

    private FrKitchenMenusBinding binding;
    private OnKitchenMenusListener mListener;
    RecyclerViewGeneralAdapter<RecyclerViewBaseAdapter.AdapterDataType> itemsAdapter;
    List<RecyclerViewBaseAdapter.AdapterDataType> itemsList = new ArrayList<>();
    private String tableNumber;
    private NFCDataModel nfcDataModel;
    private OrderOnHoldModel orderOnHoldModel;
    private boolean isFromPlacedOrderDetail;
    private PlacedOrderModel placedOrderModel;

    /*public static KitchenMenus newInstance(String tableNumber, NFCDataModel nfcDataModel){
        KitchenMenus kitchenMenus = new KitchenMenus();
        kitchenMenus.tableNumber = tableNumber;
        kitchenMenus.nfcDataModel = nfcDataModel;
        return kitchenMenus;
    }*/
    public static KitchenMenus newInstance(boolean isFromPlacedOrderDetail,PlacedOrderModel placedOrderModel) {
        KitchenMenus kitchenMenus = new KitchenMenus();
        kitchenMenus.isFromPlacedOrderDetail = isFromPlacedOrderDetail;
        kitchenMenus.placedOrderModel = placedOrderModel;
        return kitchenMenus;
    }

    public static KitchenMenus newInstance(NFCDataModel nfcDataModel) {
        KitchenMenus kitchenMenus = new KitchenMenus();
        kitchenMenus.nfcDataModel = nfcDataModel;
        return kitchenMenus;
    }

    public static KitchenMenus newInstance(OrderOnHoldModel orderOnHoldModel) {
        KitchenMenus kitchenMenus = new KitchenMenus();
        kitchenMenus.orderOnHoldModel = orderOnHoldModel;
        return kitchenMenus;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnKitchenMenusListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_kitchen_menus, container, false);
        setupComponents();


        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        setItemsAdapter();
        getKitchensFromLocalDb();
    }

    @Override
    public void setupListeners() {
        binding.kitchenMenuDoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFromPlacedOrderDetail){
                    mListener.onKitchenMenusListen(Constant.KITCHEN_ORDER_DETAIL_FOR_PLACED, null, null,placedOrderModel);
                } else {
                    if (orderOnHoldModel != null)
                        mListener.onKitchenMenusListen(Constant.KITCHEN_ORDER_DETAIL, null, orderOnHoldModel,null);
                    else
                        mListener.onKitchenMenusListen(Constant.KITCHEN_ORDER_DETAIL, nfcDataModel, null,null);
                }
            }
        });
        binding.kitchenMenuBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                popStack();
            }
        });
        binding.kitchenNameMoveOnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.kitchenRV.scrollToPosition(binding.kitchenRV.getAdapter().getItemCount()-1);
            }
        });
        binding.kitchenNameMoveBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.kitchenRV.scrollToPosition(0);
            }
        });

    }

    public interface OnKitchenMenusListener {
        void onKitchenMenusListen(int fragConst, NFCDataModel nfcDataModel,
                                  OrderOnHoldModel orderOnHoldModel, PlacedOrderModel placedOrderModel);
    }

    private void getKitchensFromLocalDb() {
        List<KitchenNameModel> list = DbScript.getInstance().getAllKitchens();
        if (list.size() > 0) {
            binding.kitchenRV.setAdapter(new KitchensAdapter(list));
            binding.kitchenRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.kitchenRV.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.HORIZONTAL));

            callListener(list.get(0),0);
        }

    }

    private void callListener(KitchenNameModel kitchenNameModel,int pos) {
        List<KitchenNameModel> kitchenItems = DbScript.getInstance().getAllKitchens();
        for (KitchenNameModel mo : kitchenItems) {
            if(mo.getKtcMainId().equalsIgnoreCase(kitchenNameModel.getKtcMainId())){
                mo.setIsSelected(1);
            } else {
                mo.setIsSelected(0);
            }
        }
        binding.kitchenRV.setAdapter(new KitchensAdapter(kitchenItems));
        binding.kitchenRV.scrollToPosition(pos);

        List<KitchenItemListModel> list;
        if(isFromPlacedOrderDetail){
            list = TransactionKitchenItemsModel.getInstance().getListItems(kitchenNameModel.getKtcTransId());
        } else {
            if (orderOnHoldModel != null)
                list = DbScript.getInstance().getOrderByTableNumber(
                        orderOnHoldModel.getTable_number(), orderOnHoldModel.getOrder_number(),
                        kitchenNameModel.getKtcTransId());
            else
                list = DbScript.getInstance().getOrderByTableNumber(
                        nfcDataModel.getTableNumber(), nfcDataModel.getOrderNumber(), kitchenNameModel.getKtcTransId());
        }

        if (list.size() > 0) {
            itemsList.clear();
            itemsList.addAll(list);
            itemsAdapter.notifyDataSetChanged();
            binding.noItemsAvailableTV.setVisibility(View.GONE);
            binding.kitchenItemsRV.setVisibility(View.VISIBLE);
        } else {
            binding.kitchenItemsRV.setVisibility(View.GONE);
            binding.noItemsAvailableTV.setVisibility(View.VISIBLE);
        }
    }

    public class KitchensAdapter extends RecyclerView.Adapter<KitchensAdapter.ViewHolderKitchen> {
        List<KitchenNameModel> list;

        public KitchensAdapter(List<KitchenNameModel> list) {
            this.list = list;
        }

        @Override
        public ViewHolderKitchen onCreateViewHolder(ViewGroup parent, int viewType) {
            LiItemKitchenMenuBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()), R.layout.li_item_kitchen_menu, parent, false);
            return new ViewHolderKitchen(binding);
        }

        @Override
        public void onBindViewHolder(ViewHolderKitchen holder, int position) {
            holder.setData(list.get(position),position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolderKitchen extends RecyclerView.ViewHolder {

            LiItemKitchenMenuBinding bind;

            public ViewHolderKitchen(LiItemKitchenMenuBinding binding) {
                super(binding.getRoot());
                this.bind = binding;
            }

            public void setData(final KitchenNameModel model, final int pos) {
                bind.menuItemTV.setBackground(ContextCompat.getDrawable(context,
                        model.getIsSelected()==1?
                                R.drawable.kitchen_name_bg :
                                R.drawable.kitchen_name_bg_white));
                bind.menuItemTV.setText(model.getKtcName());
                bind.menuItemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callListener(model,pos);
                    }
                });
            }
        }
    }

    private void setItemsAdapter() {

        int pos;
        itemsAdapter = new RecyclerViewGeneralAdapter<>(itemsList, new ViewHolderFactory<RecyclerViewBaseAdapter.AdapterDataType>() {
            @Override
            public int getItemViewType(int position, List<RecyclerViewBaseAdapter.AdapterDataType> list) {

                return list.get(position).getItemViewType();
            }

            @Override
            public GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> createViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case Constant.LIST_TYPE_KITCHENS_ITEMS:
                        LiItemKitchenItemsBinding b2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                R.layout.li_item_kitchen_items,
                                parent,
                                false
                        );

                        return new KitchenItemsViewHolder(context,isFromPlacedOrderDetail, b2, new RecyclerViewBaseAdapter.EventCallback<KitchenItemsViewHolder.Tap, KitchenItemListModel>() {
                            @Override
                            public void onEvent(KitchenItemsViewHolder.Tap event, KitchenItemListModel data) {
                                switch (event) {
                                    case ROOT:
//                                        AppLog.toast(data.getItemName());
                                        break;
                                    case UPDATE:
                                        updateClickedItemCount(data);
                                        break;
                                    case UPDATE_PLACED:
                                        updatePlacedClickedItemCount(data);
                                        break;
                                }
                            }
                        });
                }
                return null;
            }
        });
        binding.kitchenItemsRV.setAdapter(itemsAdapter);
        binding.kitchenItemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.kitchenItemsRV.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

//        binding.sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                itemsAdapter.getFilter().filter(s);
//                return false;
//            }
//        });


    }

    private void updateClickedItemCount(KitchenItemListModel model) {
        DbScript.getInstance().updateItem(model);
    }
    private void updatePlacedClickedItemCount(KitchenItemListModel model) {
        TransactionKitchenItemsModel.getInstance().updateKitchenItemForPlacedOrder(model);
    }
}
