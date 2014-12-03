package com.wanmei.arcvoice.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanmei.arcvoice.R;

/**
 * Created by cegrano on 2014/12/3.
 * tips view
 */
public class ArcHelpView extends LinearLayout{

    public ArcHelpView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_help_view, this);
        initView();
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.help_viewpager);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.help_position);

        final ViewSwitcherHelper mSwitcherHelper = new ViewSwitcherHelper(getContext(), dotLayout);
        PagerAdapter adapter = new ArcHelpAdapter(getContext());
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                mSwitcherHelper.setCurrent(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mSwitcherHelper.setViewSwitcherTip(4,0);
    }

    class ArcHelpAdapter extends PagerAdapter{
        private final Context mContext;
        int res[] = {R.layout.layout_help_tips_1,R.layout.layout_help_tips_2,
                R.layout.layout_help_tips_3,R.layout.layout_help_tips_4};

        ArcHelpAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tx = new TextView(mContext);
            tx.setText("JJJJJJJJJJ");
            return tx;
//            return LayoutInflater.from(mContext).inflate(res[position], null);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class ViewSwitcherHelper {
        Drawable posOff;
        Drawable posOn;
        ViewGroup viewGroup;
        Context context;
        int currentPos;

        public ViewSwitcherHelper(Context context, ViewGroup layout) {
            viewGroup = layout;
            this.context = context;
            posOff = context.getResources().getDrawable(R.drawable.loading_dot_normal);
            posOn = context.getResources().getDrawable(R.drawable.loading_dot_select);
        }

        public void setViewSwitcherTip(int count, int current) {
            this.currentPos = current;
            viewGroup.removeAllViews();
            for (int i = 0; i < count; i++) {
                ImageView view = getPositionView(i == current);
                view.setTag(i);
                viewGroup.addView(view);
            }
        }

        public void setOnAndOffDrawable(int onReourceId, int offReourceId) {// 设置选中和未选中的图片
            posOff = context.getResources().getDrawable(offReourceId);
            posOn = context.getResources().getDrawable(onReourceId);
        }

        private ImageView getPositionView(boolean isOn) {
            ImageView view = new ImageView(context);
            view.setImageDrawable(isOn ? posOn : posOff);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 5, 0);
            view.setLayoutParams(lp);
            return view;
        }

        public void setCurrent(int current) {
            if (current >= viewGroup.getChildCount())
                return;
            viewGroup.removeViewAt(currentPos);
            viewGroup.addView(getPositionView(false), currentPos);
            this.currentPos = current;
            viewGroup.removeViewAt(current);
            viewGroup.addView(getPositionView(true), current);
        }
    }
}
