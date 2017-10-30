package com.ryx.ryxcredit.xjd.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.xjd.bean.borrow.CXJDProductCaculateResponse;

import java.util.List;

/**
 * 借款页面还款计划
 */

public class RepaymentPlanListAdapter extends HelperAdapter<CXJDProductCaculateResponse.ResultBean.RepaymentsBean> {

    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;

    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public RepaymentPlanListAdapter(List<CXJDProductCaculateResponse.ResultBean.RepaymentsBean> data, Context context, int... layoutId) {
        super(data, context, layoutId);
        this.context = context;
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, final int position, final CXJDProductCaculateResponse.ResultBean.RepaymentsBean item) {

        if (null != item) {
            view = viewHolder.getItemView();
            TextView tv_items = (TextView) view.findViewById(R.id.tv_items);
            TextView tv_repay_amount = (TextView) view.findViewById(R.id.tv_repay_amount);
            TextView tv_base_amount = (TextView) view.findViewById(R.id.tv_base_amount);
            TextView tv_interest_amount = (TextView) view.findViewById(R.id.tv_interest_amount);
            TextView tv_service_fee = (TextView) view.findViewById(R.id.tv_service_fee);
            //判断是否已还
            tv_items.setText(String.valueOf(item.getTerm()) );
            tv_repay_amount.setText(Double.toString(item.getTotal_amount()));
            tv_interest_amount.setText(Double.toString(item.getInterest_amount()));
            tv_service_fee.setText(Double.toString(item.getLoan_term_svc_fee()));
            tv_base_amount.setText(Double.toString(item.getTerm_amount()));

        }
    }
}
