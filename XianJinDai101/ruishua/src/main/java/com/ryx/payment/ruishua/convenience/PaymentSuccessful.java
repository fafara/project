package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.dialog.ICSuccesDialog;
import com.ryx.payment.ruishua.usercenter.BMIncomeAndExpenditureDetails2_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 支付成功
 */
@EActivity(R.layout.activity_payment_successful)
public class PaymentSuccessful extends BaseActivity {
    ICSuccesDialog dialog_icsucces;
    String showunplugin,interfaceTag;
    String flag;
    Boolean lookmxi=true;
    @ViewById
    TextView paysuccess_content_tv;
    @ViewById
    Button bt_lookmxi,btn_backshouye;
@AfterViews
    public void initView(){
    setTitleLayout("确认订单",true,false);
    Intent intent=getIntent();
    if(intent.hasExtra("flag")){
        flag=intent.getStringExtra("flag");
    }
    if(intent.hasExtra("showunplugin")){
        showunplugin = intent.getStringExtra("showunplugin");
    }
    if(intent.hasExtra("interfaceTag")){
        interfaceTag=intent.getStringExtra("interfaceTag");
    }
    if ("show".equals(showunplugin)) {
        dialog_icsucces = new ICSuccesDialog(PaymentSuccessful.this, R.style.mydialog);
        dialog_icsucces.show();
    }
    if(intent.hasExtra("lookmxi")){
        lookmxi=intent.getBooleanExtra("lookmxi",true);
    }
    if(lookmxi){
        bt_lookmxi.setVisibility(View.VISIBLE);
        btn_backshouye.setText("返回首页");
        paysuccess_content_tv.setText( "恭喜你交易完成,随心支付享受移动生活!");
    }else{
        btn_backshouye.setText("返回");
        bt_lookmxi.setVisibility(View.GONE);
        paysuccess_content_tv.setText("恭喜你设备激活成功,随心支付享受移动生活!");
    }

    }
    @Click(R.id.btn_backshouye)
    public void btn_backshouyeClick(){
        Intent resultIntent=new Intent();
        resultIntent.putExtra("swiperresult","01");
        if("CLOSEALL".equals(flag)){
            setResult(RyxAppconfig.CLOSE_ALL,resultIntent);
        }else{
            setResult(RyxAppconfig.CLOSE_AT_SWIPER,resultIntent);
        }
        finish();
    }
    @Click(R.id.bt_lookmxi)
    public void bt_lookmxiClick(){
        if("15".equals(interfaceTag)){
            startActivity(new Intent(PaymentSuccessful.this,
                    RuiBeanBuyUseRecordMainActivity.class));
        }else{
            startActivity(new Intent(PaymentSuccessful.this,
                    BMIncomeAndExpenditureDetails2_.class));
        }

        Intent resultIntent=new Intent();
        resultIntent.putExtra("swiperresult","01");
        if("CLOSEALL".equals(flag)){
            setResult(RyxAppconfig.CLOSE_ALL,resultIntent);
        }else{
            setResult(RyxAppconfig.CLOSE_AT_SWIPER,resultIntent);
        }
        finish();
    }

    @Override
    protected void backUpImgOnclickListen() {
        Intent resultIntent=new Intent();
        resultIntent.putExtra("swiperresult","01");
        if("CLOSEALL".equals(flag)){
            setResult(RyxAppconfig.CLOSE_ALL,resultIntent);
        }else{
            setResult(RyxAppconfig.CLOSE_AT_SWIPER,resultIntent);
        }
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent resultIntent=new Intent();
        resultIntent.putExtra("swiperresult","01");
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if("CLOSEALL".equals(flag)){
                setResult(RyxAppconfig.CLOSE_ALL,resultIntent);
            }else{
                setResult(RyxAppconfig.CLOSE_AT_SWIPER,resultIntent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
