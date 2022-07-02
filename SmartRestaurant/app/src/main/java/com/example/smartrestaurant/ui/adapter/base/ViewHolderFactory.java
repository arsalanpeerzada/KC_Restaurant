package com.example.smartrestaurant.ui.adapter.base;

import android.view.ViewGroup;

import java.util.List;

/**
 * Created by M Faizan on 7/30/2017.
 */

public abstract class ViewHolderFactory<T extends RecyclerViewGeneralAdapter.AdapterDataType> {

    public int getItemViewType(int position, List<T> list){
        return 0;
    }

    public abstract GeneralViewHolder<T> createViewHolder(ViewGroup parent, int viewType);

}
