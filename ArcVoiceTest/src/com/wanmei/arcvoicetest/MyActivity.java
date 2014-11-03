package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.talkray.arcvoice.ArcRegion;
import com.wanmei.arcvoice.ArcVoiceHelper;

public class MyActivity extends Activity {

    public static final String ARC_APP_ID = "AFSIBQV2";
    public static final String ARC_APP_CREDENTIALS = "EP95V2XMWK1OCZVQNVCYIHBFLY1XGYWG";
    public static final ArcRegion ARC_REGION = ArcRegion.BEIJING;
    public static final String USER_ID = "123";

    ArcVoiceHelper mHelper;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mHelper = ArcVoiceHelper.getInstance(getApplicationContext());
        mHelper.init(ARC_APP_ID,ARC_APP_CREDENTIALS,ARC_REGION,USER_ID);
        mHelper.start();

        initView();
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

    private void initView() {
        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoiceHelper.getInstance(getApplicationContext()).start();
            }
        });

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoiceHelper.getInstance(getApplicationContext()).show();
            }
        });

        findViewById(R.id.btnHidden).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoiceHelper.getInstance(getApplicationContext()).hidden();
            }
        });

        findViewById(R.id.btnStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoiceHelper.getInstance(getApplicationContext()).stop();
            }
        });
    }
}
