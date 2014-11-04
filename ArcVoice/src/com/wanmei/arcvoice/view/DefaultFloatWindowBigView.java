package com.wanmei.arcvoice.view;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.talkray.arcvoice.*;
import com.wanmei.arcvoice.R;

import java.util.Iterator;
import java.util.Map;

/**
* Created by Youwei.Zhou on 11/3/2014.
*/
public class DefaultFloatWindowBigView extends ListView {

    public MembersAdapter getMembersAdapter() {
        return membersAdapter;
    }

    public void setMembersAdapter(MembersAdapter membersAdapter) {
        if(membersAdapter == null)
            return;
        this.membersAdapter = membersAdapter;
        setAdapter(membersAdapter);
    }

    private MembersAdapter membersAdapter;

    public DefaultFloatWindowBigView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundResource(R.drawable.bg_big);
        setSelector(R.drawable.transparent);
        setCacheColorHint(getResources().getColor(R.color.transparent));
        setDivider(null);
        if(membersAdapter==null)
        membersAdapter = new MembersAdapter(getContext(), R.layout.voice_member, R.id.playerName);
        setAdapter(membersAdapter);
    }

}
