package com.ryx.payment.ruishua.sjfx;

import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.sjfx.adapter.IncomeDetailInfoAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.quickadapter.recyclerView.animation.ScaleInAnimation;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiepp on 2017/6/16.
 */

@EActivity(R.layout.sjfx_activity_income_detail_info)
public class IncomeDetailInfoActivity extends BaseActivity {
    @ViewById
    public XRecyclerView rv_income_detail;

    @ViewById
    public TextView tv_empty;
    private IncomeDetailInfoAdapter incomeAdapter;
    private List<Map> list = new ArrayList<Map>();
    @ViewById
    AutoLinearLayout lay_empty;//无数据显示内容

    private String localdate;//具体哪一天的收益

    @AfterViews
    public void afterViews() {
        setTitleLayout("收益明细", true, false);
        initQtPatParams();

        RecyclerViewHelper.init().setRVGridLayout(this, rv_income_detail, 1);//1列
        incomeAdapter = new IncomeDetailInfoAdapter(list, this, R.layout.sjfx_adapter_income_item);
        incomeAdapter.openLoadAnimation(new ScaleInAnimation());
        rv_income_detail.setPullRefreshEnabled(false);
        rv_income_detail.setLoadingMoreEnabled(false);
        rv_income_detail.setAdapter(incomeAdapter);

        //收益的时间
        if (getIntent().hasExtra("localdate")) {
            localdate = getIntent().getStringExtra("localdate");
        }
        getDetailData();
    }

    private void getDetailData() {
        lay_empty.setVisibility(View.GONE);
        qtpayApplication.setValue("IncomeDetail.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("localdate", localdate));
        httpsPost("IncomeDetail", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    String data = payResult.getData();
                    JSONObject dataJsonObj = new JSONObject(data);
                    String code = JsonUtil.getValueFromJSONObject(dataJsonObj, "code");
                    if (RyxAppconfig.QTNET_SUCCESS.equals(code)) {
                        // 解析交易详情
                        JSONArray detailsArray = dataJsonObj.getJSONArray("result");
                        list.clear();
                        if (detailsArray.length() == 0) {
                            LogUtil.showToast(IncomeDetailInfoActivity.this, "暂无数据");
                            lay_empty.setVisibility(View.VISIBLE);
                            incomeAdapter.notifyDataSetChanged();
                            rv_income_detail.refreshComplete();
                            return;
                        }
                        for (int i = 0; i < detailsArray.length(); i++) {
//                           {"lvl":"1","onlycode":"20002820170409154023548025","dt_name":"T0交易","money":"0.6","deal_date":"20170409","type_id":"0","customer_id":"A005606681"}
                            Map map = new HashMap<>();
                            localdate = JsonUtil.getValueFromJSONObject(
                                    detailsArray.getJSONObject(i), "deal_date");
                            map.put("deal_date", localdate);
                            map.put("dt_name",JsonUtil.getValueFromJSONObject(
                                    detailsArray.getJSONObject(i), "dt_name"));
                            map.put("money", JsonUtil.getValueFromJSONObject(
                                    detailsArray.getJSONObject(i), "money"));
//                            map.put("pay_income",JsonUtil.getValueFromJSONObject(
//                                    detailsArray.getJSONObject(i), "pay_income"));
                            list.add(map);
                        }
                        incomeAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                lay_empty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onOtherState(String rescode, String resDesc) {
                super.onOtherState(rescode, resDesc);
                lay_empty.setVisibility(View.VISIBLE);
            }

        });
    }


}
