package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 减少力量
 * 
 * @author Administrator
 *
 */
public class BuffTemplate_JianLiLiang extends BuffTemplate {
	int[] strength;

	public BuffTemplate_JianLiLiang(){
		setName(Translate.text_3253);
		setDescription(Translate.text_3254);
		setAdvantageous(true);
		strength = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JianLiLiang buff = new Buff_JianLiLiang();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(strength != null && strength.length > level){
			if(strength[level] > 0)
				buff.setDescription(Translate.text_3253+strength[level]+"%");
			else if(strength[level] < 0)
				buff.setDescription(Translate.text_3255+(-strength[level])+"%");
		}else{
			buff.setDescription(Translate.text_3256);
		}
		return buff;
	}
	
	public int[] getStrength() {
		return strength;
	}

	public void setStrength(int[] strength) {
		this.strength = strength;
	}
}
