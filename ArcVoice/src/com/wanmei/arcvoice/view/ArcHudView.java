package com.wanmei.arcvoice.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.utils.DeviceUtils;
import com.wanmei.arcvoice.utils.LogUtils;

public class ArcHudView extends RelativeLayout {

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;
    View mMainView;
    View mHudView;
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
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕高度
     */
    private int screenHeight;


    public ArcHudView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();

        mMainView = LayoutInflater.from(context).inflate(R.layout.layout_hud_detail_view, this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;

        mHudView = findViewById(R.id.hub);
        mHudView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("HubView click!");
                ArcVoiceHelper.getInstance(getContext()).onClick();
            }
        });
        mHudView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LogUtils.e("HubView is longClick");
                isDrag = true;
                ArcVoiceHelper.getInstance(getContext()).hiddenOthers();
                return true;
            }
        });
       /* mRecylerView = (RecyclerView) findViewById(R.id.mlistview);
        showDate();*/
    }

//    public MembersAdapter getMembersAdapter() {
//        return membersAdapter;
//    }

    /*private void showDate() {
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
            mRecylerView.setVisibility(GONE);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = DensityUtils.dip2px(getContext(),50);
            setLayoutParams(params);
        } else {
            membersAdapter.setData(list);
            mRecylerView.setVisibility(VISIBLE);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = DensityUtils.dip2px(getContext(),150);
            setLayoutParams(params);
        }
    }*/

   /* @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void updateDirection(){
        if(mParams.x < screenWidth/2){
            // change the hub align
            RelativeLayout.LayoutParams params = (LayoutParams) mRecylerView.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_RIGHT);
            params.addRule(RelativeLayout.ALIGN_LEFT,R.id.hub);

            mRecylerView.setLayoutParams(params);
            //change the adapter align
            membersAdapter.setDirection(MembersAdapter.Direction.TEXT_RIGHT);
            membersAdapter.notifyDataSetChanged();
        }else{
            // change the hub align
            RelativeLayout.LayoutParams params = (LayoutParams) mRecylerView.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_LEFT);
            params.addRule(RelativeLayout.ALIGN_RIGHT,R.id.hub);

            mRecylerView.setLayoutParams(params);
            //change the adapter align
            membersAdapter.setDirection(MembersAdapter.Direction.TEXT_LEFT);
            membersAdapter.notifyDataSetChanged();
        }
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isDrag) {
            return onTouchEvent(ev);
        }
        xInView = ev.getX();
        yInView = ev.getY();
        xDownInScreen = ev.getRawX();
        yDownInScreen = ev.getRawY() - DeviceUtils.getStatusBarHeight(getContext());
        xInScreen = ev.getRawX();
        yInScreen = ev.getRawY() - DeviceUtils.getStatusBarHeight(getContext());
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
                    //ArcVoiceHelper.getInstance(getContext()).onClick();
                } else {
                    /*
                     //靠边停靠
                     //release the HUD will anchor to the left or right
                    int screenWidth = windowManager.getDefaultDisplay().getWidth();
                    int screenHeight = windowManager.getDefaultDisplay().getHeight();

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

    public WindowManager.LayoutParams getParams() {
        return mParams;
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