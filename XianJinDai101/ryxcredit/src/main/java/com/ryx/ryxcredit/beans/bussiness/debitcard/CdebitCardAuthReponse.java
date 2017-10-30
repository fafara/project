package com.ryx.ryxcredit.beans.bussiness.debitcard;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by Administrator on 2016/10/9.
 */

public class CdebitCardAuthReponse extends CbaseResponse {

    private boolean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
