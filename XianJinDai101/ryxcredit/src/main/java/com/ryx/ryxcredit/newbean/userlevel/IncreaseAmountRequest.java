package com.ryx.ryxcredit.newbean.userlevel;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by RYX on 2017/5/23.
 */

public class IncreaseAmountRequest extends CbaseRequest {
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;
}
