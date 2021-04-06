package com.sqage.stat.commonstat.entity;

import java.util.Date;

import org.apache.log4j.Logger;

public class YinZiKuCun {
	public static Logger logger = Logger.getLogger(YinZiKuCun.class);

	  private Long  id=0L;
	  private String fenQu="";
	  private Date createDate=new Date();
	  private Long count= 0L;//库存数据
	  private String column1="";//1：天 2：月 3：周
	  private String column2="";//货币类型       1：银子    2：充值积分
	  private String column3="";//封印级别（在服务器里，玩家能到的的最大级别）
	  private String column4="";//备用扩展项
	  private String column5="";//备用扩展项
	  
	
	@Override
	public String toString() {
		return "YinZiKuCun [column1:" + column1 + "] [column2:" + column2 + "] [column3:" + column3 + "] [column4:" + column4 + "] [column5:"
				+ column5 + "] [count:" + count + "] [createDate:" + createDate + "] [fenQu:" + fenQu + "] [id:" + id + "]";
	}

	public String getFenQu() {
		return fenQu;
	}

	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
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

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	
}
