package com.ryx.ryxcredit.xjd.bean.priceinfo;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by RYX on 2017/9/7.
 */

public class PriceinfoResponse extends CbaseResponse {

    /**
     * code : 2000
     * result : {"is_into":true}
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
         * is_into : true
         */

        private boolean is_into;

        public boolean getIs_into() {
            return is_into;
        }

        public void setIs_into(boolean is_into) {
            this.is_into = is_into;
        }
    }
}
