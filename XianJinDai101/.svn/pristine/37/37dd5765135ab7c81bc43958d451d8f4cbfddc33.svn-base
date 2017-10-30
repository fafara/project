package com.ryx.ryxcredit.xjd.bean.repayment;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class CXJDRepayPlanResponse extends CbaseResponse {


    /**
     * result : {"total_amount":1000,"repayment":[{"cost_amount":497.02,"loan_status":"2","term":1,"ar_svc_fee_d":14.6,"ar_svc_fee_c":5.4,"ar_int_d":8.76,"interest_amount":12,"ar_int_c":3.24,"repayment_datetime":529.02,"expired_date":"20180123","other_cost_amount":20},{"cost_amount":502.98,"loan_status":"2","term":2,"ar_svc_fee_d":14.6,"ar_svc_fee_c":5.4,"ar_int_d":8.76,"interest_amount":6.04,"ar_int_c":3.24,"repayment_datetime":509.02,"expired_date":"20180223","other_cost_amount":0}]}
     * code : 2000
     */

    private ResultBean result;
    private int code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResultBean {
        /**
         * total_amount : 1000
         * repayment : [{"cost_amount":497.02,"loan_status":"2","term":1,"ar_svc_fee_d":14.6,"ar_svc_fee_c":5.4,"ar_int_d":8.76,"interest_amount":12,"ar_int_c":3.24,"repayment_datetime":529.02,"expired_date":"20180123","other_cost_amount":20},{"cost_amount":502.98,"loan_status":"2","term":2,"ar_svc_fee_d":14.6,"ar_svc_fee_c":5.4,"ar_int_d":8.76,"interest_amount":6.04,"ar_int_c":3.24,"repayment_datetime":509.02,"expired_date":"20180223","other_cost_amount":0}]
         */

        private double total_amount;
        private List<RepaymentBean> repayment;

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

        public List<RepaymentBean> getRepayment() {
            return repayment;
        }

        public void setRepayment(List<RepaymentBean> repayment) {
            this.repayment = repayment;
        }

        public static class RepaymentBean {
            /**
             * cost_amount : 497.02
             * loan_status : 2
             * term : 1
             * ar_svc_fee_d : 14.6
             * ar_svc_fee_c : 5.4
             * ar_int_d : 8.76
             * interest_amount : 12
             * ar_int_c : 3.24
             * repayment_datetime : 529.02
             * expired_date : 20180123
             * other_cost_amount : 20
             */

            private double cost_amount;
            private String loan_status;
            private int term;
            private double ar_svc_fee_d;
            private double ar_svc_fee_c;
            private double ar_int_d;
            private double interest_amount;
            private double ar_int_c;
            private double repayment_datetime;
            private String expired_date;
            private double other_cost_amount;

            public double getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(double total_amount) {
                this.total_amount = total_amount;
            }

            private double total_amount;

            public double getCost_amount() {
                return cost_amount;
            }

            public void setCost_amount(double cost_amount) {
                this.cost_amount = cost_amount;
            }

            public String getLoan_status() {
                return loan_status;
            }

            public void setLoan_status(String loan_status) {
                this.loan_status = loan_status;
            }

            public int getTerm() {
                return term;
            }

            public void setTerm(int term) {
                this.term = term;
            }

            public double getAr_svc_fee_d() {
                return ar_svc_fee_d;
            }

            public void setAr_svc_fee_d(double ar_svc_fee_d) {
                this.ar_svc_fee_d = ar_svc_fee_d;
            }

            public double getAr_svc_fee_c() {
                return ar_svc_fee_c;
            }

            public void setAr_svc_fee_c(double ar_svc_fee_c) {
                this.ar_svc_fee_c = ar_svc_fee_c;
            }

            public double getAr_int_d() {
                return ar_int_d;
            }

            public void setAr_int_d(double ar_int_d) {
                this.ar_int_d = ar_int_d;
            }

            public double getInterest_amount() {
                return interest_amount;
            }

            public void setInterest_amount(double interest_amount) {
                this.interest_amount = interest_amount;
            }

            public double getAr_int_c() {
                return ar_int_c;
            }

            public void setAr_int_c(double ar_int_c) {
                this.ar_int_c = ar_int_c;
            }

            public double getRepayment_datetime() {
                return repayment_datetime;
            }

            public void setRepayment_datetime(double repayment_datetime) {
                this.repayment_datetime = repayment_datetime;
            }

            public String getExpired_date() {
                return expired_date;
            }

            public void setExpired_date(String expired_date) {
                this.expired_date = expired_date;
            }

            public double getOther_cost_amount() {
                return other_cost_amount;
            }

            public void setOther_cost_amount(double other_cost_amount) {
                this.other_cost_amount = other_cost_amount;
            }
        }
    }
}
