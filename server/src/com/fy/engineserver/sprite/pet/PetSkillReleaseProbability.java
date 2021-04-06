package com.fy.engineserver.sprite.pet;

import com.fy.engineserver.datasource.language.Translate;


public class PetSkillReleaseProbability {

	
	public static final String[] charactors = PetManager.宠物性格;
//	public static final String[] charactors = {Translate.宠物性格忠诚",宠物性格精明 ",宠物性格谨慎",宠物性格狡诈"};
	
	private int skillId;
	
//	忠诚    精明     谨慎    狡诈  (0,1,2,3)
	private int character;
	/**
	 * 匹配性格的释放几率 扩大1000
	 */
	private int match;
	/**
	 * 不匹配性格的释放几率  扩大1000
	 */
	private int noMatch;
	
	public long getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public double getMatch() {
		return match;
	}
	public void setMatch(int match) {
		this.match = match;
	}
	public double getNoMatch() {
		return noMatch;
	}
	public void setNoMatch(int noMatch) {
		this.noMatch = noMatch;
	}
	public int getCharacter() {
		return character;
	}
	public void setCharacter(int character) {
		this.character = character;
	}
	
	public String 得到技能概率描述(){
		String des = Translate.translateString(Translate.技能触发几率, new String[][]{{Translate.STRING_1,charactors[character]},{Translate.COUNT_2,String.format("%.1f", (1f*match/10))+""},{Translate.COUNT_1,String.format("%.1f", (1f*noMatch/10))+""}});
		return des;
	}
	
}
