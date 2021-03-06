package com.ryx.ryxpay;

import android.os.Environment;

/**
 * Created by laomao on 16/4/19.
 */
public class RyxAppconfig {
    public static final boolean DEBUG = false;
    public static  String APPUSER ="ruiyinxin";
    public static final String CLIENTVERSION = "3.0.2";
    public static  String API_SIGN_KEY ="s0mvr01ga0mavhiiqh36lvl9ahprgn76";

//	public static final boolean DEBUG = true;
//	public static final String APPUSER = "qtpay";
//	public static final String CLIENTVERSION = "1.2.0";
//	public static final String API_SIGN_KEY ="412fadsfoinhuc450f8jcnalzq08mfja";


    public static final String BASE_DEBUG_URL =  "https://119.254.93.70:4430/unifiedAction.do";//"https://apptest.imobpay.com:5566/unifiedAction.do";//"https://211.161.194.123:5566/unifiedAction.do";//

    //生产
    public static final String BASE_RELEASE_URL = "https://mposprepo.ruiyinxin.com:443/unifiedAction.do";

    //测试
//	public static final String BASE_RELEASE_URL =  "https://119.254.93.70:4430/unifiedAction.do";//"https://apptest.imobpay.com:5566/unifiedAction.do";//"https://211.161.194.123:5566/unifiedAction.do";//


    public static String gethost() {
        if (DEBUG) {
            return RyxAppconfig.BASE_DEBUG_URL;
        } else {
            return RyxAppconfig.BASE_RELEASE_URL;
        }
    }

    public static final String CLIENTTYPE = "02";

    // 网络请求返回码

    public static final String QTNET_SUCCESS = "0000";
    public static final String QTNET_OUTLOGIN1 = "0002";
    public static final String QTNET_OUTLOGIN2 = "0001";
    public static final String QTNET_NEEDFILLINFO = "00A0";

    public static final String ServiceMail = "wangmeng-bj@ruiyinxin.com";
    public static final String download_url = "http://www.imobpay.com/ryx.html";
    public static final String userType = "00"; // 个人的代码

    // merchant Id product id
    public static final String QT_CASHER_NEW_MERCHANTA = "0001000003";
    public static final String QT_CASHER_NEW_MERCHANTB = "0001000004";
    public static final String QT_CASHER_NEW_MERCHANTC = "0001000005";
    public static final String QT_CASHER_NEW_MERCHANTD = "0001000006";

    public static final String QT_CASHER_MERCHANT = "0002000002"; // 当面收款
    public static final String QT_CASHER_PRODUCT = "0000000001";
    public static final String QT_GOODS_MERCHANT = "0003000001"; // 商品销售
    public static final String QT_GOODS_PRODUCT = "0000000001";
    public static final String QT_CREDIT_MERCHANT = "0002000001"; // 信用卡快速还款
    public static final String QT_CREDIT_PRODUCT = "0000000000";
    public static final String QT_TRANSFER_MERCHANT = "0002000003"; // 转账
    public static final String QT_TRANSFER_PRODUCTSLOW = "0000000001";//转账 t+1 （原快速）
    public static final String QT_TRANSFER_PRODUCTQUICK = "0000000002";//转账 t+0
    public static final String QT_WITHDRAW_MERCHANT = "0009000001"; // 提现
    public static final String QT_WITHDRAW_T0_PRODUCT = "0000000001";// T+0 快速提款
    public static final String QT_WITHDRAW_T1_PRODUCT = "0000000000";// T+1 普通提款
    public static final String QT_QCOIN_MERCHANT = "0002000005"; // Q币
    public static final String QT_QCOIN_PRODUCT = "0000000000";
    public static final String QT_MOBILE_MERCHANT = "0001000001"; // 手机充值
    public static final String QT_MOBILE_PRODUCT = "0000000000";

    public static final String QT_JD_MERCHANT = "0002000006"; // 京东卡
    public static final String QT_JD_PRODUCT = "0000000000";

    public static final String QT_NO1_MERCHANT = "0002000007"; // 一号店卡
    public static final String QT_NO1_PRODUCT = "0000000000";
    public static final String QT_SUNING_MERCHANT = "0002000008"; // 苏宁卡
    public static final String QT_SUNING_PRODUCT = "0000000000";
    public static final String QT_YMX_MERCHANT = "0002000009"; // 亚马逊卡
    public static final String QT_YMX_PRODUCT = "0000000000";

    public static final String QT_WM_MERCHANT = "0002000010"; // 完美点劵
    public static final String QT_WM_PRODUCT = "0000000000";

    public static final String QT_SJTC_MERCHANT = "0002000011"; // 世纪天成点数
    public static final String QT_SJTC_PRODUCT = "0000000000";

    public static final String QT_SD_MERCHANT = "0002000012"; // 盛大点券
    public static final String QT_SD_PRODUCT = "0000000000";

    public static final String QT_GY_MERCHANT = "0002000013"; // 光宇币
    public static final String QT_GY_PRODUCT = "0000000000";

    public static final String QT_BKCS_MERCHANT = "0002000014"; // 波克城市
    public static final String QT_BKCS_PRODUCT = "0000000000";

    public static final String QT_REFUEL_MERCHANT = "0002000015"; // 加油卡充值
    public static final String QT_REFUEL_PRODUCT = "0000000000";

    public static final String REDENVELOPE_NORMAL_MERCHANT = "0002000016"; // 普通红包
    public static final String REDENVELOPE_NORMAL_PRODUCT = "0000000000";

    public static final String REDENVELOPE_LUCKY_MERCHANT = "0002000017"; // 拼手气红包
    public static final String REDENVELOPE_LUCKY_PRODUCT = "0000000000";




    public static final String QT_FLOW_MERCHANT = "0002000019"; // 流量卡充值
    public static final String QT_FLOW_PRODUCT = "0000000000";


    public static final String QT_ALIPAY_MERCHANT = "0002000020"; // 支付宝充值
    public static final String QT_ALIPAY_PRODUCT = "0000000000";


    public static final String QT_MOBILE_ACCPAY_MERCHANT = "0002000002"; // 手机号转账  账户付款
    public static final String QT_MOBILE_ACCPAY_PRODUCT = "0000000004";


    public static final String QT_IMPAY_LITTLE_MERCHANT = "0001000003"; // // 支付方式 4种
    public static final String QT_IMPAY_LITTLE_PRODUCT = "0000000000";
    public static final String QT_IMPAY_MUCH_MERCHANT = "0001000004";
    public static final String QT_IMPAY_MUCH_PRODUCT = "0000000000";
    public static final String QT_IMPAY_SUPER_MERCHANT = "0001000005";
    public static final String QT_IMPAY_SUPER_PRODUCT = "0000000000";
    public static final String QT_IMPAY_FREE_MERCHANT = "0001000006";
    public static final String QT_IMPAY_FREE_PRODUCT = "0000000000";

    public static final String QT_IMWITHDRAW_MERCHANT = "0001000007"; //即时取
    public static final String QT_IMWITHDRAW_PRODUCT = "0000000001";
    public static final String QT_SENIOR_MERCHANT = "0002000028"; // 高级认证充值
    public static final String QT_SENIOR_BING_PRODUCT = "0000000000";
    public static final String QT_SENIOR_GOTO_PRODUCT = "0000000001";
    // request code
    public static final int QT_RESULT_OK = 90;
    public static final int QT_RESULT_SIGN_CANCEL = 91;

    public static final int CREATE_SIGNREQUISITIONS = 1; // 生成签购单
    public static final int VIEW_SIGNREQUISITIONS = 2; // 查看签购单

    public static final String QT_IMPAY_LITTLE_MERCHANT_A = "0001000003"; // // 支付方式 4种
    public static final String QT_IMPAY_LITTLE_PRODUCT_A = "0000000000";
    public static final String QT_IMPAY_MUCH_MERCHANT_B = "0001000004";
    public static final String QT_IMPAY_MUCH_PRODUCT_B = "0000000000";
    public static final String QT_IMPAY_SUPER_MERCHANT_C = "0001000005";
    public static final String QT_IMPAY_SUPER_PRODUCT_C = "0000000000";
    public static final String QT_IMPAY_FREE_MERCHANT_D = "0001000006";
    public static final String QT_IMPAY_FREE_PRODUCT_D = "0000000000";
    // 订单相关编号

    public static final int PHONE_RECHARGE = 1;
    public static final int CREDITCARD_REPAYMENT = 3;
    public static final int FACE_OF_RECEIVABLES = 6;
    public static final int TRANSFER_REPAYMENT = 10;
    public static final int COMMON_WITHDRAW = 14;// 手机号普通提款
    public static final int QUICK_WITHDRAW = 15;// 手机号快速提款
    public static final int Q_COIN_RECHARGE = 18; // Q币充值
    public static final int GOODS_RECEIVABLES = 20; // 商品收款
    public static final int MOBILE_ACCOUNTPAY = 8; // 手机号转账 账户付款


    public static final int IMPAY_LITTLE = 21; // 支付方式 4种
    public static final int IMPAY_MUCH = 22;
    public static final int IMPAY_SUPER = 23;
    public static final int IMPAY_FREE = 24;

    public static final int JD_RECHARGE = 25; // 京东卡充值
    public static final int NO1_RECHARGE = 26; // 一号店充值
    public static final int YMX_RECHARGE = 27; // 亚马逊充值
    public static final int SUNING_RECHARGE = 28; // 苏宁充值

    public static final int WM_RECHARGE = 29; // 苏宁充值
    public static final int SJTC_RECHARGE = 30; //世纪天成
    public static final int SD_RECHARGE = 31; //盛大点券
    public static final int GY_RECHARGE = 32;  //光宇币
    public static final int BK_RECHARGE = 33;  //波克城市
    public static final int REFUEL_RECHARGE = 34;  //加油卡充值

    public static final int REDENVELOPE_NORMAL = 41;  //普通红包
    public static final int REDENVELOPE_LUCKY = 42;  //拼手气红包
    public static final int REDENVELOPE_RECHARGE = 43;  //红包充值

    public static final int ALIPAY_RECHARGE = 35; //支付宝充值
    public static final int FLOW_RECHARGE = 36; //流量卡充值



    public static final int WILL_BE_CLOSED = 8888;// activity关闭控制
    public static final int CLOSE_ALL = 8886; // 退出代号

    public static final int CLOSE_AT_SWIPER = 8885; // 退出刷卡代号


    public static final int REQUEST_CONTACT = 49;// 调用通讯录
    public static final int MEMBER_RECHARGE = 52; // 高级认证充值
    // instruction 须知字段

    public static final String Notes_Regist = "UserRegist.Login";
    public static final String Notes_Credit = "CreditCardPayment.PaymentInfo";
    public static final String Notes_Transfer = "Transfer.TransferInfo";
    public static final String Notes_Mobile = "MobileRecharging.RechargeInfo";
    public static final String Notes_WtihDraw = "MobileDrawings.DrawingInfo";
    public static final String Notes_Cash = "shouKuan.info";
    public static final String Notes_SignMerchantText = "signmerchantText.info";
    public static final String Notes_SignMerchant = "signmerchant.info";
    public static final String Notes_SignPersonText = "signPersonText.info";
    public static final String Notes_SignPerson = "signPerson.info";
    public static final String Notes_Notice = "instructionsAll.info";
    public static final String Notes_About = "aboutUs.info";
    public static final String Notes_Guide = "zhangGuiBaoIndex.info";

    public static final String Notes_Guarantee = "guarantee.info";
    public static final String Notes_Senior = "senior.info";
    public static final String Notes_Impay = "ImPay.info";
    public static final String Notes_ImWithdraw = "ImWithdraw.info";
    public static final String Notes_MobileAccPay = "MobileAccPay.info";

    public static final String Notes_ImpayLittle = "ImpayLittle.info";
    public static final String Notes_ImpayMuch = "ImapyMuch.info";
    public static final String Notes_ImpaySuper = "ImapySuper.info";
    public static final String Notes_ImpayFree = "ImapyFree.info";


    public static final String Notes_MobileRecharge = "MobileRecharge.info";
    public static final String Notes_QCoinRecharge = "QcoinRecharge.info";
    public static final String Notes_RefuelRecharge = "refuel.info";

    public static final String Notes_PosLoan = "posloan.info";
    public static final String Notes_Vipapply = "vipapply.info";
    public static final String Notes_Game = "gamecharge.info";

    public static final String Notes_Tips = "AuthTips.info";//拍照提示
    public static final String Notes_JDRecharge = "JdCharge.info";
    public static final String Notes_NO1Recharge = "yhdCharge.info";
    public static final String Notes_YMXRecharge = "amazonCharge.info";
    public static final String Notes_SUNINGRecharge = "suningCharge.info";

    public static final String Notes_WMRecharge = "wanmei.info";
    public static final String Notes_SJTCRecharge = "shiji.info";
    public static final String Notes_SDRecharge = "shengda.info";
    public static final String Notes_GYRecharge = "guangyu.info";
    public static final String Notes_BKRecharge = "boke.info";

    public static final String Notes_RedEnvelope_Rule = "gift.info";
    public static final String Notes_RedEnvelope_QA = "qagift.info";

    public static final String Notes_ALipay = "alipayCharge.info";
    public static final String Notes_Flow = "flowCharge.info";

    public static final String Notes_SeniorAgree = "senioragreement.info";
    // 图片处理的 requestcode
    public static final int REQ_CODE_IMAGE_FROM_CAMERA = 30;
    public static final int REQ_CODE_IMAGE_FROM_GALLERY = 31;
    public static final int REQ_CODE_IMAGE_CROP = 32;

    public static final String Blue_Name1 = "AC";
    public static final String Blue_Name2 = "IPOS";
    public static final String Blue_Name3 = "M";
    public static final String Blue_Name4="XGD";


    public static final String SDPATH=Environment.getExternalStorageDirectory().getPath() + "//";
    public static final String LOGFILENAME="imob.log";
    public static final String imageCachePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/imob/imageCache/";
    public static String imageCachePath_data;

    public static final String TEMP_IMAGENAME = "temp_image_name";
    public static final String IMAG_IDENTITY1 = "identity_img1";
    public static final String IMAG_IDENTITY2 = "identity_img2";
    public static final String IMAG_CERTIFICATE = "certificate_img1";
    public static final String IMAG_PROFILE = "profile_img1";


    public static final String IMAG_SENIER_TEMP = "temp_credit_img";
    public static final String IMAG_SENIER_CREDIT = "credit_img";


    public static final String aidStr1 = "319F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000999999999DF2106000000100000";
    public static final String aidStr2 = "319F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000999999999DF2106000000100000";
    public static final String aidStr3 = "319F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000999999999DF2106000000100000";
    public static final String aidStr4 = "319F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000999999999DF2106000000100000";

    public static final int device_step_type_1_audio = 0x201401;//纯音频设备检测
    public static final int device_step_type_2_audio= 0x201402;
    public static final int device_step_type_1_blue = 0x201403;//蓝牙设备音频检测
    public static final int device_step_type_2_blue = 0x201404;
    public static final int device_step_type_ok = 0x201406;
    public static final boolean ShowSenior = true;
}
