package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.talkray.arcvoice.ArcRegion;
import com.wanmei.arcvoice.ArcVoiceHelper;

/**
 * Created by liang on 14/11/3.
 */
public class GameActivity extends Activity {

    public static final String ARC_APP_ID = "AFSIBQV2";
    public static final String ARC_APP_CREDENTIALS = "EP95V2XMWK1OCZVQNVCYIHBFLY1XGYWG";
    public static final ArcRegion ARC_REGION = ArcRegion.BEIJING;

    public static void start(Context context,String sessionId,String userId){
        Intent mIntent = new Intent(context,GameActivity.class);
        mIntent.putExtra("sessionId",sessionId);
        mIntent.putExtra("userId",userId);

        context.startActivity(mIntent);
    }

    ArcVoiceHelper mHelper;
    String mSessionId;
    String mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mSessionId = getIntent().getStringExtra("sessionId");
        mUserId = getIntent().getStringExtra("userId");

        (findViewById(R.id.btnChat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.start(mSessionId);
            }
        });
        mHelper = ArcVoiceHelper.getInstance(getApplicationContext());
        mHelper.init(ARC_APP_ID, ARC_APP_CREDENTIALS, ARC_REGION, mUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.hidden();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.stop();
    }
}
