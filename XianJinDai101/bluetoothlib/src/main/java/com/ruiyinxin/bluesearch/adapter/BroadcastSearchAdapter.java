package com.ruiyinxin.bluesearch.adapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.ruiyinxin.bluesearch.adapter.BlueToothSearchCommonAdapter.BlueToothSearchCommonListener;
import com.ruiyinxin.bluesearch.bean.BlueToothSearchStatus;

/**
 * 蓝牙设备搜索适配器
 * 
 * @author Administrator
 * 
 */
public class BroadcastSearchAdapter extends BaseSearchAdapter {
	private BlueToothSearchCommonListener blueToothSearchCommonListener;
	private BluetoothAdapter bluetoothAdapter;
	private Context context;

	/**
	 * 蓝牙适配器实例构建
	 * 
	 * @param mybuleToothSearchListener
	 *            回调监听
	 */
	public BroadcastSearchAdapter(Context context,
			BlueToothSearchCommonListener mybuleToothSearchListener) {
		this.blueToothSearchCommonListener = mybuleToothSearchListener;
		this.context = context;
		initBlueToothSearchAdapter();
	}

	private void initBlueToothSearchAdapter() {
		// 检查设备是否支持蓝牙
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			// 不支持蓝牙功能
			this.blueToothSearchCommonListener.searchStateListener(
					BlueToothSearchStatus.NONSUPPORTBLUETOOTH_FLAG,
					BlueToothSearchStatus.NONSUPPORTBLUETOOTH_MESSAGE);
			return;
		}
		// 打开蓝牙
		if (!bluetoothAdapter.isEnabled()) {
			// Intent intent = new
			// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// // 设置蓝牙可见性，最多300秒
			// intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
			// timeout);
			// context.startActivity(intent);
			// 强制开启蓝牙功能
			bluetoothAdapter.enable();
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.setPriority(Integer.MAX_VALUE);
		context.registerReceiver(searchBlueToothReceiver, intentFilter);
		this.blueToothSearchCommonListener.searchStateListener(
				BlueToothSearchStatus.STARTBLUETOOTH_FLAG,
				BlueToothSearchStatus.STARTBLUETOOTH_MESSAGE);
	}

	/**
	 * 释放资源
	 */
	public void releaseResoure() {
		try {
			if (bluetoothAdapter != null) {
				bluetoothAdapter.cancelDiscovery();
				if (bluetoothAdapter.isEnabled()) {
					bluetoothAdapter.disable();
				}
			}
			if(searchBlueToothReceiver!=null){
				context.unregisterReceiver(searchBlueToothReceiver);
				searchBlueToothReceiver=null;
			}
			
		} catch (Exception e) {
		}
		this.blueToothSearchCommonListener.searchStateListener(
				BlueToothSearchStatus.RELEASEBLUETOOTHRESOURE_FLAG,
				BlueToothSearchStatus.RELEASEBLUETOOTHRESOURE_MESSAGE);
	}

	/**
	 * 搜索到设备过滤接收广播
	 */
	private BroadcastReceiver searchBlueToothReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				blueToothSearchCommonListener.discoverOneDevice(device);
			}
		}
	};

	/**
	 * 根据开始名称搜索蓝牙
	 */
	@Override
	public void searchBlueDevs() {
		if (bluetoothAdapter == null) {
			// 不支持蓝牙功能
			this.blueToothSearchCommonListener.searchStateListener(
					BlueToothSearchStatus.NONSUPPORTBLUETOOTH_FLAG,
					BlueToothSearchStatus.NONSUPPORTBLUETOOTH_MESSAGE);
		} else {
			// 当前蓝牙被关闭时,强制开启
			if (!bluetoothAdapter.isEnabled()) {
				 Log.d("ryx", "BluetoothScanAdapter_BroadcastSearchAdapter_enable");
				bluetoothAdapter.enable();
				new Thread(new Runnable() {
					@Override
					public void run() {
						 try {
							 //等待蓝牙设备开启成功后进行搜索
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(bluetoothAdapter.isEnabled()){
							blueToothSearchCommonListener.searchStateListener(
									BlueToothSearchStatus.STARTBLUETOOTH_FLAG,
									BlueToothSearchStatus.STARTBLUETOOTH_MESSAGE);
							bluetoothAdapter.startDiscovery();
							blueToothSearchCommonListener.searchStateListener(
									BlueToothSearchStatus.STARTSEARCHBLUETOOTH_FLAG,
									BlueToothSearchStatus.STARTSEARCHBLUETOOTH_MESSAGE);
						}else{
							bluetoothAdapter.enable();
							try {
								//等待蓝牙设备开启成功后进行搜索
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							blueToothSearchCommonListener.searchStateListener(
									BlueToothSearchStatus.STARTBLUETOOTH_FLAG,
									BlueToothSearchStatus.STARTBLUETOOTH_MESSAGE);
							bluetoothAdapter.startDiscovery();
							blueToothSearchCommonListener.searchStateListener(
									BlueToothSearchStatus.STARTSEARCHBLUETOOTH_FLAG,
									BlueToothSearchStatus.STARTSEARCHBLUETOOTH_MESSAGE);
						}
						 Log.d("ryx", "BluetoothScanAdapter_startDiscovery");
					}
				}).start();
			}else{
				Log.d("ryx", "BluetoothScanAdapter_startDiscovery");
				 bluetoothAdapter.startDiscovery();
				 blueToothSearchCommonListener.searchStateListener(
							BlueToothSearchStatus.STARTSEARCHBLUETOOTH_FLAG,
							BlueToothSearchStatus.STARTSEARCHBLUETOOTH_MESSAGE);
			}
			
		}
	}
	@Override
	public void stopBlueToothsearch() {
		if (bluetoothAdapter != null) {
			bluetoothAdapter.cancelDiscovery();
		}
		this.blueToothSearchCommonListener.searchStateListener(
				BlueToothSearchStatus.CLOSESEARCHBLUETOOTH_FLAG,
				BlueToothSearchStatus.CLOSESEARCHBLUETOOTH_MESSAGE);
	}

	@Override
	public void manualstopBlueToothsearch() {
		// TODO Auto-generated method stub
		if (bluetoothAdapter != null) {
			bluetoothAdapter.cancelDiscovery();
		}
		this.blueToothSearchCommonListener.searchStateListener(
				BlueToothSearchStatus.MANUALCLOSESEARCHBLUETOOTH_FLAG,
				BlueToothSearchStatus.MANUALCLOSESEARCHBLUETOOTH_FLAG_MESSAGE);
	}


}
