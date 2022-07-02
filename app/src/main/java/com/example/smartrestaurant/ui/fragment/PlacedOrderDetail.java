package com.example.smartrestaurant.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.API;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.FrPlacedOrderDetailBinding;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderSingleItem;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class PlacedOrderDetail extends BaseFragment {

    private FrPlacedOrderDetailBinding binding;
    private OnPlacedOrderDetailListener mListener;
    private int cost;
    private PlacedOrderModel placedOrderModel;
    List<PlacedOrderSingleItem> newList;
    List<PlacedOrderSingleItem> retainAlreadyPlacedItems;

    public static PlacedOrderDetail newInstance(PlacedOrderModel placedOrderModel) {
        PlacedOrderDetail detail = new PlacedOrderDetail();
        detail.placedOrderModel = placedOrderModel;
        return detail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnPlacedOrderDetailListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_placed_order_detail, container, false);
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        AppLog.d("Pause");
        if(placedOrderModel != null && placedOrderModel.getOrderDetail() != null){
            placedOrderModel.getOrderDetail().clear();
            placedOrderModel.setOrderDetail(retainAlreadyPlacedItems);
        }
        super.onPause();
    }

    @Override
    public void initializeComponents() {
        retainAlreadyPlacedItems = new ArrayList<>();
        if (placedOrderModel != null) {
            if (placedOrderModel.getOrderDetail() != null) {
                retainAlreadyPlacedItems.addAll(placedOrderModel.getOrderDetail());
            } else {
                AppLog.toast("There is an invalid order detail.");
            }
            binding.orderCartOrderNo.setText(placedOrderModel.getOmMainId());
            binding.orderCartTableNo.setText(placedOrderModel.getTableNo());
            binding.orderCartServiceCharge.setText(placedOrderModel.getServiceCharges()==null?"N/A":placedOrderModel.getServiceCharges()+"%");
            binding.orderCartPayMode.setText(placedOrderModel.getOmPaymentMethod());
            binding.grandTotalCostTV.setText(placedOrderModel.getOmGrandTotal());
            String cusName = placedOrderModel.getCustName() != null ? placedOrderModel.getCustName() :
                    placedOrderModel.getCust_child_name() != null ? placedOrderModel.getCust_child_name() : "N/A";
            String cusCardNo = placedOrderModel.getCustCardNo() != null ? placedOrderModel.getCustCardNo() :
                    placedOrderModel.getCust_child_card_no() != null ? placedOrderModel.getCust_child_card_no() : "";

            binding.orderCartCName.setText(cusName);
            binding.orderCartCNo.setText(cusCardNo);

            getAllNewSelectedItems();
        } else {
            AppLog.toast("There is an invalid order detail.");
        }
//        setAdapter();

        // Configure the refreshing colors
        binding.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorGreen,
                R.color.colorOrange,
                R.color.Red);

    }

    private void getAllNewSelectedItems() {
        List<KitchenItemListModel> newSelectedList = new ArrayList<>();
        List<KitchenNameModel> kitchenNameModels = TransactionKitchenItemsModel.getInstance()
                .getKitchenDataModelForPlaceOrder().getData();
        for (int i = 0; i < kitchenNameModels.size(); i++) {
            for (int j = 0; j < kitchenNameModels.get(i).getItemList().size(); j++) {
                KitchenItemListModel itemListModels = kitchenNameModels.get(i).getItemList().get(j);
                if (itemListModels.getIs_selected() == 1) {
                    newSelectedList.add(itemListModels);
                }
            }
        }

//        PlacedOrderModel placedOrderModel1 = new PlacedOrderModel();
//        placedOrderModel1.setOrderDetail(placedOrderModel.getOrderDetail());
        for (int i = 0; i < newSelectedList.size(); i++) {
            KitchenItemListModel kitchenItemListModel = newSelectedList.get(i);
            PlacedOrderSingleItem singleItem = new PlacedOrderSingleItem();
            singleItem.setItemSalePrice(kitchenItemListModel.getItemSalePrice());
            singleItem.setItemPrice(kitchenItemListModel.getItemPrice());
            singleItem.setItemName(kitchenItemListModel.getItemName());
            singleItem.setKtcTransId(kitchenItemListModel.getKtcTransId());
            singleItem.setCatTransId(kitchenItemListModel.getCatTransId());
            singleItem.setItemMainId(kitchenItemListModel.getItemTransId());
            singleItem.setItemQty(kitchenItemListModel.getSelected_count() + "");
            singleItem.setItemTransId(kitchenItemListModel.getItemTransId() + "");
            singleItem.setItemCookingStatus("-1");
            singleItem.setIsSelected(kitchenItemListModel.getIs_selected());
            singleItem.setRemarks(kitchenItemListModel.getRemarks());
//            singleItem.setIsNewAdded(kitchenItemListModel.getOn_hold()+"");
            placedOrderModel.getOrderDetail().add(singleItem);
        }

//        newList = new ArrayList<>();
//        newList.addAll(placedOrderModel.getOrderDetail());
        Collections.reverse(placedOrderModel.getOrderDetail());
        binding.liCart.setAdapter(
                new SelectedItemAdapter(
                        placedOrderModel.getOrderDetail() != null ? placedOrderModel.getOrderDetail() : new ArrayList<PlacedOrderSingleItem>()));
        binding.selectedItemCountTV.setText("Items (" + (placedOrderModel.getOrderDetail() != null ?
                placedOrderModel.getOrderDetail().size() : 0) + ")");
        cost = 0;
        if (placedOrderModel.getOrderDetail().size() == 0) {
            binding.grandTotalCostTV.setText(String.valueOf(cost));
            return;
        }
        for (int i = 0; i < placedOrderModel.getOrderDetail().size(); i++) {
            float itemCount = Float.valueOf(placedOrderModel.getOrderDetail().get(i).getItemQty());
            float totalCostPerItem = itemCount * Float.valueOf(placedOrderModel.getOrderDetail().get(i).getItemPrice());
            cost += totalCostPerItem;
            binding.grandTotalCostTV.setText(String.valueOf(cost));
        }

        String serviceAmount = placedOrderModel.getServiceCharges()==null?"0":placedOrderModel.getServiceCharges();
        float serviceChargeAmount = (Float.valueOf(serviceAmount)*cost)/100;
        binding.grandTotalCostTV.setText(String.valueOf(cost+serviceChargeAmount));


        /*boolean isAllItemsAreReady = true;
        for (PlacedOrderSingleItem singleItem : placedOrderModel.getOrderDetail()) {
            if(!singleItem.getItemCookingStatus().equalsIgnoreCase("3")){
                isAllItemsAreReady = false;
                break;
            }
        }
        if(isAllItemsAreReady){
            binding.orderCloseBtn.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
            binding.orderCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                    Map<String,String> map = BodyParams.closeOrder(binding.orderCartOrderNo.getText().toString());
                    RequestHandler.closeOrder(map, new Callback<PlacedOrderModel>() {
                        @Override
                        public void invoke(PlacedOrderModel obj) {
                            if(obj != null){
                                AppLog.toast("Order has been closed.");
                                popAllStack();
                            } else {
                                AppLog.toast("Please check your internet connection.");
                            }
                        }
                    });
                }
            });
        } else {
            binding.orderCloseBtn.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGrayLight));
            binding.orderCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppLog.toast("Please place the remaining items first.");
                }
            });
        }*/
        checkButtons();
    }

    private void checkButtons() {
        boolean isNewItemIsThere = false;
        for (PlacedOrderSingleItem singleItem : placedOrderModel.getOrderDetail()) {
            if (singleItem.getItemCookingStatus().equalsIgnoreCase("-1")) {
                isNewItemIsThere = true;
                break;
            }
        }
        if (isNewItemIsThere) {
            binding.orderUpdateBtn.setVisibility(View.VISIBLE);
            binding.orderCloseBtn.setVisibility(View.GONE);
        } else {
            binding.orderUpdateBtn.setVisibility(View.GONE);
            binding.orderCloseBtn.setVisibility(View.VISIBLE);
            binding.orderCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!InternetConnection.isOnline()) {
                        showConnectionErrorMsg();
                        return;
                    }

                    for (PlacedOrderSingleItem singleItem : placedOrderModel.getOrderDetail()) {
                        if (!singleItem.getItemCookingStatus().equalsIgnoreCase("7")) {
                            AppLog.toast("Please serve all items before closing this order.");
                            return;
                        }
                    }
                    showDialog();
                    Map<String, String> map = BodyParams.closeOrder(binding.orderCartOrderNo.getText().toString());
                    RequestHandler.closeOrder(map, new Callback<PlacedOrderModel>() {
                        @Override
                        public void invoke(PlacedOrderModel obj) {
                            if (obj != null) {
                                AppLog.toast("Order has been closed.");
                                popAllStack();
                            } else {
                                AppLog.toast("Please check your internet connection.");
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
            });
        }
    }

    @Override
    public void setupListeners() {
        binding.orderUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(InternetConnection.isOnline()) updateNow();
                else showConnectionErrorMsg();
            }
        });
        binding.orderAddNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPlacedOrderDetailListen(Constant.PLACED_ORDER_ADD_NEW, placedOrderModel);
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        binding.syncRefreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRefresh();
            }
        });
    }

    public void doRefresh(){
        binding.swipeRefreshLayout.setRefreshing(true);
        List<PlacedOrderSingleItem> list = placedOrderModel.getOrderDetail();
        final List<PlacedOrderSingleItem> newAddList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemCookingStatus().equalsIgnoreCase("-1")) {
                newAddList.add(list.get(i));
            }
        }
        if(newAddList.size() <= 0){
            getOrderForRefresh();
        } else {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateNow() {
        List<PlacedOrderSingleItem> list = placedOrderModel.getOrderDetail();

        final List<PlacedOrderSingleItem> newAddList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemCookingStatus().equalsIgnoreCase("-1")) {
                newAddList.add(list.get(i));
            }
        }

        String updateAlreadyPlaced = "[";
        for (int i = 0; i < retainAlreadyPlacedItems.size(); i++) {
            for (int j = 0; j < newAddList.size(); j++) {
                if (retainAlreadyPlacedItems.get(i).getItemMainId().equals(newAddList.get(j).getItemMainId())) {
                    int newQty = /*Integer.parseInt(retainAlreadyPlacedItems.get(i).getItemQty()) + */Integer.parseInt(newAddList.get(j).getItemQty());
                    updateAlreadyPlaced += "[" + newAddList.get(j).getItemMainId() + "," + newQty + ",\"" +
                            newAddList.get(j).getRemarks() + "\"],";

                    newAddList.remove(j);
                }
            }
        }
        updateAlreadyPlaced += "]";
        updateAlreadyPlaced = updateAlreadyPlaced.replace(",]", "]");


        /*List<PlacedOrderSingleItem> buildNewItemListToSend = new ArrayList<>();
        boolean isExist = false;
        int pos = -1;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < retainAlreadyPlacedItems.size(); j++) {
                if (!list.get(i).getItemMainId().equals(retainAlreadyPlacedItems.get(j).getItemMainId())){
                    pos = j;
                    isExist = true;
                }
            }
            if(!isExist && pos != -1){
                buildNewItemListToSend.add(list.get(pos));
            }
            isExist = false;
            pos = -1;
        }*/

        String addNewToPlace = "[";
        for (int i = 0; i < newAddList.size(); i++) {
            addNewToPlace += "[" + newAddList.get(i).getItemTransId() + "," + newAddList.get(i).getItemQty() + ",\"" +
                    newAddList.get(i).getRemarks() + "\"],";
        }
        addNewToPlace += "]";
        addNewToPlace = addNewToPlace.replace(",]", "]");

        /*StringBuilder orderListToSend = new StringBuilder("[");
        for (int i = 0; i < newAddList.size(); i++) {
            PlacedOrderSingleItem model = newAddList.get(i);
            if ((newAddList.size() - 1) == i) {
                orderListToSend.append("[").append(model.getItemMainId()).append(",").append(model.getItemQty()).append("]]");
                break;
            } else {
                orderListToSend.append("[").append(model.getItemMainId()).append(",").append(model.getItemQty()).append("],");
            }
        }*/

        showDialog();
        Map<String, String> map = BodyParams.updatePlaceOrder(updateAlreadyPlaced, addNewToPlace, binding.orderCartOrderNo.getText().toString());
        RequestHandler.updatePlaceOrder(map, new Callback<PlacedOrderModel>() {
            @Override
            public void invoke(PlacedOrderModel obj) {
                if (obj != null) {
//                    popAllStack();
                    AppLog.toast("Your newly added order has placed.");

                    List<KitchenItemListModel> newSelectedList = new ArrayList<>();
                    List<KitchenNameModel> kitchenNameModels = TransactionKitchenItemsModel.getInstance()
                            .getKitchenDataModelForPlaceOrder().getData();
                    for (int i = 0; i < kitchenNameModels.size(); i++) {
                        for (int j = 0; j < kitchenNameModels.get(i).getItemList().size(); j++) {
                            KitchenItemListModel itemListModels = kitchenNameModels.get(i).getItemList().get(j);
                            if (itemListModels.getIs_selected() == 1) {
                                newSelectedList.add(itemListModels);
                            }
                        }
                    }
                    for (int i = 0; i < newSelectedList.size(); i++) {
                        KitchenItemListModel singleItem = newSelectedList.get(i);
                        singleItem.setIs_selected(0);
                        TransactionKitchenItemsModel.getInstance().updateKitchenItemForPlacedOrder(singleItem);
                    }
                    mListener.onPlacedOrderDetailListen(Constant.PLACED_ORDER_UPDATE, obj);
                } else {
                    AppLog.toast("Please check your internet connection.");
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


    public interface OnPlacedOrderDetailListener {
        void onPlacedOrderDetailListen(int fragConst, PlacedOrderModel placedOrderModel);
    }

    public class SelectedItemAdapter extends BaseAdapter {
        List<PlacedOrderSingleItem> list;

        public SelectedItemAdapter(List<PlacedOrderSingleItem> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class SelectedViewHolder {
            CircleImageView placeItemImageIV;
            TextView placeItemNameIV, placeItemPriceIV, placeItemStatusIV, remarksDescTV, sameItemCount;
            ImageView addOneMoreTV, removeOneMoreTV, deleteItemIV;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final SelectedViewHolder holder;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.li_item_placed, viewGroup, false);
                holder = new SelectedViewHolder();
                holder.placeItemNameIV = (TextView) view.findViewById(R.id.placeItemNameIV);
                holder.placeItemPriceIV = (TextView) view.findViewById(R.id.placeItemPriceIV);
                holder.placeItemStatusIV = (TextView) view.findViewById(R.id.placeItemStatusIV);
                holder.remarksDescTV = (TextView) view.findViewById(R.id.remarksDescTV);
                holder.placeItemImageIV = (CircleImageView) view.findViewById(R.id.placeItemImageIV);
                holder.sameItemCount = (TextView) view.findViewById(R.id.sameItemCountTV);
                holder.addOneMoreTV = (ImageView) view.findViewById(R.id.addOneMoreIV);
                holder.removeOneMoreTV = (ImageView) view.findViewById(R.id.removeOneMoreIV);
                holder.deleteItemIV = (ImageView) view.findViewById(R.id.deleteItemIV);
                view.setTag(holder);
            } else {
                holder = (SelectedViewHolder) view.getTag();
            }


            //1 Placed
            //2 Process
            //3 Ready
            //4 Dispatch
            //5 hold
            //6 close
            //7 served

            final PlacedOrderSingleItem model = list.get(i);
            Picasso.with(context).load(API.CONCAT_ITEM_IMAGE+model.getItemTransId()+".jpg")
                    .resize(250,250).centerCrop()
                    .placeholder(R.mipmap.icon_item_placeholder).into(holder.placeItemImageIV);
            holder.placeItemNameIV.setText(model.getItemName());
            holder.placeItemPriceIV.setText("Rs. " + model.getItemPrice() + "    Qty. " + model.getItemQty());
            holder.remarksDescTV.setText(model.getRemarks() == null ||
                    model.getRemarks().equalsIgnoreCase("null") ? "" : model.getRemarks());
            String statusText = "";
            switch (model.getItemCookingStatus()) {
                case "-1":
                    statusText = "Not Placed";
                    break;
                case "1":
                    statusText = "New";
                    break;
                case "2":
                    statusText = "in process";
                    break;
                case "3":
                    statusText = "Ready";
                    break;
                case "4":
                    statusText = "Dispatched";
                    break;
                case "5":
                    statusText = "Hold";
                    break;
                case "6":
                    statusText = "Closed";
                    break;
                case "7":
                    statusText = "Served";
                    break;

            }

            holder.placeItemStatusIV.setText(statusText);
            if (model.getItemCookingStatus().equalsIgnoreCase("-1")) {
                /*Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_blue);
                img.setBounds(0, 0, 60, 60);
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));*/
                holder.placeItemStatusIV.setVisibility(View.GONE);
                holder.sameItemCount.setVisibility(View.VISIBLE);
                holder.addOneMoreTV.setVisibility(View.VISIBLE);
                holder.removeOneMoreTV.setVisibility(View.VISIBLE);
                holder.deleteItemIV.setVisibility(View.VISIBLE);
                holder.sameItemCount.setText(model.getItemQty());
                holder.addOneMoreTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int newC = Integer.parseInt(model.getItemQty());
                        newC = newC + 1;
                        model.setItemQty("" + newC);
                        KitchenItemListModel singleItem = new KitchenItemListModel();
                        singleItem.setItemSalePrice(model.getItemSalePrice());
                        singleItem.setItemPrice(model.getItemPrice());
                        singleItem.setItemName(model.getItemName());
                        singleItem.setKtcTransId(model.getKtcTransId());
                        singleItem.setCatTransId(model.getCatTransId());
                        singleItem.setItemMainId(model.getItemMainId());
                        singleItem.setSelected_count(Integer.parseInt(model.getItemQty()));
                        singleItem.setItemTransId(model.getItemTransId() + "");
                        singleItem.setIs_selected(model.getIsSelected());
                        singleItem.setRemarks(model.getRemarks());
//                        singleItem.setItemCookingStatus("-1");

//                        int addOne = singleItem.getSelected_count();
//                        addOne++;
//                        singleItem.setSelected_count(addOne);
                        holder.placeItemPriceIV.setText("Rs. " + model.getItemPrice() + "    Qty. " + model.getItemQty());
                        holder.sameItemCount.setText(model.getItemQty());
                        TransactionKitchenItemsModel.getInstance().updateKitchenItemForPlacedOrder(singleItem);
                        updateCost(singleItem, true);
                    }
                });
                holder.removeOneMoreTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int removeOne = Integer.parseInt(model.getItemQty());
//                        int removeOne = singleItem.getSelected_count();
                        if (removeOne > 1) {
//                            removeOne--;
                            removeOne = removeOne - 1;
                            model.setItemQty("" + removeOne);
                            KitchenItemListModel singleItem = new KitchenItemListModel();
                            singleItem.setItemSalePrice(model.getItemSalePrice());
                            singleItem.setItemPrice(model.getItemPrice());
                            singleItem.setItemName(model.getItemName());
                            singleItem.setKtcTransId(model.getKtcTransId());
                            singleItem.setCatTransId(model.getCatTransId());
                            singleItem.setItemMainId(model.getItemMainId());
                            singleItem.setSelected_count(Integer.parseInt(model.getItemQty()));
                            singleItem.setItemTransId(model.getItemTransId() + "");
                            singleItem.setIs_selected(model.getIsSelected());
                            singleItem.setRemarks(model.getRemarks());
//                            singleItem.setSelected_count(removeOne);
                            holder.placeItemPriceIV.setText("Rs. " + model.getItemPrice() + "    Qty. " + model.getItemQty());
                            holder.sameItemCount.setText("" + singleItem.getSelected_count());
                            TransactionKitchenItemsModel.getInstance().updateKitchenItemForPlacedOrder(singleItem);
                            updateCost(singleItem, false);
                        }
                    }
                });
                holder.remarksDescTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        KitchenItemListModel singleItem = new KitchenItemListModel();
                        singleItem.setItemSalePrice(model.getItemSalePrice());
                        singleItem.setItemPrice(model.getItemPrice());
                        singleItem.setItemName(model.getItemName());
                        singleItem.setKtcTransId(model.getKtcTransId());
                        singleItem.setCatTransId(model.getCatTransId());
                        singleItem.setItemMainId(model.getItemMainId());
                        singleItem.setSelected_count(Integer.parseInt(model.getItemQty()));
                        singleItem.setItemTransId(model.getItemTransId() + "");
                        singleItem.setIs_selected(model.getIsSelected());
                        singleItem.setRemarks(model.getRemarks());
                        showRemarksDialog(model, singleItem, holder.remarksDescTV);
                    }
                });
                holder.deleteItemIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeleteConfirmationDialog(list, i);
                    }
                });
            } else if (model.getItemCookingStatus().equalsIgnoreCase("1")){ //Placed
                Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_new);
                img.setBounds(0, 0, 60, 60);
                img.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.Red), PorterDuff.Mode.MULTIPLY));
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.Red));
            } else if (model.getItemCookingStatus().equalsIgnoreCase("2")){ //Process
                Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_processing);
                img.setBounds(0, 0, 60, 60);
                img.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.colorOrange), PorterDuff.Mode.MULTIPLY));
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
            } else if (model.getItemCookingStatus().equalsIgnoreCase("3")){ //Ready
                Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_ready);
                img.setBounds(0, 0, 60, 60);
                img.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.colorBlue), PorterDuff.Mode.MULTIPLY));
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
            } else if (model.getItemCookingStatus().equalsIgnoreCase("4")){ //Dispatched
                Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_processing);
                img.setBounds(0, 0, 60, 60);
                img.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.colorAppBlack), PorterDuff.Mode.MULTIPLY));
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.colorAppBlack));
            } else if (model.getItemCookingStatus().equalsIgnoreCase("5")){ //Hold
                Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_processing);
                img.setBounds(0, 0, 60, 60);
                img.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.colorAppBlack), PorterDuff.Mode.MULTIPLY));
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.colorAppBlack));
            } else if (model.getItemCookingStatus().equalsIgnoreCase("6")){ //Close
                Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_processing);
                img.setBounds(0, 0, 60, 60);
                img.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.colorAppBlack), PorterDuff.Mode.MULTIPLY));
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.colorAppBlack));
            } else { //Served
                Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_status_served);
                img.setBounds(0, 0, 60, 60);
                img.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.colorGreen), PorterDuff.Mode.MULTIPLY));
                holder.placeItemStatusIV.setCompoundDrawables(img, null, null, null);
                holder.placeItemStatusIV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            }
//            holder.placeItemStatusIV.setVisibility(View.GONE);
//            holder.placeItemImageIV.setVisibility(View.GONE);

            return view;
        }

        private void updateCost(KitchenItemListModel singleItem, boolean isAdd) {
            float itemActualAmout = Float.valueOf(singleItem.getItemPrice());

            String serviceAmount = placedOrderModel.getServiceCharges()==null?"0":placedOrderModel.getServiceCharges();
            float itemAmountWithServiceCharge = ((Float.valueOf(serviceAmount)*itemActualAmout)/100);


            String cost = binding.grandTotalCostTV.getText().toString();
            float totalCost = Float.valueOf(cost);
            if (isAdd) totalCost = totalCost + itemAmountWithServiceCharge + itemActualAmout;
            else totalCost = totalCost - (itemAmountWithServiceCharge + itemActualAmout);
            binding.grandTotalCostTV.setText("" + totalCost);
        }
    }

    private void updateListAfterDeleteAnItem(List<PlacedOrderSingleItem> list) {
        binding.liCart.setAdapter(new SelectedItemAdapter(list));
    }

    private void showDeleteConfirmationDialog(final List<PlacedOrderSingleItem> list, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this item?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        KitchenItemListModel singleItem = new KitchenItemListModel();
                        PlacedOrderSingleItem model = list.get(pos);
                        singleItem.setItemSalePrice(model.getItemSalePrice());
                        singleItem.setItemPrice(model.getItemPrice());
                        singleItem.setItemName(model.getItemName());
                        singleItem.setKtcTransId(model.getKtcTransId());
                        singleItem.setCatTransId(model.getCatTransId());
                        singleItem.setItemMainId(model.getItemMainId());
                        singleItem.setSelected_count(Integer.parseInt(model.getItemQty()));
                        singleItem.setItemTransId(model.getItemTransId() + "");
                        singleItem.setIs_selected(0);
                        singleItem.setRemarks("");
                        TransactionKitchenItemsModel.getInstance().updateKitchenItemForPlacedOrder(singleItem);

                        list.remove(pos);
                        updateListAfterDeleteAnItem(list);
                        checkButtons();
                        binding.selectedItemCountTV.setText("Items(" + list.size() + ")");
                        int cost = 0;
                        for (int j = 0; j < list.size(); j++) {
                            int itemCount = Integer.parseInt(list.get(j).getItemQty());
                            int totalCostPerItem = itemCount * Integer.parseInt(list.get(j).getItemPrice());
                            cost += totalCostPerItem;
                            binding.grandTotalCostTV.setText(String.valueOf(cost));
                        }

                        String serviceAmount = placedOrderModel.getServiceCharges()==null?"0":placedOrderModel.getServiceCharges();
                        float itemAmountWithServiceCharge = ((Integer.parseInt(serviceAmount)*cost)/100);
                        binding.grandTotalCostTV.setText(String.valueOf(cost+itemAmountWithServiceCharge));


                    }
                }).show();
    }

    private void showRemarksDialog(final PlacedOrderSingleItem model, final KitchenItemListModel kitchenItemListModel, final TextView textView) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.remarks_dialog);
        final EditText remarkET = (EditText) dialog.findViewById(R.id.addRemarksET);
        Button remarkBtn = (Button) dialog.findViewById(R.id.addRemarksBtn);
        remarkET.setText(model.getRemarks() == null ? "" : model.getRemarks());
        remarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(remarkET.getText().toString());
                model.setRemarks(remarkET.getText().toString());
                kitchenItemListModel.setRemarks(remarkET.getText().toString());
                TransactionKitchenItemsModel.getInstance().updateKitchenItemForPlacedOrder(kitchenItemListModel);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getOrderForRefresh(){
        if(!InternetConnection.isOnline()) {
            showConnectionErrorMsg();
            binding.swipeRefreshLayout.setRefreshing(false);
            return;
        }
        RequestHandler.getSpecificPlacedOrder(placedOrderModel.getOmMainId(), new Callback<PlacedOrderModel>() {
            @Override
            public void invoke(PlacedOrderModel obj) {
                binding.swipeRefreshLayout.setRefreshing(false);
                if(obj != null)
                    mListener.onPlacedOrderDetailListen(Constant.PLACED_ORDER_UPDATE,obj);
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
