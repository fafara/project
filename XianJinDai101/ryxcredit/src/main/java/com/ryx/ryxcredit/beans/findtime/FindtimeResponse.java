package com.ryx.ryxcredit.beans.findtime;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.io.Serializable;

/**
 * Created by RYX on 2017/6/30.
 */

public class FindtimeResponse extends CbaseResponse implements Serializable {

    /**
     * code : 2000
     * result : {"version_time":1498787261172}
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
         * version_time : 1498787261172
         */

        private long version_time;

        public long getVersion_time() {
            return version_time;
        }

        public void setVersion_time(long version_time) {
            this.version_time = version_time;
        }
    }
}
