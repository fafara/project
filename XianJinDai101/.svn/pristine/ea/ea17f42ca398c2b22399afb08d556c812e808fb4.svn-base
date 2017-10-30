package com.ryx.swiper.devs.newland;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.DeviceRTException;
import com.newland.mtype.ProcessTimeoutException;
import com.newland.mtype.conn.DeviceConnParams;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CardRule;
import com.newland.mtype.module.common.cardreader.OpenCardType;
import com.newland.mtype.module.common.emv.EmvTransController;
import com.newland.mtype.module.common.emv.EmvTransInfo;
import com.newland.mtype.module.common.emv.SecondIssuanceRequest;
import com.newland.mtype.module.common.pin.PinInputEvent;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.swiper.Account;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.tlv.TLVPackage;
import com.newland.mtype.util.Dump;
import com.newland.mtypex.bluetooth.BlueToothV100ConnParams;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.beans.DeviceInfo;
import com.ryx.swiper.devs.newland.common.ByteUtils;
import com.ryx.swiper.devs.newland.common.CardData;
import com.ryx.swiper.devs.newland.common.Const;
import com.ryx.swiper.devs.newland.common.DeviceController;
import com.ryx.swiper.devs.newland.common.DeviceControllerImplForMe15;
import com.ryx.swiper.devs.newland.common.TransferListener;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.DataUtil;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.whty.device.utils.GPMethods;

public class NewLandBlueToothAdapter extends CSwiperAdapter {
	private static final String ME3X_DRIVER_NAME = "com.newland.me.ME3xDriver";
	private Context context;
	private ISwiperStateListener listener;
	private IRyxDevSearchListener devSearchlistener;
	private DeviceController controller;
	private EmvTransController emvController;
	private BluetoothAdapter bluetoothAdapter;
	private List<DeviceInfo> discoveredDevices = new ArrayList<DeviceInfo>();
	private String pid;
	public static String transLogNo;
	private static List<Integer> L_55TAGS = new ArrayList<Integer>();
	static {
		L_55TAGS.add(0x9F26);
		L_55TAGS.add(0x9F27);
		L_55TAGS.add(0x9F10);
		L_55TAGS.add(0x9F37);
		L_55TAGS.add(0x9F02);
		L_55TAGS.add(0x9F36);
		L_55TAGS.add(0x95);
		L_55TAGS.add(0x9A);
		L_55TAGS.add(0x9C);
		L_55TAGS.add(0x82);
		// L_55TAGS.add(0x91);
		// L_55TAGS.add(0x71);
		// L_55TAGS.add(0x72);
		L_55TAGS.add(0x84);
		// L_55TAGS.add(0x8A);
		L_55TAGS.add(0x5F2A);
		L_55TAGS.add(0x9F1A);
		L_55TAGS.add(0x9F03);
		L_55TAGS.add(0x9F33);
		// L_55TAGS.add(0x9F74);
		L_55TAGS.add(0x9F34);
		L_55TAGS.add(0x9F35);
		L_55TAGS.add(0x9F1E);
		L_55TAGS.add(0x9F09);
		L_55TAGS.add(0x9F41);
		// L_55TAGS.add(0x9F63);
		// L_55TAGS.add(0xDF31);
		// L_55TAGS.add(0xDF32);
		// L_55TAGS.add(0xDF33);
		// L_55TAGS.add(0xDF34);
		// L_55TAGS.add(0xDF75);
		// L_55TAGS.add(0xDF78);
	}
	String orderid;
	private String strKsn;
	com.newland.mtype.DeviceInfo deviceInfo;

	public NewLandBlueToothAdapter(Context context,
			ISwiperStateListener listener) {
		this.context = context;
		this.listener = listener;
		controller = DeviceControllerImplForMe15.getInstance();
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
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (ifAddressExist(device.getAddress())) {
					return;
				}
				DeviceInfo devInfo = new DeviceInfo();
				devInfo.name = device.getName() == null ? device.getAddress()
						: device.getName();
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
	public int initCSwiper(String address) {
		NewLandBlueToothAdapter.this.context
				.unregisterReceiver(discoveryReciever);
		initMe3xDeviceController(new BlueToothV100ConnParams(address));
		return 0;
	}

	/**
	 * 初始化ME3x设备
	 * 
	 * @since ver1.0
	 * @param params
	 *            设备连接参数
	 */
	private void initMe3xDeviceController(DeviceConnParams params) {
		controller = DeviceControllerImplForMe15.getInstance();
		controller.init(context, ME3X_DRIVER_NAME, params,
				new DeviceEventListener<ConnectionCloseEvent>() {
					@Override
					public void onEvent(ConnectionCloseEvent event,
							Handler handler) {
						if (event.isSuccess()) {
							listener.onDecodeError("设备被客户主动断开！");
						}
						if (event.isFailed()) {
							listener.onDecodeError("设备链接异常断开！"
									+ event.getException().getMessage());
						}
					}

					@Override
					public Handler getUIHandler() {
						return null;
					}
				});
		listener.onBluetoothConnectSuccess();
	}

	@Override
	public void startEmvSwiper(Map<String, Object> map) {
		final String amount = MapUtil.getString(map, "amount");
		orderid = MapUtil.getString(map, "order_id");
		transLogNo = CryptoUtils.getInstance().getTransLogNo();
		CryptoUtils.getInstance().setTransLogUpdate(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				connectDevice();
				BigDecimal mAmt = new BigDecimal(amount);
				try {
					String msg = "";
					if("ME30".equals(pid)){
						msg = "请刷卡 、插卡或贴卡";
					}
					// 交易金额为 请刷卡或者插入IC卡
					controller.startTransfer(context, new OpenCardType[] {
							OpenCardType.SWIPER, OpenCardType.ICCARD,
							OpenCardType.NCCARD },msg, mAmt, 60,
							TimeUnit.SECONDS, CardRule.ALLOW_LOWER,
							new SimpleTransferListener());
				} catch (Exception e) {
					if (e instanceof ProcessTimeoutException) {
						// 超时
						listener.onTimeout();
						return;
					} else if (e instanceof DeviceRTException) {
						// 交易失败
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private class SimpleTransferListener implements TransferListener {
		@Override
		public void onEmvFinished(boolean arg0, EmvTransInfo context)
				throws Exception {
			Log.d(">>>>", "emv交易结束:" + ""/* context.externalToString() */);
		}

		@Override
		public void onError(EmvTransController arg0, Exception arg1) {
			Log.d(">>>>",
					"emv交易失败:" + arg0.toString() + "---- " + arg1.toString());
			// processingFinished();
		}

		@Override
		public void onFallback(EmvTransInfo arg0) throws Exception {
			Log.d(">>>>", "交易降级");
			// startSwipTransfer();
			// processingFinished();
		}

		@Override
		public void onRequestOnline(EmvTransController etccontroller,
				EmvTransInfo context) throws Exception {
			if ("ME15".equals(pid)) {
				listener.onWaitingCardDataReader();
			}
			
			emvController = etccontroller;
			SwipResult swipRslt = controller.getTrackText(
					Const.CardType.ICCARD, transLogNo);
			TLVPackage tlvPackage = context.setExternalInfoPackage(L_55TAGS);
			String mTrack1 = "";
			String mTrack2 = Dump.getHexDump(swipRslt.getSecondTrackData())
					.replaceAll(" ", "");
			String mTrack3 = "";
			String ksn = strKsn;
			String encTracks = mTrack1 + mTrack2 + mTrack3;// 暂无
			int track1Length = mTrack1 == null ? 0 : mTrack1.length() / 2;
			int track2Length = mTrack2 == null ? 0 : mTrack2.length() / 2;
			int track3Length = mTrack3 == null ? 0 : mTrack3.length() / 2;
			String maskedPAN = swipRslt.getAccount().getAcctNo();// 使用卡号
			String expiryDate = context.getCardExpirationDate();
			listener.onGetCardNoCompleted(maskedPAN, expiryDate);
			String cardSeriNo = context.getCardSequenceNumber();
			byte[] ic55Data = tlvPackage.pack();
			String randomNumber = Dump.getHexDump(swipRslt.getThirdTrackData())
					.replaceAll(" ", "");// 暂无
			String cardMAC = "";
			String macInput = "";
			String panHex = DataUtil.genHexString(maskedPAN);
			LogUtil.printInfo("randomNumber==" + randomNumber);
			String pinStr = "";
			String pin_sksn = "";
			if ("ME30".equals(pid)) {
				PinInputEvent inputEvent = controller.startPininput(maskedPAN,
						6, "请输入密码");
				pin_sksn = GPMethods.bytesToHexString(inputEvent.getKsn());
				pin_sksn = pin_sksn.substring(0, 8);
				pinStr = GPMethods.bytesToHexString(inputEvent.getEncrypPin());
				pinStr = pinStr + pin_sksn;
			}
			String pasamId = strKsn.substring(20);
			String termId = strKsn.substring(0, 20);

			if ("ME30".equals(pid)) {
				// Mac入参
				// MAB组成：32字节订单号+卡号长度+卡号密文+二三磁道及磁道加密随机数长度+二三磁道密文+磁道加密随机数
				// +PIN密文+PIN加密随机数+PSAM+终端号
				macInput = DataUtil
						.getPinMacHexString(orderid, DataUtil.genHexStr(maskedPAN.length()), panHex,
								Integer.toHexString(track2Length + track3Length
										+ randomNumber.length() / 2), mTrack2
										+ mTrack3, randomNumber, pinStr,
								pasamId, termId);
			} else if ("ME15".equals(pid)) {
				macInput = DataUtil.genMacHexString(mTrack2, mTrack3,
						randomNumber, ksn, orderid);
			}

			byte[] bMac = controller.encrypt(new WorkingKey(4),
					ByteUtils.hexString2ByteArray(macInput + transLogNo));
			for (int i = 0; i < bMac.length; i++)
				cardMAC = cardMAC + String.format("%02X", bMac[i]);
			Map<String, Object> resultmapp = null;
			if ("ME30".equals(pid)) {
				resultmapp = DataUtil.putPwdDeviceCardServerData("11",
						TextUtils.isEmpty(pinStr) ? "" : pinStr,
						RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH, cardMAC,
						pasamId, termId, DataUtil.genHexString(maskedPAN),
						encTracks + randomNumber, cardSeriNo, ic55Data);
			} else if ("ME15".equals(pid)) {
				resultmapp = DataUtil.putCardServerData("11",
						RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH, cardMAC, ksn,
						track1Length, track2Length, track3Length, randomNumber,
						maskedPAN, encTracks, expiryDate, cardSeriNo, ic55Data);
			}

			listener.onDecodeCompleted(resultmapp);

		}

		@Override
		public void onRequestPinEntry(EmvTransController arg0, EmvTransInfo arg1)
				throws Exception {
			Log.d(">>>>", "错误的事件返回，不可能要求密码输入");
			arg0.cancelEmv();
		}

		@Override
		public void onRequestSelectApplication(EmvTransController arg0,
				EmvTransInfo arg1) throws Exception {
			Log.d(">>>>", "错误的事件返回，不可能要求应用选择！");
			arg0.cancelEmv();
		}

		@Override
		public void onRequestTransferConfirm(EmvTransController arg0,
				EmvTransInfo arg1) throws Exception {
			Log.d(">>>>", "错误的事件返回，不可能要求交易确认！");
			arg0.cancelEmv();
		}

		@Override
		public void onSwipMagneticCard(SwipResult swipRslt) {
			// startSwipTransfer();
		}

		@Override
		public void onOpenCardreaderCanceled() {
			Log.d(">>>>", "用户撤销刷卡操作！");
			// processingFinished();
		}

		@Override
		public void onSwipMagneticCard(final SwipResult swipRslt, BigDecimal amt) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					connectDevice();
					try {
						if (swipRslt == null) {
							// 刷卡撤销
							Log.d(">>>>", "刷卡撤销");
							return;
						}
						String serviceCode = swipRslt.getServiceCode();
						if (serviceCode.startsWith("2")
								|| serviceCode.startsWith("6")) {
							Map<String, Object> errorMap = new HashMap<String, Object>();
							errorMap.put("demotionTrade", "IC卡请插卡!");
							listener.onDemotionTrade(errorMap);
						} else {
							if ("ME15".equals(pid)) {
								listener.onWaitingCardDataReader();
							}
							byte[] secondTrack = swipRslt.getSecondTrackData();
							byte[] thirdTrack = swipRslt.getThirdTrackData();
							Account account = swipRslt.getAccount();
							String accountNoString = account.getAcctNo();
							CardData cardData = CardData.getInstance();
							cardData.setSecondTrack(secondTrack);
							cardData.setThirdTrack(thirdTrack);
							cardData.setCardNo(accountNoString);

							// String strKsn =
							// Dump.getHexDump(swipRslt.getKsn());
							String mTrack1 = "";
							String mTrack2 = (secondTrack == null ? "" : Dump
									.getHexDump(secondTrack)
									.replaceAll(" ", ""));
							String mTrack3 = (thirdTrack == null ? "" : Dump
									.getHexDump(thirdTrack).replaceAll(" ", ""));
							String ksn = strKsn;
							String encTracks = mTrack1
									+ mTrack2
									+ mTrack3.substring(0, mTrack3.length() - 8);// 暂无
							// //加密的磁道数据+随机数。(如果磁道为明文后面不跟随机数)
							int track1Length = mTrack1 == null ? 0 : mTrack1
									.length() / 2;
							int track2Length = mTrack2 == null ? 0 : mTrack2
									.length() / 2;
							int track3Length = mTrack3 == null ? 0 : (mTrack3
									.length() - 8) / 2;
							// String cardNumber = MapUtil.get(ctresultMap,
							// "cardNo", "");
							String maskedPAN = accountNoString;// 使用卡号
							String expiryDate = swipRslt.getValidDate();
							listener.onGetCardNoCompleted(maskedPAN, expiryDate);
							String cardMAC = "";
							byte[] ic55Data = null;
							String randomNumber = mTrack3.substring(mTrack3
									.length() - 8);// 暂无
							String macInput = "";
							String pinStr = "";
							String pin_sksn = "";
							if ("ME30".equals(pid)) {
								PinInputEvent inputEvent = controller
										.startPininput(maskedPAN, 6, "请输入密码");
								pin_sksn = GPMethods
										.bytesToHexString(inputEvent.getKsn());
								pin_sksn = pin_sksn.substring(0, 8);
								pinStr = GPMethods.bytesToHexString(inputEvent
										.getEncrypPin());
								pinStr = pinStr + pin_sksn;
							}
							String panHex = DataUtil.genHexString(maskedPAN);
							String pasamId = strKsn.substring(20);
							String termId = strKsn.substring(0, 20);
							if ("ME30".equals(pid)) {
								// Mac入参
								// MAB组成：32字节订单号+卡号长度+卡号密文+二三磁道及磁道加密随机数长度+二三磁道密文+磁道加密随机数
								// +PIN密文+PIN加密随机数+PSAM+终端号
								macInput = DataUtil.getPinMacHexString(
										orderid,
										DataUtil.genHexStr(maskedPAN.length()),
										panHex, Integer
												.toHexString(track2Length
														+ track3Length
														+ randomNumber.length()
														/ 2),
										mTrack2 + mTrack3, "", pinStr, pasamId,
										termId);

							} else if ("ME15".equals(pid)) {
								macInput = DataUtil.genMacHexString(mTrack2+mTrack3,  ksn, orderid);
							}
							byte[] bMac = controller.encrypt(
									new WorkingKey(4),
									ByteUtils.hexString2ByteArray(macInput
											+ transLogNo));
							for (int i = 0; i < bMac.length; i++)
								cardMAC = cardMAC
										+ String.format("%02X", bMac[i]);

							Map<String, Object> resultmapp = null;
							if ("ME30".equals(pid)) {
								resultmapp = DataUtil
										.putPwdDeviceCardServerData(
												"10",
												TextUtils.isEmpty(pinStr) ? ""
														: pinStr,
												RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH,
												cardMAC,
												pasamId,
												termId,
												DataUtil.genHexString(maskedPAN),
												encTracks + randomNumber, "",
												ic55Data);
							} else if ("ME15".equals(pid)) {
								resultmapp = DataUtil.putCardServerData("10",
										RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH,
										cardMAC, ksn, track1Length,
										track2Length, track3Length,
										randomNumber, maskedPAN, encTracks,
										expiryDate, "", ic55Data);
							}

							listener.onDecodeCompleted(resultmapp);
						}

					} catch (Exception e) {
						// 消费处理异常:
						System.out.println("消费处理异常:" + e.getMessage());
						e.printStackTrace();
					}
				}
			}).start();
		}

		@Override
		public void onIcCardStartSwiper() {
			listener.EmvOperationWaitiing();
		}

		// 贴卡操作结束
		@Override
		public void onRFCardSwiped(SwipResult swipRslt,EmvTransInfo context) throws Exception{
			TLVPackage tlvPackage = context.setExternalInfoPackage(L_55TAGS);
			String mTrack1 = "";
			String mTrack2 = Dump.getHexDump(swipRslt.getSecondTrackData())
					.replaceAll(" ", "");
			String mTrack3 = "";
			String ksn = strKsn;
			String encTracks = mTrack1 + mTrack2 + mTrack3;// 暂无
			int track1Length = mTrack1 == null ? 0 : mTrack1.length() / 2;
			int track2Length = mTrack2 == null ? 0 : mTrack2.length() / 2;
			int track3Length = mTrack3 == null ? 0 : mTrack3.length() / 2;
			String maskedPAN = swipRslt.getAccount().getAcctNo();// 使用卡号
			String expiryDate = context.getCardExpirationDate();
			listener.onGetCardNoCompleted(maskedPAN, expiryDate);
			String cardSeriNo = context.getCardSequenceNumber();
			byte[] ic55Data = tlvPackage.pack();
			String randomNumber = Dump.getHexDump(swipRslt.getThirdTrackData())
					.replaceAll(" ", "");// 暂无
			String cardMAC = "";
			String macInput = "";
			String panHex = DataUtil.genHexString(maskedPAN);
			LogUtil.printInfo("randomNumber==" + randomNumber);
			String pinStr = "";
			String pin_sksn = "";
			if ("ME30".equals(pid)) {
				PinInputEvent inputEvent = controller.startPininput(maskedPAN,
						6, "请输入密码");
				pin_sksn = GPMethods.bytesToHexString(inputEvent.getKsn());
				pin_sksn = pin_sksn.substring(0, 8);
				pinStr = GPMethods.bytesToHexString(inputEvent.getEncrypPin());
				pinStr = pinStr + pin_sksn;
			}
			String pasamId = strKsn.substring(20);
			String termId = strKsn.substring(0, 20);

			if ("ME30".equals(pid)) {
				// Mac入参
				// MAB组成：32字节订单号+卡号长度+卡号密文+二三磁道及磁道加密随机数长度+二三磁道密文+磁道加密随机数
				// +PIN密文+PIN加密随机数+PSAM+终端号
				macInput = DataUtil
						.getPinMacHexString(orderid, DataUtil.genHexStr(maskedPAN.length()), panHex,
								Integer.toHexString(track2Length + track3Length
										+ randomNumber.length() / 2), mTrack2
										+ mTrack3, randomNumber, pinStr,
								pasamId, termId);
			} else if ("ME15".equals(pid)) {
				macInput = DataUtil.genMacHexString(mTrack2, mTrack3,
						randomNumber, ksn, orderid);
			}

			byte[] bMac = controller.encrypt(new WorkingKey(4),
					ByteUtils.hexString2ByteArray(macInput + transLogNo));
			for (int i = 0; i < bMac.length; i++)
				cardMAC = cardMAC + String.format("%02X", bMac[i]);
			Map<String, Object> resultmapp = null;
			if ("ME30".equals(pid)) {
				resultmapp = DataUtil.putPwdDeviceCardServerData("21",
						TextUtils.isEmpty(pinStr) ? "" : pinStr,
						RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH, cardMAC,
						pasamId, termId, DataUtil.genHexString(maskedPAN),
						encTracks + randomNumber, cardSeriNo, ic55Data);
			} else if ("ME15".equals(pid)) {
				resultmapp = DataUtil.putCardServerData("21",
						RyxSwiperCode.DEVICE_NEWLAND_BULETOOTH, cardMAC, ksn,
						track1Length, track2Length, track3Length, randomNumber,
						maskedPAN, encTracks, expiryDate, cardSeriNo, ic55Data);
			}

			listener.onDecodeCompleted(resultmapp);
		}

	}

	public void connectDevice() {
		// 设备连接中...
		Log.d(">>>>", "设备连接中...");
		try {
			controller.connect();
			// 设备连接成功...
			Log.d(">>>>", "设备连接成功...");
		} catch (Exception e1) {
			e1.printStackTrace();
			// 蓝牙链接异常,请检查设备或重新连接...
			Log.d(">>>>", "蓝牙链接异常,请检查设备或重新连接...");
		}
	}

	@Override
	public void disConnect() {
		controller.disConnect();
	}

	@Override
	public void releaseCSwiper() {
		controller.destroy();
	}

	@Override
	public String getKsn() {
		return this.strKsn;
	}

	@Override
	public void getKsnSync() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					connectDevice();
					deviceInfo = controller.getDeviceInfo();
					strKsn = deviceInfo.getCSN();
					if(strKsn.length()!=36){
						strKsn  = "800188799901000008678879990100000867";
					}
					pid = deviceInfo.getPID().name();
					listener.onDetected();
				} catch (Exception e) {
					listener.onDetecteError();
				}
			}
		}).start();
	}

	@Override
	public boolean isDevicePresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getDeviceType() {
		return "ME30".equals(pid)?RyxSwiperCode.DEVICE_TYPE_KEYBOARD_MPOS:RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
	}
	

	@Override
	public String getCardno() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buss_type", "");
		map.put("order_id", "0000000000000000");
		map.put("amount", "000000000000");
		startEmvSwiper(map);
		return null;
	}

	@Override
	public void writIc(String resp, String icdata) {
		if (emvController != null) {
			SecondIssuanceRequest request = new SecondIssuanceRequest();
			request.setAuthorisationResponseCode(resp);
			request.setIssuerAuthenticationData(icdata.getBytes());
			emvController.secondIssuance(request);
			listener.onICResponse(1, null, null);
		} else {
			listener.onICResponse(0, null, null);
		}
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		// emvController.
		Map map = new HashMap<String, Object>();
		map.put("index", packageNo);
		listener.onUpdateTerminalParamsCompleted(map);
		return false;
	}

	@Override
	public int getTerminalType() {
		return 0;
	}

}
