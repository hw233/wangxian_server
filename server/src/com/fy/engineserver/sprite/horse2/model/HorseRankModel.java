package com.fy.engineserver.sprite.horse2.model;

import java.util.Arrays;

public class HorseRankModel {
	/** 星级（每10星为一阶） */
	private int rankLevel;
	/** 阶数值=坐骑完美规划数值*10%*次数 */
	private double rankDataRate;
	/** 升级星级所需培养经验(不同星级不同) */
	private int needExp;
	/** 免费培养暴击概率 */
	private int[] freeMulProbabbly;
	/** 免费暴击倍率 */
	private int[] freeMul;
	/** 免费直接升星概率 */
	private int free4Next;
	/** 付费培养暴击概率 */
	private int[] costMulProbabbly;
	/** 付费暴击倍率 */
	private int[] costMul;
	/** 付费直接升星概率 */
	private int cost4Next;
	/** 开启技能格数 */
	private int openSkillNum;
	/** 变换后的avatar 对应四个职业*/
	private String[] avatar;
	private String[] monthAvatar;
	/** 飞升后avatar形象  对应四个职业 */
	private String[] newAvatar;
	/** 粒子效果 */
	private String[] partical;
	/** 移动速度 */
	private int speed;
	/** 坐骑名字 */
	private String[] horseName;
	
	
	@Override
	public String toString() {
		return "HorseRankModel [monthAvatar="+Arrays.toString(monthAvatar)+"，rankLevel=" + rankLevel + ", rankDataRate=" + rankDataRate + ", needExp=" + needExp + ", freeMulProbabbly=" + Arrays.toString(freeMulProbabbly) + ", freeMul=" + Arrays.toString(freeMul) + ", free4Next=" + free4Next + ", costMulProbabbly=" + Arrays.toString(costMulProbabbly) + ", costMul=" + Arrays.toString(costMul) + ", cost4Next=" + cost4Next + ", openSkillNum=" + openSkillNum + ", avatar=" + Arrays.toString(avatar) + ", partical=" + Arrays.toString(partical) + ", speed=" + speed + ", horseName=" + Arrays.toString(horseName) + "]";
	}
	public int getRankLevel() {
		return rankLevel;
	}
	public void setRankLevel(int rankLevel) {
		this.rankLevel = rankLevel;
	}
	public int[] getFreeMulProbabbly() {
		return freeMulProbabbly;
	}
	public void setFreeMulProbabbly(int[] freeMulProbabbly) {
		this.freeMulProbabbly = freeMulProbabbly;
	}
	public int[] getCostMulProbabbly() {
		return costMulProbabbly;
	}
	public void setCostMulProbabbly(int[] costMulProbabbly) {
		this.costMulProbabbly = costMulProbabbly;
	}
	public int getOpenSkillNum() {
		return openSkillNum;
	}
	public void setOpenSkillNum(int openSkillNum) {
		this.openSkillNum = openSkillNum;
	}
	public String[] getAvatar() {
		return avatar;
	}
	public void setAvatar(String[] avatar) {
		this.avatar = avatar;
	}
	
	public String[] getMonthAvatar() {
		return monthAvatar;
	}
	public void setMonthAvatar(String[] monthAvatar) {
		this.monthAvatar = monthAvatar;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getNeedExp() {
		return needExp;
	}
	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}
	public int[] getFreeMul() {
		return freeMul;
	}
	public void setFreeMul(int[] freeMul) {
		this.freeMul = freeMul;
	}
	public int[] getCostMul() {
		return costMul;
	}
	public void setCostMul(int[] costMul) {
		this.costMul = costMul;
	}
	public int getCost4Next() {
		return cost4Next;
	}
	public void setCost4Next(int cost4Next) {
		this.cost4Next = cost4Next;
	}
	public String[] getPartical() {
		return partical;
	}
	public void setPartical(String[] partical) {
		this.partical = partical;
	}
	public int getFree4Next() {
		return free4Next;
	}
	public void setFree4Next(int free4Next) {
		this.free4Next = free4Next;
	}
	public double getRankDataRate() {
		return rankDataRate;
	}
	public void setRankDataRate(double rankDataRate) {
		this.rankDataRate = rankDataRate;
	}
	public String[] getHorseName() {
		return horseName;
	}
	public void setHorseName(String[] horseName) {
		this.horseName = horseName;
	}
	public String[] getNewAvatar() {
		return newAvatar;
	}
	public void setNewAvatar(String[] newAvatar) {
		this.newAvatar = newAvatar;
	}
	
	
}
