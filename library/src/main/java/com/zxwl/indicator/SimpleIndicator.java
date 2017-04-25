package com.zxwl.indicator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片轮播控制
 */
public class SimpleIndicator implements OnPageChangeListener {

    public static final String TAG = "SimpleIndicator";

    private Context mContext;
    private ViewPager mViewPager; //所在的ViewPager
    private List<IndicatorEntity> mEntities; //数据源
    private TextView mTxtHolder; //显示文字的控件
    private LinearLayout mPointsLayout;  //显示指示点的容器
    private IndicatorClickListener listener; //点击监听

    private IndicatorAdapter myPagerAdapter; //ViewPager适配器
    private int lastPoint = 0; //上一个位置
    boolean stat = false; //是否正在轮播
    private Timer timer;
    private TimerTask timerTask;
    //参数
    private boolean loadNetImage = false; //是否下载网络图片
    private Drawable pointSelectDrawable;
    private Drawable pointNormalDrawable;
    private int pointColorSelected = -1;
    private int pointColorNormal = -1;
    private int mPointSize = 10; //指示点的大小
    private int mPointMargin = 10; //指示点间距
    private long delay = 3000; // 轮播延迟：3秒
    private int defaultImageResId = R.drawable.nopic;

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 0) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
            if (what == 1) {
                myPagerAdapter.notifyImages(mEntities);
            }
        }
    };

    //////////////////////////////////////构造方法//////////////////////////////////////////////
    public SimpleIndicator(Context context, ViewPager viewPager, List<IndicatorEntity> entities,
                           TextView textView, LinearLayout pointsLayout,
                           IndicatorClickListener listener, boolean loadNetImage) {
        this.mContext = context;
        this.mEntities = entities;
        this.mViewPager = viewPager;
        this.mTxtHolder = textView;
        this.mPointsLayout = pointsLayout;
        this.listener = listener;
        this.loadNetImage = loadNetImage;
    }

    public SimpleIndicator(Context context, ViewPager viewPager, List<IndicatorEntity> entities) {
        this.mContext = context;
        this.mViewPager = viewPager;
        this.mEntities = entities;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        int newPosition = position % mEntities.size();
        // 取出文字
        String txt = mEntities.get(position % mEntities.size()).getImageTxt();
        if (mTxtHolder != null && txt != null) {
            mTxtHolder.setText(txt);
        }
        if (mPointsLayout != null) {
            // 设置对应的页面高亮
            mPointsLayout.getChildAt(newPosition).setEnabled(true);
            // 是上次的点不显示高亮
            mPointsLayout.getChildAt(lastPoint).setEnabled(false);
        }
        lastPoint = newPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            timer.cancel();
            stat = true;
        }
        if (state == 0) {
            if (stat) {
                start();
            }
            stat = false;
        }
    }

    /////////////////////////////////////////公开方法/////////////////////////////////////////
    public void init() {
        if (mEntities == null || mEntities.size() == 0) {
            return;
        }
        for (int i = 0; i < mEntities.size(); i++) {
            IndicatorEntity bean = mEntities.get(i);
            // 生成一个ImageView添加到ViewPager
            ImageView view = new ImageView(mContext);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (!loadNetImage) {
                if (bean.getImgPath() != -1) {
                    view.setBackgroundResource(bean.getImgPath());
                } else {
                    // 设置默认图片
                    view.setBackgroundResource(defaultImageResId);
                }
            }
            bean.setImageHolder(view);

            // 实例化指示点
            if (mPointsLayout != null) {
                ImageView point = new ImageView(mContext);

                StateListDrawable drawable = new StateListDrawable();
                if (pointSelectDrawable != null) {
                    drawable.addState(new int[]{android.R.attr.state_enabled}, pointSelectDrawable);
                } else {
                    GradientDrawable select = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.point_select);
                    if (pointColorSelected != -1) {
                        select.setColor(pointColorSelected);
                    }
                    drawable.addState(new int[]{android.R.attr.state_enabled}, select);
                }

                if (pointNormalDrawable != null) {
                    drawable.addState(new int[]{-android.R.attr.state_enabled}, pointNormalDrawable);
                } else {
                    GradientDrawable unselect = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.point_unselect);
                    if (pointColorNormal != -1) {
                        unselect.setColor(pointColorNormal);
                    }
                    drawable.addState(new int[]{-android.R.attr.state_enabled}, unselect);
                }
                point.setImageDrawable(drawable);

                LayoutParams params = new LayoutParams(dp2px(mContext, mPointSize), dp2px(mContext, mPointSize));
                params.leftMargin = dp2px(mContext, mPointMargin / 2);
                params.rightMargin = dp2px(mContext, mPointMargin / 2);
                point.setLayoutParams(params);
                // 将指示点添加到线性布局里
                mPointsLayout.addView(point);
                // 设置第一个高亮显示
                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                }
            }
        }
        // 设置适配器
        myPagerAdapter = new IndicatorAdapter(mEntities, listener);
        mViewPager.setAdapter(myPagerAdapter);
        // 添加页面改变监听
        mViewPager.addOnPageChangeListener(this);
        //设置位置
        int midPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mEntities.size();
        mViewPager.setCurrentItem(midPosition);
        //TODO 解决初次进入时第一个没有显示正常的问题
        if (mPointsLayout != null) {
            // 设置对应的页面高亮
            mPointsLayout.getChildAt(0).setEnabled(true);
        }

        //判断是否加载网络图片
        if (loadNetImage) {
            loadNetImages();
        }
    }

    //设置指示点布局
    public void setPointLayout(LinearLayout pointsLayout) {
        this.mPointsLayout = pointsLayout;
    }

    //设置指示点自定义属性
    public void setPointColor(int selected, int normal) {
        this.pointColorSelected = selected;
        this.pointColorNormal = normal;
    }

    public void setPointSize(int pointSize) {
        this.mPointSize = pointSize;
    }

    public void setPointMargin(int pointMargin) {
        this.mPointMargin = pointMargin;
    }

    public void setCustomPoint(Drawable pointSelectDrawable, Drawable pointNormalDrawable) {
        this.pointSelectDrawable = pointSelectDrawable;
        this.pointNormalDrawable = pointNormalDrawable;
    }

    //设置显示文本的容器
    public void setTxtHolder(TextView txtHolder) {
        this.mTxtHolder = txtHolder;
    }

    /**
     * 设置翻页间隔
     *
     * @param delay：翻页间隔
     */
    public void setPagingDelay(long delay) {
        this.delay = delay;
    }

    public void loadNetImage(boolean loadNetImage) {
        this.loadNetImage = loadNetImage;
    }

    /**
     * 设置翻页速度
     *
     * @param duration:翻页用时
     */
    public void setPagingSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(
                    mViewPager.getContext(), new AccelerateInterpolator());
            field.set(mViewPager, scroller);
            scroller.setmDuration(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDefaultImage(int defaultImage) {
        this.defaultImageResId = defaultImage;
    }

    public void setClickListener(IndicatorClickListener listener) {
        this.listener = listener;
    }

    /**
     * 开启轮播图
     */
    public void start() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, delay, delay);
    }

    public void destroyView() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timer.cancel();
        }
    }

    /////////////////////////////////////////私有方法/////////////////////////////////////////

    /**
     * 加载网络图片
     */
    private void loadNetImages() {
        for (IndicatorEntity entity : mEntities) {
            String url = entity.getImageUrl();
            Log.e(TAG, url);
            if (url != null && !TextUtils.isEmpty(url)) {
                ImageLoader.load(mContext, url, entity.getImageHolder());
                handler.sendEmptyMessage(1);
            }
        }
    }

    private static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
