package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class TransactionFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(TransactionFlow.class);

	private Long id=0L;
	private Long createDate=System.currentTimeMillis();
	private String fenQu="";
	private String transactionType="";//交换类型（1邮件，2摆摊，3拍卖）
	private String daoJuName="";
	private int danjia;
	private int daojunum;
	private String fuserName="";
	private String fgameLevel="";
	private String toUserName="";
	private String toGameLevel="";
	
	private String daoJuColor="";//道具颜色
	private String daoJuLevel="";//道具级别
	private String bindType="";// 道具绑定类型
	
	private String jixing="";//机型
	
	private String fvip="";
	private String tovip="";
	private String fguoJia="";
	private String toguoJia="";
	
	
	@Override
	public String toString() {
		return "TransactionFlow [bindType:" + bindType + "] [createDate:" + createDate + "] [danjia:" + danjia + "] [daoJuColor:" + daoJuColor
				+ "] [daoJuLevel:" + daoJuLevel + "] [daoJuName:" + daoJuName + "] [daojunum:" + daojunum + "] [fenQu:" + fenQu + "] [fgameLevel:"
				+ fgameLevel + "] [fguoJia:" + fguoJia + "] [fuserName:" + fuserName + "] [fvip:" + fvip + "] [id:" + id + "] [jixing:" + jixing
				+ "] [toGameLevel:" + toGameLevel + "] [toUserName:" + toUserName + "] [toguoJia:" + toguoJia + "] [tovip:" + tovip
				+ "] [transactionType:" + transactionType + "]";
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public String getFenQu() {
		return fenQu;
	}
	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getDaoJuName() {
		return daoJuName;
	}
	public void setDaoJuName(String daoJuName) {
		this.daoJuName = daoJuName;
	}
	public int getDanjia() {
		return danjia;
	}
	public void setDanjia(int danjia) {
		this.danjia = danjia;
	}
	public int getDaojunum() {
		return daojunum;
	}
	public void setDaojunum(int daojunum) {
		this.daojunum = daojunum;
	}
	public String getFuserName() {
		return fuserName;
	}
	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}
	public String getFgameLevel() {
		return fgameLevel;
	}
	public void setFgameLevel(String fgameLevel) {
		this.fgameLevel = fgameLevel;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getToGameLevel() {
		return toGameLevel;
	}
	public void setToGameLevel(String toGameLevel) {
		this.toGameLevel = toGameLevel;
	}
	public String getDaoJuColor() {
		return daoJuColor;
	}
	public void setDaoJuColor(String daoJuColor) {
		this.daoJuColor = daoJuColor;
	}
	public String getDaoJuLevel() {
		return daoJuLevel;
	}
	public void setDaoJuLevel(String daoJuLevel) {
		this.daoJuLevel = daoJuLevel;
	}
	public String getBindType() {
		return bindType;
	}
	public void setBindType(String bindType) {
		this.bindType = bindType;
	}

	public String getFvip() {
		return fvip;
	}

	public void setFvip(String fvip) {
		this.fvip = fvip;
	}

	public String getTovip() {
		return tovip;
	}

	public void setTovip(String tovip) {
		this.tovip = tovip;
	}

	public String getFguoJia() {
		return fguoJia;
	}

	public void setFguoJia(String fguoJia) {
		this.fguoJia = fguoJia;
	}

	public String getToguoJia() {
		return toguoJia;
	}

	public void setToguoJia(String toguoJia) {
		this.toguoJia = toguoJia;
	}
	
}
