package com.ryx.payment.ruishua.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.adapter.MainAdGridAdapter;
import com.ryx.payment.ruishua.adapter.MainGridAdapter;
import com.ryx.payment.ruishua.authenticate.AuthResultActivity_;
import com.ryx.payment.ruishua.authenticate.Authenticate_;
import com.ryx.payment.ruishua.authenticate.MerchantCredentialsUpload_;
import com.ryx.payment.ruishua.authenticate.UserAuthPhotoUploadActivity_;
import com.ryx.payment.ruishua.authenticate.UserInfoAddActivity_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.IdCardUploadAct_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.NewAuthResultAct_;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.IconBean;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.IntentHelper;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

@EActivity(R.layout.activity_more_increate)
public class MoreIncreateActivity extends BaseActivity {
    @ViewById(R.id.gv_moreIncreate)
    RecyclerView mBottomRecyclerView;
    private MainGridAdapter mainBottomGridAdapter;
    BankCardInfo bankCardInfo = null;
    private String powermsg = "-1";// 实名认证有关
    private int checkposition=-1;
    private MainAdGridAdapter mainAdGridAdapter;
    private Intent intent;
    private ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();// 已经绑定银行卡列表
    //底部GridView数据集合
    private ArrayList<IconBean.IconMsgBean> mBottomIconMsgBean = new ArrayList<>();
    //所有数据集合
    private ArrayList<IconBean.IconMsgBean> mOriginalData = new ArrayList<>();
    private ArrayList<IconBean.IconMsgBean> mMainIconMsgBean = new ArrayList<>();

    @AfterViews
    public void initViews() {
        final String title = (String) getIntent().getExtras().get("title");
        String mytitle="";
        if(TextUtils.isEmpty(title)){
            mytitle= RyxAppdata.getInstance(this).getCurrentBranchName();
        }else{
            mytitle=title;
        }
        setTitleLayout("更多",true,false);
        initGridData();
    }
    /**
     * 初始化GridView数据
     */
    private void initGridData() {
        String bottomResult = getFromRaw(RyxAppdata.getInstance(MoreIncreateActivity.this).getCurrentBranchRawMoreConfigId());
        IconBean bottomIconBean = handleInputStream(bottomResult);
        mOriginalData = (ArrayList<IconBean.IconMsgBean>) bottomIconBean.getGetIconList();

        Comparator topComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                IconBean.IconMsgBean imb1 = (IconBean.IconMsgBean) o1;
                IconBean.IconMsgBean imb2 = (IconBean.IconMsgBean) o2;
                return (new Integer(imb1.getIdx())).compareTo(new Integer(imb2.getIdx()));
            }
        };
        mBottomIconMsgBean.clear();
        mBottomIconMsgBean.addAll(mOriginalData);
        Iterator<IconBean.IconMsgBean> bottomIterator = mOriginalData.iterator();
        while (bottomIterator.hasNext()) {
            IconBean.IconMsgBean bottomBean = bottomIterator.next();
            if (bottomBean.getShow().equals("1")) {
                //不显示的进行移除
                bottomIterator.remove();

            }
        }
        RecyclerViewHelper.init().setRVGridLayout(MoreIncreateActivity.this, mBottomRecyclerView, 3);//3列
        mainBottomGridAdapter = new MainGridAdapter(mOriginalData, MoreIncreateActivity.this, R.layout.gridview_main_item);
        mBottomRecyclerView.setAdapter(mainBottomGridAdapter);




        //底部网格
        mainBottomGridAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                IconBean.IconMsgBean msgBean = mBottomIconMsgBean.get(position);
                try {
                    if (null != msgBean && checkPermission(msgBean)) {
                        String activityName = msgBean.getId();
                        String flag = msgBean.getFlag();
                        Bundle bundle = new Bundle();
                        if ("usernote".equals(flag)) {//用户须知
                            bundle.putString("title", "用户须知");
                            bundle.putString("urlkey", RyxAppconfig.Notes_Notice);
                            intentToActivity(activityName, bundle);
                        } else {
                            intentToActivity(activityName, bundle);//剩余统一跳转
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 统一跳转对应模块
     *
     * @param ActivityName
     * @param bundle
     * @throws Exception
     */
    private void intentToActivity(String ActivityName, Bundle bundle) throws Exception {

        if (null == ActivityName || "".equals(ActivityName)) {
            throw new Exception("参数为空");
        }
        if (IntentHelper.getInstance().contains(ActivityName)) {
            Intent intent = new Intent(MoreIncreateActivity.this
                    .getApplicationContext(), IntentHelper.getInstance()
                    .getActivityClass(ActivityName));
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }
        throw new Exception("尚未注册此ActivityName=" + ActivityName);
    }
    /**
     * 统一跳转前检测
     *
     * @param msgBean 对象
     * @return 是否通过
     */
    private boolean checkPermission(IconBean.IconMsgBean msgBean) {
        String permissionStr = msgBean.getPermission();
        /**
         * 0   需要登录
         * 1   需要实名通过
         * 2   需要绑定默认结算卡
         */
        if (permissionStr.contains("0")) {//验证登录
            if (!QtpayAppData.getInstance( MoreIncreateActivity.this.getApplicationContext()).isLogin()) {
                toAgainLogin(MoreIncreateActivity.this.getApplicationContext(), RyxAppconfig.TOLOGINACT);
                return false;
            }
        }
        if (permissionStr.contains("1")) {//验证实名认证是否通过
            int flag = QtpayAppData.getInstance(MoreIncreateActivity.this.getApplicationContext()).getAuthenFlag();
            if (flag != 3) {
                showAuthDialog();
                return false;
            }
        }
        if (permissionStr.contains("2")) {//验证是否绑定银行卡
            getBankCardList(msgBean);
        }
        return true;
    }

    // 从resources中的raw 文件夹中获取文件并读取数据
    public String getFromRaw(int id) {
        String result = "";
        try {
            InputStream in = getResources().openRawResource(id);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, 0, buffer.length, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public IconBean handleInputStream(String inputString) {
        IconBean iconBean = new IconBean();
        try {
            JSONArray jsonArray = new JSONObject(inputString).getJSONArray("getIconList");
            ArrayList<IconBean.IconMsgBean> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                IconBean.IconMsgBean msgBean = new IconBean.IconMsgBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                msgBean.setFlag(jsonObject.getString("flag"));
                msgBean.setId(jsonObject.getString("id"));
                msgBean.setIdx(jsonObject.getString("idx"));
                msgBean.setName(jsonObject.getString("name"));
                msgBean.setRes(jsonObject.getString("res"));
                msgBean.setShow(jsonObject.getString("show"));
                msgBean.setPermission(jsonObject.getString("permission"));
                list.add(msgBean);
            }
            iconBean.setGetIconList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconBean;
    }
    /**
     * 获取银行卡列表
     */
    private void getBankCardList(final IconBean.IconMsgBean bean) {
//        getOldBankCardList(bean);
        getNewBankCardList(bean);
    }
    /**
     * 调用新的卡管理中接口获取已绑定银行卡列表接口
     *
     * @param bean
     */
    private void getNewBankCardList(final IconBean.IconMsgBean bean) {
        MoreIncreateActivity.this.qtpayApplication.setValue("BindCardList.Req");
        MoreIncreateActivity.this.qtpayAttributeList.add(MoreIncreateActivity.this.qtpayApplication);
        MoreIncreateActivity.this.qtpayParameterList.add(new Param("cardType", "10"));
        MoreIncreateActivity.this.httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                newinitBankListData(payResult.getData());
                checkBindCard(bankcardlist, bean);
            }
        });


    }   /**
     * 检查 是否绑定了银行卡
     */
    private void checkBindCard(final ArrayList<BankCardInfo> list, final IconBean.IconMsgBean bean) {
        final Bundle bundle = new Bundle();
        String flag = bean.getFlag();
        if (list.isEmpty()) {
            LogUtil.showToast(MoreIncreateActivity.this.getApplicationContext(), "您还未绑定有效结算卡,请先绑定有效结算卡!");
            return;
        }
        try {
            if ("impay".equals(flag)|| "withdrawim".equals(flag)) {//刷卡收款、扫码收款、资金结算
                bundle.putSerializable("bankcardlist", list);
                intentToActivity(bean.getId(), bundle);
//                for (BankCardInfo bankCardInfo : list) {
//                    if (bankCardInfo.getFlagInfo().equals("1")) {//设置了默认卡
//                        isSetDefaultCard = true;
//                        break;
//                    }
//                }
//                if (isSetDefaultCard) {
//                    intentToActivity(bean.getId(), bundle);
//                } else {
//                    LogUtil.showToast(getActivity().getApplicationContext(), "您还未设置默认结算卡，请进入绑定银行卡设置！");
//                }
            }else if("scanpay".equals(flag)){
                //二维码扫一扫功能在此确定权限
                String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsgs), RyxAppdata.getInstance(MoreIncreateActivity.this).getCurrentBranchName());
                ((BaseActivity) MoreIncreateActivity.this).requesDevicePermission(waring, 0x00018, new PermissionResult() {

                    @Override
                    public void requestSuccess() {
                        bundle.putSerializable("bankcardlist", list);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ((BaseActivity) MoreIncreateActivity.this).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            intentToActivity(bean.getId(), bundle);
                                        }catch (Exception e){
                                        }
                                    }
                                });
                            }
                        }).start();


                    }

                    @Override
                    public void requestFailed() {

                    }
                }, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 初始化银行卡列表(new)
     *
     * @param banklistJson
     */
    private void newinitBankListData(String banklistJson) {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            bankcardlist.clear();
            if ("0000".equals(jsonObj.getString("code"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONObject("result").getJSONArray("cardlist");
                for (int i = 0; i < banks.length(); i++) {
                    bankCardInfo = new BankCardInfo();
                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardidx"));
                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankid"));
                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardno"));
                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankname"));
                    bankCardInfo.setQuick(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "quick"));
                    bankCardInfo.setDaikou(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "msdk"));
                    bankCardInfo.setDaifustatus(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "daifustatus"));
                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "flaginfo"));//1为默认结算卡
                    bankCardInfo.setCardtype(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardtype"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "customername"));
                    bankCardInfo.setCardstatus(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardstatus"));
                    bankCardInfo.setCardnote(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardnote"));
                    bankcardlist.add(bankCardInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 展示实名认证框
     */
    private void showAuthDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(MoreIncreateActivity.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                gotoRealName();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("亲爱的用户，为了确保您的资金安全，进行此业务前需通过实名认证。");
    }
    /**
     * 跳转对应实名认证步骤
     */
    protected void gotoRealName() {
        // case 0: "未实名");
        // case 1:
        // case 5: "未认证");
        // case 2: "认证中");
        // case 4: "认证失败");
        // case 3: "已认证");
        int tag = QtpayAppData.getInstance(MoreIncreateActivity.this.getApplicationContext()).getAuthenFlag();
        //是否开启商户认证功能
        int isOpenMerchanttag = RyxAppdata.getInstance(MoreIncreateActivity.this.getApplicationContext()).getIsOpenMerchantFlag();
        LogUtil.showLog("tag==" + tag);
        switch (tag) {
            case 0:
                if (isOpenMerchanttag == 1) {
                    intent = new Intent(MoreIncreateActivity.this.getApplicationContext(), Authenticate_.class);
                } else {
                    if(RyxAppdata.getInstance(MoreIncreateActivity.this).isAuthswitchOpen()){
                        //启用新的实名认证流程
                        intent = new Intent(this.getApplicationContext(), IdCardUploadAct_.class);
                    }else{
                        intent = new Intent(MoreIncreateActivity.this, UserInfoAddActivity_.class);
                    }
                }
                break;
            case 1:
            case 5:
                if (QtpayAppData
                        .getInstance(MoreIncreateActivity.this.getApplicationContext())
                        .getUserType().equals("00")) {
                    if(RyxAppdata.getInstance(MoreIncreateActivity.this).isAuthswitchOpen()){
                        intent = new Intent(this.getApplicationContext(), IdCardUploadAct_.class);
                    }else{
                        intent = new Intent(MoreIncreateActivity.this,
                            UserAuthPhotoUploadActivity_.class);
                    }
                } else if (QtpayAppData
                        .getInstance(MoreIncreateActivity.this.getApplicationContext())
                        .getUserType().equals("01")) {
                    intent = new Intent(MoreIncreateActivity.this.getApplicationContext(),
                            MerchantCredentialsUpload_.class);
                }
                break;
            case 2:
            case 3:
            case 4:
                if(RyxAppdata.getInstance(MoreIncreateActivity.this).isAuthswitchOpen()){
                    intent = new Intent(this.getApplicationContext(),
                            NewAuthResultAct_.class).putExtra("PowerMsg", powermsg);
                }else {
                    intent = new Intent(MoreIncreateActivity.this,
                            AuthResultActivity_.class).putExtra("PowerMsg", powermsg);
                }
                break;
        }
        try {
            startActivityForResult(intent,RyxAppconfig.CLOSE_HOMEFRG_REQ);
        } catch (Exception e) {
            LogUtil.showToast(MoreIncreateActivity.this.getApplicationContext(), "当前用户状态有误!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RyxAppconfig.CLOSE_HOMEFRG_REQ&&resultCode==RyxAppconfig.CLOSE_NEWAUTHRESULT_RSP){
            //跳转实名认证结果页
            Intent  intent = new Intent(this,
                    NewAuthResultAct_.class).putExtra("PowerMsg", "-1");
            startActivity(intent);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
