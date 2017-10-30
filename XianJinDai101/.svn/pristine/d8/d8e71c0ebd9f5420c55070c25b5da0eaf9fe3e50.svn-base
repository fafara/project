package com.ryx.payment.payplug.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.payplug.base.PayPlugBaseActivity;
import com.ryx.payment.payplug.bean.SerializableMap;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.swiper.utils.MapUtil;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * 银联二维码支付
 */
@EActivity(R.layout.activity_union_qrcode_pay)
public class UnionQrcodePayActivity extends PayPlugBaseActivity {
    @ViewById
    TextView tv_productname,tv_phonenumber,tv_ordermoney;
    String reqCode;
    public Map<String,String> reqMap;
    @ViewById
    ImageView qrcodeimg;
    @ViewById
    Button saveviewBtn;
    @ViewById
    View qrcodelayoutview;
    @AfterViews
    public void initView(){
        setTitleLayout(RyxAppdata.getInstance(this).getCurrentBranchName()+"银联收款台",true);
        RyxAppdata.getInstance(this).glideLoadqrcodeimgmageViewForBranch(getRightImgView());
        reqCode= getIntent().getStringExtra("reqCode");
        Bundle bundle = getIntent().getExtras();
        SerializableMap serializableMap = (SerializableMap) bundle.get("map");
        reqMap= serializableMap.getMap();
        String productname= MapUtil.getString(reqMap,"desc");
        tv_productname.setText(productname);
        String phonenumber= MapUtil.getString(reqMap,"phoneNumber");
        tv_phonenumber.setText(phonenumber);
        String ordermoney= MapUtil.getString(reqMap,"money");
        tv_ordermoney.setText(MoneyEncoder.decodeFormat(ordermoney));
        createQrcodeImg("http://www.baidu.com",qrcodeimg);

    }
    @Click(R.id.saveviewBtn)
    public void saveviewBtn(){
        dirSaveView(qrcodelayoutview);
    }

}
