package com.ryx.ryxcredit.beans.bussiness.loanapply;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by Administrator on 2017/2/3.
 */

public class CfindBorrowRouteResponse extends CbaseResponse {


    /**
     * payment_text : payment_confirm_page
     * payment_status : 3
     * payment_type : PAGE
     */

    private ResultBean result;
    /**
     * result : {"payment_text":"payment_confirm_page","payment_status":3,"payment_type":"PAGE"}
     * code : 2000
     */

    private String code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ResultBean {
        private String payment_text;
        private String payment_status;
        private String payment_type;

        public String getPayment_text() {
            return payment_text;
        }

        public void setPayment_text(String payment_text) {
            this.payment_text = payment_text;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }
    }
}
