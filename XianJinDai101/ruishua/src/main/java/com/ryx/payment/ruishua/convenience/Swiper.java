package com.ryx.payment.ruishua.convenience;

import android.Manifest;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qtpay.qtjni.QtPayEncode;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.ruiyinxin.bluesearch.adapter.BlueToothSearchCommonAdapter;
import com.ruiyinxin.bluesearch.bean.BlueToothSearchStatus;
import com.ruiyinxin.bluesearch.listener.BlueToothSearchListener;
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
import com.ryx.payment.ruishua.dialog.AudioTypesDialog;
import com.ryx.payment.ruishua.dialog.BluetoothDialog;
import com.ryx.payment.ruishua.dialog.ICDownloadDialog;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.BlueToothListener;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.ScanningPayResultActivity_;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.widget.RyxLoadDialog;
import com.ryx.payment.ruishua.widget.RyxLoadDialogBuilder;
import com.ryx.ryxkeylib.listener.CardTipPwdListener;
import com.ryx.ryxkeylib.view.CardpwdInputDialog;
import com.ryx.swiper.IRyxSwiperListener;
import com.ryx.swiper.RyxSwiper;
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
 * @author XUCC
 */
@EActivity(R.layout.activity_swiper)
public class Swiper extends BaseActivity implements BlueToothListener, CardTipPwdListener {
    /**
     * 跳转资料补充页Flag
     */
    private final int STRIPECARDJUDEFLAG = 0x9991;
    /**
     * 设备绑定result
     */
    private final int DEVBINDINGFLAG = 0x1704;
    /***
     * 刷卡走快捷跳转等待结果页
     */
    private final int TOSCANNINGPAYRESULT=0x1705;
    private byte[] signdata = null;
    /**
     * 音频管理器
     */
    AudioManager localAudioManager;
    /**
     * 操作类型,查询还是交易
     */
    String actiontype;
    /**
     * 磁条卡信息 磁道信息+MAC
     */
    private String cardInfo;
    private String cardNo;
    private String pasamId;
    /***
     * 卡密码
     */
    private String cardpsw = "000000";// 卡密码
    /**
     * 封装刷卡组件
     */
    private RyxSwiper ryxSwiper;
    /**
     * 设备类型标志
     */
    private int type;
    /**
     * 订单号,金额
     */
    String orderid, amount;
    /***
     * 订单对象
     */
    OrderInfo orderinfo;
    /***
     * 本地保存的设备Mac值
     */
    private String myshareblueDeviceMac = "";
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
     * 蓝牙搜素适配器
     */
    BlueToothSearchCommonAdapter blueToothSearchCommonAdapter;
    /**
     * 设备列表
     */
    private ArrayList<DeviceInfo> deviceinfo = new ArrayList<DeviceInfo>();
    /**
     * 密码输入框
     */
    CardpwdInputDialog inputDialog;
    private int PAYWITHSIGN = 10;
    private int SHOWBANLANCE = 11;
    ICDownloadDialog dialog_download; // 公钥更新ui
    private boolean isshowBlueDialog = true;
    private DeviceInfo devinfo;
    private String currentConnectblueDeviceMac = "";
    private String icdata = "", serialnum = "", cardtype = "";
    private int downindex;
    private final int STARTSWIPERCARDCODE = 0x888;
    private int writresult;
    private byte[] writeresuiltScript, writedata;
    private boolean icbackruning = false;// 是否正在回写 回写失败后继续跳转
    private String translognumber;
    private String step = "publickey";
    ArrayList<ICPublickeyInfo> publickeyInfolist = new ArrayList<ICPublickeyInfo>(); // 公钥下载
    ArrayList<String> list; // aid list
    private boolean downresult;
    private String ic_ksn;// ic刷卡器ksn 区分是否初始化过
    boolean needpublickey = true;
    Param qtpayMerchantId;
    Param qtpayProductId;
    Param qtpayOrderAmt;
    Param qtpayCardInfo;
    Param qtpayCardPassword;
    Param qtpayOrderId;
    Param keyLogNoParam;
    Param qtpayIcdata;
    Param qtpaySerialNum;
    Param qtpayCardType;
    Param qtpayProvinceId, qtpayCityId;
    Param cardIdx;
    private String showunplugin = "";
    TradeSignSlipInfo signSlipInfo;
    private String longitude, latitude;
    private TradeDetailInfo tradinfo = new TradeDetailInfo();
    RyxPayResult myRyxPayResult;
    @ViewById
    ImageView shuaka;
    private boolean isfinishing = false;
    private boolean isDevInited = false;
    //判断蓝牙是都连接过，蓝牙连接过则不进行第二次连接。
    // 处理SwiperMsgUploadActivity个别手机拍照过程出现闪退导致重复连接问题
    private boolean isBlueConnectEd = false;

    @AfterViews
    public void initView() {
        keyLogNoParam = new Param("keyLogNo");
        LogUtil.showLog("Swiper===============iniView");
        setTitleLayout("请刷卡", true, false);
        Glide.with(Swiper.this).load(R.drawable.img_shoukuan_shuaka).into(shuaka);
    }

    /**
     * 初始化设备资源
     */
    private void initDev() {
        localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        actiontype = getIntent().getStringExtra("ActionType");
        if ("balance".equals(actiontype)) {
            orderid = "0000000000000000";
            amount = "000000000000";
        } else if ("DEVBINDING".equals(actiontype)) {
            orderid = "0000000000000000";
            amount = "000000000000";
        } else {
            Bundle bundle = getIntent().getExtras();
            String ryxcreditStr = bundle.getString("ryxcredit");
            orderinfo = (OrderInfo) bundle.get("orderinfo");
            amount = orderinfo.getRealAmt();
            orderid = orderinfo.getOrderId();
        }
        initQtPatParams();
        dialog = new SimpleDialog(Swiper.this);
        dialog.setContentView(R.layout.dialog_swipe_card);
        dialog.setCanceledOnTouchOutside(false);
        dialog_ic = new SimpleDialog(Swiper.this);
        dialog_ic.setContentView(R.layout.dialog_swipe_card_ic);

        myDialog = new SimpleDialog(Swiper.this);
        myDialog.setContentView(R.layout.dialog_swipe_card_ic);

//        initdialog = RyxLoadDialog.getInstance(Swiper.this);
//        initdialog.setCanceledOnTouchOutside(false);

        messagedialog = new RyxLoadDialog().getInstance(Swiper.this);
        messagedialog.setCanceledOnTouchOutside(false);
        messagedialog.setCancelable(false);
        bluelistdialog = new BluetoothDialog(Swiper.this, deviceinfo, Swiper.this);
        bluelistdialog.setCanceledOnTouchOutside(false);

        inputDialog = new CardpwdInputDialog(Swiper.this, com.ryx.ryxkeylib.R.style.mydialog, this, true);
        inputDialog.setCanceledOnTouchOutside(false);
        dialog_download = new ICDownloadDialog(Swiper.this);
        dialog_download.setCanceledOnTouchOutside(false);
        if (localAudioManager.isWiredHeadsetOn()) {
            String waring = MessageFormat.format(getResources().getString(R.string.swiperAudiowaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
            //音频插入了判断6.0权限
//            if (RyxAppconfig.BRANCH.equals("01")) {
//                waring = MessageFormat.format(getResources().getString(R.string.swiperAudiowaringmsg), getResources().getString(R.string.app_name));
//            }else if (RyxAppconfig.BRANCH.equals("02")) {
//                waring = MessageFormat.format(getResources().getString(R.string.swiperAudiowaringmsg), getResources().getString(R.string.app_name_ryx));
//            }
            final String finalWaring = waring;
            requesDevicePermission(waring, 0x0012, new PermissionResult() {
                        @Override
                        public void requestSuccess() {
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "音频权限requestSuccess==" + DateUtil.getDateTime(new Date()));

                            //仅有一个设备直接初始化设备,无需弹出选择框
                            if (RyxSwiperCode.devsNames.length == 1) {
                                type = RyxSwiperCode.devsCode[0];
                                initAudioDevs();
                            } else {
                                AudioTypesDialog audioTypesDialog = new AudioTypesDialog(Swiper.this, new AudioTypesDialog.AudioTypeItemSlectListener() {
                                    @Override
                                    public void onItemClick(int itemId) {
                                        // TODO Auto-generated method stub
                                        type = RyxSwiperCode.devsCode[itemId];
                                        initAudioDevs();
                                    }
                                });
                                audioTypesDialog.show();
                                audioTypesDialog.RefreshList(RyxSwiperCode.devsNames);
                            }
                        }

                        @Override
                        public void requestFailed() {
                            LogUtil.showToast(Swiper.this, finalWaring);
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "音频权限requestFailed==" + DateUtil.getDateTime(new Date()));
                        }
                    },
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS
            );
        } else {
            /**
             * 开始蓝牙权限及初始化
             */
            startInitBlue();
        }
    }

    /**
     * 展示对话框提示
     * @param msg
     * @param completeResultListen
     */
    private void showSimpleConfirmDialog(final String msg, final CompleteResultListen completeResultListen){
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(Swiper.this, new ConFirmDialogListener() {

                @Override
                public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                    ryxSimpleConfirmDialog.dismiss();
                    completeResultListen.compleResultok();
                }

                @Override
                public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                    ryxSimpleConfirmDialog.dismiss();
                }
            });
            ryxSimpleConfirmDialog.show();
            ryxSimpleConfirmDialog.setContent(msg);
            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
        }
    });
}
    /**
     * 开始蓝牙部分
     */
    private void startInitBlue() {
        //蓝牙判断6.0权限
        String waring = MessageFormat.format(getResources().getString(R.string.swiperBluewaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperBluewaringmsg), getResources().getString(R.string.app_name));
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperBluewaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0013, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "蓝牙权限requestSuccess==" + DateUtil.getDateTime(new Date()));
                        // 初始化并且搜索蓝牙
                        initBlu();
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(Swiper.this, finalWaring);
                        com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "蓝牙权限requestFailed==" + DateUtil.getDateTime(new Date()));
                    }
                },
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }


    // 初始化并且搜索蓝牙设备
    private void initBlu() {
        LogUtil.showLog("initBlu==================");
        if (blueToothSearchCommonAdapter == null) {
            blueToothSearchCommonAdapter = new BlueToothSearchCommonAdapter(
                    Swiper.this, toothSearchListener, 40 * 1000);
        }
        blueToothSearchCommonAdapter.searchBlueDevs(RyxSwiperCode.deviceStartNames);
        if ("".equals(myshareblueDeviceMac)) {
            LogUtil.showLog("myshareblueDeviceMac本地为存储");
            bluelistdialog.show();
        } else {
            LogUtil.showLog("蓝牙正在努力匹配中,请稍等...");
            messagedialog.setMessage("蓝牙正在努力匹配中,请稍等...");
            messagedialog.show();

        }
    }

    // 初始化音频设备
    private void initAudioDevs() {
        //7.0勿打扰模式bug处理
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            showSimpleConfirmDialog("请将音频权限及音量打开退出勿打扰模式后重试!", new CompleteResultListen() {
                @Override
                public void compleResultok() {

                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            getApplicationContext().startActivity(intent);
                    exitswiper(new Exitswiperlisten() {
                        @Override
                        public void exitswipersuccess() {
                            finish();
                        }
                    });
                }
            });
            return;
        }
        messagedialog.setMessage("正在努力连接音频设备,请稍等...");
        messagedialog.show();
        ryxSwiper = new RyxSwiper(Swiper.this, type, ryxSwiperListener);
        rundetect();
    }

    /**
     * 新的设备检测方法
     */
    private void rundetect() {
        ryxSwiper.hasdevice();
    }

    /**
     * 初始化请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        //获取上次连接的蓝牙设备mac
        myshareblueDeviceMac = PreferenceUtil.getInstance(Swiper.this).getString("myblueDeviceMac", "");
    }

    @Override
    public void getBlueToothMac(DeviceInfo deviceInfo) {
        blueToothSearchCommonAdapter.manualstopBlueToothsearch();
        connectSwiper(deviceInfo);
    }

    @Override
    public void againsearch() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startInitBlue();
            }
        });
    }

    // 蓝牙监听
    private BlueToothSearchListener toothSearchListener = new BlueToothSearchListener() {

        @Override
        public void discoverOneDevice(BluetoothDevice buleToothDeviceInfo) {
            if (!"".equals(myshareblueDeviceMac) && myshareblueDeviceMac.equals(buleToothDeviceInfo.getAddress())) {
                isshowBlueDialog = false;
                //如果设备是已经连接过的停止搜索并
                blueToothSearchCommonAdapter.manualstopBlueToothsearch();
                messagedialog.dismiss();
                connectSwiper(new DeviceInfo(buleToothDeviceInfo.getName(), buleToothDeviceInfo.getAddress()));
                return;
            }
            boolean addboolean = true;
            for (int i = 0; i < deviceinfo.size(); i++) {
                if (buleToothDeviceInfo.getAddress().equals(
                        deviceinfo.get(i).getDeviceid())) {
                    addboolean = false;
                    break;
                }
            }
            if (addboolean) {
                devinfo = new DeviceInfo();
                devinfo.setDeviceid(buleToothDeviceInfo.getAddress());
                devinfo.setDevicename(buleToothDeviceInfo.getName());
                deviceinfo.add(devinfo);
                boolean isShowing = bluelistdialog.isShowing();
                if (isShowing) {
                    bluelistdialog.RefreshList(deviceinfo);
                }
            }
        }

        @Override
        public void searchStateListener(int stateflag, String message) {
            if (BlueToothSearchStatus.CLOSESEARCHBLUETOOTH_FLAG == stateflag) {
                LogUtil.showLog("CLOSESEARCHBLUETOOTH=====================" + DateUtil.getDateTime(new Date()));
                //此处判断是当搜索到本地存储的设备并连接了，搜索完成监听不做任何操作，如果未搜索到本地存储的设备则进行展示搜索Dialog框
                if (isshowBlueDialog) {
                    boolean isinited = bluelistdialog.isinitedBlueDialog();
                    com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "isinited==" + isinited);
                    //当前是否正在展示着蓝牙搜索列表Dialog框
                    if (!isinited) {
                        messagedialog.dismiss();
                        bluelistdialog.show();
                    }
                    bluelistdialog.SearchComplete(true);
                    bluelistdialog.RefreshList(deviceinfo);
                }
            }
        }

    };

    /**
     * 连接设备
     *
     * @param deviceInfo
     */
    private void connectSwiper(final DeviceInfo deviceInfo) {
        if (isBlueConnectEd) {
            return;
        }
        isBlueConnectEd = true;
        currentConnectblueDeviceMac = deviceInfo.getDeviceid();
        //暂时根据名称来判断设备类型
        for (int i = 0; i < RyxSwiperCode.deviceStartNames.length; i++) {
            if (deviceInfo.getDevicename().startsWith(RyxSwiperCode.deviceStartNames[i])) {
                type = RyxSwiperCode.deviceblueTyes[i];
                break;
            }
        }
        ryxSwiper = null;
        ryxSwiper = new RyxSwiper(Swiper.this, type, ryxSwiperListener);
        //设备连接中
        messagedialog.setMessage("设备连接中...");
        messagedialog.show();
        new Thread() {
            public void run() {
                ryxSwiper.connectSwiper(deviceInfo.getDeviceid());
            }

            ;
        }.start();
    }

    public IRyxSwiperListener ryxSwiperListener = new IRyxSwiperListener() {

        @Override
        public void onswiperResult(int respcode, Map<String, Object> map) {

            if (respcode == RyxSwiperCode.ACTION_SWIPER_SUCCESS) {
                LogUtil.showLog("ACTION_SWIPER_SUCCESS");
                cardInfo = MapUtil.getString(map, "card_info");
                cardtype = MapUtil.getString(map, "card_type");
                icdata = MapUtil.getString(map, "ic_data");
                cardNo = MapUtil.getString(map, "card_no");
                pasamId = MapUtil.getString(map, "pasamId");
                serialnum = MapUtil.getString(map, "icsernum");
                LogUtil.showLog("cardInfo==" + cardInfo + ",cardtype==" + cardtype + ",icdata=" + icdata + ",serialnum==" + serialnum);
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_SWIPE_IC_READING) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_SWIPER_SUCCESS_WITHPIN) {
                LogUtil.showLog("ACTION_SWIPER_SUCCESS_WITHPIN");
                cardInfo = MapUtil.getString(map, "card_info");
                cardtype = MapUtil.getString(map, "card_type");
                icdata = MapUtil.getString(map, "ic_data");
                serialnum = MapUtil.getString(map, "icsernum");
                cardNo = MapUtil.getString(map, "card_no");
                pasamId = MapUtil.getString(map, "pasamId");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_GETKSN_SUCCESS) {
                ic_ksn = MapUtil.getString(map, "dev_ksn");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_GETKSN_FAIL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_SWIPER_CANCEL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_SUCCESS) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));

            }
            if (respcode == RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_FAIL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_SWIPER_FAIL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_AUDIO_READY_FAIL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_AUDIO_READY_SUCCESS) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_SWIPER_TIMEOUT) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_CARDDATA_READER) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACION_SWIPER_DEMOTIONTRADE) {
                String demotionTrade = MapUtil.getString(map, "demotionTrade");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode, demotionTrade));
            }
            if (respcode == RyxSwiperCode.ACION_SWIPER_COMMON_ERROR) {
                String msg = MapUtil.getString(map, "msg");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode, msg));
            }
        }

        @Override
        public void ondownloadResult(int respcode, Map<String, Object> map) {
            if (respcode == RyxSwiperCode.ACTION_DOWNLOADRESULT_SUCCESS) {
                downindex = MapUtil.getInt(map, "index");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            } else if (respcode == RyxSwiperCode.ACTION_DOWNLOADRESULT_FAIL) {
                downindex = MapUtil.getInt(map, "index");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
        }

        @Override
        public void onWriteResult(int respcode, Map<String, Object> map) {
            if (RyxSwiperCode.ACTION_SWIPE_IC_WRITEBACK == respcode) {
                writresult = MapUtil.getInt(map, "result");
                if (map.get("resultScript") != null)
                    writeresuiltScript = (byte[]) map.get("resultScript");
                if (map.get("data") != null)
                    writedata = (byte[]) map.get("data");
                LogUtil.showLog("onswiperResult wriht");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
        }
    };

    /**
     * 启动刷卡
     */
    private void startSwiperCard(boolean isagainStart) {
        LogUtil.showLog("startSwiperCard=============="+isagainStart);
        if (!isagainStart) {
            try {
                dialog_ic.show();
            } catch (Exception e) {
            }
        }
        Map<String, Object> mapSwiper = new HashMap<String, Object>();
        mapSwiper.put("dev_type", type);
        if ("balance".equals(actiontype)) {
            mapSwiper.put("buss_type", "BankCardBalance");
        } else {
            mapSwiper.put("buss_type", "JFPalCardPay");
        }
        mapSwiper.put("order_id", orderid);
        mapSwiper.put("amount", amount);
        if (isagainStart) {
            ryxSwiper.againSwiperCard(mapSwiper);
        } else {
            ryxSwiper.swiperCard(mapSwiper);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        // TODO Auto-generated method stub

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
                exitswiper(new Exitswiperlisten() {

                    @Override
                    public void exitswipersuccess() {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
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

        }else if(requestCode==TOSCANNINGPAYRESULT&&resultCode==REQUEST_SUCCESS){
            //查询结果完毕
            if ("11".equals(cardtype)) {
                showunplugin = "show";
            } else {
                showunplugin = "";
            }
            if (orderinfo.isNeedSign()&&RyxAppconfig.IMPAY_CASHPLEDGE!=orderinfo.getId()&&RyxAppconfig.RUIBEAN_PAY!=orderinfo.getId()) {
                getSignSlip();
            }

        }else if(requestCode==TOSCANNINGPAYRESULT&&resultCode==RyxAppconfig.CLOSE_SWIPER_SHOWIMPAY){
            //回退到刷卡收款页面
            exitswiper(new Exitswiperlisten() {

                @Override
                public void exitswipersuccess() {
                    setResult(RyxAppconfig.CLOSE_SWIPER_SHOWIMPAY, data);
                    finish();
                }
            });
        }
        else
        {
            finish();
        }

    }

    private void exitswiper(final Exitswiperlisten exitswiperlisten) {
        isfinishing = true;
        messagedialog.setMessage(getResources().getString(R.string.exitswiper));
        if (!messagedialog.isShowing()) {
            LogUtil.showLog("messagedialog===isclose");
            messagedialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ryxSwiper != null) {
                    ryxSwiper.disconnectSwiper();
                }
                LogUtil.showLog("11111111111111111111111111111111111111111111111");
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                LogUtil.showLog("222222222222222222222222222222222222222222222");
                //经过测试释放资源放到此处更加保险,防止因断开蓝牙导致设备释放资源崩溃
                if (blueToothSearchCommonAdapter != null) {
                    blueToothSearchCommonAdapter.releaseResoure();
                }
                LogUtil.showLog("333333333333333333333333333333333333333333333333");
                updateUIEx.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if(messagedialog!=null&&messagedialog.isShowing()){
                                messagedialog.dismiss();
                            }
                        }catch (Exception e){

                        }
                        exitswiperlisten.exitswipersuccess();
                    }
                });

            }
        }).start();
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

    /**
     * 新的更新UI handler
     */
    public Handler updateUIEx = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STARTSWIPERCARDCODE:
                    //启动刷卡
                    startSwiperCard(false);
                    break;
                case RyxSwiperCode.ACTION_AUDIO_READY_SUCCESS:
//				initdialog.show();
//				ryxSwiper.connectSwiper("");
//				break;
                case RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_SUCCESS:
                    //如果当前连接成功的蓝牙设备Mac和share中存储的不一致则进行更新share存储
                    if (!"".equals(currentConnectblueDeviceMac) && !currentConnectblueDeviceMac.equals(myshareblueDeviceMac)) {
                        PreferenceUtil.getInstance(Swiper.this).saveString("myblueDeviceMac", currentConnectblueDeviceMac);
                    }
                    messagedialog.setMessage("设备初始化中，请稍后...");
//                    initdialog.show();

                    ryxSwiper.getKsn();
                    break;

                case RyxSwiperCode.ACTION_SWIPE_IC_READING:
                    com.ryx.payment.ruishua.utils.LogUtil.showToast(Swiper.this, "IC卡读取中，等待界面跳转，交易完成前请勿拔卡", "up");
                    break;
                case RyxSwiperCode.ACTION_SWIPE_IC_WRITEBACK:
                    icbackruning = false;

                    messagedialog.dismiss();
//				if(writresult!=1){
//					LOG.showToast(Swiper.this, "IC回写失败!!!");
//				}
                    if (qtpayApplication.getValue().equals("BankCardBalance.Req")) {
                        intentBalance();
                    } else if (qtpayApplication.getValue().equals("JFPalCardPaySett.Req")
                            || qtpayApplication.getValue().equals("SmartPayments.Req")
                            || qtpayApplication.getValue().equals("JFPalCardPay.Req")
                            ||qtpayApplication.getValue().equals("TermPledge.Req")
                            ||qtpayApplication.getValue().equals("BuyRuiGoldbean.Req")){
                        intentCardPay();
                    }
                    break;
                case RyxSwiperCode.ACTION_SWIPER_SUCCESS_WITHPIN:
                    if(messagedialog.isShowing()){
                        messagedialog.dismiss();
                    }
                    dialog.dismiss();
                    dialog_ic.dismiss();

                    com.ryx.payment.ruishua.utils.LogUtil.showLog("jics", "dorequest");

                    translognumber = CryptoUtils.getInstance().getTransLogNo();
                    com.ryx.payment.ruishua.utils.LogUtil.showLog(translognumber);
                    keyLogNoParam.setValue(translognumber);
                    qtpayTransLogNo.setValue(translognumber);
                    stripeCardJude();
                    break;
                case RyxSwiperCode.ACTION_DOWNLOADRESULT_FAIL:
                    dialog_download.dismiss();
                    com.ryx.payment.ruishua.utils.LogUtil.showToast(Swiper.this, "注入IC参数失败,请稍后再试!");
                    break;
                case RyxSwiperCode.ACION_SWIPER_COMMON_ERROR:
//                    messagedialog.dismiss();
                    if (null != msg.obj) {
                        com.ryx.payment.ruishua.utils.LogUtil.showToast(Swiper.this, msg.obj.toString());
                    }
                    break;
                case RyxSwiperCode.ACTION_DOWNLOADRESULT_SUCCESS://公钥单个注入成功
                    int pos = 0;

                    if ("publickey".equals(step)) {
                        pos = downindex + 1;
                    } else {
                        pos = downindex + 1 + publickeyInfolist.size();
                    }

                    if (pos < publickeyInfolist.size()) {
                        dialog_download.setTip(pos,
                                publickeyInfolist.size() + list.size());
                        com.ryx.payment.ruishua.utils.LogUtil.showLog("writresult====" + downindex + "------"
                                + downresult + "++++++all.size=="
                                + publickeyInfolist.size() + list.size());
                        ryxSwiper.updateTerminalParams(0, downindex + 1,
                                publickeyInfolist.get(downindex + 1)
                                        .getRid()
                                        + publickeyInfolist.get(downindex + 1)
                                        .getKeyIndex()
                                        + publickeyInfolist.get(downindex + 1)
                                        .getIcKeys());
                        com.ryx.payment.ruishua.utils.LogUtil.showLog("  key   currestpos===" + pos + "===total==="
                                + publickeyInfolist.size() + list.size()
                                + "   index   " + downindex);

                    } else if (pos < publickeyInfolist.size() + list.size()) {

                        step = "aid";
                        dialog_download.setTip(pos,
                                publickeyInfolist.size() + list.size());
                        ryxSwiper.updateTerminalParams(1, pos - publickeyInfolist.size(),
                                list.get(pos - publickeyInfolist.size()));

                        com.ryx.payment.ruishua.utils.LogUtil.showLog(" aid  currestpos===" + pos + "===total==="
                                + publickeyInfolist.size() + list.size()
                                + "   index   " + downindex);

                    }

                    if (pos == publickeyInfolist.size() + list.size()) {
                        dialog_download.setTip(pos,
                                publickeyInfolist.size() + list.size());
                        dialog_download.dismiss();
                        LogUtil.showToast(Swiper.this, "数据同步成功");
                        updatePsam();
                    }
                    break;
                case RyxSwiperCode.ACTION_GETKSN_SUCCESS:// 设备就绪
                    messagedialog.dismiss();
                    //获取设备ksn信息
                    if ("DEVBINDING".equals(actiontype)) {
                        exitswiper(new Exitswiperlisten() {
                            @Override
                            public void exitswipersuccess() {
//                                String currentPsamid="";
//                                if(ic_ksn!=null&&ic_ksn.length()>16){
//                                    currentPsamid=ic_ksn.substring(ic_ksn.length()-16, ic_ksn.length());
//                                }else{
//                                    currentPsamid=ic_ksn;
//                                }
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
                        int mydeviceType = ryxSwiper.getDeviceType();
                        LogUtil.showLog("mydeviceType==" + mydeviceType);
                        if (!(RyxSwiperCode.DEVICE_TYPE_KEYBOARD_MPOS == mydeviceType || RyxSwiperCode.DEVICE_TYPE_KEYBOARD_ORDINARY == mydeviceType)) {
//                            LogUtil.showToast(Swiper.this,"请使用带密码键盘设备进行交易!");
                            RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(Swiper.this, new ConFirmDialogListener() {

                                @Override
                                public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                                    ryxSimpleConfirmDialog.dismiss();
                                    exitswiper(new Exitswiperlisten() {
                                        @Override
                                        public void exitswipersuccess() {
                                            finish();
                                        }
                                    });
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
                    needpublickey = ryxSwiper.isNeeddownloadKey();
                    if (needpublickey) {
                        //获取公钥
                        getPublickey();
                    } else {
                        startSwiperCard(false);
                    }
                    break;
                case RyxSwiperCode.ACTION_SWIPER_SUCCESS:
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
                    break;
                case RyxSwiperCode.ACTION_GETKSN_FAIL:
                    messagedialog.dismiss();
                    LogUtil.showToast(Swiper.this, "获取设备信息失败!");

                    break;
                case RyxSwiperCode.ACTION_SWIPER_CANCEL:
                    LogUtil.showToast(Swiper.this, "交易取消!");
                    dialog_ic.dismiss();
                    exitswiper(new Exitswiperlisten() {
                        @Override
                        public void exitswipersuccess() {
                            finish();
                        }
                    });
                    break;
                case RyxSwiperCode.ACTION_AUDIO_READY_FAIL:
                case RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_FAIL:
                case RyxSwiperCode.ACTION_SWIPER_FAIL:
                    if (!isfinishing) {
                        LogUtil.showToast(Swiper.this, "连接设备失败!");
                        messagedialog.dismiss();
                        dialog_ic.dismiss();
                    }
                    break;
                case RyxSwiperCode.ACTION_SWIPER_TIMEOUT:
                    LogUtil.showToast(Swiper.this, "操作超时!");
                    dialog_ic.dismiss();
                    break;
                case RyxSwiperCode.ACTION_CARDDATA_READER:
                    messagedialog.setMessage("数据读取中,请耐心等待...");
                    messagedialog.show();
                    break;
                case RyxSwiperCode.ACION_SWIPER_DEMOTIONTRADE:
                    if (null != msg.obj) {
                        LogUtil.showToast(Swiper.this, msg.obj.toString());
                    }
                    startSwiperCard(true);
                    break;
                case STRIPECARDJUDEFLAG:
                    ordinarySwiper();
                    break;
                default:
                    break;
            }
        }

        ;
    };

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

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

            }
        });
    }

    public void doRequest() {
        if ("balance".equals(actiontype)) {
            getBankCardBalance();
        } else {
            doJFPalCardPay();
        }
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
        LogUtil.showLog("orderid+++++" + orderid);

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
                    icbackruning = true;
                    if (null != payResult.getIcData()
                            && !"null".equals(payResult.getIcData())) {
                        ryxSwiper.icWriteBack("00", payResult.getIcData());
                    } else {
                        ryxSwiper.icWriteBack("00", "");
                    }

                } else {
                    showunplugin = "";
                    intentBalance();
                }

            }

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

            }
        });
    }

    /**
     * 公用刷卡方法
     */
    public void doJFPalCardPay() {
        String merchantId = orderinfo.getMerchantId();
        qtpayMerchantId = new Param("merchantId", orderinfo.getMerchantId());
        // 区分商户编号
//        if ("0002000043".equals(merchantId)) {
//            qtpayApplication = new Param("application", "JFPalCardPaySett.Req");
//        } else {
//            qtpayApplication = new Param("application", "JFPalCardPay.Req");
//        }
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
//        String cardTagss = RyxAppdata.getInstance(Swiper.this)
//                .getRuishuaParam(
//                        RyxAppdata.getInstance(Swiper.this).getMobileNo(),
//                        "cardNo", "");
        String cardIdxss = orderinfo.getCardIdx();
//        String cardIdxss = RyxAppdata.getInstance(Swiper.this)
//                .getRuishuaParam(
//                        RyxAppdata.getInstance(Swiper.this).getMobileNo(),
//                        "cardIdx", "");
        //cardTag无意义去除
//        if (cardTagss.length() >= 4) {
//            cardTag = new Param("cardTag", cardTagss.substring(
//                    cardTagss.length() - 4, cardTagss.length()));
//
//        } else
//            cardTag = new Param("cardTag", cardTagss);

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
        qtpayParameterList.add(new Param("account",cardNo));
        if (!TextUtils.isEmpty(orderinfo.getPayee())) {
            qtpayParameterList.add(new Param("payee", orderinfo.getPayee()));
        }
        httpsPost("JFPalCardPayTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                com.ryx.payment.ruishua.utils.LogUtil.showToast(Swiper.this, payResult.getRespDesc());
                if ("11".equals(cardtype)) {
                    showunplugin = "show";
                    messagedialog.setMessage("ic回写中，请稍后...");
                    messagedialog.show();
                    icbackruning = true;
                    if (null != payResult.getIcData()
                            && !"null".equals(payResult.getIcData())) {
                        ryxSwiper.icWriteBack("00", payResult.getIcData());
                    } else {
                        ryxSwiper.icWriteBack("00", "");
                    }

                } else {
                    showunplugin = "";
                    intentCardPay();
                }
            }

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public boolean isToastOtherMsg() {
                return false;
            }

            @Override
            public void onOtherState(String code,String msg) {
                if("9101".equals(code)){
                    //当前刷卡走快捷,无需回写
                    Intent intent=new Intent(Swiper.this, ScanningPayResultActivity_.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("orderinfo", orderinfo);
                    bundle.putString("flag","swiper");
                    intent.putExtras(bundle);
                    startActivityForResult(intent,TOSCANNINGPAYRESULT);
                }else{
                    LogUtil.showToast(Swiper.this,msg);
                }
            }

            @Override
            public void onTradeFailed() {

            }
        });
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
                            ryxSwiper.updateTerminalParams(0, 0, publickeyInfolist.get(0).getRid()
                                    + publickeyInfolist.get(0).getKeyIndex()
                                    + publickeyInfolist.get(0).getIcKeys());
                        }
                    }).start();
                } else {
                    updateUIEx.sendMessage(updateUIEx.obtainMessage(STARTSWIPERCARDCODE));
                }
            }

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

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

    private void intentCardPay() {
        if (orderinfo.isNeedSign()&&RyxAppconfig.IMPAY_CASHPLEDGE!=orderinfo.getId()&&RyxAppconfig.RUIBEAN_PAY!=orderinfo.getId()) {

            getSignSlip();

        } else {
            Intent intent = new Intent(Swiper.this, PaymentSuccessful_.class);
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

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

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

    public void intentactivity() {

        // 签购单经纬度
        tradinfo.setLongitude(longitude);
        tradinfo.setLatitude(latitude);
        Intent intent = new Intent(Swiper.this, SignRequisitions_.class);
        intent.putExtra("showunplugin", showunplugin);
        intent.putExtra("tradeDetailInfo", tradinfo);
        intent.putExtra("tradeSlipInfo", signSlipInfo);
        intent.putExtra("signarray", signdata);
        intent.putExtra("fromtype", RyxAppconfig.CREATE_SIGNREQUISITIONS);
        startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
    }

    private void intentBalance() {
        Intent itbalance = new Intent(Swiper.this, CardBalanceResultActivity_.class);
        itbalance.putExtra("showunplugin", showunplugin);
        itbalance.putExtra("CardBalance", myRyxPayResult.getBalance());
        startActivityForResult(itbalance, SHOWBANLANCE);
    }

    @Override
    public void getPwdVal(String text) {
        cardpsw = text;
        stripeCardJude();
    }

    /**
     * 执行签名或者普通交易流程
     */
    public void ordinarySwiper() {
        if ((!"balance".equals(actiontype)) && orderinfo.isNeedSign()) {
            if ("12".equals(orderinfo.getInterfaceTag()) || "14".equals(orderinfo.getInterfaceTag())||"15".equals(orderinfo.getInterfaceTag())) {
                //小贷还款
                Intent it = new Intent(Swiper.this, OrderSignature_.class);
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
//                          String resultCode=  jsonObj.getString("code");
//                        if(RyxAppconfig.QTNET_SUCCESS.equals(resultCode)){
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

//                                JSONArray feeinfoArray = reSultObject.getJSONArray("feeinfo");
//                                JSONObject feeinfoObj = feeinfoArray.getJSONObject(0);
//                                String feerate = feeinfoObj.getString("feerate");
//                                String feefixed = feeinfoObj.getString("feefixed");
//                                String feelow = feeinfoObj.getString("feelow");
//                                Double feefixedDou = parseDouble(feefixed) / 100;
//                                Double feerateDou = parseDouble(feerate) / 10000;
//                                Double amountDou = parseDouble(amount) / 100;
//                                Double poundageVal = amountDou * feerateDou + feefixedDou;
//                                Double feelowDou = parseDouble(feelow) / 100;
//                                //费率
//                                String rate = Double.parseDouble(feerate) / 100 + "%+" + feefixedDou + "元";
//                                java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
                                Intent it = new Intent(Swiper.this, OrderSignature_.class);
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
//                                String moenyRate = (poundageVal < feelowDou ? df.format(feelowDou) : df.format(poundageVal));
//                                it.putExtra("rateStr", "费率:" + rate + ",本次手续费:" + moenyRate + "元");
                                it.putExtra("moneyratedisplay",stringBuffer.toString());
                                startActivityForResult(it, PAYWITHSIGN);
                            } else {
                                String msg = reSultObject.getString("msg");
                                LogUtil.showToast(Swiper.this, msg + "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.showToast(Swiper.this, "费率信息异常,请联系代理重新设置!");
                        }
                    }
                });
            }
        } else {
            doRequest();
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        System.gc();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        switch (keycode) {
            case KeyEvent.KEYCODE_BACK: {
                exitswiper(new Exitswiperlisten() {

                    @Override
                    public void exitswipersuccess() {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
                return true;
            }
            default:
                return super.onKeyDown(keycode, event);
        }
    }

    @Override
    public void backUpImgOnclickListen() {
        exitswiper(new Exitswiperlisten() {

            @Override
            public void exitswipersuccess() {
                finish();
            }
        });
    }


    @Override
    public void requestSuccess() {
        super.requestSuccess();
        LogUtil.showLog("Swiper======requestSuccess=" + DateUtil.getDateTime(new Date()));
        if (!isDevInited) {
            initDev();
            LogUtil.showLog("Swiper======initDev=" + DateUtil.getDateTime(new Date()));
            isDevInited = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isDevInited", isDevInited);
        savedInstanceState.putBoolean("isBlueConnectEd", isBlueConnectEd);
        LogUtil.showLog("Swiper======onRestoreInstanceState=====" + isDevInited + "," + isBlueConnectEd);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isDevInited = savedInstanceState.getBoolean("isDevInited", false);
        isBlueConnectEd = savedInstanceState.getBoolean("isBlueConnectEd", false);
        LogUtil.showLog("Swiper======onRestoreInstanceState=====" + isDevInited + "," + isBlueConnectEd);
    }

    @Override
    public void requestFailed() {
        super.requestFailed();
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
                                Intent swiperMsgIntent = new Intent(Swiper.this, SwiperMsgUploadActivity_.class);
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
                            LogUtil.showToast(Swiper.this, msg + "");
                        }
                    } catch (Exception e) {
                        LogUtil.showToast(Swiper.this, "StripeCardJude数据异常,请稍后再试!");
                    }
                }
            });
        } else {
            updateUIEx.sendEmptyMessage(STRIPECARDJUDEFLAG);
        }
    }

    @Override
    protected void subActivityRelease(final ReleaseResultListen releaseResultListen) {
        exitswiper(new Exitswiperlisten() {
            @Override
            public void exitswipersuccess() {
                setResult(RyxAppconfig.CLOSE_ALL);
                releaseResultListen.releaseResultok();
            }
        });
//        super.subActivityRelease(releaseResultListen);
    }
}
