package com.ryx.ryxcredit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.TextView;
import com.ryx.ryxcredit.R;

/**
 * Created by laomao on 16/6/28.
 */
public class RepaylistAdapter extends RecyclerView.Adapter<RepaylistAdapter.ViewHolder> {
    private Context mContext;

    public RepaylistAdapter(Context context) {
        this.mContext = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private boolean isClick = false;
        public View view;
        public TextView money_tv, date_tv, money_sub_tv, date_sub_tv;

        public ViewHolder(View v) {
            super(v);
            view = v;
//            money_tv = (TextView) view.findViewById(R.id.c_repaylist_money_tv);
//            date_tv = (TextView) view.findViewById(R.id.c_repaylist_date_tv);
//            textView = (TextView) v.findViewById(R.id.text);
//            imageView = (ImageView) v.findViewById(R.id.image);
//            imageLayout = (LinearLayout) v.findViewById(R.id.image_layout);
//            textLayout = (LinearLayout) v.findViewById(R.id.text_layout);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (isClick) {
//                        CRecyclerViewUtil.collapse(v);
//                    } else {
//                        CRecyclerViewUtil.expand(v);
//                    }
//                    isClick = !isClick;
                }
            });
        }
    }

    @Override
    public RepaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c_repaylist_item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RepaylistAdapter.ViewHolder holder, int position) {
//        if (position == 0) {
//            holder.money_tv.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
//            holder.money_tv.setText(holder.money_tv.getText()+"(已逾期)");
//            holder.date_tv.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
//        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
