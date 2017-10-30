package com.ryx.ryxcredit.beans.bussiness.borrowdetail;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by Administrator on 2016/12/6.
 */

public class ChangeDkCardRequest extends CbaseRequest {
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getRepayment_card_num() {
        return repayment_card_num;
    }

    public void setRepayment_card_num(String repayment_card_num) {
        this.repayment_card_num = repayment_card_num;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    //合同编号
    private String contract_id;
    private String repayment_card_num;
    private String card_num;
}
