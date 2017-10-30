package com.ryx.payment.ruishua.bindcard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.StringUnit;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by xucc
 * 卡管理列表页面
 */
@EBean
public class BindedCardListAdapter extends BaseAdapter {

    private ArrayList<BankCardInfo> cardInfoList;
    private View contentView;

    private int height, width;
    private ViewHolder holder = null;

    @RootContext
    Context context;

    public void setList(ArrayList<BankCardInfo> banklist) {
        this.cardInfoList = banklist;
    }

    @AfterInject
    void init() {
        cardInfoList = new ArrayList<BankCardInfo>();
    }

    @Override
    public int getCount() {
        return cardInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_binded_card_item, null);
            contentView = convertView;
            holder.iv_ico = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.bankName = (TextView) convertView.findViewById(R.id.tv_bankname);
            holder.bankAccount = (TextView) convertView.findViewById(R.id.tv_bankno);
            holder.tv_dai = (TextView) convertView.findViewById(R.id.tv_dai);
            holder.tv_kuai = (TextView) convertView.findViewById(R.id.tv_kuai);
            holder.tv_mo = (TextView) convertView.findViewById(R.id.tv_mo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String bankId = cardInfoList.get(
                position).getBankId();
        if(!TextUtils.isEmpty(bankId)&&bankId.length()>6){
            bankId = bankId.substring(1,4);
        }
        BanksUtils.selectIcoidToImgView(context,bankId,holder.iv_ico);
       String bankShortName= cardInfoList.get(position).getBankName();
        if("01".equals(cardInfoList.get(
                position).getCardtype())||"".equals(cardInfoList.get(
                position).getCardtype())){
            //储蓄卡
            holder.bankName.setText(bankShortName+"(储蓄卡)");
        }else{
            //信用卡
            holder.bankName.setText( bankShortName+"(信用卡)");
        }holder.bankAccount.setText(StringUnit.cardJiaMi(cardInfoList.get(
                position).getAccountNo()));
       String flag= cardInfoList.get(
                position).getFlagInfo();
        //先判断是否已经开通，再次判断信用卡不展示，其余展示
        //只要是快捷开通则进行显示
        if("1".equals(cardInfoList.get(
                position).getQuick())){
            holder.tv_kuai.setVisibility(View.VISIBLE);
        }else{
            holder.tv_kuai.setVisibility(View.GONE);
        }

        if("1".equals(cardInfoList.get(
                position).getDaikou())&&"01".equals(cardInfoList.get(
                position).getCardtype())){
            //储蓄卡并且开通民生代扣
            holder.tv_dai.setVisibility(View.VISIBLE);
        }else{
            holder.tv_dai.setVisibility(View.GONE);
        }
      if("1".equals(cardInfoList.get(position).getFlagInfo())&&("01".equals(cardInfoList.get(position).getCardtype())||"".equals(cardInfoList.get(position).getCardtype()))){
          //储蓄卡默认结算卡
          holder.tv_mo.setVisibility(View.VISIBLE);
      }else{
        holder.tv_mo.setVisibility(View.GONE);
      }
        return convertView;
    }
    @Override
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        String flagInfo=cardInfoList.get(position).getFlagInfo();
        if("1".equals(flagInfo)){
            return 1;
        }else{
            return 0;
        }
    }
    class ViewHolder {
        ImageView iv_ico;
        ImageView iv_arrow;
        TextView bankName;
        TextView bankAccount;
        TextView tv_kuai;
        TextView tv_dai;
        TextView tv_mo;

    }


}