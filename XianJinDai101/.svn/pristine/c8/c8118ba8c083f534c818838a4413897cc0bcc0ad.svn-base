package com.ryx.payment.ruishua.convenience;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.fragment.RuiBeanBuyRecordFragment;
import com.ryx.payment.ruishua.fragment.RuibeanUseRecordFragment;
import com.ryx.payment.ruishua.listener.FragmentListener;

/**
 * 瑞豆使用记录
 */
public class RuiBeanBuyUseRecordMainActivity extends BaseActivity  implements FragmentListener {
    TabLayout myruibean_tblayout;
    private RuibeanUseRecordFragment ruibeanUseRecordFragment;
    private RuiBeanBuyRecordFragment ruiBeanBuyRecordFragment;
    public static String USERECORDFRAG="USERECORDFRAG";
    public static String BUYRECORDFRAG="BUYRECORDFRAG";
    private String selectFlag = BUYRECORDFRAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rui_bean_buy_record_main_frg);
        myruibean_tblayout=(TabLayout)findViewById(R.id.myruibean_tblayout);
        setTitleLayout("使用记录",true,false);
        initQtPatParams();
        initFragment();
    }



    private void initFragment() {
        ruibeanUseRecordFragment = RuibeanUseRecordFragment.getInstance();
        ruiBeanBuyRecordFragment = RuiBeanBuyRecordFragment.getInstance();
        myruibean_tblayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    toBuyRecordFrag();
                } else if (tab.getPosition() == 1) {
                    toUseRecordFrag();
                }
            }

            

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        toBuyRecordFrag();
    }

    /**
     * 切换购买记录
     */
    private void toBuyRecordFrag() {
        selectFlag=BUYRECORDFRAG;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ruibeanbuyrecord_fragLayout, ruiBeanBuyRecordFragment, selectFlag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 切换使用记录
     */
    private void toUseRecordFrag() {
        selectFlag=USERECORDFRAG;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ruibeanbuyrecord_fragLayout, ruibeanUseRecordFragment, selectFlag);
        fragmentTransaction.commitAllowingStateLoss();

    }
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
    }


    @Override
    public void doDataRequest(Object data) {

    }

    @Override
    public void doDataRequest(String type, Object data) {

    }
}
