package com.ryx.payment.ruishua.convenience;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.adapter.ImPayCouponsAdapter;
import com.ryx.payment.ruishua.adapter.UnionQrcodeShowPayTypAdapter;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.GlideUtils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.kt.QRcodeUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * 二维码收款二维码展示页面
 */
@EActivity(R.layout.activity_union_qrcode_pay_show)
public class UnionQrcodePayShowActivity extends BaseActivity {
    @ViewById(R.id.qrcodeallaout)
    AutoLinearLayout qrcodeallaout;
    @ViewById
    ImageView qrcodeimg,imgview;
    //展示订单页面
    private int MYORDERFLAG=0x908;
    private int selectedId = 0;//上次选中的值
    ArrayList<Map<String, String>> payTypesMap = new ArrayList<Map<String, String>>();

    String slectedval;//选择的支付类型名
    String slectedName;//选择的支付类型名
    String coupon_available;//优惠券是否不可用0，可用1
    String coupon_paytype;//优惠券类型
    String amount,cardIdx,payType,couponBindId;
    @ViewById
    TextView tv_unionqrcode_payshow,tv_qrcodedesc,selectedcoupons_tv;
    private ArrayList<JSONObject> couponsList=new ArrayList<JSONObject>();
    @ViewById
    AutoRelativeLayout relayout_unionqrcode_payshow,coupons_layout;
    private String selectedbindId;
    private String selectedcouponName;
    String qrcodeurl;
    @AfterViews
    public void initView(){
        setTitleLayout("二维码收款",true,false);
        initQtPatParams();
        Intent intent=getIntent();
      String typeJsonStr=  intent.getStringExtra("typeJsonStr");
       amount=  intent.getStringExtra("amount");
        cardIdx=  intent.getStringExtra("cardIdx");
        payType=  intent.getStringExtra("payType");

        if(!TextUtils.isEmpty(typeJsonStr)){
            analyzeMerchantInfoJson(typeJsonStr);
            restView(selectedId);
            qrPaySetOrder(new CompleteResultListen() {
                @Override
                public void compleResultok() {
                    if("1".equals(coupon_available)){
                        //当前类型二维码可以使用优惠券
                        restcouponsView(-1);
                    }else {
                        coupons_layout.setVisibility(View.GONE);
                        selectedbindId=null;
                        couponBindId=null;
                    }
                }
            });
        }else{
            LogUtil.showToast(UnionQrcodePayShowActivity.this,"二维码类型有误!");
        }
    }
    @Override
    public void  backUpImgOnclickListen(){
        setResult(MYORDERFLAG);
        super.backUpImgOnclickListen();
    }

    @Click(R.id.relayout_unionqrcode_payshow)
    public void relayout_unionqrcode_payshow(View view){
        showBottomSheet(view);
    }
    @LongClick(R.id.qrcodeimg)
    public void qrcodeimgLongClick(){
        showLongClickBottomDialog();
    }

    private void showLongClickBottomDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(UnionQrcodePayShowActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(UnionQrcodePayShowActivity.this).inflate(R.layout.dialog__bottom_qrcodelongclick, null);
        android.widget.Button btn_saveimgBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_saveimg);
        android.widget.Button btn_identifysqrcodeBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_identifysqrcode);
       int kjzfTouchPayTag= RyxAppdata.getInstance(UnionQrcodePayShowActivity.this).getkjzfTouchPayTag();
        if(kjzfTouchPayTag==1&&"RYXPAY".equals(slectedval)){
            btn_identifysqrcodeBtn.setVisibility(View.VISIBLE);
        }else {
            btn_identifysqrcodeBtn.setVisibility(View.GONE);
        }
        android.widget.Button cancelBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_cancel);
        btn_saveimgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                dirpermissionCheck();
            }
        });
        btn_identifysqrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                saomaSMZF003(qrcodeurl);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.contentView(boottomView).show();
    }


    /**
     * 拿着二维码信息进行判断是否合法，接下来流程步骤
     * @param url
     */
    private  void saomaSMZF003(final String url){
        if(TextUtils.isEmpty(url)){
            LogUtil.showToast(UnionQrcodePayShowActivity.this,"二维码信息有误,请重试!");
            return;
        }
        qtpayApplication.setValue("SaomaSMZF003.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("code",url));
        qtpayParameterList.add(new Param("payType","fukuan"));
        httpsPost("SaomaSMZF003Tag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    JSONObject jsonObject=new JSONObject(payResult.getData());
                    String code=   JsonUtil.getValueFromJSONObject(jsonObject,"code");
                    if("7766".equals(code)){
                        //ryx支付
                        qtpayApplication.setValue("RYXPAYGetOrderInfo.Req");
                        qtpayAttributeList.add(qtpayApplication);
                        qtpayParameterList.add(new Param("orderId",url));
                        httpsPost("RYXPAYGetOrderInfoTag", new XmlCallback() {
                            @Override
                            public void onTradeSuccess(RyxPayResult payResult) {
//       {"result":{"amount":"1000","skaccount":"4","qrcode":"1000000010775591","payee":"A000115786","skphone":"1329****613","skusername":"*长国"},"code":"0000","msg":"收款"}
                                String jsondata= payResult.getData();
                                try {
                                    JSONObject jsondataObj=new JSONObject(jsondata);
                                    String code= JsonUtil.getValueFromJSONObject(jsondataObj,"code");
                                    if(RyxAppconfig.QTNET_SUCCESS.equals(code)){
                                        JSONObject jsonObject1=JsonUtil.getJSONObjectFromJsonObject(jsondataObj,"result");

                                        String payee=JsonUtil.getValueFromJSONObject(jsonObject1,"payee");
                                        String re_orderid=JsonUtil.getValueFromJSONObject(jsonObject1,"re_orderid");
                                        String skusername=JsonUtil.getValueFromJSONObject(jsonObject1,"skusername");
                                        String skaccount=JsonUtil.getValueFromJSONObject(jsonObject1,"skaccount");
                                        String skphone=JsonUtil.getValueFromJSONObject(jsonObject1,"skphone");
                                        String qrcode=JsonUtil.getValueFromJSONObject(jsonObject1,"qrcode");
                                        String amount=JsonUtil.getValueFromJSONObject(jsonObject1,"amount");

                                        Intent intent=new Intent(UnionQrcodePayShowActivity.this,SweepQuickPayActivity_.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("payee",payee);
                                        bundle.putString("username",skusername);
                                        bundle.putString("qrcodeskaccount",skaccount);
                                        bundle.putString("mobileno",skphone);
                                        bundle.putString("qrcode",qrcode);
                                        bundle.putString("ordermoney",amount);
                                        bundle.putString("re_orderid",re_orderid);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        final String msg=   JsonUtil.getValueFromJSONObject(jsondataObj,"msg");
                                        if(!TextUtils.isEmpty(msg)){
                                            LogUtil.showToast(UnionQrcodePayShowActivity.this,msg);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showMsgDialog(msg);
                                                }
                                            });
                                            return;
                                        }
                                    }
                                } catch (Exception e) {
                                    final String msg="交易结果异常,请查看交易记录!"+e.getMessage();
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showMsgDialog(msg);
                                        }
                                    });
                                }
//
                            }
                        });
                    } else{
                        final String msg=   JsonUtil.getValueFromJSONObject(jsonObject,"msg");
                        if(!TextUtils.isEmpty(msg)){
                            //支付成功同步结果code为0000，还有其他情况都只是仅展示信息框
                            LogUtil.showToast(UnionQrcodePayShowActivity.this,msg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showMsgDialog(msg);
                                }
                            });

                            return;
                        }
                    }
                } catch (Exception e) {
                    //其他异常情况，仅展示信息框
                    e.printStackTrace();
                    LogUtil.showLog(e.getMessage());
                    final String msg="交易结果异常,请查看交易记录!"+e.getMessage();
                    LogUtil.showToast(UnionQrcodePayShowActivity.this,msg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMsgDialog(msg);
                        }
                    });
                }
            }
            @Override
            public void onOtherState(String rescode, final String resDesc) {
                super.onOtherState(rescode,resDesc);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsgDialog(resDesc);
                    }
                });
            }
        });


    }

    /**
     * 展示提示信息退出对话框
     * @param message
     */
    public void showMsgDialog(String message){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(UnionQrcodePayShowActivity.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {}
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(message);
        ryxSimpleConfirmDialog.setCancelable(false);
        ryxSimpleConfirmDialog.setOkbtnText("确定");
        ryxSimpleConfirmDialog.setContentgravity(Gravity.CENTER);
        ryxSimpleConfirmDialog.setOnlyokLinearlayout();
    }
    /**
     * 我的代金券
     */
    private void getVouchersData(final CompleteResultListen completeResultListen) {
        qtpayApplication.setValue("QueryAvailableCoupon.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("QueryAvailableCouponTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                couponsList.clear();
                String data = payResult.getData();
                if (TextUtils.isEmpty(data)) {
                    LogUtil.showToast(UnionQrcodePayShowActivity.this,"无可用优惠券");
                    return;
                }
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
                try {
                    JSONArray datajsonArray = new JSONArray(data);
                    if (datajsonArray == null || datajsonArray.length() == 0) {
                        LogUtil.showToast(UnionQrcodePayShowActivity.this,"无可用优惠券");
                        return;
                    }
                    for (int i = 0; i < datajsonArray.length(); i++) {
                        JSONObject acitivityObject = datajsonArray.getJSONObject(i);
                        couponsList.add(acitivityObject);
//                        JSONArray transtypeArray= acitivityObject.getJSONArray("transtype");
//                        if(transtypeArray.toString().contains(coupon_paytype)){
//                            couponsList.add(acitivityObject);
//                        };
                    }
                    if(completeResultListen!=null){
                        completeResultListen.compleResultok();
                    }
                } catch (Exception e) {
                    LogUtil.showLog("eeeeeeee"+e.getLocalizedMessage());
                }
            }
        });
    }
    /**
     * 设置优惠券
     *
     * @param i
     */
    private void restcouponsView(int i) {
        if(i<0)
        {
            coupons_layout.setVisibility(View.VISIBLE);
            selectedbindId=null;
            couponBindId=null;
            selectedcoupons_tv.setText("请选择优惠劵");
            return;
        }
        try {
            ArrayList<JSONObject> myCouponList= QRcodeUtil.Factory.filterData(coupon_paytype,couponsList);
            if(myCouponList==null||myCouponList.size()==0){
                couponBindId=null;
                coupons_layout.setVisibility(View.GONE);
                return;
            }
            LogUtil.showLog(couponsList.toString());
            coupons_layout.setVisibility(View.VISIBLE);
            JSONObject acitivityObject = myCouponList.get(i);
            selectedbindId = JsonUtil.getValueFromJSONObject(acitivityObject, "bindid");
            String coupontype = JsonUtil.getValueFromJSONObject(acitivityObject, "coupontype");
            String couponName = JsonUtil.getValueFromJSONObject(acitivityObject, "couponname");
            String couponvalue = JsonUtil.getValueFromJSONObject(acitivityObject, "couponvalue");
            couponBindId = JsonUtil.getValueFromJSONObject(acitivityObject, "bindid");
            if ("01".equals(coupontype)) {
                selectedcouponName = couponvalue + "元" + couponName;
            } else if ("02".equals(coupontype)) {
                selectedcouponName = Double.parseDouble(couponvalue) / 10.00 + "折  " + couponName;
            }
            selectedcoupons_tv.setText(selectedcouponName);
            qrPaySetOrder(null);
        } catch (Exception e) {
            coupons_layout.setVisibility(View.GONE);
        }

    }
    /**
     * 优惠券列表
     */
    @Click(R.id.coupons_layout)
    public void couponsLayoutClick(final View view) {
        getVouchersData(new CompleteResultListen() {
            @Override
            public void compleResultok() {
                ArrayList<JSONObject> myCouponList= QRcodeUtil.Factory.filterData(coupon_paytype,couponsList);
                if(myCouponList.size()==0){
                    LogUtil.showToast(UnionQrcodePayShowActivity.this,"您当前二维码类型下暂无优惠券");
                }else{
                    showcouponsLayout(view);
                }
            }
        });

    }
    /**
     * 展示优惠券视图
     */
    private void showcouponsLayout(View view) {
        disabledTimerView(view);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(UnionQrcodePayShowActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(UnionQrcodePayShowActivity.this).inflate(R.layout.impay_coupons_bootomsheet, null);
        final RecyclerView boottomRecyclerView = (RecyclerView) boottomView.findViewById(R.id.coupons_recyclerView);
        RecyclerViewHelper.init().setRVGridLayout(this, boottomRecyclerView, 1);//1列
        //// TODO: 17/4/19 根据选择T0,T1传递类型
        final ImPayCouponsAdapter couponsAdapter = new ImPayCouponsAdapter(QRcodeUtil.Factory.filterData(coupon_paytype,couponsList), UnionQrcodePayShowActivity.this, selectedbindId, R.layout.impay_coupons_item);
        couponsAdapter.setCurrentMoney(Double.parseDouble(amount)/100.00);
        boottomRecyclerView.setAdapter(couponsAdapter);
        Button couponsClosebtn = (Button) boottomView.findViewById(R.id.coupons_closebtn);
        TextView cancelTv = (TextView) boottomView.findViewById(R.id.cancel_tv);
        couponsClosebtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        cancelTv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                restcouponsView(-1);
                qrPaySetOrder(null);
                mBottomSheetDialog.dismiss();
            }
        });
        couponsAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                restcouponsView(position);
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.contentView(boottomView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetDialog.show();
                    }
                });

            }
        }).start();
    }
    /**
     * 重新绘制选中的二维码类型展示
     */
    public void restView(int position){
        tv_unionqrcode_payshow.setText(payTypesMap.get(position).get("merchantName"));
        GlideUtils.getInstance().load(UnionQrcodePayShowActivity.this,payTypesMap.get(position).get("icon"),imgview);
      String bean_available_desc=  payTypesMap.get(position).get("bean_available_desc");
        tv_qrcodedesc.setText(TextUtils.isEmpty(bean_available_desc)?"":bean_available_desc);
    }
    /**
     * 解析支付类型JSON
     */
    private void analyzeMerchantInfoJson(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                JSONArray resultBeanArray = jsonObj.getJSONArray("result");
                String code = JsonUtil.getValueFromJSONObject(jsonObj, "code");
                String msg = JsonUtil.getValueFromJSONObject(jsonObj, "msg");
                if (!RyxAppconfig.QTNET_SUCCESS.equals(code)) {
                    LogUtil.showToast(UnionQrcodePayShowActivity.this, msg + "");
                    return;
                }
                for (int i = 0; i < resultBeanArray.length(); i++) {
                    JSONObject jsonobject = (JSONObject) resultBeanArray.get(i);
                    String val = JsonUtil.getValueFromJSONObject(jsonobject, "val");
                    String name = JsonUtil.getValueFromJSONObject(jsonobject, "name");
                    String icon = JsonUtil.getValueFromJSONObject(jsonobject, "icon");
                    String coupon_paytype = JsonUtil.getValueFromJSONObject(jsonobject, "coupon_paytype");
                    String coupon_available = JsonUtil.getValueFromJSONObject(jsonobject, "coupon_available");

                    String bean_available_desc = JsonUtil.getValueFromJSONObject(jsonobject, "bean_available_desc");

                    Map<String, String> paytyp2 = new HashMap<>();
                    paytyp2.put("val", val);
                    paytyp2.put("merchantName", name);
                    paytyp2.put("icon", icon);
                    paytyp2.put("bean_available_desc", bean_available_desc);
                    paytyp2.put("coupon_paytype", coupon_paytype);
                    paytyp2.put("coupon_available", coupon_available);

                    String mobileNo = RyxAppdata.getInstance(this).getMobileNo();
                    String productFlag = PreferenceUtil.getInstance(this).
                            getString(mobileNo + "_unionqrshowcode", "");//取值
                    if (!TextUtils.isEmpty(productFlag) && productFlag.equals(val)) {
                        selectedId = i;
                    }
                    LogUtil.showLog("productFlag=="+productFlag+"selectedId="+selectedId);
                    payTypesMap.add(paytyp2);
                }
                if (payTypesMap.isEmpty()) {
                    relayout_unionqrcode_payshow.setVisibility(View.GONE);
                    return;
                }
                slectedval = payTypesMap.get(selectedId).get("val");
                slectedName = payTypesMap.get(selectedId).get("merchantName");
                coupon_paytype = payTypesMap.get(selectedId).get("coupon_paytype");
                coupon_available = payTypesMap.get(selectedId).get("coupon_available");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.showToast(UnionQrcodePayShowActivity.this, "数据解析异常!");
        }
    }

    /**
     * 二维码类型选择
     * @param position
     */
    public void mobileClick(int position) {
        selectedId=position;
        slectedval = payTypesMap.get(position).get("val");
        slectedName = payTypesMap.get(position).get("merchantName");
        coupon_paytype = payTypesMap.get(position).get("coupon_paytype");
        coupon_available = payTypesMap.get(position).get("coupon_available");
        restView(position);
        if("1".equals(coupon_available)){
            restcouponsView(-1);
        }else {
            coupons_layout.setVisibility(View.GONE);
            selectedbindId=null;
            couponBindId=null;
        }
        qrPaySetOrder(null);
    }

    /**
     *生产订单二维码
     */
    private void qrPaySetOrder(final CompleteResultListen completeResultListen){
        if(TextUtils.isEmpty(amount)||TextUtils.isEmpty(cardIdx)||TextUtils.isEmpty(slectedval)){
            return;
        }
        qtpayApplication.setValue("QRPaySetOrder.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("amount", amount));
        qtpayParameterList.add(new Param("cardIdx", cardIdx));
        qtpayParameterList.add(new Param("payType", slectedval));
        if(!TextUtils.isEmpty(couponBindId)){
            qtpayParameterList.add(new Param("couponBindId", couponBindId));
        }
        httpsPost("QRPaySetOrderTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if(completeResultListen!=null){
                    completeResultListen.compleResultok();
                }
                //交易类型存本地
                PreferenceUtil.getInstance(UnionQrcodePayShowActivity.this).saveString(
                        RyxAppdata.getInstance(UnionQrcodePayShowActivity.this).getMobileNo() + "_unionqrshowcode", slectedval);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(payResult.getData());
                    if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObject.getString("code"))) {
                        qrcodeurl=jsonObject.getJSONObject("result").getString("returl");
                        createQrcodeImg(qrcodeurl,qrcodeimg);
                    } else {
                        String msg=jsonObject.getJSONObject("result").getString("message");
                        tv_qrcodedesc.setText(msg+"");
                        LogUtil.showToast(UnionQrcodePayShowActivity.this,msg);
                        qrcodeimg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.qrcodefail));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onOtherState(String rescode, String resDesc) {
                super.onOtherState(rescode, resDesc);
                tv_qrcodedesc.setText(resDesc+"");
                if("1".equals(coupon_available)){
                    restcouponsView(-1);
                }
                qrcodeimg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.qrcodefail));
            }
        });
    }
    /**
     * 根据内容生成二维码
     * @param content 二维码内容
     * @param qrcodeimg 展现二维码的Img
     */
    public void createQrcodeImg(final String content, final ImageView qrcodeimg){
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                //解码资源ID引用的图像。
//                Bitmap logoBitMap= BitmapFactory.decodeResource(getResources(), getCurrentBranchMipmapLogoId());
//                Bitmap qrcodeBitMap=  QRCodeEncoder.syncEncodeQRCode(content,600, Color.BLACK, Color.WHITE,logoBitMap);
                Bitmap qrcodeBitMap=  QRCodeEncoder.syncEncodeQRCode(content,600, Color.BLACK, Color.WHITE,null);
                return qrcodeBitMap;
            }
            @Override
            protected void onPostExecute(Bitmap qrcodeBitMap) {
                try {
                    qrcodeimg.setImageBitmap(qrcodeBitMap);
                }catch (Exception e){
                    qrcodeimg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.qrcodefail));
                }
            }
        }.execute();
    }
    /**
     *
     * 文件操作权限判断
     */
    public void dirpermissionCheck(){
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0021, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        StringBuffer stringBuffer=new StringBuffer();
                        stringBuffer.append(RyxAppdata.getInstance(UnionQrcodePayShowActivity.this).getCurrentFileBranchRootName());
                        stringBuffer.append("PayQrCode");
                        //确定
                        //保存二维码图片到手机qrcodeallaout
                        Map<String,String> ssMap = BitmapUntils.saveQrcodeAsFile(qrcodeallaout,
                                stringBuffer.toString());
                        if(TextUtils.isEmpty(ssMap.get("path"))||TextUtils.isEmpty(ssMap.get("fileName"))){
                            LogUtil.showToast(UnionQrcodePayShowActivity.this,  ssMap.get("result"));
                            return;
                        }
                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(UnionQrcodePayShowActivity.this.getContentResolver(),
                                    ssMap.get("path") + File.separator + ssMap.get("fileName")
                                            + ".jpg", ssMap.get("fileName"), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        UnionQrcodePayShowActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + ssMap.get("path") + File.separator + ssMap.get("fileName")
                                + ".jpg")));
                        LogUtil.showToast(UnionQrcodePayShowActivity.this, ssMap.get("result"));
                    }
                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(UnionQrcodePayShowActivity.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void showBottomSheet(View view) {
        disabledTimerView(view);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(UnionQrcodePayShowActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(UnionQrcodePayShowActivity.this).inflate(R.layout.unionpay_paytype_bottomsheet, null);
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.paytype_bottomListViewid);
        UnionQrcodeShowPayTypAdapter unionQrcodeShowPayTypAdapter=new UnionQrcodeShowPayTypAdapter(UnionQrcodePayShowActivity.this,payTypesMap,slectedval);
        boottomListView.setAdapter(unionQrcodeShowPayTypAdapter);
        ImageView imageView = (ImageView) boottomView.findViewById(R.id.imgview_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        boottomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mobileClick(position);
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.contentView(boottomView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetDialog.show();
                    }
                });

            }
        }).start();
    }

}
