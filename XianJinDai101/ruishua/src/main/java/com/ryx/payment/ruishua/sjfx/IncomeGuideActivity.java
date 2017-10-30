package com.ryx.payment.ruishua.sjfx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.sjfx.adapter.IncomeGuideAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.quickadapter.recyclerView.animation.ScaleInAnimation;
import com.ryx.swiper.utils.MapUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收益指南List列表展示
 */
@EActivity(R.layout.activity_income_guide)
public class IncomeGuideActivity extends BaseActivity {

    @ViewById(R.id.rv_incomeguide)
    public RecyclerView rv_incomeguide;
    private IncomeGuideAdapter incomeGuideAdapter;
    private List<Map> list = new ArrayList<Map>();
    @AfterViews
    public void initViews(){
        setTitleLayout("收益指南", true, false);
        initQtPatParams();
        RecyclerViewHelper.init().setRVGridLayout(this, rv_incomeguide, 1);//1列

        incomeGuideAdapter = new IncomeGuideAdapter(list, this, R.layout.sjfx_adapter_income_guide_item);
        incomeGuideAdapter.openLoadAnimation(new ScaleInAnimation());
        incomeGuideAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                Intent intent=new Intent(IncomeGuideActivity.this,HtmlMessageActivity_.class);
                Bundle bundle = new Bundle();
                bundle.putString("ccurl", MapUtil.getString(list.get(position),"reUrl"));
                    bundle.putString("title",  MapUtil.getString(list.get(position),"explainText"));
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
        rv_incomeguide.setAdapter(incomeGuideAdapter);

        getData();

    }
    private void getData() {
        qtpayApplication.setValue("RequestGuide.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("RequestGuideTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                String data=payResult.getData();
                LogUtil.showLog("payResult.getData=="+data);
                try {
                    list.clear();
                    JSONArray jsonArray=new JSONArray(data);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObj=jsonArray.getJSONObject(i);
                        String imgUrl= JsonUtil.getValueFromJSONObject(jsonObj,"imgUrl");
                        String reUrl= JsonUtil.getValueFromJSONObject(jsonObj,"reUrl");
                        String explainText= JsonUtil.getValueFromJSONObject(jsonObj,"explainText");
                        Map map =  new HashMap<>();
                        map.put("imgUrl",imgUrl);
                        map.put("explainText",explainText);
                        map.put("reUrl",reUrl);
                        list.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.showLog("111111111111111111111111111"+e.getLocalizedMessage());
                }
                LogUtil.showLog("list=="+list);
                incomeGuideAdapter.notifyDataSetChanged();
            }
        });

    }

}
