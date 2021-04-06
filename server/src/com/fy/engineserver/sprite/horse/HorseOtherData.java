package com.fy.engineserver.sprite.horse;

import java.util.Arrays;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class HorseOtherData {
	/** 坐骑当前颜色 */
	private int colorType = 0;
	/** 等阶星级（每10星为1阶） */
	private int rankStar = 1;;
	/** 培养的经验值(到达固定经验自动升级rankstar) */
	private long trainExp;
	/** 血脉星级 (血脉升)*/
	private int bloodStar = 0;
	/** 技能列表(可学习的最大技能个数读表，此处只记录已经成功学习的技能) */
	private int[] skillList = new int[0];
	/** 技能等级 */
	private int[] skillLevel = new int[0];
	/**使用坐骑扩阶道具后改变*/
	private int upLevel;
	
	@Override
	public String toString() {
		return "HorseOtherData [colorType=" + colorType + ", rankStar=" + rankStar + ", trainExp=" + trainExp + ", bloodStar=" + bloodStar + ", skillList=" + Arrays.toString(skillList) + ", skillLevel=" + Arrays.toString(skillLevel) + "]";
	}
	
	public int getSkillLevelById(int skillId) {
		if (skillList != null && skillList.length > 0) {
			for (int i=0; i<skillList.length; i++) {
				if (skillList[i] == skillId) {
					return skillLevel[i];
				}
			}
		}
		return 0;
	}

	public int getRankStar() {
		return rankStar;
	}
	
	public void setRankStar(int rankStar) {
		this.rankStar = rankStar;
	}
	public int getBloodStar() {
		return bloodStar;
	}
	public void setBloodStar(int bloodStar) {
		this.bloodStar = bloodStar;
	}
	public int getColorType() {
		return colorType;
	}
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	public long getTrainExp() {
		return trainExp;
	}
	public void setTrainExp(long trainExp) {
		this.trainExp = trainExp;
	}
	public int[] getSkillList() {
		return skillList;
	}
	public void setSkillList(int[] skillList) {
		this.skillList = skillList;
	}
	public int[] getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(int[] skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getUpLevel() {
		return upLevel;
	}

	public void setUpLevel(int upLevel) {
		this.upLevel = upLevel;
	}
	
	
}
