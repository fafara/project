package com.ryx.ryxcredit.beans.bussiness.addressbook;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by laomao on 16/12/13.
 */

public class ContactsResponse extends CbaseResponse{

    /**
     * code : 2000
     * result : -4
     */

    private int code;
    private int result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
