package com.ryx.ryxcredit.beans.bussiness.cardpayment;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;
import com.ryx.ryxcredit.utils.CJSONUtils;

import java.util.List;

/**
 * Created by laomao on 16/9/11.
 * 代付卡
 */
public class CcardPaymentResponse extends CbaseResponse{

    /**
     * result : [{"total_amount":60000,"bank_name":"招商银行","holder_name":"解娉娉","statement_date":"20161103","current_balance":20000,"card_num":"6225758327687983","bank_code":"03080000","card_name":"招商银行信用卡","bank_branch_code":"308584000013","card_type":"2","payment_due_date":"20161101","holder_identity":"372428199109105725","reference_id":"A000662586","create_datetime":"20161019092728","customer_id":"100052"}]
     * code : 2000
     */

    private int code;
    /**
     * total_amount : 60000
     * bank_name : 招商银行
     * holder_name : 解娉娉
     * statement_date : 20161103
     * current_balance : 20000
     * card_num : 6225758327687983
     * bank_code : 03080000
     * card_name : 招商银行信用卡
     * bank_branch_code : 308584000013
     * card_type : 2
     * payment_due_date : 20161101
     * holder_identity : 372428199109105725
     * reference_id : A000662586
     * create_datetime : 20161019092728
     * customer_id : 100052
     */

    private List<ResultBean> result;

    @Override
    public String toString() {
        return CJSONUtils.getInstance().toJSONString(this);
    }

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

    public static class ResultBean {
        private double total_amount;
        private String bank_name;
        private String holder_name;
        private String statement_date;
        private double current_balance;
        private String card_num;
        private String card_name;
        private String bank_branch_code;
        private String card_type;
        private String payment_due_date;
        private String holder_identity;
        private String reference_id;
        private String create_datetime;
        private String customer_id;

        public String getBank_title_code() {
            return bank_title_code;
        }

        public void setBank_title_code(String bank_title_code) {
            this.bank_title_code = bank_title_code;
        }

        private String bank_title_code;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private int status;

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getHolder_name() {
            return holder_name;
        }

        public void setHolder_name(String holder_name) {
            this.holder_name = holder_name;
        }

        public String getStatement_date() {
            return statement_date;
        }

        public void setStatement_date(String statement_date) {
            this.statement_date = statement_date;
        }

        public double getCurrent_balance() {
            return current_balance;
        }

        public void setCurrent_balance(double current_balance) {
            this.current_balance = current_balance;
        }

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public String getCard_name() {
            return card_name;
        }

        public void setCard_name(String card_name) {
            this.card_name = card_name;
        }

        public String getBank_branch_code() {
            return bank_branch_code;
        }

        public void setBank_branch_code(String bank_branch_code) {
            this.bank_branch_code = bank_branch_code;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public String getPayment_due_date() {
            return payment_due_date;
        }

        public void setPayment_due_date(String payment_due_date) {
            this.payment_due_date = payment_due_date;
        }

        public String getHolder_identity() {
            return holder_identity;
        }

        public void setHolder_identity(String holder_identity) {
            this.holder_identity = holder_identity;
        }

        public String getReference_id() {
            return reference_id;
        }

        public void setReference_id(String reference_id) {
            this.reference_id = reference_id;
        }

        public String getCreate_datetime() {
            return create_datetime;
        }

        public void setCreate_datetime(String create_datetime) {
            this.create_datetime = create_datetime;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }
    }
}
