package com.ryx.ryxcredit.beans.bussiness.findcustomer;

/**
 * Created by Administrator on 2017/1/6.
 */

public class CRequestReserveUrlResponse {

    private ResultBean result;
    private int code;
    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public static class ResultBean {
        public String getAppointment_url() {
            return appointment_url;
        }

        public void setAppointment_url(String appointment_url) {
            this.appointment_url = appointment_url;
        }

        private String appointment_url;
    }
}
