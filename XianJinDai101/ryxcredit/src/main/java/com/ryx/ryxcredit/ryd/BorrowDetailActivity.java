package com.ryx.ryxcredit.ryd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.AgreementActivity;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailRequest;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailResponse;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.ChangeDkCardRequest;
import com.ryx.ryxcredit.beans.bussiness.cardpayment.CcardPaymentResponse;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentResponse;
import com.ryx.ryxcredit.beans.bussiness.loanrepay.LoanRepayResponse;
import com.ryx.ryxcredit.ryd.activity.RYDRepaymentAcitivity;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CMoneyEncoder;
import com.ryx.ryxcredit.utils.CNummberConvertUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.utils.CStringUnit;
import com.ryx.ryxcredit.xjd.BankSelectActivity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

import static com.ryx.ryxcredit.activity.QuickPaymentActivity.RESPONSE_QUICK_PAYMENT_CODE;
import static com.ryx.ryxcredit.utils.CDateUtil.DateToStrForRecord;

/**
 * 借款详情
 *
 * @author muxin
 * @time 2016-09-26 18:11
 */
public class BorrowDetailActivity extends BaseActivity {

    private static final int REQUEST_QUICK_PAYMENT_CODE = 0x0011;
    private Button btn_quikpay;
    private TextView tv_show, tv_contact;
    //借款金额（合同金额）
    private TextView contractAmountTv;
    //收款账户
    private TextView collectionAccountTv;
    //起止日期
    private TextView startEndDateTv;
    //借款期限
    private TextView totalDaysTv;
    //还款账户
    private TextView paymentAccountTv;
    //还款日
    private TextView tv_repay_date;

    private ImageView mPaymentBankIcon;
    private ImageView mRepaymentBankIcon;
    private BorrowRecordDetailResponse.ResultBean resultBeen;
    private String spanUnitStr = "";
    private String loanDateTime;
    private String expiredDateTime;
    private String[] beginDateSplit = new String[]{"", "", ""};
    private String[] endDateSplit = new String[]{"", "", ""};
//        private boolean is_opened;//是否是业务时间内
    private String unborrowTime;
    private int repayStatus;//是否可以还款状态
    private List<CcardPaymentResponse.ResultBean> cardPayList;//信用卡列表
    private String changed_dfCardNo;//更换后的代付卡号码
    private String repaymentTBankCode;
    private String repayBankNum;//代扣卡号码
    private String agreementUrl;//代扣协议url
    private String dkbankName = "";//代扣卡的银行名称
    private boolean isAgreed = true;//用户是否同意委托代扣协议,默认同意
    private String repayTBankCode = "";
    private String dkCardPhoneNo;//银行卡预留卡号
    private AutoRelativeLayout lay_showContract, lay_change, lay_show_repaylist;
    private AutoLinearLayout lay_btn;
    private boolean isPayed;//是否是已结清的记录
    private String contract_id;//合同编号

    private TextView c_dfbank_change;//更换还款卡
    private ImageView iv_chbank_icon;//银行卡图标
    private TextView tv_chaccount;//更换后的银行号码
    private double other_cost_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_borrow_detail);
        setTitleLayout(getResources().getString(R.string.c_borrow_record), true, false);
       if(getIntent().hasExtra("contract_id")){
           contract_id = getIntent().getStringExtra("contract_id");
      }
//        if (getIntent().hasExtra("is_opened")) {
//            is_opened = getIntent().getBooleanExtra("is_opened", false);
//        }
        if (getIntent().hasExtra("unborrowTime"))
            unborrowTime = getIntent().getStringExtra("unborrowTime");
        if (getIntent().hasExtra("isPayed"))
            isPayed = getIntent().getBooleanExtra("isPayed",false);
        initRes();
        initData();
    }

    private void initData() {
        BorrowRecordDetailRequest request = new BorrowRecordDetailRequest();
       request.setContract_id(contract_id);
        request.setProduct_id("8007");
        request.setSub_product_id("600101");
        httpsPost(this, request, ReqAction.APPLICATION_BORROW_RECORD_DETAIL, BorrowRecordDetailResponse.class, new ICallback<BorrowRecordDetailResponse>() {
            @Override
            public void success(BorrowRecordDetailResponse borrowRecordDetailResponse) {
                resultBeen = borrowRecordDetailResponse.getResult();
                if (resultBeen != null) {
                    repayStatus = resultBeen.getStatus();
                    other_cost_rate = resultBeen.getOther_cost_rate();
                    agreementUrl = resultBeen.getAgreement_url();
                    bindView(borrowRecordDetailResponse.getResult());
                }

            }

            @Override
            public void failture(String tips) {
            }
        });
    }

    private void bindView(BorrowRecordDetailResponse.ResultBean borrowRecordDetailResponse) {
        //状态
        String loanStatus = borrowRecordDetailResponse.getLoan_status();
        //改变背景颜色
        //已逾期
        if ("B".equals(loanStatus)) {
            btn_quikpay.setVisibility(View.VISIBLE);
            lay_change.setVisibility(View.VISIBLE);
            lay_showContract.setVisibility(View.VISIBLE);
            lay_show_repaylist.setVisibility(View.VISIBLE);
            lay_btn.setVisibility(View.VISIBLE);
        }
        //已结清
        else if ("A".equals(loanStatus)) {
            btn_quikpay.setVisibility(View.INVISIBLE);
            lay_change.setVisibility(View.GONE);
            lay_showContract.setVisibility(View.VISIBLE);
            lay_show_repaylist.setVisibility(View.VISIBLE);
            lay_btn.setVisibility(View.INVISIBLE);
        }
        //放款中
        else if ("F".equals(loanStatus) || "G".equals(loanStatus) || "H".equals(loanStatus)) {
            btn_quikpay.setVisibility(View.INVISIBLE);
            lay_showContract.setVisibility(View.VISIBLE);
            lay_change.setVisibility(View.GONE);
            lay_show_repaylist.setVisibility(View.GONE);
            lay_btn.setVisibility(View.GONE);
        }
        //使用中
        else {
            btn_quikpay.setVisibility(View.VISIBLE);
            lay_showContract.setVisibility(View.VISIBLE);
            lay_change.setVisibility(View.VISIBLE);
            lay_show_repaylist.setVisibility(View.VISIBLE);
            lay_btn.setVisibility(View.VISIBLE);
        }
        //还款日
        tv_repay_date.setText(DateToStrForRecord(
                CDateUtil.parseDate(borrowRecordDetailResponse.getExpired_date(), "yyyyMMdd")) + " 22:00 前");
        //借款金额（合同金额）
        contractAmountTv.setText(CMoneyEncoder.EncodeFormat(String.valueOf(borrowRecordDetailResponse.getTotal_amount())));
        repayBankNum = borrowRecordDetailResponse.getRepayment_card_num();
        //还款账户
        paymentAccountTv.setText(CStringUnit.cardJiaMi(borrowRecordDetailResponse.getRepayment_card_num()));
        //收款账户
        collectionAccountTv.setText(CStringUnit.cardJiaMi(borrowRecordDetailResponse.getPayment_card_num()));
        //起止日期
        loanDateTime = CDateUtil.DateToStrForRecord(
                CDateUtil.parseDate(borrowRecordDetailResponse.getLoan_date(), "yyyyMMdd"));
        expiredDateTime = CDateUtil.DateToStrForRecord(
                CDateUtil.parseDate(borrowRecordDetailResponse.getExpired_date(), "yyyyMMdd"));
        startEndDateTv.setText(loanDateTime + " - " + expiredDateTime);
        //借款期限
        totalDaysTv.setText(borrowRecordDetailResponse.getTerm_spans()
                + getSpanUnitStr(borrowRecordDetailResponse.getSpan_unit()));
        //收款银行图标
        String paymentTBankCode = borrowRecordDetailResponse.getPayment_title_code();
        CBanksUtils.selectIcoidToImgView(this, paymentTBankCode, mPaymentBankIcon);
        //还款银行图标
        repaymentTBankCode = borrowRecordDetailResponse.getRepayment_title_code();
        CBanksUtils.selectIcoidToImgView(this, repaymentTBankCode, mRepaymentBankIcon);
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

    private void initRes() {
        //快速还款
        btn_quikpay = (Button) findViewById(R.id.btn_quikpay);
        btn_quikpay.setOnClickListener(clickListener);
        //还款列表
        tv_show = (TextView) findViewById(R.id.tv_show_repaylist);
        //查看合同
        tv_contact = (TextView) findViewById(R.id.tv_repayment_contract);
        tv_contact.setOnClickListener(clickListener);
        tv_show.setOnClickListener(clickListener);
        //还款日
        tv_repay_date = (TextView) findViewById(R.id.tv_repay_date);
        //收款账户
        collectionAccountTv = (TextView) findViewById(R.id.tv_account);
        //起止日期
        startEndDateTv = (TextView) findViewById(R.id.tv_start_end_date);
        //借款期限
        totalDaysTv = (TextView) findViewById(R.id.tv_borrow_term);
        //还款账户
        paymentAccountTv = (TextView) findViewById(R.id.tv_repayment_account);
        //借款到账金额
        contractAmountTv = (TextView) findViewById(R.id.tv_contract_amount);
        //收款银行图标
        mPaymentBankIcon = (ImageView) findViewById(R.id.iv_payment_bank_icon);
        //还款银行图标
        mRepaymentBankIcon = (ImageView) findViewById(R.id.iv_repayment_bank_icon);

        lay_showContract = (AutoRelativeLayout) findViewById(R.id.lay_showContract);
        lay_change = (AutoRelativeLayout) findViewById(R.id.lay_change);
        lay_change.setOnClickListener(clickListener);
        c_dfbank_change =  (TextView) findViewById(R.id.c_dfbank_change);
        iv_chbank_icon = (ImageView) findViewById(R.id.iv_chbank_icon);
        tv_chaccount =  (TextView) findViewById(R.id.tv_chaccount);
        lay_show_repaylist = (AutoRelativeLayout) findViewById(R.id.lay_show_repaylist);
        lay_btn = (AutoLinearLayout) findViewById(R.id.lay_btn);
        if(isPayed){
            lay_change.setVisibility(View.GONE);
            btn_quikpay.setVisibility(View.GONE);
        }else{
            lay_change.setVisibility(View.VISIBLE);
            btn_quikpay.setVisibility(View.VISIBLE);
        }
    }


    //更换自动还款卡
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
                initViewSattus();
            }

            @Override
            public void failture(String tips) {

            }
        });
    }

    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            int id = view.getId();
            if (id == R.id.btn_quikpay) {
                jumpPage();
            } else if (id == R.id.tv_show_repaylist) {
                Intent intent = new Intent(BorrowDetailActivity.this, RepayRecordActivity.class);
                if (resultBeen != null)
                    intent.putExtra("repayment_data", resultBeen);
                startActivity(intent);
            } else if (id == R.id.tv_repayment_contract) {
                //借款协议
                try {
                    if (!TextUtils.isEmpty(loanDateTime)) {
                        beginDateSplit = loanDateTime.split("/");
                    }
                    if (!TextUtils.isEmpty(expiredDateTime)) {
                        endDateSplit = expiredDateTime.split("/");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(BorrowDetailActivity.this, AgreementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "借款协议");
                bundle.putString("contract_url", appendContractUrl(resultBeen.getContract_url()));
                bundle.putString("money_manage_url", appendManagerUrl(resultBeen.getMoney_manage_url()));
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (id == R.id.lay_change) {
//                if(!is_opened){
//                    CLogUtil.showToast(BorrowDetailActivity.this, "抱歉，" + unborrowTime + "非交易时间！");
//                    return;
//                }
//                   huankuanzhanghuAction();
                Intent intent = new Intent(BorrowDetailActivity.this, BankSelectActivity.class);
                intent.putExtra("is_hk", true);
                if(resultBeen!=null){
                    intent.putExtra("paid_cash_card",resultBeen.getRepaid_cash_card()+"");
                }
                startActivityForResult(intent,994);
            }
        }
    };

    private void jumpPage() {
        Intent intent = new Intent(BorrowDetailActivity.this, RYDRepaymentAcitivity.class);
        if (resultBeen != null) {
            intent.putExtra("contract_id", contract_id);//合同号
            intent.putExtra("borrow_amount", String.valueOf(resultBeen.getTotal_amount()));//借款合同金额
            intent.putExtra("owed_amount", String.valueOf(resultBeen.getOwed_amount()));//未还金额
            intent.putExtra("loan_status", resultBeen.getLoan_status());//状态
            intent.putExtra("expired_date", resultBeen.getExpired_date());//还款日
            intent.putExtra("overdue_day", String.valueOf(resultBeen.getOverdue_days()));//逾期天数
            intent.putExtra("overdue_interest", String.valueOf(resultBeen.getOverdue_interest_rate()));//罚息利率
            intent.putExtra("overdue_amount", String.valueOf(resultBeen.getOverdue_interest_amount()));//罚息
            intent.putExtra("repay_data", (Serializable) resultBeen.getRepayments());//罚息
            intent.putExtra("repaid_amount", resultBeen.getRepaid_amount());//已还金额
            intent.putExtra("remain_total_amount", String.valueOf(resultBeen.getRemain_total_amount()));
            intent.putExtra("unborrowTime", unborrowTime);
            intent.putExtra("repay_status", repayStatus);
            intent.putExtra("repaymentBankCode", repaymentTBankCode);
            intent.putExtra("repayBankNum", repayBankNum);
            intent.putExtra("agreementUrl", agreementUrl);
            startActivityForResult(intent, REQUEST_QUICK_PAYMENT_CODE);
        }
    }

    //    初始化代扣卡内容
    private void initViewSattus() {
        c_dfbank_change.setVisibility(View.GONE);
        tv_chaccount.setVisibility(View.VISIBLE);
        tv_chaccount.setText(CStringUnit.cardJiaMi(changed_dfCardNo));
        repayBankNum = changed_dfCardNo;
        repaymentTBankCode = repayTBankCode;
        iv_chbank_icon.setVisibility(View.VISIBLE);
        CBanksUtils.selectIcoidToImgView(this, repayTBankCode, iv_chbank_icon);
    }

    /**
     * 还款银行卡信息
     */
    private void getHuanKuanBank(CcardRepaymentResponse.ResultBean dkCardInfo) {
      CLogUtil.showLog("dkCardInfo-----",dkCardInfo+"====");
        if (dkCardInfo == null) {
            return;
        }
        changed_dfCardNo = dkCardInfo.getCard_num();
        CLogUtil.showLog("changed_dfCardNo-----",changed_dfCardNo+"====");
        repayTBankCode = dkCardInfo.getBank_title_code();
        dkbankName = dkCardInfo.getBank_name();
        change_dkCard();
    }

    /**
     * 弹出代付银行卡选择
     */

    //信用咨询及管理服务协议
    private String appendManagerUrl(String url) {
        String finalUrl = "";
        BigDecimal b2 = new BigDecimal(100);
        BigDecimal b3 = new BigDecimal(1000);
        try {
            finalUrl = url + "?name=" + URLEncoder.encode(RyxcreditConfig.getRealName(),
                    "UTF-8")
                    + "&idNo=" + RyxcreditConfig.getCardId(getApplicationContext())
                    + "&year=" + beginDateSplit[0] +
                    "&month=" + beginDateSplit[1]
                    + "&day=" + beginDateSplit[2]
                    + "&service_rate="+ subZeroAndDot(CNummberUtil.getStrFromBigDecimal(String.valueOf(resultBeen.getOther_cost_rate())).multiply(b2).toString())
                    + "&service_amount=" + resultBeen.getOther_cost_amount()
                    + "&breach_contract_fee=" +  subZeroAndDot(CNummberUtil.getStrFromBigDecimal(String.valueOf(resultBeen.getOther_overdue_interest_rate())).toString());
            CLogUtil.showLog("other_cost_amount", resultBeen.getOther_cost_rate()+"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }

    //借款协议参数
    private String appendContractUrl(String url) {
        String finalUrl = "";
        try {
            BigDecimal b2 = new BigDecimal(100);
            BigDecimal b3 = new BigDecimal(1000);
            finalUrl = url + "?name=" + URLEncoder.encode(RyxcreditConfig.getRealName(),
                    "UTF-8")
                    + "&idNo=" + RyxcreditConfig.getCardId(getApplicationContext())
                    + "&year=" + beginDateSplit[0] +
                    "&month=" + beginDateSplit[1]
                    + "&day=" + beginDateSplit[2]
                    + "&loanInitPrin=" + resultBeen.getTotal_amount()
                    + "&arrAmount=" + resultBeen.getLoan_amount()
                    + "&beheadFee=" + resultBeen.getCost_amount()
                    + "&stmtDay=" + resultBeen.getTerm_spans()
                    + "&yearR=" + endDateSplit[0]
                    + "&monthR=" + endDateSplit[1] +
                    "&dayR=" + endDateSplit[2]
                    + "&contrNbr="+ contract_id
                    + "&contract=0"
                    +"&promiseMoney=" + resultBeen.getSub_cost_amount()
                    + "&arrAmountBig="
                    + CNummberConvertUtil.convert(String.valueOf(resultBeen.getLoan_amount()))
                    +"&brinterest= "+ subZeroAndDot(CNummberUtil.getStrFromBigDecimal(String.valueOf(resultBeen.getInterest_rate())).multiply(b2).toString())
                    +"&overdue_rate="+ subZeroAndDot(CNummberUtil.getStrFromBigDecimal(String.valueOf(resultBeen.getSub_overdue_interest_rate())).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QUICK_PAYMENT_CODE &&
                resultCode == RESPONSE_QUICK_PAYMENT_CODE) {
            finish();
        } else if (requestCode==994&&resultCode == 995) {
            if(data!=null)
                getHuanKuanBank((CcardRepaymentResponse.ResultBean)data.getSerializableExtra("hkCardInfo"));
        }
    }
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
