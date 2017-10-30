package com.livedetect;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.newfragment.baseinfo.boss.RYDBossCallRecordsFragment;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.xjd.RYDBossBaseInfoActivity;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

/**
 * Created by RYX on 2017/8/2.
 */

public class RKDCallRecordsLoginUtil {

    private EditText edt_accout;//手机账号
    private EditText edt_pwd;//服务密码
    private Button btn_login;
    private String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt9MS+bXUO6IftXgrjJJQ78jDFuyvFsDbNlnHX\n" +
            "QTfEigXpQkIxlL0y8xRCczZf9bQfs89ow4XVtDo3s3tjMRyhtCUWZMkLQwiEPkMPddO076Qa+Bea\n" +
            "PyBAhx7DHM8/v5+hozODvXqbVj3cx9pwGBH/2rAqj1YaipjPJ/bb+FISMwIDAQAB";
    private RYDBossCallRecordsFragment mfragment;
    private RYDBossBaseInfoActivity mainActivity;
    private String phoneNo, serviceCode;
    private TextView contactInfo1;
    //private TextView  contactInfo2;
    private String cm_regEx = "^1(3[4-9]|4[7]|5[0-27-9]|7[08]|8[2-478])\\d{8}$";//中国移动号段表达式
    private String cu_regEx = "^1(3[0-2]|4[5]|5[56]|7[0156]|8[56])\\d{8}$";//中国联通号段表达式
    private String ct_regEx = "^1(3[3]|4[9]|53|7[037]|8[019])\\d{8}$";//中国电信号段表达式
    private String contact_url = "https://bj.ac.10086.cn/login";//点击下面修改密码地址后跳转地址；
    private String contact_no = "10086";//点击下面客服号码后拨打的号码；

    private View mView;
    public void onCreateView(RYDBossCallRecordsFragment fragment, View rootView) {
        mfragment = fragment;
        mView= rootView;
        mainActivity = (RYDBossBaseInfoActivity) mfragment.getActivity();
        initView();
    }

    private void initView() {
        edt_accout = (EditText) mView.findViewById(R.id.edt_accout);
        edt_pwd = (EditText) mView.findViewById(R.id.edt_pwd);
        btn_login = (Button) mView.findViewById(R.id.btn_login);
        contactInfo1 = (TextView) mView.findViewById(R.id.tv_contactinfo1);
        contactInfo1.setText(Html.fromHtml(mView.getResources().getString(R.string.contact_info1)));
/*        contactInfo2 = (TextView) mView.findViewById(R.id.tv_contactinfo2);
        contactInfo2.setText(Html.fromHtml(mView.getResources().getString(R.string.contact_info2)));*/
        edt_accout.setText(RyxcreditConfig.getPhoneNo());
        String  info1 = String.format(mView.getResources().getString(R.string.contact_info1),
                "中国移动","10086");
        String info2 = String.format(mView.getResources().getString(R.string.contact_info2),
                "中国移动", "www.10086.cn");
        contactInfo1.setText(Html.fromHtml(info1));
      //  contactInfo2.setText(Html.fromHtml(info2));
        edt_accout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNo = s.toString();
                CLogUtil.showLog("phoneNo---",phoneNo+"---");
                checkNo(phoneNo);
            }
        });



        btn_login.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                phoneNo = edt_accout.getText().toString().trim();
                serviceCode = edt_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNo)) {
                    CLogUtil.showToast(mainActivity, "请输入手机号码！");
                    return;
                }
                if (TextUtils.isEmpty(serviceCode)) {
                    CLogUtil.showToast(mainActivity, "请输入服务密码！");
                    return;
                }
                if (serviceCode.length() < 6||serviceCode.length()>16) {
                    CLogUtil.showToast(mainActivity, "请输入正确的服务密码！");
                    return;
                }
                String encryPsw = "";
                try {
                    encryPsw = encrypt(serviceCode);
                    mfragment.doLogin(phoneNo, encryPsw);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        contactInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转系统拨号
                Uri uri = Uri.parse("tel:" + contact_no);
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                mainActivity.startActivity(it);
            }
        });
/*        contactInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(mainActivity, Class.forName(mainActivity.getApplicationContext().getPackageName() + ".activity.HtmlMessageActivity_"));
                    intent.putExtra("ccurl", contact_url);
                    intent.putExtra("title","手机运营商");
                    intent.putExtra("webPage",true);
                    mainActivity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });*/

    }

    public void setBtnVisiblity(int flag){
        btn_login.setVisibility(flag);
    }

    private void checkNo(String phoneStr){
        if (TextUtils.isEmpty(phoneStr)) {
            CLogUtil.showToast(mainActivity, "请输入手机号码！");
            return;
        }
        String info1 = "";
        String info2 = "";
        //移动
        if (checkPhoneNo(cm_regEx, phoneStr)) {
            info1 = String.format(mainActivity.getResources().getString(R.string.contact_info1),
                    "中国移动","10086");
            info2 = String.format(mainActivity.getResources().getString(R.string.contact_info2),
                    "中国移动","www.10086.cn");
            contactInfo1.setText(Html.fromHtml(info1));
         //   contactInfo2.setText(Html.fromHtml(info2));
            contact_url = "https://bj.ac.10086.cn/login";
            contact_no = "10086";
        }
        //联通
        else if (checkPhoneNo(cu_regEx, phoneStr)) {
            info1 = String.format(mainActivity.getResources().getString(R.string.contact_info1),
                    "中国联通","10010");
            info2 = String.format(mainActivity.getResources().getString(R.string.contact_info2),
                    "中国联通","uac.10010.com");
            contactInfo1.setText(Html.fromHtml(info1));
         //   contactInfo2.setText(Html.fromHtml(info2));
            contact_url = "https://uac.10010.com/cust/resetpwd/inputName";
            contact_no = "10010";
        }
        //电信
        else if (checkPhoneNo(ct_regEx, phoneStr)) {
            info1 = String.format(mainActivity.getResources().getString(R.string.contact_info1),
                    "中国电信","10000");
            info2 = String.format(mainActivity.getResources().getString(R.string.contact_info2),
                    "中国电信","www.189.cn");
            contactInfo1.setText(Html.fromHtml(info1));
        //    contactInfo2.setText(Html.fromHtml(info2));
            contact_url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000180";
            contact_no = "10000";
        }
    }

//    /*
//    * 手机号合法性校验
//    * */
//    private boolean phonNorexgStr(String str) {
//        Pattern pattern = Pattern.compile(phoneNoregEx);
//        Matcher m = pattern.matcher(str);
//        return m.matches();
//    }

    /*
    * 手机号运营商校验
    * */
    private boolean checkPhoneNo(String regEx, String phoneStr) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(phoneStr);
        return m.matches();
    }

    /*
     * 函数说明：getPublicKey 取得公钥
     * @param key 公钥字符串
     * @return PublicKey 返回公钥
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public String encrypt(String source) throws Exception {
        Key publicKey;
        publicKey = getPublicKey(PUBLIC_KEY);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return Base64.encodeToString(b1, Base64.DEFAULT);
    }


}

