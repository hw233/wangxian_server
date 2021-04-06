package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 降低整体输出伤害比例
 * 
 * 
 *
 */
public class BuffTemplate_JiangDiZhengTiShuChuBiLi extends BuffTemplate{

	/**
	 * 各个等级降低整体输出伤害比例，10表示降低10%
	 */
	protected int skillDamageDeIncreaseRate[];

	public int[] getSkillDamageDeIncreaseRate() {
		return skillDamageDeIncreaseRate;
	}

	public void setSkillDamageDeIncreaseRate(int[] skillDamageDeIncreaseRate) {
		this.skillDamageDeIncreaseRate = skillDamageDeIncreaseRate;
	}

	public BuffTemplate_JiangDiZhengTiShuChuBiLi(){
		setName(Translate.text_3240);
		setDescription(Translate.text_3240);
		skillDamageDeIncreaseRate = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiangDiZhengTiShuChuBiLi buff = new Buff_JiangDiZhengTiShuChuBiLi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(skillDamageDeIncreaseRate != null && skillDamageDeIncreaseRate.length > level){
			if(skillDamageDeIncreaseRate[level] > 0)
				buff.setDescription(Translate.text_3241+skillDamageDeIncreaseRate[level]+"%");
			else if(skillDamageDeIncreaseRate[level] < 0)
				buff.setDescription(Translate.text_3242+(-skillDamageDeIncreaseRate[level])+"%");
		}else{
			buff.setDescription(Translate.text_3240);
		}
		return buff;
	}

}
