package com.ryx.ryxcredit.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.PhoneNumber;
import com.livedetect.FaceCollectActivity;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.addressbook.ContactsRequest;
import com.ryx.ryxcredit.beans.bussiness.addressbook.ContactsResponse;
import com.ryx.ryxcredit.beans.bussiness.loanapply.CLoanApplyRequest;
import com.ryx.ryxcredit.beans.bussiness.loanapply.CLoanApplyResponse;
import com.ryx.ryxcredit.beans.bussiness.loanapply.CpamentRouteResponse;
import com.ryx.ryxcredit.beans.bussiness.msgsend.CMsgSendRequest;
import com.ryx.ryxcredit.beans.bussiness.msgsend.CMsgSendResponse;
import com.ryx.ryxcredit.beans.bussiness.product.CfindRouteRequest;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CNummberConvertUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.utils.CStringUnit;
import com.ryx.ryxcredit.utils.PermissionResult;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.ryx.ryxcredit.activity.ActivateLineActivity.IDENTIFY_RECORDS_CODE;

/**
 * 确认借款
 */
public class SureBorrowActivity extends BaseActivity implements View.OnClickListener {

    private Button sureBorrowBtn;

    //借款合同金额
    private TextView borrowAmountTv;
    //收款账户
    private TextView shoukuanAmountTv;
    private ImageView shoukuanAmountIv;
    //还款账户
    private TextView huankuanAmountTv;
    private TextView tv_borrowterm;
    private ImageView huankuanAmountIv;
    //到账金额
    private TextView daozhagnAmountTv;
    //起止日期
    private TextView startDeadDateTv;
    //借款人姓名
    private TextView borrowNameTv;
    //借款人身份证
    private TextView borrowManIdTv;
    private TextView borrowSureTip1;
    private TextView tvPhoneno;
    private String totalAmout, payCardno, repayCardno, loanAmont, loadDate;
    private CLoanApplyRequest applyRequest;
    private String dkTbankCode, dfTbankCode;
    private int borrowTerm;
    private Timer mTimer;
    private int secondsRremaining = 0;//倒计时时间
    private TextView tvVertifyCode;
    private String[] beginDateSplit = new String[]{"", "", ""};
    private String[] endDateSplit = new String[]{"", "", ""};
    private String contractUrl;
    private String agreementUrl;
    //    private String service_agreement;
    private Button buttonSure;
    private String sub_cost_amount;//借款利息
    private String brinterest;//借款利率
    private String money_manage_url;//信用咨询及管理服务协议
    private String other_cost_amount;//服务费
    private String other_cost_rate;//服务费率

    private String chanllenge_type;//是否需要短信验证码，人脸识别
    private double other_overdue_interest_rate;//逾期服务费率
    private double overdue_interest_rate;//逾期利息率
    private String daikou_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_sure_borrow);
        setTitleLayout(getResources().getString(R.string.c_sure_borrow), true, false);
        initView();
        borrowSureTip1.setText(Html.fromHtml(getResources().getString(R.string.c_sure_borrow_tip1)));
        borrowSureTip1.setOnClickListener(this);
        getRoute();
    }

    private void getRoute() {
        final CfindRouteRequest request = new CfindRouteRequest();
        request.setKey("payment_page");
        httpsPost(this, request, ReqAction.APPLICATION_ROUTE, CpamentRouteResponse.class, new ICallback<CpamentRouteResponse>() {
            @Override
            public void success(CpamentRouteResponse routeResponse) {
                CpamentRouteResponse.ResultBean result = routeResponse.getResult();
                if (result != null) {
                    chanllenge_type = result.getChallenge_type();
                }
            }

            @Override
            public void failture(String tips) {

            }
        });

    }

    private void getData() {
        applyRequest = new CLoanApplyRequest();
        if (getIntent() == null)
            return;
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;
        applyRequest.setProduct_descr(bundle.getString("product_descr"));
        applyRequest.setProduct_id(bundle.getString("product_id"));
        applyRequest.setSub_product_id(bundle.getString("sub_product_id"));
        applyRequest.setCost_amount(bundle.getDouble("cost_amount"));
        applyRequest.setLoan_amount(bundle.getDouble("loan_amount"));
        applyRequest.setTotal_amount(bundle.getDouble("total_amount"));
        applyRequest.setPayment_card_num(bundle.getString("payment_card_num"));
        applyRequest.setRepayment_card_num(bundle.getString("repayment_card_num"));
        applyRequest.setAgreement_id(bundle.getInt("agreement_id"));
        loadDate = bundle.getString("loadDate");
        payCardno = bundle.getString("payment_card_num");
        repayCardno = bundle.getString("repayment_card_num");
        loadDate = bundle.getString("date");
        //代付卡银行编码
        dfTbankCode = bundle.getString("payment_bankCode");
        //代扣卡银行编码
        dkTbankCode = bundle.getString("repayment_bankCode");
        borrowTerm = bundle.getInt("borrowterm");
        contractUrl = bundle.getString("contract_url");
        agreementUrl = bundle.getString("dkagreement_url");
//        service_agreement = bundle.getString("service_agreement_url");
        sub_cost_amount = bundle.getString("Sub_cost_amount");
        brinterest = bundle.getString("brinterest");
        money_manage_url = bundle.getString("money_manage_url");
        other_cost_amount = bundle.getString("other_cost_amount");
        other_cost_rate = bundle.getString("other_cost_rate");
        other_overdue_interest_rate = bundle.getDouble("other_overdue_interest_rate");
        overdue_interest_rate = bundle.getDouble("overdue_interest_rate");
    }

    private void initView() {
        getData();
        sureBorrowBtn = (Button) findViewById(R.id.c_sure_borrow_btn);
        shoukuanAmountTv = (TextView) findViewById(R.id.c_sure_borrow_shoukuan_account);
        shoukuanAmountTv.setText(CStringUnit.cardJiaMi(applyRequest.getPayment_card_num()));//收款账户
        borrowAmountTv = (TextView) findViewById(R.id.c_tv_borrow_amount);
        borrowAmountTv.setText("¥" + String.valueOf(applyRequest.getTotal_amount()) + "元");//合同金额
        huankuanAmountTv = (TextView) findViewById(R.id.c_sure_borrow_huankuan_account);
        huankuanAmountTv.setText(CStringUnit.cardJiaMi(applyRequest.getRepayment_card_num()));//还款账户
        daozhagnAmountTv = (TextView) findViewById(R.id.c_daozhang_amount_tv);
        daozhagnAmountTv.setText("¥" + String.valueOf(applyRequest.getLoan_amount()) + "元");
        startDeadDateTv = (TextView) findViewById(R.id.c_start_dead_date_tv);
        startDeadDateTv.setText(loadDate);
        borrowNameTv = (TextView) findViewById(R.id.c_tv_borrow_name);
        tv_borrowterm = (TextView) findViewById(R.id.tv_borrowterm);
        tv_borrowterm.setText(borrowTerm + "天");
        borrowNameTv.setText(CStringUnit.realNameJiaMi(RyxcreditConfig.getRealName()));
        borrowManIdTv = (TextView) findViewById(R.id.c_borrow_sure_id_tv);
        borrowManIdTv.setText(RyxcreditConfig.getCardId(getApplicationContext()));
        huankuanAmountIv = (ImageView) findViewById(R.id.c_sure_borrow_huankuan_iv);

        shoukuanAmountIv = (ImageView) findViewById(R.id.c_sure_borrow_shoukuan_iv);

        daikou_url = getIntent().getStringExtra("daikou_url");//委托代扣协议
        
        borrowSureTip1 = (TextView) findViewById(R.id.c_borrow_sure_tip1);
        sureBorrowBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if ("1".equals(chanllenge_type)) {
                    showBottomSheet();
                } else if ("2".equals(chanllenge_type)) {
                    Intent intent = new Intent(SureBorrowActivity.this, FaceCollectActivity.class);
                    intent.putExtra("applyRequest", (Serializable) applyRequest);
                    startActivityForResult(intent, IDENTIFY_RECORDS_CODE);
                }
            }
        });
        CBanksUtils.selectIcoidToImgView(SureBorrowActivity.this, dfTbankCode, shoukuanAmountIv);
        CBanksUtils.selectIcoidToImgView(SureBorrowActivity.this, dkTbankCode, huankuanAmountIv);
    }

    public void showBottomSheet() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(SureBorrowActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(SureBorrowActivity.this).inflate(R.layout.c_popwindow_sms_check, null);
        tvVertifyCode = (TextView) boottomView.findViewById(R.id.c_btn_sendmsg);
        final EditText etPhonecode = (EditText) boottomView.findViewById(R.id.c_et_phonecode);
        tvPhoneno = (TextView) boottomView.findViewById(R.id.c_tv_phoneno);
        tvPhoneno.setText(RyxcreditConfig.getPhoneNo());
        String code = "";
        if (mTimer == null) {
            tvVertifyCode.setText(getResources().getString(R.string.c_common_send_verify_code));
            tvVertifyCode.setClickable(true);
            tvVertifyCode.setTextColor(Color.parseColor("#1db7f0"));
        } else {
            tvVertifyCode.setTextColor(getResources().getColor(R.color.text_a));
            tvVertifyCode.setText(getResources().getString(R.string.resend)
                    + "(" + secondsRremaining + ")");
            tvVertifyCode.setClickable(false);
        }
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendVertifyCode();
            }
        }, 200l);
        tvVertifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVertifyCode();
            }
        });
        //借款确认
        buttonSure = (Button) boottomView.findViewById(R.id.c_sure_borrow_btn);
        buttonSure.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (TextUtils.isEmpty(etPhonecode.getText().toString().trim())) {
                    CLogUtil.showToast(SureBorrowActivity.this, "请填写验证码!");
                    return;
                }
                applyRequest.setCode(etPhonecode.getText().toString().trim());
                httpsPost(SureBorrowActivity.this, applyRequest, ReqAction.APPLICATION_LOAN_APPLY, CLoanApplyResponse.class, new ICallback<CLoanApplyResponse>() {
                    @Override
                    public void success(CLoanApplyResponse cLoanApplyResponse) {
                        mBottomSheetDialog.dismiss();
                        //如果返回的申请编码不为空，则表示借款成功
                        if (!TextUtils.isEmpty(cLoanApplyResponse.getResult())) {
                            CCommonDialog.showRepaymentOK(SureBorrowActivity.this,
                                    "借款申请成功", "请随时关注银行卡到账信息！",
                                    new CCommonDialog.IMessage() {
                                        @Override
                                        public void callback() {
                                            checkContactPermission();
                                        }
                                    });

                        } else {
                            CLogUtil.showToast(SureBorrowActivity.this,  "对不起，您的综合平分不足！");
                            setResult();
                        }
                    }

                    @Override
                    public void failture(String tips) {
                        mBottomSheetDialog.dismiss();
                        CLogUtil.showToast(SureBorrowActivity.this, tips + "");
                        setResult();
                    }
                });
            }
        });
        mBottomSheetDialog.contentView(boottomView)
                .show();
    }

    /**
     * 手机访问联系人权限
     */
    public void checkContactPermission() {
        final String waring = getResources().getString(R.string.contacts_waring_msg);
        requesDevicePermission(waring, 0x0011, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        CLogUtil.showLog("checkContactPermission---", "requestSuccess");
                        uploadContacts();
                    }

                    @Override
                    public void requestFailed() {
                        CLogUtil.showLog("checkContactPermission---", "requestFailed");
                        setResult();
                    }
                },
                Manifest.permission.READ_CONTACTS);
    }

    //上传通讯录
    private void uploadContacts() {
        List<Contact> contacts = Contacts.getQuery().find();
        ContactsRequest contactsRequest = new ContactsRequest();
        List<ContactsRequest.ContactsBean> list = new ArrayList<ContactsRequest.ContactsBean>();
        for (Contact c : contacts) {
            ContactsRequest.ContactsBean bean = new ContactsRequest.ContactsBean();
            bean.setName(c.getBestDisplayName());
            List<String> listString = new ArrayList<String>();
            for (PhoneNumber p : c.getPhoneNumbers()) {
                listString.add(p.getNormalizedNumber() + ",");
            }
            bean.setPhone_nums(listString);
            list.add(bean);
        }
        contactsRequest.setContacts(list);
        if (list == null || list.size() == 0) {
            setResult();
            return;
        }
        httpsPost(this, contactsRequest, ReqAction.APPLICATION_ADDRESSBOOK_CREATE,
                ContactsResponse.class, new ICallback<ContactsResponse>() {
                    @Override
                    public void success(ContactsResponse contactsResponse) {
                        CLogUtil.showLog("checkContactPermission---", "uploadContacts--success");
                        setResult();
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showLog("checkContactPermission---", "uploadContacts--failture");
                        setResult();
                    }
                });
    }

    private void sendVertifyCode() {
        if (mTimer != null) {
            return;
        }
        mTimer = new Timer();
//      CLogUtil.showToast(SureBorrowActivity.this,"发送验证码");
        final CMsgSendRequest request = new CMsgSendRequest();
        request.setProduct_id(RyxcreditConfig.rkd_procudtid);
        httpsPost(this, request, ReqAction.APPLICATION_MESSAGE_CHALLENGECODE_SEND, CMsgSendResponse.class, new ICallback<CMsgSendResponse>() {
            @Override
            public void success(CMsgSendResponse cMsgSendResponse) {
                applyRequest.setIndex(cMsgSendResponse.getResult());
                CLogUtil.showToast(SureBorrowActivity.this, "验证码已发送!");
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(SureBorrowActivity.this, tips);
            }
        });
        startCountdown();
    }

    private Handler mhandler = new Handler();

    /**
     * 开始倒计时60秒
     */
    public void startCountdown() {
        secondsRremaining = 60;
        TimerTask task = new TimerTask() {

            public void run() {
                Message msg = Message.obtain();
                msg.what = secondsRremaining--;
                timeHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(task, 1000, 1000);
    }

    Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0) {
                tvVertifyCode.setTextColor(getResources().getColor(R.color.text_a));
                tvVertifyCode.setText(getResources().getString(R.string.resend)
                        + "(" + msg.what + ")");
                tvVertifyCode.setClickable(false);
            } else {
                mTimer.cancel();
                mTimer = null;
                tvVertifyCode.setText(getResources().getString(
                        R.string.resend_verification_code));
                tvVertifyCode.setClickable(true);
                tvVertifyCode.setTextColor(Color.parseColor("#1db7f0"));
            }
        }
    };

    private void setResult() {
        setResult(999);
        finish();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.c_borrow_sure_tip1) {
            Intent intent = new Intent(this, AgreementActivity.class);
            intent.putExtra("contract_url", appendContractUrl(contractUrl));//借款协议地址
            intent.putExtra("daikou_url", daikou_url);//委托代扣地址
            intent.putExtra("money_manage_url", appendManagerUrl(money_manage_url));//信用咨询及管理服务协议
            startActivity(intent);
        }
    }

    private String appAgreementUtl(String agreementUrl){
        String finalUrl = agreementUrl +"?breach_contract_fee=" + CNummberUtil.getStrFromBigDecimal(String.valueOf(overdue_interest_rate)).toString();
        return finalUrl;
    }

    //信用咨询及管理服务协议
    private String appendManagerUrl(String url) {
        String finalUrl = "";
        BigDecimal b2 = new BigDecimal(100);
        try {
            finalUrl = url + "?name=" + URLEncoder.encode(RyxcreditConfig.getRealName(),
                    "UTF-8")
                    + "&idNo=" + RyxcreditConfig.getCardId(getApplicationContext())
                    + "&year=" + beginDateSplit[0] +
                    "&month=" + beginDateSplit[1]
                    + "&day=" + beginDateSplit[2]
                    + "&service_rate=" +  subZeroAndDot(CNummberUtil.getStrFromBigDecimal(other_cost_rate).multiply(b2).toString())
                    + "&service_amount=" + other_cost_amount
                    + "&breach_contract_fee=" +  subZeroAndDot(CNummberUtil.getStrFromBigDecimal(String.valueOf(other_overdue_interest_rate)).toString());
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
        //日期处理
        String[] dateStr = loadDate.trim().split("-");
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
            BigDecimal b3 = new BigDecimal(1000);
            finalUrl = url + "?name=" + URLEncoder.encode(RyxcreditConfig.getRealName(), "UTF-8")
                    + "&idNo=" + RyxcreditConfig.getCardId(getApplicationContext())
                    + "&year=" + beginDateSplit[0] +
                    "&month=" + beginDateSplit[1]
                    + "&day=" + beginDateSplit[2]
                    + "&arrAmount=" + applyRequest.getLoan_amount()
                    + "&loanInitPrin=" + applyRequest.getTotal_amount()
                    + "&beheadFee=" + applyRequest.getCost_amount()
                    + "&stmtDay=" + borrowTerm
                    + "&yearR=" + endDateSplit[0]
                    + "&monthR=" + endDateSplit[1]
                    + "&dayR=" + endDateSplit[2]
                    + "&promiseMoney=" + sub_cost_amount
                    + "&arrAmountBig="
                    + CNummberConvertUtil.convert(String.valueOf(applyRequest.getLoan_amount()))
                    + "&brinterest=" + subZeroAndDot(CNummberUtil.getStrFromBigDecimal(brinterest).multiply(b2).toString())
                     +"&overdue_rate="+ subZeroAndDot(CNummberUtil.getStrFromBigDecimal(String.valueOf(overdue_interest_rate)).toString());
            CLogUtil.showLog("finalUrl---", finalUrl + "---");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }

    public static final int BORROW_RESULT_CODE = 0X1008;

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BORROW_RESULT_CODE) {
            setResult();
        }
    }
}
