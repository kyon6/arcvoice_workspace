package com.wanmei.arcvoice;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Youwei.Zhou on 11/13/2014.
 * save the user modify data of ArcVoice
 */
public class ArcVoicePersistenceData {
    private static volatile ArcVoicePersistenceData INSTANCE;
    private Context context;
    private SharedPreferences sharedPref;

    public static ArcVoicePersistenceData getInstance() {
        if (INSTANCE == null) {
            synchronized (ArcVoicePersistenceData.class) {
                if (INSTANCE == null)
                    INSTANCE = new ArcVoicePersistenceData();
            }
        }
        return INSTANCE;
    }

    //need init before use it
    public void init(Context context) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences("ArcVoiceSDK_user_insist", Context.MODE_PRIVATE);
    }

    public boolean getArcMicEnable() {
        return sharedPref.getBoolean("Arc_Mic_Enable", true);
    }

    public void setArcMicEnable(boolean enable) {
        sharedPref.edit().putBoolean("Arc_Mic_Enable", enable).apply();
    }

    public boolean getArcEnable() {
        return sharedPref.getBoolean("Arc_Enable", true);
    }

    public void setArcEnable(boolean enable) {
        sharedPref.edit().putBoolean("Arc_Enable", enable).apply();
    }

}
