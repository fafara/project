package com.ryx.payment.ruishua.usercenter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.adapter.IncomeAdapter;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.fragment.CashFragment;
import com.ryx.payment.ruishua.fragment.ExpenditureFragment;
import com.ryx.payment.ruishua.fragment.PaymentFragment;
import com.ryx.payment.ruishua.fragment.SweepCodeFragment;
import com.ryx.payment.ruishua.fragment.WithDrawlFragment;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * 交易明细
 */
@EActivity(R.layout.activity_bmincome_and_expenditure_details2)
public class BMIncomeAndExpenditureDetails2 extends BaseActivity implements FragmentListener {
    Param qtpayflag, qtpayRecordDate, qtpayRecordTime;
    LinkedList<TradeDetailInfo> templist = new LinkedList<TradeDetailInfo>();
    private String date, time;
    String jsondata = "";
    String isIncomeLast = "0"; // 收入数据是否加载完
    String sumAmount;
    IncomeAdapter IncomeAdapter;
    private String selectFlag = "income";

    //收款
    CashFragment cashFragment;
    //付款
    ExpenditureFragment expenditureFragment;
    //结算
    WithDrawlFragment withdrawlFragment;
    //缴费
    PaymentFragment payFragment;
    //扫码支付
    SweepCodeFragment sweepCodeFragment;

    @ViewById
    TabLayout commonTabLayout_rs;
    @ViewById
    TabLayout commonTabLayout_ryx;

//    新版瑞银信：收款：明细，电子收据，资金动态，补充代发黑名单资料，                       更换结算卡
//    扫码收款：明细，电子收据，资金动态
//    付款：明细，电子收据
//    结算：明细，无电子收据，资金动态
//    便民：明细，电子收据

    @AfterViews
    public void afterView() {
        setTitleLayout("交易明细", true, false);
//        if("01".equals(RyxAppconfig.BRANCH)){
        if(1==RyxAppdata.getInstance(this).getCurrentBranchType_Details()){
            //瑞刷
            commonTabLayout_rs.setVisibility(View.VISIBLE);
            commonTabLayout_ryx.setVisibility(View.GONE);
        }else  if(2==RyxAppdata.getInstance(this).getCurrentBranchType_Details()){
            //瑞银信
            commonTabLayout_rs.setVisibility(View.GONE);
            commonTabLayout_ryx.setVisibility(View.VISIBLE);
            expenditureFragment = ExpenditureFragment.newInstance();
            withdrawlFragment = WithDrawlFragment.newInstance();
        }
        cashFragment = CashFragment.newInstance();
        payFragment = PaymentFragment.newInstance();
        sweepCodeFragment=SweepCodeFragment.newInstance();
        initQtPatParams();
//        if("01".equals(RyxAppconfig.BRANCH)){
        if(1==RyxAppdata.getInstance(this).getCurrentBranchType_Details()){
            //瑞刷
            commonTabLayout_rs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        toCashFrag();
                    } else if (tab.getPosition() == 1) {
                        toSweepCodeFrag();
                    }else if(tab.getPosition() == 2){
                        toPaymentFrag();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }else
//        if("02".equals(RyxAppconfig.BRANCH))
        if(2==RyxAppdata.getInstance(this).getCurrentBranchType_Details())
        {
            //瑞银信
            commonTabLayout_ryx.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        //收款
                        toCashFrag();
                    } else if (tab.getPosition() == 1) {
                        //二维码收款
                        toSweepCodeFrag();
                    }else if(tab.getPosition() == 2){
                        //付款
                        toExpenditureFrag();
                    }else if(tab.getPosition() == 3){
                        //提现
                        toWithdrawalFrag();
                    }else if(tab.getPosition() == 4){
                        //缴费
                        toPaymentFrag();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        toCashFrag();
    }

    /**
     * 获取交易明细
     */
    private void getDetailData(final String flag) {
        if ("income".equals(flag)) {
            qtpayApplication.setValue("GetTradeRecordIn2.Req");
            qtpayflag.setValue("SF");
            if (!cashFragment.isRefresh) {
                date = cashFragment.cashList.get(cashFragment.cashList.size() - 1).getLocalDate()
                        .split("\\|")[1];
                time = cashFragment.cashList.get(cashFragment.cashList.size() - 1).getLocalTime()
                        .split("\\|")[1];
            } else {
                SimpleDateFormat formatterdate = new SimpleDateFormat(
                        "yyyyMMdd");
                SimpleDateFormat formattertime = new SimpleDateFormat("HHmmss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                date = formatterdate.format(curDate);
                time = formattertime.format(curDate);
            }
        } else if ("expend".equals(flag)) {
            qtpayApplication.setValue("GetTradeRecordIn2.Req");
            qtpayflag.setValue("newOut");
            if (!expenditureFragment.isRefresh) {
                date = expenditureFragment.expendList.get(expenditureFragment.expendList.size() - 1).getLocalDate()
                        .split("\\|")[1];
                time = expenditureFragment.expendList.get(expenditureFragment.expendList.size() - 1).getLocalTime()
                        .split("\\|")[1];
            } else {
                SimpleDateFormat formatterdate = new SimpleDateFormat(
                        "yyyyMMdd");
                SimpleDateFormat formattertime = new SimpleDateFormat("HHmmss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                date = formatterdate.format(curDate);
                time = formattertime.format(curDate);
            }
        } else if ("payment".equals(flag)) {
            qtpayApplication.setValue("GetTradeRecordIn2.Req");
            qtpayflag.setValue("DETAIL");
            if (!payFragment.isRefresh) {
                date = payFragment.paymentList.get(payFragment.paymentList.size() - 1).getLocalDate()
                        .split("\\|")[1];
                time = payFragment.paymentList.get(payFragment.paymentList.size() - 1).getLocalTime()
                        .split("\\|")[1];
            } else {
                SimpleDateFormat formatterdate = new SimpleDateFormat(
                        "yyyyMMdd");
                SimpleDateFormat formattertime = new SimpleDateFormat("HHmmss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                date = formatterdate.format(curDate);
                time = formattertime.format(curDate);
            }
        } else if ("sweepcode".equals(flag)) {
            //扫码支付
            qtpayApplication.setValue("GetTradeRecordIn2.Req");//扫码支付后台接口未提供,暂时测试写这个哈
            qtpayflag.setValue("SAOMA");
            if (!sweepCodeFragment.isRefresh) {
                date = sweepCodeFragment.paymentList.get(sweepCodeFragment.paymentList.size() - 1).getLocalDate()
                        .split("\\|")[1];
                time = sweepCodeFragment.paymentList.get(sweepCodeFragment.paymentList.size() - 1).getLocalTime()
                        .split("\\|")[1];
            } else {
                SimpleDateFormat formatterdate = new SimpleDateFormat(
                        "yyyyMMdd");
                SimpleDateFormat formattertime = new SimpleDateFormat("HHmmss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                date = formatterdate.format(curDate);
                time = formattertime.format(curDate);
            }
        }else {
            qtpayApplication.setValue("GetTradeRecordIn2.Req");
            qtpayflag.setValue("DEAW");
            if (!withdrawlFragment.isRefresh) {
                date = withdrawlFragment.withdrawlList.get(withdrawlFragment.withdrawlList.size() - 1).getLocalDate()
                        .split("\\|")[1];
                time = withdrawlFragment.withdrawlList.get(withdrawlFragment.withdrawlList.size() - 1).getLocalTime()
                        .split("\\|")[1];
            } else {
                SimpleDateFormat formatterdate = new SimpleDateFormat(
                        "yyyyMMdd");
                SimpleDateFormat formattertime = new SimpleDateFormat("HHmmss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                date = formatterdate.format(curDate);
                time = formattertime.format(curDate);
            }
        }
        qtpayRecordDate.setValue(date);
        qtpayRecordTime.setValue(time);

        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayflag);
        qtpayParameterList.add(qtpayRecordDate);
        qtpayParameterList.add(qtpayRecordTime);
        showLoading();
        httpsPost("GetTradeRecordIn2Tag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult qtpayResult) {
                if (qtpayResult != null
                        && qtpayResult.getRespCode().equals(
                        RyxAppconfig.QTNET_SUCCESS)) {
                    jsondata = qtpayResult.getData();
                    LinkedList<TradeDetailInfo> tempList = getDetailList();
                    if ("income".equals(flag)) {
                        cashFragment.send(0x111, tempList);
                    } else if ("expend".equals(flag)) {
                        expenditureFragment.send(0x111, tempList);
                    } else if ("payment".equals(flag)) {
                        payFragment.send(0x111, tempList);
                    }else if("sweepcode".equals(flag)){
                        sweepCodeFragment.send(0x111, tempList);
                    } else {
                        withdrawlFragment.send(0x111, tempList);
                    }
                } else {
                    com.ryx.payment.ruishua.utils.LogUtil.showToast(BMIncomeAndExpenditureDetails2.this,
                            qtpayResult.getRespDesc());
                    jsondata = "";
                    if ("income".equals(flag)) {
                        cashFragment.send(0x111, null);
                    } else if ("expend".equals(flag)) {
                        expenditureFragment.send(0x111, null);
                    } else if ("payment".equals(flag)) {
                        payFragment.send(0x111, null);
                    }else if("sweepcode".equals(flag)) {
                        sweepCodeFragment.send(0x111, null);
                    } else {
                        withdrawlFragment.send(0x111, null);
                    }
                }
            }

            @Override
            public void onTradeFailed() {
                jsondata = "";
                if ("income".equals(flag)) {
                    cashFragment.send(0x222, null);
                } else if ("expend".equals(flag)) {
                    expenditureFragment.send(0x222, null);
                } else if ("payment".equals(flag)) {
                    payFragment.send(0x222, null);
                }else if ("sweepcode".equals(flag)) {
                    sweepCodeFragment.send(0x222, null);
                } else {
                    withdrawlFragment.send(0x222, null);
                }
            }

            @Override
            public void onOtherState() {
                jsondata = "";
                if ("income".equals(flag)) {
                    cashFragment.send(0x222, null);
                } else if ("expend".equals(flag)) {
                    expenditureFragment.send(0x222, null);
                } else if ("payment".equals(flag)) {
                    payFragment.send(0x222, null);
                } else if ("sweepcode".equals(flag)) {
                    sweepCodeFragment.send(0x222, null);
                }else {
                    withdrawlFragment.send(0x222, null);
                }
            }

            @Override
            public void onLoginAnomaly() {
                finish();
            }
        });
    }

    @Override
    public void doDataRequest(Object data) {
        Map<String, Object> map = (Map<String, Object>) data;
        selectFlag = (String) map.get("flag");
        getDetailData(selectFlag);
    }

    @Override
    public void doDataRequest(String type, Object data) {

    }

    public LinkedList<TradeDetailInfo> getDetailList() {
        LinkedList<TradeDetailInfo> list = null;
        if (jsondata != null && jsondata.length() > 0) {
            try {
                JSONObject jsonObj = new JSONObject(jsondata);
                isIncomeLast = (String) jsonObj.getJSONObject("summary")
                        .getString("isLast");
                sumAmount = jsonObj.getString("sumAmount");

                LogUtil.showLog("sumAmount===" + sumAmount);


                // 解析交易详情
                JSONArray detailsArray = jsonObj.getJSONArray("resultBean");
                list = new LinkedList<TradeDetailInfo>();
                TradeDetailInfo detailinfo;
                for (int i = 0; i < detailsArray.length(); i++) {
                    detailinfo = new TradeDetailInfo();
                    detailinfo.setStatus(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "status"));
                    detailinfo.setSignPic(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "signPic"));
                    detailinfo.setLocalDate(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "localDate"));
                    detailinfo.setTermId(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "termId"));
                    detailinfo.setLocalLogNo(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "localLogNo"));
                    detailinfo.setFee(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "fee"));
                    detailinfo.setAmount(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "amount"));
                    detailinfo.setBizAmount(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "bizAmount"));
                    detailinfo.setBranchId(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "branchId"));
                    detailinfo.setPayType(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "payType"));
                    detailinfo.setAccount(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "account"));
                    detailinfo.setAccount2(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "account2"));
                    detailinfo.setMerchantId(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "merchantId"));
                    detailinfo.setLocalTime(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "localTime"));
                    detailinfo.setOrderId(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "orderId"));
                    detailinfo.setTransName(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "transName"));
                    detailinfo.setBranchName(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "branchName"));
                    detailinfo.setMobileno(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "mobileno"));
                    detailinfo.setDeawstatus(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "deawstatus"));

                    detailinfo.setPaytag(JsonUtil.getValueFromJSONObject(
                            detailsArray.getJSONObject(i), "payTag"));

                    list.add(detailinfo);
                    detailinfo = null;
                }
                if (list != null && list.size() != 0) {
                    return list;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                jsondata = "";
            }

        }
        return null;
    }


    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");

        qtpayflag = new Param("flag");
        qtpayRecordDate = new Param("tradeRecordDate");
        qtpayRecordTime = new Param("tradeRecordTime");

        isNeedThread = false;
    }

    /**
     * 收入
     */
    private void toCashFrag() {
        selectFlag = "cash";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tabfragemetcomtent, cashFragment, "cash");
        fragmentTransaction.commit();
    }

    /**
     * 付款
     */
    private void toExpenditureFrag() {
        selectFlag = "expend";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tabfragemetcomtent, expenditureFragment, "expend");
        fragmentTransaction.commit();
    }

    /**
     * 提现
     */
    private void toWithdrawalFrag() {
        selectFlag = "withdrawal";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tabfragemetcomtent, withdrawlFragment, "withdrawal");
        fragmentTransaction.commit();
    }

    /**
     * 缴费
     */
    private void toPaymentFrag() {
        selectFlag = "payment";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tabfragemetcomtent, payFragment, "payment");
        fragmentTransaction.commit();
    }
    /**
     * 去扫码支付明细列表页
     */
    private void toSweepCodeFrag() {
        selectFlag = "sweepcode";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tabfragemetcomtent, sweepCodeFragment, "sweepcode");
        fragmentTransaction.commit();
    }

}
