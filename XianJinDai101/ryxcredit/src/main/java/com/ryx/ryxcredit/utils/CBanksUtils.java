package com.ryx.ryxcredit.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;

/**
 * Created by laomao on 16/9/12.
 */
public class CBanksUtils {
    private static int id;

    /**
     * 联网获取银行图标
     * @param context
     * @param sid
     * @param imageView
     */
    public static void selectIcoidToImgView(Context context, String sid, ImageView imageView){
        if(TextUtils.isEmpty(sid))
            return;
        String imgUrl= RyxcreditConfig.getBankImgUrl().replace("placeholder",sid);
        Glide.with(context)
                .load(imgUrl)
                .error(R.drawable.bank_default)//加载失败默认显示的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存
                .dontAnimate()//无动画
                .into(imageView);
    }

    public static String selectshortname(String sid, String bankname) {

        if (null == sid || "".equals(sid)) {
            return bankname;
        }
        id = CNummberUtil.parseInt(sid,0);
        String shortname = "";

        switch (id) {
            case 102:
                shortname = "工商银行";
                break;
            case 103:
                shortname = "农业银行";
                break;
            case 104:
                shortname = "中国银行";
                break;
            case 105:
                shortname = "建设银行";
                break;
            case 301:
                shortname = "交通银行";
                break;
            case 302:
                shortname = "中信银行";
                break;
            case 303:
                shortname = "光大银行";
                break;
            case 304:
                shortname = "华夏银行";
                break;
            case 305:
                shortname = "民生银行";
                break;
            case 306:
                shortname = "广发银行";
                break;
            case 307:
                shortname = "平安银行";
                break;
            case 308:
                shortname = "招商银行";
                break;
            case 309:
                shortname = "兴业银行";
                break;
            case 310:
                shortname = "上海浦东发展银行";
                break;
            case 313:
                shortname = "城市商业银行";
                break;
            case 322:
                shortname = "农村商业银行";
                break;
            case 402:
                shortname = "信用社";
                break;
            case 403:
                shortname = "中国邮政储蓄银行";
                break;
            case 501:
                shortname = "汇丰银行";
                break;
            case 502:
                shortname = "东亚银行";
                break;
            case 531:
                shortname = "花旗银行";
                break;
            case 671:
                shortname = "渣打银行";
                break;
        }

        return "".equals(shortname) ? bankname : shortname;
    }
}
