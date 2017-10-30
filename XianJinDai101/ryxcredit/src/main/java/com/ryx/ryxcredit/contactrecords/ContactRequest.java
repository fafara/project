package com.ryx.ryxcredit.contactrecords;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/8.
 */

public class ContactRequest implements Serializable {

    private String phoneNo;
    private String serviceCode;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
