package com.ryx.ryxcredit.xjd.bean.EmailBill;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.io.Serializable;

/**
 * Created by RYX on 2017/9/22.
 */

public class EmailBillResponse extends CbaseResponse implements Serializable {

    /**
     * code : 2000
     * result : SUCCESS
     */

    private int code;
    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}