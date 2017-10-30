package com.ryx.payment.ruishua.usercenter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.convenience.RuiBeanBuyUseRecordMainActivity;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.RyxMoneyEncoder;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.swiper.utils.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by xucc on 2017/4/18.
 */

public class RuibeanRecordGridAdapter extends HelperAdapter<Map> {
    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;
    String frgTag;

    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public RuibeanRecordGridAdapter(List<Map> data, Context context, String frgTag, int... layoutId) {
        super(data, context, layoutId);
        this.context=context;
        this.frgTag=frgTag;
    }
    public void setmOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, int position, Map item) {
//        {"customerid":"A000662471","customerid2":"A000662471","productdesc":"闪付","orderid":"170525462462456","localdate":"20170524","localtime":"173454","type":"购买","count":"30","count_real":"30","amount":"3","count_before":"30","count_after":"0"}
        if(null != item){
        if( RuiBeanBuyUseRecordMainActivity.BUYRECORDFRAG.equals(frgTag)){
            //购买记录
            view = viewHolder.getItemView();
            String typeStr=    MapUtil.get(item,"type","");
            String localdateStr=    MapUtil.get(item,"localdate","");
            String localtimeStr=    MapUtil.get(item,"localtime","");
            String countStr=    MapUtil.get(item,"count","0");
            String amountStr=    MapUtil.get(item,"amount","0");
            TextView tv_shouming=(TextView) view.findViewById(R.id.tv_shouming);
            tv_shouming.setText(typeStr);
            TextView tv_count=(TextView) view.findViewById(R.id.tv_count);
            tv_count.setText("+"+countStr);
            TextView tv_time=(TextView) view.findViewById(R.id.tv_time);
            String dateTime=  DateUtil.StrToDateStr(localdateStr+localtimeStr,"yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");
            tv_time.setText(dateTime);
            TextView tv_money=(TextView) view.findViewById(R.id.tv_money);
            tv_money.setText(RyxMoneyEncoder.EncodeFormat(String.valueOf(Integer.parseInt(amountStr)/100.00)));
        }else{
            //使用记录
            view = viewHolder.getItemView();
            String typeStr=    MapUtil.get(item,"type","");
            String localdateStr=    MapUtil.get(item,"localdate","");
            String localtimeStr=    MapUtil.get(item,"localtime","");
            String countStr=    MapUtil.get(item,"count","0");
            TextView tv_shouming=(TextView) view.findViewById(R.id.tv_shouming);
            tv_shouming.setText(typeStr);
            TextView tv_count=(TextView) view.findViewById(R.id.tv_count);
            tv_count.setText("-"+countStr);
            TextView tv_time=(TextView) view.findViewById(R.id.tv_time);
            String dateTime=  DateUtil.StrToDateStr(localdateStr+localtimeStr,"yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");
            tv_time.setText(dateTime);
        }
        }
    }
}
