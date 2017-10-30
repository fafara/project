package com.ryx.ryxcredit.newbean.userlevel.cashLimit;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by RYX on 2017/5/18.
 */

public class CashLimitRequest extends CbaseRequest {
    private String product_id;
    private String customer_type ;
    private String flag;

    public String getRaise_type() {
        return raise_type;
    }

    public void setRaise_type(String raise_type) {
        this.raise_type = raise_type;
    }

    private String raise_type;
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }



    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
