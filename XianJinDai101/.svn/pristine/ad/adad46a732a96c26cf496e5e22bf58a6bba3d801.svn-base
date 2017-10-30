package com.ryx.payment.ruishua.authenticate;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.IDCardUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

@EActivity(R.layout.activity_merchant_info_add)
public class MerchantInfoAdd extends BaseActivity {
@ViewById
EditText merchant_customerUserName_et,merchant_customerIdCard_et,merchant_customerName_et,merchant_customerNumber_et,merchant_customerAddress_et;
    Param qtpayCertType;
    Param qtpayUserType;
    Param qtpayEmail;
    Param qtpayMerchantName;
    Param qtpayMerchantAddres, qtpayLicence;
    @AfterViews
    public void afterView(){
        setTitleLayout("商户认证",true,false);
        initQtPatParams();
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayCertType = new Param("certType", "01");
        qtpayUserType = new Param("userType", "01");
        qtpayEmail = new Param("email", "test@163.com");
        qtpayMerchantName = new Param("merchantName", "");
        qtpayMerchantAddres = new Param("merchantAddres", "");
        qtpayLicence = new Param("businessLicence", "");
    }
    private void doRequest() {
        String  customerUserName= merchant_customerUserName_et.getText().toString();
        String  customerIdCard= merchant_customerIdCard_et.getText().toString();
        String  customerName= merchant_customerName_et.getText().toString();
        String  customerNumber= merchant_customerNumber_et.getText().toString();
        String  customerAddress= merchant_customerAddress_et.getText().toString();
        qtpayMerchantName.setValue(customerName);
        qtpayMerchantAddres.setValue(customerAddress);
        qtpayLicence.setValue(customerNumber);

        qtpayApplication .setValue("UserUpdateInfo.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCertType);
        qtpayParameterList.add(qtpayUserType);
        qtpayParameterList.add(qtpayEmail);
        qtpayParameterList.add(qtpayMerchantName);
        qtpayParameterList.add(qtpayMerchantAddres);
        qtpayParameterList.add(qtpayLicence);
        qtpayParameterList.add(new Param("realName", customerUserName));
        qtpayParameterList.add(new Param("certPid", customerIdCard));
       httpsPost("UserUpdateInfoTag", new XmlCallback() {
           @Override
           public void onTradeSuccess(RyxPayResult payResult) {
               //设置当前为商户
               RyxAppdata.getInstance(MerchantInfoAdd.this).setAuthenFlag(payResult.getAuthenFlag());
               RyxAppdata.getInstance(MerchantInfoAdd.this).setUserType("01");
               //用户信息新增完毕
               startActivity(new Intent(MerchantInfoAdd.this,MerchantCredentialsUpload_.class));
               finish();
//               parsingJsonData(payResult.getData());
           }
       });
    }

    /**
     * 返回数据解析
     * @param jsonstring
     */
    private void parsingJsonData(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                JSONObject resultObject = jsonObj.getJSONObject("result");
                String resultCode = resultObject.getString("resultCode");
                //代表数据保存成功
                if (RyxAppconfig.QTNET_SUCCESS.equals(resultCode)) {
                    JSONObject resultBeanObject = jsonObj.getJSONObject("resultBean");
                    String authenFlag = resultBeanObject.getString("authenFlag");
                    //设置当前为商户
                    RyxAppdata.getInstance(MerchantInfoAdd.this).setAuthenFlag(Integer.parseInt(authenFlag));
                    RyxAppdata.getInstance(MerchantInfoAdd.this).setUserType("01");
                    //用户信息新增完毕
                    startActivity(new Intent(MerchantInfoAdd.this,MerchantCredentialsUpload_.class));
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.showToast(MerchantInfoAdd.this, "数据解析异常!");
        }
    }

    @Click(R.id.btn_next)
    public void btnNextClick(){
        if(checkInput()){
            doRequest();
        }
}
   public boolean checkInput(){
     String  customerUserName= merchant_customerUserName_et.getText().toString();
       if(TextUtils.isEmpty(customerUserName)){
           LogUtil.showToast(MerchantInfoAdd.this,"请输入真实姓名");
          return false;
       }
       String  customerIdCard= merchant_customerIdCard_et.getText().toString();
       if(!IDCardUtil.isIDCard(customerIdCard)){
           LogUtil.showToast(MerchantInfoAdd.this,"请正确输入身份证号码");
           return false;
       }
       String  customerName= merchant_customerName_et.getText().toString();
       if(TextUtils.isEmpty(customerName)){
           LogUtil.showToast(MerchantInfoAdd.this,"请输入商户名称");
           return false;
       }
       String  customerNumber= merchant_customerNumber_et.getText().toString();
       if(TextUtils.isEmpty(customerNumber)){
           LogUtil.showToast(MerchantInfoAdd.this,"请输入营业执照号");
           return false;
       }
       String  customerAddress= merchant_customerAddress_et.getText().toString();
       if(TextUtils.isEmpty(customerAddress)){
           LogUtil.showToast(MerchantInfoAdd.this,"请输入营业地址");
           return false;
       }

       return true;
   }
}
