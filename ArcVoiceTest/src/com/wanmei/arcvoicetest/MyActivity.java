package com.wanmei.arcvoicetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.wanmei.arcvoice.ArcVoice;
import com.wanmei.arcvoice.ArcWindowService;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoice.getInstance(getApplicationContext()).start();
            }
        });

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoice.getInstance(getApplicationContext()).show();
            }
        });

        findViewById(R.id.btnHidden).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoice.getInstance(getApplicationContext()).hidden();
            }
        });

        findViewById(R.id.btnStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcVoice.getInstance(getApplicationContext()).stop();
            }
        });
    }
}
