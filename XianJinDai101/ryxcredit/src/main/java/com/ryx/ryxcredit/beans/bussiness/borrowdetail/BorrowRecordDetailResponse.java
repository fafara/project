package com.ryx.ryxcredit.beans.bussiness.borrowdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DIY on 2016/9/14.
 * 借款详情
 */
public class BorrowRecordDetailResponse extends CbaseResponse {

    /**
     * code : 2000
     * result : {"agreement_url":"","business_date":"20160922","contract_url":"","cost_amount":75,"expired_date":"20160919","loan_amount":5000,"loan_date":"20160913","loan_datetime":"20160913","loan_status":"B","overdue_days":2,"overdue_interest_amount":24.38,"overdue_interest_rate":1.8,"owed_amount":4899.38,"payment_bank_code":"03010000","payment_card_num":"6222521048636561","product_id":"8007","repayment_bank_code":"105","repayment_card_num":"6217002250002696238","repayments":[{"loan_status":"S","product_id":"8007","repayment_amount":200,"repayment_datetime":"20161013165320"}],"span_unit":"D","term_spans":7,"total_amount":5075}
     */

    private int code;
    /**
     * agreement_url :
     * business_date : 20160922
     * contract_url :
     * cost_amount : 75
     * expired_date : 20160919
     * loan_amount : 5000
     * loan_date : 20160913
     * loan_datetime : 20160913
     * loan_status : B
     * overdue_days : 2
     * overdue_interest_amount : 24.38
     * overdue_interest_rate : 1.8
     * owed_amount : 4899.38
     * payment_bank_code : 03010000
     * payment_card_num : 6222521048636561
     * product_id : 8007
     * repayment_bank_code : 105
     * repayment_card_num : 6217002250002696238
     * repayments : [{"loan_status":"S","product_id":"8007","repayment_amount":200,"repayment_datetime":"20161013165320"}]
     * span_unit : D
     * term_spans : 7
     * total_amount : 5075
     */

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

    public static class ResultBean implements Parcelable {
        private String agreement_url;
        private String business_date;
        private String contract_url;
        private double cost_amount;
        private String expired_date;
        private double loan_amount;
        private String loan_date;
        private String loan_datetime;
        private String loan_status;
        private int overdue_days;
        private double overdue_interest_amount;
        private double overdue_interest_rate;
        private double owed_amount;//当前未还合同金额，包括手续费
        private String payment_card_num;
        private String product_id;
        private String repayment_card_num;
        private String span_unit;
        private int term_spans;
        private double total_amount;
        private double remain_total_amount;//当前未还合同金额，不包括手续费
        private double sub_cost_rate;//拆分的利率
        private double other_cost_rate;//拆分的服务费率
        private double other_cost_amount;//拆分的服务费
        private double term_repay_amount;//
        public double getTerm_repay_amount() {
            return term_repay_amount;
        }

        public void setTerm_repay_amount(double term_repay_amount) {
            this.term_repay_amount = term_repay_amount;
        }


        public double getInterest_rate() {
            return interest_rate;
        }

        public void setInterest_rate(double interest_rate) {
            this.interest_rate = interest_rate;
        }

        private double interest_rate;
        public String getRepaid_cash_card() {
            return repaid_cash_card;
        }

        public void setRepaid_cash_card(String repaid_cash_card) {
            this.repaid_cash_card = repaid_cash_card;
        }

        private String repaid_cash_card;

        public String getSub_overdue_interest_rate() {
            return sub_overdue_interest_rate;
        }

        public void setSub_overdue_interest_rate(String sub_overdue_interest_rate) {
            this.sub_overdue_interest_rate = sub_overdue_interest_rate;
        }

        private String sub_overdue_interest_rate;

        public String getInterest_amount() {
            return interest_amount;
        }

        public void setInterest_amount(String interest_amount) {
            this.interest_amount = interest_amount;
        }

        private String interest_amount;

        public String getOther_overdue_interest_rate() {
            return other_overdue_interest_rate;
        }

        public void setOther_overdue_interest_rate(String other_overdue_interest_rate) {
            this.other_overdue_interest_rate = other_overdue_interest_rate;
        }

        private String other_overdue_interest_rate;

        public String getMoney_manage_url() {
            return money_manage_url;
        }

        public void setMoney_manage_url(String money_manage_url) {
            this.money_manage_url = money_manage_url;
        }

        private String money_manage_url;//信用咨询及管理服务协议

        public double getSub_cost_rate() {
            return sub_cost_rate;
        }

        public void setSub_cost_rate(double sub_cost_rate) {
            this.sub_cost_rate = sub_cost_rate;
        }

        public double getOther_cost_rate() {
            return other_cost_rate;
        }

        public void setOther_cost_rate(double other_cost_rate) {
            this.other_cost_rate = other_cost_rate;
        }

        public double getOther_cost_amount() {
            return other_cost_amount;
        }

        public void setOther_cost_amount(double other_cost_amount) {
            this.other_cost_amount = other_cost_amount;
        }

        public double getSub_cost_amount() {
            return sub_cost_amount;
        }

        public void setSub_cost_amount(double sub_cost_amount) {
            this.sub_cost_amount = sub_cost_amount;
        }

        private double sub_cost_amount;//拆分的利息

        public String getPayment_title_code() {
            return payment_title_code;
        }

        public void setPayment_title_code(String payment_title_code) {
            this.payment_title_code = payment_title_code;
        }

        public String getRepayment_title_code() {
            return repayment_title_code;
        }

        public void setRepayment_title_code(String repayment_title_code) {
            this.repayment_title_code = repayment_title_code;
        }

        private String payment_title_code;
        private String repayment_title_code;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private int status;

        public double getRemain_total_amount() {
            return remain_total_amount;
        }

        public void setRemain_total_amount(double remain_total_amount) {
            this.remain_total_amount = remain_total_amount;
        }

        public double getRepaid_amount() {
            return repaid_amount;
        }

        public void setRepaid_amount(double repaid_amount) {
            this.repaid_amount = repaid_amount;
        }

        private double repaid_amount;
        /**
         * loan_status : S
         * product_id : 8007
         * repayment_amount : 200
         * repayment_datetime : 20161013165320
         */

        private List<RepaymentsBean> repayments;

        public double getCost_amount() {
            return cost_amount;
        }

        public void setCost_amount(double cost_amount) {
            this.cost_amount = cost_amount;
        }

        public double getLoan_amount() {
            return loan_amount;
        }

        public void setLoan_amount(double loan_amount) {
            this.loan_amount = loan_amount;
        }

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public String getAgreement_url() {
            return agreement_url;
        }

        public void setAgreement_url(String agreement_url) {
            this.agreement_url = agreement_url;
        }

        public String getBusiness_date() {
            return business_date;
        }

        public void setBusiness_date(String business_date) {
            this.business_date = business_date;
        }

        public String getContract_url() {
            return contract_url;
        }

        public void setContract_url(String contract_url) {
            this.contract_url = contract_url;
        }

        public String getExpired_date() {
            return expired_date;
        }

        public void setExpired_date(String expired_date) {
            this.expired_date = expired_date;
        }

        public String getLoan_date() {
            return loan_date;
        }

        public void setLoan_date(String loan_date) {
            this.loan_date = loan_date;
        }

        public String getLoan_datetime() {
            return loan_datetime;
        }

        public void setLoan_datetime(String loan_datetime) {
            this.loan_datetime = loan_datetime;
        }

        public String getLoan_status() {
            return loan_status;
        }

        public void setLoan_status(String loan_status) {
            this.loan_status = loan_status;
        }

        public int getOverdue_days() {
            return overdue_days;
        }

        public void setOverdue_days(int overdue_days) {
            this.overdue_days = overdue_days;
        }

        public double getOverdue_interest_amount() {
            return overdue_interest_amount;
        }

        public void setOverdue_interest_amount(double overdue_interest_amount) {
            this.overdue_interest_amount = overdue_interest_amount;
        }

        public double getOverdue_interest_rate() {
            return overdue_interest_rate;
        }

        public void setOverdue_interest_rate(double overdue_interest_rate) {
            this.overdue_interest_rate = overdue_interest_rate;
        }

        public double getOwed_amount() {
            return owed_amount;
        }

        public void setOwed_amount(double owed_amount) {
            this.owed_amount = owed_amount;
        }

        public String getPayment_card_num() {
            return payment_card_num;
        }

        public void setPayment_card_num(String payment_card_num) {
            this.payment_card_num = payment_card_num;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getRepayment_card_num() {
            return repayment_card_num;
        }

        public void setRepayment_card_num(String repayment_card_num) {
            this.repayment_card_num = repayment_card_num;
        }

        public String getSpan_unit() {
            return span_unit;
        }

        public void setSpan_unit(String span_unit) {
            this.span_unit = span_unit;
        }

        public int getTerm_spans() {
            return term_spans;
        }

        public void setTerm_spans(int term_spans) {
            this.term_spans = term_spans;
        }

        public List<RepaymentsBean> getRepayments() {
            return repayments;
        }

        public void setRepayments(List<RepaymentsBean> repayments) {
            this.repayments = repayments;
        }

        public static class RepaymentsBean implements Parcelable {
            private String loan_status;
            private String product_id;
            private double repayment_amount;
            private String repayment_datetime;

            public double getRepayment_amount() {
                return repayment_amount;
            }

            public void setRepayment_amount(double repayment_amount) {
                this.repayment_amount = repayment_amount;
            }

            public String getLoan_status() {
                return loan_status;
            }

            public void setLoan_status(String loan_status) {
                this.loan_status = loan_status;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }


            public String getRepayment_datetime() {
                return repayment_datetime;
            }

            public void setRepayment_datetime(String repayment_datetime) {
                this.repayment_datetime = repayment_datetime;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.loan_status);
                dest.writeString(this.product_id);
                dest.writeDouble(this.repayment_amount);
                dest.writeString(this.repayment_datetime);
            }

            public RepaymentsBean() {
            }

            protected RepaymentsBean(Parcel in) {
                this.loan_status = in.readString();
                this.product_id = in.readString();
                this.repayment_amount = in.readDouble();
                this.repayment_datetime = in.readString();
            }

            public static final Creator<RepaymentsBean> CREATOR = new Creator<RepaymentsBean>() {
                @Override
                public RepaymentsBean createFromParcel(Parcel source) {
                    return new RepaymentsBean(source);
                }

                @Override
                public RepaymentsBean[] newArray(int size) {
                    return new RepaymentsBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.agreement_url);
            dest.writeString(this.business_date);
            dest.writeString(this.contract_url);
            dest.writeDouble(this.cost_amount);
            dest.writeString(this.expired_date);
            dest.writeDouble(this.loan_amount);
            dest.writeString(this.loan_date);
            dest.writeString(this.loan_datetime);
            dest.writeString(this.loan_status);
            dest.writeInt(this.overdue_days);
            dest.writeDouble(this.overdue_interest_amount);
            dest.writeDouble(this.overdue_interest_rate);
            dest.writeDouble(this.owed_amount);
            dest.writeString(this.payment_card_num);
            dest.writeString(this.product_id);
            dest.writeString(this.repayment_card_num);
            dest.writeString(this.span_unit);
            dest.writeInt(this.term_spans);
            dest.writeDouble(this.total_amount);
            dest.writeDouble(this.remain_total_amount);
            dest.writeList(this.repayments);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.agreement_url = in.readString();
            this.business_date = in.readString();
            this.contract_url = in.readString();
            this.cost_amount = in.readDouble();
            this.expired_date = in.readString();
            this.loan_amount = in.readDouble();
            this.loan_date = in.readString();
            this.loan_datetime = in.readString();
            this.loan_status = in.readString();
            this.overdue_days = in.readInt();
            this.overdue_interest_amount = in.readDouble();
            this.overdue_interest_rate = in.readDouble();
            this.owed_amount = in.readDouble();
            this.payment_card_num = in.readString();
            this.product_id = in.readString();
            this.repayment_card_num = in.readString();
            this.span_unit = in.readString();
            this.term_spans = in.readInt();
            this.total_amount = in.readDouble();
            this.remain_total_amount = in.readDouble();
            this.repayments = new ArrayList<RepaymentsBean>();
            in.readList(this.repayments, RepaymentsBean.class.getClassLoader());
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }
}
