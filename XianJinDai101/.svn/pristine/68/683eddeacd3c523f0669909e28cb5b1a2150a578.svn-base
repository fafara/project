package com.ryx.ryxcredit.beans.bussiness.product;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.util.List;

/**
 * Created by laomao on 16/9/7.
 */
public class CproductResponse extends CbaseResponse{
    /**
     * ceiling_purchasable_amount : 13000
     * sub_products : [{"sub_product_id":"800707","product_id":"8007","term":1,"term_spans":7,"interest_rate":0,"span_unit":"D"},{"sub_product_id":"800714","product_id":"8007","cost_rate":0,"term":1,"term_spans":14,"interest_rate":0,"span_unit":"D"}]
     * floor_purchasable_amount : 1000
     * price : 100
     * product_id : 8007
     * contract_url : https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/loanContractTem.html
     * agreement_url : https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/serContractTem.html
     * product_descr : 巧还
     */

    private ResultBean result;
    /**
     * result : {"ceiling_purchasable_amount":13000,"sub_products":[{"sub_product_id":"800707","product_id":"8007","term":1,"term_spans":7,"interest_rate":0,"span_unit":"D"},{"sub_product_id":"800714","product_id":"8007","cost_rate":0,"term":1,"term_spans":14,"interest_rate":0,"span_unit":"D"}],"floor_purchasable_amount":1000,"price":100,"product_id":"8007","contract_url":"https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/loanContractTem.html","agreement_url":"https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/serContractTem.html","product_descr":"巧还"}
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
        private int ceiling_purchasable_amount;
        private double floor_purchasable_amount;

        public double getContract_version() {
            return contract_version;
        }

        public void setContract_version(double contract_version) {
            this.contract_version = contract_version;
        }

        private double contract_version;

        public String getMoney_manage_url() {
            return money_manage_url;
        }

        public void setMoney_manage_url(String money_manage_url) {
            this.money_manage_url = money_manage_url;
        }

        private String money_manage_url;
        private double price;
        private String product_id;

        public String getPayment_bank_url() {
            return payment_bank_url;
        }

        public void setPayment_bank_url(String payment_bank_url) {
            this.payment_bank_url = payment_bank_url;
        }

        private String payment_bank_url;
        /**
         * 贷款合同
         */
        private String contract_url;
        /**
         * 委托代扣协议
         */
        private String agreement_url;
        private String product_descr;
        /**
         * 瑞卡贷服务协议
         */
        private String service_agreement_url;
        /**
         * 代扣银行卡列表
         */
        private String repayment_bank_url;



        public String getRepayment_bank_url() {
            return repayment_bank_url;
        }

        public void setRepayment_bank_url(String repayment_bank_url) {
            this.repayment_bank_url = repayment_bank_url;
        }

        public String getService_agreement_url() {
            return service_agreement_url;
        }

        public void setService_agreement_url(String service_agreement_url) {
            this.service_agreement_url = service_agreement_url;
        }

        /**
         * sub_product_id : 800707
         * product_id : 8007
         * term : 1
         * term_spans : 7
         * interest_rate : 0
         * span_unit : D
         */

        private List<SubProductsBean> sub_products;

        public int getCeiling_purchasable_amount() {
            return ceiling_purchasable_amount;
        }

        public void setCeiling_purchasable_amount(int ceiling_purchasable_amount) {
            this.ceiling_purchasable_amount = ceiling_purchasable_amount;
        }

        public double getFloor_purchasable_amount() {
            return floor_purchasable_amount;
        }

        public void setFloor_purchasable_amount(double floor_purchasable_amount) {
            this.floor_purchasable_amount = floor_purchasable_amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getContract_url() {
            return contract_url;
        }

        public void setContract_url(String contract_url) {
            this.contract_url = contract_url;
        }

        public String getAgreement_url() {
            return agreement_url;
        }

        public void setAgreement_url(String agreement_url) {
            this.agreement_url = agreement_url;
        }

        public String getProduct_descr() {
            return product_descr;
        }

        public void setProduct_descr(String product_descr) {
            this.product_descr = product_descr;
        }

        public List<SubProductsBean> getSub_products() {
            return sub_products;
        }

        public void setSub_products(List<SubProductsBean> sub_products) {
            this.sub_products = sub_products;
        }

        public static class SubProductsBean {
            private String sub_product_id;
            private String product_id;
            private int term;
            private int term_spans;
            private double interest_rate;
            private String span_unit;

            public String getSub_product_id() {
                return sub_product_id;
            }

            public void setSub_product_id(String sub_product_id) {
                this.sub_product_id = sub_product_id;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public int getTerm() {
                return term;
            }

            public void setTerm(int term) {
                this.term = term;
            }

            public int getTerm_spans() {
                return term_spans;
            }

            public void setTerm_spans(int term_spans) {
                this.term_spans = term_spans;
            }

            public double getInterest_rate() {
                return interest_rate;
            }

            public void setInterest_rate(double interest_rate) {
                this.interest_rate = interest_rate;
            }

            public String getSpan_unit() {
                return span_unit;
            }

            public void setSpan_unit(String span_unit) {
                this.span_unit = span_unit;
            }
        }
    }
}
