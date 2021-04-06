package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 闪避
 * 
 * 
 *
 */
public class BuffTemplate_ShanBi extends BuffTemplate{

	/**
	 * 闪避率的百分比
	 */
	protected int[] dodge;

	public int[] getDodge() {
		return dodge;
	}

	public void setDodge(int[] dodge) {
		this.dodge = dodge;
	}

	public BuffTemplate_ShanBi(){
		setName(Translate.text_979);
		setDescription(Translate.text_3340);
		dodge = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ShanBi buff = new Buff_ShanBi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(dodge != null && dodge.length > level){
			if(dodge[level] > 0)
				buff.setDescription(Translate.text_3208+dodge[level]+"%");
			else if(dodge[level] < 0)
				buff.setDescription(Translate.text_3154+(-dodge[level])+"%");
		}else{
			buff.setDescription(Translate.text_3208);
		}
		return buff;
	}

}
