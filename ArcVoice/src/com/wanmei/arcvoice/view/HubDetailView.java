package com.wanmei.arcvoice.view;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.model.Player;
import com.wanmei.arcvoice.utils.DeviceUtils;
import com.wanmei.arcvoice.utils.LogUtils;

import java.util.List;

public class HubDetailView extends RelativeLayout {

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;
    View mMainView;
    View mHubView;
    RecyclerView mRecylerView;
    boolean isListViewShow = false;
    boolean isDrag = false;
    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;
    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;
    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;
    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;
    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;
    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;
    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;
    private MembersAdapter membersAdapter;

    public HubDetailView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMainView = LayoutInflater.from(context).inflate(R.layout.layout_hub_detail_view, this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;

        mHubView = findViewById(R.id.hub);
        mHubView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                isDrag = !isDrag;
                LogUtils.e("HubView click!");
//                Toast.makeText(getContext(),"hub click!",Toast.LENGTH_SHORT).show();
//                if (isListViewShow) {
//                    ArcVoiceHelper.getInstance(getContext()).hiddenAvatars();
//                    mRecylerView.setVisibility(View.GONE);
//                    isListViewShow = false;
//                } else {
//                    ArcVoiceHelper.getInstance(getContext()).showAvatars();
//                    mRecylerView.setVisibility(View.VISIBLE);
//                    isListViewShow = true;
//                }
                ArcVoiceHelper.getInstance(getContext()).doShowAvatars();
            }
        });
        mHubView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LogUtils.e("HubView is longClick");
                isDrag = true;
                return true;
            }
        });
        mRecylerView = (RecyclerView) findViewById(R.id.mlistview);
//        mRecylerView.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LogUtils.e("listview ontouch");
////                HubDetailView.this.onTouchEvent(event);
//                return false;
//            }
//        });
//        mRecylerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "item click:" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        showDate();
    }

//    public MembersAdapter getMembersAdapter() {
//        return membersAdapter;
//    }
private void showDate() {
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

    public void updateAdapter(List<Player> list) {
        if (list == null) {
            membersAdapter.clear();
        } else {
            membersAdapter.setData(list);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isDrag) {
            return onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - DeviceUtils.getStatusBarHeight(getContext());
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - DeviceUtils.getStatusBarHeight(getContext());
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - DeviceUtils.getStatusBarHeight(getContext());
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {

                } else {
                    /*
                     * release the HUD will anchor to the left or right
                     */
                    /*int screenWidth = windowManager.getDefaultDisplay().getWidth();
                    int screenHeight = windowManager.getDefaultDisplay().getHeight();

                    //todo 判断是横屏显示还是竖屏显示
                    boolean isPortrait = true;
                    if(isPortrait){
                        if(xInScreen < screenWidth/2){
                            mParams.x = 0;
                        }else{
                            mParams.x = (int)(screenWidth - xInView);
                        }
                        mParams.y = (int) (yInScreen - yInView);
                    }else{
                        if(yInScreen < screenHeight/2){
                            mParams.y = 0;
                        }else{
                            mParams.y = (int)(screenHeight - yInView);
                        }
                        mParams.x = (int)(xInScreen - xInView);
                    }

                    windowManager.updateViewLayout(this, mParams);*/
                }
                isDrag = false;
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }
}