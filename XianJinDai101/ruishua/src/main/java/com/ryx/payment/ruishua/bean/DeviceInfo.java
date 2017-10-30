package com.ryx.payment.ruishua.bean;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String devicename; 

	private String deviceid;

	public DeviceInfo(){
		super();
	}
	public DeviceInfo(String devicename, String deviceid){
		this.devicename=devicename;
		this.deviceid=deviceid;
	}
	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	} 

	
	
	
 

	
}
