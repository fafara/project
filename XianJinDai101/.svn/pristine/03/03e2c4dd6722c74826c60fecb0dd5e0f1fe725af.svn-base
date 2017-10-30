package com.ryx.payment.payplug.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.ryx.payment.payplug.activity.RyxPayOrderMainActivity_;
import com.ryx.payment.payplug.activity.RyxPaySwiperActivity;
import com.ryx.payment.payplug.bean.SerializableMap;
import com.ryx.payment.payplug.bean.Students;
import com.ryx.payment.ruishua.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IRyxPayService extends IRyxPayServiceInterface.Stub{
    private List<Students> students=new ArrayList<Students>();
    private Context context;
    private ICallBack mCallBack;
    LocalBroadcastManager localBroadcastManager;
    IntentFilter filter;
    public IRyxPayService(Context context, List<Students> list) {
        this.students=list;
        this.context = context;
        //注册本地监听广播
        localBroadcastManager=  LocalBroadcastManager.getInstance(context);
        dataReceiver = new DataReceiver();
         filter = new IntentFilter();
        filter.addAction("com.ryxpay.payplug.server");
        localBroadcastManager.registerReceiver(dataReceiver, filter);
    }
    @Override
    public int add(int num1, int num2) throws RemoteException {
        return num1 + num2;
    }

    @Override
    public List<Students> addStudent(Students student) throws RemoteException {
        students.add(student);
        return students;
    }

    @Override
    public void addStudentCallBack(Students student, ICallBack callBack) throws RemoteException {
        mCallBack = callBack;
        students.add(student);
        Log.d("ryx","ryx==Context"+context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Log.d("ryx","ryx=="+students.toString());
                    mCallBack.parcelableBeanCallBack(students);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 此接口后续调整
     * @deprecated
     * @param reqCode
     * @param callBack
     * @throws RemoteException
     */
    @Override
    public void getCardBalance(String reqCode, ICallBack callBack) throws RemoteException {
        mCallBack = callBack;
        dataReceiver = new DataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ryxpay.payplug.server");
        context.registerReceiver(dataReceiver, filter);
        Intent intent=new Intent(context,RyxPaySwiperActivity.class);
        intent.putExtra("reqCode",reqCode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void toOrderPay(String reqCode, Map reqMap, ICallBack callBack) throws RemoteException {
        mCallBack = callBack;
        //启动界面Activity
        Intent intent=new Intent(context,RyxPayOrderMainActivity_.class);
        intent.putExtra("reqCode",reqCode);
            final SerializableMap myMap=new SerializableMap();
            myMap.setMap(reqMap);//将map数据添加到封装的myMap中
            Bundle bundle=new Bundle();
            bundle.putSerializable("map", myMap);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    DataReceiver dataReceiver;
    private class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ryx","dataReceiver=============="+intent.getAction());
            if (intent.getAction().equals("com.ryxpay.payplug.server")) {
                String code= intent.getStringExtra("code");
                if("unregisterReceiver".equals(code)){
                    //判断code如果为unregisterReceiver则注销自己
                    localBroadcastManager.unregisterReceiver(this);
                    LogUtil.showLog("广播主动注销");
                    return;
                }
                String reqCode= intent.getStringExtra("reqCode");
                Map map = new HashMap<String, String>();
                String msg= intent.getStringExtra("msg");
                String result= intent.getStringExtra("result");
                map.put("result", TextUtils.isEmpty(result)?"":result);
                map.put("msg",TextUtils.isEmpty(msg)?"":msg);
                map.put("code",TextUtils.isEmpty(code)?"":code);
                Log.d("ryx","reqCode=="+reqCode+",map=="+map.toString());
                try {
                    mCallBack.resultCallBack(TextUtils.isEmpty(reqCode)?"":reqCode,map);
                } catch (RemoteException e) {
                    Log.d("ryx","send fail");
                }
            }
        }
    }

}
