package com.ryx.payment.ruishua.sjfx.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.swiper.utils.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by xiepp on 2017/3/21.
 */

public class IncomeAdapter extends HelperAdapter<Map> {

    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;
    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public IncomeAdapter(List<Map> data, Context context, int... layoutId) {
        super(data, context, layoutId);
        this.context=context;
    }
    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(final HelperViewHolder viewHolder, final int position, final Map item) {
        LogUtil.showLog("item---",item+"---");
        if (null != item) {
            view = viewHolder.getItemView();
            String paymsgStr=    MapUtil.get(item,"paymsg","");
            String localdateStr=    MapUtil.get(item,"localdate","");
            String pay_incomeStr=    MapUtil.get(item,"amount","");
//            String term_incomeStr=    MapUtil.get(item,"term_income","");

//            TextView term_income_tv =  (TextView)view.findViewById(R.id.term_income_tv);
            TextView states_tv =  (TextView)view.findViewById(R.id.states_tv);
            TextView pay_income_tv =  (TextView)view.findViewById(R.id.pay_income_tv);
            TextView date_tv =  (TextView)view.findViewById(R.id.date_tv);
//            term_income_tv.setText("机具收益:"+term_incomeStr+"元");
            states_tv.setText(paymsgStr);
            pay_income_tv.setText("¥"+pay_incomeStr);
            date_tv.setText(DateUtil.StrToDateStr(localdateStr));
            view.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClick(view, position, item);
                }
            });
        }
    }

}
