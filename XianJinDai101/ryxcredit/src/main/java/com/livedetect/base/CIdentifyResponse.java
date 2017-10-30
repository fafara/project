package com.livedetect.base;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

/**
 * Created by Administrator on 2017/4/12.
 */

public class CIdentifyResponse extends CbaseResponse {


    /**
     * result : {"fileName":"d5a4feba45b3b16e00d7aa0dbc5cb9d7.jpg","score":84}
     * code : 2000
     */

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
        /**
         * fileName : d5a4feba45b3b16e00d7aa0dbc5cb9d7.jpg
         * score : 84
         */

        private String fileName;
        private String score;

        public String getFace_id() {
            return face_id;
        }

        public void setFace_id(String face_id) {
            this.face_id = face_id;
        }

        private String face_id;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }
}
