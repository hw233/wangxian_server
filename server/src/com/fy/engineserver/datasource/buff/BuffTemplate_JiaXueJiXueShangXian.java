package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加血上限同时增加相应生命值
 * 
 * 
 *
 */
public class BuffTemplate_JiaXueJiXueShangXian extends BuffTemplate{

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

	public BuffTemplate_JiaXueJiXueShangXian(){
		setName(Translate.text_3288);
		setDescription(Translate.text_3289);
		totalHP = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXueJiXueShangXian buff = new Buff_JiaXueJiXueShangXian();
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
				buff.setDescription(Translate.text_3290+totalHP[level]+Translate.text_3297+totalHP[level]+Translate.text_1469);
			else if(totalHP[level] < 0)
				buff.setDescription(Translate.text_3292+(-totalHP[level])+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3298);
		}
		return buff;
	}

}
