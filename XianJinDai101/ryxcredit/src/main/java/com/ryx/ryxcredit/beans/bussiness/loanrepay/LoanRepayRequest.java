package com.ryx.ryxcredit.beans.bussiness.loanrepay;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by laomao on 16/9/19.
 */
public class LoanRepayRequest extends CbaseRequest{


    /**
     * user_id : 100027
     * contract_id : 600116100300000269
     * repayment_amount : 100
     */

    private String contract_id;
    private double repayment_amount;

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    private String card_num;

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public double getRepayment_amount() {
        return repayment_amount;
    }

    public void setRepayment_amount(double repayment_amount) {
        this.repayment_amount = repayment_amount;
    }
}
