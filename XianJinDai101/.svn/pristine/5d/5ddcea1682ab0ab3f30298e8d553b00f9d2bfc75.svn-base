package com.ryx.swiper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.itron.android.lib.Logger;
import com.ryx.lib.BLECommandController;
import com.ryx.swiper.beans.DeviceInfo;
import com.ryx.swiper.devs.centerm.CentermBluAdapter;
import com.ryx.swiper.devs.dynamic.DynamicAdapter;
import com.ryx.swiper.devs.itron.BlueToothCommandAdapter;
import com.ryx.swiper.devs.itron.F2fMiniAdapter;
import com.ryx.swiper.devs.itron.F2fMiniIcAdapter;
import com.ryx.swiper.devs.landi.LanDiAudioAdapter;
import com.ryx.swiper.devs.landi.LanDiBlueAdapter;
import com.ryx.swiper.devs.newland.NewLandBlueToothAdapter;
import com.ryx.swiper.devs.pax.PaxBlueToothAdapter;
import com.ryx.swiper.devs.whty.WhtyBlueToothAdapter;
import com.ryx.swiper.devs.xgd.XgdBluek100Adapter;
import com.ryx.swiper.utils.LogUtil;

/**
 * Created by laomao on 15/12/3.
 */
public class RyxSwiper implements ISwiperStateListener {

	private Context context;
	private int devType;
	private IRyxSwiperListener listener;

	private CSwiperAdapter adapter;
	private Logger logger = Logger.getInstance(RyxSwiper.class);
	/**
	 * 刷卡失败后需要重新调用刷卡接口的设备类型数组
	 */
	private int [] needagainswiperTypes={RyxSwiperCode.DEVICE_XGD_BULETOOTH,RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH,RyxSwiperCode.DEVICE_AC_BULETOOTH,RyxSwiperCode.DEVICE_TY_BULETOOTH,RyxSwiperCode.DEVICE_DYNAMIC_BULETOOTH,RyxSwiperCode.DEVICE_PAX_BULETOOTH};
	/**
	 * 需要更新公钥和AId的设备数组
	 */
	private int [] needdownloadKey={RyxSwiperCode.DEVICE_LANDI_BULETOOTH,RyxSwiperCode.DEVICE_XGD_BULETOOTH,RyxSwiperCode.DEVICE_TY_BULETOOTH,RyxSwiperCode.DEVICE_PAX_BULETOOTH,RyxSwiperCode.DEVICE_CENTERM_BULETOOTH};
	
	public RyxSwiper(Context context, int devType, IRyxSwiperListener listener) {
		this.context = context;
		this.devType = devType;
		this.listener = listener;
		initAdapter(devType);
	}

	/**
	 * 初始化设备适配
	 * 
	 * @param devType
	 */
	private void initAdapter(int devType) {
		Log.e("initAdapter","initAdapter="+devType);
		switch (devType) {
		case RyxSwiperCode.DEVICE_AC_AUDIO_MINI:
			adapter = new F2fMiniAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_AC_AUDIO_IC:
			adapter = new F2fMiniIcAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_AC_BULETOOTH:
			adapter=new BlueToothCommandAdapter(context, this);
			break;
//		case RyxSwiperCode.DEVICE_AC_SMALL_BULETOOTH:
//			adapter = new BuleToothAdapter(context, this);
//			break;
//		case RyxSwiperCode.DEVICE_AC_BIG_BULETOOTH:
//			adapter = new BuleToothBigAdapter(context, this);
//			break;
		case RyxSwiperCode.DEVICE_LANDI_AUDIO:// 联迪音频设备
			adapter = new LanDiAudioAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_LANDI_BULETOOTH:// 联迪蓝牙设备
			adapter = new LanDiBlueAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH:// 新大陆蓝牙设备
			adapter = new NewLandBlueToothAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_XGD_BULETOOTH:// 新国都蓝牙k100
			adapter = new XgdBluek100Adapter(context, this,"");
			break;
		case RyxSwiperCode.DEVICE_XGD_K205_BULETOOTH://新国都蓝牙K205
			adapter=new XgdBluek100Adapter(context, this, BLECommandController.DEVICENAME);
			break;
		case RyxSwiperCode.DEVICE_TY_BULETOOTH://天喻蓝牙
			adapter=new WhtyBlueToothAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_DYNAMIC_BULETOOTH://动联蓝牙
			adapter=new DynamicAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_PAX_BULETOOTH://百富蓝牙
			adapter=new PaxBlueToothAdapter(context, this);
			break;
		case RyxSwiperCode.DEVICE_CENTERM_BULETOOTH://升腾蓝牙C821E设备
			adapter=new CentermBluAdapter(context, this);
			break;

		}
	}

	/**
	 * 设备是否就绪
	 * 
	 * @return
	 */
	public boolean hasdevice() {
		return adapter.isDevicePresent();
	}

	/**
	 * 打印数据
	 * 
	 * @param data
	 */
	public void printdata(final String data) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				adapter.printData(data);
			}
		}).start();

	}
	/**
	 * 获取设备类型</br>
	 * 带键盘非接MPOS类型设备</br>
	 * 普通带键盘设备</br>
	 *  普通不带键盘设备</br>
	 *  当前接口调用是在getKsn信息之后再进行调用当前方法
	 * @return
	 */
	public int getDeviceType(){
		return adapter.getDeviceType();
		
	}

	/**
	 * 启动刷卡
	 * 
	 * @param map
	 */
	public void swiperCard(Map<String, Object> map) {
		adapter.startEmvSwiper(map);
	}

	/**
	 * 重新启动刷卡
	 * @param map
	 */
	public void againSwiperCard(Map<String, Object> map){
		for (int arg : needagainswiperTypes) {
			if(this.devType==arg){
				adapter.startEmvSwiper(map);
				break;
			}
		}
	}
	/**
	 * 重新获取卡号
	 */
	public void agingetCardNo(){
		for (int arg : needagainswiperTypes) {
			if(this.devType==arg){
				adapter.getCardno();
				break;
			}
		}
	}
	/**
	 * 获取卡号
	 * 
	 * @param
	 */
	public void getCardNo() {
		adapter.getCardno();
	}

	/**
	 * 获取KSN
	 * 
	 * @param
	 */
	public void getKsn() {
		adapter.getKsnSync();
	}

	/**
	 * 释放设备
	 */
	public void disconnectSwiper() {
		if (adapter != null) {
			adapter.disConnect();
			adapter.stopCSwiper();
		}
		adapter.releaseCSwiper();
	}

	/**
	 * 设备连接
	 */
	public int connectSwiper(String address) {
		LogUtil.printInfo("connectSwiper");
		logger.info("connectSwiper");
		beforeBluConnectInit(address);
		
		return adapter.connectCSwiper(address);

	}
	/**
	 * 蓝牙设备连接之前先进行判断是否已经进行过本地匹配,如果没有匹配则进行调用下设备搜索接口</br>
	 * 处理某些机型蓝牙匹配框不显示问题
	 * @param address
	 */
	private void beforeBluConnectInit(String address){
		//艾创，联迪，首次配对需要先调用连接,小米等个别机型才弹出提示框否则只是消息提示,天喻设备,新国都，新大陆设备首次自动配对不会弹任何框.
//		动联设备暂无搜索方法
			boolean isHaveBonde=false;
		if(devType==RyxSwiperCode.DEVICE_AC_BULETOOTH
				||devType==RyxSwiperCode.DEVICE_LANDI_BULETOOTH){
		BluetoothAdapter bluetoothAdapter=	BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> bondedDevices= bluetoothAdapter.getBondedDevices();
		if(bondedDevices!=null){
		for (BluetoothDevice bluetoothDevice : bondedDevices) {  
			if(address.equals(bluetoothDevice.getAddress())){
				isHaveBonde=true;
				break;
			}
		}  
		}
		LogUtil.printInfo(address+"==是否配对=="+isHaveBonde);
		if(!isHaveBonde){
			searchBlueDevs(new IRyxDevSearchListener() {
				
				@Override
				public void discoverOneDevice(DeviceInfo deviceInfo) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void discoverComplete() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void disConnected() {
					// TODO Auto-generated method stub
					
				}
			});
			try {
				//保证搜索在前，连接在后此处休息休息
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}}
	public void searchBlueDevs(IRyxDevSearchListener listener) {
		LogUtil.printInfo("connectSwiper");
		logger.info("connectSwiper");

		adapter.searchBlueDevs(listener);

	}

	public void icWriteBack(String code, String content) {
		LogUtil.printInfo("code=="+code+",content=="+content);
		if(TextUtils.isEmpty(content)||"null".equals(content)){
			//服务端返回："null",null,""IC回写无需调用SDK直接回调成功即可
			this.onICResponse(1, null, null);
		}else{
			adapter.writIc(code, content);
		}
	}
	/**
	 * 注入公钥或者aid
	 * @param _flag
	 * @param _index
	 * @param _icdata
	 */
	public void updateTerminalParams(int _flag,  final int _index, String _icdata)
	{
		//服务端可能返回31开头的数据,在此统一去除31字符，后续各自根据设备类型情况自行拼接31字符
		if(!TextUtils.isEmpty(_icdata)&&_icdata.startsWith("31")){
			_icdata=_icdata.substring(2);
		}
		Log.d("ryx", "_flag=="+_flag+",_index=="+_index+",_icdata=="+_icdata);
		adapter.updateParam(_flag, _index, _icdata);
	}
	/**
	 *当前设备是否需要下载公钥 
	 * @return
	 */
	public boolean isNeeddownloadKey(){
		//联迪蓝牙,新国都蓝牙设备进行公钥下载,其余暂不支持公钥下载
//		if(devType==RyxSwiperCode.DEVICE_LANDI_BULETOOTH||devType==RyxSwiperCode.DEVICE_XGD_BULETOOTH){
//			return true;
//		}else{
//			return false;
//		}
		for (int arg : needdownloadKey) {
			if(this.devType==arg){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onBluetoothConnectSuccess() {
//		Log.e("onBluetoothConnectSuccess", "onBluetoothConnectSuccess");
		// Log.e("dev_type====", adapter.getTerminalType()+"");
		
		listener.onswiperResult(RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_SUCCESS,
				null);
	}

	@Override
	public void onBluetoothConnectFail() {
		listener.onswiperResult(RyxSwiperCode.ACTION_BLUETOOTH_CONNECT_FAIL,
				null);
	}

	@Override
	public void onDetectStart() {

	}

	@Override
	public void onDetected() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dev_type", adapter.getDeviceType());
		String ksn=adapter.getKsn();
		map.put("dev_ksn", ksn);
		map.put("msg", "检测到设备");
		Log.e("xgd", "exec_ondetected");
		LogUtil.printInfo("ksn========"+ksn);
		listener.onswiperResult(RyxSwiperCode.ACTION_GETKSN_SUCCESS, map);
	}

	@Override
	public void onDetecteError() {
		Map<String, Object> map = new HashMap<String, Object>();
		listener.onswiperResult(RyxSwiperCode.ACTION_GETKSN_FAIL, map);
	}

	@Override
	public void onCardSwipeDetected() {

	}

	@Override
	public void onDecodeError(String arg1) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("errmessage", arg1);
		LogUtil.printInfo("errmessage=="+arg1);
		listener.onswiperResult(RyxSwiperCode.ACTION_SWIPER_FAIL, map);
	}

	@Override
	public void onInterrupted() {

	}

	@Override
	public void onNoDeviceDetected() {

	}

	@Override
	public void onTimeout() {
		listener.onswiperResult(RyxSwiperCode.ACTION_SWIPER_TIMEOUT, null);
	}

	@Override
	public void onDecodingStart() {

	}

	@Override
	public void onWaitingForCardSwipe() {
		listener.onswiperResult(
				RyxSwiperCode.ACTION_SWIPER_WAITTING_SWIPERCARD, null);
	}

	@Override
	public void onWaitingForDevice() {

	}

	@Override
	public void onDevicePlugged() {
		listener.onswiperResult(RyxSwiperCode.ACTION_SWIPER_DEV_PLUGGED, null);
	}

	@Override
	public void onDeviceUnplugged() {
		listener.onswiperResult(RyxSwiperCode.ACTION_SWIPER_DEV_UNPLUGGED, null);
	}

	@Override
	public void onDecodeCompleted(Map<String, Object> map) {
		if(map.containsKey("has_pin"))
		{
			listener.onswiperResult(
					RyxSwiperCode.ACTION_SWIPER_SUCCESS_WITHPIN, map);
		} else {
			listener.onswiperResult(RyxSwiperCode.ACTION_SWIPER_SUCCESS, map);
		}
	}

	@Override
	public void onError(int errorCode, String arg1) {
		// todo 这个地方目前适配钱拓，其他厂商考虑错误类型
		if (-2001 == errorCode) {
			listener.onswiperResult(RyxSwiperCode.ACTION_SWIPE_REDO_ERROR, null);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("msg", arg1);
			listener.onswiperResult(RyxSwiperCode.ACION_SWIPER_COMMON_ERROR, map);
//			listener.onswiperResult(RyxSwiperCode.ACTION_SWIPE_ERROR_FAIL, null);
//			adapter.stopCSwiper();
		}
	}

	@Override
	public void onGetKsnCompleted(String ksn) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ksn", ksn);
		listener.onswiperResult(RyxSwiperCode.ACTION_GETKSN_SUCCESS, map);
	}

	@Override
	public void onGetCardNoCompleted(String carno,String expirydate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_no", carno);
		if(!TextUtils.isEmpty(expirydate)&&expirydate.length()>=4){
			map.put("expirydate", expirydate.substring(0, 4));
		}else{
			map.put("expirydate", "");
		}
		listener.onswiperResult(RyxSwiperCode.ACTION_GETCARDNO_SUCCESS, map);
	}

	@Override
	public void EmvOperationWaitiing() {
		listener.onswiperResult(RyxSwiperCode.ACTION_SWIPE_IC_READING, null);
	}

	@Override
	public void onICResponse(int result, byte[] resultScript, byte[] data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("resultScript", resultScript);
		map.put("data", data);
		listener.onWriteResult(RyxSwiperCode.ACTION_SWIPE_IC_WRITEBACK, map);

	}

	@Override
	public void onWaitingForICCardSwipe() {

	}

	@Override
	public void onCancelTimeout() {

	}

	@Override
	public void onAudioDevOpenSuccess() {
		listener.onswiperResult(RyxSwiperCode.ACTION_AUDIO_READY_SUCCESS, null);
	}

	@Override
	public void onAudioDevOpenFail() {
		listener.onswiperResult(RyxSwiperCode.ACTION_AUDIO_READY_FAIL, null);
	}

	@Override
	public void onUpdateTerminalParamsCompleted(Map<String, Object> map) {
		listener.ondownloadResult(RyxSwiperCode.ACTION_DOWNLOADRESULT_SUCCESS, map);
	}

	@Override
	public void onUpdateTerminalParamsFailed(Map<String, Object> map) {
		listener.ondownloadResult(RyxSwiperCode.ACTION_DOWNLOADRESULT_FAIL, map);
	}

	@Override
	public void onWaitingCardDataReader() {
		listener.onswiperResult(RyxSwiperCode.ACTION_CARDDATA_READER, null);
	}

	@Override
	public void onDemotionTrade(Map<String, Object> map) {
		// TODO Auto-generated method stub
		listener.onswiperResult(RyxSwiperCode.ACION_SWIPER_DEMOTIONTRADE, map);
	}

	@Override
	public void onCancelSwiper() {
		// TODO Auto-generated method stub
		listener.onswiperResult(RyxSwiperCode.ACTION_SWIPER_CANCEL, null);
	}
}
