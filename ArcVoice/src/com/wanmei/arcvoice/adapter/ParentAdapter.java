package com.wanmei.arcvoice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mData;

    public ParentAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        mData = new ArrayList<T>();
    }

    public ParentAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return (mData == null ? 0 : mData.size());
    }

    @Override
    public T getItem(int position) {
        return (mData == null ? null : mData.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addData(List<T> data) {
        this.mData.addAll(data);
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public List<T> getData() {
        return this.mData;
    }

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);
}
