package com.ryx.ryxcredit.xjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.xjd.bean.borrow.CXJDProductDetailResponse;

import java.util.List;

/**
 * 产品期数
 */

public class CProductTermAdapter extends BaseAdapter {

    private Context cont;
    private List<CXJDProductDetailResponse.ResultBean.SubProductsBean> msubList;

    public CProductTermAdapter(Context context, List<CXJDProductDetailResponse.ResultBean.SubProductsBean> termList) {
        this.cont = context;
        this.msubList = termList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return msubList.size();
    }

    /**
     * 单位标签转汉字
     *
     * @param unit
     * @return
     */
    private String unit2text(String unit) {
        if ("M".equals(unit))
            return "个月";
        if ("D".equals(unit))
            return "天";
        return "";
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(cont).inflate(R.layout.c_listview_date_item, null);
        TextView tv_text = (TextView)convertView.findViewById(R.id.tv_day);
        tv_text.setText(msubList.get(position).getTerm()+unit2text(msubList.get(position).getSpan_unit()));
        return convertView;
    }

}
