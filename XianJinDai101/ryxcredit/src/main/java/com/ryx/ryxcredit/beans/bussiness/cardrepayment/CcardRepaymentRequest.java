package com.ryx.ryxcredit.beans.bussiness.cardrepayment;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by laomao on 16/9/11.
 */
public class CcardRepaymentRequest extends CbaseRequest{

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String version;

}
