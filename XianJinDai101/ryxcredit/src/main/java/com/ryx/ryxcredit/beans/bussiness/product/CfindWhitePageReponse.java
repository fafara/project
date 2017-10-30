package com.ryx.ryxcredit.beans.bussiness.product;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by Administrator on 2016/12/6.
 */

public class CfindWhitePageReponse extends CbaseResponse {


    /**
     * is_white : false
     */

    private ResultBean result;
    /**
     * result : {"is_white":false}
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
        private boolean is_white;

        public boolean isIs_white() {
            return is_white;
        }

        public void setIs_white(boolean is_white) {
            this.is_white = is_white;
        }
    }
}
