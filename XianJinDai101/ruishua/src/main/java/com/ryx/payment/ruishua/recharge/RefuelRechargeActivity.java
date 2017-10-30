package com.ryx.payment.ruishua.recharge;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.recharge.adapter.RefuelGridviewAdapter;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.widget.RyxGridView;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_refuel_recharge)
public class RefuelRechargeActivity extends BaseActivity {

    @ViewById
    RyxGridView gv_account;
    @ViewById
    EditText edt_userName;
    @ViewById
    EditText edt_phonenum;
    @ViewById
    EditText edt_cardnum;
    @Bean
    RefuelGridviewAdapter refuelAdapter;

    ArrayList<String> list = new ArrayList<String>();
    OrderInfo orderinfo;

    private int selectedPos = 0;

    @AfterViews
    public void initViews() {
        setTitleLayout("加油卡充值", true, true);
        initList();
        refuelAdapter.setList(list);
        gv_account.setAdapter(refuelAdapter);
        gv_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refuelAdapter.setSelection(position);
                refuelAdapter.notifyDataSetChanged();
                selectedPos = position;
            }
        });

//        edt_userName.addTextChangedListener(new TextWatcher() {
//            String before = "";
//
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
//                if (!str.matches(RegUtils.REGEX_CHINESE) && !TextUtils.isEmpty(str)) {
//                    edt_userName.setText(before);
//                    edt_userName.setSelection(edt_userName.getText().toString().length());
//                }
//            }
//        });
    }

    @Click(R.id.tilerightImg)
    public void showHelp() {
        Intent intent = new Intent(RefuelRechargeActivity.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "加油卡充值");
        intent.putExtra("urlkey", RyxAppconfig.Notes_RefuelRecharge);
        startActivity(intent);
    }

    @Click(R.id.tileleftImg)
    public void closeWindow() {
        finish();
    }

    /**
     * 设置充值金额列表的数据
     */
    private void initList() {
        list = new ArrayList<String>();
        list.add(new String("￥100.00"));
        list.add(new String("￥500.00"));
        list.add(new String("￥1000.00"));
    }

    @Click(R.id.btn_recharge)
    public void dorecharge() {
        String userName = edt_userName.getText().toString();
        String phoneNum = edt_phonenum.getText().toString();
        String cardNum = edt_cardnum.getText().toString();
        String money = list.get(selectedPos);
        if (TextUtils.isEmpty(userName)) {
            LogUtil.showToast(RefuelRechargeActivity.this, getResources().getString(R.string.please_enter_userName));
            return;
        }
        if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
            LogUtil.showToast(RefuelRechargeActivity.this, getResources().getString(R.string.please_enter_phoneNum));
            return;
        }
        if (TextUtils.isEmpty(cardNum) || cardNum.length() != 19) {
            LogUtil.showToast(RefuelRechargeActivity.this, getResources().getString(R.string.please_enter_cardnum));
            return;
        }
        try {
            Intent intent = new Intent(RefuelRechargeActivity.this, CreateOrder_.class);
            orderinfo = Order.REFUEL_RECHARGE;
            orderinfo.setOrderAmt(money);
            orderinfo.setOrderRemark("");
            orderinfo.setOrderDesc(phoneNum
                    + "|"
                    + MoneyEncoder
                    .EncodeFormat(money).replace("￥", "").replace(".", "").replace(",", "")
                    + "|"
                    + cardNum
                    + "|"
                    + userName);
            orderinfo.setOrderAmt(money);
            orderinfo.setOrderRemark(cardNum);
            intent.putExtra("orderinfo", orderinfo);
            startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        } catch (Exception e) {

        }

    }
}
