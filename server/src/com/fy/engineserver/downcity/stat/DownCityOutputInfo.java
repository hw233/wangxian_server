package com.fy.engineserver.downcity.stat;

import java.io.Serializable;

/**
 * 副本产出信息
 * 
 *
 */
public class DownCityOutputInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3870641061394958455L;

	/**
	 * 产出时间点
	 */
	protected long outputPoint;
	
	/**
	 * 怪物名
	 */
	protected String monsterName;
	
	/**
	 * 掉落物品名，多个物品逗号分隔
	 */
	protected String propName;

	/**
	 * 金币
	 */
	protected int flopMoney;

	public long getOutputPoint() {
		return outputPoint;
	}

	public void setOutputPoint(long outputPoint) {
		this.outputPoint = outputPoint;
	}

	public String getMonsterName() {
		return monsterName;
	}

	public void setMonsterName(String monsterName) {
		this.monsterName = monsterName;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public int getFlopMoney() {
		return flopMoney;
	}

	public void setFlopMoney(int flopMoney) {
		this.flopMoney = flopMoney;
	}

}
