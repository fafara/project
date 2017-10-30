package com.ryx.payment.ruishua.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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
//提现
@EFragment(R.layout.fragment_withdrawl)
public class WithDrawlFragment extends BaseFragment {

    @ViewById
    ListView lv_withdrawl;
    @ViewById
    MaterialRefreshLayout materialRefreshLayout;
    @ViewById
    AutoLinearLayout lay_top;

    private FragmentListener mListener;
    public LinkedList<TradeDetailInfo> withdrawlList = new LinkedList<TradeDetailInfo>();
    private IncomeAdapter adapter;
    public boolean isRefresh = true;

    /**
     * 创建资金动态实例
     *
     * @return
     */
    public static WithDrawlFragment newInstance() {
        WithDrawlFragment fragment = new WithDrawlFragment_();
        return fragment;
    }
    @AfterViews
    public void afterView(){
        initViews();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag","withdrawl");
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
    private void initViews(){
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("flag","withdrawl");
                isRefresh = true;
                mListener.doDataRequest(map);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("flag","withdrawl");
                isRefresh = false;
                mListener.doDataRequest(map);
            }

        });

        lv_withdrawl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent((BMIncomeAndExpenditureDetails2)mListener,
                        DetailsTabMainActivity_.class);
                intent.putExtra("detailInfo", withdrawlList.get(position));
                intent.putExtra("ActivityFlag", "withdrawl");
                intent.putExtra("fromtype", 1); // 1 代表是 查看明细
                startActivity(intent);
            }
        });
        adapter = new IncomeAdapter((BMIncomeAndExpenditureDetails2) mListener, withdrawlList);
        lv_withdrawl.setAdapter(adapter);
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
            if (withdrawlList.size() != 0) {
                lv_withdrawl.setVisibility(View.VISIBLE);
                lay_top.setVisibility(View.GONE);
            } else {
                lay_top.setVisibility(View.VISIBLE);
                lv_withdrawl.setVisibility(View.GONE);
            }
        }

    }

    public void notifyDataSetChanged(List list) {
        if (isRefresh) {
            withdrawlList.clear();
            if (list != null) {
                withdrawlList.addAll(list);
            }
            materialRefreshLayout.finishRefresh();
        } else {
            if (list != null) {
                withdrawlList.addAll(list);
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
        adapter.notifyDataSetChanged();
        if (withdrawlList.size() != 0) {
            lv_withdrawl.setVisibility(View.VISIBLE);
            lay_top.setVisibility(View.GONE);
        } else {
            lay_top.setVisibility(View.VISIBLE);
            lv_withdrawl.setVisibility(View.GONE);
        }
    }
}
