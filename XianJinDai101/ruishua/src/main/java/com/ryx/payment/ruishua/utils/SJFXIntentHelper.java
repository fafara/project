package com.ryx.payment.ruishua.utils;

import com.ryx.payment.ruishua.sjfx.DevPurchaseActivity_;
import com.ryx.payment.ruishua.sjfx.IncomeDetailsActivity_;
import com.ryx.payment.ruishua.sjfx.IncomeGuideActivity_;
import com.ryx.payment.ruishua.sjfx.MyInvitationCodeActivity_;

import java.util.HashMap;

/**
 * 三级分销首页，点击列表跳转页面
 */

public class SJFXIntentHelper {

    private static SJFXIntentHelper instance;

    public static SJFXIntentHelper getInstance() {
        return instance = (null != instance) ? instance : new SJFXIntentHelper();
    }
    private static final HashMap<String, Class> ACTIVITY_CLASS = new HashMap<String, Class>();
    {
        ACTIVITY_CLASS.put("IncomeDetailsActivity_",IncomeDetailsActivity_.class);
        ACTIVITY_CLASS.put("DevPurchaseActivity_",DevPurchaseActivity_.class);
        ACTIVITY_CLASS.put("IncomeGuideActivity_", IncomeGuideActivity_.class);//收益指南
        ACTIVITY_CLASS.put("MyInvitationCodeActivity_", MyInvitationCodeActivity_.class);//我的邀请码

    }

    public Class getActivityClass(String name) {
        return ACTIVITY_CLASS.get(name);
    }

    public boolean contains(String name) {
        return ACTIVITY_CLASS.containsKey(name);
    }
}
