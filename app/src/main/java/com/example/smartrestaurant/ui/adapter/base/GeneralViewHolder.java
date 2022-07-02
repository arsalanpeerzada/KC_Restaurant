package com.example.smartrestaurant.ui.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Amir Raza on 7/28/2017.
 */

public abstract class GeneralViewHolder<T> extends RecyclerView.ViewHolder {

    public GeneralViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setItem(int position, T data);

    public void onRecycled(){}
    public void onAttachedToWindow(){}
    public void onDetachedFromWindow(){}

}
