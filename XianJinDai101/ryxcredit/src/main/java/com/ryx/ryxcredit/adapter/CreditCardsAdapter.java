package com.ryx.ryxcredit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.pojo.CCard;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CStringUnit;

import java.util.List;


/**
 * Created by DIY on 2016/9/5.
 */
public class CreditCardsAdapter extends  RecyclerView.Adapter<CreditCardsAdapter.CardItemViewHolder> {
    Context context;
    private List<CCard> datas;
    private  OnItemClickListener onItemClickListener;
    public CreditCardsAdapter(List<CCard> datas,Context cont){
        this.datas=datas;
        this.context = cont;
    }
    @Override
    public CardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_view_card_item, parent, false);
        return new CardItemViewHolder(view);
    }

    public interface OnItemClickListener {
        public void onClick(View parent, int position);
    }

    @Override
    public void onBindViewHolder(final CardItemViewHolder holder, final int position) {
        CCard ccard = (CCard)datas.get(position);
        String cardNo = ccard.getCard_num();
        String card_type = ccard.getCard_type();
        if(!TextUtils.isEmpty(cardNo)){
        holder.tv_cardNo.setText(CStringUnit.cardJiaMi(cardNo)+"");
        }else{
            holder.img_bank.setVisibility(View.GONE);
            holder.tv_cardNo.setText("信用卡"+(position+1));
        }
        if ("2".equalsIgnoreCase(card_type)){
            holder.tv_cardtype.setText("信用卡");
        }else{
            holder.tv_cardtype.setText("储蓄卡");
        }
        if(TextUtils.isEmpty(ccard.getCreate_datetime())){
            if(TextUtils.isEmpty(cardNo)){
                holder.tv_add.setVisibility(View.VISIBLE);
                holder.tv_finish.setVisibility(View.GONE);
            }else{
                holder.tv_add.setVisibility(View.GONE);
                holder.tv_finish.setVisibility(View.VISIBLE);
            }
            holder.img_right.setVisibility(View.GONE);
        }else{
            holder.tv_finish.setVisibility(View.GONE);
            holder.img_right.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(holder.itemView,position);
                }
            }
        });
        String bankCode = ccard.getBank_title_code();
        if(!TextUtils.isEmpty(bankCode)){
            CBanksUtils.selectIcoidToImgView(context,bankCode,holder.img_bank);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {

        if(datas!=null)
            return datas.size();
        else
            return 0;
    }

    /**
     * cards item
     */
    public class CardItemViewHolder extends RecyclerView.ViewHolder{
        public   TextView tv_finish;
        public   TextView tv_cardNo;
        public   TextView tv_add;
        public    ImageView img_right;
        public    ImageView img_bank;
        public    TextView tv_cardtype;
        public CardItemViewHolder(View itemView) {
            super(itemView);
            tv_finish =  (TextView)itemView.findViewById(R.id.tv_finish);
            img_right =  (ImageView)itemView.findViewById(R.id.img_right);
            tv_cardNo = (TextView)itemView.findViewById(R.id.tv_cardNo);
            img_bank = (ImageView)itemView.findViewById(R.id.img_bank);
            tv_add = (TextView)itemView.findViewById(R.id.tv_add);
            tv_cardtype =(TextView)itemView.findViewById(R.id.tv_cardtype);
        }
    }
}
