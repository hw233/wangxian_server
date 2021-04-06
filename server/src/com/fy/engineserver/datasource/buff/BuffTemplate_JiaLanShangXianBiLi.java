package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加蓝上限
 * 
 * 
 *
 */
public class BuffTemplate_JiaLanShangXianBiLi extends BuffTemplate{

	/**
	 * 蓝上限
	 */
	protected int[] totalMPC;

	public int[] getTotalMPC() {
		return totalMPC;
	}

	public void setTotalMPC(int[] totalMPC) {
		this.totalMPC = totalMPC;
	}

	public BuffTemplate_JiaLanShangXianBiLi(){
		setName(Translate.text_3235);
		setDescription(Translate.text_3235);
		totalMPC = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaLanShangXianBiLi buff = new Buff_JiaLanShangXianBiLi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(totalMPC != null && totalMPC.length > level){
			if(totalMPC[level] > 0)
				buff.setDescription(Translate.text_3236+totalMPC[level]+"%");
			else if(totalMPC[level] < 0)
				buff.setDescription(Translate.text_3237+(-totalMPC[level])+"%");
		}else{
			buff.setDescription(Translate.text_3238);
		}
		return buff;
	}

}
