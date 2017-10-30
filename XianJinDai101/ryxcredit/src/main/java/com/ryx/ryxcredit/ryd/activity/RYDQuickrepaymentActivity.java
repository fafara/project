package com.ryx.ryxcredit.ryd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.activity.QuickPaymentActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.HttpUtil;
import com.ryx.ryxcredit.xjd.XJDRepaymentAcitivity;
import com.ryx.ryxcredit.xjd.adapter.MultiBorrowRecordsAdapter;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordRequest;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordsResponse;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

/**
 * 快速还款
 */

public class RYDQuickrepaymentActivity extends BaseActivity {

    private String[] productStrs = {"", RyxcreditConfig.rkd_procudtid,RyxcreditConfig.xjd_procudtId};
    private int pos = 0;//选中的产品
    private XRecyclerView xRecyclerView;
    private List<MultiBorrowRecordsResponse.ResultBean> unPayList;
    private ImageView img_no_item;
    private TextView nocredittv;
    private AutoLinearLayout lay_no_item;

    private AutoRelativeLayout ll_over_tip;
    private  ImageView iv_close;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_xjd_quick_repayment_record);
        setTitleLayout("快速还款",true,false);
        xRecyclerView = (XRecyclerView) findViewById(R.id.c_rv_unpay);
        xRecyclerView.setLoadingMoreEnabled(false);
        RecyclerViewHelper.init().setXRVLinearLayout(this, xRecyclerView);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (HttpUtil.checkNet(RYDQuickrepaymentActivity.this)) {
                    requestBorrowRecord(productStrs[0]);
                } else {
                    CLogUtil.showToast(RYDQuickrepaymentActivity.this, "请检查网络！");
                    xRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {
                xRecyclerView.refreshComplete();
            }
        });
        img_no_item =(ImageView) findViewById(R.id.img_no_item);
        nocredittv =(TextView) findViewById(R.id.nocredittv);
        lay_no_item =(AutoLinearLayout) findViewById(R.id.lay_no_item);

        ll_over_tip  = (AutoRelativeLayout) findViewById(R.id.ll_over_tip);
        iv_close  = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                ll_over_tip.setVisibility(View.GONE);
            }
        });
        requestBorrowRecord(productStrs[0]);
    }

    public void requestBorrowRecord(final String product_id) {
        MultiBorrowRecordRequest request = new MultiBorrowRecordRequest();
        request.setLoan_status("2");
        request.setProduct_id("");
        httpsPost(this, request, ReqAction.LOAN_LIST, MultiBorrowRecordsResponse.class, new ICallback<MultiBorrowRecordsResponse>() {
            @Override
            public void success(MultiBorrowRecordsResponse borrowRecordsResponse) {
                   if(unPayList!=null)
                        unPayList.clear();
                    unPayList = borrowRecordsResponse.getResult();
                    recordCallBackSuccess(unPayList);

            }

            @Override
            public void failture(String tips) {
                recordCallBackFailed(tips);
            }
        });
    }

    public void recordCallBackSuccess(final List<MultiBorrowRecordsResponse.ResultBean> list) {
        xRecyclerView.refreshComplete();
        if (list == null || list.isEmpty()) {
            lay_no_item.setVisibility(View.VISIBLE);
            nocredittv.setText("暂无申请中记录");
            CLogUtil.showToast(this, "暂无记录");
            return;
        }
        for (MultiBorrowRecordsResponse.ResultBean bean : list) {
            if ("B".equals(bean.getLoan_status())) {
                //逾期
                ll_over_tip.setVisibility(View.VISIBLE);
                break;
            }
        }
        lay_no_item.setVisibility(View.GONE);
        MultiBorrowRecordsAdapter borrowRecordsAdapter = new MultiBorrowRecordsAdapter(list, this,
                "2", "", R.layout.c_view_borrow_records_un_pay_item);
        borrowRecordsAdapter.setOnItemClickListener(new OnListItemClickListener<MultiBorrowRecordsResponse.ResultBean>() {
            @Override
            public void onItemClick(View view, int position, MultiBorrowRecordsResponse.ResultBean data) {
                Intent intent = null;
                String loanStatus = list.get(position).getLoan_status();
                if ("A".equals(loanStatus)){
                    CLogUtil.showToast(RYDQuickrepaymentActivity.this,"已结清记录，不可还款！");
                    return;
                }
                if ("F".equals(loanStatus) || "H".equals(loanStatus)){
                    CLogUtil.showToast(RYDQuickrepaymentActivity.this,"放款中，暂不可还款！");
                    return;
                }
                if ("U".equals(loanStatus) || "G".equals(loanStatus)){
                    CLogUtil.showToast(RYDQuickrepaymentActivity.this,"处理中！");
                    return;
                }
                if ("R".equals(loanStatus) ){
                    CLogUtil.showToast(RYDQuickrepaymentActivity.this,"审核未通过！");
                    return;
                }
                if (RyxcreditConfig.rkd_procudtid.equals(list.get(position).getProduct_id())) {
                    //判断是现金贷还是瑞卡贷
                    intent = new Intent(RYDQuickrepaymentActivity.this, QuickPaymentActivity.class);
                    intent.putExtra("flag","Repayment");
                } else {
                    intent = new Intent(RYDQuickrepaymentActivity.this, XJDRepaymentAcitivity.class);
                }
                intent.putExtra("contract_id",data.getContract_id()+"");
                startActivity(intent);
            }
        });
        xRecyclerView.setAdapter(borrowRecordsAdapter);
    }

    public void recordCallBackFailed(String tips) {
        xRecyclerView.refreshComplete();
        CLogUtil.showToast(this, tips);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
