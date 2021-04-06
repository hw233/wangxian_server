package com.fy.engineserver.hunshi;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.util.SimpleKey;

/**
 * 魂石套装
 * 
 * 
 */
public class HunshiSuit {
	@SimpleKey
	private int suitId;
	private String suitName;// 套装名称
	private String suitCNName;// 套装名称
	private int suitColor;
	private String[] hunshi2Names;// 组成套装的套装石
	private String[] hunshi2CNNames;// 组成套装的套装石统计名
	private int[] propId;// 激活的属性数组
	private int[] propValue;// 属性值
	private int skillId;// 技能id

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public String getSuitName() {
		return suitName;
	}

	public void setSuitName(String suitName) {
		this.suitName = suitName;
	}

	public String getSuitCNName() {
		return suitCNName;
	}

	public void setSuitCNName(String suitCNName) {
		this.suitCNName = suitCNName;
	}

	public int getSuitColor() {
		return suitColor;
	}

	public void setSuitColor(int suitColor) {
		this.suitColor = suitColor;
	}

	public String[] getHunshi2Names() {
		return hunshi2Names;
	}

	public void setHunshi2Names(String[] hunshi2Names) {
		this.hunshi2Names = hunshi2Names;
	}

	public String[] getHunshi2CNNames() {
		return hunshi2CNNames;
	}

	public void setHunshi2CNNames(String[] hunshi2cnNames) {
		hunshi2CNNames = hunshi2cnNames;
	}

	public int[] getPropId() {
		return propId;
	}

	public void setPropId(int[] propId) {
		this.propId = propId;
	}

	public int[] getPropValue() {
		return propValue;
	}

	public void setPropValue(int[] propValue) {
		this.propValue = propValue;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getInfoShow() {
		StringBuffer sbf = new StringBuffer();
		for (int j = 0; j < getPropId().length; j++) {
			sbf.append("<f color='0xffffffff' size='28'>").append(HunshiManager.propertyInfo[getPropId()[j]]).append(":+").append(getPropValue()[j]).append("</f>\n");
		}
		SuitSkill ss = HunshiManager.getInstance().suitSkillMap.get(skillId);
		sbf.append("<f color='0xff009cff' size='28'>").append(suitName).append(Translate.套装技能).append("</f>");
		sbf.append("\n<f color='0xff009cff' size='28'>").append(ss.getDes()).append("</f>");

		return sbf.toString();
	}

}
