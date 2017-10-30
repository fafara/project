package com.ryx.payment.ruishua.convenience.swiping;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;

import com.ruiyinxin.bluesearch.adapter.BlueToothSearchCommonAdapter;
import com.ruiyinxin.bluesearch.bean.BlueToothSearchStatus;
import com.ruiyinxin.bluesearch.listener.BlueToothSearchListener;
import com.ryx.payment.ruishua.bean.DeviceInfo;
import com.ryx.payment.ruishua.dialog.AudioTypesDialog;
import com.ryx.payment.ruishua.inteface.BlueToothListener;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.swiper.IRyxSwiperListener;
import com.ryx.swiper.RyxSwiper;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.MapUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * 刷卡服务类
 */
public class SwipingService extends Service {
    private SwipingBinder swipingBinder=new SwipingBinder();
    /**
     * 设备列表
     */
    private ArrayList<DeviceInfo> deviceinfo = new ArrayList<DeviceInfo>();
    private Callback mCallback = null;
    public static int SHOWAUDIOTYPEDIALOG=0x001;
    public static int SHOWMESSAGEDIALOG=0x002;
    public static int MESSAGEDIALOGDISMISS=0x003;
    public static int BLUELISTDIALOGREFRESHLIST=0x004;
    public static int BLUELISTCOMPLETEREFRESHLIST=0x005;
    public static int SHOWTOASTMSG=0x006;
    public static int EXITSWIPER=0x007;
    public static int ICDIALOGDISMISS=0x008;
    public static int STARTSWIPERCARD=0x009;
    int type;
    private RyxSwiper ryxSwiper;
    /***
     * 本地保存的设备Mac值
     */
    private String myshareblueDeviceMac = "";
    /**
     * 蓝牙搜素适配器
     */
    BlueToothSearchCommonAdapter blueToothSearchCommonAdapter;
    private boolean isshowBlueDialog = true;
    private String currentConnectblueDeviceMac = "";
    /**
     * 蓝牙是否连接着
     */
    private boolean blueisConnected=false;
    public SwipingService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.showLog("SwipingService==onBind");
        //获取上次连接的蓝牙设备mac
        myshareblueDeviceMac = PreferenceUtil.getInstance(SwipingService.this).getString("myblueDeviceMac", "");
        connectOrDisConnectListen();
        return swipingBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.showLog("SwipingService==onCreate");

    }

    public class SwipingBinder extends Binder {
        public SwipingService getService() {
            return SwipingService.this;
        }
    }

    public void setSwipingCallback(Callback callback) {
        mCallback = callback;
    }

    /**
     * Activity，service交互接口
     */
    public interface Callback {
        /**
         * 刷卡数据回调
         * @param var1
         * @param var2
         */
        void onSwipingResult(int var1, Map<String, Object> var2);

        /**
         * 服务命令
         * @param code
         * @param data
         */
        void onServicecommand(int code ,Object data);
    }

    /**
     * 判断当前是否已经连接着设备如果连接着直接进行获取信息走交易流程<br/>
     * 如果当前设备没有连接着则进行搜索供用户选择连接
     */
    public boolean isConnectedBlue(){
        return blueisConnected;
    }

    /**
     * 音频设备是否插入正常
     * @return
     */
    public boolean isWiredHeadsetOn(){
        AudioManager localAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return localAudioManager.isWiredHeadsetOn();
    }

    /**
     *对外初始化音频设备
     * @return
     */
    public void  initAudioDev(){

        //仅有一个设备直接初始化设备,无需弹出选择框
        if (RyxSwiperCode.devsNames.length == 1) {
            type = RyxSwiperCode.devsCode[0];
            _initAudioDevs();
        } else {
            onServicecommand_runOnUiThread(SHOWAUDIOTYPEDIALOG,new AudioTypesDialog.AudioTypeItemSlectListener(){

                @Override
                public void onItemClick(int itemId) {
                    type = RyxSwiperCode.devsCode[itemId];
                    _initAudioDevs();
                }
            });
        }
    }

    /**
     * 对外蓝牙资源初始化
     */
    public void initBluthDev(){
        if(isConnectedBlue()){
            //蓝牙已经连接中
            onServicecommand_runOnUiThread(SHOWMESSAGEDIALOG, "设备初始化中，请稍后...");
            ryxSwiper.getKsn();
        }else{
            //蓝牙搜索
            LogUtil.showLog("initBlu==================");
            if (blueToothSearchCommonAdapter == null) {
                blueToothSearchCommonAdapter = new BlueToothSearchCommonAdapter(SwipingService.this, toothSearchListener, 15 * 1000);
            }
            blueToothSearchCommonAdapter.searchBlueDevs(RyxSwiperCode.deviceStartNames);
            deviceinfo.clear();
            if ("".equals(myshareblueDeviceMac)) {
                onServicecommand_runOnUiThread(SHOWMESSAGEDIALOG,null);
            } else {
                onServicecommand_runOnUiThread(SHOWMESSAGEDIALOG,"蓝牙正在努力匹配中,请稍等...");
            }
        }
    }

    /**
     * 初始化音频资源
     */
    private void _initAudioDevs(){
        onServicecommand_runOnUiThread(SHOWMESSAGEDIALOG,"正在努力连接音频设备,请稍等...");
        ryxSwiper = new RyxSwiper(SwipingService.this, type, ryxSwiperListener);
        rundetect();
    }

    /**
     * 新的设备检测方法
     */
    private void rundetect() {
        ryxSwiper.hasdevice();
    }

    // 蓝牙监听
    private BlueToothSearchListener toothSearchListener = new BlueToothSearchListener() {

        @Override
        public void discoverOneDevice(BluetoothDevice buleToothDeviceInfo) {
            if (!"".equals(myshareblueDeviceMac) && myshareblueDeviceMac.equals(buleToothDeviceInfo.getAddress())) {
                isshowBlueDialog = false;
                //如果设备是已经连接过的停止搜索并
                blueToothSearchCommonAdapter.manualstopBlueToothsearch();
                onServicecommand_runOnUiThread(MESSAGEDIALOGDISMISS, null);
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
                DeviceInfo devinfo = new DeviceInfo();
                devinfo.setDeviceid(buleToothDeviceInfo.getAddress());
                devinfo.setDevicename(buleToothDeviceInfo.getName());
                deviceinfo.add(devinfo);
                onServicecommand_runOnUiThread(BLUELISTDIALOGREFRESHLIST, deviceinfo);
            }
        }

        @Override
        public void searchStateListener(int stateflag, String message) {
            if (BlueToothSearchStatus.CLOSESEARCHBLUETOOTH_FLAG == stateflag) {
                LogUtil.showLog("CLOSESEARCHBLUETOOTH=====================" + DateUtil.getDateTime(new Date()));
                //此处判断是当搜索到本地存储的设备并连接了，搜索完成监听不做任何操作，如果未搜索到本地存储的设备则进行展示搜索Dialog框
                if (isshowBlueDialog) {
                    onServicecommand_runOnUiThread(BLUELISTCOMPLETEREFRESHLIST, deviceinfo);
                }
            }
        }
    };

    /**
     * 蓝牙设备连接
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
        ryxSwiper = new RyxSwiper(SwipingService.this, type, ryxSwiperListener);
        //设备连接中
        onServicecommand_runOnUiThread(SHOWMESSAGEDIALOG,"设备连接中...");
        new Thread() {
            public void run() {
                ryxSwiper.connectSwiper(deviceInfo.getDeviceid());
            }
        }.start();
    }

    /**
     * 刷卡页面蓝牙相关操作监听
     */
    public BlueToothListener blueToothListener=new BlueToothListener() {
        @Override
        public void getBlueToothMac(DeviceInfo deviceInfo) {
            blueToothSearchCommonAdapter.manualstopBlueToothsearch();
            connectSwiper(deviceInfo);
        }

        @Override
        public void againsearch() {
            ((Activity)mCallback).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initBluthDev();
                }
            });
        }
    };
    /**
     * 刷卡回调监听
     */
    public IRyxSwiperListener ryxSwiperListener = new IRyxSwiperListener(){

        @Override
        public void onswiperResult(final int respcode, final Map<String, Object> map) {
            onSwipingResult_runOnUiThread(respcode,map);
        }

        @Override
        public void onWriteResult(final int respcode, final Map<String, Object> map) {
            onSwipingResult_runOnUiThread(respcode,map);
        }

        @Override
        public void ondownloadResult(final int respcode, final Map<String, Object> map) {
            onSwipingResult_runOnUiThread(respcode,map);
        }
    };


    /**
     * 重新刷卡
     * @param mapSwiper
     */
    public void againSwiperCard(Map<String, Object> mapSwiper){
        mapSwiper.put("dev_type", type);
        ryxSwiper.againSwiperCard(mapSwiper);
    }

    /**
     * 获取和设备类型
     * @return
     */
    public int getDeviceType(){
      return  ryxSwiper.getDeviceType();
    }

    /**
     * 是否需要下载公钥
     * @return
     */
    public boolean isNeeddownloadKey(){
        return ryxSwiper.isNeeddownloadKey();
    }
    /**
     * 执行刷卡操作
     * @param mapSwiper
     */
    public void swiperCard(Map<String, Object> mapSwiper){
        ryxSwiper.swiperCard(mapSwiper);
    }
    public void icWriteBack(String code,String icData){
        ryxSwiper.icWriteBack(code,icData);
    }
    public void updateTerminalParams(int _flag, int _index, String _icdata){
        ryxSwiper.updateTerminalParams(_flag,_index,_icdata);
    }
    /**
     * 主线程执行返回刷卡结果
     * @param map
     */
    private void onSwipingResult_runOnUiThread(final int code, final Map<String, Object> map){

        if(code==RyxSwiperCode.ACTION_AUDIO_READY_SUCCESS||code==RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_SUCCESS){
            if (!"".equals(currentConnectblueDeviceMac) && !currentConnectblueDeviceMac.equals(myshareblueDeviceMac)) {
                PreferenceUtil.getInstance(SwipingService.this).saveString("myblueDeviceMac", currentConnectblueDeviceMac);
            }
            onServicecommand_runOnUiThread(SHOWMESSAGEDIALOG, "设备初始化中，请稍后...");
            ryxSwiper.getKsn();
        }else if(code==RyxSwiperCode.ACTION_SWIPE_IC_READING){
            onServicecommand_runOnUiThread(SHOWTOASTMSG, "IC卡读取中，等待界面跳转，交易完成前请勿拔卡");
        }else if(code==RyxSwiperCode.ACTION_GETKSN_FAIL){
            //获取ksn信息
            onServicecommand_runOnUiThread(MESSAGEDIALOGDISMISS);
            onServicecommand_runOnUiThread(SHOWTOASTMSG, "获取设备信息失败!");
        }else if(code==RyxSwiperCode.ACTION_SWIPER_CANCEL){
            onServicecommand_runOnUiThread(SHOWTOASTMSG, "交易取消!");
            onServicecommand_runOnUiThread(ICDIALOGDISMISS);
            onServicecommand_runOnUiThread(EXITSWIPER);
        }else if(code==RyxSwiperCode.ACTION_AUDIO_READY_FAIL||
                code==RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_FAIL||
                code==RyxSwiperCode.ACTION_SWIPER_FAIL){
            onServicecommand_runOnUiThread(SHOWTOASTMSG, "连接设备失败!");
            onServicecommand_runOnUiThread(MESSAGEDIALOGDISMISS);
            onServicecommand_runOnUiThread(ICDIALOGDISMISS);
        }else if(code==RyxSwiperCode.ACTION_SWIPER_TIMEOUT){
            onServicecommand_runOnUiThread(SHOWTOASTMSG, "操作超时!");
            onServicecommand_runOnUiThread(ICDIALOGDISMISS);
        }else if(code==RyxSwiperCode.ACTION_CARDDATA_READER){
            onServicecommand_runOnUiThread(SHOWMESSAGEDIALOG, "数据读取中,请耐心等待...");
        }else if(code==RyxSwiperCode.ACION_SWIPER_DEMOTIONTRADE){
            String demotionTrade = MapUtil.getString(map, "demotionTrade");
            onServicecommand_runOnUiThread(SHOWTOASTMSG, demotionTrade);
            onServicecommand_runOnUiThread(STARTSWIPERCARD);
        }




        ((Activity)mCallback).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onSwipingResult(code,map);
            }
        });
    }
    private void onServicecommand_runOnUiThread(final int code ){
        ((Activity)mCallback).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onServicecommand(code,null);
            }
        });
    }
    /**
     * 主线程返回服务命令
     */
    private void onServicecommand_runOnUiThread(final int code , final Object data){
        ((Activity)mCallback).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onServicecommand(code,data);
            }
        });
    }

    /**
     * 监听蓝牙连接断开状态
     */
    private void  connectOrDisConnectListen(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(searchBlueToothReceiver, intentFilter);
    }

    private BroadcastReceiver searchBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.showLog("blueisConnected==="+action);
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                blueisConnected=true;
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                blueisConnected=false;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //服务被撤销一定要注销监听广播
        unregisterReceiver(searchBlueToothReceiver);
    }
}
