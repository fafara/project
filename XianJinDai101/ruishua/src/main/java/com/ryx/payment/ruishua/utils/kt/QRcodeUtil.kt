package com.ryx.payment.ruishua.utils.kt

import org.json.JSONObject
import java.util.*

/**
 * Created by laomao on 17/4/19.
 */
class QRcodeUtil {
    companion object Factory {

        fun filterData(type: String, dataIn: ArrayList<JSONObject>): ArrayList<JSONObject> {
            val dataOut = arrayListOf<JSONObject>()
            dataIn.filter { it.getString("transtype").contains(type) }.forEach {
                dataOut.add(it)
            }
            return dataOut
        }

        fun findData(bindid: String, dataIn: ArrayList<JSONObject>): JSONObject {
            return dataIn.filter { it.getString("bindid").equals(bindid) }.first()
        }
    }

}

