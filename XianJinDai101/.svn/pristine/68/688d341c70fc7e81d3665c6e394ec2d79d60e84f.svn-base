package com.ryx.swiper.devs.itron;

import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.itron.android.bluetooth.DeviceInfo;
import com.itron.android.bluetooth.DeviceSearchListener;
import com.itron.android.ftf.Util;
import com.itron.protol.android.CommandReturn;
import com.itron.protol.android.CommunicationListener;
import com.nexgo.common.ByteUtils;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.ryx.swiper.utils.MoneyEncoder;

/**
 * Created by laomao on 15/12/3.
 */
public class BuleToothAdapter extends CSwiperAdapter {
	public CSwiperAdapter adapter;
	private Context context;
	private ISwiperStateListener listener;
	private int deviceType = 0;
	private String cardIdStr = "";
	private ITCommunicationCallBack itComListener;
	private BLEF2FCmdTest commandtest;
	private String cardnum = "";
	private String cardno = "";
	String miniksn = "";
	private BluetoothAdapter bluetoothAdapter;

	public BuleToothAdapter(Context context, ISwiperStateListener listener) {
		this.context = context;
		this.listener = listener;
		itComListener = new ITCommunicationCallBack();

		commandtest = new BLEF2FCmdTest(context, itComListener);

	}

	@Override
	public void searchBlueDevs(final IRyxDevSearchListener listener) {
		commandtest.searchDevices(new DeviceSearchListener() {
			@Override
			public void discoverOneDevice(DeviceInfo deviceInfo) {
				com.ryx.swiper.beans.DeviceInfo devInfo = new com.ryx.swiper.beans.DeviceInfo();
				devInfo.identifier = deviceInfo.identifier;
				devInfo.name = deviceInfo.name;
				listener.discoverOneDevice(devInfo);
			}

			@Override
			public void discoverComplete() {
				listener.discoverComplete();
			}

			@Override
			public void disConnected() {
				listener.disConnected();
			}
		});
	}

	@Override
	public int connectCSwiper(String address) {

		return initCSwiper(address);
	}

	@Override
	public int initCSwiper(String address) {
		int i = commandtest.openDevice(address);
		if (i == 0) {
			listener.onBluetoothConnectSuccess();
		} else {
			listener.onBluetoothConnectFail();
		}
		return i;
	}

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {
		new Thread() {
			@Override
			public void run() {
				String action = MapUtil.getString(map, "buss_type");
				String orderid = MapUtil.getString(map, "order_id");
				String realamt = MapUtil.getString(map, "amount");
				byte[] random = Util.HexToBin(CryptoUtils.getInstance()
						.getTransLogNo());

				for (int i = 0; i < random.length; i++)
					LogUtil.printInfo(Integer.toHexString(random[i]));

				CryptoUtils.getInstance().setTransLogUpdate(false);

				LogUtil.printInfo("money:="
						+ MoneyEncoder.encodeForSwiper(realamt) + " orderid="
						+ orderid);
				CommandReturn cmresult = commandtest.statEmvSwiperI21(action,
						random, orderid.getBytes(),
						MoneyEncoder.encodeForSwiper(realamt));

				if (null == cmresult) {
					listener.onTimeout();
				} else {

					Map<String, Object> map = new HashMap<String, Object>();
					LogUtil.printInfo("ic刷卡成功");

					LogUtil.printInfo("printInfo");

					String ksn, encTracks;
					int track1Length, track2Length, track3Length;
					String randomNumber, maskedPAN, pan, expiryDate, cardHolderName, cardMAC;
					int cardType;
					byte[] cardSeriNo, ic55Data;

					// ksn ="F00192782307000199829278230700019982";
					ksn = Util.BinToHex(cmresult.Return_PSAMNo, 0,
							cmresult.Return_PSAMNo.length);
					encTracks = Util.BinToHex(cmresult.Return_PSAMTrack, 0,
							cmresult.Return_PSAMTrack.length);

					track1Length = cmresult.trackLengths[0];
					track2Length = cmresult.trackLengths[1];
					track3Length = cmresult.trackLengths[2];

					randomNumber = Util.BinToHex(cmresult.Return_PSAMRandom, 0,
							cmresult.Return_PSAMRandom.length);
					maskedPAN = new String(cmresult.Return_CardNo, 0,
							cmresult.Return_CardNo.length);
					
					// expiryDate = Util.BinToHex(cmresult.cardexpiryDate,
					// 0,
					// cmresult.cardexpiryDate.length);

					expiryDate = new String(cmresult.cardexpiryDate, 0,
							cmresult.cardexpiryDate.length);
					listener.onGetCardNoCompleted(maskedPAN,expiryDate);
					cardMAC = Util.BinToHex(cmresult.Return_PSAMMAC, 0,
							cmresult.Return_PSAMMAC.length);

					cardType = cmresult.CardType;
					cardSeriNo = cmresult.CardSerial;
					ic55Data = cmresult.emvDataInfo;

					int offset = 2;
					int len = 8 + encTracks.length() / 2
							+ randomNumber.length() / 2 + ksn.length() / 2
							+ maskedPAN.length() + 4 + cardMAC.length() / 2;
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
									randomNumber.getBytes()), 0, data, offset
									+ 6 + encTracks.length() / 2,
							randomNumber.length() / 2);
					// 终端序号
					System.arraycopy(
							CryptoUtils.getInstance()
									.bytesToHex(ksn.getBytes()),
							0,
							data,
							offset + 6 + encTracks.length() / 2
									+ randomNumber.length() / 2,
							ksn.length() / 2);
					// 卡号
					System.arraycopy(maskedPAN.getBytes(), 0, data, offset + 6
							+ encTracks.length() / 2 + randomNumber.length()
							/ 2 + ksn.length() / 2, maskedPAN.length());
					// 卡有效期(4)
					System.arraycopy(expiryDate.getBytes(), 0, data, offset + 6
							+ encTracks.length() / 2 + randomNumber.length()
							/ 2 + ksn.length() / 2 + maskedPAN.length(), 4);
					// MAC
					System.arraycopy(
							CryptoUtils.getInstance().bytesToHex(
									cardMAC.getBytes()),
							0,
							data,
							offset + 6 + encTracks.length() / 2
									+ randomNumber.length() / 2 + ksn.length()
									/ 2 + maskedPAN.length() + 4,
							cardMAC.length() / 2);

					LogUtil.printInfo(Util.BinToHex(data, 0, data.length));

					cardnum = "FF00" + Util.BinToHex(data, 0, data.length);
					map.put("card_info", cardnum);
					String cardSeriNoStr = "", ic55DataStr = "";

					if (cardSeriNo != null) {
						cardSeriNoStr = Util.BinToHex(cardSeriNo, 0,
								cardSeriNo.length);
					}

					if (ic55Data != null) {
						ic55DataStr = Util.BinToHex(ic55Data, 0,
								ic55Data.length);
					}
					Log.e("laomaoic55", ic55DataStr);
					if (ic55DataStr.length() > 10) {
						int pos = 0;

						pos = ic55DataStr.indexOf("9F41");
						if (pos > 0) {

							// Log.v("jics", ic55DataStr);
							// Log.v("jics", ic55DataStr.substring(0, pos));
							// Log.v("jics", ic55DataStr.substring(0, pos +
							// 4));
							int lenth;
							lenth = Integer.parseInt(ic55DataStr.substring(
									pos + 5, pos + 6));

							ic55DataStr = ic55DataStr.substring(0, pos + 6
									+ (lenth * 2));
						}
						// Log.v("jics", ic55DataStr);
					}
					Log.e("laomaoic55+", ic55DataStr);
					map.put("card_type", "1" + cmresult.CardType);

					while (cardSeriNoStr.length() < 3
							&& cardSeriNoStr.length() > 0)
						cardSeriNoStr = "0" + cardSeriNoStr;
					// cardSeriNoStr =
					// cardSeriNoStr.substring(cardSeriNoStr.length() - 3,
					// cardSeriNoStr.length());

					map.put("ic_data", ic55DataStr);

					map.put("icsernum", cardSeriNoStr);
					map.put("dev_type", RyxSwiperCode.DEVICE_AC_SMALL_BULETOOTH);
					map.put("card_no", maskedPAN);
					map.put("pasamId",ksn.substring(20));
					
					listener.onDecodeCompleted(map);

				}

			}
		}.start();

	}

	@Override
	public void disConnect() {
		if (commandtest != null)
			commandtest.stopCSwiper();
	}

	@Override
	public void releaseCSwiper() {
		if (commandtest != null)
			commandtest.release();
	}

	@Override
	public String getKsn() {
		return miniksn;
	}

	@Override
	public void getKsnSync() {
		new GetKsnThread().start();
	}

	@Override
	public boolean isDevicePresent() {
		if (commandtest.isConnect()) {
			listener.onBluetoothConnectSuccess();
		} else {
			listener.onBluetoothConnectFail();
		}
		return false;
	}

	@Override
	public int getDeviceType() {
		return 0;
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
		new Thread(new icWriteBackThread(resp, icdata)).start();
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		boolean updateresult;
		if(flag==0){
			updateresult=commandtest.updateTerminalParameters(0, packageNo+1, ByteUtils.hexString2ByteArray("31"+data));
		}else{
			updateresult=commandtest.updateTerminalParameters(1, packageNo+1, ByteUtils.hexString2ByteArray("31"+data));
		}
		LogUtil.printInfo("BuleToothAdapter+++flag=="+flag+",packageNo=="+packageNo+",data=="+data +",updateresult"+updateresult);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("index", packageNo);
			if(updateresult)
			{
				listener.onUpdateTerminalParamsCompleted(map);
			}
			else
			{
				listener.onUpdateTerminalParamsFailed(map);
			}
		return updateresult;
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

			CommandReturn cmdret = commandtest.secondIssuance(resp, icdata);
			if (cmdret != null) {

				LogUtil.printInfo("blue write back ok ");
				listener.onICResponse(cmdret.resultIC, cmdret.resuiltScriptIC,
						cmdret.resuiltDataIC);
			} else {
				LogUtil.printInfo("cmdret == null");
				listener.onError(1, "ic回写失败");
			}

		}

	}

	class GetKsnThread extends Thread {
		@Override
		public void run() {

			CommandReturn cmdret = commandtest.getKsnI21();

			if (cmdret != null) {

				miniksn = Util.BinToHex(cmdret.ksn, 0, cmdret.ksn.length);

				listener.onDetected();

			} else {
				listener.onTimeout();
			}
		}
	}

	class ITCommunicationCallBack implements CommunicationListener {

		@Override
		public void onTimeout() {
			// TODO Auto-generated method stub
			LogUtil.printInfo("onTimeoutsys");
			listener.onTimeout();
		}

		@Override
		public void onError(int code, String msg) {
			// TODO Auto-generated method stub
			LogUtil.printInfo("onError code:" + code + "msg:" + msg);
			listener.onError(code, msg);
		}

		@Override
		public void onWaitingcard() {
			// TODO Auto-generated method stub
			LogUtil.printInfo("onWaitingcard");
			listener.onWaitingForCardSwipe();
		}

		@Override
		public void onWaitingPin() {
			// TODO Auto-generated method stub
			LogUtil.printInfo("onWaitingPin");
			// listener..sendMessage(updateUI.obtainMessage(1, "请输入密码"));
		}

		@Override
		public void onWaitingOper() {
			// TODO Auto-generated method stub
			LogUtil.printInfo("onWaitingOper");
			listener.onWaitingForCardSwipe();
		}

		@Override
		public void onNfcSwipingcard() {

		}

		@Override
		public void onShowMessage(String msg) {
			// TODO Auto-generated method stub
			LogUtil.printInfo("onShowMessage:" + msg);
			// listener..sendMessage(updateUI.obtainMessage(1, msg));
		}

		@Override
		public void onICWaitingOper() {
			LogUtil.printInfo("onWaiting IC BLUE Oper");
			listener.EmvOperationWaitiing();

		}

		@Override
		public void onQueryIcRecord(int arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRemoveCard(int i, String s) {

		}

		@Override
		public void onUploadOfflineTradeRecord(int arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

	}

	@Override
	public int getTerminalType() {
		return commandtest.getTerminalType();
	}
}
