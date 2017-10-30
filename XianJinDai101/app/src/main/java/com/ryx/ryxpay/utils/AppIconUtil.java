package com.ryx.ryxpay.utils;


import com.ryx.ryxpay.R;

public class AppIconUtil {
	private static int id =  R.mipmap.ic_launcher;

	public static int selectIcoid(String sid) {
		int icoid = 0;

		if (null != sid && !"".equals(sid)) {
			if ("yinshuozhifu".equals(sid)) {
				id = R.drawable.yinshuozhifu;
			} else if ("ronghe".equals(sid)) {
				id = R.drawable.ronghe;
			} else if ("huiduo".equals(sid)) {
				id = R.drawable.huiduo;
			} else if ("tianxiahui".equals(sid)) {
				id = R.drawable.tianxiahui;
			} else if ("youzhifu".equals(sid)) {
				id = R.drawable.youzhifu;
			} else if ("qunfeng".equals(sid)) {
				id = R.drawable.qunfeng;
			} else if ("chengpinzhifu".equals(sid)) {
				id = R.drawable.chengpinzhifu;
			} else if ("ruiyinxin".equals(sid)) {
				id = R.drawable.ic_menu_about;
			} else if ("miaoyi".equals(sid)) {
				id = R.drawable.miaoyi;
			} else if ("yinshuokuaifu".equals(sid)) {
				id = R.drawable.yinshuozhifu;
			} else if ("shuakala".equals(sid)) {
				id = R.drawable.shuakala;
			} else if ("d0zhifu".equals(sid)) {
				id = R.drawable.d0zhifu;
			} else if ("shoushuabao".equals(sid)) {
				id = R.drawable.shoushuabao;
			} else if ("shoushoubao".equals(sid)) {
				id = R.drawable.shoushoubao;
			} else if ("qifu".equals(sid)) {
				id = R.drawable.qifu;
			} else if ("xuanhuangzhifu".equals(sid)) {
				id = R.drawable.xuanhuangzhifu;
			} else if ("youbei".equals(sid)) {
				id = R.drawable.youbei;
			} else if ("bianminzhifu".equals(sid)) {
				id = R.drawable.bianminzhifu;
			} else if ("kashuashua".equals(sid)) {
				id = R.drawable.kashuashua;
			} else if ("pinganhui".equals(sid)) {
				id = R.drawable.pinganhui;
			}else if("ruishua".equals(sid)){
				id = R.drawable.ruishua_icon;
			}
			else if("shunshua".equals(sid))
			{
				id=R.drawable.shunshua;
			}
		} else {
			id = R.mipmap.ic_launcher;
			
		}
		return id;
	}
}
