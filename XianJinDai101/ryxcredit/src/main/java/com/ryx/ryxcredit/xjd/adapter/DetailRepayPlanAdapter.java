package com.ryx.ryxcredit.xjd.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.xjd.bean.repayment.CXJDRepayPlanResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */

public class DetailRepayPlanAdapter extends HelperAdapter<CXJDRepayPlanResponse.ResultBean.RepaymentBean> {
    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;

    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public DetailRepayPlanAdapter(List<CXJDRepayPlanResponse.ResultBean.RepaymentBean> data, Context context, int... layoutId) {
        super(data, context, layoutId);
        this.context = context;
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, final int position, final CXJDRepayPlanResponse.ResultBean.RepaymentBean item) {

        if (null != item) {
            view = viewHolder.getItemView();
            TextView tv_items = (TextView) view.findViewById(R.id.tv_items);
            TextView tv_repay_amount = (TextView) view.findViewById(R.id.tv_repay_amount);
            TextView tv_base_amount = (TextView) view.findViewById(R.id.tv_base_amount);
            TextView tv_interest_amount = (TextView) view.findViewById(R.id.tv_interest_amount);
            TextView tv_service_fee = (TextView) view.findViewById(R.id.tv_service_fee);
            //判断是否已还
            if ("1".equals(item.getLoan_status())) {
                tv_items.setText(String.valueOf(item.getTerm()) + "/已还");
                tv_items.setTextColor(context.getResources().getColor(R.color.threeblack));
                tv_repay_amount.setTextColor(context.getResources().getColor(R.color.threeblack));
                tv_interest_amount.setTextColor(context.getResources().getColor(R.color.threeblack));
                tv_service_fee.setTextColor(context.getResources().getColor(R.color.threeblack));
                tv_base_amount.setTextColor(context.getResources().getColor(R.color.threeblack));
            } else if ("2".equals(item.getLoan_status())) {
                tv_items.setText(String.valueOf(item.getTerm()) + "/未还");
            }
            tv_repay_amount.setText(Double.toString(item.getTotal_amount()));
            tv_interest_amount.setText(Double.toString(item.getInterest_amount()));
            tv_service_fee.setText(Double.toString(item.getOther_cost_amount()));
            tv_base_amount.setText(Double.toString(item.getCost_amount()));
            tv_items.setText(item.getTerm()+"");

        }
    }
}
