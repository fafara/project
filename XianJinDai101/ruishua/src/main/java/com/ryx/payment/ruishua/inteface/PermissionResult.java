package com.ryx.payment.ruishua.inteface;

/**
 * Created by XCC on 2016/6/14.
 */
public interface PermissionResult {
    /**
     * 授予权限成功
     */
    void requestSuccess();

    /**
     * 授予权限失败
     */
    void requestFailed();
}
