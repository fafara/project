package com.ryx.ryxcredit.beans.bussiness.borrowrecords;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DIY on 2016/9/14.
 */
public class BorrowRecordsResponse extends CbaseResponse {
    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        //产品编号
        private String product_id;
        //产品描述
        private String product_descr;
        //合同到期日
        private String expired_date;
        //产品状态
        private String loan_status;
        //合同金额
        private String total_amount;
        //已还金额
        private String repaid_amount;
        //合同号
        private String contract_id;
        //未结清金额
        private String owed_amount;
        //申请时间(申请中才返回)
        private String application_datetime;

        public String getApplication_datetime() {
            return application_datetime;
        }

        public void setApplication_datetime(String application_datetime) {
            this.application_datetime = application_datetime;
        }

        public String getRepaid_amount() {
            return repaid_amount;
        }

        public void setRepaid_amount(String repaid_amount) {
            this.repaid_amount = repaid_amount;
        }

        public String getOwed_amount() {
            return owed_amount;
        }

        public void setOwed_amount(String owed_amount) {
            this.owed_amount = owed_amount;
        }

        public String getContract_id() {
            return contract_id;
        }

        public void setContract_id(String contract_id) {
            this.contract_id = contract_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_descr() {
            return product_descr;
        }

        public void setProduct_descr(String product_descr) {
            this.product_descr = product_descr;
        }

        public String getExpired_date() {
            return expired_date;
        }

        public void setExpired_date(String expired_date) {
            this.expired_date = expired_date;
        }

        public String getLoan_status() {
            return loan_status;
        }

        public void setLoan_status(String loan_status) {
            this.loan_status = loan_status;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "product_id='" + product_id + '\'' +
                    ", product_descr='" + product_descr + '\'' +
                    ", expired_date='" + expired_date + '\'' +
                    ", loan_status='" + loan_status + '\'' +
                    ", total_amount='" + total_amount + '\'' +
                    ", repaid_amount='" + repaid_amount + '\'' +
                    ", contract_id='" + contract_id + '\'' +
                    ", owed_amount='" + owed_amount + '\'' +
                    ", application_datetime='" + application_datetime + '\'' +
                    '}';
        }
    }
}
