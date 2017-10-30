package com.ryx.ryxcredit.utils;

import com.ryx.ryxcredit.RyxcreditConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DIY on 2016/7/19.
 */
public class CConstants {
    public static final String C_TRANS_LOG_NO="TransLogNO";
    public static String[] datas=new String[]{"1","2","3","4","5","6","7","8","9","A","B","C"};

    //如何激活额度
    public static final String HOW_ACTIVATE =  RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruiyidai/help/jihuoedu.html";
    //如何还款
    public static final String HOW_REPAYMENT = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruiyidai/help/huankuan.html";
    //如何借款
    public static final String HOW_BORROW = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruiyidai/help/jiekuan.html";


    //瑞卡贷如何激活额度
    public static final String RKD_HOW_ACTIVATE = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/help/jihuoedu.html";
    //瑞卡贷如何还款
    public static final String RKD_HOW_REPAYMENT = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/help/huankuan.html";
    //瑞卡贷如何借款
    public static final String RKD_HOW_BORROW = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/help/jiekuan.html";


    //关于瑞易贷
    public static final String ABOUT_PRODUCT = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/about.html";
    //额度问题
    public static final String EDU_PRO =  RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/eduProblem.html";
    //借款问题
    public static final String CREDIT_PRO = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/jiekuanProblem.html";
    //还款问题
    public static final String REPAY_PRO = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/huankuanProblem.html";

    //关于瑞卡贷
    public static final String RKD_ABOUT_PRODUCT = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruishangdai/about.html";
    //瑞卡贷额度问题
    public static final String RKD_EDU_PRO = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruishangdai/eduwenti.html";
    //瑞卡贷借款问题
    public static final String RKD_CREDIT_PRO = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruishangdai/jiekuanProblem.html";
    //瑞卡贷还款问题
    public static final String RKD_REPAY_PRO = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruishangdai/huankuanProblem.html";
    //瑞卡贷还款参数
    public static final String RKD_HUANKUAN_PRO = RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruishangdai/huankuanParams.html";
    //额度共享问题
    public static final String SHARKING_QUOTA =RyxcreditConfig.ORIGNAL_URL+"xiaodai/ruikadai/edugxProblem.html";

    //人行征信获取身份码地址
    public static final String CREDIT_ID_NUM_ADDRESS ="https://mposprepo.ruiyinxin.com:4430/xiaodai/peopleline/bank.html";

    public  static String getSelectedKey(int pos){
        return datas[pos];
    }

    static HashMap<String,String> loadstatus=new HashMap<String,String>();
    static {
        loadstatus.put("A","已结清");
        loadstatus.put("B","已逾期");
        loadstatus.put("C","使用中");
        loadstatus.put("D","使用中");
        loadstatus.put("E","使用中");
        loadstatus.put("F","放款中");
        loadstatus.put("G","处理中");
        loadstatus.put("U","处理中");
        loadstatus.put("R","审批未通过");
    }
    public static String getLoanStatus(String key){
        return loadstatus.get(key);
    }

    static HashMap<String,String> payedstatus=new HashMap<String,String>();
    static {
        payedstatus.put("A","已结清");
        payedstatus.put("B","已逾期");
        payedstatus.put("C","已结清");
        payedstatus.put("D","已结清");
        payedstatus.put("E","已结清");
        payedstatus.put("F","放款中");
        payedstatus.put("G","放款失败");
        payedstatus.put("U","审批中");
        payedstatus.put("R","审批未通过");
        payedstatus.put("I","放款失败");
    }
    public static String getPayedstatusStatus(String key){
        return payedstatus.get(key);
    }
    //行业类别
    static HashMap<String,String> industrystatus=new HashMap<String,String>();
    static {

        industrystatus.put("J","银行及证券业");
        industrystatus.put("N","政府机关及事业单位");
        industrystatus.put("L","保险及其它金融业");
        industrystatus.put("D","电信、电力、烟草");
        industrystatus.put("G","IT行业、互联网行业");
        industrystatus.put("S","社会组织、行业协会");
        industrystatus.put("H","批发、零售及商业贸易");
        industrystatus.put("E","制造业、建筑业及房地产");
        industrystatus.put("I","住宿、餐饮、旅游及其他服务业");
        industrystatus.put("A","农林牧渔");
        industrystatus.put("T","现役军人、警察");
        industrystatus.put("R","娱乐业及其他");
    }

    public static String getIndustryStatus(String key){
        return industrystatus.get(key);
    }

    public static String getIndustryKey(String value){
          for(Map.Entry<String,String> data: industrystatus.entrySet()){
              if(value.equals(data.getValue())){
                  return data.getKey();
              }
          }
        return  null;
    }

    //婚姻类别
    static HashMap<String,String> marrystatus=new HashMap<String,String>();
    static {

        marrystatus.put("S","未婚");
        marrystatus.put("C","已婚");
        marrystatus.put("D","离异");
        marrystatus.put("O","其他");

    }

    public static String getMarryStatus(String key){
        return marrystatus.get(key);
    }

    public static String getMarryKey(String value){
        for(Map.Entry<String,String> data: marrystatus.entrySet()){
            if(value.equals(data.getValue())){
                return data.getKey();
            }
        }
        return  null;
    }
    //学历
    static HashMap<String,String> educationstatus=new HashMap<String,String>();
    static {
        /*educationstatus.put("B","研究生及以上");
        educationstatus.put("C","大专大学本科");
        educationstatus.put("F","高中中专及以下");*/
        educationstatus.put("H","大学本科及以上");
        educationstatus.put("I","大学专科和专科学校");
        educationstatus.put("J","中等专业学校或中等技术学校");
        educationstatus.put("K","技术学校");
        educationstatus.put("L","高中及以下");
    }

    //亲属关系
    static HashMap<String,String> relationShipMaps=new HashMap<String,String>();
    static {
        relationShipMaps.put("D","父母");
        relationShipMaps.put("Y","朋友");
        relationShipMaps.put("W","同事");
        relationShipMaps.put("C","配偶");
        relationShipMaps.put("L","其他亲属");
    }

    public static String getRelationShipStatus(String key){
        return relationShipMaps.get(key);
    }

    public static String getRelationShipKey(String value){
        for(Map.Entry<String,String> data: relationShipMaps.entrySet()){
            if(value.equals(data.getValue())){
                return data.getKey();
            }
        }
        return  null;
    }

    public static String getEducationStatus(String key){
        return educationstatus.get(key);
    }

    public static String getEducationKey(String value){
        for(Map.Entry<String,String> data: educationstatus.entrySet()){
            if(value.equals(data.getValue())){
                return data.getKey();
            }
        }
        return  null;
    }


    //职业类别
    static HashMap<String,String> occupationalstatus=new HashMap<String,String>();
    static {

        occupationalstatus.put("A","技工类人员");
        occupationalstatus.put("B","技工类人员");
        occupationalstatus.put("C","销售类人员");
        occupationalstatus.put("D","行政办事类人员");
        occupationalstatus.put("F","专业技术类人员");
        occupationalstatus.put("F","其他类人员");
    }

    public static String getOccupationalStatus(String key){
        return occupationalstatus.get(key);
    }

    public static String getOccupationalKey(String value){
        for(Map.Entry<String,String> data: occupationalstatus.entrySet()){
            if(value.equals(data.getValue())){
                return data.getKey();
            }
        }
        return  null;
    }

}
