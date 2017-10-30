package com.ryx.ryxcredit.xjd;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordDetailReponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 还款记录
 *
 * @author muxin
 * @time 2016-09-27 14:13
 */
public class MultiRYDRepayRecordActivity extends BaseActivity {

    private LinearLayout mRepaymentRecordLl;
    private String contract_id;//借款合同编号
    private List<MultiBorrowRecordDetailReponse.ResultBean.RepaymentsBean> list = new ArrayList<>();
    private XRecyclerView xRecyclerView;
    private List<MultiBorrowRecordDetailReponse.ResultBean> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_repay_record);
        setTitleLayout("还款记录", true, false);
        MultiBorrowRecordDetailReponse.ResultBean data = (MultiBorrowRecordDetailReponse.ResultBean)getIntent().getSerializableExtra("repayment_data");
        CLogUtil.showLog("data---",data.toString()+"-----");
        xRecyclerView = (XRecyclerView) findViewById(R.id.lv);
        xRecyclerView.setPullRefreshEnabled(false);//不启用下拉刷新
        xRecyclerView.setLoadingMoreEnabled(false);//不启用加载更多
        RecyclerViewHelper.init().setXRVLinearLayout(this, xRecyclerView);
        mRepaymentRecordLl = (LinearLayout) findViewById(R.id.ll_repayment_record_null);
        if (null != data) {
            List<MultiBorrowRecordDetailReponse.ResultBean.RepaymentsBean> beanList = data.getRepayments();
            if (beanList==null||beanList.isEmpty()) {
                mRepaymentRecordLl.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < beanList.size(); i++) {
                    if ("S".equals(beanList.get(i).getLoan_status())) {
                        list.add(beanList.get(i));
                    }
                }
                CLogUtil.showLog("list----",list.size()+"----"+list.toString());
                if (list.isEmpty()) {
                    mRepaymentRecordLl.setVisibility(View.VISIBLE);
                } else {
                    MultiRePayRecordsAdapter adapter = new MultiRePayRecordsAdapter
                            (list, this, R.layout.c_listview_repay_record);
                    xRecyclerView.setAdapter(adapter);
                }
            }
        }
    }

}
