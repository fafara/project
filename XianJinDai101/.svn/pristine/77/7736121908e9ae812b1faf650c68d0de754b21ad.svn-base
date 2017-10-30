package com.ryx.ryxcredit.beans.bussiness.cardrepayment;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by Administrator on 2017/2/3.
 */

public class CfindRepayRouteResponse extends CbaseResponse {

    /**
     * repayment_type : PAGE
     * repayment_status : 3
     * repayment_text : home_page
     */

    private ResultBean result;
    /**
     * result : {"repayment_type":"PAGE","repayment_status":3,"repayment_text":"home_page"}
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
        private String repayment_type;
        private String repayment_status;
        private String repayment_text;
        private String temporary_status;//更换临时卡按钮状态
        private String temporary_type;//更换临时卡按钮类型
        private String temporary_text;//更换临时卡提示语或页面

        public String getTemporary_status() {
            return temporary_status;
        }

        public void setTemporary_status(String temporary_status) {
            this.temporary_status = temporary_status;
        }

        public String getTemporary_type() {
            return temporary_type;
        }

        public void setTemporary_type(String temporary_type) {
            this.temporary_type = temporary_type;
        }

        public String getTemporary_text() {
            return temporary_text;
        }

        public void setTemporary_text(String temporary_text) {
            this.temporary_text = temporary_text;
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
    }
}
