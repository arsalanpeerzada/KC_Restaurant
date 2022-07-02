package com.example.smartrestaurant.ui.adapter.base;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class RecyclerViewGeneralAdapter<T extends RecyclerViewGeneralAdapter.AdapterDataType> extends RecyclerViewBaseAdapter<T, GeneralViewHolder<T>> {

    protected ViewHolderFactory<T> viewHolderFactory;
    List<KitchenItemListModel> filteredList;
    List<KitchenItemListModel> nfilterList ;


    public RecyclerViewGeneralAdapter(List<T> items, ViewHolderFactory<T> viewHolderFactory) {
        super(items);
        this.viewHolderFactory = viewHolderFactory;
    }

    @Override
    public int getItemViewType(int position) {
        filteredList = (List<KitchenItemListModel>) getList();
        return viewHolderFactory.getItemViewType(position, getList());//getItem(position).getItemViewType();
    }

    @Override
    public GeneralViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderFactory.createViewHolder(parent, viewType);
    }

//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String charString = constraint.toString();
//                if (charString.isEmpty()) {
//                    filteredList = (List<KitchenItemListModel>) getList();
//                }
//                else {
//                   // nfilterList = (List<KitchenItemListModel>) getList();
//                    ListIterator iterator = getList().listIterator();
//
//                    while (iterator.hasNext()){
//                     KitchenItemListModel mylist = (KitchenItemListModel) iterator.next();
//                     String name = mylist.getItemName().toLowerCase();
//
//                     if (name.contains(charString.toLowerCase().toString())){
//                         filteredList.add(mylist);
//                     }
//
//                    }
//
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filterResults;
//                return null;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            }
//        };
//    }

}