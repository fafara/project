package com.ryx.ryxcredit.widget;

import android.content.Context;

import com.bigkoo.pickerview.OptionsPickerView;
import com.ryx.ryxcredit.beans.PickerBean;

import java.util.ArrayList;

/**
 * Created by laomao on 16/6/27.
 */
public class RyxViewPicker extends OptionsPickerView {
    private int mCount = 1;

    public RyxViewPicker(Context context) {
        super(context);
    }

    public RyxViewPicker setPickTitle(String title) {
        this.setTitle(title);
        return this;
    }

    public RyxViewPicker setSource(Object o) {
        ArrayList<PickerBean> options1Items = new ArrayList<PickerBean>();
        ArrayList<ArrayList<PickerBean>> options2Items = new ArrayList<ArrayList<PickerBean>>();
        ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
        //选项1
        options1Items.add(new PickerBean("0", "0", "广东"));
        options1Items.add(new PickerBean("1", "1", "湖南"));
        options1Items.add(new PickerBean("2", "2", "广西"));

        //选项2
        ArrayList<PickerBean> options2Items_01 = new ArrayList<PickerBean>();
        options2Items_01.add(new PickerBean("0", "0", "广州"));
        options2Items_01.add(new PickerBean("1", "1", "佛山"));
        options2Items_01.add(new PickerBean("2", "2", "东莞"));

        ArrayList<PickerBean> options2Items_02 = new ArrayList<PickerBean>();
        options2Items_02.add(new PickerBean("0", "0", "长沙"));
        options2Items_02.add(new PickerBean("0", "0", "岳阳"));
        ArrayList<PickerBean> options2Items_03 = new ArrayList<PickerBean>();
        options2Items_03.add(new PickerBean("0", "0", "桂林"));
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);

        //选项3
        ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_03 = new ArrayList<ArrayList<String>>();
        ArrayList<String> options3Items_01_01 = new ArrayList<String>();
        options3Items_01_01.add("白云");
        options3Items_01_01.add("天河");
        options3Items_01_01.add("海珠");
        options3Items_01_01.add("越秀");
        options3Items_01.add(options3Items_01_01);
        ArrayList<String> options3Items_01_02 = new ArrayList<String>();
        options3Items_01_02.add("南海");
        options3Items_01_02.add("高明");
        options3Items_01_02.add("顺德");
        options3Items_01_02.add("禅城");
        options3Items_01.add(options3Items_01_02);
        ArrayList<String> options3Items_01_03 = new ArrayList<String>();
        options3Items_01_03.add("其他");
        options3Items_01_03.add("常平");
        options3Items_01_03.add("虎门");
        options3Items_01.add(options3Items_01_03);
        ArrayList<String> options3Items_01_04 = new ArrayList<String>();
        options3Items_01_04.add("其他1");
        options3Items_01_04.add("其他2");
        options3Items_01_04.add("其他3");
        options3Items_01.add(options3Items_01_04);
        ArrayList<String> options3Items_01_05 = new ArrayList<String>();
        options3Items_01_05.add("其他1");
        options3Items_01_05.add("其他2");
        options3Items_01_05.add("其他3");
        options3Items_01.add(options3Items_01_05);

        ArrayList<String> options3Items_02_01 = new ArrayList<String>();
        options3Items_02_01.add("长沙长沙长沙长沙长沙长沙长沙长沙长沙1111111111");
        options3Items_02_01.add("长沙2");
        options3Items_02_01.add("长沙3");
        options3Items_02_01.add("长沙4");
        options3Items_02_01.add("长沙5");
        options3Items_02_01.add("长沙6");
        options3Items_02_01.add("长沙7");
        options3Items_02_01.add("长沙8");
        options3Items_02.add(options3Items_02_01);
        ArrayList<String> options3Items_02_02 = new ArrayList<String>();
        options3Items_02_02.add("岳1");
        options3Items_02_02.add("岳2");
        options3Items_02_02.add("岳3");
        options3Items_02_02.add("岳4");
        options3Items_02_02.add("岳5");
        options3Items_02_02.add("岳6");
        options3Items_02_02.add("岳7");
        options3Items_02_02.add("岳8");
        options3Items_02_02.add("岳9");
        options3Items_02.add(options3Items_02_02);
        ArrayList<String> options3Items_03_01 = new ArrayList<String>();
        options3Items_03_01.add("好山水");
        options3Items_03.add(options3Items_03_01);

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);
        setPicker(options1Items, options2Items, options3Items, true);
        setCyclic(false, false, false);
        setSelectOptions(1, 1, 1);
        return this;
    }

    public RyxViewPicker onSelectListener(OnOptionsSelectListener optionsSelectListener) {
        super.setOnoptionsSelectListener(optionsSelectListener);
        return this;
    }

    public void showView() {
        show();
    }
}
