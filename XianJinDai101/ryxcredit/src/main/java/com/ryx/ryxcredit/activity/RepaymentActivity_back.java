package com.ryx.ryxcredit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;

import com.rey.material.widget.TextView;
import com.ryx.ryxcredit.R;

/**
 * Created by laomao on 16/6/28.
 */
public class RepaymentActivity_back extends BaseActivity implements View.OnClickListener {
    private LinearLayout swiper_layout, auto_layout;
    private RecyclerView repaylist_rclview;
    private RecyclerView.Adapter repaylist_adapter;
    private RecyclerView.LayoutManager repaylist_layoutmanager;
    private TextView swiper_tv, swiper_comment_tv, auto_tv, auto_comment_tv;
    private TextView repayment_sum_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_repayment_list);
        initView();
    }


    private void initView() {
        setTitleLayout(getResources().getString(R.string.c_repayment), true, false);
//        auto_layout = (LinearLayout) findViewById(R.id.c_auto_layout);
//        auto_layout.setOnClickListener(this);
//        swiper_layout = (LinearLayout) findViewById(R.id.c_swiper_layout);
//        swiper_layout.setOnClickListener(this);
//        repaylist_rclview = (RecyclerView) findViewById(R.id.c_rcv_repayment_list);
//        repaylist_rclview.setHasFixedSize(true);
//        // use a linear layout manager
//        repaylist_layoutmanager = new LinearLayoutManager(this);
//        repaylist_rclview.setLayoutManager(repaylist_layoutmanager);
//        repaylist_rclview.addItemDecoration(new CItemDivider(this, R.drawable.c_recyclerview_divide)
//        );
//
//        repaylist_adapter = new RepaylistAdapter(this);
//        repaylist_rclview.setAdapter(repaylist_adapter);

//        swiper_tv = (TextView) findViewById(R.id.c_swiper_tv);
//        swiper_comment_tv = (TextView) findViewById(R.id.c_swiper_comment_tv);
//        auto_tv = (TextView) findViewById(R.id.c_auto_tv);
//        auto_comment_tv = (TextView) findViewById(R.id.c_auto_comment_tv);
//        repayment_sum_tv = (TextView) findViewById(R.id.c_repayment_sum_tv);
//        setSumMoney("15000.00");
//        initSwiperChecked();
    }

    private void setSumMoney(String money) {
        String s = String.format(getResources().getString(R.string.c_repayment_sum_interest), money);
        repayment_sum_tv.setText(Html.fromHtml(s));
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.c_auto_layout) {
//            initAutoChecked();
//        } else if (v.getId() == R.id.c_swiper_layout) {
//            initSwiperChecked();
//        }
    }
//
//    private void initSwiperChecked() {
//        swiper_layout.setBackgroundResource(R.drawable.c_repayment_type_press);
//        auto_layout.setBackgroundResource(R.drawable.c_repayment_type_unpress);
//        auto_tv.setTextColor(getResources().getColor(R.color.threeblack));
//        auto_comment_tv.setTextColor(getResources().getColor(R.color.threeblack));
//        swiper_tv.setTextColor(getResources().getColor(R.color.white));
//        swiper_comment_tv.setTextColor(getResources().getColor(R.color.white));
//    }
//
//    private void initAutoChecked() {
//        auto_layout.setBackgroundResource(R.drawable.c_repayment_type_press);
//        swiper_layout.setBackgroundResource(R.drawable.c_repayment_type_unpress);
//        auto_tv.setTextColor(getResources().getColor(R.color.white));
//        auto_comment_tv.setTextColor(getResources().getColor(R.color.white));
//        swiper_tv.setTextColor(getResources().getColor(R.color.threeblack));
//        swiper_comment_tv.setTextColor(getResources().getColor(R.color.threeblack));
//    }
}
