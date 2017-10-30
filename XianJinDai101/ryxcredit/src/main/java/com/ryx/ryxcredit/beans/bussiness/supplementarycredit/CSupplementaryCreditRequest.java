package com.ryx.ryxcredit.beans.bussiness.supplementarycredit;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by lijing on 2016/9/12.
 * 补充信用卡资料请求
 */
public class CSupplementaryCreditRequest extends CbaseRequest {

    //用户ID
    private String customer_id;
    //卡号
    private String card_num;
    //总额度
    private String total_amount;
    //当前账单余额
    private String current_balance;
    //账单日
    private String statement_date;
    //还款日
    private String payment_due_date;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(String current_balance) {
        this.current_balance = current_balance;
    }

    public String getStatement_date() {
        return statement_date;
    }

    public void setStatement_date(String statement_date) {
        this.statement_date = statement_date;
    }

    public String getPayment_due_date() {
        return payment_due_date;
    }

    public void setPayment_due_date(String payment_due_date) {
        this.payment_due_date = payment_due_date;
    }

    @Override
    public String toString() {
        return "CSupplementaryCreditRequest{" +
                "customer_id='" + customer_id + '\'' +
                ", card_num='" + card_num + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", current_balance='" + current_balance + '\'' +
                ", statement_date='" + statement_date + '\'' +
                ", payment_due_date='" + payment_due_date + '\'' +
                '}';
    }
}
