package com.ryx.ryxpay.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ryx.ryxpay.R;
import com.ryx.ryxpay.adapter.MainGridviewAdapter;
import com.ryx.ryxpay.bean.IconBean;
import com.ryx.ryxpay.utils.AdapterUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main_drawer)
public class MainDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;
    @ViewById(R.id.gv_top)
    GridView gvTop;
    @ViewById(R.id.gv_bottom)
    GridView gvBottom;
    @ViewById
    android.support.v7.widget.Toolbar toolbar;
    @ViewById(R.id.collapsing_toolbar)
    android.support.design.widget.CollapsingToolbarLayout toolbarLayout;

    @Bean
    MainGridviewAdapter topAdapter;
    List<IconBean.IconMsgBean> list = new ArrayList<IconBean.IconMsgBean>();

    @AfterViews
    void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);
        topAdapter.setSize(AdapterUtil.getSize(this));
        gvTop.setAdapter(topAdapter);
        gvTop.setOnItemClickListener(onItemClickListener);
        gvBottom.setAdapter(topAdapter);
        initData();

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getBaseContext(), list.get(position).getName(), Toast.LENGTH_SHORT).show();
            if (position == 0)
                showLoading("test...");
            if (position == 1)
                startActivity(new Intent(MainDrawerActivity.this, LoginActivity_.class));
            if (position == 2)
                showLoading();
        }
    };


    @Click(R.id.iv_header)
    public void headerClick() {
        drawer.openDrawer(GravityCompat.START);
//        startActivity(new Intent(this,RegisterActivity_.class));
        startActivity(new Intent(this, LoginActivity_.class));
//        showLoading();
    }

    private void initData() {
        list = new ArrayList<IconBean.IconMsgBean>();
        IconBean.IconMsgBean bean = new IconBean.IconMsgBean();
        bean.setName("我要付款");
        bean.setRes("icon_skin_pay");
        list.add(bean);
        bean = new IconBean.IconMsgBean();
        bean.setName("手机号付款");
        bean.setRes("icon_skin_telpay");
        list.add(bean);
        bean = new IconBean.IconMsgBean();
        bean.setName("即时取");
        bean.setRes("icon_skin_drawnow");
        list.add(bean);
        bean = new IconBean.IconMsgBean();
        bean.setName("余额查询");
        bean.setRes("icon_skin_balance");
        list.add(bean);
        bean = new IconBean.IconMsgBean();
        bean.setName("信用卡还款");
        bean.setRes("icon_skin_credit");
        list.add(bean);
        bean = new IconBean.IconMsgBean();
        bean.setName("转账汇款");
        bean.setRes("icon_skin_transfer");
        list.add(bean);
        bean = new IconBean.IconMsgBean();
        bean.setName("实名认证");
        bean.setRes("icon_skin_realname");
        list.add(bean);
        bean = new IconBean.IconMsgBean();
        bean.setName("红包");
        bean.setRes("icon_skin_redgift");
        list.add(bean);

        topAdapter.update(list);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
