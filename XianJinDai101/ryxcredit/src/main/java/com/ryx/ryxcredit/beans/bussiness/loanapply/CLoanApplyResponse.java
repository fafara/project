package com.ryx.ryxcredit.beans.bussiness.loanapply;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by laomao on 16/9/12.
 */
public class CLoanApplyResponse extends CbaseResponse{

    /**
     * code : 2000
     * application_id :
     */

    private int code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
