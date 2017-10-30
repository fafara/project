package com.ryx.swiper.devs.landi;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.landicorp.android.mpos.reader.LandiMPos;
import com.landicorp.mpos.reader.BasicReaderListeners;
import com.landicorp.mpos.reader.BasicReaderListeners.AddAidListener;
import com.landicorp.mpos.reader.BasicReaderListeners.AddPubKeyListener;
import com.landicorp.robert.comm.api.CommunicationManagerBase;
import com.landicorp.robert.comm.api.DeviceInfo;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.LogUtil;

/**
 * Created by xuchenchen on 2015/12/14.
 */
public class LanDiAudioAdapter extends LanDiBaseAdapter {

	public LanDiAudioAdapter(Context context, ISwiperStateListener listener) {
		this.context = context;
		this.listener = listener;
		reader = LandiMPos.getInstance(context);
		deviceType = RyxSwiperCode.DEVICE_LANDI_AUDIO;
		deviceInfo = new DeviceInfo();
		deviceInfo.setDevChannel(CommunicationManagerBase.DeviceCommunicationChannel.AUDIOJACK);
		LogUtil.printInfo("LanDiAudioAdapter====================");

	}

	@Override
	public void searchBlueDevs(IRyxDevSearchListener listener) {
	}

	@Override
	public int connectCSwiper(String address) {
		getKsnSync();
		return 0;
	}

	@Override
	public int initCSwiper(String address) {
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
		deviceInfo = new DeviceInfo();
		deviceInfo.setDevChannel(CommunicationManagerBase.DeviceCommunicationChannel.AUDIOJACK);
		reader.openDevice(CommunicationManagerBase.CommunicationMode.MODE_DUPLEX, deviceInfo, new BasicReaderListeners.OpenDeviceListener() {
			@Override
			public void openSucc() {
				listener.onAudioDevOpenSuccess();
			}

			@Override
			public void openFail() {
				listener.onAudioDevOpenFail();
			}
		});
		return true;
	}

	@Override
	public int getDeviceType() {
		return RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
	}

	@Override
	public String getCardno() {
		return null;
	}

	@Override
	public void writIc(String resp, String icdata) {
		super.writIc(resp, icdata);
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		final Map<String,Object> map=new HashMap<String,Object>();
		map.put("index", packageNo);
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
		// TODO Auto-generated method stub
		return 0;
	}

}
