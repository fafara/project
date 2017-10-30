package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.swiper.utils.MoneyEncoder;

import java.util.List;

/**
 * 交易明细
 */
public class IncomeAdapter extends BaseAdapter {
    Context context;
    public List<TradeDetailInfo> incomeList;
    public IncomeAdapter(Context context,List incomeLis) {
        super();
        this.context = context;
        this.incomeList=incomeLis;
    }
    @Override
    public int getCount() {
        return incomeList.size();
    }

    @Override
    public Object getItem(int position) {
        return incomeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.detailslist_item, null);
            holder = new ViewHolder();
            holder.tv_explanation = (TextView) convertView
                    .findViewById(R.id.tv_shouming);
            holder.tv_money = (TextView) convertView
                    .findViewById(R.id.tv_money);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
//            holder.tv_state = (TextView) convertView
//                    .findViewById(R.id.tv_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_explanation.setText(DataUtil.getRightValue(incomeList.get(position)
                .getTransName())); //
        holder.tv_money.setText(MoneyEncoder.decodeFormat(
                DataUtil. getRightValue(incomeList.get(position).getAmount())).replace(
                "￥", "+"));
//        holder.tv_money.setTextColor(context.getResources().getColor(
//                R.color.draw));
        holder.tv_time.setText(fomatDate(DataUtil.getRightValue(incomeList.get(
                position).getLocalDate())
                + DataUtil.getRightValue(incomeList.get(position).getLocalTime())));
//        holder.tv_state.setText(DataUtil.getRightValue(incomeList.get(position)
//                .getPaytag()));
        return convertView;


    }

    class ViewHolder {
        TextView tv_explanation;
        TextView tv_money;
        TextView tv_time;
//        TextView tv_state;
    }
    public static String fomatDate(String date) {
        return DateUtil.DateToString(DateUtil.StrToDate(date));
    }
}
