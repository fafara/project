package com.ryx.ryxcredit.beans.bussiness.product;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by laomao on 16/9/7.
 * 产品详情
 */
public class CproductRequest extends CbaseRequest {
    private String product_id;
    private String sub_product_id;
    public String getSub_product_id() {
        return sub_product_id;
    }

    public void setSub_product_id(String sub_product_id) {
        this.sub_product_id = sub_product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


}
