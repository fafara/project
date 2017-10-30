package com.ryx.payment.ruishua.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.AppDownLoadDialog;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.UriUtils;
import com.ryx.payment.ryxhttp.callback.FileCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;

import okhttp3.Call;

@EActivity(R.layout.activity_update)
public class Update extends BaseActivity {
    @ViewById(R.id.tv_version_info)
    TextView mVersionInfo;//1、不需要更新2、最新版本
    @ViewById(R.id.tv_version_name)
    TextView mVersionName;//1、当前版本号2、最新版本号
    @ViewById(R.id.btn_update)
    Button mUpdateBtn;//1、不需要更新隐藏2、需要更新显示
    @ViewById(R.id.iv_update)
    ImageView mUpdateIv;

    @ViewById
    RelativeLayout rl_version_info;
    String version;
    String updateUrl;
    String updateInfo;
    String updateContent;
//    RyxLoadDialogBuilder ryxLoadDialogBuilder;
    AppDownLoadDialog appDownLoadDialog;
    @AfterViews
    public void initViews() {
        setTitleLayout("版本更新",true,false);
        //获取当前版本号
        mVersionName.setText(getVersion());
        RyxAppdata.getInstance(this).glideLoadmipmapLogoImageViewForBranch(mUpdateIv);
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            GlideUtils.getInstance().load(this, R.mipmap.ruishualogo, mUpdateIv);
//        } else if (RyxAppconfig.BRANCH.equals("02")) {
//            GlideUtils.getInstance().load(this, R.mipmap.ruiyinxinlogo, mUpdateIv);
//        }
        bindData();
        initQtPatParams();
    }

    public void bindData() {
        version = PreferenceUtil.getInstance(Update.this).getString("version", "");
        updateContent = PreferenceUtil.getInstance(Update.this).getString("updateContent", "");
        updateUrl = PreferenceUtil.getInstance(Update.this).getString("updateUrl", "");
        updateInfo = PreferenceUtil.getInstance(Update.this).getString("updateInfo", "");
        if (updateInfo.length() > 0 && updateInfo.equals("发现新版本")) {
            mUpdateBtn.setVisibility(View.VISIBLE);
        } else {
            mUpdateBtn.setVisibility(View.GONE);
        }
    }

    private String getVersion() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
//            String version= PreferenceUtil.getInstance(Update.this).getString("version",packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Click(R.id.tileleftImg)
    public void backBtnClick() {
        finish();
    }

    @Click(R.id.btn_update)
    public void updateBtnClick() {
        showUpdataDialog(); // 需要更新就显示升级对话框
    }

    @Click(R.id.rl_version_info)
    public void rl_version_infoClick(){
        doUpdate();
    }
    /**
     * 版本更新检测
     */
    public void doUpdate() {
        qtpayApplication.setValue("ClientUpdate2.Req");
        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(new Param("flag","1"));//只是捡重要的升级
        qtpayParameterList.add(new Param("flag","0"));//有任何版本变化都进行返回升级信息
        httpsPost("ClientUpdateTag", new XmlCallback() {

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                getUpdateInfo(payResult.getData());
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

    private void getUpdateInfo(String jsonstring) {
        try {
            JSONObject jsonObj = new JSONObject(jsonstring);
            if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                    "result").getString("resultCode"))) {
                // 解析更新的信息
                JSONArray resultBeans = jsonObj.getJSONArray("resultBean");
                if (resultBeans != null) {
                    for (int i = 0; i < resultBeans.length(); i++) {
                        updateContent = updateContent
                                + resultBeans.getJSONObject(i).getString(
                                "updateContent") + "\n";
                    }
                    updateUrl = jsonObj.getJSONObject("summary").getString(
                            "updateUrl");
                    showUpdataDialog(); // 需要更新就显示升级对话框
                }
            } else if ("0001".equals(jsonObj.getJSONObject("result").getString(
                    "resultCode"))) {
                LogUtil.showToast(Update.this,"当前版本即最新版本，无需升级!");

            } else if ("0002".equals(jsonObj.getJSONObject("result").getString(
                    "resultCode"))) {
                LogUtil.showToast(Update.this,"当前版本即最新版本，无需升级!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 展示下载框
     */
    private void showUpdataDialog() {

        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                dirpermissionCheck();
                ryxSimpleConfirmDialog.dismiss();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(TextUtils.isEmpty(updateContent)?"发现新版本,请升级！":updateContent);

    }

    public void downApk(String url) {
        if (TextUtils.isEmpty(url) || !url.contains("http")) {
            LogUtil.showToast(Update.this, "更新路径有误,请联系客服!!!");
            return;
        }
        if(appDownLoadDialog==null){
            appDownLoadDialog= AppDownLoadDialog.simpleShowDownLoadDialog(Update.this);
        }else{
            appDownLoadDialog.show();
        }
//        if (ryxLoadDialogBuilder == null) {
//            ryxLoadDialogBuilder = new RyxLoadDialog().getInstance(Update.this);
//            ryxLoadDialogBuilder.setCancelable(false);
//            ryxLoadDialogBuilder.setCanceledOnTouchOutside(false);
//        }
//        ryxLoadDialogBuilder.setMessage("当前进度为:0.00%");
//        ryxLoadDialogBuilder.show();
        final String downUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ruishua/";
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
//                ryxLoadDialogBuilder.dismiss();
                appDownLoadDialog.dismiss();
                LogUtil.showToast(Update.this,"访问服务端超时,请检查网络是否正常!!!");
            }

            @Override
            public void onResponse(File response) {
                LogUtil.showLog("onResponse====");
            }
        });
    }

    private void showProgressView(float progress, String fileName) {
//        DecimalFormat df = new DecimalFormat("#0.00");
//        ryxLoadDialogBuilder.setMessage("当前进度为：" + (df.format(progress * 100)) + "%");
        appDownLoadDialog.setProgress((int)(progress*100));
        if (progress == 1) {
            appDownLoadDialog.dismiss();
            installApk(fileName);
        }
    }

    private void installApk(String filename) {
        File file = new File(filename);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(UriUtils.fromFile(file,Update.this), type);
        startActivity(intent);
    }

    /**
     * 文件操作权限判断
     */
    public void dirpermissionCheck() {
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0021, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        downApk(updateUrl);
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(Update.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }
}
