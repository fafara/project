package com.ryx.ryxcredit.xjd.bean.centralBankCredit;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by RYX on 2017/6/12.
 */

public class CentralBankCreditRequest extends CbaseRequest {
    private String bankcredit_name;
    private String bankcredit_pass_word;

    public String getBankcredit_name() {
        return bankcredit_name;
    }

    public void setBankcredit_name(String bankcredit_name) {
        this.bankcredit_name = bankcredit_name;
    }

    public String getBankcredit_pass_word() {
        return bankcredit_pass_word;
    }

    public void setBankcredit_pass_word(String bankcredit_pass_word) {
        this.bankcredit_pass_word = bankcredit_pass_word;
    }

    public String getBankcredit_verification() {
        return bankcredit_verification;
    }

    public void setBankcredit_verification(String bankcredit_verification) {
        this.bankcredit_verification = bankcredit_verification;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String bankcredit_verification;
    private String user_id;

}
