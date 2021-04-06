package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class Transaction_SpecialFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(Transaction_SpecialFlow.class);

	private Long id=0L;
	private long createDate=System.currentTimeMillis();
	private String fenQu="";
	private String transactionType="";//交换类型（摆摊，,面对面）
	
	/////////////比正常交易多加的字段 start/////////////
	private String fuuId="";
	private String fuserName="";
	private String fplayerName="";    //出货方角色名
	private String fquDao="";
	private String fdaoJuName="";
	private long fyinzi=0L;
	private long fjiazhi=0L;
	
	
	private long fregisttime=0L;
	private long fcreatPlayerTime=0L;
	private String flevel="";
	private String fvip="";
	private long ftotalMoney=0L;//出货方总充值钱数
	
	private long toRegisttime=0L;
	private long toCreatPlayerTime=0L;
	private String toLevel="";
	private String toVip="";
	private long toTotalMoney=0;// 收货方总充值钱数
	
	
	
	private String touuId="";
	private String toUserName="";
	private String toPlayername="";   //收货方角色名
	private String toquDao="";
	private String todaoJuName="";
	private long toyinzi=0L;
	private long tojiazhi=0L;
	@Override
	public String toString() {
		return "Transaction_SpecialFlow [createDate:" + createDate + "] [fcreatPlayerTime:" + fcreatPlayerTime + "] [fdaoJuName:" + fdaoJuName
				+ "] [fenQu:" + fenQu + "] [fjiazhi:" + fjiazhi + "] [flevel:" + flevel + "] [fplayerName:" + fplayerName + "] [fquDao:" + fquDao
				+ "] [fregisttime:" + fregisttime + "] [ftotalMoney:" + ftotalMoney + "] [fuserName:" + fuserName + "] [fuuId:" + fuuId + "] [fvip:"
				+ fvip + "] [fyinzi:" + fyinzi + "] [id:" + id + "] [toCreatPlayerTime:" + toCreatPlayerTime + "] [toLevel:" + toLevel
				+ "] [toPlayername:" + toPlayername + "] [toRegisttime:" + toRegisttime + "] [toTotalMoney:" + toTotalMoney + "] [toUserName:"
				+ toUserName + "] [toVip:" + toVip + "] [todaoJuName:" + todaoJuName + "] [tojiazhi:" + tojiazhi + "] [toquDao:" + toquDao
				+ "] [touuId:" + touuId + "] [toyinzi:" + toyinzi + "] [transactionType:" + transactionType + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getFuuId() {
		return fuuId;
	}
	public void setFuuId(String fuuId) {
		this.fuuId = fuuId;
	}
	public String getFuserName() {
		return fuserName;
	}
	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}
	public String getFplayerName() {
		return fplayerName;
	}
	public void setFplayerName(String fplayerName) {
		this.fplayerName = fplayerName;
	}
	public String getFquDao() {
		return fquDao;
	}
	public void setFquDao(String fquDao) {
		this.fquDao = fquDao;
	}
	public String getFdaoJuName() {
		return fdaoJuName;
	}
	public void setFdaoJuName(String fdaoJuName) {
		this.fdaoJuName = fdaoJuName;
	}
	public long getFyinzi() {
		return fyinzi;
	}
	public void setFyinzi(long fyinzi) {
		this.fyinzi = fyinzi;
	}
	public long getFjiazhi() {
		return fjiazhi;
	}
	public void setFjiazhi(long fjiazhi) {
		this.fjiazhi = fjiazhi;
	}
	public long getFregisttime() {
		return fregisttime;
	}
	public void setFregisttime(long fregisttime) {
		this.fregisttime = fregisttime;
	}
	public long getFcreatPlayerTime() {
		return fcreatPlayerTime;
	}
	public void setFcreatPlayerTime(long fcreatPlayerTime) {
		this.fcreatPlayerTime = fcreatPlayerTime;
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
	public long getFtotalMoney() {
		return ftotalMoney;
	}
	public void setFtotalMoney(long ftotalMoney) {
		this.ftotalMoney = ftotalMoney;
	}
	public long getToRegisttime() {
		return toRegisttime;
	}
	public void setToRegisttime(long toRegisttime) {
		this.toRegisttime = toRegisttime;
	}
	public long getToCreatPlayerTime() {
		return toCreatPlayerTime;
	}
	public void setToCreatPlayerTime(long toCreatPlayerTime) {
		this.toCreatPlayerTime = toCreatPlayerTime;
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
	public long getToTotalMoney() {
		return toTotalMoney;
	}
	public void setToTotalMoney(long toTotalMoney) {
		this.toTotalMoney = toTotalMoney;
	}
	public String getTouuId() {
		return touuId;
	}
	public void setTouuId(String touuId) {
		this.touuId = touuId;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getToPlayername() {
		return toPlayername;
	}
	public void setToPlayername(String toPlayername) {
		this.toPlayername = toPlayername;
	}
	public String getToquDao() {
		return toquDao;
	}
	public void setToquDao(String toquDao) {
		this.toquDao = toquDao;
	}
	public String getTodaoJuName() {
		return todaoJuName;
	}
	public void setTodaoJuName(String todaoJuName) {
		this.todaoJuName = todaoJuName;
	}
	public long getToyinzi() {
		return toyinzi;
	}
	public void setToyinzi(long toyinzi) {
		this.toyinzi = toyinzi;
	}
	public long getTojiazhi() {
		return tojiazhi;
	}
	public void setTojiazhi(long tojiazhi) {
		this.tojiazhi = tojiazhi;
	}

	
}
