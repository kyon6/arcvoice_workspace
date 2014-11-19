package com.wanmei.arcvoice;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.wanmei.arcvoice.utils.DeviceUtils;
import com.wanmei.arcvoice.utils.LogUtils;
import com.wanmei.arcvoice.view.ArcHudView;
import com.wanmei.arcvoice.view.ArcMemberView;
import com.wanmei.arcvoice.view.ArcSettingsView;

/**
 * ArcVoice悬浮框 界面类
 * Created by liang on 14/10/31.
 */
public class ArcWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static ArcHudView arcHudView;
    /**
     * 大悬浮窗View的实例
     */
    private static ArcMemberView arcMemberView;

    private static ArcSettingsView arcSettingsView;
    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams arcHudWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams arcMemberWindowParams;


    private static LayoutParams arcSettingsWindowParams;
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    public static ArcHudView getArcHudView() {
        return arcHudView;
    }

    public static ArcMemberView getArcMemberView() {
        return arcMemberView;
    }

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createArcHudWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (arcHudView == null) {
            arcHudView = new ArcHudView(context);
            if (arcHudWindowParams == null) {
                arcHudWindowParams = new LayoutParams();
                arcHudWindowParams.type = LayoutParams.TYPE_PHONE;
                arcHudWindowParams.format = PixelFormat.RGBA_8888;
                arcHudWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                arcHudWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                arcHudWindowParams.width = ArcHudView.viewWidth;
                arcHudWindowParams.height = arcHudView.viewHeight;
                arcHudWindowParams.x = 0;
                arcHudWindowParams.y = screenHeight / 2;
            }
            arcHudView.setParams(arcHudWindowParams);
            windowManager.addView(arcHudView, arcHudWindowParams);
            LogUtils.e("==HubView:" + arcHudView);
        }
    }

    public static void createArcSettingsWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        ArcVoiceHelper.Orientation orientation = ArcVoiceHelper.getInstance(context).getOrientation();
        if (arcSettingsView == null) {
            arcSettingsView = new ArcSettingsView(context);
            arcSettingsWindowParams = new LayoutParams();

            WindowManager.LayoutParams mHubParams = arcHudView.getParams();
            int hubX = mHubParams.x;
            int hubY = mHubParams.y;
            //判断ArcHub所在位置标记。0：左上，1：右上，2：左下，3：右下
//            if(orientation == ArcVoiceHelper.Orientation.VERTICAL) {
            if (hubX <= screenWidth / 2) {
                arcSettingsWindowParams.x = mHubParams.x;
                if (hubY <= screenHeight / 2) {//左上
                    arcSettingsWindowParams.y = mHubParams.y + ArcHudView.viewHeight;
                } else {//左下
                    arcSettingsWindowParams.y = mHubParams.y - ArcSettingsView.viewHeight;
                }
            } else {
                arcSettingsWindowParams.x = mHubParams.x - ArcSettingsView.viewWidth + ArcHudView.viewWidth;
                if (hubY <= screenHeight / 2) {//右上
                    arcSettingsWindowParams.y = mHubParams.y + ArcHudView.viewHeight;
                } else {//右下
                    arcSettingsWindowParams.y = mHubParams.y - ArcSettingsView.viewHeight;
                }
            }
//            }else{
//                if(hubY <= screenHeight / 2){
//                    arcSettingsWindowParams.y = mHubParams.y;
//                    if (hubX <= screenWidth / 2) {//左上
//                        arcSettingsWindowParams.x = mHubParams.x + ArcHubView.viewWidth;
//                    } else {//右上
//                        arcSettingsWindowParams.x = mHubParams.x - ArcSettingsView.viewWidth;
//                    }
//                }else{
//                    arcSettingsWindowParams.y = mHubParams.y - ArcSettingsView.viewHeight + arcHubView.viewHeight;
//                    if (hubX <= screenWidth / 2) {//左下
//                        arcSettingsWindowParams.x = mHubParams.x + ArcHubView.viewWidth;
//                    } else {//右下
//                        arcSettingsWindowParams.x = mHubParams.x - ArcSettingsView.viewWidth;
//                    }
//                }
//            }


            arcSettingsWindowParams.type = LayoutParams.TYPE_PHONE;
            arcSettingsWindowParams.format = PixelFormat.RGBA_8888;
            arcSettingsWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | LayoutParams.FLAG_NOT_FOCUSABLE;
            arcSettingsWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
            arcSettingsWindowParams.width = ArcSettingsView.viewWidth;
            arcSettingsWindowParams.height = ArcSettingsView.viewHeight;
            windowManager.addView(arcSettingsView, arcSettingsWindowParams);
            LogUtils.e("hub position:" + arcHudView.getParams().x + "," + arcHudView.getParams().y);
            LogUtils.e("settings position:" + arcSettingsWindowParams.x + "," + arcSettingsWindowParams.y);

        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createArcMemberWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        ArcVoiceHelper.Orientation orientation = ArcVoiceHelper.getInstance(context).getOrientation();
        if (arcMemberView == null) {
            /**
             * check the hub view position
             */
            WindowManager.LayoutParams mHubParams = arcHudView.getParams();
            int hubX = mHubParams.x;
            int hubY = mHubParams.y;

            /**
             * 位置校验
             */
//            int maxHubX = screenWidth - ArcHudView.viewWidth / 2;
//            int maxHubY = screenHeight - ArcHudView.viewHeight / 2;
            if(hubX < 0){
                hubX = 0;
            }
            if(hubX > screenWidth ){
                hubX = screenWidth;
            }
            if(hubY < 0){
                hubY = 0;
            }
            int h = DeviceUtils.getNavigationBarHeight(context);
            if(hubY > screenHeight - h){
                hubY = screenHeight - h;
            }

            arcMemberView = new ArcMemberView(context, orientation);
            arcMemberWindowParams = new LayoutParams();
            ArcMemberView.Direction direction = ArcMemberView.Direction.TEXT_RIGHT;
            //判断ArcHub所在位置标记。0：左上，1：右上，2：左下，3：右下
            if (orientation == ArcVoiceHelper.Orientation.VERTICAL) {
                if (hubX <= screenWidth / 2) {
                    arcMemberWindowParams.x = hubX;
                    if (hubY <= screenHeight / 2) {//左上
                        arcMemberWindowParams.y = hubY + ArcHudView.viewHeight;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.LEFT_UP);
                    } else {//左下
                        arcMemberWindowParams.y = hubY - ArcMemberView.viewHeight;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.LEFT_DOWN);
                    }
                    //arcMemberView.updateDirection(ArcMemberView.Direction.TEXT_RIGHT);
                } else {
                    arcMemberWindowParams.x = hubX - ArcMemberView.viewWidth + ArcHudView.viewWidth;
                    if (hubY <= screenWidth / 2) {//右上
                        arcMemberWindowParams.y = hubY + ArcHudView.viewHeight;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.RIGHT_UP);
                    } else {//右下
                        arcMemberWindowParams.y = hubY - ArcMemberView.viewHeight;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.RIGHT_DOWN);
                    }
                    //arcMemberView.updateDirection(ArcMemberView.Direction.TEXT_LEFT);
                }
            } else {
                if (hubY <= screenHeight / 2) {
                    arcMemberWindowParams.y = hubY;
                    if (hubX <= screenWidth / 2) {//左上
                        arcMemberWindowParams.x = hubX + ArcHudView.viewWidth;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.LEFT_UP);
                    } else {//右上
                        arcMemberWindowParams.x = hubX - ArcMemberView.viewWidth;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.RIGHT_UP);
                    }
                    //arcMemberView.updateDirection(ArcMemberView.Direction.TEXT_DOWN);
                } else {
                    arcMemberWindowParams.y = hubY - arcMemberView.viewHeight + arcHudView.viewHeight;
                    if (hubX <= screenWidth / 2) {//左下
                        arcMemberWindowParams.x = hubX + ArcHudView.viewWidth;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.LEFT_DOWN);
                    } else {//右下
                        arcMemberWindowParams.x = hubX - ArcMemberView.viewWidth;
                        arcMemberView.updateHubPosition(ArcMemberView.HubPosition.RIGHT_DOWN);
                    }
                    //arcMemberView.updateDirection(ArcMemberView.Direction.TEXT_UP);
                }
            }

            arcMemberWindowParams.type = LayoutParams.TYPE_PHONE;
            arcMemberWindowParams.format = PixelFormat.RGBA_8888;
            arcMemberWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | LayoutParams.FLAG_NOT_FOCUSABLE;
            arcMemberWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
            arcMemberWindowParams.width = ArcMemberView.viewWidth;
            arcMemberWindowParams.height = ArcMemberView.viewHeight;
            windowManager.addView(arcMemberView, arcMemberWindowParams);
            LogUtils.e("hub position:" + arcHudView.getParams().x + "," + arcHudView.getParams().y+ "-[" + hubX + "," + hubY + "]");
            LogUtils.e("screenWidthHeight:" + screenWidth + "," + screenHeight);
//            int[] xy = new int[2];
//            arcHudView.getLocationOnScreen(xy);
//            int[] zn = new int[2];
//            arcHudView.getLocationInWindow(zn);
//            LogUtils.e("getLocationOnScreen:" + xy[0] + "," + xy[1]);
//            LogUtils.e("getLocationInWindow:" + zn[0] + "," + zn[1]);
            LogUtils.e("big position:" + arcMemberWindowParams.x + "," + arcMemberWindowParams.y + "-[" + ArcMemberView.viewWidth + "," + ArcMemberView.viewHeight + "]");
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeArcHudWindow(Context context) {
        if (arcHudView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(arcHudView);
            arcHudView = null;
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeArcMemberWindow(Context context) {
        if (arcMemberView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(arcMemberView);
            arcMemberView = null;
        }
    }

    public static void removeArcSettingsWindow(Context context) {
        if (arcSettingsView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(arcSettingsView);
            arcSettingsView = null;
        }
    }


    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return arcHudView != null;
    }

    public static boolean isArcHubShowing() {
        return arcHudView != null;
    }

    public static boolean isArcMemberShowing() {
        return arcMemberView != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
