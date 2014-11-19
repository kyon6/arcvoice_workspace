package com.wanmei.arcvoice.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.adapter.MembersListAdapter;
import com.wanmei.arcvoice.model.Member;
import com.wanmei.arcvoice.utils.DensityUtils;
import com.wanmei.arcvoice.widget.HorizontalListView;

import java.util.List;

public class ArcMemberView extends LinearLayout {
	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

    private ListView mListView;
    private HorizontalListView mHorizontalListView;
    private MembersListAdapter mAdapter;
    private ArcVoiceHelper.Orientation mOrientation;
    private boolean isScrolling;

    public ArcMemberView(final Context context, ArcVoiceHelper.Orientation orientation) {
        super(context);
		LayoutInflater.from(context).inflate(R.layout.layout_member_view, this);
		View view = findViewById(R.id.big_window_layout);

        LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
        mOrientation = orientation;
        if(mOrientation == ArcVoiceHelper.Orientation.VERTICAL){
            if(ArcVoiceHelper.getInstance(context).isNameShowing()){
                params.width = DensityUtils.dip2px(context,100);
            }else{
                params.width = DensityUtils.dip2px(context,50);
            }
            params.height = DensityUtils.dip2px(context,200);
        }else{
            params.width = DensityUtils.dip2px(context,300);
            params.height = DensityUtils.dip2px(context,60);
        }
        view.setLayoutParams(params);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;

        mListView = (ListView)findViewById(R.id.mListView);
        mHorizontalListView = (HorizontalListView)findViewById(R.id.mHorizontalListView);

        mAdapter = new MembersListAdapter(getContext());

        if(mOrientation == ArcVoiceHelper.Orientation.VERTICAL){
            mHorizontalListView.setVisibility(View.GONE);
            mListView.setAdapter(mAdapter);
        }else{
            mListView.setVisibility(View.GONE);
            mHorizontalListView.setAdapter(mAdapter);
        }
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrolling = scrollState == SCROLL_STATE_TOUCH_SCROLL;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mHorizontalListView.setOnScrollStateChangedListener(new HorizontalListView.OnScrollStateChangedListener() {
            @Override
            public void onScrollStateChanged(ScrollState scrollState) {
                isScrolling = scrollState == ScrollState.SCROLL_STATE_TOUCH_SCROLL;
            }
        });
    }

    public void updateAdapter(List<Member> list) {
        if (list != null && mAdapter != null) {
            mAdapter.setData(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void updateNameShowing(){
        if(mAdapter != null){
            mAdapter.updateNameShowing();
            mAdapter.notifyDataSetChanged();
        }
    }

//    public void showMemberName(){
//        if(mAdapter != null){
//            mAdapter.setNameShowing(true);
//            mAdapter.notifyDataSetChanged();
//        }
//    }

    public void updateHubPosition(HubPosition position){
        if(mOrientation == ArcVoiceHelper.Orientation.VERTICAL){
            if(position == HubPosition.LEFT_UP){
                mListView.setStackFromBottom(false);
                mAdapter.setDirection(Direction.TEXT_RIGHT);
            }else if(position == HubPosition.LEFT_DOWN){
                mListView.setStackFromBottom(true);
                mAdapter.setDirection(Direction.TEXT_RIGHT);
            }else if(position == HubPosition.RIGHT_UP){
                mListView.setStackFromBottom(false);
                mAdapter.setDirection(Direction.TEXT_LEFT);
            }else if(position == HubPosition.RIGHT_DOWN){
                mListView.setStackFromBottom(true);
                mAdapter.setDirection(Direction.TEXT_LEFT);
            }
        }else{
            if(position == HubPosition.LEFT_UP){
                mListView.setStackFromBottom(false);
                mAdapter.setDirection(Direction.TEXT_DOWN);
            }else if(position == HubPosition.RIGHT_UP){
                mListView.setStackFromBottom(false);
                mAdapter.setDirection(Direction.TEXT_DOWN);
            }else if(position == HubPosition.LEFT_DOWN){
                mListView.setStackFromBottom(true);
                mAdapter.setDirection(Direction.TEXT_UP);
            }else if(position == HubPosition.RIGHT_DOWN){
                mListView.setStackFromBottom(true);
                mAdapter.setDirection(Direction.TEXT_UP);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /*public void updateDirection(Direction direction){
        if(direction == Direction.TEXT_RIGHT){
            mAdapter.setDirection(Direction.TEXT_RIGHT);
        }else if(direction == Direction.TEXT_LEFT){
            mAdapter.setDirection(Direction.TEXT_LEFT);
        }else if(direction == Direction.TEXT_DOWN){
            mAdapter.setDirection(Direction.TEXT_DOWN);
        }else if(direction == Direction.TEXT_UP){
            mAdapter.setDirection(Direction.TEXT_UP);
        }
        mAdapter.notifyDataSetChanged();
    }*/

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        LogUtils.e("bigWindow onTouch:" + event.getX() +":"+event.getY());
////        return super.onTouchEvent(event);
//        return false;
//    }

    public static enum Direction {TEXT_LEFT, TEXT_RIGHT, TEXT_UP, TEXT_DOWN}

    public static enum HubPosition{LEFT_UP,LEFT_DOWN,RIGHT_UP,RIGHT_DOWN}
}
