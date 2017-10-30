package com.ryx.swiper.devs.xgd;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.itron.android.ftf.Util;
import com.nexgo.common.ByteUtils;
import com.ryx.lib.BLECommandController;
import com.ryx.lib.bean.CardInfo;
import com.ryx.lib.bean.DeviceInfo;
import com.ryx.lib.listener.CommunicationListener;
import com.ryx.lib.listener.DeviceSearchListener;
import com.ryx.lib.utils.AppLogger;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.ByteArrayUtils;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.DataUtil;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;

/**
 * Created by laomao on 16/2/24.
 * 
 */
public class XgdBluek100Adapter extends CSwiperAdapter {
	private static final String TAG = "XgdBluek100Adapter";
	public CSwiperAdapter adapter;
	private Context context;
	private ISwiperStateListener listener;
	String orderid;
	String cardType;
	int devType;
	String cardMAC;
	int track1Length;
	int track2Length;
	int track3Length;
	String randomNumber;
	String maskedPAN,psampin;
	String encTracks;
	String expiryDate;
	String cardSeriNo;
	byte[] ic55Data;
	int xgddevType;
	String miniksn = "";
	int index=-1;
	private BLECommandController bleCommandController;
	/**
	 * 新国都设备适配需要宿主App做以下操作
	 * 1.QtpayApplication中onCreate中执行以下操作
	 * 	BleConnectMain.getInstance().init(getApplicationContext());
        ConnSession.getInstance();
        DatahubInit.getInstance();
        MPOSJni.getInstance().init();
       2.在MainFragmentActivity中的onDestroy方法中执行
       	DatahubInit.getInstance().uninit();
        BleConnectMain.getInstance().uninit();
        MPOSJni.getInstance().uninit();
                 谨记,如不进行这两步操作，宿主App将无法正常连接使用.
	 * @param context
	 * @param listener
	 */
	public XgdBluek100Adapter(Context context, ISwiperStateListener listener,String blueFlag) {
		
		
		this.context = context;
		this.listener = listener;
		AppLogger.setDEBUG(false);
		bleCommandController = BLECommandController.GetInstance(context,
				communicationListener);
		try
		{
			bleCommandController.init(blueFlag);
		}
		catch(Exception e)
		{
			bleCommandController.uninit();
			bleCommandController.init(blueFlag);
		}
	}

	@Override
	public void searchBlueDevs(final IRyxDevSearchListener listener) {
		bleCommandController.searchDevices(new DeviceSearchListener() {

			@Override
			public void discoverOneDevice(DeviceInfo deviceInfo) {
				com.ryx.swiper.beans.DeviceInfo devInfo = new com.ryx.swiper.beans.DeviceInfo();
				devInfo.identifier = deviceInfo.getIdentifier();
				devInfo.name = deviceInfo.getName();
				listener.discoverOneDevice(devInfo);
			}

			@Override
			public void discoverComplete() {
				listener.discoverComplete();
			}
		});
	}

	@Override
	public int connectCSwiper(String address) {
		return initCSwiper(address);
	}

	@Override
	public int initCSwiper(String address) {
		LogUtil.printInfo("openDevice"+address);
		bleCommandController.openDevice(address);
//		bleCommandController.stopSearchDevices();
		return 0;
	}

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String action = MapUtil.getString(map, "buss_type");
				orderid = MapUtil.getString(map, "order_id");
				String realamt = MapUtil.getString(map, "amount");
				int timeout = 30;
				String logNo = CryptoUtils.getInstance().getTransLogNo();
				CryptoUtils.getInstance().setTransLogUpdate(false);
				byte[] random = Util.HexToBin(logNo);
				LogUtil.printInfo("启动刷卡===================");
				bleCommandController.statEmvSwiper(realamt, timeout, action, random);
			}
		}).start();
		
	}

	@Override
	public void disConnect() {
		if (null != bleCommandController)
		{
			Log.e("XGD_DISCONNECT", "EXEC");
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(bleCommandController!=null){
				bleCommandController.closeDevice();
			}
		}
	}

	@Override
	public void releaseCSwiper() {
		if (null != bleCommandController)
		{
			bleCommandController.uninit();
			Log.e("releaseCSwiper", "exec");
		}

		bleCommandController = null;
	}

	@Override
	public String getKsn() {
		return miniksn;
	}

	@Override
	public void getKsnSync() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (bleCommandController.isConnected()) {
			bleCommandController.Get_PsamNo();
			Log.e("Get_PsamNo", "Get_PsamNo");
		} else {
			listener.onBluetoothConnectFail();
		}
	}

	@Override
	public boolean isDevicePresent() {
		boolean b = bleCommandController.isConnected();
		if (b) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			listener.onBluetoothConnectSuccess();

		} else {
			listener.onBluetoothConnectFail();
		}
		return b;
	}

	@Override
	public int getDeviceType() {
		return RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
	}

	@Override
	public String getCardno() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LogUtil.printInfo("getCardno====");
		bleCommandController.getCardNum(30);//30s超时
		return null;
	}

	@Override
	public void writIc(String resp, String icdata) {
		bleCommandController.secondIssuance(resp, icdata.getBytes());
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		index=packageNo;
		bleCommandController.UpdateTerminalParameters(flag, data);
		return false;
	}

	private CommunicationListener communicationListener = new CommunicationListener() {

		@Override
		public void onReceiveCheckCardError(byte code) {
			LogUtil.printInfo("onReceiveCheckCardError=="+code);
			String errorString = "";
			switch (code) {
			case 0x01:
				errorString = "刷卡取消";
				listener.onError(code, errorString);
				break;
			case 0x10:
				errorString = "刷卡超时";
				listener.onTimeout();
//				listener.onError(code, errorString);
				break;
			case 0x11:
				errorString = "读卡失败";
				listener.onError(code, errorString);
				break;
			case 0x21:
				errorString = "加密磁道数据密钥不存在";
				listener.onError(code, errorString);
				break;
			case 0x41:
				errorString = "IC卡请插卡!";
				Map<String,Object> errorMap=new HashMap<String, Object>();
				errorMap.put("demotionTrade", errorString);
				listener.onDemotionTrade(errorMap);
				break;
				case 0x31:
					errorString = "交易拒绝,请正常插卡输入密码后重试!";
					listener.onError(code, errorString);
					break;
			default:
				errorString = "其它错误";
				listener.onError(code, errorString);
				break;
			}
		}

		@Override
		public void onReceiveDeviceConnected() {
			LogUtil.printInfo("新国都蓝牙连接成功");
			// 蓝牙连接成功
			listener.onBluetoothConnectSuccess();
		}

		@Override
		public void onReceiveDeviceDisconnected() {
			Log.e("XGD_DISCONNECT", "EXEC====");
			// 蓝牙连接失败
			listener.onBluetoothConnectFail();
		}


		@Override
		public void onReceiveGetEncTrack(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onReceiveGetMAC(byte[] carmac) {
			try {
			if(xgddevType==1){
				cardMAC = ByteUtils.byteArray2HexString(carmac);
				String termId=miniksn.substring(0, 20);
				String pasamId=miniksn.substring(20);
				Map<String, Object> resultmapp =DataUtil.putPwdDeviceCardServerData(cardType, TextUtils.isEmpty(psampin)?"":psampin, RyxSwiperCode.DEVICE_XGD_K205_BULETOOTH, cardMAC, pasamId,termId, DataUtil.genHexString(maskedPAN), encTracks+randomNumber, TextUtils.isEmpty(cardSeriNo)?"":cardSeriNo, ic55Data);
				listener.onDecodeCompleted(resultmapp);
			}else{
				cardMAC = ByteUtils.byteArray2HexString(carmac);
				// cardMAC=cardMAC.substring(0, 8)+randomNumber;
				Log.e("cardMAC", cardMAC);
				Map<String, Object> map = new HashMap<String, Object>();
				map = DataUtil.putCardServerData(cardType, devType, cardMAC,
						miniksn, track1Length, track2Length, track3Length,
						randomNumber, maskedPAN, encTracks, expiryDate, cardSeriNo,
						carmac);
				Log.e("map", map.toString());
				String icData = ByteUtils.byteArray2HexString(ic55Data);
				if (icData.length() > 10) {
					int pos = 0;
					pos = icData.toUpperCase().indexOf("9F41");
					if (pos > 0) {
						int lenth;
						lenth = Integer
								.parseInt(icData.substring(pos + 5, pos + 6));
						icData = icData.substring(0, pos + 6 + (lenth * 2));
					}
				}
				Log.e("ic_data",icData);
				map.put("ic_data", icData);
				listener.onDecodeCompleted(map);


			}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.printInfo("新国都ERROR==="+e.getMessage());
				listener.onError(0x0089, "数据解析异常,请重新操作!");
			}
		}

		@Override
		public void onReceiveReadCardResult(CardInfo cardinfo) {
			listener.onWaitingCardDataReader();
			if(xgddevType==1){
				//当前是K205设备
				try {
					maskedPAN = cardinfo.getPan();
					//获取21号文内容
//					bleCommandController.getDeviceIdentifyInfo(maskedPAN.substring(maskedPAN.length()-6));

					// 1,磁条卡；2，IC卡；4，非接卡
					if (cardinfo.getCardType()==1){
						cardType = "10";
					} else if(cardinfo.getCardType()==2) {
						cardType = "11";
					}else{
						cardType = "21";
					}
					devType = RyxSwiperCode.DEVICE_XGD_BULETOOTH;
					track1Length = cardinfo.getTrack1Length();
					track2Length = cardinfo.getTrack2Length();
					track3Length = cardinfo.getTrack3Length();
					randomNumber = cardinfo.getRandomNumber();
					String termId=miniksn.substring(0, 20);
					String pasamId=miniksn.substring(20);

					 psampin=cardinfo.getPin();
					Log.e("cardinfo", cardinfo.toString());
					encTracks = cardinfo.getEncTracks();
					LogUtil.printInfo("encTracks==="+encTracks+",track2Length=="+track2Length+",track3Length=="+track3Length);
					expiryDate = cardinfo.getExpiryDate();
					cardSeriNo = ByteUtils
							.byteArray2HexString(cardinfo.getCardSeriNo());
					ic55Data = cardinfo.getIc55Data();
//					String macData = DataUtil.genMacHexString(encTracks, randomNumber,
//							miniksn, orderid);
					//Mac入参 MAB组成：32字节订单号+卡号长度+卡号密文+二三磁道及磁道加密随机数长度+二三磁道密文+磁道加密随机数+PIN密文+PIN加密随机数+PSAM+终端号
					//String hexString = DataUtil.genMacHexString( track2, track3, getmacRandomNo, strKsn, orderid);
					String panHex=DataUtil.genHexString(maskedPAN);
					String hexString=DataUtil.getPinMacHexString(orderid, DataUtil.genHexStr(maskedPAN.length()), panHex,Integer.toHexString(track2Length+track3Length+randomNumber.length()/2) , encTracks, randomNumber, psampin, pasamId, termId);
					LogUtil.printInfo("Mac入参=="+ hexString);
					Log.e("orderid", orderid);
					Log.e("XGDMAC_PARAMS", hexString);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					bleCommandController
							.Get_MAC(ByteUtils.hexString2ByteArray(hexString));

				} catch (Exception e) {
					LogUtil.printInfo("新国都ERROR===1111=="+e.getMessage());
					listener.onError(0x0089, "数据解析异常,请重新操作!");
				}
			}else{
				try {
					// 刷卡后回调
					// String cardType, int dateType, String cardMAC, String ksn, int
					// track1Length,
					// int track2Length, int track3Length, String randomNumber, String
					// maskedPAN,
					// String encTracks, String expiryDate, String cardSeriNo, byte[]
					// ic55Data
					// 1,磁条卡；2，IC卡；4，非接卡
					if (cardinfo.getCardType()==2){
						cardType = "11";
					} else {
						cardType = "10";
					}
					devType = RyxSwiperCode.DEVICE_XGD_BULETOOTH;
					track1Length = cardinfo.getTrack1Length();
					track2Length = cardinfo.getTrack2Length();
					track3Length = cardinfo.getTrack3Length();
					randomNumber = cardinfo.getRandomNumber();
					maskedPAN = cardinfo.getPan();

					Log.e("cardinfo", cardinfo.toString());
					encTracks = cardinfo.getEncTracks();
					expiryDate = cardinfo.getExpiryDate();
					// expiryDate="1249";//暂时写死，待提供
					cardSeriNo = ByteUtils
							.byteArray2HexString(cardinfo.getCardSeriNo());
					ic55Data = cardinfo.getIc55Data();
					String macData = DataUtil.genMacHexString(encTracks, randomNumber,
							miniksn, orderid);
					Log.e("orderid", orderid);
					Log.e("XGDMAC_PARAMS", macData);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					LogUtil.printInfo("cardType=="+cardinfo.getCardType()+",pin="+cardinfo.getPin()+"cardType="+cardType+",maskedPAN="+maskedPAN+",randomNumber="+randomNumber+",encTracks="+encTracks+",expiryDate="+expiryDate+",cardSeriNo="+cardSeriNo+",ic55Data="+ic55Data.toString());
					bleCommandController
							.Get_MAC(ByteUtils.hexString2ByteArray(macData));

				} catch (Exception e) {
					listener.onError(0x0089, "数据解析异常,请重新操作!");
				}
			}


		}

		@Override
		public void onReceiveReturnPSAMNo(byte[] terID, byte[] psamNo) {
			LogUtil.printInfo("xgd获取ksn完毕");
			try {
				miniksn = ByteArrayUtils.toStringHex(ByteUtils
						.byteArray2HexString(terID))
						+ ByteArrayUtils.toStringHex(ByteUtils
								.byteArray2HexString(psamNo));
//				miniksn=ByteUtils
//						.byteArray2HexString(terID)
//						+ByteUtils
//								.byteArray2HexString(psamNo);

				LogUtil.printInfo("xgd获取ksn成功=="+miniksn);
				Log.e("xgdsdk", miniksn);
				listener.onDetected();
			} catch (Exception e) {
				listener.onError(0x0089, "数据解析异常,请重新操作!");
			}
		}

		@Override
		public void onReceiveVersion(String arg0) {

		}

		@Override
		public void onReceiveWriteIcResult(boolean arg0) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// IC卡回写
			if(arg0)
			{
				Log.e("onReceiveWriteIcResult", "true");
				listener.onICResponse(1, null, null);
			}
			else
			{
				Log.e("onReceiveWriteIcResult", "false");
				listener.onICResponse(0, null, null);
			}
		}
;
		@Override
		public void onReceiveUpdateTerminalResult(boolean arg0) {
			LogUtil.printInfo("onReceiveUpdateTerminalResult==result=="+arg0+",index=="+index);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("index", index);
			if(arg0)
			{
				listener.onUpdateTerminalParamsCompleted(map);
			}
			else
			{
				listener.onUpdateTerminalParamsFailed(map);
			}

		}

		@Override
		public void onReceiveReadICCard() {
			listener.EmvOperationWaitiing();

		}

		@Override
		public void onReceiveTwentyOneInfo(String s, String s1, String s2, String s3) {
			LogUtil.printInfo("21号文信息==s=="+s+",s1=="+s1+",s2=="+s2+",s3=="+s3);
		}

		@Override
		public void onReceiveGetType(int i) {
			LogUtil.printInfo("xgddevType=="+i);
			xgddevType=i;
		}

		@Override
		public void onReceiveGetCardNoAndExpiryDate(String cardno, String expirydate) {
			LogUtil.printInfo("onReceiveGetCardNoAndExpiryDate==="+cardno+",expirydate=="+expirydate);
			listener.onGetCardNoCompleted(cardno,expirydate);
		}

	};

	@Override
	public int getTerminalType() {
		// TODO Auto-generated method stub
		return 0;
	}

}
