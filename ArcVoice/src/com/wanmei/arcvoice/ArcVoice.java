package com.wanmei.arcvoice;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.wanmei.arcvoice.utils.DeviceUtils;

/**
 * Created by liang on 14/10/31.
 */
public class ArcVoice {

    private static ArcVoice mInstance = null;

    public static ArcVoice getInstance(Context context){
        if(mInstance == null){
            mInstance = new ArcVoice(context);
        }

        return mInstance;
    }

    private Context mContext;
    private boolean isServiceRunning = false;

    private ArcVoice(Context context){
        this.mContext = context;
    }

    public void start(){
        isServiceRunning = true;
        Intent mIntent = new Intent(mContext,ArcWindowService.class);
        mContext.startService(mIntent);
    }

    public void show(){
        if(isServiceRunning){
            ArcWindowManager.createSmallWindow(mContext);
        }else{
            Toast.makeText(mContext,"start ArcVoice first!!",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 移除所有悬浮窗,service为停止
     */
    public void hidden(){
        ArcWindowManager.removeBigWindow(mContext);
        ArcWindowManager.removeSmallWindow(mContext);
    }

    /**
     * 移除所有悬浮窗，并停止Service
     */
    public void stop(){
        hidden();
        isServiceRunning = false;
        Intent intent = new Intent(mContext, ArcWindowService.class);
        mContext.stopService(intent);
    }
}
