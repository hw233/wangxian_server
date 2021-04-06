package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 抽血
 * 
 * 
 *
 */
public class BuffTemplate_ChouXie extends BuffTemplate{

	/**
	 * 各个级别抽取的百分比
	 */
	protected int hpStealPercent[];
	
	public int[] getHpStealPercent() {
		return hpStealPercent;
	}

	public void setHpStealPercent(int[] hpStealPercent) {
		this.hpStealPercent = hpStealPercent;
	}

	public BuffTemplate_ChouXie(){
		setName(Translate.text_3160);
		setDescription(Translate.text_3161);
		hpStealPercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_CouXie buff = new Buff_CouXie();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
//		buff.setTemplateName(this.getName());
		buff.setTemplateName(Translate.text_3160);
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(hpStealPercent != null && hpStealPercent.length > level){
			buff.setDescription(Translate.text_3162+hpStealPercent[level]+Translate.text_3163);
		}else{
			buff.setDescription(Translate.text_3164);
		}
		return buff;
	}

}
