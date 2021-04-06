package com.fy.engineserver.smith;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 * @version 创建时间：Mar 27, 2013 6:43:06 PM
 * 
 */
public class MoneyRelationShip4Client {
		
	private int id;
	
	/**
	 * 顶层的工作室成员，有可能是最终的买金者
	 */
	private List<MoneyMaker4Client> topMakers = new ArrayList<MoneyMaker4Client>();
	
	/**
	 * 是否被封禁
	 */
	private boolean forbid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<MoneyMaker4Client> getTopMakers() {
		return topMakers;
	}

	public void setTopMakers(List<MoneyMaker4Client> topMakers) {
		this.topMakers = topMakers;
	}

	public boolean isForbid() {
		return forbid;
	}

	public void setForbid(boolean forbid) {
		this.forbid = forbid;
	}
	
	
}
