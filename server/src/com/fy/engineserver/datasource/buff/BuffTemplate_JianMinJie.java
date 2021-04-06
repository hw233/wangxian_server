package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 降低敏捷
 * 
 * 
 *
 */
public class BuffTemplate_JianMinJie extends BuffTemplate{

	/**
	 * 降低敏捷百分比
	 */
	protected int[] dexterity;

	public int[] getDexterity() {
		return dexterity;
	}

	public void setDexterity(int[] dexterity) {
		this.dexterity = dexterity;
	}

	public BuffTemplate_JianMinJie(){
		setName(Translate.text_3260);
		setDescription(Translate.text_3261);
		dexterity = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JianMingJie buff = new Buff_JianMingJie();
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
				buff.setDescription(Translate.text_3260+dexterity[level]+"%");
			else if(dexterity[level] < 0)
				buff.setDescription(Translate.text_3262+(-dexterity[level])+"%");
		}else{
			buff.setDescription(Translate.text_3260);
		}
		return buff;
	}

}
