package com.ryx.ryxcredit.xjd.bean.borrow;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class CXJDProductCaculateResponse extends CbaseResponse implements Serializable{

    /**
     * result : {"total_amount":1038.04,"fee_rate_d":0.0146,"fee_rate_c":0.0054,"repayments":[{"total_amount":529.02,"loan_term_int_c":3.24,"cost_amount":0,"loan_term_int":12,"term":1,"loan_term_svc_fee":20,"interest_amount":12,"term_amount":497.02,"loan_term_int_d":8.76,"loan_term_svc_fee_d":14.6,"expired_date":"20180129","loan_term_svc_feec":5.4},{"total_amount":509.02,"loan_term_int_c":1.63,"cost_amount":0,"loan_term_int":6.04,"term":2,"loan_term_svc_fee":0,"interest_amount":6.04,"term_amount":502.98,"loan_term_int_d":4.41,"expired_date":"20180228"}],"fee_rate":0.02,"loan_date":"20171229","lnt_erest_rate_d":0.10512,"loan_fee_sum_d":14.6,"interest_amount":18.04,"term_end_date":"20180228","cost_amount":0,"loan_fee_sum":20,"interest_rate_c":0.03888,"term":2,"term_start_date":"20171229","total_term_svcfee":20,"loan_int_sum":18.04,"loan_int_sum_d":13.17,"loan_int_sum_c":4.87,"loan_datetime":"20171229000000","interest_rate":0.144,"loan_fee_sumc":5.4}
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

    public static class ResultBean implements Serializable {
        /**
         * total_amount : 1038.04
         * fee_rate_d : 0.0146
         * fee_rate_c : 0.0054
         * repayments : [{"total_amount":529.02,"loan_term_int_c":3.24,"cost_amount":0,"loan_term_int":12,"term":1,"loan_term_svc_fee":20,"interest_amount":12,"term_amount":497.02,"loan_term_int_d":8.76,"loan_term_svc_fee_d":14.6,"expired_date":"20180129","loan_term_svc_feec":5.4},{"total_amount":509.02,"loan_term_int_c":1.63,"cost_amount":0,"loan_term_int":6.04,"term":2,"loan_term_svc_fee":0,"interest_amount":6.04,"term_amount":502.98,"loan_term_int_d":4.41,"expired_date":"20180228"}]
         * fee_rate : 0.02
         * loan_date : 20171229
         * lnt_erest_rate_d : 0.10512
         * loan_fee_sum_d : 14.6
         * interest_amount : 18.04
         * term_end_date : 20180228
         * cost_amount : 0
         * loan_fee_sum : 20
         * interest_rate_c : 0.03888
         * term : 2
         * term_start_date : 20171229
         * total_term_svcfee : 20
         * loan_int_sum : 18.04
         * loan_int_sum_d : 13.17
         * loan_int_sum_c : 4.87
         * loan_datetime : 20171229000000
         * interest_rate : 0.144
         * loan_fee_sumc : 5.4
         */

        private double total_amount;
        private double fee_rate_d;
        private double fee_rate_c;
        private double fee_rate;
        private String loan_date;
        private double lnt_erest_rate_d;
        private double loan_fee_sum_d;
        private double interest_amount;
        private String term_end_date;
        private double cost_amount;
        private double loan_fee_sum;
        private double interest_rate_c;
        private int term;
        private String term_start_date;
        private double total_term_svcfee;
        private double loan_int_sum;
        private double loan_int_sum_d;
        private double loan_int_sum_c;
        private String loan_datetime;
        private double interest_rate;
        private double loan_fee_sumc;
        private List<RepaymentsBean> repayments;

        public double getSub_cost_rate() {
            return sub_cost_rate;
        }

        public void setSub_cost_rate(double sub_cost_rate) {
            this.sub_cost_rate = sub_cost_rate;
        }

        public double getOverdue_interest_rate() {
            return overdue_interest_rate;
        }

        public void setOverdue_interest_rate(double overdue_interest_rate) {
            this.overdue_interest_rate = overdue_interest_rate;
        }

        private double sub_cost_rate;
        private double overdue_interest_rate;

        public double getSub_overdue_interest_rate() {
            return sub_overdue_interest_rate;
        }

        public void setSub_overdue_interest_rate(double sub_overdue_interest_rate) {
            this.sub_overdue_interest_rate = sub_overdue_interest_rate;
        }

        public double getOther_overdue_interest_rate() {
            return other_overdue_interest_rate;
        }

        public void setOther_overdue_interest_rate(double other_overdue_interest_rate) {
            this.other_overdue_interest_rate = other_overdue_interest_rate;
        }

        private double sub_overdue_interest_rate;
        private double other_overdue_interest_rate;
        public int getAgreement_id() {
            return agreement_id;
        }

        public void setAgreement_id(int agreement_id) {
            this.agreement_id = agreement_id;
        }

        private int agreement_id;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getFee_rate_d() {
            return fee_rate_d;
        }

        public void setFee_rate_d(double fee_rate_d) {
            this.fee_rate_d = fee_rate_d;
        }

        public double getFee_rate_c() {
            return fee_rate_c;
        }

        public void setFee_rate_c(double fee_rate_c) {
            this.fee_rate_c = fee_rate_c;
        }

        public double getFee_rate() {
            return fee_rate;
        }

        public void setFee_rate(double fee_rate) {
            this.fee_rate = fee_rate;
        }

        public String getLoan_date() {
            return loan_date;
        }

        public void setLoan_date(String loan_date) {
            this.loan_date = loan_date;
        }

        public double getLnt_erest_rate_d() {
            return lnt_erest_rate_d;
        }

        public void setLnt_erest_rate_d(double lnt_erest_rate_d) {
            this.lnt_erest_rate_d = lnt_erest_rate_d;
        }

        public double getLoan_fee_sum_d() {
            return loan_fee_sum_d;
        }

        public void setLoan_fee_sum_d(double loan_fee_sum_d) {
            this.loan_fee_sum_d = loan_fee_sum_d;
        }

        public double getInterest_amount() {
            return interest_amount;
        }

        public void setInterest_amount(double interest_amount) {
            this.interest_amount = interest_amount;
        }

        public String getTerm_end_date() {
            return term_end_date;
        }

        public void setTerm_end_date(String term_end_date) {
            this.term_end_date = term_end_date;
        }

        public double getCost_amount() {
            return cost_amount;
        }

        public void setCost_amount(double cost_amount) {
            this.cost_amount = cost_amount;
        }

        public double getLoan_fee_sum() {
            return loan_fee_sum;
        }

        public void setLoan_fee_sum(double loan_fee_sum) {
            this.loan_fee_sum = loan_fee_sum;
        }

        public double getInterest_rate_c() {
            return interest_rate_c;
        }

        public void setInterest_rate_c(double interest_rate_c) {
            this.interest_rate_c = interest_rate_c;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public String getTerm_start_date() {
            return term_start_date;
        }

        public void setTerm_start_date(String term_start_date) {
            this.term_start_date = term_start_date;
        }

        public double getTotal_term_svcfee() {
            return total_term_svcfee;
        }

        public void setTotal_term_svcfee(double total_term_svcfee) {
            this.total_term_svcfee = total_term_svcfee;
        }

        public double getLoan_int_sum() {
            return loan_int_sum;
        }

        public void setLoan_int_sum(double loan_int_sum) {
            this.loan_int_sum = loan_int_sum;
        }

        public double getLoan_int_sum_d() {
            return loan_int_sum_d;
        }

        public void setLoan_int_sum_d(double loan_int_sum_d) {
            this.loan_int_sum_d = loan_int_sum_d;
        }

        public double getLoan_int_sum_c() {
            return loan_int_sum_c;
        }

        public void setLoan_int_sum_c(double loan_int_sum_c) {
            this.loan_int_sum_c = loan_int_sum_c;
        }

        public String getLoan_datetime() {
            return loan_datetime;
        }

        public void setLoan_datetime(String loan_datetime) {
            this.loan_datetime = loan_datetime;
        }

        public double getInterest_rate() {
            return interest_rate;
        }

        public void setInterest_rate(double interest_rate) {
            this.interest_rate = interest_rate;
        }

        public double getLoan_fee_sumc() {
            return loan_fee_sumc;
        }

        public void setLoan_fee_sumc(double loan_fee_sumc) {
            this.loan_fee_sumc = loan_fee_sumc;
        }

        public List<RepaymentsBean> getRepayments() {
            return repayments;
        }

        public void setRepayments(List<RepaymentsBean> repayments) {
            this.repayments = repayments;
        }

        public static class RepaymentsBean implements Serializable {
            /**
             * total_amount : 529.02
             * loan_term_int_c : 3.24
             * cost_amount : 0
             * loan_term_int : 12
             * term : 1
             * loan_term_svc_fee : 20
             * interest_amount : 12
             * term_amount : 497.02
             * loan_term_int_d : 8.76
             * loan_term_svc_fee_d : 14.6
             * expired_date : 20180129
             * loan_term_svc_feec : 5.4
             */

            private double total_amount;
            private double loan_term_int_c;
            private double cost_amount;
            private double loan_term_int;
            private int term;
            private double loan_term_svc_fee;
            private double interest_amount;
            private double term_amount;
            private double loan_term_int_d;
            private double loan_term_svc_fee_d;
            private String expired_date;
            private double loan_term_svc_feec;

            public double getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(double total_amount) {
                this.total_amount = total_amount;
            }

            public double getLoan_term_int_c() {
                return loan_term_int_c;
            }

            public void setLoan_term_int_c(double loan_term_int_c) {
                this.loan_term_int_c = loan_term_int_c;
            }

            public double getCost_amount() {
                return cost_amount;
            }

            public void setCost_amount(double cost_amount) {
                this.cost_amount = cost_amount;
            }

            public double getLoan_term_int() {
                return loan_term_int;
            }

            public void setLoan_term_int(double loan_term_int) {
                this.loan_term_int = loan_term_int;
            }

            public int getTerm() {
                return term;
            }

            public void setTerm(int term) {
                this.term = term;
            }

            public double getLoan_term_svc_fee() {
                return loan_term_svc_fee;
            }

            public void setLoan_term_svc_fee(double loan_term_svc_fee) {
                this.loan_term_svc_fee = loan_term_svc_fee;
            }

            public double getInterest_amount() {
                return interest_amount;
            }

            public void setInterest_amount(double interest_amount) {
                this.interest_amount = interest_amount;
            }

            public double getTerm_amount() {
                return term_amount;
            }

            public void setTerm_amount(double term_amount) {
                this.term_amount = term_amount;
            }

            public double getLoan_term_int_d() {
                return loan_term_int_d;
            }

            public void setLoan_term_int_d(double loan_term_int_d) {
                this.loan_term_int_d = loan_term_int_d;
            }

            public double getLoan_term_svc_fee_d() {
                return loan_term_svc_fee_d;
            }

            public void setLoan_term_svc_fee_d(double loan_term_svc_fee_d) {
                this.loan_term_svc_fee_d = loan_term_svc_fee_d;
            }

            public String getExpired_date() {
                return expired_date;
            }

            public void setExpired_date(String expired_date) {
                this.expired_date = expired_date;
            }

            public double getLoan_term_svc_feec() {
                return loan_term_svc_feec;
            }

            public void setLoan_term_svc_feec(double loan_term_svc_feec) {
                this.loan_term_svc_feec = loan_term_svc_feec;
            }
        }
    }
}
