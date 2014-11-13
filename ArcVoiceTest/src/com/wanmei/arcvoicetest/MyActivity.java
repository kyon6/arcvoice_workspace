package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.talkray.arcvoice.ArcRegion;
import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.utils.LogUtils;

import java.util.Random;

public class MyActivity extends Activity {

    EditText mSessionEditText, mUserIdEditText;
    Button mStartBtn;
    RadioGroup mRadioGroup;

    String userId = "1";
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
        mRadioGroup = (RadioGroup)findViewById(R.id.rb_orientation);
        mStartBtn = (Button) findViewById(R.id.btnStart);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = mSessionEditText.getText().toString().trim();
                //String userId = mUserIdEditText.getText().toString().trim();
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
                if(id == R.id.rb_hor){
                    isHor = true;
                }
                GameActivity.start(MyActivity.this, sessionId, userId,isHor);
            }
        });

        userId = String.valueOf((int) (Math.random()*10 + 1));
        mUserIdEditText.setText(userId);
        mHelper = ArcVoiceHelper.getInstance(getApplicationContext());
        mHelper.init(ARC_APP_ID, ARC_APP_CREDENTIALS, ARC_REGION, userId);

//        ListView mListView = (ListView)findViewById(R.id.mlistview);
//        String[] objects = new String[]{"A"};
//        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,objects);
//        mListView.setAdapter(mAdapter);
//        mListView.setStackFromBottom(true);
    }

    ArcVoiceHelper mHelper;

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
        mHelper.stop();
        super.onDestroy();
    }

    public static final String ARC_APP_ID = "AFSIBQV2";
    public static final String ARC_APP_CREDENTIALS = "EP95V2XMWK1OCZVQNVCYIHBFLY1XGYWG";
    public static final ArcRegion ARC_REGION = ArcRegion.VIRGINIA;
}
