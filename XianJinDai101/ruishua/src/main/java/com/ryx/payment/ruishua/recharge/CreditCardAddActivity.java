package com.ryx.payment.ruishua.recharge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.Cardno_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.recharge.adapter.CreditCardAddBootSheetAdapter;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.utils.UnitTransformUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加信用卡
 */
@EActivity(R.layout.activity_credit_card_add)
public class CreditCardAddActivity extends BaseActivity {
    private int SWIPING_CARD = 0x001002;
    @ViewById
    ImageView card_add_swiperimg;
    @ViewById
    EditText card_add_cardnumber, card_add_cardname;
    @ViewById
    TextView card_add_huankuanmsg;
    @ViewById
    CheckBox card_add_togglebtn;
    List<String> dateList = new ArrayList<String>();
    String cardid, realName, repayDate = "";
    BankCardInfo bankCardInfo;
    private PopupWindow mPopupWindow;
    int textwidth, textheight;

    @AfterViews
    public void afterInitView() {
        setTitleLayout("添加信用卡", true, true);
        setRightImgMessage("信用卡还款", RyxAppconfig.Notes_Credit);
        initQtPatParams();
        initListData();
        card_add_togglebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showBottomSheet();
                } else {

                }
            }
        });
        getTextWidthAndHeight();
        initPopWindow();
//        card_add_cardname.addTextChangedListener(new TextWatcher() {
//            String before = "";
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                before = s.toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String str = s.toString();
//                if (!TextUtils.isEmpty(str) && !str.matches(RegUtils.REGEX_CHINESE)) {
//                    card_add_cardname.setText(before);
//                    card_add_cardname.setSelection(card_add_cardname.getText().toString().length());
//                }
//            }
//        });

        card_add_cardnumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                show(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() != 0) {
                    mPopupWindow.showAsDropDown(card_add_cardnumber);
                } else {
                    mPopupWindow.dismiss();
                }

            }
        });
        card_add_cardnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    if (card_add_cardnumber.getText().toString().length() > 0) {
                        mPopupWindow.showAsDropDown(card_add_cardnumber);
                    }
                } else {
                    mPopupWindow.dismiss();
                }
            }
        });

    }

    public void initListData() {
        dateList.clear();
        for (int i = 1; i < 29; i++) {
            dateList.add("每月" + new java.text.DecimalFormat("#00").format(i) + "日");
        }
    }

    @Click(R.id.card_add_nextbtn)
    public void cardAddNextbtn() {
        if (checkparams()) {
            bankCardInfo = new BankCardInfo();
            bankCardInfo.setAccountNo(cardid); // 卡号
            bankCardInfo.setName(realName); // 姓名
            bankCardInfo.setRepaydate(repayDate); // 还款日期
            doVerifyiCard();
        }
    }

    /**
     * 创建PopupWindow
     */
    TextView tishi;
    View popupWindowview;
    int maxPopTextNum;
    int popWidth;
    int popHight;

    private void initPopWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popupWindowview = layoutInflater.inflate(R.layout.dialog_enlargetext_bottom, null);
        tishi = (TextView) popupWindowview.findViewById(R.id.tishi);

        popWidth = PhoneinfoUtils.getWindowsWidth(CreditCardAddActivity.this)
                - UnitTransformUtil.dip2px(CreditCardAddActivity.this, 30);
        popHight = UnitTransformUtil.sp2px(CreditCardAddActivity.this, 45)
                + UnitTransformUtil.dip2px(CreditCardAddActivity.this, 30);
        maxPopTextNum = (popWidth - UnitTransformUtil.dip2px(CreditCardAddActivity.this, 6)) / textwidth;
        mPopupWindow = new PopupWindow(popupWindowview, popWidth, popHight);
        mPopupWindow.update();
        tishi.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                mPopupWindow.dismiss();
                return false;
            }
        });

    }

    // 是否输入
    private boolean isRun = false;
    // 输入的内容
    private String inputStr = "";

    /**
     * 设置银行卡号显示方式
     */
    private void show(CharSequence s) {
        if (s != null && s.length() > 0) {
            if (isRun) {
                isRun = false;
                return;
            }
            isRun = true;
            inputStr = "";
            String newStr = s.toString();
            newStr = newStr.replace(" ", "");
            int index = 0;

            while ((index + 4) < newStr.length()) {
                inputStr += (newStr.substring(index, index + 4) + " ");
                index += 4;
            }
            inputStr += (newStr.substring(index, newStr.length()));
            card_add_cardnumber.setText(inputStr);
            tishi.setText(inputStr);
            card_add_cardnumber.setSelection(inputStr.length());
            resetPopWindow();
        }
    }

    /**
     * 重设卡输入放大窗口
     */
    public void resetPopWindow() {
        if ((inputStr.length() > maxPopTextNum)) {
            popHight = UnitTransformUtil.sp2px(CreditCardAddActivity.this, 95)
                    + UnitTransformUtil.dip2px(CreditCardAddActivity.this, 25);

        } else {
            popHight = UnitTransformUtil.sp2px(CreditCardAddActivity.this, 45)
                    + UnitTransformUtil.dip2px(CreditCardAddActivity.this, 30);
        }
        mPopupWindow.update(popWidth, popHight);
//        LOG.showLog("(inputStr.length()) =" + (inputStr.length()));

    }

    /**
     * 计算一个数字所占的宽高
     */
    public void getTextWidthAndHeight() {
        Paint pFont = new Paint();
        Rect rect = new Rect();
        pFont.setTextSize(UnitTransformUtil.sp2px(CreditCardAddActivity.this, 40));
        pFont.getTextBounds("9", 0, 1, rect);
        textwidth = rect.width();
        textheight = rect.height();
//        LOG.showLog("textwidth:" + rect.width() + " textheight: "
//                + rect.height());
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        // TODO Auto-generated method stub
        super.initQtPatParams();
        qtpayApplication = new Param("application", "QueryCreditInfo.Req");
    }

    /**
     * 检查是否为合法的卡
     */
    public void doVerifyiCard() {
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("realName", bankCardInfo.getName()));// 户名
        // URLEncoder.encode(bankInfo.getRealName(),"UTF-8")
        qtpayParameterList.add(new Param("accountNo", bankCardInfo
                .getAccountNo()));// 卡号
        httpsPost("QueryCreditInfoTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(CreditCardAddActivity.this,
                        getResources().getString(R.string.verifyi_card_success));
                Intent intent = new Intent(CreditCardAddActivity.this, RepaymentActivity_.class);
                OrderInfo orderinfo = Order.CREDIT_CARD_RECHARGE_TRUE_TIME;
                // orderinfo.setOrderDesc(bankCardInfo.getKanum());
                bankCardInfo.setBankName(payResult.getBankName());
                bankCardInfo.setBankId(payResult.getBankId());
                intent.putExtra("orderinfo", orderinfo);
                intent.putExtra("bankCardInfo", bankCardInfo);
                startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
            }
        });


    }

    /**
     * 检查参数
     */
    public boolean checkparams() {
        cardid = card_add_cardnumber.getText().toString().replace(" ", "") + "";

        if (cardid.length() < 14) {
            LogUtil.showToast(CreditCardAddActivity.this,
                    getResources().getString(R.string.card_digit_error));
            return false;
        }
        realName = card_add_cardname.getText().toString() + "";

        if (realName.length() < 2) {
            LogUtil.showToast(CreditCardAddActivity.this,
                    getResources()
                            .getString(R.string.please_enter_correct_name));
            return false;
        }
        return true;
    }

    @Click(R.id.card_add_swiperimg)
    public void swiperimgClick() {
        //跳转刷卡页面
        startActivityForResult(new Intent(this,
                        Cardno_.class),
                SWIPING_CARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RyxAppconfig.QT_RESULT_OK) {
            String cardNumber = data.getExtras().getString("result");
            card_add_cardnumber.setText(cardNumber);
        }
    }

    public void showBottomSheet() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CreditCardAddActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(CreditCardAddActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(CreditCardAddActivity.this).inflate(R.layout.creditcardadd_bottomsheet, null);
        final RecyclerView boottomListView = (RecyclerView) boottomView.findViewById(R.id.creditcard_bottomListViewid);
        final CreditCardAddBootSheetAdapter creditCardAddBootSheetAdapter = new CreditCardAddBootSheetAdapter(CreditCardAddActivity.this, dateList, new CreditCardAddBootSheetAdapter.CreditCardAddBootSheetItemClickListen() {
            @Override
            public void onItemClick(int positions) {
                String dataStr = dateList.get(positions);
                card_add_huankuanmsg.setText(dataStr);
                repayDate = dataStr;
                mBottomSheetDialog.dismiss();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        boottomListView.setLayoutManager(manager);
        boottomListView.setAdapter(creditCardAddBootSheetAdapter);

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
                        mBottomSheetDialog .show();
                    }
                });

            }
        }).start();
    }

}
