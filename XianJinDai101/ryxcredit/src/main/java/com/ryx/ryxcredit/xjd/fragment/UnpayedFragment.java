package com.ryx.ryxcredit.xjd.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.ryd.activity.RYDMultiBorrowDetailActivity;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.HttpUtil;
import com.ryx.ryxcredit.xjd.IMultiBorrowRecordCallBack;
import com.ryx.ryxcredit.xjd.MultiBorrowDetailActivity;
import com.ryx.ryxcredit.xjd.MultiBorrowRecordsActivity;
import com.ryx.ryxcredit.xjd.MultiIFragmentListener;
import com.ryx.ryxcredit.xjd.adapter.MultiBorrowRecordsAdapter;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordsResponse;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

/**
 * 待还款
 */
public class UnpayedFragment extends Fragment implements IMultiBorrowRecordCallBack {
    private TextView mTips;
    private LinearLayout mNoDataList;
    private String status = "2";
    private MultiIFragmentListener mListener;
    private XRecyclerView xRecyclerView;
    private TabLayout tl_borrow_records;
    private boolean isFirstRequest;
//    private List<MultiBorrowRecordsResponse.ResultBean> unpayedList;
    private String flag = "";
    private MultiBorrowRecordsAdapter borrowRecordsAdapter;
    private AutoRelativeLayout ll_over_tip;
    private  ImageView iv_close;
    private boolean is_opened;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.c_fragment_borrow_records_unpay, null, false);
        mTips = (TextView) rootview.findViewById(R.id.nocredittv);
        ll_over_tip  = (AutoRelativeLayout) rootview.findViewById(R.id.ll_over_tip);
        iv_close  = (ImageView) rootview.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                ll_over_tip.setVisibility(View.GONE);
            }
        });
        mNoDataList = (LinearLayout) rootview.findViewById(R.id.lay_no_item);
        xRecyclerView = (XRecyclerView) rootview.findViewById(R.id.c_rv_unpay);
        RecyclerViewHelper.init().setXRVLinearLayout(getActivity(), xRecyclerView);
        xRecyclerView.setLoadingMoreEnabled(false);
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
        tl_borrow_records = (TabLayout) rootview.findViewById(R.id.c_tl_borrow_records);
        Bundle bundle = getArguments();
        if (bundle != null) {
            flag = bundle.getString("flag");
             is_opened = bundle.getBoolean("is_opened");
// Bundle bundle = getArguments();
//        if (bundle != null) {
//            if(bundle.containsKey("is_FirstRequest"))
//            isFirstRequest = bundle.getBoolean("is_FirstRequest");
//            //如果不是首次打开借还记录页面，获取到历史的申请中记录
//            if(bundle.containsKey("applyedList")) {
//                applyedList = (List<BorrowRecordsResponse.ResultBean>) bundle.getSerializable("applyedList");
//                recordCallBackSuccess(applyedList);
//            }
        }
        return rootview;
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

    @Override
    public void onResume() {
        super.onResume();
        //首次进入“申请中”，请求所有类型的记录
//        if (isFirstRequest) {
//            mListener.getData("1");
//        }else{
            initData();
//        }
    }

    private void initData() {
        mListener.requestBorrowRecord(status, "unpayed","");
    }

    public static UnpayedFragment newInstance() {
        UnpayedFragment fragment = new UnpayedFragment();
        return fragment;
    }

    @Override
    public void recordCallBackSuccess(final List<MultiBorrowRecordsResponse.ResultBean> list) {
        if (list == null || list.isEmpty()) {
            mNoDataList.setVisibility(View.VISIBLE);
            mTips.setText("暂无申请中记录");
            CLogUtil.showToast(getActivity(), "暂无记录");
            return;
        }
        for (MultiBorrowRecordsResponse.ResultBean bean : list) {
            if ("B".equals(bean.getLoan_status())) {
                //逾期
                ll_over_tip.setVisibility(View.VISIBLE);
                break;
            }
        }
        mNoDataList.setVisibility(View.GONE);
        xRecyclerView.reset();
        borrowRecordsAdapter = new MultiBorrowRecordsAdapter(list, getActivity(),
                status, "", R.layout.c_view_borrow_records_un_pay_item);
        xRecyclerView.setAdapter(borrowRecordsAdapter);
        borrowRecordsAdapter.setOnItemClickListener(new OnListItemClickListener<MultiBorrowRecordsResponse.ResultBean>() {
            @Override
            public void onItemClick(View view, int position, MultiBorrowRecordsResponse.ResultBean data) {
                Intent intent = null;
                if (RyxcreditConfig.rkd_procudtid.equals(list.get(position).getProduct_id())) {
                    //判断是现金贷还是瑞卡贷
                       String statue = list.get(position).getLoan_status();
                        if ("U".equals(statue)||"R".equals(statue)
                                ||"G".equals(statue)) {
                            CLogUtil.showToast(getActivity(), "审核未通过!");
                        }else{
                            intent = new Intent(getActivity(), RYDMultiBorrowDetailActivity.class);
                            intent.putExtra("flag", flag);
                            intent.putExtra("is_opened", is_opened);
                            intent.putExtra("product_descr",list.get(position).getProduct_descr());
                            intent.putExtra("contract_id",list.get(position).getContract_id());
                            startActivity(intent);
                        }

                } else {
                    //判断是现金贷还是瑞卡贷
                         String statue = list.get(position).getLoan_status();
                        if ("U".equals(statue)||"R".equals(statue)
                                ||"G".equals(statue)) {
                          //  CLogUtil.showToast(getActivity(), "审核未通过");
                        }else{
                            intent = new Intent(getActivity(), MultiBorrowDetailActivity.class);
                            intent.putExtra("flag", flag);
                            intent.putExtra("is_opened", is_opened);
                            intent.putExtra("product_descr",list.get(position).getProduct_descr());
                            intent.putExtra("contract_id",list.get(position).getContract_id());
                            startActivity(intent);
                        }


                }

            }
        });
        borrowRecordsAdapter.notifyDataSetChanged();
    }

    @Override
    public void recordCallBackFailed(String tips) {
        xRecyclerView.refreshComplete();
        CLogUtil.showToast(getActivity(), tips);
    }
}
