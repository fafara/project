package com.ryx.swiper;

import java.util.Map;

/**
 * Created by laomao on 15/12/3.
 */
public abstract class CSwiperAdapter {



    private ISwiperStateListener listener;

    public abstract void searchBlueDevs(IRyxDevSearchListener listener);

    public abstract int connectCSwiper(String address);
    public abstract int getTerminalType();


    //蓝牙初始化
    public abstract int initCSwiper(String address);


    public abstract void startEmvSwiper(Map<String,Object> map);

    public void stopCSwiper(){}

    public abstract void disConnect();
    public abstract void releaseCSwiper();
    public void setDetectDeviceChange(boolean b){}
    public abstract String getKsn();
    public abstract void getKsnSync();
    public abstract boolean isDevicePresent();
    /**
     *  获取设备类型</br>
	 * 带键盘非接MPOS类型设备</br>
	 * 普通带键盘设备</br>
	 *  普通不带键盘设备</br>
	 *  当前接口调用是在getKsn信息之后再进行调用当前方法
     * @return
     */
    public abstract int getDeviceType();
    public void printData(String data){};
    public abstract String getCardno();
    public abstract void writIc(String resp ,String icdata);

    public abstract boolean updateParam(int flag ,int packageNo,String data);


}
