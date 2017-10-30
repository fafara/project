package com.ryx.swiper.devs.landi;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.landicorp.android.mpos.reader.LandiMPos;
import com.landicorp.mpos.reader.BasicReaderListeners;
import com.landicorp.mpos.reader.BasicReaderListeners.AddAidListener;
import com.landicorp.mpos.reader.BasicReaderListeners.AddPubKeyListener;
import com.landicorp.mpos.reader.model.M1CardAuthModel;
import com.landicorp.robert.comm.api.CommunicationManagerBase;
import com.landicorp.robert.comm.api.CommunicationManagerBase.DeviceSearchListener;
import com.landicorp.robert.comm.api.DeviceInfo;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.LogUtil;

public class LanDiBlueAdapter extends LanDiBaseAdapter {

	public LanDiBlueAdapter(Context context, ISwiperStateListener listener) {
		this.context = context;
		this.listener = listener;
		reader = LandiMPos.getInstance(context);
		deviceType = RyxSwiperCode.DEVICE_LANDI_BULETOOTH;
		deviceInfo = new DeviceInfo();
		deviceInfo.setDevChannel(CommunicationManagerBase.DeviceCommunicationChannel.BLUETOOTH);
	}

	/**
	 * 开启蓝牙设备
	 */
	private void openDev() {
		new Thread() {
			@Override
			public void run() {
				// deviceInfo.setDevChannel(CommunicationManagerBase.DeviceCommunicationChannel.BLUETOOTH);
				reader.openDevice(CommunicationManagerBase.CommunicationMode.MODE_DUPLEX, deviceInfo, new BasicReaderListeners.OpenDeviceListener() {

					@Override
					public void openSucc() {
						LogUtil.printInfo("打开设备成功,Id=" + deviceInfo.getIdentifier() + " Name=" + deviceInfo.getName());
						M1CardAuthModel authModel = new M1CardAuthModel();
						authModel.setCardCrypt("00");
						authModel.setSectorNo(1);
						authModel.setCyptType(BasicReaderListeners.CryptType.CRYPT_A);
						// 测试交易
						// Map<String, Object> map = new HashMap<String,
						// Object>();
						// map.put("amount", "000000000001");
						// startEmvSwiper(map);
						listener.onBluetoothConnectSuccess();
					}

					@Override
					public void openFail() {
						LogUtil.printInfo("打开设备失败,Id=" + deviceInfo.getIdentifier() + " Name=" + deviceInfo.getName());
						listener.onBluetoothConnectFail();
					}
				});
			}
		}.start();
	}

	@Override
	public void searchBlueDevs(final IRyxDevSearchListener listener) {
		reader.startSearchDev(new DeviceSearchListener() {
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
		}, false, true, 60000);
	}

	@Override
	public int connectCSwiper(String address) {
		return initCSwiper(address);
	}

	@Override
	public int initCSwiper(String address) {
		if (deviceInfo == null) {
			deviceInfo = new DeviceInfo();
			deviceInfo.setDevChannel(CommunicationManagerBase.DeviceCommunicationChannel.BLUETOOTH);
		}
		deviceInfo.setIdentifier(address);
		openDev();
		return 0;
	}

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {
		super.startEmvSwiper(map);
	}

	@Override
	public void disConnect() {
		super.disConnect();
	}

	@Override
	public String getKsn() {
		return this.strKsn;
	}

	@Override
	public void getKsnSync() {
		super.getKsnSync();
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
		Map<String, Object> map=new HashMap<String, Object>();
    	map.put("buss_type", "");
    	map.put("order_id", "0000000000000000");
    	map.put("amount", "000000000000");
    	startEmvSwiper(map);
		return "";
	}

	@Override
	public void writIc(String resp, String icdata) {
		super.writIc(resp, icdata);
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		this.index=packageNo;
		final Map<String,Object> map=new HashMap<String,Object>();
		map.put("index", index);
		//更新公钥
		if(flag==0)
		{
			reader.addPubKey(com.ryx.swiper.devs.landi.utils.ByteArrayUtils.toByteArray(data), new AddPubKeyListener() {
				
				@Override
				public void onError(int arg0, String arg1) {
					Log.e("landi_update_pubkey", index+"fail");
					listener.onUpdateTerminalParamsFailed(map);
				}
				
				@Override
				public void onAddPubKeySucc() {
					Log.e("landi_update_pubkey", index+"success");
					listener.onUpdateTerminalParamsCompleted(map);
				}
			});
		}
		//更新aid
		else
		{
			reader.AddAid(com.ryx.swiper.devs.landi.utils.ByteArrayUtils.toByteArray(data), new AddAidListener() {
				
				@Override
				public void onError(int arg0, String arg1) {
					Log.e("landi_update_aid", index+"fail");
					listener.onUpdateTerminalParamsFailed(map);
				}
				
				@Override
				public void onAddAidSucc() {
					Log.e("landi_update_aid", index+"success");
					listener.onUpdateTerminalParamsCompleted(map);
				}
			});
		}
		return false;
	}

	@Override
	public void releaseCSwiper() {
		super.releaseCSwiper();
	}
	@Override
	public int getTerminalType() {
		return 0;
	}

}
