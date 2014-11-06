package com.wanmei.arcvoice;

import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.talkray.arcvoice.ArcRegion;
import com.talkray.arcvoice.ArcVoice;
import com.talkray.arcvoice.ArcVoiceEventHandler;
import com.talkray.arcvoice.MemberCallStatus;
import com.wanmei.arcvoice.model.Player;
import com.wanmei.arcvoice.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ArcVoice HUD
 * Created by liang on 14/10/31.
 */
public class ArcVoiceHelper {
    public static final String TAG = "ArcVoiceDemo";

    private static ArcVoiceHelper mInstance = null;
    private Context mContext;

    /**
     * params needed
     */
    private String ARC_APP_ID;
    private String ARC_APP_CREDENTIALS;
    private ArcRegion ARC_REGION;
    private String USER_ID;

    private boolean isMuteMyself = false;
    private boolean isMuteOthers = false;
    private boolean isAvatarShow = true;//默认显示头像
    /**
     * 用来记录头像是否显示
     * 如果头像从没显示到显示，立马更新adapter
     * 如果头像一直显示，则延迟更新adapter
     */
    private boolean isStatusUpdateRunning = false;
    /**
     * record user's name and avatar
     */
    private Map<String, Player> mPlayerInfoList;

    /**
     * ArcVoice and ArcVoiceEventHandler useed to talk to the ArcVoiceSDK
     */
    private ArcVoice arcVoice;
    private ArcVoiceEventHandler eventHandler;
    private Handler mainThreadHandler;
    private List<Player> mPlayerList;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (ArcWindowManager.getHubDetailView() != null) {
                ArcWindowManager.getHubDetailView().updateAdapter(mPlayerList);
            }
        }
    };

    private ArcVoiceHelper(Context context) {
        this.mContext = context;
        this.mainThreadHandler = new Handler();
    }

    public static ArcVoiceHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ArcVoiceHelper(context);
        }

        return mInstance;
    }

    /**
     * MUST call first
     * Arc appId and appCredentials assigned to the app.
     *
     * @param arcAppId
     * @param appCredentials
     * @param arcRegion
     * @param userId
     */
    public void init(String arcAppId, String appCredentials, ArcRegion arcRegion, String userId) {
        this.ARC_APP_ID = arcAppId;
        this.ARC_APP_CREDENTIALS = appCredentials;
        this.ARC_REGION = arcRegion;
        this.USER_ID = userId;
        this.mPlayerInfoList = new HashMap<String, Player>();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
        setupArc();
    }

    public void start(String sessionId) {
        if (arcVoice != null) {
            arcVoice.joinSession(sessionId);
        }
    }

    /**
     * show ArcVoice HUD
     */
    public void show() {
        ArcWindowManager.createHubDetailWindow(mContext);
    }

    /**
     * hidden ArcVoice HUD
     * include the Arc Icon and user avatars
     */
    public void hidden() {
        ArcWindowManager.removeHubDetailWindow(mContext);
    }

    /**
     * stop ArcVoiceHelper
     */
    public void stop() {
        if (arcVoice != null) {
            arcVoice.leaveSession();
            arcVoice = null;
        }
        mInstance = null;
    }

    /**
     * show or hidden avatars
     */
    public void doShowAvatars() {
        if (isAvatarShow) {
            hiddenAvatars();
        } else {
            showAvatars();
        }
    }

    /**
     * show user avatars
     */
    public void showAvatars() {
        if (isAvatarShow)
            return;

        isAvatarShow = true;
    }

    /**
     * hidden user avatars
     */
    public void hiddenAvatars() {
        if (!isAvatarShow)
            return;

        isAvatarShow = false;

        mainThreadHandler.removeCallbacks(runnable);
        mPlayerList = null;
        mainThreadHandler.post(runnable);

        isStatusUpdateRunning = false;
    }

    /**
     * show user's name
     */
    public void showUserNames() {
        //todo
    }

    /**
     * hidden user's name
     */
    public void hiddenUserNames() {
        //todo
    }

    /**
     * get user id
     *
     * @return
     */
    public String getUserId() {
        return USER_ID;
    }

    /**
     * mute all
     */
    public void muteAll() {
        if (arcVoice != null) {
            arcVoice.muteSelf();
            arcVoice.muteOthers();
        }
    }

    /**
     * unmute all
     */
    public void unMuteAll() {
        if (arcVoice != null) {
            arcVoice.unmuteSelf();
            arcVoice.unmuteOthers();
        }
    }

    public boolean isMuteMyself() {
        return isMuteMyself;
    }

    public boolean isMuteOthers() {
        return isMuteOthers;
    }

    /**
     * mute or unmute myself
     */
    public void doMuteMyself() {
        if (arcVoice == null)
            return;

        if (isMuteMyself) {
            unMuteMyself();
            isMuteMyself = false;
        } else {
            muteMyself();
            isMuteMyself = true;
        }
    }

    /**
     * mute or unmute others
     */
    public void doMuteOthers() {
        if (arcVoice == null)
            return;

        if (isMuteOthers) {
            unMuteOthers();
            isMuteOthers = false;
        } else {
            muteOthers();
            isMuteOthers = true;
        }
    }

    /**
     * mute myself
     */
    private void muteMyself() {
        if (arcVoice != null) {
            arcVoice.muteSelf();
        }
    }

    /**
     * umnute myself
     */
    private void unMuteMyself() {
        if (arcVoice != null) {
            arcVoice.unmuteSelf();
        }
    }

    /**
     * mute others
     */
    private void muteOthers() {
        if (arcVoice != null) {
            arcVoice.muteOthers();
        }
    }

    /**
     * unmute others
     */
    private void unMuteOthers() {
        if (arcVoice != null) {
            arcVoice.unmuteOthers();
        }
    }

    private void setupArc() {
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
                // 更新用户状态
//                mainThreadHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Iterator<MemberCallStatus> callStatusItera = memberStatusMap.values().iterator();
//                        while(callStatusItera.hasNext()){
//                            MemberCallStatus status = callStatusItera.next();
//                            LogUtils.e(status.getUserId()+":"+ status.getUserState());
//                            ArcWindowManager.getHubDetailView().getMembersAdapter().add(status);
//                            callStatusItera.remove();
//                        }
//                    }
//                });
                if (isAvatarShow) {
                    LogUtils.e("onCallStatusUpdate");
                    mPlayerList = new ArrayList<Player>();
                    Iterator<MemberCallStatus> callStatusItera = memberStatusMap.values().iterator();

                    Player mPlayer = null;
                    while (callStatusItera.hasNext()) {
                        MemberCallStatus status = callStatusItera.next();
                        Player player = new Player();
                        player.setUserId(status.getUserId());
                        player.setUserState(status.getUserState());
                        if (mPlayerInfoList.get(status.getUserId()) != null) {
                            mPlayer = mPlayerInfoList.get(status.getUserId());
                            player.setUserName(mPlayer.getUserName());
                            player.setUserAvatar(mPlayer.getUserAvatar());
                        }
                        mPlayerList.add(player);
                    }

                    if (isStatusUpdateRunning) {
                        mainThreadHandler.postDelayed(runnable, 1000);
                    } else {
                        mainThreadHandler.post(runnable);
                        isStatusUpdateRunning = true;
                    }
                }
            }

            @Override
            public void onError(com.talkray.arcvoice.Error error) {
                LogUtils.e("onError");
            }
        };
        arcVoice = ArcVoice.getInstance(mContext, ARC_APP_ID, ARC_APP_CREDENTIALS, ARC_REGION, USER_ID, eventHandler);
    }
}