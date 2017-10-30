package com.ruiyinxin.bluesearch.adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ruiyinxin.bluesearch.listener.BlueToothSearchListener;

/**
 * 蓝牙设备总适配器
 * 
 * @author Administrator
 * 
 */
public class BlueToothSearchCommonAdapter {
	private BlueToothSearchListener buleToothSearchListener;
	private Context context;
	/**
	 * 需要搜索过滤的设备名称数组
	 */
	private String[] deviceStartNames;
	private Timer timer;
	private ArrayList<String> bluetoothDeviceids = new ArrayList<String>();
	/**
	 * 所有搜索到的蓝牙设备
	 */
	private ArrayList<Integer> allbluetoothDeviceids = new ArrayList<Integer>();
	private Handler myHandler = new Handler();
	private BaseSearchAdapter searchAdapter;

	private BlueToothSearchCommonListener blueToothSearchCommonListener;

	/**
	 * 最长毫秒
	 */
	private long timeout = 60*1000;

	public BlueToothSearchCommonAdapter(Context context,
			BlueToothSearchListener mybuleToothSearchListener) {
		this.buleToothSearchListener = mybuleToothSearchListener;
		this.context = context;
		initBlueToothSearchAdapter();
	}

	/**
	 * 蓝牙适配器实例构建
	 * @author timeoutSecond 毫秒数
	 * @param mybuleToothSearchListener
	 *            回调监听
	 */
	public BlueToothSearchCommonAdapter(Context context,
			BlueToothSearchListener mybuleToothSearchListener, long timeoutSecond) {
		this.buleToothSearchListener = mybuleToothSearchListener;
		this.timeout = timeoutSecond;
		this.context = context;
		initBlueToothSearchAdapter();
	}

	/**
	 * 初始化蓝牙搜索
	 */
	private void initBlueToothSearchAdapter() {

		blueToothSearchCommonListener = new BlueToothSearchCommonListener() {

			@Override
			public void discoverOneDevice(final BluetoothDevice buleToothDeviceInfo) {
				allbluetoothDeviceids.add(1);
				Log.d("ryx", "real_discoverOneDevice_name:"+buleToothDeviceInfo.getName()+",address:"+buleToothDeviceInfo.getAddress());
				if(deviceStartNames!=null&&deviceStartNames.length>0){
					for (int i = 0; i < deviceStartNames.length; i++) {
						String deviceStartName = deviceStartNames[i];
						//设备名称为null则直接跳过,继续搜索
						if(TextUtils.isEmpty(buleToothDeviceInfo.getName())){
							break;
						}
						if (buleToothDeviceInfo.getName().startsWith(
										deviceStartName)
								&& !bluetoothDeviceids.contains(buleToothDeviceInfo
										.getAddress())) {
							bluetoothDeviceids
									.add(buleToothDeviceInfo.getAddress());
							myHandler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									buleToothSearchListener
									.discoverOneDevice(buleToothDeviceInfo);
								}
							});
							break;
						}
					}
				} else if (!bluetoothDeviceids.contains(buleToothDeviceInfo
						.getAddress())) {
					bluetoothDeviceids.add(buleToothDeviceInfo.getAddress());
					myHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							buleToothSearchListener
							.discoverOneDevice(buleToothDeviceInfo);
						}
					});
				}
			}

			@Override
			public void searchStateListener(final int stateflag, final String message) {
				
				myHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						buleToothSearchListener.searchStateListener(stateflag, message);
					}
				});
			}
		};

//		if (context.getPackageManager().hasSystemFeature(
//				PackageManager.FEATURE_BLUETOOTH_LE)) {
//			// 4.0及其以上的版本
//			searchAdapter = new BluetoothScanAdapter(context,
//					blueToothSearchCommonListener);
//		} else {
			// 4.0以下的版本
			searchAdapter = new BroadcastSearchAdapter(context,
					blueToothSearchCommonListener);
//		}
	}
	/***
	 * 蓝牙搜索
	 */
	public void searchBlueDevs(final String[] startNames){
		this.deviceStartNames=startNames;
		bluetoothDeviceids.clear();
		// 执行搜索
		searchAdapter.searchBlueDevs();
		if(timer==null){
			timer = new Timer();
		}
		// 设置停止搜索时间
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				myHandler.post(new Runnable() {

					@Override
					public void run() {
						if(timer!=null){
							//停止定时器
							timer.cancel();
							stopBlueToothsearch();
						}
					}
				});
			}
		}, timeout);

		/**
		 * 3秒后检查蓝牙是否搜索正常,如果不正常则进行重新搜索
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				allbluetoothDeviceids.clear();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(allbluetoothDeviceids.size()==0){
					//3秒内没有搜索到蓝牙设备则进行重新搜索
					searchAdapter.manualstopBlueToothsearch();
					// 执行搜索
					searchAdapter.searchBlueDevs();
				}

			}
		}).start();

	}
	/**
	 * 自动停止搜索蓝牙
	 */
	private void stopBlueToothsearch() {
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		searchAdapter.stopBlueToothsearch();
	}
	/**
	 * 手动停止蓝牙搜索
	 */
	public void manualstopBlueToothsearch(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		searchAdapter.manualstopBlueToothsearch();
	}
	
	/**
	 * 释放资源
	 */
	public void releaseResoure() {
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		searchAdapter.releaseResoure();
	}

	/**
	 * 蓝牙Common中中间回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface BlueToothSearchCommonListener {
		/**
		 * 搜索到蓝牙
		 * 
		 * @param buleToothDeviceInfo
		 */
		public void discoverOneDevice(BluetoothDevice buleToothDeviceInfo);

		/**
		 * 搜索状态监听
		 * 
		 */
		public void searchStateListener(int stateflag, String message);
	}
}
