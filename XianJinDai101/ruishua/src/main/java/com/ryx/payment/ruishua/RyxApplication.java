package com.ryx.payment.ruishua;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.antfortune.freeline.FreelineCore;
import com.github.tamir7.contacts.Contacts;
import com.nexgo.libble.BleConnectMain;
import com.nexgo.libbluetooth.SppConnectMain;
import com.nexgo.oaf.mpos.jni.MPOSJni;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.ryx.payment.ryxhttp.OkHttpUtils;
import com.ryx.swiper.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import org.scf4a.ConnSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import exocr.bankcard.BankManager;
import oaf.datahub.DatahubInit;

/**
 * Created by muxin on 2016-05-23.
 */
public class RyxApplication extends MultiDexApplication {

    public static RyxApplication instance;
    private List<Activity> stackActivities = new ArrayList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttp();
        initXgD();
        initUmeng();
        initBranch();
        initCardIdentifyPk();
        FreelineCore.init(this);
//        GrowingIO.startTracing(this, "b8bd343337427752");
//        GrowingIO.setScheme("growing.d8e6dc12bf3c61bf");
        Contacts.initialize(this);
//        if(BuildConfig.DEBUG)
//            LeakCanary.install(this);
        Logger
        .init("ryx")              // 默认为PRETTYLOGGER，可以设置成为自定义tag
        .methodCount(2)             // logger所在方法显示开关 0 为不显示，1、2 为不同的方法信息显示样式
//       .hideThreadInfo()              // 线程信息显示，默认打开
        .logLevel(BuildConfig.DEBUG?LogLevel.FULL:LogLevel.NONE);    // 默认是打开日志显示（FULL），关闭（NONE）
//        .methodOffset(0);日志展示默认模式就是0
    }
//    设置银行卡识别的applicationId为当前包名
    private void initCardIdentifyPk(){
        BankManager.setPackageName(getPackageName());
    }

    /**
     * 将Activity添加到退出Activity集合中
     * @param activity
     */
    public void addActivity(Activity activity) {
        stackActivities.add(activity);
    }

    /**
     * 移除Activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
        try {
            if(stackActivities.contains(activity)){
                stackActivities.remove(activity);
            }
        }catch (Exception e){

        }
    }
    /**
     * 根据包名修改BRANCH值
     */
    private void initBranch() {
        RyxAppdata.getInstance(this).initCurrentBranchIdForPakg();
//        RyxAppconfig.initBranchConfig();
        RyxAppdata.getInstance(this).resetCurrentBranchConfig();
    }

    /**
     * 初始化友盟友盟
     */
    private void initUmeng() {
        //设置场景类型
        // EScenarioType. E_UM_NORMAL　　普通统计场景类型
        //  EScenarioType. E_UM_GAME     　　游戏场景类型
        //  EScenarioType. E_UM_ANALYTICS_OEM  统计盒子场景类型
        //  EScenarioType. E_UM_GAME_OEM      　 游戏盒子场景类型
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    private void initXgD() {
        try {
            int version = android.os.Build.VERSION.SDK_INT;
            if (version >= 18) {
                BleConnectMain.getInstance().init(getApplicationContext());
                ConnSession.getInstance();
                DatahubInit.getInstance();
                MPOSJni.getInstance();
                SppConnectMain.getInstance().init(getApplicationContext());
                LogUtil.printInfo("版本为:" + version + ",加载新国都资源.");
            } else {
                LogUtil.printInfo("版本为:" + version + ",未加载新国都资源!!");
            }
        }catch (Exception e){

        }
    }

    private void initOkHttp() {
        final int CONNECT_TIMEOUT = 45;
        final int READ_TIMEOUT = 45;
        final int WRITE_TIMEOUT = 45;
        OkHttpUtils httpUtils = OkHttpUtils.getInstance().debug("OkHttpUtils");
        httpUtils.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpUtils.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpUtils.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
//        httpUtils.setCertificates();
        try {
            OkHttpUtils.getInstance().setCertificates(getAssets().open("STAR.ruiyinxin.com.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 单例模式中获取唯一的MyApplication实例
    public static RyxApplication getInstance() {
        if (null == instance) {
            instance = new RyxApplication();
        }
        return instance;
    }

    /**
     * 清空栈内Activity
     */
    public void finishAllActivity(boolean... isExitProcess){
        for (int i=0;i<stackActivities.size();i++){
          Activity activity=  stackActivities.get(i);
            try {
                activity.finish();
            }catch (Exception e){

            }
        }
        if(isExitProcess.length>0&&isExitProcess[0]){
            //是否需要退出当前进程
            exit();
        }
    }

    public void exit() {
        //杀死进程前保存统计数据
        MobclickAgent.onKillProcess(instance);
        System.exit(0);
    }
}
