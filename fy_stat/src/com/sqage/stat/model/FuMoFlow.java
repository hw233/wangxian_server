package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class FuMoFlow implements Serializable {
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(FuMoFlow.class);

	private String userName="";
	private Long   createTime=System.currentTimeMillis();; // 时间
	private String type=""; // 开始或者结束类型标示：附魔 1， 附魔消耗完 2
	private String fenQu="";
	private String foMoWuPinName="";
	private String column1=""; // 备用项 1
	private String column2=""; // 备用项2

	@Override
	public String toString() {
		return "FuMoFlow [column1:" + column1 + "] [column2:" + column2 + "] [createTime:" + createTime + "] [fenQu:" + fenQu + "] [foMoWuPinName:"
				+ foMoWuPinName + "] [type:" + type + "] [userName:" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFenQu() {
		return fenQu;
	}


	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}


	public String getFoMoWuPinName() {
		return foMoWuPinName;
	}


	public void setFoMoWuPinName(String foMoWuPinName) {
		this.foMoWuPinName = foMoWuPinName;
	}


	public String getColumn1() {
		return column1;
	}


	public void setColumn1(String column1) {
		this.column1 = column1;
	}


	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}
	
}
