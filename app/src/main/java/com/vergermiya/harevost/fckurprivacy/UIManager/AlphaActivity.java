package com.vergermiya.harevost.fckurprivacy.UIManager;

/**
 * Created by wangjing on 2018/7/19.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.vergermiya.harevost.fckurprivacy.R;

public class AlphaActivity extends Activity implements AnimationListener {

    ImageView img_kaiji;
    Animation alpahAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaiji);// 加载开机动画页面

        img_kaiji = findViewById(R.id.kaiji_id);// 获得开机图片

        // 加载透明度变化的xml配置文件
        alpahAnimation = AnimationUtils.loadAnimation(this,
                R.anim.welcome_alpha);
        // 为ImageView图片控件加载透明度动画
        img_kaiji.setAnimation(alpahAnimation);
        alpahAnimation.setAnimationListener(this);

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
//        Toast.makeText(this, "开始加载动画", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MGuideAction.class);
        startActivity(intent);
        //设置切换动画，从右边进入，左边退出,带动态效果
        overridePendingTransition(R.anim.enter_from_right,
                R.anim.out_exist_left);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
        /**
         * android:repeatCount="1"
         */
        Toast.makeText(this, "重复加载动画", Toast.LENGTH_LONG).show();
    }
}

