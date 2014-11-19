package com.wanmei.arcvoice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.talkray.arcvoice.ArcRegion;
import com.talkray.arcvoice.ArcVoice;
import com.talkray.arcvoice.ArcVoiceEventHandler;
import com.talkray.arcvoice.MemberCallStatus;
import com.wanmei.arcvoice.model.Member;
import com.wanmei.arcvoice.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * the view show the member list
 * Created by liang on 14/10/31.
 */
public class ArcVoiceHelper {
    public static final String TAG = "ArcVoiceDemo";
    private static ArcVoiceHelper mInstance = null;
    private boolean isInSession;
    private Context mContext;
    /**
     * params needed
     */
    private String ARC_APP_ID;
    private String ARC_APP_CREDENTIALS;
    private ArcRegion ARC_REGION;
    private String USER_ID;
    /**
     * myself mute状态
     */
    private boolean isMuteMyself = false;
    /**
     * others mute状态
     */
    private boolean isMuteOthers = false;
    /**
     * 用户头像是否显示
     */
    private boolean isAvatarShow = false;//默认显示头像
    /**
     * setting view  是否显示状态
     */
    private boolean isSettingsShow = false;//show settings default
    private Orientation mOrientation = Orientation.VERTICAL;
    /**
     * 用来记录头像是否显示
     * 如果头像从没显示到显示，立马更新adapter
     * 如果头像一直显示，则延迟更新adapter
     */
    private boolean isStatusUpdateRunning = false;
    /**
     * record user's name and avatar
     */
    private Map<String, Member> mPlayerInfoList;
    /**
     * ArcVoice and ArcVoiceEventHandler used to talk to the ArcVoiceSDK
     */
    private ArcVoice arcVoice;
    private ArcVoiceEventHandler eventHandler;
    private Handler mainThreadHandler;
    private List<Member> mPlayerList = null;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (ArcWindowManager.getArcMemberView() != null) {
                ArcWindowManager.getArcMemberView().updateAdapter(mPlayerList);
            }
        }
    };
    private boolean mEnable;
    private boolean mMicEnable;

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

        this.mPlayerInfoList = new HashMap<String, Member>();
        this.mPlayerList = new ArrayList<Member>();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
        ArcVoicePersistenceData.getInstance().init(mContext);

        setupArc();
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public void setOrientation(Orientation orientation) {
        mOrientation = orientation;
    }

    /**
     * 启动，加入到会话当中
     * 显示同一个session中用户状态
     *
     * @param sessionId
     */
    public void startSession(String sessionId) {
        if (arcVoice != null) {
            arcVoice.joinSession(sessionId);
            isInSession = true;
            ArcWindowManager.removeArcSettingsWindow(mContext);
            showAvatars();
        }
    }

    /**
     * 由于arcVoice只返回userId和userStatus
     * <p/>
     * 这里需要游戏开发者将朋友信息传递进来（包括userId，userName，userAvatar），这样通过userId关联可以显示用户信息
     * <p/>
     *  如果是进入某个区域形成会话，游戏开发者能不能获得其他玩家的相关信息？？需要确认
     *  不需要考虑
     * <p/>
     * String imageUri = "http://site.com/image.png"; // from Web
     * String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     * String imageUri = "content://media/external/audio/albumart/1"; // from content provider
     * String imageUri = "assets://image.png"; // from assets
     * String imageUri = "drawable://" + R.drawable.img; // from drawables (non-9patch images)
     *
     * @param userId
     * @param userName   用户昵称
     * @param userAvatar 用户头像
     */
    public void addPlayerInfo(String userId, String userName, String userAvatar) {
        if (!mPlayerInfoList.containsKey(userId)) {
            Member mPlayer = new Member();
            mPlayer.setUserId(userId);
            mPlayer.setUserName(userName);
            mPlayer.setUserAvatar(userAvatar);
            mPlayerInfoList.put(userId, mPlayer);
        }
    }

    public void setArcEnable(boolean enable) {
        mEnable = enable;
    }

    public void setMicEnable(boolean enable) {
        mMicEnable = enable;
        if (mMicEnable)
            unMuteMyself();
        else
            muteMyself();
        isMuteMyself = mMicEnable;
    }

    /**
     * show ArcVoice HUD
     * 显示 ArcVoice HUB
     */
    public void show() {
        ArcWindowManager.createArcHudWindow(mContext);
    }

    /**
     * hiddenAll ArcVoice HUD
     * include the Arc Icon and user avatars
     * 隐藏所有的view
     */
    public void hiddenAll() {
        ArcWindowManager.removeArcHudWindow(mContext);
//        ArcWindowManager.removeArcMemberWindow(mContext);
//        ArcWindowManager.removeArcSettingsWindow(mContext);
        hiddenOthers();
    }

    public void hiddenOthers() {
        hiddenAvatars();
        hiddenSettings();
    }

    /**
     * 退出会话
     */
    public void quiteSession() {
        if (arcVoice != null) {
            arcVoice.leaveSession();
            isInSession = false;
        }
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
     * Arc Hub 点击事件
     * <p/>
     * 显示/隐藏 Arc Setting View
     * 显示/隐藏 Arc Member View
     */
    public void onClick() {
        if (isInSession) {
            doShowAvatars();
        } else {
            doShowSettings();
        }
    }

    /**
     * show or hidden avatars
     */
    private void doShowAvatars() {
        if (isAvatarShow) {
            hiddenAvatars();
        } else {
            showAvatars();
        }
    }

    /**
     * show user avatars
     */
    private void showAvatars() {
        if (isAvatarShow)
            return;

        ArcWindowManager.createArcMemberWindow(mContext);
        isAvatarShow = true;

        if (ArcWindowManager.getArcMemberView() != null && mPlayerList != null) {
            ArcWindowManager.getArcMemberView().updateAdapter(mPlayerList);
        }
    }

    /**
     * hidden user avatars
     */
    private void hiddenAvatars() {
        if (!isAvatarShow)
            return;

        ArcWindowManager.removeArcMemberWindow(mContext);
        isAvatarShow = false;

        mainThreadHandler.removeCallbacks(runnable);

        isStatusUpdateRunning = false;
    }

    /**
     * show or hidden settings
     */
    private void doShowSettings() {
        if (isSettingsShow) {
            hiddenSettings();
        } else {
            showSettings();
        }
    }

    /**
     * show user avatars
     */
    private void showSettings() {
        if (isSettingsShow)
            return;

        ArcWindowManager.createArcSettingsWindow(mContext);
        isSettingsShow = true;
    }

    /**
     * hidden user avatars
     */
    private void hiddenSettings() {
        if (!isSettingsShow)
            return;

        ArcWindowManager.removeArcSettingsWindow(mContext);
        isSettingsShow = false;

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
        //Toast.makeText(mContext, "mute myself:" + isMuteMyself, Toast.LENGTH_SHORT).show();
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
        //Toast.makeText(mContext, "mute others:" + isMuteOthers, Toast.LENGTH_SHORT).show();
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
//                            ArcWindowManager.getArcHubView().getMembersAdapter().add(status);
//                            callStatusItera.remove();
//                        }
//                    }
//                });
                if (isAvatarShow) {
                    mPlayerList = new ArrayList<Member>();
                    Iterator<MemberCallStatus> callStatusItera = memberStatusMap.values().iterator();

                    Member mPlayer = null;
                    while (callStatusItera.hasNext()) {
                        MemberCallStatus status = callStatusItera.next();
                        Member player = new Member();
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
                        mainThreadHandler.postDelayed(runnable, 2000);
                    } else {
                        LogUtils.e("onCallStatusUpdate first");
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

    /**
     * zhe arc member show orientation
     */
    public static enum Orientation {
        VERTICAL, HORIZONTAL
    }
}
