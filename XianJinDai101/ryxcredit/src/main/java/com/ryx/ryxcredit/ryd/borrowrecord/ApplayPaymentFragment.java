package com.ryx.ryxcredit.ryd.borrowrecord;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.ryd.BorrowRecordsActivity;
import com.ryx.ryxcredit.adapter.BorrowRecordsAdapter;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsResponse;
import com.ryx.ryxcredit.inter.IBorrowRecordCallBack;
import com.ryx.ryxcredit.inter.IFragmentListener;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请中
 */
public class ApplayPaymentFragment extends Fragment implements IBorrowRecordCallBack {
    private TextView mTips;
    private LinearLayout mNoDataList;
    private String status = "1";
    private IFragmentListener mListener;
    private XRecyclerView xRecyclerView;
    private TabLayout tl_borrow_records;
    private boolean isFirstRequest;
    private List<BorrowRecordsResponse.ResultBean> applyedList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.c_fragment_borrow_records_unpay, null, false);
        mTips = (TextView) rootview.findViewById(R.id.nocredittv);
        mNoDataList = (LinearLayout) rootview.findViewById(R.id.lay_addcard);
        xRecyclerView = (XRecyclerView) rootview.findViewById(R.id.c_rv_unpay);
        xRecyclerView.setLoadingMoreEnabled(false);
        applyedList = new ArrayList<BorrowRecordsResponse.ResultBean>();
        RecyclerViewHelper.init().setXRVLinearLayout(getActivity(), xRecyclerView);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (HttpUtil.checkNet(getActivity())) {
                    initData();
                } else {
                    mNoDataList.setVisibility(View.VISIBLE);
                    mTips.setText("暂无申请中记录");
                    CLogUtil.showToast(getActivity(), "请检查网络！");
                    xRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
        tl_borrow_records = (TabLayout) rootview.findViewById(R.id.c_tl_borrow_records);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if(bundle.containsKey("is_FirstRequest"))
            isFirstRequest = bundle.getBoolean("is_FirstRequest");
            //如果不是首次打开借还记录页面，获取到历史的申请中记录
            if(bundle.containsKey("applyedList")) {
                applyedList = (List<BorrowRecordsResponse.ResultBean>) bundle.getSerializable("applyedList");
                recordCallBackSuccess(applyedList);
            }
        }
        return rootview;
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

    @Override
    public void onResume() {
        super.onResume();
        //首次进入“申请中”，请求所有类型的记录
        if (isFirstRequest) {
            mListener.getData("1");
        }else{
            initData();
        }
    }

    private void initData() {
        mListener.requestBorrowRecord(status, "apply");
    }

    public static ApplayPaymentFragment newInstance() {
        ApplayPaymentFragment fragment = new ApplayPaymentFragment();
        return fragment;
    }

    @Override
    public void recordCallBackSuccess(List<BorrowRecordsResponse.ResultBean> list) {
        xRecyclerView.refreshComplete();
        if (list == null || list.isEmpty()) {
            mNoDataList.setVisibility(View.VISIBLE);
            mTips.setText("暂无申请中记录");
            CLogUtil.showToast(getActivity(), "暂无记录");
            CLogUtil.showLog("zanwu","apply");
            return;
        }
        BorrowRecordsAdapter borrowRecordsAdapter = new BorrowRecordsAdapter(list, getActivity(),
                status, "", R.layout.c_view_borrow_records_un_pay_item);
        xRecyclerView.setAdapter(borrowRecordsAdapter);
    }

    @Override
    public void recordCallBackFailed(String tips) {
        xRecyclerView.refreshComplete();
        CLogUtil.showToast(getActivity(), tips);
    }
}
