package com.ryx.ryxpay.net;


import com.ryx.ryxpay.bean.RyxPayResult;

/**
 * Created by laomao on 16/4/19.
 */
public interface XmlCallback  {
//     public void success(RyxPayResult payResult);
//    public void fail(String message);

    /**
     *  交易成功
     * @param payResult 返回结果
     */
    public void onTradeSuccess(RyxPayResult payResult);

    /**
     * 登录异常
     */
    public void onLoginAnomaly();

    /**
     * 其他状态
     */
    public void onOtherState();

    /**
     * 失败状态
     */
    public void onTradeFailed();
}
