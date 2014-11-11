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
    boolean isHorizontal;

    Button mChatBtn;

    Handler mHandler = new Handler();

    public static void start(Context context, String sessionId, String userId,boolean isHorizontal) {
        Intent mIntent = new Intent(context, GameActivity.class);
        mIntent.putExtra("sessionId", sessionId);
        mIntent.putExtra("userId", userId);
        mIntent.putExtra("horizontal",isHorizontal);

        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("onCreate");
        setContentView(R.layout.activity_game);

        mSessionId = getIntent().getStringExtra("sessionId");
        mUserId = getIntent().getStringExtra("userId");
        isHorizontal = getIntent().getBooleanExtra("horizontal",false);
        if(isHorizontal){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mChatBtn = (Button) findViewById(R.id.btnChat);
        mChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mHelper.start(mSessionId);
//                mChatBtn.setVisibility(View.INVISIBLE);
                Toast.makeText(GameActivity.this,"游戏操作输入",Toast.LENGTH_SHORT).show();
            }
        });

        mHelper = ArcVoiceHelper.getInstance(getApplicationContext());
        mHelper.init(ARC_APP_ID, ARC_APP_CREDENTIALS, ARC_REGION, mUserId);
        if(isHorizontal){
            mHelper.setOrientation(ArcVoiceHelper.Orientation.HORIZONTAL);
        }else{
            mHelper.setOrientation(ArcVoiceHelper.Orientation.VERTICAL);
        }


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHelper.start(mSessionId);
                initUserInfo();
            }
        });
        //Leon","http://ecx.images-amazon.com/images/I/41X0uJ4jbcL._AC_UY370_SX420.jpg"
    }

    private void initUserInfo(){
        mHelper.addPlayerInfo(mUserId,"Leon","https://cdn4.iconfinder.com/data/icons/ironman_lin/512/ironman_III.png");
        mHelper.addPlayerInfo("99","周有为","http://wallpho.com/download.php?filename=8589130450113-hulk-face-drawing-wallpaper-hd.jpg");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume");
        mHelper.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause");
        mHelper.hidden();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.stop();
    }
}
