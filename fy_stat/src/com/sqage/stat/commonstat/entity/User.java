/**
 * 
 */
package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 
 *
 */
public class User {
	
	
	private Long id;
	private String name;
	private String quDao;
	private String game;
	private String jiXing;
	private String diDian;
	private String haoMa;
	private String imei;
	private Date registTime;
	private String uuid;
	private String guojia;
	private String playerName;
	private Date creatPlayerTime;
	private String fenQu;
	
	private String sex;
	private String quDaoId;
	
	private String jixing;
	
	@Override
	public String toString() {
		return "User [creatPlayerTime:" + creatPlayerTime + "] [diDian:" + diDian + "] [fenQu:" + fenQu + "] [game:" + game + "] [guojia:" + guojia
				+ "] [haoMa:" + haoMa + "] [id:" + id + "] [imei:" + imei + "] [jiXing:" + jiXing + "] [jixing:" + jixing + "] [name:" + name
				+ "] [playerName:" + playerName + "] [quDao:" + quDao + "] [quDaoId:" + quDaoId + "] [registTime:" + registTime + "] [sex:" + sex
				+ "] [uuid:" + uuid + "]";
	}
	public String getFenQu() {
		return fenQu;
	}
	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}
	public String getGuojia() {
		return guojia;
	}
	public void setGuojia(String guojia) {
		this.guojia = guojia;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuDao() {
		return quDao;
	}
	public void setQuDao(String quDao) {
		this.quDao = quDao;
	}
	
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public String getJiXing() {
		return jiXing;
	}
	public void setJiXing(String jiXing) {
		this.jiXing = jiXing;
	}
	public String getDiDian() {
		return diDian;
	}
	public void setDiDian(String diDian) {
		this.diDian = diDian;
	}
	public String getHaoMa() {
		return haoMa;
	}
	public void setHaoMa(String haoMa) {
		this.haoMa = haoMa;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Date getRegistTime() {
		return registTime;
	}
	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public Date getCreatPlayerTime() {
		return creatPlayerTime;
	}
	public void setCreatPlayerTime(Date creatPlayerTime) {
		this.creatPlayerTime = creatPlayerTime;
	}
	public String getQuDaoId() {
		return quDaoId;
	}
	public void setQuDaoId(String quDaoId) {
		this.quDaoId = quDaoId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

}
