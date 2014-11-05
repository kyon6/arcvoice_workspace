package com.wanmei.arcvoice;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.wanmei.arcvoice.view.HubDetailView;

/**
 * ArcVoice悬浮框 界面类
 * Created by liang on 14/10/31.
 */
public class ArcWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static HubDetailView hubDetailView;
    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams hubViewWindowParams;
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;
    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;

    //    public static FloatWindowSmallView getHubDetailView() {
//        return hubDetailView;
//    }
    public static HubDetailView getHubDetailView() {
        return hubDetailView;
    }

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createHubDetailWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (hubDetailView == null) {
            hubDetailView = new HubDetailView(context);
            if (hubViewWindowParams == null) {
                hubViewWindowParams = new LayoutParams();
                hubViewWindowParams.type = LayoutParams.TYPE_PHONE;
                hubViewWindowParams.format = PixelFormat.RGBA_8888;
                hubViewWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                hubViewWindowParams.gravity = Gravity.START | Gravity.TOP;
                hubViewWindowParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;//FloatWindowSmallView.viewWidth;
                hubViewWindowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                hubViewWindowParams.x = 0;
                hubViewWindowParams.y = screenHeight / 2;
            }
            hubDetailView.setParams(hubViewWindowParams);
            windowManager.addView(hubDetailView, hubViewWindowParams);
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeHubDetailWindow(Context context) {
        if (hubDetailView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(hubDetailView);
            hubDetailView = null;
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return hubDetailView != null;
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

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context 可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }
}
