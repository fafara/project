package com.ryx.payment.ruishua.usercenter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.swiper.utils.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by RYX on 2017/4/18.
 */

public class UsedCouponGridAdapter extends HelperAdapter<Map> {
    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;
    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public UsedCouponGridAdapter(List<Map> data, Context context, int... layoutId) {
        super(data, context, layoutId);
        this.context=context;
    }

    public void setmOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, int position, Map item) {
        if (null != item) {
            view = viewHolder.getItemView();
            String couponnameStr = MapUtil.get(item, "couponname", "");
            String transdescStr = MapUtil.get(item, "transdesc", "");
            String usestarttimeStr = MapUtil.get(item, "usestarttime", "");
            String couponvalueStr = MapUtil.get(item, "couponvalue", "");
            String valuestartStr = MapUtil.get(item, "valuestart", "");

            TextView feecoupon_top = (TextView) view.findViewById(R.id.tv_feecoupon_top);
            TextView standflash = (TextView) view.findViewById(R.id.tv_standflash);
            TextView feecoupon_bottom = (TextView) view.findViewById(R.id.tv_feecoupon_bottom);
            TextView money = (TextView) view.findViewById(R.id.tv_money);
            TextView full100canuse = (TextView) view.findViewById(R.id.tv_full100canuse);

            feecoupon_top.setText(couponnameStr);
            standflash.setText(transdescStr);
            feecoupon_bottom.setText("有效期至" + DateUtil.StrToDateStr(usestarttimeStr));
            money.setText("¥" + couponvalueStr);
            full100canuse.setText("满" + valuestartStr + "元使用");
        }
    }
}
