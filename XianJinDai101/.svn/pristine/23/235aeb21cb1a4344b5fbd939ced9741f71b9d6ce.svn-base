package com.ryx.ryxpay.adapter.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryx.ryxpay.R;
import com.ryx.ryxpay.bean.IconBean;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

/**
 * Created by laomao on 16/4/27.
 */
@EViewGroup(R.layout.gridview_main_item)
public class ItemView extends LinearLayout{

    public ItemView(Context context) {
        super(context);
    }
    @ViewById
    ImageView ivIcon;

    @ViewById
    TextView tvIcon;

    @ColorRes
    Integer home_text;

    public void bindView(IconBean.IconMsgBean item)
    {
        int id = getResources().getIdentifier(
                item.getRes(), "drawable",
                getContext().getPackageName());
        ivIcon.setImageResource(id);
        tvIcon.setText(item.getName());
        tvIcon.setTextColor(home_text);
//        TextPaint tp = tvIcon.getPaint();
//        tp.setFakeBoldText(true);
    }

}
