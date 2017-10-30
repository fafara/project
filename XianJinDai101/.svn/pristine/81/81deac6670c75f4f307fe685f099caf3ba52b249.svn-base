package com.ryx.payment.ruishua.usercenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_register_success)
public class RegisterSuccess extends BaseActivity {

    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tv_title)
    TextView mTitle;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.btn_register_success)
    Button mRegSuccBtn;

    @AfterViews
    public void initViews() {
        mTitle.setText("注册成功");
        mBackImg.setVisibility(View.GONE);
        mMsgImg.setVisibility(View.GONE);
    }

    @Click(R.id.btn_register_success)
    public void regSuccess() {
        setResult(RyxAppconfig.CLOSE_ALL);
        finish();
    }
}
