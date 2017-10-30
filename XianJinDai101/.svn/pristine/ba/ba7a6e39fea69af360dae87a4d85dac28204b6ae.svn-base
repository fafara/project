package com.ryx.ryxpay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.ryx.payment.ryxhttp.callback.StringCallback;
import com.ryx.ryxpay.RyxAppconfig;
import com.ryx.ryxpay.RyxAppdata;
import com.ryx.ryxpay.bean.Param;
import com.ryx.ryxpay.bean.RyxPayResult;
import com.ryx.ryxpay.net.XmlCallback;
import com.ryx.ryxpay.net.XmlParse;
import com.ryx.ryxpay.utils.HttpUtil;
import com.ryx.ryxpay.utils.LogUtil;
import com.ryx.ryxpay.utils.PhoneinfoUtils;
import com.ryx.ryxpay.utils.PreferenceUtil;
import com.ryx.ryxpay.widget.RyxLoadDialogBuilder;
import com.ryx.swiper.utils.CryptoUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by laomao on 16/4/18.
 */


public class BaseActivity extends AppCompatActivity implements AMapLocationListener {


    public static final int REQUEST_SUCCESS = 81;
    public static final int REQUEST_Failure = 82;
    public static final int After_REQUEST_SUCCESS = 83;
    public static final int TOAST_MSG = 84;
    public static final int Other_status = 85;

    protected Param qtpayApplication;
    protected Param qtpayMobileNO;
    protected Param qtpayTransDate;
    protected Param qtpayTransTime;
    protected Param qtpayTransLogNo;
    protected Param qtpayToken;

    protected Param qtpayblongitude, qtpayblatitude;

    protected Param qtpayCustomerId;
    protected Param qtpayPhone;
    protected Param qtpaySign;
//    protected RyxPayResult qtpayResult; // 网络请求返回数据
    protected ProgressDialog progressDialog;
    protected ArrayList<Param> qtpayAttributeList; // qtpay的属性参数列表
    protected ArrayList<Param> qtpayParameterList; // qtpay 的下级条目参数列表
    protected boolean isNeedThread = true;
    private RyxLoadDialogBuilder loading;

    private String baselongitude, baselatitude;
    private LocationManagerProxy baseMapLocManager = null;
    private AMapLocation baseMapLocation;// 用于判断定位超时
    private boolean isForground = true;

    protected String baseprovinceid = "", basecityid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // log信息控制 显示隐藏
        LogUtil.setLogdebug(true);

        long transLog = Long.parseLong(CryptoUtils.getInstance().getTransLogNo());


        if (transLog == 1)
            CryptoUtils.getInstance().setTransLogNo(PreferenceUtil.getInstance(BaseActivity.this).getLong("TransLogNO", 0));


//        startbaseLocation();
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
        }
        qtpayApplication=new Param("application");
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

    protected void onResume() {

        super.onResume();
        isForground = true;

    }

    public void showLoading() {
        showLoading("努力加载中...");
    }

    public void showLoading(String msg) {
        if (null != loading) {
            loading.dismiss();
        } else {
                loading = RyxLoadDialogBuilder.getInstance(BaseActivity.this);
            loading.setCanceledOnTouchOutside(false);
        }
        loading.setMessage(msg);
        loading.show();
    }

    /**
     * 取消等待框
     */
    protected   void cancleLoading(){
        if (null != loading) {
            loading.dismiss();
        }
    }
    /**
     * 请求前参数检查
     */
    private String postCheck() {
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
        qtpayTransDate.setValue(CryptoUtils.getInstance().getTransDate());
        qtpayTransTime.setValue(CryptoUtils.getInstance().getTransTime());

        qtpayAttributeList.addAll(RyxAppdata.getInstance(BaseActivity.this).getQtpayPublicAttributeList()); // 添加6个公用属性
        qtpayAttributeList.add(qtpayToken);
        qtpayAttributeList.add(qtpayCustomerId);
        qtpayAttributeList.add(qtpayPhone);
        if (baselongitude == null || baselongitude.length() == 0) {
            baselongitude = "0";
        }
        if (baselatitude == null || baselatitude.length() == 0) { // 如果获取不到的时候就给一个默认值
            baselatitude = "0";
        }
        qtpayblongitude.setValue(baselongitude);
        qtpayblatitude.setValue(baselatitude);

        if (!qtpayApplication.getValue().equals("JFPalCardPay.Req")) {
            qtpayTransLogNo.setValue(CryptoUtils.getInstance().getTransLogNo());
        }
        if (qtpayParameterList == null) {
            qtpayParameterList = new ArrayList<Param>();
        }
        qtpayParameterList.add(qtpayMobileNO);
        qtpayParameterList.add(qtpayTransDate);
        qtpayParameterList.add(qtpayTransTime);
        qtpayParameterList.add(qtpayTransLogNo);
        qtpayParameterList.add(qtpaySign);

        long curLog = PreferenceUtil.getInstance(BaseActivity.this).getLong("TransLogNO", 0);

        if (Long.parseLong(qtpayTransLogNo.getValue()) > curLog)
            PreferenceUtil.getInstance(BaseActivity.this).saveLong("TransLogNO", Long.parseLong(qtpayTransLogNo.getValue()));

        String xmlString = null;

        LogUtil.showLog(qtpayApplication.getValue());

        if ("UserIdentityPicUpload2.Req".equals(qtpayApplication.getValue()) || "UserIdentityPicUpload3.Req".equals(qtpayApplication.getValue())
                || "SaveGood.Req".equals(qtpayApplication.getValue()) || "UpdateGood.Req".equals(qtpayApplication.getValue())) { //
            xmlString = XmlParse.creatRequestWithPic(qtpayAttributeList, qtpayParameterList);
        } else {
            xmlString = XmlParse.creatRequest(qtpayAttributeList, qtpayParameterList);
        }


        qtpayAttributeList.clear();
        qtpayParameterList.clear();


        return xmlString;
    }

    /**
     * 新网络请求方法
     *
     * @param callback
     */
    public void httpsPost(String tag, final XmlCallback callback) {
        if (HttpUtil.checkNet(getApplicationContext())) {
            showLoading();
            String xmlString = postCheck();
            LogUtil.showLog("httprequest", xmlString);
            HttpUtil.getInstance().httpsPost(tag, xmlString, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    cancleLoading();
                    //// TODO: 16/4/22 系统异常情况
                    Log.e("error", "系统异常--------"+e.getLocalizedMessage());
                    callback.onTradeFailed();
                }


                @Override
                public void onResponse(String response) {
                    cancleLoading();
                    String resultString = response;
                    LogUtil.showLog("httpresult", response);
                    List list = XmlParse.getXmlList(resultString, RyxPayResult.class, "QtPay");
                    RyxPayResult  qtpayResult = (RyxPayResult) list.get(0);
                    //返回数据分析
                    //如果是登录返回,则进行TransLogNo获取并保存
                    if(qtpayApplication.getValue().equals("UserLogin.Req")){
                        if(qtpayResult!=null&&!TextUtils.isEmpty(qtpayResult.getTransLogNo())){
                            PreferenceUtil.getInstance(BaseActivity.this).saveLong("TransLogNO", Long.parseLong(qtpayResult.getTransLogNo())+1);
                            long curLog1 = PreferenceUtil.getInstance(BaseActivity.this).getLong("TransLogNO", 0);
                            CryptoUtils.getInstance().setTransLogNo(curLog1);
                        }
                    }
                    // 处理交易成功
                    if (qtpayResult.getRespCode().equals(RyxAppconfig.QTNET_SUCCESS)) {
                        callback.onTradeSuccess(qtpayResult);
                    } else if (RyxAppconfig.QTNET_OUTLOGIN2.equals(qtpayResult.getRespCode()) || RyxAppconfig.QTNET_OUTLOGIN1.equals(qtpayResult.getRespCode())) {
                        // 处理登录异常
                        RyxAppdata.getInstance(BaseActivity.this).setCustomerId("0000");
                        RyxAppdata.getInstance(BaseActivity.this).setLogin(false);
                        if (qtpayApplication.getValue().equals("UserLogin.Req")) {
                            LogUtil.showToast(BaseActivity.this,"请重试");
                        }else {
                            LogUtil.showToast(BaseActivity.this,"为保证账户安全，请你重新登!");
                            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        LogUtil.showLog("onLoginAnomaly");
                        callback.onLoginAnomaly();
                    } else {
                        LogUtil.showToast(BaseActivity.this,qtpayResult.getRespDesc());
                        callback.onOtherState();
                    }
                }
            });
        } else
        {
            //// TODO: 16/4/21 没有网络
            Toast.makeText(this, "请检查网络！", Toast.LENGTH_SHORT).show();
        }

    }

    public void canelHttpsPost(String tag) {
        HttpUtil.getInstance().canelHttpsPost(tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RyxAppconfig.CLOSE_ALL == resultCode) {
            setResult(RyxAppconfig.CLOSE_ALL);
            finish();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void startbaseLocation() {
        baseMapLocManager = LocationManagerProxy.getInstance(this);
        /*
         * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
        baseMapLocManager.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 2000, 10, this);
//        handler.postDelayed(stopLocation, 12000);// 设置超过12秒还没有定位到就停止定位
    }

    /**
     * 销毁定位
     */
    private void stopLocation() {
        if (baseMapLocManager != null) {
            baseMapLocManager.removeUpdates(this);
            baseMapLocManager.destory();
        }
        baseMapLocManager = null;
    }

    Runnable stopLocation = new Runnable() {

        @Override
        public void run() {
            if (baseMapLocation == null) {
                stopLocation();// 销毁掉定位
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();// 停止定位
        isForground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        isForground = false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        try {
            if (location != null) {
                this.baseMapLocation = location;// 判断超时机制
                baselongitude = location.getLongitude() + "";
                baselatitude = location.getLatitude() + "";
                baseprovinceid = location.getAdCode();
                LogUtil.showLog("高德定位的经纬度(" + location.getLongitude() + ","
                        + location.getLatitude() + ")");
                stopLocation();// 停止定位
            }
        } catch (Exception e) {
            stopLocation();// 停止定位
            e.printStackTrace();
        }
    }
}



