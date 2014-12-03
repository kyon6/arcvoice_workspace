package com.wanmei.arcvoice.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.ArcVoicePersistenceData;
import com.wanmei.arcvoice.ArcWindowManager;
import com.wanmei.arcvoice.R;

/**
 * Created by Youwei.Zhou on 11/10/2014.
 * setting the arc at the load page
 */
public class ArcSettingsView extends LinearLayout {


    public static int viewWidth;

    public static int viewHeight;
    private CheckBox enable_CheckBox, mic_CheckBox;
    private TextView arc_help;

    public ArcSettingsView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_setting_view, this);
        View view = findViewById(R.id.arc_settings);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        initView();
        initActions();
    }

    private void initView() {
        enable_CheckBox = (CheckBox) findViewById(R.id.arc_enable_check);
        enable_CheckBox.setChecked(ArcVoicePersistenceData.getInstance().getArcEnable());
        ArcVoiceHelper.getInstance(getContext()).setArcEnable(enable_CheckBox.isChecked());

        mic_CheckBox = (CheckBox) findViewById(R.id.arc_mic_check);
        mic_CheckBox.setChecked(ArcVoicePersistenceData.getInstance().getArcMicEnable());
        ArcVoiceHelper.getInstance(getContext()).setMicEnable(mic_CheckBox.isChecked());

        arc_help = (TextView) findViewById(R.id.arc_help);
    }

    private void initActions() {
        enable_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArcVoiceHelper.getInstance(getContext()).setArcEnable(isChecked);
                ArcVoicePersistenceData.getInstance().setArcEnable(isChecked);
            }
        });
        mic_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArcVoiceHelper.getInstance(getContext()).setMicEnable(isChecked);
                ArcVoicePersistenceData.getInstance().setArcMicEnable(isChecked);
            }
        });

        arc_help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcWindowManager.createArcHelpWindow(getContext());
            }
        });

    }
}
