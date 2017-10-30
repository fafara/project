package com.ryx.ryxcredit.ryd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.quickadapter.recyclerView.animation.ScaleInAnimation;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailRequest;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.xjd.adapter.DetailRepayPlanAdapter;
import com.ryx.ryxcredit.xjd.adapter.RepaymentPlanListAdapter;
import com.ryx.ryxcredit.xjd.bean.borrow.CXJDProductCaculateResponse;
import com.ryx.ryxcredit.xjd.bean.repayment.CXJDRepayPlanResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 瑞易贷还款计划
 */

public class RYDRepayPlanActivity extends BaseActivity {

    private List<CXJDProductCaculateResponse.ResultBean.RepaymentsBean> caculateRepaymentList;

    private RecyclerView lv_repay_plan;
    private RepaymentPlanListAdapter repaymentPlanListAdapter;
    private boolean isFromDetail;//是否是在借还详情页面返回来的
    private String contract_id;//借款合同编号
    private List<CXJDRepayPlanResponse.ResultBean.RepaymentBean> planList;
    private CXJDRepayPlanResponse.ResultBean planResult;
    private TextView tv_repay_amount;
    private TextView tv_repay_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_ryd_activity_repay_plan);
        lv_repay_plan = (RecyclerView)findViewById(R.id.lv_repay_plan);
        tv_repay_amount = (TextView)findViewById(R.id.tv_repay_amount);
        tv_repay_date =(TextView)findViewById(R.id.tv_repay_date);
        setTitleLayout("还款计划",true,false);
        if(getIntent()!=null){
            if(getIntent().hasExtra("caculateRepaymentList")) {
                caculateRepaymentList = (ArrayList<CXJDProductCaculateResponse.ResultBean.RepaymentsBean>) getIntent().getSerializableExtra("caculateRepaymentList");
                if(caculateRepaymentList!=null&&caculateRepaymentList.size()>0)
                {
                    String endDate = caculateRepaymentList.get(0).getExpired_date();
                    CLogUtil.showLog("endDate----",endDate+"----");
                    if(!TextUtils.isEmpty(endDate)&&endDate.length()>=8)
                        tv_repay_date.setText("还款日为每月"+endDate.substring(6)+"号");
                }

            }
            if(getIntent().hasExtra("contract_id"))
                contract_id = getIntent().getStringExtra("contract_id");
            if(getIntent().hasExtra("total_amount"))
                tv_repay_amount.setText(String.valueOf(getIntent().getDoubleExtra("total_amount",0)));
            //借款页面的还款计划
            if(TextUtils.isEmpty(contract_id)){
                initReapymentPlanData();
            }
            //借还详情页面的还款计划
            else {
                if(getIntent().hasExtra("end_date")){
                String endDate = getIntent().getStringExtra("end_date");
                if(!TextUtils.isEmpty(endDate)&&endDate.length()>=8)
                    tv_repay_date.setText("还款日为每月"+endDate.substring(6)+"号");
                }
                getPlanData();
            }

        }
    }

    //借还详情页面，还款计划
    private void getPlanData(){
        BorrowRecordDetailRequest request = new BorrowRecordDetailRequest();
        request.setContract_id(contract_id);
        httpsPost(this, request, ReqAction.LOAN_REPAYMENTSPLAN, CXJDRepayPlanResponse.class, new ICallback<CXJDRepayPlanResponse>() {
            @Override
            public void success(CXJDRepayPlanResponse borrowRecordDetailResponse) {
                planResult = borrowRecordDetailResponse.getResult();
                planList = borrowRecordDetailResponse.getResult().getRepayment();
                int borrowRecordDetailCode = borrowRecordDetailResponse.getCode();
                if (borrowRecordDetailCode==5031) {
                    showMaintainDialog();
                } else {
                    initDetailPlanData();
                }
            }

            @Override
            public void failture(String tips) {

            }
        });
    }

    private void initDetailPlanData(){
        tv_repay_amount.setText(String.valueOf(planResult.getTotal_amount()));
        tv_repay_date.setText("还款日为每月"+planResult.getTerm_date()+"号");
        RecyclerViewHelper.init().setRVGridLayout(this, lv_repay_plan, 1);//1列
        DetailRepayPlanAdapter  drepaymentPlanListAdapter = new DetailRepayPlanAdapter(planList,this, R.layout.c_repayment_plan_item);
        drepaymentPlanListAdapter.openLoadAnimation(new ScaleInAnimation());
        drepaymentPlanListAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {

            }
        });
        lv_repay_plan.setAdapter(drepaymentPlanListAdapter);
    }

    //借还详情页面，还款计划
    private void initReapymentPlanData() {
        RecyclerViewHelper.init().setRVGridLayout(this, lv_repay_plan, 1);//1列
        repaymentPlanListAdapter = new RepaymentPlanListAdapter(caculateRepaymentList,this, R.layout.c_repayment_plan_item);
        repaymentPlanListAdapter.openLoadAnimation(new ScaleInAnimation());
        repaymentPlanListAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {

            }
        });
        lv_repay_plan.setAdapter(repaymentPlanListAdapter);
    }
}
