package com.ryx.payment.ruishua.recharge;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.QcoinInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.recharge.adapter.QqCoinAccountAdapter;
import com.ryx.payment.ruishua.utils.ContactHelper;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.widget.RyxGridView;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 流量充值
 */
@EActivity(R.layout.activity_flow)
public class FlowActivity extends BaseActivity {
    private String[] mPhoneString = null;
    @ViewById
    RyxGridView flow_ryxgridview;
    @ViewById
    EditText flow_phonenumberet;
    @ViewById
    ImageView flow_contactImg;

    @ViewById
    Button flow_nextbtn;

    private String gameid = "CARD60305";
    Param qtpayGameId;
    Param prepaidPhone;
    QcoinInfo sqcoinInfo = null;
    String qcoinlistJson;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<QcoinInfo> coinlist = new ArrayList<QcoinInfo>();
    @Bean
    QqCoinAccountAdapter qqCoinAccountAdapter;
    private String money;
    @AfterViews
    public void initView(){
    setTitleLayout("流量充值",true);
        initParams();
        setRightImgMessage("用户须知",RyxAppconfig.Notes_Flow);
        flow_ryxgridview.setAdapter(qqCoinAccountAdapter);
        flow_phonenumberet.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String content = flow_phonenumberet.getText().toString();
                if(content.length()>=11)
                {
                    money="";
                    doOrderRequest();
                }
            }
        });
        String mobileNo=QtpayAppData.getInstance(FlowActivity.this).getMobileNo();
        if(!TextUtils.isEmpty(mobileNo)){
            flow_phonenumberet.setText(mobileNo);
            flow_phonenumberet.setSelection(mobileNo.length());
        }

    }
    @Click(R.id.flow_nextbtn)
    public void nextBtnClick(){
        String  qqnum = flow_phonenumberet.getText().toString();
    if(checkparams()){
        Intent intent = new Intent(FlowActivity.this, CreateOrder_.class);
        OrderInfo orderinfo = Order.FLOW_RECHARGE;
        orderinfo.setOrderDesc(sqcoinInfo.getFlowvalue()+"|"+MoneyEncoder.EncodeFormat(sqcoinInfo.getParvalue()).replace("￥","").replace(".","").replace(",","")+"|"+qqnum+"|"+sqcoinInfo.getNettype()); // 要充值qq号
        orderinfo.setOrderAmt(money);
        orderinfo.setOrderRemark(qqnum);
        intent.putExtra("orderinfo", orderinfo);
        startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
    }
    }
    /**
     * 检查参数是否符合要求
     */
    public boolean checkparams() {
       String  qqnum = flow_phonenumberet.getText().toString();

        if (qqnum == null || qqnum.length() != 11) {
            LogUtil.showToast(FlowActivity.this, getResources().getString(R.string.please_enter_the_11_phone_numbers));
            return false;
        } else if (qqnum.substring(0, 1).equals("0")) {
            LogUtil.showToast(FlowActivity.this, getResources().getString(R.string.not_a_phone_number));
            return false;
        }

        else if (TextUtils.isEmpty(money)) {
            LogUtil.showToast(
                    FlowActivity.this,"请选择所充流量!!!");
            return false;
        }
        return true;
    }
    @ItemClick(R.id.flow_ryxgridview)
    public void gridViewItemClick(int position){
        sqcoinInfo=coinlist.get(position);
        money= MoneyEncoder.EncodeFormat(coinlist.get(position).getParvalue());
        qqCoinAccountAdapter.setSelection(position);
        qqCoinAccountAdapter.notifyDataSetChanged();
    }
    private void doOrderRequest() {
        prepaidPhone.setValue(flow_phonenumberet.getText().toString());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayGameId);
        qtpayParameterList.add(prepaidPhone);
        httpsPost("GetPrepaidPhoneInfoTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                qcoinlistJson=payResult.getData();
                coinlist.clear();
                list.clear();
                initListData();
                qqCoinAccountAdapter.setList(coinlist);
                qqCoinAccountAdapter.notifyDataSetChanged();
                sqcoinInfo=coinlist.get(0);
                money= MoneyEncoder.EncodeFormat(coinlist.get(0).getParvalue());
            }

            @Override
            public void onLoginAnomaly() {
                super.onLoginAnomaly();
                coinlist.clear();
                list.clear();
                qqCoinAccountAdapter.setList(coinlist);
                qqCoinAccountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                coinlist.clear();
                list.clear();
                qqCoinAccountAdapter.setList(coinlist);
                qqCoinAccountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                coinlist.clear();
                list.clear();
                qqCoinAccountAdapter.setList(coinlist);
                qqCoinAccountAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "GetPrepaidPhoneInfo.Req");  //GetPrepaidPhoneInfo    GetGameMoney.Req
        qtpayGameId = new Param("gameId", gameid); // 商品编码
        prepaidPhone = new Param("prepaidPhone"); // 电话
    }
    public void initListData() {
        try {
            JSONObject jsonObj = new JSONObject(qcoinlistJson);
            JSONArray banks = jsonObj.getJSONArray("resultBean");
            for (int i = 0; i < banks.length(); i++) {
                QcoinInfo      qcoinInfo = new QcoinInfo();
                qcoinInfo.setGameid(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "gameid"));
                qcoinInfo.setGamename(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "gamename"));
                qcoinInfo.setOnlineid(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "onlineid"));
                qcoinInfo.setOnlinename(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "onlinename"));
                qcoinInfo.setParvalue(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "parvalue"));
                qcoinInfo.setSaleprice(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "saleprice"));
                qcoinInfo.setNettype(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "nettype"));

                qcoinInfo.setFlowvalue(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "flowvalue"));

                qcoinInfo.setBigDidsName(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "flowvalue"));
                list.add(qcoinInfo.getOnlinename());
                qcoinInfo.setTipName("¥"+JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "parvalue"));
                coinlist.add(qcoinInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 通讯录信息获取
     */
    @Click(R.id.flow_contactImg)
    public void contactClick(){
        checkContactPemission();
    }
    /**
     * 处理返回的手机号
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            setAccount(data);
        }
    }

    private void setAccount(Intent intent) {

        if (intent == null) {
            return;
        } else {
            ArrayList<String> tPhone = ContactHelper.getContactPhoneNo(intent.getData(), this);
            LogUtil.showLog("data==="+tPhone);
            if (tPhone.size() > 1) {

                mPhoneString = new String[tPhone.size()];
                for (int i = 0; i < tPhone.size(); i++) {
                    mPhoneString[i] = tPhone.get(i);
                }
               Dialog.Builder builder= new SimpleDialog.Builder(R.style.SimpleDialogLight){
                   @Override
               public void onPositiveActionClicked(DialogFragment fragment) {
                   SimpleDialog dialog = (SimpleDialog) fragment.getDialog();
                   if (dialog == null)
                       return;
                   final int index = dialog.getSelectedIndex();
                   fragment.dismiss();
                   String phoneNumber= mPhoneString[index];
                  String   mobileNo=   phoneNumber.replace("+86", "").replace(" ", "").replace("-", "");
                       flow_phonenumberet.setText(mobileNo);
                       flow_phonenumberet.setSelection(mobileNo.length());
               }
                   @Override
                   public void onNegativeActionClicked(DialogFragment fragment) {
                       fragment.dismiss();
                   }
            };
                ((SimpleDialog.Builder) builder).items(mPhoneString, 0).title("请选择电话号码").positiveAction("确定").negativeAction("取消");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(),null);
             }else if (tPhone.size() == 1) {
               String mobileNo= tPhone.get(0).replace("+86", "").replace(" ", "").replace("-", "");
                flow_phonenumberet.setText(mobileNo);
                flow_phonenumberet.setSelection(mobileNo.length());
            } else {
                flow_phonenumberet.setText("");
            }
}}
    /**
     * 手机访问权限
     */
    public void checkContactPemission(){
        String   waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), getResources().getString(R.string.app_name));
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
        requesDevicePermission(waring, 0x0011,  new PermissionResult() {

                    @Override
                    public void requestSuccess() {
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI), 0);
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE);

    }
}
