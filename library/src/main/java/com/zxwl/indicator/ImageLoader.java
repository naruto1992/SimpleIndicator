package com.zxwl.indicator;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageLoader {

    public static void load(Context context, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        Glide.with(context).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void load(Activity activity, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    /**
     * 加载圆角图片
     *
     * @param url
     * @param radius
     * @param iv
     */
    public static void loadRadiusRoundedCorners(Context context, String url, int radius, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        Glide.with(context)
                .load(url)
                .crossFade()
                .centerCrop()
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0,
                        RoundedCornersTransformation.CornerType.ALL))
                .into(iv);
    }

    public static void loadRadiusRoundedCorners(Context context, Integer resId, int radius, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        Glide.with(context).load(resId).crossFade().centerCrop().thumbnail(0.5f)
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0,
                        RoundedCornersTransformation.CornerType.ALL)).into(iv);
    }


    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param iv
     */
    public static void loadCircle(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(iv);
    }


    public static void loadAll(Context context, String url, ImageView iv) {    //不缓存，全部从网络加载
        Glide.with(context).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
    }

    public static void loadAll(Activity activity, String url, ImageView iv) {    //不缓存，全部从网络加载
        Glide.with(activity).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
    }


}
