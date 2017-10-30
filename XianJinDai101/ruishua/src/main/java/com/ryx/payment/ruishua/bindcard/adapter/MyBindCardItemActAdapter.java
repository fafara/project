package com.ryx.payment.ruishua.bindcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by xucc on 16/6/4.
 */
@EBean
public class MyBindCardItemActAdapter extends BaseAdapter {
    public ArrayList<Map<String,String>> devsList = new ArrayList<Map<String,String>>();
    public OnClickListene onClickListene;
    @RootContext
    Context context;
    public void setList(ArrayList<Map<String,String>> banklist) {
        this.devsList = banklist;
    }
    @Override
    public int getCount() {
        return devsList.size();
    }

    @Override
    public Object getItem(int position) {
        return devsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置点击事件
     */
    public void setItemClickListener(OnClickListene listener){
        this.onClickListene=listener;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.mybndcardserveritem_item, null);
            holder.cardserver_displaycontennt = (TextView) convertView
                    .findViewById(R.id.cardserver_displaycontennt);
            holder.cardserver_isstarted = (TextView) convertView
                    .findViewById(R.id.cardserver_isstarted);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cardserver_displaycontennt.setText(devsList.get(position).get("content")+"");
        String flag=devsList.get(position).get("flag");
        if("1".equals(flag)){
            holder.cardserver_isstarted.setText("已开通");
            holder.cardserver_isstarted.setBackgroundColor(context.getResources().getColor(R.color.card_shadow));
        }else{
            holder.cardserver_isstarted.setText("开通");
            holder.cardserver_isstarted.setBackgroundColor(context.getResources().getColor(R.color.blue_bank));
            holder.cardserver_isstarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("0102".equals(devsList.get(position).get("serverFlag"))){
                        //储蓄卡的代扣服务
                        LogUtil.showToast(context,"暂不支持开通代扣服务,敬请期待!");
                    }else {
                        String isCanOpenQuick=devsList.get(position).get("isCanOpenQuick");
                        if("01".equals(isCanOpenQuick)){

                            if(onClickListene!=null){
                                onClickListene.ClickListener(position,v,devsList.get(position).get("accountNo"));
                            }
                            //当前卡可以进行开通快捷支付(改为Activity执行操作)
//                            Intent intent=new Intent(context,BindedCardOpenQuickPay_.class);
//                            intent.putExtra("accountNo",devsList.get(position).get("accountNo"));
//                            ((Activity)context).startActivityForResult(intent,0x001);
                        }

                    }
                }
            });
        }


        return convertView;
    }
    class ViewHolder {
        TextView cardserver_displaycontennt;
        TextView cardserver_isstarted;
    }
    public interface OnClickListene{
        public void ClickListener(int position,View v,String accountNo);
    }
}
