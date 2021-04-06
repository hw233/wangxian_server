package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class LibaoFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(LibaoFlow.class);

	  private String fenQu="";
	  private long createDate=System.currentTimeMillis();
	  private String daoJuName="";
	  private int count=0;//个数数
	  private long danjia=0l;
	  private int type;  // 1：  达到条件，2：领取

	  private String column1="";//备用项
	  private String column2="";//备用项
	  

	@Override
	public String toString() {
		return "LibaoFlow [column1:" + column1 + "] [column2:" + column2 + "] [count:" + count + "] [createDate:" + createDate + "] [danjia:"
				+ danjia + "] [daoJuName:" + daoJuName + "] [fenQu:" + fenQu + "] [type:" + type + "]";
	}


	public String getFenQu() {
		return fenQu;
	}


	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}


	public long getCreateDate() {
		return createDate;
	}


	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}


	public String getDaoJuName() {
		return daoJuName;
	}


	public void setDaoJuName(String daoJuName) {
		this.daoJuName = daoJuName;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public long getDanjia() {
		return danjia;
	}


	public void setDanjia(long danjia) {
		this.danjia = danjia;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
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
