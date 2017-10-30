package com.ryx.payment.ruishua.fragment;


import android.content.Context;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.usercenter.MyCouponActivity;
import com.ryx.payment.ruishua.usercenter.adapter.CouponGridAdapter;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 已使用优惠券Frg
 */
@EFragment(R.layout.fragment_used)
public class UsedFragment extends BaseFragment {
    private static UsedFragment thisInstance;
    private BaseActivity baseActivity;
    private String customerId;
    private String couponname;
    private String transdesc;
    private String usestarttime;
    private String couponvalue;
    private String valuestart;
    @ViewById(R.id.rc_userd)
    public XRecyclerView rc_userd;
    @ViewById(R.id.nodatalayout)
    AutoLinearLayout nodatalayout;
    private CouponGridAdapter usedCouponGridAdapter;
    private List<Map> usedlist = new ArrayList<Map>();

    /**
     * 已使用Frag
     * @return
     */
    public static UsedFragment getInstance() {
        return new UsedFragment_();
    }
    @Override
    public void onAttach(Context context) {
        try {
            baseActivity = getBaseActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);

    }
    @AfterViews
    public void afterView() {
        RecyclerViewHelper.init().setRVGridLayout(getActivity(), rc_userd, 1);//1列
        usedCouponGridAdapter = new CouponGridAdapter(usedlist, getActivity(), MyCouponActivity.USEDFRAG ,R.layout.coupon_adapter_item);
        rc_userd.setPullRefreshEnabled(false);
        rc_userd.setLoadingMoreEnabled(false);
        rc_userd.setAdapter(usedCouponGridAdapter);
        ((FragmentListener)baseActivity).doDataRequest(MyCouponActivity.USEDFRAG,"1");
    }
    /**
     * 数据结果
     */
    public void sendResult(int code,List<Map> myunUsedlist){
        if(0x001==code){
            if(myunUsedlist==null||myunUsedlist.size()==0){
                rc_userd.setVisibility(View.GONE);
                nodatalayout.setVisibility(View.VISIBLE);
                return;
            }
            nodatalayout.setVisibility(View.GONE);
            rc_userd.setVisibility(View.VISIBLE);
            usedlist.clear();
            usedlist.addAll(myunUsedlist);
            usedCouponGridAdapter.notifyDataSetChanged();
        }
    }
}
