package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.NoDoubleItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.adapter.CRepayBankListAdapter;
import com.ryx.ryxcredit.adapter.RePayRecordsAdapter;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailRequest;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailResponse;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsResponse;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentRequest;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentResponse;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CfindRepayRouteResponse;
import com.ryx.ryxcredit.beans.bussiness.debitcard.CdebitCardAuthReponse;
import com.ryx.ryxcredit.beans.bussiness.debitcard.CdebitCardAuthRequest;
import com.ryx.ryxcredit.beans.bussiness.loanrepay.LoanRepayRequest;
import com.ryx.ryxcredit.beans.bussiness.loanrepay.LoanRepayResponse;
import com.ryx.ryxcredit.beans.bussiness.product.CfindRouteRequest;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CConstants;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CDensityUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CMoneyEncoder;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.utils.CStringUnit;
import com.ryx.ryxcredit.views.KeyboardChangeListener;
import com.ryx.ryxcredit.widget.RyxSimpleConfirmDialog;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import static com.ryx.ryxcredit.RyxcreditConfig.context;

/**
 * 快速还款
 *
 * @author muxin
 * @time 2016-09-13 13:22
 */
public class QuickPaymentActivity extends BaseActivity implements KeyboardChangeListener.KeyBoardListener {

    public static final int RESPONSE_QUICK_PAYMENT_CODE = 0x0012;
    private LinearLayout swiper_layout, auto_layout;
    private LinearLayout top_layout, bottom_layout;
    private boolean isOverdue = true;
    private TextView swiper_tv, swiper_comment_tv, auto_tv, auto_comment_tv;
    private TextView currentRepaymentAmountTv, tv_status;
    private Button paybackBtn;
    private EditText etRepaymentMoney;
    private TextView tvRepayAmout;
    private TextView tvOverdatetip;
    private RelativeLayout llOverdatecontent;
    private RelativeLayout llOverdueAmount;
    private TextView mOverDay;
    private TextView mOverPay;
    private TextView tvRepayDate;
    private String contract_id;
    private String repaymentMoney;
    private String finalRepaymentMoney;
    private double repaid_amount;
    private String repayDate;
    private KeyboardChangeListener mKeyboardChangeListener;
    private TextView mBorrowMoney;
    private String owedAmount;//当前未还合同金额
    private String remainAmount;//当前未还合同金额b不包括手续费
    private String loanStatus;

    private String overdueDay;
    private String overdueInterest;
    private String flag;
    private BorrowRecordsResponse.ResultBean data;
    private BorrowRecordDetailResponse.ResultBean resultBeen;
    private String overdueAmount;
    private LinearLayout mRepaymentRecordLl;
    private List<BorrowRecordDetailResponse.ResultBean.RepaymentsBean> list = new ArrayList<>();
    private XRecyclerView xRecyclerView;
    private TextView tv_showRecord;
    private List<BorrowRecordDetailResponse.ResultBean.RepaymentsBean> recordsList;
    private int repay_status;
    private ImageView bankImg;
    private TextView tv_bankNo;
    private TextView btn_change;
    private String repayTBankCode, repayBankNum;
    private String dkCardPhoneNo;//银行卡预留卡号
    private boolean isAgreed = true;//用户是否同意委托代扣协议,默认同意
    private String  agreementUrl = "";
    private TextView tv_unrepay_pay;

    private AutoLinearLayout lly_skhk, lly_dkhk;
    private TextView tv_skhk, tv_dkhk;
    private int selectIdx = 0;//选中的子产品索引

    private String repayment_type;
    private String repayment_status;//快速还款按钮状态
    private String repayment_text;

    private String temporary_status;//更换临时卡按钮状态
    private String temporary_type;//更换临时卡按钮类型
    private String temporary_text;//更换临时卡提示语或页面
    private double term_repay_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_quick_payment);
        initViewName();
        Intent intent = getIntent();
        if (intent != null) {
            flag = intent.getStringExtra("flag");
            //如果是从首页的，“我要还款”按钮进来
            if ("Repayment".equals(flag)) {
                //请求借款详情接口
                if( getIntent().hasExtra("contract_id"))
                contract_id = getIntent().getStringExtra("contract_id");
                initData();
            } else {
                repay_status = intent.getIntExtra("repay_status", 0);
                contract_id = intent.getStringExtra("contract_id");//合同号
                repaymentMoney = intent.getStringExtra("borrow_amount");//借款合同金额
                repayDate = intent.getStringExtra("expired_date");//还款日
                owedAmount = intent.getStringExtra("owed_amount");//未还金额，包括手续费
                remainAmount = intent.getStringExtra("remain_total_amount");//未还金额,不包括手续费
                loanStatus = intent.getStringExtra("loan_status");//借款状态
                overdueDay = intent.getStringExtra("overdue_day");//逾期天数
                overdueInterest = intent.getStringExtra("overdue_interest");//罚息利率
                overdueAmount = intent.getStringExtra("overdue_amount");//罚息
                repaid_amount = intent.getDoubleExtra("repaid_amount", 0);//罚息
                repayTBankCode = intent.getStringExtra("repaymentBankCode");//银行编码
                repayBankNum = intent.getStringExtra("repayBankNum");//卡片号码
                recordsList = (List<BorrowRecordDetailResponse.ResultBean.RepaymentsBean>) intent.getSerializableExtra("repay_data");
                agreementUrl = intent.getStringExtra("agreementUrl");//贷款协议地址
                initView();
                //获取路由接口
                getRoute();
            }
        }

    }

    private void initViewName() {
        paybackBtn = (Button) findViewById(R.id.c_payback_money_btn);
        paybackBtn.setOnClickListener(clickListener);
        lly_skhk = (AutoLinearLayout) findViewById(R.id.lly_skhk);
        lly_dkhk = (AutoLinearLayout) findViewById(R.id.lly_dkhk);
        tv_skhk = (TextView) findViewById(R.id.tv_skhk);
        tv_dkhk = (TextView) findViewById(R.id.tv_dkhk);
        lly_skhk.setOnClickListener(clickListener);
        lly_dkhk.setOnClickListener(clickListener);
        setTitleLayout(getResources().getString(R.string.c_borrow_quickly_repment), true, false);
        mKeyboardChangeListener = new KeyboardChangeListener(this);
        mKeyboardChangeListener.setKeyBoardListener(this);
        top_layout = (LinearLayout) findViewById(R.id.lay_top);
        bottom_layout = (LinearLayout) findViewById(R.id.lay_bottom);
        bankImg = (ImageView) findViewById(R.id.img_banklogo);
        tv_bankNo = (TextView) findViewById(R.id.tv_bankNo);
        btn_change = (TextView) findViewById(R.id.c_btn_change);
        btn_change.setOnClickListener(clickListener);
        tv_unrepay_pay = (TextView) findViewById(R.id.tv_unrepay_pay);
        //借款状态
        tv_status = (TextView) findViewById(R.id.tv_status);
        //借款合同金额
        mBorrowMoney = (TextView) findViewById(R.id.tv_payment_top_money);
        swiper_tv = (TextView) findViewById(R.id.c_swiper_tv);
        swiper_comment_tv = (TextView) findViewById(R.id.c_swiper_comment_tv);
        auto_tv = (TextView) findViewById(R.id.c_auto_tv);
        auto_comment_tv = (TextView) findViewById(R.id.c_auto_comment_tv);
        currentRepaymentAmountTv = (TextView) findViewById(R.id.c_current_repayment_amount_tv);
        tvOverdatetip = (TextView) findViewById(R.id.c_tv_overdatetip);
        mOverDay = (TextView) findViewById(R.id.tv_overdue_day);
        llOverdatecontent = (RelativeLayout) findViewById(R.id.rl_overdue_content);
        llOverdueAmount = (RelativeLayout) findViewById(R.id.rl_overdue_amount);
        mOverPay = (TextView) findViewById(R.id.tv_overdue_pay);
        etRepaymentMoney = (EditText) findViewById(R.id.c_tv_repayment_money);
        tvRepayDate = (TextView) findViewById(R.id.c_tv_repay_date);
        tv_showRecord = (TextView) findViewById(R.id.c_tv_show_record);
        tv_showRecord.setOnClickListener(clickListener);
    }

    private void initData() {
        final BorrowRecordDetailRequest request = new BorrowRecordDetailRequest();
        request.setContract_id(contract_id);
        httpsPost(this, request, ReqAction.APPLICATION_BORROW_RECORD_DETAIL, BorrowRecordDetailResponse.class, new ICallback<BorrowRecordDetailResponse>() {
            @Override
            public void success(BorrowRecordDetailResponse borrowRecordDetailResponse) {
                resultBeen = borrowRecordDetailResponse.getResult();
                if (resultBeen != null) {
                    repay_status = resultBeen.getStatus();
                    repaymentMoney = String.valueOf(resultBeen.getTotal_amount());
                    repayDate = resultBeen.getExpired_date();
                    owedAmount = String.valueOf(resultBeen.getOwed_amount());
                    remainAmount =  String.valueOf(resultBeen.getRemain_total_amount());
                    loanStatus = resultBeen.getLoan_status();
                    repaid_amount = resultBeen.getRepaid_amount();
                    overdueDay = String.valueOf(resultBeen.getOverdue_days());
                    overdueInterest = String.valueOf(resultBeen.getOverdue_interest_rate());
                    overdueAmount = String.valueOf(resultBeen.getOverdue_interest_amount());
                    recordsList = resultBeen.getRepayments();
                    repayTBankCode = resultBeen.getRepayment_title_code();
                    repayBankNum = resultBeen.getRepayment_card_num();
                    agreementUrl = resultBeen.getAgreement_url();
                    term_repay_amount = resultBeen.getTerm_repay_amount();
                }
                initView();
                //获取路由接口
                getRoute();
            }

            @Override
            public void failture(String tips) {
                getRoute();
            }
        });

    }

    private void getRoute() {
        final CfindRouteRequest request = new CfindRouteRequest();
        request.setKey("repayment_page");
        httpsPost(this, request, ReqAction.APPLICATION_ROUTE, CfindRepayRouteResponse.class, new ICallback<CfindRepayRouteResponse>() {
            @Override
            public void success(CfindRepayRouteResponse routeResponse) {
                CfindRepayRouteResponse.ResultBean result = routeResponse.getResult();
                if (result != null) {
                    repayment_status = result.getRepayment_status();
                    repayment_type = result.getRepayment_type();
                    repayment_text = result.getRepayment_text();
                    temporary_status = result.getTemporary_status();
                    temporary_type = result.getTemporary_type();
                    temporary_text = result.getTemporary_text();
                    if ("0".equals(repayment_status)) {
                        paybackBtn.setVisibility(View.GONE);
                    } else if ("1".equals(repayment_status) || "3".equals(repayment_status)) {
                        paybackBtn.setVisibility(View.VISIBLE);
                    }
                    if ("0".equals(temporary_status)) {
                        btn_change.setVisibility(View.GONE);
                    } else if ("1".equals(temporary_status) || "3".equals(temporary_status)) {
                        btn_change.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void failture(String tips) {

            }
        });
    }

    private void setOverdueStatus(boolean isOverdue) {
        if (isOverdue) {
            //已逾期
            top_layout.setBackgroundResource(R.drawable.red_roundshape);
            bottom_layout.setBackgroundResource(R.drawable.c_dark_red_rectangle);
            //逾期提示
            String overTip = String.format(getResources().getString(R.string.c_borrow_record_current_payment_tip),
                    0.5 + "%");
            tvOverdatetip.setVisibility(View.VISIBLE);
            tvOverdatetip.setText(Html.fromHtml(overTip));
            //逾期天数
            String overDay = String.format(getResources().getString(R.string.tv_overdue_day), overdueDay);
            mOverDay.setText(Html.fromHtml(overDay));
            String overPay = String.format(getResources().getString(R.string.tv_overdue_pay),
                    CMoneyEncoder.EncodeFormat(overdueAmount));
            mOverPay.setText(Html.fromHtml(overPay));
            llOverdatecontent.setVisibility(View.VISIBLE);
            llOverdueAmount.setVisibility(View.VISIBLE);
        } else {
            //未逾期
            top_layout.setBackgroundResource(R.drawable.roundshape);
            bottom_layout.setBackgroundResource(R.drawable.c_dark_blue_rectangle);
            tvOverdatetip.setVisibility(View.GONE);
            llOverdatecontent.setVisibility(View.GONE);
            llOverdueAmount.setVisibility(View.GONE);
        }
        //提示用户，当前所有应还金额，包括手续费
        setSumAmount(owedAmount);
    }

    /**
     * 快速还款防止重复点击
     */
    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            int id = view.getId();
            if (id == R.id.c_tv_show_record) {
                showRepayRecords();
            } else if (id == R.id.c_payback_money_btn) {
                if (checkInput()) {
                    loanRepay();
                }
            } else if (id == R.id.c_btn_change) {
                if ("3".equals(temporary_status)) {
                    if ("PAGE".equals(temporary_type)) {
                        huankuanzhanghuAction();
                    } else if ("TEXT".equals(temporary_type)) {
                        CLogUtil.showToast(QuickPaymentActivity.this, temporary_text);
                    }
                } else if ("1".equals(temporary_status)) {
                    CLogUtil.showToast(QuickPaymentActivity.this, temporary_text);
                }

            }
            //默认刷卡还款
            else if (id == R.id.lly_skhk) {
                lly_skhk.setBackgroundResource(R.drawable.c_repayment_type_press);
                lly_dkhk.setBackgroundResource(R.drawable.c_repayment_type_unpress);
                tv_skhk.setTextColor(getResources().getColor(R.color.white));
                tv_dkhk.setTextColor(getResources().getColor(R.color.secondblack));
                selectIdx = 0;
            } else if (id == R.id.lly_dkhk) {
                selectIdx = 1;
                lly_skhk.setBackgroundResource(R.drawable.c_repayment_type_unpress);
                lly_dkhk.setBackgroundResource(R.drawable.c_repayment_type_press);
                tv_skhk.setTextColor(getResources().getColor(R.color.secondblack));
                tv_dkhk.setTextColor(getResources().getColor(R.color.white));
            }
        }
    };

    //刷卡还款
    private void gotoSwipe() {
        try {
            Intent intent = new Intent(QuickPaymentActivity.this,
                    Class.forName(getApplicationContext().getPackageName() + ".convenience.CreateOrder_"));
            JSONObject jsObject = new JSONObject();
            jsObject.put("id", 70);//定死值70
            jsObject.put("ordertype", "闪付");
            jsObject.put("merchantId", "0001000008");
            jsObject.put("productId", "0000000000");
            jsObject.put("isNeedSign", true);
            jsObject.put("orderRemark", "刷卡还款");
            jsObject.put("orderAmt", CMoneyEncoder.EncodeFormat2(finalRepaymentMoney));
            jsObject.put("orderDesc", contract_id);// orderdesc 传输客户合同编号
            jsObject.put("account2", RyxcreditConfig.getRealName());//用户显示名称
            jsObject.put("disPlayContent", "瑞蚨小贷");//显示备注
            jsObject.put("isMustMpos", false);//是否必须为MPOS设备
//            intentMapVal.put("payee",customerId);//传输小贷客户编号
//            intentMapVal.put("cardIdx",cardIdx);//待确定
            jsObject.put("iscashCardIntercept", true);//是否进行磁条卡拦截
            jsObject.put("interfaceTag", "12");//12走SmartPayments小贷接口
            jsObject.put("servcode", "200029");
            jsObject.put("tradecode", "RYX003");
            intent.putExtra("orderStr", jsObject.toString());
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            CLogUtil.showToast(QuickPaymentActivity.this, "数据错误,请稍后再试!");
        }
    }

    /**
     * 还款账户点击
     */
    private void huankuanzhanghuAction() {
        CcardRepaymentRequest requestRepayment = new CcardRepaymentRequest();
        requestRepayment.setVersion(RyxcreditConfig.getVersion());
        httpsPost(this, requestRepayment, ReqAction.APPLICATION_CARD_REPAYMENT, CcardRepaymentResponse.class, new ICallback<CcardRepaymentResponse>() {
            @Override
            public void success(CcardRepaymentResponse ccardRepaymentResponse) {
                if (ccardRepaymentResponse.getResult() != null)
                    showBottomSheet2(ccardRepaymentResponse.getResult());
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(QuickPaymentActivity.this, tips + "");
            }
        });
    }

    /**
     * 弹出代扣银行卡选择
     */
    public void showBottomSheet2(final List<CcardRepaymentResponse.ResultBean> cardinfo) {
        if (cardinfo == null || cardinfo.size() == 0) {
            try {
                Intent intent = new Intent(QuickPaymentActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(QuickPaymentActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(QuickPaymentActivity.this).inflate(R.layout.c_impay_bottomsheet, null);
        final TextView titleView = (TextView) boottomView.findViewById(R.id.tv_title);
        titleView.setText("请选择还款账户");
        final TextView tv_add = (TextView) boottomView.findViewById(R.id.tv_addCard);
        tv_add.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                try {
                    Intent intent = new Intent(QuickPaymentActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                    startActivityForResult(intent, 997);
                    mBottomSheetDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final CRepayBankListAdapter bankLisAdapter = new CRepayBankListAdapter(QuickPaymentActivity.this, cardinfo, "");
        boottomListView.setAdapter(bankLisAdapter);
        ImageView imageView = (ImageView) boottomView.findViewById(R.id.imgview_close);
        imageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        boottomListView.setOnItemClickListener(new NoDoubleItemClickListener() {
            @Override
            public void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id) {
                CcardRepaymentResponse.ResultBean dkCardInfo = cardinfo.get(position);
                if (dkCardInfo == null) {
                    mBottomSheetDialog.dismiss();
                    return;
                }
                int status = dkCardInfo.getStatus();
                //卡片status=0不支持代扣；status=1支持代扣
                if (status != 1) {
                    return;
                }
                mBottomSheetDialog.dismiss();
                int auth_status = dkCardInfo.getAuth_status();
                int collection_status = dkCardInfo.getCollection_status();
                if (auth_status != 1 || collection_status != 1) {
                    cardAuth(dkCardInfo);
                    return;
                }
                repayBankNum = dkCardInfo.getCard_num();
                repayTBankCode = dkCardInfo.getBank_title_code();
//                dkbankName = dkCardInfo.getBank_name();
                initViewSattus();
            }
        });
        mBottomSheetDialog.contentView(boottomView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetDialog.show();
                    }
                });

            }
        }).start();
    }

    //初始化代扣卡内容
    private void initViewSattus() {
        tv_bankNo.setText(CStringUnit.cardJiaMi(repayBankNum));
        CBanksUtils.selectIcoidToImgView(QuickPaymentActivity.this, repayTBankCode, bankImg);
    }

    //代扣卡认证
    private void cardAuth(final CcardRepaymentResponse.ResultBean dkCardInfo) {
        //弹出认证代扣卡对话框
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            protected void onBuildDone(final Dialog dialog) {
                WindowManager wm = dialog.getWindow().getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                dialog.layoutParams(dm.widthPixels - CDensityUtil.dip2px(context, 32), ViewGroup.LayoutParams.WRAP_CONTENT);
                final EditText phoneEdt = (EditText) dialog.findViewById(R.id.c_bankPhoneNum);
                ImageView bankLogo = (ImageView) dialog.findViewById(R.id.c_banklogo);
                CBanksUtils.selectIcoidToImgView(QuickPaymentActivity.this, dkCardInfo.getBank_title_code(), bankLogo);
                TextView bankNoTv = (TextView) dialog.findViewById(R.id.c_dfBankNo);
                bankNoTv.setText(CStringUnit.cardJiaMi(dkCardInfo.getCard_num()));
                TextView agreeMentView = (TextView) dialog.findViewById(R.id.c_authcontract);
                agreeMentView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        try {
                            Intent intent = new Intent(QuickPaymentActivity.this, Class.forName(getApplicationContext().getPackageName() + ".activity.HtmlMessageActivity_"));
                            intent.putExtra("ccurl", agreementUrl + "?PayBankAcctName=" + RyxcreditConfig.getRealName()
                                    + "&ddBankName=" + dkCardInfo.getBank_name() + "&ddBankAcctNbr=" + CStringUnit.cardJiaMi(dkCardInfo.getCard_num()) + "");
                            intent.putExtra("title", "委托扣款服务协议");
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                CheckBox agreeCb = (CheckBox) dialog.findViewById(R.id.cb_agree);
                agreeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isAgreed = isChecked;
                    }
                });
                Button sureBtn = (Button) dialog.findViewById(R.id.c_sure_btn);
                dialog.findViewById(R.id.c_close).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        dialog.dismiss();
                    }
                });
                sureBtn.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (!isAgreed) {
                            CLogUtil.showToast(QuickPaymentActivity.this, "请先同意委托扣款服务协议!");
                            return;
                        }
                        dkCardPhoneNo = phoneEdt.getText().toString().trim();
                        if (dkCardPhoneNo.length() != 11) {
                            CLogUtil.showToast(QuickPaymentActivity.this, "请输入正确的银行预留手机号码!");
                            return;
                        }
                        doCardAuth(dialog, dkCardInfo);
                    }
                });

            }
        };
        builder.contentView(R.layout.c_dialog_auth_debit_card);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void doCardAuth(final Dialog dialog, final CcardRepaymentResponse.ResultBean dkCardInfo) {
        CdebitCardAuthRequest request = new CdebitCardAuthRequest();
        request.setCard_num(dkCardInfo.getCard_num());
        request.setReserved_phone_num(dkCardPhoneNo);
        httpsPost(this, request, ReqAction.APPLICATION_WITHHELD_CARD_CERTIFICATE, CdebitCardAuthReponse.class, new ICallback<CdebitCardAuthReponse>() {
            @Override
            public void success(CdebitCardAuthReponse cdebitCardResponse) {
                dialog.dismiss();
                boolean isResult = cdebitCardResponse.isResult();
                String msg = (isResult) ? "认证成功！" : "认证失败！";
                CLogUtil.showToast(QuickPaymentActivity.this, msg);
                if (isResult) {
                    repayBankNum = dkCardInfo.getCard_num();
                    repayTBankCode = dkCardInfo.getBank_title_code();
//                    dkbankName = dkCardInfo.getBank_name();
                    initViewSattus();
                }
            }

            @Override
            public void failture(String tips) {
                dialog.dismiss();
                CLogUtil.showToast(QuickPaymentActivity.this, tips);
            }
        });
    }


    private void initView() {
        tv_bankNo.setText(CStringUnit.cardJiaMi(repayBankNum));
        tv_status.setText(CConstants.getLoanStatus(loanStatus));
        if ("A".equals(loanStatus)) {
            //已结清灰色 快速还款按钮隐藏
            paybackBtn.setVisibility(View.INVISIBLE);
        } else if ("F".equals(loanStatus)) {
            //放款中蓝色 快速还款按钮隐藏
            paybackBtn.setVisibility(View.INVISIBLE);
        }
        mBorrowMoney.setText(CMoneyEncoder.EncodeFormat(String.valueOf(term_repay_amount)));
        //还款日
        tvRepayDate.setText(CDateUtil.DateToStrForRecord(CDateUtil.parseDate(repayDate, "yyyyMMdd")) + " 22:00");
        setRePaymentAmountListener(etRepaymentMoney);

        //当前应还金额，包括手续费
        if (CNummberUtil.parseDouble(owedAmount, 0) <= 0) {
            etRepaymentMoney.setText(CMoneyEncoder.EncodeFormat("0"));
        } else {
            etRepaymentMoney.setText(CMoneyEncoder.EncodeFormat(owedAmount));
        }
        if ("B".equals(loanStatus)) {
            //已逾期
            setOverdueStatus(true);
        } else {
            setOverdueStatus(false);
        }
        //当前未还合同金额，不包括手续费
        tv_unrepay_pay.setText(CMoneyEncoder.EncodeFormat(remainAmount));
        CBanksUtils.selectIcoidToImgView(QuickPaymentActivity.this, repayTBankCode, bankImg);
    }

    private void intListView(com.rey.material.app.Dialog dialog) {
        xRecyclerView = (XRecyclerView) dialog.findViewById(R.id.lv);
        xRecyclerView.setPullRefreshEnabled(false);//不启用下拉刷新
        xRecyclerView.setLoadingMoreEnabled(false);//不启用加载更多
        RecyclerViewHelper.init().setXRVLinearLayout(this, xRecyclerView);
        list.clear();
        mRepaymentRecordLl = (LinearLayout) dialog.findViewById(R.id.ll_repayment_record_null);
        if (recordsList.isEmpty()) {
            mRepaymentRecordLl.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < recordsList.size(); i++) {
                if ("S".equals(recordsList.get(i).getLoan_status())) {
                    list.add(recordsList.get(i));
                }
            }
            if (list.isEmpty()) {
                mRepaymentRecordLl.setVisibility(View.VISIBLE);
            } else {
                RePayRecordsAdapter adapter = new RePayRecordsAdapter
                        (list, this, R.layout.c_listview_repay_record);
                xRecyclerView.setAdapter(adapter);
            }
        }
    }

    /**
     * 当前应还金额
     *
     * @param money
     */
    private void setSumAmount(String money) {
        String s = String.format(getResources().getString(R.string.c_borrow_record_current_payment),
                CMoneyEncoder.EncodeFormat(money));
        currentRepaymentAmountTv.setText(Html.fromHtml(s));
    }

    private void showRepayRecords() {
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleNewDialogLight) {
            @Override
            protected void onBuildDone(final com.rey.material.app.Dialog dialog) {
                WindowManager wm = dialog.getWindow().getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                intListView(dialog);
                dialog.layoutParams(dm.widthPixels - CDensityUtil.dip2px(context, 32), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        builder.contentView(R.layout.c_dialog_repay_records);
        builder.title("还款记录");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    /**
     * 输入还款额校验
     *
     * @return
     */
    public boolean checkInput() {
        if ("TEXT".equals(repayment_type)) {
            CLogUtil.showToast(this, repayment_text);
            return false;
        }
        if ("1".equals(repayment_status)) {
            if ("TEXT".equals(repayment_type)) {
                CLogUtil.showToast(this, repayment_text);
            }
            return false;
        }
        String repaymentPrice = etRepaymentMoney.getText().toString().trim();
        if (repay_status != 1) {
            CLogUtil.showToast(this, "抱歉，借款成功后第二个自然日开始可进行还款!");
            return false;
        }
        if (TextUtils.isEmpty(repaymentPrice)) {
            CLogUtil.showToast(this, "请填写还款金额!");
            return false;
        }
//        if (!is_opened) {
//            CLogUtil.showToast(this, "抱歉，" + unborrowTime + "非交易时间！");
//            return false;
//        }
        //当前未还金额，包括手续费
        String maxRepaymentAmount = CMoneyEncoder.EncodeFormat(owedAmount);
        if (CNummberUtil.parseDouble(owedAmount, 0) <= 0) {
            CLogUtil.showToast(this, "您无需还款");
            return false;
        }

        if ("¥0.00".equals(repaymentPrice)) {
            CLogUtil.showToast(this, "还款金额必须大于零");
            return false;
        }
        if (repaymentPrice.replace(",", "").length() > 9) {
            CLogUtil.showToast(this, "还款金额超限");
            return false;
        }
        if (CMoneyEncoder.encodeToPost(repaymentPrice)
                .compareTo(
                        CMoneyEncoder.encodeToPost(maxRepaymentAmount)) > 0) {
            CLogUtil.showToast(this, "还款金额超出最大还款额");
            return false;
        }
        return true;
    }

    private void loanRepay() {
        finalRepaymentMoney = CMoneyEncoder.CleanFormat(etRepaymentMoney.getText().toString().trim());
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this,
                new RyxSimpleConfirmDialog.ConFirmDialogListener() {

                    @Override
                    public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        paybackBtn.setEnabled(false);
                        paybackBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                                QuickPaymentActivity.this, R.drawable.roundshape_gray));
                        ryxSimpleConfirmDialog.dismiss();
                        //刷卡还款
                        if (selectIdx == 0) {
                            gotoSwipe();
                        }
                        //代扣还款
                        else if (selectIdx == 1) {
                            gotoLoanRepay();
                        }

                    }

                    @Override
                    public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        ryxSimpleConfirmDialog.dismiss();
                    }
                });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("您确定要还款 " + finalRepaymentMoney + " 元吗?");
    }

    //代扣还款
    public void gotoLoanRepay() {
        LoanRepayRequest request = new LoanRepayRequest();
        request.setContract_id(contract_id);
        request.setRepayment_amount(CNummberUtil.parseDouble(finalRepaymentMoney, 0));
        request.setCard_num(repayBankNum);
        httpsPost(this, request, ReqAction.APPLICATION_LOAN_REPAY, LoanRepayResponse.class, new ICallback<LoanRepayResponse>() {
            @Override
            public void success(LoanRepayResponse loanRepayResponse) {
               int  result = loanRepayResponse.getResult();
                if (result==1){
                    CCommonDialog.showRepaymentOK(QuickPaymentActivity.this, "还款成功", "如已结清，借款状态会在24:00之后变更", new CCommonDialog.IMessage() {
                        @Override
                        public void callback() {
                            finish();
                        }
                    });
                    setResult(RESPONSE_QUICK_PAYMENT_CODE);
                }else if(result==0){
                    CCommonDialog.showRepaymentOK(QuickPaymentActivity.this, "还款失败", "", new CCommonDialog.IMessage() {
                        @Override
                        public void callback() {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void failture(String tips) {
                paybackBtn.setEnabled(true);
                paybackBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                        QuickPaymentActivity.this, R.drawable.roundshape));
                CCommonDialog.showRepaymentError(QuickPaymentActivity.this, "还款失败", tips);
            }
        });
    }

    /**
     * 监听还款金额输入框限定最多小数点后两位
     *
     * @param editText
     */
    public void setRePaymentAmountListener(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (".".equals(s.toString().trim().substring(0))) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!".".equals(s.toString().substring(1, 2))) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initSwiperChecked() {
        swiper_layout.setBackgroundResource(R.drawable.c_repayment_type_press);
        auto_layout.setBackgroundResource(R.drawable.c_repayment_type_unpress);
        auto_tv.setTextColor(getResources().getColor(R.color.threeblack));
        auto_comment_tv.setTextColor(getResources().getColor(R.color.threeblack));
        swiper_tv.setTextColor(getResources().getColor(R.color.white));
        swiper_comment_tv.setTextColor(getResources().getColor(R.color.white));
    }

    private void initAutoChecked() {
        auto_layout.setBackgroundResource(R.drawable.c_repayment_type_press);
        swiper_layout.setBackgroundResource(R.drawable.c_repayment_type_unpress);
        auto_tv.setTextColor(getResources().getColor(R.color.white));
        auto_comment_tv.setTextColor(getResources().getColor(R.color.white));
        swiper_tv.setTextColor(getResources().getColor(R.color.threeblack));
        swiper_comment_tv.setTextColor(getResources().getColor(R.color.threeblack));
    }

    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        if (!isShow && etRepaymentMoney.isFocused()) {
            String money = etRepaymentMoney.getText().toString().trim();
            if (TextUtils.isEmpty(money)) {
                return;
            }
            //格式化输入的还款额度
            etRepaymentMoney.setText(CMoneyEncoder.EncodeFormat(money));
            etRepaymentMoney.setSelection(etRepaymentMoney.getText().length());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            paybackBtn.setEnabled(true);
            paybackBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                    QuickPaymentActivity.this, R.drawable.roundshape));
            if (data != null &&data.hasExtra("swiperresult")&&
                    "01".equals(data.getStringExtra("swiperresult"))) {
                setResult(RESPONSE_QUICK_PAYMENT_CODE);
                finish();
            } else {
                initData();
            }

        }
    }
}
