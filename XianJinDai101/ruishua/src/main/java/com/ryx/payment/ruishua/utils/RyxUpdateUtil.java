package com.ryx.payment.ruishua.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.dialog.AppDownLoadDialog;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ryxhttp.callback.FileCallBack;

import java.io.File;
import java.text.MessageFormat;

import okhttp3.Call;

/**
 * Created by xiepp on 2017/6/23.
 */

public class RyxUpdateUtil {

    private static RyxUpdateUtil updateUtil;

    private static Context mcontext;

    private String murl;//下载地址
    AppDownLoadDialog appDownLoadDialog;//下载对话框
    private UpdateListener mupdateListener;//回调

    private RyxUpdateUtil() {

    }

    public static RyxUpdateUtil getInstance(Context context) {
        mcontext = context;
        if (updateUtil == null) {
            return new RyxUpdateUtil();
        } else {
            return updateUtil;
        }
    }

    /*
     * 展示下载框
     * @param url下载apk地址
     * @param must 是否必须升级
     */
    public void showUpdataDialog(String url, String must, final UpdateListener updateListener) {
        murl = url;
        mupdateListener = updateListener;
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(mcontext, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                dirpermissionCheck();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                if (mupdateListener != null) {
                    mupdateListener.onCancelUpdateClicked();
                }
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("n".equals(must) ? "发现新版本是否需要升级?" : "发现新版本请升级！");
        if (!"n".equals(must)) {
            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
        }
    }

    /**
     * 文件操作权限判断
     */
    private void dirpermissionCheck() {
        String waring = MessageFormat.format(mcontext.getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(mcontext).getCurrentBranchName());
        final String finalWaring = waring;
        ((BaseActivity) mcontext).requesDevicePermission(waring, 0x0021, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        //确定下载
                        downApk(murl);
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(mcontext, finalWaring);
                        if (mupdateListener != null) {
                            mupdateListener.onUpdatePermRefused();
                        }
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    private void downApk(String url) {
        if (TextUtils.isEmpty(url) || !url.contains("http")) {
            LogUtil.showToast(mcontext, "更新路径有误,请联系客服!!!");
            return;
        }
//        if (ryxLoadDialogBuilder == null) {
//            ryxLoadDialogBuilder =  new RyxLoadDialog().getInstance(SplashActivity.this);
//        }
//        ryxLoadDialogBuilder.setMessage("当前进度为:0.00%");
//        ryxLoadDialogBuilder.show();
        if (appDownLoadDialog == null) {
            appDownLoadDialog = AppDownLoadDialog.simpleShowDownLoadDialog(mcontext);
        } else {
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
                LogUtil.showToast(mcontext, "访问服务端超时,请检查网络是否正常!!!");
            }

            @Override
            public void onResponse(File response) {
                LogUtil.showLog("onResponse====");
            }
        });
    }

    private void showProgressView(float progress, String fileName) {
        appDownLoadDialog.setProgress((int) (progress * 100));
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
        intent.setDataAndType(UriUtils.fromFile(file, mcontext), type);
        mcontext.startActivity(intent);
    }

    public interface UpdateListener {
        /**
         * 用户对升级对话框的操作
         **/
        //用户点击了升级按钮
//        public void onUpateBtnClicked();

        //用户点击了取消升级按钮
        public void onCancelUpdateClicked();

        /**
         * 用户对下载所需权限操作
         **/
        //用户允许文件操作权限
//        public void onUpdatePermConfirmed();

        //用户取消文件操作权限
        public void onUpdatePermRefused();
    }


}
