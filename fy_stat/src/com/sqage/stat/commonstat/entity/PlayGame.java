/**
 * 
 */
package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 
 *
 */
public class PlayGame {

	private Long id;
	private Date enterDate;
	private Long userId;
	private String userName;
	private Long enterTimes;
	private Long onLineTime;
	private String game;
	private Long chongZhiShu;
	private Long xiaoFeiShu;
	private String fenQu;
	private Long maxLevel;
	private Long minLevel;
	
	private Long yuanBaoCount;  //剩余元宝数
	private Long youXiBi;       //剩余游戏币
	private String quDao;//渠道
	private String jixing;
	
	private String zhiye;// 职业
	private String column1;//备用扩展项
	private String column2;//备用扩展项
	

	@Override
	public String toString() {
		return "PlayGame [chongZhiShu:" + chongZhiShu + "] [enterDate:" + enterDate + "] [enterTimes:" + enterTimes + "] [fenQu:" + fenQu
				+ "] [game:" + game + "] [id:" + id + "] [jixing:" + jixing + "] [maxLevel:" + maxLevel + "] [minLevel:" + minLevel
				+ "] [onLineTime:" + onLineTime + "] [quDao:" + quDao + "] [userId:" + userId + "] [userName:" + userName + "] [xiaoFeiShu:"
				+ xiaoFeiShu + "] [youXiBi:" + youXiBi + "] [yuanBaoCount:" + yuanBaoCount + "]  [zhiye:" + zhiye + "] [column1:" + column1 + "]  [column2:" + column2 + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getEnterTimes() {
		return enterTimes;
	}
	public void setEnterTimes(Long enterTimes) {
		this.enterTimes = enterTimes;
	}
	public Long getOnLineTime() {
		return onLineTime;
	}
	public void setOnLineTime(Long onLineTime) {
		this.onLineTime = onLineTime;
	}
	
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public Long getChongZhiShu() {
		return chongZhiShu;
	}
	public void setChongZhiShu(Long chongZhiShu) {
		this.chongZhiShu = chongZhiShu;
	}
	public Long getXiaoFeiShu() {
		return xiaoFeiShu;
	}
	public void setXiaoFeiShu(Long xiaoFeiShu) {
		this.xiaoFeiShu = xiaoFeiShu;
	}
	public Long getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(Long maxLevel) {
		this.maxLevel = maxLevel;
	}
	public Long getMinLevel() {
		return minLevel;
	}
	public void setMinLevel(Long minLevel) {
		this.minLevel = minLevel;
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
	public Long getYuanBaoCount() {
		return yuanBaoCount;
	}
	public void setYuanBaoCount(Long yuanBaoCount) {
		this.yuanBaoCount = yuanBaoCount;
	}
	public Long getYouXiBi() {
		return youXiBi;
	}
	public void setYouXiBi(Long youXiBi) {
		this.youXiBi = youXiBi;
	}
	public String getQuDao() {
		return quDao;
	}
	public void setQuDao(String quDao) {
		this.quDao = quDao;
	}
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	public String getZhiye() {
		return zhiye;
	}
	public void setZhiye(String zhiye) {
		this.zhiye = zhiye;
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
