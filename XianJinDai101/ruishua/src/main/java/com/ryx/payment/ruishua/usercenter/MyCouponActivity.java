package com.ryx.payment.ruishua.usercenter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.fragment.ExpiredFragment;
import com.ryx.payment.ruishua.fragment.UnUsedFragment;
import com.ryx.payment.ruishua.fragment.UsedFragment;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_mycoupon)
public class MyCouponActivity extends BaseActivity implements FragmentListener {
    @ViewById
    TabLayout mycoupon_tblayout;
    private UnUsedFragment unUsedFragment;
    private UsedFragment usedFragment;
    private ExpiredFragment expiredFragment;
    public static String UNUSEDFRAG="UNUSEDFRAG";
    public static String USEDFRAG="USEDFRAG";
    public static String EXPIREDFRAG="EXPIREDFRAG";
    private String selectFlag = UNUSEDFRAG;
    @AfterViews
    public void initViews() {
     setTitleLayout("优惠券",true,false);
        initFragment();
        initQtPatParams();
    }
    private void initFragment() {
        unUsedFragment = UnUsedFragment.getInstance();
        usedFragment = UsedFragment.getInstance();
        expiredFragment = ExpiredFragment.getInstance();
        mycoupon_tblayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    toUnUsedFrag();
                } else if (tab.getPosition() == 1) {
                    toUsedFrag();
                }else if(tab.getPosition() == 2){
                    toExpiredFrag();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        toUnUsedFrag();
    }

    /**
     * 未使用
     */
    private void toUnUsedFrag() {
        selectFlag=UNUSEDFRAG;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mycoupon_fragLayout, unUsedFragment, selectFlag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 已经使用
     */
    private void toUsedFrag() {
        selectFlag=USEDFRAG;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mycoupon_fragLayout, usedFragment, selectFlag);
        fragmentTransaction.commitAllowingStateLoss();

    }

    /**
     * 已经过期
     */
    private void toExpiredFrag() {
        selectFlag=EXPIREDFRAG;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mycoupon_fragLayout, expiredFragment, selectFlag);
        fragmentTransaction.commitAllowingStateLoss();

    }
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        isNeedThread = false;
    }
    @Override
    public void doDataRequest(Object data) {



    }

    @Override
    public void doDataRequest(String frgtag, Object data) {
            getDetailData(frgtag,String.valueOf(data));
    }


    private void getDetailData(final String type, String isUsedFlag) {
        qtpayApplication.setValue("QueryMyCoupon.Req");
        qtpayAttributeList.add(qtpayApplication);
        Param unUsedParam = new Param("isUsed");
        unUsedParam.setValue(isUsedFlag);
        qtpayParameterList.add(unUsedParam);
        showLoading();
        httpsPost("QueryMyCouponTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                String data=payResult.getData();
                List<Map> listMap = new ArrayList<Map>();
                try {
                        JSONArray dataJsonArr = new JSONArray(data);
                        for (int i = 0; i < dataJsonArr.length(); i++) {
                            Map map = new HashMap<>();
                            String  couponname = JsonUtil.getValueFromJSONObject(dataJsonArr.getJSONObject(i), "couponname");
                            map.put("couponname", couponname);
                            String  transdesc = JsonUtil.getValueFromJSONObject(dataJsonArr.getJSONObject(i), "transdesc");
                            map.put("transdesc", transdesc);
                            String   usestarttime = JsonUtil.getValueFromJSONObject(dataJsonArr.getJSONObject(i), "usestarttime");
                            map.put("usestarttime", usestarttime);
                            String   couponvalue = JsonUtil.getValueFromJSONObject(dataJsonArr.getJSONObject(i), "couponvalue");
                            map.put("couponvalue", couponvalue);
                            String  valuestart = JsonUtil.getValueFromJSONObject(dataJsonArr.getJSONObject(i), "valuestart");
                            map.put("valuestart", valuestart);
                            String  useendtime = JsonUtil.getValueFromJSONObject(dataJsonArr.getJSONObject(i), "useendtime");
                            map.put("useendtime", useendtime);
                            String  coupontype = JsonUtil.getValueFromJSONObject(dataJsonArr.getJSONObject(i), "coupontype");
                            map.put("coupontype", coupontype);
                            listMap.add(map);
                        }
                    sendData(type,0x001,listMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.showToast(MyCouponActivity.this,"数据异常!");
                    sendData(type,0x002,listMap);
                }


            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                cancleLoading();
                sendData(type,0x002,null);
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                cancleLoading();
                sendData(type,0x002,null);
            }

            @Override
            public void onLoginAnomaly() {
                super.onLoginAnomaly();
                cancleLoading();
                sendData(type,0x002,null);
            }
        });

    }

    /**
     * 0x001正常
     * 0x002异常数据
     * 发送给各个Frg结果
     * @param type
     * @param code
     * @param unUsedlist
     */
    private void sendData(String type,int code,List<Map> unUsedlist){
        if(MyCouponActivity.UNUSEDFRAG.equals(type)){
            //未使用
            if(unUsedFragment!=null&&unUsedFragment.isVisible()){
                unUsedFragment.sendResult(code,unUsedlist);
            }
        }else if(MyCouponActivity.USEDFRAG.equals(type)){
            //已使用
            if(usedFragment!=null&&usedFragment.isVisible()){
                usedFragment.sendResult(code,unUsedlist);
            }

        }else if(MyCouponActivity.EXPIREDFRAG.equals(type)){
            //已过期
            if(expiredFragment!=null&&expiredFragment.isVisible()){
                expiredFragment.sendResult(code,unUsedlist);
            }
        }

    }
}
