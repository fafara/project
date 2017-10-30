package com.ryx.payment.ruishua.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.adapter.CashBankFlowLisAdapter;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.usercenter.BlackCheckMsgActivity_;
import com.ryx.payment.ruishua.usercenter.DetailsTabMainActivity;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EFragment(R.layout.fragment_cash_flow)
public class CashFlowFragment extends BaseFragment {
    //状态Map
    private Map<String, String> consumestepMap = new HashMap<String, String>();
    private Map<String, String> acceptstepMap = new HashMap<String, String>();
    private Map<String, String> outstepMap = new HashMap<String, String>();
    private Map<String, String> offlinestepMap = new HashMap<String, String>();
    @ViewById
    ImageView cashflowimg, expressionimg, impay_bankimg;

    @ViewById
    Button btn_changebankcard, btn_submitnewbank, btn_toBlackCheckMsg;

    @ViewById
    LinearLayout ll_changebank;
    @ViewById
    LinearLayout toBlackCheckMsgll;
    @ViewById
    AutoRelativeLayout im_pay_selectbankid;
    @ViewById
    TextView tv_first, tv_second, tv_third, tv_textmessage, impay_bankname, impay_accountno;
    private String cardInfo, cardidx, oldaccount, md5code,serveroldaccount;
    private ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    private FragmentListener mListener;
    private TradeDetailInfo tradeDetailInfo;
    private boolean isInit = false;
    private static String actionFlag;
    String cashstatus = "";

    public CashFlowFragment() {
    }

    /**
     * 创建资金动态实例
     *
     * @param tradeDetailInfo
     * @return
     */
    public static CashFlowFragment newInstance(TradeDetailInfo tradeDetailInfo, String actionType) {
        CashFlowFragment_ fragment = new CashFlowFragment_();
        Bundle args = new Bundle();
        args.putSerializable("tradeDetailInfo", tradeDetailInfo);
        fragment.setArguments(args);
        actionFlag = actionType;
        return fragment;
    }

    @AfterViews
    public void afterView() {
        tradeDetailInfo = (TradeDetailInfo) getArguments().getSerializable("tradeDetailInfo");
        if("income".equals(actionFlag)||"expend".equals(actionFlag)||"sweepcode".equals(actionFlag)){
        Map paramMap = new HashMap();
        paramMap.put("flag", "GetBalanceStep");
        paramMap.put("data", tradeDetailInfo);
        mListener.doDataRequest(paramMap);
        }else if("withdrawl".equals(actionFlag)){
            cashstatus = getRightValue(tradeDetailInfo.getDeawstatus());
            initFlow();
        }

    }

    public String getRightValue(String value) {
        String[] values;
        if (value != null) {
            values = value.split("\\|");
            if (values.length == 2) {
                return values[1];
            }
            return "";
        }
        return "";
    }

    public void initFlow() {
        expressionimg.setVisibility(View.GONE);
        tv_first.setText("申请提现");
        tv_third.setText("提现成功");
        if("1".equals(cashstatus)){
            cashflowimg.setImageResource(R.drawable.cashflow_process2);
            tv_first.setTextColor(getResources().getColor(R.color.black));
            tv_second.setTextColor(getResources().getColor(R.color.graytext));
            tv_third.setTextColor(getResources().getColor(R.color.graytext));
        }else if("2".equals(cashstatus)){
            cashflowimg.setImageResource(R.drawable.cashflow_process3);
            tv_first.setTextColor(getResources().getColor(R.color.graytext));
            tv_second.setTextColor(getResources().getColor(R.color.black));
            tv_third.setTextColor(getResources().getColor(R.color.graytext));
        }else if("3".equals(cashstatus)){
            expressionimg.setVisibility(View.VISIBLE);
            expressionimg.setImageResource(R.drawable.smile);
            cashflowimg.setImageResource(R.drawable.cashflow_process4);
            tv_first.setTextColor(getResources().getColor(R.color.graytext));
            tv_second.setTextColor(getResources().getColor(R.color.graytext));
            tv_third.setTextColor(getResources().getColor(R.color.black));
        }else{
            cashflowimg.setImageResource(R.drawable.cashflow_process3);
            tv_first.setTextColor(getResources().getColor(R.color.graytext));
            tv_second.setTextColor(getResources().getColor(R.color.graytext));
            tv_third.setTextColor(getResources().getColor(R.color.red_second));
            tv_third.setText("提现失败");
        }
    }

    /**
     * activity回调Fragment接口
     * @param actiontype
     * @param qtpayResult
     */
    public void send(int actiontype, RyxPayResult qtpayResult) {
        if (actiontype == 0x111) {
            //资金动态信息
            String reminder = qtpayResult.getDesc();
            if (!TextUtils.isEmpty(reminder)) {
                tv_textmessage.setText(Html.fromHtml(reminder));
            }
            initFundDynamicData(qtpayResult.getData());
            initFundDynamicView();
        } else if (actiontype == 0x222) {
            //获取银行卡列表值
            newinitBankListData(qtpayResult.getData());
            if (bankcardlist.size() > 0) {
                showSelectBankList();
            } else {
                LogUtil.showToast(getContext(), "您还未绑定有效结算卡,请先绑定有效结算卡!");
                Map paramMap = new HashMap();
                paramMap.put("flag", "tobindBankAct");
                mListener.doDataRequest(paramMap);
            }
        } else if (actiontype == 0x333) {
            //提交更换银行卡后的数据
            LogUtil.showToast(getActivity(), "银行卡信息提交成功");
            getActivity().finish();
        }

    }
    private void newinitBankListData(String banklistJson) {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            if ("0000".equals(jsonObj.getString("code"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONObject("result").getJSONArray("cardlist");
                bankcardlist.clear();
                for (int i = 0; i < banks.length(); i++) {
                    BankCardInfo   bankCardInfo = new BankCardInfo();
                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardidx"));
                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankid"));
                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardno"));
                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankname"));
                    bankCardInfo.setQuick(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "quick"));
                    bankCardInfo.setDaikou(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "msdk"));
                    bankCardInfo.setDaifustatus(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "daifustatus"));
                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "flaginfo"));//1为默认结算卡
                    bankCardInfo.setCardtype(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardtype"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "customername"));
                    bankcardlist.add(bankCardInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//    /**
//     * 初始化银行卡列表
//     */
//    public void initBankListData(String banklistJson) {
//
//
//        /***
//         * 01-05 19:08:56.352: E/banklist(23142):
//         * {"summary":{"numOfCard":"1"},
//         * "result":{"message":"成功","resultCode":"0000"},
//         * "resultBean":[{"remark":"","bankCity":"济南市","bankProvinceId":"370000","accountNo":"6228450258024439275",
//         * "bankName":"中国农业银行股份有限公司","bankProvince":"山东省","bankId":"103","flagInfo":"","bankCityId":"370100",
//         * "cardIdx":"1","name":"孙立彬","branchBankId":"103451015513","branchBankName":"济南高新技术产业开发区支行"}]}
//
//         */
//
//        try {
//            JSONObject jsonObj = new JSONObject(banklistJson);
//            if ("0000".equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
//                // 解析银行卡信息
//                JSONArray banks = jsonObj.getJSONArray("resultBean");
//                bankcardlist.clear();
//                for (int i = 0; i < banks.length(); i++) {
//                    BankCardInfo bankCardInfo = new BankCardInfo();
//                    bankCardInfo.setBankCity(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankCity"));
//                    bankCardInfo.setRemark(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "remark"));
//                    bankCardInfo.setBankProvinceId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvinceId"));
//                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "accountNo"));
//                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankName"));
//                    bankCardInfo.setBankProvince(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvince"));
//                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankId"));
//                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "flagInfo"));
//                    bankCardInfo.setBankCityId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankCityId"));
//                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardIdx"));
//                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "name"));
//                    bankCardInfo.setBranchBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankId"));
//                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
//                    bankcardlist.add(bankCardInfo);
//                }
//            }
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    /**
     * 更换银行卡
     */
    @Click(R.id.btn_changebankcard)
    public void changebankcard() {
        if (bankcardlist.size() == 0) {
            Map<String, String> map = new HashMap<>();
            map.put("flag", "getBankCardList");
            mListener.doDataRequest(map);
        } else {
            showSelectBankList();
        }
    }

    @Click(R.id.btn_submitnewbank)
    public void submitNewBank() {
        String cardInfoShort=StringUnit.cardShortShow(cardInfo);
        if(TextUtils.isEmpty(cardInfoShort)||cardInfoShort.equals(serveroldaccount)){
            LogUtil.showToast(getActivity(), "请更换别的银行卡!");
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("flag", "anewDrawMoney");
            map.put("cardInfo", cardInfo);
            map.put("cardidx", cardidx);
            map.put("oldaccount", TextUtils.isEmpty(serveroldaccount)?"":serveroldaccount);
            map.put("md5code", TextUtils.isEmpty(md5code)?"":md5code);
            map.put("data", tradeDetailInfo);
            mListener.doDataRequest(map);
        }
    }

    @Click(R.id.btn_toBlackCheckMsg)
    public void toBlackCheckMsg() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BlackCheckMsgActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("cardIdjiami", acceptstepMap.get("account"));
        bundle.putString("md5Value", acceptstepMap.get("md5code"));
        bundle.putString("localtime", acceptstepMap.get("localtime"));
        bundle.putString("localDate", acceptstepMap.get("localDate"));
        bundle.putString("locallogo", acceptstepMap.get("locallogo"));
        bundle.putString("msgid", acceptstepMap.get("msgid"));
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().finish();
    }

    /***
     * 展示选择银行卡列表页面
     */
    public void showSelectBankList() {
        final Dialog simpleDialog = new Dialog(getActivity(), R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(getActivity()).inflate(R.layout.cashflow_banklistview, null);
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        TextView textView = (TextView) boottomView.findViewById(R.id.cashflow_addbankcard);

        final CashBankFlowLisAdapter bankLisAdapter = new CashBankFlowLisAdapter(getActivity(), bankcardlist, oldaccount);
        boottomListView.setAdapter(bankLisAdapter);

        boottomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                oldaccount = bankcardlist.get(position).getAccountNo();
                //卡号
                cardInfo = bankcardlist.get(position).getAccountNo();
                //银行卡索引值
                cardidx = bankcardlist.get(position).getCardIdx();
                //布局显示
                BanksUtils.selectIcoidToImgView(getActivity(),bankcardlist.get(position).getBankId(), impay_bankimg);
                impay_bankname.setText(bankcardlist.get(position).getBankName());
                impay_accountno.setText(StringUnit.cardJiaMi(bankcardlist.get(position).getAccountNo()));
                //选择银行卡按钮隐藏
                btn_changebankcard.setVisibility(View.GONE);
                //提交按钮显示
                btn_submitnewbank.setVisibility(View.VISIBLE);

                im_pay_selectbankid.setVisibility(View.VISIBLE);
                simpleDialog.dismiss();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
                Map paramMap = new HashMap();
                paramMap.put("flag", "tobindBankAct");
                mListener.doDataRequest(paramMap);
            }
        });
        simpleDialog.contentView(boottomView)
                .show();
    }

    @Click(R.id.im_pay_selectbankid)
    public void selectbankViewClick() {
        //银行卡展示布局点击事件
        showSelectBankList();
    }

    /**
     * 初始化流程布局
     */
    private void initFundDynamicView() {

        if (consumestepMap.isEmpty()) {
            cashflowimg.setImageResource(R.drawable.cashflow_process1);
            expressionimg.setImageResource(R.drawable.weep);
            tv_textmessage.setText("订单存在问题!!!");
            btn_changebankcard.setVisibility(View.GONE);
            ll_changebank.setVisibility(View.GONE);
            return;
        }

        //订单消费
        String consumestepStatus = consumestepMap.get("status");
        String consumestepdesc = consumestepMap.get("desc");
        tv_first.setText(consumestepdesc);

        //只有00状态才会有后续,其他状态后续都不显示
        if ("00".equals(consumestepStatus)) {
            cashflowimg.setImageResource(R.drawable.cashflow_process2);
            tv_first.setTextColor(getResources().getColor(R.color.black));
        } else {
            cashflowimg.setImageResource(R.drawable.cashflow_process1);
            tv_first.setTextColor(getResources().getColor(R.color.graytext));
            return;
        }
        //银行受理情况
        String acceptstepStatus = acceptstepMap.get("status");
        String acceptstepdesc = acceptstepMap.get("desc");
        tv_second.setText(acceptstepdesc);
        //走到accept步,将图片修改为亮图
        cashflowimg.setImageResource(R.drawable.cashflow_process3);
        tv_second.setTextColor(getResources().getColor(R.color.black));
        //0,1,2,3,4,5、19时不会有后续状态
        if ("0,1,2,3,4,5,19".contains(acceptstepStatus)) {
            //19是明确失败
            if ("19".equals(acceptstepStatus)) {
                cashflowimg.setImageResource(R.drawable.cashflow_process2);
                tv_second.setTextColor(getResources().getColor(R.color.graytext));

                if ("1".equals(acceptstepMap.get("enableblack"))) {
                    //enableblack為1的時候可以補充資料
                    toBlackCheckMsgll.setVisibility(View.VISIBLE);
                    btn_toBlackCheckMsg.setVisibility(View.VISIBLE);
                } else {
                    toBlackCheckMsgll.setVisibility(View.GONE);
                    btn_toBlackCheckMsg.setVisibility(View.GONE);
                }

            }
            return;
        }
        //银行出款
        String outstepStatus = outstepMap.get("status");
        String outstepdesc = outstepMap.get("desc");
        tv_third.setText(outstepdesc);
        //线下出款
        String offlinestepStatus = offlinestepMap.get("status");
        String offlinestepdesc = offlinestepMap.get("desc");
        //status是11自动出款成功,22线下出款中
        if ("11".equals(outstepStatus) || "22".equals(outstepStatus)) {
            expressionimg.setImageResource(R.drawable.smile);
            //出款成功
            cashflowimg.setImageResource(R.drawable.cashflow_process4);
            tv_third.setTextColor(getResources().getColor(R.color.black));
            return;
        } else if ("23".equals(outstepStatus)) {
//            //获取错误的卡号
//            oldaccount = outstepMap.get("account");
            //服务端返回的错误的旧的卡号
            serveroldaccount=outstepMap.get("account");
            md5code = outstepMap.get("md5code");
            //可进行重提操作
            cashflowimg.setImageResource(R.drawable.cashflow_process3);
            tv_third.setTextColor(getResources().getColor(R.color.graytext));
            //更换银行卡
            btn_changebankcard.setVisibility(View.VISIBLE);
            ll_changebank.setVisibility(View.VISIBLE);
        } else
            //24线下重提后出款中(此时判断offline状态11的显示线下成功 )
            if ("24".equals(outstepStatus) && "11".equals(offlinestepStatus)) {
                //自动失败，但是线下转出成功
                cashflowimg.setImageResource(R.drawable.cashflow_process4);
                tv_third.setTextColor(getResources().getColor(R.color.black));
                tv_third.setText(offlinestepdesc);
            } else if ("24".equals(outstepStatus) && !"11".equals(offlinestepStatus)) {
                //自动失败，但是线下转出情况未知
                cashflowimg.setImageResource(R.drawable.cashflow_process3);
                tv_third.setTextColor(getResources().getColor(R.color.graytext));
                tv_third.setText(offlinestepdesc);
            }
    }

    /**
     * 1、消费
     * 2、银行转出受理
     * 3、银行转出状态
     * 4、线下出款状态
     * {"desc":"交易成功","status":"00","datetime":"20160204142038","step":"consume"},  //00状态才会有后续状态
     * {"desc":"银行已受理","status":"11","datetime":"20160204142037","step":"accept"},  //2、5、19时不会有后续状态
     * {"desc":"交易成功","status":"23","datetime":"20160204215902","step":"out"}]}
     * //status是11自动出款成功,22线下出款中 ,23线下出款失败(可进行重提操作),24线下重提后出款中(此时判断offline状态11的显示线下成功)
     * 初始化资金动态数据
     */
    private void initFundDynamicData(String jsondata) {
        try {
            JSONObject jsonObj = new JSONObject(jsondata);
            JSONArray cashFLos = jsonObj.getJSONArray("resultBean");
            for (int i = 0; i < cashFLos.length(); i++) {
                String step = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "step");
                String status = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "status");
                String datetime = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "datetime");
                String desc = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "desc");
                if ("consume".equals(step)) {
                    consumestepMap.put("step", "consume");
                    consumestepMap.put("status", status);
                    consumestepMap.put("datetime", datetime);
                    consumestepMap.put("desc", desc);
                } else if ("accept".equals(step)) {
                    acceptstepMap.put("step", "accept");
                    acceptstepMap.put("status", status);
                    acceptstepMap.put("datetime", datetime);
                    acceptstepMap.put("desc", desc);
                    //状态19的
                    String md5code = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "md5code");
                    acceptstepMap.put("md5code", md5code);
                    String account = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "account");
                    acceptstepMap.put("account", account);
                    acceptstepMap.put("localtime", DataUtil.getRightValue(tradeDetailInfo.getLocalTime()));
                    acceptstepMap.put("localDate", DataUtil.getRightValue(tradeDetailInfo.getLocalDate()));
                    acceptstepMap.put("locallogo", DataUtil.getRightValue(tradeDetailInfo.getLocalLogNo()));
                    String primarykey = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "primarykey");
                    acceptstepMap.put("msgid", primarykey);
                    //是否可以補充資料標誌
                    String enableblack = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "enableblack");
                    acceptstepMap.put("enableblack", enableblack);
                } else if ("out".equals(step)) {
                    String account = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "account");
                    String md5code = JsonUtil.getValueFromJSONObject(cashFLos.getJSONObject(i), "md5code");
                    outstepMap.put("md5code", md5code);
                    outstepMap.put("step", "out");
                    outstepMap.put("status", status);
                    outstepMap.put("datetime", datetime);
                    outstepMap.put("desc", desc);
                    outstepMap.put("account", account);
                } else if ("offline".equals(step)) {
                    offlinestepMap.put("step", "offline");
                    offlinestepMap.put("status", status);
                    offlinestepMap.put("datetime", datetime);
                    offlinestepMap.put("desc", desc);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (DetailsTabMainActivity) getBaseActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }


}
