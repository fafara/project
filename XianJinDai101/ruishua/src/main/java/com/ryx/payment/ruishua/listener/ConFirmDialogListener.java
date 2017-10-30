
package com.ryx.payment.ruishua.listener;

import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;

public interface ConFirmDialogListener {
    /**
     * 确定按钮监听
     */
    public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog);

    /**
     * 取消按钮监听
     */
    public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog);
}
