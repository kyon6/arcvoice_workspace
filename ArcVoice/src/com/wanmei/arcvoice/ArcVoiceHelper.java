package com.wanmei.arcvoice;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import com.talkray.arcvoice.*;
import com.wanmei.arcvoice.utils.LogUtils;

import java.util.Map;

/**
 * ArcVoice悬浮框 主工具类
 * Created by liang on 14/10/31.
 */
public class ArcVoiceHelper {
    public static final String TAG = "ArcVoiceDemo";

    private static ArcVoiceHelper mInstance = null;
    private Context mContext;
    private boolean isServiceRunning = false;


    public static ArcVoiceHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new ArcVoiceHelper(context);
        }

        return mInstance;
    }

    private ArcVoiceHelper(Context context){
        this.mContext = context;
    }

    private String ARC_APP_ID;
    private String ARC_APP_CREDENTIALS;
    private ArcRegion ARC_REGION;
    private String USER_ID;

    /**
     * should call after init
     * Arc appId and appCredentials assigned to the app.
     * @param arcAppId
     * @param appCredentials
     * @param arcRegion
     * @param userId
     */
    public void init(String arcAppId,String appCredentials,ArcRegion arcRegion,String userId){
        this.ARC_APP_ID = arcAppId;
        this.ARC_APP_CREDENTIALS = appCredentials;
        this.ARC_REGION = arcRegion;
        this.USER_ID = userId;
    }

    /**
     * start ArcVoiceHelper
     * MUST call init() before
     */
    public void start(){
        isServiceRunning = true;
        Intent mIntent = new Intent(mContext,ArcWindowService.class);
        mContext.startService(mIntent);
    }

    /**
     * show ArcVoiceHelper
     */
    public void show(){
        if(isServiceRunning){
            ArcWindowManager.createSmallWindow(mContext);
        }else{
            Toast.makeText(mContext,"start ArcVoiceHelper first!!",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * hidden ArcVoiceHelper
     */
    public void hidden(){
        ArcWindowManager.removeBigWindow(mContext);
        ArcWindowManager.removeSmallWindow(mContext);
    }

    /**
     * stop ArcVoiceHelper
     */
    public void stop(){
        hidden();
        isServiceRunning = false;
        Intent intent = new Intent(mContext, ArcWindowService.class);
        mContext.stopService(intent);
    }

    private Handler mainThreadHandler;

    private ArcVoice arcVoice;
    private ArcVoiceEventHandler eventHandler;

    private void setupArc(String appId,String appCredentials,ArcRegion arcRegion,String userId){
        eventHandler = new ArcVoiceEventHandler() {
            @Override
            public void onCallConnected() {
                LogUtils.e("onCallConnected");
            }

            @Override
            public void onCallDisconnected() {
                LogUtils.e("onCallDisconnected");
            }

            @Override
            public void onRegister() {
                LogUtils.e("onRegister");
            }

            @Override
            public void onCallStatusUpdate(Map map) {
                LogUtils.e("onCallStatusUpdate");
            }

            @Override
            public void onError(com.talkray.arcvoice.Error error) {
                LogUtils.e("onError");
            }
        };
        arcVoice = ArcVoice.getInstance(mContext,appId,appCredentials,arcRegion,userId,eventHandler);

    }
}
