package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 抽血
 * 
 * 
 *
 */
public class BuffTemplate_JiaXieBase extends BuffTemplate{

	/**
	 * 每5秒钟恢复血
	 */
	protected int hpRecoverBase[];

	public int[] getHpRecoverBase() {
		return hpRecoverBase;
	}

	public void setHpRecoverBase(int[] hpRecoverBase) {
		this.hpRecoverBase = hpRecoverBase;
	}

	public BuffTemplate_JiaXieBase(){
		setName(Translate.text_3280);
		setDescription(Translate.text_3284);
		hpRecoverBase = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXieBase buff = new Buff_JiaXieBase();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(hpRecoverBase != null && hpRecoverBase.length > level){
			buff.setDescription(Translate.text_3205+hpRecoverBase[level]+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3206);
		}
		return buff;
	}

}
