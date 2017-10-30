package com.ryx.payment.ruishua.authenticate.creditcard;

import android.content.Intent;
import android.text.Html;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.Param;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by xiepingping on 2016/5/27.
 */
@EActivity(R.layout.activity_credit_card_auth_step1)
public class CreditCardAuthStep1Activity extends BaseActivity {

    @ViewById(R.id.tv_instruction1)
    TextView tv_instruction1;
    @ViewById(R.id.tv_instruction2)
    TextView tv_instruction2;

    Param qtpayflag;
    Param qtpayAccountNo;
    private String resultSwipeStr = "";
    private String expirydate;
    private String bankcardid = "";


    @AfterViews
    public void initViews() {
        setTitleLayout("信用卡认证", true, true);
        tv_instruction1.setText(Html
                .fromHtml("刷卡验证时需要使用<font color=#117ffa>您本人的信用卡</font>我们只验证信用卡的真实性及有效性，不会扣除您任何费用，也不涉及您的信用卡安全问题，审核需要<font color=#117ffa>1个工作日</font>。"));

        tv_instruction2.setText(Html
                .fromHtml("进行信用卡认证可以提高<font color=#117ffa>用户等级</font>，越高等级用户可以交易的<font color=#117ffa>额度越高</font>。"));
        bankcardid = getIntent().getStringExtra("BankcardId");
    }



    @Click(R.id.btn_next)
    public void swipeCard() {
        startActivity(new Intent(CreditCardAuthStep1Activity.this,
               CreditCardAuthCardAddActivity_.class).putExtra("BankcardId",bankcardid));
        finish();
//        requestCode = 1;
//        String warning = "";
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            warning = "请在\"设置\"里，打开\""+getResources().getString(R.string.app_name)+"\"的定位和蓝牙权限，否则您将无法连接蓝牙设备";
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            warning = "请在\"设置\"里，打开\""+getResources().getString(R.string.app_name_ryx)+"\"的定位和蓝牙权限，否则您将无法连接蓝牙设备";
//        }
//        requesDevicePermission(warning, requestCode, permissionResult, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});

    }

//    private PermissionResult permissionResult = new PermissionResult() {
//        @Override
//        public void requestSuccess() {
//            //跳转刷卡页面
//            startActivityForResult(new Intent(CreditCardAuthStep1Activity.this,
//                            Cardno_.class).putExtra("CARDNOType", "SENIOR"),
//                    SWIPING_CARD);
//        }
//
//        @Override
//        public void requestFailed() {
//
//        }
//    };

    @Click(R.id.tilerightImg)
    public void showHelp(){
        Intent intent = new Intent(CreditCardAuthStep1Activity.this,HtmlMessageActivity_.class);
        intent.putExtra("title","信用卡认证及升级规则");
        intent.putExtra("urlkey",RyxAppconfig.Notes_Senior);
        startActivity(intent);
    }

    @Click(R.id.tileleftImg)
    public void closeWindow() {
        finish();
    }

}
