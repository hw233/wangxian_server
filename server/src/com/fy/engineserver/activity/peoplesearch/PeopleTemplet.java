package com.fy.engineserver.activity.peoplesearch;

import java.util.Arrays;

/**
 * 斩妖降魔样板,目标NPC
 * 
 */
public class PeopleTemplet {

	/** 唯一索引 */
	private int id;
	/** 目标 */
	private CountryNpc target;
	/** 对该NPC的描述 第一维是分组,第二维是每个信息 */
	private String[][] des;

	public PeopleTemplet(int id, CountryNpc target, String[][] des) {
		super();
		this.id = id;
		this.target = target;
		this.des = des;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CountryNpc getTarget() {
		return target;
	}

	public void setTarget(CountryNpc target) {
		this.target = target;
	}

	public String[][] getDes() {
		return des;
	}

	public void setDes(String[][] des) {
		this.des = des;
	}

	/**
	 * 得到随机的描述信息
	 * @return
	 */
	public int[] getRandomMessageIndexs() {
		int[] res = new int[PeopleSearchManager.messageNum];
		for (int i = 0; i < res.length; i++) {
			res[i] = PeopleSearchManager.random.nextInt(des[i].length);
		}
		return res;
	}

	@Override
	public String toString() {
		return "PeopleTemplet [id=" + id + ", target=" + target + ", des=" + Arrays.toString(des) + "]";
	}

}