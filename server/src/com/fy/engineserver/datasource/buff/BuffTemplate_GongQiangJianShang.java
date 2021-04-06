package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 攻击强度和减少伤害百分比转换
 * @author Administrator
 *
 */
public class BuffTemplate_GongQiangJianShang extends BuffTemplate{

	/**
	 * 攻击强度百分比
	 */
	protected int[] gongQiang;

	public int[] getGongQiang() {
		return gongQiang;
	}

	public void setGongQiang(int[] gongQiang) {
		this.gongQiang = gongQiang;
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

	public BuffTemplate_GongQiangJianShang(){
		setName(Translate.text_3184);
		setDescription(Translate.text_3184);
		gongQiang = new int[]{1,3,5,7,9,11,13,15,17,19};
		skillDamageDecreaseRate = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GongQiangJianShang buff = new Buff_GongQiangJianShang();
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
		if(gongQiang != null && gongQiang.length > level){
			if(gongQiang[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3182+gongQiang[level]+"%");
			}else if(gongQiang[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3183+(-gongQiang[level])+"%");
			}
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
