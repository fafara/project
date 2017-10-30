package com.ryx.payment.ruishua.recharge;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.GridView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.QcoinInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.recharge.adapter.QqCoinAccountAdapter;
import com.ryx.payment.ruishua.recharge.adapter.QqCoinAccountAdapter_;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 盛大点券
 */
@EActivity(R.layout.activity_sd_pay)
public class SDActivity extends BaseActivity {

    @ViewById(R.id.gv_sdpay)
    GridView mSDPayGridView;
    @ViewById(R.id.et_account)
    EditText mSDPayEt;
    @ViewById(R.id.btn_recharge)
    Button mRechargeBtn;
    private QqCoinAccountAdapter adapter;
    private String[] mPhoneString;
    private String gameid = "GAME3993";
    private Param qtpayGameId;
    private String jsonStr;
    private QcoinInfo qcoinInfo;
    private ArrayList<QcoinInfo> coinlist = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private String mobile;
    private String mMoney;
    private OrderInfo orderinfo;
    private int selectid = 0;
    private String parvalue;

    @AfterViews
    public void initViews() {
        setTitleLayout("盛大点券", true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_SDRecharge);
        initQtPatParams();
        doOrderRequest();
    }

    private void doOrderRequest() {
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayGameId);
        httpsPost("doAliPayReq", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                jsonStr = payResult.getData();
                LogUtil.showLog("muxin","sd json:"+jsonStr);
                initListData();
                initData();
            }
        });
    }

    private boolean checkInput() {
        mobile = mSDPayEt.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            LogUtil.showToast(SDActivity.this, getResources().getString(R.string.please_enter_account));
            return false;
        }else if (TextUtils.isEmpty(mMoney)) {
            LogUtil.showToast(
                    SDActivity.this, "请选择充值金额!!!");
            return false;
        }
        return true;
    }

    @Click(R.id.btn_recharge)
    public void rechargeClick() {
        if (checkInput()) {
            Intent intent = new Intent(SDActivity.this, CreateOrder_.class);
            orderinfo = Order.SD_RECHARGE;
            orderinfo.setOrderDesc(
                    "00|"
                            + MoneyEncoder
                            .EncodeFormat(
                                    coinlist.get(selectid).getParvalue())
                            .replace("￥", "").replace(".", "")
                            .replace(",", "") + "|" + mobile + "|00");
            orderinfo.setOrderAmt(mMoney);
            orderinfo.setOrderRemark(mobile);
            intent.putExtra("orderinfo", orderinfo);
            startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        }
    }

    private void initData() {
        adapter = QqCoinAccountAdapter_.getInstance_(SDActivity.this);
        adapter.setList(coinlist);
        adapter.setSelection(0);
        mMoney = MoneyEncoder.EncodeFormat(coinlist.get(0).getParvalue());
        mSDPayGridView.setAdapter(adapter);
    }

    private void initListData() {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray banks = jsonObj.getJSONArray("resultBean");
            for (int i = 0; i < banks.length(); i++) {
                qcoinInfo = new QcoinInfo();
                qcoinInfo.setGameid(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "gameid"));
                qcoinInfo.setGamename(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "gamename"));
                qcoinInfo.setOnlineid(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "onlineid"));
                qcoinInfo.setOnlinename(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "onlinename"));
                qcoinInfo.setParvalue(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "parvalue"));
                qcoinInfo.setSaleprice(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "saleprice"));
                parvalue = String.valueOf(
                        (int) Double.parseDouble(JsonUtil.getValueFromJSONObject(
                                banks.getJSONObject(i), "parvalue")));
                qcoinInfo.setBigDidsName("￥" + parvalue);
                qcoinInfo.setTipName("盛大点券" + parvalue);
                list.add(qcoinInfo.getOnlinename());
                coinlist.add(qcoinInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ItemClick(R.id.gv_sdpay)
    public void selectAmountClick(int position) {
        mMoney = MoneyEncoder.EncodeFormat(coinlist.get(position).getParvalue());
        selectid = position;
        adapter.setSelection(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "GetGameMoney.Req");
        // 商品编码
        qtpayGameId = new Param("gameId", gameid);
    }
}
