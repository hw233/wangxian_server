package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加蓝上限
 * 
 * 
 *
 */
public class BuffTemplate_LanShangXian extends BuffTemplate{

	/**
	 * 蓝上限
	 */
	protected int[] totalMP;

	public int[] getTotalMP() {
		return totalMP;
	}

	public void setTotalMP(int[] totalMP) {
		this.totalMP = totalMP;
	}

	public BuffTemplate_LanShangXian(){
		setName(Translate.text_3235);
		setDescription(Translate.text_3309);
		totalMP = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_LanShangXian buff = new Buff_LanShangXian();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(totalMP != null && totalMP.length > level){
			if(totalMP[level] > 0)
				buff.setDescription(Translate.text_3236+totalMP[level]+Translate.text_1469);
			else if(totalMP[level] < 0)
				buff.setDescription(Translate.text_3237+(-totalMP[level])+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3236);
		}
		return buff;
	}

}
