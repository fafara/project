package com.ryx.ryxcredit.ryd;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.NoDoubleItemClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.activity.SureBorrowActivity;
import com.ryx.ryxcredit.adapter.CPayBankListAdapter;
import com.ryx.ryxcredit.adapter.CRepayBankListAdapter;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.cardpayment.CcardPaymentRequest;
import com.ryx.ryxcredit.beans.bussiness.cardpayment.CcardPaymentResponse;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentRequest;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentResponse;
import com.ryx.ryxcredit.beans.bussiness.debitcard.CdebitCardAuthReponse;
import com.ryx.ryxcredit.beans.bussiness.debitcard.CdebitCardAuthRequest;
import com.ryx.ryxcredit.beans.bussiness.loanapply.CfindBorrowRouteResponse;
import com.ryx.ryxcredit.beans.bussiness.loancalculate.CLoanCalculateResquest;
import com.ryx.ryxcredit.beans.bussiness.loancalculate.CLoanCalulateResponse;
import com.ryx.ryxcredit.beans.bussiness.product.CfindRouteRequest;
import com.ryx.ryxcredit.beans.bussiness.product.CproductRequest;
import com.ryx.ryxcredit.beans.bussiness.product.CproductResponse;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CDensityUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.utils.CStringUnit;
import com.ryx.ryxcredit.views.KeyboardChangeListener;

import java.util.Date;
import java.util.List;

import static com.ryx.ryxcredit.R.id.img_bank;
import static com.ryx.ryxcredit.R.id.img_banklogo;
import static com.ryx.ryxcredit.RyxcreditConfig.context;

/**
 * 瑞易贷我要借款
 */
public class BorrowingMoneyActivity extends BaseActivity implements KeyboardChangeListener.KeyBoardListener {

    //借款金额
    private EditText amountMoneyEt;

    private Button borrowBtn;
    private TextView chooseCard;

    private LinearLayout deadLine7LinearLayout;
    private LinearLayout deadLine14LinearLayout;
    private LinearLayout llLoanterm;
    private TextView deadLine7Tv;
    private TextView deadLine14Tv;
    //查看支持的银行列表
    private TextView searchBankListTv;
    private TextView tvProductName;
    private ImageView img_ask;
    //收款账户
    private TextView shoukuanzhanghuBtn;
    //还款账户
    private TextView huankuanzhanghuBtn;
    private CproductResponse.ResultBean product;
    private CLoanCalulateResponse.ResultBean loanCal;
    private KeyboardChangeListener mKeyboardChangeListener;
    private TextView tvTotalMoney, tvTotalMoney1, tvRepaymentDate;
    private TextView tvCardno1, tvCardno2, tv_bind;
    private String cardno1 = "", cardno2 = "", dkbankName = "";
    private List<CcardPaymentResponse.ResultBean> cardPayList;//信用卡列表
    private int selectIdx = 0;//选中的子产品索引

    private NestedScrollView scoll;
    private ImageView dk_banklogo;
    private TextView btn_accountCh;
    private ImageView df_banklogo;
    private String dkTbankCode, dfTbankCode;
    //代扣卡认证
    private String dkCardPhoneNo;//银行卡预留卡号
    private boolean isAgreed = true;//用户是否同意委托代扣协议,默认同意
    private String bankUrl;
    private double avilableAmount;
    private boolean is_opened;
    private boolean isTimeout = false;//试算超时
    private int times = 0;//超时次数
    private String unborrowTime;
    private TextView tv_dflist;

    private String paymentStatus;//确认借款按钮
    private String paymentType;//确认借款类型
    private String paymentText;//确认借款提示语或页面

    private boolean isJumped = false;//是否跳转到了确认借款页面
    private String agreement_id;//合同版本号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_borrowing_money);
        setTitleLayout(getResources().getString(R.string.c_borrowing_money), true, false);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("avilableAmount"))
                avilableAmount = intent.getDoubleExtra("avilableAmount", 0);
            if (intent.hasExtra("is_opened"))
                is_opened = intent.getBooleanExtra("is_opened", true);
            if (intent.hasExtra("unborrowTime"))
                unborrowTime = intent.getStringExtra("unborrowTime");
        }
        initViews();
        initListener();
        requestData();
        cardPaymentList();
        getRoute();
    }

    private void getRoute() {
        final CfindRouteRequest request = new CfindRouteRequest();
        request.setKey("payment_page");
        httpsPost(this, request, ReqAction.APPLICATION_ROUTE, CfindBorrowRouteResponse.class, new ICallback<CfindBorrowRouteResponse>() {
            @Override
            public void success(CfindBorrowRouteResponse routeResponse) {
                CfindBorrowRouteResponse.ResultBean result = routeResponse.getResult();
                if (result != null) {
                    paymentStatus = result.getPayment_status();
                    paymentType = result.getPayment_type();
                    paymentText = result.getPayment_text();
                    if ("0".equals(paymentStatus)) {
                        borrowBtn.setVisibility(View.GONE);
                    } else if ("1".equals(paymentStatus) || "3".equals(paymentStatus)) {
                        borrowBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failture(String tips) {

            }
        });
    }

    private void initViews() {
        scoll = (NestedScrollView) findViewById(R.id.scoll);
        chooseCard = (TextView) findViewById(R.id.c_chooseCard);
        chooseCard.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                huankuanzhanghuAction();
            }
        });
        img_ask = (ImageView) findViewById(R.id.img_ask);
        img_ask.setOnClickListener(clickListener);
        dk_banklogo = (ImageView) findViewById(img_banklogo);
        df_banklogo = (ImageView) findViewById(img_bank);
        tv_dflist = (TextView) findViewById(R.id.c_tv_dfbank_list);
        tv_dflist.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //查看33家代付卡支持列表，注意url为空的情况
                if (product != null) {
                    bankUrl = product.getPayment_bank_url();
                }
                searchBankList(bankUrl);
            }
        });
        btn_accountCh = (TextView) findViewById(R.id.c_btn_huankuanzhanghu_change);
        btn_accountCh.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                huankuanzhanghuAction();
            }
        });
    }

    /**
     * 监听借款金额输入框限定最多小数点后两位
     *
     * @param editText
     */
    public void setBorrowListener(final EditText editText) {
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
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
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

            }
        });

    }

    /**
     * 获取产品详情
     */
    private void requestData() {
        CproductRequest request = new CproductRequest();
        request.setProduct_id("8007");
        request.setSub_product_id("600101");
        httpsPost(this, request, ReqAction.APPLICATION_PRODUCT_DETAIL, CproductResponse.class, new ICallback<CproductResponse>() {
            @Override
            public void success(CproductResponse cproductResponse) {
                product = cproductResponse.getResult();
                if (product != null)
                    bindView(product);
            }

            @Override
            public void failture(String tips) {

            }
        });
    }

    /**
     * 绑定产品详情view
     *
     * @param bean
     */
    private void bindView(CproductResponse.ResultBean bean) {
        tvProductName.setText(bean.getProduct_descr());
        if (bean.getSub_products().size() == 0) {
            CLogUtil.showToast(BorrowingMoneyActivity.this, "没有产品");
        } else if (bean.getSub_products().size() == 1) {
            deadLine7Tv.setText(bean.getSub_products().get(0).getTerm_spans() + unit2text(bean.getSub_products().get(0).getSpan_unit()));
            deadLine14LinearLayout.setVisibility(View.GONE);
        } else if (bean.getSub_products().size() == 2) {
            deadLine7Tv.setText(bean.getSub_products().get(0).getTerm_spans() + unit2text(bean.getSub_products().get(0).getSpan_unit()));
            deadLine14Tv.setText(bean.getSub_products().get(1).getTerm_spans() + unit2text(bean.getSub_products().get(1).getSpan_unit()));
            amountMoneyEt.setHint("最低金额" + bean.getFloor_purchasable_amount() + "元");
        }
    }

    /**
     * 单位标签转汉字
     *
     * @param unit
     * @return
     */
    private String unit2text(String unit) {
        if ("M".equals(unit))
            return "个月";
        if ("D".equals(unit))
            return "天";
        return "";
    }

    /**
     * UI 监听器
     */
    private void initListener() {
        initViewRes();
        deadLine7LinearLayout.setOnClickListener(clickListener);
        deadLine14LinearLayout.setOnClickListener(clickListener);
        searchBankListTv.setOnClickListener(clickListener);
        shoukuanzhanghuBtn.setOnClickListener(clickListener);
        huankuanzhanghuBtn.setOnClickListener(clickListener);
        borrowBtn.setOnClickListener(clickListener);
        mKeyboardChangeListener = new KeyboardChangeListener(this);
        mKeyboardChangeListener.setKeyBoardListener(this);
    }

    /**
     * 初始化资源
     */
    private void initViewRes() {
        amountMoneyEt = (EditText) findViewById(R.id.c_borrow_money_amount_et);
        borrowBtn = (Button) findViewById(R.id.c_borrow_money_btn);
        tvProductName = (TextView) findViewById(R.id.c_tv_product_name);
        deadLine7LinearLayout = (LinearLayout) findViewById(R.id.c_lly_dead_line_7);
        deadLine14LinearLayout = (LinearLayout) findViewById(R.id.c_lly_dead_line_14);
        deadLine7Tv = (TextView) findViewById(R.id.c_tv_dead_line_7);
        deadLine14Tv = (TextView) findViewById(R.id.c_tv_dead_line_14);
        searchBankListTv = (TextView) findViewById(R.id.c_tv_search_bank_list);
        shoukuanzhanghuBtn = (TextView) findViewById(R.id.c_btn_shoukuanzhanghu_change);
        huankuanzhanghuBtn = (TextView) findViewById(R.id.c_btn_huankuanzhanghu_change);
        tvTotalMoney = (TextView) findViewById(R.id.c_tv_total_amount);
        tvTotalMoney1 = (TextView) findViewById(R.id.c_tv_total_amount1);
        tvRepaymentDate = (TextView) findViewById(R.id.c_tv_repayment_date);
        llLoanterm = (LinearLayout) findViewById(R.id.c_ll_loanterm);
        tvCardno1 = (TextView) findViewById(R.id.c_tv_cardno1);
        tvCardno2 = (TextView) findViewById(R.id.c_tv_cardno2);
        tv_bind = (TextView) findViewById(R.id.c_tv_bind);
        setBorrowListener(amountMoneyEt);
    }

    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            if (v.getId() == R.id.c_lly_dead_line_7) {
                deadLine7LinearLayout.setBackgroundResource(R.drawable.c_repayment_type_press);
                deadLine14LinearLayout.setBackgroundResource(R.drawable.c_repayment_type_unpress);
                deadLine7Tv.setTextColor(getResources().getColor(R.color.white));
                deadLine14Tv.setTextColor(getResources().getColor(R.color.secondblack));
                selectIdx = 0;
                times = 0;
                productCalculate(2, false, false);

            } else if (v.getId() == R.id.c_lly_dead_line_14) {
                deadLine7LinearLayout.setBackgroundResource(R.drawable.c_repayment_type_unpress);
                deadLine14LinearLayout.setBackgroundResource(R.drawable.c_repayment_type_press);
                deadLine14Tv.setTextColor(getResources().getColor(R.color.white));
                deadLine7Tv.setTextColor(getResources().getColor(R.color.secondblack));
                selectIdx = 1;
                times = 0;
                productCalculate(2, false, false);
            } else if (v.getId() == R.id.c_tv_search_bank_list) {
                if (product != null) {
                    bankUrl = product.getRepayment_bank_url();
                }
                searchBankList(bankUrl);
            } else if (v.getId() == R.id.c_borrow_money_btn) {
                times = 0;
                borrowSure();
            } else if (v.getId() == R.id.c_btn_huankuanzhanghu_change) {
                huankuanzhanghuAction();
            } else if (v.getId() == R.id.c_btn_shoukuanzhanghu_change) {
                shoukuanzhanghuAction();
            } else if (v.getId() == R.id.img_ask) {
                showHelpPopWindow();
            }
        }
    };

    /**
     * 借款按钮
     */
    private void borrowSure() {
        if("TEXT".equals(paymentType)){
            CLogUtil.showToast(this, paymentText);
            return;
        }
        String amountStr = amountMoneyEt.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            CLogUtil.showToast(this, "请输入借款到账金额！");
            return;
        }
        Intent intent = new Intent(this, SureBorrowActivity.class);
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(cardno1.trim())) {
            CLogUtil.showToast(this, "请完善您的收款账户信息！");
            return;
        }
        if (TextUtils.isEmpty(tvCardno2.getText().toString().trim())) {
            CLogUtil.showToast(this, "请完善您的还款账户信息！");
            return;
        }
//        if (!is_opened) {
//            CLogUtil.showToast(BorrowingMoneyActivity.this, "抱歉，" + unborrowTime + "非交易时间!");
//            return;
//        }
        if (product != null) {
            float loanAmount = CNummberUtil.parseFloat(amountStr, 0);
            if (loanAmount < product.getFloor_purchasable_amount()) {
                CLogUtil.showToast(BorrowingMoneyActivity.this, "金额不能低于" + product.getFloor_purchasable_amount());
                return;
            }
            if (loanAmount > product.getCeiling_purchasable_amount()) {
                CLogUtil.showToast(BorrowingMoneyActivity.this, "金额不能高于" + product.getCeiling_purchasable_amount());
                return;
            }
            bundle.putString("product_descr", product.getProduct_descr());
            bundle.putString("product_id", product.getProduct_id());
            bundle.putString("sub_product_id", product.getSub_products().get(selectIdx).getSub_product_id());
            //服务协议
            bundle.putString("service_agreement_url", product.getService_agreement_url());
            //借款合同
            bundle.putString("contract_url", product.getContract_url() + "");
            //协议版本号
            bundle.putDouble("contract_version", product.getContract_version());
            //委托代扣
            bundle.putString("dkagreement_url", product.getAgreement_url() + "?PayBankAcctName=" + RyxcreditConfig.getRealName()
                    + "&ddBankName=" + dkbankName + "&ddBankAcctNbr=" + CStringUnit.cardJiaMi(cardno2) + "");
        }
        if (!TextUtils.isEmpty(amountStr))
            bundle.putDouble("loan_amount", CNummberUtil.parseDouble(amountStr, 0));
        //试算接口请求超时
        if (isTimeout) {
            productCalculate(0, true, true);
            return;
        }
        if (loanCal != null) {
            bundle.putDouble("cost_amount", loanCal.getCost_amount());
            bundle.putDouble("total_amount", loanCal.getTotal_amount());
            String endDate = CDateUtil.DateToStrForRecord(
                    CDateUtil.parseDate(loanCal.getRepayments().get(0).getExpired_date(), "yyyyMMdd"));
            String beginDate = CDateUtil.DateToStrForRecord(
                    CDateUtil.parseDate(loanCal.getLoan_date(), "yyyyMMdd"));
            bundle.putString("date", beginDate + " - " + endDate);
        } else {
            productCalculate(0, true, true);
//            CLogUtil.showToast(this, "借款信息有误，请重试！");
            return;
        }
        //收款账号
        bundle.putString("payment_card_num", cardno1);
        //代付卡
        bundle.putString("payment_bankCode", dfTbankCode);
        //代扣卡
        bundle.putString("repayment_bankCode", dkTbankCode);
        //付款账号
        bundle.putString("repayment_card_num", cardno2);
        //还款保证金（利息）
        bundle.putString("Sub_cost_amount", String.valueOf(loanCal.getSub_cost_amount()));
        bundle.putString("brinterest", String.valueOf(loanCal.getSub_cost_rate()));
        //信用咨询及管理服务协议
        bundle.putString("money_manage_url", product.getMoney_manage_url());
        //拆分的服务费率
        bundle.putString("other_cost_rate", String.valueOf(loanCal.getOther_cost_rate()));
        //拆分的服务费率
        bundle.putString("other_cost_amount", String.valueOf(loanCal.getOther_cost_amount()));
        if (selectIdx == 0) {
            bundle.putInt("borrowterm", 7);
        } else {
            bundle.putInt("borrowterm", 14);
        }
        bundle.putString("agreement_id",agreement_id);
        bundle.putDouble("overdue_interest_rate",loanCal.getOverdue_interest_rate());
        bundle.putDouble("other_overdue_interest_rate",loanCal.getOther_overdue_interest_rate());
        intent.putExtras(bundle);
        isJumped = true;
        startActivityForResult(intent, 999);
    }

    public void showHelpPopWindow() {
        if(loanCal==null){
            return;
        }
        View view =  LayoutInflater.from(this).inflate(R.layout.c_popview_borrow_help,null);
        //到账金额
        TextView arriveTv = (TextView)view.findViewById(R.id.tv_arriveAmount);
        String amountStr = amountMoneyEt.getText().toString().trim();
        arriveTv.setText(amountStr);
        //服务费
        TextView interestTvv = (TextView)view.findViewById(R.id.tv_interest);
        interestTvv.setText(String.valueOf(loanCal.getSub_cost_amount()));
        //利息
        TextView  interestTv= (TextView)view.findViewById(R.id.tv_service_fee);
        interestTv.setText(String.valueOf(loanCal.getOther_cost_amount()));
        CLogUtil.showLog("arriveTv---",amountStr+"---"+String.valueOf(loanCal.getOther_cost_amount())
                +"---"+String.valueOf(loanCal.getSub_cost_amount()));
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        Dialog.Builder builder = new Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final Dialog dialog = builder.build(BorrowingMoneyActivity.this);
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isJumped = false;
        if (resultCode == 999) {
            this.finish();
        } else if (requestCode == 996) {
            cardPaymentList();
        } else if (requestCode == 997) {

        } else if (requestCode == 998) {

        }
    }

    //查看支持的银行列表
    private void searchBankList(final String bankUrl) {

        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            protected void onBuildDone(final Dialog dialog) {
                WindowManager wm = dialog.getWindow().getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                WebView webView = (WebView) dialog.findViewById(R.id.bank_list_webview);
//                webView.loadUrl(CConstants.BANK_LIST_URL);
                webView.loadUrl(bankUrl);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                dialog.layoutParams(dm.widthPixels - CDensityUtil.dip2px(context, 32), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        builder.contentView(R.layout.c_dialog_bank_list);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }


    /**
     * 收款账户点击
     */
    private void shoukuanzhanghuAction() {
        showBottomSheet1(cardPayList);
    }

    private void cardPaymentList() {
        CcardPaymentRequest requestPayment = new CcardPaymentRequest();
        requestPayment.setVersion(RyxcreditConfig.getVersion());
        httpsPost(this, requestPayment, ReqAction.APPLICATION_CARD_PAYMENT, CcardPaymentResponse.class, new ICallback<CcardPaymentResponse>() {
            @Override
            public void success(CcardPaymentResponse ccardPaymentResponse) {
                cardPayList = ccardPaymentResponse.getResult();
                if (cardPayList != null && cardPayList.size() == 0) {
                    tvCardno1.setText("你尚未绑定卡");
                    tv_bind.setVisibility(View.VISIBLE);
                    tv_bind.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
                            try {
                                Intent intent = new Intent(BorrowingMoneyActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                                startActivityForResult(intent, 996);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (cardPayList != null) {
                    tv_bind.setVisibility(View.GONE);
                    CcardPaymentResponse.ResultBean cardBean = cardPayList.get(0);
                    dfTbankCode = cardBean.getBank_title_code();
                    shoukuanzhanghuBtn.setVisibility(View.VISIBLE);
                    df_banklogo.setVisibility(View.VISIBLE);
                    cardno1 = cardBean.getCard_num();
                    tvCardno1.setText(CStringUnit.cardJiaMi(cardno1));
                    CBanksUtils.selectIcoidToImgView(BorrowingMoneyActivity.this, dfTbankCode, df_banklogo);

                }
            }

            @Override
            public void failture(String tips) {
            }
        });
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
                CLogUtil.showToast(BorrowingMoneyActivity.this, tips + "");
            }
        });
    }

    /**
     * 产品试算
     *
     * @param isBorrow  是否是点击的借款按钮
     * @param showToast 是否需要提示用户输入的金额是空
     * @maxTimes 超时后请求网络的次数
     */
    private void productCalculate(final int maxTimes, boolean isBorrow, boolean showToast) {
        //清空计算出来的合同金额，还款金额，还款日期
        setBlankCal();
        loanCal = null;
        if (product == null || product.getSub_products() == null || product.getSub_products().size() <= selectIdx)
            return;
        CproductResponse.ResultBean.SubProductsBean subProductsBean = product.getSub_products().get(selectIdx);
        if (TextUtils.isEmpty(amountMoneyEt.getText().toString().trim()) || "0".equals(amountMoneyEt.getText().toString())) {
            if (showToast)
                CLogUtil.showToast(BorrowingMoneyActivity.this, "请输入金额");
            return;
        }
        float loanAmount = CNummberUtil.parseFloat(amountMoneyEt.getText().toString().trim(), 0);
        if (loanAmount < product.getFloor_purchasable_amount()) {
            CLogUtil.showToast(BorrowingMoneyActivity.this, "金额不能低于" + product.getFloor_purchasable_amount());
            return;
        }

        if (loanAmount > avilableAmount) {
            CLogUtil.showToast(BorrowingMoneyActivity.this, "金额不能高于" + avilableAmount);
            return;
        }
        caculateMoney(subProductsBean, loanAmount, maxTimes, isBorrow);
    }

    private void caculateMoney(final CproductResponse.ResultBean.SubProductsBean subProductsBean, final float loanAmount, final int maxtimes, final boolean isBorrow) {
        CLoanCalculateResquest request = new CLoanCalculateResquest();
        request.setProduct_id(product.getProduct_id());
        request.setSub_product_id(subProductsBean.getSub_product_id());
        request.setTerm(subProductsBean.getTerm());
        request.setLoan_amount(loanAmount);
        httpsPost(this, request, ReqAction.APPLICATION_LOAN_CALCULATE, CLoanCalulateResponse.class, new ICallback<CLoanCalulateResponse>() {
            @Override
            public void success(CLoanCalulateResponse cLoanCalulateResponse) {
//                CLogUtil.showToast(BorrowingMoneyActivity.this, CJSONUtils.getInstance().toJSONString(cLoanCalulateResponse));
                loanCal = cLoanCalulateResponse.getResult();
                agreement_id = loanCal.getAgreement_id();
                bindLoanCal();
                isTimeout = false;
                times = 0;
                if (isBorrow) {
                    borrowSure();
                }
            }

            @Override
            public void failture(String tips) {
                setBlankCal();
                //超时后重新请求试算接口
                isTimeout = true;
                if (times < maxtimes) {
                    times = times + 1;
                    caculateMoney(subProductsBean, loanAmount, maxtimes, isBorrow);
                }
            }
        });
    }

    private void setBlankCal() {
        tvTotalMoney.setText("借款合同金额¥0.00");
        tvTotalMoney1.setText("¥0.00");
        tvRepaymentDate.setText("");
    }

    /**
     * 试算结果绑定
     */
    private void bindLoanCal() {
        if (null != loanCal) {
            tvTotalMoney.setText("借款合同金额¥" + loanCal.getTotal_amount() + "");
            tvTotalMoney1.setText("¥" + loanCal.getTotal_amount() + "");
            String dateStr = loanCal.getRepayments().get(0).getExpired_date();
            Date expiredDate = null;
            if (!TextUtils.isEmpty(dateStr)) {
                expiredDate = CDateUtil.parseDate(dateStr, "yyyyMMdd");
            }
            if (expiredDate != null)
                tvRepaymentDate.setText(CDateUtil.DateToStrForRecord(expiredDate) + " 22:00 前");
        }
    }

    /**
     * 弹出代扣银行卡选择
     */
    public void showBottomSheet2(final List<CcardRepaymentResponse.ResultBean> cardinfo) {
        if (cardinfo == null || cardinfo.size() == 0) {
            try {
                Intent intent = new Intent(BorrowingMoneyActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(BorrowingMoneyActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(BorrowingMoneyActivity.this).inflate(R.layout.c_impay_bottomsheet, null);
        final TextView titleView = (TextView) boottomView.findViewById(R.id.tv_title);
        titleView.setText("请选择还款账户");
        final TextView tv_add = (TextView) boottomView.findViewById(R.id.tv_addCard);
        tv_add.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                try {
                    Intent intent = new Intent(BorrowingMoneyActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                    startActivityForResult(intent, 997);
                    mBottomSheetDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final CRepayBankListAdapter bankLisAdapter = new CRepayBankListAdapter(BorrowingMoneyActivity.this, cardinfo, "");
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
                cardno2 = dkCardInfo.getCard_num();
                dkTbankCode = dkCardInfo.getBank_title_code();
                dkbankName = dkCardInfo.getBank_name();
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
        tvCardno2.setText(CStringUnit.cardJiaMi(cardno2));
        tvCardno2.setVisibility(View.VISIBLE);
        dk_banklogo.setVisibility(View.VISIBLE);
        btn_accountCh.setVisibility(View.VISIBLE);
        chooseCard.setVisibility(View.GONE);
        CBanksUtils.selectIcoidToImgView(BorrowingMoneyActivity.this, dkTbankCode, dk_banklogo);
    }

    //代扣卡认证
    private void cardAuth(final CcardRepaymentResponse.ResultBean cardInfo) {
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
                TextView bankNoTv = (TextView) dialog.findViewById(R.id.c_dfBankNo);
                bankNoTv.setText(CStringUnit.cardJiaMi(cardInfo.getCard_num()));
                CBanksUtils.selectIcoidToImgView(BorrowingMoneyActivity.this, cardInfo.getBank_title_code(), bankLogo);
                TextView agreeMentView = (TextView) dialog.findViewById(R.id.c_authcontract);
                agreeMentView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        try {
                            Intent intent = new Intent(BorrowingMoneyActivity.this, Class.forName(getApplicationContext().getPackageName() + ".activity.HtmlMessageActivity_"));
                            intent.putExtra("ccurl", product.getAgreement_url() + "?PayBankAcctName=" + RyxcreditConfig.getRealName()
                                    + "&ddBankName=" + cardInfo.getBank_name() + "&ddBankAcctNbr=" + CStringUnit.cardJiaMi(cardInfo.getCard_num()) + "");
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
                            CLogUtil.showToast(BorrowingMoneyActivity.this, "请先同意委托扣款服务协议!");
                            return;
                        }
                        dkCardPhoneNo = phoneEdt.getText().toString().trim();
                        if (dkCardPhoneNo.length() != 11) {
                            CLogUtil.showToast(BorrowingMoneyActivity.this, "请输入正确的银行预留手机号码!");
                            return;
                        }
                        doCardAuth(dialog, cardInfo);
                    }
                });

            }
        };
        builder.contentView(R.layout.c_dialog_auth_debit_card);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void doCardAuth(final Dialog dialog, final CcardRepaymentResponse.ResultBean cardInfo) {
        CdebitCardAuthRequest request = new CdebitCardAuthRequest();
        request.setCard_num(cardInfo.getCard_num());
        request.setReserved_phone_num(dkCardPhoneNo);
        httpsPost(this, request, ReqAction.APPLICATION_WITHHELD_CARD_CERTIFICATE, CdebitCardAuthReponse.class, new ICallback<CdebitCardAuthReponse>() {
            @Override
            public void success(CdebitCardAuthReponse cdebitCardResponse) {
                dialog.dismiss();
                boolean isResult = cdebitCardResponse.isResult();
                String msg = (isResult) ? "认证成功！" : "认证失败！";
                CLogUtil.showToast(BorrowingMoneyActivity.this, msg);
                if (isResult) {
                    cardno2 = cardInfo.getCard_num();
                    dkTbankCode = cardInfo.getBank_title_code();
                    dkbankName = cardInfo.getBank_name();
                    initViewSattus();
                }
            }

            @Override
            public void failture(String tips) {
                dialog.dismiss();
                CLogUtil.showToast(BorrowingMoneyActivity.this, tips);
            }
        });
    }

    /**
     * 弹出代付银行卡选择
     */
    public void showBottomSheet1(final List<CcardPaymentResponse.ResultBean> cardinfo) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(BorrowingMoneyActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(BorrowingMoneyActivity.this).inflate(R.layout.c_impay_bottomsheet, null);
        final TextView titleView = (TextView) boottomView.findViewById(R.id.tv_title);
        titleView.setText("请选择收款账户");
        final TextView tv_add = (TextView) boottomView.findViewById(R.id.tv_addCard);
        tv_add.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                try {
                    Intent intent = new Intent(BorrowingMoneyActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                    startActivityForResult(intent, 996);
                    mBottomSheetDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final CPayBankListAdapter bankLisAdapter = new CPayBankListAdapter(BorrowingMoneyActivity.this, cardinfo, "");
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
                int status = cardinfo.get(position).getStatus();
                //卡片status=0不支持代付；status=1支持代付
                if (status != 1) {
                    return;
                }
                cardno1 = cardinfo.get(position).getCard_num();
                dfTbankCode = cardinfo.get(position).getBank_title_code();
                tvCardno1.setText(CStringUnit.cardJiaMi(cardno1));
                mBottomSheetDialog.dismiss();
                CBanksUtils.selectIcoidToImgView(BorrowingMoneyActivity.this, cardinfo.get(position).getBank_title_code(), df_banklogo);
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

    /**
     * call back
     *
     * @param isShow         true is show else hidden
     * @param keyboardHeight keyboard height
     */
    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        if (!isShow && amountMoneyEt.isFocused()&&!isJumped) {
            times = 0;
            productCalculate(2, false, false);
        }
    }

}
