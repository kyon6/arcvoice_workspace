package com.wanmei.arcvoice.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wanmei.arcvoice.R;

/**
 * Created by Youwei.Zhou on 11/10/2014.
 * setting the arc at the load page
 */
public class ArcSettingsView extends LinearLayout {


    public static int viewWidth;

    public static int viewHeight;

    public ArcSettingsView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_setting_view, this);
        View view = findViewById(R.id.arc_settings);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }
}
