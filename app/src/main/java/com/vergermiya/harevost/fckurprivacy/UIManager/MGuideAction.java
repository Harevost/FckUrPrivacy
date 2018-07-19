package com.vergermiya.harevost.fckurprivacy.UIManager;

/**
 * Created by wangjing on 2018/7/19.
 */


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.vergermiya.harevost.fckurprivacy.R;

import java.util.ArrayList;
import java.util.List;

public class MGuideAction extends Activity {

    ViewPager viewPager  ;
    View view1 ,view2 ,view3 ;
    List<View> viewList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mguide_layout);

        //实例化ViewPager组件
        viewPager = findViewById(R.id.viewpager) ;

        //加载要显示的页卡组件
        LayoutInflater inflater = LayoutInflater.from(this) ;
        view1 = inflater.inflate(R.layout.guide_layout1, null) ;
        view2 = inflater.inflate(R.layout.guide_layout2, null) ;
        view3 = inflater.inflate(R.layout.guide_layout3, null) ;

        //将要滑动显示的组件View放入集合中
        viewList  = new ArrayList<>() ;
        viewList.add(view1) ;
        viewList.add(view2) ;
        viewList.add(view3) ;

        //实例化适配器,将页卡集合传入适配器中
        MPagerAdapter mPagerAdapter = new MPagerAdapter(viewList, this) ;

        //为ViewPager设置适配器
        viewPager.setAdapter(mPagerAdapter);
    }
}
