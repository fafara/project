package com.ryx.payment.ruishua.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.RuiBeanBuyUseRecordMainActivity;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.adapter.RuibeanRecordGridAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 瑞豆使用记录Frg
 */
@EFragment(R.layout.fragment_ruibean_use_record)
public class RuibeanUseRecordFragment extends BaseFragment {

    private static RuibeanUseRecordFragment thisInstance;
    private BaseActivity baseActivity;
    @ViewById(R.id.rc_ruibean_use_record)
    public XRecyclerView rc_ruibean_use_record;
    @ViewById(R.id.nodatalayout)
    AutoLinearLayout nodatalayout;
    private RuibeanRecordGridAdapter ruibeanRecordGridAdapter;
    private List<Map> usedlist = new ArrayList<Map>();
    String localdate;String localtime;
    /**
     * 使用记录Frag
     * @return
     */
    public static RuibeanUseRecordFragment getInstance() {
        return new RuibeanUseRecordFragment_();
    }

    @Override
    public void onAttach(Context context) {
        try {
            baseActivity = getBaseActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);

    }
    @AfterViews
    public void afterView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rc_ruibean_use_record.setLayoutManager(layoutManager);
        ruibeanRecordGridAdapter = new RuibeanRecordGridAdapter(usedlist, getActivity(), RuiBeanBuyUseRecordMainActivity.USERECORDFRAG,R.layout.ruibean_usefrg_detailslist_item);
        rc_ruibean_use_record.setPullRefreshEnabled(true);
        rc_ruibean_use_record.setLoadingMoreEnabled(true);
        rc_ruibean_use_record.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rc_ruibean_use_record.setAdapter(ruibeanRecordGridAdapter);
        rc_ruibean_use_record.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                httpGetResult(true);
            }

            @Override
            public void onLoadMore() {
                httpGetResult(false);
            }
        });
        httpGetResult(true);
    }

    private void httpGetResult(final boolean isRefresh) {
//        baseActivity.showLoading();
        if(isRefresh){
            SimpleDateFormat formatterdate = new SimpleDateFormat(
                    "yyyyMMdd");
            SimpleDateFormat formattertime = new SimpleDateFormat("HHmmss");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            localdate = formatterdate.format(curDate);
            localtime = formattertime.format(curDate);
        }
        baseActivity.initQtPatParams();
        baseActivity.qtpayApplication.setValue("customerGoldbeanDetail.Req");
        baseActivity.qtpayAttributeList.add(baseActivity.qtpayApplication);
        baseActivity.qtpayParameterList.add(new Param("type","D"));
        baseActivity.qtpayParameterList.add(new Param("localdate",localdate));
        baseActivity.qtpayParameterList.add(new Param("localtime",localtime));
        baseActivity.httpsPost("customerGoldbeanDetailTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if(isRefresh){
                    rc_ruibean_use_record.refreshComplete();
                    usedlist.clear();
                }else{
                    rc_ruibean_use_record.loadMoreComplete();
                }
//                {"result":[],"code":"0000","msg":"请求成功！"}
                String data=  payResult.getData();
//                String data="{\"result\":[{\"customerid\":\"A000662471\",\"customerid2\":\"A000662471\",\"productdesc\":\"其他\",\"orderid\":\"170525462462456\",\"localdate\":\"20170524\",\"localtime\":\"173456\",\"type\":\"购买\",\"count\":\"30\",\"count_real\":\"30\",\"amount\":\"3\",\"count_before\":\"30\",\"count_after\":\"60\"},{\"customerid\":\"A000662471\",\"customerid2\":\"A000662471\",\"productdesc\":\"闪付\",\"orderid\":\"170525462462456\",\"localdate\":\"20170524\",\"localtime\":\"173454\",\"type\":\"购买\",\"count\":\"30\",\"count_real\":\"30\",\"amount\":\"3\",\"count_before\":\"30\",\"count_after\":\"0\"}],\"code\":\"0000\",\"msg\":\"请求成功！\"}";
                try {
                    JSONObject dataJson=new JSONObject(data);
                    String code= JsonUtil.getValueFromJSONObject(dataJson,"code");
                    if(RyxAppconfig.QTNET_SUCCESS.equals(code)){
                        JSONArray resultArray=dataJson.getJSONArray("result");
                        if(resultArray!=null&&resultArray.length()>0){
                            for(int i=0;i<resultArray.length();i++){
                                HashMap ruibeanMap=new HashMap();
                                JSONObject ruibeanObj= resultArray.getJSONObject(i);
                                localdate=  JsonUtil.getValueFromJSONObject(ruibeanObj,"localdate");
                                localtime=  JsonUtil.getValueFromJSONObject(ruibeanObj,"localtime");
                                ruibeanMap.put("localdate",localdate);
                                ruibeanMap.put("localtime",localtime);
                                String type=  JsonUtil.getValueFromJSONObject(ruibeanObj,"productdesc");
                                ruibeanMap.put("type",type);
                                String count_real=  JsonUtil.getValueFromJSONObject(ruibeanObj,"count_real");
                                ruibeanMap.put("count",count_real);
                                usedlist.add(ruibeanMap);
                            }
                            ruibeanRecordGridAdapter.notifyDataSetChanged();
                        }else{
                            if(isRefresh){
                                //没有记录
                                LogUtil.showToast(getBaseActivity(),"暂无使用记录");
                                ruibeanRecordGridAdapter.notifyDataSetChanged();
                                nodatalayout.setVisibility(View.VISIBLE);
                                rc_ruibean_use_record.setVisibility(View.GONE);
                            }else{
                                nodatalayout.setVisibility(View.GONE);
                                rc_ruibean_use_record.setVisibility(View.VISIBLE);
                                LogUtil.showToast(getBaseActivity(),"没有更多使用记录");
                            }
                        }
                    }else{
                        String msg= JsonUtil.getValueFromJSONObject(dataJson,"msg");
                        LogUtil.showToast(getBaseActivity(),msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.showToast(getBaseActivity(),"数据异常");
                }

            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                if(isRefresh){
                    rc_ruibean_use_record.refreshComplete();
                }else{
                    rc_ruibean_use_record.loadMoreComplete();
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                if(isRefresh){
                    rc_ruibean_use_record.refreshComplete();
                }else{
                    rc_ruibean_use_record.loadMoreComplete();
                }
            }
        });

    }
}
