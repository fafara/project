package com.ryx.swiper.devs.dynamic;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.centerm.mpos.model.CardType;
import com.dynamicode.p27.lib.jifu.CDcSwiperController;
import com.dynamicode.p27.lib.jifu.CSwiperStateChangedListener;
import com.dynamicode.p27.lib.jifu.CTransType;
import com.dynamicode.p27.lib.util.DcConstant;
import com.itron.android.ftf.Util;
import com.landicorp.robert.comm.util.StringUtil;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.DataUtil;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;

public class DynamicAdapter extends CSwiperAdapter {
	private Context context;
	private ISwiperStateListener listener;
	CDcSwiperController mSwiperController;
	private String miniKsn;
	 private  String orderid;
	public DynamicAdapter(Context context, ISwiperStateListener listener) {
		this.context = context;
		this.listener = listener;
		DcConstant.isDebug=false;
//		LogUtil.setLogdebug(true);
		if (mSwiperController == null) {
			mSwiperController = new CDcSwiperController(context,
					mSwiperListener);
		}
	}

	@Override
	public void searchBlueDevs(IRyxDevSearchListener listener) {

	}

	@Override
	public int connectCSwiper(String address) {
		return initCSwiper(address);
	}

	@Override
	public int getTerminalType() {
		return RyxSwiperCode.DEVICE_DYNAMIC_BULETOOTH;
	}

	@Override
	public int initCSwiper(final String address) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mSwiperController.connectBluetoothDevice(30 * 1000, address);
			}
		}) {
		}.start();

		return 0;
	}

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				String action = MapUtil.getString(map, "buss_type");
				orderid = MapUtil.getString(map, "order_id");
				String realamt = MapUtil.getString(map, "amount");
				if ("BankCardBalance".equals(action)) {
					mSwiperController.setAmount(realamt, null, null,
							CTransType.QUERY);
				} else {
					mSwiperController.setAmount(realamt, "人民币", "",
							CTransType.TRADE);
				}

				
				byte[] random = Util.HexToBin(CryptoUtils.getInstance()
						.getTransLogNo());
				for (int i = 0; i < random.length; i++) {
					Log.e("random=", Integer.toHexString(random[i]));

				}
				CryptoUtils.getInstance().setTransLogUpdate(false);
				mSwiperController.startCSwiper(0, random, orderid.getBytes(),
						30 * 1000);
			}
		}) {
		}.start();

	}

	@Override
	public void disConnect() {
		if (null != mSwiperController) {
			mSwiperController.disconnectBT();
		}
	}

	@Override
	public void releaseCSwiper() {

	}

	@Override
	public String getKsn() {
		LogUtil.printInfo("miniKsn====="+miniKsn);
		return miniKsn;
	}

	@Override
	public void getKsnSync() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mSwiperController.getCSwiperKsn();
			}
		}) {
		}.start();

	}

	@Override
	public boolean isDevicePresent() {
		return false;
	}

	@Override
	public int getDeviceType() {
		return RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
	}

	@Override
	public String getCardno() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buss_type", "BankCardBalance");
		map.put("order_id", "0000000000000000");
		map.put("amount", "000000000000");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				mSwiperController.getCSwiperKsn();
			}
		}){}.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startEmvSwiper(map);
		return "";
	}

	@Override
	public void writIc(String resp, String icdata) {
		Log.e("laomao", "writIc");
		mSwiperController.sendOnlineProcessResult(icdata);
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		return false;
	}

	private CSwiperStateChangedListener mSwiperListener = new CSwiperStateChangedListener() {

		@Override
		public void onDetectIcc() {
			listener.EmvOperationWaitiing();
		}

		@Override
		public void onWaitingForCardSwipe() {
			// mHandler.sendEmptyMessage(MSG_WAIT_SWIPE);
			LogUtil.printInfo("onWaitingForCardSwipe");
		}

		@Override
		public void onTimeout() {
			// mHandler.sendEmptyMessage(MSG_TIME_OUT);
			LogUtil.printInfo( "onTimeout");
			listener.onCancelSwiper();
		}

		@Override
		public void onTradeCancel() {
			// mHandler.sendEmptyMessage(MSG_CANCLE_TRADE);
			LogUtil.printInfo("onTradeCancel");
		}

		@Override
		public void onDecodeCompleted(String formatID, String ksn,
				String encTracks, int track1Length, int track2Length,
				int track3Length, String randomNumber, String maskedPAN,
				String expiryDate, String cardHolderName, String cardType,
				String cardMAC, String iccData, boolean isIC, String pingBlock,
				String cardSN,int  posType) {
			LogUtil.printInfo("ksn==="+ksn+",===pingBlock="+pingBlock);
			//posType:27代表P27,84代表P84
			if ("2".equals(cardType)) {// 2，降级交易
				Map<String, Object> errorMap = new HashMap<String, Object>();
				errorMap.put("demotionTrade", "IC卡请插卡!");
				listener.onDemotionTrade(errorMap);
			} else{
				listener.onGetCardNoCompleted(maskedPAN, expiryDate);
				String pasamId=ksn.substring(20);
				 String termId= 	ksn.substring(0, 20);
			if(posType==84){
				//MPOS设备
				 String panHex=DataUtil.genHexString(maskedPAN);
				 String hexString=DataUtil.getPinMacHexString(orderid, DataUtil.genHexStr(maskedPAN.length()), panHex,Integer.toHexString(track2Length+track3Length+randomNumber.length()/2) , encTracks, randomNumber, (!TextUtils.isEmpty(pingBlock)&&!"null".equals(pingBlock))?pingBlock:"", pasamId, termId);
					 LogUtil.printInfo("hexString=="+hexString);
					 LogUtil.printInfo("calcMac11111111==");
				 String macStr=mSwiperController.calcMac(hexString);
//				 String cardMACHexStr= StringUtil.str2HexStr(macStr);
				 LogUtil.printInfo("macStr=="+macStr);
				if("0".equals(cardType)){
					//磁条卡
					Map<String, Object> resultmapp =DataUtil.putPwdDeviceCardServerData("10", (!TextUtils.isEmpty(pingBlock)&&!"null".equals(pingBlock))?pingBlock:"", RyxSwiperCode.DEVICE_DYNAMIC_BULETOOTH, macStr,pasamId, termId, DataUtil.genHexString(maskedPAN), encTracks+randomNumber, "", null);
	              	listener.onDecodeCompleted(resultmapp);
				}else if("1".equals(cardType)||"3".equals(cardType)){
					//IC磁条卡或者NFC挥卡
					 //Mac入参
					
					String mycardType="";
	             	if("1".equals(cardType)){
	             		mycardType="11";
	             	}else if("3".equals(cardType)){
//	             		20：标准PBOC，21：快速QPBOC
	             		mycardType="21";
	             	}
	             	Map<String, Object> resultmapp =DataUtil.putPwdDeviceCardServerData(mycardType, (!TextUtils.isEmpty(pingBlock)&&!"null".equals(pingBlock))?pingBlock:"", RyxSwiperCode.DEVICE_DYNAMIC_BULETOOTH, macStr, pasamId,termId, DataUtil.genHexString(maskedPAN), encTracks+randomNumber, cardSN, StringUtil.hexStr2Bytes(iccData));
	             	 listener.onDecodeCompleted(resultmapp);
				}
			}else if(posType==27){
				//p27设备
			LogUtil.printInfo("动联=cardNo=="+maskedPAN+",expiryDate=="+expiryDate+",iccData=="+iccData+",encTracks="+encTracks);
				
				Map<String, Object> map = new HashMap<String, Object>();
				int offset = 2;
				int len = 8 + encTracks.length() / 2 + randomNumber.length()
						/ 2 + ksn.length() / 2 + maskedPAN.length() + 4
						+ cardMAC.length() / 2;
				byte[] data = new byte[len];

				data[0] = (byte) ((len - 2) / 256);
				data[1] = (byte) ((len - 2) & 0x000000FF);
				// 1磁道长度（1）
				data[offset + 0] = (byte) track1Length;
				// 2磁道长度（1）
				data[offset + 1] = (byte) track2Length;
				// 3磁道长度（1）
				data[offset + 2] = (byte) track3Length;
				// 随机数长度(1)
				data[offset + 3] = (byte) (randomNumber.length() / 2);
				// 终端序号长度(1)
				data[offset + 4] = (byte) (ksn.length() / 2);
				// 卡号长度（1）
				data[offset + 5] = (byte) (maskedPAN.length());

				// 磁道密文

				System.arraycopy(
						CryptoUtils.getInstance().bytesToHex(
								encTracks.getBytes()), 0, data, offset + 6,
						encTracks.length() / 2);
				// 随机数
				System.arraycopy(
						CryptoUtils.getInstance().bytesToHex(
								randomNumber.getBytes()), 0, data, offset + 6
								+ encTracks.length() / 2,
						randomNumber.length() / 2);
				// 终端序号
				System.arraycopy(
						CryptoUtils.getInstance().bytesToHex(ksn.getBytes()),
						0, data, offset + 6 + encTracks.length() / 2
								+ randomNumber.length() / 2, ksn.length() / 2);
				// 卡号
				System.arraycopy(maskedPAN.getBytes(), 0, data, offset + 6
						+ encTracks.length() / 2 + randomNumber.length() / 2
						+ ksn.length() / 2, maskedPAN.length());
				// 卡有效期(4)
				System.arraycopy(expiryDate.getBytes(), 0, data, offset + 6
						+ encTracks.length() / 2 + randomNumber.length() / 2
						+ ksn.length() / 2 + maskedPAN.length(), 4);
				// MAC
				System.arraycopy(
						CryptoUtils.getInstance()
								.bytesToHex(cardMAC.getBytes()),
						0,
						data,
						offset + 6 + encTracks.length() / 2
								+ randomNumber.length() / 2 + ksn.length() / 2
								+ maskedPAN.length() + 4, cardMAC.length() / 2);

				// show id
				LogUtil.printInfo("maskedPAN=" + maskedPAN);

				if (!maskedPAN.contains("*")) {

					map.put("card_no", maskedPAN);
					listener.onGetCardNoCompleted(maskedPAN, expiryDate);
				}

				LogUtil.printInfo(Util.BinToHex(data, 0, data.length));

				String cardinfo = "FF00" + Util.BinToHex(data, 0, data.length);
				map.put("card_info", cardinfo);
				String cardSeriNoStr = "", ic55DataStr = "";

				// if (cardSeriNo != null) {
				// cardSeriNoStr = Util.BinToHex(cardSeriNo, 0,
				// cardSeriNo.length);
				// }
				cardSeriNoStr = cardSN;
				LogUtil.printInfo("IC55域=="+iccData);
				ic55DataStr = iccData;
				// if (ic55Data != null) {
				// ic55DataStr = Util.BinToHex(ic55Data, 0, ic55Data.length);
				// }

				if (null != ic55DataStr && ic55DataStr.length() > 10) {
					int pos = 0;

					pos = ic55DataStr.indexOf("9F41");
					if (pos > 0) {

						int lenth;
						lenth = Integer.parseInt(ic55DataStr.substring(pos + 5,
								pos + 6));

						ic55DataStr = ic55DataStr.substring(0, pos + 6
								+ (lenth * 2));
					}

				}
				map.put("card_type", "1" + cardType);

				while (null != cardSeriNoStr && cardSeriNoStr.length() < 3
						&& cardSeriNoStr.length() > 0)
					cardSeriNoStr = "0" + cardSeriNoStr;

				map.put("ic_data", ic55DataStr);
				map.put("pasamId",pasamId);
				map.put("icsernum", cardSeriNoStr);
				map.put("dev_type", RyxSwiperCode.DEVICE_DYNAMIC_BULETOOTH);
				listener.onDecodeCompleted(map);
			}
		}}

		@Override
		public void onError(int errorCode, String msg) {
			LogUtil.printInfo("calcMac222222222=="+errorCode+","+msg);
			Log.e("laomao1", errorCode + msg);
			if (errorCode == 0) {
				// 不做处理的错误回调，SDK没回调的方法也会走onError，奇葩
			} else if (errorCode == 1010) {
				listener.onICResponse(0, null, null);
				LogUtil.printInfo("onICResponse===error");
			} else {
				listener.onError(errorCode, msg);
			}
		}

		@Override
		public void onBluetoothBounded() {
			// 蓝牙连接成功
			listener.onBluetoothConnectSuccess();
		}

		@Override
		public void onBluetoothBounding() {
			// mHandler.sendEmptyMessage(MSG_CONNECTING_DEVICE);
			LogUtil.printInfo("onBluetoothBounding");
		}

		@Override
		public void onDetectNoBlueTooth() {
			// mHandler.sendEmptyMessage(MSG_CONNECT_DEVICE_FAILED);
			LogUtil.printInfo("onDetectNoBlueTooth");
			listener.onBluetoothConnectFail();
		}

		@Override
		public void onDecodeError() {
			// TODO
			LogUtil.printInfo( "onDecodeError");
		}

		@Override
		public void onDeviceUnplugged() {
			// TODO Auto-generated method stub
			LogUtil.printInfo("onDeviceUnplugged");
		}

		@Override
		public void onDetected() {
			// TODO Auto-generated method stub
			LogUtil.printInfo( "onDetected");
		}

		@Override
		public void onDetecteError() {
			// TODO Auto-generated method stub
			LogUtil.printInfo( "onDetecteError");
			listener.onDetecteError();
		}

		@Override
		public void onDetectStart() {
			// TODO Auto-generated method stub
			LogUtil.printInfo( "onDetectStart");
		}

		@Override
		public void onGetKsnCompleted(String ksn) {
			miniKsn = ksn;
			listener.onDetected();
		}

		@Override
		public void onICResponse(int result, byte[] arg1, byte[] arg2) {
			Log.e("laomao", "ICRESPONSE");
			listener.onICResponse(result, arg1, arg2);
			LogUtil.printInfo("onICResponse===success");
		}

		@Override
		public void onInterrupted() {
			LogUtil.printInfo( "onInterrupted");
		}

		@Override
		public void onPressCancleKey() {
			LogUtil.printInfo("==========onPressCancleKey================");
			listener.onCancelSwiper();
			
		}

		@Override
		public void onNeedInsertICCard() {
			// TODO Auto-generated method stub
			Map<String, Object> errorMap = new HashMap<String, Object>();
			errorMap.put("demotionTrade", "IC卡请插卡!");
			listener.onDemotionTrade(errorMap);
		}

		
	};

}
