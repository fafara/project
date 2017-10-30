package com.ryx.swiper.devs.itron;

import android.content.Context;
import android.util.Log;

import com.itron.android.ftf.Util;
import com.itron.cswiper4.CSwiper;
import com.itron.cswiper4.CSwiperStateChangedListener;
import com.itron.cswiper4.DecodeResult;
import com.itron.protol.android.TransactionDateTime;
import com.itron.protol.android.TransactionInfo;
import com.itron.protol.android.TransationCurrencyCode;
import com.itron.protol.android.TransationTime;
import com.itron.protol.android.TransationType;
import com.nexgo.common.ByteUtils;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.ryx.swiper.utils.MoneyEncoder;
import com.ryx.swiper.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laomao on 15/12/9.
 */
public class F2fMiniIcAdapter extends CSwiperAdapter {

	private Context context;
	private ISwiperStateListener listener;
	private CSwiper cSwiper;
	private CSwiperListener cswiperListener;
	private String strKsn;
	private String strCardNo;
	private int devType;
	private CmdTest commandtest;

	public F2fMiniIcAdapter(Context context, ISwiperStateListener listener) {
		this.context = context;
		this.listener = listener;
		cswiperListener = new CSwiperListener();
		cSwiper = CSwiper.GetInstance(context, cswiperListener);

	}

	@Override
	public int connectCSwiper(String address) {
		cSwiper.deleteCSwiper();

		if (commandtest != null) {
			LogUtil.printInfo("正在初始化F2F, 请稍等");
			commandtest.cmdctrl.ReleaseDevice();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
		initCSwiper("");
		// getKsnSync();
		return 0;
	}

	@Override
	public void searchBlueDevs(IRyxDevSearchListener listener) {

	}

	@Override
	public int initCSwiper(String address) {
		if (cSwiper.isDevicePresent()) {
			listener.onAudioDevOpenSuccess();
		} else {
			listener.onAudioDevOpenFail();
		}
		return 0;
	}

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {
		new Thread() {
			@Override
			public void run() {
				String action = MapUtil.getString(map, "buss_type");
				String orderid = MapUtil.getString(map, "order_id");
				String realamt = MapUtil.getString(map, "amount");
				TransactionInfo info = new TransactionInfo();
				// 设置交易日期 格式: YYMMDD
				TransactionDateTime dateTime = new TransactionDateTime();
				// dateTime.setDateTime("140930");
			String 	transDate=CryptoUtils.getInstance().getTransDate();
				dateTime.setDateTime(transDate);

				LogUtil.printInfo("jics=="+transDate);

				// 设置交易时间 hhssmm
				TransationTime time = new TransationTime();
				// time.setTime("105130");
				time.setTime(CryptoUtils.getInstance().getTransTime());

				// 设置货币代码
				TransationCurrencyCode currencyCode = new TransationCurrencyCode();

				currencyCode.setCode("0156");
				// 设置交易流水号
				// TransationNum num = new TransationNum();
				//
				String translogno = CryptoUtils.getInstance().getTransLogNo();
				Log.e("translogno", translogno);
				byte[] random = Util.HexToBin(translogno);
				Log.e("random", random.toString());
				for (int i = 0; i < random.length; i++) {
					Log.e("random=", Integer.toHexString(random[i]));

				}
				// num.setNum(translogno);

				CryptoUtils.getInstance().setTransLogUpdate(false);

				// num.setNum("00000001");

				// 设置交易类型 00 消费
				TransationType type = new TransationType();

				if ("BankCardBalance".equals(action)) {
					type.setType("31");
					LogUtil.printInfo("cardcation type balance 31");

				} else {
					type.setType("00");
					LogUtil.printInfo("cardcation type business 00");
				}

				info.setDateTime(dateTime);
				info.setCurrencyCode(currencyCode);
				// info.setNum(num);
				info.setTime(time);
				info.setType(type);
				// bit0＝0/1 表示随机有爱刷产生/由双方产生。
				// Bit1＝0/1 表示终端需要不上送/上送mac。
				// Bit2＝0/1 表示终端上送的卡号不屏蔽/屏蔽
				// Bit3＝0/1 不上送/上送各磁道的原始长度

				// Bit4=0/1磁道数据加密/不加密
				// Bit5=0/1上送/不上送PAN码
				// Bit6=0/1 55域不加密/加密
				// Bit7 =0/1 不上送/上送各磁道密文的长度

				String byte0 = "10000011";

				int flag0 = Util.binaryStr2Byte(byte0);
				cSwiper.statEmvSwiper((byte) 0, new byte[] { (byte) flag0,
						0x01, 0x00, 0x00 }, random,
						MoneyEncoder.encodeForSwiper(realamt),
						orderid.getBytes(), 50, info);
			}
		}.start();

	}

	@Override
	public void disConnect() {
		if (cSwiper != null)
			cSwiper.stopCSwiper();
	}

	@Override
	public void releaseCSwiper() {
		if (cSwiper != null)
			cSwiper.deleteCSwiper();
	}

	@Override
	public String getKsn() {
		return strKsn;
	}

	@Override
	public void getKsnSync() {
		new GetKsnThread().start();
	}

	@Override
	public boolean isDevicePresent() {
		if (cSwiper.isDevicePresent()) {
			listener.onAudioDevOpenSuccess();
		} else {
			listener.onAudioDevOpenFail();
		}
		return true;
	}

	@Override
	public int getDeviceType() {
		return RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
	}

	@Override
	public String getCardno() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buss_type", "");
		map.put("order_id", "0000000000000000");
		map.put("amount", "0");
		startEmvSwiper(map);
		return strCardNo;
	}

	@Override
	public void writIc(String resp, String icdata) {
		new Thread(new icWriteBackThread(resp, icdata)).start();
	}

	private class icWriteBackThread implements Runnable {

		private String resp;
		private String icdata;

		public icWriteBackThread(String _resp, String _icdata) {
			super();
			this.resp = _resp;
			this.icdata = _icdata;
		}

		@Override
		public void run() {
			cSwiper.secondIssuance(resp, Util.hexStringToByteArray(icdata));
		}

	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		boolean updateresult;
		if (flag == 0) {
			updateresult = cSwiper.updateTerminalParameters(0, packageNo + 1,
					ByteUtils.hexString2ByteArray("31" + data));
		} else {
			updateresult = cSwiper.updateTerminalParameters(1, packageNo + 1,
					ByteUtils.hexString2ByteArray("31" + data));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("index", packageNo);
		if (updateresult) {
			listener.onUpdateTerminalParamsCompleted(map);
		} else {
			listener.onUpdateTerminalParamsFailed(map);
		}
		return updateresult;
	}

	class GetKsnThread extends Thread {
		@Override
		public void run() {
			String ksn = cSwiper.getKSN();
			LogUtil.printInfo("GetKsnThread==ksn=="+ksn);
			if (!StringUtils.isBlank(ksn)) {
				if (ksn.startsWith("D201") || ksn.startsWith("F001")
						|| ksn.startsWith("AC00") || ksn.startsWith("8001")) {

//					strKsn = ksn.substring((ksn.length() - 16), ksn.length());
					strKsn = ksn;//设备绑定需要完整的ksn信息

					if (ksn.contains("3150007") || ksn.contains("92782307")
							|| ksn.toUpperCase().contains("AC")
							|| ksn.toUpperCase().contains("88700002")) {
						devType = RyxSwiperCode.DEVICE_AC_AUDIO_IC;
					} else {
						devType = RyxSwiperCode.DEVICE_AC_AUDIO_MINI;
					}

					listener.onDetected();

					LogUtil.printInfo("device detected f2f");

				} else {
//					cswiperListener.onTimeout();
					listener.onDetecteError();
				}
			} else if ("".equals(ksn)) {
				listener.onDetecteError();
//				cswiperListener.onTimeout();
			} else {
				listener.onDetecteError();
				LogUtil.printInfo("f2f ksn == null");
			}
		}
	}

	class CSwiperListener implements CSwiperStateChangedListener {
		@Override
		public void onDevicePlugged() {

		}

		@Override
		public void onDeviceUnplugged() {

		}

		@Override
		public void onWaitingForDevice() {

		}

		@Override
		public void onNoDeviceDetected() {

		}

		@Override
		public void onWaitingForCardSwipe() {

		}

		@Override
		public void onCardSwipeDetected() {

		}

		@Override
		public void onDecodingStart() {

		}

		@Override
		public void onError(int i, String s) {

		}

		@Override
		public void onInterrupted() {

		}

		@Override
		public void onTimeout() {
				
		}

		@Override
		public void onDecodeCompleted(String formatID, String ksn,
				String encTracks, int track1Length, int track2Length,
				int track3Length, String randomNumber, String maskedPAN,
				String pan, String expiryDate, String cardHolderName,
				String cardMAC, int cardType, byte[] cardSeriNo, byte[] cvm, byte[] ic55Data) {
			if (2 == cardType) {//2，降级交易
				 Map<String,Object> errorMap=new HashMap<String, Object>();
					errorMap.put("demotionTrade", "IC卡请插卡!");
					listener.onDemotionTrade(errorMap);
			} else {
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

				if (cardSeriNo != null) {
					cardSeriNoStr = Util.BinToHex(cardSeriNo, 0,
							cardSeriNo.length);
				}

				if (ic55Data != null) {
					ic55DataStr = Util.BinToHex(ic55Data, 0, ic55Data.length);
				}

				Log.e("laomaoic55", ic55DataStr);
				if (ic55DataStr.length() > 10) {
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
				Log.e("laomaoic55", ic55DataStr);
				map.put("card_type", "1" + cardType);

				while (cardSeriNoStr.length() < 3 && cardSeriNoStr.length() > 0)
					cardSeriNoStr = "0" + cardSeriNoStr;

				map.put("ic_data", ic55DataStr);

				map.put("icsernum", cardSeriNoStr);
				map.put("pasamId", ksn.substring(20));
				map.put("dev_type", RyxSwiperCode.DEVICE_AC_AUDIO_IC);
				listener.onDecodeCompleted(map);
			}
		}

		@Override
		public void onICResponse(int i, byte[] bytes, byte[] bytes1) {
			listener.onICResponse(i, bytes, bytes1);
		}

		@Override
		public void EmvOperationWaitiing() {
			listener.EmvOperationWaitiing();
		}

		@Override
		public void onDecodeError(DecodeResult decodeResult) {

		}
	}

	@Override
	public int getTerminalType() {
		return 0;
	}
}
