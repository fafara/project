package com.ryx.payment.ruishua.sjfx

import android.os.Bundle
import com.ryx.payment.ruishua.R
import com.ryx.payment.ruishua.activity.BaseActivity
import com.ryx.payment.ruishua.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_circlefriends.*

/**
 * @author xucc
 * 圈友下三级圈友数+下三级累计收益展示
 */
open class CirclefriendsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circlefriends)
        initData()
    }
    fun initData(){
        var  titleStr=intent.getStringExtra("titleStr")
        setTitleLayout(titleStr,true,false)

       var  oneImgUrl=intent.getStringExtra("oneImgUrl")
       var  onedisName=intent.getStringExtra("onedisName")
       var  oneResult=intent.getStringExtra("oneResult")

        var  twoImgUrl=intent.getStringExtra("twoImgUrl")
        var  twodisName=intent.getStringExtra("twodisName")
        var  twoResult=intent.getStringExtra("twoResult")

        var  threeImgUrl=intent.getStringExtra("threeImgUrl")
        var  threedisName=intent.getStringExtra("threedisName")
        var  threeResult=intent.getStringExtra("threeResult")

        GlideUtils.getInstance().load(this@CirclefriendsActivity,oneImgUrl,circlefriend_imgview1)
        circlefriend_text11.setText(onedisName)
        circlefriend_text12.setText(oneResult)

        GlideUtils.getInstance().load(this@CirclefriendsActivity,twoImgUrl,circlefriend_imgview2)
        circlefriend_text21.setText(twodisName)
        circlefriend_text22.setText(twoResult)

        GlideUtils.getInstance().load(this@CirclefriendsActivity,threeImgUrl,circlefriend_imgview3)
        circlefriend_text31.setText(threedisName)
        circlefriend_text32.setText(threeResult)


    }
}
