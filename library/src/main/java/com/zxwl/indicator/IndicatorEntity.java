package com.zxwl.indicator;

import android.widget.ImageView;

public class IndicatorEntity {

    private String imageTxt = "";// 图片描述
    private String imageUrl;// 网络图片资源
    private int imgPath = -1;// 本地图片资源id
    private ImageView imageHolder; // 图片容器

    public ImageView getImageHolder() {
        return imageHolder;
    }

    public void setImageHolder(ImageView imageHolder) {
        this.imageHolder = imageHolder;
    }

    public String getImageTxt() {
        return imageTxt;
    }

    public void setImageTxt(String imageTxt) {
        this.imageTxt = imageTxt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

}
