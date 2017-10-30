package com.ryx.payment.ruishua.bindcard;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.adapter.MyBindCardItemActAdapter;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_my_bind_card_item)
public class MyBindCardItemActivity extends BaseActivity {
@ViewById
ListView mybindcardlistview;
    @ViewById
    ImageView iv_logo;
    @ViewById
    TextView tv_bankname;
    @ViewById
    TextView tv_bankno;
    @ViewById
    TextView tv_kuai;
    @ViewById
    TextView tv_dai;
    @ViewById
    TextView tv_debitcard,daifustatus_nosupport;

    @ViewById
    Button bt_setdefaultcard,bt_cancledefaultcard;

    @Bean
    MyBindCardItemActAdapter myBindCardItemActAdapter;
    BankCardInfo bankCardInfo=null;
    Param qtpayCardIdx;
    @AfterViews
    public void afterInitView(){
        setTitleLayout("我的银行卡", true, false);
        initQtPatParams();
        initData();
    }
    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayCardIdx = new Param("cardIdx");
    }
    private void initData() {
        try {
       bankCardInfo=(BankCardInfo) getIntent().getSerializableExtra("bankObj");
            ArrayList<Map<String,String>> cardServerList=new ArrayList<Map<String,String>>();
        //头部横条显示
            String bankId=bankCardInfo.getBankId();
            if(!TextUtils.isEmpty(bankId)&&bankId.length()>6){
                bankId = bankId.substring(1,4);
            }
            String bankShotName=bankCardInfo.getBankName();
            if("01".equals(bankCardInfo.getCardtype())||"".equals(bankCardInfo.getCardtype())){
                //当前为储蓄卡
                tv_bankname.setText(bankShotName+"(储蓄卡)");
                if("1".equals(bankCardInfo.getQuick())){
                    tv_kuai.setVisibility(View.VISIBLE);
                }else{
                    tv_kuai.setVisibility(View.GONE);
                }
                if("1".equals(bankCardInfo.getDaikou())){
                    tv_dai.setVisibility(View.VISIBLE);
                }else{
                    tv_dai.setVisibility(View.GONE);
                }
                if("1".equals(bankCardInfo.getFlagInfo())){
                    //当前为默认结算卡
                    tv_debitcard.setVisibility(View.VISIBLE);
                    bt_setdefaultcard.setVisibility(View.GONE);
                    bt_cancledefaultcard.setVisibility(View.VISIBLE);
                }else{
                    //当前储蓄卡，非默认结算卡
                    tv_debitcard.setVisibility(View.GONE);
                    if("1".equals(bankCardInfo.getDaifustatus())){
                        //当前支持代扣业务卡,才可以设置为默认结算卡
                        bt_setdefaultcard.setVisibility(View.VISIBLE);
                        daifustatus_nosupport.setVisibility(View.GONE);
                        bt_cancledefaultcard.setVisibility(View.GONE);
                    }else{
                        daifustatus_nosupport.setVisibility(View.VISIBLE);
                    }
                }
                Map map1= new HashMap<String,String >();
                map1.put("content","代扣服务:信用卡,借助自动还款");
                map1.put("flag",bankCardInfo.getDaikou());
                map1.put("cardType",bankCardInfo.getCardtype());
                map1.put("serverFlag","0102");
                cardServerList.add(map1);

                Map map2= new HashMap<String,String >();
                map2.put("content","快捷支付:快捷付款,免刷卡");
                map2.put("flag",bankCardInfo.getQuick());
                map2.put("cardType",bankCardInfo.getCardtype());
                map2.put("serverFlag","0101");
                map2.put("isCanOpenQuick","01");//01代表可以开通
                map2.put("accountNo",bankCardInfo.getAccountNo());//账号
                cardServerList.add(map2);
                myBindCardItemActAdapter.setList(cardServerList);
//                ViewGroup.LayoutParams params = mybindcardlistview.getLayoutParams();
//                params.height = cardServerList.size()*200;
//                mybindcardlistview.setLayoutParams(params);
                mybindcardlistview.setAdapter(myBindCardItemActAdapter);
            }else{
                tv_bankname.setText(bankShotName+"(信用卡)");
                bt_setdefaultcard.setVisibility(View.GONE);
                bt_cancledefaultcard.setVisibility(View.GONE);
                if("1".equals(bankCardInfo.getQuick())){
                    tv_kuai.setVisibility(View.VISIBLE);
                }else{
                    tv_kuai.setVisibility(View.GONE);
                }
                tv_dai.setVisibility(View.GONE);
                tv_debitcard.setVisibility(View.GONE);
                Map map2= new HashMap<String,String >();
                map2.put("content","快捷支付:快捷付款,免刷卡");
                map2.put("flag",bankCardInfo.getQuick());
                map2.put("cardType",bankCardInfo.getCardtype());
                map2.put("serverFlag","0101");
                map2.put("isCanOpenQuick","01");//01代表可以开通
                map2.put("accountNo",bankCardInfo.getAccountNo());//账号
                cardServerList.add(map2);
//                ViewGroup.LayoutParams params = mybindcardlistview.getLayoutParams();
//                params.height = cardServerList.size()*260;
//                mybindcardlistview.setLayoutParams(params);
                myBindCardItemActAdapter.setList(cardServerList);
                mybindcardlistview.setAdapter(myBindCardItemActAdapter);
            }
            myBindCardItemActAdapter.setItemClickListener(new MyBindCardItemActAdapter.OnClickListene() {
                @Override
                public void ClickListener(int position, View v, final String accountNo) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initQtPatParams();
                            qtpayApplication = new Param("application", "QuickPaymentBindCardPrepare.Req");
                            qtpayAttributeList.add(qtpayApplication);
                            qtpayParameterList.add(new Param("account", accountNo));
                            httpsPost("QuickPaymentBindCardPrepareTag", new XmlCallback() {
                                @Override
                                public void onTradeSuccess(RyxPayResult payResult) {
                                    String result =  payResult.getData();
                                    Intent intent=new Intent(MyBindCardItemActivity.this,BindedCardOpenQuickPay_.class);
                                     intent.putExtra("accountNo",accountNo);
                                     intent.putExtra("prepareresult",result);
                                     startActivityForResult(intent,0x001);
                                }
                            });
                        }
                    });
                }
            });
            innitListViewHeight();
        tv_bankno.setText(StringUnit.cardJiaMi(bankCardInfo.getAccountNo()));
            BanksUtils.selectIcoidToImgView(MyBindCardItemActivity.this,bankId,iv_logo);


        }catch (Exception e){
            LogUtil.showToast(MyBindCardItemActivity.this,"数据异常!");
        }
    }
    @Click(R.id.my_unbind_btn)
    public void unbindBtnClick(){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                unBindCard();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确定删除该卡片，此操作不可逆！");
    }
    @Click(R.id.bt_setdefaultcard)
    public void setdefaultcardBtnClick(){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                setDefaultCard();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确定要设置当前储蓄卡为默认结算卡?");
    }
    @Click(R.id.bt_cancledefaultcard)
    public void cancledefaulBtnClick(){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                canceltDefaultCard();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确定要取消默认结算卡?");
    }
    /**
     * 卡解绑
     */
    private void unBindCard() {
        qtpayCardIdx.setValue(bankCardInfo.getCardIdx());
        qtpayApplication.setValue("BankCardUnBind.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardIdx);
        httpsPost("BankCardUnBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(MyBindCardItemActivity.this, payResult.getRespDesc());
                finish();
            }
        });
    }

    /**
     * 取消默认银行卡
     */
    private void canceltDefaultCard() {
        qtpayCardIdx.setValue(bankCardInfo.getCardIdx());
        qtpayApplication.setValue("BankCardSetDefault.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add( new Param("bindType", "0"));
        httpsPost("BankCardSetDefaultTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(MyBindCardItemActivity.this, payResult.getRespDesc());
//                bt_setdefaultcard.setVisibility(View.VISIBLE);
//                bt_cancledefaultcard.setVisibility(View.GONE);
                finish();
            }
        });
    }
    /**
     * 设置为默认银行卡
     */
    private void setDefaultCard() {
        qtpayCardIdx.setValue(bankCardInfo.getCardIdx());
        qtpayApplication.setValue("BankCardSetDefault.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add( new Param("bindType", "1"));
        httpsPost("BankCardUnBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(MyBindCardItemActivity.this, payResult.getRespDesc());
//                bt_setdefaultcard.setVisibility(View.GONE);
//                bt_cancledefaultcard.setVisibility(View.VISIBLE);
                finish();
            }
        });
    }
    /**
     * 初始化listView的高度
     */
    public void innitListViewHeight(){
        try {
            int totalHeight = 0;
            for (int i = 0; i < myBindCardItemActAdapter.getCount(); i++) {
                View listItem = myBindCardItemActAdapter.getView(i, null, mybindcardlistview);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mybindcardlistview.getLayoutParams();
            params.height = totalHeight + (mybindcardlistview.getDividerHeight() * (mybindcardlistview.getCount()-1));
            ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
            mybindcardlistview.setLayoutParams(params);
        }catch (Exception e){

        }

    }
}
