package com.ryx.payment.ruishua;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.utils.GlideUtils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.ryx.payment.ruishua.RyxAppconfig.BRANCH;
import static com.ryx.payment.ruishua.RyxAppconfig.resetBANKConfig;
import static com.ryx.payment.ruishua.RyxAppconfig.resetNEWRSConfig;
import static com.ryx.payment.ruishua.RyxAppconfig.resetRSConfig;
import static com.ryx.payment.ruishua.RyxAppconfig.resetRUIHEBAOConfig;
import static com.ryx.payment.ruishua.RyxAppconfig.resetRYXConfig;

/**
 * Created by laomao on 16/4/20.
 * APP数据类
 */
public class RyxAppdata {
    private static Context context;

    private String token = "0000";
    private boolean login=false; // 是否登录
    private String mobileNo = ""; // 代表登录账户手机号
    private String phone = ""; // 目前设置为跟登录号一样， 以后要获取登录的手机的手机号
    private String realName = ""; // 真实姓名/商户名称

    private String customerName = ""; // 法人名称
    private int authenFlag; // 用户是否已认证
    private int openMerchantflag; // 用户是否已认证
    private String customerId; // 用户ID；
    private String certPid; // 证件号码；
    private String certType; // 认证类型
    private String userType; // 用户类型
    private String email; // 用户email

    private String tagDesc; // 用户审核状态名

    private String transLogNo;

    private static RyxAppdata instance= null;
    //登录返回的信息
    /**
     * 手续费中固定金额
     */
    private String rsfee;
    /**
     * 费率
     */
    private String rsRate;
    private String merchantId;
    private String productId;

    private ArrayList<Param> qtpayPublicAttributeList = new ArrayList<Param>();
    // 存储公用的Qtpay里的属性

    private Param tokenafterlogin;
    private Param phoneafterlogin;
    private Param signbeforelogin;



    private RyxAppdata(){

    }

    public static Set<String> resultKeyList=new HashSet<String>();

    /**
     * 校验服务端key值：接口名+transLog+时间
     * @param key
     * @return
     */
    public static boolean checkKeyisOk(String key){
        if(TextUtils.isEmpty(key)){
            return false;
        }
      if(resultKeyList.contains(key.trim())){
          resultKeyList.remove(key);
          return true;
      }
        return  false;
    }

    /**
     * 保存APP中的key值
     */
    public static void saveAppKey(String appKey){
        LogUtil.showLog("saveAppKey=="+appKey);
        resultKeyList.add(appKey.trim());
    }

    public static RyxAppdata getInstance(Context mcontext){

        context=mcontext.getApplicationContext();

        if(instance == null)
            instance = new RyxAppdata();

        return instance;
    }

    public String getRsRate() {
        rsRate= PreferenceUtil.getInstance(context).getString("rsRate", "0.0");
        return rsRate;
    }

    public void setRsRate(String rsRate) {
        PreferenceUtil.getInstance(context).saveString("rsRate", rsRate);
        this.rsRate = rsRate;
    }

    public String getMerchantId() {
        merchantId= PreferenceUtil.getInstance(context).getString("merchantId", "0000000000");
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        PreferenceUtil.getInstance(context).saveString("merchantId", merchantId);
        this.merchantId = merchantId;
    }

    public String getRsfee() {
        rsfee= PreferenceUtil.getInstance(context).getString("rsfee", "0");
        return rsfee;
    }

    public void setRsfee(String rsfee) {
        PreferenceUtil.getInstance(context).saveString("rsfee", rsfee);
        this.rsfee = rsfee;
    }

    public String getProductId() {
        productId= PreferenceUtil.getInstance(context).getString("productId", "0000000000");
        return productId;
    }

    public void setProductId(String productId) {
        PreferenceUtil.getInstance(context).saveString("productId", productId);
        this.productId = productId;
    }
    public void setAuthprocessswitch(boolean authprocessswitch){
        PreferenceUtil.getInstance(context).saveBoolean("authprocessswitch", authprocessswitch);
    }

    /**
     * true开着，使用新的流程<br/>
     * false关着使用旧的流程
     * @return
     */
    public boolean isAuthswitchOpen(){
        return PreferenceUtil.getInstance(context).getBoolean("authprocessswitch",true);
    }

    public String getToken() {
        token= PreferenceUtil.getInstance(context).getString("qtpaytoken", "0000");

        return token;
    }

    public boolean isLogin() {
        login=PreferenceUtil.getInstance(context).getBoolean("qtpaylogin", false);
        return login;
    }

    public void setLogin(boolean login) {
        PreferenceUtil.getInstance(context).saveBoolean("qtpaylogin", login);
        this.login = login;
    }

    public String getMobileNo() {
        mobileNo=PreferenceUtil.getInstance(context).getString("qtpaymobileno", "");
        return mobileNo;
    }

    public String getPhone() {
        phone=PreferenceUtil.getInstance(context).getString("qtpayphone", "");

        return phone;
    }

    public void setToken(String token) {
        PreferenceUtil.getInstance(context).saveString("qtpaytoken", token);
        this.token = token;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        PreferenceUtil.getInstance(context).saveString("qtpaymobileno", mobileNo);

    }

    public void setPhone(String phone) {
        this.phone = phone;
        PreferenceUtil.getInstance(context).saveString("qtpayphone", phone);
    }

    public String getCustomerId() {

        customerId=PreferenceUtil.getInstance(context).getString("qtpaycustomerid","0000" );

        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        PreferenceUtil.getInstance(context).saveString("qtpaycustomerid", customerId);

    }

    public String getRealName() {

        realName=PreferenceUtil.getInstance(context).getString("qtpayrealname", realName);
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
        PreferenceUtil.getInstance(context).saveString("qtpayrealname", realName);

    }

    public int getAuthenFlag() {
        authenFlag=PreferenceUtil.getInstance(context).getInt("qtpayauthenflag", 0);
        return authenFlag;
    }

    public void setAuthenFlag(int authenFlag) {
        PreferenceUtil.getInstance(context).saveInt("qtpayauthenflag", authenFlag);
        this.authenFlag = authenFlag;
    }

    public String getCertPid() {

        return certPid;
    }

    public void setCertPid(String certPid) {
        this.certPid = certPid;
    }

    public String getCertType() {
        return certType;
    }

    public String getUserType() {
        userType=PreferenceUtil.getInstance(context).getString("qtpayusertype", "");
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public void setUserType(String userType) {
        PreferenceUtil.getInstance(context).saveString("qtpayusertype", userType);
        this.userType = userType;
    }


    public ArrayList<Param> getQtpayPublicAttributeList() {
        return qtpayPublicAttributeList;
    }

    public void setQtpayPublicAttributeList(
            ArrayList<Param> qtpayPublicAttributeList) {
        this.qtpayPublicAttributeList = qtpayPublicAttributeList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransLogNo() {
        return transLogNo;
    }

    public void setTransLogNo(String transLogNo) {
        this.transLogNo = transLogNo;
    }

    public Param getTokenafterlogin() {
        return tokenafterlogin;
    }

    public Param getPhoneafterlogin() {
        return phoneafterlogin;
    }

    public Param getSignbeforelogin() {
        return signbeforelogin;
    }

    public void setTokenafterlogin(Param tokenafterlogin) {
        this.tokenafterlogin = tokenafterlogin;
    }

    public void setPhoneafterlogin(Param phoneafterlogin) {
        this.phoneafterlogin = phoneafterlogin;
    }

    public void setSignbeforelogin(Param signbeforelogin) {
        this.signbeforelogin = signbeforelogin;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTagDesc() {

        tagDesc=PreferenceUtil.getInstance(context).getString("qtpaytagDesc", "0000");

        return tagDesc;
    }

    public void setTagDesc(String tagDesc) {


        PreferenceUtil.getInstance(context).saveString("qtpaytagDesc", tagDesc);

        this.tagDesc = tagDesc;
    }

    /**
     * 瑞刷有关
     * @param mobileNo
     * @param cardNo
     * @param cardIdx
     */
    public   void saveRuishuaparam(String mobileNo,String cardNo,String cardIdx) {

        SharedPreferences shareditorPreferences = context.getSharedPreferences(mobileNo, 0);
        SharedPreferences.Editor editor = shareditorPreferences.edit();

        editor.putString("cardNo"	, cardNo);
        editor.putString("cardIdx"	, cardIdx);

        editor.commit();

    }
    public    String getRuishuaParam(String mobileNo,String key, String defaultValue ) {
        return context.getSharedPreferences(mobileNo, 0).getString(key, defaultValue );
    }
    /**
     * 获取是否可以开通商户功能标识
     * @return
     */
    public int getIsOpenMerchantFlag() {
        openMerchantflag = PreferenceUtil.getInstance(context).getInt("IsOpenMerchantflag", 0);
        return openMerchantflag;
    }

    /**
     * 设置是否可以开通商户功能标识
     * @param openMerchantflag
     */
    public void setIsOpenMerchantFlag(int openMerchantflag) {
        PreferenceUtil.getInstance(context).saveInt("IsOpenMerchantflag", openMerchantflag);
        this.openMerchantflag = openMerchantflag;
        LogUtil.showLog("IsOpenMerchantflag=="+openMerchantflag+"++");
    }

    /**
     * 设置是否支持长按识别二维码
     * @param touchPayTag
     */
    public void setkjzfTouchPayTag(int touchPayTag){
        PreferenceUtil.getInstance(context).saveInt("touchPayTag", touchPayTag);
    }

    public void setbeanStatusDesc(String desc){
        PreferenceUtil.getInstance(context).saveString("bean_status_desc",desc);
    }
    public String getBeanStatusDesc(){
        return PreferenceUtil.getInstance(context).getString("bean_status_desc","");
    }
    /**
     * 获取是否支持长按识别二维码1是支持
     * @return
     */
    public int getkjzfTouchPayTag(){
        return PreferenceUtil.getInstance(context).getInt("touchPayTag",0);
    }

    //以下为贴牌灵活更换新增接口

    /**
     * 获取当前贴牌如果用户没有实名认证是否可以进行更新用户信息
     * @return 1需要 0不需要
     */
    public  int  getCurrentIsNeedUpdateUserInfoType(){
//        if("04".equals(BRANCH)){
//            //瑞和宝如果用户没有实名认证，可以从其他贴牌进行更新用户信息
//            return 1;
//        }
//        if("05".equals(BRANCH)){
//            //瑞银信分享版是独立app无需自动导入用户信息
//            return 0;
//        }
        return 1;
    }


//    /**
//     * 根据Branch来加载不同贴牌闪屏页
//     */
//    public void glideLoadsplashimgImageViewForBranch(ImageView imageView){
//        if (BRANCH.equals("01")){//瑞刷
//            GlideUtils.getInstance().load(context, R.drawable.splashimg_ruishua, imageView);
//        }else if (BRANCH.equals("02")) { //瑞银信
//            GlideUtils.getInstance().load(context, R.drawable.splashimg_ruiyinxin, imageView);
//        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
//            GlideUtils.getInstance().load(context, R.drawable.splashimg_newrs, imageView);
//        }else if(BRANCH.equals("04")){ //瑞和宝
//            GlideUtils.getInstance().load(context, R.drawable.splashimg_ruihebao, imageView);
//        }else if(BRANCH.equals("05")){//瑞银信分享版
//            GlideUtils.getInstance().load(context, R.drawable.splashimg_ryxfxb, imageView);
//        }
//    }
    /**
     * 根据Branch来加载不同贴牌drawable的logo页
     */
    public void glideLoaddrawableLogoImageViewForBranch(ImageView imageView){
        if (BRANCH.equals("01")){//瑞刷
            GlideUtils.getInstance().load(context, R.drawable.ruishualogin_logo, imageView);
        }else if (BRANCH.equals("02")) { //瑞银信
            GlideUtils.getInstance().load(context,R.drawable.ruiyinxinlogin_logo, imageView);
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            GlideUtils.getInstance().load(context, R.drawable.newrslogin_logo, imageView);
        }else if(BRANCH.equals("04")){ //瑞和宝
            GlideUtils.getInstance().load(context, R.drawable.ruihebaologin_logo, imageView);
        }else if(BRANCH.equals("05")){//瑞银信分享版
            GlideUtils.getInstance().load(context, R.drawable.ryx_fxblogin_logo, imageView);
        }
    }
    /**
     * 根据Branch来加载不同贴牌mipmap的logo页
     */
    public void glideLoadmipmapLogoImageViewForBranch(ImageView imageView){
        if (BRANCH.equals("01")){//瑞刷
            GlideUtils.getInstance().load(context, R.mipmap.ruishualogo, imageView);
        }else if (BRANCH.equals("02")) { //瑞银信
            GlideUtils.getInstance().load(context, R.mipmap.ruiyinxinlogo, imageView);
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            GlideUtils.getInstance().load(context, R.mipmap.newrslogo, imageView);
        }else  if(BRANCH.equals("04")){ //瑞和宝
            GlideUtils.getInstance().load(context, R.mipmap.ruihebaologo, imageView);
        }else  if(BRANCH.equals("05")){ //瑞银信分享版
            GlideUtils.getInstance().load(context, R.mipmap.ryx_fxb, imageView);
        }
        }

    /**
     * 获取当前分支图标（AppIConUtil使用）
     * @return
     */
    public static int getCurrentBranchMipmapLogoId(){
        if (BRANCH.equals("01")){//瑞刷
            return R.mipmap.ruishualogo;
        }else if (BRANCH.equals("02")) { //瑞银信
            return R.mipmap.ruiyinxinlogo;
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return R.mipmap.newrslogo;
        }else if(BRANCH.equals("04")){
            return R.mipmap.ruihebaologo;
        }else if(BRANCH.equals("05")){//瑞银信分享版
            return R.mipmap.ryx_fxb;
        }
        return R.mipmap.ruiyinxinlogo;
    }
    /**
     * 根据Branch来加载我的二维码界面不同贴牌最下面应用图标
     * */
    public void glideLoadqrcodeimgmageViewForBranch(ImageView imageView){
        if (BRANCH.equals("01")){//瑞刷
            GlideUtils.getInstance().load(context, R.drawable.qrcode_img_ruishua, imageView);
        }else if (BRANCH.equals("02")) { //瑞银信
            GlideUtils.getInstance().load(context, R.drawable.qrcode_img_ruiyinxin, imageView);
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            GlideUtils.getInstance().load(context, R.drawable.qrcode_img_newrs, imageView);
        }else if(BRANCH.equals("04")){ //瑞和宝
            GlideUtils.getInstance().load(context, R.drawable.qrcode_img_ruihebao, imageView);
        }else if(BRANCH.equals("05")){ //瑞银信分享版
            GlideUtils.getInstance().load(context, R.drawable.qrcode_img_ryx_fxb, imageView);
        }
    }
    /**
     * 获取当前分支名称
     * @return
     */
    public String getCurrentBranchName(){
        if (BRANCH.equals("01")){//瑞刷
            return context.getResources().getString(R.string.app_name);
        }else if (BRANCH.equals("02")) { //瑞银信
            return context.getResources().getString(R.string.app_name_ryx);
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return context.getResources().getString(R.string.app_name_newrs);
        }else if(BRANCH.equals("04")){
            return context.getResources().getString(R.string.app_name_rhb);
        }else if(BRANCH.equals("05")){
            return context.getResources().getString(R.string.app_name_bank);
        }
        return context.getResources().getString(R.string.app_name_ryx);
    }

    /**
     * 获取当前分支Home中的广告位id
     * @return
     */
    public String getCurrentHomeAdposiId(){
        if (BRANCH.equals("01")){//瑞刷
            return "5";
        }else if (BRANCH.equals("02")) { //瑞银信
            return "6";
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return "7";
        }else if(BRANCH.equals("04")){//瑞和宝
            return "8";
        }else if(BRANCH.equals("05")){//瑞银信分享版
            return "2";
        }
        return "5";
    }

    /**
     * 获取当前分支的Id值
     * @return
     */
    public String getCurrentBranchId(){
        if (BRANCH.equals("01")){//瑞刷
            return "00800118";
        }else if (BRANCH.equals("02")) { //瑞银信
            return "00800084";
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return "00800133";
        }else if(BRANCH.equals("04")){
            return "00800189";
        }else if(BRANCH.equals("05")){
            return "00800127";
        }
        return "00800118";
    }

    /**
     * 获取当前
     * @return
     */
    public String getCurrentFileBranchRootName(){
        if (BRANCH.equals("01")){//瑞刷
            return "ruishua/";
        }else if (BRANCH.equals("02")) { //瑞银信
            return "ruiyinxin/";
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return "newrs/";
        }else if(BRANCH.equals("04")){
            return "ruihebao/";
        }else if(BRANCH.equals("05")){
            return "bank/";
        }
        return "ruiyinxin/";
    }


    /**
     * 获取当前分支Home中Raw中配置文件
     * @return
     */
    public int getCurrentBranchRawTopGridConfigId(){
        if(BRANCH.equals("04")){
            return R.raw.top_grid_rhb;
        }
        return R.raw.top_grid;
    }

    /**
     * 获取当前贴牌下标题栏背景色
     * @return
     */
    public int getCurrentBranchTitleBagColor(){
        if(BRANCH.equals("04")){
            return R.color.rhb_title_bc;
        }
        return   R.color.blue;
    }
    /**
     * 获取当前分支Home中Raw中TopMain配置文件
     * @return
     */
    public int getCurrentBranchRawTopMainConfigId(){
        if(BRANCH.equals("04")){
            return R.raw.top_main_rhb;
        }

        return R.raw.top_main;
    }

    /**
     * 更多中资源信息配置
     * @return
     */
    public int getCurrentBranchRawMoreConfigId(){
        if(BRANCH.equals("04")){
            return R.raw.bottom_grid_moreincreat_rhb;
        }

        return R.raw.bottom_grid_moreincreat;
    }

    /**
     * 获取当前贴牌客服key值
     * @return
     */
    public String getCurrentBranchKefuKey(){
        if(BRANCH.equals("01")){
            //瑞刷
            return "abec5faafde448e3900b4ad25980107b";
        }else if(BRANCH.equals("04")){
            //瑞和宝
            return "06f20e387139430381b2d4595843fba4";
        }else if(BRANCH.equals("05")){
            //瑞银信分享版
            return "2f930a19e0274b2894dc5c66eb4caf61";
        }
        return "";
    }

    /**
     * 获取当前主题Tag  1,蓝色主题,2灰色主题
     * @return
     */
    public int getCurrentBranchMainStyleTag(){
        if(BRANCH.equals("04")){
            return 2;
        }
        return 1;
    }

    /**
     * 获取当前引导页类型1，为瑞银信瑞刷瑞和宝引导页2，为瑞银信分享版类型引导页3瑞和宝
     * @return
     */
    public int getCurrentGuideType(){
        if(BRANCH.equals("05")){
            return 2;
        }else if(BRANCH.equals("04")){
            return 3;
        }
        return 1;
    }
    /**
     * 获取当前贴牌所属分支1，瑞刷分支 2，瑞银信分支
     * @return
     */
    public int getCurrentBranchType(){
//        if (BRANCH.equals("01")){//瑞刷
//            return 1;
//        }else if (BRANCH.equals("02")) { //瑞银信
//            return 2;
//        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
//            return 2;
//        }else if(BRANCH.equals("04")){
//            return 2;
//        }
        return 2;
    }
    /**
     * 获取当前贴牌所属分支1，瑞刷分支 2，瑞银信分支
     * @return
     */
    public int getCurrentBranchType_Details(){
//        if (BRANCH.equals("01")){//瑞刷
//            return 2;
//        }else if (BRANCH.equals("02")) { //瑞银信
//            return 2;
//        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
//            return 2;
//        }else if(BRANCH.equals("04")){
//            return 2;
//        }else if(BRANCH.equals("05")){
//            return 2;
//        }
        return 2;
    }
    /**
     * 仅限于在LoginActivity使用
     * 获取当前登录所属类型
     * 1，瑞刷类型，无需选择贴牌 2，提供选择贴牌设置贴牌
     * @return
     */
    public int getCurrentLoginBranchType(){
        if (BRANCH.equals("01")){//瑞刷
            return 2;
        }else if (BRANCH.equals("02")) { //瑞银信
            return 2;
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return 2;
        }else if(BRANCH.equals("04")){
            return 2;
        }else if(BRANCH.equals("05")){
            return 1;
        }
        return 1;
    }

    /**
     * 仅限于ResetPassword界面是否展示类型选择用
     * 1，瑞刷类型，无需选择贴牌 2，提供选择贴牌设置贴牌
     * @return
     */
    public int getCurrentResetBranchType(){

        if (BRANCH.equals("01")){//瑞刷
            return 2;
        }else if (BRANCH.equals("02")) { //瑞银信
            return 2;
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return 2;
        }else if(BRANCH.equals("04")){
            return 2;
        }else if(BRANCH.equals("05")){
            return 1;
        }
        return 1;
    }

    /**
     * 仅限于RegActivity注册页使用
     * 判断当前注册类型1不展示贴牌列表，2展示贴牌列表
     * @return
     */
    public int getCurrentRegBranchType(){
//        if (BRANCH.equals("01")){//瑞刷
//            return 1;
//        }else if (BRANCH.equals("02")) { //瑞银信
//            return 1;
//        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
//            return 1;
//        }else if(BRANCH.equals("04")){
//            return 1;
//        }else if(BRANCH.equals("05")){
//            return 1;
//        }
        return 1;
    }

    /**
     * 获取当前分支下载地址
     * @return
     */
    public String getCurrentBranchDownLoadAddress(){
        if (BRANCH.equals("01")){//瑞刷
            return context.getResources().getString(R.string.service_download);
        }else if (BRANCH.equals("02")) { //瑞银信
            return context.getResources().getString(R.string.service_download_ryx);
        }else if(BRANCH.equals("03")){ //新的瑞刷2.0
            return context.getResources().getString(R.string.service_download_newrs);
        }else if(BRANCH.equals("04")){
            //瑞和宝
            return context.getResources().getString(R.string.service_download_rhx);
        }else if(BRANCH.equals("05")){
            //瑞银信分享版
            return context.getResources().getString(R.string.service_download_ryxfxb);
        }
        return context.getResources().getString(R.string.service_download_gw);
    }

    /**
     * 获取当前app是否支持商城
     * @return
     */
    public boolean getCurrentBranchIsSupportBank(){
        //帮客App支持三级分销商城
        if(BRANCH.equals("05")){
            return true;
        }
        return false;
    }

    /**
     * 判断当前贴牌注册是否需要邀请码
     * @return
     */
    public boolean getCurrentBranchRegIsNeedCode(){
        //Bank贴牌
        if(BRANCH.equals("05")){
            return true;
        }
        return false;
    }
    /**
     * 重置当前配置
     */
    public void resetCurrentBranchConfig(){
        if (BRANCH.equals("01")) {
            //瑞刷
            resetRSConfig();
        } else if (BRANCH.equals("02")) {
            //瑞银信
            resetRYXConfig();
        }else if(BRANCH.equals("03")){
            //瑞刷2.0
            resetNEWRSConfig();
        }else if(BRANCH.equals("04")){
            //瑞和宝
            resetRUIHEBAOConfig();
        }else if(BRANCH.equals("05")){
            resetBANKConfig();
        }
    }

    /**
     * 资金结算收费信息是否展示开关
     */
    public Boolean withdrawListImActivityfeeMessageIsshow(){
        if(BRANCH.equals("05")){
            return true;
        }
        return false;
    }
    /**
     * 初始化当前分支Id根据包名
     */
    public void initCurrentBranchIdForPakg(){
            String pkName = context.getPackageName();
            if (RyxAppconfig.RUISHUAPAGENAME.equals(pkName)) {
                BRANCH = "01";
            } else if (RyxAppconfig.RYXPAGENAME.equals(pkName)) {
                BRANCH = "02";
            }else if(RyxAppconfig.NEWRSPAGENAME.equals(pkName)){
                BRANCH = "03";
            }else if(RyxAppconfig.RUIHEBAOPAGENAME.equals(pkName)){
                BRANCH = "04";
            }else if(RyxAppconfig.BANKPAGENAME.equals(pkName)){
                BRANCH = "05";
            }
    }


}
