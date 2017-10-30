package com.ryx.ryxcredit.ryd;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.adapter.RePayRecordsAdapter;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 还款记录
 *
 * @author muxin
 * @time 2016-09-27 14:13
 */
public class RepayRecordActivity extends BaseActivity {

    private LinearLayout mRepaymentRecordLl;
    private List<BorrowRecordDetailResponse.ResultBean.RepaymentsBean> list = new ArrayList<>();
    private XRecyclerView xRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_repay_record);
        setTitleLayout("还款记录", true, false);
        BorrowRecordDetailResponse.ResultBean data = getIntent().getParcelableExtra("repayment_data");
        xRecyclerView = (XRecyclerView) findViewById(R.id.lv);
        xRecyclerView.setPullRefreshEnabled(false);//不启用下拉刷新
        xRecyclerView.setLoadingMoreEnabled(false);//不启用加载更多
        RecyclerViewHelper.init().setXRVLinearLayout(this, xRecyclerView);
        mRepaymentRecordLl = (LinearLayout) findViewById(R.id.ll_repayment_record_null);
        if (null != data) {
            List<BorrowRecordDetailResponse.ResultBean.RepaymentsBean> beanList = data.getRepayments();
            if (beanList==null||beanList.isEmpty()) {
                mRepaymentRecordLl.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < beanList.size(); i++) {
                    if ("S".equals(beanList.get(i).getLoan_status())) {
                        list.add(beanList.get(i));
                    }
                }
                if (list.isEmpty()) {
                    mRepaymentRecordLl.setVisibility(View.VISIBLE);
                } else {
                    RePayRecordsAdapter adapter = new RePayRecordsAdapter
                            (list, this, R.layout.c_listview_repay_record);
                    xRecyclerView.setAdapter(adapter);
                }
            }
        }
    }

}
