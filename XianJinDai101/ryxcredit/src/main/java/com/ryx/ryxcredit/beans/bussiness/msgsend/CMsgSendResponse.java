package com.ryx.ryxcredit.beans.bussiness.msgsend;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by laomao on 16/9/12.
 */
public class CMsgSendResponse extends CbaseResponse{

    /**
     * code : 2000
     * result : 1232
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
