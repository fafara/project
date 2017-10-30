package com.ryx.ryxcredit.beans.bussiness.msgsend;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by laomao on 16/9/12.
 */
public class CMsgSendRequest extends CbaseRequest{
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    private String product_id;
    
}
