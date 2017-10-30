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
//付款
@EFragment(R.layout.fragment_expenditure)
public class ExpenditureFragment extends BaseFragment {

    @ViewById
    ListView lv_expenditure;

    @ViewById
    MaterialRefreshLayout materialRefreshLayout;
    @ViewById
    AutoLinearLayout lay_top;

    private FragmentListener mListener;
    public LinkedList<TradeDetailInfo> expendList = new LinkedList<TradeDetailInfo>();
    private  IncomeAdapter adapter;
    public boolean isRefresh=true;

    @AfterViews
    public void afterView() {
        initViews();
        Map<String, Object> map = new HashMap<String, Object>();
        isRefresh = true;
        map.put("flag", "expend");
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
    public static ExpenditureFragment newInstance() {
        ExpenditureFragment fragment = new ExpenditureFragment_();
        return fragment;
    }

    private void initViews() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("flag", "expend");
                isRefresh = true;
                mListener.doDataRequest(map);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("flag", "expend");
                isRefresh = false;
                mListener.doDataRequest(map);
            }

        });

        lv_expenditure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent((BMIncomeAndExpenditureDetails2)mListener,
                        DetailsTabMainActivity_.class);
                intent.putExtra("detailInfo", expendList.get(position));
                intent.putExtra("ActivityFlag", "expend");
                intent.putExtra("fromtype", 1); // 1 代表是 查看明细
                startActivity(intent);
            }
        });
        adapter = new IncomeAdapter((BMIncomeAndExpenditureDetails2) mListener, expendList);
        lv_expenditure.setAdapter(adapter);
    }

    /**
     * @param actiontype
     * @param list
     */
    public void send(int actiontype, List list) {

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
            if (expendList.size() != 0) {
                lv_expenditure.setVisibility(View.VISIBLE);
                lay_top.setVisibility(View.GONE);
            } else {
                lay_top.setVisibility(View.VISIBLE);
                lv_expenditure.setVisibility(View.GONE);
            }
        }
    }

    public void notifyDataSetChanged(List list) {
        if (isRefresh) {
            expendList.clear();
            if (list != null) {
                expendList.addAll(list);
            }
            materialRefreshLayout.finishRefresh();
        } else {
            if (list != null) {
                expendList.addAll(list);
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
        adapter.notifyDataSetChanged();
        if (expendList.size() != 0) {
            lv_expenditure.setVisibility(View.VISIBLE);
            lay_top.setVisibility(View.GONE);
        } else {
            lay_top.setVisibility(View.VISIBLE);
            lv_expenditure.setVisibility(View.GONE);
        }
    }
}
