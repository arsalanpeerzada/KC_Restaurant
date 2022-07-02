package com.example.smartrestaurant.ui.adapter.base;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class RecyclerViewBaseAdapter<T extends RecyclerViewBaseAdapter.AdapterDataType, V extends GeneralViewHolder<T>> extends RecyclerView.Adapter<V> {

    protected List<T> list;

    public RecyclerViewBaseAdapter(List<T> items) {
        list = items;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.setItem(position, getItem(position));
    }

    @Override
    public void onViewRecycled(V holder) {
        super.onViewRecycled(holder);
        holder.onRecycled();
    }

    @Override
    public void onViewAttachedToWindow(V holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(V holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onDetachedFromWindow();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public T getItem(int position) {
        int size = list.size();
        boolean isOk = position >= 0 && position < size;
        return isOk ? list.get(position) : null;
    }

    public void addItem(T item) {
        list.add(item);
        notifyItemInserted(list.size() - 1);
    }

    public void addItem(int position, T item) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(List<T> list) {
        addItem(getItemCount(), list);
    }

    public void addItem(int position, List<T> list) {
        this.list.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    public List<T> getList() {
        return list;
    }

    public int updateItem(T updateItem) {
        for (int i = 0; i < list.size(); i++) {
            T item = list.get(i);
            if (item.equals(updateItem)) {
                list.remove(i);
                list.add(i, updateItem);
                notifyItemChanged(i);
                return i;
            }
        }
        return -1;
    }

    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    public int getPosition(T item) {
        if (list.size() == 0) return -1;

        int i = 0;
        while (i < list.size() && !list.get(i).equals(item)) {
            i++;
        }

        return i >= list.size() ? -1 : i;
    }

    public void moveToFirst(T item) {
        int pos = getPosition(item);
        if (pos != -1) {
            list.remove(pos);
        }
        list.add(0, item);
        if (pos != -1) {
            notifyItemMoved(pos, 0);
            notifyItemChanged(0);
        } else {
            notifyItemInserted(0);
        }
    }

    public int removeItem(T item) {
        int pos = getPosition(item);
        if (pos != -1) {
            list.remove(pos);
            notifyItemRemoved(pos);
        }
        return pos;
    }

    public interface AdapterDataType {
        int getItemViewType();
    }

    public interface EventCallback<A,T> {
        void onEvent(A event, T data);
    }
}

