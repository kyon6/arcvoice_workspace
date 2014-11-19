package com.wanmei.arcvoice.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by liang on 14/10/31.
 */
public class DeviceUtils {

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    private static int navigationBarHeight;
    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context mContext) {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public static int getNavigationBarHeight(Context mContext){
        if(navigationBarHeight == 0){
            try{
                Resources resources = mContext.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    return resources.getDimensionPixelSize(resourceId);
                }
                return 0;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return navigationBarHeight;
    }

    public static boolean isServiceRunning(Context context, String packageName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
