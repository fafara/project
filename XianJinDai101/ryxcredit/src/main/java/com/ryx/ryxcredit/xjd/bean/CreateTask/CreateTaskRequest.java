package com.ryx.ryxcredit.xjd.bean.CreateTask;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by RYX on 2017/9/22.
 */

public class CreateTaskRequest extends CbaseRequest {
    private  String login_name;
    private  String login_password;
    private  String login_phone;
    private  String smsCode;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
    public String getLogin_phone() {
        return login_phone;
    }

    public void setLogin_phone(String login_phone) {
        this.login_phone = login_phone;
    }
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
