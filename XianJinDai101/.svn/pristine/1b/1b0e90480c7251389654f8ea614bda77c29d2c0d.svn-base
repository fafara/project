package com.ryx.payment.ruishua.authenticate;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.authenticate.newauthenticate.IdCardUploadAct_;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_authenticate)
public class Authenticate extends BaseActivity {
    boolean  a = false ,b =false;  // 分别为是否显示 业务规则的开关
    @ViewById
    AutoLinearLayout ll_show_yewuguize_geti;
    @ViewById
    AutoLinearLayout yewuguize_geti;
    @ViewById
    AutoLinearLayout yewuguize_shanghu;
    @ViewById
    ImageView iv_renzheng_2;

    @ViewById
    ImageView iv_renzheng_1;
    private String rule1,rule2;
    @ViewById
    TextView tv_rule1;
    @ViewById
    TextView tv_rule2;

    @AfterViews
    public void afterView(){
        setTitleLayout("实名认证",true,false);
        rule2= PreferenceUtil.getInstance(Authenticate.this).getString(RyxAppconfig.Notes_SignMerchantText, "");
        rule1=PreferenceUtil.getInstance(Authenticate.this).getString(RyxAppconfig.Notes_SignPersonText, "");
        bindData();
    }
    @Click(R.id.ll_show_yewuguize_geti)
    public void showYewuguizeGeti(){
        if(!a){
            a= true;
            yewuguize_geti.setVisibility(View.VISIBLE);
            iv_renzheng_1.setImageResource(R.drawable.img_shiming_jiantou12);
        }else{				a= false;
            yewuguize_geti.setVisibility(View.GONE);
            iv_renzheng_1.setImageResource(R.drawable.img_shiming_jiantou11);
        }
    }
    @Click(R.id.ll_show_yewuguize_shanghu)
    public void showYewuguizeShanghu(){
        if(!b){
            b=true;
            yewuguize_shanghu.setVisibility(View.VISIBLE);
            iv_renzheng_2.setImageResource(R.drawable.img_shiming_jiantou22);
        }else{
            b=false;
            yewuguize_shanghu.setVisibility(View.GONE);
            iv_renzheng_2.setImageResource(R.drawable.img_shiming_jiantou21);
        }
    }

    @Click(R.id.ll_shanghu)
    public void toShanghuMerchantInfoAddAct(){
        startActivity(new Intent(Authenticate.this,MerchantInfoAdd_.class));
        finish();
    }
    @Click(R.id.ll_geti)
    public void userinfoaddAct(){

        if(RyxAppdata.getInstance(Authenticate.this).isAuthswitchOpen()){
            //新的实名认证流程
            startActivity(new Intent(Authenticate.this,IdCardUploadAct_.class));
        }else{
            //旧的用户实名认证流程
            startActivity(new Intent(Authenticate.this,UserInfoAddActivity_.class));
        }

        finish();
    }
    /**
     * 绑定数据
     */
    public void bindData(){
        tv_rule1.setText(rule1);
        tv_rule2.setText(rule2);
    }
}
