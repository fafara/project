package com.ryx.ryxcredit.ryd.borrowrecord;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.ryd.BorrowDetailActivity;
import com.ryx.ryxcredit.ryd.BorrowRecordsActivity;
import com.ryx.ryxcredit.adapter.BorrowRecordsAdapter;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsResponse;
import com.ryx.ryxcredit.inter.IBorrowRecordCallBack;
import com.ryx.ryxcredit.inter.IFragmentListener;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DIY on 2016/6/30.
 * 已结清
 */
public class PayedFragment extends Fragment implements IBorrowRecordCallBack {
    private LinearLayout mNoDataList;
    private String status = "3";
    private IFragmentListener mListener;
    private TextView mTips;
    private XRecyclerView xRecyclerView;
    private List<BorrowRecordsResponse.ResultBean> payedList;
    private boolean isFirstReuest = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.c_fragment_borrow_records_payed, null, false);
        mNoDataList = (LinearLayout) rootview.findViewById(R.id.lay_addcard);
        xRecyclerView = (XRecyclerView) rootview.findViewById(R.id.c_rv_pay);
        xRecyclerView.setLoadingMoreEnabled(false);
        mTips = (TextView) rootview.findViewById(R.id.nocredittv);
        RecyclerViewHelper.init().setXRVLinearLayout(getActivity(), xRecyclerView);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (HttpUtil.checkNet(getActivity())) {
                    initData();
                } else {
                    mNoDataList.setVisibility(View.VISIBLE);
                    mTips.setText("暂无已结清记录");
                    CLogUtil.showToast(getActivity(), "请检查网络！");
                    xRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
            }
        });
        payedList = new ArrayList<BorrowRecordsResponse.ResultBean>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("payedList")) {
                payedList = (ArrayList<BorrowRecordsResponse.ResultBean>) bundle.getSerializable("payedList");
                if(isFirstReuest)
                recordCallBackSuccess(payedList);
            }
        }
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstReuest) {
            mListener.hideView("3");
            saveReadFlag();
            isFirstReuest = false;
        }else{
            initData();
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (BorrowRecordsActivity) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }

    // 保存用户已经访问过待还款页面
    private void saveReadFlag() {
        CPreferenceUtil.getInstance(getActivity().getApplicationContext()).saveBoolean("payedRecord_hasReaded", true);
    }

    private void initData() {
        mListener.requestBorrowRecord(status, "pay");
    }

    public static PayedFragment newInstance() {
        PayedFragment fragment = new PayedFragment();
        return fragment;
    }

    @Override
    public void recordCallBackSuccess(List<BorrowRecordsResponse.ResultBean> list) {
        xRecyclerView.refreshComplete();
        if (list == null || list.isEmpty()) {
            mNoDataList.setVisibility(View.VISIBLE);
            mTips.setText("暂无已结清记录");
            CLogUtil.showToast(getActivity(), "暂无记录");
            CLogUtil.showLog("zanwu","payed");
            return;
        }
        BorrowRecordsAdapter borrowRecordsAdapter = new BorrowRecordsAdapter(list, getActivity(),
                status, "", R.layout.c_view_borrow_records_un_pay_item);
        borrowRecordsAdapter.setOnItemClickListener(new OnListItemClickListener<BorrowRecordsResponse.ResultBean>() {
            @Override
            public void onItemClick(View view, int position, BorrowRecordsResponse.ResultBean data) {
                //放款失败的记录，不允许看详情
                if("G".equals(data.getLoan_status()) ){
                     return;
                }
                Intent intent = new Intent(getActivity(), BorrowDetailActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
        xRecyclerView.setAdapter(borrowRecordsAdapter);
    }

    @Override
    public void recordCallBackFailed(String tips) {
        xRecyclerView.refreshComplete();
        CLogUtil.showToast(getActivity(), tips);
    }
}
