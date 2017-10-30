package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.dialog.ICSuccesDialog;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_bank_card_balance_inquiry)
public class CardBalanceResultActivity extends BaseActivity {
    String balance;
    String showunplugin;
    @ViewById
    TextView tv_yue;

    ICSuccesDialog dialog_icsucces;
@AfterViews
public void initView(){
    setTitleLayout("余额查询", true, false);
    balance = getIntent().getStringExtra("CardBalance");
    showunplugin = getIntent().getStringExtra("showunplugin");
    tv_yue.setText(MoneyEncoder.decodeFormat(balance));
    if ("show".equals(showunplugin)) {
        dialog_icsucces = new ICSuccesDialog(CardBalanceResultActivity.this, R.style.mydialog);
        dialog_icsucces.show();
    }
}
    @Click(R.id.bt_redo)
    public void btRedoCick(){
        setResult(RyxAppconfig.CLOSE_AT_SWIPER);
        // 关闭Activity
        CardBalanceResultActivity.this.finish();
    }
    @Click(R.id.bt_againswiper)
    public void btnAgainswiper(){
        Intent intent = new Intent();
        // 把返回数据存入Intent
        intent.putExtra("result", "Balance");
        // 设置返回数据
        CardBalanceResultActivity.this.setResult(RyxAppconfig.QT_RESULT_OK,
                intent);
        // 关闭Activity
        CardBalanceResultActivity.this.finish();
        }
    @Override
    public void  backUpImgOnclickListen(){
        setResult(RyxAppconfig.CLOSE_AT_SWIPER);
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            setResult(RyxAppconfig.CLOSE_AT_SWIPER);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
