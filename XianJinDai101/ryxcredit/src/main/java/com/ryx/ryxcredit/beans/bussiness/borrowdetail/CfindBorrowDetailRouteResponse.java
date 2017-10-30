package com.ryx.ryxcredit.beans.bussiness.borrowdetail;

/**
 * Created by Administrator on 2017/2/9.
 */

public class CfindBorrowDetailRouteResponse {


    /**
     * replacing_card_text : replacing_card_page
     * repayment_type : PAGE
     * repayment_status : 3
     * repayment_text : home_page
     * replacing_card_status : 3
     * replacing_card_type : PAGE
     */

    private ResultBean result;
    /**
     * result : {"replacing_card_text":"replacing_card_page","repayment_type":"PAGE","repayment_status":3,"repayment_text":"home_page","replacing_card_status":3,"replacing_card_type":"PAGE"}
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
        private String replacing_card_text;
        private String repayment_type;
        private String repayment_status;
        private String repayment_text;
        private String replacing_card_status;
        private String replacing_card_type;

        public String getReplacing_card_text() {
            return replacing_card_text;
        }

        public void setReplacing_card_text(String replacing_card_text) {
            this.replacing_card_text = replacing_card_text;
        }

        public String getRepayment_type() {
            return repayment_type;
        }

        public void setRepayment_type(String repayment_type) {
            this.repayment_type = repayment_type;
        }

        public String getRepayment_status() {
            return repayment_status;
        }

        public void setRepayment_status(String repayment_status) {
            this.repayment_status = repayment_status;
        }

        public String getRepayment_text() {
            return repayment_text;
        }

        public void setRepayment_text(String repayment_text) {
            this.repayment_text = repayment_text;
        }

        public String getReplacing_card_status() {
            return replacing_card_status;
        }

        public void setReplacing_card_status(String replacing_card_status) {
            this.replacing_card_status = replacing_card_status;
        }

        public String getReplacing_card_type() {
            return replacing_card_type;
        }

        public void setReplacing_card_type(String replacing_card_type) {
            this.replacing_card_type = replacing_card_type;
        }
    }
}
