package com.wanmei.arcvoice.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanmei.arcvoice.ArcWindowManager;
import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.utils.DeviceUtils;

public class FloatWindowSmallView extends RelativeLayout {

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

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

    public DefaultFloatWindowBigView getDefaultBigView() {
        return defaultBigView;
    }

    private DefaultFloatWindowBigView defaultBigView = new DefaultFloatWindowBigView(getContext());

    public void setBigWindow(FloatWindowBigView bigWindow) {
        this.bigWindow = bigWindow;
    }

    public View getBigWindow() {
        return bigWindow;
    }

    private View bigWindow;
    private WindowManager.LayoutParams bigWindowParams;

    public FloatWindowSmallView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        View percentView = findViewById(R.id.hub);
//        percentView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openBigWindow();
//            }
//        });
//        addBigView();
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
                    openBigWindow();
                }else{
                    /*
                     * release the HUD will anchor to the left or right
                     */
                    int screenWidth = windowManager.getDefaultDisplay().getWidth();
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

                    windowManager.updateViewLayout(this, mParams);
                }
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

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    private void openBigWindow() {
//        addBigView();
        if (bigWindow!=null) {
            removeView(bigWindow);
            bigWindow = null;
        }
        else
            addBigView();
    }

    private void addBigView() {
//        int screenWidth = windowManager.getDefaultDisplay().getWidth();
//        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (bigWindow == null) {
//            bigWindow = new FloatWindowBigView(getContext());
            bigWindow = defaultBigView;
//            if (bigWindowParams == null) {
//                bigWindowParams = new WindowManager.LayoutParams();
//                bigWindowParams.x = screenWidth / 2 - FloatWindowBigView.viewWidth / 2;
//                bigWindowParams.y = screenHeight / 2 - FloatWindowBigView.viewHeight / 2;
//                bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//                bigWindowParams.format = PixelFormat.RGBA_8888;
//                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
//                bigWindowParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//                bigWindowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            }
//            bigWindow.setLayoutParams(bigWindowParams);
        }
        setGravity(Gravity.CENTER);
        addView(bigWindow, 0);
        invalidate();
        defaultBigView.getMembersAdapter().notifyDataSetChanged();
    }


}