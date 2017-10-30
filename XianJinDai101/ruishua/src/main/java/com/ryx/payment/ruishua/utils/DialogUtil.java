package com.ryx.payment.ruishua.utils;

import android.content.Context;

import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;

/**
 * Created by Administrator on 2017/7/14.
 */

public class DialogUtil {
    Context context;
    public DialogUtil(Context context){
        this.context=context;
    }
    public static DialogUtil getInstance(Context context){
        return  new DialogUtil(context);
    }
    /**
     * 展示信息提示框
     */
    public  void showSwipingDialog(String content, final BaseActivity.CompleteResultListen completeResultListen, boolean... isOnlyOk) {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(context, new ConFirmDialogListener() {
            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                if(completeResultListen!=null){
                    completeResultListen.compleResultok();
                }
            }
            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(content);
        if(isOnlyOk.length!=0&&isOnlyOk[0]){
            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
        }
    }
}
