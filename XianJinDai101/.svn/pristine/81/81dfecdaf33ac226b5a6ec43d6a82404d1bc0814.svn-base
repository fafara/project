package com.ryx.swiper.utils;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.itron.android.ftf.Util;
import com.whty.device.utils.GPMethods;

/**
 * Created by xuchenchen on 2015/12/17.
 */
public class DataUtil {
	
	public static Map<String,Object> putPwdDeviceCardServerData(String cardType,String psampin, int devType, String cardMAC,String pasamId, String termId,   String maskedPAN, String encTracks,  String cardSeriNo, byte[] ic55Data){
//		maskedPAN="FA887B2618E2DD185CA454D60067E7BD689C5D8CF6E39D30D6C5883E";
//		encTracks="3E1319943422B388DA6C9954497C1ED6B6498DE0B355E3A36BD67E9D3814841E0E64DDE159F312756258E031900D81F45E9C8B38AC308631235C631E3D6E0D07CED87AAF00E778501732754562FE7D8C84373529";
		
		LogUtil.printInfo("putPwdDeviceCardServerData===="+"cardType="+cardType+",psampin="+ psampin+",devType="+ devType+",cardMAC="+ cardMAC+",pasamId="+ pasamId+",termId="+termId+",maskedPAN="+ maskedPAN+",encTracks="+ encTracks+",cardSeriNo="+ cardSeriNo+","+ ic55Data);
		Map<String, Object> map = new HashMap<String, Object>();
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

		int len = 2 + 1 + 1 + maskedPAN.length()/2
				+ 1 + encTracks.length()/2 + 1
				+ psampin.length()/2 + 1
				+ pasamId.length()/2 + 1
				+ termId.length()/2 + 1
				+ cardMAC.length()/2;
//		int len = 2 + 1 + 1 + cmresult.Return_ENCCardNo.length
//				+ 1 + cmresult.Return_PSAMTrack.length + 1
//				+ cmresult.Return_PSAMPIN.length + 1
//				+ cmresult.Return_PSAMNo.length + 1
//				+ cmresult.Return_TerSerialNo.length + 1
//				+ cmresult.Return_PSAMMAC.length;
		byte[] data = new byte[len];

		data[0] = (byte) ((len - 2) / 256);
		data[1] = (byte) ((len - 2) & 0x000000FF);

		// jics todo
		// data[2] = (byte) ((len - 2) & 0x000000FF);

		data[2] = 0x1F;

		// 卡号长度
		data[3] = (byte) (maskedPAN.length()/2);
		// 卡号密文
		System.arraycopy(GPMethods.str2bytes(maskedPAN), 0, data, 4,
				maskedPAN.length()/2);
		data[4 + maskedPAN.length()/2] = (byte) (encTracks.length()/2);
		// 二三磁道及磁道加密随机数长度maskedPAN.length()/2maskedPAN.length()] = (byte) (encTracks.length()/2);
		// 磁道加密数据
		System.arraycopy(GPMethods.str2bytes(encTracks), 0, data,
				4 + maskedPAN.length()/2 + 1,
				encTracks.length()/2);
		// PIN密文长度及PIN随机数长度
		data[4 + maskedPAN.length()/2 + 1
				+ encTracks.length()/2] = (byte) (psampin.length()/2);
		if(!TextUtils.isEmpty(psampin)){
			// PIN密文及PIN随机数
			System.arraycopy(GPMethods.str2bytes(psampin), 0, data, 4
					+ maskedPAN.length()/2 + 1
					+ encTracks.length()/2 + 1,
					psampin.length()/2);
		}
		// PSAM长度
		data[4 + maskedPAN.length()/2 + 1
				+ encTracks.length()/2 + 1
				+ psampin.length()/2] = (byte) (pasamId.length()/2);
		// PSAM号
		System.arraycopy(GPMethods.str2bytes(pasamId), 0, data, 4
				+ maskedPAN.length()/2 + 1
				+ encTracks.length()/2 + 1
				+ psampin.length()/2 + 1,
				pasamId.length()/2);
		// 终端长度
		data[4 + maskedPAN.length()/2 + 1
				+ encTracks.length()/2 + 1
				+ psampin.length()/2 + 1
				+ pasamId.length()/2] = (byte) (termId.length()/2);
		// 终端号
		System.arraycopy(GPMethods.str2bytes(termId), 0, data,
				4 + maskedPAN.length()/2 + 1
						+ encTracks.length()/2 + 1
						+ psampin.length()/2 + 1
						+ pasamId.length()/2 + 1,
				termId.length()/2);
		// MAC长度
		data[4 + maskedPAN.length()/2 + 1
				+ encTracks.length()/2 + 1
				+ psampin.length() /2+ 1
				+ pasamId.length() /2+ 1
				+ termId.length()/2] = (byte) (cardMAC.length()/2);
		
		// MAC
		System.arraycopy(GPMethods.str2bytes(cardMAC), 0, data, 4
				+ maskedPAN.length()/2 + 1
				+ encTracks.length()/2 + 1
				+ psampin.length()/2 + 1
				+ pasamId.length()/2 + 1
				+ termId.length()/2 + 1,
				cardMAC.length()/2);
		
		String cardnum = "FE00" + Util.BinToHex(data, 0, data.length);
		map.put("card_info", cardnum);
		System.out.println("==="+cardnum);
		String ic55DataStr = "";

//		if (cardSeriNo != null) {
//			cardSeriNoStr = Util.BinToHex(cardSeriNo,
//					0, cardSeriNo.length());
//		}

		if (ic55Data != null) {
			ic55DataStr = Util.BinToHex(ic55Data,
					0, ic55Data.length);
		}
		LogUtil.printInfo("55=="+ic55DataStr);
		if (ic55DataStr.length()> 10) {
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
		map.put("card_type", cardType);

		while (cardSeriNo.length() < 3
				&& cardSeriNo.length() > 0)
			cardSeriNo = "0" + cardSeriNo;

		map.put("ic_data", ic55DataStr);

		map.put("icsernum", cardSeriNo);
		map.put("dev_type",devType);
		map.put("pasamId",pasamId);
		map.put("has_pin", "1");
		String carNoStr=GPMethods.hexStr2Str(maskedPAN);
		map.put("card_no", carNoStr);
		return map;
		
	}
	
	public static Map<String, Object> putCardServerData(String cardType, int devType, String cardMAC, String ksn, int track1Length, int track2Length, int track3Length, String randomNumber, String maskedPAN, String encTracks, String expiryDate, String cardSeriNo, byte[] ic55Data) {
		LogUtil.printInfo("putCardServerData===="+"cardType="+cardType+",devType="+ devType+",cardMAC="+ cardMAC+",ksn="+ ksn+",randomNumber="+ randomNumber+",maskedPAN="+maskedPAN+",encTracks="+ encTracks+",expiryDate="+ expiryDate+",cardSeriNo="+ cardSeriNo+","+ ic55Data);
		// 计算MAC：
		// 磁道信息+随机数+终端号+PASMID+订单号
		//
		// 磁道信息：二磁+三磁 （加密后）
		// 随机数：厂家4字节+FF+瑞银信3字节
		// 终端号：20位长度
		// PASMID:16位长度
		// 订单号：APP调用传入
		Map<String, Object> map = new HashMap<String, Object>();
		int offset = 2;
		LogUtil.printInfo("cardMAC==" + cardMAC + ",=ksn=" + ksn);
		int len = 8 + encTracks.length() / 2 + randomNumber.length() / 2 + ksn.length() / 2 + maskedPAN.length() + 4 + cardMAC.length() / 2;
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

		System.arraycopy(CryptoUtils.getInstance().bytesToHex(encTracks.getBytes()), 0, data, offset + 6, encTracks.length() / 2);
		// 随机数
		System.arraycopy(CryptoUtils.getInstance().bytesToHex(randomNumber.getBytes()), 0, data, offset + 6 + encTracks.length() / 2, randomNumber.length() / 2);
		// 终端序号
		System.arraycopy(CryptoUtils.getInstance().bytesToHex(ksn.getBytes()), 0, data, offset + 6 + encTracks.length() / 2 + randomNumber.length() / 2, ksn.length() / 2);
		// 卡号
		System.arraycopy(maskedPAN.getBytes(), 0, data, offset + 6 + encTracks.length() / 2 + randomNumber.length() / 2 + ksn.length() / 2, maskedPAN.length());
		// 卡有效期(4)
		System.arraycopy(expiryDate.getBytes(), 0, data, offset + 6 + encTracks.length() / 2 + randomNumber.length() / 2 + ksn.length() / 2 + maskedPAN.length(), 4);
		// MAC
		System.arraycopy(CryptoUtils.getInstance().bytesToHex(cardMAC.getBytes()), 0, data, offset + 6 + encTracks.length() / 2 + randomNumber.length() / 2 + ksn.length() / 2 + maskedPAN.length() + 4, cardMAC.length() / 2);

		// show id
		LogUtil.printInfo("maskedPAN=" + maskedPAN);

		if (!maskedPAN.contains("*")) {

			map.put("card_no", maskedPAN);
		}

		LogUtil.printInfo(Util.BinToHex(data, 0, data.length));

		String cardinfo = "FF00" + Util.BinToHex(data, 0, data.length);
		map.put("card_info", cardinfo);
		String cardSeriNoStr = "", ic55DataStr = "";
 
		if (ic55Data != null) {
			ic55DataStr = Util.BinToHex(ic55Data, 0, ic55Data.length);
		}
		LogUtil.printInfo( "IC55域==="+ic55DataStr);

		if (ic55DataStr.length() > 10) {
			int pos = 0;
			pos = ic55DataStr.indexOf("9F41");
			if (pos > 0) {

				int lenth;
				lenth = Integer.parseInt(ic55DataStr.substring(pos + 5, pos + 6));

				ic55DataStr = ic55DataStr.substring(0, pos + 6 + (lenth * 2));
			}

		}

		map.put("card_type", cardType);

		while (cardSeriNo.length() < 3 && cardSeriNo.length() > 0)
			cardSeriNo = "0" + cardSeriNo;

		map.put("ic_data", ic55DataStr);
		map.put("pasamId",ksn.substring(20));
		map.put("icsernum", cardSeriNo);
		map.put("dev_type", devType);
		LogUtil.printInfo(map.toString());
		return map;
	}

	/**
	 * 计算出MAC算法所需的HexString参数
	 * 
	 * @param mTrack2
	 * @param mTrack3
	 * @param randomNumber
	 * @param termId
	 * @param psamId
	 * @param orderId
	 * @return
	 */
	public static String genMacHexString(String mTrack2, String mTrack3, String randomNumber, String termId, String psamId, String orderId) {
		return genMacHexString(mTrack2, mTrack3, randomNumber, termId + psamId, orderId);
	}

	public static String genMacHexString(String mTrack2, String mTrack3, String randomNumber, String ksn, String orderId) {
		String mTrackDatas = mTrack2;
		if (!StringUtils.isBlank(mTrack3)) {
			mTrackDatas += mTrack3;
		}
		return genMacHexString(mTrackDatas, randomNumber, ksn, orderId);
	}

	public static String genMacHexString(String mTrackDatas, String randomNumber, String ksn, String orderId) {
		return genMacHexString(mTrackDatas+ randomNumber, ksn, orderId);
	}

	public static String genMacHexString(String trackAndRandom, String ksn, String orderId) {
		// 磁道密文+磁道加密中由终端生成的随机数+20字节终端号+16字节psam号+32字节订单号,最后总长度为16的倍数
				LogUtil.printInfo("mTrackDatas=" + trackAndRandom + "ksn=" + ksn + "orderId=" + orderId);
				StringBuffer sb = new StringBuffer();
				sb.append(trackAndRandom);
				sb.append(ksn);
				sb.append(genHexString(orderId));
				// 长度必须为16的整数倍,不足的则进行补0
				int sblength = sb.length();
				int surplusLength = sblength % 16;
				if (surplusLength != 0) {
					for (int j = 0; j < 16 - surplusLength; j++) {
						sb.append("0");
					}
				}
				return sb.toString();
	}
	/**
	 * pin入参拼接
	 * @param orderId
	 * @param cardNolength
	 * @param cardNoMi
	 * @param track23randLength
	 * @param track
	 * @param trackrandNo
	 * @param pinAndpinRandNo
	 * @param pasamId
	 * @param termId
	 * @return
	 */
	public static String getPinMacHexString(String orderId,String cardNolength,String cardNoMi,String track23randLength,String track,String trackrandNo,String pinAndpinRandNo,String pasamId,String termId){
		LogUtil.printInfo("orderId="+genHexString(orderId)+",cardNolength="+cardNolength+",cardNoMi="+cardNoMi
				+",track23randLength="+track23randLength+",track="+track+",trackrandNo="+trackrandNo+",pinAndpinRandNo="+pinAndpinRandNo+",pasamId="+pasamId+",termId="+termId);
		//		  MAB组成：32字节订单号+卡号长度+卡号密文+二三磁道及磁道加密随机数长度+二三磁道密文+磁道加密随机数+PIN密文+PIN加密随机数+PSAM+终端号
		StringBuffer sb = new StringBuffer();
		sb.append(genHexString(orderId))
		.append(cardNolength)
		.append(cardNoMi).append(track23randLength)
		.append(track).append(trackrandNo)
		.append(pinAndpinRandNo).append(pasamId).append(termId);
		// 长度必须为16的整数倍,不足的则进行补0
		int sblength = sb.length();
		int surplusLength = sblength % 16;
		if (surplusLength != 0) {
			for (int j = 0; j < 16 - surplusLength; j++) {
				sb.append("0");
			}
		}
		return sb.toString();
	}

	/**
	 * 十进制字符串转换十六进制字符串
	 * 
	 * @param keyString
	 * @return
	 */
	public static String genHexString(String keyString) {
		StringBuffer orderNumberSb = new StringBuffer();
		byte[] orderNumberBytes = keyString.getBytes();
		for (int i = 0; i < orderNumberBytes.length; i++) {
			orderNumberSb.append(Integer.toHexString(orderNumberBytes[i]));
		}
		return orderNumberSb.toString();
	}
	public static String genHexStr(int intValue){
		String hex = Integer.toHexString(intValue);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex;
	}
	/**
	 * 字符串转为Ascii格式字符串
	 * @param str
	 * @return
	 */
	public static String parseAscii(String str){
        StringBuilder sb=new StringBuilder();
        byte[] bs=str.getBytes();
        for(int i=0;i<bs.length;i++)
            sb.append(toHex(bs[i]));
        return sb.toString();
    }
	 public static String toHex(int n){
	        StringBuilder sb=new StringBuilder();
	        if(n/16==0){
	            return toHexUtil(n);
	        }else{
	            String t=toHex(n/16);
	            int nn=n%16;
	            sb.append(t).append(toHexUtil(nn));
	        }
	        return sb.toString();
	    }
	 private static String toHexUtil(int n){
	        String rt="";
	        switch(n){
	        case 10:rt+="A";break;
	        case 11:rt+="B";break;
	        case 12:rt+="C";break;
	        case 13:rt+="D";break;
	        case 14:rt+="E";break;
	        case 15:rt+="F";break;
	        default:
	            rt+=n;
	        }
	        return rt;
	    }
}
