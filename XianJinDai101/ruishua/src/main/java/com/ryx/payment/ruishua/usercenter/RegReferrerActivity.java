package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_reg_referrer)
public class RegReferrerActivity extends BaseActivity {
@ViewById(R.id.et_referrer_phonenumber)
EditText referrer_phonenumber;
 @ViewById(R.id.btn_reg_referrer)
 Button btn_reg_referrer;
 @ViewById(R.id.btn_skip)
 Button btn_skip;
    Param userNameParam;
    private String userName;
   @AfterViews
    public void afterInitView(){
       setTitleLayout("注册",true,false);
       Intent intent=getIntent();
       userName= intent.getStringExtra("userName");
       initQtPatParams();
       btn_reg_referrer.setOnClickListener(new NoDoubleClickListener() {
           @Override
           protected void onNoDoubleClick(View view) {
               httpsPostRegFerrer();
           }
       });
       btn_skip.setOnClickListener(new NoDoubleClickListener() {
           @Override
           protected void onNoDoubleClick(View view) {
               finish();
           }
       });

   }
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application","addRecommend.Req");
        userNameParam=new Param("username",userName);
    }
    /**
     * 提交推荐人手机号
     */
    private void httpsPostRegFerrer() {
       String phonenumberVal= referrer_phonenumber.getText().toString();
        if(TextUtils.isEmpty(phonenumberVal)||phonenumberVal.replace(" ","").length()!=11){
            LogUtil.showToast(RegReferrerActivity.this,"请正确填写推荐人手机号");
            return;
        }
        if(userName.equals(phonenumberVal)){
            LogUtil.showToast(RegReferrerActivity.this,"推荐人不可以是本人!");
            return;
        }
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(userNameParam);
        qtpayParameterList.add(new Param("recommend",phonenumberVal.replace(" ","")));
        httpsPost("addRecommendTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(RegReferrerActivity.this,"推荐人推荐成功");
                finish();
            }
        });


    }


}
