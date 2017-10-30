package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.GlideUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * 二维码收款收款二维码类型适配器
 * Created by Administrator on 2017/6/26.
 */

public class UnionQrcodeShowPayTypAdapter extends  BaseAdapter {
    Context context;
    ArrayList<Map<String, String>> payTypesMap;
    String selctedPayType;
    public UnionQrcodeShowPayTypAdapter(Context context, ArrayList<Map<String, String>> payTypesMap , String selctedPayType) {
        super();
        this.context = context;
        this.payTypesMap = payTypesMap;
        this.selctedPayType=selctedPayType;
    }
    @Override
    public int getCount() {
        return payTypesMap.size();
    }

    @Override
    public Object getItem(int position) {
        return payTypesMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if(view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.unionqrcode_paytype_bottom_listitem, null);
            holder.paytypenametextview= (TextView)view.findViewById(R.id.paytypenametextview);
            holder.tv_checked= (ImageView)view.findViewById(R.id.tv_checked);
            holder.icon=(ImageView) view.findViewById(R.id.bankimgview);
            view.setTag(holder);
        }else{
            holder =(ViewHolder) view.getTag();
        }
        holder.paytypenametextview.setText(payTypesMap.get(position).get("merchantName"));
//        if("1".equals(cardInfoList.get(position).getCardstatus())){
//            view.setBackgroundColor(context.getResources().getColor(R.color.btn_dark));
//        }else{
//            view.setBackgroundColor(0);
//        }
        GlideUtils.getInstance().load(context,payTypesMap.get(position).get("icon"),holder.icon);
        if(payTypesMap.get(position).get("val").equals(selctedPayType)){
            holder.tv_checked.setVisibility(View.VISIBLE);
        }else{
            holder.tv_checked.setVisibility(View.INVISIBLE);
        }
        return view;
    }
    class ViewHolder {
        TextView paytypenametextview;
        ImageView tv_checked,icon;
    }
}
