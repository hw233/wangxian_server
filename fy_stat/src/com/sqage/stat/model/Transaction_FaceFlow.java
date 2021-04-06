package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class Transaction_FaceFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(Transaction_FaceFlow.class);

	private long createDate=System.currentTimeMillis();
	private String fenQu="";
	private String zhenYing="";
	
	private String fuserName="";
	private String fquDao="";
	private String fdaoJu="";
	private String flevel="";
	private String fvip="";
	private long   fmoney=0L;//出货方总充值钱数
	
	private String toLevel="";
	private String toVip="";
	private long   toMoney=0;// 收货方总充值钱数
	private String toUserName="";
	private String toquDao="";
	private String todaoJu="";


	@Override
	public String toString() {
		return "Transaction_FaceFlow [createDate:" + createDate + "] [fMoney:" + fmoney + "] [fdaoJu:" + fdaoJu + "] [fenQu:" + fenQu + "] [flevel:"
				+ flevel + "] [fquDao:" + fquDao + "] [fuserName:" + fuserName + "] [fvip:" + fvip + "] [toLevel:" + toLevel + "] [toMoney:"
				+ toMoney + "] [toUserName:" + toUserName + "] [toVip:" + toVip + "] [todaoJu:" + todaoJu + "] [toquDao:" + toquDao + "] [zhenYing:"
				+ zhenYing + "]";
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public String getFenQu() {
		return fenQu;
	}
	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}
	public String getFuserName() {
		return fuserName;
	}
	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}
	public String getFquDao() {
		return fquDao;
	}
	public void setFquDao(String fquDao) {
		this.fquDao = fquDao;
	}
	public String getFdaoJu() {
		return fdaoJu;
	}
	public void setFdaoJu(String fdaoJu) {
		this.fdaoJu = fdaoJu;
	}
	public String getFlevel() {
		return flevel;
	}
	public void setFlevel(String flevel) {
		this.flevel = flevel;
	}
	public String getFvip() {
		return fvip;
	}
	public void setFvip(String fvip) {
		this.fvip = fvip;
	}
	
	public long getFmoney() {
		return fmoney;
	}
	public void setFmoney(long fmoney) {
		this.fmoney = fmoney;
	}
	public String getToLevel() {
		return toLevel;
	}
	public void setToLevel(String toLevel) {
		this.toLevel = toLevel;
	}
	public String getToVip() {
		return toVip;
	}
	public void setToVip(String toVip) {
		this.toVip = toVip;
	}
	public long getToMoney() {
		return toMoney;
	}
	public void setToMoney(long toMoney) {
		this.toMoney = toMoney;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getToquDao() {
		return toquDao;
	}
	public void setToquDao(String toquDao) {
		this.toquDao = toquDao;
	}
	public String getTodaoJu() {
		return todaoJu;
	}
	public void setTodaoJu(String todaoJu) {
		this.todaoJu = todaoJu;
	}
	public String getZhenYing() {
		return zhenYing;
	}
	public void setZhenYing(String zhenYing) {
		this.zhenYing = zhenYing;
	}
	
	
}
