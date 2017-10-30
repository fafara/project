package com.ryx.ryxcredit.xjd.bean.ProvidentFund;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by RYX on 2017/9/29.
 */

public class ProvidentFundResponse extends CbaseResponse{

    /**
     * code : 2000
     * result : {"code":"0000","msg":"请求成功","result":"https://open.shujumohe.com/box/she_bao?box_token=3BDA012XXXXXXXXXX&id=10811","success":true}
     */

    private int code;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * code : 0000
         * msg : 请求成功
         * result : https://open.shujumohe.com/box/she_bao?box_token=3BDA012XXXXXXXXXX&id=10811
         * success : true
         */

        private String code;
        private String msg;
        private String result;
        private boolean success;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
