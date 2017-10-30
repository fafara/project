package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tamir7.contacts.Contacts;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.beans.bussiness.findcustomer.CfindCustomerResponse;
import com.ryx.ryxcredit.fragment.products.OtherFragment;
import com.ryx.ryxcredit.fragment.products.RuiqiaoloanFragment;

/**
 * Created by DIY on 2016/9/8.
 * 信贷产品列表
 */
public class CreditProductsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private int width;

    private int newWidth;

    private int padding;

    private static final int PAGE_COUNT = 1;
    //显示进度
    private LinearLayout layoutDots;

    private ViewPager creditVp;
    private TextView mMobile;
    private CfindCustomerResponse.ResultBean customerResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_credit_products);
        setTitleLayout("信用俱乐部", true, false);
        mMobile = (TextView) findViewById(R.id.tv_service_mobile);
        mMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转系统拨号
                Uri uri = Uri.parse("tel:" + getResources().getString(R.string.service_phone));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
            }
        });
        RyxcreditConfig.context = this;
        Contacts.initialize(this);
        layoutDots = (LinearLayout) findViewById(R.id.c_dots_ll);
        creditVp = (ViewPager) findViewById(R.id.c_credit_product_vp);
        width = getResources().getDisplayMetrics().widthPixels;
        newWidth = (int) (divideWidth(width));
        padding = (int) (divideWidth(width) / 3);
        initDots(PAGE_COUNT);
        CreditFragmentAdapter adapter = new CreditFragmentAdapter(getSupportFragmentManager());
        creditVp.setAdapter(adapter);
        creditVp.addOnPageChangeListener(this);
        setbottomMenu();
    }

    /**
     * @param screenWidth 手机屏幕的宽度
     * @author SheXiaoHeng
     */
    public double divideWidth(int screenWidth) {

        return screenWidth / 36;
    }

    private ImageView[] mDots;

    /**
     * @author 初始化ViewPager的底部小点
     */
    private void initDots(int length) {
        mDots = new ImageView[length];
        for (int i = 0; i < length; i++) {
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(newWidth, newWidth);
            lp.leftMargin = padding;
            lp.rightMargin = padding;
            lp.topMargin = padding;
            lp.bottomMargin = padding;
            iv.setLayoutParams(lp);
            iv.setImageResource(R.drawable.c_dot_black_corners);
            layoutDots.addView(iv);
            mDots[i] = iv;
        }
        mDots[0].setImageResource(R.drawable.c_dot_orange_corners);
    }

    /**
     * @author 设置ViewPager当前的底部小点
     */
    private void setCurrentDot(int currentPosition) {
        for (int i = 0; i < mDots.length; i++) {
            if (i == currentPosition) {
                mDots[i].setImageResource(R.drawable.c_dot_orange_corners);
            } else {
                mDots[i].setImageResource(R.drawable.c_dot_black_corners);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * Fragment 数据适配器
     */
    private class CreditFragmentAdapter extends FragmentPagerAdapter {

        public CreditFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    RuiqiaoloanFragment rql = new RuiqiaoloanFragment();
                    return rql;
                case 1:
                    OtherFragment other = new OtherFragment();

                    return other;
                default:
                    OtherFragment other1 = new OtherFragment();

                    return other1;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

}