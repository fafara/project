package com.ryx.ryxcredit.beans.bussiness.debitcard;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by Administrator on 2016/10/9.
 */

public class CdebitCardAuthRequest extends CbaseRequest {

    private String card_num;
    private String reserved_phone_num;

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getReserved_phone_num() {
        return reserved_phone_num;
    }

    public void setReserved_phone_num(String reserved_phone_num) {
        this.reserved_phone_num = reserved_phone_num;
    }

}
