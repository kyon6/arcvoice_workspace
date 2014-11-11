package com.wanmei.arcvoice;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.wanmei.arcvoice.utils.LogUtils;
import com.wanmei.arcvoice.view.ArcMemberView;
import com.wanmei.arcvoice.view.ArcHubView;
import com.wanmei.arcvoice.view.MembersAdapter;

/**
 * ArcVoice悬浮框 界面类
 * Created by liang on 14/10/31.
 */
public class ArcWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static ArcHubView arcHubView;
    /**
     * 大悬浮窗View的实例
     */
    private static ArcMemberView arcMemberView;
    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams arcHubWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams arcMemberWindowParams;
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    public static ArcHubView getArcHubView() {
        return arcHubView;
    }

    public static ArcMemberView getArcMemberView(){
        return arcMemberView;
    }

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createArcHubWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (arcHubView == null) {
            arcHubView = new ArcHubView(context);
            if (arcHubWindowParams == null) {
                arcHubWindowParams = new LayoutParams();
                arcHubWindowParams.type = LayoutParams.TYPE_PHONE;
                arcHubWindowParams.format = PixelFormat.RGBA_8888;
                arcHubWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                arcHubWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                arcHubWindowParams.width = ArcHubView.viewWidth;
                arcHubWindowParams.height = arcHubView.viewHeight;
                arcHubWindowParams.x = 0;
                arcHubWindowParams.y = screenHeight/2;
            }
            arcHubView.setParams(arcHubWindowParams);
            windowManager.addView(arcHubView, arcHubWindowParams);
            LogUtils.e("==HubView:" + arcHubView);
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param context
     *            必须为应用程序的Context.
     */
    public static void createArcMemberWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        ArcVoiceHelper.Orientation orientation = ArcVoiceHelper.getInstance(context).getOrientation();
        if (arcMemberView == null) {
            arcMemberView = new ArcMemberView(context,orientation);
            arcMemberWindowParams = new LayoutParams();
            /**
             * check the hub view postion
             */
            WindowManager.LayoutParams mHubParams = arcHubView.getParams();
            int hubX = mHubParams.x;
            int hubY = mHubParams.y;

            //判断ArcHub所在位置标记。0：左上，1：右上，2：左下，3：右下
            if(orientation == ArcVoiceHelper.Orientation.VERTICAL) {
                if (hubX <= screenWidth / 2) {
                    arcMemberWindowParams.x = mHubParams.x;
                    if (hubY <= screenHeight / 2) {//左上
                        arcMemberWindowParams.y = mHubParams.y + ArcHubView.viewHeight;
                    } else {//左下
                        arcMemberWindowParams.y = mHubParams.y - ArcMemberView.viewHeight;
                    }
                    arcMemberView.updateDirection(MembersAdapter.Direction.TEXT_RIGHT);
                } else {
                    arcMemberWindowParams.x = mHubParams.x - ArcMemberView.viewWidth + ArcHubView.viewWidth;
                    if (hubY <= screenWidth / 2) {//右上
                        arcMemberWindowParams.y = mHubParams.y + ArcHubView.viewHeight;
                    } else {//右下
                        arcMemberWindowParams.y = mHubParams.y - ArcMemberView.viewHeight;
                    }
                    arcMemberView.updateDirection(MembersAdapter.Direction.TEXT_LEFT);
                }
            }else{
                if(hubY <= screenHeight / 2){
                    arcMemberWindowParams.y = mHubParams.y;
                    if (hubX <= screenWidth / 2) {//左上
                        arcMemberWindowParams.x = mHubParams.x + ArcHubView.viewWidth;
                    } else {//右上
                        arcMemberWindowParams.x = mHubParams.x - ArcMemberView.viewWidth;
                    }
                    arcMemberView.updateDirection(MembersAdapter.Direction.TEXT_DOWN);
                }else{
                    arcMemberWindowParams.y = mHubParams.y - arcMemberView.viewHeight + arcHubView.viewHeight;
                    if (hubX <= screenWidth / 2) {//左下
                        arcMemberWindowParams.x = mHubParams.x + ArcHubView.viewWidth;
                    } else {//右下
                        arcMemberWindowParams.x = mHubParams.x - ArcMemberView.viewWidth;
                    }
                    arcMemberView.updateDirection(MembersAdapter.Direction.TEXT_UP);
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
            LogUtils.e("hub position:" + arcHubView.getParams().x+","+arcHubView.getParams().y);
            LogUtils.e("big position:" + arcMemberWindowParams.x+","+arcMemberWindowParams.y+"-["+ArcMemberView.viewWidth+","+ArcMemberView.viewHeight+"]");
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeArcHubWindow(Context context) {
        if (arcHubView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(arcHubView);
            arcHubView = null;
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context
     *            必须为应用程序的Context.
     */
    public static void removeArcMemberWindow(Context context) {
        if (arcMemberView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(arcMemberView);
            arcMemberView = null;
        }
    }


    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return arcHubView != null;
    }

    public static boolean isArcHubShowing(){
        return arcHubView != null;
    }

    public static boolean isArcMemberShowing(){
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
