package com.ryx.ryxcredit.ryd.borrowrecord;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.ryd.BorrowDetailActivity;
import com.ryx.ryxcredit.ryd.BorrowRecordsActivity;
import com.ryx.ryxcredit.activity.QuickPaymentActivity;
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
 * 待还款
 *
 * @author muxin
 * @time 2016-09-19 11:06
 */
public class UnPaymentFragment extends Fragment implements IBorrowRecordCallBack {
    private RelativeLayout mOverTip;
    private LinearLayout mNoDataList;
    private TextView mTips;
    private String status = "2";
    private String flag = "";
    private ImageView mCloseIv;
    private IFragmentListener mListener;
    private XRecyclerView xRecyclerView;
    private boolean is_opened;
    private String unborrowTime;
    private List<BorrowRecordsResponse.ResultBean> unPayedList;
    private boolean isFirstRequest=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.c_fragment_borrow_records_unpay, null, false);
        mTips = (TextView) rootview.findViewById(R.id.nocredittv);
        mNoDataList = (LinearLayout) rootview.findViewById(R.id.lay_addcard);
        mOverTip = (RelativeLayout) rootview.findViewById(R.id.ll_over_tip);
        xRecyclerView = (XRecyclerView) rootview.findViewById(R.id.c_rv_unpay);
        xRecyclerView.setLoadingMoreEnabled(false);
        unPayedList = new ArrayList<BorrowRecordsResponse.ResultBean>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            flag = bundle.getString("flag");
            if (bundle.containsKey("is_opened")) {
                is_opened = bundle.getBoolean("is_opened");
            }
            if (bundle.containsKey("unborrowTime"))
                unborrowTime = bundle.getString("unborrowTime");
            if (bundle.containsKey("unpayedList")) {
                unPayedList = (ArrayList<BorrowRecordsResponse.ResultBean>) bundle.getSerializable("unpayedList");
                if(isFirstRequest)//第一次请求，则根据传递过来的结果
                recordCallBackSuccess(unPayedList);
            }
        }
        RecyclerViewHelper.init().setXRVLinearLayout(getActivity(), xRecyclerView);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (HttpUtil.checkNet(getActivity())) {
                    initData();
                } else {
                    mNoDataList.setVisibility(View.VISIBLE);
                    mTips.setText("暂无待还款记录");
                    CLogUtil.showToast(getActivity(), "请检查网络！");
                    xRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
            }
        });
        mCloseIv = (ImageView) rootview.findViewById(R.id.iv_close);
        mCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOverTip.setVisibility(View.GONE);
            }
        });
        return rootview;
    }

    // 保存用户已经访问过待还款页面
    private void saveReadFlag(){
        CPreferenceUtil.getInstance(getActivity().getApplicationContext()).saveBoolean("unPayment_hasReaded",true);
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

    //请求未还款内容
    private void initData() {
        mListener.requestBorrowRecord(status, "unpay");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isFirstRequest){
            mListener.hideView("2");
            saveReadFlag();
            isFirstRequest = false;
        }else{
            initData();
        }
    }

    public static UnPaymentFragment newInstance() {
        UnPaymentFragment fragment = new UnPaymentFragment();
        return fragment;
    }

    @Override
    public void recordCallBackSuccess(List<BorrowRecordsResponse.ResultBean> list) {
        xRecyclerView.refreshComplete();
        if (list == null || list.isEmpty()) {
            mNoDataList.setVisibility(View.VISIBLE);
            mTips.setText("暂无待还款记录");
            CLogUtil.showToast(getActivity(), "暂无记录");
            CLogUtil.showLog("zanwu","unpay");
            return;
        }
        for (BorrowRecordsResponse.ResultBean bean : list) {
            if ("B".equals(bean.getLoan_status())) {
                //逾期
                mOverTip.setVisibility(View.VISIBLE);
                break;
            }
        }

        BorrowRecordsAdapter borrowRecordsAdapter = new BorrowRecordsAdapter(list, getActivity(), status, "", R.layout.c_view_borrow_records_un_pay_item);
        borrowRecordsAdapter.setOnItemClickListener(new OnListItemClickListener<BorrowRecordsResponse.ResultBean>() {
            @Override
            public void onItemClick(View view, int position, BorrowRecordsResponse.ResultBean data) {
                Intent intent = null;
                if ("Repayment".equals(flag)) {
                    intent = new Intent(getActivity(), QuickPaymentActivity.class);
                } else {
                    intent = new Intent(getActivity(), BorrowDetailActivity.class);
                }
                intent.putExtra("flag", flag);
                intent.putExtra("data", data);
                intent.putExtra("is_opened", is_opened);
                intent.putExtra("unborrowTime", unborrowTime);
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
