package com.ryx.payment.ruishua.bean;

/**
 * Created by laomao on 16/6/4.
 */
public class SwiperDeviceInfo {
    private static final long serialVersionUID = 1L;

    private String terminalId;

    private String psamId;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPsamId() {
        return psamId;
    }

    public void setPsamId(String psamId) {
        this.psamId = psamId;
    }
}
