package com.ryx.ryxcredit.beans.bussiness.loanapply;

/**
 * Created by Administrator on 2017/2/9.
 */

public class CSureBorrowRouteResponse {

    /**
     * quick_status : 3
     * quick_text : repayment_page
     * quick_type : PAGE
     */

    private ResultBean result;
    /**
     * result : {"quick_status":3,"quick_text":"repayment_page","quick_type":"PAGE"}
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
        private String quick_status;
        private String quick_text;
        private String quick_type;

        public String getQuick_status() {
            return quick_status;
        }

        public void setQuick_status(String quick_status) {
            this.quick_status = quick_status;
        }

        public String getQuick_text() {
            return quick_text;
        }

        public void setQuick_text(String quick_text) {
            this.quick_text = quick_text;
        }

        public String getQuick_type() {
            return quick_type;
        }

        public void setQuick_type(String quick_type) {
            this.quick_type = quick_type;
        }
    }
}
