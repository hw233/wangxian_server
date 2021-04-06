package com.fy.engineserver.activity.TransitRobbery.model;

import java.util.Map;

public class RayRobberyDamage {
	/** 对应当前天劫level */
	private int id;
	/** key为第几道天雷  value为需要劈几次和总伤害 */
	private Map<Integer, Integer[]> rayDamage;
	
	private int defaultDamage;
	
	private int maxDamage;
	
	public String toString(){
		return "id=" + id + "  defaultdamage=" + defaultDamage + "  rayDamage[1]= " + rayDamage.get(1)[0] + "," +  rayDamage.get(1)[1]
				+ "  rayDamage[2]= " + rayDamage.get(2)[0] + "," +  rayDamage.get(2)[1]
						+ "  rayDamage[3]= " + rayDamage.get(3)[0] + "," +  rayDamage.get(3)[1]
								+ "  rayDamage[4]= " + rayDamage.get(4)[0] + "," +  rayDamage.get(4)[1];
	}
	
	/**
	 * 根据第N道雷得到劈的次数、总伤害和间隔时间---总伤害为百分比(最后一道的间隔时间没用，只是为了方便格式)
	 * @return
	 */
	public Integer[] getDamageByTimes(int key){
		Integer[] result = new Integer[3];
		result = rayDamage.get(key);
		return result;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<Integer, Integer[]> getRayDamage() {
		return rayDamage;
	}
	public void setRayDamage(Map<Integer, Integer[]> rayDamage) {
		this.rayDamage = rayDamage;
	}

	public int getDefaultDamage() {
		return defaultDamage;
	}

	public void setDefaultDamage(int defaultDamage) {
		this.defaultDamage = defaultDamage;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}
	
	
}
