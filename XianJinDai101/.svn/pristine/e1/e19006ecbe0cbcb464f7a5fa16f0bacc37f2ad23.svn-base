package com.ryx.payment.ruishua.usercenter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.usercenter.MyCouponActivity;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.swiper.utils.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Twh on 2017/4/18.
 */

public class CouponGridAdapter extends HelperAdapter<Map> {
    private OnListItemClickListener mOnItemClickListener = null;
    Context context;
    private View view;
    String frgTag;

    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public CouponGridAdapter(List<Map> data, Context context,String frgTag, int... layoutId) {
        super(data, context, layoutId);
        this.context=context;
        this.frgTag=frgTag;
    }
    public void setmOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, int position, Map item) {
        if (null != item) {
            view = viewHolder.getItemView();
            String couponnameStr=    MapUtil.get(item,"couponname","");
            String  transdescStr=    MapUtil.get(item,"transdesc","");
            String  couponvalueStr=    MapUtil.get(item,"couponvalue","");
            String  useendtimeStr=    MapUtil.get(item,"useendtime","");
            String  valuestartStr=    MapUtil.get(item,"valuestart","");
            String  coupontypeStr=    MapUtil.get(item,"coupontype","");
            LinearLayout mycouponRightlayout  = (LinearLayout) view.findViewById(R.id.mycoupon_rightlayout);
            ImageView used_img  = (ImageView) view.findViewById(R.id.mucoupon_used_img);
            if(MyCouponActivity.UNUSEDFRAG.equals(this.frgTag)){
                //未使用
                used_img.setVisibility(View.GONE);
                mycouponRightlayout.setBackgroundResource(R.drawable.mycoupon_bluebg);
            }else if(MyCouponActivity.USEDFRAG.equals(this.frgTag)){
                //已使用
                used_img.setVisibility(View.VISIBLE);
                mycouponRightlayout.setBackgroundResource(R.drawable.mycoupon_graybg);
            }else{
                //已过期
                used_img.setVisibility(View.GONE);
                mycouponRightlayout.setBackgroundResource(R.drawable.mycoupon_graybg);
            }
            viewHolder.setText(R.id.tv_feecoupon_top, couponnameStr);
            viewHolder.setText(R.id.tv_standflash, transdescStr);
            viewHolder.setText(R.id.tv_feecoupon_bottom, "有效期至 "+DateUtil.StrToDateStr(useendtimeStr,"yyyyMMddHHmmss","yyyy-MM-dd"));
            viewHolder.setText(R.id.tv_full100canuse, "满"+valuestartStr+"元使用");

            if("01".equals(coupontypeStr)){
                String topDisPlay="¥"+couponvalueStr;
                //现金
                viewHolder.setText(R.id.tv_money, topDisPlay);
                //
            }else if("02".equals(coupontypeStr)){
                String topDisPlay=Double.parseDouble(couponvalueStr)/10.00+"折";
                viewHolder.setText(R.id.tv_money, topDisPlay);

            }





        }
    }
}
