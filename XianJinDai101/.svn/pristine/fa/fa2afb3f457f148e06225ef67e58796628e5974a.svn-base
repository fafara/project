package com.ryx.swiper;

import java.util.Map;

/**
 * Created by laomao on 15/12/3.
 */
public interface ISwiperStateListener {
	public void onAudioDevOpenSuccess();
	public void onAudioDevOpenFail();
    public void onBluetoothConnectSuccess();
    public void onBluetoothConnectFail();

    public void onDetectStart();

    public void onDetected();

    public void onDetecteError();

    public void onCardSwipeDetected();

    public void onDecodeError(String arg1);

    public void onInterrupted();

    public void onNoDeviceDetected();

    public void onTimeout();

    public void onDecodingStart();

    public void onWaitingForCardSwipe();

    public void onWaitingForDevice();
    /**
     * 刷卡后卡数据读取中
     */
    public void onWaitingCardDataReader();
    
    public void onDevicePlugged();

    public void onDeviceUnplugged();


    public void onDecodeCompleted(Map<String, Object> map);

    public void onError(int arg0, String arg1);

    public void onGetKsnCompleted(String ksn);

    public void onGetCardNoCompleted(String carno, String expirydate);

    public void EmvOperationWaitiing();

    public void onICResponse(int arg0, byte[] arg1, byte[] arg2);

    public void onWaitingForICCardSwipe();

    public void onCancelTimeout();
    
    public void onUpdateTerminalParamsCompleted(Map<String, Object> map);
    public void onUpdateTerminalParamsFailed(Map<String, Object> map);
    /**
     * 降级交易回调
     * @param map
     */
    public void onDemotionTrade(Map<String, Object> map);
    /**
     * 取消刷卡
     */
    public void onCancelSwiper();
    
}
