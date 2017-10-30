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
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.QcoinInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.recharge.adapter.QqCoinAccountAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.widget.RyxGridView;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@EActivity(R.layout.activity_qq_coin_recharge)
public class QqCoinRechargeActivity extends BaseActivity {

    @ViewById
    EditText edt_accout;
    @ViewById
    RyxGridView gv_account;
    OrderInfo orderinfo;

    @Bean
    QqCoinAccountAdapter gridViewAdapter;

    ArrayList<QcoinInfo> coinlist = new ArrayList<QcoinInfo>();
    Param qtpayGameId;
    private String gameid = "GAME4980";
    QcoinInfo qcoinInfo = null;

    private int selectedPos = 0;

    @AfterViews
    public void initViews() {
        setTitleLayout("Q币充值", true);
        initQtPatParams();
        doOrderRequest();
        gridViewAdapter.setList(coinlist);
        gv_account.setAdapter(gridViewAdapter);
        gv_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridViewAdapter.setSelection(position);
                gridViewAdapter.notifyDataSetChanged();
                selectedPos = position;
            }
        });
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "GetGameMoney.Req");
        qtpayGameId = new Param("gameId", gameid); //商品编码
    }

    public void doOrderRequest() {
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayGameId);
        httpsPost("GetGameMoney", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showLog("payResult", payResult.toString() + "---");
                initListData(payResult.getData());
            }
        });
    }

    public void initListData(String data) {
        try {
            JSONObject jsonObj = new JSONObject(data);
            JSONArray banks = jsonObj.getJSONArray("resultBean");
            for (int i = 0; i < banks.length(); i++) {
                qcoinInfo = new QcoinInfo();
                qcoinInfo.setGameid(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "gameid"));
                qcoinInfo.setGamename(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "gamename"));
                qcoinInfo.setOnlineid(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "onlineid"));
                qcoinInfo.setOnlinename(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "onlinename"));
                qcoinInfo.setParvalue(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "parvalue"));
                qcoinInfo.setSaleprice(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "saleprice"));
                coinlist.add(qcoinInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.tilerightImg)
    public void showHelp() {
        Intent intent = new Intent(QqCoinRechargeActivity.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "Q币充值");
        intent.putExtra("urlkey", RyxAppconfig.Notes_QCoinRecharge);
        startActivity(intent);
    }

    @Click(R.id.tileleftImg)
    public void closeWindow() {
        finish();
    }

    @Click(R.id.btn_recharge)
    public void doRecharge() {
        String qqNum = edt_accout.getText().toString();
        String money = MoneyEncoder.EncodeFormat(coinlist.get(selectedPos).getParvalue());
        if (TextUtils.isEmpty(qqNum) || qqNum.length() < 4) {
            LogUtil.showToast(QqCoinRechargeActivity.this, getResources().getString(R.string.please_enter_qq_account));
            return;
        }
        if(TextUtils.isEmpty(money)){
            LogUtil.showToast(QqCoinRechargeActivity.this, getResources().getString(R.string.please_enter_right_qq_account));
            return;
        }

        Intent intent = new Intent(QqCoinRechargeActivity.this, CreateOrder_.class);
        orderinfo = Order.Q_COIN_RECHARGE;
        orderinfo.setOrderDesc(gameid + "|" + MoneyEncoder.EncodeFormat(coinlist.get(selectedPos).getParvalue()).replace("￥", "").replace(".", "").replace(",", "") + "|" + qqNum + "|" + PhoneinfoUtils.getPsdnIp()); // 要充值qq号

        orderinfo.setOrderAmt(money);
        orderinfo.setOrderRemark(qqNum);
        intent.putExtra("orderinfo", orderinfo);
        startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
    }

}
