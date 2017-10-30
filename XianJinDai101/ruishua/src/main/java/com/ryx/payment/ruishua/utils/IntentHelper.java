
package com.ryx.payment.ruishua.utils;

import com.ryx.payment.ruishua.activity.CreditClubActivity_;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.activity.MoreActivity_;
import com.ryx.payment.ruishua.activity.MoreIncreateActivity_;
import com.ryx.payment.ruishua.authenticate.Authenticate_;
import com.ryx.payment.ruishua.authenticate.MerchantInfoAdd_;
import com.ryx.payment.ruishua.authenticate.UserToMerchantActivity_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.NewAuthResultAct_;
import com.ryx.payment.ruishua.bindcard.BindedCardListActivity_;
import com.ryx.payment.ruishua.bindcard.BindedDebitCardListActivity_;
import com.ryx.payment.ruishua.convenience.CreditSplashActivity_;
import com.ryx.payment.ruishua.convenience.ImPayActivity_;
import com.ryx.payment.ruishua.convenience.PaymentActivity_;
import com.ryx.payment.ruishua.convenience.RuibeanActivity;
import com.ryx.payment.ruishua.convenience.ScanningPayActivity_;
import com.ryx.payment.ruishua.convenience.Swiper_;
import com.ryx.payment.ruishua.convenience.UnionQrcodePayActivity_;
import com.ryx.payment.ruishua.recharge.AliPayActivity_;
import com.ryx.payment.ruishua.recharge.CreditCardListActivity_;
import com.ryx.payment.ruishua.recharge.FlowActivity_;
import com.ryx.payment.ruishua.recharge.MobileRechargeActivity_;
import com.ryx.payment.ruishua.recharge.RefuelRechargeActivity_;
import com.ryx.payment.ruishua.sjfx.MyInvitationCodeActivity_;
import com.ryx.payment.ruishua.sjfx.DevPurchaseActivity_;
import com.ryx.payment.ruishua.usercenter.BMIncomeAndExpenditureDetails2_;
import com.ryx.payment.ruishua.usercenter.QrcodePayActivity_;
import com.ryx.payment.ruishua.usercenter.ScanningCodeActivity_;
import com.ryx.payment.ruishua.usercenter.WithdrawListImActivity_;
import com.ryx.ryxcredit.activity.CreditActivity;
import com.ryx.ryxcredit.activity.CreditProductsActivity;

import java.util.HashMap;

public class IntentHelper {
    private static IntentHelper instance;

    public static IntentHelper getInstance() {
        return instance = (null != instance) ? instance : new IntentHelper();
    }

    private static final HashMap<String, Class> ACTIVITY_CLASS = new HashMap<String, Class>();
    {
        ACTIVITY_CLASS.put("CreditActivity", CreditActivity.class);
        ACTIVITY_CLASS.put("WithdrawListImActivity_", WithdrawListImActivity_.class);//资金结算
        ACTIVITY_CLASS.put("CreditCardListActivity_", CreditCardListActivity_.class);//信用卡还款
        ACTIVITY_CLASS.put("Swiper_", Swiper_.class);//余额查询
        ACTIVITY_CLASS.put("BMIncomeAndExpenditureDetails2_", BMIncomeAndExpenditureDetails2_.class);//交易明细
//        ACTIVITY_CLASS.put("AuthResultActivity_", AuthResultActivity_.class);//用户认证
        ACTIVITY_CLASS.put("AuthResultActivity_", NewAuthResultAct_.class);//用户认证

        ACTIVITY_CLASS.put("BindedDebitCardListActivity_", BindedDebitCardListActivity_.class);//绑定银行卡
        ACTIVITY_CLASS.put("HtmlMessageActivity_", HtmlMessageActivity_.class);//加载html
        ACTIVITY_CLASS.put("MobileRechargeActivity_", MobileRechargeActivity_.class);//手机充值
        ACTIVITY_CLASS.put("AliPayActivity_", AliPayActivity_.class);//支付宝充值
        ACTIVITY_CLASS.put("RefuelRechargeActivity_", RefuelRechargeActivity_.class);//加油卡充值
        ACTIVITY_CLASS.put("FlowActivity_", FlowActivity_.class);//流量充值
        ACTIVITY_CLASS.put("PaymentActivity_", PaymentActivity_.class);//我要付款
        ACTIVITY_CLASS.put("ScanningCodeActivity_", ScanningCodeActivity_.class);//扫一扫
        ACTIVITY_CLASS.put("QrcodePayActivity_", QrcodePayActivity_.class);//我的二维码
        ACTIVITY_CLASS.put("UnionQrcodePayActivity_", UnionQrcodePayActivity_.class);//银联二维码支付
        ACTIVITY_CLASS.put("ImPayActivity_", ImPayActivity_.class);//刷卡收款
        ACTIVITY_CLASS.put("ScanningPayActivity_", ScanningPayActivity_.class);//二维码收款
        ACTIVITY_CLASS.put("MoreActivity_", MoreActivity_.class);//更多
        ACTIVITY_CLASS.put("CreditSplashActivity_", CreditSplashActivity_.class);//更多
        ACTIVITY_CLASS.put("BindedCardListActivity_", BindedCardListActivity_.class);//更多
        ACTIVITY_CLASS.put("MerchantInfoAdd_", MerchantInfoAdd_.class);
        ACTIVITY_CLASS.put("Authenticate_", Authenticate_.class);
        ACTIVITY_CLASS.put("UserToMerchantActivity_", UserToMerchantActivity_.class);
        ACTIVITY_CLASS.put("CreditProductsActivity", CreditProductsActivity.class);
        ACTIVITY_CLASS.put("CreditClubActivity_", CreditClubActivity_.class);
        ACTIVITY_CLASS.put("MoreIncreateActivity_", MoreIncreateActivity_.class);//更多
        ACTIVITY_CLASS.put("MyInvitationCodeActivity_", MyInvitationCodeActivity_.class);//我的邀请码
        ACTIVITY_CLASS.put("DevPurchaseActivity_", DevPurchaseActivity_.class);
        ACTIVITY_CLASS.put("RuibeanActivity", RuibeanActivity.class);//瑞豆

    }

    public Class getActivityClass(String name) {
        return ACTIVITY_CLASS.get(name);
    }

    public boolean contains(String name) {
        return ACTIVITY_CLASS.containsKey(name);
    }
}
