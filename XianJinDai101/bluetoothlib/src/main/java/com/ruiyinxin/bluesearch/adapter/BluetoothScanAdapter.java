package com.ruiyinxin.bluesearch.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ruiyinxin.bluesearch.adapter.BlueToothSearchCommonAdapter.BlueToothSearchCommonListener;
import com.ruiyinxin.bluesearch.bean.BlueToothSearchStatus;

public class BluetoothScanAdapter extends BaseSearchAdapter {

	private Context context;
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BlueToothSearchCommonListener commonListener;

	private int REQUEST_ENABLE_BT = 11;

	public BluetoothScanAdapter(Context context,
			BlueToothSearchCommonListener commonListener) {

		this.context = context;
		this.commonListener = commonListener;
		initBluetooth();
	}

	@SuppressLint("NewApi")
	public void initBluetooth() {
		bluetoothManager = (BluetoothManager) context
				.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			((Activity) context).startActivityForResult(enableBtIntent,
					REQUEST_ENABLE_BT);
		}
		commonListener.searchStateListener(
				BlueToothSearchStatus.STARTBLUETOOTH_FLAG,
				BlueToothSearchStatus.STARTBLUETOOTH_MESSAGE);
	}

	// Device scan callback.
	@SuppressLint("NewApi")
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			commonListener.discoverOneDevice(device);
		}

	};

	@SuppressLint("NewApi")
	@Override
	public void searchBlueDevs() {
		if (mBluetoothAdapter == null) {
			this.commonListener.searchStateListener(
					BlueToothSearchStatus.NONSUPPORTBLUETOOTH_FLAG,
					BlueToothSearchStatus.NONSUPPORTBLUETOOTH_MESSAGE);
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				 Log.d("ryx", "BluetoothScanAdapter_enable");
				mBluetoothAdapter.enable();
				commonListener.searchStateListener(
							BlueToothSearchStatus.STARTBLUETOOTH_FLAG,
							BlueToothSearchStatus.STARTBLUETOOTH_MESSAGE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						 try {
							  //等待蓝牙设备开启成功后进行搜索
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						 Log.d("ryx", "BluetoothScanAdapter_startLeScan");
						 mBluetoothAdapter.startLeScan(mLeScanCallback);
						 commonListener.searchStateListener(
									BlueToothSearchStatus.STARTSEARCHBLUETOOTH_FLAG,
									BlueToothSearchStatus.STARTSEARCHBLUETOOTH_MESSAGE);
					}
				}).start();
				
			}else{
				Log.d("ryx", "BluetoothScanAdapter_startLeScan");
				 mBluetoothAdapter.startLeScan(mLeScanCallback);
				 commonListener.searchStateListener(
							BlueToothSearchStatus.STARTSEARCHBLUETOOTH_FLAG,
							BlueToothSearchStatus.STARTSEARCHBLUETOOTH_MESSAGE);
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void releaseResoure() {
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			mBluetoothAdapter.disable();
		}

		commonListener.searchStateListener(
				BlueToothSearchStatus.RELEASEBLUETOOTHRESOURE_FLAG,
				BlueToothSearchStatus.RELEASEBLUETOOTHRESOURE_MESSAGE);

	}

	@SuppressLint("NewApi")
	@Override
	public void stopBlueToothsearch() {
		if(mBluetoothAdapter!=null)
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		commonListener.searchStateListener(
				BlueToothSearchStatus.CLOSESEARCHBLUETOOTH_FLAG,
				BlueToothSearchStatus.CLOSESEARCHBLUETOOTH_MESSAGE);
	}
	@SuppressLint("NewApi")
	@Override
	public void manualstopBlueToothsearch() {
		// TODO Auto-generated method stub
		if(mBluetoothAdapter!=null)
			 Log.d("ryx", "BluetoothScanAdapter_manualstopBlueToothsearch_stopLeScan");
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			commonListener.searchStateListener(
					BlueToothSearchStatus.MANUALCLOSESEARCHBLUETOOTH_FLAG,
					BlueToothSearchStatus.MANUALCLOSESEARCHBLUETOOTH_FLAG_MESSAGE);
	}

}
