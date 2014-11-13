package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.talkray.arcvoice.ArcRegion;
import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.utils.LogUtils;

public class MyActivity extends Activity {

    public static final String ARC_APP_ID = "AFSIBQV2";
    public static final String ARC_APP_CREDENTIALS = "EP95V2XMWK1OCZVQNVCYIHBFLY1XGYWG";
    public static final ArcRegion ARC_REGION = ArcRegion.VIRGINIA;
    EditText mSessionEditText, mUserIdEditText;
    Button mStartBtn;
    RadioGroup mRadioGroup;
    String userId = "1";
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
                    ;
                    return;
                }
//                if (TextUtils.isEmpty(userId)) {
//                    Toast.makeText(MyActivity.this, "pls input the userId!", Toast.LENGTH_SHORT).show();
//                    ;
//                    return;
//                }
                int id = mRadioGroup.getCheckedRadioButtonId();
                boolean isHor = false;
                if (id == R.id.rb_hor) {
                    isHor = true;
                }
                mHelper.init(ARC_APP_ID, ARC_APP_CREDENTIALS, ARC_REGION, userId);
                GameActivity.start(MyActivity.this, sessionId, userId, isHor);
            }
        });

        mUserIdEditText.setText(String.valueOf((int) (Math.random() * 10 + 1)));
        mHelper = ArcVoiceHelper.getInstance(getApplicationContext());

//        ListView mListView = (ListView)findViewById(R.id.mlistview);
//        String[] objects = new String[]{"A"};
//        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,objects);
//        mListView.setAdapter(mAdapter);
//        mListView.setStackFromBottom(true);
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
        mHelper.hiddenAll();
    }

    @Override
    protected void onDestroy() {
        mHelper.stop();
        super.onDestroy();
    }
}
