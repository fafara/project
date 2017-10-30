package com.ryx.payment.ruishua.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.convenience.SignRequisitions_;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.usercenter.DetailsTabMainActivity;
import com.ryx.payment.ruishua.usercenter.adapter.DetailTradeAdapter;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.fragment_details_content)
public class DetailsContentFragment extends BaseFragment {
    TradeDetailInfo tradeDetailInfo;
    @ViewById
    Button btn_looksign;
    private FragmentListener mListener;
    DetailsContentFragment instance;
    ArrayList<Param> details = new ArrayList<Param>();
    @ViewById
    ListView lv_detaile;
    @Bean
    DetailTradeAdapter detailTradeAdapter;
    private static String actionFlag;

    public DetailsContentFragment() {

    }

    @AfterInject
    public void onCreate() {
        LogUtil.showLog("ryx", "DetailsContentFragment===AfterInject");
        instance = this;
    }

    /**
     * 创建Fragement实例
     *
     * @param tradeDetailInfo
     * @return
     */
    public static DetailsContentFragment newInstance(TradeDetailInfo tradeDetailInfo,String actionType) {
        DetailsContentFragment_ fragment = new DetailsContentFragment_();
        Bundle args = new Bundle();
        args.putSerializable("tradeDetailInfo", tradeDetailInfo);
        fragment.setArguments(args);
        actionFlag = actionType;
        return fragment;
    }

    @AfterViews
    public void afterView() {
        tradeDetailInfo = (TradeDetailInfo) getArguments().getSerializable("tradeDetailInfo");
        lv_detaile.setAdapter(detailTradeAdapter);
        if("income".equals(actionFlag)||"expend".equals(actionFlag)||"sweepcode".equals(actionFlag)||"payment".equals(actionFlag)){
            btn_looksign.setVisibility(View.VISIBLE);
        }else{
            btn_looksign.setVisibility(View.GONE);
        }
        initData();
        detailTradeAdapter.setList(details);
        detailTradeAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv_detaile);
    }

    private void initData(){
        // 订单号 交易类型 还款信用卡卡号 支付账户 支付方式 交易金额
        if (RyxAppconfig.QT_WITHDRAW_MERCHANT.equals(getRightValue(tradeDetailInfo
                .getMerchantId()))
                ) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param("支付账户", StringUnit.phoneJiaMi(QtpayAppData
                    .getInstance((DetailsTabMainActivity)mListener).getMobileNo())));
            details.add(new Param("支付方式", ("02".equals(getRightValue(tradeDetailInfo
                    .getPayType())) ? "账户支付" : "刷卡支付")));
        } else if (RyxAppconfig.QT_CREDIT_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                ) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getOrderId()),
                    getRightValue(tradeDetailInfo.getOrderId())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount2()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount2()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getFee()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo.getFee()))));
            details.add(new Param("支付账户", StringUnit.phoneJiaMi(QtpayAppData
                    .getInstance((DetailsTabMainActivity)mListener).getMobileNo())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getPayType()), ("02"
                    .equals(getRightValue(tradeDetailInfo.getPayType())) ? "账户支付"
                    : "刷卡支付")));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount()))));
        } else if (RyxAppconfig.QT_TRANSFER_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getOrderId()),
                    getRightValue(tradeDetailInfo.getOrderId())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount2()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount2()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getFee()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo.getFee()))));
            details.add(new Param("支付账户", StringUnit.phoneJiaMi(QtpayAppData
                    .getInstance((DetailsTabMainActivity)mListener).getMobileNo())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getPayType()), ("02"
                    .equals(getRightValue(tradeDetailInfo.getPayType())) ? "账户支付"
                    : "刷卡支付")));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount()))));
        } else if (RyxAppconfig.QT_MOBILE_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getOrderId()),
                    getRightValue(tradeDetailInfo.getOrderId())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAccount2()),
                    StringUnit.phoneJiaMi(getRightValue(tradeDetailInfo
                            .getAccount2()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param("支付账户", StringUnit.phoneJiaMi(QtpayAppData
                    .getInstance((DetailsTabMainActivity)mListener).getMobileNo())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getPayType()), ("02"
                    .equals(getRightValue(tradeDetailInfo.getPayType())) ? "账户支付"
                    : "刷卡支付")));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount()))));
        }

        else if (RyxAppconfig.QT_IMPAY_FREE_MERCHANT_D
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_IMPAY_LITTLE_MERCHANT_A
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_IMPAY_MUCH_MERCHANT_B
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_IMPAY_SUPER_MERCHANT_C
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_GOODS_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                ||RyxAppconfig.QT_SHANFU_MERCHANT.equals(getRightValue(tradeDetailInfo.getMerchantId()))
                ||RyxAppconfig.QT_YUNSHANFU_MERCHANT.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYX_IMPAY_CASHPLEDGE_MERCHANT.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYXSWEEPCODE_1.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYXSWEEPCODE_2.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYXSWEEPCODE_3.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYXSWEEPCODE_4.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYXSWEEPCODE_5.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYXSWEEPCODE_6.equals(getRightValue(tradeDetailInfo.getMerchantId()))||
                RyxAppconfig.RYXSWEEPCODE_7.equals(getRightValue(tradeDetailInfo.getMerchantId()))
                ) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getOrderId()),
                    getRightValue(tradeDetailInfo.getOrderId())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAccount2()),
                    StringUnit.phoneJiaMi(getRightValue(tradeDetailInfo
                            .getAccount2()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getFee()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo.getFee()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getMobileno()),
                    StringUnit.phoneJiaMi(getRightValue(tradeDetailInfo
                            .getMobileno()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getPayType()), ("02"
                    .equals(getRightValue(tradeDetailInfo.getPayType())) ? "账户支付"
                    : "刷卡支付")));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount()))));
        } else if (RyxAppconfig.QT_MOBILE_ACCPAY_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getOrderId()),
                    getRightValue(tradeDetailInfo.getOrderId())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAccount2()),
                    StringUnit.phoneJiaMi(getRightValue(tradeDetailInfo
                            .getAccount2()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getMobileno()),
                    StringUnit.phoneJiaMi(getRightValue(tradeDetailInfo
                            .getMobileno()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getPayType()), ("02"
                    .equals(getRightValue(tradeDetailInfo.getPayType())) ? "账户支付"
                    : "刷卡支付")));
        }

        else if (RyxAppconfig.QT_QCOIN_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_JD_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_NO1_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_YMX_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_SUNING_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_WM_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_SJTC_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_SD_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_GY_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_BKCS_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_REFUEL_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_ALIPAY_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))
                || RyxAppconfig.QT_FLOW_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))


                ) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getOrderId()),
                    getRightValue(tradeDetailInfo.getOrderId())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAccount2()),
                    StringUnit.phoneJiaMi(getRightValue(tradeDetailInfo
                            .getAccount2()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param("支付账户", StringUnit.phoneJiaMi(QtpayAppData
                    .getInstance((DetailsTabMainActivity)mListener).getMobileNo())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getPayType()), ("02"
                    .equals(getRightValue(tradeDetailInfo.getPayType())) ? "账户支付"
                    : "刷卡支付")));

            LogUtil.showLog("type==" + getRightValue(tradeDetailInfo.getPayType()));
            details.add(new Param(
                    getLeftValue(tradeDetailInfo.getAccount()),
                    StringUnit.cardJiaMi(getRightValue(tradeDetailInfo.getAccount()))));
        }
        else if (RyxAppconfig.QT_SENIOR_MERCHANT
                .equals(getRightValue(tradeDetailInfo.getMerchantId()))) {
            details.add(new Param(getLeftValue(tradeDetailInfo.getOrderId()),
                    getRightValue(tradeDetailInfo.getOrderId())));
            details.add(new Param(getLeftValue(tradeDetailInfo.getTransName()),
                    getRightValue(tradeDetailInfo.getTransName())));

            details.add(new Param(getLeftValue(tradeDetailInfo.getLocalTime()),
                    fomatDate(getRightValue(tradeDetailInfo.getLocalDate())
                            + getRightValue(tradeDetailInfo.getLocalTime()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getAmount()),
                    MoneyEncoder.decodeFormat(getRightValue(tradeDetailInfo
                            .getAmount()))));
            details.add(new Param(getLeftValue(tradeDetailInfo.getPayType()), ("02"
                    .equals(getRightValue(tradeDetailInfo.getPayType())) ? "账户支付"
                    : "刷卡支付")));
            details.add(new Param("支付账户", StringUnit.phoneJiaMi(QtpayAppData
                    .getInstance((DetailsTabMainActivity)mListener).getMobileNo())));

        }
    }

    public String getLeftValue(String value) {
        String[] values;
        if (value != null) {
            values = value.split("\\|");
            return values[0];
        }
        return "";
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Click(R.id.btn_looksign)
    public void btn_saveClick() {
        Intent intent = new Intent(getActivity(),
                SignRequisitions_.class);
        intent.putExtra("tradeDetailInfo", tradeDetailInfo);
        intent.putExtra("fromtype",
                RyxAppconfig.VIEW_SIGNREQUISITIONS);
        intent.putExtra("showunplugin", "no");
        startActivity(intent);
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

    public static String fomatDate(String date) {
        return DateUtil.DateToString(DateUtil.StrToDate(date));
    }
}
