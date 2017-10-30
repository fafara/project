package com.ryx.payment.ruishua.utils;


import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;

public class AppIconUtil {
    private static int id = R.mipmap.ruishualogo;

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
                id = R.mipmap.ruiyinxinlogo;
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
            } else if ("ruishua".equals(sid)) {
                id = R.mipmap.ruishualogo;
            } else if ("shunshua".equals(sid)) {
                id = R.drawable.shunshua;
            }else if("newrs".equals(sid)){
                id = R.mipmap.newrslogo;
            }else if("ruihebao".equals(sid)){
                id = R.mipmap.ruihebaologo;
            }else{
                id=RyxAppdata.getCurrentBranchMipmapLogoId();
//                if (RyxAppconfig.BRANCH.equals("01")){//瑞刷
//                    id = R.mipmap.ruishualogo;
//                }else if (RyxAppconfig.BRANCH.equals("02")) { //瑞银信
//                    id = R.mipmap.ruiyinxinlogo;
//                }
            }
        } else {
            id=RyxAppdata.getCurrentBranchMipmapLogoId();
//            if (RyxAppconfig.BRANCH.equals("01")){//瑞刷
//                id = R.mipmap.ruishualogo;
//            }else if (RyxAppconfig.BRANCH.equals("02")) { //瑞银信
//                id = R.mipmap.ruiyinxinlogo;
//            }
        }
        return id;
    }
}
