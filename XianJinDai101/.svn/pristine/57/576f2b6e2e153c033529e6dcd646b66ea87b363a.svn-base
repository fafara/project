package com.ryx.swiper.devs.landi;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.itron.android.ftf.Util;
import com.landicorp.android.mpos.reader.LandiMPos;
import com.landicorp.android.mpos.reader.PBOCOnlineDataProcessListener;
import com.landicorp.android.mpos.reader.PBOCStartListener;
import com.landicorp.android.mpos.reader.model.OnlineDataProcessResult;
import com.landicorp.android.mpos.reader.model.PBOCOnlineData;
import com.landicorp.android.mpos.reader.model.StartPBOCParam;
import com.landicorp.android.mpos.reader.model.StartPBOCResult;
import com.landicorp.mpos.reader.BasicReaderListeners;
import com.landicorp.mpos.reader.model.MPosDeviceInfo;
import com.landicorp.mpos.reader.model.MPosEMVProcessResult;
import com.landicorp.robert.comm.api.CommunicationManagerBase;
import com.landicorp.robert.comm.api.DeviceInfo;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.ByteArrayUtils;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.DataUtil;
import com.ryx.swiper.utils.DateUtil;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;

public abstract class LanDiBaseAdapter extends CSwiperAdapter {
	Context context;
	ISwiperStateListener listener;
	DeviceInfo deviceInfo;
	LandiMPos reader;
	String strKsn;
	int deviceType;
	int index=-1;

	@Override
	public abstract void searchBlueDevs(final IRyxDevSearchListener listener);

	@Override
	public abstract int connectCSwiper(String address);

	@Override
	public abstract int initCSwiper(String address);

	@Override
	public void startEmvSwiper(final Map<String, Object> map) {
		try {
		LogUtil.printInfo("startEmvSwiper--------------------------------------=");
		final String amount = MapUtil.getString(map, "amount");
		final String orderid = MapUtil.getString(map, "order_id");
		reader.getDeviceInfo(new BasicReaderListeners.GetDeviceInfoListener() {
			@Override
			public void onError(int i, String s) {
				LogUtil.printInfo("startEmvSwiper-getDeviceInfo-onError==" + s);
				listener.onDecodeError(s);
				listener.onError(i, s);
			}

			@Override
			public void onGetDeviceInfoSucc(MPosDeviceInfo mPosDeviceInfo) {
				//strKsn = mPosDeviceInfo.deviceSN;
//				终端编号+psdmid 
				strKsn=mPosDeviceInfo.clientSN+mPosDeviceInfo.pinpadSN;
//				Log.d("ryx", "strKsn="+strKsn);
				if(TextUtils.isEmpty(strKsn)||strKsn.length()!=36){
					//为空的话为测试设备,后续生产时候需要去掉此代码xucc
					if (deviceType==RyxSwiperCode.DEVICE_LANDI_BULETOOTH) {
						strKsn = "800188799901000000008879990100000000";
					}else {
						strKsn = "887999010000004680018879990100000046";
					}
				}
				String transLogNo = CryptoUtils.getInstance().getTransLogNo();
				LogUtil.printInfo("getRandomCode-transLogNo==" + transLogNo+".deviceSN="+mPosDeviceInfo.deviceSN+" clientSN="+mPosDeviceInfo.clientSN+" ksn="+mPosDeviceInfo.ksn+" terminalSn="+mPosDeviceInfo.terminalSn);
				CryptoUtils.getInstance().setTransLogUpdate(false);
				try {
					reader.getRandomCode(Util.HexToBin(transLogNo), new BasicReaderListeners.GetRandomCodeListener() {

						@Override
						public void onError(int i, String s) {
							LogUtil.printInfo("getRandomCode-onError" + s);
							listener.onError(i, s);
						}

						@Override
						public void onGetRandomCodeSucc(String randomNumber) {
							final String randomNumStr = randomNumber.substring(0, 8);
							final Map<String, Object> icresultMap = new HashMap<String, Object>();
							LogUtil.printInfo("randomNumber=====================================" + randomNumber);
							// 80018879990100000046|8879990100000046|ED640BA73474833683D8F93E2740B3D8|B492B647
							reader.waitingCard(BasicReaderListeners.WaitCardType.MAGNETIC_IC_CARD_RFCARD, amount, "请刷卡", 60000, new BasicReaderListeners.WaitingCardListener() {
								@Override
								public void onWaitingCardSucc(final BasicReaderListeners.CardType cardType) {
									listener.onWaitingCardDataReader();
									LogUtil.printInfo("onGetDeviceInfoSucc-waitingCard-onWaitingCardSucc==" + cardType.name());
									
									//modify by laomao 20160324 调整PBOC交易调用顺序
									// 磁条卡
									if (cardType.compareTo(BasicReaderListeners.CardType.MAGNETIC_CARD) == 0) {
										reader.getPANPlain(new BasicReaderListeners.GetPANListener() {
										@Override
										public void onError(int arg0, String arg1) {
											LogUtil.printInfo("onGetDeviceInfoSucc-waitingCard-getPANPlain==" + arg1);
											listener.onError(arg0, arg1);
										}

										@Override
										public void onGetPANSucc(final String cardNumber) {
											LogUtil.printInfo("onGetDeviceInfoSucc-waitingCard-getPANPlain-onGetPANSucc==cardNumber==" + cardNumber);
											// IC卡数据Map
											final Map<String, Object> icresultMap = new HashMap<String, Object>();
											// 磁条卡
											if (cardType.compareTo(BasicReaderListeners.CardType.MAGNETIC_CARD) == 0) {

												//Toast.makeText(context, "卡号==" + cardNumber, Toast.LENGTH_SHORT).show();
												// 4:磁条卡交易第四步，获取磁道密文
												reader.getTrackDataCipher(new BasicReaderListeners.GetTrackDataCipherListener() {
													@Override
													public void onError(int i, String s) {
														LogUtil.printInfo("getTrackDataCipher-onError==" + i + "," + s);
														listener.onError(i, s);
													}

													@Override
													public void onGetTrackDataCipherSucc(final String mTrack1, final String mTrack2,  String sdkmTrack3, final String mExpireDate) {
														//防止三磁信息为null时,出现"abc"+null==>"abcnull"情况
														if(sdkmTrack3==null){
															sdkmTrack3="";
														}
														final String mTrack3=sdkmTrack3;
														final String encTracks = mTrack2 + mTrack3; // 加密的磁道数据+随机数。(如果磁道为明文后面不跟随机数)
														final int track1Length = TextUtils.isEmpty(mTrack1)  ? 0 : mTrack1.length() / 2;
														final int track2Length = TextUtils.isEmpty(mTrack2) ? 0 : mTrack2.length() / 2;
														final int track3Length = TextUtils.isEmpty(mTrack3) ? 0 : mTrack3.length() / 2;
														final String maskedPAN = cardNumber;// 使用卡号
														final String expiryDate = mExpireDate;
														listener.onGetCardNoCompleted(maskedPAN,expiryDate);
														// 计算Mac所需的HexString参数
														String hexString = DataUtil.genMacHexString(mTrack2, mTrack3, randomNumStr, strKsn, orderid);
														LogUtil.printInfo("calculateMac===原参数" + hexString);
														reader.calculateMac(ByteArrayUtils.hexString2ByteArray(hexString), new BasicReaderListeners.CalcMacListener() {
															@Override
															public void onError(int errCode, String errDesc) {
																LogUtil.printInfo("calculateMac===" + errCode + errDesc);
																// 回调onDecodeError
																listener.onDecodeError(errDesc);
																listener.onError(errCode, errDesc);
															}

															@Override
															public void onCalcMacSucc(byte[] mac) {
																String cardMAC = ByteArrayUtils.byteArray2HexString(mac);
																LogUtil.printInfo("calculateMac=success==" + cardMAC);
																byte[] ic55Data = null;// 词条卡没有55数据域
																LogUtil.printInfo("citiao_onGetRandomCodeSucc==" + "mTrack1=" + mTrack1 + ",mTrack2=" + mTrack2 + ",mTrack3=" + mTrack3 + ",cardNumber=" + cardNumber + ",mExpireDate=" + mExpireDate);
																// Toast.makeText(context,"mTrack1="+mTrack1+",mTrack2="+mTrack2+",mTrack3="+mTrack3+",cardNumber="+cardNumber+",mExpireDate="+mExpireDate,Toast.LENGTH_SHORT).show();
																Map<String, Object> resultmapp = DataUtil.putCardServerData("10", deviceType, cardMAC, strKsn, track1Length, track2Length, track3Length, randomNumStr, maskedPAN, encTracks, expiryDate, "", ic55Data);
																LogUtil.printInfo("元数据====mTrack2=" + mTrack2 + ",mTrack3=" + mTrack3 + ",randomNumber=" + randomNumStr + ",strKsn=" + strKsn + ",cardNumber=" + DataUtil.genHexString(cardNumber) + ",encTracks=" + encTracks);
																LogUtil.printInfo("加密数据===" + resultmapp.get("card_info").toString());
																listener.onDecodeCompleted(resultmapp);

															}
														});
													}
												});
											}
 
										}
									});									
										}
									// IC卡
									else if (cardType.equals(BasicReaderListeners.CardType.IC_CARD) || cardType.equals(BasicReaderListeners.CardType.RF_CARD)) {
												final StartPBOCParam startPBOCParam = new StartPBOCParam();
												byte emvTradeType = 0x00;
												startPBOCParam.setTransactionType(emvTradeType);
												startPBOCParam.setAuthorizedAmount(amount);
												Log.d("landisdk--amount", amount);
												startPBOCParam.setOtherAmount("000000000000");
												startPBOCParam.setDate(DateUtil.formatDateToStr(new Date(), "yyMMdd"));
												startPBOCParam.setTime(DateUtil.formatDatetime(new Date(), "HHmmss")); // "pos_time":
												startPBOCParam.setForbidContactCard(false);// 是否使用接触卡
												startPBOCParam.setForceOnline(true);
												startPBOCParam.setForbidMagicCard(false);
												startPBOCParam.setForbidContactlessCard(false);// 是否使用非接卡
												reader.startPBOC(startPBOCParam, new BasicReaderListeners.EMVProcessListener() {
													@Override
													public void onError(int errCode, String errDesc) {
														LogUtil.printInfo("IC--startPBOC-onError" + errDesc);
														// 错误回调
														listener.onDecodeError(errDesc);
														listener.onError(errCode, errDesc);
													}

													@Override
													public void onEMVProcessSucc(MPosEMVProcessResult result) {
														LogUtil.printInfo(DateUtil.currentDatetime() + "IC-姓名:" + result.getCardHolderName() + "失效日期:" + result.getExpireData() + "pan序列号" + result.getPanSerial() + "二磁道:" + result.getTrack2() + " getCredentialNo:" + result.getCredentialNo() + " getPan:" + result.getPan() + " getRandomCode:" + result.getRandomCode() + " getRandomCode:" + result.getAuthentication());
														icresultMap.put("mposemvProcessResult", result);
													}
												}, new PBOCStartListener() {
													@Override
													public void onError(int errCode, String errDesc) {
														LogUtil.printInfo("PBOCStartListener-onError" + errDesc);
														// 错误回调
														listener.onDecodeError(errDesc);
														listener.onError(errCode, errDesc);
													}

													@Override
													public void onPBOCStartSuccess(final StartPBOCResult result) {
														Object mposemvProcessResult = MapUtil.get(icresultMap, "mposemvProcessResult", null);
														final MPosEMVProcessResult posEMVProcessResult = (MPosEMVProcessResult) mposemvProcessResult;
														final String mTrack1 = null;// 暂无
														final String mTrack2 = posEMVProcessResult.getTrack2();
														final String mTrack3 = null;// 暂无
														final String encTracks = mTrack2;
														final String mExpireDate = posEMVProcessResult.getExpireData();
														final String cardNum=posEMVProcessResult.getPan();
														// 计算Mac所需的HexString参数
														String hexString = DataUtil.genMacHexString(mTrack2, mTrack3, randomNumStr, strKsn, orderid);
														LogUtil.printInfo("calculateMac==ic卡=原参数：" + hexString);
														reader.calculateMac(ByteArrayUtils.hexString2ByteArray(hexString), new BasicReaderListeners.CalcMacListener() {

															@Override
															public void onError(int arg0, String arg1) {
																LogUtil.printInfo("calculateMac===" + arg0 + arg1);
																// 回调onDecodeError
																listener.onDecodeError(arg1);
																listener.onError(arg0, arg1);
															}

															@Override
															public void onCalcMacSucc(byte[] mac) {
																String cardMAC = ByteArrayUtils.byteArray2HexString(mac);
																LogUtil.printInfo("calculateMac=success==" + cardMAC);

																int track1Length = mTrack1 == null ? 0 : mTrack1.length() / 2;
																int track2Length = mTrack2 == null ? 0 : mTrack2.length() / 2;
																int track3Length = mTrack3 == null ? 0 : mTrack3.length() / 2;
																String maskedPAN = cardNum;// 使用卡号
																String expiryDate = mExpireDate;
																listener.onGetCardNoCompleted(maskedPAN,expiryDate);
																byte[] ic55Data = result.getICCardData();
																String cardType = "11";// 应该是11
																Map<String, Object> resultMap = DataUtil.putCardServerData(cardType, deviceType, cardMAC, strKsn, track1Length, track2Length, track3Length, randomNumStr, maskedPAN, encTracks, expiryDate, posEMVProcessResult.getPanSerial(), ic55Data);
																listener.onDecodeCompleted(resultMap);
															}
														});
													}
												});
									}
									//modify by laomao 20160324 调整PBOC交易调用顺序 end
								
									
									/* 屏蔽老的顺序
									// 获取卡号
									reader.getPANPlain(new BasicReaderListeners.GetPANListener() {
										@Override
										public void onError(int arg0, String arg1) {
											LogUtil.printInfo("onGetDeviceInfoSucc-waitingCard-getPANPlain==" + arg1);
											listener.onError(arg0, arg1);
										}

										@Override
										public void onGetPANSucc(final String cardNumber) {
											LogUtil.printInfo("onGetDeviceInfoSucc-waitingCard-getPANPlain-onGetPANSucc==cardNumber==" + cardNumber);
											// IC卡数据Map
											final Map<String, Object> icresultMap = new HashMap<String, Object>();
											// 磁条卡
											if (cardType.compareTo(BasicReaderListeners.CardType.MAGNETIC_CARD) == 0) {

												//Toast.makeText(context, "卡号==" + cardNumber, Toast.LENGTH_SHORT).show();
												// 4:磁条卡交易第四步，获取磁道密文
												reader.getTrackDataCipher(new BasicReaderListeners.GetTrackDataCipherListener() {
													@Override
													public void onError(int i, String s) {
														LogUtil.printInfo("getTrackDataCipher-onError==" + i + "," + s);
														listener.onError(i, s);
													}

													@Override
													public void onGetTrackDataCipherSucc(final String mTrack1, final String mTrack2, final String mTrack3, final String mExpireDate) {
														final String encTracks = mTrack2 + mTrack3; // 加密的磁道数据+随机数。(如果磁道为明文后面不跟随机数)
														final int track1Length = mTrack1 == null ? 0 : mTrack1.length() / 2;
														final int track2Length = mTrack2 == null ? 0 : mTrack2.length() / 2;
														final int track3Length = mTrack3 == null ? 0 : mTrack3.length() / 2;
														final String maskedPAN = cardNumber;// 使用卡号
														final String expiryDate = mExpireDate;
														listener.onGetCardNoCompleted(maskedPAN,expiryDate);
														// 计算Mac所需的HexString参数
														String hexString = DataUtil.genMacHexString(mTrack2, mTrack3, randomNumStr, strKsn, orderid);
														LogUtil.printInfo("calculateMac===原参数" + hexString);
														reader.calculateMac(ByteArrayUtils.hexString2ByteArray(hexString), new BasicReaderListeners.CalcMacListener() {
															@Override
															public void onError(int errCode, String errDesc) {
																LogUtil.printInfo("calculateMac===" + errCode + errDesc);
																// 回调onDecodeError
																listener.onDecodeError(errDesc);
																listener.onError(errCode, errDesc);
															}

															@Override
															public void onCalcMacSucc(byte[] mac) {
																String cardMAC = ByteArrayUtils.byteArray2HexString(mac);
																LogUtil.printInfo("calculateMac=success==" + cardMAC);
																byte[] ic55Data = null;// 词条卡没有55数据域
																LogUtil.printInfo("citiao_onGetRandomCodeSucc==" + "mTrack1=" + mTrack1 + ",mTrack2=" + mTrack2 + ",mTrack3=" + mTrack3 + ",cardNumber=" + cardNumber + ",mExpireDate=" + mExpireDate);
																// Toast.makeText(context,"mTrack1="+mTrack1+",mTrack2="+mTrack2+",mTrack3="+mTrack3+",cardNumber="+cardNumber+",mExpireDate="+mExpireDate,Toast.LENGTH_SHORT).show();
																Map<String, Object> resultmapp = DataUtil.putCardServerData("10", deviceType, cardMAC, strKsn, track1Length, track2Length, track3Length, randomNumStr, maskedPAN, encTracks, expiryDate, "", ic55Data);
																LogUtil.printInfo("元数据====mTrack2=" + mTrack2 + ",mTrack3=" + mTrack3 + ",randomNumber=" + randomNumStr + ",strKsn=" + strKsn + ",cardNumber=" + DataUtil.genHexString(cardNumber) + ",encTracks=" + encTracks);
																LogUtil.printInfo("加密数据===" + resultmapp.get("card_info").toString());
																listener.onDecodeCompleted(resultmapp);

															}
														});
													}
												});
											}
											// IC卡
											else if (cardType.equals(BasicReaderListeners.CardType.IC_CARD) || cardType.equals(BasicReaderListeners.CardType.RF_CARD)) {
												final StartPBOCParam startPBOCParam = new StartPBOCParam();
												byte emvTradeType = 0x00;
												startPBOCParam.setTransactionType(emvTradeType);
												startPBOCParam.setAuthorizedAmount(amount);
												Log.d("landisdk--amount", amount);
												startPBOCParam.setOtherAmount("000000000000");
												startPBOCParam.setDate(DateUtil.formatDateToStr(new Date(), "yyMMdd"));
												startPBOCParam.setTime(DateUtil.formatDatetime(new Date(), "HHmmss")); // "pos_time":
												startPBOCParam.setForbidContactCard(false);// 是否使用接触卡
												startPBOCParam.setForceOnline(true);
												startPBOCParam.setForbidMagicCard(false);
												startPBOCParam.setForbidContactlessCard(false);// 是否使用非接卡
												reader.startPBOC(startPBOCParam, new BasicReaderListeners.EMVProcessListener() {
													@Override
													public void onError(int errCode, String errDesc) {
														LogUtil.printInfo("IC--startPBOC-onError" + errDesc);
														// 错误回调
														listener.onDecodeError(errDesc);
														listener.onError(errCode, errDesc);
													}

													@Override
													public void onEMVProcessSucc(MPosEMVProcessResult result) {
														LogUtil.printInfo(DateUtil.currentDatetime() + "IC-姓名:" + result.getCardHolderName() + "失效日期:" + result.getExpireData() + "pan序列号" + result.getPanSerial() + "二磁道:" + result.getTrack2() + " getCredentialNo:" + result.getCredentialNo() + " getPan:" + result.getPan() + " getRandomCode:" + result.getRandomCode() + " getRandomCode:" + result.getAuthentication());
														icresultMap.put("mposemvProcessResult", result);
													}
												}, new PBOCStartListener() {
													@Override
													public void onError(int errCode, String errDesc) {
														LogUtil.printInfo("PBOCStartListener-onError" + errDesc);
														// 错误回调
														listener.onDecodeError(errDesc);
														listener.onError(errCode, errDesc);
													}

													@Override
													public void onPBOCStartSuccess(final StartPBOCResult result) {
														Object mposemvProcessResult = MapUtil.get(icresultMap, "mposemvProcessResult", null);
														final MPosEMVProcessResult posEMVProcessResult = (MPosEMVProcessResult) mposemvProcessResult;
														final String mTrack1 = null;// 暂无
														final String mTrack2 = posEMVProcessResult.getTrack2();
														final String mTrack3 = null;// 暂无
														final String encTracks = mTrack2;
														final String mExpireDate = posEMVProcessResult.getExpireData();
														// 计算Mac所需的HexString参数
														String hexString = DataUtil.genMacHexString(mTrack2, mTrack3, randomNumStr, strKsn, orderid);
														LogUtil.printInfo("calculateMac==ic卡=原参数：" + hexString);
														reader.calculateMac(ByteArrayUtils.hexString2ByteArray(hexString), new BasicReaderListeners.CalcMacListener() {

															@Override
															public void onError(int arg0, String arg1) {
																LogUtil.printInfo("calculateMac===" + arg0 + arg1);
																// 回调onDecodeError
																listener.onDecodeError(arg1);
																listener.onError(arg0, arg1);
															}

															@Override
															public void onCalcMacSucc(byte[] mac) {
																String cardMAC = ByteArrayUtils.byteArray2HexString(mac);
																LogUtil.printInfo("calculateMac=success==" + cardMAC);

																int track1Length = mTrack1 == null ? 0 : mTrack1.length() / 2;
																int track2Length = mTrack2 == null ? 0 : mTrack2.length() / 2;
																int track3Length = mTrack3 == null ? 0 : mTrack3.length() / 2;
																String maskedPAN = cardNumber;// 使用卡号
																String expiryDate = mExpireDate;
																listener.onGetCardNoCompleted(maskedPAN,expiryDate);
																byte[] ic55Data = result.getICCardData();
																String cardType = "11";// 应该是11
																Map<String, Object> resultMap = DataUtil.putCardServerData(cardType, deviceType, cardMAC, strKsn, track1Length, track2Length, track3Length, randomNumStr, maskedPAN, encTracks, expiryDate, posEMVProcessResult.getPanSerial(), ic55Data);
																listener.onDecodeCompleted(resultMap);
															}
														});
													}
												});
											}
										}
									});
									*/
								}

								@Override
								public void onProgressMsg(String s) {
									LogUtil.printInfo("waitingCard-onProgressMsg" + s);
//									Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//									listener.onError(0x000, s);
									//此处回调不一定是降级交易原因,借助降级交易接口将信息返回,等待调用重新刷卡接口时进行类型判断即可
									Map<String,Object> errorMap=new HashMap<String, Object>();
									errorMap.put("demotionTrade", s);
									listener.onDemotionTrade(errorMap);
								}

								@Override
								public void onError(int i, String s) {
									LogUtil.printInfo("waitingCard-onError" + s);
//									Toast.makeText(context, s, Toast.LENGTH_LONG).show();
									listener.onError(i, s);
								}
							});
						}
					});
				} catch (Exception e1) {
					LogUtil.printInfo("getRandomCode-onError" + e1.getMessage());
					// 回调onDecodeError
					listener.onDecodeError(e1.getMessage());
					listener.onError(0x000, e1.getMessage());
				}
			}
		});

		//
		
		} catch (Exception e) {
			listener.onError(0x0089,"数据解析异常,请重新操作!");
		}
	}

	@Override
	public void disConnect() {
		reader.closeDevice(new BasicReaderListeners.CloseDeviceListener() {
			@Override
			public void closeSucc() {
				Toast.makeText(context, "关闭设备成功", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public String getKsn() {
		return this.strKsn;
	}

	@Override
	public void getKsnSync() {
		LogUtil.printInfo("getKsnSync--------------------");
		reader.openDevice(CommunicationManagerBase.CommunicationMode.MODE_DUPLEX, deviceInfo, new BasicReaderListeners.OpenDeviceListener() {
			@Override
			public void openSucc() {
				reader.getDeviceInfo(new BasicReaderListeners.GetDeviceInfoListener() {
					@Override
					public void onError(int arg0, String arg1) {
						LogUtil.printInfo("getDeviceInfo-onError" + arg1);
						listener.onDetecteError();
					}

					@Override
					public void onGetDeviceInfoSucc(MPosDeviceInfo mposdeviceinfo) {
//						终端编号+psdmid 
						strKsn=mposdeviceinfo.clientSN+mposdeviceinfo.pinpadSN;
						if(TextUtils.isEmpty(strKsn)||strKsn.length()!=36){
							//为空的话为测试设备,后续生产时候需要去掉此代码xucc
							if (deviceType==RyxSwiperCode.DEVICE_LANDI_BULETOOTH) {
								strKsn = "800188799901000000008879990100000000";
							}else {
								strKsn = "887999010000004680018879990100000046";
							}
						}
						LogUtil.printInfo("getDeviceInfo-onGetDeviceInfoSucc===" + mposdeviceinfo.ksn+"-------"+mposdeviceinfo.deviceSN);
						listener.onDetected();
					}
				});
			}

			@Override
			public void openFail() {
				Toast.makeText(context, "openFail", Toast.LENGTH_LONG).show();
				listener.onDetecteError();
			}
		});
	}

	@Override
	public abstract boolean isDevicePresent();

	@Override
	public int getDeviceType() {
		return 0;
	}

	@Override
	public String getCardno() {
		return null;
	}

	@Override
	public void writIc(String resp, String icdata) {
		LogUtil.printInfo("writIc----resp：" + resp + " icdata:" + icdata);
		PBOCOnlineData onlineData = new PBOCOnlineData();
		onlineData.setAuthRespCode(ByteArrayUtils.getBytes(resp));
		onlineData.setOnlineData(ByteArrayUtils.toByteArray(icdata));
		reader.onlineDataProcess(onlineData, new PBOCOnlineDataProcessListener() {

			@Override
			public void onError(int arg0, String arg1) {
				LogUtil.printInfo("交易失败" + arg1);
				listener.onICResponse(0, null, null);
			}

			@Override
			public void onPBOCOnlineDataProcess(OnlineDataProcessResult arg0) {
				LogUtil.printInfo("Ic回写成功" + ByteArrayUtils.byteArray2HexString(arg0.getICCardData()));
				listener.onICResponse(1, null, null);
			}

		});
	}

	@Override
	public boolean updateParam(int flag, int packageNo, String data) {
		return false;
	}

	@Override
	public void releaseCSwiper() {
		reader.closeDevice(new BasicReaderListeners.CloseDeviceListener() {
			@Override
			public void closeSucc() {
				// 回调成功
			}
		});
	}

}
