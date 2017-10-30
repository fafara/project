package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.swiper.utils.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * 二维码收款支付类型RecycleView
 * Created by Administrator on 2017/6/26.
 */

public class UnionQrcodePayTypeAdapter extends HelperAdapter<Map<String, String>> {
    private OnListItemClickListener mOnItemClickListener = null;
    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public UnionQrcodePayTypeAdapter(List<Map<String, String>> data, Context context, int... layoutId) {
        super(data, context, layoutId);
    }
    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    protected void HelperBindData(final HelperViewHolder viewHolder, final int position, final Map<String, String> item) {
        if (null != item) {
            viewHolder.setText(R.id.tv_paytypename, MapUtil.getString(item,"merchantName"));
           }
        viewHolder.getItemView().setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, item);
            }
        });
    }
}
