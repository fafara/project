package com.ryx.ryxcredit.xjd.fragment;

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
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.ryd.activity.RYDMultiBorrowDetailActivity;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.utils.HttpUtil;
import com.ryx.ryxcredit.xjd.IMultiBorrowRecordCallBack;
import com.ryx.ryxcredit.xjd.MultiBorrowDetailActivity;
import com.ryx.ryxcredit.xjd.MultiBorrowRecordsActivity;
import com.ryx.ryxcredit.xjd.MultiIFragmentListener;
import com.ryx.ryxcredit.xjd.adapter.MultiBorrowRecordsAdapter;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 已结清fragment
 *
 * @author muxin
 * @time 2016-09-19 11:06
 */
public class PayedFragment extends Fragment implements IMultiBorrowRecordCallBack {
    private RelativeLayout mOverTip;
    private LinearLayout mNoDataList;
    private TextView mTips;
    private String status = "3";
    private String flag = "";
    private ImageView mCloseIv;
    private MultiIFragmentListener mListener;
    private XRecyclerView xRecyclerView;
    private boolean is_opened;
    private String unborrowTime;
    private List<MultiBorrowRecordsResponse.ResultBean> unPayedList;
    private boolean isFirstRequest=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.c_fragment_borrow_records_unpay, null, false);
        mTips = (TextView) rootview.findViewById(R.id.nocredittv);
        mNoDataList = (LinearLayout) rootview.findViewById(R.id.lay_no_item);
        mOverTip = (RelativeLayout) rootview.findViewById(R.id.ll_over_tip);
        xRecyclerView = (XRecyclerView) rootview.findViewById(R.id.c_rv_unpay);
        xRecyclerView.setLoadingMoreEnabled(false);
        unPayedList = new ArrayList<MultiBorrowRecordsResponse.ResultBean>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            flag = bundle.getString("flag");
            if (bundle.containsKey("is_opened")) {
                is_opened = bundle.getBoolean("is_opened");
            }
            if (bundle.containsKey("unborrowTime"))
                unborrowTime = bundle.getString("unborrowTime");
            if (bundle.containsKey("payedList")) {
                unPayedList = (ArrayList<MultiBorrowRecordsResponse.ResultBean>) bundle.getSerializable("payedList");
                if(isFirstRequest)//第一次请求，则根据传递过来的结果
                recordCallBackSuccess(unPayedList);
            }
        }
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (HttpUtil.checkNet(getActivity())) {
                    initData();
                } else {
                    CLogUtil.showToast(getActivity(), "请检查网络！");
                    xRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
                xRecyclerView.refreshComplete();
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
            mListener = (MultiBorrowRecordsActivity) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);


    }

    //请求未还款内容
    private void initData() {
        mListener.requestBorrowRecord(status, "payed","");
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(isFirstRequest){
//
// mListener.hideView("2");
//            saveReadFlag();
//            isFirstRequest = false;
//        }else{
            initData();
//        }
    }

    public static PayedFragment newInstance() {
        PayedFragment fragment = new PayedFragment();
        return fragment;
    }

    @Override
    public void recordCallBackSuccess(final List<MultiBorrowRecordsResponse.ResultBean> list) {
        RecyclerViewHelper.init().setXRVLinearLayout(getActivity(), xRecyclerView);
        xRecyclerView.refreshComplete();
        if (list == null || list.isEmpty()) {
            mNoDataList.setVisibility(View.VISIBLE);
            mTips.setText("暂无待还款记录");
            CLogUtil.showToast(getActivity(), "暂无记录");
            return;
        }
        for (MultiBorrowRecordsResponse.ResultBean bean : list) {
            if ("B".equals(bean.getLoan_status())) {
                //逾期
                mOverTip.setVisibility(View.VISIBLE);
                break;
            }
        }
        mNoDataList.setVisibility(View.GONE);
        xRecyclerView.reset();
        MultiBorrowRecordsAdapter borrowRecordsAdapter = new MultiBorrowRecordsAdapter(list, getActivity(), status, "", R.layout.c_view_borrow_records_un_pay_item);
        borrowRecordsAdapter.setOnItemClickListener(new OnListItemClickListener<MultiBorrowRecordsResponse.ResultBean>() {
            @Override
            public void onItemClick(View view, int position, MultiBorrowRecordsResponse.ResultBean data) {
                Intent intent = null;
                if (RyxcreditConfig.rkd_procudtid.equals(list.get(position).getProduct_id())) {
                    String statue = list.get(position).getLoan_status();
                    if ("U".equals(statue)||"R".equals(statue)
                            ||"G".equals(statue)) {
                        CLogUtil.showToast(getActivity(), "审核未通过!");
                    }else {
                        intent = new Intent(getActivity(), RYDMultiBorrowDetailActivity.class);
                        intent.putExtra("flag", flag);
                        intent.putExtra("data", data);
                        intent.putExtra("contract_id",data.getContract_id()+"");
                        intent.putExtra("is_opened", is_opened);
                        intent.putExtra("unborrowTime", unborrowTime);
                        intent.putExtra("isPayed",true);
                        startActivity(intent);
                    }
                } else {
                    String statue = list.get(position).getLoan_status();
                    if ("U".equals(statue)||"R".equals(statue)
                            ||"G".equals(statue)) {
                      //  CLogUtil.showToast(getActivity(), "审核未通过!");
                    }else {
                        intent = new Intent(getActivity(), MultiBorrowDetailActivity.class);
                        intent.putExtra("flag", flag);
                        intent.putExtra("data", data);
                        intent.putExtra("contract_id", data.getContract_id() + "");
                        intent.putExtra("is_opened", is_opened);
                        intent.putExtra("unborrowTime", unborrowTime);
                        intent.putExtra("isPayed", true);
                        startActivity(intent);
                    }
                }

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
