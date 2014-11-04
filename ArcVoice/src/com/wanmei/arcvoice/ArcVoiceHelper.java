package com.wanmei.arcvoice;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import com.talkray.arcvoice.*;
import com.wanmei.arcvoice.utils.LogUtils;

import java.lang.reflect.Member;
import java.util.Iterator;
import java.util.Map;

/**
 * ArcVoice悬浮框 主工具类
 * Created by liang on 14/10/31.
 */
public class ArcVoiceHelper {
    public static final String TAG = "ArcVoiceDemo";

    private static ArcVoiceHelper mInstance = null;
    private Context mContext;

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
    private String USER_NAME;
    private String USER_LOGO;

    /**
     * should call after init
     * Arc appId and appCredentials assigned to the app.
     * @param arcAppId
     * @param appCredentials
     * @param arcRegion
     * @param userId
     */
    public void init(String arcAppId,String appCredentials,ArcRegion arcRegion,String userId
            ,String userName,String userLogo){
        this.ARC_APP_ID = arcAppId;
        this.ARC_APP_CREDENTIALS = appCredentials;
        this.ARC_REGION = arcRegion;
        this.USER_ID = userId;
        this.USER_NAME = userName;
        this.USER_LOGO = userLogo;

        setupArc();
    }

    public void start(String sessionId){
        if(arcVoice != null){
            arcVoice.joinSession(sessionId);
        }
    }
    /**
     * show ArcVoiceHelper
     */
    public void show(){
        ArcWindowManager.createSmallWindow(mContext);
    }
    /**
     * hidden ArcVoiceHelper
     */
    public void hidden(){
        ArcWindowManager.removeBigWindow(mContext);
        ArcWindowManager.removeSmallWindow(mContext);
    }

    public void stop(){
        if(arcVoice != null){
            arcVoice.leaveSession();
            arcVoice = null;
        }
        mInstance = null;
    }

    public void muteMyself(){
        if(arcVoice != null){
            arcVoice.muteSelf();
        }
    }

    public void unMuteMyself(){
        if(arcVoice != null){
            arcVoice.unmuteSelf();
        }
    }

    public void muteOthers(){
        if(arcVoice != null){
            arcVoice.muteOthers();
        }
    }

    public void unMuteOthers(){
        if(arcVoice != null){
            arcVoice.unmuteOthers();
        }
    }

    /**
     * 显示用户名称
     */
    public void showUserName(){

    }

    /**
     * 隐藏用户名称
     */
    public void hiddenUserName(){

    }

    private ArcVoice arcVoice;
    private ArcVoiceEventHandler eventHandler;

    private void setupArc(){
        eventHandler = new ArcVoiceEventHandler() {
            @Override
            public void onCallConnected() {
                LogUtils.e("onCallConnected");
                //当前用户上线
            }

            @Override
            public void onCallDisconnected() {
                LogUtils.e("onCallDisconnected");
                //当前用户下线
            }

            @Override
            public void onRegister() {
                LogUtils.e("onRegister");
            }

            @Override
            public void onCallStatusUpdate(final Map memberStatusMap) {
                LogUtils.e("onCallStatusUpdate");
                //TODO 更新用户状态
                //ArcWindowManager.getSmallWindow().getMembersAdapter().clear();
                Iterator<MemberCallStatus> callStatusItera = memberStatusMap.values().iterator();
                while(callStatusItera.hasNext()){
                    MemberCallStatus status = callStatusItera.next();
                    LogUtils.e(status.getUserId()+":"+ status.getUserState());
                    ArcWindowManager.getSmallWindow().getMembersAdapter().add(status);
                    callStatusItera.remove();
                }
            }

            @Override
            public void onError(com.talkray.arcvoice.Error error) {
                LogUtils.e("onError");
            }
        };
        arcVoice = ArcVoice.getInstance(mContext,ARC_APP_ID,ARC_APP_CREDENTIALS,ARC_REGION,USER_ID,eventHandler);
    }
}
