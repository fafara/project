package com.livedetect.base;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

/**
 * Created by Administrator on 2017/4/12.
 */

public class CIdentifyRequest extends CbaseRequest {

    private String image_data;

    public String getImage_data() {
        return image_data;
    }

    public void setImage_data(String image_data) {
        this.image_data = image_data;
    }
}
