package com.ryx.ryxcredit.xjd.bean.SearchTask;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.io.Serializable;

/**
 * Created by RYX on 2017/9/22.
 */

public class SearchTaskResponse extends CbaseResponse implements Serializable {

    /**
     * code : 2000
     * result : {"reason":"暂不支持的登录方式，请重试","status":"failed"}
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
         * reason : 暂不支持的登录方式，请重试
         * status : failed
         */

        private String reason;
        private String status;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
