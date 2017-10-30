package com.ryx.payment.ruishua.convenience;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import com.ryx.payment.ruishua.dialog.AudioTypesDialog;
import com.ryx.payment.ruishua.dialog.BluetoothDialog;
import com.ryx.payment.ruishua.inteface.BlueToothListener;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.widget.RyxLoadDialog;
import com.ryx.payment.ruishua.widget.RyxLoadDialogBuilder;
import com.ryx.swiper.IRyxSwiperListener;
import com.ryx.swiper.RyxSwiper;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.MapUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * 获取卡号组件类
 */
@EActivity(R.layout.activity_swiper)
public class Cardno extends BaseActivity implements BlueToothListener {
    @ViewById
    ImageView shuaka;
    int type; // 刷卡器类型
    String cardNo;
    String expirydate;
    Dialog dialog, dialog_ic;// 刷卡提示dialog
    /**
     * 等待框
     */
    RyxLoadDialogBuilder messagedialog;
    BluetoothDialog bluelistdialog;// 蓝牙提示dialog
    /**
     * 搜索的蓝牙设备列表
     */
    private ArrayList<DeviceInfo> deviceinfos = new ArrayList<DeviceInfo>();
    AudioManager localAudioManager;
    private RyxSwiper ryxSwiper;
    /**
     * Ryx蓝牙搜索组件
     */
    BlueToothSearchCommonAdapter blueToothSearchCommonAdapter;
    private String myshareblueDeviceMac = "";
    private boolean isshowBlueDialog = true;
    private String currentConnectblueDeviceMac = "";
    private DeviceInfo devinfo;
    private boolean isfinishing = false;

    @AfterViews
    public void initview() {
        setTitleLayout("请刷卡", true, false);
        Glide.with(Cardno.this).load(R.drawable.img_shoukuan_shuaka).into(shuaka);
    }

    /**
     * 初始化刷卡设备资源
     */
    private void initDev() {
        //获取上次连接的蓝牙设备mac
        myshareblueDeviceMac = PreferenceUtil.getInstance(Cardno.this).getString("myblueDeviceMac", "");
        localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //刷磁条卡
        dialog = new SimpleDialog(Cardno.this);
        dialog.setContentView(R.layout.dialog_swipe_card);
        dialog.setCanceledOnTouchOutside(false);
        //刷IC卡和磁条
        dialog_ic = new SimpleDialog(Cardno.this);
        dialog_ic.setContentView(R.layout.dialog_swipe_card_ic);
        messagedialog = new RyxLoadDialog().getInstance(Cardno.this);
        messagedialog.setCanceledOnTouchOutside(false);
        messagedialog.setCancelable(false);

        //蓝牙列表对话框
        bluelistdialog = new BluetoothDialog(Cardno.this, deviceinfos, Cardno.this);
        bluelistdialog.setCanceledOnTouchOutside(false);


        //音频已经插入
        if (localAudioManager.isWiredHeadsetOn()) {
            //音频插入了判断6.0权限
            String  waring = MessageFormat.format(getResources().getString(R.string.swiperAudiowaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//            if (RyxAppconfig.BRANCH.equals("01")) {
//                waring = MessageFormat.format(getResources().getString(R.string.swiperAudiowaringmsg), getResources().getString(R.string.app_name));
//            } else if (RyxAppconfig.BRANCH.equals("02")) {
//                waring = MessageFormat.format(getResources().getString(R.string.swiperAudiowaringmsg), getResources().getString(R.string.app_name_ryx));
//            }
            final String finalWaring = waring;
            requesDevicePermission(waring, 0x0014, new PermissionResult() {
                        @Override
                        public void requestSuccess() {
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "音频权限requestSuccess==" + DateUtil.getDateTime(new Date()));
                            //仅有一个设备直接初始化设备,无需弹出选择框
                            if (RyxSwiperCode.devsNames.length == 1) {
                                type = RyxSwiperCode.devsCode[0];
                                initAudioDevs();
                            } else {
                                AudioTypesDialog audioTypesDialog = new AudioTypesDialog(Cardno.this, new AudioTypesDialog.AudioTypeItemSlectListener() {
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
                            LogUtil.showToast(Cardno.this, finalWaring);
                            com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "音频权限requestFailed==" + DateUtil.getDateTime(new Date()));
                        }
                    },
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS
            );
        } else {
            startInitBlue();
        }
    }

    /**
     * 开始蓝牙部分
     */
    private void startInitBlue() {
        //蓝牙判断6.0权限
        String  waring = MessageFormat.format(getResources().getString(R.string.swiperBluewaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperBluewaringmsg), getResources().getString(R.string.app_name));
//        } else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperBluewaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0015, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "蓝牙权限requestSuccess==" + DateUtil.getDateTime(new Date()));
                        // 初始化并且搜索蓝牙
                        initBlu();
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(Cardno.this, finalWaring);
                        com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "蓝牙权限requestFailed==" + DateUtil.getDateTime(new Date()));
                    }
                },
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH
        );
    }


    // 初始化并且搜索蓝牙设备
    private void initBlu() {
        if (blueToothSearchCommonAdapter == null) {
            blueToothSearchCommonAdapter = new BlueToothSearchCommonAdapter(
                    Cardno.this, toothSearchListener, 15 * 1000);
        }
        blueToothSearchCommonAdapter.searchBlueDevs(RyxSwiperCode.deviceStartNames);
        if ("".equals(myshareblueDeviceMac)) {
            bluelistdialog.show();
        } else {
            messagedialog.setMessage("蓝牙正在努力匹配中,请稍等...");
            messagedialog.show();
        }
    }

    /**
     * 连接设备
     *
     * @param deviceInfo
     */
    private void connectSwiper(final DeviceInfo deviceInfo) {
        currentConnectblueDeviceMac = deviceInfo.getDeviceid();
        //暂时根据名称来判断设备类型
        for (int i = 0; i < RyxSwiperCode.deviceStartNames.length; i++) {
            if (deviceInfo.getDevicename().startsWith(RyxSwiperCode.deviceStartNames[i])) {
                type = RyxSwiperCode.deviceblueTyes[i];
                break;
            }
        }
        ryxSwiper = null;
        ryxSwiper = new RyxSwiper(Cardno.this, type, ryxSwiperListener);
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
            for (int i = 0; i < deviceinfos.size(); i++) {
                if (buleToothDeviceInfo.getAddress().equals(
                        deviceinfos.get(i).getDeviceid())) {
                    addboolean = false;
                    break;
                }
            }
            if (addboolean) {
                devinfo = new DeviceInfo();
                devinfo.setDeviceid(buleToothDeviceInfo.getAddress());
                devinfo.setDevicename(buleToothDeviceInfo.getName());
                deviceinfos.add(devinfo);
                boolean isShowing = bluelistdialog.isShowing();
                if (isShowing) {
                    bluelistdialog.RefreshList(deviceinfos);
                }
            }
        }

        @Override
        public void searchStateListener(int stateflag, String message) {
            if (BlueToothSearchStatus.CLOSESEARCHBLUETOOTH_FLAG == stateflag) {
                if (isshowBlueDialog) {
                    boolean isinited = bluelistdialog.isinitedBlueDialog();
                    com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "isinited==" + isinited);
                    //当前
                    if (!isinited) {
                        messagedialog.dismiss();
                        bluelistdialog.show();
                    }
                    bluelistdialog.SearchComplete(true);
                    bluelistdialog.RefreshList(deviceinfos);
                }
            }
        }

    };

    /**
     * 初始化音频设备
     */
    private void initAudioDevs() {
        ryxSwiper = new RyxSwiper(Cardno.this, type, ryxSwiperListener);
        messagedialog.setMessage("设备初始化中，请稍后...");
        //就绪之后回调listener
        messagedialog.show();
        ryxSwiper.hasdevice();

    }

    //刷卡回调监听
    public IRyxSwiperListener ryxSwiperListener = new IRyxSwiperListener() {

        @Override
        public void onswiperResult(int respcode, Map<String, Object> map) {
            if (respcode == RyxSwiperCode.ACTION_AUDIO_READY_SUCCESS) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_SWIPE_IC_READING) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_SUCCESS) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));

            }
            if (respcode == RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_FAIL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_AUDIO_READY_FAIL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_GETCARDNO_SUCCESS) {
                cardNo = MapUtil.getString(map, "card_no");
                expirydate = MapUtil.getString(map, "expirydate");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_CARDDATA_READER) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACTION_SWIPER_CANCEL) {
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode));
            }
            if (respcode == RyxSwiperCode.ACION_SWIPER_COMMON_ERROR) {
                String msg = MapUtil.getString(map, "msg");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode, msg));
            }
            if (respcode == RyxSwiperCode.ACION_SWIPER_DEMOTIONTRADE) {
                String demotionTrade = MapUtil.getString(map, "demotionTrade");
                updateUIEx.sendMessage(updateUIEx.obtainMessage(respcode, demotionTrade));
            }

        }

        @Override
        public void ondownloadResult(int respcode, Map<String, Object> map) {

        }

        @Override
        public void onWriteResult(int respcode, Map<String, Object> map) {
        }
    };
    /**
     * 新的更新UI handler
     */
    public Handler updateUIEx = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RyxSwiperCode.ACTION_AUDIO_READY_SUCCESS:
                    messagedialog.dismiss();
                    dialog_ic.show();
                    ryxSwiper.getCardNo();
                    break;
                case RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_SUCCESS:
                    messagedialog.dismiss();
                    dialog_ic.show();
                    //如果当前连接成功的蓝牙设备Mac和share中存储的不一致则进行更新share存储
                    if (!"".equals(currentConnectblueDeviceMac) && !currentConnectblueDeviceMac.equals(myshareblueDeviceMac)) {
                        PreferenceUtil.getInstance(Cardno.this).saveString("myblueDeviceMac", currentConnectblueDeviceMac);
                    }
                    ryxSwiper.getCardNo();
                    break;
                case RyxSwiperCode.ACTION_GETCARDNO_SUCCESS:
                    messagedialog.dismiss();
                    dialog_ic.dismiss();
                    LogUtil.showLog("刷卡成功==UI=111======================" + cardNo);
                    setCardnoTobackAct();
                    break;
                case RyxSwiperCode.ACTION_SWIPE_IC_READING:
                    com.ryx.payment.ruishua.utils.LogUtil.showToast(Cardno.this, "IC卡信息读取中,等待界面跳转,完成前请勿拔卡");
                    break;
                case RyxSwiperCode.ACTION_AUDIO_READY_FAIL:
                    LogUtil.showToast(getApplicationContext(), "音频设备初始化失败!");
                    messagedialog.dismiss();
                    break;
                case RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_FAIL:
                case RyxSwiperCode.ACTION_SWIPER_FAIL:
                    if (!isfinishing) {
                        LogUtil.showToast(getApplicationContext(), "连接设备失败!");
                        dialog_ic.dismiss();
                    }
                    break;
                case RyxSwiperCode.ACTION_CARDDATA_READER:
                    messagedialog.setMessage("数据读取中,请耐心等待...");
                    messagedialog.show();
                    break;
                case RyxSwiperCode.ACION_SWIPER_COMMON_ERROR:
                    if (null != msg.obj) {
                        com.ryx.payment.ruishua.utils.LogUtil.showToast(getApplicationContext(), msg.obj.toString());
                    }
                    break;
                case RyxSwiperCode.ACION_SWIPER_DEMOTIONTRADE:
                    if (null != msg.obj) {
                        LogUtil.showToast(getApplicationContext(), msg.obj.toString());
                    }
                    ryxSwiper.agingetCardNo();
                    break;
                case RyxSwiperCode.ACTION_SWIPER_CANCEL:
                    LogUtil.showToast(Cardno.this,"操作取消!");
                    messagedialog.dismiss();
                    dialog_ic.dismiss();
                        exitswiper(new Exitswiperlisten() {
                            @Override
                            public void exitswipersuccess() {
                                finish();
                            }
                        });
                    break;
                default:
                    break;
            }
        }


    };

    /**
     * 设置卡号进行返回
     */
    private void setCardnoTobackAct() {
        Intent intent = new Intent();
        // 把返回数据存入Intent
        intent.putExtra("result", cardNo);
        intent.putExtra("expirydate", expirydate);
        com.ryx.payment.ruishua.utils.LogUtil.showLog("ryx", "setCardnoTobackAct接收值===cardNo=" + cardNo + ",expirydate=" + expirydate);
        // 设置返回数据
        Cardno.this.setResult(RyxAppconfig.QT_RESULT_OK, intent);
        exitswiper(new Exitswiperlisten() {

            @Override
            public void exitswipersuccess() {
                // TODO Auto-generated method stub
                // 关闭Activity
                Cardno.this.finish();
            }
        });

    }

    private void exitswiper(final Exitswiperlisten exitswiperlisten) {
        isfinishing = true;
        messagedialog.setMessage(getResources().getString(R.string.exitswiper));
        messagedialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ryxSwiper != null) {
                    ryxSwiper.disconnectSwiper();
                    ryxSwiper = null;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //经过测试释放资源放到此处更加保险,防止因断开蓝牙导致设备释放资源崩溃
                if (blueToothSearchCommonAdapter != null) {
                    blueToothSearchCommonAdapter.releaseResoure();
                }
                updateUIEx.post(new Runnable() {

                    @Override
                    public void run() {
                        messagedialog.dismiss();
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
    protected void onDestroy() {
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
    public void requestSuccess() {
        super.requestSuccess();
        initDev();
    }

    @Override
    public void requestFailed() {
        super.requestFailed();
    }
}
