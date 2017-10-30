package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;

import org.json.JSONObject;

import java.util.List;

/**
 * 优惠券item适配器
 *
 */
public class ImPayCouponsAdapter extends HelperAdapter<JSONObject> {
    private OnListItemClickListener mOnItemClickListener = null;
    private View view;
    private String selectedbindid;
    private Double currentMoney=0.0;
    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public ImPayCouponsAdapter(List<JSONObject> data, Context context, String selectedbindid, int... layoutId) {
        super(data, context, layoutId);
        this.selectedbindid=selectedbindid;
    }
   public void setCurrentMoney(Double money){
       this.currentMoney=money;
   }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }



    @Override
    protected void HelperBindData(final HelperViewHolder viewHolder, final int position, final JSONObject acitivityObject) {
        if (null != acitivityObject) {
            /**
             * acitivity_code : 333
             * isused : 0
             * useendtime : 20170420000000
             * couponname : 2元代金券
             * usestarttime : 20170414000000
             * couponvalue : 2
             * couponid : 1
             * valueend : 2000
             * coupontype : 01
             * valuestart : 1000
             * bindid : 27
             */
//            "acitivity_code": "333",
//                    "isused": "0",
//                    "useendtime": "20170420000000",
//                    "couponname": "90折扣元代金券",
//                    "usestarttime": "20170414000000",
//                    "couponvalue": "90",
//                    "couponid": "7",
//                    "valueend": "400",
//                    "coupontype": "02",
//                    "valuestart": "100",
//                    "bindid": "33"

            view = viewHolder.itemView;
            try {
                String useendtime= JsonUtil.getValueFromJSONObject(acitivityObject,"useendtime");
                String couponname= JsonUtil.getValueFromJSONObject(acitivityObject,"couponname");
                String usestarttime= JsonUtil.getValueFromJSONObject(acitivityObject,"usestarttime");
                String couponvalue= JsonUtil.getValueFromJSONObject(acitivityObject,"couponvalue");
                String couponid= JsonUtil.getValueFromJSONObject(acitivityObject,"couponid");
                String valueend= JsonUtil.getValueFromJSONObject(acitivityObject,"valueend");
                String coupontype= JsonUtil.getValueFromJSONObject(acitivityObject,"coupontype");
                final String valuestart= JsonUtil.getValueFromJSONObject(acitivityObject,"valuestart");
                String bindid= JsonUtil.getValueFromJSONObject(acitivityObject,"bindid");

                LogUtil.showLog("33333333333333333"+"selectedbindid=="+selectedbindid+",bindid=="+bindid);
                if("01".equals(coupontype)){
                    String topDisPlay=couponvalue+"元  "+couponname;
                    //现金
                    viewHolder.setText(R.id.type_coupons_tv, topDisPlay);
                    //
                }else if("02".equals(coupontype)){
                    String topDisPlay=Double.parseDouble(couponvalue)/10.00+"折  "+couponname;
                    viewHolder.setText(R.id.type_coupons_tv, topDisPlay);

                }
                String bootomDisplay="满"+valuestart+"可用;有效期至:"+ DateUtil.StrToDateStr(useendtime,"yyyyMMddHHmmss","yyyy-MM-dd");
                viewHolder.setText(R.id.content_coupons_tv, bootomDisplay);
                if(!TextUtils.isEmpty(selectedbindid)&&selectedbindid.equals(bindid)){
                    LogUtil.showLog("111111111111111111"+"selectedbindid=="+selectedbindid+",bindid=="+bindid);
                    viewHolder.setVisible(R.id.iv_checked,true);
                }else{
                    LogUtil.showLog("22222222222222222"+"selectedbindid=="+selectedbindid+",bindid=="+bindid);
                    viewHolder.setVisible(R.id.iv_checked,false);
                }

                if(currentMoney!=0&&currentMoney<Double.parseDouble(valuestart)){
                    view.setBackgroundResource(R.drawable.icon_user_action_bg_pressed);
                }else{
                    view.setBackgroundResource(R.drawable.selector_user_action_bg);
                }
                viewHolder.getItemView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        if (mOnItemClickListener != null){
                            if(currentMoney!=0&&currentMoney<Double.parseDouble(valuestart)){
                                LogUtil.showToast(mContext,"优惠劵金额不符合，请重新选择！");
                            }else{
                                mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, acitivityObject);
                            }
                        }
                    }
                });
            }catch (Exception e){
                LogUtil.showLog("44444444444"+e.getLocalizedMessage()+"");
            }
        }

    }
}
