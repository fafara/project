package com.ryx.ryxcredit.beans.findkinds;

/**
 * Created by RYX on 2017/6/30.
 */

public class FindKindsResponse {

    /**
     * code : 2000
     * result : {"version_id":"4.4.1"}
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
         * version_id : 4.4.1
         * version_add:"http://www.ruiyinxin.com/ruishua/"
         */

        private String version_id;

        public String getVersion_add() {
            return version_add;
        }

        public void setVersion_add(String version_add) {
            this.version_add = version_add;
        }

        private String version_add;

        public String getVersion_id() {
            return version_id;
        }

        public void setVersion_id(String version_id) {
            this.version_id = version_id;
        }
    }
}
