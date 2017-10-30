package com.ryx.ryxcredit.beans.pojo;

import java.util.List;

/**
 * Created by muxin on 2016-09-13.
 */
public class BorrowRecordBeanTest {


    /**
     * code : 0000
     * msg : 交易成功
     * result : {"code":2000,"records":[{"borrow_amount":1000,"borrow_date":"201609040808","borrow_status":0,"payment_date":"201609180909"},{"borrow_amount":2000,"borrow_date":"201609040808","borrow_status":1,"payment_date":"201609180909"},{"borrow_amount":3000,"borrow_date":"201609040808","borrow_status":1,"payment_date":"201609180909"},{"borrow_amount":4000,"borrow_date":"201609040808","borrow_status":1,"payment_date":"201609180909"}]}
     */

    private String code;
    private String msg;
    /**
     * code : 2000
     * records : [{"borrow_amount":1000,"borrow_date":"201609040808","borrow_status":0,"payment_date":"201609180909"},{"borrow_amount":2000,"borrow_date":"201609040808","borrow_status":1,"payment_date":"201609180909"},{"borrow_amount":3000,"borrow_date":"201609040808","borrow_status":1,"payment_date":"201609180909"},{"borrow_amount":4000,"borrow_date":"201609040808","borrow_status":1,"payment_date":"201609180909"}]
     */

    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int code;
        /**
         * borrow_amount : 1000
         * borrow_date : 201609040808
         * borrow_status : 0
         * payment_date : 201609180909
         */

        private List<RecordsBean> records;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean {
            private int borrow_amount;
            private String borrow_date;
            private int borrow_status;
            private String payment_date;

            public int getBorrow_amount() {
                return borrow_amount;
            }

            public void setBorrow_amount(int borrow_amount) {
                this.borrow_amount = borrow_amount;
            }

            public String getBorrow_date() {
                return borrow_date;
            }

            public void setBorrow_date(String borrow_date) {
                this.borrow_date = borrow_date;
            }

            public int getBorrow_status() {
                return borrow_status;
            }

            public void setBorrow_status(int borrow_status) {
                this.borrow_status = borrow_status;
            }

            public String getPayment_date() {
                return payment_date;
            }

            public void setPayment_date(String payment_date) {
                this.payment_date = payment_date;
            }
        }
    }
}
