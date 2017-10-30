package com.ryx.ryxcredit.beans.bussiness.activateline;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by DIY on 2016/9/13.
 */
public class CActivateLimitRequest extends CbaseRequest {

    private String score;
    private String face_id;
    private String product;
    private String type;

    public String getCredit_type() {
        return credit_type;
    }

    public void setCredit_type(String credit_type) {
        this.credit_type = credit_type;
    }

    private String credit_type;


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    private String kind;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag;

    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }

    public String getCustomer_type() {
        return Customer_type;
    }

    public void setCustomer_type(String customer_type) {
        Customer_type = customer_type;
    }

    private String Product_id;
    private String Customer_type;
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }
}
