package com.ryx.ryxcredit.beans.bussiness.loancalculate;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.util.List;

/**
 * Created by laomao on 16/9/11.
 */
public class CLoanCalulateResponse extends CbaseResponse {

    /**
     * total_amount : 1015
     * cost_amount : 15
     * product_id : 8007
     * repayments : [{"current_interest_amount":0,"product_id":"8007","current_cost_amount":0,"current_term":1,"current_balance":1000,"expired_date":"20160824"}]
     * loan_date : 20160818
     * term : 1
     * interest_amount : 0
     * loan_datetime : 20160818
     */

    private ResultBean result;
    /**
     * result : {"total_amount":1015,"cost_amount":15,"product_id":"8007","repayments":[{"current_interest_amount":0,"product_id":"8007","current_cost_amount":0,"current_term":1,"current_balance":1000,"expired_date":"20160824"}],"loan_date":"20160818","term":1,"interest_amount":0,"loan_datetime":"20160818"}
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
        private double total_amount;
        private double cost_amount;
        private String product_id;
        private String loan_date;
        private int term;
        private double interest_amount;
        private String loan_datetime;
        private double sub_cost_rate;//拆分的利率
        private double other_cost_rate;//拆分的服务费率
        private double other_cost_amount;//拆分的服务费
        private double sub_cost_amount;//拆分的利息

        public String getAgreement_id() {
            return agreement_id;
        }

        public void setAgreement_id(String agreement_id) {
            this.agreement_id = agreement_id;
        }

        private String agreement_id;//合同版本号

        public double getOther_overdue_interest_rate() {
            return other_overdue_interest_rate;
        }

        public void setOther_overdue_interest_rate(double other_overdue_interest_rate) {
            this.other_overdue_interest_rate = other_overdue_interest_rate;
        }

        private double other_overdue_interest_rate;//逾期服务费率

        public double getOverdue_interest_rate() {
            return overdue_interest_rate;
        }

        public void setOverdue_interest_rate(double overdue_interest_rate) {
            this.overdue_interest_rate = overdue_interest_rate;
        }

        private double overdue_interest_rate;//逾期利息费率

        public double getSub_cost_rate() {
            return sub_cost_rate;
        }

        public void setSub_cost_rate(double sub_cost_rate) {
            this.sub_cost_rate = sub_cost_rate;
        }

        public double getOther_cost_rate() {
            return other_cost_rate;
        }

        public void setOther_cost_rate(double other_cost_rate) {
            this.other_cost_rate = other_cost_rate;
        }

        public double getOther_cost_amount() {
            return other_cost_amount;
        }

        public void setOther_cost_amount(double other_cost_amount) {
            this.other_cost_amount = other_cost_amount;
        }

        public double getSub_cost_amount() {
            return sub_cost_amount;
        }

        public void setSub_cost_amount(double sub_cost_amount) {
            this.sub_cost_amount = sub_cost_amount;
        }

        /**
         * current_interest_amount : 0
         * product_id : 8007
         * current_cost_amount : 0
         * current_term : 1
         * current_balance : 1000
         * expired_date : 20160824
         */

        private List<RepaymentsBean> repayments;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getCost_amount() {
            return cost_amount;
        }

        public void setCost_amount(double cost_amount) {
            this.cost_amount = cost_amount;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getLoan_date() {
            return loan_date;
        }

        public void setLoan_date(String loan_date) {
            this.loan_date = loan_date;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public double getInterest_amount() {
            return interest_amount;
        }

        public void setInterest_amount(double interest_amount) {
            this.interest_amount = interest_amount;
        }

        public String getLoan_datetime() {
            return loan_datetime;
        }

        public void setLoan_datetime(String loan_datetime) {
            this.loan_datetime = loan_datetime;
        }

        public List<RepaymentsBean> getRepayments() {
            return repayments;
        }

        public void setRepayments(List<RepaymentsBean> repayments) {
            this.repayments = repayments;
        }

        public static class RepaymentsBean {
            public double getInterest_amount() {
                return interest_amount;
            }

            public void setInterest_amount(double interest_amount) {
                this.interest_amount = interest_amount;
            }

            public double getCost_amount() {
                return cost_amount;
            }

            public void setCost_amount(double cost_amount) {
                this.cost_amount = cost_amount;
            }

            public int getTerm() {
                return term;
            }

            public void setTerm(int term) {
                this.term = term;
            }

            private double interest_amount;
            private double cost_amount;
            private int term;
            private String expired_date;

            public String getExpired_date() {
                return expired_date;
            }

            public void setExpired_date(String expired_date) {
                this.expired_date = expired_date;
            }
        }
    }
}
