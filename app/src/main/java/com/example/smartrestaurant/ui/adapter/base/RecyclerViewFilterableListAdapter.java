package com.example.smartrestaurant.ui.adapter.base;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewFilterableListAdapter<T extends RecyclerViewFilterableListAdapter.Filterable, V extends GeneralViewHolder<T>>
        extends RecyclerViewBaseAdapter<T, V> {

    private static final String TAG = RecyclerViewFilterableListAdapter.class.getSimpleName();

    private List<T> backupList;
    private String filterString = "";

    public RecyclerViewFilterableListAdapter(List<T> items) {
        super(items);
        backupList = new ArrayList<>(list);
    }

    @Override
    public void addItem(T item) {
        super.addItem(item);
        backupList.add(item);
    }

    @Override
    public void addItem(int position, List<T> list) {
        super.addItem(position,list);
        backupList.addAll(position, list);
    }

    public void filter(String s) {
        s = s.toLowerCase();
        if (s.length() == 0) {
            if (filterString.length() == 0) {
                return;
            }
            filterString = s;
            list.clear();
            list.addAll(backupList);
            notifyDataSetChanged();
            return;
        } else if (filterString.length() > s.length()) {
            list.clear();
            list.addAll(backupList);
        }
        filterString = s;
        for (int i = 0; i < list.size(); ) {
            T item = list.get(i);
            Log.d(TAG, "item to filter = " + item.toString());
            if (item.filter(s)) {
                list.remove(i);
                continue;
            }
            i++;
        }
        notifyDataSetChanged();

    }

    public interface Filterable extends RecyclerViewBaseAdapter.AdapterDataType{
        boolean filter(String s);
    }

}
