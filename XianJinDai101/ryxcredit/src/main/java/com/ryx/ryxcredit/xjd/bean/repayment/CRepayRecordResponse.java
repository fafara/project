package com.ryx.ryxcredit.xjd.bean.repayment;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */

public class CRepayRecordResponse extends CbaseResponse {

    private String code;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private List<CRepayRecordResponse.ResultBean> result;
    public static class ResultBean {
        private String loan_status;
        private String repayment_amount;
        private String repayment_datetime;

        public String getLoan_status() {
            return loan_status;
        }

        public void setLoan_status(String loan_status) {
            this.loan_status = loan_status;
        }

        public String getRepayment_amount() {
            return repayment_amount;
        }

        public void setRepayment_amount(String repayment_amount) {
            this.repayment_amount = repayment_amount;
        }

        public String getRepayment_datetime() {
            return repayment_datetime;
        }

        public void setRepayment_datetime(String repayment_datetime) {
            this.repayment_datetime = repayment_datetime;
        }
    }
}
