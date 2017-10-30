package com.ryx.ryxcredit.xjd.bean.JdSubmmit;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by RYX on 2017/9/27.
 */

public class JdSubmitResponse extends CbaseResponse{

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
