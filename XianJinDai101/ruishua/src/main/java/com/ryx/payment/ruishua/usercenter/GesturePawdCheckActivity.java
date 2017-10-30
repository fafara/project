package com.ryx.payment.ruishua.usercenter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.RyxApplication;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.MainFragmentActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.GesturesPaswdUtil;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxgesturespswd.util.LockPatternUtil;
import com.ryx.ryxgesturespswd.widget.LockPatternView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.ryx.payment.ruishua.RyxAppconfig.LOGINACTFINISH;
import static com.ryx.payment.ruishua.RyxAppconfig.QTNET_SUCCESS;

/**
 * 手势密码校验
 */
@EActivity(R.layout.activity_gesture_pawd_check)
public class GesturePawdCheckActivity extends BaseActivity {
    @ViewById(R.id.lockPatternView)
    LockPatternView lockPatternView;
    @ViewById(R.id.phone_messageTv)
    TextView messageTv;
    @ViewById(R.id.iv_logo)
    ImageView iv_logo;
    @ViewById(R.id.tologinact_btn)
    Button tologinact_btn;
    private String paswd="";
    private int errorcount=0;
    private static final long DELAYTIME = 600l;
    private long exitTime = 0;
    /**
     * 去登录页面
     */
    private static final int TOLOGINACTIVITY=0x00800;
    /**
     *手势密码验证成功
     */
    private static final int GESTUREPAWDCHECKSUCCESS=0x00801;

    /**
     * 登录密码方式验证成功
     */
    private static final int LOGINPAWDCHECKSUCCESS=0x00802;
    /**
     *手势密码验证失败
     */
    private static final int GESTUREPAWDCHECKFAIL=0x00803;
    private boolean isTokenIntent,nohttpcheck;
    @AfterViews
    public void initData(){
        initQtPatParams();
        isTokenIntent=getIntent().getBooleanExtra("tokenIntent", false);
        nohttpcheck =getIntent().getBooleanExtra("nohttpcheck",false);
        //得到当前用户的手势密码
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
        String user_id=  RyxAppdata.getInstance(GesturePawdCheckActivity.this).getCustomerId();
        GesturesPaswdUtil spUserid=new GesturesPaswdUtil(getApplicationContext(),user_id );
        paswd=spUserid.loadStringSharedPreference("gesturepwd");
        LogUtil.showLog("paswd=="+paswd);
        errorcount=spUserid.loadIntSharedPreference("errorcount");
        //logo显示
        RyxAppdata.getInstance(this).glideLoaddrawableLogoImageViewForBranch(iv_logo);
        messageTv.setText(StringUnit.phoneJiaMi(RyxAppdata.getInstance(this).getPhone()));
        tologinact_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                Intent intent=new Intent(GesturePawdCheckActivity.this,LoginActivity_.class);
                startActivityForResult(intent,TOLOGINACTIVITY);
            }
        });
    }
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
            String user_id=  RyxAppdata.getInstance(GesturePawdCheckActivity.this).getCustomerId();
            GesturesPaswdUtil spUserid=new GesturesPaswdUtil(getApplicationContext(),user_id );
                errorcount = spUserid.loadIntSharedPreference("errorcount");
            if(errorcount>=3){
                LogUtil.showToast(GesturePawdCheckActivity.this,"绘制错误次数已超限,请账号验证!");
                updateStatus(Status.MOSTFAIL);
//                if(nohttpcheck){
//                    //当前是验证手势密码
//                    setResult(GESTUREPAWDCHECKFAIL);
//                    finish();
//                }else if(isTokenIntent){
//                    Intent intent=new Intent(GesturePawdCheckActivity.this,LoginActivity_.class);
//                    startActivityForResult(intent,TOLOGINACTIVITY);
//                }
                clearParamsToLogin();
            }else{
                if(pattern.size() < 4) {
                    updateStatus(Status.LESSERROR);
                } else if(pattern.size() >=4&&LockPatternUtil.checkPaswd(pattern, paswd)) {
                    spUserid.saveSharedPreferences("errorcount",0);
                    updateStatus(Status.CORRECT);
                }else {
                    spUserid.saveSharedPreferences("errorcount",errorcount+1);
                    updateStatus(Status.ERROR);
                    errorcount = spUserid.loadIntSharedPreference("errorcount");
                    if(errorcount>=3){
//                        if(nohttpcheck){
//                            //当前是验证手势密码
//                            setResult(GESTUREPAWDCHECKFAIL);
//                            finish();
//                        }else if(isTokenIntent){
//                            Intent intent=new Intent(GesturePawdCheckActivity.this,LoginActivity_.class);
//                            startActivityForResult(intent,TOLOGINACTIVITY);
//                        }
                        clearParamsToLogin();
                    }

                }
            }
            }
        }
    };

    /**
     * 直接登录页
     */
    private void clearParamsToLogin() {
        doExit();
        toAgainLogin(GesturePawdCheckActivity.this,0x0011,true);
    }

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        if(!TextUtils.isEmpty(status.strId)){
            LogUtil.showToast(GesturePawdCheckActivity.this,status.strId);
        }
//        messageTv.setText(status.strId);
//        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case MOSTFAIL:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功
     */
    private void loginGestureSuccess() {
        if(nohttpcheck){
            setResult(GESTUREPAWDCHECKSUCCESS);
            finish();
        }else{
//            LogUtil.showToast(GesturePawdCheckActivity.this,"网络请求更新session");
            qtpayApplication.setValue("TokenRefresh.Req");
            qtpayAttributeList.add(qtpayApplication);
            try {
                String phoneMsg = DataUtil.encode(getPhoneInfo().toString().trim().replace(" ", ""));
                qtpayParameterList
                        .add(new Param("phoneMsg", phoneMsg));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                qtpayParameterList
                        .add(new Param("phoneMsg", ""));
            }
            httpsPost("TokenRefreshTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    String data=payResult.getData();
                    try {
//                        <![CDATA[{"result":{"token":"c75f15b9a5a736f08031261f727ca96a"},"code":"0000","msg":"成功"}]]>
                        JSONObject dataJsonObj=new JSONObject(data);
                      String code=  JsonUtil.getValueFromJSONObject(dataJsonObj,"code");
                      String msg=  JsonUtil.getValueFromJSONObject(dataJsonObj,"msg");
                        if(QTNET_SUCCESS.equals(code)){
                            String result=   JsonUtil.getValueFromJSONObject(dataJsonObj,"result");
                            JSONObject resultJsonObj=new JSONObject(result);
                          String token=  JsonUtil.getValueFromJSONObject(resultJsonObj,"token");
                          String appuser=  JsonUtil.getValueFromJSONObject(resultJsonObj,"app_user");
                            RyxAppdata.getInstance(GesturePawdCheckActivity.this).setToken(token);
                            if(!TextUtils.isEmpty(appuser)){
                                RyxAppconfig.APPUSER=appuser;
                                LogUtil.showLog("Token刷新APPUSER修改成功");
                            }
                            LogUtil.showLog("Token刷新成功并保存");
                            if (isTokenIntent) {
                                //如果是token失效跳转到的登录，则跳转到瑞刷主页面
                                Intent intent = new Intent(GesturePawdCheckActivity.this, MainFragmentActivity_.class);
                                intent.putExtra("LoginFlag",true);
                                startActivity(intent);
                            } else {
                                setResult(LOGINACTFINISH);
                            }
                            finish();
                        }else{
                            LogUtil.showToast(GesturePawdCheckActivity.this,msg+"");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.showToast(GesturePawdCheckActivity.this,"数据异常请稍后再试!");
                    }
                }
            });
        }
    }


    private enum Status {
        //默认的状态
        DEFAULT("", R.color.grey_a5a5a5),
        LESSERROR("至少连接4个点，请重新绘制", R.color.warning_icon_ff7a6b),
        //密码输入错误
        ERROR("手势密码输入错误", R.color.red_f4333c),
        //密码输入正确
        CORRECT("", R.color.grey_a5a5a5),
        MOSTFAIL("", R.color.grey_a5a5a5);

        private Status(String strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private String strId;
        private int colorId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==TOLOGINACTIVITY&&resultCode==LOGINACTFINISH){
            if(nohttpcheck){
                setResult(LOGINPAWDCHECKSUCCESS);
            }else{
                if (isTokenIntent) {
                    //如果是token失效跳转到的登录，则跳转到瑞刷主页面
                    Intent intent = new Intent(GesturePawdCheckActivity.this, MainFragmentActivity_.class);
                    intent.putExtra("LoginFlag",true);
                    startActivity(intent);
                } else {
                    setResult(LOGINACTFINISH);
                }
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果是token失效跳转到的登录界面，操作后退时会提示两次操作
        if (isTokenIntent && keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                LogUtil.showToast(GesturePawdCheckActivity.this, getResources()
                        .getString(R.string.press_again));
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                if (HttpUtil.checkNet(getApplicationContext())) {
                    qtpayApplication = new Param("application");
                    qtpayApplication.setValue("UserLoginExit.Req");
                    qtpayAttributeList.add(qtpayApplication);
                    httpsPost(false, true, "UserLoginExit", new XmlCallback() {
                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            doExit();
                            RyxApplication.getInstance().exit();
                        }
                    });
                } else {
                    doExit();
                    RyxApplication.getInstance().exit();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
    }

    /**
     * 获取手机信息
     * @return
     */
    public JSONObject getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        JSONObject jsObject = new JSONObject();
        try {
            // 设备参数
            jsObject.put("Host", Build.HOST);
            // 获取手机唯一标识，GSM手机为IMEI码，CDMA手机为MEID码。
            jsObject.put("IMEI_or_MEID", PhoneinfoUtils.getDeviceId(GesturePawdCheckActivity.this));
            // UUID
            jsObject.put("UUID", PhoneinfoUtils.getUUid(GesturePawdCheckActivity.this));
            // MAC地址
            jsObject.put("MacAddress", PhoneinfoUtils.getMacAddress(GesturePawdCheckActivity.this));
            jsObject.put("AndroidId", PhoneinfoUtils.getAndroidId(GesturePawdCheckActivity.this));
            // android系统定制商
            jsObject.put("BRAND", Build.BRAND);
            // 系统版本
            jsObject.put("os_version", Build.VERSION.RELEASE);
            jsObject.put("TotalMemory",
                    PhoneinfoUtils.getTotalMemory(GesturePawdCheckActivity.this));
            // 版本
            jsObject.put("MODEL", Build.MODEL);
            jsObject.put("bankProvinceId", baseprovinceid);

//			// 手机串号
//			jsObject.put("SerialNum", PhoneinfoUtils.getSerialNum(Login.this));
//
//			jsObject.put("SimSerialNum",
//					PhoneinfoUtils.getSimSerialNumber(Login.this));
//
//			jsObject.put("BOARD", Build.BOARD);
//
//			// cpu指令集
//			jsObject.put("CPU_ABI", Build.CPU_ABI);
//			// 设备参数
//			jsObject.put("Device", Build.DEVICE);
//
//			jsObject.put("user", Build.USER);
//			// builder类型
//			jsObject.put("type", Build.TYPE);
//			// build的标签
//			jsObject.put("tags", Build.TAGS);
//			// 显示屏参数
//			jsObject.put("DISPLAY", Build.DISPLAY);
//			// 修订版本列表
//			jsObject.put("ID", Build.ID);
//			// 硬件制造商
//			jsObject.put("MANUFACTURER", Build.MANUFACTURER);
//
//			// sdk版本
//			jsObject.put("sdk_version", Build.VERSION.SDK);
//
//			// 手机制造商
//			jsObject.put("PRODUCT", Build.PRODUCT);
//			// SERIAL
//			jsObject.put("serial", Build.SERIAL);
//
//
//
//			jsObject.put("AvailMemory",
//					PhoneinfoUtils.getAvailMemory(Login.this));
//
//			jsObject.put("MaxCpuFreq", PhoneinfoUtils.getMaxCpuFreq());
//			jsObject.put("MinCpuFreq", PhoneinfoUtils.getMinCpuFreq());
//			jsObject.put("CurCpuFreq", PhoneinfoUtils.getCurCpuFreq());
//			jsObject.put("CpuName", PhoneinfoUtils.getCpuName());
//			jsObject.put("CpuInfo", PhoneinfoUtils.getCpuInfo());
//			// 是否是漫游
//			jsObject.put("NetworkRoaming",
//					PhoneinfoUtils.isNetworkRoaming(Login.this) + "");
//			jsObject.put("SubscriberId",
//					PhoneinfoUtils.getSubscriberId(Login.this));
//			jsObject.put("SimState", PhoneinfoUtils.getSimState(Login.this)
//					+ "");
//			jsObject.put("SimOperatorName",
//					PhoneinfoUtils.getSimOperatorName(Login.this) + "");
//			jsObject.put("SimOperator",
//					PhoneinfoUtils.getSimOperatorName(Login.this) + "");
//			jsObject.put("SoftWareVersion",
//					PhoneinfoUtils.getSoftWareVersion(Login.this) + "");
//			jsObject.put("phoneNum", PhoneinfoUtils.getLine1Number(Login.this)
//					+ "");
//			jsObject.put("NeighboringCellInfo", tm.getNeighboringCellInfo()
//					+ "");
//			jsObject.put("NetworkCountryIso", tm.getNetworkCountryIso() + "");
//			jsObject.put("NetworkOperator", tm.getNetworkOperator() + "");
//			jsObject.put("NetworkOperatorName", tm.getNetworkOperatorName()
//					+ "");
//			jsObject.put("NetworkType", tm.getNetworkType() + "");
//			jsObject.put("PhoneType", tm.getPhoneType() + "");
//			jsObject.put("SimCountryIso", tm.getSimCountryIso() + "");
//			jsObject.put("VoiceMailAlphaTag", tm.getVoiceMailAlphaTag() + "");
//			jsObject.put("VoiceMailNumber", tm.getVoiceMailNumber() + "");
//			jsObject.put("hasIccCard", tm.hasIccCard() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsObject;
    }
}
