package com.ryx.payment.ruishua.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.ryx.lib.devfp.SpUtil;
import com.ryx.payment.ruishua.BuildConfig;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.RyxApplication;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.net.XmlParse;
import com.ryx.payment.ruishua.usercenter.GesturePawdCheckActivity;
import com.ryx.payment.ruishua.usercenter.GesturePawdCheckActivity_;
import com.ryx.payment.ruishua.usercenter.LoginActivity;
import com.ryx.payment.ruishua.usercenter.LoginActivity_;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.GesturesPaswdUtil;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.widget.RyxLoadDialog;
import com.ryx.payment.ruishua.widget.RyxLoadDialogBuilder;
import com.ryx.payment.ryxhttp.callback.StringCallback;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.swiper.utils.CryptoUtils;
import com.sobot.chat.SobotApi;
import com.umeng.analytics.MobclickAgent;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

import static com.ryx.payment.ruishua.RyxAppconfig.TOLOGINACT;

public class BaseActivity extends AppCompatActivity implements AMapLocationListener ,PermissionResult{
    public static final int REQUEST_SUCCESS = 81;
    public static final int REQUEST_Failure = 82;
    public static final int After_REQUEST_SUCCESS = 83;
    public static final int TOAST_MSG = 84;
    public static final int Other_status = 85;

    public Param qtpayApplication;
    public Param qtpayMobileNO;
    public Param qtpayTransDate;
    public Param qtpayTransTime;
    public Param qtpayTransLogNo;
    public Param qtpayToken;

    public Param qtpayblongitude, qtpayblatitude;

    public Param qtpayCustomerId;
    public Param qtpayPhone;
    public Param qtpaySign;
    //    protected RyxPayResult qtpayResult; // 网络请求返回数据
    public ProgressDialog progressDialog;
    public ArrayList<Param> qtpayAttributeList; // qtpay的属性参数列表
    public ArrayList<Param> qtpayParameterList; // qtpay 的下级条目参数列表
    public boolean isNeedThread = true;
    private RyxLoadDialogBuilder loading;

    private boolean isForground = true;
    public String baseprovinceid = "", basecityid = "";
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    public String baselongitude="", baselatitude="";
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    public int requestCode;
    private PermissionUtil.PermissionRequestObject permissionRequestObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RyxApplication.getInstance().addActivity(this);
        // log信息控制 显示隐藏
        LogUtil.setLogdebug(BuildConfig.DEBUG);
        long transLog = Long.parseLong(CryptoUtils.getInstance().getTransLogNo());
        if (transLog == 1) {
            CryptoUtils.getInstance().setTransLogNo(PreferenceUtil.getInstance(BaseActivity.this).getLong("TransLogNO", 0));
        }
        LogUtil.showLog("currentAct:===="+this.getClass());
        locationPermissionCheck();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /**
     * 6.0权限检查
     */
    private void locationPermissionCheck() {
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.locationwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.locationwaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
      String  waring = MessageFormat.format(getResources().getString(R.string.locationwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x0001, this,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
//        requesDevicePermission(waring, 0x0001, new PermissionResult() {
//                    @Override
//                    public void requestSuccess() {
//                        LogUtil.showLog("ryx", "定位requestSuccess==" + DateUtil.getDateTime(new Date()));
//                        startbaseLocation();
//                    }
//                    @Override
//                    public void requestFailed() {
//                        LogUtil.showLog("ryx", "定位requestFailed==" + DateUtil.getDateTime(new Date()));
//                    }
//                }, Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_PHONE_STATE);
    }


    /**
     * 需要连网的页面初始化网络请求参数，使用之前记得初始化 context
     */
    public void initQtPatParams() {
        if (RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().size() == 0) {
            // 在第一次开启程序的时候 初始化公用请求参数
            RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("appUser", RyxAppconfig.APPUSER));
            RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("version", RyxAppconfig.CLIENTVERSION));
            RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("osType", "android" + android.os.Build.VERSION.RELEASE));
            RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("mobileSerialNum", PhoneinfoUtils.getMobileSerialNum(getApplicationContext())));
            RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("userIP", PhoneinfoUtils.getPsdnIp()));
            RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("clientType", RyxAppconfig.CLIENTTYPE));
            RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("deviceToken",new SpUtil(BaseActivity.this).getSp("key")));
        }
        qtpayApplication = new Param("application");
        qtpayAttributeList = new ArrayList<Param>();
        qtpayParameterList = new ArrayList<Param>();
        qtpayMobileNO = new Param("mobileNo");
        qtpayTransDate = new Param("transDate");
        qtpayTransTime = new Param("transTime");
        qtpayTransLogNo = new Param("transLogNo");
        qtpayToken = new Param("token");

        qtpayblongitude = new Param("longitude");
        qtpayblatitude = new Param("latitude");

        qtpayCustomerId = new Param("customerId");
        qtpayPhone = new Param("phone");
        qtpaySign = new Param("sign", RyxAppconfig.API_SIGN_KEY);
        if (isNeedThread) {
            progressDialog = new ProgressDialog(BaseActivity.this);
            progressDialog.setMessage("loading");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
                isForground = true;
        refreshToken();
    }
    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.showLog("BaseonPauseresetTime=========");
        RyxAppconfig.resetTime(BaseActivity.this);
        MobclickAgent.onPause(this);
    }
    public void showLoading(String... message) {
        try {
            if (null != loading) {
                loading.dismiss();
            } else {
                loading = new RyxLoadDialog().getInstance(BaseActivity.this);
                loading.setCanceledOnTouchOutside(false);
            }
            if (message.length > 0) {
                loading.setMessage(message[0]);
            } else {
                loading.setMessage("努力加载中...");
            }
            //不可取消
            loading.setCancelable(false);
            loading.show();
        }catch (Exception e){

        }

    }

    /**
     * 取消等待框
     */
    protected void cancleLoading() {
        try {
            if (null != loading) {
                loading.dismiss();
            }
        }catch (Exception e){

        }

    }

    /**
     * 请求前参数检查
     */
    private String postCheck() {
        RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList().add(new Param("appUser", RyxAppconfig.APPUSER));
        qtpaySign = new Param("sign", RyxAppconfig.API_SIGN_KEY);
        if (qtpayApplication.getValue().equals("UserRegister.Req")) {
            qtpayToken.setValue("0001"); // 注册
        } else if (qtpayApplication.getValue().equals("GetMobileMac.Req")) {
            qtpayToken.setValue("0002");// 验证码
        } else if (false == RyxAppdata.getInstance(BaseActivity.this).isLogin()) {
            qtpayToken.setValue("0000");
        } else {
            qtpayToken.setValue(RyxAppdata.getInstance(BaseActivity.this).getToken());
        }

        qtpayPhone.setValue(RyxAppdata.getInstance(BaseActivity.this).getPhone());

        if (!qtpayApplication.getValue().equals("UserInfoQuery.Req")) {
            qtpayMobileNO.setValue(RyxAppdata.getInstance(BaseActivity.this).getMobileNo());
        }


        if (RyxAppdata.getInstance(BaseActivity.this).isLogin() == false) {
            qtpayCustomerId.setValue("0000");
        } else {
            qtpayCustomerId.setValue(RyxAppdata.getInstance(BaseActivity.this).getCustomerId());
        }
        String transDate = CryptoUtils.getInstance().getTransDate();
        String transTime = CryptoUtils.getInstance().getTransTime();
        qtpayTransDate.setValue(transDate);
        qtpayTransTime.setValue(transTime);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
        String transTime2 = sDateFormat.format(new Date());
        LogUtil.showLog("ryx", "mytransTime2=" + transTime2);
        LogUtil.showLog("ryx", "transDate=" + transDate);
        LogUtil.showLog("ryx", "transTime=" + transTime);
        LogUtil.showLog("ryx", "qtpayTransDate=" + qtpayTransDate.getValue());
        LogUtil.showLog("ryx", "qtpayTransTime=" + qtpayTransTime.getValue());
        qtpayAttributeList.addAll(RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList()); // 添加6个公用属性
        qtpayAttributeList.add(qtpayToken);
        qtpayAttributeList.add(qtpayCustomerId);
        qtpayAttributeList.add(qtpayPhone);
//        if (baselongitude == null || baselongitude.length() == 0) {
//            baselongitude = "0";
//        }
//        if (baselatitude == null || baselatitude.length() == 0) { // 如果获取不到的时候就给一个默认值
//            baselatitude = "0";
//        }
        qtpayblongitude.setValue(baselongitude);
        qtpayblatitude.setValue(baselatitude);

//        if (!(qtpayApplication.getValue().equals("JFPalCardPaySett.Req")||qtpayApplication.getValue().equals("JFPalCardPay.Req"))) {
            qtpayTransLogNo.setValue(CryptoUtils.getInstance().getTransLogNo());
//        }
        if (qtpayParameterList == null) {
            qtpayParameterList = new ArrayList<Param>();
        }
        qtpayParameterList.add(qtpayblongitude);
        qtpayParameterList.add(qtpayblatitude);
        qtpayParameterList.add(qtpayMobileNO);
        qtpayParameterList.add(qtpayTransDate);
        qtpayParameterList.add(qtpayTransTime);
        qtpayParameterList.add(qtpayTransLogNo);
        LogUtil.showLog("qtpayTransLogNo=="+qtpayTransLogNo.getValue()+",qtpayApplication=="+ qtpayApplication.getValue());
        qtpayParameterList.add(qtpaySign);

        long curLog = PreferenceUtil.getInstance(BaseActivity.this).getLong("TransLogNO", 0);

        if (!TextUtils.isEmpty(qtpayTransLogNo.getValue())&& Long.parseLong(qtpayTransLogNo.getValue()) > curLog)
            PreferenceUtil.getInstance(BaseActivity.this).saveLong("TransLogNO", Long.parseLong(qtpayTransLogNo.getValue()));

        String xmlString = null;

        LogUtil.showLog(qtpayApplication.getValue());
        upRequestParams();
        if ("UserIdentityPicUpload2.Req".equals(qtpayApplication.getValue()) || "UserIdentityPicUpload3.Req".equals(qtpayApplication.getValue())
                || "SaveGood.Req".equals(qtpayApplication.getValue()) || "UpdateGood.Req".equals(qtpayApplication.getValue())) { //
            xmlString = XmlParse.creatRequestWithPic(qtpayAttributeList, qtpayParameterList);
        } else {
            xmlString = XmlParse.creatRequest(qtpayAttributeList, qtpayParameterList);
        }
//---------------------------暂时屏蔽--------------AppKey验证------------------------------------------
//                //接口名称
//                String reqApplication=qtpayApplication.getValue().replace(".Req","");
//                //transLog
//                String reqtransLog=qtpayTransLogNo.getValue();
//                //日期时间时间
//                 String reqdate=  qtpayTransDate.getValue();
//               String reqTime= qtpayTransTime.getValue();
//                String appkey=reqApplication+reqtransLog+reqdate+reqTime;
//                RyxAppdata.saveAppKey(appkey);
//---------------------------暂时屏蔽--------------AppKey验证------------------------------------------
        qtpayAttributeList.clear();
        qtpayParameterList.clear();


        return xmlString;
    }

    /**
     * 修改请求参数(用于一些接口需要修改在发起网络请求前修改请求中某些本地存储的数据使用)
     */
    public void upRequestParams() {
    }

    @Override
    protected void onDestroy() {
        cancleLoading();
        stopLocation();
        System.gc();
        super.onDestroy();
        RyxApplication.getInstance().removeActivity(this);

    }

    public void httpsPost(boolean blShowLoading, final boolean blHideLoading, String tag, final XmlCallback callback, String... message)
    {
        if (HttpUtil.checkNet(getApplicationContext())) {
            if(isNeedThread) {
                if(blShowLoading) {
                    showLoading(message);
                }
            }
            String xmlString = postCheck();
            LogUtil.showLog("httprequest", xmlString);
            LogUtil.showLog("qtpayApplication.getValue==+"+xmlString);
            final String value = qtpayApplication.getValue();
            HttpUtil.getInstance().httpsPost(tag, xmlString, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    cancleLoading();
                    //// TODO: 16/4/22 系统异常情况
                    LogUtil.showLog("error", "系统异常--------" + e.getLocalizedMessage());
                    LogUtil.showToast(BaseActivity.this, "访问服务端超时,请检查网络是否正常!!!");
                    callback.onTradeFailed();
                }


                @Override
                public void onResponse(String response) {
                    if(blHideLoading) {
                        cancleLoading();
                    }
                     String resultString = response;
                    LogUtil.showLog("httpresult", response);
                    List list = XmlParse.getXmlList(resultString, RyxPayResult.class, "QtPay");
                    RyxPayResult qtpayResult = (RyxPayResult) list.get(0);
//------------------------------sign及key安全验证--------------------------------------------

                    LogUtil.showLog("qtpayApplication.getValue==+"+value+"+==="+qtpayResult.getApplication());
                    if (!TextUtils.isEmpty(value)) {
//=====================================================暂时屏蔽服务端sign校验及AppKey校验=====================================================
//                        if(!value.equals("ClientUpdate2.Req")){
//                            //因为ClientUpdate2此接口后台在最新版本没有维护的时候返回的sign值是错误的,所以无需校验
//                            String serverSign=qtpayResult.getSign();
//                            //替换为默认signkey
//                            String reseltParams= resultString.trim().replace(serverSign,RyxAppconfig.API_SIGN_KEY);
//                            String mynewsign = CryptoUtils.getInstance().EncodeDigest(URLEncoder.encode(reseltParams).getBytes());
//                            LogUtil.showLog("mynewsign="+mynewsign+",serverSign="+serverSign);
//                            if(!mynewsign.equals(serverSign)){
//                                LogUtil.showLog( "sign校验错误,请重试!");
//                                //以下暂时屏蔽银联监测
//                                LogUtil.showToast(BaseActivity.this, "sign校验错误,请重试!");
//                                cancleLoading();
//                                callback.onTradeFailed();
//                                return;
//                            }
//                        }
                        //接口名称
//                        String rsqApplication=qtpayResult.getApplication().replace(".Rsp","").replace(".Req","");
//                        //transLog
//                        String rsqtransLog = qtpayResult.getTransLogNo();
//                        if (value.equals("UserLogin.Req")) {
//                            //登录接口keyLog返回的是原值，tranLog返回的是最大值
//                            if (null != qtpayResult.getKeyLogNo())
//                                rsqtransLog = qtpayResult.getKeyLogNo();
//                        }
//                        //日期时间时间
//                        String rsqdate= qtpayResult.getTransDate();
//                        String rsqTime= qtpayResult.getTransTime();
//                        String rsqappkey=rsqApplication+rsqtransLog+rsqdate+rsqTime;
//                        if(!RyxAppdata.checkKeyisOk(rsqappkey)){
//                            cancleLoading();
//                            LogUtil.showLog("APPKey校验错误,请重试!==="+rsqappkey);
//                            LogUtil.showToast(BaseActivity.this, "APPKey校验错误,请重试!");
//                            return;
//                        }
//------------------------------sign及key安全验证--------------------------------------------
                        //=====================================================暂时屏蔽服务端sign校验及AppKey校验=====================================================

                        //返回数据分析
                        //如果是登录返回,则进行TransLogNo获取并保存
//                    if (qtpayApplication.getValue().equals("UserLogin.Req")) {
                        if(RyxAppconfig.QTNET_TRANSLOGFAIL.equals(qtpayResult.getRespCode())){
                            if(value.equals("UserLogin.Req")){
                                //登录返回的transLog是最大值
                                if (qtpayResult != null && !TextUtils.isEmpty(qtpayResult.getTransLogNo())) {
                                    PreferenceUtil.getInstance(BaseActivity.this).saveLong("TransLogNO", Long.parseLong(qtpayResult.getTransLogNo()) + 1);
                                }
                            }else{
                                //其他接口返回的keyLog是最大值
                                if (qtpayResult != null && !TextUtils.isEmpty(qtpayResult.getKeyLogNo())) {
                                    PreferenceUtil.getInstance(BaseActivity.this).saveLong("TransLogNO", Long.parseLong(qtpayResult.getKeyLogNo()) + 1);
                                }
                            }
                            long curLog1 = PreferenceUtil.getInstance(BaseActivity.this).getLong("TransLogNO", 0);
                            CryptoUtils.getInstance().setTransLogNo(curLog1);
                        }
//                    }
                        // 处理交易成功
                        if (RyxAppconfig.QTNET_SUCCESS2.equals( qtpayResult.getRespCode())|| qtpayResult.getRespCode().equals(RyxAppconfig.QTNET_SUCCESS)) {
                            callback.onTradeSuccess(qtpayResult);
                        } else if (RyxAppconfig.QTNET_OUTLOGIN2.equals(qtpayResult.getRespCode()) || RyxAppconfig.QTNET_OUTLOGIN1.equals(qtpayResult.getRespCode())) {
                            // 处理登录异常
                            if (value.equals("UserLogin.Req")) {
                                LogUtil.showToast(BaseActivity.this, "请重试!");
                            } else {
                                LogUtil.showToast(BaseActivity.this, "为保证账户安全，请你重新登录！");
                                subActivityRelease(new ReleaseResultListen() {
                                    @Override
                                    public void releaseResultok() {
                                        toAgainLogin(BaseActivity.this,TOLOGINACT,true);
                                    }
                                });
                            }
                            doExit();
//                            RyxAppdata.getInstance(BaseActivity.this).setCustomerId("0000");
//                            RyxAppdata.getInstance(BaseActivity.this).setLogin(false);
                            LogUtil.showLog("onLoginAnomaly");
                            cancleLoading();
                            callback.onLoginAnomaly();
                        } else {
                            if(callback.isToastOtherMsg()){
                                LogUtil.showToast(BaseActivity.this, qtpayResult.getRespDesc());
                            }
                            cancleLoading();
                            callback.onOtherState();
                            callback.onOtherState(qtpayResult.getRespCode(),qtpayResult.getRespDesc());
                        }
                    }else{
                        cancleLoading();
                        LogUtil.showToast(BaseActivity.this, "请重试!!");
                        callback.onTradeFailed();
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.showToast(BaseActivity.this, "请检查网络！");
                }
            });
            callback.onTradeFailed();
        }
    }
    /**
     * 新网络请求方法
     *
     * @param callback
     */
    public void httpsPost(String tag, final XmlCallback callback, String... message) {
         httpsPost(true,true,tag,callback,message);
    }

    public void canelHttpsPost(String tag) {
        HttpUtil.getInstance().canelHttpsPost(tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RyxAppconfig.CLOSE_ALL == resultCode) {
            setResult(RyxAppconfig.CLOSE_ALL,data);
            finish();
        }
    }

    public void startbaseLocation() {
        LogUtil.showLog("ryx","startbaseLocation=="+ DateUtil.getDateTime(new Date()));
        baseprovinceid = PreferenceUtil.getInstance(BaseActivity.this).getString("baseprovinceid", "");
        baselongitude=PreferenceUtil.getInstance(BaseActivity.this).getString("baselongitude", "");
        baselatitude=PreferenceUtil.getInstance(BaseActivity.this).getString("baselatitude", "");
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        //false为持续定位,true为单次定位
        locationOption.setOnceLocation(false);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);

        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置发送定位请求的时间间隔,最小值为1000
        locationOption.setInterval(Long.valueOf(1000));

        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null && location.getErrorCode() == 0) {
            String logit=location.getLongitude()+"";
            if(!TextUtils.isEmpty(logit)&&!"null".equals(logit)){
                baselongitude=logit;
                PreferenceUtil.getInstance(BaseActivity.this).saveString("baselongitude", baselongitude);
            }
            String lati=location.getLatitude() +"";
            if(!TextUtils.isEmpty(lati)&&!"null".equals(lati)){
                baselatitude=lati;
                PreferenceUtil.getInstance(BaseActivity.this).saveString("baselatitude", baselatitude);
            }
            String adCode = location.getAdCode();
            if (!TextUtils.isEmpty(adCode) && !"null".equals(adCode)) {
                baseprovinceid = adCode;
                PreferenceUtil.getInstance(BaseActivity.this).saveString("baseprovinceid", baseprovinceid);
            }
            String country= location.getCountry();//国家信息
            if (!TextUtils.isEmpty(country)) {
                PreferenceUtil.getInstance(BaseActivity.this).saveString("country", country);
            }
//            String street=location.getStreet();//街道信息
//            String streetnumber=location.getStreetNum();//街道门牌号信息

            String address=   location.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            if (!TextUtils.isEmpty(address)) {
                PreferenceUtil.getInstance(BaseActivity.this).saveString("address", address);
            }
            String provice= location.getProvince();//省信息
            if (!TextUtils.isEmpty(provice)) {
                PreferenceUtil.getInstance(BaseActivity.this).saveString("provice", provice);
            }

            String city=location.getCity();//城市信息
            if (!TextUtils.isEmpty(city)) {
                PreferenceUtil.getInstance(BaseActivity.this).saveString("city", city);
            }
            String district=location.getDistrict();//城区信息
            if (!TextUtils.isEmpty(district)) {
                PreferenceUtil.getInstance(BaseActivity.this).saveString("district", district);
            }
            LogUtil.showLog("location=="+location.toString());
            LogUtil.showLog("adCode==" + adCode + ",baselongitude==" + baselongitude + ",baselatitude==" + baselatitude);
            stopLocation();
        } else {
            if (location == null) {
                LogUtil.showLog("定位错误location为空");
            } else {
                LogUtil.showLog("=======定位失败=====" + "错误码:" + location.getErrorCode() + "," + "错误信息:" + location.getErrorInfo() + "," + "错误描述:" + location.getLocationDetail());
            }
            stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    private void stopLocation() {
        LogUtil.showLog("停止定位==============");
        if (locationClient != null && locationClient.isStarted()) {
            // 停止定位
            locationClient.stopLocation();
        }
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 设置当前页面的标题
     *
     * @param title           标题
     * @param leftRightisShow 左侧右侧是否显示,
     * @author xucc
     */
    public void setTitleLayout(String title, boolean... leftRightisShow) {
        try {
       RelativeLayout ryxpaytitlelyout=(RelativeLayout) findViewById(R.id.ryxpaytitlelyout);
       int colorId= RyxAppdata.getInstance(BaseActivity.this).getCurrentBranchTitleBagColor();
        ryxpaytitlelyout.setBackgroundResource(colorId);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        ImageView leftImageView = (ImageView) findViewById(R.id.tileleftImg);
        ImageView rightImageView = (ImageView) findViewById(R.id.tilerightImg);
        if (leftRightisShow.length > 0) {
            //第一个代表左侧返回图标
            boolean leftIshow = leftRightisShow[0];
            if (leftIshow) {
                leftImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backUpImgOnclickListen();
                    }
                });
                leftImageView.setVisibility(View.VISIBLE);
            } else {
                leftImageView.setVisibility(View.INVISIBLE);
            }
            //第二个代表右侧帮助图标
            boolean rightIshow = leftRightisShow[leftRightisShow.length - 1];
            if (rightIshow) {
                rightImageView.setVisibility(View.VISIBLE);
            } else {
                rightImageView.setVisibility(View.INVISIBLE);
            }
        }
        }catch (Exception e){

        }
    }

    /**
     * 右侧ImgView点击事件
     * @param title
     * @param urkKey
     */
    public void setRightImgMessage(final String title, final String urkKey){
        ImageView rightImageView = (ImageView) findViewById(R.id.tilerightImg);
        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BaseActivity.this, HtmlMessageActivity_.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",title);
                bundle.putString("urlkey",urkKey);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    /**
     * 获取右侧按钮View对象
     */
    public ImageView getRightImgView(){
        ImageView rightImageView = (ImageView) findViewById(R.id.tilerightImg);
       return rightImageView;
    }
    /**
     * 设置右上角图片src路径
     * @param resId
     */
    public void setRightImageSrc(int resId){
        ImageView rightImageView = (ImageView) findViewById(R.id.tilerightImg);
        rightImageView.setImageResource(resId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (permissionRequestObject != null&&permissions!=null&&permissions.length!=0&&grantResults!=null&&grantResults.length!=0){
            permissionRequestObject.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void requesDevicePermission(final String waring,int requestCode ,final PermissionResult permissionResult,String... permissions){
//        int version=android.os.Build.VERSION.SDK_INT;
//        if(version>=23){
        if(permissionRequestObject!=null){
            permissionRequestObject=null;
        }
        permissionRequestObject = PermissionUtil.with(this).request(permissions).onAllGranted(
                new Func() {
                    @Override protected void call() {
                        if(permissionResult!=null)
                        permissionResult.requestSuccess();
                    }
                }).onAnyDenied(
                new Func() {
                    @Override protected void call() {
                        if(permissionResult!=null)
                        showMissingPermissionDialog(permissionResult,waring);
                        }
                }).ask(requestCode);
//        }else{
//            if(permissionResult!=null)
//            permissionResult.requestSuccess();
//        }
    }
    // 显示缺失权限提示
    public void showMissingPermissionDialog(final PermissionResult permissionResult, final String warning) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage(warning);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                permissionResult.requestFailed();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    /**
     * 关闭按钮点击监听事件
     */
    protected  void backUpImgOnclickListen(){
        BaseActivity.this.finish();
    };

    @Override
    public void requestSuccess() {
        LogUtil.showLog("ryx", "定位requestSuccess==" + DateUtil.getDateTime(new Date()));
        startbaseLocation();
    }

    @Override
    public void requestFailed() {
        LogUtil.showLog("ryx", "定位requestFailed==" + DateUtil.getDateTime(new Date()));
    }

    /**
     * 设置View2秒内不能重复点击（经测试material布局因为有效果存在，此方法无法控制material布局重复点击，
     * 用disabledTimerAnyView接口控制material布局重复点击）
     * @param v
     */
    public  void disabledTimerView(final View v) {
        if(v!=null){
            v.setClickable(false);
            v.setEnabled(false);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    v.setClickable(true);
                    v.setEnabled(true);
                }
            }, 2000);
        }
    }
    private long lastTime = 0;
    /**
     * 主要用于material布局重复点击使用
     * 如果true则进行限制往下进行即可。
     * 例如：if(disabledTimerAnyView()){
            return;
            }
     */
    protected boolean  disabledTimerAnyView(){
        if ((System.currentTimeMillis() - lastTime) > 2000) {
            lastTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    /**
     *当用户Session失效后，跳转登录页前进行子类资源释放
     * (目前仅Swiper类调用)
     */
    protected void subActivityRelease(ReleaseResultListen releaseResultListen){
        releaseResultListen.releaseResultok();
    }
    public interface  ReleaseResultListen{
        /**
         * 释放资源完毕后监听
         */
        void releaseResultok();
    }
    public interface  CompleteResultListen{
        void compleResultok();
    }

    /**
     * 去重新登录
     */
    public void toAgainLogin(Context context,int requestCode,boolean... iscleartask){
//        String user_id=  RyxAppdata.getInstance(context).getCustomerId();
//        int switchFlag=0;
//        if(!TextUtils.isEmpty(user_id)&&!"0000".equals(user_id)){
//            //如果连用户信息都没有则当前没有登录过,应走LoginActivity_登录密码验证
//            GesturesPaswdUtil spUserid=new GesturesPaswdUtil(context,user_id );
//            switchFlag = spUserid.loadIntSharedPreference("switch");
//        }
//
//        LogUtil.showLog("switchFlag=="+switchFlag+",user_id=="+user_id);
//        if(switchFlag==1){
//            //手势密码开关开着
//            intent = new Intent(context, GesturePawdCheckActivity_.class);
//        }else{
        Intent intent = new Intent(context, LoginActivity_.class);
//        }
        if(iscleartask.length>0&&iscleartask[0]){
            //清空activity栈
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加token失效的标示
            intent.putExtra("tokenIntent", true);
        }
        startActivityForResult(intent,requestCode);
    }

    /**
     * 跳转手势密码界面刷新当前token
     */
    public void refreshToken(){
        LogUtil.showLog("refreshToken111=="+System.currentTimeMillis()+","+RyxAppconfig.getExitTime(BaseActivity.this)+"==");
        if ((System.currentTimeMillis() - RyxAppconfig.getExitTime(BaseActivity.this))<RyxAppconfig.lockTime) {
            //5分钟
                return;
        }
        if(this instanceof SplashActivity){
            return;
        }
        if(this instanceof GesturePawdCheckActivity){
            return;
        }
        if(this instanceof LoginActivity){
            return;
        }
        if(!RyxAppdata.getInstance(this).isLogin()){
            return;
        }
        LogUtil.showLog("refreshToken2222222=="+System.currentTimeMillis());
        String user_id=  RyxAppdata.getInstance(this).getCustomerId();
        if(!TextUtils.isEmpty(user_id)&&!"0000".equals(user_id)){
            //用户登录过，并且打开手势密码开关
             GesturesPaswdUtil spUserid=new GesturesPaswdUtil(this,user_id );
          int  switchFlag = spUserid.loadIntSharedPreference("switch");
        String    paswd=spUserid.loadStringSharedPreference("gesturepwd");
       final int   errorcount = spUserid.loadIntSharedPreference("errorcount");
            if(switchFlag==1&&!TextUtils.isEmpty(paswd)){
                subActivityRelease(new ReleaseResultListen() {
                    @Override
                    public void releaseResultok() {

                        if(errorcount>=3){
                            //错误次数超过三次,则直接登录页
                            LogUtil.showToast(BaseActivity.this,"手势密码绘制错误次数已超限,请账号验证!");
                            toAgainLogin(BaseActivity.this,TOLOGINACT,true);
                        }else{
                            Intent intent = new Intent(BaseActivity.this, GesturePawdCheckActivity_.class);
                            //清空activity栈
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //添加token失效的标示
                            intent.putExtra("tokenIntent", true);
                            startActivityForResult(intent,0x777);
                        }

                    }
                });
            }
        }
}
    public void doExit() {
        QtpayAppData.getInstance(this).setLogin(false);
        QtpayAppData.getInstance(this).setRealName("");
        QtpayAppData.getInstance(this).setMobileNo("");
        QtpayAppData.getInstance(this).setPhone("");
        QtpayAppData.getInstance(this).setCustomerId("");
        QtpayAppData.getInstance(this).setAuthenFlag(0);
        QtpayAppData.getInstance(this).setCustomerName("");
        QtpayAppData.getInstance(this).setToken("");
        SobotApi.exitSobotChat(BaseActivity.this);
    }
    /**
     * 网页webview加载失败后默认页显示（调用当前方法页面布局一定要有<include layout="@layout/ryxpaynointernetlyout"></include>）
     * @param context Activty
     * @param webView webView对象
     * @param completeResultListen  点击刷新按钮后监听事件
     */
    protected void htmlNetworkFail(Activity context, final WebView webView, final CompleteResultListen completeResultListen){
        Button button= (Button)context.findViewById(R.id.button);
        final LinearLayout linearLayout= (LinearLayout)context.findViewById(R.id.ll_noInternet);
        webView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                LogUtil.showLog("htmlNetworkFail==刷新");
                webView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                completeResultListen.compleResultok();
            }
        });
    }
}