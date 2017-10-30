package com.ryx.swiper;

import com.ryx.swiper.beans.DeviceInfo;

/**
 * Created by laomao on 15/12/18.
 */
public  interface IRyxDevSearchListener {
    public void discoverOneDevice(DeviceInfo deviceInfo);
    public void discoverComplete();
    public void disConnected();

}
