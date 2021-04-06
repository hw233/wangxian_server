package com.sqage.stat.commonstat.entity;

import java.util.Date;

public class DaoJu {

	private Long id;
	private Date createDate;
	private String userName;
	private String fenQu;
	private String gameLevel;
	private String daoJuName;//道具名称
	private String huoBiType;//货币类型
	private Long danJia;     //道具单价
	private Long daoJuNum;   //道具数量
	private String getType;  //获取类型（如： 购买，使用，系统发送，交换）
	private String position; //获取道具的地方 （如，商店购买，页面购买并使用）
	
	private String daoJuColor="";//道具颜色
	private String daoJuLevel="";//道具级别
	private String bindType="";// 道具绑定类型
	
	private String jixing;
	
	private Long vip=0L;        //vip
	private String guojia="";  //国家
	
	@Override
	public String toString() {
		return "DaoJu [bindType:" + bindType + "] [createDate:" + createDate + "] [danJia:" + danJia + "] [daoJuColor:" + daoJuColor
				+ "] [daoJuLevel:" + daoJuLevel + "] [daoJuName:" + daoJuName + "] [daoJuNum:" + daoJuNum + "] [fenQu:" + fenQu + "] [gameLevel:"
				+ gameLevel + "] [getType:" + getType + "] [guojia:" + guojia + "] [huoBiType:" + huoBiType + "] [id:" + id + "] [jixing:" + jixing
				+ "] [position:" + position + "] [userName:" + userName + "] [vip:" + vip + "]";
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFenQu() {
		return fenQu;
	}
	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}
	public String getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(String gameLevel) {
		this.gameLevel = gameLevel;
	}
	public String getDaoJuName() {
		return daoJuName;
	}
	public void setDaoJuName(String daoJuName) {
		this.daoJuName = daoJuName;
	}
	public String getHuoBiType() {
		return huoBiType;
	}
	public void setHuoBiType(String huoBiType) {
		this.huoBiType = huoBiType;
	}
	public Long getDanJia() {
		return danJia;
	}
	public void setDanJia(Long danJia) {
		this.danJia = danJia;
	}
	public Long getDaoJuNum() {
		return daoJuNum;
	}
	public void setDaoJuNum(Long daoJuNum) {
		this.daoJuNum = daoJuNum;
	}
	public String getGetType() {
		return getType;
	}
	public void setGetType(String getType) {
		this.getType = getType;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	public Long getVip() {
		return vip;
	}
	public void setVip(Long vip) {
		this.vip = vip;
	}
	public String getGuojia() {
		return guojia;
	}
	public void setGuojia(String guojia) {
		this.guojia = guojia;
	}
	
}
