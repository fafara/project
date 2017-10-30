package com.ryx.swiper.devs.itron;

/**
 * Created by laomao on 15/12/9.
 */

import android.content.Context;
import android.util.Log;

import com.itron.android.ftf.Util;
import com.itron.android.lib.Logger;
import com.itron.protol.android.CommandController;
import com.itron.protol.android.CommandReturn;
import com.itron.protol.android.CommandStateChangedListener;

import java.io.UnsupportedEncodingException;

public class CmdTest {
    static Logger logger = Logger.getInstance(CmdTest.class);
    public CommandController cmdctrl;

    byte[] random = null;
    byte[] cash = "12345".getBytes();
    byte[] data = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0xab, (byte) 0xcd, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0xab, (byte) 0xcd, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0xab, (byte) 0xcd, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0xab, (byte) 0xcd, (byte) 0x43, (byte) 0x31, (byte) 0x33, (byte) 0x33, (byte) 0x35, (byte) 0x41, (byte) 0x37, (byte) 0x39};
    byte[] PAN = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0x19};
    byte[] pinkey = {(byte) 0xaa, (byte) 0xba, (byte) 0xc7, (byte) 0x80, (byte) 0x2d, (byte) 0xF4, (byte) 0x9f, (byte) 0x68, (byte) 0x44, (byte) 0xb4, (byte) 0xd4, (byte) 0x39};
    byte[] mackey = {(byte) 0x22, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0x19};
    byte[] deskey = {(byte) 0x32, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0x19};
    byte[] vendor = "123456789012345".getBytes();
    byte[] terid = "32145678".getBytes();

    public CmdTest(Context ctx, CommandStateChangedListener s) {
        cmdctrl = new CommandController(ctx, s);
        //cmdctrl=new CommandController(s);
        //cmdctrl=new CommandController();
        //cmdctrl.logger.setLocalLevel(Logger.Level.ERROR);
    }

    /**
     * 获取磁道
     *
     * @return
     */
    public boolean Get_CardTrack() {
        CommandReturn cmdret = new CommandReturn();
        cmdret = cmdctrl.Get_CardTrack(30);
        if (cmdret == null) {
            return false;
        }
        //if(cmdret.Return_Result!=0)
        //{
        //	return false;
        //}
        String cardNo = "";
        if (cmdret.Return_Track2 != null) {
            cardNo = Util.BinToHex(cmdret.Return_Track2, 0, cmdret.Return_Track2.length) + "FF";
        }
        if (cmdret.Return_Track3 != null) {
            cardNo += Util.BinToHex(cmdret.Return_Track3, 0, cmdret.Return_Track3.length);
            cardNo += "FF";
        }
        if (cardNo.equals("")) {
            return false;
        }
        cmdctrl.getlistener().onGetCardNo(cardNo);
        Log.d("itron", "PAN=" + Util.BinToHex(cmdret.Return_PAN, 0, 8));
        logger.info("itron+PAN=" + Util.BinToHex(cmdret.Return_PAN, 0, 8));
        return true;
    }

    /**
     * 获取磁道密文
     *
     * @return
     */
    public boolean Get_EncCardTrack() {
        CommandReturn cmdret = new CommandReturn();
        byte[] random = {1, 2, 3};
        //byte[] random={1,2,3,4,5,6,7,8};
        //byte[] random=null;
        cmdret = cmdctrl.Get_EncTrack(0, 2, random, 50);
        if ((cmdret != null) && (cmdret.Return_PSAMTrack != null)) {

            cmdctrl.getlistener().onGetKsn("磁道密文:\n" + Util.BinToHex(cmdret.Return_PSAMTrack, 0, cmdret.Return_PSAMTrack.length));
            logger.error("cmdtest getksn=" + Util.BinToHex(cmdret.Return_PSAMTrack, 0, cmdret.Return_PSAMTrack.length));
            //return Util.BcdToString(cmdret.Return_PSAMNo);
            return true;

        } else {
            cmdctrl.getlistener().onGetKsn(null);
            //return Util.BcdToString(cmdret.Return_PSAMNo);
            logger.error("cmdtest getksn=null");
            return false;

        }
    }

    public void printData(String printInfo) {

		/*
        String str[] = new String[]{
		         "11      商户名称：盛付通\n商户存根            请妥善保管\n", //第一份的标题
		         "12    商户名称：盛付通切客\n持卡人存根             请妥善保管\n", //第二份的标题
		         "00商户编号（MERCHAANT NO） 123456",
		         "00终端编号（TERMINAL NO） 123456",
		         "00发卡行：中国银行",
		         "00卡号（CARD NO）622209******6620",
		         "00交易类型（TRANS TYPE）",
		         "00    消费（SALE）",
		         "00交易时间（DATE/TIME）",
		         "00    2012/12/02   19:05:26",
		         "00参考号（REFERENCE NO）P000000000324",
		         "00交易金额（RMB）￥ 200.00",
		         "00交易金额：1000.00元",
		         "00备注（REFERENCE）：",

		         "00交易时间（DATE/TIME）",
		         "00    2012/12/02   19:05:26",
		         "00参考号（REFERENCE NO）P000000000324",
		         "00交易金额（RMB）￥ 200.00",
		         "00交易金额：1000.00元",
		         "00备注（REFERENCE）：",

		         "21持卡人签名\n\n\n\n\n\n\n本人确认以上交易，同意将其计入本卡账户\nI ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES",              //第一份的落款
		         "22",//第二份的落款
		};
		*/

        String str[] = printInfo.split(",");
        cmdctrl.Set_PtrData(2, str, 60);
    }

    /**
     * 刷卡输入密码连续操作
     *
     * @return
     */
    public String getcardpsw(byte[] random, byte[] appendData, String money) {
        CommandReturn cmdret = new CommandReturn();
        cmdret = cmdctrl.Get_ExtCtrlConOperator(1, 1, 1, 1, (byte) 0x1f, random, money.getBytes(), appendData, 60);

        if (cmdret != null && cmdret.Return_RecvData != null) {
            byte[] temp = new byte[cmdret.Return_RecvData.length + 3];
            temp[1] = (byte) ((cmdret.Return_RecvData.length) / 256);
            temp[2] = (byte) ((cmdret.Return_RecvData.length) % 256);
            System.arraycopy(cmdret.Return_RecvData, 0, temp, 3, cmdret.Return_RecvData.length);
            String buf = Util.BinToHex(temp, 0, cmdret.Return_RecvData.length + 3);

            //cmdctrl.getlistener().onGetKsn(buf);
            logger.error(buf);


            return buf;


        } else {
            //cmdctrl.getlistener().onGetKsn(null);
            //return Util.BcdToString(cmdret.Return_PSAMNo);
            logger.error("cmdtest getksn=null");
            return null;

        }
    }

    /**
     * 关闭fsk连接
     */
    public void fskexit() {
        logger.debug("fsk关闭连接");
        cmdctrl.ReleaseDevice();
        //cmdctrl.FSKstop();  //jzhl
    }

    /**
     * 发送退出指令
     */
    public void stopCSwiper() {
        CommandReturn cmdret = new CommandReturn();
        cmdret = cmdctrl.Get_CommExit();

    }

    /**
     */
    public void RenewKey() {
        String pinstr = "89b07b35a1b3f47e4eb13bf6";
        String macstr = "89b07b35a1b3f47e4eb13bf6";
        String desstr = "89b07b35a1b3f47e4eb13bf6";
        byte[] PINkey = Util.HexToBin(pinstr);
        byte[] MACkey = Util.HexToBin(macstr);
        byte[] DESkey = Util.HexToBin(desstr);
        CommandReturn cmdret = new CommandReturn();
        cmdret = cmdctrl.Get_RenewKey(PINkey, MACkey, DESkey);
        if ((cmdret != null) && ((cmdret.Return_Result == 0X00))) {
            cmdctrl.getlistener().onGetKsn("密钥成功");
        } else {
            cmdctrl.getlistener().onGetKsn("密钥失败");
        }
    }

    public CommandReturn getCardNo() {
        logger.debug("getCardNo()");
        CommandReturn cmdret = new CommandReturn();
        cmdret = cmdctrl.Get_CardNo(30);

        logger.debug("getCardNo()==" + cmdret.Return_CardNo + "dddd" + cmdret);
        if ((cmdret != null) && (cmdret.Return_CardNo != null)) {
            String str = new String(cmdret.Return_CardNo);
            cmdctrl.getlistener().onGetKsn(str);

            return cmdret;
        } else {
            cmdctrl.getlistener().onGetKsn("获取卡号失败");
            return null;
        }


    }

    /**
     * @param str
     */
    public void display(String str, int time) {
        logger.debug("CheckMAC()");
        CommandReturn cmdret = new CommandReturn();
        try {
            cmdret = cmdctrl.Cmd_Display(str.getBytes("GBK"), time);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if ((cmdret != null) && (cmdret.Return_Result == 0x00)) {
            cmdctrl.getlistener().onGetKsn("显示成功");
            logger.error("显示成功");
        } else if ((cmdret != null) && (cmdret.Return_Result == 10)) {
            cmdctrl.getlistener().onGetKsn("用户取消");
            logger.error("用户取消");
        } else {
            cmdctrl.getlistener().onGetKsn("显示失敗");
            logger.error("显示失敗");

        }
    }

    /**
     * 校验MAC
     */
    public void checkMAC() {
        logger.debug("CheckMAC()");
        CommandReturn cmdret = new CommandReturn();
        byte[] data = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x6C, 0x6B, (byte) 0x90, 0x5D, 0x30, 0x30, 0x30, 0x30};
        cmdret = cmdctrl.Get_CheckMAC(0, 1, random, data);
        if ((cmdret != null) && (cmdret.Return_Result == 0x00)) {

            cmdctrl.getlistener().onGetKsn("MAC校驗成功");
            logger.error("cmdtest MAC校驗成功");
            // return Util.BcdToString(cmdret.Return_PSAMNo);

        } else {
            cmdctrl.getlistener().onGetKsn("MAC校驗失敗");
            // return Util.BcdToString(cmdret.Return_PSAMNo);
            logger.error("cmdtest MAC校驗失敗");

        }
    }

    /**
     * 获取MAC值 随机数为3位
     *
     * @return
     */
    public void getMAC() {
        logger.debug("getMAC()");
        CommandReturn cmdret = new CommandReturn();
        byte[] data = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14,
                (byte) 0x15, (byte) 0xab, (byte) 0xcd, (byte) 0x11};
        cmdret = cmdctrl.Get_MAC(0, 1, random, data);
        if ((cmdret != null) && (cmdret.Return_PSAMMAC != null)) {
            logger.error("cmdtest getksn="
                    + Util.BcdToString(cmdret.Return_PSAMMAC));
            cmdctrl.getlistener().onGetKsn(
                    "MAC:" + Util.BcdToString(cmdret.Return_PSAMMAC));
        } else {
            logger.error("cmdtest getksn=null");
            cmdctrl.getlistener().onGetKsn("MAC请求失败");
        }
    }

    public boolean get_Pin() {
        CommandReturn cmdret = new CommandReturn();
        // cmdret=cmdctrl.Get_EncTrack(0, 2,random , 50);
        // cmdret = cmdctrl.Get_ExtConOperator(1, random, cash, null, 50);//(0,
        // random, cash , null, 50);
        cmdret = cmdctrl.Get_PIN(0, 1, cash, random, null, 30);// (1, 1, 1, 1,
        // (byte)0x1c,
        // random, cash,
        // null, 30);
        if ((cmdret != null) && (cmdret.Return_Result == 0)) {
            StringBuffer buf = new StringBuffer();
            if (cmdret.Return_PSAMMAC != null)
                buf.append("PSMMAC:"
                        + Util.BinToHex(cmdret.Return_PSAMMAC, 0,
                        cmdret.Return_PSAMMAC.length) + "\n");
            if (cmdret.Return_PSAMNo != null)
                buf.append("PSAMNo:"
                        + Util.BinToHex(cmdret.Return_PSAMNo, 0,
                        cmdret.Return_PSAMNo.length) + "\n");
            if (cmdret.Return_PSAMPIN != null)
                buf.append("PSAMPIN:"
                        + Util.BinToHex(cmdret.Return_PSAMPIN, 0,
                        cmdret.Return_PSAMPIN.length) + "\n");
            if (cmdret.Return_PSAMTrack != null)
                buf.append("PSAMTrack:"
                        + Util.BinToHex(cmdret.Return_PSAMTrack, 0,
                        cmdret.Return_PSAMTrack.length) + "\n");
            if (cmdret.Return_TerSerialNo != null)
                buf.append("TerSerialNo:"
                        + Util.BinToHex(cmdret.Return_TerSerialNo, 0,
                        cmdret.Return_TerSerialNo.length) + "\n");
            cmdctrl.getlistener().onGetKsn(buf.toString());
            logger.error(buf.toString());
            return true;
        } else
            return false;
    }

    /**
     * 获取ksn或者psam号
     */
    public CommandReturn getKSN() {
//		logger.debug("getKSN()");
//		CommandReturn cmdret= new CommandReturn();
//
//		cmdret=cmdctrl.Get_PsamNo();
//
//		if((cmdret!=null)&&(cmdret.Return_PSAMNo!=null)){
//
//			cmdctrl.getlistener().onGetKsn("PSAM:"+Util.BcdToString(cmdret.Return_PSAMNo));
//			logger.error("cmdtest getksn="+Util.BcdToString(cmdret.Return_PSAMNo));
//			//return Util.BcdToString(cmdret.Return_PSAMNo);
//			return cmdret;
//		}
//		else
//		{
//			//cmdctrl.getlistener().onGetKsn(null);
//			//return Util.BcdToString(cmdret.Return_PSAMNo);
//			logger.error("cmdtest getksn=null");
//			return null;
//		}

        logger.debug("getKSN()");
        CommandReturn cmdret = new CommandReturn();

        cmdret = cmdctrl.Get_PsamNo();

        if ((cmdret != null) && (cmdret.Return_PSAMNo != null)) {

            cmdctrl.getlistener().onGetKsn(
                    "PSAM:" + Util.BcdToString(cmdret.Return_PSAMNo));
            logger.error("cmdtest getksn="
                    + Util.BcdToString(cmdret.Return_PSAMNo));
            return cmdret;

        } else if ((cmdret != null) && (cmdret.Return_Result == 1)) {
            cmdctrl.getlistener().onGetKsn("命令处理超时");
            return null;
        } else if (cmdret != null) {
            cmdctrl.getlistener()
                    .onGetKsn("命令处理错误,返回状态" + cmdret.Return_Result);
            return null;
        } else {
            cmdctrl.getlistener().onGetKsn("命令处理超时");
            logger.error("cmdtest getksn=null");
            return cmdret;


        }


    }

}
