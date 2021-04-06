package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 总体受到伤害减成
 * 
 * 
 *
 */
public class BuffTemplate_JianShaoShouDaoShangHaiBiLi extends BuffTemplate{

	/**
	 * 各个等级增加总体受到伤害减成比例，10表示增加10%
	 */
	protected int skillDamageDecreaseRate[];

	public int[] getSkillDamageDecreaseRate() {
		return skillDamageDecreaseRate;
	}

	public void setSkillDamageDecreaseRate(int[] skillDamageDecreaseRate) {
		this.skillDamageDecreaseRate = skillDamageDecreaseRate;
	}

	public BuffTemplate_JianShaoShouDaoShangHaiBiLi(){
		setName(Translate.text_3266);
		setDescription(Translate.text_3266);
		skillDamageDecreaseRate = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JianShaoShouDaoShangHaiBiLi buff = new Buff_JianShaoShouDaoShangHaiBiLi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(skillDamageDecreaseRate != null && skillDamageDecreaseRate.length > level){
			if(skillDamageDecreaseRate[level] > 0)
				buff.setDescription(Translate.text_3185+skillDamageDecreaseRate[level]+"%");
			else if(skillDamageDecreaseRate[level] < 0)
				buff.setDescription(Translate.text_3186+(-skillDamageDecreaseRate[level])+"%");
		}else{
			buff.setDescription(Translate.text_3187);
		}
		return buff;
	}

}
