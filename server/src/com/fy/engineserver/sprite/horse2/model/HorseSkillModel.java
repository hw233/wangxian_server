package com.fy.engineserver.sprite.horse2.model;

import java.util.Arrays;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.SkillInfoHelper;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;

/**
 * 坐骑技能model
 * @author Administrator
 *
 */
public class HorseSkillModel {
	/** 技能id(所有技能唯一) */
	private int id;
	/** 技能名 */
	private String name;
	/** 技能类型（0初级  1高级） */
	private int skillType;
	/** 可升最大等级 */
	private int maxLevel;
	/** 升级所需物品名 */
	private String[] needArticleName;
	/** 升级所需物品个数 */
	private int[] needArticleNum;
	/** 升级技能成功率 */
	private int[] upGradeRate;
	/** 免费刷新获得概率 (千分比)*/
	private double probabbly4Free;
	/** 普通道具刷新获得概率 */
	private double probabbly4Nomal;
	/** 高级道具刷新获得概率 */
	private double probabbly4Special;
	/** 技能增加属性类型(血、攻击等) */
	private int addAttrType;
	/** 技能增加类型(0为增加具体数值   1为增加千分比) */
	private int addType;
	/** 每个等级对应增加数值 */
	private double[] addNum;
	/** 技能描述 (需要后拼)*/
	private String description;
	/** icon */
	private String[] icon;
	/** 升级技能所需临时物品id */
	private long[] tempArticleIds;
	
	/**
	 * 技能描述---改就改这个方法
	 * @return
	 */
	public String getInfoShow(int skillLevel) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<I iconId='" + this.getIcon()[skillLevel].split(",")[0] + "'></I>");
			if (this.skillType == 0) {
				sb.append("<f color='" + SkillInfoHelper.color_des_green + "'>");
			} else {
				sb.append("<f color='0xff8400'>");
			}
			sb.append(this.getName() + "Lv." + (skillLevel+1));
			sb.append("</f>");
			sb.append("\n\n\n");
			String addedNum = "";
			if (skillLevel < getAddNum().length) {
				if (addType == 0) {
					addedNum = (int)getAddNum()[skillLevel] + "";
				} else {
					int tt = (int) (getAddNum()[skillLevel] / 10L);
					addedNum =  tt + "%";
				}
			}
			String des = String.format(this.description, addedNum);
			sb.append(des);
			int next = skillLevel + 1;
			if (next < this.maxLevel) {
				sb.append("\n<f color='" + SkillInfoHelper.color_red + "'>");
				sb.append(Translate.下一级 );
				sb.append("</f>");
				sb.append("\n");
				String nextNum = "";
				if (addType == 0) {
					nextNum = (int)getAddNum()[skillLevel+1] + "";
				} else {
					int tt = (int) (getAddNum()[skillLevel+1] / 10L);
					nextNum = tt + "%";
				}
				sb.append("<f color='0xA9A9A9'>");
				String des2 = String.format(this.description, nextNum);
				sb.append(des2);
				sb.append("</f>");
			}
			return sb.toString();
		} catch (Exception e) {
			Horse2Manager.logger.error("[新坐骑系统] [获取坐骑技能描述异常] [" + this.getName() + "] [skillLevel:" + skillLevel + "]");
			StringBuffer sb = new StringBuffer();
			sb.append("<f color='" + SkillInfoHelper.color_des_green + "'>");
			sb.append(this.getName() + "Lv." + skillLevel);
			sb.append("</f>");
			sb.append("\n");
			return sb.toString();
		}
	}
	public String getInfoShow() {
		return getInfoShow(0);
	}
	
	public double getProbabblyByType(int type) {
		double result = probabbly4Free;
		if(type == 1) {
			result = probabbly4Nomal;
		} else if (type == 2) {
			result = probabbly4Special;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "HorseSkillModel [id=" + id + ", name=" + name + ", skillType=" + skillType + ", maxLevel=" + maxLevel + ", needArticleName=" + Arrays.toString(needArticleName) + ", needArticleNum=" + Arrays.toString(needArticleNum) + ", upGradeRate=" + Arrays.toString(upGradeRate) + ", probabbly4Free=" + probabbly4Free + ", probabbly4Nomal=" + probabbly4Nomal + ", probabbly4Special=" + probabbly4Special + ", addAttrType=" + addAttrType + ", addType=" + addType + ", addNum=" + Arrays.toString(getAddNum()) + ", description=" + description + ", icon=" + Arrays.toString(icon) + ", tempArticleIds=" + Arrays.toString(tempArticleIds) + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public double getProbabbly4Free() {
		return probabbly4Free;
	}
	public void setProbabbly4Free(double probabbly4Free) {
		this.probabbly4Free = probabbly4Free;
	}
	public double getProbabbly4Nomal() {
		return probabbly4Nomal;
	}
	public void setProbabbly4Nomal(double probabbly4Nomal) {
		this.probabbly4Nomal = probabbly4Nomal;
	}
	public double getProbabbly4Special() {
		return probabbly4Special;
	}
	public void setProbabbly4Special(double probabbly4Special) {
		this.probabbly4Special = probabbly4Special;
	}
	public int getAddAttrType() {
		return addAttrType;
	}
	public void setAddAttrType(int addAttrType) {
		this.addAttrType = addAttrType;
	}
	public int getAddType() {
		return addType;
	}
	public void setAddType(int addType) {
		this.addType = addType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSkillType() {
		return skillType;
	}
	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}
	public int[] getNeedArticleNum() {
		return needArticleNum;
	}
	public void setNeedArticleNum(int[] needArticleNum) {
		this.needArticleNum = needArticleNum;
	}
	public String[] getNeedArticleName() {
		return needArticleName;
	}
	public void setNeedArticleName(String[] needArticleName) {
		this.needArticleName = needArticleName;
	}

	public long[] getTempArticleIds() {
		return tempArticleIds;
	}

	public void setTempArticleIds(long[] tempArticleIds) {
		this.tempArticleIds = tempArticleIds;
	}

	public String[] getIcon() {
		return icon;
	}

	public void setIcon(String[] icon) {
		this.icon = icon;
	}
	public int[] getUpGradeRate() {
		return upGradeRate;
	}
	public void setUpGradeRate(int[] upGradeRate) {
		this.upGradeRate = upGradeRate;
	}
	public double[] getAddNum() {
		return addNum;
	}
	public void setAddNum(double[] addNum) {
		this.addNum = addNum;
	}
	
	
	
}
