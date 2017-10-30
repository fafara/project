package com.ryx.ryxcredit.contactrecords;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.newbean.userlevel.cashLimit.CashLimitRequest;
import com.ryx.ryxcredit.newbean.userlevel.cashLimit.CashLimitResponse;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CJSONUtils;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;
import com.ryx.ryxcredit.xjd.CreditActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ryx.ryxcredit.R.id.c_sure_borrow_btn;

/**
 * Created by xiepp on 2017/3/7.
 */

public class CallRecordsActivity extends BaseActivity {

    private String status = "";
    private boolean isFirst = true;//是否是第一次收到查询状态“smsCode”
    private long nowTime = 0;//现在收到查询状态“smsCode”，时间
    private String searchStatus = "NONE";//轮询返回状态
    private ExecutorService catchThreadPool;
    private long firstResearchTime;//第一次轮询时间
    private long lastSmsCodeTime;//上一次收到“验证码已发送”时间
    private Timer smsTimer;//发送验证码的倒计时timer；
    private BottomSheetDialog mBottomSheetDialog;

    private enum CallRecordsEnum {
        RECORDS,
        RECORDS_AUTH
    }

    private String mobilephoneNo;//手机号码
    private String serviceCode;//服务密码
    private CallRecordsLoginFragment loginfragment;
    private CallRecordsAuthFragment authFragment;
    private Timer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_call_records);
        setTitleLayout("手机运营商", true, false);
        catchThreadPool = Executors.newFixedThreadPool(5);
        openFragment(CallRecordsEnum.RECORDS);
    }


    //创建上传获取通话记录的必要信息
    public void doLogin(final String phoneNo, final String edt_pwd) {
        mobilephoneNo = phoneNo;
        serviceCode = edt_pwd;
        //需要重新输入密码
        if("password".equals(searchStatus)){
            HashMap map = new HashMap();
            map.put("password",edt_pwd);
            doSubmit(map);
        }
        //需要输入验证码
        else if("smsCode".equals(searchStatus)){
            sendSmsCode("", phoneNo);
            lastSmsCodeTime = System.currentTimeMillis();
        }//需要填写身份证号、姓名
        else if("nameAndId".equals(searchStatus)){
            openFragment(CallRecordsEnum.RECORDS_AUTH);
        } //首次进入，需要创建订单
        else if("NONE".equals(searchStatus)){
            RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).setMessage("授权需要1-2分钟，请不要退出!");
            RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).show();
            ceateWork(phoneNo,edt_pwd);
        }
    }


    private void ceateWork(final String phoneNo, final String edt_pwd){
        HashMap map = new HashMap();
        map.put("phoneNo", phoneNo);
        map.put("serviceCode", edt_pwd);
        doRequest(map, ReqAction.MOBILE_CERATE, new RequestCallBack() {
            @Override
            public void requestSuccess(String result) {
                CLogUtil.showLog("result---doLogin", result + "---");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if ("0000".equals(jsonObject.getString("code"))
                            || "1018".equals(jsonObject.getString("code"))) {
                        RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).setMessage("授权需要1-2分钟，请不要退出!");
                        RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).show();
                        doSearch(phoneNo, edt_pwd);
                    } else {
                        RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).dismiss();
                        CLogUtil.showToast(CallRecordsActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestFailed(String tip) {
                RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).dismiss();
            }
        });
    }

    private Timer mtimer;

    //查询
    public void doSearch(final String phoneNo, final String servicePwd) {
        HashMap map = new HashMap();
        map.put("phoneNo", phoneNo);
        doRequest(map, ReqAction.MOBILE_SEARCH, new RequestCallBack() {
            @Override
            public void requestSuccess(String result) {
                RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).dismiss();
                CLogUtil.showLog("result---doSearch", result + "----");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String jsresult = jsonObject.getString("result");
                    JSONObject jsObj = new JSONObject(jsresult);
                    String status = "";
                    String reason = "";
                    if (jsObj.has("status")) {
                        status = jsObj.getString("status");
                    }
                    if (jsObj.has("reason")) {
                        reason = jsObj.getString("reason");
                    }
                    CLogUtil.showLog("searchStatus---", searchStatus + "---");
                    switch (status) {
                        case "login":
                            //如果用户之前就是login状态，则有timer在运行，不必进行任何操作，
                            //如果之前不是，则修改状态，执行timer
                            if (!"login".equals(searchStatus)) {
                                searchStatus = "login";
                                RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).setMessage("授权需要1-2分钟，请不要退出!");
                                RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).show();
                                //执行轮询
                                doResearch(phoneNo, servicePwd);
                                firstResearchTime = System.currentTimeMillis();
                            }
                            //根据已经执行的时间判断是否需要继续轮询，超过2分钟停止轮询
                            else {
                                if ((System.currentTimeMillis() - firstResearchTime) > 2 * 60 * 1000) {
                                    if (mtimer != null) {
                                        mtimer.cancel();
                                        mtimer = null;
                                    }
                                    CLogUtil.showToast(CallRecordsActivity.this, "采集运营商信息失败！");
                                }
                            }
                            break;
                        case "smsCode":
                            //如果用户之前就是login状态，则有timer在运行，则关闭timer，
                            // 并且修改searchStatus状态
                            if ("login".equals(searchStatus) && mtimer != null) {
                                mtimer.cancel();
                                mtimer = null;
                            }
                            searchStatus = "smsCode";
                            sendSmsCode(reason, phoneNo);
                            lastSmsCodeTime = System.currentTimeMillis();
                            break;
                        case "password":
                            if ("login".equals(searchStatus) && mtimer != null) {
                                mtimer.cancel();
                                mtimer = null;
                            }
                            searchStatus = "password";
                            CLogUtil.showToast(CallRecordsActivity.this, reason);
                            break;
                        case "nameAndId":
                            if ("login".equals(searchStatus) && mtimer != null) {
                                mtimer.cancel();
                                mtimer = null;
                            }
                            searchStatus = "nameAndId";
                            openFragment(CallRecordsEnum.RECORDS_AUTH);
                            break;
                        case "failed"://测试用
                            if ("login".equals(searchStatus) && mtimer != null) {
                                mtimer.cancel();
                                mtimer = null;
                            }
                            searchStatus = "failed";
                            CLogUtil.showToast(CallRecordsActivity.this, "采集运营商信息失败！");
                            if(loginfragment!=null){
                                loginfragment.setBtnVisiblity(View.GONE);
                            }
                            break;
                        case "success":
                            if ("login".equals(searchStatus) && mtimer != null) {
                                mtimer.cancel();
                                mtimer = null;
                            }
                            searchStatus = "success";
                            CCommonDialog.showRepaymentOK(CallRecordsActivity.this, "采集运营商信息成功", "", new CCommonDialog.IMessage() {
                                @Override
                                public void callback() {
                                    promoteAmount();
                                }
                            });

                            break;
                        case "phoneNul":
                            if ("login".equals(searchStatus) && mtimer != null) {
                                mtimer.cancel();
                                mtimer = null;
                            }
                            searchStatus = "phoneNul";
                            CLogUtil.showToast(CallRecordsActivity.this, "手机号不存在！");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailed(String tip) {
                //如果接口请求失败，之前是login状态则继续请求
                RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment_records = fragmentManager.findFragmentByTag(CallRecordsEnum.RECORDS.name());
        Fragment fragment_auth = fragmentManager.findFragmentByTag(CallRecordsEnum.RECORDS_AUTH.name());
        if (null != fragment_records && fragment_records.isVisible()) {
            finish();
            return;
        }
        if (null != fragment_auth && fragment_auth.isVisible()) {
            super.onBackPressed();
        }
    }

    //轮询search接口
    private void doResearch(final String phoneNo, final String servicePwd) {
        mtimer = new Timer();
        mtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        doSearch(phoneNo, servicePwd);
                    }
                });
            }
        }, 16000l, 16000l);
    }

    //发送验证码
    private void sendSmsCode(String reason, String phoneNo) {
        //第一次收到smscode时间
        if (isFirst) {
            isFirst = false;
            CLogUtil.showToast(CallRecordsActivity.this, reason);
            showBottomSheet(phoneNo);
        } else {
            nowTime = System.currentTimeMillis();
            //当第二次收到smscode时间和第一次时间对比大于10分钟，并且和上一次收到“验证码已发送”
            // 时间差小于24小时，则直接提交验证码为0000
            if (((nowTime - lastSmsCodeTime) > 10 * 60 * 1000)
                    && ((nowTime - lastSmsCodeTime) < 24 * 60 * 60 * 1000)) {
                HashMap map = new HashMap();
                map.put("phoneNo", phoneNo);
                map.put("smsCode", "0000");
                CLogUtil.showToast(CallRecordsActivity.this, reason);
                doSubmit(map);
            }else{
                CLogUtil.showToast(CallRecordsActivity.this, reason);
                showBottomSheet(phoneNo);
            }
        }
    }

    private void sendVertifyCode() {
        if (smsTimer != null) {
            return;
        }
        smsTimer = new Timer();
        startCountdown();
    }

    private int secondsRremaining = 0;//倒计时时间
    /**
     * 开始倒计时60秒
     */
    public void startCountdown() {
        secondsRremaining = 60;
        TimerTask task = new TimerTask() {

            public void run() {
                Message msg = Message.obtain();
                msg.what = secondsRremaining--;
                timeHandler.sendMessage(msg);
            }
        };
        smsTimer.schedule(task, 1000, 1000);
    }

    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                tvVertifyCode.setTextColor(getResources().getColor(R.color.text_a));
                tvVertifyCode.setText(getResources().getString(R.string.resend)
                        + "(" + msg.what + ")");
                tvVertifyCode.setClickable(false);
            } else {
                smsTimer.cancel();
                smsTimer = null;
                tvVertifyCode.setText(getResources().getString(
                        R.string.resend_verification_code));
                tvVertifyCode.setClickable(true);
                tvVertifyCode.setTextColor(Color.parseColor("#1db7f0"));
            }
        }
    };

    private void openFragment(CallRecordsEnum callRecordsEnum) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(callRecordsEnum.name());
        if (callRecordsEnum == CallRecordsEnum.RECORDS) {//填写手机服务密码
            if (fragment == null) {
                loginfragment = new CallRecordsLoginFragment();
                fragment = loginfragment;
            }
        } else if (callRecordsEnum == CallRecordsEnum.RECORDS_AUTH) {//填写身份证和姓名
            if (fragment == null) {
                authFragment = new CallRecordsAuthFragment();
                fragment = authFragment;
                Bundle args = new Bundle();
                args.putString("phoneNo", mobilephoneNo);
                args.putString("serviceCode", serviceCode);
                fragment.setArguments(args);
            }
        }
        if (fragment != null) {
            fragmentTransaction.replace(R.id.c_fl_base_info, fragment, callRecordsEnum.name());
            fragmentTransaction.addToBackStack(callRecordsEnum.name());
            fragmentTransaction.commit();
        }

    }

    private TextView tvVertifyCode;//"重新发送按钮"

    public void showBottomSheet(final String phoneNo) {
        //如果底部输入验证码对话框正在显示，则不重复打开
        if(mBottomSheetDialog!=null&&mBottomSheetDialog.isShowing()){
            setVertifyText();
            return;
        }
        mBottomSheetDialog  = new BottomSheetDialog(CallRecordsActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(CallRecordsActivity.this).inflate(R.layout.c_call_records_sms_check, null);
        tvVertifyCode = (TextView) boottomView.findViewById(R.id.c_btn_sendmsg);
        setVertifyText();
        tvVertifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap map = new HashMap();
                map.put("phoneNo", phoneNo);
                map.put("smsCode", "0000");
                doSubmit(map);
                sendVertifyCode();
            }
        });
        final EditText etPhonecode = (EditText) boottomView.findViewById(R.id.c_et_phonecode);
        TextView tvPhoneno = (TextView) boottomView.findViewById(R.id.c_tv_phoneno);
        tvPhoneno.setText(phoneNo);
        Button buttonSure = (Button) boottomView.findViewById(c_sure_borrow_btn);
        buttonSure.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (TextUtils.isEmpty(etPhonecode.getText().toString().trim())) {
                    CLogUtil.showToast(CallRecordsActivity.this, "请填写验证码!");
                    return;
                }
                mBottomSheetDialog.dismiss();
                HashMap map = new HashMap();
                map.put("smsCode", etPhonecode.getText().toString().trim());
                doSubmit(map);
            }
        });
        mBottomSheetDialog.contentView(boottomView).show();
        sendVertifyCode();
    }

    //设置“发送验证码”文字样式
    private void setVertifyText(){
        if (smsTimer == null) {
            tvVertifyCode.setText(getResources().getString(R.string.c_common_send_verify_code));
            tvVertifyCode.setClickable(true);
            tvVertifyCode.setTextColor(Color.parseColor("#1db7f0"));
        } else {
            tvVertifyCode.setTextColor(getResources().getColor(R.color.text_a));
            tvVertifyCode.setText(getResources().getString(R.string.resend)
                    + "(" + secondsRremaining + ")");
            tvVertifyCode.setClickable(false);
        }
    }

    public void doSubmit(final HashMap map) {
        RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).setMessage("授权需要1-2分钟，请不要退出!");
        RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).show();
        map.put("phoneNo", mobilephoneNo);
        doRequest(map, ReqAction.MOBILE_SUBMIT, new RequestCallBack() {
            @Override
            public void requestSuccess(String result) {
                String code = "";
                String msg = "";
                try {
                    JSONObject jsObj = new JSONObject(result);
                    if (jsObj.has("code")) {
                        code = jsObj.getString("code");
                    }
                    if (jsObj.has("msg")) {
                        msg = jsObj.getString("msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CLogUtil.showLog("result--doSubmit----", result + "---");
                if ("0000".equals(code)) {
                    //把身份认证页面关闭，打开验证服务密码页面
                    if ("nameAndId".equals(searchStatus)) {
                        if (authFragment != null) {
                            openFragment(CallRecordsEnum.RECORDS);
                        }
                    }
                    doSearch((String) map.get("phoneNo"), (String) map.get("serviceCode"));
                } else {
                    RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).dismiss();
                    CLogUtil.showToast(CallRecordsActivity.this, msg);
                }

            }

            @Override
            public void requestFailed(String tip) {
                RyxCreditLoadDialog.getInstance(CallRecordsActivity.this).dismiss();
                CLogUtil.showToast(CallRecordsActivity.this, tip);
            }
        });
    }

    private Handler mhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void doRequest(final Map map, final ReqAction reqAction, final RequestCallBack callback) {
        CLogUtil.showLog("请求参数---", CJSONUtils.getInstance().toJSONString(map) + "---" + RyxcreditConfig.CALL_RECORDS_URL + reqAction.getContent());
        catchThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                doHttpRequest(map, reqAction, callback);
            }
        });

    }

    private void doHttpRequest(final Map map, final ReqAction reqAction, final RequestCallBack callback) {
        try {
            URL urlPost = new URL(RyxcreditConfig.CALL_RECORDS_URL + reqAction.getContent());//此处的URL需要进行URL编码；
            HttpURLConnection httpClient = (HttpURLConnection) urlPost.openConnection();
            httpClient.setRequestMethod("POST");
            httpClient.setDoOutput(true);
            httpClient.setDoInput(true);
            httpClient.setUseCaches(false);
            httpClient.setConnectTimeout(15000);
            httpClient.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpClient.connect();
            OutputStream out = httpClient.getOutputStream();
            out.write(CJSONUtils.getInstance().toJSONString(map).getBytes());
            out.flush();
            out.close();
            CLogUtil.showLog("doRequestiiii---", httpClient.getResponseCode() + "---");
            if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //封装写给服务器的数据（这里是要传递的参数）
                InputStream is = httpClient.getInputStream();
                final byte[] responseBody = getBytesByInputStream(is);
                int code = httpClient.getResponseCode();
                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        if (responseBody != null) {
                            callback.requestSuccess(new String(responseBody).toString());
                        }
                    }
                });
            } else {
                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.requestFailed("connect failed");
                    }
                });
            }
            httpClient.disconnect();
        } catch (Exception e) {
            mhandle.post(new Runnable() {
                @Override
                public void run() {
                    callback.requestFailed("connect failed");
                }
            });
            CLogUtil.showLog("Exception---", e.getMessage() + "---");
            e.printStackTrace();
        }
    }

    private byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    interface RequestCallBack {
        public void requestSuccess(String result);

        public void requestFailed(String tip);
    }

    //提额接口
    private void promoteAmount() {
        final CashLimitRequest request = new CashLimitRequest();
        request.setProduct_id(RyxcreditConfig.xjd_procudtId);
        request.setCustomer_type("4");
        request.setRaise_type("P");
        //提额
        request.setFlag("M");
        httpsPost(this, request, ReqAction.CASH_LIMIT, CashLimitResponse.class, new ICallback<CashLimitResponse>() {

            public void success(CashLimitResponse cashLimitResponse) {
                Integer code = cashLimitResponse.getCode();
                String result = cashLimitResponse.getResult();
               // finish();
    /*            Intent data=new Intent();
                data.putExtra("status", "U");
                //请求代码可以自己设置，这里设置成20
                setResult(20, data);*/
                Intent intent = new Intent(CallRecordsActivity.this, CreditActivity.class);
                startActivity(intent);
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(CallRecordsActivity.this,tips+"");
            }
        });
    }

}
