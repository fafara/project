package com.ryx.payment.payplug.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ryx.payment.payplug.bean.Students;
import com.ryx.payment.ruishua.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供给客户端获取AIDL接口
 *@deprecated
 * 因为包名原因外界使用此方法
 * intent.setComponent(new ComponentName("com.ryx.payment.ruishua","com.ryx.payment.payplug.service.IFactoryService"));
 * 无法连接到Service，故将此service移到ruishua包下，其他业务逻辑不做修改。
 * @see com.ryx.payment.ruishua.service.IFactoryService
 */
public class IFactoryService extends Service {
    private List<Students> students=new ArrayList<Students>();
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.showLog("IFactoryService-----onBind");
        return factoryServiceAidl;
    }
    private IFactoryServiceInterface.Stub factoryServiceAidl= new IFactoryServiceInterface.Stub()

    {
        @Override
        public IRyxPayServiceInterface getMyAidlService() throws RemoteException {
            IRyxPayService remoteService= new IRyxPayService(getApplicationContext(),students);
            Log.d("ryx","remoteService============"+remoteService);
            return remoteService;
        }
    };
}
