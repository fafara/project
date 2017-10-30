package com.ryx.ryxcredit.newactivity.bean;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by RYX on 2017/5/22.
 */

public class IncreaseAmountResponse extends CbaseRequest {

    /**
     * code : 2000
     * result : {"accumulation_fund_status":1,"bank_credit_status":1,"limit_amount":50000,"mobile_operator_status":1,"online_shopping_status":1,"product_id":"8007"}
     */

    private String code;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * accumulation_fund_status : 1
         * bank_credit_status : 1
         * limit_amount : 50000
         * mobile_operator_status : 1
         * online_shopping_status : 1
         * product_id : 8007
         */

        private String accumulation_fund_status;
        private String bank_credit_status;
        private String limit_amount;
        private String mobile_operator_status;
        private String online_shopping_status;
        private String product_id;

        public String getAccumulation_fund_status() {
            return accumulation_fund_status;
        }

        public void setAccumulation_fund_status(String accumulation_fund_status) {
            this.accumulation_fund_status = accumulation_fund_status;
        }

        public String getBank_credit_status() {
            return bank_credit_status;
        }

        public void setBank_credit_status(String bank_credit_status) {
            this.bank_credit_status = bank_credit_status;
        }

        public String getLimit_amount() {
            return limit_amount;
        }

        public void setLimit_amount(String limit_amount) {
            this.limit_amount = limit_amount;
        }

        public String getMobile_operator_status() {
            return mobile_operator_status;
        }

        public void setMobile_operator_status(String mobile_operator_status) {
            this.mobile_operator_status = mobile_operator_status;
        }

        public String getOnline_shopping_status() {
            return online_shopping_status;
        }

        public void setOnline_shopping_status(String online_shopping_status) {
            this.online_shopping_status = online_shopping_status;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }
    }
}
