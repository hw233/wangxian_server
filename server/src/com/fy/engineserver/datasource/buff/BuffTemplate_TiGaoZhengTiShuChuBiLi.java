package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 提高整体输出伤害比例
 * 
 * 
 *
 */
public class BuffTemplate_TiGaoZhengTiShuChuBiLi extends BuffTemplate{

	/**
	 * 各个等级增加整体输出伤害比例，10表示增加10%
	 */
	protected int skillDamageIncreaseRate[];

	public int[] getSkillDamageIncreaseRate() {
		return skillDamageIncreaseRate;
	}

	public void setSkillDamageIncreaseRate(int[] skillDamageIncreaseRate) {
		this.skillDamageIncreaseRate = skillDamageIncreaseRate;
	}

	public BuffTemplate_TiGaoZhengTiShuChuBiLi(){
		setName(Translate.text_3358);
		setDescription(Translate.text_3358);
		skillDamageIncreaseRate = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_TiGaoZhengTiShuChuBiLi buff = new Buff_TiGaoZhengTiShuChuBiLi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(skillDamageIncreaseRate != null && skillDamageIncreaseRate.length > level){
			if(skillDamageIncreaseRate[level] > 0)
				buff.setDescription(Translate.text_3242+skillDamageIncreaseRate[level]+"%");
			else if(skillDamageIncreaseRate[level] < 0)
				buff.setDescription(Translate.text_3241+(-skillDamageIncreaseRate[level])+"%");
		}else{
			buff.setDescription(Translate.text_3358);
		}
		return buff;
	}

}
