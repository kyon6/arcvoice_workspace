package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.talkray.arcvoice.ArcRegion;
import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.utils.LogUtils;


/**
 * Created by liang on 14/11/3.
 */
public class GameActivity extends Activity {

    public static final String ARC_APP_ID = "AFSIBQV2";
    public static final String ARC_APP_CREDENTIALS = "EP95V2XMWK1OCZVQNVCYIHBFLY1XGYWG";
    public static final ArcRegion ARC_REGION = ArcRegion.VIRGINIA;
    ArcVoiceHelper mHelper;
    String mSessionId;
    String mUserId;
    int mOrientation;

    Button mChatBtn;

    Handler mHandler = new Handler();

    public static void start(Context context, String sessionId, String userId, int orientation) {
        Intent mIntent = new Intent(context, GameActivity.class);
        mIntent.putExtra("sessionId", sessionId);
        mIntent.putExtra("userId", userId);
        mIntent.putExtra("orientation", orientation);

        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mSessionId = getIntent().getStringExtra("sessionId");
        mUserId = getIntent().getStringExtra("userId");
        mOrientation = getIntent().getIntExtra("orientation", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //noinspection ResourceType
        setRequestedOrientation(mOrientation);

        mChatBtn = (Button) findViewById(R.id.btnChat);
        mChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameActivity.this, "游戏操作输入", Toast.LENGTH_SHORT).show();
            }
        });

        mHelper = ArcVoiceHelper.getInstance(getApplicationContext());
        if (mOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mHelper.setOrientation(ArcVoiceHelper.Orientation.HORIZONTAL);
        } else {
            mHelper.setOrientation(ArcVoiceHelper.Orientation.VERTICAL);
        }


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHelper.startSession(mSessionId);
                initUserInfo();
            }
        });

        //Leon","http://ecx.images-amazon.com/images/I/41X0uJ4jbcL._AC_UY370_SX420.jpg"
    }

    private void initUserInfo() {
        mHelper.addPlayerInfo("1", "Ironman", "https://cdn4.iconfinder.com/data/icons/ironman_lin/512/ironman_III.png");
        mHelper.addPlayerInfo("2", "Hulk", "https://cdn4.iconfinder.com/data/icons/ironman_lin/512/ironman_III.png");
        mHelper.addPlayerInfo("3", "Batman", "https://cdn4.iconfinder.com/data/icons/ironman_lin/512/ironman_III.png");
        mHelper.addPlayerInfo("4", "Superman", "https://cdn4.iconfinder.com/data/icons/ironman_lin/512/ironman_III.png");
        mHelper.addPlayerInfo("5", "Flash", "https://cdn4.iconfinder.com/data/icons/ironman_lin/512/ironman_III.png");
        mHelper.addPlayerInfo("6", "Thor", "https://cdn4.iconfinder.com/data/icons/ironman_lin/512/ironman_III.png");

        mHelper.addPlayerInfo("7", "Captain America", "drawable://"+R.drawable.hugh);
        mHelper.addPlayerInfo("8", "Wonder Woman", "drawable://"+ R.drawable.hugh);
        mHelper.addPlayerInfo("9", "Green Lantern", "drawable://"+R.drawable.hugh);
        mHelper.addPlayerInfo("10", "Aquaman", "drawable://"+R.drawable.hugh);
    }

    @Override
    protected void onResume() {
        LogUtils.e("GameActivity onResume");
        mHelper.show();
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtils.e("GameActivity onPause");
        mHelper.hiddenAll();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        mHelper.quiteSession();
        super.onBackPressed();
    }
}
