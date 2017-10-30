package com.ryx.payment.payplug.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by XCC on 2017/1/19.
 */

public class SerializableMap  implements Serializable {
    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
