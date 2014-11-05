package com.wanmei.arcvoice.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wanmei.arcvoice.R;

/**
 * Created by Youwei.Zhou on 11/5/2014.
 */
public class TestAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.voice_member, null);
    }
}
