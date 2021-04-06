/**
 * 
 */
package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 
 */
public class ChongZhi {
	
	private Long id;
	private String userName;
	private Date time;
	private String game;
	private String fenQu;
    private String gameLevel;
    private Long money;
    
    private String type;//充值类型
    private String quDaoId;
    private String quDao;
    
    /**
     * 渠道手续费
     */
    private String cost;
    
    /**
	 * 充值卡类型，例如盛大卡，神州行卡，联通卡
	 */
	private String cardType="";
	private String jixing="";
	
	
	private String modelType="";  ///设备型号
	private String vip="";  // vip
	private Date registDate=new Date(0L);;// 用户注册时间
	private String playName="";  //角色名称
	private String colum1="";  ///备用字段
	
	@Override
	public String toString() {
		return "ChongZhi [cardType:" + cardType + "] [colum1:" + colum1 + "] [cost:" + cost + "] [fenQu:" + fenQu + "] [game:" + game
				+ "] [gameLevel:" + gameLevel + "] [id:" + id + "] [jixing:" + jixing + "] [modelType:" + modelType + "] [money:" + money
				+ "] [playName:" + playName + "] [quDao:" + quDao + "] [quDaoId:" + quDaoId + "] [registDate:" + registDate + "] [time:" + time
				+ "] [type:" + type + "] [userName:" + userName + "] [vip:" + vip + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
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
	public Long getMoney() {
		return money;
	}
	public void setMoney(Long money) {
		this.money = money;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQuDaoId() {
		return quDaoId;
	}
	public void setQuDaoId(String quDaoId) {
		this.quDaoId = quDaoId;
	}
	public String getQuDao() {
		return quDao;
	}
	public void setQuDao(String quDao) {
		this.quDao = quDao;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public String getVip() {
		return vip;
	}
	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getPlayName() {
		return playName;
	}
	public void setPlayName(String playName) {
		this.playName = playName;
	}
	public String getColum1() {
		return colum1;
	}
	public void setColum1(String colum1) {
		this.colum1 = colum1;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
    
}
