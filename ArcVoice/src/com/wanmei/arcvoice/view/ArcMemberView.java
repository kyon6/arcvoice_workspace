package com.wanmei.arcvoice.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.model.Member;
import com.wanmei.arcvoice.utils.LogUtils;

import java.util.List;

public class ArcMemberView extends LinearLayout {
    RecyclerView mRecylerView;
    private MembersAdapter membersAdapter;
	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

	public ArcMemberView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.layout_member_view, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;

        mRecylerView = (RecyclerView) findViewById(R.id.mlistview);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // 设置垂直布局，目前仅支持LinearLayout(有垂直和横向)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置缓冲池最大循环使用view数
        mRecylerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        // 设置布局管理器
        mRecylerView.setLayoutManager(layoutManager);
        // 创建Adapter，并指定数据集
        membersAdapter = new MembersAdapter(getContext());
        // 设置Adapter
        mRecylerView.setAdapter(membersAdapter);
        // 设置默认动画
        mRecylerView.setItemAnimator(new DefaultItemAnimator());
        // 还有下面这上三种动画FlipDownItemAnimator, SlideItemAnimator, FromTopItemAnimator

    }

    public void updateAdapter(List<Member> list) {
        if (list != null) {
            membersAdapter.setData(list);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.e("bigWindow onTouch:" + event.getX() +":"+event.getY());
//        return super.onTouchEvent(event);
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void updateDirection(MembersAdapter.Direction direction){
        if(direction == MembersAdapter.Direction.TEXT_RIGHT){
            //change the adapter align
            membersAdapter.setDirection(MembersAdapter.Direction.TEXT_RIGHT);
            membersAdapter.notifyDataSetChanged();
        }else if(direction == MembersAdapter.Direction.TEXT_LEFT){
            //change the adapter align
            membersAdapter.setDirection(MembersAdapter.Direction.TEXT_LEFT);
            membersAdapter.notifyDataSetChanged();
        }
    }
}
