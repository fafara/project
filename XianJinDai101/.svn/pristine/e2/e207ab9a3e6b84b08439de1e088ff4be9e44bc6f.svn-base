package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.payment.ruishua.view.CreateOrderView;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_create_order)
public class CreateOrder extends BaseActivity {
    @ViewById
    CreateOrderView createorder;
    OrderInfo orderinfo;
    Intent intent;
    Param qtpayMerchantId;
    Param qtpayProductId;
    Param qtpayOrderAmt;
    Param qtpayPayTool;
    Param qtpayOrderDesc;
    Param qtpayOrderRemark;
@AfterViews
public void initView(){
    setTitleLayout("确认订单",true,false);
    bandata();
    initQtPatParams();
    doOrderRequest();
}

    /**
     * 生成订单
     */
    private void doOrderRequest() {
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayMerchantId);
        qtpayParameterList.add(qtpayProductId);
        qtpayParameterList.add(qtpayOrderAmt);
        qtpayParameterList.add(qtpayPayTool);
        qtpayParameterList.add(qtpayOrderDesc);
        qtpayParameterList.add(qtpayOrderRemark);
        if(orderinfo.isUseRuiBean()){
            qtpayParameterList.add(new Param("beansType","1"));
        }
        if(!TextUtils.isEmpty(orderinfo.getPayee())){
            qtpayParameterList.add(new Param("payee",orderinfo.getPayee()));
        }
        if(!TextUtils.isEmpty(orderinfo.getCouponBindId())){
            qtpayParameterList.add(new Param("couponBindId",orderinfo.getCouponBindId()));
        }
        httpsPost("RequestOrderTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

                orderinfo.setRealAmt(payResult.getRealAmt());
                orderinfo.setTransLogNo(payResult.getTransLogNo());
                orderinfo.setOrderId(payResult.getOrderId());
                orderinfo.setOrderAmt(payResult.getOrderAmt());

                showOrderInfo();
                createorder.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoginAnomaly() {
                    finish();
            }

            @Override
            public void onOtherState() {
                finish();
            }

            @Override
            public void onTradeFailed() {
                finish();
            }
        });


    }

    /**
     * 动态展示订单布局
     */
    private void showOrderInfo() {
        String realname = RyxAppdata.getInstance(CreateOrder.this)
                .getRealName();
        if (realname.equals("")) {
            realname = RyxAppdata.getInstance(CreateOrder.this).getMobileNo();
        }
        switch (orderinfo.getId()) {
            case RyxAppconfig.PHONE_RECHARGE: // 手机充值
                createorder.setExplanation("订单号", "支付说明", "充值手机号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderDesc(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;

            case RyxAppconfig.CREDITCARD_REPAYMENT: // 信用卡还款
                createorder.setExplanation("订单号", "支付说明", "还款信用卡卡号", "支付账户");
                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderDesc(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                // orderinfo.setOrderRemark(kainfo.getName()+","+kainfo.getRepaydate());
                // orderinfo.setOrderRemark(kainfo.getName()+","+kainfo.getCityid()+","+kainfo.getBankid()+","+kainfo.getSubBranchid()+","+kainfo.getRepaydate());
                // 姓名, 城市编号,银行编号,支行编号,还款日|(只填姓名和还款日)
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;
            case RyxAppconfig.FACE_OF_RECEIVABLES: // 当面收款
                createorder.setExplanation("订单号", "付款说明", "商户名称", "收款账户");
                // 5个参数分别为 ：付款金额，"订单号","收款说明","商户名称","收款账户"
                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(),
                        StringUnit.lengthLimit(8, orderinfo.getOrderRemark()),
                        realname,
                        // orderinfo.get.getMerchantId(),
                        // orderinfo.getProductId()
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());

                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;
            case RyxAppconfig.TRANSFER_REPAYMENT: // 转帐还款
                createorder.setTransferExplanation();
                createorder.setTransferValue(MoneyEncoder.decodeFormat(orderinfo
                                .getRealAmt()), orderinfo.getOrderId(), orderinfo
                                .getOrdertype(), orderinfo.getOrderRemark().split(",")[0],
                        orderinfo.getOrderDesc(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());

                intent = new Intent(CreateOrder.this, Swiper_.class);

                // orderinfo.setOrderDesc(kainfo.getKanum()); // 添加要转账的卡号 前面已经设了值
                // orderinfo.setOrderRemark(cardinfo.getName()+","+cardinfo.getBankCityId()+","+cardinfo.getBankId()+","+cardinfo.getBranchBankId());
                // 姓名,城市编号,银行编号,支行编号,备注|

                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;

//            case RyxAppconfig.COMMON_WITHDRAW: // 手机号普通提款
//            case RyxAppconfig.QUICK_WITHDRAW: // 手机号快速提款
//                createorder.setExplanation("订单号", "支付说明", "提款到银行卡", "收款账户");
//                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
//                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
//                        orderinfo.getOrderDesc(),
//                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo(),
//                        "确认提款");
//                intent = new Intent(CreateOrder.this, WithdrawEnterPassword.class);
//                createorder.setIntent(intent);
//                break;

            case RyxAppconfig.Q_COIN_RECHARGE: // q币充值
                createorder.setExplanation("订单号", "支付说明", "充值QQ帐号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;


            case RyxAppconfig.JD_RECHARGE: // 京东卡充值
                createorder.setExplanation("订单号", "支付说明", "充值手机号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;

            case RyxAppconfig.NO1_RECHARGE: // 1号店充值
                createorder.setExplanation("订单号", "支付说明", "充值手机号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;
            case RyxAppconfig.YMX_RECHARGE: // 亚马逊充值
                createorder.setExplanation("订单号", "支付说明", "充值手机号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;
            case RyxAppconfig.SUNING_RECHARGE: // 苏宁充值
                createorder.setExplanation("订单号", "支付说明", "充值手机号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;




            case RyxAppconfig.GOODS_RECEIVABLES:		// 商品收款
                createorder.setExplanation("订单号","商品说明","收款账户","支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(),
                        StringUnit.lengthLimit(8, orderinfo.getOrderRemark()),
                        orderinfo.getOrderDesc(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;





//            case RyxAppconfig.MOBILE_ACCOUNTPAY: // 手机号付款 账户付款
//                createorder.setExplanation("订单号", "付款说明", "收款人", "收款账户");
//                // 5个参数分别为 ：付款金额，"订单号","收款说明","商户名称","收款账户"
//                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
//                        orderinfo.getOrderId(),
//                        StringUnit.lengthLimit(8, orderinfo.getOrderRemark()),
//                        orderinfo.getAccount2(),
//                        // orderinfo.get.getMerchantId(),
//                        // orderinfo.getProductId()
//                        orderinfo.getOrderDesc());
//
//                intent = new Intent(CreateOrder.this, WithdrawEnterPassword.class);
//                intent.putExtra("orderinfo", orderinfo);
//                intent.putExtra("PassType", "pay");
//                createorder.setIntent(intent);
//                break;

            case RyxAppconfig.IMPAY_MUCH: // 4种付款方式 个人付款
            case RyxAppconfig.IMPAY_SUPER:
            case RyxAppconfig.IMPAY_FREE:
            case RyxAppconfig.IMPAY_LITTLE:
            case RyxAppconfig.IMPAY_SHANFU:
            case RyxAppconfig.IMPAY_CREDITPAY:
                if(!TextUtils.isEmpty(orderinfo.getCouponBindId())){
                    createorder.setExplanation("订单号","收款说明","收款账户","付款账户","优惠券","");
                    //  5个参数分别为  ：付款金额，"订单号","收款说明","商户名称","收款账户"
                    createorder.setBindDisPalyValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                            orderinfo.getOrderId(),
                            StringUnit.lengthLimit(8, orderinfo.getOrderRemark()),
                            TextUtils.isEmpty(orderinfo.getDisPlayContent())? StringUnit.cardJiaMi(orderinfo.getOrderDesc()):orderinfo.getDisPlayContent(),
                            RyxAppdata.getInstance(CreateOrder.this).getMobileNo(),orderinfo.getCouponBindDisPaly());
                }else{
                    createorder.setExplanation("订单号","收款说明","收款账户","付款账户");
                    //  5个参数分别为  ：付款金额，"订单号","收款说明","商户名称","收款账户"
                    createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                            orderinfo.getOrderId(),
                            StringUnit.lengthLimit(8, orderinfo.getOrderRemark()),
                            TextUtils.isEmpty(orderinfo.getDisPlayContent())? StringUnit.cardJiaMi(orderinfo.getOrderDesc()):orderinfo.getDisPlayContent(),
                            RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                }

                intent = new Intent(CreateOrder.this, Swiper_.class);
//                intent = new Intent(CreateOrder.this, SwipingCardActivity_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;


            case RyxAppconfig.WM_RECHARGE:   //完美充值
                createorder.setExplanation("订单号", "支付说明", "充值帐号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);


                break;
            case RyxAppconfig.SJTC_RECHARGE:  //世纪天成
                createorder.setExplanation("订单号", "支付说明", "充值帐号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);

                break;
            case RyxAppconfig.SD_RECHARGE:  //盛大
                createorder.setExplanation("订单号", "支付说明", "充值帐号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);


                break;
            case RyxAppconfig.GY_RECHARGE:  //光宇
                createorder.setExplanation("订单号", "支付说明", "充值帐号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);

                break;
            case RyxAppconfig.BK_RECHARGE:  //波克
                createorder.setExplanation("订单号", "支付说明", "充值帐号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;
            case RyxAppconfig.REFUEL_RECHARGE:  //
                createorder.setExplanation("订单号", "支付说明", "充值卡号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;

//            case RyxAppconfig.REDENVELOPE_NORMAL:  //普通红包
//                createorder.setExplanation("订单号", "支付说明", "收款人", "支付账户");
//                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
//                        orderinfo.getOrderId(),
//                        orderinfo.getOrdertype(),
//                        orderinfo.getOrderDesc(),
//                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
//                intent = new Intent(CreateOrder.this, RedEnvelope_Scene_Pay.class);
//                intent.putExtra("orderinfo", orderinfo);
//                intent.putExtra("sourceType", "00");
//                intent.putExtra("greets", (String)getIntent().getExtras().get("greets"));
//                createorder.setIntent(intent);
//                break;

//            case RyxAppconfig.REDENVELOPE_LUCKY:  //拼手气红包
//                createorder.setExplanation("订单号", "支付说明", "红包数量", "支付账户");
//                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
//                        orderinfo.getOrderId(),
//                        orderinfo.getOrdertype(),
//                        (String)getIntent().getExtras().get("packageCount"),
//                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
//                intent = new Intent(CreateOrder.this, RedEnvelope_Scene_Pay.class);
//                intent.putExtra("orderinfo", orderinfo);
//                intent.putExtra("sourceType", "01");
//                intent.putExtra("greets", (String)getIntent().getExtras().get("greets"));
//                createorder.setIntent(intent);
//                break;
            case RyxAppconfig.REDENVELOPE_RECHARGE:  //红包充值
                createorder.setExplanation("订单号", "支付说明", "收款人", "支付账户");
                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(),
                        orderinfo.getOrdertype(),
                        (String)getIntent().getExtras().get("realName"),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;

            case RyxAppconfig.ALIPAY_RECHARGE:  //
                createorder.setExplanation("订单号", "支付说明", "充值手机号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());
                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);


                break;

//            case RyxAppconfig.MEMBER_RECHARGE: //
//                createorder.payType = orderinfo.getPaytype();
//                createorder.setExplanation("订单号", "支付说明", "支付方式", "");
//
//                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
//                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
//                        createorder.payType == 0 ? "账户支付" : "刷卡支付");
//                intent = new Intent(CreateOrder.this, Swiper_.class);
//                intent.putExtra("orderinfo", orderinfo);
//                intent.putExtra("PayForSenior", payFortype);
//                createorder.setIntent(intent);
//
//                break;

            case RyxAppconfig.FLOW_RECHARGE:  //
                createorder.setExplanation("订单号", "支付说明", "充值手机号", "支付账户");

                createorder.setValue(MoneyEncoder.decodeFormat(orderinfo.getRealAmt()),
                        orderinfo.getOrderId(), orderinfo.getOrdertype(),
                        orderinfo.getOrderRemark(),
                        RyxAppdata.getInstance(CreateOrder.this).getMobileNo());

                intent = new Intent(CreateOrder.this, Swiper_.class);
                intent.putExtra("orderinfo", orderinfo);
                createorder.setIntent(intent);
                break;

        }




    }

    private  void bandata(){
        createorder.setVisibility(View.GONE);
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            if(bundle.get("orderinfo")!=null){
                orderinfo = (OrderInfo) bundle.get("orderinfo");
            }else if(!TextUtils.isEmpty(bundle.getString("orderStr"))){
              String orderJsonStr =bundle.getString("orderStr");
                LogUtil.showLog("接收到小贷数据==="+orderJsonStr);
                try{
                Gson gson=new Gson();
                orderinfo= gson.fromJson(orderJsonStr,OrderInfo.class);
                }catch(Exception e){
                    e.printStackTrace();
                }
                LogUtil.showLog("接收到小贷数据==转换后="+orderinfo.toString());
            }
        }
    }

    @Override
  public void  initQtPatParams(){
        super.initQtPatParams();
        qtpayApplication = new Param("application", "RequestOrder.Req");
        qtpayMerchantId = new Param("merchantId", orderinfo.getMerchantId()); // 商品编码
        qtpayProductId = new Param("productId", orderinfo.getProductId());
        qtpayOrderAmt = new Param("orderAmt", MoneyEncoder.encodeToPost(orderinfo.getOrderAmt()));
        if (orderinfo.getId() == RyxAppconfig.MOBILE_ACCOUNTPAY) {
            qtpayPayTool = new Param("payTool", "02"); // O 支付方式 02-电子钱包账户
        } else {
            qtpayPayTool = new Param("payTool", "01"); // O 支付方式 01—刷卡支付
        }
        qtpayOrderDesc = new Param("orderDesc", orderinfo.getOrderDesc()); // 订单信息
        qtpayOrderRemark = new Param("orderRemark", orderinfo.getOrderRemark()); // 订单备注信息
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RyxAppconfig.CLOSE_SWIPER_SHOWIMPAY == resultCode) {
            finish();
        }
    }
}
