package com.ryx.ryxcredit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsResponse;
import com.ryx.ryxcredit.utils.CConstants;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CMoneyEncoder;

import java.util.List;

/**
 * 借款记录适配器
 * @author muxin
 * @time 2016-10-17 16:04
 */
public class BorrowRecordsAdapter extends HelperAdapter<BorrowRecordsResponse.ResultBean> {

    private OnListItemClickListener mOnItemClickListener = null;
    private Context mContext;
    private String mStatus;
    private String tag;

    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public BorrowRecordsAdapter(List<BorrowRecordsResponse.ResultBean> data, Context context, String status,String tag, int... layoutId) {
        super(data, context, layoutId);
        this.mContext = context;
        this.mStatus = status;
        this.tag = tag;
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(final HelperViewHolder holder, final int position,
                                  final BorrowRecordsResponse.ResultBean resultBean) {

        if (null != resultBean) {
            //还款日期
            holder.setText(R.id.tv_repayment_date, CDateUtil.DateToShortStr(CDateUtil.parseDate(resultBean.getExpired_date(), "yyyyMMdd"),"yyyy-MM-dd"));
            //当前状态
            holder.setText(R.id.tv_borrow_status, CConstants.getLoanStatus(resultBean.getLoan_status()));
            if ("2".equals(mStatus)) {//待还款
                //产品名称
                holder.setText(R.id.c_tv_loan_name, "待还金额");
                //待还金额
                holder.setText(R.id.tv_borrow_amount, CMoneyEncoder.EncodeFormat(resultBean.getOwed_amount()));
                //还款日期文字显示
                holder.setText(R.id.tv_pay_txt, "到期还款日");
                //已逾期
                if ("B".equals(resultBean.getLoan_status())) {
                    holder.setTextColor(R.id.tv_borrow_amount, ContextCompat.getColor(holder.itemView.getContext(), R.color.red_second));
                    holder.setTextColor(R.id.tv_repayment_date, ContextCompat.getColor(holder.itemView.getContext(), R.color.red_second));
                    holder.setTextColor(R.id.tv_borrow_status, ContextCompat.getColor(holder.itemView.getContext(), R.color.red_second));
                    holder.setTextColor(R.id.c_tv_loan_name, ContextCompat.getColor(holder.itemView.getContext(), R.color.red_second));
                    holder.setTextColor(R.id.tv_pay_txt, ContextCompat.getColor(holder.itemView.getContext(), R.color.red_second));
                }
            }
            if ("3".equals(mStatus)) {//已结清
                //产品名称
                holder.setText(R.id.c_tv_loan_name, "合同金额");
                //合同金额
                holder.setText(R.id.tv_borrow_amount, CMoneyEncoder.EncodeFormat(resultBean.getTotal_amount()));
                //还款日期文字显示
                holder.setText(R.id.tv_pay_txt, "到期还款日");
                //状态灰色
                holder.setTextColor(R.id.tv_borrow_status, ContextCompat.getColor(holder.itemView.getContext(), R.color.threeblack));
                //当前状态
                holder.setText(R.id.tv_borrow_status, CConstants.getPayedstatusStatus(resultBean.getLoan_status()));
            }
            if ("1".equals(mStatus)) {//申请中
                //产品名称
                holder.setText(R.id.c_tv_loan_name, "金额");
                //合同金额
                holder.setText(R.id.tv_borrow_amount, CMoneyEncoder.EncodeFormat(resultBean.getOwed_amount()));
                //文字显示
                holder.setText(R.id.tv_pay_txt, "申请日");
                //申请日期
                holder.setText(R.id.tv_repayment_date, CDateUtil.DateToShortStr(CDateUtil.parseDate(resultBean.getApplication_datetime(), "yyyyMMddHHmmss"),"yyyy-MM-dd HH:mm:ss"));
                //隐藏箭头
                holder.getView(R.id.iv_borrow_right).setVisibility(View.INVISIBLE);
            }


        }
        holder.getItemView().setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(holder.getItemView(), position, resultBean);
            }
        });


    }
}
