package com.ryx.ryxpay.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtpay.qtjni.QtPayEncode;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;
import com.ryx.ryxpay.R;
import com.ryx.ryxpay.RyxAppconfig;
import com.ryx.ryxpay.RyxAppdata;
import com.ryx.ryxpay.adapter.AppLisDialogAdapter;
import com.ryx.ryxpay.bean.Param;
import com.ryx.ryxpay.bean.RyxPayResult;
import com.ryx.ryxpay.net.XmlCallback;
import com.ryx.ryxpay.utils.JsonUtil;
import com.ryx.ryxpay.utils.PreferenceUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by laomao on 16/4/21.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private String httpsTag = "LoginHttps";
    @ViewById(R.id.btn_login)
    Button btnLogin;
    @ViewById(R.id.btn_register)
    Button btnRegister;

    @ViewById(R.id.btn_show_pwd)
    ImageView btn_show_pwd;
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

    private LoginAccountListAdapter accountListAdapter;

    private boolean showPwd = false;

    private ArrayList<Map<String,String>> applist;
    private ArrayList<String> userList = new ArrayList<String>();//账号列表
    private ArrayList<Map<String,String>> noticeList= new ArrayList<Map<String,String>>();//通知列表
    private boolean isRyx = false;
    private String noticeCode = "0000";
    private String toastmsg;
    private int unreadNoticeNumber = 0;//通知数量

    private String phoneStr;
    @Bean
    AppLisDialogAdapter applistAapter;

    private  Dialog dialog;

    @AfterViews
    public void iniViews(){
        initUserList();
        accountListAdapter = new LoginAccountListAdapter(this);
        accountListAdapter.setList(userList);
        lv_account.setAdapter(accountListAdapter);

    }

    void doLogin(boolean isMerge) {
        String pwdStr = edt_pwd.getText().toString().trim();
       phoneStr= edt_accout.getText().toString().trim();
        if(phoneStr.length()<11){
            Toast.makeText(LoginActivity.this,"请输入正确的手机号码！",3).show();
            return;
        }
        if(TextUtils.isEmpty(pwdStr)){
            Toast.makeText(LoginActivity.this,"请输入密码！",3).show();
            return;
        }
        showLoading("正在加登录，请稍候...");
        initQtPatParams();
        qtpayApplication.setValue( "UserLogin.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("password", QtPayEncode
                .encryptUserPwd(pwdStr, phoneStr, RyxAppconfig.DEBUG)));
        if (isMerge)
            qtpayParameterList.add(new Param("isMerge", "1"));
        RyxAppdata.getInstance(LoginActivity.this).setPhone(phoneStr);
        RyxAppdata.getInstance(LoginActivity.this).setMobileNo(phoneStr);
        httpsPost(httpsTag, new XmlCallback() {
            @Override
            public void onLoginAnomaly() {
                Log.i("doLogin", "成功");
            }

            @Override
            public void onTradeSuccess(RyxPayResult qtpayResult) {
                String code = qtpayResult.getRespCode();
                Log.i("onTradeSuccess",code+"---"+qtpayResult.getIsMerge());
                if("1".equals(qtpayResult.getIsMerge())){
                    try {
                        JSONObject jsonObj = new JSONObject(qtpayResult.getData());
                        JSONArray tps = jsonObj.getJSONArray("resultBean");
                        Log.i("onTradeSuccess",tps.toString()+"---");
                        if(tps.length()==0)
                        {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误！",3).show();
                        }
                        else if (tps.length() == 1)// 只有一个贴牌
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
                            applist = new ArrayList<Map<String,String>>();

                            for (int i = 0; i < tps.length(); i++) {
                                Map<String,String> appMap = new HashMap<String, String>();
                                appMap.put("branchid",tps.getJSONObject(i).getString(
                                        "branchid"));
                                appMap.put("appuser",tps.getJSONObject(i).getString(
                                        "appuser"));
                                appMap.put("key",tps.getJSONObject(i).getString(
                                        "key"));
                                applist.add(appMap);
                                if(RyxAppconfig.APPUSER.equals(tps.getJSONObject(i).getString(
                                        "appuser"))){
                                    isRyx = true;
                                }
                            }
                            if(!isRyx){
                            createApplist();
                            }else{
                                doLogin(false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "登录成功！", 3).show();
                    saveUserInfo(qtpayResult);
                }
                cancleLoading();
                Log.i("doLogin", "onTradeSuccess");
            }

            @Override
            public void onOtherState() {
                Log.i("doLogin", "onOtherState");
            }

            @Override
            public void onTradeFailed() {
                Log.i("doLogin", "onTradeFailed");
            }
        });
    }
    //选择设备列表对话框
    private void createApplist(){

        LayoutInflater inflater = LayoutInflater.from(this);
        com.zhy.autolayout.AutoLinearLayout layout = (com.zhy.autolayout.AutoLinearLayout)inflater.inflate(R.layout.dialog_login_applist, null );
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder =
                new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        dialog  = builder.build(LoginActivity.this);
        ListView applv = (ListView)layout.findViewById(R.id.lv_apps);
        applistAapter.setList(applist);
        Log.i("applist",applist.toString());
        applv.setAdapter(applistAapter);
        applv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView checkImg = (ImageView)view.findViewById(R.id.img_check);
                checkImg.setVisibility(View.VISIBLE);
                RyxAppconfig.APPUSER = applist.get(position).get("branchid");
                RyxAppconfig.API_SIGN_KEY = applist.get(position).get("key");
                dialog.dismiss();
                doLogin(false);

            }
        });

        dialog.setTitle("请选择您的设备类型");
        dialog.setContentView(layout);
        dialog.show();

    }

    //保存账号
    private void saveAccount(String account){
        Log.i("account",account);
        if (!TextUtils.isEmpty(account)) {
            userList.remove(account);
            userList.add(account);
            if (userList.size() > 5) {
                userList.remove(0);
            }
            accountListAdapter.setList(userList);
            accountListAdapter.notifyDataSetChanged();
        }
    }
    //保存用户信息
    private void saveUserInfo(RyxPayResult qtpayResult){
        RyxAppdata.getInstance(LoginActivity.this).setToken(qtpayResult.getToken());
        RyxAppdata.getInstance(LoginActivity.this).setMobileNo(
                qtpayResult.getMobileNo());
        RyxAppdata.getInstance(LoginActivity.this)
                .setPhone(qtpayResult.getMobileNo());
        RyxAppdata.getInstance(LoginActivity.this).setCustomerName(
                "" + qtpayResult.getUserName());
        RyxAppdata.getInstance(LoginActivity.this).setRealName(
                "" + qtpayResult.getRealName());
        RyxAppdata.getInstance(LoginActivity.this).setLogin(true);

        RyxAppdata.getInstance(LoginActivity.this).setAuthenFlag(
                qtpayResult.getAuthenFlag());
        RyxAppdata.getInstance(LoginActivity.this).setCustomerId(
                qtpayResult.getCustomerId());
        RyxAppdata.getInstance(LoginActivity.this).setCertPid(
                qtpayResult.getCertPid());
        RyxAppdata.getInstance(LoginActivity.this).setCertType(
                qtpayResult.getCertType());
        RyxAppdata.getInstance(LoginActivity.this).setUserType(
                qtpayResult.getUserType());
        RyxAppdata.getInstance(LoginActivity.this).setEmail(
                "" + qtpayResult.getEmail());
        RyxAppdata.getInstance(LoginActivity.this).setTransLogNo(
                qtpayResult.getTransLogNo());
        RyxAppdata.getInstance(LoginActivity.this).setTagDesc(qtpayResult.getTagDesc());
        noticeCode = PreferenceUtil.getInstance(LoginActivity.this).getString(
                "noticeCode_"
                        + RyxAppdata.getInstance(LoginActivity.this)
                        .getMobileNo(), noticeCode);
        getNoticeList(); // 先读取本地的，再去读取最新的消息
        getNoticeJsonData();
    }

    //获取通知内容
    public void getNoticeJsonData(){
        qtpayApplication.setValue( "GetPublicNotice.Req");
        Param qtpayNoticeCode = new Param("noticeCode", noticeCode);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayNoticeCode);
        httpsPost("GetPublicNoticetag", new XmlCallback() {
            @Override
            public void onLoginAnomaly() {
            }

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeNotices(payResult.getData());
                saveNoticeList();
                setResult(RyxAppconfig.CLOSE_ALL);
                finish();
            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

            }
        });
    }

    //解析通知内容
    private void analyzeNotices(String noticeData){
        if(noticeData!=null){
            try{
                JSONObject noticeObj = new JSONObject(noticeData);
                toastmsg = (String) noticeObj.getJSONObject("result").getString(
                        "message");
                JSONArray noticeArray = noticeObj.getJSONArray("resultBean");
                unreadNoticeNumber = noticeArray.length();
                Map<String,String> msgMap = null;
                for(int i=0;i<unreadNoticeNumber;i++){
                    if (i == noticeArray.length() - 1) {
                        noticeCode = noticeArray.getJSONObject(i).getString(
                                "noticeCode");
                    }
                    msgMap = new HashMap<String,String>();
                    msgMap.put("noticeCode",
                            JsonUtil.getValueFromJSONObject(noticeArray.getJSONObject(i),"noticeCode"));
                    msgMap.put("title",
                            JsonUtil.getValueFromJSONObject(noticeArray.getJSONObject(i),"title"));
                    msgMap.put("noticeContent",
                            JsonUtil.getValueFromJSONObject(noticeArray.getJSONObject(i),"noticeContent"));
                    msgMap.put("effectTime",
                            JsonUtil.getValueFromJSONObject(noticeArray.getJSONObject(i),"effectTime"));
                    noticeList.add(msgMap);
                 }

                PreferenceUtil.getInstance(LoginActivity.this).saveInt(
                        "unreadNoticeNumber_"
                                + RyxAppdata.getInstance(LoginActivity.this)
                                .getMobileNo(), unreadNoticeNumber);
                PreferenceUtil.getInstance(LoginActivity.this).saveString(
                        "noticeCode_"
                                + RyxAppdata.getInstance(LoginActivity.this)
                                .getMobileNo(), noticeCode);

            }catch(Exception e){
                e.printStackTrace();
            }finally{
                noticeData=null;
            }
        }


    }

    @Click(R.id.btn_login)
    public void setBtnLogin() {
        doLogin(true);
    }

    @Click(R.id.btn_close)
    public void closePage(){
        finish();
    }
    private boolean isShowList = false;
    @Click(R.id.btn_pop_list)
    public void popAccountList(){
        if(userList.size()<=0){
            return;
        }
        //如果用户没
        if(!isShowList) {
            isShowList = true;
            lay_list.setVisibility(View.VISIBLE);
            accountListAdapter.notifyDataSetChanged();
            btn_pop_list.setBackgroundResource(R.drawable.login_pull_up_img);
        }else{
            isShowList = false;
            lay_list.setVisibility(View.GONE);
            btn_pop_list.setBackgroundResource(R.drawable.login_drop_down_img);
        }

    }

    /**
     * 读取本地已经存储的消息
     */
    private void getNoticeList() {
        ObjectInputStream in = null;
        try {
            InputStream is = openFileInput("notice_"
                    + RyxAppdata.getInstance(LoginActivity.this).getMobileNo()
                    + ".obj");
            in = new ObjectInputStream(is);
            if (in != null) {
                noticeList = (ArrayList<Map<String,String>>) in.readObject();
            }
        } catch (Exception e) {
            if (noticeList == null) {
                noticeList = new ArrayList<Map<String,String>>();
            }
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
    }

    /**
     * 保存消息到本地
     */
    public void saveNoticeList() {
        ObjectOutputStream out = null;
        try {
            FileOutputStream os = openFileOutput("notice_"
                    + RyxAppdata.getInstance(LoginActivity.this).getMobileNo()
                    + ".obj", MODE_PRIVATE);
            out = new ObjectOutputStream(os);
            out.writeObject(noticeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Click(R.id.btn_register)
    public void setBtnRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity_.class));
    }

    @Click(R.id.btn_show_pwd)
    public void setShow_pwd() {
        if (showPwd) {
            showPwd = false;
            btn_show_pwd.setBackgroundResource(R.drawable.register_hide_password);
            edt_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            showPwd = true;
            btn_show_pwd.setBackgroundResource(R.drawable.register_show_password);
            edt_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
    }

//    @AfterViews
//    public void initListView(){
//
//        lv_account.setAdapter(accountListAdapter);
//    }

    @ItemClick(R.id.lv_account)
    public void setAccount(int position) {
            edt_accout.setText(userList.get(position));
    }

    public void removeAccount(int position) {
        userList.remove(position);
        accountListAdapter.notifyDataSetChanged();
        if(userList.size()<=0){
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        if(userList.size()>0)
            edt_accout.setText(userList.get(userList.size()-1));
    }
    public void saveAccount(){
        ObjectOutputStream out = null;
        try {
            FileOutputStream os = openFileOutput("account.obj",
                    MODE_PRIVATE);
            out = new ObjectOutputStream(os);
            out.writeObject(userList);
        } catch (Exception e) {

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public class LoginAccountListAdapter extends BaseAdapter {
        private ArrayList list = new ArrayList();
        private ViewHolder viewHolder = null;
        Context context;
        public LoginAccountListAdapter(Context context){
            this.context = context;
        }

        public void setList(ArrayList list){
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
                convertView = layoutInflater.inflate(R.layout.adapter_login_account_list_item,null);
                viewHolder.tv =(TextView)convertView.findViewById(R.id.tv_account);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img_delete);
            viewHolder.tv.setText((String)list.get(position));
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAccount(position);
                }
            });
            return convertView;
        }

        class ViewHolder{
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
}
