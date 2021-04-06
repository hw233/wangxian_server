package com.fy.engineserver.sprite.horse2.model;

import java.util.Arrays;

/**
 * 基础配置   存储每天免费次数，每次消耗等
 * @author Administrator
 *
 */
public class HorseBaseModel {
	/** 每天免费阶培养次数 */
	private int freeTimes4Rank;
	/** 阶培养需要道具名 */
	private String needArticle4Rank;
	/** 每天免费刷新技能次数 */
	private int freeTimes4Skill;
	/** 阶培养增加经验值 */
	private int exp4Rank;
	/** 阶培养话费银子数量（单位：两）  免费次数用完后用 --- 对应次数话费银子数量*/
	private int[] cost4Rank;
	/** 阶培养银子消耗次数阶层 */
	private int[] nums4Rank;
	/** 普通刷新技能所需道具名 */
	private String needArticle4NomalSkill;
	/** 高级刷新技能所需道具名 */
	private String needArticle4SpecSkill;
	
	public String getUseArticleName(int type) {
		if (type == 1) {
			return needArticle4NomalSkill;
		} else {
			return needArticle4SpecSkill;
		}
	}
	
	public int getCost4RankByTimes(int times) {
		int tempTimes = 0;
		for(int i=0; i<cost4Rank.length; i++) {
			tempTimes = nums4Rank[i];
			if(times <= tempTimes) {
				return cost4Rank[i];
			}
		}
		return cost4Rank[cost4Rank.length - 1];
	}
	
	@Override
	public String toString() {
		return "HorseBaseModel [freeTimes4Rank=" + freeTimes4Rank + ", needArticle4Rank=" + needArticle4Rank + ", freeTimes4Skill=" + freeTimes4Skill + ", exp4Rank=" + exp4Rank + ", cost4Rank=" + Arrays.toString(cost4Rank) + ", nums4Rank=" + Arrays.toString(nums4Rank) + ", needArticle4NomalSkill=" + needArticle4NomalSkill + ", needArticle4SpecSkill=" + needArticle4SpecSkill + "]";
	}
	public String getNeedArticle4NomalSkill() {
		return needArticle4NomalSkill;
	}
	public void setNeedArticle4NomalSkill(String needArticle4NomalSkill) {
		this.needArticle4NomalSkill = needArticle4NomalSkill;
	}
	public String getNeedArticle4SpecSkill() {
		return needArticle4SpecSkill;
	}
	public void setNeedArticle4SpecSkill(String needArticle4SpecSkill) {
		this.needArticle4SpecSkill = needArticle4SpecSkill;
	}
	public int getFreeTimes4Rank() {
		return freeTimes4Rank;
	}
	public void setFreeTimes4Rank(int freeTimes4Rank) {
		this.freeTimes4Rank = freeTimes4Rank;
	}
	public int getFreeTimes4Skill() {
		return freeTimes4Skill;
	}
	public void setFreeTimes4Skill(int freeTimes4Skill) {
		this.freeTimes4Skill = freeTimes4Skill;
	}
	public int getExp4Rank() {
		return exp4Rank;
	}
	public void setExp4Rank(int exp4Rank) {
		this.exp4Rank = exp4Rank;
	}
	public String getNeedArticle4Rank() {
		return needArticle4Rank;
	}
	public void setNeedArticle4Rank(String needArticle4Rank) {
		this.needArticle4Rank = needArticle4Rank;
	}
	public int[] getCost4Rank() {
		return cost4Rank;
	}
	public void setCost4Rank(int[] cost4Rank) {
		this.cost4Rank = cost4Rank;
	}
	public void setCost4Rank(String cost4Rank) {
		String[] temp = cost4Rank.split(",");
		this.cost4Rank = new int[temp.length];
		for (int i=0; i<this.cost4Rank.length; i++) {
			this.cost4Rank[i] = Integer.parseInt(temp[i]);
		}
		
	}
	public int[] getNums4Rank() {
		return nums4Rank;
	}
	public void setNums4Rank(int[] nums4Rank) {
		this.nums4Rank = nums4Rank;
	}
	public void setNums4Rank(String nums4Rank) {
		String[] temp = nums4Rank.split(",");
		this.nums4Rank = new int[temp.length];
		for (int i=0; i<this.nums4Rank.length; i++) {
			this.nums4Rank[i] = Integer.parseInt(temp[i]);
		}
	}

	
	
}
