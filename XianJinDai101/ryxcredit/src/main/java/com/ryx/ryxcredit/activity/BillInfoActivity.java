package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.supplementarycredit.CSupplementaryCreditRequest;
import com.ryx.ryxcredit.beans.bussiness.supplementarycredit.CSupplementaryCreditResponse;
import com.ryx.ryxcredit.beans.pojo.CCard;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.utils.CStringUnit;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 账单信息
 */
public class BillInfoActivity extends BaseActivity {
    //银行卡图片
    private ImageView bankIconIv;
    //银行名称
    private TextView bankNameTv;
    //银行卡号
    private TextView bankNumberTv;

    //总额度
    private EditText totalAmountEt;
    //当前已出账单金额
    private EditText currentBalanceEt;
    //账单日
    private TextView statementDateTv;
    //还款日
    private TextView paymentDueDateTv;
    private Button completeBtn;
    //是否为新增

    private String cardNumber;

    public static final int CARD_RESULT_CODE = 0X1004;

    private String totalAmount, balanceAmount;
    private String statetDate, rePayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_bill_info);
        setTitleLayout("账单信息", true, false);
        CCard ccd = (CCard) getIntent().getSerializableExtra("data");
        initRes();

        if (ccd != null) {
            cardNumber = ccd.getCard_num();
            if (!TextUtils.isEmpty(ccd.getBank_name()))
                bankNameTv.setText(ccd.getBank_name() + "");
            if (!TextUtils.isEmpty(ccd.getCard_num()))
                bankNumberTv.setText(CStringUnit.cardJiaMi(ccd.getCard_num()) + "");
            if (!TextUtils.isEmpty(ccd.getTotal_amount()))
                totalAmountEt.setText(ccd.getTotal_amount() + "");
            if (!TextUtils.isEmpty(ccd.getCurrent_balance()))
                currentBalanceEt.setText(ccd.getCurrent_balance() + "");
            if (!TextUtils.isEmpty(ccd.getStatement_date())) {
                Date expiredDate = CDateUtil.parseDate(ccd.getStatement_date(), "yyyyMMdd");
                statetDate = CDateUtil.DateToStrForDay(expiredDate);
                statementDateTv.setText("每月" + statetDate + "日");
            }
            if (!TextUtils.isEmpty(ccd.getPayment_due_date())) {
                Date expiredDate = CDateUtil.parseDate(ccd.getPayment_due_date(), "yyyyMMdd");
                rePayDate = CDateUtil.DateToStrForDay(expiredDate);
                paymentDueDateTv.setText("每月" + rePayDate + "日");
            }
            String bankCode = ccd.getBank_title_code();
            if (!TextUtils.isEmpty(bankCode)) {
                CBanksUtils.selectIcoidToImgView(this,ccd.getBank_title_code(),bankIconIv);
            }
        }
        initListen();
    }

    /**
     * 监听账单金额输入框限定最多小数点后两位
     *
     * @param editText
     */
    public void setBillListener(final EditText editText) {
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

            }
        });
    }

    /**
     * 监听器
     */

    private void initListen() {
        statementDateTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CCommonDialog.showPayDays(BillInfoActivity.this, "日期选择", new CCommonDialog.ICommonChooseDateListen() {
                    @Override
                    public void chooseDate(String date) {
                        statetDate = date;
                        statementDateTv.setText("每月" + statetDate + "日");
                    }
                });
               /* CCommonDialog.showPayDays(BillInfoActivity.this, "日期选择", new CCommonDialog.ICommonChooseDateListen() {
                    @Override
                    public void chooseDate(String date) {
                        statementDateTv.setText(date);
                    }
                });*/
            }
        });
        paymentDueDateTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CCommonDialog.showPayDays(BillInfoActivity.this, "日期选择", new CCommonDialog.ICommonChooseDateListen() {
                    @Override
                    public void chooseDate(String date) {
                        rePayDate = date;
                        paymentDueDateTv.setText("每月" + rePayDate + "日");
                    }
                });
               /* CCommonDialog.showPayDays(BillInfoActivity.this, "日期选择", new CCommonDialog.ICommonChooseDateListen() {
                    @Override
                    public void chooseDate(String date) {
                        paymentDueDateTv.setText(date);
                    }
                });*/
            }
        });
        completeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                completAction();
            }
        });
    }

    /**
     * 完成
     */
    private void completAction() {
        if (!checkInput()) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String date = sdf.format(new java.util.Date());
        final CSupplementaryCreditRequest cscr = new CSupplementaryCreditRequest();
        cscr.setCard_num(cardNumber);
        cscr.setCurrent_balance(balanceAmount);
        cscr.setTotal_amount(totalAmount);
        cscr.setStatement_date(date + statetDate);
        cscr.setPayment_due_date(date + rePayDate);
        httpsPost(this, cscr, ReqAction.APPLICATION_CREATE_OR_CARD_PAYMENT, CSupplementaryCreditResponse.class, new ICallback<CSupplementaryCreditResponse>() {
            @Override
            public void success(CSupplementaryCreditResponse cSupplementaryCreditResponse) {
                Intent idata = new Intent();
                idata.putExtra("data", cscr);
                setResult(CARD_RESULT_CODE, idata);
                finish();
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(BillInfoActivity.this, tips);
            }
        });

    }

    private boolean checkInput() {
        double maxTotalAmount = 9999999;//最大额度
        totalAmount = totalAmountEt.getText().toString().trim();
        balanceAmount = currentBalanceEt.getText().toString().trim();
        if (TextUtils.isEmpty(totalAmount)) {
            CLogUtil.showToast(BillInfoActivity.this, "请输入信用卡额度！");
            return false;
        }
        if (CNummberUtil.parseDouble(totalAmount,0) > maxTotalAmount) {
            CLogUtil.showToast(BillInfoActivity.this, "总额度超出限制！");
            return false;
        }
        if (TextUtils.isEmpty(balanceAmount)) {
            CLogUtil.showToast(BillInfoActivity.this, "请输入当期已出账单金额！");
            return false;
        }
        if (CNummberUtil.parseDouble(balanceAmount,0) > maxTotalAmount) {
            CLogUtil.showToast(BillInfoActivity.this, "当期已出账单金额超出限制！");
            return false;
        }
        if (CNummberUtil.parseDouble(balanceAmount,0) > Double.parseDouble(totalAmount)) {
            CLogUtil.showToast(BillInfoActivity.this, "当期已出账单金额大于总额度！");
            return false;
        }
        if (TextUtils.isEmpty(statetDate)) {
            CLogUtil.showToast(BillInfoActivity.this, "请选择账单日");
            return false;
        }
        if (TextUtils.isEmpty(rePayDate)) {
            CLogUtil.showToast(BillInfoActivity.this, "请选择还款日");
            return false;
        }
        return true;
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        bankIconIv = (ImageView) findViewById(R.id.c_iv_bank_icon);
        bankNameTv = (TextView) findViewById(R.id.c_tv_bank_name);
        bankNumberTv = (TextView) findViewById(R.id.c_bank_number);
        totalAmountEt = (EditText) findViewById(R.id.c_et_total_amount);
        currentBalanceEt = (EditText) findViewById(R.id.c_et_current_balance);
        statementDateTv = (TextView) findViewById(R.id.c_tv_statement_date);
        paymentDueDateTv = (TextView) findViewById(R.id.c_tv_pament_due_date);
        completeBtn = (Button) findViewById(R.id.c_borrow_money_btn);
        setBillListener(currentBalanceEt);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
