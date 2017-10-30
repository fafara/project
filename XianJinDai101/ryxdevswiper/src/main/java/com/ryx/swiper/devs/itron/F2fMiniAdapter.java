package com.ryx.swiper.devs.itron;

import android.content.Context;
import android.util.Log;

import com.itron.android.ftf.Util;
import com.itron.cswiper4.CSwiper;
import com.itron.cswiper4.CSwiperStateChangedListener;
import com.itron.cswiper4.DecodeResult;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.ryx.swiper.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laomao on 15/12/3.
 */
public class F2fMiniAdapter extends CSwiperAdapter {

	private Context context;
	private ISwiperStateListener listener;
	private CSwiper cSwiper;
	private CSwiperListener cswiperListener;
	private String strKsn;
	private int devType;
	private CmdTest commandtest;

	public F2fMiniAdapter(Context context, ISwiperStateListener listener) {
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
		// initCSwiper("");
		getKsnSync();
		return 0;
	}

	@Override
	public void searchBlueDevs(IRyxDevSearchListener listener) {

	}

	@Override
	public int initCSwiper(String address) {
		return 0;
	}

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int ctrlmode = 3;
				String orderid = MapUtil.getString(map, "order_id");
				byte[] random = Util.HexToBin(CryptoUtils.getInstance()
						.getTransLogNo());
				for (int i = 0; i < random.length; i++)
					LogUtil.printInfo("random="
							+ Integer.toHexString(random[i]));

				CryptoUtils.getInstance().setTransLogUpdate(false);
				LogUtil.printInfo(CryptoUtils.getInstance().getTransLogNo());
				LogUtil.printInfo("mini orderid==" + orderid);
				cSwiper.startCSwiper(ctrlmode, random, orderid.getBytes(), 50);
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
		Map<String, Object> map=new HashMap<String, Object>();
    	map.put("buss_type", "");
    	map.put("order_id", "0000000000000000");
    	map.put("amount", "0");
    	startEmvSwiper(map);
		return "";
	}

	@Override
	public void writIc(String resp, String icdata) {

	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		return false;
	}

	class GetKsnThread extends Thread {
		@Override
		public void run() {
			String ksn = cSwiper.getKSN();
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
					cswiperListener.onTimeout();
				}
			} else if ("".equals(ksn)) {
				cswiperListener.onTimeout();
			} else {
				LogUtil.printInfo("f2f ksn == null");
			}
		}
	}

	class CSwiperListener implements CSwiperStateChangedListener {
		@Override
		public void onDevicePlugged() {
			listener.onDevicePlugged();
		}

		@Override
		public void onDeviceUnplugged() {
			listener.onDeviceUnplugged();
		}

		@Override
		public void onWaitingForDevice() {
			listener.onWaitingForDevice();
		}

		@Override
		public void onNoDeviceDetected() {
			listener.onNoDeviceDetected();
		}

		@Override
		public void onWaitingForCardSwipe() {
			listener.onWaitingForCardSwipe();
		}

		@Override
		public void onCardSwipeDetected() {
			listener.onCardSwipeDetected();
		}

		@Override
		public void onDecodingStart() {
			listener.onDecodingStart();
		}

		@Override
		public void onError(int i, String s) {
			listener.onError(i, s);
		}

		@Override
		public void onInterrupted() {
			listener.onInterrupted();
		}

		@Override
		public void onTimeout() {
			listener.onTimeout();
		}



		@Override
		public void onDecodeCompleted(String formatID, String ksn,
				String encTracks, int track1Length, int track2Length,
				int track3Length, String randomNumber, String maskedPAN,
				String pan, String expiryDate, String cardHolderName,
				String cardMAC, int cardType, byte[] cardSeriNo,byte[] cvm, byte[] ic55Data) {
			Map<String, Object> map = new HashMap<String, Object>();

			int offset = 2;
			int len = 8 + encTracks.length() / 2 + randomNumber.length() / 2
					+ ksn.length() / 2 + maskedPAN.length() + 4
					+ cardMAC.length() / 2;
			Log.e("liweimini", len + "");
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
					CryptoUtils.getInstance().bytesToHex(encTracks.getBytes()),
					0, data, offset + 6, encTracks.length() / 2);
			// 随机数
			System.arraycopy(
					CryptoUtils.getInstance().bytesToHex(
							randomNumber.getBytes()), 0, data, offset + 6
							+ encTracks.length() / 2, randomNumber.length() / 2);
			// 终端序号
			System.arraycopy(
					CryptoUtils.getInstance().bytesToHex(ksn.getBytes()), 0,
					data,
					offset + 6 + encTracks.length() / 2 + randomNumber.length()
							/ 2, ksn.length() / 2);
			// 卡号
			System.arraycopy(maskedPAN.getBytes(), 0, data,
					offset + 6 + encTracks.length() / 2 + randomNumber.length()
							/ 2 + ksn.length() / 2, maskedPAN.length());
			// 卡有效期(4)
			System.arraycopy(expiryDate.getBytes(), 0, data,
					offset + 6 + encTracks.length() / 2 + randomNumber.length()
							/ 2 + ksn.length() / 2 + maskedPAN.length(), 4);
			// MAC
			System.arraycopy(
					CryptoUtils.getInstance().bytesToHex(cardMAC.getBytes()),
					0, data,
					offset + 6 + encTracks.length() / 2 + randomNumber.length()
							/ 2 + ksn.length() / 2 + maskedPAN.length() + 4,
					cardMAC.length() / 2);

			// show id
			LogUtil.printInfo("maskedPAN=" + maskedPAN);

			if (!maskedPAN.contains("*")) {

				map.put("card_no", maskedPAN);
				listener.onGetCardNoCompleted(maskedPAN,expiryDate);
			}

			LogUtil.printInfo(Util.BinToHex(data, 0, data.length));

			String cardinfo = "FF00" + Util.BinToHex(data, 0, data.length);
			Log.e("laomao", "cardinfo长度：" + cardinfo.length());
			map.put("card_info", cardinfo);
			String cardSeriNoStr = "", ic55DataStr = "";

			if (cardSeriNo != null) {
				cardSeriNoStr = Util.BinToHex(cardSeriNo, 0, cardSeriNo.length);
			}

			if (ic55Data != null) {
				ic55DataStr = Util.BinToHex(ic55Data, 0, ic55Data.length);
			}

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

			map.put("card_type", "00");

			while (cardSeriNoStr.length() < 3 && cardSeriNoStr.length() > 0)
				cardSeriNoStr = "0" + cardSeriNoStr;

			map.put("ic_data", ic55DataStr);

			map.put("icsernum", cardSeriNoStr);
			map.put("pasamId",ksn.substring(20));
			map.put("dev_type", RyxSwiperCode.DEVICE_AC_AUDIO_MINI);
			listener.onDecodeCompleted(map);
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
			listener.onDecodeError(decodeResult.toString());
		}
	}

	@Override
	public int getTerminalType() {
		return 0;
	}
}
