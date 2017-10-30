package com.ryx.ryxcredit.xjd.bean.borrowrecord;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MultiBorrowRecordsResponse extends CbaseResponse implements Serializable{

    /**
     * result : [{"total_amount":1000,"owed_amount":1000,"product_id":"8001","loan_status":"D","loan_date":"20181211","repaid_amount":0,"product_descr":"现金贷","contract_id":"800118121100001290","application_datetime":"20181211","expired_date":"20190211","loan_datetime":"20181211"},{"total_amount":4321,"owed_amount":4321,"product_id":"8001","loan_status":"D","loan_date":"20181211","repaid_amount":0,"product_descr":"现金贷","contract_id":"800118121100001308","application_datetime":"20181211","expired_date":"20190211","loan_datetime":"20181211"}]
     * code : 2000
     */

    private int code;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * total_amount : 1000
         * owed_amount : 1000
         * product_id : 8001
         * loan_status : D
         * loan_date : 20181211
         * repaid_amount : 0
         * product_descr : 现金贷
         * contract_id : 800118121100001290
         * application_datetime : 20181211
         * expired_date : 20190211
         * loan_datetime : 20181211
         */

        private double total_amount;
        private double owed_amount;
        private String product_id;
        private String loan_status;
        private String loan_date;
        private double repaid_amount;
        private String product_descr;
        private String contract_id;
        private String application_datetime;
        private String expired_date;
        private String loan_datetime;

        public String getTerm_date() {
            return term_date;
        }

        public void setTerm_date(String term_date) {
            this.term_date = term_date;
        }

        private String term_date;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getOwed_amount() {
            return owed_amount;
        }

        public void setOwed_amount(double owed_amount) {
            this.owed_amount = owed_amount;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getLoan_status() {
            return loan_status;
        }

        public void setLoan_status(String loan_status) {
            this.loan_status = loan_status;
        }

        public String getLoan_date() {
            return loan_date;
        }

        public void setLoan_date(String loan_date) {
            this.loan_date = loan_date;
        }

        public double getRepaid_amount() {
            return repaid_amount;
        }

        public void setRepaid_amount(double repaid_amount) {
            this.repaid_amount = repaid_amount;
        }

        public String getProduct_descr() {
            return product_descr;
        }

        public void setProduct_descr(String product_descr) {
            this.product_descr = product_descr;
        }

        public String getContract_id() {
            return contract_id;
        }

        public void setContract_id(String contract_id) {
            this.contract_id = contract_id;
        }

        public String getApplication_datetime() {
            return application_datetime;
        }

        public void setApplication_datetime(String application_datetime) {
            this.application_datetime = application_datetime;
        }

        public String getExpired_date() {
            return expired_date;
        }

        public void setExpired_date(String expired_date) {
            this.expired_date = expired_date;
        }

        public String getLoan_datetime() {
            return loan_datetime;
        }

        public void setLoan_datetime(String loan_datetime) {
            this.loan_datetime = loan_datetime;
        }
    }
}
