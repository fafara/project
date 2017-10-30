package com.ryx.ryxcredit.xjd.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.ryxcredit.R;

import java.util.List;

/**
 * Created by xiepp on 2017/5/27.
 */

public class CRepayStyleAdapter extends HelperAdapter<String> {

    private View view;
    private OnListItemClickListener mOnItemClickListener = null;

    public CRepayStyleAdapter(Context context, List<String> data, int... layoutId) {
        super(data, context, layoutId);

    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, final int position, final String item) {
        if (null != item) {
            view = viewHolder.getItemView();
            TextView tv_style = (TextView)view.findViewById(R.id.tv_style);
            tv_style.setText(item+"");
            if(this.mOnItemClickListener!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(view,position,item);
                    }
                });
            }
        }
    }
}
