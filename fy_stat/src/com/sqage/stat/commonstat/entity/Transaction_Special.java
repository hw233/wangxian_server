package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 异常 交易
 * 
 *
 */
public class Transaction_Special {

	private Long id;
	private Date createDate=new Date();
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
	
	private Date fRegisttime=new Date();
	private Date fCreatPlayerTime=new Date();
	private String fLevel="";
	private String fVip="";
	private long fTotalMoney=0L;//出货方总充值钱数
	
	
	private Date toRegisttime=new Date();;
	private Date toCreatPlayerTime=new Date();
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
		return "Transaction_Special [createDate:" + createDate + "] [fCreatPlayerTime:" + fCreatPlayerTime + "] [fLevel:" + fLevel
				+ "] [fRegisttime:" + fRegisttime + "] [fTotalMoney:" + fTotalMoney + "] [fVip:" + fVip + "] [fdaoJuName:" + fdaoJuName + "] [fenQu:"
				+ fenQu + "] [fjiazhi:" + fjiazhi + "] [fplayerName:" + fplayerName + "] [fquDao:" + fquDao + "] [fuserName:" + fuserName
				+ "] [fuuId:" + fuuId + "] [fyinzi:" + fyinzi + "] [id:" + id + "] [toCreatPlayerTime:" + toCreatPlayerTime + "] [toLevel:" + toLevel
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
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

	public Date getfRegisttime() {
		return fRegisttime;
	}

	public void setfRegisttime(Date fRegisttime) {
		this.fRegisttime = fRegisttime;
	}

	public Date getfCreatPlayerTime() {
		return fCreatPlayerTime;
	}

	public void setfCreatPlayerTime(Date fCreatPlayerTime) {
		this.fCreatPlayerTime = fCreatPlayerTime;
	}

	public String getfLevel() {
		return fLevel;
	}

	public void setfLevel(String fLevel) {
		this.fLevel = fLevel;
	}

	public String getfVip() {
		return fVip;
	}

	public void setfVip(String fVip) {
		this.fVip = fVip;
	}

	public long getfTotalMoney() {
		return fTotalMoney;
	}

	public void setfTotalMoney(long fTotalMoney) {
		this.fTotalMoney = fTotalMoney;
	}

	public Date getToRegisttime() {
		return toRegisttime;
	}

	public void setToRegisttime(Date toRegisttime) {
		this.toRegisttime = toRegisttime;
	}

	public Date getToCreatPlayerTime() {
		return toCreatPlayerTime;
	}

	public void setToCreatPlayerTime(Date toCreatPlayerTime) {
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
	
	
	
}
