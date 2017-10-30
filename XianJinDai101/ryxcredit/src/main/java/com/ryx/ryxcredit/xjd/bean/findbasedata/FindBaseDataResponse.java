package com.ryx.ryxcredit.xjd.bean.findbasedata;

import com.ryx.ryxcredit.beans.bussiness.CbaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RYX on 2017/8/4.
 */

public class FindBaseDataResponse extends CbaseResponse implements Serializable {


    /**
     * result : {"bossInfo":{"company_address":"null婆婆给我","credit_code":"热熔","customer_name":"陶务华","identity_name":"陶务华","monthly_turnover":334646,"legal_partner":"L","customer_type":"00","tradeInfo":{"user_level":"2","first_datetime":"20170412","trades":[],"cards":[{"bank_name":"民生银行","trade_total_amount":1050000,"trade_count":3,"card_name":"民生贷记卡(银联卡)"}],"monthly_trades":[{"dailymean_trade_amount":35000,"trade_total_amount":1050000,"trade_max_amount":700000,"trade_average_amount":350000,"dailymean_trade_count":0.1,"trade_cards":1,"trade_month":"201704","trade_count":3}]},"contact_phone_num":"13552982091","phone_num":"13552982091","company_name":"哈咯","education_status":"B","contact_name":"陶务华","company_phone_num":"0531-6657184","reference_id":"A000662820","industry_type":"S","user_id":"100212","create_datetime":"20170803173433","marital_status":"S","contact_relation":"D","identity_num":"371202199006271215"},"card_floor_count":1,"cards":[{"bank_name":"中国民生银行","holder_name":"陶务华","bank_branch_code":"305100000013","card_type":"2","holder_identity":"371202199006271215","reference_id":"A000662820","card_num":"4218700018145081","bank_code":"305100000013","bank_title_code":"305","customer_id":"100212","card_name":"民生贷记卡(银联卡)"}]}
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
         * bossInfo : {"company_address":"null婆婆给我","credit_code":"热熔","customer_name":"陶务华","identity_name":"陶务华","monthly_turnover":334646,"legal_partner":"L","customer_type":"00","tradeInfo":{"user_level":"2","first_datetime":"20170412","trades":[],"cards":[{"bank_name":"民生银行","trade_total_amount":1050000,"trade_count":3,"card_name":"民生贷记卡(银联卡)"}],"monthly_trades":[{"dailymean_trade_amount":35000,"trade_total_amount":1050000,"trade_max_amount":700000,"trade_average_amount":350000,"dailymean_trade_count":0.1,"trade_cards":1,"trade_month":"201704","trade_count":3}]},"contact_phone_num":"13552982091","phone_num":"13552982091","company_name":"哈咯","education_status":"B","contact_name":"陶务华","company_phone_num":"0531-6657184","reference_id":"A000662820","industry_type":"S","user_id":"100212","create_datetime":"20170803173433","marital_status":"S","contact_relation":"D","identity_num":"371202199006271215"}
         * card_floor_count : 1
         * cards : [{"bank_name":"中国民生银行","holder_name":"陶务华","bank_branch_code":"305100000013","card_type":"2","holder_identity":"371202199006271215","reference_id":"A000662820","card_num":"4218700018145081","bank_code":"305100000013","bank_title_code":"305","customer_id":"100212","card_name":"民生贷记卡(银联卡)"}]
         */

        private BossInfoBean bossInfo;
        private int card_floor_count;
        private List<CardsBeanX> cards;

        public BossInfoBean getBossInfo() {
            return bossInfo;
        }

        public void setBossInfo(BossInfoBean bossInfo) {
            this.bossInfo = bossInfo;
        }

        public int getCard_floor_count() {
            return card_floor_count;
        }

        public void setCard_floor_count(int card_floor_count) {
            this.card_floor_count = card_floor_count;
        }

        public List<CardsBeanX> getCards() {
            return cards;
        }

        public void setCards(List<CardsBeanX> cards) {
            this.cards = cards;
        }

        public static class BossInfoBean {
            /**
             * company_address : null婆婆给我
             * credit_code : 热熔
             * customer_name : 陶务华
             * identity_name : 陶务华
             * monthly_turnover : 334646
             * legal_partner : L
             * customer_type : 00
             * tradeInfo : {"user_level":"2","first_datetime":"20170412","trades":[],"cards":[{"bank_name":"民生银行","trade_total_amount":1050000,"trade_count":3,"card_name":"民生贷记卡(银联卡)"}],"monthly_trades":[{"dailymean_trade_amount":35000,"trade_total_amount":1050000,"trade_max_amount":700000,"trade_average_amount":350000,"dailymean_trade_count":0.1,"trade_cards":1,"trade_month":"201704","trade_count":3}]}
             * contact_phone_num : 13552982091
             * phone_num : 13552982091
             * company_name : 哈咯
             * education_status : B
             * contact_name : 陶务华
             * company_phone_num : 0531-6657184
             * reference_id : A000662820
             * industry_type : S
             * user_id : 100212
             * create_datetime : 20170803173433
             * marital_status : S
             * contact_relation : D
             * identity_num : 371202199006271215
             */

            private String company_address;
            private String credit_code;
            private String customer_name;
            private String identity_name;
            private int monthly_turnover;
            private String legal_partner;
            private String customer_type;
            private TradeInfoBean tradeInfo;
            private String contact_phone_num;
            private String phone_num;
            private String company_name;
            private String education_status;
            private String contact_name;
            private String company_phone_num;
            private String reference_id;
            private String industry_type;
            private String user_id;
            private String create_datetime;
            private String marital_status;
            private String contact_relation;
            private String identity_num;

            public String getCompany_address() {
                return company_address;
            }

            public void setCompany_address(String company_address) {
                this.company_address = company_address;
            }

            public String getCredit_code() {
                return credit_code;
            }

            public void setCredit_code(String credit_code) {
                this.credit_code = credit_code;
            }

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public String getIdentity_name() {
                return identity_name;
            }

            public void setIdentity_name(String identity_name) {
                this.identity_name = identity_name;
            }

            public int getMonthly_turnover() {
                return monthly_turnover;
            }

            public void setMonthly_turnover(int monthly_turnover) {
                this.monthly_turnover = monthly_turnover;
            }

            public String getLegal_partner() {
                return legal_partner;
            }

            public void setLegal_partner(String legal_partner) {
                this.legal_partner = legal_partner;
            }

            public String getCustomer_type() {
                return customer_type;
            }

            public void setCustomer_type(String customer_type) {
                this.customer_type = customer_type;
            }

            public TradeInfoBean getTradeInfo() {
                return tradeInfo;
            }

            public void setTradeInfo(TradeInfoBean tradeInfo) {
                this.tradeInfo = tradeInfo;
            }

            public String getContact_phone_num() {
                return contact_phone_num;
            }

            public void setContact_phone_num(String contact_phone_num) {
                this.contact_phone_num = contact_phone_num;
            }

            public String getPhone_num() {
                return phone_num;
            }

            public void setPhone_num(String phone_num) {
                this.phone_num = phone_num;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getEducation_status() {
                return education_status;
            }

            public void setEducation_status(String education_status) {
                this.education_status = education_status;
            }

            public String getContact_name() {
                return contact_name;
            }

            public void setContact_name(String contact_name) {
                this.contact_name = contact_name;
            }

            public String getCompany_phone_num() {
                return company_phone_num;
            }

            public void setCompany_phone_num(String company_phone_num) {
                this.company_phone_num = company_phone_num;
            }

            public String getReference_id() {
                return reference_id;
            }

            public void setReference_id(String reference_id) {
                this.reference_id = reference_id;
            }

            public String getIndustry_type() {
                return industry_type;
            }

            public void setIndustry_type(String industry_type) {
                this.industry_type = industry_type;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCreate_datetime() {
                return create_datetime;
            }

            public void setCreate_datetime(String create_datetime) {
                this.create_datetime = create_datetime;
            }

            public String getMarital_status() {
                return marital_status;
            }

            public void setMarital_status(String marital_status) {
                this.marital_status = marital_status;
            }

            public String getContact_relation() {
                return contact_relation;
            }

            public void setContact_relation(String contact_relation) {
                this.contact_relation = contact_relation;
            }

            public String getIdentity_num() {
                return identity_num;
            }

            public void setIdentity_num(String identity_num) {
                this.identity_num = identity_num;
            }

            public static class TradeInfoBean {
                /**
                 * user_level : 2
                 * first_datetime : 20170412
                 * trades : []
                 * cards : [{"bank_name":"民生银行","trade_total_amount":1050000,"trade_count":3,"card_name":"民生贷记卡(银联卡)"}]
                 * monthly_trades : [{"dailymean_trade_amount":35000,"trade_total_amount":1050000,"trade_max_amount":700000,"trade_average_amount":350000,"dailymean_trade_count":0.1,"trade_cards":1,"trade_month":"201704","trade_count":3}]
                 */

                private String user_level;
                private String first_datetime;
                private List<?> trades;
                private List<CardsBean> cards;
                private List<MonthlyTradesBean> monthly_trades;

                public String getUser_level() {
                    return user_level;
                }

                public void setUser_level(String user_level) {
                    this.user_level = user_level;
                }

                public String getFirst_datetime() {
                    return first_datetime;
                }

                public void setFirst_datetime(String first_datetime) {
                    this.first_datetime = first_datetime;
                }

                public List<?> getTrades() {
                    return trades;
                }

                public void setTrades(List<?> trades) {
                    this.trades = trades;
                }

                public List<CardsBean> getCards() {
                    return cards;
                }

                public void setCards(List<CardsBean> cards) {
                    this.cards = cards;
                }

                public List<MonthlyTradesBean> getMonthly_trades() {
                    return monthly_trades;
                }

                public void setMonthly_trades(List<MonthlyTradesBean> monthly_trades) {
                    this.monthly_trades = monthly_trades;
                }

                public static class CardsBean {
                    /**
                     * bank_name : 民生银行
                     * trade_total_amount : 1050000
                     * trade_count : 3
                     * card_name : 民生贷记卡(银联卡)
                     */

                    private String bank_name;
                    private int trade_total_amount;
                    private int trade_count;
                    private String card_name;

                    public String getBank_name() {
                        return bank_name;
                    }

                    public void setBank_name(String bank_name) {
                        this.bank_name = bank_name;
                    }

                    public int getTrade_total_amount() {
                        return trade_total_amount;
                    }

                    public void setTrade_total_amount(int trade_total_amount) {
                        this.trade_total_amount = trade_total_amount;
                    }

                    public int getTrade_count() {
                        return trade_count;
                    }

                    public void setTrade_count(int trade_count) {
                        this.trade_count = trade_count;
                    }

                    public String getCard_name() {
                        return card_name;
                    }

                    public void setCard_name(String card_name) {
                        this.card_name = card_name;
                    }
                }

                public static class MonthlyTradesBean {
                    /**
                     * dailymean_trade_amount : 35000
                     * trade_total_amount : 1050000
                     * trade_max_amount : 700000
                     * trade_average_amount : 350000
                     * dailymean_trade_count : 0.1
                     * trade_cards : 1
                     * trade_month : 201704
                     * trade_count : 3
                     */

                    private int dailymean_trade_amount;
                    private int trade_total_amount;
                    private int trade_max_amount;
                    private int trade_average_amount;
                    private double dailymean_trade_count;
                    private int trade_cards;
                    private String trade_month;
                    private int trade_count;

                    public int getDailymean_trade_amount() {
                        return dailymean_trade_amount;
                    }

                    public void setDailymean_trade_amount(int dailymean_trade_amount) {
                        this.dailymean_trade_amount = dailymean_trade_amount;
                    }

                    public int getTrade_total_amount() {
                        return trade_total_amount;
                    }

                    public void setTrade_total_amount(int trade_total_amount) {
                        this.trade_total_amount = trade_total_amount;
                    }

                    public int getTrade_max_amount() {
                        return trade_max_amount;
                    }

                    public void setTrade_max_amount(int trade_max_amount) {
                        this.trade_max_amount = trade_max_amount;
                    }

                    public int getTrade_average_amount() {
                        return trade_average_amount;
                    }

                    public void setTrade_average_amount(int trade_average_amount) {
                        this.trade_average_amount = trade_average_amount;
                    }

                    public double getDailymean_trade_count() {
                        return dailymean_trade_count;
                    }

                    public void setDailymean_trade_count(double dailymean_trade_count) {
                        this.dailymean_trade_count = dailymean_trade_count;
                    }

                    public int getTrade_cards() {
                        return trade_cards;
                    }

                    public void setTrade_cards(int trade_cards) {
                        this.trade_cards = trade_cards;
                    }

                    public String getTrade_month() {
                        return trade_month;
                    }

                    public void setTrade_month(String trade_month) {
                        this.trade_month = trade_month;
                    }

                    public int getTrade_count() {
                        return trade_count;
                    }

                    public void setTrade_count(int trade_count) {
                        this.trade_count = trade_count;
                    }
                }
            }
        }

        public static class CardsBeanX {
            /**
             * bank_name : 中国民生银行
             * holder_name : 陶务华
             * bank_branch_code : 305100000013
             * card_type : 2
             * holder_identity : 371202199006271215
             * reference_id : A000662820
             * card_num : 4218700018145081
             * bank_code : 305100000013
             * bank_title_code : 305
             * customer_id : 100212
             * card_name : 民生贷记卡(银联卡)
             */

            private String bank_name;
            private String holder_name;
            private String bank_branch_code;
            private String card_type;
            private String holder_identity;
            private String reference_id;
            private String card_num;
            private String bank_code;
            private String bank_title_code;
            private String customer_id;
            private String card_name;

            public String getBank_name() {
                return bank_name;
            }

            public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
            }

            public String getHolder_name() {
                return holder_name;
            }

            public void setHolder_name(String holder_name) {
                this.holder_name = holder_name;
            }

            public String getBank_branch_code() {
                return bank_branch_code;
            }

            public void setBank_branch_code(String bank_branch_code) {
                this.bank_branch_code = bank_branch_code;
            }

            public String getCard_type() {
                return card_type;
            }

            public void setCard_type(String card_type) {
                this.card_type = card_type;
            }

            public String getHolder_identity() {
                return holder_identity;
            }

            public void setHolder_identity(String holder_identity) {
                this.holder_identity = holder_identity;
            }

            public String getReference_id() {
                return reference_id;
            }

            public void setReference_id(String reference_id) {
                this.reference_id = reference_id;
            }

            public String getCard_num() {
                return card_num;
            }

            public void setCard_num(String card_num) {
                this.card_num = card_num;
            }

            public String getBank_code() {
                return bank_code;
            }

            public void setBank_code(String bank_code) {
                this.bank_code = bank_code;
            }

            public String getBank_title_code() {
                return bank_title_code;
            }

            public void setBank_title_code(String bank_title_code) {
                this.bank_title_code = bank_title_code;
            }

            public String getCustomer_id() {
                return customer_id;
            }

            public void setCustomer_id(String customer_id) {
                this.customer_id = customer_id;
            }

            public String getCard_name() {
                return card_name;
            }

            public void setCard_name(String card_name) {
                this.card_name = card_name;
            }
        }
    }
}
