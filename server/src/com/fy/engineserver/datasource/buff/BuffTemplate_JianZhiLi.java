package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JianZhiLi extends BuffTemplate{
	protected int spellpower[];
	
	public BuffTemplate_JianZhiLi(){
		setName(Translate.text_3276);
		setDescription(Translate.text_3277);
		spellpower = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JianZhiLi buff = new Buff_JianZhiLi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(spellpower != null && spellpower.length > level){
			if(spellpower[level] > 0)
				buff.setDescription(Translate.text_3278+spellpower[level]+"%");
			else if(spellpower[level] < 0)
				buff.setDescription(Translate.text_3279+(-spellpower[level])+"%");
		}else{
			buff.setDescription(Translate.text_3278);
		}
		return buff;
	}

	public int[] getSpellpower() {
		return spellpower;
	}

	public void setSpellpower(int[] spellpower) {
		this.spellpower = spellpower;
	}
}
