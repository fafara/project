package com.ryx.ryxcredit.beans.pojo;

import java.io.Serializable;

/**
 * Created by DIY on 2016/9/13.
 */
public class Customer implements Serializable {
    //小贷客户号
    private String customer_id	;
    //手刷商户号
    private String reference_id	;
    //用户姓名
    private String  customer_name	;
    //用户类型
    private String customer_type	;
    //客户状态
    private String customer_status	;
    ////证件号
    private String identity_num	;
    ////证件姓名
    private String  identity_name	;
    //证件类型
    private String  identity_type	;
    //手机号
    private String phone_num	;
    //用户id
    private String user_id;
    //代理商编号
    private String agent_id	;
    //代理商名称
    private String  agent_name	;
    //	公司名称
    private String  company_name;
    //公司电话
    private String company_phone_num	;
    //公司地址
    private String  company_address	;
    //工业类型
    private String  industry_type	;
    //月收入
    private String monthly_income;
    //联系人姓名
    private String contact_name;
    //	联系人关系
    private String contact_relation;
    //	联系人电话
    private String contact_phone_num;
    //	婚姻状况
    private String  marital_status;
    //	教育程度
    private String  education_status;
    //	创建时间
    private String  create_datetime;
    //法人股东
    private String legal_partner;

    public String getMonthly_turnover() {
        return monthly_turnover;
    }

    public void setMonthly_turnover(String monthly_turnover) {
        this.monthly_turnover = monthly_turnover;
    }

    //月营业额
    private String monthly_turnover;
    //社会统一信用编码
    private String credit_code;

    public String getCareer_type() {
        return career_type;
    }

    public void setCareer_type(String career_type) {
        this.career_type = career_type;
    }

    //职业类型
    private String career_type;

    public String getLegal_partner() {
        return legal_partner;
    }

    public void setLegal_partner(String legal_partner) {
        this.legal_partner = legal_partner;
    }



    public String getCredit_code() {
        return credit_code;
    }

    public void setCredit_code(String credit_code) {
        this.credit_code = credit_code;
    }


    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public String getIdentity_num() {
        return identity_num;
    }

    public void setIdentity_num(String identity_num) {
        this.identity_num = identity_num;
    }

    public String getIdentity_name() {
        return identity_name;
    }

    public void setIdentity_name(String identity_name) {
        this.identity_name = identity_name;
    }

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_phone_num() {
        return company_phone_num;
    }

    public void setCompany_phone_num(String company_phone_num) {
        this.company_phone_num = company_phone_num;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getIndustry_type() {
        return industry_type;
    }

    public void setIndustry_type(String industry_type) {
        this.industry_type = industry_type;
    }

    public String getMonthly_income() {
        return monthly_income;
    }

    public void setMonthly_income(String monthly_income) {
        this.monthly_income = monthly_income;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_relation() {
        return contact_relation;
    }

    public void setContact_relation(String contact_relation) {
        this.contact_relation = contact_relation;
    }

    public String getContact_phone_num() {
        return contact_phone_num;
    }

    public void setContact_phone_num(String contact_phone_num) {
        this.contact_phone_num = contact_phone_num;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getEducation_status() {
        return education_status;
    }

    public void setEducation_status(String education_status) {
        this.education_status = education_status;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id='" + customer_id + '\'' +
                ", reference_id='" + reference_id + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", customer_type='" + customer_type + '\'' +
                ", customer_status='" + customer_status + '\'' +
                ", identity_num='" + identity_num + '\'' +
                ", identity_name='" + identity_name + '\'' +
                ", identity_type='" + identity_type + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", agent_id='" + agent_id + '\'' +
                ", agent_name='" + agent_name + '\'' +
                ", company_name='" + company_name + '\'' +
                ", company_phone_num='" + company_phone_num + '\'' +
                ", company_address='" + company_address + '\'' +
                ", industry_type='" + industry_type + '\'' +
                ", monthly_income='" + monthly_income + '\'' +
                ", contact_name='" + contact_name + '\'' +
                ", contact_relation='" + contact_relation + '\'' +
                ", contact_phone_num='" + contact_phone_num + '\'' +
                ", marital_status='" + marital_status + '\'' +
                ", education_status='" + education_status + '\'' +
                '}';
    }
}
