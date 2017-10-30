package com.ryx.ryxcredit.adapter;

import android.content.Context;

import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.bussiness.borrowdetail.BorrowRecordDetailResponse;
import com.ryx.ryxcredit.utils.CDateUtil;
import com.ryx.ryxcredit.utils.CMoneyEncoder;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public class RePayRecordsAdapter extends HelperAdapter<BorrowRecordDetailResponse.ResultBean.RepaymentsBean> {

    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public RePayRecordsAdapter(List<BorrowRecordDetailResponse.ResultBean.RepaymentsBean> data, Context context, int... layoutId) {
        super(data, context, layoutId);
    }

    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, int position, BorrowRecordDetailResponse.ResultBean.RepaymentsBean item) {
        if (null != item) {
            Date date = CDateUtil.parseDate(item.getRepayment_datetime() , "yyyyMMddHHmmss");
            viewHolder.setText(R.id.c_detail_date,CDateUtil.DateToShortStr(date,"yyyy/MM/dd HH:mm"));
//            viewHolder.setText(R.id.c_detail_time,CDateUtil.DateToShortStr(date,"yyyy/MM/dd HH:mm"));
            viewHolder.setText(R.id.c_detail_money, "还款 " + CMoneyEncoder.EncodeFormat(String.valueOf(item.getRepayment_amount())));
        }
    }

}
