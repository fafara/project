package com.ryx.payment.ruishua.recharge;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.convenience.Cardno_;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.utils.UnitTransformUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_transfer_card_add)
public class TransferCardAddActivity extends BaseActivity {


    @ViewById(R.id.tv_userName)
    EditText tv_userName;
    @ViewById(R.id.edt_debitcardNo)
    EditText edt_debitcardNo;
    @ViewById
    TextView tv_title;

    private int textwidth, textheight;
    private int SWIPING_CARD=50;
    private int ADDCARDINFO=1;
    String  cardid;
    String  realName;
    BankCardInfo bankCardInfo;
    private PopupWindow mPopupWindow;
    @AfterViews
    public void initViews(){
        tv_title.setText("添加提款账户");
        getTextWidthAndHeight();
        initPopWindow();
        edt_debitcardNo.addTextChangedListener(new TextWatcher() {

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
                    mPopupWindow.showAsDropDown(edt_debitcardNo);
                } else {
                    mPopupWindow.dismiss();
                }

            }
        });
        edt_debitcardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    if (edt_debitcardNo.getText().toString().length() > 0) {
                        mPopupWindow.showAsDropDown(edt_debitcardNo);
                    }
                } else {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 计算一个数字所占的宽高
     */
    public void getTextWidthAndHeight() {
        Paint pFont = new Paint();
        Rect rect = new Rect();
        pFont.setTextSize(UnitTransformUtil.sp2px(TransferCardAddActivity.this, 40));
        pFont.getTextBounds("9", 0, 1, rect);
        textwidth = rect.width();
        textheight = rect.height();
    }

    private void initPopWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popupWindowview = layoutInflater.inflate(R.layout.dialog_enlargetext_bottom, null);
        tishi = (TextView) popupWindowview.findViewById(R.id.tishi);

        popWidth = PhoneinfoUtils.getWindowsWidth(TransferCardAddActivity.this)
                - UnitTransformUtil.dip2px(TransferCardAddActivity.this, 30);
        popHight = UnitTransformUtil.sp2px(TransferCardAddActivity.this, 45)
                + UnitTransformUtil.dip2px(TransferCardAddActivity.this, 30);
        maxPopTextNum = (popWidth - UnitTransformUtil.dip2px(TransferCardAddActivity.this, 6)) / textwidth;
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
     * 创建PopupWindow
     */
    TextView tishi;
    View popupWindowview;
    int maxPopTextNum;
    int popWidth;
    int popHight;
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
            edt_debitcardNo.setText(inputStr);
            tishi.setText(inputStr);
            edt_debitcardNo.setSelection(inputStr.length());
            resetPopWindow();
        }
    }

    /**
     * 重设卡输入放大窗口
     */
    public void resetPopWindow() {
        if ((inputStr.length() > maxPopTextNum)) {
            popHight = UnitTransformUtil.sp2px(TransferCardAddActivity.this, 95)
                    + UnitTransformUtil.dip2px(TransferCardAddActivity.this, 25);

        } else {
            popHight = UnitTransformUtil.sp2px(TransferCardAddActivity.this, 45)
                    + UnitTransformUtil.dip2px(TransferCardAddActivity.this, 30);
        }
        mPopupWindow.update(popWidth, popHight);
    }

    @Click(R.id.iv_swipecard)
    public void swipeCard(){
        startActivityForResult(new Intent(TransferCardAddActivity.this,Cardno_.class),SWIPING_CARD);
    }

    @Click(R.id.btn_next)
    public void goNext(){
        String cardNo = edt_debitcardNo.getText().toString().replace(" ","").trim();
        if (!(cardNo.length()>=14)) {
            LogUtil.showToast(TransferCardAddActivity.this, getResources().getString(R.string.card_digit_error));
        }else {
            cardid = edt_debitcardNo.getText().toString().replace(" ","").trim()+"";
            realName = tv_userName.getText().toString().trim();
            bankCardInfo = new BankCardInfo();
            bankCardInfo.setAccountNo(cardid);	// 卡号
            bankCardInfo.setName(realName);	// 姓名
            Intent intent = new Intent(TransferCardAddActivity.this,TransferCardInfoAddActivity_.class);
            intent.putExtra("bankCardInfo", bankCardInfo);
            intent.putExtra("usertype","00");

            startActivityForResult(intent,ADDCARDINFO);
        }
    }
    @Click(R.id.btn_back)
    public void closeWindow(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDCARDINFO) {
                finish();
            }
        }
        if (resultCode == RyxAppconfig.QT_RESULT_OK) {
            if (requestCode == SWIPING_CARD) {
                String signdata= data.getExtras().getString("result");
                edt_debitcardNo.setText(signdata);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
