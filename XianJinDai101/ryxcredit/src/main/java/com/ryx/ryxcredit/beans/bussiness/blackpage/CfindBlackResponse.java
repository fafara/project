package com.ryx.ryxcredit.beans.bussiness.blackpage;

/**
 * Created by Administrator on 2017/1/6.
 */

public class CfindBlackResponse {

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
        public boolean is_black() {
            return is_black;
        }

        public void setIs_black(boolean is_black) {
            this.is_black = is_black;
        }

        private boolean is_black;
    }
}
