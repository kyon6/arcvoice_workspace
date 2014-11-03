package com.wanmei.arcvoice.utils;

import java.util.Map;

/**
 * 日志工具类
 * User: pliliang
 */
public class LogUtils {

    private static final String TAG = "ArcVoiceHelper";

    public static final boolean IS_DEBUG = true;
    
    private LogUtils() {
    }

    public static void v(String msg) {
        if (IS_DEBUG) {
            android.util.Log.v(TAG, buildMessage(msg));
        }
    }

    public static void d(String msg) {
        if (IS_DEBUG) {
            android.util.Log.d(TAG, buildMessage(msg));
        }
    }

    public static void i(String msg) {
        if (IS_DEBUG) {
            android.util.Log.i(TAG, buildMessage(msg));
        }
    }

    public static void w(String msg) {
        if (IS_DEBUG) {
            android.util.Log.w(TAG, buildMessage(msg));
        }
    }

    public static void e(String msg) {
        if (IS_DEBUG) {
            android.util.Log.e(TAG, buildMessage(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            android.util.Log.e(tag, buildMessage(msg));
        }
    }

    public static void es(String... msg) {
        StringBuilder sb = new StringBuilder();
        for (String s : msg) {
            sb.append(s).append("=");
        }
        String result = "";
        if (sb.length() > 1) {
            result = sb.substring(0, sb.length() - 1);
        }
        android.util.Log.e(TAG, buildMessage(result));
    }

    public static void log_request(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (String s : params.keySet()) {
            sb.append(s).append("=").append(params.get(s)).append("&");
        }
        String s = sb.substring(0, sb.length() - 1);
        if (IS_DEBUG) {
            android.util.Log.w(TAG, s);
        }
    }

    private static String buildMessage(String msg) {
        /*StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return new StringBuilder()
                .append(caller.getClassName())
                .append(".")
                .append(caller.getMethodName())
                .append("(): ")
                .append(msg).toString();*/
        return msg;
    }
}
