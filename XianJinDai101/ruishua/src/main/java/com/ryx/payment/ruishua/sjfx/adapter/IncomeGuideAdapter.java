package com.ryx.payment.ruishua.sjfx.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.GlideUtils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.swiper.utils.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by xucc on
 */

public class IncomeGuideAdapter extends HelperAdapter<Map> {

    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;
    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public IncomeGuideAdapter(List<Map> data, Context context, int... layoutId) {
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
            String incomguide_img=    MapUtil.get(item,"imgUrl","");
            ImageView imgView =  (ImageView)view.findViewById(R.id.incomguide_img);
            GlideUtils.getInstance().load( this.context,incomguide_img,imgView);
            String explainText=    MapUtil.get(item,"explainText","");
            TextView tv_explaintext =  (TextView)view.findViewById(R.id.tv_explaintext);
            tv_explaintext.setText(explainText);
            if(this.mOnItemClickListener!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v,position,null);
                    }
                });
            }
        }
    }

}
