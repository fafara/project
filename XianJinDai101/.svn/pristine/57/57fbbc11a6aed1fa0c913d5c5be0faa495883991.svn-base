package com.ryx.payment.ruishua.usercenter;

import android.view.View;
import android.widget.ListView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.adapter.DevsAdapter;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bean.SwiperDeviceInfo;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by laomao on 16/6/4.
 * 我的设备
 */
 @EActivity(R.layout.usercenter_device)
public class DeviceList extends BaseActivity {
    @ViewById
    ListView dev_lisview;
    @ViewById
    AutoLinearLayout lay_top;
    private ArrayList<SwiperDeviceInfo> devsList = new ArrayList<SwiperDeviceInfo>();
    Param qtpayNoticeCode;
    String jsondata = null;
    DevsAdapter devsAdapter;
    @AfterViews
    public void initView() {
        initQtPatParams();
        setTitleLayout("我的设备",true,false);
        devsAdapter=new DevsAdapter(this,devsList);
        dev_lisview.setAdapter(devsAdapter);
        getData();
    }

    /**
     * 请求网络数据
     */
    void getData()
    {
        qtpayApplication = new Param("application",
                "QueryBindTerminalAndCard.Req");

        qtpayAttributeList.add(qtpayApplication);
        httpsPost("QueryBindTerminalAndCard", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                jsondata=payResult.getData();
                devsList=getListData();
                if(devsList.size()>0){
                    lay_top.setVisibility(View.GONE);
                }else{
                    lay_top.setVisibility(View.VISIBLE);
                }
                devsAdapter.devsList=devsList;
                devsAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * 解析获取的json数据，获得list数据
     */
    public ArrayList<SwiperDeviceInfo> getListData() {
        if (jsondata != null) {
            ArrayList<SwiperDeviceInfo> list = null;
            try {
                JSONObject jsonObj = new JSONObject(jsondata);

                // 解析信息
                JSONArray msginfos = jsonObj.getJSONArray("resultBean");
                list = new ArrayList<SwiperDeviceInfo>();
                for (int i = 0; i < msginfos.length(); i++) {
                    SwiperDeviceInfo msginfo = new SwiperDeviceInfo();
                    msginfo.setTerminalId(getValueFromJSONObject(
                            msginfos.getJSONObject(i), "terminalId"));
                    msginfo.setPsamId(getValueFromJSONObject(
                            msginfos.getJSONObject(i), "psamId"));
                    list.add(msginfo);
                }

                return list;

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                jsondata = null; // 数据用完以后，清空掉
            }

            if (list != null && list.size() != 0) {
                return list;
            }
        }
        return null;
    }

    public static String getValueFromJSONObject(JSONObject jsonObject, String key) throws JSONException {
        String result = "";
        if (jsonObject.has(key)) {
            result = jsonObject.getString(key);
        }
        return result;
    }
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayNoticeCode = new Param("noticeCode");
    }
}
