package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 敏捷
 * 
 * 
 *
 */
public class BuffTemplate_MinJie extends BuffTemplate{

	/**
	 * 敏捷百分比
	 */
	protected int[] dexterity;

	public int[] getDexterity() {
		return dexterity;
	}

	public void setDexterity(int[] dexterity) {
		this.dexterity = dexterity;
	}

	public BuffTemplate_MinJie(){
		setName(Translate.text_2370);
		setDescription(Translate.text_3328);
		dexterity = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_MingJie buff = new Buff_MingJie();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(dexterity != null && dexterity.length > level){
			if(dexterity[level] > 0)
				buff.setDescription(Translate.text_3262+dexterity[level]+"%");
			else if(dexterity[level] < 0)
				buff.setDescription(Translate.text_3260+(-dexterity[level])+"%");
		}else{
			buff.setDescription(Translate.text_3262);
		}
		return buff;
	}

}
