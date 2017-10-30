package com.ryx.payment.ruishua.bean;

public class QcoinInfo {
	
	/*
 "gameid": "GAME4980",
 "gamename": "Q币",
 "onlineid": "1281",
 "onlinename": "Q币40元",
 "parvalue": "40.0",
 "saleprice": "38.00"
 */
	private String   gameid;
	private String   gamename; 
	private String   onlineid;
	private String   onlinename;
	private String   parvalue;
	private String   saleprice;
	private String   nettype;
	private String    flowvalue;
	private String   bigDidsName;
    private String tipName;

	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public String getOnlineid() {
		return onlineid;
	}
	public void setOnlineid(String onlineid) {
		this.onlineid = onlineid;
	}
	public String getOnlinename() {
		return onlinename;
	}
	public void setOnlinename(String onlinename) {
		this.onlinename = onlinename;
	}
	public String getParvalue() {
		return parvalue;
	}
	public void setParvalue(String parvalue) {
		this.parvalue = parvalue;
	}
	public String getSaleprice() {
		return saleprice;
	}
	public void setSaleprice(String saleprice) {
		this.saleprice = saleprice;
	}
	public String getNettype() {
		return nettype;
	}
	public void setNettype(String nettype) {
		this.nettype = nettype;
	}
	public String getFlowvalue() {
		return flowvalue;
	}
	public void setFlowvalue(String flowvalue) {
		this.flowvalue = flowvalue;
	}

	public String getBigDidsName() {
		return bigDidsName;
	}

    public void setBigDidsName(String bigDidsName) {
        this.bigDidsName = bigDidsName;
    }

    public String getTipName() {
        return tipName;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }
}
