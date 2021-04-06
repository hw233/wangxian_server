package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 抽血
 * 
 * 
 *
 */
public class BuffTemplate_ChouLan extends BuffTemplate{

	/**
	 * 各个级别抽取的百分比
	 */
	protected int mpStealPercent[];
	
	public int[] getMpStealPercent() {
		return mpStealPercent;
	}

	public void setMpStealPercent(int[] mpStealPercent) {
		this.mpStealPercent = mpStealPercent;
	}

	public BuffTemplate_ChouLan(){
		setName(Translate.text_3155);
		setDescription(Translate.text_3156);
		mpStealPercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_CouLan buff = new Buff_CouLan();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(mpStealPercent != null && mpStealPercent.length > level){
			buff.setDescription(Translate.text_3157+mpStealPercent[level]+Translate.text_3158);
		}else{
			buff.setDescription(Translate.text_3159);
		}
		return buff;
	}

}
