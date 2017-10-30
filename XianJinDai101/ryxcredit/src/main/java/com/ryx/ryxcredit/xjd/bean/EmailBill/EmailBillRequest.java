package com.ryx.ryxcredit.xjd.bean.EmailBill;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by RYX on 2017/9/22.
 */

public class EmailBillRequest extends CbaseRequest {

    private String login_name;//登陆账号
    private String login_password;//登陆密码
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

}
