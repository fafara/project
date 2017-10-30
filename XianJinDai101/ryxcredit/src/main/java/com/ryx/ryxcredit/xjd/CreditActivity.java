package com.ryx.ryxcredit.xjd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.agreement.AgreementRequest;
import com.ryx.ryxcredit.beans.bussiness.agreement.AgreementResponse;
import com.ryx.ryxcredit.beans.findkinds.FindKindsRequest;
import com.ryx.ryxcredit.beans.findkinds.FindKindsResponse;
import com.ryx.ryxcredit.beans.findtime.FindtimeRequest;
import com.ryx.ryxcredit.beans.findtime.FindtimeResponse;
import com.ryx.ryxcredit.newactivity.ActivateLineActivity;
import com.ryx.ryxcredit.newactivity.newFindCustommer.CfindCustomerRequest;
import com.ryx.ryxcredit.newactivity.newFindCustommer.CfindCustomerResponse;
import com.ryx.ryxcredit.newbean.userlevel.UserLevelRequest;
import com.ryx.ryxcredit.newbean.userlevel.UserLevelResponse;
import com.ryx.ryxcredit.ryd.activity.RYDBaseInfoSuccesActivity;
import com.ryx.ryxcredit.ryd.activity.RYDBorrowMoneyActivity;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CDensityUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.widget.CMoneyTextView;
import com.ryx.ryxcredit.widget.CRyxSlideUnlock;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;
import com.ryx.ryxcredit.xjd.bean.findbasedata.FindBaseDataRequest;
import com.ryx.ryxcredit.xjd.bean.findbasedata.FindBaseDataResponse;
import com.ryx.ryxcredit.xjd.bean.priceinfo.PriceinfoRequest;
import com.ryx.ryxcredit.xjd.bean.priceinfo.PriceinfoResponse;
import com.xyzlf.poplib.PopCommon;
import com.xyzlf.poplib.PopModel;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import static com.livedetect.utils.DensityUtils.dp2px;
import static com.ryx.ryxcredit.R.id.c_repayment_btn;
import static com.ryx.ryxcredit.R.id.tilerightImg;
import static com.ryx.ryxcredit.R.id.under_check_tv1;

/**
 * 首页，额度页面
 */

public class CreditActivity extends BaseActivity {
/*    @Override
    public int getLayoutId() {
        return R.layout.c_activity_credit_new;
    }*/
    private ImageView iv_tilerightImg;
    private Button bt_repayment;
    //我要借款
    private AutoRelativeLayout borrowBtn, c_borrow_xjd_btn;
    private Button repaymentBtn;
    //            borrowRecordsBtn;
    //当前额度，可用金额,最高额度
    private CMoneyTextView tvDefaultTotal, tvDefaultAvailable, tvDefaultMax;
    //默认总额度，默认可用金额,默认当前额度
    private CMoneyTextView tvTotal, tvAvailable, tvMax;
    private ImageView edtImg;

    //重新激活按钮
    //private Button recoveryBtn;
    private int customerStatus;
    //是否还款中
    private boolean isPlaying;
    private double avilableAmount;
    private String openTime;//交易开始时间
    private String closeTime;//交易结束时间
    private boolean isOverdue;//有逾期未还贷款


    private String paymentStatus;//我要借款按钮状态
    private String paymentType;//我要借款请求类型
    private String paymentText;//点“我要借款”提示内容

    private String repaymentStatus;//我要还款按钮状态
    private String repaymentType;//我要还款请求类型
    private String repaymentText;//点“我要还款”提示内容

    private String loanRecordStatus;//借还记录按钮状态
    private String loanRecordType;//借还记录请求类型
    private String loanRecordText;//点“借还记录”提示内容

    private String ownDataStatus;//个人资料按钮状态，首页头部为“个人资料”
    private String ownDataType;//个人资料请求类型
    private String ownDataText;//点“个人资料”提示内容

    private String total_amount;//总额度
    private String available_amount;//可用额度
    private String max_amount;//最高额度
    private int customer_status;//客户状态
    private int payment_card_count;//需要填写的信用卡数量
    private boolean is_opened;//是否在业务时间内
    private boolean is_active;//是否激活
    private boolean is_repaying;//是否还款中
    private boolean is_beoverdue;//是否有逾期贷款
    private int active_status;//激活状态（未激活；激活已过期；激活未过期）
    private String activation_status;//审批状态（U:正在处理,R拒绝,A:通过,N:签署：审核通过 ）
    private String agreement_id;//协议编号
    private String agreement_url;//协议地址
    private boolean is_agreed;//是否同意《俱乐部服务协议》
    private String service_authorize_url;//服务授权协议地址
    private boolean is_service;//是否签署过瑞卡贷服务授权书
    private String appointment_url;//预约活动地址
    private TextView tv_promote_amount;//提额文字
    private ImageView right_promote_amount;//提额箭头
    private TextView tv_work_time;//业务时间
    private String card_activation;//瑞卡贷激活状态
    private String business_activation;//现金贷激活状态
    private String trade_product_url;//服务授权协议
    private double timer =0;
    private long locantime;
    private long version_time;
    private FindtimeResponse.ResultBean result;
    private String locan_version_id;
    private String version_id;//
    private long locan_version_time =-1;
    private FindKindsResponse findtimeResponse;
    SharedPreferences sp;
    private FindBaseDataResponse.ResultBean.BossInfoBean bossInfo;
    private boolean is_into ;//贷款状态
    private TextView tv_under_check;//人工处理的文言
    private TextView tv1_under_check;//人工处理的文言
    private String version_add;//下载地址
    private Uri uri;
    private String authorise_info_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_credit_new);
        RyxcreditConfig.context = this;
        setTitleLayout("信用俱乐部", true, true);
        setbottomMenu();
        initView();
        sp = getSharedPreferences("findtime", Context.MODE_PRIVATE);
        locan_version_time = sp.getLong("version_time", 0);
        locan_version_id = sp.getString("version_id", "");
        locantime = sp.getLong("locantime", 0);
        requestFindTime();
    //    getXJDUserLevel();


    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
        getPriceInfo();
    }

    /**
     * 获取首页相关信息
     */
    private void requestData() {
        final CfindCustomerRequest request = new CfindCustomerRequest();
        request.setKey("user_id");
        request.setKey("reference_id");
        httpsPost(this, request, ReqAction.APPLICATIOIN_FIND_CUSTOMER,
                CfindCustomerResponse.class, new ICallback<CfindCustomerResponse>() {
                    @Override
                    public void success(CfindCustomerResponse cfindCustomerResponse) {
                        CfindCustomerResponse.ResultBean data = cfindCustomerResponse.getResult();
                        int cfindCustomerCode = cfindCustomerResponse.getCode();
                        if (cfindCustomerCode==5031) {
                            showMaintainDialog();
                        } else {
                            bindView(cfindCustomerResponse.getResult());
                        }
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(CreditActivity.this, tips);
                        RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
                    }
                });
    }

    /**
     * 获取时间截
     */
    public void requestFindTime() {
        FindtimeRequest request = new FindtimeRequest();
        httpsPost(this, request, ReqAction.FIND_TIME,
                FindtimeResponse.class, new ICallback<FindtimeResponse>() {
                    @Override
                    public void success(FindtimeResponse findtimeResponse) {
                        FindtimeResponse.ResultBean result = findtimeResponse.getResult();
                        int findtimeCode = findtimeResponse.getCode();
                        if (findtimeCode==5031) {
                            showMaintainDialog();
                        } else {
                            if (result != null) {
                                version_time = result.getVersion_time();
                            }
                            //本地数据（此接口为以后 废弃使用)
                            if (locantime == 0) {
                                locantime = 10;
                                //第一次进入信用卡俱乐部，调用第二个接口，然后保存version_time到本地
                                requestFindKinds();
                            } else if (version_time == 0) {
                                //直接进入业务层
                            } else if (version_time > locan_version_time) {
                                //请求的大于本地，强制升级(先去取kind，存到本地，然后比较版本号)
                                requestFindKinds();
                            } else if (version_time <= locan_version_time) {
                                //请求的小与本地
                                //不是第一次进入app并且后台时间接口有返回数据(直接比较版本号)
                                requestFindKinds();
                            }
                            sp = getSharedPreferences("findtime", Context.MODE_PRIVATE);
                            sp.edit().putLong("version_time", version_time).commit();
                            Log.i("version_time", version_time + "");
                        }
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(CreditActivity.this, tips);
                        RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
                    }
                });
    }

    /**
     * 获取版本号
     */
    private void requestFindKinds() {
        final FindKindsRequest request = new FindKindsRequest();
        request.setPhone_type("A");
        httpsPost(this, request, ReqAction.FIND_KIND,
                FindKindsResponse.class, new ICallback<FindKindsResponse>() {
                    @Override
                    public void success(FindKindsResponse findKindsResponse) {
                        FindKindsResponse.ResultBean result = findKindsResponse.getResult();
                        int findKindsCode = findKindsResponse.getCode();
                        if (findKindsCode == 5031) {
                            showMaintainDialog();
                        } else {
                            if (result != null) {
                                version_id = result.getVersion_id().trim();
                                version_add = result.getVersion_add();
                                locantime = 10;
                                if (version_id != null) {
                                    String request_version_id = version_id;//请求
                                    String locan_version_id = RyxcreditConfig.CLIENTVERSION;//本地
                                    int size = request_version_id.compareTo(locan_version_id);
                                    if (size == 0) {
                                        //相等
                                    } else if (size > 0) {
                                        //t1大于t2
                                        dialog();

                                    } else if (size < 0) {

                                    }
                                }
                            }
                            sp = getSharedPreferences("findtime", Context.MODE_PRIVATE);
                            sp.edit().putString("version_id", locan_version_id).putLong("locantime", locantime).commit();

                        }
                    }
                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(CreditActivity.this, tips);
                        RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
                    }
                });
    }

    //额度审核中
    private AutoLinearLayout lay_under_check;
    //额度陆续开放中
    private AutoLinearLayout lay_opening;
    //额度审核未通过
    private LinearLayout lay_check_denied;
    //重新激活
    private LinearLayout lay_re_apply;
    //正常使用
    private AutoRelativeLayout c_ll_common_use;
    //activation_status未返回,未激活过
    private AutoRelativeLayout lay_has_not_applied;

    private void bindView(CfindCustomerResponse.ResultBean bean) {
        if (bean == null)
            return;
        total_amount = bean.getTotal_amount();
        available_amount = bean.getAvailable_amount();
        max_amount = bean.getMax_amount();
        customer_status = bean.getCustomer_status();
        payment_card_count = bean.getPayment_card_count();
        is_opened = bean.Is_opened();
        is_active = bean.isIs_active();
        is_repaying = bean.Is_repaying();
        is_beoverdue = bean.is_beoverdue();
        active_status = bean.getActive_status();
        activation_status = bean.getActivation_status();
        agreement_id = bean.getAgreement_id();
        agreement_url = bean.getAgreement_url();
        is_agreed = bean.isIs_agreed();
        service_authorize_url = bean.getService_authorize_url();
        is_service = bean.isIs_service();
        appointment_url = bean.getAppointment_url();
        customerStatus = bean.getCustomer_status();
        openTime = bean.getOpen_time();//业务开始时间
        closeTime = bean.getClose_time();//业务关闭时间
        card_activation = bean.getCard_activation();//瑞卡贷激活状态
        business_activation = bean.getBusiness_activation();//现金贷激活状态
        trade_product_url= bean.getTrade_product_url();
        authorise_info_url = bean.getAuthorise_info_url();//信息授权及使用协议
        try {
            openTime = CDateUtil.DateToShortStr(CDateUtil.parseTme(openTime, "yyyyMMdd HHmmss"), "yyyyMMdd HH:mm:ss").substring(8, 14);
            closeTime = CDateUtil.DateToShortStr(CDateUtil.parseTme(closeTime, "yyyyMMdd HHmmss"), "yyyyMMdd HH:mm:ss").substring(8, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }
        avilableAmount = CNummberUtil.parseFloat(bean.getAvailable_amount(), 0.00f);
        tv_work_time.setText("业务时间为当日" + openTime + "-" + closeTime);
        //首页是否审核中、重新激活、显示额度
        /*  N 待认证
            U 正在处理
            R 拒绝
            A 通过
            O 已过期*/
        if ("A".equals(business_activation)||"A".equals(card_activation)) {
            //正常显示额度
            c_ll_common_use.setVisibility(View.VISIBLE);
            lay_under_check.setVisibility(View.GONE);
            lay_opening.setVisibility(View.GONE);
            lay_check_denied.setVisibility(View.GONE);
            lay_re_apply.setVisibility(View.GONE);
            lay_has_not_applied.setVisibility(View.GONE);
            iv_tilerightImg.setVisibility(View.VISIBLE);
            repaymentBtn.setVisibility(View.VISIBLE);
            tvAvailable.withNumber(CNummberUtil.parseFloat(bean.getAvailable_amount(), 0.00f)).start();
            tvTotal.withNumber(CNummberUtil.parseFloat(bean.getTotal_amount(), 0.00f)).start();
        } else if ("U".equals(card_activation) || "U".equals(business_activation)) {
            c_ll_common_use.setVisibility(View.GONE);
            //正在审核
            lay_under_check.setVisibility(View.VISIBLE);
            lay_opening.setVisibility(View.GONE);
            lay_check_denied.setVisibility(View.GONE);
            lay_re_apply.setVisibility(View.GONE);
            lay_has_not_applied.setVisibility(View.GONE);
            iv_tilerightImg.setVisibility(View.VISIBLE);
            repaymentBtn.setVisibility(View.VISIBLE);

            }else if ("U1".equals(card_activation) || "U1".equals(business_activation)) {
                c_ll_common_use.setVisibility(View.GONE);
                //人工正在审核
                lay_under_check.setVisibility(View.VISIBLE);
                lay_opening.setVisibility(View.GONE);
                lay_check_denied.setVisibility(View.GONE);
                lay_re_apply.setVisibility(View.GONE);
                lay_has_not_applied.setVisibility(View.GONE);
                iv_tilerightImg.setVisibility(View.VISIBLE);
                repaymentBtn.setVisibility(View.VISIBLE);
                tv_under_check.setText("您的审批已转至人工处理");
                tv1_under_check.setText("请保持手机畅通~");
        }
        //已过期
        else if ("O".equals(card_activation) || "O".equals(business_activation)) {
            c_ll_common_use.setVisibility(View.GONE);
            lay_under_check.setVisibility(View.VISIBLE);
            lay_opening.setVisibility(View.GONE);
            lay_check_denied.setVisibility(View.GONE);
            //重新激活
            lay_re_apply.setVisibility(View.VISIBLE);
            lay_has_not_applied.setVisibility(View.GONE);
            bt_repayment.setVisibility(View.VISIBLE);
        }
        //激活被拒绝
        else if ("R".equals(card_activation) || "R".equals(business_activation)) {
            c_ll_common_use.setVisibility(View.GONE);
            lay_under_check.setVisibility(View.VISIBLE);
            lay_opening.setVisibility(View.GONE);
            //被拒绝
            lay_check_denied.setVisibility(View.VISIBLE);
            lay_re_apply.setVisibility(View.GONE);
            lay_has_not_applied.setVisibility(View.GONE);
        }
        //未申请
        else if ("N".equals(card_activation) || "N".equals(business_activation)) {
            c_ll_common_use.setVisibility(View.GONE);
            lay_under_check.setVisibility(View.VISIBLE);
            lay_opening.setVisibility(View.GONE);
            lay_check_denied.setVisibility(View.GONE);
            lay_re_apply.setVisibility(View.GONE);
            lay_has_not_applied.setVisibility(View.VISIBLE);
            iv_tilerightImg.setVisibility(View.GONE);
            //最高额度
            tvDefaultMax.withNumber(CNummberUtil.parseFloat(bean.getMax_amount(), 0.00f)).start();
            //当前额度
            //tvDefaultTotal.withNumber(CNummberUtil.parseFloat(bean.getTotal_amount(), 0.00f)).start();
        }
        //暂时不显示提额功能
        if("A".equals(business_activation)){
            tv_promote_amount.setVisibility(View.GONE);
            right_promote_amount.setVisibility(View.GONE);
        }else{
            tv_promote_amount.setVisibility(View.GONE);
            right_promote_amount.setVisibility(View.GONE);
        }

        //用户未签署《俱乐部服务协议》
        if(!is_agreed){
            showAgreeDialog();
        }

    }


    private void initView() {
        borrowBtn = (AutoRelativeLayout) findViewById(R.id.c_borrow_rkd_btn);
        borrowBtn.setOnClickListener(clickListener);
        c_borrow_xjd_btn = (AutoRelativeLayout) findViewById(R.id.c_borrow_xjd_btn);
        c_borrow_xjd_btn.setOnClickListener(clickListener);
        repaymentBtn = (Button) findViewById(c_repayment_btn);
        repaymentBtn.setOnClickListener(clickListener);
        tvAvailable = (CMoneyTextView) findViewById(R.id.c_tv_available_amount);
        tvTotal = (CMoneyTextView) findViewById(R.id.c_tv_total_amount);
        tvDefaultMax = (CMoneyTextView) findViewById(R.id.c_default_tv_max_amount);
        tvDefaultTotal = (CMoneyTextView) findViewById(R.id.c_defaut_tv_available_total);
        lay_under_check = (AutoLinearLayout) findViewById(R.id.lay_under_check);
        lay_opening = (AutoLinearLayout) findViewById(R.id.lay_opening);
        lay_check_denied = (AutoLinearLayout) findViewById(R.id.lay_check_denied);
        lay_re_apply = (AutoLinearLayout) findViewById(R.id.lay_re_apply);
        c_ll_common_use = (AutoRelativeLayout) findViewById(R.id.c_ll_common_use);
        lay_has_not_applied = (AutoRelativeLayout) findViewById(R.id.lay_has_not_applied);
        iv_tilerightImg = (ImageView) findViewById(R.id.tilerightImg);
        bt_repayment = (Button) findViewById(R.id.c_repayment_btn);
        tv_work_time = (TextView) findViewById(R.id.tv_work_time);
    //    recoveryBtn = (Button) findViewById(R.id.c_btn_activate);
    //    recoveryBtn.setOnClickListener(clickListener);
        tv_promote_amount = (TextView) findViewById(R.id.tv_promote_amount);
        right_promote_amount = (ImageView) findViewById(R.id.right_promote_amount);
        tv_promote_amount.setOnClickListener(clickListener);
        right_promote_amount.setOnClickListener(clickListener);
        setRightBtn();
        rightImg.setOnClickListener(clickListener);
        tv_work_time = (TextView) findViewById(R.id.tv_work_time);
        tv_under_check = (TextView) findViewById(R.id.under_check_tv);
        tv1_under_check = (TextView) findViewById(under_check_tv1);
        //第一次进入的时候为新用户

    }

    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            int id = view.getId();
            //还款按钮
            if (id == c_repayment_btn) {
                repaymentAction();
            }
            //瑞易贷产品按钮
            else if (id == R.id.c_borrow_rkd_btn) {
                getRKDUserLevel();
                //瑞卡贷产品按钮
            } else if (id == R.id.c_borrow_xjd_btn) {
                getXJDUserLevel();
            }
            //右上角的按钮
            else if (id == tilerightImg) {
                showMenuPop(view);
            }
            //重新激活
/*            else if (id == R.id.c_btn_activate) {
                //现金贷重新激活
                if("O".equals(business_activation)){
                    recoveryActivate(xjd_user_level,xjd_user_info_level,RyxcreditConfig.xjd_procudtId);
                }
                //瑞卡贷重新激活
                else if("O".equals(card_activation)){
                    recoveryActivate(rkd_user_level,rkd_user_info_level,RyxcreditConfig.rkd_procudtid);
                }

            }*/
            //申请提额
            else if (id == R.id.tv_promote_amount || id == R.id.right_promote_amount) {
                Intent intent = new Intent(CreditActivity.this, PromoteAmountActivity.class);
                intent.putExtra("xjd_user_level",xjd_user_level);
                intent.putExtra("xjd_user_info_level",xjd_user_info_level);
                startActivity(intent);

            }
        }
    };
/*
* 查询用户贷款状态
* */
    private void getPriceInfo() {
        final PriceinfoRequest request = new PriceinfoRequest();
        httpsPost(this, request, ReqAction.PRICE_INFO,
                PriceinfoResponse.class,new ICallback<PriceinfoResponse>() {
                    @Override
                    public void success(PriceinfoResponse priceinfoResponse) {
                        PriceinfoResponse.ResultBean priceResult =  priceinfoResponse.getResult();
                        int priceinfoCode = priceinfoResponse.getCode();
                        if (priceinfoCode==5031) {
                            showMaintainDialog();
                        } else {
                            if (priceResult != null) {
                                is_into = priceResult.getIs_into();
                            }
                        }
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(CreditActivity.this, tips);
                        RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
                    }
                });

    }

    /**
     * 显示用户同意协议对话框
     */
    public void showAgreeDialog() {
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            protected void onBuildDone(final Dialog dialog) {
                WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                dialog.setCanceledOnTouchOutside(false);
                WebView webView = (WebView) dialog.findViewById(R.id.c_wv_agree);
                webView.loadUrl(agreement_url);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                dialog.layoutParams(dm.widthPixels - CDensityUtil.dip2px(CreditActivity.this, 32), ViewGroup.LayoutParams.WRAP_CONTENT);
                CRyxSlideUnlock cRyxSlideUnlock = (CRyxSlideUnlock) dialog.findViewById(R.id.c_slide_unlock);
                cRyxSlideUnlock.setListen(new CRyxSlideUnlock.ISlideToEnd() {
                    @Override
                    public void slideToEnd() {
                        agreement();
                        dialog.dismiss();
                    }
                });
            }
        };
        builder.contentView(R.layout.c_dialog_credit_enter).build(this).show();
    }

    private String agreementId = "1";//俱乐部服务协议
    /**
     * 同意非正式协议请求
     */
    private void agreement() {
        AgreementRequest request = new AgreementRequest();
        request.setAgreement_id(agreementId);
        httpsPost(this, request, ReqAction.APPLICATION_AGREEMENT_AGREE, AgreementResponse.class,
                new ICallback<AgreementResponse>() {
                    @Override
                    public void success(AgreementResponse agreementResponse) {

                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(CreditActivity.this, tips);
                        RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
                    }
                });
    }

    //右上角的按钮：选择跳转，资料管理页面，借还记录页面
    private void showMenuPop(View menuView) {
        int location[] = new int[2];
        menuView.getLocationOnScreen(location);
        int y = location[1] + menuView.getHeight();

        PopModel feedPopModel = new PopModel();
        feedPopModel.setDrawableId(R.drawable.pop_info);
        feedPopModel.setItemDesc("资料管理");

        PopModel messagePopMode = new PopModel();
        //如果设置了图标，则会显示，否则不显示
        messagePopMode.setDrawableId(R.drawable.pop_table);
        messagePopMode.setItemDesc("借还记录");

        /** 初始化数据源 **/
        final List<PopModel> list = new ArrayList<>();
        list.add(feedPopModel);
        list.add(messagePopMode);

        PopCommon popCommon = new PopCommon(this, list, new PopCommon.OnPopCommonListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if("U".equals(business_activation)||"U".equals(card_activation)){
                        CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中！");
                        return;
                    }
                    toActivePage();
                } else if (position == 1) {
                    borrowRecordsAction();
                }
            }

            @Override
            public void onDismiss() {

            }
        });
        /** 是否显示黑色背景，默认不显示 **/
        popCommon.setShowAplhaWindow(false);
        popCommon.showPop(menuView, dp2px(getApplicationContext(), 5), y);
    }

    /**
     * 重新激活：
     *          如果瑞卡贷需要重新激活，传瑞卡贷的产品信息，
     *          如果现金贷需要重新激活，传现金贷的产品信息
     */
    private void recoveryActivate(String user_level,String userinfo_level,String procudtId) {
        Intent intent = new Intent(this, BaseInfoSuccesActivity.class);
        intent.putExtra("isActive", active_status);
        intent.putExtra("user_level",user_level);
        intent.putExtra("user_info_level", userinfo_level);
        intent.putExtra("product_id", procudtId);
        startActivity(intent);
    }

    //资料编辑页面
    private void toActivePage() {
        getFindBaseData();

    }

    /**
     * 借款记录
     */
    private void borrowRecordsAction() {
        Intent intent = new Intent(this, MultiBorrowRecordsActivity.class);
        intent.putExtra("is_opened", is_opened);
        intent.putExtra("unborrowTime", closeTime + "-" + openTime);
        startActivity(intent);
    }

    /**
     * 快速还款
     */
    private void repaymentAction() {
        Intent intent = new Intent(this, XJDQuickrepaymentActivity.class);
        intent.putExtra("flag", "Repayment");
        intent.putExtra("is_opened", is_opened);
        intent.putExtra("unborrowTime", closeTime + "-" + openTime);
        startActivity(intent);
    }

    //现金贷用户级别，客户资料级别
    private String xjd_user_level, xjd_user_info_level;
    //瑞卡贷用户级别，客户资料级别
    private String rkd_user_level, rkd_user_info_level;

    //获取瑞卡贷用户级别
    private void getXJDUserLevel() {
        final UserLevelRequest request = new UserLevelRequest();
        request.setProduct(RyxcreditConfig.xjd_procudtId);
        request.setType("4");
        httpsPost(this, request, ReqAction.USER_LEVEL, UserLevelResponse.class,
                new ICallback<UserLevelResponse>() {

            @Override
            public void success(UserLevelResponse userleverResponse) {
                UserLevelResponse.ResultBean result = userleverResponse.getResult();
                int userleverCode = userleverResponse.getCode();
                if (userleverCode==5031) {
                    showMaintainDialog();
                } else {
                    if (result != null) {
                        xjd_user_level = result.getUser_level();
                        xjd_user_info_level = result.getUser_info_level();
                        if (("N".equals(business_activation)) || ("O".equals(business_activation))) {

                            //现金贷,白名单、vip、员工、标准用户可以进激活
                            if (!(("V".equals(xjd_user_level) || "W".equals(xjd_user_level)
                                    || "R".equals(xjd_user_level) || "P".equals(xjd_user_level)))) {
                                CLogUtil.showToast(CreditActivity.this, "额度陆续开放中!");
                                return;
                            }
                            //如果瑞卡贷激活正在审核中，不可以重新激活现金贷
                            if(("U".equals(card_activation))){
                                CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中！");
                                return;
                            }
                            if(("U1".equals(card_activation))){
                                CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中！");
                                return;
                            }
                            //判断逾期并且逾期
                            if(is_beoverdue==true){
                                CLogUtil.showToast(CreditActivity.this,"您当前有逾期状态的借款，成功还款后次日可激活使用!");
                                return;
                            }
                            //Intent intent = new Intent(CreditActivity.this, BaseInfoSuccesActivity.class);
                            Intent intent = new Intent(CreditActivity.this, RKDQuotaActivity.class);
                            intent.putExtra("user_level", xjd_user_level);
                            intent.putExtra("user_info_level", xjd_user_info_level);
                            intent.putExtra("product_id", RyxcreditConfig.xjd_procudtId);
                            intent.putExtra("trade_product_url",trade_product_url);
                            intent.putExtra("authorise_info_url",authorise_info_url);
                            startActivity(intent);
                        } else if ("R".equals(business_activation)) {
                            CLogUtil.showToast(CreditActivity.this, "亲，很抱歉，您申请的瑞卡贷业务因综合评分不足审核未通过，感谢关注瑞卡贷。");
                        } else if("A".equals(business_activation)){
                            if(is_beoverdue){
                                CLogUtil.showToast(CreditActivity.this,"您有逾期记录，暂不可借款!");
                                return;
                            }
                            //getPriceInfo();
                            if(String.valueOf(is_into)!=null&&!"".equalsIgnoreCase(String.valueOf(is_into))) {
                                if (is_into == false) {
                                    Intent intent = new Intent(CreditActivity.this, XJDBorrowMoneyActivity.class);
                                    intent.putExtra("avilableAmount", avilableAmount);
                                    intent.putExtra("is_opened", is_opened);
                                    intent.putExtra("unborrowTime", closeTime + "-" + openTime);
                                    startActivity(intent);
                                } else {
                                    CLogUtil.showToast(CreditActivity.this,
                                            "亲，您有一笔借款正在审核中，请等待审核结果后再发起新的借款，谢谢!");
                                }
                            }
                        }else if(("U".equals(business_activation))){
                            CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中！");
                        }else if(("U1".equals(business_activation))){
                            CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中！");
                        }else {
                            CLogUtil.showToast(CreditActivity.this, "您的账户，暂不可使用!");
                        }
                    }
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(CreditActivity.this,tips);
                RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
            }
        });
    }

    //获取瑞易贷用户级别
    private void getRKDUserLevel() {
        final UserLevelRequest request = new UserLevelRequest();
        request.setProduct(RyxcreditConfig.rkd_procudtid);
        request.setType("4");
        httpsPost(this, request, ReqAction.USER_LEVEL, UserLevelResponse.class, new ICallback<UserLevelResponse>() {
            @Override
            public void success(UserLevelResponse userleverResponse) {
                UserLevelResponse.ResultBean result = userleverResponse.getResult();
                int userleverCode = userleverResponse.getCode();
                if (userleverCode==5031) {
                    showMaintainDialog();
                } else {
                    if (result != null) {
                        rkd_user_level = result.getUser_level();
                        rkd_user_info_level = result.getUser_info_level();
                        //现金贷激活成功，瑞卡贷可以直接借款
                        if ("A".equals(business_activation)) {
                            if (is_beoverdue) {
                                CLogUtil.showToast(CreditActivity.this, "您有逾期记录，暂不可借款!");
                                return;

                            }

                            if (String.valueOf(is_into) != null && !"".equalsIgnoreCase(String.valueOf(is_into))) {
                                if (is_into == false) {
                                    Intent intent = new Intent(CreditActivity.this, RYDBorrowMoneyActivity.class);
                                    intent.putExtra("avilableAmount", avilableAmount);
                                    intent.putExtra("is_opened", is_opened);
                                    intent.putExtra("unborrowTime", closeTime + "-" + openTime);
                                    startActivity(intent);
                                } else {
                                    CLogUtil.showToast(CreditActivity.this,
                                            "亲，您有一笔借款正在审核中，请等待审核结果后再发起新的借款，谢谢!");
                                }

                            }
                        } else if (("N".equals(card_activation)) || ("O".equals(card_activation))) {

                            //瑞卡贷，白名单、vip、员工、标准用户可以进激活
                            if (!(("V".equals(rkd_user_level) || "W".equals(rkd_user_level)
                                    || "R".equals(rkd_user_level) || "P".equals(rkd_user_level)))) {
                                CLogUtil.showToast(CreditActivity.this, "额度陆续开放中!");
                                return;
                            }
                            //如果现金贷激活正在审核中，不可以重新激活瑞卡贷
                            if (("U".equals(business_activation))) {
                                CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中！");
                                return;
                            }
                            if (("U1".equals(business_activation))) {
                                CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中！");
                                return;
                            }
                            if (is_beoverdue == true) {
                                CLogUtil.showToast(CreditActivity.this, "您当前有逾期状态的借款，成功还款后次日可激活使用!");
                                return;
                            }
                            //未激活，跳转激活页面
                            Intent intent = new Intent(CreditActivity.this, RYDBaseInfoSuccesActivity.class);
                            intent.putExtra("user_level", rkd_user_level);
                            intent.putExtra("user_info_level", rkd_user_info_level);
                            intent.putExtra("product_id", RyxcreditConfig.rkd_procudtid);
                            intent.putExtra("service_authorize_url", service_authorize_url);
                            startActivity(intent);
                        } else if ("R".equals(card_activation)) {
                            CLogUtil.showToast(CreditActivity.this,
                                    "亲，很抱歉，您申请的瑞易贷业务因综合评分不足审核未通过，感谢关注瑞易贷。");
                        } else if ("A".equals(card_activation)) {
                            if (is_beoverdue) {
                                CLogUtil.showToast(CreditActivity.this, "您有逾期记录，暂不可借款!");
                                return;
                            }
                            //  getPriceInfo();
                            if (String.valueOf(is_into) != null && !"".equalsIgnoreCase(String.valueOf(is_into))) {
                                if (is_into == false) {
                                    Intent intent = new Intent(CreditActivity.this, RYDBorrowMoneyActivity.class);
                                    intent.putExtra("avilableAmount", avilableAmount);
                                    intent.putExtra("is_opened", is_opened);
                                    intent.putExtra("unborrowTime", closeTime + "-" + openTime);
                                    startActivity(intent);
                                } else {
                                    CLogUtil.showToast(CreditActivity.this, "亲，您有一笔借款正在审核中，请等待审核结果后再发起新的借款，谢谢!");
                                }
                            }
                        } else if (("U".equals(card_activation))) {
                            CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中!");
                        } else if (("U1".equals(card_activation))) {
                            CLogUtil.showToast(CreditActivity.this, "您的额度正在审核中!");
                        } else {
                            CLogUtil.showToast(CreditActivity.this, "您的账户，暂不可使用!");
                        }
                    }
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(CreditActivity.this,tips);
                RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
            }
        });
    }

    public void dialog() {
        new AlertDialog.Builder(CreditActivity.this).setTitle("升级提示")//设置对话框标题
                .setMessage("是否跳转深圳瑞银信官网下载最新APP！")//设置显示的内容
                .setCancelable(false)//按对话框以外的地方不起作用。按返回键也不起作用
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        if(getPackageName().equalsIgnoreCase("com.ryx.payment.ruishua")){
                           // uri = Uri.parse("http://www.ruiyinxin.com/ruishua/");
                            uri = Uri.parse(version_add);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            finish();
                        }else if(getPackageName().equalsIgnoreCase("com.ryx.payment.ruiyinxin")){
                           // uri = Uri.parse("http://www.ruiyinxin.com/ruishua/");
                            uri = Uri.parse(version_add);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            finish();
                        }else if(getPackageName().equalsIgnoreCase("com.ryx.payment.newrs")){
                           // uri = Uri.parse("http://www.ruiyinxin.com/ruishua/
                            uri = Uri.parse(version_add);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            finish();
                        }else if(getPackageName().equalsIgnoreCase("com.ryx.payment.ruihebao")){
                           // uri = Uri.parse("http://www.ruiyinxin.com/ruihebao/");
                            uri = Uri.parse(version_add);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            finish();
                        }else if(getPackageName().equalsIgnoreCase("com.ryx.payment.bank")){
                           // uri = Uri.parse("http://www.ruiyinxin.com/ruiyishenghuoshanghu/");
                            uri = Uri.parse(version_add);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            finish();
                        }
                    }

                }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件

                        finish();
                    }

                }).show();//在按键响应事件中显示此对话框

    }
    //进入老板还是员工的资料管理
    private void getFindBaseData() {
        final FindBaseDataRequest request = new FindBaseDataRequest();
        httpsPost(this, request, ReqAction.FIND_BASEDATA, FindBaseDataResponse.class, new ICallback<FindBaseDataResponse>() {

            @Override
            public void success(FindBaseDataResponse findBaseDataResponse) {
                FindBaseDataResponse.ResultBean result = findBaseDataResponse.getResult();
                if (result != null) {
                    bossInfo = result.getBossInfo();
                    if (bossInfo!=null){
                        String bossInfo = "L";
                        Intent intent = new Intent(CreditActivity.this, ActivateLineActivity.class);
                        intent.putExtra("bossInfo","L");
                        startActivity(intent);
                    }else {
                        String bossInfo = "Y";
                        Intent intent = new Intent(CreditActivity.this, ActivateLineActivity.class);
                        intent.putExtra("bossInfo","Y");
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(CreditActivity.this,tips);
                RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
            }
        });
    }

}