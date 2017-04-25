package com.zxwl.simpleindicator;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxwl.indicator.IndicatorClickListener;
import com.zxwl.indicator.IndicatorEntity;
import com.zxwl.indicator.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndicatorClickListener {

    private SimpleIndicator indicator; // 自定义轮播图
    private ViewPager indicatorViewPager; // 轮播图片viewPager
    private LinearLayout pointsLayout; // 圆点指示器容器
    private TextView txt;
    private List<IndicatorEntity> entityList; // 轮播图bean集合

    String[] txts = new String[]{"图片1", "图片2", "图片3"};
    String[] urls = new String[]{"http://img.serholiu.com/blog/2011101401.jpg", "http://swordi.com/wp-content/uploads/2013/02/github_520-520x245.png", "http://static.oschina.net/uploads/img/201402/11084725_4sKZ.jpg"};
    int[] pics = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicatorViewPager = (ViewPager) findViewById(R.id.viewPager);
        pointsLayout = (LinearLayout) findViewById(R.id.points);
        txt = (TextView) findViewById(R.id.txt);

        initIndicator();
    }

    private void initIndicator() {
        entityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            IndicatorEntity entity = new IndicatorEntity();
            entity.setImageTxt(txts[i]);
            entity.setImageUrl(urls[i]);
            entity.setImgPath(pics[i]);
            entityList.add(entity);
        }

        indicator = new SimpleIndicator(this, indicatorViewPager, entityList);
        indicator.setPointLayout(pointsLayout);
        indicator.setPointSize(20);
        indicator.setCustomPoint(getResources().getDrawable(R.drawable.custom_point_select), getResources().getDrawable(R.drawable.custom_point_normal));
//        indicator.setPointColor(getResources().getColor(R.color.green), getResources().getColor(R.color.white));
        indicator.setTxtHolder(txt);
        indicator.setPagingDelay(3000);
//        indicator.setPagingSpeed(200);
        indicator.loadNetImage(true);
        indicator.setClickListener(this);
        indicator.init(); //必须在start前调用
        indicator.start();
    }

    @Override
    public void onIndicatorClick(IndicatorEntity entity) {
        Toast.makeText(this, entity.getImageTxt(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        indicator.destroyView();
    }
}
