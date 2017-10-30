package com.ryx.payment.ruishua.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.WindowManager;

import com.ryx.lib.devfp.DataModel;
import com.ryx.lib.devfp.DfpManager;
import com.ryx.lib.devfp.IFpCallback;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.AppDownLoadDialog;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.GesturePawdCheckActivity_;
import com.ryx.payment.ruishua.utils.GesturesPaswdUtil;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.UriUtils;
import com.ryx.payment.ryxhttp.callback.FileCallBack;
import com.ryx.ryxcredit.RyxcreditConfig;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;

import okhttp3.Call;

import static com.ryx.payment.ruishua.RyxAppconfig.TOLOGINACT;

/**
 * Created by Administrator on 2016/5/9.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    String updateContent = "";
    String version;
    String updateUrl;
    String must;
    String updateInfo;
    boolean isstart = false;
//    RyxLoadDialogBuilder ryxLoadDialogBuilder;
AppDownLoadDialog appDownLoadDialog;

//    @ViewById(R.id.iv_splash)
//    ImageView mSplashImg;
	@AfterInject
	public void afterInject(){
        initCreditconfig();

    }
    private void initCreditconfig()
    {
        RyxcreditConfig.CONTENT_LOGIN="com.ryx.payment.ruishua.usercenter.LoginActivity";
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @AfterViews
    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        RyxAppdata.getInstance(this).glideLoadsplashimgImageViewForBranch(mSplashImg);
    }

    /**
     * 版本更新检测
     */
    public void doUpdate() {
        qtpayApplication.setValue("ClientUpdate2.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("flag","1"));//只是捡重要的升级
//        qtpayParameterList.add(new Param("flag","0"));//有任何版本变化都进行返回升级信息
        httpsPost("ClientUpdateTag", new XmlCallback() {

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                doAfterSuccess(payResult);
            }

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

            }
        });
    }

    /**
     * 请求成功后做的事情
     *
     * @param ryxPayResult
     */
    private void doAfterSuccess(RyxPayResult ryxPayResult) {
        if (qtpayApplication.getValue().equals("ClientUpdate2.Req")) {
            getUpdateInfo(ryxPayResult.getData());
        } else if (qtpayApplication.getValue().equals("GetUserInstruction.Req")) {
            getUserInstructionFromJson(ryxPayResult.getData());
            String isfirst = PreferenceUtil.getInstance(SplashActivity.this).getString("isfirst", "");
            if (TextUtils.isEmpty(isfirst)) {
                startActivity(new Intent(SplashActivity.this, GuideActivity_.class));
            } else {
                branchToActivity();
            }
            finish();
        }
    }

    /**
     * 根据当前app内是否登录过并且手势密码有效来判断跳转页面
     */
    private void branchToActivity() {
       boolean isLogined= RyxAppdata.getInstance(this).isLogin();
        String user_id=  RyxAppdata.getInstance(this).getCustomerId();
//        (System.currentTimeMillis() - RyxAppconfig.getExitTime(SplashActivity.this))>=RyxAppconfig.lockTime)&&
        if((isLogined&&!"0000".equals(user_id))){
            //当前用户登录过
            GesturesPaswdUtil spUserid=new GesturesPaswdUtil(this,user_id );
            int  switchFlag = spUserid.loadIntSharedPreference("switch");
            String    paswd=spUserid.loadStringSharedPreference("gesturepwd");
            final int   errorcount = spUserid.loadIntSharedPreference("errorcount");
            if(switchFlag==1&&!TextUtils.isEmpty(paswd)){
                if(errorcount>=3){
                    //错误次数超过三次,则直接登录页
                    LogUtil.showToast(SplashActivity.this,"手势密码绘制错误次数已超限,请账号验证!");
                    toAgainLogin(SplashActivity.this,TOLOGINACT,true);
                    return;
                }else{
                    //手势密码开着
                    Intent intent = new Intent(SplashActivity.this, GesturePawdCheckActivity_.class);
                    //清空activity栈
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //添加token失效的标示
                    intent.putExtra("tokenIntent", true);
                    startActivityForResult(intent,0x777);
                    return;
                }
            }
        }
        startActivity(new Intent(SplashActivity.this, MainFragmentActivity_.class));
    }

    private void getUpdateInfo(String jsonstring) {
        try {
            JSONObject jsonObj = new JSONObject(jsonstring);
            if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                    "result").getString("resultCode"))) {
                updateInfo = "发现新版本";
                // 解析更新的信息
                JSONArray resultBeans = jsonObj.getJSONArray("resultBean");
                if (resultBeans != null) {
                    for (int i = 0; i < resultBeans.length(); i++) {
                        updateContent = updateContent
                                + resultBeans.getJSONObject(i).getString(
                                "updateContent") + "\n";
                    }
                    // updateContent = "强制更新\n强制更新\n强制更新";
                    version = jsonObj.getJSONObject("summary").getString(
                            "version");
                    updateUrl = jsonObj.getJSONObject("summary").getString(
                            "updateUrl");
                    must = jsonObj.getJSONObject("summary").getString("must");
//                    updateUrl= "http://ruiyinxin-10007319.file.myqcloud.com/ryx/ruiyinxin.apk";
//                    must="";
                    PreferenceUtil.getInstance(SplashActivity.this)
                            .saveString("version", version);
                    PreferenceUtil.getInstance(SplashActivity.this)
                            .saveString("updateContent", updateContent);
                    PreferenceUtil.getInstance(SplashActivity.this)
                            .saveString("updateUrl", updateUrl);
                    PreferenceUtil.getInstance(SplashActivity.this)
                            .saveString("must", must);
                    showUpdataDialog(); // 需要更新就显示升级对话框
                }
            } else if ("0001".equals(jsonObj.getJSONObject("result").getString(
                    "resultCode"))) {
                updateInfo = "查询失败";
                getUserInstruction();
            } else if ("0002".equals(jsonObj.getJSONObject("result").getString(
                    "resultCode"))) {
                updateInfo = "不需要更新";
                getUserInstruction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PreferenceUtil.getInstance(SplashActivity.this).saveString(
                    "updateInfo", updateInfo);
        }
    }

    /**
     * 展示下载框
     */
    private void showUpdataDialog() {

        RyxSimpleConfirmDialog ryxSimpleConfirmDialog=new RyxSimpleConfirmDialog(SplashActivity.this,new ConFirmDialogListener(){

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                dirpermissionCheck();
            }
            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                if ("n".equals(must))
                {
                    getUserInstruction();
                }
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(TextUtils.isEmpty(updateContent)? ("n".equals(must) ? "发现新版本是否需要升级?" : "发现新版本请升级！"):updateContent);
        if(!"n".equals(must)){
            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
        }
        ryxSimpleConfirmDialog.setCanceledOnTouchOutside(false);
    }

    public void downApk(String url) {
        if(TextUtils.isEmpty(url)||!url.contains("http")){
            LogUtil.showToast(SplashActivity.this,"更新路径有误,请联系客服!!!");
            return ;
        }
//        if (ryxLoadDialogBuilder == null) {
//            ryxLoadDialogBuilder =  new RyxLoadDialog().getInstance(SplashActivity.this);
//        }
//        ryxLoadDialogBuilder.setMessage("当前进度为:0.00%");
//        ryxLoadDialogBuilder.show();
        if(appDownLoadDialog==null){
            appDownLoadDialog= AppDownLoadDialog.simpleShowDownLoadDialog(SplashActivity.this);
        }else{
            appDownLoadDialog.show();
        }
        final String downUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ruibao/";
        int index = url.lastIndexOf("/");
        final String fname = url.substring(index + 1); // 获取文件名
        HttpUtil.getInstance().httpsFilePost(url, "downApkTag", "", new FileCallBack(downUrl, fname) {
            @Override
            public void inProgress(float progress, long total) {
                LogUtil.showLog("progress=" + progress);
                showProgressView(progress, downUrl + fname);
            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtil.showLog("onError=" + e.getMessage());
                appDownLoadDialog.dismiss();
                LogUtil.showToast(SplashActivity.this,"访问服务端超时,请检查网络是否正常!!!");
            }

            @Override
            public void onResponse(File response) {
                LogUtil.showLog("onResponse====");
            }
        });
    }



//    public void downApk(String url) {
//        if(TextUtils.isEmpty(url)||!url.contains("http")){
//            LogUtil.showToast(SplashActivity.this,"更新路径有误,请联系客服!!!");
//            return ;
//        }
//        if (ryxLoadDialogBuilder == null) {
//            ryxLoadDialogBuilder =  new RyxLoadDialog().getInstance(SplashActivity.this);
//        }
//        ryxLoadDialogBuilder.setMessage("当前进度为:0.00%");
//        ryxLoadDialogBuilder.show();
//        final String downUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ruibao/";
//        int index = url.lastIndexOf("/");
//        final String fname = url.substring(index + 1); // 获取文件名
//        HttpUtil.getInstance().httpsFilePost(url, "downApkTag", "", new FileCallBack(downUrl, fname) {
//            @Override
//            public void inProgress(float progress, long total) {
//                LogUtil.showLog("progress=" + progress);
//                showProgressView(progress, downUrl + fname);
//            }
//
//            @Override
//            public void onError(Call call, Exception e) {
//                LogUtil.showLog("onError=" + e.getMessage());
//                ryxLoadDialogBuilder.dismiss();
//                LogUtil.showToast(SplashActivity.this,"访问服务端超时,请检查网络是否正常!!!");
//            }
//
//            @Override
//            public void onResponse(File response) {
//                LogUtil.showLog("onResponse====");
//            }
//        });
//    }

    /**
     * 获取用户须知
     */
    private void getUserInstruction() {
        qtpayApplication.setValue("GetUserInstruction.Req");
        qtpayAttributeList.add(qtpayApplication);
        String instr_version= PreferenceUtil
                .getInstance(SplashActivity.this).getString("instr_version",
                "0.0.0");
        Param qtpayInstrVersion = new Param("instrVersion", TextUtils.isEmpty(instr_version)?"0.0.0":instr_version);
        qtpayParameterList.add(qtpayInstrVersion);
        httpsPost("getUserInstruction", new XmlCallback() {
            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                doAfterSuccess(payResult);
            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

            }
        });
    }

    private void getUserInstructionFromJson(String jsonData) {
        JSONObject jsonObject = null;
        String phone = "";
        String version = "";
        String instrCode = "";
        String instrContent = "";
        try {
            jsonObject = new JSONObject(jsonData);
            phone = jsonObject.getJSONObject("summary").getString("appPhone");
            version = jsonObject.getJSONObject("summary").getString("version");
            PreferenceUtil.getInstance(SplashActivity.this).saveString(
                    "appPhone", phone);
            PreferenceUtil.getInstance(SplashActivity.this).saveString(
                    "instr_version", version);
            JSONArray instructions = jsonObject.getJSONArray("resultBean");
            for (int i = 0; i < instructions.length(); i++) {
                instrCode = instructions.getJSONObject(i).getString(
                        "instrCode");
                instrContent = instructions.getJSONObject(i).getString(
                        "instrContent");
                LogUtil.showLog("UserInstruction==="+instrCode+","+instrContent);
                PreferenceUtil.getInstance(SplashActivity.this)
                        .saveString(instrCode, instrContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showProgressView(float progress, String fileName) {
//        DecimalFormat df = new DecimalFormat("#0.00");
        appDownLoadDialog.setProgress((int)(progress*100));
        if (progress == 1) {
            appDownLoadDialog.dismiss();
            installApk(fileName);
        }
//		showLoading("当前进度为："+(df.format(progress*100))+"%");
//        if (ryxLoadDialogBuilder == null) {
//            ryxLoadDialogBuilder =  new RyxLoadDialog().getInstance(SplashActivity.this);
//            ryxLoadDialogBuilder.setCancelable(false);
//            ryxLoadDialogBuilder.setCanceledOnTouchOutside(false);
//        }
//        ryxLoadDialogBuilder.setMessage("当前进度为：" + (df.format(progress * 100)) + "%");
//        if (progress == 1) {
//            ryxLoadDialogBuilder.dismiss();
//            installApk(fileName);
//        }
    }

    private void installApk(String filename) {
        File file = new File(filename);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(UriUtils.fromFile(file,this), type);
        startActivity(intent);
    }

//    public void doExit() {
//        QtpayAppData.getInstance(SplashActivity.this).setLogin(false);
//        QtpayAppData.getInstance(SplashActivity.this).setRealName("");
//        QtpayAppData.getInstance(SplashActivity.this).setMobileNo("");
//        QtpayAppData.getInstance(SplashActivity.this).setPhone("");
//        QtpayAppData.getInstance(SplashActivity.this).setCustomerId("");
//        QtpayAppData.getInstance(SplashActivity.this).setAuthenFlag(0);
//        QtpayAppData.getInstance(SplashActivity.this).setCustomerName("");
//        QtpayAppData.getInstance(SplashActivity.this).setToken("");
//    }
    @Override
    public void requestSuccess() {
        super.requestSuccess();
        initQtPatParams();
        try {
            int version = android.os.Build.VERSION.SDK_INT;
            if (version >= 23) {
                String  waring = MessageFormat.format(getResources().getString(R.string.locationwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
                requesDevicePermission(waring, 0x1001, new PermissionResult() {
                            @Override
                            public void requestSuccess() {
                                LogUtil.showLog(this.toString()+"requestSuccess");
                                try {
                                    DfpManager.init(SplashActivity.this).start(new IFpCallback() {
                                        @Override
                                        public void getData(final DataModel data) {
                                            LogUtil.showLog("设备指纹结果:"+data.toString());
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                doUpdate();
                            }

                            @Override
                            public void requestFailed() {
                                LogUtil.showLog(this.toString()+"requestFailed");
                            }
                        },
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
                try {
                    DfpManager.init(SplashActivity.this).start(new IFpCallback() {
                        @Override
                        public void getData(final DataModel data) {
                            LogUtil.showLog("设备指纹结果:"+data.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                doUpdate();
            }
        }catch (Exception e){

        }
    }


    @Override
    public void requestFailed() {
         LogUtil.showToast(SplashActivity.this, MessageFormat.format(getResources().getString(R.string.locationwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName()));
    }

    /**
     *
     * 文件操作权限判断
     */
    public void dirpermissionCheck(){
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0021, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        //确定
                        downApk(updateUrl);
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(SplashActivity.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }
}
