package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加血上限
 * 
 * 
 *
 */
public class BuffTemplate_XueShangXian extends BuffTemplate{

	/**
	 * 血上限
	 */
	protected int[] totalHP;

	public int[] getTotalHP() {
		return totalHP;
	}

	public void setTotalHP(int[] totalHP) {
		this.totalHP = totalHP;
	}

	public BuffTemplate_XueShangXian(){
		setName(Translate.text_3302);
		setDescription(Translate.text_3379);
		totalHP = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_XueShangXian buff = new Buff_XueShangXian();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(totalHP != null && totalHP.length > level){
			if(totalHP[level] > 0)
				buff.setDescription(Translate.text_3290+totalHP[level]+Translate.text_1469);
			else if(totalHP[level] < 0)
				buff.setDescription(Translate.text_3292+(-totalHP[level])+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3290);
		}
		return buff;
	}

}
