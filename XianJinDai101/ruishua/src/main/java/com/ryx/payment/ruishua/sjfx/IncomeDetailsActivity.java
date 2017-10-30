package com.ryx.payment.ruishua.sjfx;

import android.content.Intent;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.sjfx.adapter.IncomeAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.OnListItemClickListener;
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
 * Created by xiepp on 2017/3/21.
 */
@EActivity(R.layout.sjfx_activity_my_income)
public class IncomeDetailsActivity extends BaseActivity {
    @ViewById(R.id.rv_income)
    public XRecyclerView rv_income;
//    @ViewById(R.id.materialRefreshLayout)
//    MaterialRefreshLayout materialRefreshLayout;
    private IncomeAdapter incomeAdapter;
    private List<Map> list = new ArrayList<Map>();
    private String localdate;
    @ViewById(R.id.nodatalayout)
    AutoLinearLayout nodatalayout;
    @AfterViews
    public void initViews(){
        setTitleLayout("我的收益", true, false);
        initQtPatParams();
        getDetailData(true);
        RecyclerViewHelper.init().setRVGridLayout(this, rv_income, 1);//1列
        incomeAdapter = new IncomeAdapter(list, this, R.layout.sjfx_adapter_income_item);
        incomeAdapter.openLoadAnimation(new ScaleInAnimation());
        rv_income.setPullRefreshEnabled(true);
        rv_income.setLoadingMoreEnabled(true);
        rv_income.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rv_income.setAdapter(incomeAdapter);
        rv_income.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getDetailData(true);
            }

            @Override
            public void onLoadMore() {
                getDetailData(false);
            }
        });
        //点击查看明细
        incomeAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
            Intent intent = new Intent(IncomeDetailsActivity.this,IncomeDetailInfoActivity_.class);
            intent.putExtra("localdate",(String)((Map)data).get("localdate"));
            startActivity(intent);
            }
        });
//        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
//            @Override
//            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
//                getDetailData(true);
//            }
//
//            @Override
//            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
//                getDetailData(false);
//            }
//
//        });
    }
    private void getDetailData(final boolean isfresh) {
        nodatalayout.setVisibility(View.GONE);
        qtpayApplication.setValue("IncomeDays.Req");
        qtpayAttributeList.add(qtpayApplication);
        if(!isfresh){
            qtpayParameterList.add(new Param("localdate",localdate));
        }
        httpsPost("IncomeDetailTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    String data=payResult.getData();
                    JSONObject dataJsonObj = new JSONObject(data);
                    String code=  JsonUtil.getValueFromJSONObject(dataJsonObj,"code");
                    if(RyxAppconfig.QTNET_SUCCESS.equals(code)) {
                        // 解析交易详情
                        JSONArray detailsArray = dataJsonObj.getJSONArray("result");
                        if(isfresh){
                            list.clear();
                            if(detailsArray.length()==0){
                                LogUtil.showToast(IncomeDetailsActivity.this,"暂无数据");
                                nodatalayout.setVisibility(View.VISIBLE);
                                incomeAdapter.notifyDataSetChanged();
                                rv_income.refreshComplete();
                                return;
                            }
                        } else{
                            if(detailsArray.length()==0){
                                LogUtil.showToast(IncomeDetailsActivity.this,"没有更多了!");
                                incomeAdapter.notifyDataSetChanged();
                                rv_income.loadMoreComplete();
                                return;
                            }
                        }
                        for (int i = 0; i < detailsArray.length(); i++) {
//                           {"lvl":"1","onlycode":"20002820170409154023548025","dt_name":"T0交易","money":"0.6","deal_date":"20170409","type_id":"0","customer_id":"A005606681"}
                            Map map =  new HashMap<>();
                              localdate=  JsonUtil.getValueFromJSONObject(
                                    detailsArray.getJSONObject(i), "localdate");
                            map.put("localdate",localdate);
                            map.put("amount",JsonUtil.getValueFromJSONObject(
                                    detailsArray.getJSONObject(i), "amount"));
//                            map.put("pay_income",JsonUtil.getValueFromJSONObject(
//                                    detailsArray.getJSONObject(i), "pay_income"));
                            map.put("paymsg",JsonUtil.getValueFromJSONObject(
                                    detailsArray.getJSONObject(i), "paymsg"));
                            list.add(map);
                        }
                        incomeAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(isfresh){
                    rv_income.refreshComplete();
                }else{
                    rv_income.loadMoreComplete();
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                if(isfresh){
                    rv_income.refreshComplete();
                }else{
                    rv_income.loadMoreComplete();
                }
                nodatalayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onOtherState(String rescode, String resDesc) {
                super.onOtherState(rescode, resDesc);
                if(isfresh){
                    rv_income.refreshComplete();
                }else{
                    rv_income.loadMoreComplete();
                }
                nodatalayout.setVisibility(View.VISIBLE);
            }

        });
    }


}
