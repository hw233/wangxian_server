package com.fy.engineserver.datasource.skill.master;

import com.fy.engineserver.datasource.skill.Skill;

/**
 * 大师技能配置。
 * 
 * create on 2013年9月13日
 */
public class SkEnConf {
	/**
	 * 大师技能里的禁忌技能。
	 */
	public Skill jjSk;
	//职业{技能}
	public static SkEnConf[][] conf;
	public String zhiYe;
	public int baseId;
	public String baseName;
	public String[] desc;
	//
	public String openTip;
	public int[] levelAddPoint;
	public String levelDesc;
	public int[] levelNeedPoint;
	public int[] levelNeedMoney;
	/** 嗜血、港胆、兽印特殊处理，需要带小数点 */
	public double[] specilAddPoint;
}
