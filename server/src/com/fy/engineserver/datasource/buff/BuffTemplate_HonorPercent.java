package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 荣誉百分比
 * 
 * 
 *
 */
public class BuffTemplate_HonorPercent extends BuffTemplate{

	/**
	 * 荣誉百分比
	 */
	protected int honorPercent[];
	
	public int[] getHonorPercent() {
		return honorPercent;
	}

	public void setHonorPercent(int[] honorPercent) {
		this.honorPercent = honorPercent;
	}

	public BuffTemplate_HonorPercent(){
		setName(Translate.text_3199);
		setDescription(Translate.text_3199);
		honorPercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_HonorPercent buff = new Buff_HonorPercent();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(honorPercent != null && honorPercent.length > level){
			if(honorPercent[level] > 0)
				buff.setDescription(Translate.text_3199+honorPercent[level]+"%");
			else if(honorPercent[level] < 0)
				buff.setDescription(Translate.text_3200+(-honorPercent[level])+"%");
		}else{
			buff.setDescription(Translate.text_3199);
		}
		return buff;
	}

}
