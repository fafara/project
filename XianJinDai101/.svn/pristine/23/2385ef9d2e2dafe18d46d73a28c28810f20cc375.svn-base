package com.ryx.payment.ruishua.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qtpay.qtjni.QtPayEncode;
import com.rey.material.app.ThemeManager;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.RyxApplication;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.AppLisDialogAdapter;
import com.ryx.payment.ruishua.adapter.BranchListAdapter;
import com.ryx.payment.ruishua.bean.AppListBean;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.service.CustomKeyBoardService;
import com.sobot.chat.SobotApi;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * xiepingping
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends LoginBaseAct {

    private String httpsTag = "LoginHttps";
    @ViewById(R.id.btn_login)
    Button btnLogin;
    @ViewById(R.id.tv_register)
    TextView tv_register;

    @ViewById(R.id.iv_show_pwd)
    ImageView iv_show_pwd;
    @ViewById(R.id.edt_pwd)
    EditText edt_pwd;
    @ViewById
    EditText edt_accout;

    @ViewById(R.id.lv_account)
    ListView lv_account;
    @ViewById(R.id.lay_list)
    AutoLinearLayout lay_list;

    @ViewById(R.id.btn_pop_list)
    ImageView btn_pop_list;
    @ViewById(R.id.iv_show_pwd)
    ImageView ivShowPwd;
    @ViewById(R.id.layout_username)
    LinearLayout layoutUsername;
    @ViewById
    ImageView iv_logo;

    @ViewById(R.id.layout_pwd)
    LinearLayout layoutPwd;

    @ViewById(R.id.layout_pwd_state)
    LinearLayout layoutPwdState;
    private LoginAccountListAdapter accountListAdapter;

    private boolean showPwd = false;
    private long exitTime = 0;
    private ArrayList<Map<String, String>> applist;

    //    private ArrayList<MsgInfo> personal_noticeList = new ArrayList<MsgInfo>();

    private boolean isRyx = false;



    private String phoneStr,phoneMsg;
    @Bean
    AppLisDialogAdapter applistAapter;

    private Dialog dialog;
    private ArrayList<AppListBean> app_list;

    private static int TOLOGINVERIFICATION=0x001;
    @AfterViews
    public void iniViews() {
        isTokenIntent = getIntent().getBooleanExtra("tokenIntent", false);
        RyxAppdata.getInstance(this).glideLoaddrawableLogoImageViewForBranch(iv_logo);
        initUserList();
        RyxAppdata.getInstance(this).resetCurrentBranchConfig();
        accountListAdapter = new LoginAccountListAdapter(this);
        accountListAdapter.setList(userList);
        lv_account.setAdapter(accountListAdapter);
        ivShowPwd.setBackgroundResource(R.drawable.icon_user_eye_close);
        iniRyxKeyWord();
        initQtPatParams();
        initListView();
      int styleTag=  RyxAppdata.getInstance(this).getCurrentBranchMainStyleTag();
        if(styleTag==2){
            //灰色主题
            btnLogin.setBackgroundResource(R.drawable.btn_rhb_roundshape);
        }
    }

    private void iniRyxKeyWord() {
        CustomKeyBoardService customKeyBoardService = CustomKeyBoardService.registerKeyBoardForEdit(LoginActivity.this, true, edt_pwd, new EditPwdListener() {
            @Override
            public void getPwdVal(String realVal, String disVal) {
//            textView1.setText("密码:"+realVal);
                edt_pwd.setText(realVal);
            }

            @Override
            public void getPwdDisVal(String disVal, int count) {
                edt_pwd.setText(disVal);
            }

            @Override
            public void pwdViewOkbtnLisener() {
                setBtnLogin();
            }

        });
        customKeyBoardService.setEditMaxLenth(20);
    }

    public JSONObject getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        JSONObject jsObject = new JSONObject();
        try {
            // 设备参数
            jsObject.put("Host", Build.HOST);
            // 获取手机唯一标识，GSM手机为IMEI码，CDMA手机为MEID码。
            jsObject.put("IMEI_or_MEID", PhoneinfoUtils.getDeviceId(LoginActivity.this));
            // UUID
            jsObject.put("UUID", PhoneinfoUtils.getUUid(LoginActivity.this));
            // MAC地址
            jsObject.put("MacAddress", PhoneinfoUtils.getMacAddress(LoginActivity.this));
            jsObject.put("AndroidId", PhoneinfoUtils.getAndroidId(LoginActivity.this));
            // android系统定制商
            jsObject.put("BRAND", Build.BRAND);
            // 系统版本
            jsObject.put("os_version", Build.VERSION.RELEASE);
            jsObject.put("TotalMemory",
                    PhoneinfoUtils.getTotalMemory(LoginActivity.this));
            // 版本
            jsObject.put("MODEL", Build.MODEL);
            jsObject.put("bankProvinceId",baseprovinceid);

//			// 手机串号
//			jsObject.put("SerialNum", PhoneinfoUtils.getSerialNum(Login.this));
//
//			jsObject.put("SimSerialNum",
//					PhoneinfoUtils.getSimSerialNumber(Login.this));
//
//			jsObject.put("BOARD", Build.BOARD);
//
//			// cpu指令集
//			jsObject.put("CPU_ABI", Build.CPU_ABI);
//			// 设备参数
//			jsObject.put("Device", Build.DEVICE);
//
//			jsObject.put("user", Build.USER);
//			// builder类型
//			jsObject.put("type", Build.TYPE);
//			// build的标签
//			jsObject.put("tags", Build.TAGS);
//			// 显示屏参数
//			jsObject.put("DISPLAY", Build.DISPLAY);
//			// 修订版本列表
//			jsObject.put("ID", Build.ID);
//			// 硬件制造商
//			jsObject.put("MANUFACTURER", Build.MANUFACTURER);
//
//			// sdk版本
//			jsObject.put("sdk_version", Build.VERSION.SDK);
//
//			// 手机制造商
//			jsObject.put("PRODUCT", Build.PRODUCT);
//			// SERIAL
//			jsObject.put("serial", Build.SERIAL);
//
//
//
//			jsObject.put("AvailMemory",
//					PhoneinfoUtils.getAvailMemory(Login.this));
//
//			jsObject.put("MaxCpuFreq", PhoneinfoUtils.getMaxCpuFreq());
//			jsObject.put("MinCpuFreq", PhoneinfoUtils.getMinCpuFreq());
//			jsObject.put("CurCpuFreq", PhoneinfoUtils.getCurCpuFreq());
//			jsObject.put("CpuName", PhoneinfoUtils.getCpuName());
//			jsObject.put("CpuInfo", PhoneinfoUtils.getCpuInfo());
//			// 是否是漫游
//			jsObject.put("NetworkRoaming",
//					PhoneinfoUtils.isNetworkRoaming(Login.this) + "");
//			jsObject.put("SubscriberId",
//					PhoneinfoUtils.getSubscriberId(Login.this));
//			jsObject.put("SimState", PhoneinfoUtils.getSimState(Login.this)
//					+ "");
//			jsObject.put("SimOperatorName",
//					PhoneinfoUtils.getSimOperatorName(Login.this) + "");
//			jsObject.put("SimOperator",
//					PhoneinfoUtils.getSimOperatorName(Login.this) + "");
//			jsObject.put("SoftWareVersion",
//					PhoneinfoUtils.getSoftWareVersion(Login.this) + "");
//			jsObject.put("phoneNum", PhoneinfoUtils.getLine1Number(Login.this)
//					+ "");
//			jsObject.put("NeighboringCellInfo", tm.getNeighboringCellInfo()
//					+ "");
//			jsObject.put("NetworkCountryIso", tm.getNetworkCountryIso() + "");
//			jsObject.put("NetworkOperator", tm.getNetworkOperator() + "");
//			jsObject.put("NetworkOperatorName", tm.getNetworkOperatorName()
//					+ "");
//			jsObject.put("NetworkType", tm.getNetworkType() + "");
//			jsObject.put("PhoneType", tm.getPhoneType() + "");
//			jsObject.put("SimCountryIso", tm.getSimCountryIso() + "");
//			jsObject.put("VoiceMailAlphaTag", tm.getVoiceMailAlphaTag() + "");
//			jsObject.put("VoiceMailNumber", tm.getVoiceMailNumber() + "");
//			jsObject.put("hasIccCard", tm.hasIccCard() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.showLog("phoneMsg=="+jsObject.toString());
        return jsObject;
    }

    private void doLogin(final boolean isMerge) {
        String pwdStr = edt_pwd.getText().toString().trim();
        phoneStr = edt_accout.getText().toString().trim();
        if (phoneStr.length() < 11) {
            LogUtil.showToast(LoginActivity.this, "请输入正确的手机号码!");
            return;
        }
        if (TextUtils.isEmpty(pwdStr)) {
            LogUtil.showToast(LoginActivity.this, "请输入密码!");
            return;
        }
        qtpayApplication.setValue("UserLogin.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("password", QtPayEncode
                .encryptUserPwd(pwdStr, phoneStr, RyxAppconfig.DEBUG)));
        qtpayParameterList.add(new Param("verifiCode",""));
        try {
            qtpayParameterList.add(new Param("phoneModel", android.os.Build.MODEL.trim().replace(" ","")));
        }catch (Exception e){
            qtpayParameterList.add(new Param("phoneModel",""));
        }
        try {
             phoneMsg = DataUtil.encode(getPhoneInfo().toString().trim().replace(" ", ""));
            qtpayParameterList
                    .add(new Param("phoneMsg", phoneMsg));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            qtpayParameterList
                    .add(new Param("phoneMsg", ""));
        }
        if (isMerge) {
            qtpayParameterList.add(new Param("isMerge", "1"));
        }
        RyxAppdata.getInstance(LoginActivity.this).setPhone(phoneStr);
        RyxAppdata.getInstance(LoginActivity.this).setMobileNo(phoneStr);

        httpsPost(httpsTag, new XmlCallback() {
            @Override
            public void onLoginAnomaly() {

            }
            @Override
            public void onTradeSuccess(RyxPayResult qtpayResult) {
                        if (isMerge) {
                            anaLyzeUserInfo(qtpayResult);
                        }else{
                            LogUtil.showToast(LoginActivity.this, "登录成功！");
                            doRSAnalyze(phoneStr,qtpayResult);
                        }
            }

            @Override
            public void onOtherState(String code,String msg) {
                if("6543".equals(code)){
                    //需要验证码
                    startToLoginVerificationCode();
                }else{
                    RyxAppdata.getInstance(LoginActivity.this).resetCurrentBranchConfig();
                }
            }

            @Override
            public void onTradeFailed() {
                    RyxAppdata.getInstance(LoginActivity.this).resetCurrentBranchConfig();
            }
        });

    }



    private void anaLyzeUserInfo(RyxPayResult qtpayResult) {
        if ("1".equals(qtpayResult.getIsMerge())) {// 第一次登录请求
            try {
                JSONObject jsonObj = new JSONObject(qtpayResult.getData());
                JSONArray tps = jsonObj.getJSONArray("resultBean");
                if (tps.length() == 0) {
                    LogUtil.showToast(LoginActivity.this, "用户名或密码错误！");
                } else if (tps.length() == 1)// 只有一个贴牌
                {
                    RyxAppconfig.APPUSER = tps.getJSONObject(0)
                            .getString("appuser");
                    RyxAppconfig.API_SIGN_KEY = tps.getJSONObject(0)
                            .getString("key");
                    saveAccount(phoneStr);
                    doLogin(false);
                } else {
                    saveAccount(phoneStr);
                    // 弹出列表显示
                    app_list = new ArrayList<AppListBean>();

                    for (int i = 0; i < tps.length(); i++) {
                        AppListBean applist = new AppListBean();
                        applist.setAppName(tps.getJSONObject(i).getString(
                                "branchid"));
                        applist.setIconName(tps.getJSONObject(i).getString(
                                "appuser"));
                        applist.setKey(tps.getJSONObject(i)
                                .getString("key"));
                        app_list.add(applist);
                    }
                    createApplist();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
//            getAppCustomerInfo(qtpayResult);
                 saveUserInfo(phoneStr,qtpayResult,null);
        }
    }


    private void createApplist() {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_card_bank_list, null);
        TextView titlenametv = (TextView) layout.findViewById(R.id.titlename);
        titlenametv.setText("请选择设备类型");
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder = new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final com.rey.material.app.Dialog dialog = builder.build(LoginActivity.this);
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_bank);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        BranchListAdapter branchListAdapter = new BranchListAdapter(LoginActivity.this, app_list);
        lv_bank.setAdapter(branchListAdapter);
        final Long[] lastClickTime = new Long[1];
        lastClickTime[0]= new Long(0);
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime[0] > 1000) {
                    lastClickTime[0] = currentTime;
                    RyxAppconfig.APPUSER = app_list.get(position).getIconName();
                    RyxAppconfig.API_SIGN_KEY = app_list.get(position).getKey();
                    dialog.dismiss();
                    doLogin(false);
                }
            }
        });
        dialog.setContentView(layout);
        dialog.show();
    }






    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果是token失效跳转到的登录界面，操作后退时会提示两次操作
        if (isTokenIntent && keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                LogUtil.showToast(LoginActivity.this, getResources()
                        .getString(R.string.press_again));
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                if (HttpUtil.checkNet(getApplicationContext())) {
                    qtpayApplication = new Param("application");
                    qtpayApplication.setValue("UserLoginExit.Req");
                    qtpayAttributeList.add(qtpayApplication);
                    httpsPost(false, true, "UserLoginExit", new XmlCallback() {
                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            doExit();
                            RyxApplication.getInstance().exit();
                        }
                    });
                } else {
                    doExit();
                    RyxApplication.getInstance().exit();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doExit() {
        QtpayAppData.getInstance(this).setLogin(false);
        QtpayAppData.getInstance(this).setRealName("");
        QtpayAppData.getInstance(this).setMobileNo("");
        QtpayAppData.getInstance(this).setPhone("");
        QtpayAppData.getInstance(this).setCustomerId("");
        QtpayAppData.getInstance(this).setAuthenFlag(0);
        QtpayAppData.getInstance(this).setCustomerName("");
        QtpayAppData.getInstance(this).setToken("");
        SobotApi.exitSobotChat(LoginActivity.this);
    }




    @FocusChange({R.id.edt_pwd, R.id.edt_accout})
    public void focusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.edt_accout:
                layoutUsername.setBackgroundResource(R.color.login_edt_getfocus);
                layoutPwd.setBackgroundResource(R.color.login_edt_lostfocus);
                break;
            case R.id.edt_pwd:
                layoutUsername.setBackgroundResource(R.color.login_edt_lostfocus);
                layoutPwd.setBackgroundResource(R.color.login_edt_getfocus);
                break;
        }
    }

    @Click({R.id.layout_pwd, R.id.iv_show_pwd})
    public void setLayoutPwd() {
        if (edt_pwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivShowPwd.setBackgroundResource(R.drawable.icon_user_eye_close);
        } else {
            edt_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivShowPwd.setBackgroundResource(R.drawable.icon_user_eye_open);
        }
        edt_pwd.setSelection(edt_pwd.getText().length());
    }

    @Click(R.id.tv_resetPwd)
    public void resetPwd() {
        startActivity(new Intent(LoginActivity.this, ResetPassword_.class));
    }
    private void startToLoginVerificationCode(){
        String pwdStr = edt_pwd.getText().toString().trim();
       String  intent_phone = edt_accout.getText().toString().trim();
      String intent_pwd=  QtPayEncode
                .encryptUserPwd(pwdStr, phoneStr, RyxAppconfig.DEBUG);
        Intent intent=new Intent(LoginActivity.this,LoginVerificationCodeAct_.class);
        intent.putExtra("phone",intent_phone);
        intent.putExtra("paswd",intent_pwd);
        try {
            intent.putExtra("phoneModel", android.os.Build.MODEL.trim().replace(" ",""));
        }catch (Exception e){
            intent.putExtra("phoneModel","");
        }
        intent.putExtra("phoneMsg",TextUtils.isEmpty(phoneMsg)?"":phoneMsg);
        startActivityForResult(intent,TOLOGINVERIFICATION);
    }
    @Click(R.id.btn_login)
    public void setBtnLogin() {
        if (lay_list.getVisibility() == View.VISIBLE) {
            lay_list.setVisibility(View.GONE);
        }
        disabledTimerView(btnLogin);
        doLogin(RyxAppdata.getInstance(this).getCurrentLoginBranchType() != 1);
    }

    @Click(R.id.btn_close)
    public void closePage() {
        finish();
    }

    private boolean isShowList = false;

    @Click(R.id.btn_pop_list)
    public void popAccountList() {
        if (userList.size() <= 0) {
            return;
        }
        //如果用户没
        if (!isShowList) {
            isShowList = true;
            lay_list.setVisibility(View.VISIBLE);
            accountListAdapter.setList(userList);
            accountListAdapter.notifyDataSetChanged();
            btn_pop_list.setBackgroundResource(R.drawable.login_pull_up_img);
        } else {
            isShowList = false;
            lay_list.setVisibility(View.GONE);
            btn_pop_list.setBackgroundResource(R.drawable.login_drop_down_img);
        }

    }





    @Click(R.id.tv_register)
    public void setBtnRegister() {
        Intent intent = new Intent(this, RegisterEnterPhoneNumber_.class);
        startActivity(intent);
    }

//    @Click(R.id.iv_show_pwd)
//    public void setShow_pwd() {
//        if (showPwd) {
//            showPwd = false;
////            btn_show_pwd.setBackgroundResource(R.drawable.register_hide_password);
//            edt_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        } else {
//            showPwd = true;
////            btn_show_pwd.setBackgroundResource(R.drawable.register_show_password);
//            edt_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//
//        }
//    }

    public void initListView() {

        lv_account.setAdapter(accountListAdapter);
    }

    @ItemClick(R.id.lv_account)
    public void setAccount(int position) {
        lay_list.setVisibility(View.GONE);
        String account = userList.get(position);
        edt_accout.setText(account);
        edt_accout.setSelection(account.length());
        btn_pop_list.setBackgroundResource(R.drawable.login_drop_down_img);
    }

    public void removeAccount(int position) {
        userList.remove(position);
        accountListAdapter.notifyDataSetChanged();
        if (userList.size() <= 0) {
            isShowList = false;
            lay_list.setVisibility(View.GONE);
            btn_pop_list.setBackgroundResource(R.drawable.login_drop_down_img);
        }
    }

    //获取登录账号
    @SuppressWarnings("unchecked")
    private void initUserList() {
        ObjectInputStream in = null;
        try {
            InputStream is = openFileInput("account.obj");
            in = new ObjectInputStream(is);
            if (in != null) {
                userList = (ArrayList<String>) in.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtil.showLog("userList", userList + "---");
        if (userList.size() > 0) {
            String accountstr = userList.get(userList.size() - 1);
            edt_accout.setText(accountstr);
            edt_accout.setSelection(accountstr.length());
            edt_pwd.requestFocus();//密码编辑框获得焦点 弹出软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt_pwd.getWindowToken(), 0); //强制隐藏键盘
            btn_pop_list.setVisibility(View.VISIBLE);
            btn_pop_list.setBackgroundResource(R.drawable.login_drop_down_img);
        } else {
            btn_pop_list.setVisibility(View.GONE);
            edt_accout.requestFocus();//账号编辑框获取焦点 弹出软键盘
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) edt_accout
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(edt_accout, InputMethodManager.SHOW_FORCED);
                }
            }, 500);
        }
    }



    public class LoginAccountListAdapter extends BaseAdapter {
        private ArrayList list = new ArrayList();
        private ViewHolder viewHolder = null;
        Context context;

        public LoginAccountListAdapter(Context context) {
            this.context = context;
        }

        public void setList(ArrayList list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.adapter_login_account_list_item, null);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_account);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_delete);
            viewHolder.tv.setText((String) list.get(position));
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAccount(position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView img;
            TextView tv;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        canelHttpsPost(httpsTag);
        saveAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RyxAppdata.getInstance(this).resetCurrentBranchConfig();
    }


}
