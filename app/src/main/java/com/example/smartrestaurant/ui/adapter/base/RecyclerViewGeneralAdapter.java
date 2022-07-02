package com.example.smartrestaurant.ui.adapter.base;

import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewGeneralAdapter<T extends RecyclerViewGeneralAdapter.AdapterDataType> extends RecyclerViewBaseAdapter<T, GeneralViewHolder<T>> {

    protected ViewHolderFactory<T> viewHolderFactory;

    public RecyclerViewGeneralAdapter(List<T> items, ViewHolderFactory<T> viewHolderFactory) {
        super(items);
        this.viewHolderFactory = viewHolderFactory;
    }

    @Override
    public int getItemViewType(int position) {
        return viewHolderFactory.getItemViewType(position, getList());//getItem(position).getItemViewType();
    }

    @Override
    public GeneralViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderFactory.createViewHolder(parent, viewType);
    }

}