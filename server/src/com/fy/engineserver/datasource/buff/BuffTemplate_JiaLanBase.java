package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 加蓝
 * 
 * 
 *
 */
public class BuffTemplate_JiaLanBase extends BuffTemplate{

	/**
	 * 每5秒加蓝多少点
	 */
	protected int mpRecoverBase[];

	public int[] getMpRecoverBase() {
		return mpRecoverBase;
	}

	public void setMpRecoverBase(int[] mpRecoverBase) {
		this.mpRecoverBase = mpRecoverBase;
	}

	public BuffTemplate_JiaLanBase(){
		setName(Translate.text_3224);
		setDescription(Translate.text_3228);
		mpRecoverBase = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaLanBase buff = new Buff_JiaLanBase();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(mpRecoverBase != null && mpRecoverBase.length > level){
			buff.setDescription(Translate.text_3229+mpRecoverBase[level]+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3230);
		}
		return buff;
	}

}
