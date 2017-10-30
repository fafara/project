package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.findcustomer.CfindCustomerRequest;
import com.ryx.ryxcredit.beans.bussiness.findcustomer.CfindCustomerResponse;
import com.ryx.ryxcredit.beans.bussiness.home.CfindHomeRouteResponse;
import com.ryx.ryxcredit.beans.bussiness.product.CfindRouteRequest;
import com.ryx.ryxcredit.ryd.BorrowRecordsActivity;
import com.ryx.ryxcredit.ryd.RepaymentActivity;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.widget.CMoneyTextView;

public class CreditActivity extends BaseActivity {

    //我要借款
    private com.rey.material.widget.Button borrowBtn, repaymentBtn, borrowRecordsBtn;
    //总额度，可用金额
    private CMoneyTextView tvTotal, tvAvailable;
    private ImageView edtImg;
    //额度陆续开放中
    private LinearLayout creditOpeningLay;
    //额度受限
    private LinearLayout limitOfCreditLay;
    //信用评级未通过
    private LinearLayout estimateNotGoneLay;
    //重新激活
    private LinearLayout recoveryCreditLay;
    //正常使用
    private LinearLayout commonUseLay;
    //重新激活按钮
    private com.rey.material.widget.Button recoveryBtn;
    private int customerStatus;
    //是否还款中
    private boolean isPlaying;
    private double avilableAmount;
    private boolean is_opened;
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

    private String activation_status;//激活状态（U:正在处理,R拒绝,A:通过,N:签署：审核通过 ）
    private int active_status;//激活状态（未激活；激活已过期；激活未过期）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_credit);
        RyxcreditConfig.context = this;
        setTitleLayout("瑞卡贷", true, false);
        initView();
        setbottomMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRoute();
    }

    //请求激活页路由接口
    private void getRoute() {
        final CfindRouteRequest request = new CfindRouteRequest();
        request.setKey("home_page");
        httpsPost(this, request, ReqAction.APPLICATION_ROUTE, CfindHomeRouteResponse.class, new ICallback<CfindHomeRouteResponse>() {
            @Override
            public void success(CfindHomeRouteResponse routeResponse) {
                CfindHomeRouteResponse.ResultBean result = routeResponse.getResult();
                if (result != null) {
                    paymentStatus = result.getPayment_status();
                    paymentType = result.getPayment_type();
                    paymentText = result.getPayment_text();

                    repaymentStatus = result.getRepayment_status();
                    repaymentType = result.getRepayment_type();
                    repaymentText = result.getRepayment_text();

                    loanRecordStatus = result.getLoan_record_status();
                    loanRecordType = result.getLoan_record_type();
                    loanRecordText = result.getLoan_record_text();

                    ownDataStatus = result.getOwn_data_status();
                    ownDataType = result.getOwn_data_type();
                    ownDataText = result.getOwn_data_text();

                    initBtnClick();
                    requestData();
                }
            }

            @Override
            public void failture(String tips) {
            }
        });
    }

    //初始化按钮状态
    private void initBtnClick() {
        if ("0".equals(paymentStatus)) {
            borrowBtn.setVisibility(View.GONE);
        } else if ("1".equals(paymentStatus) || "3".equals(paymentStatus)) {
            borrowBtn.setVisibility(View.VISIBLE);
        }

        if ("0".equals(repaymentStatus)) {
            repaymentBtn.setVisibility(View.GONE);
        } else if ("1".equals(repaymentStatus) || "3".equals(repaymentStatus)) {
            repaymentBtn.setVisibility(View.VISIBLE);
        }

        if ("0".equals(loanRecordStatus)) {
            borrowRecordsBtn.setVisibility(View.GONE);
        } else if ("1".equals(loanRecordStatus) || "3".equals(loanRecordStatus)) {
            borrowRecordsBtn.setVisibility(View.VISIBLE);
        }

        if ("0".equals(ownDataStatus)) {
            rightImg.setVisibility(View.GONE);
        } else if ("1".equals(ownDataStatus) || "3".equals(ownDataStatus)) {
            rightImg.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取首页相关信息
     */
    private void requestData() {
        CfindCustomerRequest request = new CfindCustomerRequest();
        httpsPost(this, request, ReqAction.APPLICATIOIN_FIND_CUSTOMER,
                CfindCustomerResponse.class, new ICallback<CfindCustomerResponse>() {
                    @Override
                    public void success(CfindCustomerResponse cfindCustomerResponse) {
                        bindView(cfindCustomerResponse.getResult());
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(CreditActivity.this, tips);
                    }
                });
    }



    private void bindView(CfindCustomerResponse.ResultBean bean) {
        if (bean == null)
            return;
        active_status =bean.getActive_status();
        customerStatus = bean.getCustomer_status();
        activation_status = bean.getActivation_status();
        isPlaying = bean.Is_repaying();
        is_opened = bean.Is_opened();
        openTime = bean.getOpen_time();//业务开始时间
        closeTime = bean.getClose_time();//业务关闭时间
        isOverdue = bean.is_beoverdue();
        try {
            openTime = CDateUtil.DateToShortStr(CDateUtil.parseTme(openTime, "yyyyMMdd HHmmss"), "yyyyMMdd HH:mm:ss").substring(8, 14);
            closeTime = CDateUtil.DateToShortStr(CDateUtil.parseTme(closeTime, "yyyyMMdd HHmmss"), "yyyyMMdd HH:mm:ss").substring(8, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }
        avilableAmount = CNummberUtil.parseDouble(bean.getAvailable_amount(), 0);
        //首页是否审核中、重新激活、显示额度
        //额度审核通过
        if("A".equals(activation_status)){
            //需要重新激活的流程
            if(active_status==1){
                commonUseLay.setVisibility(View.GONE);
                recoveryCreditLay.setVisibility(View.VISIBLE);
                estimateNotGoneLay.setVisibility(View.GONE);
                limitOfCreditLay.setVisibility(View.GONE);
                creditOpeningLay.setVisibility(View.GONE);
            }
            //额度正在开放中
            else if(active_status==0){
                commonUseLay.setVisibility(View.GONE);
                recoveryCreditLay.setVisibility(View.GONE);
                estimateNotGoneLay.setVisibility(View.GONE);
                limitOfCreditLay.setVisibility(View.GONE);
                creditOpeningLay.setVisibility(View.VISIBLE);
            }
            //显示额度
            else{
                commonUseLay.setVisibility(View.VISIBLE);
                estimateNotGoneLay.setVisibility(View.GONE);
                recoveryCreditLay.setVisibility(View.GONE);
                limitOfCreditLay.setVisibility(View.GONE);
                creditOpeningLay.setVisibility(View.GONE);
                tvAvailable.withNumber(CNummberUtil.parseFloat(bean.getAvailable_amount(), 0.00f)).start();
                tvTotal.withNumber(CNummberUtil.parseFloat(bean.getTotal_amount(), 0.00f)).start();
            }
        }
        //激活请求正在处理
        else if("U".equals(activation_status)){
            commonUseLay.setVisibility(View.GONE);
            recoveryCreditLay.setVisibility(View.GONE);
            estimateNotGoneLay.setVisibility(View.GONE);
            limitOfCreditLay.setVisibility(View.VISIBLE);
            creditOpeningLay.setVisibility(View.GONE);
        }
        //激活被拒绝
        else if("R".equals(activation_status)){
            commonUseLay.setVisibility(View.GONE);
            recoveryCreditLay.setVisibility(View.GONE);
            estimateNotGoneLay.setVisibility(View.VISIBLE);
            limitOfCreditLay.setVisibility(View.GONE);
            creditOpeningLay.setVisibility(View.GONE);
        }


        //额度正常使用
//        if ((active_status & 3) == 3 && ("A".equals(activation_status)
//                || "N".equals(activation_status))) {
//            commonUseLay.setVisibility(View.VISIBLE);
//            estimateNotGoneLay.setVisibility(View.GONE);
//            recoveryCreditLay.setVisibility(View.GONE);
//            limitOfCreditLay.setVisibility(View.GONE);
//            tvAvailable.withNumber(CNummberUtil.parseFloat(bean.getAvailable_amount(), 0.00f)).start();
//            tvTotal.withNumber(CNummberUtil.parseFloat(bean.getTotal_amount(), 0.00f)).start();
//        }
//        //需要重新激活的流程
//        else if ((active_status & 3) == 1)
//        {
//            commonUseLay.setVisibility(View.GONE);
//            recoveryCreditLay.setVisibility(View.VISIBLE);
//            estimateNotGoneLay.setVisibility(View.GONE);
//            limitOfCreditLay.setVisibility(View.GONE);
//        }
//        //激活被拒绝
//        else if ("R".equals(activation_status)) {
//            commonUseLay.setVisibility(View.GONE);
//            recoveryCreditLay.setVisibility(View.GONE);
//            estimateNotGoneLay.setVisibility(View.VISIBLE);
//            limitOfCreditLay.setVisibility(View.GONE);
//        }
//        //激活请求正在处理
//        else if ("U".equals(activation_status)) {
//            commonUseLay.setVisibility(View.GONE);
//            recoveryCreditLay.setVisibility(View.GONE);
//            estimateNotGoneLay.setVisibility(View.GONE);
//            limitOfCreditLay.setVisibility(View.VISIBLE);
//        }
    }

    private void initView() {

        borrowBtn = (com.rey.material.widget.Button) findViewById(R.id.c_borrow_money_btn);
        borrowBtn.setOnClickListener(clickListener);

        repaymentBtn = (com.rey.material.widget.Button) findViewById(R.id.c_repayment_btn);
        repaymentBtn.setOnClickListener(clickListener);

        borrowRecordsBtn = (com.rey.material.widget.Button) findViewById(R.id.c_borrow_records_btn);
        borrowRecordsBtn.setOnClickListener(clickListener);

        tvAvailable = (CMoneyTextView) findViewById(R.id.c_tv_available_amount);
        tvTotal = (CMoneyTextView) findViewById(R.id.c_tv_total_amount);

        limitOfCreditLay = (LinearLayout) findViewById(R.id.llay_limit_of_credit);
        estimateNotGoneLay = (LinearLayout) findViewById(R.id.llay_estimate_not_gone);
        recoveryCreditLay = (LinearLayout) findViewById(R.id.c_lly_activate_recovery);
        commonUseLay = (LinearLayout) findViewById(R.id.c_ll_common_use);
        creditOpeningLay = (LinearLayout) findViewById(R.id.llay_credit_opening);

        recoveryBtn = (com.rey.material.widget.Button) findViewById(R.id.c_btn_activate);
        recoveryBtn.setOnClickListener(clickListener);

        setRightBtn();
        rightImg.setOnClickListener(clickListener);
    }

    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            int id = view.getId();
            //借还记录按钮
            if (id == R.id.c_borrow_records_btn) {
                if ("1".equals(loanRecordStatus)) {
                    if ("TEXT".equals(loanRecordType)) {
                        CLogUtil.showToast(CreditActivity.this, loanRecordText);
                    }
                } else if ("3".equals(loanRecordStatus)) {
                    if ("TEXT".equals(loanRecordType)) {
                        CLogUtil.showToast(CreditActivity.this, loanRecordText);
                    } else if ("PAGE".equals(loanRecordType)) {
                        if ("loan_record_page".equals(loanRecordText)) {
                            borrowRecordsAction();//借还记录
                        }
                    }
                }
            }
            //还款按钮
            else if (id == R.id.c_repayment_btn) {
                if ("1".equals(repaymentStatus)) {
                    if ("TEXT".equals(repaymentType)) {
                        CLogUtil.showToast(CreditActivity.this, repaymentText);
                    }
                } else if ("3".equals(repaymentStatus)) {
                    if ("TEXT".equals(repaymentType)) {
                        CLogUtil.showToast(CreditActivity.this, repaymentText);
                    } else if ("PAGE".equals(repaymentType)) {
                        if ("repayment_page".equals(repaymentText)) {
                            repaymentAction();//我要还款
                        }
                    }
                }
            }
            //借款按钮
            else if (id == R.id.c_borrow_money_btn) {
                //如果是黑名单或者不是vip
//                if (((customerStatus&1)==1) || ((customerStatus&6)==0)) {
//                    CCommonDialog.showRepaymentError(CreditActivity.this, "暂不可借款", "您暂未达到借款标准！");
//                    return;
//                }
//                if (isPlaying) {
//                    CCommonDialog.showRepaymentError(CreditActivity.this, "暂不可借款", "您的贷款尚未还清，暂不可以借款！");
//                    return;
//                }
//                if(isOverdue){
//                    CCommonDialog.showRepaymentError(CreditActivity.this, "暂不可借款", "您有一笔借款已逾期，请还清后再尝试借款！");
//                    return;
//                }
//                if ((isActive & 3) != 3) {
//                    CCommonDialog.showRepaymentError(CreditActivity.this, "暂不可借款", "您暂未达到借款标准！");
//                    return;
//                }
                if ("1".equals(paymentStatus)) {
                    if ("TEXT".equals(paymentType)) {
                        CLogUtil.showToast(CreditActivity.this, paymentText);
                    }
                } else if ("3".equals(paymentStatus)) {
                    if ("TEXT".equals(paymentType)) {
                        CLogUtil.showToast(CreditActivity.this, paymentText);
                    } else if ("PAGE".equals(paymentType)) {
                        if ("payment_page".equals(paymentText)) {
                            borrowMoneyAction();//我要借款
                        }
                    }
                }
            }
            //激活资料编辑
            else if (id == R.id.tilerightImg) {
                if ("1".equals(ownDataStatus)) {
                    if ("TEXT".equals(ownDataType)) {
                        CLogUtil.showToast(CreditActivity.this, ownDataText);
                    }
                } else if ("3".equals(ownDataStatus)) {
                    if ("TEXT".equals(ownDataType)) {
                        CLogUtil.showToast(CreditActivity.this, ownDataText);
                    } else if ("PAGE".equals(ownDataType)) {
                        if ("date_message_page".equals(ownDataText) || "active_page".equals(ownDataText)) {
                            Intent intent = new Intent(CreditActivity.this, ActivateLineActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
            //重新激活
            else if (id == R.id.c_btn_activate) {
                recoveryActivate();
            }
        }
    };

    /**
     * 调整图片顶部位置
     *
     * @param button
     */
    private void setButtonImageLoction(com.rey.material.widget.Button button) {
        Rect rect = new Rect();
        rect.set(0, 30, 150, 150);
        Drawable drawableBorrow = button.getCompoundDrawables()[1];
        drawableBorrow.setBounds(rect);
        button.setCompoundDrawables(null, drawableBorrow, null, null);
    }

    /**
     * 重新激活
     */
    private void recoveryActivate() {
        Intent intent = new Intent(this, ActivateLineActivity.class);
        intent.putExtra("isActive", active_status);
        startActivity(intent);
    }

    /**
     * 借款记录
     */
    private void borrowRecordsAction() {
        Intent intent = new Intent(this, BorrowRecordsActivity.class);
        intent.putExtra("is_opened", is_opened);
        intent.putExtra("unborrowTime", closeTime + "-" + openTime);
        startActivity(intent);
    }

    /**
     * 还款
     */
    private void repaymentAction() {
        Intent intent = new Intent(this, RepaymentActivity.class);
        intent.putExtra("flag", "Repayment");
        intent.putExtra("is_opened", is_opened);
        intent.putExtra("unborrowTime", closeTime + "-" + openTime);
        startActivity(intent);
    }

    /**
     * 借款
     */
    private void borrowMoneyAction() {
        Intent intent = new Intent(this, BorrowingMoneyActivity.class);
        intent.putExtra("avilableAmount", avilableAmount);
        intent.putExtra("is_opened", is_opened);
        intent.putExtra("unborrowTime", closeTime + "-" + openTime);
        startActivity(intent);
    }


}
