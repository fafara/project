package com.ryx.payment.ruishua.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.adapter.GuideViewPageAdapter;
import com.ryx.payment.ruishua.utils.GlideUtils;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_guide)
public class GuideActivity extends AppCompatActivity {

    @ViewById
    ViewPager plash_ad;
    @ViewById
    AutoLinearLayout ll_point_group;
    private int lastPosition;
    List<View> views = new ArrayList<View>();
    @AfterViews
    public void initView() {
        initViewPage();
    }

    private void initViewPage() {
       int type= RyxAppdata.getInstance(this).getCurrentGuideType();
        if(type==1){
            initRyxRSGuideView();
        }else if(type==2){
            initRyxFxBGuideView();
        }else if(type==3){
            initRHbGuideView();
        }
        GuideViewPageAdapter adapter = new GuideViewPageAdapter(this, views);
        plash_ad.setAdapter(adapter);
        plash_ad.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected (int position)
            {
                // 改变指示点的状态,当前的指示点选中为true，上一个指示点选中为false
                ll_point_group.getChildAt(position).setEnabled(true);
                ll_point_group.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;// 记录上一个点
            }

            @Override
            public void onPageScrolled (int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged (int arg0)
            {

            }
        });
    }

    /**
     * 瑞和宝引导页
     */
    private void initRHbGuideView(){
        View view1 = LayoutInflater.from(this).inflate(
                R.layout.viewpageitem_rhb, null);
        ImageView imageView=(ImageView)  view1.findViewById(R.id.iv_viewpageitem);
        GlideUtils.getInstance().load(this, R.drawable.guide_viewpage_one_rhb, imageView);
        views.add(view1);
        View view2 = LayoutInflater.from(this).inflate(
                R.layout.viewpageitem_rhb, null);
        ImageView imageView2=(ImageView)  view2.findViewById(R.id.iv_viewpageitem);
        GlideUtils.getInstance().load(this, R.drawable.guide_viewpage_two_rhb, imageView2);
        views.add(view2);
        View view3 = LayoutInflater.from(this).inflate(
                R.layout.viewpageitem_rhb, null);
        ImageView imageView3=(ImageView)  view3.findViewById(R.id.iv_viewpageitem);
        GlideUtils.getInstance().load(this, R.drawable.guide_viewpage_three_rhb, imageView3);
        ImageView iv_atoncetiyan=(ImageView)  view3.findViewById(R.id.iv_atoncetiyan);
        iv_atoncetiyan.setVisibility(View.VISIBLE);
        iv_atoncetiyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance(GuideActivity.this).saveString("isfirst", "no");
                startActivity(new Intent(GuideActivity.this, MainFragmentActivity_.class));
                finish();
            }
        });
        views.add(view3);
        initDots(R.drawable.point_bg_guide_ryxfxb);
        ll_point_group.setVisibility(View.GONE);
    }
    /**
     * 瑞银信分享版引导页
     */
    private void initRyxFxBGuideView(){
        View view = LayoutInflater.from(this).inflate(
                R.layout.viewpageonell_ryxfxb, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.viewpageoneimg);
        GlideUtils.getInstance().load(this, R.drawable.ryxfxb_guide_oneimg, imageView);
        ImageView guideonetextimgview = (ImageView) view.findViewById(R.id.guideonetextimgview);
        GlideUtils.getInstance().load(this, R.drawable.ryxfxb_guide_onepage_textimg, guideonetextimgview);
        views.add(view);

        View view3 = LayoutInflater.from(this).inflate(
                R.layout.viewpagetwo_ryxfxb, null);
        ImageView imageView3 = (ImageView) view3.findViewById(R.id.viewpagefouthimg);
        GlideUtils.getInstance().load(this, R.drawable.ryxfxb_guide_twoimg, imageView3);
        ImageView guidefouthtextimgview = (ImageView) view3.findViewById(R.id.guidefouthtextimgview);
        GlideUtils.getInstance().load(this, R.drawable.ryxfxb_guide_twotext, guidefouthtextimgview);
        ImageView startimageView = (ImageView) view3.findViewById(R.id.viewpagefouth_startimg);
        GlideUtils.getInstance().load(this, R.drawable.guideryxfxb_guide_button, startimageView);
        views.add(view3);

        initDots(R.drawable.point_bg_guide_ryxfxb);
        startimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance(GuideActivity.this).saveString("isfirst", "no");
                startActivity(new Intent(GuideActivity.this, MainFragmentActivity_.class));
                finish();
            }
        });
    }
    /**
     * 初始化瑞刷，瑞刷2.0，瑞银信，瑞和宝类型引导页
     */
    private void initRyxRSGuideView(){
        View view = LayoutInflater.from(this).inflate(
                R.layout.viewpageonell, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.viewpageoneimg);
        GlideUtils.getInstance().load(this, R.drawable.guideonemaximg, imageView);
        ImageView guideonetextimgview = (ImageView) view.findViewById(R.id.guideonetextimgview);
        GlideUtils.getInstance().load(this, R.drawable.guideonetextimg, guideonetextimgview);
        views.add(view);

        View view1 = LayoutInflater.from(this).inflate(
                R.layout.viewpagetwoll, null);
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.viewpagetwoimg);
        GlideUtils.getInstance().load(this, R.drawable.guidetwomaximg, imageView1);
        ImageView guidetwotextimgview = (ImageView) view1.findViewById(R.id.guidetwotextimgview);
        GlideUtils.getInstance().load(this, R.drawable.guidetwotextimg, guidetwotextimgview);
        views.add(view1);

        View view2 = LayoutInflater.from(this).inflate(
                R.layout.viewpagethrdll, null);
        ImageView imageView2 = (ImageView) view2.findViewById(R.id.viewpagethrdImg);
        GlideUtils.getInstance().load(this, R.drawable.guidethrdmaximg, imageView2);
        ImageView guidethrdtextimgview = (ImageView) view2.findViewById(R.id.guidethrdtextimgview);
        GlideUtils.getInstance().load(this, R.drawable.guidethrdtextimg, guidethrdtextimgview);
        views.add(view2);

        View view3 = LayoutInflater.from(this).inflate(
                R.layout.viewpagefouthll, null);
        ImageView imageView3 = (ImageView) view3.findViewById(R.id.viewpagefouthimg);
        GlideUtils.getInstance().load(this, R.drawable.guidefouthmaximg, imageView3);
        ImageView guidefouthtextimgview = (ImageView) view3.findViewById(R.id.guidefouthtextimgview);
        GlideUtils.getInstance().load(this, R.drawable.guidefouthmaxtextview, guidefouthtextimgview);
        ImageView startimageView = (ImageView) view3.findViewById(R.id.viewpagefouth_startimg);
        views.add(view3);
        initDots(R.drawable.point_bg_guide_rs);
        startimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance(GuideActivity.this).saveString("isfirst", "no");
                startActivity(new Intent(GuideActivity.this, MainFragmentActivity_.class));
                finish();
            }
        });
        }

    /**
     * 初始化点点
     * @param resId
     */
    private void initDots(int resId) {
        for (int i = 0; i < views.size(); i++) {
            // 添加指示点(一个ImageView)
            ImageView pointImageView = new ImageView(this);
            // 指示点在线性布局中，设置布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 12;// 右外边距
            pointImageView.setLayoutParams(params);
            pointImageView.setImageResource(resId);
            if (i == 0) {
                pointImageView.setEnabled(true);
            } else {
                pointImageView.setEnabled(false);
            }
            ll_point_group.addView(pointImageView);
        }
    }
}
