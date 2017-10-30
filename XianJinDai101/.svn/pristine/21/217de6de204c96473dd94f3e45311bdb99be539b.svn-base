package com.ryx.payment.ruishua.bean;


import com.ryx.payment.ruishua.RyxAppconfig;

public class Order {
	
	/* 
	 * @param int id 业务代号ID
	 * @param String ordertype 业务种类
	 * @param String merchantId商户编码 
	 * @param String productId产品编码
	 * /
	
	
	
	/*手机充值 */    
	public  static OrderInfo  PHONE_RECHARGE = new OrderInfo(RyxAppconfig.PHONE_RECHARGE,"手机充值", RyxAppconfig.QT_MOBILE_MERCHANT, RyxAppconfig.QT_MOBILE_PRODUCT,true);
	/*Q币充值 */ 
    public  static OrderInfo  Q_COIN_RECHARGE = new OrderInfo(RyxAppconfig.Q_COIN_RECHARGE,"Q币充值", RyxAppconfig.QT_QCOIN_MERCHANT,RyxAppconfig.QT_QCOIN_PRODUCT,true);
    /*当面收款*/ 
    public  static OrderInfo  PAYMENT_BY_CREDIT2 = new OrderInfo(RyxAppconfig.FACE_OF_RECEIVABLES,"信用支付", RyxAppconfig.QT_CASHER_MERCHANT, RyxAppconfig.QT_CASHER_PRODUCT,true);	
    /*商品销售*/ 
    public  static OrderInfo  GOODS_RECEIVABLES = new OrderInfo(RyxAppconfig.GOODS_RECEIVABLES,"商品销售", RyxAppconfig.QT_GOODS_MERCHANT,RyxAppconfig.QT_GOODS_PRODUCT,true);
    /*信用卡快速还款*/
    public  static OrderInfo  CREDIT_CARD_RECHARGE_TRUE_TIME = new OrderInfo(RyxAppconfig.CREDITCARD_REPAYMENT,"信用卡还款",RyxAppconfig.QT_CREDIT_MERCHANT, RyxAppconfig.QT_CREDIT_PRODUCT,true);
    /*转账*/
    public  static OrderInfo  KUAI_SU_ZHUAN_ZHANG = new OrderInfo(RyxAppconfig.TRANSFER_REPAYMENT,"普通转账", RyxAppconfig.QT_TRANSFER_MERCHANT, RyxAppconfig.QT_TRANSFER_PRODUCTSLOW,true);
    /*手机号普通提款*/
    public  static OrderInfo  PHONE_TI_KUAN_NOT_TRUE_TIME = new OrderInfo(RyxAppconfig.COMMON_WITHDRAW,"手机号普通提款",RyxAppconfig.QT_WITHDRAW_MERCHANT, RyxAppconfig.QT_WITHDRAW_T1_PRODUCT,true);
    /*手机号快速提款*/
	public  static OrderInfo  PHONE_TI_KUAN_TRUE_TIME = new OrderInfo(RyxAppconfig.QUICK_WITHDRAW,"手机号快速提款",RyxAppconfig.QT_WITHDRAW_MERCHANT,RyxAppconfig.QT_WITHDRAW_T0_PRODUCT,true);
    
	/*账户支付  手机号转账*/
	public  static OrderInfo  MOBILE_ACCPAY = new OrderInfo(RyxAppconfig.MOBILE_ACCOUNTPAY,"手机号付款", RyxAppconfig.QT_MOBILE_ACCPAY_MERCHANT, RyxAppconfig.QT_MOBILE_ACCPAY_PRODUCT,true);
	
	
	/*付款方式 4种*/
	public  static OrderInfo  IMPAY_LITTLE= new OrderInfo(RyxAppconfig.IMPAY_LITTLE,"小额付款", RyxAppconfig.QT_IMPAY_LITTLE_MERCHANT, RyxAppconfig.QT_IMPAY_LITTLE_PRODUCT,true);
	public  static OrderInfo  IMPAY_MUCH= new OrderInfo(RyxAppconfig.IMPAY_MUCH,"大额付款", RyxAppconfig.QT_IMPAY_MUCH_MERCHANT, RyxAppconfig.QT_IMPAY_MUCH_PRODUCT,true);
	public  static OrderInfo  IMPAY_SUPER= new OrderInfo(RyxAppconfig.IMPAY_SUPER,"超级付款", RyxAppconfig.QT_IMPAY_SUPER_MERCHANT, RyxAppconfig.QT_IMPAY_SUPER_PRODUCT,true);
	public  static OrderInfo  IMPAY_FREE= new OrderInfo(RyxAppconfig.IMPAY_FREE,"自由付", RyxAppconfig.QT_IMPAY_FREE_MERCHANT, RyxAppconfig.QT_IMPAY_FREE_PRODUCT,true);
	
	// 闪付
	public  static OrderInfo  IMPAY_SHANFU= new OrderInfo(RyxAppconfig.IMPAY_SHANFU,"闪付", RyxAppconfig.QT_SHANFU_MERCHANT, RyxAppconfig.QT_SHANFU_PRODUCT,true);
	//瑞豆付款
    public  static OrderInfo  RUIBEAN_PAY= new OrderInfo(RyxAppconfig.RUIBEAN_PAY,"瑞豆购买", RyxAppconfig.QT_RUIBEAN_MERCHANT, RyxAppconfig.QT_RUIBEAN_PRODUCTID,true);
	//设备激活押金
    public  static OrderInfo  IMPAY_CASHPLEDGE= new OrderInfo(RyxAppconfig.IMPAY_CASHPLEDGE,"设备激活押金", RyxAppconfig.RYX_IMPAY_CASHPLEDGE_MERCHANT, RyxAppconfig.RYX_IMPAY_CASHPLEDGE_PRODUCT,true);

	/*京东卡充值 */ 
    public  static OrderInfo  JD_RECHARGE = new OrderInfo(RyxAppconfig.JD_RECHARGE,"京东卡充值", RyxAppconfig.QT_JD_MERCHANT,RyxAppconfig.QT_JD_PRODUCT,true);
    /*一号店充值 */ 
    public  static OrderInfo  NO1_RECHARGE = new OrderInfo(RyxAppconfig.NO1_RECHARGE,"1号店礼品卡", RyxAppconfig.QT_NO1_MERCHANT,RyxAppconfig.QT_NO1_PRODUCT,true);
    /*亚马逊充值 */ 
    public  static OrderInfo  YMX_RECHARGE = new OrderInfo(RyxAppconfig.YMX_RECHARGE,"亚马逊礼品卡", RyxAppconfig.QT_YMX_MERCHANT,RyxAppconfig.QT_YMX_PRODUCT,true);
    /*苏宁充值 */ 
    public  static OrderInfo  SUNING_RECHARGE = new OrderInfo(RyxAppconfig.SUNING_RECHARGE,"苏宁易购卡", RyxAppconfig.QT_SUNING_MERCHANT,RyxAppconfig.QT_SUNING_PRODUCT,true);
    /* 完美点劵 */
    public  static OrderInfo  WM_RECHARGE = new OrderInfo(RyxAppconfig.WM_RECHARGE,"完美点券", RyxAppconfig.QT_WM_MERCHANT,RyxAppconfig.QT_WM_PRODUCT,true);
    /* 世纪天成 */
    public  static OrderInfo  SJTC_RECHARGE = new OrderInfo(RyxAppconfig.SJTC_RECHARGE,"世纪天成点数", RyxAppconfig.QT_SJTC_MERCHANT,RyxAppconfig.QT_SJTC_PRODUCT,true);
    /* 盛大点券 */
    public  static OrderInfo  SD_RECHARGE = new OrderInfo(RyxAppconfig.SD_RECHARGE,"盛大点券", RyxAppconfig.QT_SD_MERCHANT,RyxAppconfig.QT_SD_PRODUCT,true);
    /* 光宇币 */
    public  static OrderInfo  GY_RECHARGE = new OrderInfo(RyxAppconfig.GY_RECHARGE,"光宇币", RyxAppconfig.QT_GY_MERCHANT,RyxAppconfig.QT_GY_PRODUCT,true);
    /* 波克城市 */
    public  static OrderInfo  BK_RECHARGE = new OrderInfo(RyxAppconfig.BK_RECHARGE,"波克币", RyxAppconfig.QT_BKCS_MERCHANT,RyxAppconfig.QT_BKCS_PRODUCT,true);
    /* 加油卡充值 */
    public  static OrderInfo  REFUEL_RECHARGE = new OrderInfo(RyxAppconfig.REFUEL_RECHARGE,"加油卡充值", RyxAppconfig.QT_REFUEL_MERCHANT,RyxAppconfig.QT_REFUEL_PRODUCT,true);
  
    /* 普通红包 */
    public  static OrderInfo  REDENVELOPE_NORMAL = new OrderInfo(RyxAppconfig.REDENVELOPE_NORMAL, "普通红包", RyxAppconfig.REDENVELOPE_NORMAL_MERCHANT,RyxAppconfig.REDENVELOPE_NORMAL_PRODUCT,true);
    /* 拼手气红包 */
    public  static OrderInfo  REDENVELOPE_LUCKY = new OrderInfo(RyxAppconfig.REDENVELOPE_LUCKY, "拼手气红包", RyxAppconfig.REDENVELOPE_LUCKY_MERCHANT,RyxAppconfig.REDENVELOPE_LUCKY_PRODUCT,true);
    /* 红包充值 */
    public  static OrderInfo  REDENVELOPE_RECHARGE = new OrderInfo(RyxAppconfig.REDENVELOPE_RECHARGE, "账户充值", RyxAppconfig.QT_IMPAY_LITTLE_MERCHANT_A, RyxAppconfig.QT_IMPAY_LITTLE_PRODUCT_A, true);	

    
    /* 支付宝充值 */
    public  static OrderInfo  ALIPAY_RECHARGE = new OrderInfo(RyxAppconfig.ALIPAY_RECHARGE,"支付宝充值", RyxAppconfig.QT_ALIPAY_MERCHANT,RyxAppconfig.QT_ALIPAY_PRODUCT,true);
    /* 流量卡充值 */
    public  static OrderInfo  FLOW_RECHARGE = new OrderInfo(RyxAppconfig.FLOW_RECHARGE,"流量卡充值", RyxAppconfig.QT_FLOW_MERCHANT,RyxAppconfig.QT_FLOW_PRODUCT,true);

	
    /*会员账户充值 */ 
    public  static OrderInfo MEMBER_RECHARGE = new OrderInfo(RyxAppconfig.MEMBER_RECHARGE,"会员费", RyxAppconfig.QT_SENIOR_MERCHANT,RyxAppconfig.QT_SENIOR_BING_PRODUCT,true);
	
}
