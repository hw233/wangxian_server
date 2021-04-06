package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class YinZiKuCunFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(YinZiKuCunFlow.class);

	  //private Long  id=0L;
	  private String fenQu="";
	  private Long createDate=System.currentTimeMillis();
	  private Long count= 0L;//库存银子数据
	  private String column1="";//0 每天，1 周，2 月
	  private String column2="";//备用项
	  private String column3="";//备用项
	  private String column4="";//备用项
	  private String column5="";//备用项
	  
	  @Override
	public String toString() {
		return "YinZiKuCunFlow [column1:" + column1 + "] [column2:" + column2 + "] [column3:" + column3 + "] [column4:" + column4 + "] [column5:"
				+ column5 + "] [count:" + count + "] [createDate:" + createDate + "] [fenQu:" + fenQu + "]";
	}


	public String getFenQu() {
		return fenQu;
	}

	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
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


	public String getColumn3() {
		return column3;
	}


	public void setColumn3(String column3) {
		this.column3 = column3;
	}


	public String getColumn4() {
		return column4;
	}


	public void setColumn4(String column4) {
		this.column4 = column4;
	}


	public String getColumn5() {
		return column5;
	}


	public void setColumn5(String column5) {
		this.column5 = column5;
	}
	  
}
