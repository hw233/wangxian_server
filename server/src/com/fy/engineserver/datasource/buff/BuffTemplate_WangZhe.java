package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 智力/敏捷/耐力/力量
 * 
 * @author Administrator
 *
 */
public class BuffTemplate_WangZhe extends BuffTemplate {
	int[] strength;
	/**
	 * 敏捷百分比
	 */
	protected int[] dexterity;
	protected int spellpower[];
	int[] constitution;
	
	public int[] getDexterity() {
		return dexterity;
	}

	public void setDexterity(int[] dexterity) {
		this.dexterity = dexterity;
	}

	public int[] getSpellpower() {
		return spellpower;
	}

	public void setSpellpower(int[] spellpower) {
		this.spellpower = spellpower;
	}

	public int[] getConstitution() {
		return constitution;
	}

	public void setConstitution(int[] constitution) {
		this.constitution = constitution;
	}

	public BuffTemplate_WangZhe(){
		setName(Translate.text_3315);
		setDescription(Translate.text_3360);
		setAdvantageous(true);
		strength = new int[]{1,3,5,7,9,11,13,15,17,19};
		dexterity = new int[]{1,3,5,7,9,11,13,15,17,19};
		spellpower = new int[]{1,3,5,7,9,11,13,15,17,19};
		constitution = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	/* (non-Javadoc)
	 * @see com.fy.engineserver.datasource.buff.BuffTemplate#createBuff(int)
	 */
	public Buff createBuff(int level) {
		Buff_WangZhe buff = new Buff_WangZhe();
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
		if(strength != null && strength.length > level){
			if(strength[level] > 0)
				sb.append(Translate.text_3255+strength[level]+"%");
			else if(strength[level] < 0)
				sb.append(Translate.text_3313+(-strength[level])+"%");
		}
		if(constitution != null && constitution.length > level){
			if(constitution[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3273+constitution[level]+"%");
			}else if(constitution[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3271+(-constitution[level])+"%");
			}
		}
		if(spellpower != null && spellpower.length > level){
			if(spellpower[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3279+spellpower[level]+"%");
			}else if(spellpower[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3278+(-spellpower[level])+"%");
			}
		}
		if(dexterity != null && dexterity.length > level && dexterity[level] > 0){
			if(dexterity[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3262+dexterity[level]+"%");
			}else if(dexterity[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3260+(-dexterity[level])+"%");
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}
	
	public int[] getStrength() {
		return strength;
	}

	public void setStrength(int[] strength) {
		this.strength = strength;
	}
}
