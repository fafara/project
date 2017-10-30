package com.ryx.payment.ruishua.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ryx.payment.payplug.bean.Students;
import com.ryx.payment.payplug.service.IFactoryServiceInterface;
import com.ryx.payment.payplug.service.IRyxPayService;
import com.ryx.payment.payplug.service.IRyxPayServiceInterface;
import com.ryx.payment.ruishua.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供给客户端获取AIDL接口
 *@see com.ryx.payment.payplug.service.IFactoryService
 */
public class IFactoryService extends Service {
    private List<Students> students=new ArrayList<Students>();
    public IFactoryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.showLog("MyService=======onBind====1111===");
        // TODO: Return the communication channel to the service.
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
