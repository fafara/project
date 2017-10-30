package com.ryx.payment.ruishua.bean;

/**
 * Created by laomao on 16/4/20.
 */


import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.swiper.utils.CryptoUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class RyxPayParams {
    private String application;
    private String appUser;
    private String version;
    private String osType;
    private String mobileSerialNum;
    private String userIP;
    private String clientType;
    private String token;
    private String phone;
    private String mobileNo;
    private String password;
    private String transDate;
    private String transTime;
    private String transLogNo;
    private String sign;
    private String appType;
    private String orderId;
    private String username;
    private String referrerMobileNo;
    private String mobileMac;


    public String createMobileNO() {
        return "<mobileNo>" + getMobileNo() + "</mobileNo>";
    }

    public String createPassword() {
        return "<password>" + password + "</password>";
    }

    public String createTransDate() {
        return "<transDate>" + getTransDate() + "</transDate>";
    }

    public String createTransTime() {
        return "<transTime>" + getTransTime() + "</transTime>";
    }

    public String createTransLogNo() {
        return "<transLogNo>" + getTransLogNo() + "</transLogNo>";
    }

    public String createSign() {
        return "<sign>" + getSign() + "</sign>";
    }

    public String createAppType() {
        return "<appType>" + getAppType() + "</appType>";
    }

    public String createOrderId() {
        return "<orderId>" + getOrderId() + "</orderId>";
    }

    public String createUsername() {
        return "<userName>" + getUsername() + "</userName>";
    }

    public String createReferrerMobileNo() {
        return "<referrerMobileNo>" + getReferrerMobileNo() + "</referrerMobileNo>";
    }

    public String createMobileMac() {
        return "<mobileMac>" + getMobileMac() + "</mobileMac>";
    }


    /* ******************************公用生成报文头**************************** */
    public String createQtPayLeft() {
        StringBuilder sb = new StringBuilder();

        sb.append("<QtPay");
        sb.append(" application=" + "\"" + application + "\"");
        sb.append(" appUser=" + "\"" + appUser + "\"");
        sb.append(" version=" + "\"" + version + "\"");
        sb.append(" osType=" + "\"" + osType + "\"");
        sb.append(" mobileSerialNum=" + "\"" + mobileSerialNum + "\"");
        sb.append(" userIP=" + "\"" + userIP + "\"");
        sb.append(" clientType=" + "\"" + clientType + "\"");
        sb.append(" token=" + "\"" + token + "\"");    // 写死
        sb.append(" phone=" + "\"" + phone + "\">");
        return sb.toString();

    }


    /* ******************************登录**************************** */
    //	拼接的字符串不包括签名
    public String createLoginWithoutSign() {
        return createQtPayLeft() + createMobileNO() + createPassword()
                + createTransDate() + createTransTime() + createTransLogNo();
    }

    public String createLoginXML() {
        String leftstring = createLoginWithoutSign();
        //根据拼接的字符串生成新的签名,再根据新的签名生成 最终的 XML文件

        String newsign = null;
        try {
            newsign = URLEncoder.encode(leftstring + createSign() + "</QtPay>", "UTF-8");
//			 LOG.showLog("密文", leftstring + createSign() +"</QtPay>");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.sign = CryptoUtils.getInstance().EncodeDigest(newsign.getBytes());
//		LOG.showLog("登录 sign ", sign);
        return "requestXml=" + leftstring + createSign() + "</QtPay>";
    }


    /* ******************************注册用户**************************** */
    //	拼接的字符串不包括签名
    public String createRegWithoutSign() {
        return createQtPayLeft() + createMobileNO() + createAppType() + createOrderId()
                + createTransDate() + createTransTime() + createTransLogNo();
    }

    public String createRegXML() {
        String leftstring = createRegWithoutSign();
        //根据拼接的字符串生成新的签名,再根据新的签名生成 最终的 XML文件

        String newsign = null;
        try {
            newsign = URLEncoder.encode(leftstring + createSign() + "</QtPay>", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.sign = CryptoUtils.getInstance().EncodeDigest(newsign.getBytes());
        return "requestXml=" + leftstring + createSign() + "</QtPay>";
    }


    /* ****************************** 设置密码  **************************** */
    //	拼接的字符串不包括签名
    public String createReg3WithoutSign() {
        return createQtPayLeft()
                + createMobileNO() + createPassword() + createUsername()
                + createReferrerMobileNo() + createMobileMac()
                + createTransDate() + createTransTime() + createTransLogNo();
    }

    public String createReg3XML() {
        String leftstring = createReg3WithoutSign();
        //根据拼接的字符串生成新的签名,再根据新的签名生成 最终的 XML文件

        String newsign = null;
        try {
            newsign = URLEncoder.encode(leftstring + createSign() + "</QtPay>", "UTF-8");
//			 LOG.showLog("注册密文", leftstring + createSign() +"</QtPay>");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.sign = CryptoUtils.getInstance().EncodeDigest(newsign.getBytes());
        LogUtil.showLog("验证码 sign ", sign);
        return "requestXml=" + leftstring + createSign() + "</QtPay>";
    }


    /////////////////////////////////////////////////////////////////

    public String getApplication() {
        return application;
    }

    public String getAppUser() {
        return appUser;
    }

    public String getVersion() {
        return version;
    }

    public String getOsType() {
        return osType;
    }

    public String getMobileSerialNum() {
        return mobileSerialNum;
    }

    public String getUserIP() {
        return userIP;
    }

    public String getClientType() {
        return clientType;
    }

    public String getToken() {
        return token;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public String getTransDate() {
        return CryptoUtils.getInstance().getTransDate();
    }

    public String getTransTime() {
        return CryptoUtils.getInstance().getTransTime();
    }

    public String getTransLogNo() {
        return CryptoUtils.getInstance().getTransLogNo();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSign() {
        return sign;
    }

    //	============================================
    public void setApplication(String application) {
        this.application = application;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public void setMobileSerialNum(String mobileSerialNum) {
        this.mobileSerialNum = mobileSerialNum;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public void setTransLogNo(String transLogNo) {
        this.transLogNo = transLogNo;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReferrerMobileNo() {
        return referrerMobileNo;
    }

    public void setReferrerMobileNo(String referrerMobileNo) {
        this.referrerMobileNo = referrerMobileNo;
    }

    public String getMobileMac() {
        return mobileMac;
    }

    public void setMobileMac(String mobileMac) {
        this.mobileMac = mobileMac;
    }
}

