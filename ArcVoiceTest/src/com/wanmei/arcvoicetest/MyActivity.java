package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.talkray.arcvoice.ArcRegion;
import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.utils.LogUtils;

public class MyActivity extends Activity {

    public static final String ARC_APP_ID = "AFSIBQV2";
    public static final String ARC_APP_CREDENTIALS = "EP95V2XMWK1OCZVQNVCYIHBFLY1XGYWG";
    public static final ArcRegion ARC_REGION = ArcRegion.BEIJING;
    EditText mSessionEditText;
    EditText mUserIdEditText;
    Button mStartBtn;
    RadioGroup mRadioGroup;
    ArcVoiceHelper mHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
    }

    private void initView() {
        mSessionEditText = (EditText) findViewById(R.id.et_sessionid);
        mUserIdEditText = (EditText) findViewById(R.id.et_userid);
        mRadioGroup = (RadioGroup) findViewById(R.id.rb_orientation);
        mStartBtn = (Button) findViewById(R.id.btnStart);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = mSessionEditText.getText().toString().trim();
                String userId = mUserIdEditText.getText().toString().trim();
                if (TextUtils.isEmpty(sessionId)) {
                    Toast.makeText(MyActivity.this, "pls input the sessionId!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int id = mRadioGroup.getCheckedRadioButtonId();
                int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
                if (id == R.id.rb_hor) {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else if (id == R.id.rb_ver)
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

                GameActivity.start(MyActivity.this, sessionId, userId, orientation);
            }
        });

        String userId = String.valueOf((int) (Math.random() * 10 + 1));
        mUserIdEditText.setText(userId);
        mHelper = ArcVoiceHelper.getInstance(getApplicationContext());
        mHelper.init(ARC_APP_ID, ARC_APP_CREDENTIALS, ARC_REGION, userId);
    }

    @Override
    protected void onResume() {
        LogUtils.e("MyActivity onResume");
        mHelper.show();
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtils.e("MyActivity onPause");
        mHelper.hiddenAll();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mHelper.stop();
        super.onDestroy();
    }
}
