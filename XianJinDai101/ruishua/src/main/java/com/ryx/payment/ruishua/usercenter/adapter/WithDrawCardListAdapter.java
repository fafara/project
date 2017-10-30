package com.ryx.payment.ruishua.usercenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * 余额提现adapter
 * @author muxin
 * @time 2016-08-03 13:03
 */
@EBean
public class WithDrawCardListAdapter extends BaseAdapter {

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
                    R.layout.adapter_withdraw_show_bankcard, null);
            contentView = convertView;
            holder.iv_ico = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.bankName = (TextView) convertView.findViewById(R.id.tv_bankname);
            holder.bankAccount = (TextView) convertView.findViewById(R.id.tv_bankno);
            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
//            holder.autoRelativeLayout = (AutoRelativeLayout) convertView.findViewById(R.id.lay_logo_bg);
            convertView.setTag(holder);
            LogUtil.showLog("height", convertView.getHeight() + "---");
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String bankId = cardInfoList.get(
                position).getBankId();
        BanksUtils.selectIcoidToImgView(context,bankId, holder.iv_ico);
        holder.bankName.setText(cardInfoList.get(position).getBankName());
//        if ("102".equals(bankId) || "104".equals(bankId) || "302".equals(bankId)
//                || "304".equals(bankId) || "306".equals(bankId) || "308".equals(bankId)) {
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.red_bank));
//        } else if ("105".equals(bankId) || "301".equals(bankId) || "309".equals(bankId)
//                || "310".equals(bankId)) {
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.blue_bank));
//        } else if ("103".equals(bankId) || "305".equals(bankId) || "403".equals(bankId)) {
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.green_bank));
//        } else if ("303".equals(bankId) || "307".equals(bankId) || "".equals(bankId)) {
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow_bank));
//        }
        holder.bankAccount.setText(StringUnit.cardJiaMi(cardInfoList.get(
                position).getAccountNo()));

        holder.tv_userName.setText(StringUnit.realNameJiaMi(cardInfoList.get(position).getName()));
        if (null != cardInfoList.get(position).getFlagInfo() && "01".equals(cardInfoList.get(position).getFlagInfo())) {
            holder.iv_hasbound.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_ico;
        ImageView iv_arrow;
        TextView bankName;
        TextView bankAccount;
        TextView tv_userName;
//        AutoRelativeLayout autoRelativeLayout;
        ImageView iv_hasbound;
    }


}