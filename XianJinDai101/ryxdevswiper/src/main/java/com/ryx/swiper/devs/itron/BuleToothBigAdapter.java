package com.ryx.swiper.devs.itron;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.itron.android.bluetooth.DeviceInfo;
import com.itron.android.bluetooth.DeviceSearchListener;
import com.itron.android.ftf.Util;
import com.itron.protol.android.CommandReturn;
import com.itron.protol.android.CommunicationListener;
import com.nexgo.common.ByteUtils;
import com.ryx.swiper.CSwiperAdapter;
import com.ryx.swiper.IRyxDevSearchListener;
import com.ryx.swiper.ISwiperStateListener;
import com.ryx.swiper.RyxSwiperCode;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.ryx.swiper.utils.MoneyEncoder;

/**
 * Created by laomao on 15/12/3.
 */
public class BuleToothBigAdapter extends CSwiperAdapter {
    public CSwiperAdapter adapter;
    private Context context;
    private ISwiperStateListener listener;
    private int deviceType = 0;
    private String cardIdStr = "";
    private ITCommunicationCallBack itComListener;
    private BLEF2FCmdTest commandtest;
    private String cardnum = "";
    private String cardno = "";
    String miniksn = "";

    public BuleToothBigAdapter(Context context, ISwiperStateListener listener) {
        this.context = context;
        this.listener = listener;
        itComListener = new ITCommunicationCallBack();

        commandtest = new BLEF2FCmdTest(context, itComListener);

    }


    @Override
    public void searchBlueDevs(final IRyxDevSearchListener listener) {
        commandtest.searchDevices(new DeviceSearchListener() {
            @Override
            public void discoverOneDevice(DeviceInfo deviceInfo) {
                com.ryx.swiper.beans.DeviceInfo devInfo = new com.ryx.swiper.beans.DeviceInfo();
                devInfo.identifier = deviceInfo.identifier;
                devInfo.name = deviceInfo.name;
                listener.discoverOneDevice(devInfo);
            }

            @Override
            public void discoverComplete() {
                listener.discoverComplete();
            }

            @Override
            public void disConnected() {
                listener.disConnected();
            }
        });
    }

    @Override
    public int connectCSwiper(String address) {

        return initCSwiper(address);
    }

    @Override
    public int initCSwiper(String address) {
        int i = commandtest.openDevice(address);
        if (i == 0) {
            listener.onBluetoothConnectSuccess();
        } else {
            listener.onBluetoothConnectFail();
        }
        return i;
    }

    @Override
    public void startEmvSwiper(final Map<String, Object> map) {
        new Thread() {
            @Override
            public void run() {
                String action = MapUtil.getString(map, "buss_type");
                String orderid = MapUtil.getString(map, "order_id");
                String realamt = MapUtil.getString(map, "amount");
                byte[] random = Util.HexToBin(CryptoUtils.getInstance()
                        .getTransLogNo());

                for (int i = 0; i < random.length; i++)
                    LogUtil.printInfo(Integer.toHexString(random[i]));

                CryptoUtils.getInstance().setTransLogUpdate(false);
                CommandReturn cmresult = commandtest.statEmvSwiper(1,0x00,0x00,action,
                        random, orderid.getBytes(),
                        MoneyEncoder.encodeForSwiper(realamt));
                if (null == cmresult) {
                    listener.onTimeout();
                } else {
                    LogUtil.printInfo("ic刷卡成功");
                    Map<String, Object> map = new HashMap<String, Object>();
                    String card_no=new String(cmresult.Return_CardNo, 0,
							cmresult.Return_CardNo.length);
                    listener.onGetCardNoCompleted(card_no,"");
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

                    int len = 2 + 1 + 1 + cmresult.Return_ENCCardNo.length
                            + 1 + cmresult.Return_PSAMTrack.length + 1
                            + cmresult.Return_PSAMPIN.length + 1
                            + cmresult.Return_PSAMNo.length + 1
                            + cmresult.Return_TerSerialNo.length + 1
                            + cmresult.Return_PSAMMAC.length;
                    byte[] data = new byte[len];

                    data[0] = (byte) ((len - 2) / 256);
                    data[1] = (byte) ((len - 2) & 0x000000FF);

                    // jics todo
                    // data[2] = (byte) ((len - 2) & 0x000000FF);

                    data[2] = 0x1F;

                    // 卡号长度
                    data[3] = (byte) (cmresult.Return_ENCCardNo.length);
                    // 卡号密文
                    System.arraycopy(cmresult.Return_ENCCardNo, 0, data, 4,
                            cmresult.Return_ENCCardNo.length);
                    // 二三磁道及磁道加密随机数长度，16进制
                    data[4 + cmresult.Return_ENCCardNo.length] = (byte) (cmresult.Return_PSAMTrack.length);
                    // 磁道加密数据
                    System.arraycopy(cmresult.Return_PSAMTrack, 0, data,
                            4 + cmresult.Return_ENCCardNo.length + 1,
                            cmresult.Return_PSAMTrack.length);
                    // PIN密文长度及PIN随机数长度
                    data[4 + cmresult.Return_ENCCardNo.length + 1
                            + cmresult.Return_PSAMTrack.length] = (byte) (cmresult.Return_PSAMPIN.length);
                    // PIN密文及PIN随机数
                    System.arraycopy(cmresult.Return_PSAMPIN, 0, data, 4
                                    + cmresult.Return_ENCCardNo.length + 1
                                    + cmresult.Return_PSAMTrack.length + 1,
                            cmresult.Return_PSAMPIN.length);
                    // PSAM长度
                    data[4 + cmresult.Return_ENCCardNo.length + 1
                            + cmresult.Return_PSAMTrack.length + 1
                            + cmresult.Return_PSAMPIN.length] = (byte) (cmresult.Return_PSAMNo.length);
                    // PSAM号
                    System.arraycopy(cmresult.Return_PSAMNo, 0, data, 4
                                    + cmresult.Return_ENCCardNo.length + 1
                                    + cmresult.Return_PSAMTrack.length + 1
                                    + cmresult.Return_PSAMPIN.length + 1,
                            cmresult.Return_PSAMNo.length);
                    // 终端长度
                    data[4 + cmresult.Return_ENCCardNo.length + 1
                            + cmresult.Return_PSAMTrack.length + 1
                            + cmresult.Return_PSAMPIN.length + 1
                            + cmresult.Return_PSAMNo.length] = (byte) (cmresult.Return_TerSerialNo.length);
                    // 终端号
                    System.arraycopy(cmresult.Return_TerSerialNo, 0, data,
                            4 + cmresult.Return_ENCCardNo.length + 1
                                    + cmresult.Return_PSAMTrack.length + 1
                                    + cmresult.Return_PSAMPIN.length + 1
                                    + cmresult.Return_PSAMNo.length + 1,
                            cmresult.Return_TerSerialNo.length);
                    // MAC长度
                    data[4 + cmresult.Return_ENCCardNo.length + 1
                            + cmresult.Return_PSAMTrack.length + 1
                            + cmresult.Return_PSAMPIN.length + 1
                            + cmresult.Return_PSAMNo.length + 1
                            + cmresult.Return_TerSerialNo.length] = (byte) (cmresult.Return_PSAMMAC.length);
                    // MAC
                    System.arraycopy(cmresult.Return_PSAMMAC, 0, data, 4
                                    + cmresult.Return_ENCCardNo.length + 1
                                    + cmresult.Return_PSAMTrack.length + 1
                                    + cmresult.Return_PSAMPIN.length + 1
                                    + cmresult.Return_PSAMNo.length + 1
                                    + cmresult.Return_TerSerialNo.length + 1,
                            cmresult.Return_PSAMMAC.length);


                    cardnum = "FE00"
                            + Util.BinToHex(data, 0, data.length);
                    map.put("card_info", cardnum);
                    String cardSeriNoStr = "", ic55DataStr = "";

                    if (cmresult.CardSerial != null) {
                        cardSeriNoStr = Util.BinToHex(cmresult.CardSerial,
                                0, cmresult.CardSerial.length);
                    }

                    if (cmresult.emvDataInfo != null) {
                        ic55DataStr = Util.BinToHex(cmresult.emvDataInfo,
                                0, cmresult.emvDataInfo.length);
                    }

                    if (ic55DataStr.length() > 10) {
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
                    map.put("card_type", "1" + cmresult.CardType);


                    while (cardSeriNoStr.length() < 3
                            && cardSeriNoStr.length() > 0)
                        cardSeriNoStr = "0" + cardSeriNoStr;

                    map.put("ic_data", ic55DataStr);

                    map.put("icsernum", cardSeriNoStr);
                    map.put("dev_type", RyxSwiperCode.DEVICE_AC_BIG_BULETOOTH);

                    map.put("card_no", card_no);
                 String   pasamId= Util.BinToHex(cmresult.Return_PSAMNo, 0,
                		 cmresult.Return_PSAMNo.length);
                    map.put("pasamId",pasamId);
                    
                    listener.onDecodeCompleted(map);

                }


            }
        }.start();

    }

    @Override
    public void disConnect() {
        if (commandtest != null)
            commandtest.stopCSwiper();
    }

    @Override
    public void releaseCSwiper() {
        if (commandtest != null)
            commandtest.release();
    }

    @Override
    public String getKsn() {
        return miniksn;
    }

    @Override
    public void getKsnSync() {
        new GetKsnThread().start();
    }


    @Override
    public boolean isDevicePresent() {
        return false;
    }


    @Override
    public int getDeviceType() {
        return 0;
    }


    @Override
    public String getCardno() {
    	Map<String, Object> map=new HashMap<String, Object>();
    	map.put("buss_type", "");
    	map.put("order_id", "0000000000000000");
    	map.put("amount", "0");
    	startEmvSwiper(map);
        return "";
    }

    @Override
    public void writIc(String resp, String icdata) {
        new Thread(new icWriteBackThread(resp, icdata)).start();
    }

    @Override
    public boolean updateParam(int flag, int packageNo, String data) {
    	boolean updateresult;
		if(flag==0){
			updateresult=commandtest.updateTerminalParameters(0, packageNo+1, ByteUtils.hexString2ByteArray("31"+data));
		}else{
			updateresult=commandtest.updateTerminalParameters(1, packageNo+1, ByteUtils.hexString2ByteArray("31"+data));
		}
		LogUtil.printInfo("BuleToothBigAdapter+++flag=="+flag+",packageNo=="+packageNo+",data=="+data +",updateresult"+updateresult);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("index", packageNo);
			if(updateresult)
			{
				listener.onUpdateTerminalParamsCompleted(map);
			}
			else
			{
				listener.onUpdateTerminalParamsFailed(map);
			}
		return updateresult;
    }

    private class icWriteBackThread implements Runnable {

        private String resp;
        private String icdata;

        public icWriteBackThread(String _resp, String _icdata) {
            super();
            this.resp = _resp;
            this.icdata = _icdata;
        }

        @Override
        public void run() {


            CommandReturn cmdret = commandtest.secondIssuance(resp, icdata);
            if (cmdret != null) {

                LogUtil.printInfo("BIG blue write back ok ");
                listener.onICResponse(cmdret.resultIC, cmdret.resuiltScriptIC,
                        cmdret.resuiltDataIC);
            } else {
                LogUtil.printInfo("cmdret == null");
                listener.onError(1, "ic回写失败");
            }

        }

    }

    class GetKsnThread extends Thread {
        @Override
        public void run() {


            CommandReturn cmdret = commandtest.getKSN();


            if (cmdret != null) {


                miniksn = Util.BinToHex(cmdret.Return_PSAMNo, 0,
                        cmdret.Return_PSAMNo.length);


                listener.onDetected();

            } else {
                listener.onTimeout();
            }
        }
    }

    class ITCommunicationCallBack implements CommunicationListener {

        @Override
        public void onTimeout() {
            // TODO Auto-generated method stub
            LogUtil.printInfo("onTimeoutsys");
            listener.onTimeout();
        }

        @Override
        public void onError(int code, String msg) {
            // TODO Auto-generated method stub
            LogUtil.printInfo("onError code:" + code + "msg:" + msg);
            listener.onError(code, msg);
        }

        @Override
        public void onWaitingcard() {
            // TODO Auto-generated method stub
            LogUtil.printInfo("onWaitingcard");
            listener.onWaitingForCardSwipe();
        }

        @Override
        public void onWaitingPin() {
            // TODO Auto-generated method stub
            LogUtil.printInfo("onWaitingPin");
            // listener..sendMessage(updateUI.obtainMessage(1, "请输入密码"));
        }

        @Override
        public void onWaitingOper() {
            // TODO Auto-generated method stub
            LogUtil.printInfo("onWaitingOper");
            listener.onWaitingForCardSwipe();
        }

        @Override
        public void onNfcSwipingcard() {

        }

        @Override
        public void onShowMessage(String msg) {
            // TODO Auto-generated method stub
            LogUtil.printInfo("onShowMessage:" + msg);
            // listener..sendMessage(updateUI.obtainMessage(1, msg));
        }

        @Override
        public void onICWaitingOper() {
            LogUtil.printInfo("onWaiting IC BLUE Oper");
            listener.EmvOperationWaitiing();

        }

		@Override
		public void onQueryIcRecord(int arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

        @Override
        public void onRemoveCard(int i, String s) {

        }

        @Override
		public void onUploadOfflineTradeRecord(int arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

    }

	@Override
	public int getTerminalType() {
		return commandtest.getTerminalType();
	}
}

