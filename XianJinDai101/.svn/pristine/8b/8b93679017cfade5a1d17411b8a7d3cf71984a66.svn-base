package com.ryx.swiper.devs.whty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.itron.android.ftf.Util;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiper;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.DataUtil;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.whty.bluetoothsdk.util.Utils;
import com.whty.device.utils.GPMethods;
import com.whty.ryxposapi.CommandController;
import com.whty.ryxposapi.CommandReturn;
import com.whty.ryxposapi.CommandStateChangedListener;
import com.whty.ryxposapi.bluetooth.BLECommandController;
import com.whty.ryxposapi.bluetooth.DeviceInfo;
import com.whty.ryxposapi.bluetooth.DeviceSearchListener;

public class WhtyBlueToothAdapter  extends CSwiperAdapter implements CommandStateChangedListener{
	private static final int TRADE_SUCCESS = 30;  //交易成功
	private static final int DOWN_GRADE_TRANSACTION = 31;// 降级交易 
	private static final int CANCEL_CREDIT_CARD = 32; // 取消交易
	private static final int OTHER_ERROR = 33; // 其他错误
	private static final int UNKNOWN_EXCEPTION = 34; //未知异常
	private static final int ICWAIT = 35; //IC卡插入
	private  byte[]  transLogNobyte;
	private  String amount;
	private  String orderid;
	Context context;
	ISwiperStateListener listener;
	 private CommandController controller;
	 private BLECommandController bleController;
	 String strKsn="";
	 String address="";
	 //1表示无键盘  2表示有键盘
	 private int deviceType = -1;
	 private boolean isGetCardSimpleMsg=false;
	 public WhtyBlueToothAdapter(Context context, ISwiperStateListener listener){
		 LogUtil.setLogdebug(true);
		 this.context= context;
		 this.listener=listener;
		 initCSwiper("");
	 }
	 @Override
		public int initCSwiper(String address) {
			// TODO Auto-generated method stub
		 controller = new CommandController(context.getApplicationContext(), this);
		  bleController = BLECommandController.GetInstance(context.getApplicationContext(), null);
			return 0;
		}
	 
	@Override
	public void searchBlueDevs(final IRyxDevSearchListener listener) {
		// TODO Auto-generated method stub
		bleController.searchDevices(new DeviceSearchListener() {
			
			@Override
			public void discoverOneDevice(DeviceInfo deviceInfo) {
				// TODO Auto-generated method stub
				 com.ryx.swiper.beans.DeviceInfo devInfo = new com.ryx.swiper.beans.DeviceInfo();
	                devInfo.identifier = deviceInfo.identifier;
	                devInfo.name = deviceInfo.name;
	                listener.discoverOneDevice(devInfo);
			}
			
			@Override
			public void discoverComplete() {
				// TODO Auto-generated method stub
				listener.discoverComplete();
			}
		});
	}

	@Override
	public int connectCSwiper(final String address) {
		// TODO Auto-generated method stub
		this.address=address;
        int isSuccess = bleController.openDevice(address);
        LogUtil.printInfo("connectCSwiper==isSuccess="+isSuccess);
        if (isSuccess == 0) {  
        	CommandReturn commandReturn=controller.Get_Type();
        	deviceType= commandReturn.Return_DeviceType;
        	listener.onBluetoothConnectSuccess();
        }else{
        	deviceType=-1;
        	listener.onBluetoothConnectFail();
        }
		return 0;
	}

	@Override
	public int getTerminalType() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void startEmvSwiper(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 amount = MapUtil.getString(map, "amount");
		 orderid = MapUtil.getString(map, "order_id");
		 isGetCardSimpleMsg=false;
		 sdkStartEmvSwiper();
	}
	/**
	 * 启动SDK的刷卡
	 */
	private void sdkStartEmvSwiper(){
		LogUtil.printInfo("order_id=="+orderid+",amount=="+amount);
		 new Thread() {
             public void run() {
            	 if(!bleController.isConnected()){
         			bleController.openDevice(address);
         		}
            	 String transLogNo = CryptoUtils.getInstance().getTransLogNo();
            	 CryptoUtils.getInstance().setTransLogUpdate(false);
            	 transLogNobyte=Util.HexToBin(transLogNo);
//            	 byte[]  tys= Utils.hexString2Bytes(transLogNo);
            	 LogUtil.printInfo("transLogNo=="+transLogNo+",transLogNobyte=="+Arrays.toString(transLogNobyte));
            	 Log.e("laomao", "transLogNo=="+transLogNo+",transLogNobyte=="+Arrays.toString(transLogNobyte));
            	 LogUtil.printInfo("amount=="+amount);
                 controller.statEmvSwiper(0, 0, 0, 0, 0, transLogNobyte, amount, null, 60000, null);
             }
         }.start();
	}
	@Override
	public void disConnect() {
		// TODO Auto-generated method stub
		if(bleController.isConnected()){
			 LogUtil.printInfo("disConnect========1111");
	    	 controller.comm_reset();
	    	 LogUtil.printInfo("disConnect========2222");
		}
	}

	@Override
	public void releaseCSwiper() {
		if(bleController.isConnected()){
			 LogUtil.printInfo("releaseCSwiper========11111");
	    	 bleController.closeDevice();
	    	 LogUtil.printInfo("releaseCSwiper========22222");
		}
	}

	@Override
	public String getKsn() {
		// TODO Auto-generated method stub
		return this.strKsn;
	}

	@Override
	public void getKsnSync() {
//		HashMap<String, String> deviceksninfo= controller.getDeviceKSNInfo();
//		LogUtil.printInfo("deviceksninfo=="+deviceksninfo);
		// TODO Auto-generated method stub
		 CommandReturn res = controller.Get_PsamNo();
		 if(res!=null&&res.Return_PSAMNo!=null){
			 this.strKsn= GPMethods.bytesToHexString(res.Return_PSAMNo);
			 LogUtil.printInfo("getKsnSync=="+this.strKsn);
			 listener.onDetected();
		 }else{
			 listener.onDetecteError();
		 }
	}

	@Override
	public boolean isDevicePresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getDeviceType() {
		 //1表示无键盘  2表示有键盘
		return deviceType==2?RyxSwiperCode.DEVICE_TYPE_KEYBOARD_MPOS:RyxSwiperCode.DEVICE_TYPE_NOKEYBOARD_ORDINARY;
	}

	@Override
	public String getCardno() {
//		Map<String, Object> map=new HashMap<String, Object>();
//    	map.put("buss_type", "");
//    	map.put("order_id", "0000000000000000");
//    	map.put("amount", "000000000000");
//    	startEmvSwiper(map);
		isGetCardSimpleMsg=true;
		LogUtil.printInfo("getCardno=="+controller);
		new Thread() {
            public void run() {
            	controller.getCardSimpleMsg();
            }}.start();
		return null;
	}

	@Override
	public void writIc(String resp, String icdata) {
		if("".equals(icdata)){
			listener.onICResponse(0, null, null);
			return;
		}
		// TODO Auto-generated method stub
		String asciiStr=DataUtil.parseAscii(resp);
		int responsecode= controller.ICTradeResponse(asciiStr, icdata);
		if(responsecode==0){
			LogUtil.printInfo("true"+"==resp=="+resp+"asciiStr=="+asciiStr+",icdata=="+icdata);
			listener.onICResponse(1, null, null);
		}else{
			LogUtil.printInfo("false"+"==resp=="+resp+"asciiStr=="+asciiStr+",icdata=="+icdata);
			listener.onICResponse(0, null, null);
		}
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		// TODO Auto-generated method stub
		int resultCode=-1;
		//公钥
		if(flag==0){
			/**
		     * 更新AID或公钥（一组）
		     * 
		     * @param type 0：AID 1（其他）：公钥
		     * @param data AID或公钥的数据
		     * @return 0：成功  -1：失败
		     */
			resultCode=controller.updateAIDorRID(1, data);
		}else{
			//aid
			resultCode=controller.updateAIDorRID(0, data);
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("index", packageNo);
		if(resultCode==0){
//			Log.d("xucc", "updateParam==packageNo=="+packageNo+",success");
			//成功
			listener.onUpdateTerminalParamsCompleted(map);
		}else{
			//失败
//			Log.d("xucc", "updateParam==packageNo=="+packageNo+",falis");
			listener.onUpdateTerminalParamsFailed(map);
		}
		
		return false;
	}
	
	//这里以下是天喻各种回调
	@Override
	public void OnCheckCRCErr() {
		
	}
	@Override
	public void OnConnectErr() {
		listener.onDecodeError("设备连接错误!!");
	}
	@Override
	public void OnDevicePlug() {
		
	}
	@Override
	public void OnDevicePresent() {
		
	}
	@Override
	public void OnDeviceUnPlug() {
		
	}
	@Override
	public void OnGetCardNo(String arg0) {
		
	}
	@Override
	public void OnGetKsn(String arg0) {
		
	}
	@Override
	public void OnKeyError() {
		
	}
	@Override
	public void OnNoAck() {
		
	}
	@Override
	public void OnPrinting() {
		
	}
	@Override
	public void OnReadCardErr(int code) {
//		3.定义了刷卡的错误码
//		private static final int TRADE_SUCCESS = 30;  交易成功
//		private static final int DOWN_GRADE_TRANSACTION = 31; 降级交易 
//		private static final int CANCEL_CREDIT_CARD = 32;  取消交易
//		private static final int OTHER_ERROR = 33;  其他错误
//		private static final int UNKNOWN_EXCEPTION = 34; 未知异常
		String errorString = "";
		switch (code) {
		case TRADE_SUCCESS:
			errorString = "交易成功";
			break;
		case DOWN_GRADE_TRANSACTION:
			errorString = "IC卡请插卡!";
			//降级交易回调
			Map<String,Object> errorMap=new HashMap<String, Object>();
			errorMap.put("demotionTrade", errorString);
			listener.onDemotionTrade(errorMap);
			break;
		case CANCEL_CREDIT_CARD:
			errorString = "取消交易";
			listener.onError(code, errorString);
			break;
		case OTHER_ERROR:
			errorString = "其他错误";
			listener.onError(code, errorString);
			break;
		case UNKNOWN_EXCEPTION:
			errorString = "未知异常";
			listener.onError(code, errorString);
			break;
		case ICWAIT://IC卡
			LogUtil.printInfo("========ICWAIT===");
//			listener.onWaitingCardDataReader();
			listener.EmvOperationWaitiing();	
			//因天喻监听到此状态后交易就截止了，只能再次调用下刷卡
			if(isGetCardSimpleMsg){
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						controller.getCardSimpleMsg();
					}
				}).start();
			}else{
				sdkStartEmvSwiper();
			}
			break;
		}
		
	}
	@Override
	public void OnTimeout() {
		listener.onTimeout();
	}
	@Override
	public void OnWaitingOper() {
		
	}
	@Override
	public void OnWaitingPIN() {
		
	}
	@Override
	public void OnWaitingcard() {
		
	}
	@Override
	public void onDeviceUnPresent() {
		
	}
	/**
	 * 获取完卡号和有效期信息返回
	 */
	@Override
	public void OnReadCardSimpleMsg(CommandReturn res) {
		try {
		if(res != null && res.Return_Result == 0x00){
			LogUtil.printInfo("res.Return_CardNo=="+res.Return_CardNo+",cardexpiryDate="+res.cardexpiryDate);
			 //卡号
	     	 String maskedPAN=res.Return_CardNo;
	     	 //有效期
	       	 final String expiryDate = GPMethods.bytesToHexString(res.cardexpiryDate).substring(0,4);
	       	 //回调获取卡号成功
	       	 listener.onGetCardNoCompleted(maskedPAN,expiryDate);
		}else{
			LogUtil.printInfo("Return_Result=="+res.Return_Result);
			listener.onDecodeError("获取卡号信息失败");
		}
		} catch (Exception e) {
			listener.onDecodeError("刷卡数据异常,请重新操作!");
		}
	}
	@Override
	public void OnReadCard(CommandReturn res) {
		try {
			HashMap<String, String> hashMap=controller.getDeviceIdentifyInfo();
			String isonStr=controller.getIso8583Feild117 ();
			LogUtil.printInfo("hashMap=="+hashMap+",isonStr=="+isonStr);
		LogUtil.printInfo("deviceType=="+deviceType);
		//CommandReturn.CardType = 2 的时候表示卡片类型为非接卡CommandReturn.CardType = 1 表示IC卡CommandReturn.CardType = 0 表示磁条卡
		LogUtil.printInfo("WhtyCardType=="+res.CardType);
		if (res != null && res.Return_Result == 0x00) {
			 //卡号
          	 String maskedPAN=res.Return_CardNo;
           	 //有效期
           	 final String expiryDate = GPMethods.bytesToHexString(res.cardexpiryDate).substring(0,4);
//           	 //回调获取卡号成功
//           	 listener.onGetCardNoCompleted(maskedPAN,expiryDate);
             //如果ksn为空则进行返回
//         	 if(TextUtils.isEmpty(strKsn)){
//         		LogUtil.printInfo("strKsn=="+strKsn);
//         		 return;
//         	 }
             //二磁
            String track2= GPMethods.bytesToHexString(res.Return_Track2);
            //二磁长度
          	 int track2Length=track2==null?0:track2.length()/2;
	         //获取随机数
	      	 String getmacRandomNo=GPMethods.bytesToHexString(res.Return_PSAMRandom);
	      	 LogUtil.printInfo("getmacRandomNo=="+getmacRandomNo+",strKsn=="+strKsn);
      	 //------------------------------------------设备类型和卡类型判断--------------------------------------------------------
			//设备类型判断
			if(deviceType==1){
				//无密码键盘设备
				if (res.CardType == 0) {
					//磁条卡
		           	 //三磁
		           	 String track3= GPMethods.bytesToHexString(res.Return_Track3);
		           	 //三磁长度
		           	 int track3Length=track3==null?0:track3.length()/2;
		           	 final String encTracks = track2 + track3;
		           	 byte[] ic55Data = null;// 磁条卡没有55数据域
		           	 //Mac入参
		           	 String hexString = DataUtil.genMacHexString( track2, track3, getmacRandomNo, strKsn, orderid);
		           	 LogUtil.printInfo("Mac入参=="+ hexString);
		           	 //获取Mac
		           	 CommandReturn macRes = 
		                        controller.Get_MAC(0, 0, transLogNobyte, Utils.hexString2Bytes(hexString));
		           	String cardMAC= GPMethods.bytesToHexString(macRes.Return_PSAMMAC);
		           	LogUtil.printInfo("元数据====mTrack2=" + track2 + ",mTrack3=" + track3 + ",Mac入参=="+hexString+",cardMAC=="+cardMAC);
		           	Map<String, Object> resultmapp = DataUtil.putCardServerData("10", RyxSwiperCode.DEVICE_TY_BULETOOTH, cardMAC, strKsn, 0, track2Length, track3Length, getmacRandomNo, maskedPAN, encTracks, expiryDate.substring(0, 4), "", ic55Data);
		           	 listener.onDecodeCompleted(resultmapp);
            } else {
            	  //IC卡包括插卡和非接卡
		           	 //三磁
		           	 String track3= null;
		           	 //三磁长度
		           	 int track3Length=0;
		           	 final String encTracks = track2 ;
		           	 //卡的序列号
		           	String panSerial= GPMethods.bytesToHexString(res.CardSerial);
		           	 byte[] ic55Data =res.EmvDataInfo;
		           	 //Mac入参
		           	 String hexString = DataUtil.genMacHexString( track2, track3, getmacRandomNo, strKsn, orderid);
		           	LogUtil.printInfo( "Whty===track2="+track2+",CardNo="+maskedPAN+",track2="+track2+",expiryDate="+expiryDate);
		           	 //获取Mac
		           	 CommandReturn macRes = 
		                        controller.Get_MAC(0, 0, transLogNobyte, Utils.hexString2Bytes(hexString));
		           	String cardMAC= GPMethods.bytesToHexString(macRes.Return_PSAMMAC);
		           	 Map<String, Object> resultmapp = DataUtil.putCardServerData("11", RyxSwiperCode.DEVICE_TY_BULETOOTH, cardMAC, strKsn, 0, track2Length, track3Length, getmacRandomNo, maskedPAN, encTracks, expiryDate, panSerial, ic55Data);
		           	 listener.onDecodeCompleted(resultmapp);
            }}else if(deviceType==2){
            	//有密码键盘
            	if (res.CardType == 0) {
            		//磁条卡
	                  	 //三磁
	                  	 String track3= GPMethods.bytesToHexString(res.Return_Track3);
	                  	 //三磁长度
	                  	 int track3Length=track3==null?0:track3.length()/2;
	                  	 final String encTracks = track2 + track3;
	                  	 byte[] ic55Data = null;// 磁条卡没有55数据域
	                  	String psampin=GPMethods.bytesToHexString(res.Return_PSAMPIN);
	                    String termId= 	strKsn.substring(0, 20);
	                    String pasamId=strKsn.substring(20);
	                    String panHex=DataUtil.genHexString(maskedPAN);
	                  	 //Mac入参 MAB组成：32字节订单号+卡号长度+卡号密文+二三磁道及磁道加密随机数长度+二三磁道密文+磁道加密随机数+PIN密文+PIN加密随机数+PSAM+终端号
	                    //String hexString = DataUtil.genMacHexString( track2, track3, getmacRandomNo, strKsn, orderid);
					LogUtil.printInfo("panHex=="+panHex+",getmacRandomNo="+getmacRandomNo+",track2Length="+track2Length+",track3Length="+track3Length+",getmacRandomNo="+getmacRandomNo.length()/2);
	                  	 String hexString=DataUtil.getPinMacHexString(orderid,DataUtil.genHexStr(maskedPAN.length()), panHex,Integer.toHexString(track2Length+track3Length+getmacRandomNo.length()/2) , track2+track3, getmacRandomNo, psampin, pasamId, termId);
	                  	LogUtil.printInfo("Mac入参=="+ hexString);
	                  	 //获取Mac
	                  	 CommandReturn macRes = 
	                               controller.Get_MAC(0, 0, transLogNobyte, Utils.hexString2Bytes(hexString));
	                  	String cardMAC= GPMethods.bytesToHexString(macRes.Return_PSAMMAC);
	                  	LogUtil.printInfo("元数据====mTrack2=" + track2 + ",mTrack3=" + track3 + ",Mac入参=="+hexString+",cardMAC=="+cardMAC);
	                  	Map<String, Object> resultmapp =DataUtil.putPwdDeviceCardServerData("10", TextUtils.isEmpty(psampin)?"":psampin, RyxSwiperCode.DEVICE_TY_BULETOOTH, cardMAC,pasamId, termId, DataUtil.genHexString(maskedPAN), encTracks+getmacRandomNo, "", ic55Data);
	                  	listener.onDecodeCompleted(resultmapp);
                   } else {
                	   //IC卡包括插卡和非接卡
                  	 //三磁长度
                  	 int track3Length=0;
                  	 final String encTracks = track2 ;
                  	 //卡的序列号
                  	String panSerial= GPMethods.bytesToHexString(res.CardSerial);
                  	 byte[] ic55Data =res.EmvDataInfo;
                  	 String panHex=DataUtil.genHexString(maskedPAN);
                  	 String psampin=GPMethods.bytesToHexString(res.Return_PSAMPIN);
                  	 String termId=strKsn.substring(0, 20);
                  	 String pasamId=strKsn.substring(20);
                  	 //Mac入参
                  	 String hexString=DataUtil.getPinMacHexString(orderid, DataUtil.genHexStr(maskedPAN.length()), panHex, Integer.toHexString(track2Length+track3Length+getmacRandomNo.length()/2) , track2, getmacRandomNo, psampin, pasamId, termId);
                  	LogUtil.printInfo("Mac入参=="+ hexString);
                  	 LogUtil.printInfo( "Whty===track2="+track2+",CardNo="+maskedPAN+",track2="+track2+",expiryDate="+expiryDate);
                  	 //获取Mac
                  	 CommandReturn macRes = 
                               controller.Get_MAC(0, 0, transLogNobyte, Utils.hexString2Bytes(hexString));
                  	String cardMAC= GPMethods.bytesToHexString(macRes.Return_PSAMMAC);
                  	String mycardType="";
                  	if(res.CardType==1){
                  		mycardType="11";
                  	}else if(res.CardType==2){
//                  		20：标准PBOC，21：快速QPBOC
                  		//天喻MPOS设备都是快速QPBC
                  		mycardType="21";
                  	}
                  	Map<String, Object> resultmapp =DataUtil.putPwdDeviceCardServerData(mycardType, TextUtils.isEmpty(psampin)?"":psampin, RyxSwiperCode.DEVICE_TY_BULETOOTH, cardMAC, pasamId,termId, DataUtil.genHexString(maskedPAN), encTracks+getmacRandomNo, panSerial, ic55Data);
                  	 listener.onDecodeCompleted(resultmapp);
                   }
            }else{

            	listener.onDecodeError("蓝牙设备类型有误!");
            }
        } else {
       	 	listener.onDecodeError("刷卡操作失败");
        }
	} catch (Exception e) {
			e.printStackTrace();
			LogUtil.printInfo("error=="+e.getLocalizedMessage());
		listener.onDecodeError("刷卡数据异常,请重新操作!");
	}}
 //以上是天喻各种回调
	
	
}
