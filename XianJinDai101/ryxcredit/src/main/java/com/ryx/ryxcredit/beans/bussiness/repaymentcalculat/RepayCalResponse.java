package com.ryx.ryxcredit.beans.bussiness.repaymentcalculat;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by laomao on 16/9/19.
 */
public class RepayCalResponse extends CbaseResponse{

    /**
     * code : 2000
     * result : 1204.44
     */

    private int code;
    private double result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
