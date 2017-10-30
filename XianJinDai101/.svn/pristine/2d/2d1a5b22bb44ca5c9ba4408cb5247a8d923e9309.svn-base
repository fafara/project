package com.ryx.payment.ruishua.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.adapter.IncomeAdapter;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.usercenter.BMIncomeAndExpenditureDetails2;
import com.ryx.payment.ruishua.usercenter.DetailsTabMainActivity_;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiepingping on 2016/8/2.
 */
//缴费
@EFragment(R.layout.fragment_payment)
public class PaymentFragment extends BaseFragment {

    @ViewById
    ListView lv_payment;

    @ViewById
    MaterialRefreshLayout materialRefreshLayout;
    @ViewById
    AutoLinearLayout lay_top;
    @ViewById
    ImageView iv_norecord;
    private FragmentListener mListener;
    public LinkedList<TradeDetailInfo> paymentList = new LinkedList<TradeDetailInfo>();
    private IncomeAdapter adapter;
    public boolean isRefresh=true;

    @AfterViews
    public void afterView(){
        initViews();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag","payment");
        isRefresh = true;
        mListener.doDataRequest(map);
    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (BMIncomeAndExpenditureDetails2) getBaseActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);

    }

    /**
     * 创建资金动态实例
     *
     * @return
     */
    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment_();
        return fragment;
    }

    private void initViews(){
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("flag","payment");
                isRefresh = true;
                mListener.doDataRequest(map);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("flag","payment");
                isRefresh = false;
                mListener.doDataRequest(map);
            }

        });

        lv_payment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent((BMIncomeAndExpenditureDetails2)mListener,
                        DetailsTabMainActivity_.class);
                intent.putExtra("detailInfo", paymentList.get(position));
                intent.putExtra("ActivityFlag", "payment");
                intent.putExtra("fromtype", 1); // 1 代表是 查看明细
                startActivity(intent);
            }
        });
        adapter = new IncomeAdapter((BMIncomeAndExpenditureDetails2) mListener, paymentList);
        lv_payment.setAdapter(adapter);
    }

    /**
     * @param actiontype
     * @param list
     */
    public void send(int actiontype, LinkedList<TradeDetailInfo> list){
        if (actiontype == 0x111) {
            if (!isRefresh&&list== null) {
                LogUtil.showToast((BMIncomeAndExpenditureDetails2)mListener,"无更多记录！");
            }else if(isRefresh&&list== null){
                LogUtil.showToast((BMIncomeAndExpenditureDetails2)mListener,"无记录！");
            }
            notifyDataSetChanged(list);
        }else if(actiontype == 0x222){
            if (isRefresh) {
                materialRefreshLayout.finishRefresh();
            }else{
                materialRefreshLayout.finishRefreshLoadMore();
            }
            if (paymentList.size() != 0) {
                lv_payment.setVisibility(View.VISIBLE);
                lay_top.setVisibility(View.GONE);
            } else {
                lay_top.setVisibility(View.VISIBLE);
                lv_payment.setVisibility(View.GONE);
            }
        }

    }

    public void notifyDataSetChanged(List list) {
        if (isRefresh) {
            paymentList.clear();
            if (list != null) {
                paymentList.addAll(list);
            }
            materialRefreshLayout.finishRefresh();
        } else {
            if (list != null) {
                paymentList.addAll(list);
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
        adapter.notifyDataSetChanged();
        if (paymentList.size() != 0) {
            lv_payment.setVisibility(View.VISIBLE);
            lay_top.setVisibility(View.GONE);
        } else {
            lay_top.setVisibility(View.VISIBLE);
            lv_payment.setVisibility(View.GONE);
        }
    }
}
