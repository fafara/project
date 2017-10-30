package com.ryx.ryxcredit.beans.bussiness.findcustomer;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by laomao on 16/8/13.
 */
public class CfindCustomerRequest extends CbaseRequest {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

}
