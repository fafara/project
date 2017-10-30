package com.ryx.payment.ruishua.fragment;


import android.content.Context;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_expired)
public class ExpiredFragment extends BaseFragment {
    private static ExpiredFragment thisInstance;
    private BaseActivity baseActivity;
    @ViewById(R.id.rc_expired)
    public XRecyclerView rc_expired;
    private CouponGridAdapter expiredCouponGridAdapter;
    private List<Map> expiredlist = new ArrayList<Map>();
    @ViewById(R.id.nodatalayout)
    AutoLinearLayout nodatalayout;
    /***
     * 已过期Frag
     * @return
     */
    public static ExpiredFragment getInstance() {
        return new ExpiredFragment_();
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
        RecyclerViewHelper.init().setRVGridLayout(getActivity(), rc_expired, 1);//1列
        expiredCouponGridAdapter = new CouponGridAdapter(expiredlist, getActivity(), MyCouponActivity.EXPIREDFRAG ,R.layout.coupon_adapter_item);
        rc_expired.setPullRefreshEnabled(false);
        rc_expired.setLoadingMoreEnabled(false);
        rc_expired.setAdapter(expiredCouponGridAdapter);
        ((FragmentListener)baseActivity).doDataRequest(MyCouponActivity.EXPIREDFRAG,"2");
    }
    /**
     * 数据结果
     */
    public void sendResult(int code,List<Map> myunUsedlist){
        if(0x001==code){
            if(myunUsedlist==null||myunUsedlist.size()==0){
                rc_expired.setVisibility(View.GONE);
                nodatalayout.setVisibility(View.VISIBLE);
                return;
            }
            nodatalayout.setVisibility(View.GONE);
            rc_expired.setVisibility(View.VISIBLE);
            expiredlist.clear();
            expiredlist.addAll(myunUsedlist);
            expiredCouponGridAdapter.notifyDataSetChanged();
        }
    }


}
