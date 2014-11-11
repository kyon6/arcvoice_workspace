package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MyActivity extends Activity {

    EditText mSessionEditText, mUserIdEditText;
    Button mStartBtn;
    RadioGroup mRadioGroup;

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
                String userId = mUserIdEditText.getText().toString().trim();
                if (TextUtils.isEmpty(sessionId)) {
                    Toast.makeText(MyActivity.this, "pls input the sessionId!", Toast.LENGTH_SHORT).show();
                    ;
                    return;
                }
                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(MyActivity.this, "pls input the userId!", Toast.LENGTH_SHORT).show();
                    ;
                    return;
                }
                int id = mRadioGroup.getCheckedRadioButtonId();
                boolean isHor = false;
                if(id == R.id.rb_hor){
                    isHor = true;
                }
                GameActivity.start(MyActivity.this, sessionId, userId,isHor);
            }
        });
    }
}
