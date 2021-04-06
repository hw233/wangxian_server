package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 速度和减少伤害百分比
 * 
 *
 */
public class BuffTemplate_SuduJianShang extends BuffTemplate{

	protected int speed[];
	
	public int[] getSpeed() {
		return speed;
	}

	public void setSpeed(int[] speed) {
		this.speed = speed;
	}

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

	public BuffTemplate_SuduJianShang(){
		setName(Translate.text_3356);
		setDescription(Translate.text_3357);
		speed = new int[]{1,3,5,7,9,11,13,15,17,19};
		skillDamageDecreaseRate = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_SuduJianShang buff = new Buff_SuduJianShang();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		StringBuffer sb = new StringBuffer();
		if(speed != null && speed.length > level){
			if(speed[level] > 0)
				sb.append(Translate.text_3210+speed[level]+"%");
			else if(speed[level] < 0)
				sb.append(Translate.text_3211+(-speed[level])+"%");
		}else{
			sb.append(Translate.text_3212);
		}
		if(skillDamageDecreaseRate != null && skillDamageDecreaseRate.length > level){
			if(skillDamageDecreaseRate[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3185+skillDamageDecreaseRate[level]+"%");
			}else if(skillDamageDecreaseRate[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3186+(-skillDamageDecreaseRate[level])+"%");
			}
		}else{
			sb.append(Translate.text_3187);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
