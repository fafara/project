package com.ryx.ryxcredit.xjd.bean.repayment;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.util.List;

/**
 * Created by RYX on 2017/6/22.
 */

public class XJDRepaymentRecord extends CbaseResponse {

    /**
     * result : {"loan_amount":1000,"service_agreement_url":"https://mposprepo.ruiyinxin.com:4430/xiaodai/ruishangdai/Creditadvice.html","loan_date":"20180712","schedule_curr_bal":329.2,"remain_total_amount":745.41,"term_repay_amount":62.01,"interest_amount":12.5,"term_spans":3,"overdue_interest":0,"sub_cost_rate":0.15,"sub_overdue_interest_rate":0.5,"other_cost_rate":0.02,"money_manage_url":"https://mposprepo.ruiyinxin.com:4430/xiaodai/credit_consult_and_manager.html","total_term_svcfee":20,"repayment_card_num":"6212260200101512396","other_overdue_interest_amount":0,"overdue_interest_rate":1.8,"paid_cash_card":"支持银行：网银银行,建设银行,招商银行,农业银行,中信实业银行,恒丰银行,广东发展银行,深圳发展银行,光大银行,兴业银行,交通银行,名生银行,华夏银行,上海浦发发展银行","term_fixed_repay_amount":341.7,"expired_date":"20181012","interest_rate":0.0125,"other_cost_amount":20,"total_amount":1000,"owed_amount":745.41,"other_overdue_interest_rate":4.5,"business_date":"20180714","sub_overdue_interest_amount":0,"loan_status":"D","repayments":[{"loan_status":"S","repayment_amount":33.61,"repayment_datetime":"20171209034852"},{"loan_status":"S","repayment_amount":10.01,"repayment_datetime":"20171209032840"},{"loan_status":"S","repayment_amount":12.03,"repayment_datetime":"20171209032616"},{"loan_status":"S","repayment_amount":10.01,"repayment_datetime":"20171209031819"},{"loan_status":"S","repayment_amount":121.01,"repayment_datetime":"20171209031008"},{"loan_status":"S","repayment_amount":13.01,"repayment_datetime":"20171209023853"},{"loan_status":"S","repayment_amount":100.01,"repayment_datetime":"20171209002843"}],"status":1,"payment_title_code":"102","repaid_amount":299.69,"repaid_cash_card":"支持银行：工商银行,农业银行,中国银行,建设银行,交通银行,兴业银行,中信银行,光大银行,浦发银行,平安银行,广发银行,华夏银行,邮储银行,民生银行,恒丰银行","contract_id":"300718061200002203","term_date":"20180812","repayment_bank_url":"https://mposprepo.ruiyinxin.com:4430/xiaodai/bank_list.html","overdue_days":0,"payment_card_num":"6212260200101512396","cost_amount":0,"repayment_bank_code":"01020000","repayment_title_code":"102","payment_bank_code":"01020000","contract_url":"https://mposprepo.ruiyinxin.com:4430/xiaodai/ruishangdai/Agreement.html","overdue_interest_amount":0,"customer_id":"100212","loan_datetime":"20180712","span_unit":"M"}
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
         * loan_amount : 1000
         * service_agreement_url : https://mposprepo.ruiyinxin.com:4430/xiaodai/ruishangdai/Creditadvice.html
         * loan_date : 20180712
         * schedule_curr_bal : 329.2
         * remain_total_amount : 745.41
         * term_repay_amount : 62.01
         * interest_amount : 12.5
         * term_spans : 3
         * overdue_interest : 0
         * sub_cost_rate : 0.15
         * sub_overdue_interest_rate : 0.5
         * other_cost_rate : 0.02
         * money_manage_url : https://mposprepo.ruiyinxin.com:4430/xiaodai/credit_consult_and_manager.html
         * total_term_svcfee : 20
         * repayment_card_num : 6212260200101512396
         * other_overdue_interest_amount : 0
         * overdue_interest_rate : 1.8
         * paid_cash_card : 支持银行：网银银行,建设银行,招商银行,农业银行,中信实业银行,恒丰银行,广东发展银行,深圳发展银行,光大银行,兴业银行,交通银行,名生银行,华夏银行,上海浦发发展银行
         * term_fixed_repay_amount : 341.7
         * expired_date : 20181012
         * interest_rate : 0.0125
         * other_cost_amount : 20
         * total_amount : 1000
         * owed_amount : 745.41
         * other_overdue_interest_rate : 4.5
         * business_date : 20180714
         * sub_overdue_interest_amount : 0
         * loan_status : D
         * repayments : [{"loan_status":"S","repayment_amount":33.61,"repayment_datetime":"20171209034852"},{"loan_status":"S","repayment_amount":10.01,"repayment_datetime":"20171209032840"},{"loan_status":"S","repayment_amount":12.03,"repayment_datetime":"20171209032616"},{"loan_status":"S","repayment_amount":10.01,"repayment_datetime":"20171209031819"},{"loan_status":"S","repayment_amount":121.01,"repayment_datetime":"20171209031008"},{"loan_status":"S","repayment_amount":13.01,"repayment_datetime":"20171209023853"},{"loan_status":"S","repayment_amount":100.01,"repayment_datetime":"20171209002843"}]
         * status : 1
         * payment_title_code : 102
         * repaid_amount : 299.69
         * repaid_cash_card : 支持银行：工商银行,农业银行,中国银行,建设银行,交通银行,兴业银行,中信银行,光大银行,浦发银行,平安银行,广发银行,华夏银行,邮储银行,民生银行,恒丰银行
         * contract_id : 300718061200002203
         * term_date : 20180812
         * repayment_bank_url : https://mposprepo.ruiyinxin.com:4430/xiaodai/bank_list.html
         * overdue_days : 0
         * payment_card_num : 6212260200101512396
         * cost_amount : 0
         * repayment_bank_code : 01020000
         * repayment_title_code : 102
         * payment_bank_code : 01020000
         * contract_url : https://mposprepo.ruiyinxin.com:4430/xiaodai/ruishangdai/Agreement.html
         * overdue_interest_amount : 0
         * customer_id : 100212
         * loan_datetime : 20180712
         * span_unit : M
         */

        private int loan_amount;
        private String service_agreement_url;
        private String loan_date;
        private double schedule_curr_bal;
        private double remain_total_amount;
        private double term_repay_amount;
        private double interest_amount;
        private int term_spans;
        private int overdue_interest;
        private double sub_cost_rate;
        private double sub_overdue_interest_rate;
        private double other_cost_rate;
        private String money_manage_url;
        private int total_term_svcfee;
        private String repayment_card_num;
        private int other_overdue_interest_amount;
        private double overdue_interest_rate;
        private String paid_cash_card;
        private double term_fixed_repay_amount;
        private String expired_date;
        private double interest_rate;
        private int other_cost_amount;
        private int total_amount;
        private double owed_amount;
        private double other_overdue_interest_rate;
        private String business_date;
        private int sub_overdue_interest_amount;
        private String loan_status;
        private int status;
        private String payment_title_code;
        private double repaid_amount;
        private String repaid_cash_card;
        private String contract_id;
        private String term_date;
        private String repayment_bank_url;
        private int overdue_days;
        private String payment_card_num;
        private int cost_amount;
        private String repayment_bank_code;
        private String repayment_title_code;
        private String payment_bank_code;
        private String contract_url;
        private int overdue_interest_amount;
        private String customer_id;
        private String loan_datetime;
        private String span_unit;
        private List<RepaymentsBean> repayments;

        public int getLoan_amount() {
            return loan_amount;
        }

        public void setLoan_amount(int loan_amount) {
            this.loan_amount = loan_amount;
        }

        public String getService_agreement_url() {
            return service_agreement_url;
        }

        public void setService_agreement_url(String service_agreement_url) {
            this.service_agreement_url = service_agreement_url;
        }

        public String getLoan_date() {
            return loan_date;
        }

        public void setLoan_date(String loan_date) {
            this.loan_date = loan_date;
        }

        public double getSchedule_curr_bal() {
            return schedule_curr_bal;
        }

        public void setSchedule_curr_bal(double schedule_curr_bal) {
            this.schedule_curr_bal = schedule_curr_bal;
        }

        public double getRemain_total_amount() {
            return remain_total_amount;
        }

        public void setRemain_total_amount(double remain_total_amount) {
            this.remain_total_amount = remain_total_amount;
        }

        public double getTerm_repay_amount() {
            return term_repay_amount;
        }

        public void setTerm_repay_amount(double term_repay_amount) {
            this.term_repay_amount = term_repay_amount;
        }

        public double getInterest_amount() {
            return interest_amount;
        }

        public void setInterest_amount(double interest_amount) {
            this.interest_amount = interest_amount;
        }

        public int getTerm_spans() {
            return term_spans;
        }

        public void setTerm_spans(int term_spans) {
            this.term_spans = term_spans;
        }

        public int getOverdue_interest() {
            return overdue_interest;
        }

        public void setOverdue_interest(int overdue_interest) {
            this.overdue_interest = overdue_interest;
        }

        public double getSub_cost_rate() {
            return sub_cost_rate;
        }

        public void setSub_cost_rate(double sub_cost_rate) {
            this.sub_cost_rate = sub_cost_rate;
        }

        public double getSub_overdue_interest_rate() {
            return sub_overdue_interest_rate;
        }

        public void setSub_overdue_interest_rate(double sub_overdue_interest_rate) {
            this.sub_overdue_interest_rate = sub_overdue_interest_rate;
        }

        public double getOther_cost_rate() {
            return other_cost_rate;
        }

        public void setOther_cost_rate(double other_cost_rate) {
            this.other_cost_rate = other_cost_rate;
        }

        public String getMoney_manage_url() {
            return money_manage_url;
        }

        public void setMoney_manage_url(String money_manage_url) {
            this.money_manage_url = money_manage_url;
        }

        public int getTotal_term_svcfee() {
            return total_term_svcfee;
        }

        public void setTotal_term_svcfee(int total_term_svcfee) {
            this.total_term_svcfee = total_term_svcfee;
        }

        public String getRepayment_card_num() {
            return repayment_card_num;
        }

        public void setRepayment_card_num(String repayment_card_num) {
            this.repayment_card_num = repayment_card_num;
        }

        public int getOther_overdue_interest_amount() {
            return other_overdue_interest_amount;
        }

        public void setOther_overdue_interest_amount(int other_overdue_interest_amount) {
            this.other_overdue_interest_amount = other_overdue_interest_amount;
        }

        public double getOverdue_interest_rate() {
            return overdue_interest_rate;
        }

        public void setOverdue_interest_rate(double overdue_interest_rate) {
            this.overdue_interest_rate = overdue_interest_rate;
        }

        public String getPaid_cash_card() {
            return paid_cash_card;
        }

        public void setPaid_cash_card(String paid_cash_card) {
            this.paid_cash_card = paid_cash_card;
        }

        public double getTerm_fixed_repay_amount() {
            return term_fixed_repay_amount;
        }

        public void setTerm_fixed_repay_amount(double term_fixed_repay_amount) {
            this.term_fixed_repay_amount = term_fixed_repay_amount;
        }

        public String getExpired_date() {
            return expired_date;
        }

        public void setExpired_date(String expired_date) {
            this.expired_date = expired_date;
        }

        public double getInterest_rate() {
            return interest_rate;
        }

        public void setInterest_rate(double interest_rate) {
            this.interest_rate = interest_rate;
        }

        public int getOther_cost_amount() {
            return other_cost_amount;
        }

        public void setOther_cost_amount(int other_cost_amount) {
            this.other_cost_amount = other_cost_amount;
        }

        public int getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(int total_amount) {
            this.total_amount = total_amount;
        }

        public double getOwed_amount() {
            return owed_amount;
        }

        public void setOwed_amount(double owed_amount) {
            this.owed_amount = owed_amount;
        }

        public double getOther_overdue_interest_rate() {
            return other_overdue_interest_rate;
        }

        public void setOther_overdue_interest_rate(double other_overdue_interest_rate) {
            this.other_overdue_interest_rate = other_overdue_interest_rate;
        }

        public String getBusiness_date() {
            return business_date;
        }

        public void setBusiness_date(String business_date) {
            this.business_date = business_date;
        }

        public int getSub_overdue_interest_amount() {
            return sub_overdue_interest_amount;
        }

        public void setSub_overdue_interest_amount(int sub_overdue_interest_amount) {
            this.sub_overdue_interest_amount = sub_overdue_interest_amount;
        }

        public String getLoan_status() {
            return loan_status;
        }

        public void setLoan_status(String loan_status) {
            this.loan_status = loan_status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPayment_title_code() {
            return payment_title_code;
        }

        public void setPayment_title_code(String payment_title_code) {
            this.payment_title_code = payment_title_code;
        }

        public double getRepaid_amount() {
            return repaid_amount;
        }

        public void setRepaid_amount(double repaid_amount) {
            this.repaid_amount = repaid_amount;
        }

        public String getRepaid_cash_card() {
            return repaid_cash_card;
        }

        public void setRepaid_cash_card(String repaid_cash_card) {
            this.repaid_cash_card = repaid_cash_card;
        }

        public String getContract_id() {
            return contract_id;
        }

        public void setContract_id(String contract_id) {
            this.contract_id = contract_id;
        }

        public String getTerm_date() {
            return term_date;
        }

        public void setTerm_date(String term_date) {
            this.term_date = term_date;
        }

        public String getRepayment_bank_url() {
            return repayment_bank_url;
        }

        public void setRepayment_bank_url(String repayment_bank_url) {
            this.repayment_bank_url = repayment_bank_url;
        }

        public int getOverdue_days() {
            return overdue_days;
        }

        public void setOverdue_days(int overdue_days) {
            this.overdue_days = overdue_days;
        }

        public String getPayment_card_num() {
            return payment_card_num;
        }

        public void setPayment_card_num(String payment_card_num) {
            this.payment_card_num = payment_card_num;
        }

        public int getCost_amount() {
            return cost_amount;
        }

        public void setCost_amount(int cost_amount) {
            this.cost_amount = cost_amount;
        }

        public String getRepayment_bank_code() {
            return repayment_bank_code;
        }

        public void setRepayment_bank_code(String repayment_bank_code) {
            this.repayment_bank_code = repayment_bank_code;
        }

        public String getRepayment_title_code() {
            return repayment_title_code;
        }

        public void setRepayment_title_code(String repayment_title_code) {
            this.repayment_title_code = repayment_title_code;
        }

        public String getPayment_bank_code() {
            return payment_bank_code;
        }

        public void setPayment_bank_code(String payment_bank_code) {
            this.payment_bank_code = payment_bank_code;
        }

        public String getContract_url() {
            return contract_url;
        }

        public void setContract_url(String contract_url) {
            this.contract_url = contract_url;
        }

        public int getOverdue_interest_amount() {
            return overdue_interest_amount;
        }

        public void setOverdue_interest_amount(int overdue_interest_amount) {
            this.overdue_interest_amount = overdue_interest_amount;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getLoan_datetime() {
            return loan_datetime;
        }

        public void setLoan_datetime(String loan_datetime) {
            this.loan_datetime = loan_datetime;
        }

        public String getSpan_unit() {
            return span_unit;
        }

        public void setSpan_unit(String span_unit) {
            this.span_unit = span_unit;
        }

        public List<RepaymentsBean> getRepayments() {
            return repayments;
        }

        public void setRepayments(List<RepaymentsBean> repayments) {
            this.repayments = repayments;
        }

        public static class RepaymentsBean {
            /**
             * loan_status : S
             * repayment_amount : 33.61
             * repayment_datetime : 20171209034852
             */

            private String loan_status;
            private double repayment_amount;
            private String repayment_datetime;

            public String getLoan_status() {
                return loan_status;
            }

            public void setLoan_status(String loan_status) {
                this.loan_status = loan_status;
            }

            public double getRepayment_amount() {
                return repayment_amount;
            }

            public void setRepayment_amount(double repayment_amount) {
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
}
