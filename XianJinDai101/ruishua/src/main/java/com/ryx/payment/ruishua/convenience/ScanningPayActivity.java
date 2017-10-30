package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BankLisAdapter;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 扫码收款
 */
@EActivity(R.layout.activity_scanning_pay)
public class ScanningPayActivity extends BaseActivity {
    OrderInfo orderinfo;
    private String httpsTag = "ScanningPayActivityHttps";
    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    @ViewById
    AutoRelativeLayout im_pay_selectbankid;
    @ViewById
    TextView impay_username, impay_bankname, impay_accountno, im_pay_poundagetv, drawingInfo_tv;
    @ViewById
    TextView impayphonetv;
    @ViewById
    ImageView impay_bankimg;
    String accountNo="", realName,cardIdx = "";
    @ViewById
    EditText impay_paymoney;
    @ViewById
    Button im_pay_nextbtn;
    @ViewById
    View im_pay_poundagetvline, banklistviewLine;
    @ViewById
    AutoLinearLayout im_pay_poundagetvLayout;
    private boolean isneendGetBanks;
    @AfterViews
    public void initView() {
        isneendGetBanks = false;
        setTitleLayout("二维码收款", true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_Impay);
        initQtPatParams();
        bindData();
    }

    private void bindData() {
        String mobileNo = QtpayAppData.getInstance(ScanningPayActivity.this).getMobileNo();
        if (TextUtils.isEmpty(mobileNo)) {
//            Intent intent = new Intent(ScanningPayActivity.this, LoginActivity_.class);
//            startActivityForResult(intent,RyxAppconfig.TOLOGINACT);
            toAgainLogin(ScanningPayActivity.this,RyxAppconfig.TOLOGINACT);
            finish();
            return;
        }
        impayphonetv.setText(mobileNo);
        ArrayList<BankCardInfo> bankList = (ArrayList<BankCardInfo>) getIntent().getSerializableExtra("bankcardlist");
        if (bankList != null && bankList.size() > 0) {
            banklistviewLine.setVisibility(View.VISIBLE);
            im_pay_selectbankid.setVisibility(View.VISIBLE);
            bankcardlist.addAll(bankList);
            boolean ishaveDefault=false;
            for (int i=0;i<bankList.size();i++){
                String flagInfo=  bankList.get(i).getFlagInfo();
                if("1".equals(flagInfo)){
                    resetBankListView(i);
                    ishaveDefault=true;
                    break;
                }
            }
            if(!ishaveDefault){
                resetBankListView(0);
            }
        }
        userInfo();
    }

    @Click(R.id.im_pay_selectbankid)
    public void showBottomSheet() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ScanningPayActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(ScanningPayActivity.this).inflate(R.layout.impay_bottomsheet, null);
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final BankLisAdapter bankLisAdapter = new BankLisAdapter(ScanningPayActivity.this, bankcardlist, accountNo);
        boottomListView.setAdapter(bankLisAdapter);
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
                resetBankListView(position);
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
                        mBottomSheetDialog .show();
                    }
                });

            }
        }).start();

    }

    @Click(R.id.im_pay_nextbtn)
    public void nextBtnCLick() {
        String moneyStr = MoneyEncoder.EncodeFormat(impay_paymoney.getText().toString());


        if (moneyStr.length() == 0) {
            LogUtil.showToast(ScanningPayActivity.this, "付款金额不能为空");
            return;
        }

        if (moneyStr.equals("￥0.00")) {
            LogUtil.showToast(ScanningPayActivity.this, "付款金额必须大于零");
            return;
        }

        if (moneyStr.replace(",", "").replace("￥", "")
                .length()
                - moneyStr.replace(",", "")
                .replace("￥", "").indexOf(".") > 3) {
            LogUtil.showToast(ScanningPayActivity.this, "付款金额单位过小");
            return;
        }
        if (moneyStr.replace(",", "").replace("￥", "")
                .length() > 9) {
            LogUtil.showToast(ScanningPayActivity.this, "付款金额超限");
            return;
        }
        if (TextUtils.isEmpty(accountNo)) {
            LogUtil.showToast(ScanningPayActivity.this, "请选择收款账户");
            return;
        }

        Param accountNoParam = new Param("accountNo",accountNo);
        Param moneyStrParam = new Param("amount", MoneyEncoder.encodeToPost(moneyStr));
        qtpayApplication.setValue("QRCodeGathering.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(accountNoParam);
        qtpayParameterList.add(moneyStrParam);
        httpsPost("QRCodeGatheringTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                Intent intent = new Intent(ScanningPayActivity.this, ScanPayQrcodeDisPalyActivity_.class);
                try {
                    if (payResult.getData() != null && payResult.getData().length() > 0) {
                        JSONObject jsonObj = new JSONObject(payResult.getData());
//{"qrcodepath":"https://mposprepo.ruiyinxin.com:4430//qtcode/20160831/A000115786/10000000000006207337.jpg","qrcodeStatus":"1","qrcodeDesc":"请求成功","outtime":"60"}
                      if( jsonObj.has("qrcodepath")){
                        String qrcodepath=  jsonObj.getString("qrcodepath");
                          intent.putExtra("qrcodepath",qrcodepath);
                      }
                        if( jsonObj.has("qrcodeStatus")){
                            String qrcodeStatus=  jsonObj.getString("qrcodeStatus");
                            intent.putExtra("qrcodeStatus",qrcodeStatus);
                        }
                        if( jsonObj.has("qrcodeDesc")){
                            String qrcodeDesc=  jsonObj.getString("qrcodeDesc");
                            intent.putExtra("qrcodeDesc",qrcodeDesc);
                        }
                        if( jsonObj.has("outtime")){
                            String outtime=  jsonObj.getString("outtime");
                            intent.putExtra("outtime",outtime);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                startActivity(intent);
                finish();
            }
        });






//        orderinfo = Order.IMPAY_SHANFU;
//        orderinfo.setOrderRemark(orderRemark);
//        orderinfo.setOrderAmt(moneyStr);
//        orderinfo.setOrderDesc(accountNo);
//        orderinfo.setAccount2(realName);
//        orderinfo.setDisPlayContent(impayphonetv.getText().toString().trim()+"  "+realName);
////        orderinfo.setMustMpos(true);
//        orderinfo.setPayee(customerId);
//        orderinfo.setCardIdx(cardIdx);
//        //根据merchantID和prodectid确定是走闪付还是小额付
//        orderinfo.setMerchantId(merchantId);
//        orderinfo.setProductId(productId);
//        orderinfo.setInterfaceTag("01");
//        intent.putExtra("orderinfo", orderinfo);
//        startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);



    }

    public void getBankCardList2() {
        Param qtpayCardIdx = new Param("cardIdx");
        Param qtpayBindType = new Param("bindType", "01");
        Param qtpayCardNum = new Param("cardNum", "05");
        qtpayCardIdx.setValue("00");
        qtpayApplication.setValue("GetBankCardList2.Req");

        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBindType);
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add(qtpayCardNum);

        httpsPost(httpsTag, new XmlCallback() {

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                //银行卡列表
                initBankListData(payResult.getData());
                boolean ishaveDefault=false;
                for (int i=0;i<bankcardlist.size();i++){
                    String flagInfo=  bankcardlist.get(i).getFlagInfo();
                    if("1".equals(flagInfo)){
                        resetBankListView(i);
                        ishaveDefault=true;
                        break;
                    }
                }
                if(!ishaveDefault){
                    resetBankListView(0);
                }
            }
            @Override
            public void onTradeFailed() {
                LogUtil.showToast(ScanningPayActivity.this, "获取用户信息失败,请稍后再试!");
            }
        });
    }

    private void resetBankListView(int i) {
        if (bankcardlist.size() > 0) {
            im_pay_selectbankid.setVisibility(View.VISIBLE);
            banklistviewLine.setVisibility(View.VISIBLE);
            //闪付储蓄卡操作
            BanksUtils.selectIcoidToImgView(ScanningPayActivity.this,bankcardlist.get(i).getBankId(), impay_bankimg);
            impay_bankname.setText(bankcardlist.get(i).getBankName());
            impay_accountno.setText(StringUnit.cardJiaMi(bankcardlist.get(i).getAccountNo()));
            accountNo = bankcardlist.get(i).getAccountNo();
            cardIdx= bankcardlist.get(i).getCardIdx();
//            RyxAppdata.getInstance(ScanningPayActivity.this).saveRuishuaparam(RyxAppdata.getInstance(
//                    ScanningPayActivity.this).getMobileNo(), accountNo, bankcardlist.get(i).getCardIdx());
        } else {
            im_pay_selectbankid.setVisibility(View.GONE);
        }
    }

    /**
     * 解析银行卡信息数据
     *
     * @param banklistJson
     */
    private void initBankListData(String banklistJson) {
//        banklistJson="{\"summary\":{\"numOfCard\":\"2\"},\"result\":{\"message\":\"成功\",\"bindStatus\":\"\",\"resultCode\":\"0000\"},\"resultBean\":[{\"remark\":\"\",\"bankCity\":\"济南市\",\"bankProvinceId\":\"370000\",\"accountNo\":\"6225684321000099630\",\"bankName\":\"广东发展银行股份有限公司\",\"bankProvince\":\"山东省\",\"bankId\":\"306\",\"flagInfo\":\"\",\"bankCityId\":\"370100\",\"cardIdx\":\"1\",\"name\":\"徐臣臣\",\"branchBankId\":\"306451000010\",\"branchBankName\":\"济南分行\"},{\"remark\":\"\",\"bankCity\":\"济南市\",\"bankProvinceId\":\"370000\",\"accountNo\":\"6223200100062334\",\"bankName\":\"中国农业银行股份有限公司\",\"bankProvince\":\"山东省\",\"bankId\":\"103\",\"flagInfo\":\"\",\"bankCityId\":\"370100\",\"cardIdx\":\"2\",\"name\":\"徐臣臣\",\"branchBankId\":\"103451011139\",\"branchBankName\":\"济南济大路支行\"}]}";

        bankcardlist.clear();
        /***
         * 01-05 19:08:56.352: E/banklist(23142):
         * {"summary":{"numOfCard":"1"},
         * "result":{"message":"成功","resultCode":"0000"},
         * "resultBean":[{"remark":"","bankCity":"济南市","bankProvinceId":"370000","accountNo":"6228450258024439275",
         * "bankName":"中国农业银行股份有限公司","bankProvince":"山东省","bankId":"103","flagInfo":"","bankCityId":"370100",
         * "cardIdx":"1","name":"孙立彬","branchBankId":"103451015513","branchBankName":"济南高新技术产业开发区支行"}]}

         */

        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            if ("0000".equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONArray("resultBean");
                for (int i = 0; i < banks.length(); i++) {
                    BankCardInfo bankCardInfo = new BankCardInfo();
                    bankCardInfo.setBankCity(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankCity"));
                    bankCardInfo.setRemark(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "remark"));
                    bankCardInfo.setBankProvinceId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvinceId"));
                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "accountNo"));
                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankName"));
                    bankCardInfo.setBankProvince(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvince"));
                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankId"));
                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "flagInfo"));
                    bankCardInfo.setBankCityId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankCityId"));
                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardIdx"));
                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "name"));
                    bankCardInfo.setBranchBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankId"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
                    bankcardlist.add(bankCardInfo);
                }


            } else {
                String message = jsonObj.getJSONObject("result").getString("message");
                LogUtil.showToast(ScanningPayActivity.this, message);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 获取用户真实姓名
     */
    public void userInfo() {
        qtpayApplication.setValue("UserInfoQuery.Req");
        Param qtpayTransType = new Param("transType", "01");
        qtpayMobileNO.setValue(impayphonetv.getText().toString());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayTransType);
        httpsPost("userinfoTg", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeJson(payResult.getData());
                impay_username.setText(realName);
            }
            @Override
            public void onTradeFailed() {
                LogUtil.showToast(ScanningPayActivity.this, "获取绑定银行卡列表失败!");
            }
        });
    }

    /**
     * 解析用户信息
     */
    private void analyzeJson(String jsonstring) {

        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    JSONObject userinfo = jsonObj.getJSONObject("resultBean");
                    realName = userinfo.getString("realName");
                    if ("".equals(realName)) {
                        realName = MessageFormat.format(getResources().getString(R.string.phone_name), RyxAppdata.getInstance(this).getCurrentBranchName());
//                        if (RyxAppconfig.BRANCH.equals("01")) {
//                            realName = MessageFormat.format(getResources().getString(R.string.phone_name), getResources().getString(R.string.app_name));
//                        }else if (RyxAppconfig.BRANCH.equals("02")) {
//                            realName = MessageFormat.format(getResources().getString(R.string.phone_name), getResources().getString(R.string.app_name_ryx));
//                        }
                    }
                } else {
                    realName = "--";
                }
            } else {
                realName = "--";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isneendGetBanks) {
            getBankCardList2();
        } else {
            isneendGetBanks = true;
        }
    }
}
