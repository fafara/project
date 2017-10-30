package com.ryx.ryxcredit.xjd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.AgreementActivity;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailRequest;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.ChangeDkCardRequest;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentResponse;
import com.ryx.ryxcredit.beans.bussiness.loanrepay.LoanRepayResponse;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CMoneyEncoder;
import com.ryx.ryxcredit.utils.CNummberConvertUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.utils.CStringUnit;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordDetailReponse;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;


/*
* 现金贷借还详情页面
* */
public class MultiBorrowDetailActivity extends BaseActivity {

    private String totalAmout, payCardno, repayCardno, loanAmont, loadDate;
    private String contract_id;

    //借款金额
    private TextView c_tv_borrow_amount;
    //收款账户（储蓄卡）
    private TextView c_sure_borrow_shoukuan_account;
    private ImageView c_sure_borrow_shoukuan_iv;

    //还款账户（储蓄卡）
    private TextView c_sure_borrow_huankuan_account;
    private ImageView c_sure_borrow_huankuan_iv;
    //    起止日期
    private TextView c_start_dead_date_tv;
    //    借款期限
    private TextView tv_borrowterm;
    // 还款日期
    private TextView c_tv_repay_date;
    // 借款协议
    private TextView tv_contact;
    private AutoRelativeLayout lay_contract;
    //    更换还款卡
    private ImageView c_changed_huankuan_iv;
    private TextView tv_change_hkcardNo;
    private TextView tv_changeCard;

    private TextView tv_repayPlan;
    private MultiBorrowRecordDetailReponse.ResultBean detailResponse;
    private TextView tv_show_repaylist;

    private int repayStatus;
    private String agreementUrl;
    private String spanUnitStr = "";

    private String repaymentBankCode;

    private Button c_reapy_btn;
    private AutoRelativeLayout lay_changerepay_card;
    private boolean isPayed;//true:已结清 false：待还款
    private String product_descr;//产品名称
    private String money_manage_url;
    private String contract_url;
    private String[] beginDateSplit = new String[]{"", "", ""};
    private String[] endDateSplit = new String[]{"", "", ""};
    private boolean is_opened;

    @Override
    protected void  onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_xjd_borrow_detail);
        setTitleLayout("借款详情", true, false);
        if (getIntent() != null) {
            if(getIntent().hasExtra("contract_id"))
            contract_id = getIntent().getStringExtra("contract_id");
            if(getIntent().hasExtra("isPayed"))
                isPayed = getIntent().getBooleanExtra("isPayed",false);
            if(getIntent().hasExtra("product_descr"))
                product_descr = getIntent().getStringExtra("product_descr");
            CLogUtil.showLog("product_descr---",product_descr+"---");
            if(getIntent().hasExtra("is_opened"))
                is_opened = getIntent().getBooleanExtra("is_opened",false);
        }
        getDetailData();
    }

    private void getDetailData() {
        BorrowRecordDetailRequest request = new BorrowRecordDetailRequest();
        request.setContract_id(contract_id);
        httpsPost(this, request, ReqAction.APPLICATION_BORROW_RECORD_DETAIL, MultiBorrowRecordDetailReponse.class, new ICallback<MultiBorrowRecordDetailReponse>() {
            @Override
            public void success(MultiBorrowRecordDetailReponse borrowRecordDetailResponse) {
                detailResponse = borrowRecordDetailResponse.getResult();
                int borrowRecordDetailCode = borrowRecordDetailResponse.getCode();
                if (borrowRecordDetailCode==5031) {
                    showMaintainDialog();
                } else {
                    if (detailResponse != null) {
                        repayStatus = detailResponse.getStatus();
                        agreementUrl = detailResponse.getAgreement_url();
                        money_manage_url = detailResponse.getMoney_manage_url();
                        contract_url = detailResponse.getContract_url();
                        bindView(detailResponse);
                    }
                }
            }

            @Override
            public void failture(String tips) {

            }
        });
    }

    private String repayBankNum;
    private String loanDateTime, expiredDateTime;

    private void bindView(MultiBorrowRecordDetailReponse.ResultBean borrowRecordDetailResponse) {
        initViews();
        if(borrowRecordDetailResponse==null)
            return;
        //状态
        String loanStatus = borrowRecordDetailResponse.getLoan_status();
        //已逾期
        if ("B".equals(loanStatus)) {
            c_reapy_btn.setVisibility(View.VISIBLE);
            lay_changerepay_card.setVisibility(View.VISIBLE);
            lay_contract.setVisibility(View.VISIBLE);
        }
        //已结清
        else if ("A".equals(loanStatus)) {
            c_reapy_btn.setVisibility(View.INVISIBLE);
            lay_changerepay_card.setVisibility(View.GONE);
            lay_contract.setVisibility(View.VISIBLE);
        }
        //放款中
        else if ("F".equals(loanStatus) || "G".equals(loanStatus) || "H".equals(loanStatus)) {
            c_reapy_btn.setVisibility(View.INVISIBLE);
            lay_contract.setVisibility(View.VISIBLE);
            lay_changerepay_card.setVisibility(View.GONE);
        }
        //使用中
        else {
            c_reapy_btn.setVisibility(View.VISIBLE);
            lay_contract.setVisibility(View.VISIBLE);
            lay_changerepay_card.setVisibility(View.VISIBLE);
        }
//        statusTv.setText(CConstants.getLoanStatus(loanStatus));
        //借款金额
        c_tv_borrow_amount.setText(CMoneyEncoder.EncodeFormat(String.valueOf(borrowRecordDetailResponse.getTotal_amount())));
//        double loanAmout = borrowRecordDetailResponse.getTotal_amount() - borrowRecordDetailResponse.getCurrent_cost_amount();
        repayBankNum = borrowRecordDetailResponse.getRepayment_card_num();
        //还款账户
        c_sure_borrow_huankuan_account.setText(CStringUnit.cardJiaMi(borrowRecordDetailResponse.getRepayment_card_num()));
        //收款账户
        c_sure_borrow_shoukuan_account.setText(CStringUnit.cardJiaMi(borrowRecordDetailResponse.getPayment_card_num()));
        //起止日期
        loanDateTime = CDateUtil.DateToStrForRecord(
                CDateUtil.parseDate(borrowRecordDetailResponse.getLoan_date(), "yyyyMMdd"));
        expiredDateTime = CDateUtil.DateToStrForRecord(
                CDateUtil.parseDate(borrowRecordDetailResponse.getExpired_date(), "yyyyMMdd"));
        c_start_dead_date_tv.setText(loanDateTime + " - " + expiredDateTime);
        //借款期限
        tv_borrowterm.setText(borrowRecordDetailResponse.getTerm_spans()
                + getSpanUnitStr(borrowRecordDetailResponse.getSpan_unit()));
        //收款银行图标
        String paymentTBankCode = borrowRecordDetailResponse.getPayment_title_code();
        CBanksUtils.selectIcoidToImgView(this, paymentTBankCode, c_sure_borrow_shoukuan_iv);
        //还款银行图标
        repaymentBankCode = borrowRecordDetailResponse.getRepayment_title_code();
        CBanksUtils.selectIcoidToImgView(this, repaymentBankCode, c_sure_borrow_huankuan_iv);
        //
        if(borrowRecordDetailResponse.getExpired_date()!=null&&borrowRecordDetailResponse.getExpired_date().length()>6)
        c_tv_repay_date.setText("每月"+borrowRecordDetailResponse.getExpired_date().substring(6)+"日22:00前");
    }

    private String getSpanUnitStr(String str) {
        switch (str) {
            case "D":
                spanUnitStr = "天";
                break;
            case "W":
                spanUnitStr = "周";
                break;
            case "H":
                spanUnitStr = "半月";
                break;
            case "M":
                spanUnitStr = "月";
                break;
            case "Q":
                spanUnitStr = "季";
                break;
            case "Y":
                spanUnitStr = "年";
                break;
        }
        return spanUnitStr;
    }

    private void initViews() {
        c_tv_repay_date = (TextView) findViewById(R.id.c_tv_repay_date);
        c_tv_borrow_amount = (TextView) findViewById(R.id.c_tv_borrow_amount);
        tv_repayPlan = (TextView) findViewById(R.id.tv_repayPlan);
        tv_repayPlan.setOnClickListener(clickListener);

        //收款账户
        c_sure_borrow_shoukuan_account = (TextView) findViewById(R.id.c_sure_borrow_shoukuan_account);
        c_sure_borrow_shoukuan_iv = (ImageView) findViewById(R.id.c_sure_borrow_shoukuan_iv);

        //还款账户
        c_sure_borrow_huankuan_account = (TextView) findViewById(R.id.c_sure_borrow_huankuan_account);
        c_sure_borrow_huankuan_iv = (ImageView) findViewById(R.id.c_sure_borrow_huankuan_iv);

        c_start_dead_date_tv = (TextView) findViewById(R.id.c_start_dead_date_tv);
        tv_borrowterm = (TextView) findViewById(R.id.tv_borrowterm);

        //还款计划
        c_reapy_btn = (Button) findViewById(R.id.c_reapy_btn);
        c_reapy_btn.setOnClickListener(clickListener);
        //还款记录
        tv_show_repaylist = (TextView) findViewById(R.id.tv_show_repaylist);
        tv_show_repaylist.setOnClickListener(clickListener);

        //更换还款卡
        lay_changerepay_card = (AutoRelativeLayout) findViewById(R.id.lay_changerepay_card);
        lay_changerepay_card.setOnClickListener(clickListener);
        //是否已还清，已还清，不显示还款和更换银行卡按钮
        if(isPayed){
            lay_changerepay_card.setVisibility(View.GONE);
            c_reapy_btn.setVisibility(View.GONE);
        }else{
            lay_changerepay_card.setVisibility(View.VISIBLE);
            c_reapy_btn.setVisibility(View.VISIBLE);
        }
        tv_changeCard = (TextView) findViewById(R.id.tv_changeCard);
        lay_contract = (AutoRelativeLayout) findViewById(R.id.lay_contract);
        tv_contact = (TextView) findViewById(R.id.tv_contact);
        tv_contact.setOnClickListener(clickListener);
    }

    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            int id = view.getId();
            if (id == R.id.c_reapy_btn) {
                jumpPage();
            } else if (id == R.id.tv_repayPlan) {
                Intent intent = new Intent(MultiBorrowDetailActivity.this, XJDRepayPlanActivity.class);
                intent.putExtra("contract_id", contract_id);
                startActivity(intent);
            } else if (id == R.id.lay_changerepay_card) {
                Intent intent = new Intent(MultiBorrowDetailActivity.this, BankSelectActivity.class);
                intent.putExtra("is_hk", true);
                if (detailResponse != null) {
                    intent.putExtra("paid_cash_card", detailResponse.getRepaid_cash_card() + "");
                }
                startActivityForResult(intent, 994);
            } else if (id == R.id.tv_contact) {
                Intent intent = new Intent(MultiBorrowDetailActivity.this, AgreementActivity.class);
                intent.putExtra("productId", RyxcreditConfig.xjd_procudtId);
                intent.putExtra("money_manage_url", appendManagerUrl(money_manage_url));
                intent.putExtra("contract_url", appendContractUrl(contract_url));
                startActivity(intent);
            } else if (id == R.id.tv_show_repaylist) {
                //还款记录
                Intent intent = new Intent(MultiBorrowDetailActivity.this, MultiRYDRepayRecordActivity.class);
                if (detailResponse != null)
                    intent.putExtra("repayment_data", detailResponse);
                intent.putExtra("contract_id", contract_id);
                startActivity(intent);
            }
        }
    };
    private static final int REQUEST_QUICK_PAYMENT_CODE = 0x0011;

    private String changed_dfCardNo,hkBankCode,hkBankName;//还款卡
    /**
     * 还款银行卡信息
     */
    private void getHuanKuanBank(CcardRepaymentResponse.ResultBean dkCardInfo) {
        if (dkCardInfo == null) {
            return;
        }
        changed_dfCardNo = dkCardInfo.getCard_num();
        hkBankCode = dkCardInfo.getBank_title_code();
        hkBankName = dkCardInfo.getBank_name();
        change_dkCard();
    }

    private void change_dkCard() {
        //如果更换后的银行卡不为空，并且和代扣卡相同则不更换
        if (!TextUtils.isEmpty(changed_dfCardNo) && changed_dfCardNo.equals(repayBankNum)) {
            CLogUtil.showToast(this,"请选择另一张银行卡！");
            return;
        }
        ChangeDkCardRequest request = new ChangeDkCardRequest();
        request.setCard_num(changed_dfCardNo);
        request.setRepayment_card_num(repayBankNum);
        request.setContract_id(contract_id);
        httpsPost(this, request, ReqAction.APPLICATION_CHANGE_DKCARD, LoanRepayResponse.class, new ICallback<LoanRepayResponse>() {
            @Override
            public void success(LoanRepayResponse loanRepayResponse) {
                int loanRepayResponseCode = loanRepayResponse.getCode();
                if (loanRepayResponseCode==5031) {
                    showMaintainDialog();
                } else {
                    initViewSattus();
                }
            }

            @Override
            public void failture(String tips) {
            CLogUtil.showToast(MultiBorrowDetailActivity.this,tips);
            }
        });
    }

    //还款卡内容显示
    private void initViewSattus() {
        repayBankNum = changed_dfCardNo;
        repaymentBankCode = hkBankCode;
        tv_changeCard.setVisibility(View.GONE);
        tv_change_hkcardNo = (TextView)findViewById(R.id.tv_change_hkcardNo);
        tv_change_hkcardNo.setVisibility(View.VISIBLE);
        tv_change_hkcardNo.setText(CStringUnit.cardJiaMi(changed_dfCardNo));
        tv_change_hkcardNo.setVisibility(View.VISIBLE);
        c_changed_huankuan_iv = (ImageView)findViewById(R.id.c_changed_huankuan_iv);
        c_changed_huankuan_iv.setVisibility(View.VISIBLE);
        CBanksUtils.selectIcoidToImgView(MultiBorrowDetailActivity.this, hkBankCode, c_changed_huankuan_iv);
    }

    private void jumpPage() {
        Intent intent = new Intent(MultiBorrowDetailActivity.this, XJDRepaymentAcitivity.class);
        if (detailResponse != null) {
            intent.putExtra("repaymentBankCode", repaymentBankCode);
            intent.putExtra("repayBankNum", repayBankNum);
            intent.putExtra("detailResponse", (Serializable) detailResponse);
            intent.putExtra("product_descr",product_descr);
            intent.putExtra("is_opened", is_opened);
            intent.putExtra("repayStatus", repayStatus);
            startActivityForResult(intent, REQUEST_QUICK_PAYMENT_CODE);
        }
    }
    public static final int RESPONSE_QUICK_PAYMENT_CODE = 0x0012;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CLogUtil.showLog("onActivityResult---",requestCode+"---"+resultCode+"----"+data);
        if(requestCode==994&&resultCode==995){
            if(data!=null)
            getHuanKuanBank((CcardRepaymentResponse.ResultBean)data.getSerializableExtra("hkCardInfo"));
        }else if(resultCode==RESPONSE_QUICK_PAYMENT_CODE){
            setResult(RESPONSE_QUICK_PAYMENT_CODE);
            finish();
        }
    }
    //信用咨询及管理服务协议
    private String appendManagerUrl(String url) {
        loadDate = c_start_dead_date_tv.getText().toString();
        //日期处理
        String[] dateStr = loadDate.trim().split("-");
        CLogUtil.showLog("c_start_dead_date_tv---", c_start_dead_date_tv + "---");
        try {
            String beginDate = dateStr[0].trim();
            String endDate = dateStr[1].trim();
            if (!TextUtils.isEmpty(beginDate)) {
                beginDateSplit = beginDate.split("/");
            }
            if (!TextUtils.isEmpty(endDate)) {
                endDateSplit = endDate.split("/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String finalUrl = "";
        BigDecimal b2 = new BigDecimal(100);
        DecimalFormat df = new DecimalFormat("#0.00");
        try {
            finalUrl = url + "?name=" + URLEncoder.encode(RyxcreditConfig.getRealName(),
                    "UTF-8")
                    + "&idNo=" + RyxcreditConfig.getCardId(getApplicationContext())
                    + "&year=" + beginDateSplit[0] +
                    "&month=" + beginDateSplit[1]
                    + "&day=" + beginDateSplit[2]
                    //服务费率
                    + "&service_rate=" +df.format(CNummberUtil.getStrFromBigDecimal(String.valueOf(detailResponse.getOther_cost_rate())).multiply(b2))
                   // + "&service_rate=" + subZeroAndDot(CNummberUtil.getStrFromBigDecimal(detailResponse.getOther_cost_rate()).multiply(b2).toString())
                    //服务费
                    + "&service_amount=" + df.format(detailResponse.getTotal_term_svcfee())
                   //逾期服务费率
                    +"&breach_contract_fee="+df.format(detailResponse.getOther_overdue_interest_rate());
            CLogUtil.showLog("appendManagerUrl--", finalUrl + "---");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }

    /**
     * 拼接借款协议地址
     *
     * @param url
     * @return
     */
    private String appendContractUrl(String url) {
        loadDate = c_start_dead_date_tv.getText().toString();
        //日期处理
        String[] dateStr = loadDate.trim().split("-");
        CLogUtil.showLog("c_start_dead_date_tv---", c_start_dead_date_tv + "---");
        try {
            String beginDate = dateStr[0].trim();
            String endDate = dateStr[1].trim();
            if (!TextUtils.isEmpty(beginDate)) {
                beginDateSplit = beginDate.split("/");
            }
            if (!TextUtils.isEmpty(endDate)) {
                endDateSplit = endDate.split("/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String finalUrl = "";
        try {
            // "&loanInitPrin=" + applyRequest.getTotal_amount() +
            //"&beheadFee=" + applyRequest.getCost_amount() +
            BigDecimal b2 = new BigDecimal(100);
            DecimalFormat df = new DecimalFormat("#0.00");
            String ll=detailResponse.getExpired_date();
            finalUrl = url + "?name=" + URLEncoder.encode(RyxcreditConfig.getRealName(), "UTF-8")
                    +"&contrNbr="+contract_id
                    + "&idNo=" + RyxcreditConfig.getCardId(getApplicationContext())
                    + "&year=" + beginDateSplit[0] +
                    "&month=" + beginDateSplit[1]
                    + "&day=" + beginDateSplit[2]
                    + "&yearr=" + endDateSplit[0]
                    + "&monthr=" + endDateSplit[1]
                    + "&dayr=" + endDateSplit[2]
                    //借款本金（大写）
                    + "&arrAmountBig="
                    + CNummberConvertUtil.convert(String.valueOf(detailResponse.getLoan_amount()))
                    //借款本金（小写）
                    + "&arrAmount=" + df.format(detailResponse.getLoan_amount())
                    //每期应还金额
                    +"&mqyhMoney="+detailResponse.getTerm_fixed_repay_amount()
                    //借款利率
                    +"&brinterest= "+df.format(CNummberUtil.getStrFromBigDecimal(String.valueOf(detailResponse.getInterest_rate())).multiply(b2))
                   // + "&brinterest= " + subZeroAndDot(CNummberUtil.getStrFromBigDecimal(detailResponse.getInterest_rate()).multiply(b2).toString())
                    //还款分期数
                    + "&stmtDay=" + detailResponse.getTerm_spans()
                    //收款账户
                    + "&payment_card_num=" + CStringUnit.cardJiaMi(detailResponse.getPayment_card_num())
                    //借款到期日
                    + "&expired_date=" + detailResponse.getExpired_date()
                    //罚息利息
                   // +"&overdue_rate="+detailResponse.getSub_overdue_interest_rate()
                    +"&overdue_rate="+df.format(detailResponse.getSub_overdue_interest_rate())
                    //每期还款日
                    +"&mqhkDay="+endDateSplit[2];
            CLogUtil.showLog("finalUrl---", finalUrl + "---");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
