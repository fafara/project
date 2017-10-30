package com.ryx.payment.ruishua.convenience.swiping;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qtpay.qtjni.QtPayEncode;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.DeviceInfo;
import com.ryx.payment.ruishua.bean.ICPublickeyInfo;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.bean.TradeSignSlipInfo;
import com.ryx.payment.ruishua.convenience.CardBalanceResultActivity_;
import com.ryx.payment.ruishua.convenience.OrderSignature_;
import com.ryx.payment.ruishua.convenience.PaymentSuccessful_;
import com.ryx.payment.ruishua.convenience.SignRequisitions_;
import com.ryx.payment.ruishua.convenience.SwiperMsgUploadActivity_;
import com.ryx.payment.ruishua.dialog.AudioTypesDialog;
import com.ryx.payment.ruishua.dialog.BluetoothDialog;
import com.ryx.payment.ruishua.dialog.ICDownloadDialog;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.DialogUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.widget.RyxLoadDialog;
import com.ryx.payment.ruishua.widget.RyxLoadDialogBuilder;
import com.ryx.ryxkeylib.listener.CardTipPwdListener;
import com.ryx.ryxkeylib.view.CardpwdInputDialog;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.MapUtil;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 刷卡交易View操作Activity
 */
@EActivity(R.layout.activity_swiping_card)
public class SwipingCardActivity extends BaseActivity implements SwipingService.Callback,ServiceConnection,CardTipPwdListener {
    @ViewById
    ImageView shuaka;
    private SwipingService swipingService;
    /**
     * 操作类型,查询还是交易
     */
    String actiontype,orderid, amount,myshareblueDeviceMac="";
    /***
     * 订单对象
     */
    OrderInfo orderinfo;
    /**
     * Dialog
     */
    Dialog dialog, dialog_ic, myDialog;
    /**
     * 等待框
     */
    RyxLoadDialogBuilder messagedialog;
    /**
     * 蓝牙提示dialog
     */
    BluetoothDialog bluelistdialog;
    /**
     * 密码输入框
     */
    CardpwdInputDialog inputDialog;
    ICDownloadDialog dialog_download; // 公钥更新ui
    private String step = "publickey", cardInfo,cardNo,pasamId,cardtype,icdata,serialnum,ic_ksn,showunplugin="",longitude, latitude,translognumber,cardpsw = "000000";
    private int downindex,writresult,SHOWBANLANCE = 11,PAYWITHSIGN = 10;
    private byte[] writeresuiltScript, writedata,signdata;
    private final int STARTSWIPERCARDCODE = 0x888,STRIPECARDJUDEFLAG = 0x9991, DEVBINDINGFLAG = 0x1704;
    RyxPayResult myRyxPayResult;
    TradeSignSlipInfo signSlipInfo;
    private TradeDetailInfo tradinfo = new TradeDetailInfo();
    Param keyLogNoParam,qtpayCardPassword,qtpayOrderId,qtpayIcdata,qtpaySerialNum,qtpayCardType,qtpayProvinceId,cardIdx,qtpayMerchantId,qtpayProductId,qtpayOrderAmt,qtpayCardInfo;
    ArrayList<ICPublickeyInfo> publickeyInfolist = new ArrayList<ICPublickeyInfo>(); // 公钥下载
    ArrayList<String> list; // aid list
    boolean needpublickey = true;
    @AfterViews
    public void initView(){
        setTitleLayout("请刷卡", true, false);
        Glide.with(SwipingCardActivity.this).load(R.drawable.img_shoukuan_shuaka).into(shuaka);
        keyLogNoParam = new Param("keyLogNo");
    }
    /**
     * 初始化传过来的参数
     */
    private void iniIntentData() {
        actiontype = getIntent().getStringExtra("ActionType");
        if ("balance".equals(actiontype)) {
            orderid = "0000000000000000";
            amount = "000000000000";
        } else if ("DEVBINDING".equals(actiontype)) {
            orderid = "0000000000000000";
            amount = "000000000000";
        } else {
            Bundle bundle = getIntent().getExtras();
            orderinfo = (OrderInfo) bundle.get("orderinfo");
            amount = orderinfo.getRealAmt();
            orderid = orderinfo.getOrderId();
        }
        initQtPatParams();
    }
    /**
     * 初始化对话框
     */
    private void initDialog(){
        dialog = new SimpleDialog(SwipingCardActivity.this);
        dialog.setContentView(R.layout.dialog_swipe_card);
        dialog.setCanceledOnTouchOutside(false);
        dialog_ic = new SimpleDialog(SwipingCardActivity.this);
        dialog_ic.setContentView(R.layout.dialog_swipe_card_ic);

        myDialog = new SimpleDialog(SwipingCardActivity.this);
        myDialog.setContentView(R.layout.dialog_swipe_card_ic);

        messagedialog = new RyxLoadDialog().getInstance(SwipingCardActivity.this);
        messagedialog.setCanceledOnTouchOutside(false);
        messagedialog.setCancelable(false);
        bluelistdialog = new BluetoothDialog(SwipingCardActivity.this,swipingService.blueToothListener);
        bluelistdialog.setCanceledOnTouchOutside(false);

        inputDialog = new CardpwdInputDialog(SwipingCardActivity.this, com.ryx.ryxkeylib.R.style.mydialog, this, true);
        inputDialog.setCanceledOnTouchOutside(false);
        dialog_download = new ICDownloadDialog(SwipingCardActivity.this);
        dialog_download.setCanceledOnTouchOutside(false);
    }
    @Override
    public void requestSuccess() {
        super.requestSuccess();
        /**
         * 绑定刷卡服务
         */
        bindService();
    }
    private void bindService() {
        Intent bindIntent = new Intent(this.getApplicationContext(), SwipingService.class);
        this.getApplicationContext().bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
    }
    /**
     * 服务监听Connected
     * @param service
     */
    @Override
    public void onServiceConnected(ComponentName componentname, final IBinder service) {
        LogUtil.showLog("onServiceConnected================"+ DateUtil.getDateTime(new Date()));
        swipingService = ((SwipingService.SwipingBinder) service).getService();
        swipingService.setSwipingCallback(SwipingCardActivity.this);
        //初始化上个页面传送过来的数据
        iniIntentData();
        //初始化对话框组件
        initDialog();
        if(swipingService.isWiredHeadsetOn()){
            //音频
            final String waring = MessageFormat.format(getResources().getString(R.string.swiperAudiowaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
            requesDevicePermission(waring, 0x0012, new PermissionResult() {
                        @Override
                        public void requestSuccess() {
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "音频权限requestSuccess==" + DateUtil.getDateTime(new Date()));
                            swipingService.initAudioDev();
                        }

                        @Override
                        public void requestFailed() {
                            LogUtil.showToast(SwipingCardActivity.this, waring);
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "音频权限requestFailed==" + DateUtil.getDateTime(new Date()));
                        }
                    },
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS);
        }else{
            //蓝牙
            final String waring = MessageFormat.format(getResources().getString(R.string.swiperBluewaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
            requesDevicePermission(waring, 0x0013, new PermissionResult() {
                        @Override
                        public void requestSuccess() {
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "蓝牙权限requestSuccess==" + DateUtil.getDateTime(new Date()));
                            // 初始化并且搜索蓝牙
                            swipingService.initBluthDev();
                        }
                        @Override
                        public void requestFailed() {
                            LogUtil.showToast(SwipingCardActivity.this, waring);
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "蓝牙权限requestFailed==" + DateUtil.getDateTime(new Date()));
                        }
                    },
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH
            );
        }
    }
    /**
     * 服务监听Disconnected
     * @param name
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        LogUtil.showLog("onServiceDisconnected================"+ DateUtil.getDateTime(new Date()));
        LogUtil.showToast(SwipingCardActivity.this,"服务连接断开,请重新再试!");
        swipingService=null;
        exitswiper(null);
    }
    /**
     * 退出刷卡
     */
    private void exitswiper(Exitswiperlisten exitswiperlisten) {
        if(exitswiperlisten!=null){
            exitswiperlisten.exitswipersuccess();
        }else{
            finish();
        }
    }
    /**
     * 刷卡服务数据回调
     */
    @Override
    public void onSwipingResult(int respcode, Map<String, Object> map) {
        if (respcode == RyxSwiperCode.ACTION_SWIPER_SUCCESS) {
            LogUtil.showLog("ACTION_SWIPER_SUCCESS");
            cardInfo = MapUtil.getString(map, "card_info");
            cardtype = MapUtil.getString(map, "card_type");
            icdata = MapUtil.getString(map, "ic_data");
            cardNo = MapUtil.getString(map, "card_no");
            pasamId = MapUtil.getString(map, "pasamId");
            serialnum = MapUtil.getString(map, "icsernum");
            LogUtil.showLog("cardInfo==" + cardInfo + ",cardtype==" + cardtype + ",icdata=" + icdata + ",serialnum==" + serialnum);
            try {
                messagedialog.dismiss();
                dialog_ic.dismiss();
                translognumber = CryptoUtils.getInstance().getTransLogNo();
                LogUtil.showLog(translognumber);
                keyLogNoParam.setValue(translognumber);
                qtpayTransLogNo.setValue(translognumber);
                inputDialog.show();
                if ("balance".equals(actiontype)) {
                    inputDialog.setTip("");
                } else {
                    inputDialog.setTip(MessageFormat.format(getResources()
                                    .getString(R.string.tips_mini_itron_pay),
                            MoneyEncoder.decodeFormat(amount)));
                }
            } catch (Exception e) {
            }
        }else if (respcode == RyxSwiperCode.ACTION_SWIPER_SUCCESS_WITHPIN) {
            LogUtil.showLog("ACTION_SWIPER_SUCCESS_WITHPIN");
            cardInfo = MapUtil.getString(map, "card_info");
            cardtype = MapUtil.getString(map, "card_type");
            icdata = MapUtil.getString(map, "ic_data");
            serialnum = MapUtil.getString(map, "icsernum");
            cardNo = MapUtil.getString(map, "card_no");
            pasamId = MapUtil.getString(map, "pasamId");
            dialog.dismiss();
            dialog_ic.dismiss();
            translognumber = CryptoUtils.getInstance().getTransLogNo();
            com.ryx.payment.ruishua.utils.LogUtil.showLog(translognumber);
            keyLogNoParam.setValue(translognumber);
            qtpayTransLogNo.setValue(translognumber);
            stripeCardJude();
        }else if (respcode == RyxSwiperCode.ACTION_GETKSN_SUCCESS) {
            ic_ksn = MapUtil.getString(map, "dev_ksn");
            messagedialog.dismiss();
            //获取设备ksn信息
            if ("DEVBINDING".equals(actiontype)) {
                exitswiper(new Exitswiperlisten() {
                    @Override
                    public void exitswipersuccess() {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("ic_ksn", ic_ksn);
                        setResult(DEVBINDINGFLAG, resultIntent);
                        finish();
                    }
                });
                return;
            }
            //新增获取设备类型，判断是否必须为MPOS
            boolean isMustMpos = false;
            if (orderinfo != null) {
                isMustMpos = orderinfo.getIsMustMpos();
            }
            LogUtil.showLog("isMustMpos==" + isMustMpos);
            if (isMustMpos) {
                int mydeviceType = swipingService.getDeviceType();
                LogUtil.showLog("mydeviceType==" + mydeviceType);
                if (!(RyxSwiperCode.DEVICE_TYPE_KEYBOARD_MPOS == mydeviceType || RyxSwiperCode.DEVICE_TYPE_KEYBOARD_ORDINARY == mydeviceType)) {
                    RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(SwipingCardActivity.this, new ConFirmDialogListener() {
                        @Override
                        public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                            ryxSimpleConfirmDialog.dismiss();
                            exitswiper(null);
                        }
                        @Override
                        public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                            ryxSimpleConfirmDialog.dismiss();
                        }
                    });
                    ryxSimpleConfirmDialog.show();
                    ryxSimpleConfirmDialog.setContent("只允许使用密码键盘设备进行交易!");
                    ryxSimpleConfirmDialog.setOnlyokLinearlayout();
                    return;
                }
            }
            //是否需要公钥
            needpublickey = swipingService.isNeeddownloadKey();
            if (needpublickey) {
                //获取公钥
                getPublickey();
            } else {
                startSwiperCard(false);
            }
        }else if (respcode == RyxSwiperCode.ACION_SWIPER_DEMOTIONTRADE) {
            String demotionTrade = MapUtil.getString(map, "demotionTrade");
                LogUtil.showToast(SwipingCardActivity.this, demotionTrade);
                startSwiperCard(true);
        }else if (respcode == RyxSwiperCode.ACION_SWIPER_COMMON_ERROR) {
            String msg = MapUtil.getString(map, "msg");
            LogUtil.showToast(SwipingCardActivity.this, msg);
        }else if (respcode == RyxSwiperCode.ACTION_DOWNLOADRESULT_SUCCESS) {
            downindex = MapUtil.getInt(map, "index");
            int pos = 0;
            if ("publickey".equals(step)) {
                pos = downindex + 1;
            } else {
                pos = downindex + 1 + publickeyInfolist.size();
            }

            if (pos < publickeyInfolist.size()) {
                dialog_download.setTip(pos,
                        publickeyInfolist.size() + list.size());
                swipingService.updateTerminalParams(0, downindex + 1,
                        publickeyInfolist.get(downindex + 1)
                                .getRid()
                                + publickeyInfolist.get(downindex + 1)
                                .getKeyIndex()
                                + publickeyInfolist.get(downindex + 1)
                                .getIcKeys());
                LogUtil.showLog("  key   currestpos===" + pos + "===total==="
                        + publickeyInfolist.size() + list.size()
                        + "   index   " + downindex);
            } else if (pos < publickeyInfolist.size() + list.size()) {
                step = "aid";
                dialog_download.setTip(pos,
                        publickeyInfolist.size() + list.size());
                swipingService.updateTerminalParams(1, pos - publickeyInfolist.size(),
                        list.get(pos - publickeyInfolist.size()));
                LogUtil.showLog(" aid  currestpos===" + pos + "===total==="
                        + publickeyInfolist.size() + list.size()
                        + "   index   " + downindex);
            }
            if (pos == publickeyInfolist.size() + list.size()) {
                dialog_download.setTip(pos,
                        publickeyInfolist.size() + list.size());
                dialog_download.dismiss();
                LogUtil.showToast(SwipingCardActivity.this, "数据同步成功");
                updatePsam();
            }
        } else if (respcode == RyxSwiperCode.ACTION_DOWNLOADRESULT_FAIL) {
            downindex = MapUtil.getInt(map, "index");
            dialog_download.dismiss();
            LogUtil.showToast(SwipingCardActivity.this, "注入IC参数失败,请稍后再试!");
        }else if(RyxSwiperCode.ACTION_SWIPE_IC_WRITEBACK == respcode) {
            writresult = MapUtil.getInt(map, "result");
            if (map.get("resultScript") != null){
                writeresuiltScript = (byte[]) map.get("resultScript");
            }
            if (map.get("data") != null){
                writedata = (byte[]) map.get("data");
            }
            messagedialog.dismiss();
            if (qtpayApplication.getValue().equals("BankCardBalance.Req")) {
                intentBalance();
            } else if (qtpayApplication.getValue().equals("JFPalCardPaySett.Req")
                    || qtpayApplication.getValue().equals("SmartPayments.Req")
                    || qtpayApplication.getValue().equals("JFPalCardPay.Req")
                    ||qtpayApplication.getValue().equals("TermPledge.Req")
                    ||qtpayApplication.getValue().equals("BuyRuiGoldbean.Req")){
                intentCardPay();
            }
        }else if(SwipingService.STARTSWIPERCARD==respcode){
            startSwiperCard(true);
        }
    }
    /**
     * 网络请求 获取公钥
     */
    private void getPublickey() {
        String psamid = null;
        if (ic_ksn != null && ic_ksn.length() > 16) {
            psamid = ic_ksn.substring(ic_ksn.length() - 16, ic_ksn.length());
        } else {
            psamid = ic_ksn;
        }
        qtpayApplication = new Param("application", "GetPublicKey.Req");
        qtpayAttributeList.add(qtpayApplication);
        Param qtpayInstrVersion = new Param("psamid", psamid);
        qtpayParameterList.add(qtpayInstrVersion);
        httpsPost("GetPublicKeyTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                getpublickeyFromJson(payResult.getData());
                if (needpublickey) {
                    needpublickey = false;
                    dialog_download.show();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            swipingService.updateTerminalParams(0, 0, publickeyInfolist.get(0).getRid()
                                    + publickeyInfolist.get(0).getKeyIndex()
                                    + publickeyInfolist.get(0).getIcKeys());
                        }
                    }).start();
                } else {
                    updateUIEx.sendMessage(updateUIEx.obtainMessage(STARTSWIPERCARDCODE));
                }
            }
        });
    }
    /**
     * 从json数据中解析公钥的信息
     */
    public void getpublickeyFromJson(String jsonstring) {

        if (jsonstring != null && jsonstring.length() > 0) {
            String toastmsg = "";

            try {
                JSONObject jsonObj = new JSONObject(jsonstring);
                toastmsg = (String) jsonObj.getJSONObject("result").getString(
                        "message");
                if ("0000".equals(jsonObj.getJSONObject("result").getString(
                        "resultCode"))) {
                    needpublickey = true;

                    JSONArray keys = jsonObj.getJSONArray("resultBean");
                    for (int i = 0; i < keys.length(); i++) {
                        ICPublickeyInfo publickeyInfo = new ICPublickeyInfo();
                        publickeyInfo.setDownFlag(JsonUtil
                                .getValueFromJSONObject(keys.getJSONObject(i),
                                        "downFlag"));
                        publickeyInfo.setExpiDate(JsonUtil
                                .getValueFromJSONObject(keys.getJSONObject(i),
                                        "expiDate"));
                        publickeyInfo.setIcKeys(JsonUtil
                                .getValueFromJSONObject(keys.getJSONObject(i),
                                        "icKeys"));
                        publickeyInfo.setKeyIndex(JsonUtil
                                .getValueFromJSONObject(keys.getJSONObject(i),
                                        "keyIndex"));
                        publickeyInfo.setRid(JsonUtil.getValueFromJSONObject(
                                keys.getJSONObject(i), "rid"));
                        publickeyInfo.setTimesTamp(JsonUtil
                                .getValueFromJSONObject(keys.getJSONObject(i),
                                        "timesTamp"));
                        publickeyInfo.setVerNo(JsonUtil
                                .getValueFromJSONObject(keys.getJSONObject(i),
                                        "verNo"));

                        publickeyInfolist.add(publickeyInfo);
                    }

                    String[] resultaid = jsonObj.getString("resultAic")
                            .replace("[", "").replace("]", "")
                            .replace("\"", "").split(",");

                    list = new ArrayList<String>();

                    for (int i = 0; i < resultaid.length; i++) {
                        list.add(resultaid[i]);
                    }
                    LogUtil.showLog("resultaid.lenth===" + resultaid.length);
                } else {
                    needpublickey = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 网络请求 更新公钥
     */
    private void updatePsam() {
        String psamid = null;
        if (ic_ksn != null && ic_ksn.length() > 16) {
            psamid = ic_ksn.substring(ic_ksn.length() - 16, ic_ksn.length());
        } else {
            psamid = ic_ksn;
        }
        qtpayApplication = new Param("application", "UpdatePsamid.Req");
        qtpayAttributeList.add(qtpayApplication);
        Param qtpayInstrVersion = new Param("psamid", psamid);
        qtpayParameterList.add(qtpayInstrVersion);
        httpsPost("UpdatePsamidTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(STARTSWIPERCARDCODE));
            }
        });
    }
    /**
     * 磁条卡交易判断是否需要上传资料接口
     */
    public void stripeCardJude() {
        if ("10".equals(cardtype) && !"balance".equals(actiontype) && orderinfo.getIscashCardIntercept()) {
            //调取StripeCardJude接口判断状态，跳转页面
            qtpayApplication = new Param("application", "StripeCardJude.Req");
            Param moneyAmountParam = new Param("amount", String.valueOf(Integer.parseInt(amount)));
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(moneyAmountParam);
            Param cardNoParam = new Param("account", cardNo);
            qtpayParameterList.add(cardNoParam);
            CryptoUtils.getInstance().setTransLogUpdate(true);// translogno 更新
            httpsPost("StripeCardJudeTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    try {
                        String jsonDataStr = payResult.getData();
                        JSONObject jsonObj = new JSONObject(jsonDataStr);
                        String code = JsonUtil.getValueFromJSONObject(jsonObj, "code");
                        if (RyxAppconfig.QTNET_SUCCESS.equals(code)) {
                            String uploadflag = JsonUtil.getValueFromJSONObject(jsonObj, "uploadflag");
                            if ("1".equals(uploadflag) || "2".equals(uploadflag) || "3".equals(uploadflag)) {
                                Intent swiperMsgIntent = new Intent(SwipingCardActivity.this, SwiperMsgUploadActivity_.class);
                                //因资料上传界面拍照耗内存
                                swiperMsgIntent.putExtra("orderinfo", orderinfo);
                                swiperMsgIntent.putExtra("cardInfo", cardInfo);
                                swiperMsgIntent.putExtra("orderid", orderid);
                                swiperMsgIntent.putExtra("cardpsw", cardpsw);
                                swiperMsgIntent.putExtra("icdata", icdata);
                                swiperMsgIntent.putExtra("serialnum", serialnum);
                                swiperMsgIntent.putExtra("cardtype", cardtype);
                                swiperMsgIntent.putExtra("baseprovinceid", baseprovinceid);
                                swiperMsgIntent.putExtra("translognumber", translognumber);
                                swiperMsgIntent.putExtra("uploadflag", uploadflag);
                                swiperMsgIntent.putExtra("account", cardNo);
                                startActivityForResult(swiperMsgIntent, STRIPECARDJUDEFLAG);
                            } else {
                                updateUIEx.sendEmptyMessage(STRIPECARDJUDEFLAG);
                            }
                        } else {
                            String msg = JsonUtil.getValueFromJSONObject(jsonObj, "msg");
                            LogUtil.showToast(SwipingCardActivity.this, msg + "");
                        }
                    } catch (Exception e) {
                        LogUtil.showToast(SwipingCardActivity.this, "StripeCardJude数据异常,请稍后再试!");
                    }
                }
            });
        } else {
            updateUIEx.sendEmptyMessage(STRIPECARDJUDEFLAG);
        }
    }
    private void intentBalance() {
        Intent itbalance = new Intent(SwipingCardActivity.this, CardBalanceResultActivity_.class);
        itbalance.putExtra("showunplugin", showunplugin);
        itbalance.putExtra("CardBalance", myRyxPayResult.getBalance());
        startActivityForResult(itbalance, SHOWBANLANCE);
    }
    /**
     * UI操作
     */
    public Handler updateUIEx = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STARTSWIPERCARDCODE:
                    //启动刷卡
                    startSwiperCard(false);
                    break;
                case STRIPECARDJUDEFLAG:
                    ordinarySwiper();
                    break;
            }
        }
    };

    /**
     * 启动刷卡
     */
    private void startSwiperCard(boolean isagainStart) {
        if (!isagainStart) {
            try {
                dialog_ic.show();
            } catch (Exception e) {
            }
        }
        Map<String, Object> mapSwiper = new HashMap<String, Object>();
        if ("balance".equals(actiontype)) {
            mapSwiper.put("buss_type", "BankCardBalance");
        } else {
            mapSwiper.put("buss_type", "JFPalCardPay");
        }
        mapSwiper.put("order_id", orderid);
        mapSwiper.put("amount", amount);
        if (isagainStart) {
            swipingService.againSwiperCard(mapSwiper);
        } else {
            swipingService.swiperCard(mapSwiper);
        }
    }
    /**
     * 服务命令
     * @param code
     * @param data
     */
    @Override
    public void onServicecommand(int code, Object data) {
        if(SwipingService.SHOWAUDIOTYPEDIALOG==code&&data instanceof AudioTypesDialog.AudioTypeItemSlectListener){
            //展示音频选择框
            AudioTypesDialog audioTypesDialog = new AudioTypesDialog(SwipingCardActivity.this, (AudioTypesDialog.AudioTypeItemSlectListener)data);
            audioTypesDialog.show();
            audioTypesDialog.RefreshList(RyxSwiperCode.devsNames);
        }else if(SwipingService.SHOWMESSAGEDIALOG==code){
            if(data==null){
                bluelistdialog.show();
            }else{
                messagedialog.setMessage((String)data);
                messagedialog.show();
            }
        }else if(SwipingService.MESSAGEDIALOGDISMISS==code){
            messagedialog.dismiss();
        }else if(SwipingService.BLUELISTDIALOGREFRESHLIST==code){
            boolean isShowing = bluelistdialog.isShowing();
            if (isShowing) {
                bluelistdialog.RefreshList((ArrayList<DeviceInfo>)data);
            }
        }else if(SwipingService.BLUELISTCOMPLETEREFRESHLIST==code){
                boolean isinited = bluelistdialog.isinitedBlueDialog();
                com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "isinited==" + isinited);
                //当前是否正在展示着蓝牙搜索列表Dialog框
                if (!isinited) {
                    messagedialog.dismiss();
                    bluelistdialog.show();
                }
                bluelistdialog.SearchComplete(true);
                bluelistdialog.RefreshList((ArrayList<DeviceInfo>)data);
        }else if(SwipingService.SHOWTOASTMSG==code){
            LogUtil.showToast(SwipingCardActivity.this,(String)data);
        }else if(SwipingService.EXITSWIPER==code){
            exitswiper(null);
        }else if(SwipingService.ICDIALOGDISMISS==code){
            dialog_ic.dismiss();
        }

    }
    @Override
    public void getPwdVal(String pswd) {
        cardpsw = pswd;
        stripeCardJude();
    }
    private void intentCardPay() {
        if (orderinfo.isNeedSign()&& RyxAppconfig.IMPAY_CASHPLEDGE!=orderinfo.getId()&&RyxAppconfig.RUIBEAN_PAY!=orderinfo.getId()) {
            getSignSlip();
        } else {
            Intent intent = new Intent(SwipingCardActivity.this, PaymentSuccessful_.class);
            intent.putExtra("showunplugin", showunplugin);
            intent.putExtra("interfaceTag", orderinfo.getInterfaceTag());
            if(RyxAppconfig.IMPAY_CASHPLEDGE==orderinfo.getId()){
                intent.putExtra("lookmxi", false);
            }
            startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        }
    }

    /**
     * 网络请求 获取签购单
     */
    private void getSignSlip() {
        qtpayApplication = new Param("application", "GetSignSalesSlipInfo.Req");
        qtpayAttributeList.add(qtpayApplication);
        Param qtpayOrderno = new Param("orderId", orderinfo.getOrderId());
        Param qtpayFlag = new Param("flag", "0");

        qtpayParameterList.add(qtpayOrderno);
        qtpayParameterList.add(qtpayFlag);

        try {
            Thread.sleep(50); // 歇一会，等以上操作完成再启动线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        httpsPost("GetSignSalesSlipInfoTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                getSaleSlipFromJson(payResult.getData());

                intentactivity();
            }
        });
    }
    /**
     * 从json数据中解析签购单的信息
     */
    public void getSaleSlipFromJson(String jsonstring) {

        if (jsonstring != null && jsonstring.length() > 0) {
            String toastmsg = "";

            try {
                JSONObject jsonObj = new JSONObject(jsonstring);

                JSONArray keys = jsonObj.getJSONArray("resultBean");
                signSlipInfo = new TradeSignSlipInfo();
                signSlipInfo.setHostMerchantName(JsonUtil
                        .getValueFromJSONObject(keys.getJSONObject(0),
                                "hostMerchantName"));
                signSlipInfo.setAcqMerchantId(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "acqMerchantId"));
                signSlipInfo.setAcqTermId(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "acqTermId"));
                signSlipInfo.setOperatorNum(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "operatorNum"));
                signSlipInfo.setBankNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "bankNo"));
                signSlipInfo.setBankName(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "bankName"));
                signSlipInfo.setAccount(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "account"));
                signSlipInfo.setAcqBranchName(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "acqBranchName"));
                signSlipInfo.setTradeType(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "tradeType"));
                signSlipInfo.setValid(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "valid"));
                signSlipInfo.setBatchNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "batchNo"));
                signSlipInfo.setHostLogNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "hostLogNo"));
                signSlipInfo.setHostAuthCode(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "hostAuthCode"));
                signSlipInfo.setDate(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "date"));
                signSlipInfo.setOrderId(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "orderId"));
                signSlipInfo.setHostRefNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "hostRefNo"));
                signSlipInfo.setAmount(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "amount"));
                signSlipInfo.setTradeType2(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "tradeType2"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RyxAppconfig.QT_RESULT_OK) {
            if (requestCode == PAYWITHSIGN) {
                signdata = (byte[]) data.getExtras().get("signarray");
                longitude = data.getExtras().getString("longitude");
                latitude = data.getExtras().getString("latitude");
                cardInfo = data.getExtras().getString("cardInfo");
                orderid = data.getExtras().getString("orderid");
                cardpsw = data.getExtras().getString("cardpsw");
                icdata = data.getExtras().getString("icdata");
                serialnum = data.getExtras().getString("serialnum");
                cardtype = data.getExtras().getString("cardtype");
                baseprovinceid = data.getExtras().getString("baseprovinceid");
                translognumber = data.getExtras().getString("translognumber");
                initQtPatParams();
                keyLogNoParam.setValue(translognumber);
                qtpayTransLogNo.setValue(translognumber);
                doRequest();
            } else if (requestCode == SHOWBANLANCE) {
                String result = data.getExtras().getString("result");// 得到新Activity
                // 关闭后返回的数据
                if ("Finish".equals(result)) {
                    exitswiper(new Exitswiperlisten() {
                        @Override
                        public void exitswipersuccess() {
                            // TODO Auto-generated method stub
                            setResult(RyxAppconfig.CLOSE_ALL);
                            finish();
                        }
                    });

                } else {
                    //重新刷卡
                    startSwiperCard(false);
                }
            } else if (requestCode == STRIPECARDJUDEFLAG) {
                orderinfo = (OrderInfo) data.getExtras().get("orderinfo");
                cardInfo = data.getExtras().getString("cardInfo");
                orderid = data.getExtras().getString("orderid");
                cardpsw = data.getExtras().getString("cardpsw");
                icdata = data.getExtras().getString("icdata");
                serialnum = data.getExtras().getString("serialnum");
                cardtype = data.getExtras().getString("cardtype");
                baseprovinceid = data.getExtras().getString("baseprovinceid");
                translognumber = data.getExtras().getString("translognumber");
                //资料补充界面返回
                updateUIEx.sendEmptyMessage(STRIPECARDJUDEFLAG);
            }

        } else if (resultCode == RyxAppconfig.QT_RESULT_SIGN_CANCEL) {
            if (requestCode == PAYWITHSIGN) {
                exitswiper(null);
            }
        } else if (RyxAppconfig.CLOSE_AT_SWIPER == resultCode) {
            com.ryx.payment.ruishua.utils.LogUtil.showLog("child_close_all");
            exitswiper(new Exitswiperlisten() {
                @Override
                public void exitswipersuccess() {
                    setResult(RyxAppconfig.CLOSE_ALL, data);
                    finish();
                }
            });
        } else if (resultCode == RyxAppconfig.QT_RESULT_STRIPECARD_CANCEL) {
            if (requestCode == STRIPECARDJUDEFLAG) {
                exitswiper(new Exitswiperlisten() {
                    @Override
                    public void exitswipersuccess() {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
            }
        }
        else
        {
            finish();
        }
    }
    public void doRequest() {
        if ("balance".equals(actiontype)) {
            getBankCardBalance();
        } else {
            doJFPalCardPay();
        }
    }
    public void intentactivity() {
        // 签购单经纬度
        tradinfo.setLongitude(longitude);
        tradinfo.setLatitude(latitude);
        Intent intent = new Intent(SwipingCardActivity.this, SignRequisitions_.class);
        intent.putExtra("showunplugin", showunplugin);
        intent.putExtra("tradeDetailInfo", tradinfo);
        intent.putExtra("tradeSlipInfo", signSlipInfo);
        intent.putExtra("signarray", signdata);
        intent.putExtra("fromtype", RyxAppconfig.CREATE_SIGNREQUISITIONS);
        startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
    }
    /**
     * 余额查询
     */
    public void getBankCardBalance() {
        qtpayApplication = new Param("application", "BankCardBalance.Req");
        qtpayCardInfo = new Param("cardInfo", cardInfo);
        qtpayCardPassword = new Param("cardPassword",
                QtPayEncode.encryptCardPwd(orderid, cardpsw,
                        RyxAppconfig.DEBUG));
        qtpayOrderId = new Param("orderId", orderid);
        com.ryx.payment.ruishua.utils.LogUtil.showLog("cardpsw+++++" + cardpsw);
        qtpayIcdata = new Param("icData", icdata);
        qtpaySerialNum = new Param("serialNum", serialnum);
        qtpayCardType = new Param("cardType", cardtype);
        qtpayProvinceId = new Param("bankProvinceId", baseprovinceid);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardInfo);
        qtpayParameterList.add(qtpayCardPassword);
        qtpayParameterList.add(qtpayOrderId);
        qtpayParameterList.add(qtpayIcdata);
        qtpayParameterList.add(qtpaySerialNum);
        qtpayParameterList.add(qtpayCardType);
        qtpayParameterList.add(qtpayProvinceId);
        qtpayParameterList.add(keyLogNoParam);
        httpsPost("BankCardBalanceTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                myRyxPayResult = payResult;
                if ("11".equals(cardtype)) {
                    showunplugin = "show";
                    LogUtil.showLog("icdata ===" + payResult.getIcData());
                    messagedialog.setMessage("ic回写中，请稍后...");
                    messagedialog.show();
                    if (null != payResult.getIcData()
                            && !"null".equals(payResult.getIcData())) {
                        swipingService.icWriteBack("00", payResult.getIcData());
                    } else {
                        swipingService.icWriteBack("00", "");
                    }
                } else {
                    showunplugin = "";
                    intentBalance();
                }
            }
        });
    }
    /**
     * 公用刷卡方法
     */
    public void doJFPalCardPay() {
        String merchantId = orderinfo.getMerchantId();
        qtpayMerchantId = new Param("merchantId", orderinfo.getMerchantId());
        if ("01".equals(orderinfo.getInterfaceTag())) {
            qtpayApplication = new Param("application", "JFPalCardPaySett.Req");
        } else if ("12".equals(orderinfo.getInterfaceTag())) {
            qtpayApplication = new Param("application", "SmartPayments.Req");
            qtpayParameterList.add(new Param("servcode", orderinfo.getServcode()));
            qtpayParameterList.add(new Param("tradecode", orderinfo.getTradecode()));
        } else if ("14".equals(orderinfo.getInterfaceTag())) {
            qtpayApplication = new Param("application", "TermPledge.Req");
        } else if("15".equals(orderinfo.getInterfaceTag())){
            qtpayApplication = new Param("application", "BuyRuiGoldbean.Req");
        }
        else {
            qtpayApplication = new Param("application", "JFPalCardPay.Req");
        }
        qtpayMerchantId = new Param("merchantId", orderinfo.getMerchantId());
        qtpayProductId = new Param("productId", orderinfo.getProductId());

        qtpayOrderAmt = new Param("orderAmt",
                MoneyEncoder.encodeToPost(orderinfo.getOrderAmt()));
        qtpayCardInfo = new Param("cardInfo", cardInfo);
        qtpayCardPassword = new Param("cardPassword",
                QtPayEncode.encryptCardPwd(orderid, cardpsw,
                        RyxAppconfig.DEBUG));
        qtpayOrderId = new Param("orderId", orderid);
        qtpayIcdata = new Param("icData", icdata);
        qtpaySerialNum = new Param("serialNum", serialnum);
        qtpayCardType = new Param("cardType", cardtype);
        qtpayProvinceId = new Param("bankProvinceId", baseprovinceid);
        String cardIdxss = orderinfo.getCardIdx();
        cardIdx = new Param("cardIdx", TextUtils.isEmpty(cardIdxss) ? "" : cardIdxss);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayMerchantId);
        qtpayParameterList.add(qtpayProductId);
        qtpayParameterList.add(qtpayOrderAmt);
        qtpayParameterList.add(qtpayCardInfo);
        qtpayParameterList.add(qtpayCardPassword);
        qtpayParameterList.add(qtpayOrderId);
        qtpayParameterList.add(qtpayIcdata);
        qtpayParameterList.add(qtpaySerialNum);
        qtpayParameterList.add(qtpayCardType);
        qtpayParameterList.add(qtpayProvinceId);
        qtpayParameterList.add(cardIdx);
        qtpayParameterList.add(keyLogNoParam);
        if (!TextUtils.isEmpty(orderinfo.getPayee())) {
            qtpayParameterList.add(new Param("payee", orderinfo.getPayee()));
        }
        httpsPost("JFPalCardPayTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                com.ryx.payment.ruishua.utils.LogUtil.showToast(SwipingCardActivity.this, payResult.getRespDesc());
                if ("11".equals(cardtype)) {
                    showunplugin = "show";
                    messagedialog.setMessage("ic回写中，请稍后...");
                    messagedialog.show();
                    if (null != payResult.getIcData()
                            && !"null".equals(payResult.getIcData())) {
                        swipingService.icWriteBack("00", payResult.getIcData());
                    } else {
                        swipingService.icWriteBack("00", "");
                    }
                } else {
                    showunplugin = "";
                    intentCardPay();
                }
            }
            @Override
            public void onOtherState(String rescode, String resDesc) {
                super.onOtherState(rescode, resDesc);
                DialogUtil.getInstance(SwipingCardActivity.this).showSwipingDialog(resDesc, new CompleteResultListen() {
                    @Override
                    public void compleResultok() {
                        exitswiper(null);
                    }
                },true);
            }
        });
    }
    /**
     * 执行签名或者普通交易流程
     */
    public void ordinarySwiper() {
        if ((!"balance".equals(actiontype)) && orderinfo.isNeedSign()) {
            if ("12".equals(orderinfo.getInterfaceTag()) || "14".equals(orderinfo.getInterfaceTag())||"15".equals(orderinfo.getInterfaceTag())) {
                //小贷还款
                Intent it = new Intent(SwipingCardActivity.this, OrderSignature_.class);
                //因签名页面横屏展示，有些手机此时内存吃紧，数据容易丢失，只能传值解决
                it.putExtra("orderinfo", orderinfo);
                it.putExtra("cardInfo", cardInfo);
                it.putExtra("orderid", orderid);
                it.putExtra("cardpsw", cardpsw);
                it.putExtra("icdata", icdata);
                it.putExtra("serialnum", serialnum);
                it.putExtra("cardtype", cardtype);
                it.putExtra("baseprovinceid", baseprovinceid);
                it.putExtra("translognumber", translognumber);
                //此处费率展示为0
                it.putExtra("rateStr", "本次手续费:0.00元");
                startActivityForResult(it, PAYWITHSIGN);
            } else {
                qtpayApplication = new Param("application", "FindTermLs.Req");
                qtpayAttributeList.add(qtpayApplication);
                qtpayParameterList.add(new Param("psamid", pasamId));
                qtpayParameterList.add(new Param("merchantId", orderinfo.getMerchantId()));
                qtpayParameterList.add(new Param("productId", orderinfo.getProductId()));
                qtpayParameterList.add(new Param("type", "01"));
                qtpayParameterList.add(new Param("orderId",orderid));
                qtpayParameterList.add(new Param("amount",amount));
                qtpayParameterList.add(new Param("IcData",icdata));
                LogUtil.showLog("icdata=="+icdata+",money=="+amount);
                httpsPost("FindTermLsTag", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(RyxPayResult payResult) {
                        try {
                            JSONObject jsonObj = new JSONObject(payResult.getData());
                            JSONArray reSultArray = jsonObj.getJSONArray("resultBean");
                            JSONObject reSultObject = reSultArray.getJSONObject(0);
                            String status = reSultObject.getString("status");
                            if ("0".equals(status)) {
                                JSONArray resultfeelistArray = reSultObject.getJSONArray("resultfeelist");
                                StringBuffer stringBuffer=new StringBuffer();
                                for (int i=0;i<resultfeelistArray.length();i++){
                                    JSONObject resultfeeObj=resultfeelistArray.getJSONObject(i);
                                    String val= JsonUtil.getValueFromJSONObject(resultfeeObj,"val");
                                    String name= JsonUtil.getValueFromJSONObject(resultfeeObj,"name");
                                    stringBuffer.append(name+val+";");
                                }
                                Intent it = new Intent(SwipingCardActivity.this, OrderSignature_.class);
                                //因签名页面横屏展示，有些手机此时内存吃紧，数据容易丢失，只能传值解决
                                it.putExtra("orderinfo", orderinfo);
                                it.putExtra("cardInfo", cardInfo);
                                it.putExtra("orderid", orderid);
                                it.putExtra("cardpsw", cardpsw);
                                it.putExtra("icdata", icdata);
                                it.putExtra("serialnum", serialnum);
                                it.putExtra("cardtype", cardtype);
                                it.putExtra("baseprovinceid", baseprovinceid);
                                it.putExtra("translognumber", translognumber);
                                //此处请求网络获取当前设备费率
                                //手续费
                                it.putExtra("moneyratedisplay",stringBuffer.toString());
                                startActivityForResult(it, PAYWITHSIGN);
                            } else {
                                String msg = reSultObject.getString("msg");
                                LogUtil.showToast(SwipingCardActivity.this, msg + "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.showToast(SwipingCardActivity.this, "费率信息异常,请联系代理重新设置!");
                        }
                    }
                });
            }
        } else {
            doRequest();
        }
    }
    /**
     * 释放资源完毕监听
     *
     * @author Administrator
     */
    private interface Exitswiperlisten {
        /**
         * 释放资源完毕后监听
         */
        void exitswipersuccess();
    }

}
