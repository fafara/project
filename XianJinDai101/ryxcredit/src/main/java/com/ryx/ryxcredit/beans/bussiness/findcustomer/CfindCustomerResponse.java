package com.ryx.ryxcredit.beans.bussiness.findcustomer;

/**
 * Created by laomao on 16/8/13.
 */
public class CfindCustomerResponse {

    /**
     * total_amount : 20000
     * close_time : 220000
     * is_active : true
     * available_amount : 15940
     * payment_card_count : 1
     * is_service : true
     * service_authorize_url : https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/credit_club_service_agreement.html
     * active_status : 3
     * open_time : 050000
     * is_opened : true
     * customer_status : 4
     * is_agreed : true
     * agreement_url : https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/license_agreement.html
     * user_id : 100052
     * is_repaying : false
     * agreement_id : 1
     */

    private ResultBean result;
    /**
     * result : {"total_amount":20000,"close_time":"220000","is_active":true,"available_amount":15940,"payment_card_count":1,"is_service":true,"service_authorize_url":"https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/credit_club_service_agreement.html","active_status":3,"open_time":"050000","is_opened":true,"customer_status":4,"is_agreed":true,"agreement_url":"https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/license_agreement.html","user_id":"100052","is_repaying":false,"agreement_id":1}
     * code : 2000
     */

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
        private String total_amount;
        private String close_time;
        private boolean is_active;
        private String available_amount;
        private int payment_card_count;
        private boolean is_service;
        private String service_authorize_url;
        private int active_status;
        private String open_time;
        private boolean is_opened;
        private int customer_status;
        private boolean is_agreed;
        private String agreement_url;
        private String user_id;
        private boolean is_repaying;
        private String agreement_id;
        private boolean is_beoverdue;
        private String activation_status;

        public String getAppointment_url() {
            return appointment_url;
        }

        public void setAppointment_url(String appointment_url) {
            this.appointment_url = appointment_url;
        }

        public String getActivation_status() {
            return activation_status;
        }

        public void setActivation_status(String activation_status) {
            this.activation_status = activation_status;
        }

        private String appointment_url;

        public boolean is_beoverdue() {
            return is_beoverdue;
        }

        public void setIs_beoverdue(boolean is_beoverdue) {
            this.is_beoverdue = is_beoverdue;
        }



        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getClose_time() {
            return close_time;
        }

        public void setClose_time(String close_time) {
            this.close_time = close_time;
        }

        public boolean isIs_active() {
            return is_active;
        }

        public void setIs_active(boolean is_active) {
            this.is_active = is_active;
        }

        public String getAvailable_amount() {
            return available_amount;
        }

        public void setAvailable_amount(String available_amount) {
            this.available_amount = available_amount;
        }

        public int getPayment_card_count() {
            return payment_card_count;
        }

        public void setPayment_card_count(int payment_card_count) {
            this.payment_card_count = payment_card_count;
        }

        public boolean isIs_service() {
            return is_service;
        }

        public void setIs_service(boolean is_service) {
            this.is_service = is_service;
        }

        public String getService_authorize_url() {
            return service_authorize_url;
        }

        public void setService_authorize_url(String service_authorize_url) {
            this.service_authorize_url = service_authorize_url;
        }

        public int getActive_status() {
            return active_status;
        }

        public void setActive_status(int active_status) {
            this.active_status = active_status;
        }

        public String getOpen_time() {
            return open_time;
        }

        public void setOpen_time(String open_time) {
            this.open_time = open_time;
        }

        public boolean Is_opened() {
            return is_opened;
        }

        public void setIs_opened(boolean is_opened) {
            this.is_opened = is_opened;
        }

        public int getCustomer_status() {
            return customer_status;
        }

        public void setCustomer_status(int customer_status) {
            this.customer_status = customer_status;
        }

        public boolean isIs_agreed() {
            return is_agreed;
        }

        public void setIs_agreed(boolean is_agreed) {
            this.is_agreed = is_agreed;
        }

        public String getAgreement_url() {
            return agreement_url;
        }

        public void setAgreement_url(String agreement_url) {
            this.agreement_url = agreement_url;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public boolean Is_repaying() {
            return is_repaying;
        }

        public void setIs_repaying(boolean is_repaying) {
            this.is_repaying = is_repaying;
        }

        public String getAgreement_id() {
            return agreement_id;
        }

        public void setAgreement_id(String agreement_id) {
            this.agreement_id = agreement_id;
        }
    }
}



