
package com.ryx.payment.ruishua.bean;

import java.io.Serializable;

public class OrderInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id; // 自定义业务种类统一编号
    private String ordertype; // 业务种类
    private String merchantId; // 商户编码
    private String productId; // 产品编码
    private String orderAmt; // 订单金额
    private String realAmt; // 实际支付金额
    private String orderDesc; // 订单信息
    private String payTool; // 支付方式
    private String orderRemark; // 订单备注信息
    private String orderId; // 系统返回的订单号
    private String transLogNo; // 流水号
    private String payee;//服务端返回的客户CustomerID编号
    private String merchantname; // 交易类型

    private String account2; // 收款账户名

    private boolean isNeedSign = false;// 是否显示

    private int paytype = -1;
    private String cardIdx;//付款给别人，银行卡索引
    private boolean isMustMpos = false;
    private String disPlayContent;//收款账户描述信息：手机号+收款用户名（XX**）

    private String interfaceTag;//根据当前字段判断交易时接口是否走JFPalCardPaySett
    // 01是走JFPalCardPaySett接口，12走SmartPayments小贷接口,13走TradeMerchant接口//14为激活设备押金流程
    //15为瑞豆购买
    // 其他则走正常

    private boolean iscashCardIntercept=false;

    private String servcode;
    private String tradecode;
    private String industId;
    private String industUserId;
    private String couponBindId;
    private String couponBindDisPaly;
    private boolean isUseRuiBean=false;

    public OrderInfo() {
    }

    ;

    public OrderInfo(int id, String ordertype, String merchantId, String productId, boolean isPrint) {
        super();
        this.id = id;
        this.ordertype = ordertype;
        this.merchantId = merchantId;
        this.productId = productId;
        this.isNeedSign = isPrint;
    }

    public OrderInfo(String ordertype, String merchantId, String productId,
                     String orderDesc, String orderRemark, boolean isPrint) {
        super();
        this.ordertype = ordertype;
        this.merchantId = merchantId;
        this.productId = productId;
        this.orderDesc = orderDesc;
        this.orderRemark = orderRemark;
        this.isNeedSign = isPrint;
    }

    public String getInterfaceTag() {
        return interfaceTag;
    }

    public void setInterfaceTag(String interfaceTag) {
        this.interfaceTag = interfaceTag;
    }

    public String getCardIdx() {
        return cardIdx;
    }

    public void setCardIdx(String cardIdx) {
        this.cardIdx = cardIdx;
    }

    public boolean isMustMpos() {
        return isMustMpos;
    }

    public void setMustMpos(boolean isMustMpos) {
        this.isMustMpos = isMustMpos;
    }

    public Boolean getIsMustMpos() {
        return this.isMustMpos = isMustMpos;
    }

    public int getId() {
        return id;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getProductId() {
        return productId;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public String getRealAmt() {
        return realAmt;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public String getPayTool() {
        return payTool;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public void setPayTool(String payTool) {
        this.payTool = payTool;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTransLogNo() {
        return transLogNo;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setTransLogNo(String transLogNo) {
        this.transLogNo = transLogNo;
    }

    public String getAccount2() {
        return account2;
    }

    public void setAccount2(String account2) {
        this.account2 = account2;
    }

    public boolean isNeedSign() {
        return isNeedSign;
    }

    public void setNeedSign(boolean isNeedSign) {
        this.isNeedSign = isNeedSign;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getDisPlayContent() {
        return disPlayContent;
    }

    public void setDisPlayContent(String disPlayContent) {
        this.disPlayContent = disPlayContent;
    }

    public boolean getIscashCardIntercept() {
        return iscashCardIntercept;
    }

    public void setIscashCardIntercept(boolean iscashCardIntercept) {
        this.iscashCardIntercept = iscashCardIntercept;
    }

    public String getServcode() {
        return servcode;
    }

    public void setServcode(String servcode) {
        this.servcode = servcode;
    }

    public String getTradecode() {
        return tradecode;
    }

    public void setTradecode(String tradecode) {
        this.tradecode = tradecode;
    }

    public String getIndustUserId() {
        return industUserId;
    }

    public void setIndustUserId(String industUserId) {
        this.industUserId = industUserId;
    }

    public String getIndustId() {
        return industId;
    }

    public void setIndustId(String industId) {
        this.industId = industId;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id=" + id +
                ", ordertype='" + ordertype + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", productId='" + productId + '\'' +
                ", orderAmt='" + orderAmt + '\'' +
                ", realAmt='" + realAmt + '\'' +
                ", orderDesc='" + orderDesc + '\'' +
                ", payTool='" + payTool + '\'' +
                ", orderRemark='" + orderRemark + '\'' +
                ", orderId='" + orderId + '\'' +
                ", transLogNo='" + transLogNo + '\'' +
                ", payee='" + payee + '\'' +
                ", merchantname='" + merchantname + '\'' +
                ", account2='" + account2 + '\'' +
                ", isNeedSign=" + isNeedSign +
                ", paytype=" + paytype +
                ", cardIdx='" + cardIdx + '\'' +
                ", isMustMpos=" + isMustMpos +
                ", disPlayContent='" + disPlayContent + '\'' +
                ", interfaceTag='" + interfaceTag + '\'' +
                ", iscashCardIntercept=" + iscashCardIntercept +
                ", servcode='" + servcode + '\'' +
                ", tradecode='" + tradecode + '\'' +
                ", industId='" + industId + '\'' +
                ", industUserId='" + industUserId + '\'' +
                '}';
    }

    public String getCouponBindId() {
        return couponBindId;
    }

    public void setCouponBindId(String couponBindId) {
        this.couponBindId = couponBindId;
    }

    public String getCouponBindDisPaly() {
        return couponBindDisPaly;
    }

    public void setCouponBindDisPaly(String couponBindDisPaly) {
        this.couponBindDisPaly = couponBindDisPaly;
    }

    public boolean isUseRuiBean() {
        return isUseRuiBean;
    }

    public void setUseRuiBean(boolean useRuiBean) {
        isUseRuiBean = useRuiBean;
    }
}
