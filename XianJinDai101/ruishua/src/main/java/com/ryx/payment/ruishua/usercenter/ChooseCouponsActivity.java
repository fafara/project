package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.os.Bundle;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.sjfx.ADHtmlActivity_;
import com.ryx.payment.ruishua.utils.Base64Utils;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.json.JSONObject;

@EActivity(R.layout.activity_choose_coupons)
public class ChooseCouponsActivity extends BaseActivity {

    @AfterViews
    public void iniview(){
        setTitleLayout("优惠券",true,false);
    }
    /**
     * 手续费优惠券
     */
    @Click(R.id.choose_coupons_mycoupons)
    public void choose_coupons_mycouponsClick(){
        startActivity(new Intent(ChooseCouponsActivity.this, MyCouponActivity_.class));
    }

    /**
     * 卡券
     */
    @Click(R.id.choose_coupons_mycardvoucher)
    public void choose_coupons_mycardvoucherClick(){
        String customerId= RyxAppdata.getInstance(ChooseCouponsActivity.this).getCustomerId();
        String token= RyxAppdata.getInstance(ChooseCouponsActivity.this).getToken();
        String phone= RyxAppdata.getInstance(ChooseCouponsActivity.this).getPhone();

        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("customerId",customerId);
            jsonObject.put("token",token);
            jsonObject.put("phone",phone);
            jsonObject.put("appUser",RyxAppconfig.APPUSER);
            jsonObject.put("ryxpayUrl",RyxAppconfig.BASE_RELEASE_URL);
            String paramsJsonStr= jsonObject.toString();
            LogUtil.showLog("paramsJsonStr=="+paramsJsonStr);
            String params=    Base64Utils.strEncodeHex(paramsJsonStr);
            LogUtil.showLog("params=="+params);
            Bundle bundle =new Bundle();
            bundle.putString("url", RyxAppconfig.BASE_MEMBER_URL+"app/member/static/ext/module/coupon?random="+params);
            bundle.putString("title","我的卡券");
            Intent intent = new Intent(ChooseCouponsActivity.this, ADHtmlActivity_.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }catch (Exception e){

        }



    }

}
