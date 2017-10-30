package com.ryx.swiper.devs.pax;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.widget.Toast;

import com.pax.commonlib.convert.Convert;
import com.pax.ryx.api.RyxD121PaxMpos;
import com.pax.ryx.inf.BTCommandController.TransactionInfo;
import com.pax.ryx.listener.CommandStateChangedListener;
import com.pax.ryx.listener.CommandStateChangedListener.CardType;
import com.pax.ryx.mis.Enum.CmdType;
import com.pax.ucswiper.mis.UEnum.UParamType;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.beans.DeviceInfo;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.DataUtil;
import com.ryx.swiper.utils.DateUtil;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;

/**
 * 百富蓝牙设备D121适配器
 */
public class PaxBlueToothAdapter extends CSwiperAdapter {
	private Context context;
	private IRyxDevSearchListener devSearchlistener;
	private ISwiperStateListener listener;
	private RyxD121PaxMpos reader;
	private BluetoothAdapter bluetoothAdapter;
	private List<DeviceInfo> discoveredDevices = new ArrayList<DeviceInfo>();
	private String orderid;
	private String strKsn;
	CardType cardType;
	byte[] secondTrack;
	byte[] thirdTrack;
	String mTrack1 = "";
	String mTrack2;
	String mTrack3;
	int track1Length;
	int track2Length;
	int track3Length;
	String maskedPAN;// 使用卡号
	String expiryDate;
	String cardMAC = "";
	byte[] ic55Data = null;
	
	String randomNumber;
	int packageNo;
	String panSerial;
	byte[] random;
	TransactionInfo info;

	public PaxBlueToothAdapter(Context context, ISwiperStateListener listener) {
		LogUtil.setLogdebug(true);
		LogUtil.printInfo("PaxBlueToothAdapter--------------------");
		this.context = context;
		this.listener = listener;
		reader = RyxD121PaxMpos.getInstance(context);
		reader.setBTCommandControllerListener(commandListener);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		/** 注册一个蓝牙发现监听器 */
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(discoveryReciever, filter);
	}

	/**
	 * 默认蓝牙接受处理器
	 */
	private final BroadcastReceiver discoveryReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (ifAddressExist(device.getAddress())) {
					return;
				}
				DeviceInfo devInfo = new DeviceInfo();
				devInfo.name = device.getName() == null ? device.getAddress() : device.getName();
				devInfo.identifier = device.getAddress();
				discoveredDevices.add(devInfo);
				devSearchlistener.discoverOneDevice(devInfo);
			}
		}
	};

	/**
	 * 启动蓝牙搜索
	 */
	private void startDiscovery() {
		LogUtil.printInfo("startDiscovery=");
		if (bluetoothAdapter.isEnabled()) {
			if (discoveredDevices != null) {
				discoveredDevices.clear();
			}
			bluetoothAdapter.startDiscovery();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e) {
					} finally {
						bluetoothAdapter.cancelDiscovery();
						// devSearchlistener.discoverComplete();
					}
				}
			}).start();
		} else {
			Toast.makeText(context, "蓝牙未打开！", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 检查是蓝牙地址是否已经存在
	 * 
	 * @return
	 */
	private boolean ifAddressExist(String addr) {
		for (DeviceInfo devcie : discoveredDevices) {
			if (addr.equals(devcie.identifier))
				return true;
		}
		return false;
	}

	@Override
	public void searchBlueDevs(IRyxDevSearchListener listener) {
		devSearchlistener = listener;
		startDiscovery();
	}

	@Override
	public int connectCSwiper(String address) {
		return initCSwiper(address);
	}

	@Override
	public int getTerminalType() {
		return 0;
	}

	@Override
	public int initCSwiper(String address) {
		LogUtil.printInfo("initCSwiper=" + address);
		context.unregisterReceiver(discoveryReciever);
		reader.openDevice(address);
		return 0;
	}

	@Override
	public void startEmvSwiper(Map<String, Object> map) {
		LogUtil.printInfo("startEmvSwiper--------------------------------------map=" + map);
		final String amount = MapUtil.getString(map, "amount");
		orderid = MapUtil.getString(map, "order_id");
		info = new TransactionInfo();
		info.amount = amount;
		info.date =DateUtil.getDateString(DateUtil.STR_DATE);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
		info.time = timeFormat.format(new Date());
		info.transNum = "01";
		info.transType = 0x01;
		info.currencyCode = new byte[] { 0x01 };
		String logNo = CryptoUtils.getInstance().getTransLogNo();
		LogUtil.printInfo("startEmvSwiper--------------------------------------logNo=" + logNo);
		CryptoUtils.getInstance().setTransLogUpdate(false);
		random = Convert.str2Bcd(logNo);
		LogUtil.printInfo("ryx==random==startEmvSwiper="+Convert.bcd2Str(random)) ;
		reader.startEmvSwiper(random, info, 60);
	}

	@Override
	public void disConnect() {
		reader.closeDevice();
	}

	@Override
	public void releaseCSwiper() {
		LogUtil.printInfo("releaseCSwiper");
	}

	@Override
	public String getKsn() {
		return this.strKsn;
	}

	@Override
	public void getKsnSync() {
		LogUtil.printInfo("getKsnSync");
		reader.getTermInfo();
	}

	@Override
	public boolean isDevicePresent() {
		LogUtil.printInfo("isDevicePresent");
		return false;
	}

	@Override
	public int getDeviceType() {
		LogUtil.printInfo("getDeviceType");
		return RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
	}

	@Override
	public String getCardno() {
		LogUtil.printInfo("getCardno");
		Map<String, Object> map=new HashMap<String, Object>();
    	map.put("buss_type", "");
    	map.put("order_id", "0000000000000000");
    	map.put("amount", "000000000000");
    	startEmvSwiper(map);
		return null;
	}

	@Override
	public void writIc(String resp, String icdata) {
		LogUtil.printInfo("writIc======resp="+resp+" icdata="+icdata);
		if("".equals(icdata)){
			listener.onICResponse(1, null, null);
			return;
		}
		reader.secondIssuance(resp, Convert.str2Bcd(icdata));
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String publicKeyStr) {
		this.packageNo=packageNo;
		LogUtil.printInfo("updateParam======flag="+flag+" packageNo="+packageNo+ " data="+publicKeyStr);
		ArrayList<byte[]>  dataArry = new ArrayList<byte[]>();
		dataArry.add(Convert.str2Bcd(publicKeyStr));
		reader.updateTerminalParameters(flag==0?UParamType.PAX_PUK:UParamType.PAX_AID, dataArry);
		return false;
	}

	CommandStateChangedListener commandListener = new CommandStateChangedListener() {

		@Override
		public void onCardSwipeDetected() {
			LogUtil.printInfo("onCardSwipeDetected");
		}

		@Override
		public void onClearTermiParasListener() {

		}

		@Override
		public void onEmvOperationWaitiing() {
			LogUtil.printInfo("================onEmvOperationWaitiing===================");
			listener.EmvOperationWaitiing();
		}

		
		@Override
		public void onGetTermInfoSuccess(TerminalInfo terminalInfo) {
			LogUtil.printInfo("terminalInfo=" + terminalInfo.toString());
			strKsn = terminalInfo.ksn;
			listener.onDetected();
		}

		@Override
		public void onOpenDivceSuccess() {
			LogUtil.printInfo("================onOpenDivceSuccess===================");
			listener.onBluetoothConnectSuccess();
		}

		@Override
		public void onSecondIssuanceSuccess(int result, byte[] resultScript, byte[] iccData) {
//			LogUtil.printInfo("onSecondIssuanceSuccess result="+result+" resultScript="+Convert.bcd2Str(resultScript)+" iccData="+Convert.bcd2Str(iccData));
			LogUtil.printInfo("onSecondIssuanceSuccess=="+result);
			listener.onICResponse(1, resultScript, iccData);
		}

		@Override
		public void onStartEmvSwiperSuccess(CommandReturn result) {
			try {
			LogUtil.printInfo("================onStartEmvSwiperSuccess===================");
			cardType=result.cardType;
//			StringBuffer stringBuffer = new StringBuffer("");
//			if (result.track2 != null) {
//				stringBuffer.append("\n" + "track2:" + Convert.bcd2Str(result.track2));
//				stringBuffer.append("\n" + "track2Length:" + result.track2Length);
//			}
//			if (result.track3 != null) {
//				stringBuffer.append("\n" + "track3:" + Convert.bcd2Str(result.track3));
//				stringBuffer.append("\n" + "track3Length:" + result.track3Length);
//			}
//			stringBuffer.append("\n" + "CardType:" + result.cardType + "\n" + "pan:" + result.pan + "\n" + "cardSerial:" + result.cardSerial + "\n" + "cvm:" + result.cvm + "\n" + "expiryDate:" + result.expiryDate + "\n" + "random:" + Convert.bcd2Str(result.random));
//			LogUtil.printInfo(stringBuffer.toString());
			
			listener.onWaitingCardDataReader();
			secondTrack = result.track2;
			thirdTrack = result.track3;
			mTrack1 = "";
			mTrack2 = (secondTrack == null ? "" : Convert.bcd2Str(secondTrack).replaceAll(" ", ""));
			mTrack3 = (thirdTrack == null ? "" : Convert.bcd2Str(thirdTrack).replaceAll(" ", ""));
			track1Length = mTrack1 == null ? 0 : mTrack1.length() / 2;
			track2Length = mTrack2 == null ? 0 : result.track2Length;
			track3Length = mTrack3 == null ? 0 : result.track3Length;
			maskedPAN = result.pan;// 使用卡号
			expiryDate = result.expiryDate;
			listener.onGetCardNoCompleted(maskedPAN, expiryDate);
			ic55Data = result.emvDataInfo;
			randomNumber = Convert.bcd2Str(result.random).substring(0, 8);
			String macInput = DataUtil.genMacHexString(mTrack2, mTrack3, randomNumber, strKsn, orderid);
			LogUtil.printInfo(">>>>Mac入参:" + macInput);
			String logNo = CryptoUtils.getInstance().getTransLogNo();
			LogUtil.printInfo("onStartEmvSwiperSuccess--------------------------------------logNo=" + logNo);
			byte[] random = Convert.str2Bcd(logNo);
			LogUtil.printInfo("ryx==random==getMac="+Convert.bcd2Str(random)) ;
			panSerial=TextUtils.isEmpty(result.cardSerial)?"":result.cardSerial;
			reader.getMac(random, Convert.str2Bcd(macInput));
			LogUtil.printInfo("元数据====mTrack2=" + mTrack2 + ",mTrack3=" + mTrack3 + ",randomNumber=" + randomNumber + ",ksn=" + strKsn + ",cardNumber=" + maskedPAN + ",encTracks=" + mTrack1 + mTrack2 + mTrack3);
			} catch (Exception e) {
				listener.onError(0x0089,"数据解析异常,请重新操作!");
			}
			}

		@Override
		public void onUpdateTerminalParaSuccess() {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("index", packageNo);
			listener.onUpdateTerminalParamsCompleted(map);
		}

		@Override
		public void onWaitingForCard() {
			LogUtil.printInfo("================onWaitingForCard===================");
			listener.onWaitingForCardSwipe();
		}

		@Override
		public void onGetMacSuccess(byte[] mac) {
			try {
			LogUtil.printInfo(">>>>Mac结果元==:" + Convert.bcd2Str(mac));
			cardMAC = Convert.bcd2Str(mac).substring(0, 16);
			LogUtil.printInfo(">>>>Mac结果:" + cardMAC);
			String cardofType=(cardType==CardType.IC_CARD)?"11":"10";
			Map<String, Object> resultmapp = DataUtil.putCardServerData(cardofType, RyxSwiperCode.DEVICE_PAX_BULETOOTH, cardMAC, strKsn, track1Length, track2Length, track3Length, randomNumber, maskedPAN, mTrack1 + mTrack2 + mTrack3, expiryDate, panSerial, ic55Data);
			LogUtil.printInfo("加密数据===" + resultmapp.get("card_info").toString());
			listener.onDecodeCompleted(resultmapp);
			} catch (Exception e) {
				listener.onError(0x0089,"数据解析异常,请重新操作!");
			}
		}

		@Override
		public void onError(CmdType arg0, int code, String msg) {
//			OPEN_DEVICE,打开设备
//			CLEAR_TERMINAL_PARAMS,清除参数
//			GET_TERMINAL_INFO,
//			UPDATA_TERMINAL_PARAMS,更新公钥
//			GET_MAC,获取mac
//			START_EMV_SWIPER,启动刷卡
//			SECOND_ISSUANCE IC回写
			
			LogUtil.printInfo("onError--------code:" + code + "msg:" + msg);
			if (arg0==CmdType.START_EMV_SWIPER&&0x6F06 == code) {
				//不允许降级交易
				Map<String, Object> errorMap = new HashMap<String, Object>();
				errorMap.put("demotionTrade", msg);
				listener.onDemotionTrade(errorMap);
				// reader.startEmvSwiper(random, info, 60);
			}
			else if(arg0==CmdType.UPDATA_TERMINAL_PARAMS){
				//更新公钥和Aid错误
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("index", packageNo);
				listener.onUpdateTerminalParamsFailed(map);
			}else if(arg0==CmdType.SECOND_ISSUANCE){
				//IC回写错误
				listener.onICResponse(0, null, null);
			}
			else {
			
				listener.onError(code, msg);
			}
		}

	};
}
