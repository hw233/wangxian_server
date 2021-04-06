package com.fy.engineserver.smith;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个打金成员，它具有的特点是：
 * 	自己向上一级打金号汇聚银子，从下一级获取银子
 * 
 * @version 创建时间：Mar 27, 2013 6:32:00 PM
 * 
 */
public class MoneyMaker4Client {
	
	private long id;
	
	/**
	 * 玩家的名字
	 */
	private String name;
	
	/**
	 * 玩家的用户名
	 */
	private String username;
	
	/**
	 * 下线的成员列表
	 */
	private List<MoneyMaker4Client> subMakers = new ArrayList<MoneyMaker4Client>();
	
	/**
	 * 这个人真正的充值额度，用来判定是买金的，还是工作室上线，单位是分
	 */
	private long rmb;
	
	/**
	 * 当前拥有的银两
	 */
	private long currentSilver;
	
	/**
	 * 一共向上汇聚
	 */
	private long totalUp;
	
	/**
	 * IP地址
	 */
	private String ip;
	
	/**
	 * 等级
	 */
	private int level;
	
	/**
	 * 是佛被封禁
	 */
	private boolean forbid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<MoneyMaker4Client> getSubMakers() {
		return subMakers;
	}

	public void setSubMakers(List<MoneyMaker4Client> subMakers) {
		this.subMakers = subMakers;
	}

	public long getRmb() {
		return rmb;
	}

	public void setRmb(long rmb) {
		this.rmb = rmb;
	}

	public long getCurrentSilver() {
		return currentSilver;
	}

	public void setCurrentSilver(long currentSilver) {
		this.currentSilver = currentSilver;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getTotalUp() {
		return totalUp;
	}

	public void setTotalUp(long totalUp) {
		this.totalUp = totalUp;
	}

	public boolean isForbid() {
		return forbid;
	}

	public void setForbid(boolean forbid) {
		this.forbid = forbid;
	}
	
	
}
