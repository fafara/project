package com.ryx.payment.ruishua.sjfx.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.AdavertisementInfo;
import com.ryx.payment.ruishua.utils.GlideUtils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public class AdavertisementAdapter extends HelperAdapter<AdavertisementInfo.AdvertRowsBean> {
    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;
    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public AdavertisementAdapter(List<AdavertisementInfo.AdvertRowsBean> data, Context context, int... layoutId) {
        super(data, context, layoutId);
        this.context=context;
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(final HelperViewHolder viewHolder, final int position, final AdavertisementInfo.AdvertRowsBean item) {
        if (null != item) {
        view = viewHolder.getItemView();
        String url = item.getMediaRow().getMedia_url();
        ImageView iv_advert =  (ImageView)view.findViewById(R.id.iv_advert);
        LogUtil.showLog("url----",url+"----"+iv_advert);
        GlideUtils.getInstance().load(context,url,iv_advert);
            view.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, item);
                }
            });
        }
    }
}
