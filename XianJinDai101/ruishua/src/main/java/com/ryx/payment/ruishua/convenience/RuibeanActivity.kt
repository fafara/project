package com.ryx.payment.ruishua.convenience

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager

import com.ryx.payment.ruishua.R
import com.ryx.payment.ruishua.RyxAppconfig
import com.ryx.payment.ruishua.activity.BaseActivity
import com.ryx.payment.ruishua.activity.HtmlMsgKotlinMiddleWareAct
import com.ryx.payment.ruishua.adapter.RuiBeanGridAdapter
import com.ryx.payment.ruishua.bean.AdBeanMap
import com.ryx.payment.ruishua.bean.RyxPayResult
import com.ryx.payment.ruishua.net.XmlCallback
import com.ryx.payment.ruishua.utils.*
import com.ryx.payment.ruishua.view.SpaceItemDecoration
import com.ryx.quickadapter.inter.OnListItemClickListener
import kotlinx.android.synthetic.main.activity_ruibean.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

/**
 * 购买瑞豆
 * @author xucc 2017.05.27
 * @describe kotlin写当前页面,freeline暂未支持kotlin需要python freeline.py -f才可看到效果，kotlin暂不支持跳转AA注解Activity.
 */
public class RuibeanActivity : BaseActivity() {
    private var ruiBeanGridAdapter: RuiBeanGridAdapter ?= null
    private var ruiBeanMapList = ArrayList<AdBeanMap>()
    private var buyRuiBean=0x0011

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruibean)
        setTitleLayout("瑞豆",true)
        tilerightImg.setOnClickListener {
            val intent = Intent(this@RuibeanActivity, RuiBeanBuyUseRecordMainActivity::class.java)
            startActivity(intent)
        }
        agreement_layout.setOnClickListener {
            val intent = Intent(this@RuibeanActivity, HtmlMsgKotlinMiddleWareAct::class.java)
            intent.putExtra("title","瑞豆使用说明")
            intent.putExtra("urlkey","RuiDou.info")
            startActivity(intent)
        }
        initQtPatParams()
        initRecyView()
        initData()
    }

    private fun initRecyView() {
        ruiBeanGridAdapter = RuiBeanGridAdapter(ruiBeanMapList,this@RuibeanActivity,R.layout.adapter_ruibean_recyview)
        ruiBeanGridAdapter!!.setOnItemClickListener(OnListItemClickListener { view, position, data ->
            ruiBeanGridAdapter!!.notifyDataSetChanged()
//            LogUtil.showToast(this@RuibeanActivity,"")
            val intent = Intent(this@RuibeanActivity, RuiBeanPayActivity::class.java)
          val id= ruiBeanMapList.get(position).map.get("id");
          val discountamount= ruiBeanMapList.get(position).map.get("discountamount");
          val discountamountval= ruiBeanMapList.get(position).map.get("discountamountval");
          val count= ruiBeanMapList.get(position).map.get("count");
            intent.putExtra("beansType",id)
            intent.putExtra("discountamount",discountamount)
            intent.putExtra("discountamountval",discountamountval)
            intent.putExtra("count",count)
            startActivityForResult(intent,buyRuiBean)
        })
        val layoutManager = GridLayoutManager(this@RuibeanActivity, 3)
        ruibeans_rcv.setLayoutManager(layoutManager)
        ruibeans_rcv.addItemDecoration(SpaceItemDecoration(40))
        ruibeans_rcv.setAdapter(ruiBeanGridAdapter)
    }

    /**
     * 初始化数据
     */
    fun initData(){
        qtpayApplication.value="QueryGoldBeanProduct.Req"
        qtpayAttributeList.add(qtpayApplication)
        httpsPost("queryGoldBeanProductTag", object : XmlCallback() {
            override fun onTradeSuccess(payResult: RyxPayResult) {
                ruiBeanMapList.clear()
        // {"result":[{"id":"1","name":"10000瑞豆","univalence":"1","count":"10000","discount":"99",
        // "amount":"10000","discountamount":"9900","status":"0","createdate":"20170525","createtime":"095600"}],"code":"0000","msg":"交易成功"}
        //  NAME，瑞豆名称，UNIVALENCE单价，COUNT 数量,折扣，AMOUNT原始总金额，DISCOUNTAMOUNT折扣后金额
                try {
                    val  resultData=payResult.data
                    val  jsonobject: JSONObject= JSONObject(resultData)
                    val code=   JsonUtil.getValueFromJSONObject(jsonobject,"code")
                    val resultObj= JsonUtil.getJSONObjectFromJsonObject(jsonobject,"result")
                   val usercount= JsonUtil.getValueFromJSONObject(resultObj,"usercount")
                    ruibean_count_tv.withNumber(CNummberUtil.parseInt(usercount, 0)).start()
                    if("0000".equals(code)){
                        val jsonArray: JSONArray=resultObj.getJSONArray("beanproductdetail")
                        for (i in 0..jsonArray.length()-1){
                            val ruibeanObj = jsonArray.getJSONObject(i)
                            val map:HashMap<String,String> =HashMap<String,String>()
                            map.set("name",JsonUtil.getValueFromJSONObject(ruibeanObj,"name"))
                            map.set("count",JsonUtil.getValueFromJSONObject(ruibeanObj,"count"))
                            val discount= JsonUtil.getValueFromJSONObject(ruibeanObj,"discount");
                            map.set("discount",(Integer.parseInt(discount)/10.00).toString()+"折")
                            val discountamount=  JsonUtil.getValueFromJSONObject(ruibeanObj,"discountamount")
                            map.set("discountamount",RyxMoneyEncoder.EncodeFormat((Integer.parseInt(discountamount)/100.00).toString()))
                            map.set("discountamountval",discountamount)//实际折后金额
                            map.set("id",JsonUtil.getValueFromJSONObject(ruibeanObj,"id"))
                            val ruibeanMap: AdBeanMap =AdBeanMap(map)
                            ruiBeanMapList.add(ruibeanMap)
                        }
                        ruiBeanGridAdapter!!.notifyDataSetChanged()
                    }else{
                        val msg=   JsonUtil.getValueFromJSONObject(jsonobject,"msg")
                        LogUtil.showToast(this@RuibeanActivity,msg)
                    }
                }catch ( e:Exception){
                    LogUtil.showLog("瑞豆数据解析错误:"+e.localizedMessage)
                    LogUtil.showToast(this@RuibeanActivity,"返回的瑞豆信息有误,请稍后再试!")
                }
            }
            override fun onLoginAnomaly() {

            }

            override fun onOtherState() {

            }

            override fun onTradeFailed() {

            }
        })






    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==buyRuiBean){
            ruiBeanGridAdapter!!.setSelectedIndex(-1)
            ruiBeanGridAdapter!!.notifyDataSetChanged()
        }
        if(requestCode==buyRuiBean&&resultCode==RyxAppconfig.CLOSE_ALL){
            initData()
        }

    }


}

