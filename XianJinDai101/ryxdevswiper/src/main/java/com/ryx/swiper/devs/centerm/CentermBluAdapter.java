package com.ryx.swiper.devs.centerm;

import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.text.TextUtils;

import com.centerm.mpos.bluetooth.BLECommandController;
import com.centerm.mpos.bluetooth.BluetoothStateListener;
import com.centerm.mpos.command.CommandController;
import com.centerm.mpos.command.CommandStateChangedListener;
import com.centerm.mpos.exception.MPOSException;
import com.centerm.mpos.model.CardType;
import com.centerm.mpos.model.CommandReturn;
import com.centerm.mpos.util.Logger;
import com.centerm.mpos.util.StringUtil;
import com.itron.android.ftf.Util;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.DataUtil;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.whty.bluetoothsdk.util.Utils;
import com.whty.device.utils.GPMethods;

/**
 * 升腾蓝牙C821E设备
 * @author XCC
 *
 */
public class CentermBluAdapter extends CSwiperAdapter implements CommandStateChangedListener{
	Context context;
	ISwiperStateListener listener;
	 private CommandController controller;
	 private BLECommandController bleController;
	 String address="";
	 String strKsn="";
	 private  byte[]  transLogNobyte;
	 private  String amount;
	 private  String orderid;
	 private int swiperflag=0;
	public CentermBluAdapter(Context context, ISwiperStateListener listener){
		Logger.setLogDebug(false);
		 this.context= context;
		 this.listener=listener;
		 initCSwiper("");
	}
	
	@Override
	public void searchBlueDevs(IRyxDevSearchListener listener) {
	}

	@Override
	public int connectCSwiper(String address) {
		this.address=address;
		 //连接操作
	bleController.openDevice(this.address, new BluetoothStateListener() {
			
			@Override
			public void onStateChange(int state) {
			}
		});
		return 0;
	}

	@Override
	public int getTerminalType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int initCSwiper(String address) {
		  controller = new CommandController(this.context, this);
		bleController = BLECommandController.getInstance(this.context, controller);
			return 0;
	}
	@Override
	public void startEmvSwiper(Map<String, Object> map) {
		 amount = MapUtil.getString(map, "amount");
		 orderid = MapUtil.getString(map, "order_id");
		 sdkStartEmvSwiper();
	}
/**
 * 调取sdk执行刷卡操作
 */
public void sdkStartEmvSwiper(){
	swiperflag=0;
	String transLogNo = CryptoUtils.getInstance().getTransLogNo();
	CryptoUtils.getInstance().setTransLogUpdate(false);
	transLogNobyte=Util.HexToBin(transLogNo);
    try {
		controller.statEmvSwiper(0, 0, 0, 0, 0, transLogNobyte, amount, null, 60000, null);
	} catch (MPOSException e) {
		e.printStackTrace();
		listener.onDecodeError("刷卡失败,请重新操作!");
	}
}
	@Override
	public void disConnect() {
		// TODO Auto-generated method stub
		LogUtil.printInfo("===================disConnect==========================");
	}

	@Override
	public void releaseCSwiper() {
		controller.releaseAll();
	}

	@Override
	public String getKsn() {
		// TODO Auto-generated method stub
		  LogUtil.printInfo("==============getKsn=====================");
		return this.strKsn;
	}

	@Override
	public void getKsnSync() {
		   try {
	            controller.get_Ksn();
	        } catch (MPOSException e) {
	            e.printStackTrace();
	            LogUtil.printInfo("error=="+e.getLocalizedMessage());
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
		// TODO Auto-generated method stub
		return RyxSwiperCode.DEVICE_TYPE_KEYBOARD_MPOS;
	}

	@Override
	public String getCardno() {
		swiperflag=1;
			controller.getCardNoExpiryDate();
		return null;
	}

	@Override
	public void writIc(String resp, String icdata) {
		try {
		if("".equals(icdata)){
			listener.onICResponse(0, null, null);
			return;
		}
		// TODO Auto-generated method stub
//		String asciiStr=DataUtil.parseAscii(resp);
		int responsecode= controller.secondIssuance(resp,icdata );
		if(responsecode==1){
			LogUtil.printInfo("true"+"==resp=="+"icdata=="+icdata);
			listener.onICResponse(1, null, null);
		}else{
			LogUtil.printInfo("false"+"==resp=="+resp+",icdata=="+icdata);
			listener.onICResponse(0, null, null);
		}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.printInfo("false"+"==resp=="+resp+",icdata=="+icdata+",error="+e.getLocalizedMessage());
			listener.onICResponse(0, null, null);
		}
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("index", packageNo);
        try {
         boolean issuccess=  controller.updateTerminalParameters(flag, packageNo, data);
		if(issuccess){
			//成功
			listener.onUpdateTerminalParamsCompleted(map);
		}else{
			//失败
			listener.onUpdateTerminalParamsFailed(map);
		}
        } catch (Exception e) {
            e.printStackTrace();
            listener.onUpdateTerminalParamsFailed(map);
        }
		return false;
	}


	@Override
	public void OnReadCard(CommandReturn res) {
		LogUtil.printInfo("=======OnReadCard===44444444444==="+res.getCardType());
		try {
		//升腾C821E设备
		if( CommandController.device_type.equals(StringUtil.byte2HexStr(res.getReturn_DeviceType()))){
			String maskedPAN=	res.getReturn_CardNo();
			 //二磁
            String track2= StringUtil.byte2HexStr(res.getReturn_Track2());
            //二磁长度
          	 int track2Length=track2==null?0:track2.length()/2;
	         //获取随机数
	      	 String getmacRandomNo=StringUtil.byte2HexStr(res.getReturn_PSAMRandom());
			
	        if (res.getCardType() == CardType.NORMAL_CARD) {
	        	//"磁条卡";
	        	//三磁
             	 String track3= StringUtil.byte2HexStr(res.getReturn_Track3());
             	 //三磁长度
             	 int track3Length=track3==null?0:track3.length()/2;
             	 final String encTracks = track2 + track3;
             	 byte[] ic55Data = null;// 磁条卡没有55数据域
             	String psampin=StringUtil.byte2HexStr(res.getReturn_PSAMPIN());
               String termId= 	strKsn.substring(0, 20);
               String pasamId=strKsn.substring(20);
               String panHex=DataUtil.genHexString(maskedPAN);
               String hexString=DataUtil.getPinMacHexString(orderid, DataUtil.genHexStr(maskedPAN.length()), panHex,Integer.toHexString(track2Length+track3Length+getmacRandomNo.length()/2) , track2+track3, getmacRandomNo, psampin, pasamId, termId);
             	LogUtil.printInfo("Mac入参=="+ hexString);
           	 //获取Mac
             	 CommandReturn commandReturn = controller.get_MAC(0, 0, transLogNobyte, Utils.hexString2Bytes(hexString));
             	String cardMAC= StringUtil.byte2HexStr(commandReturn.getReturn_PSAMMAC());
            	Map<String, Object> resultmapp =DataUtil.putPwdDeviceCardServerData("10", TextUtils.isEmpty(psampin)?"":psampin, RyxSwiperCode.DEVICE_CENTERM_BULETOOTH, cardMAC,pasamId, termId, DataUtil.genHexString(maskedPAN), encTracks+getmacRandomNo, "", ic55Data);
              	listener.onDecodeCompleted(resultmapp);
	        } else if (res.getCardType() == CardType.IC_CARD|| res.getCardType() == CardType.NFC_CARD){
	           //"IC卡||NFC";
	        	
	        	 //IC卡包括插卡和非接卡
             	 //三磁长度
             	 int track3Length=0;
             	 final String encTracks = track2 ;
             	 //卡的序列号
             	String panSerial= StringUtil.byte2HexStr(res.getCardSerial());
             	 byte[] ic55Data =res.getEmvDataInfo();
             	 String panHex=DataUtil.genHexString(maskedPAN);
             	 String psampin= StringUtil.byte2HexStr(res.getReturn_PSAMPIN());
             	 String termId=strKsn.substring(0, 20);
             	 String pasamId=strKsn.substring(20);
             	 //Mac入参
             	 String hexString=DataUtil.getPinMacHexString(orderid, DataUtil.genHexStr(maskedPAN.length()), panHex, Integer.toHexString(track2Length+track3Length+getmacRandomNo.length()/2) , track2, getmacRandomNo, psampin, pasamId, termId);
             	LogUtil.printInfo("Mac入参=="+ hexString);
             	 //获取Mac
             	 CommandReturn commandReturn = controller.get_MAC(0, 0, transLogNobyte, Utils.hexString2Bytes(hexString));
             	String cardMAC= StringUtil.byte2HexStr(commandReturn.getReturn_PSAMMAC());
             	
             	String mycardType="";
             	if(res.getCardType() == CardType.IC_CARD){
             		mycardType="11";
             	}else if(res.getCardType() == CardType.NFC_CARD){
//             		20：标准PBOC，21：快速QPBOC
             		//天喻MPOS设备都是快速QPBC
             		mycardType="21";
             	}
             	Map<String, Object> resultmapp =DataUtil.putPwdDeviceCardServerData(mycardType, TextUtils.isEmpty(psampin)?"":psampin, RyxSwiperCode.DEVICE_CENTERM_BULETOOTH, cardMAC, pasamId,termId, DataUtil.genHexString(maskedPAN), encTracks+getmacRandomNo, panSerial, ic55Data);
             	 listener.onDecodeCompleted(resultmapp);
	        }
		}else{
			listener.onDecodeError("设备类型有误,当前设备暂不支持!");
		}
		
		} catch (Exception e) {
			LogUtil.printInfo("errMsg="+e.getLocalizedMessage());
			listener.onDecodeError("刷卡操作失败");
		}
	}
	@Override
	public void onWaitingcard() {
		// TODO Auto-generated method stub
		LogUtil.printInfo("===================onWaitingcard==========================");
	}

	@Override
	public void onIcinsert() {
		// TODO Auto-generated method stub
		listener.EmvOperationWaitiing();	
	}

	@Override
	public void onNFCinsert() {
		// TODO Auto-generated method stub
		listener.EmvOperationWaitiing();	
	}

//	@Override
//	public void onDemotionTrade() {
//		// TODO Auto-generated method stub
//		//降级交易回调
//		Map<String,Object> errorMap=new HashMap<String, Object>();
//		errorMap.put("demotionTrade", "IC卡请插卡!");
//		listener.onDemotionTrade(errorMap);
//	}

	@Override
	public void onTimeout() {
		listener.onTimeout();
	}

	@Override
	public void onConnectSuccess() {
		listener.onBluetoothConnectSuccess();
	}

	@Override
	public void onConnectErr() {
		listener.onBluetoothConnectFail();
	}

	@Override
	public void onGetKsn(String ksn) {
		// TODO Auto-generated method stub
		this.strKsn=ksn;
		 listener.onDetected();
	}

	@Override
	public void OnGetCardNoExpiryDate(String CardNo, String expiryDate) {
		// TODO Auto-generated method stub
		 //回调获取卡号成功
      	 listener.onGetCardNoCompleted(CardNo,expiryDate);
	}

	@Override
	public void onGetCardNoExpiryDate(String CardNo, String cardSerial,
			String expiryDate) {
		// TODO Auto-generated method stub
		 listener.onGetCardNoCompleted(CardNo,expiryDate);
	}

	@Override
	public void onAgainSwiper() {
		// TODO Auto-generated method stub
		listener.onError(0x0001, "请重新刷卡或插卡!");
		if(swiperflag==0){
			sdkStartEmvSwiper();
		}else{
			getCardno();
		}
	}

	

	@Override
	public void onICAfterInputPin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int code, String msg) {
		// TODO Auto-generated method stub
		listener.onError(code, msg);
	}

	@Override
	public void onCancleStatEmvSwiper() {
		// TODO Auto-generated method stub
//		listener.onError(0x00012, "取消交易");
		listener.onCancelSwiper();
	}

	@Override
	public void onPinEnterDetected(int code) {
		//0:输密成功1：空密码2：取消输密
		if(code==2){
			listener.onCancelSwiper();
		}
	}

}
