package com.ryx.payment.ruishua.fragment;


import android.content.Context;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.usercenter.MyCouponActivity;
import com.ryx.payment.ruishua.usercenter.adapter.CouponGridAdapter;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.quickadapter.recyclerView.animation.ScaleInAnimation;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_unused)
public class UnUsedFragment extends BaseFragment {
    private static UnUsedFragment thisInstance;
    private BaseActivity baseActivity;
    @ViewById(R.id.rc_unUserd)
    public XRecyclerView rc_unUserd;
    @ViewById
    AutoLinearLayout nodatalayout;
    private CouponGridAdapter unUsedCouponGridAdapter;
    private List<Map> unUsedlist = new ArrayList<Map>();

    public static UnUsedFragment getInstance() {
        return new UnUsedFragment_();
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
        RecyclerViewHelper.init().setRVGridLayout(getActivity(), rc_unUserd, 1);//1列
        unUsedCouponGridAdapter = new CouponGridAdapter(unUsedlist, getActivity(), MyCouponActivity.UNUSEDFRAG ,R.layout.coupon_adapter_item);
        unUsedCouponGridAdapter.openLoadAnimation(new ScaleInAnimation());
        rc_unUserd.setPullRefreshEnabled(false);
        rc_unUserd.setLoadingMoreEnabled(false);
        rc_unUserd.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rc_unUserd.setAdapter(unUsedCouponGridAdapter);
        ((FragmentListener)baseActivity).doDataRequest(MyCouponActivity.UNUSEDFRAG,"0");
    }

    /**
     * 数据结果
     */
    public void sendResult(int code,List<Map> myunUsedlist){
        if(0x001==code){
            if(myunUsedlist==null||myunUsedlist.size()==0){
                rc_unUserd.setVisibility(View.GONE);
                nodatalayout.setVisibility(View.VISIBLE);
                return;
            }
            nodatalayout.setVisibility(View.GONE);
            rc_unUserd.setVisibility(View.VISIBLE);
            unUsedlist.clear();
            unUsedlist.addAll(myunUsedlist);
            unUsedCouponGridAdapter.notifyDataSetChanged();
        }
    }
}
