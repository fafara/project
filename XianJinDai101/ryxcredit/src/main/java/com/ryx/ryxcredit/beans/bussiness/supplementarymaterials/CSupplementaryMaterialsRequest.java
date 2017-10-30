package com.ryx.ryxcredit.beans.bussiness.supplementarymaterials;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by lijing on 2016/9/12.
 * 个人资料补充请求
 */
public class CSupplementaryMaterialsRequest extends CbaseRequest {


    /**
     * company_name :
     * company_phone_num :
     * company_address :
     * industry_type :
     * monthly_income :
     * contact_name :
     * contact_relation :
     * contact_phone_num :
     * marital_status :
     * education_status :
     * career_type
     */

    private String company_name;
    private String company_phone_num;
    private String company_address;
    private String industry_type;
    private String monthly_income;
    private String contact_name;
    private String contact_relation;
    private String contact_phone_num;
    private String marital_status;
    private String education_status;
    //法人股东
    private String legal_partner;
    //月营业额
    private String monthly_turnover;
    //社会统一信用编码
    private String credit_code;
    //职业类型
    private String career_type;
    public String getCareer_type() {
        return career_type;
    }

    public void setCareer_type(String career_type) {
        this.career_type = career_type;
    }

    public String getMonthly_turnover() {
        return monthly_turnover;
    }

    public void setMonthly_turnover(String monthly_turnover) {
        this.monthly_turnover = monthly_turnover;
    }



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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    private String province ;
    private String city ;
    private String region ;

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
}
