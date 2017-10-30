package com.ryx.ryxcredit.beans.bussiness.loancalculate;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by laomao on 16/9/11.
 */
public class CLoanCalculateResquest extends CbaseRequest{

    /**
     * user_id : 100026
     * product_id : 6001
     * sub_product_id : 600101
     * loan_amount : 1000
     * application_term : 1
     */

    private String product_id;
    private String sub_product_id;

    public double getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(double loan_amount) {
        this.loan_amount = loan_amount;
    }

    private double loan_amount;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    private int term;

    public double getInterest_amount() {
        return interest_amount;
    }

    public void setInterest_amount(double interest_amount) {
        this.interest_amount = interest_amount;
    }

    public double getSub_cost_rate() {
        return sub_cost_rate;
    }

    public void setSub_cost_rate(double sub_cost_rate) {
        this.sub_cost_rate = sub_cost_rate;
    }

    public double getLoan_fee_sum() {
        return loan_fee_sum;
    }

    public void setLoan_fee_sum(double loan_fee_sum) {
        this.loan_fee_sum = loan_fee_sum;
    }

    public double getFee_rate() {
        return fee_rate;
    }

    public void setFee_rate(double fee_rate) {
        this.fee_rate = fee_rate;
    }

    public double getOther_overdue_interest_rate() {
        return other_overdue_interest_rate;
    }

    public void setOther_overdue_interest_rate(double other_overdue_interest_rate) {
        this.other_overdue_interest_rate = other_overdue_interest_rate;
    }

    public double getOverdue_interest_rate() {
        return overdue_interest_rate;
    }

    public void setOverdue_interest_rate(double overdue_interest_rate) {
        this.overdue_interest_rate = overdue_interest_rate;
    }

    private double interest_amount;//总利息
    private double sub_cost_rate;//利息利率
    private double loan_fee_sum;//总服务费
    private double fee_rate;//总服务费率
    private double other_overdue_interest_rate;//逾期服务费率
    private double overdue_interest_rate;//逾期利息费率
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSub_product_id() {
        return sub_product_id;
    }

    public void setSub_product_id(String sub_product_id) {
        this.sub_product_id = sub_product_id;
    }


}
