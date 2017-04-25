package com.zxwl.indicator;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

public class IndicatorAdapter extends PagerAdapter {

    private List<IndicatorEntity> listADbeans;
    private IndicatorClickListener listener;

    public IndicatorAdapter(List<IndicatorEntity> listADbeans, IndicatorClickListener listener) {
        this.listADbeans = listADbeans;
        this.listener = listener;
    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        final IndicatorEntity bean = listADbeans.get(position % listADbeans.size());
        // 根据位置取出某一个ImageView
        ImageView view = bean.getImageHolder();
        // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        // 添加到容器
        container.addView(view);
        // 添加点击事件
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onIndicatorClick(bean);
                }
            }
        });
        return view;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    public synchronized void notifyImages(List<IndicatorEntity> listADbeans) {
        this.listADbeans = listADbeans;
        notifyDataSetChanged();
    }

}
