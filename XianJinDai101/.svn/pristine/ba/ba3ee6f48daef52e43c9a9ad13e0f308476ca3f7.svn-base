package com.ryx.ryxcredit.beans.bussiness.activateline;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by Administrator on 2017/1/24.
 */

public class CfindActiveRouteResponse extends CbaseResponse {


    /**
     * active_status : 3
     * payment_card_count : 1
     */

    private ResultBean result;
    /**
     * result : {"active_status":3,"payment_card_count":1}
     * code : 2000
     */

    private String code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ResultBean {
        public String getActive_type() {
            return active_type;
        }

        public void setActive_type(String active_type) {
            this.active_type = active_type;
        }

        public String getActive_text() {
            return active_text;
        }

        public void setActive_text(String active_text) {
            this.active_text = active_text;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        private String active_type;//激活按钮电视显示文字类型
        private String active_text;//点击激活按钮显示内容
        private String active_status;//激活按钮状态{0:不可见不可用 1:可见  2:可用 3:可用可见}
        private int payment_card_count;//需要绑定的信用卡数量
        private String active;//激活状态

        public String getMo_status() {
            return mo_status;
        }

        public void setMo_status(String mo_status) {
            this.mo_status = mo_status;
        }

        public String getFace_status() {
            return face_status;
        }

        public void setFace_status(String face_status) {
            this.face_status = face_status;
        }

        public String getIs_mo_auth() {
            return is_mo_auth;
        }

        public void setIs_mo_auth(String is_mo_auth) {
            this.is_mo_auth = is_mo_auth;
        }

        public String getMo_auth_url() {
            return mo_auth_url;
        }

        public void setMo_auth_url(String mo_auth_url) {
            this.mo_auth_url = mo_auth_url;
        }

        private String mo_status;//是否采集手机运营商信息
        private String face_status;//是否采集活信息
        private String is_mo_auth;//是否授权手机运营商信息采集协议
        private String mo_auth_url;//手机运营商采集授权协议

        public String getActive_status() {
            return active_status;
        }

        public void setActive_status(String active_status) {
            this.active_status = active_status;
        }

        public int getPayment_card_count() {
            return payment_card_count;
        }

        public void setPayment_card_count(int payment_card_count) {
            this.payment_card_count = payment_card_count;
        }
    }
}
