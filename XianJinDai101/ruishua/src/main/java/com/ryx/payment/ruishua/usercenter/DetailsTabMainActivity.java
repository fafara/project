package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.bindcard.BankCardAddActivity_;
import com.ryx.payment.ruishua.fragment.CashFlowFragment;
import com.ryx.payment.ruishua.fragment.DetailsContentFragment;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

@EActivity(R.layout.activity_details_tab_main)
public class DetailsTabMainActivity extends BaseActivity implements FragmentListener {
    TradeDetailInfo tradeDetailInfo;
    CashFlowFragment cashFlowFragment;
    DetailsContentFragment  detailsContentFragment;
    @ViewById
    ListView lv_detaile;
    @ViewById
    AppBarLayout appLayout;
    private String flag;
    @ViewById
    TextView tv_title;

    @AfterViews
    public void afterInitViews(){

        tradeDetailInfo=(TradeDetailInfo)getIntent().getSerializableExtra("detailInfo");
        flag = getIntent().getExtras().getString("ActivityFlag");
        if ("income".equals(flag)){
            setTitleLayout("收款明细",true,false);
        }else if("expend".equals(flag)){
            setTitleLayout("付款明细",true,false);
        }else if("withdrawl".equals(flag)){
            setTitleLayout("结算明细",true,false);
        }else if("payment".equals(flag)){
            setTitleLayout("缴费明细",true,false);
        }else if("sweepcode".equals(flag)){
            setTitleLayout("扫码收款明细",true,false);
        }
        if ("income".equals(flag)||"withdrawl".equals(flag)||"sweepcode".equals(flag)) {
            appLayout.setVisibility(View.VISIBLE);
        }else{
            appLayout.setVisibility(View.GONE);
        }
        LogUtil.showLog("ryx","DetailsTabMainActivity=="+tradeDetailInfo.getAmount());
        TabLayout tabLayout= (TabLayout) findViewById(R.id.commonTabLayout);
        cashFlowFragment=CashFlowFragment.newInstance(tradeDetailInfo,flag);
        detailsContentFragment=  DetailsContentFragment.newInstance(tradeDetailInfo,flag);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    todetailsFrg();
                }else {
                    tocashFrg();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        todetailsFrg();
    }

    /**
     * 收支明细
     */
    private void todetailsFrg(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(!detailsContentFragment.isAdded()){
            LogUtil.showLog("detailsContentFragment===初次加载");
            fragmentTransaction.add(R.id.tabfragemetcomtent, detailsContentFragment,"details").commitAllowingStateLoss();
        }else{
            LogUtil.showLog("detailsContentFragment===isAdded");
            fragmentTransaction.hide(cashFlowFragment).show(detailsContentFragment).commitAllowingStateLoss();
//            fragmentTransaction.replace(R.id.tabfragemetcomtent,detailsContentFragment,"details").commitAllowingStateLoss();
        }
//        fragmentTransaction.replace(R.id.tabfragemetcomtent,detailsContentFragment,"details");
//        fragmentTransaction.commit();
    }

    /**
     * 资金动态
     */
    private void tocashFrg(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(!cashFlowFragment.isAdded()){
            LogUtil.showLog("cashFlowFragment===初次加载");
            fragmentTransaction.hide(detailsContentFragment).add(R.id.tabfragemetcomtent, cashFlowFragment,"cash").commitAllowingStateLoss();
        }else{
            LogUtil.showLog("cashFlowFragment===isAdded");
            fragmentTransaction.hide(detailsContentFragment).show(cashFlowFragment).commitAllowingStateLoss();
//            fragmentTransaction.replace(R.id.tabfragemetcomtent,cashFlowFragment,"cash").commitAllowingStateLoss();
        }
//        fragmentTransaction.replace(R.id.tabfragemetcomtent,cashFlowFragment,"cash");
//        fragmentTransaction.commit();
    }
    @Override
    public void doDataRequest(Object data) {
        Map paramMap=(Map)data;
        if("GetBalanceStep".equals(paramMap.get("flag"))){
            //获取资金动态数据
            TradeDetailInfo detailInfo =(TradeDetailInfo)paramMap.get("data");
            initQtPatParams();
            qtpayApplication.setValue("GetBalanceStep.Req");
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(new Param("localdate", DataUtil.getRightValue(detailInfo.getLocalDate())));
            qtpayParameterList.add(new Param("locallogno",DataUtil.getRightValue(detailInfo.getLocalLogNo())));
            qtpayParameterList.add(new Param("orderId",DataUtil.getRightValue(detailInfo.getOrderId())));
            httpsPost("GetBalanceStepTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    cashFlowFragment.send(0x111,payResult);
                }
            });
        }else if("getBankCardList".equals(paramMap.get("flag"))){
            //获取银行卡列表
            initQtPatParams();
            qtpayApplication.setValue("BindCardList.Req");
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(new Param("cardType","10"));
            httpsPost("BindCardListTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    cashFlowFragment.send(0x222,payResult);
                }
            });
//            qtpayApplication.setValue("GetBankCardList2.Req");
//            qtpayAttributeList.add(qtpayApplication);
//            qtpayParameterList.add(new Param("bindType", "01"));
//            qtpayParameterList.add(new Param("cardIdx","00"));
//            qtpayParameterList.add(new Param("cardNum", "05"));
//            httpsPost("GetBankCardList2Tag", new XmlCallback() {
//                @Override
//                public void onTradeSuccess(RyxPayResult payResult) {
//                    cashFlowFragment.send(0x222,payResult);
//                }
//            });
        }else if("tobindBankAct".equals(paramMap.get("flag"))){
            //去绑定银行卡界面
        Intent    intent = new Intent(this,
                BankCardAddActivity_.class);
            startActivity(intent);
            finish();
        }else if("anewDrawMoney".equals(paramMap.get("flag"))){
            String  cardInfo=(String)paramMap.get("cardInfo");
            String  cardidx=(String)paramMap.get("cardidx");
            String  oldaccount=(String)paramMap.get("oldaccount");
            String  md5code=(String)paramMap.get("md5code");
            TradeDetailInfo detailInfo =(TradeDetailInfo)paramMap.get("data");
            anewDrawMoney(detailInfo,cardInfo,cardidx,oldaccount,md5code);
        }

    }

    @Override
    public void doDataRequest(String type, Object data) {

    }

    /**
     * 银行卡信息重提
     */
    private void anewDrawMoney( TradeDetailInfo detailInfo,String cardInfo,String cardidx,String oldaccount,String md5code) {
        initQtPatParams();
        qtpayApplication.setValue("AnewDrawMoney.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("localdate",DataUtil.getRightValue(detailInfo.getLocalDate())));
        qtpayParameterList.add(new Param("locallogno",DataUtil.getRightValue(detailInfo.getLocalLogNo())));
        qtpayParameterList.add(new Param("orderId",DataUtil.getRightValue(detailInfo.getOrderId())));
        qtpayParameterList.add(new Param("cardInfo",cardInfo));
        qtpayParameterList.add(new Param("cardIdx",cardidx));
        qtpayParameterList.add(new Param("account",oldaccount));
        qtpayParameterList.add(new Param("md5code",md5code));
        httpsPost("AnewDrawMoneyTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                cashFlowFragment.send(0x333,payResult);
            }
        });
    }

}
