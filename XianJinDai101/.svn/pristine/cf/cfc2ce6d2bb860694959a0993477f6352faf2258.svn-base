package com.ryx.swiper.devs.itron;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.itron.android.bluetooth.DeviceInfo;
import com.itron.android.bluetooth.DeviceSearchListener;
import com.itron.android.ftf.Util;
import com.itron.android.lib.Logger;
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

public class BlueToothCommandAdapter extends CSwiperAdapter {
	public CSwiperAdapter adapter;
	private Context context;
	private ISwiperStateListener listener;
	private int deviceType = 0;
	private String cardIdStr = "";
	private ITCommunicationCallBack itComListener;
	private BLEF2FCmdTest commandtest;
	private String cardnum = "";
	private String cardno = "";
	/**
	 *  6小蓝牙<br>
	 *  8非接<br>0x0A
	 *  其他大蓝牙
	 */
	private int ac_type = -1;
	private int mpos_newtype=0x0A;
	String miniksn = "";
	//是否支持21号文信息
	private boolean issupport21Msg=false;
//	CommandReturn terminalTypecomReturn;
	public BlueToothCommandAdapter(Context context,
			ISwiperStateListener listener) {
		this.context = context;
		this.listener = listener;
		itComListener = new ITCommunicationCallBack();
		commandtest = new BLEF2FCmdTest(context, itComListener);
//		Logger.getInstance(BlueToothCommandAdapter.class).setDebug(true);
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
	public int getTerminalType() {
		return 0;
	}

	@Override
	public int initCSwiper(String address) {
		int i = commandtest.openDevice(address);
		if (i == 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CommandReturn	returnData=commandtest.getTerminalTypeNew();
//			ac_type = commandtest.getTerminalType();
			ac_type=returnData.deviceType;
			issupport21Msg=returnData.Return_UNIONPAY21;


//			StringBuffer stringBuffer=new StringBuffer();
//			if(returnData!=null){
//				if(returnData.Return_Result==0x00){
//					stringBuffer.append("应答成功:"+returnData.Return_Result+"\n");
//				}else{
//					stringBuffer.append("应答失败:"+returnData.Return_Result+"\n");
//				}
//				stringBuffer.append("设备类型："+returnData.deviceType+"\n");
//				if(returnData.terminalNum!=null){
//					stringBuffer.append("终端号:"+Util.BinToHex(returnData.terminalNum,0,returnData.terminalNum.length));
//				}
//				if(returnData.versionNum!=null){
//					stringBuffer.append("版本号:"+new String (returnData.versionNum)+"\n");
//				}
//				if(returnData.btName!=null){
//					stringBuffer.append("蓝牙名称:"+new String(returnData.btName)+"\n");
//				}
//				if(returnData.btVersion!=null){
//					stringBuffer.append("蓝牙名版本:"+new String(returnData.btVersion)+"\n");
//				}
//				if(returnData.Return_UNIONPAY21){
//					if(returnData.terminalSerialNo!=null){
//						stringBuffer.append("终端硬件序列号:"+new String(returnData.terminalSerialNo));
//					}
//					if(returnData.terminalSerialNoRandom!=null){
//						stringBuffer.append("随机因子:"+new String(returnData.terminalSerialNoRandom));
//					}
//					if(returnData.terminalSerialNoEnc!=null){
//						stringBuffer.append("硬件序列号密文:"+new String(returnData.terminalSerialNoEnc));
//					}
//					LogUtil.printInfo("当前新设备");
//				}else{
//					LogUtil.printInfo("当前旧设备");
//				}
//				LogUtil.printInfo(stringBuffer.toString());
//			}
//			LogUtil.printInfo("-----------------------------------------------------------");
			LogUtil.printInfo("MPOS类型="+ac_type+",mpos_newtype="+mpos_newtype);
			listener.onBluetoothConnectSuccess();
		} else {
			listener.onBluetoothConnectFail();
		}
		return i;
	}

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {
		if (ac_type == 6)// 小蓝牙
		{
			Log.e("======", "小蓝牙");
			new Thread() {
				@Override
				public void run() {
					Log.e("laomaotest", "小蓝牙启动刷卡");
					String action = MapUtil.getString(map, "buss_type");
					String orderid = MapUtil.getString(map, "order_id");
					String realamt = MapUtil.getString(map, "amount");
					byte[] random = Util.HexToBin(CryptoUtils.getInstance()
							.getTransLogNo());

					for (int i = 0; i < random.length; i++) {
						Log.e("random=", Integer.toHexString(random[i]));

					}
					CryptoUtils.getInstance().setTransLogUpdate(false);

					LogUtil.printInfo("money:="
							+ MoneyEncoder.encodeForSwiper(realamt)
							+ " orderid=" + orderid);
					CommandReturn cmresult = commandtest.statEmvSwiperI21(
							action, random, orderid.getBytes(),
							MoneyEncoder.encodeForSwiper(realamt));
					if (null == cmresult) {
						listener.onTimeout();
					} else {
						if (cmresult.CardType == 2)// 2,降级交易
						{
							Map<String,Object> errorMap=new HashMap<String, Object>();
							errorMap.put("demotionTrade", "IC卡请插卡!");
							listener.onDemotionTrade(errorMap);
						} else {
							Map<String, Object> map = new HashMap<String, Object>();
							String ksn, encTracks;
							int track1Length, track2Length, track3Length;
							String randomNumber, maskedPAN, pan, expiryDate, cardHolderName, cardMAC;
							int cardType;
							byte[] cardSeriNo, ic55Data;

							// ksn ="F00192782307000199829278230700019982";
							ksn = Util.BinToHex(cmresult.Return_PSAMNo, 0,
									cmresult.Return_PSAMNo.length);
							encTracks = Util.BinToHex(
									cmresult.Return_PSAMTrack, 0,
									cmresult.Return_PSAMTrack.length);

							track1Length = cmresult.trackLengths[0];
							track2Length = cmresult.trackLengths[1];
							track3Length = cmresult.trackLengths[2];

							randomNumber = Util.BinToHex(
									cmresult.Return_PSAMRandom, 0,
									cmresult.Return_PSAMRandom.length);
							maskedPAN = new String(cmresult.Return_CardNo, 0,
									cmresult.Return_CardNo.length);
							// expiryDate =
							// Util.BinToHex(cmresult.cardexpiryDate,
							// 0,
							// cmresult.cardexpiryDate.length);

							expiryDate = new String(cmresult.cardexpiryDate, 0,
									cmresult.cardexpiryDate.length);
							listener.onGetCardNoCompleted(maskedPAN, expiryDate);

							cardMAC = Util.BinToHex(cmresult.Return_PSAMMAC, 0,
									cmresult.Return_PSAMMAC.length);

							cardType = cmresult.CardType;
							cardSeriNo = cmresult.CardSerial;
							ic55Data = cmresult.emvDataInfo;

							int offset = 2;
							int len = 8 + encTracks.length() / 2
									+ randomNumber.length() / 2 + ksn.length()
									/ 2 + maskedPAN.length() + 4
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

							System.arraycopy(CryptoUtils.getInstance()
									.bytesToHex(encTracks.getBytes()), 0, data,
									offset + 6, encTracks.length() / 2);
							// 随机数
							System.arraycopy(CryptoUtils.getInstance()
									.bytesToHex(randomNumber.getBytes()), 0,
									data, offset + 6 + encTracks.length() / 2,
									randomNumber.length() / 2);
							// 终端序号
							System.arraycopy(CryptoUtils.getInstance()
									.bytesToHex(ksn.getBytes()), 0, data,
									offset + 6 + encTracks.length() / 2
											+ randomNumber.length() / 2, ksn
											.length() / 2);
							// 卡号
							System.arraycopy(
									maskedPAN.getBytes(),
									0,
									data,
									offset + 6 + encTracks.length() / 2
											+ randomNumber.length() / 2
											+ ksn.length() / 2,
									maskedPAN.length());
							// 卡有效期(4)
							System.arraycopy(
									expiryDate.getBytes(),
									0,
									data,
									offset + 6 + encTracks.length() / 2
											+ randomNumber.length() / 2
											+ ksn.length() / 2
											+ maskedPAN.length(), 4);
							// MAC
							System.arraycopy(
									CryptoUtils.getInstance().bytesToHex(
											cardMAC.getBytes()),
									0,
									data,
									offset + 6 + encTracks.length() / 2
											+ randomNumber.length() / 2
											+ ksn.length() / 2
											+ maskedPAN.length() + 4, cardMAC
											.length() / 2);

							LogUtil.printInfo(Util.BinToHex(data, 0,
									data.length));

							cardnum = "FF00"
									+ Util.BinToHex(data, 0, data.length);
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
									int lenth;
									lenth = Integer.parseInt(ic55DataStr
											.substring(pos + 5, pos + 6));

									ic55DataStr = ic55DataStr.substring(0, pos
											+ 6 + (lenth * 2));
								}
							}
							map.put("card_type", "1" + cmresult.CardType);

							while (cardSeriNoStr.length() < 3
									&& cardSeriNoStr.length() > 0)
								cardSeriNoStr = "0" + cardSeriNoStr;
							// cardSeriNoStr =
							// cardSeriNoStr.substring(cardSeriNoStr.length() -
							// 3,
							// cardSeriNoStr.length());

							map.put("ic_data", ic55DataStr);

							map.put("icsernum", cardSeriNoStr);
							map.put("dev_type",
									RyxSwiperCode.DEVICE_AC_SMALL_BULETOOTH);
							map.put("card_no", maskedPAN);
							map.put("pasamId", ksn.substring(20));
							listener.onDecodeCompleted(map);

						}
					}

				}
			}.start();
		}
//		else if(ac_type == 8){
//			//带密码键盘支持NFC的蓝牙点付宝
//			encapsulationData(map);
//		}
		// 带密码键盘大蓝牙
		else {
			Log.e("======", "大蓝牙");
			encapsulationData(map);
		}
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
		// TODO Auto-generated method stub
		if(ac_type==mpos_newtype){
			return RyxSwiperCode.DEVICE_TYPE_KEYBOARD_MPOS;
		}else if(ac_type==6){
			return RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
		}else{
			return RyxSwiperCode.DEVICE_TYPE_KEYBOARD_ORDINARY;
		}
	}

	@Override
	public String getCardno() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buss_type", "");
		map.put("order_id", "0000000000000000");
		map.put("amount", "0");
		map.put("getcardno", "1");
		startEmvSwiper(map);
		return "";
	}

	@Override
	public void writIc(String resp, String icdata) {
		new Thread(new icWriteBackThread(resp, icdata)).start();

	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		// TODO Auto-generated method stub
		boolean updateresult;
		if (flag == 0) {
			updateresult = commandtest.updateTerminalParameters(0,
					packageNo + 1, ByteUtils.hexString2ByteArray("31" + data));
		} else {
			updateresult = commandtest.updateTerminalParameters(1,
					packageNo + 1, ByteUtils.hexString2ByteArray("31" + data));
		}
		LogUtil.printInfo("BlueToothCommandAdapter+++flag==" + flag
				+ ",packageNo==" + packageNo + ",data==" + data
				+ ",updateresult=" + updateresult);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("index", packageNo);
		if (updateresult) {
			listener.onUpdateTerminalParamsCompleted(map);
		} else {
			listener.onUpdateTerminalParamsFailed(map);
		}
		return updateresult;
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
	/**
	 * 获取磁道信息并传递后台
	 */
	private void encapsulationData( final Map<String, Object> map){
		new Thread() {
			@Override
			public void run() {
				String getcardno = MapUtil.getString(map, "getcardno","");
				String action = MapUtil.getString(map, "buss_type");
				String orderid = MapUtil.getString(map, "order_id");
				String realamt = MapUtil.getString(map, "amount");
			String transLogNo=	CryptoUtils.getInstance()
				.getTransLogNo();
				byte[] random = Util.HexToBin(transLogNo);

				for (int i = 0; i < random.length; i++){
					LogUtil.printInfo(Integer.toHexString(random[i]));
				}

				CryptoUtils.getInstance().setTransLogUpdate(false);
				int devType=ac_type==mpos_newtype?0x21:1;
				if("1".equals(getcardno)){
					//获取卡号操作
					devType=ac_type==mpos_newtype?0x20:0;
				}
				CommandReturn cmresult = commandtest.statEmvSwiper(devType,
						ac_type==mpos_newtype?0x04:0x00,
						ac_type==mpos_newtype?(issupport21Msg?0x50:0x10):0x00,
						action,
						random, orderid.getBytes(),
						MoneyEncoder.encodeForSwiper(realamt));
				LogUtil.printInfo("transLogNo="+transLogNo+",random="+random.toString());
				if (null == cmresult) {
					listener.onTimeout();
				} else {
					LogUtil.printInfo("cmresult.CardType==="+cmresult.CardType);
					if(cmresult.CardType==2)//降级交易
					{
						Map<String,Object> errorMap=new HashMap<String, Object>();
						errorMap.put("demotionTrade", "IC卡请插卡!");
						listener.onDemotionTrade(errorMap);
					}
					else
					{
						LogUtil.printInfo("ic刷卡成功");
						StringBuffer stringBuffer=new StringBuffer();
						if(issupport21Msg){
							if(cmresult.terminalSerialNo!=null){
								stringBuffer.append("终端硬件序列号:"+new String(cmresult.terminalSerialNo));
							}
							if(cmresult.terminalSerialNoRandom!=null){
								stringBuffer.append("随机因子:"+new String(cmresult.terminalSerialNoRandom));
							}
							if(cmresult.terminalSerialNoEnc!=null){
								stringBuffer.append("硬件序列号密文:"+new String(cmresult.terminalSerialNoEnc));
							}
							LogUtil.printInfo("当前新设备"+stringBuffer.toString());
						}else {
							LogUtil.printInfo("当前旧设备");
						}
					Map<String, Object> map = new HashMap<String, Object>();
					String card_no = new String(cmresult.Return_ENCCardNo,
							0, cmresult.Return_ENCCardNo.length);
					String	expiryDateMax ="";
					if(cmresult.cardexpiryDate!=null){
						expiryDateMax = new String(cmresult.cardexpiryDate, 0,
								cmresult.cardexpiryDate.length);
					}
					LogUtil.printInfo("expiryDateMax="+expiryDateMax);
					listener.onGetCardNoCompleted(card_no, TextUtils.isEmpty(expiryDateMax)?"":expiryDateMax);
						if("1".equals(getcardno)){
							return;//如果是获取卡号操作则直接到此为止，不进行下面数据操作
						}
					// cardnum = "FE" + result;

					/*
					 * 带密码键盘设备样例报文分解： FE00 设备信息头 009D
					 * 后续报文长度，16进制（不包含信息头与本域的长度） 1F 刷卡成功标识 1C 卡号长度，16进制
					 * FA887B2618E2DD185CA454D60067E7BD689C5D8CF6E39D30D6C5883E
					 * 卡号密文 54 二三磁道及磁道加密随机数长度，16进制 3E1319943422
					 * B388DA6C9954497C1ED6B6498DE0B355E3A36BD67E9D3814841E0E64DDE159F312756258E031900D81F45E9C8B38AC308631235C631E3D6E0D07CED87AAF00E778501732754562FE7D8C
					 * 二三磁道密文 84373529 磁道加密随机数 0C PIN密文长度及PIN随机数长度，16进制
					 * 314DA084E7671871 PIN密文 6D7FAE83 PIN随机数 08 PSAM长度，16进制
					 * 1100000500000000 PSAM 0A 终端长度，16进制
					 * 00191120130620003129 终端号 08 MAC及MAC随机数长度，16进制
					 * F9675293 MAC 7DF7B1AF MAC随机数 带密码键盘设备样例报文分解： FE00
					 * 设备信息头 009D 后续报文长度，16进制（不包含信息头与本域的长度） 1F 刷卡成功标识 1C
					 * 卡号长度，16进制
					 * FA887B2618E2DD185CA454D60067E7BD689C5D8CF6E39D30D6C5883E
					 * 卡号密文 54 二三磁道及磁道加密随机数长度，16进制 3E1319943422
					 * B388DA6C9954497C1ED6B6498DE0B355E3A36BD67E9D3814841E0E64DDE159F312756258E031900D81F45E9C8B38AC308631235C631E3D6E0D07CED87AAF00E778501732754562FE7D8C
					 * 二三磁道密文 84373529 磁道加密随机数 0C PIN密文长度及PIN随机数长度，16进制
					 * 314DA084E7671871 PIN密文 6D7FAE83 PIN随机数 08 PSAM长度，16进制
					 * 1100000500000000 PSAM 0A 终端长度，16进制
					 * 00191120130620003129 终端号 08 MAC及MAC随机数长度，16进制
					 * F9675293 MAC 7DF7B1AF MAC随机数
					 */

					int len = 2 + 1 + 1 + cmresult.Return_ENCCardNo.length
							+ 1 + cmresult.Return_PSAMTrack.length + 1
							+ cmresult.Return_PSAMPIN.length + 1
							+ cmresult.Return_PSAMNo.length + 1
							+ cmresult.Return_TerSerialNo.length + 1
							+ cmresult.Return_PSAMMAC.length;
					LogUtil.printInfo("Return_ENCCardNo=="+cmresult.Return_ENCCardNo+",Return_PSAMTrack="+cmresult.Return_PSAMTrack
					+",Return_PSAMPIN="+cmresult.Return_PSAMPIN+",Return_PSAMNo="+cmresult.Return_PSAMNo+",Return_TerSerialNo="+cmresult.Return_TerSerialNo
					+",Return_PSAMMAC="+cmresult.Return_PSAMMAC);
					byte[] data = new byte[len];

					data[0] = (byte) ((len - 2) / 256);
					data[1] = (byte) ((len - 2) & 0x000000FF);

					// jics todo
					// data[2] = (byte) ((len - 2) & 0x000000FF);

					data[2] = 0x1F;

					// 卡号长度
					data[3] = (byte) (cmresult.Return_ENCCardNo.length);
					// 卡号密文
					System.arraycopy(cmresult.Return_ENCCardNo, 0, data, 4,
							cmresult.Return_ENCCardNo.length);
					// 二三磁道及磁道加密随机数长度，16进制
					data[4 + cmresult.Return_ENCCardNo.length] = (byte) (cmresult.Return_PSAMTrack.length);
					// 磁道加密数据
					System.arraycopy(cmresult.Return_PSAMTrack, 0, data,
							4 + cmresult.Return_ENCCardNo.length + 1,
							cmresult.Return_PSAMTrack.length);
					// PIN密文长度及PIN随机数长度
					data[4 + cmresult.Return_ENCCardNo.length + 1
							+ cmresult.Return_PSAMTrack.length] = (byte) (cmresult.Return_PSAMPIN.length);
					// PIN密文及PIN随机数
					System.arraycopy(cmresult.Return_PSAMPIN, 0, data, 4
							+ cmresult.Return_ENCCardNo.length + 1
							+ cmresult.Return_PSAMTrack.length + 1,
							cmresult.Return_PSAMPIN.length);
					// PSAM长度
					data[4 + cmresult.Return_ENCCardNo.length + 1
							+ cmresult.Return_PSAMTrack.length + 1
							+ cmresult.Return_PSAMPIN.length] = (byte) (cmresult.Return_PSAMNo.length);
					// PSAM号
					System.arraycopy(cmresult.Return_PSAMNo, 0, data, 4
							+ cmresult.Return_ENCCardNo.length + 1
							+ cmresult.Return_PSAMTrack.length + 1
							+ cmresult.Return_PSAMPIN.length + 1,
							cmresult.Return_PSAMNo.length);
					// 终端长度
					data[4 + cmresult.Return_ENCCardNo.length + 1
							+ cmresult.Return_PSAMTrack.length + 1
							+ cmresult.Return_PSAMPIN.length + 1
							+ cmresult.Return_PSAMNo.length] = (byte) (cmresult.Return_TerSerialNo.length);
					// 终端号
					System.arraycopy(cmresult.Return_TerSerialNo, 0, data,
							4 + cmresult.Return_ENCCardNo.length + 1
									+ cmresult.Return_PSAMTrack.length + 1
									+ cmresult.Return_PSAMPIN.length + 1
									+ cmresult.Return_PSAMNo.length + 1,
							cmresult.Return_TerSerialNo.length);
					// MAC长度
					data[4 + cmresult.Return_ENCCardNo.length + 1
							+ cmresult.Return_PSAMTrack.length + 1
							+ cmresult.Return_PSAMPIN.length + 1
							+ cmresult.Return_PSAMNo.length + 1
							+ cmresult.Return_TerSerialNo.length] = (byte) (cmresult.Return_PSAMMAC.length);
					// MAC
					System.arraycopy(cmresult.Return_PSAMMAC, 0, data, 4
							+ cmresult.Return_ENCCardNo.length + 1
							+ cmresult.Return_PSAMTrack.length + 1
							+ cmresult.Return_PSAMPIN.length + 1
							+ cmresult.Return_PSAMNo.length + 1
							+ cmresult.Return_TerSerialNo.length + 1,
							cmresult.Return_PSAMMAC.length);

					cardnum = "FE00" + Util.BinToHex(data, 0, data.length);
					map.put("card_info", cardnum);
					LogUtil.printInfo("card_info=="+cardnum);
					String cardSeriNoStr = "", ic55DataStr = "";

					if (cmresult.CardSerial != null) {
						cardSeriNoStr = Util.BinToHex(cmresult.CardSerial,
								0, cmresult.CardSerial.length);
					}

					if (cmresult.emvDataInfo != null) {
						ic55DataStr = Util.BinToHex(cmresult.emvDataInfo,
								0, cmresult.emvDataInfo.length);
					}
					LogUtil.printInfo("IC55域emvDataInfo==="+ic55DataStr);
					if (ic55DataStr.length() > 10) {
						int pos = 0;

						pos = ic55DataStr.indexOf("9F41");
						if (pos > 0) {
							int lenth;
							lenth = Integer.parseInt(ic55DataStr.substring(
									pos + 5, pos + 6));
							ic55DataStr = ic55DataStr.substring(0, pos + 6
									+ (lenth * 2));
						}

					}
					//nfcResult有值走QPBOC无值则走标准PBOC
					System.out.println("nfcResult======"+cmresult.nfcResult);
//					20：标准PBOC，21：快速QPBOC
					String myCardType="";
					if(cmresult.CardType==0){
						myCardType="10";
					}else if(cmresult.CardType==1){
						myCardType="11";
					}else if(cmresult.CardType==3){
						//sdk支持虚拟与实体区分
//						if(cmresult.nfcQuickType == 1){
//							buf.append("虚拟卡"+"\n");
//						}else if(cmresult.nfcQuickType == 0){
//							buf.append("实体卡"+"\n");
//						}else{
//							buf.append("未知类型卡"+"\n");
//						}
						//非接设备
						if(cmresult.nfcResult==0){
							//无值则标准PBOC
							myCardType="20";
						}else{
							myCardType="21";
						}
					}
					map.put("card_type", myCardType);

					while (cardSeriNoStr.length() < 3
							&& cardSeriNoStr.length() > 0)
						cardSeriNoStr = "0" + cardSeriNoStr;

					map.put("ic_data", ic55DataStr);

					map.put("icsernum", cardSeriNoStr);
					map.put("dev_type",
							RyxSwiperCode.DEVICE_AC_BIG_BULETOOTH);
					map.put("has_pin", "1");
					map.put("card_no", card_no);
				String pasamId=	 Util.BinToHex(cmresult.Return_PSAMNo, 0,cmresult.Return_PSAMNo.length);
				map.put("pasamId",pasamId);
					listener.onDecodeCompleted(map);
					}
				}

			}
		}.start();
		
	}

	class GetKsnThread extends Thread {
		@Override
		public void run() {
			try {
				CommandReturn cmdret = null;
				if (commandtest.getTerminalType() == 6) {
					//不带密码键盘小蓝牙
					cmdret = commandtest.getKsnI21();
				} else if(commandtest.getTerminalType() == mpos_newtype){
					//带密码键盘NFC非接设备
					cmdret = commandtest.getKSN();
				} else{
					//带密码键盘大蓝牙设备
					cmdret = commandtest.getKSN();
				}

				if (cmdret != null) {

					if (commandtest.getTerminalType() == 6) {
						miniksn = Util.BinToHex(cmdret.ksn, 0, cmdret.ksn.length);
					} else if(commandtest.getTerminalType()  == mpos_newtype){
						miniksn = Util.BinToHex(cmdret.Return_TerSerialNo, 0,
								cmdret.Return_TerSerialNo.length)+ Util.BinToHex(cmdret.Return_PSAMNo, 0,
								cmdret.Return_PSAMNo.length);
					}
					else
					{
						miniksn =  Util.BinToHex(cmdret.Return_TerSerialNo, 0,
								cmdret.Return_TerSerialNo.length)+Util.BinToHex(cmdret.Return_PSAMNo, 0,
								cmdret.Return_PSAMNo.length);
					}
					listener.onDetected();

				} else {
					listener.onTimeout();
				}
			} catch (Exception e) {
				listener.onError(0x001, "连接异常,请重试!");
			}
		}
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
}
