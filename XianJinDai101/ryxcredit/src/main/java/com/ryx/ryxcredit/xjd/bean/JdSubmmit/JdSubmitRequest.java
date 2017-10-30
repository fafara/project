package com.ryx.ryxcredit.xjd.bean.JdSubmmit;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by RYX on 2017/9/27.
 */

public class JdSubmitRequest extends CbaseRequest {
    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

    private  String login_name;
    private  String login_password;
}
